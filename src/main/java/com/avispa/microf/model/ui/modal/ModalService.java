package com.avispa.microf.model.ui.modal;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.content.control.Table;
import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.base.dto.IDto;
import com.avispa.microf.model.base.mapper.IEntityDtoMapper;
import com.avispa.microf.model.ui.modal.context.MicroFContext;
import com.avispa.microf.model.ui.modal.page.ModalPage;
import com.avispa.microf.model.ui.modal.page.ModalPageService;
import com.avispa.microf.model.ui.modal.page.ModalPageType;
import com.avispa.microf.model.ui.propertypage.PropertyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ModalService {
    private final ModalPageService modalPageService;
    private final PropertyPageService propertyPageService;

    /**
     * Returns modal instance. Useful for inserts. Creates new empty instance of DTO object.
     * @param entityClass  class of ECM object
     * @param dtoClass class of DTO object
     * @param modal modal configuration
     * @param <D>
     * @return
     */
    public <T extends EcmObject, D extends IDto> Map<String, Object> constructModal(Class<T> entityClass, Class<D> dtoClass, ModalConfiguration modal) {
        T object = BeanUtils.instantiateClass(entityClass);
        D dto = BeanUtils.instantiateClass(dtoClass);
        return constructModal(object, dto, modal);
    }

    /**
     * Returns modal instance. This version of method works with already existing DTOs. Useful for
     * updates.
     * @param entity ECM object
     * @param contextTypedDto DTO object
     * @param modal modal configuration
     * @param <D>
     * @return
     */
    public <T extends EcmObject, D extends IDto> ModelMap constructModal(T entity, D contextTypedDto, ModalConfiguration modal) {
        ModelMap modelMap = new ModelMap();
        List<ModalPage> modalPages = new ArrayList<>();

        MicroFContext<D> context = new MicroFContext<>();
        context.setTypeName(entity.getClass().getSimpleName());
        context.setObject(contextTypedDto);

        if(modal.isCloneModal()) {
            initCloneModal(modelMap, modalPages, context);
        } else {
            initUpsertModal(entity, modal, modelMap, modalPages, context);
        }

        addNavigationButtons(modalPages);

        context.setPages(modalPages.stream().map(ModalPage::getType).collect(Collectors.toList()));

        modelMap.addAttribute("pages", modalPages);
        modelMap.addAttribute("modal", modal);

        modelMap.addAttribute("context", context);

        return modelMap;
    }

    private <D extends IDto> void initCloneModal(ModelMap modelMap, List<ModalPage> modalPages, MicroFContext<D> context) {
        modalPages.add(modalPageService.createSourceModalPage());
        modalPages.add(modalPageService.createInsertionModalPage());

        modalPageService.createSelectSourcePropertyPage(modelMap, context);
    }

    private <T extends EcmObject, D extends IDto> void initUpsertModal(T entity, ModalConfiguration modal, ModelMap modelMap, List<ModalPage> modalPages, MicroFContext<D> context) {
        if(modal.isUpdateModal()) {
            modalPages.add(modalPageService.createUpdateModalPage());
        } else {
            modalPages.add(modalPageService.createInsertionModalPage());
        }

        modalPageService.createPropertiesPropertyPage(entity, modelMap, context);
    }

    private void addNavigationButtons(List<ModalPage> modalPages) {
        if(modalPages.size() > 1) {
            for (int i = 0; i < modalPages.size(); i++) {
                ModalPage modalPage = modalPages.get(i);
                if(i > 0) {
                    modalPage.addPreviousButton();
                }
                if(i < modalPages.size() - 1) {
                    modalPage.addNextButton();
                }
            }
        }
    }

    public <T extends EcmObject, D extends IDto, C extends MicroFContext<D>> ModelMap loadPage(int pageNumber, C context, BaseService<T, D, ? extends IEntityDtoMapper<T, D>> service) {
        ModelMap modelMap = new ModelMap();
        ModalPageType pageType = context.getPageType(pageNumber);

        switch(pageType) {
            case SELECT_SOURCE:
                modalPageService.createSelectSourcePropertyPage(modelMap, context);
                break;
            case PROPERTIES:
                T entity = service.findById(context.getSourceId());
                D dto = service.getEntityDtoMapper().convertToDto(entity);

                // TODO: missing when loading next page
                //modalPageService.createPropertiesPropertyPage(entity, dto, modelMap, context);
                break;
            default:
                break;
        }

        modelMap.addAttribute("context", context);

        return modelMap;
    }

    public ModelMap getTemplateRow(String tableName, Class<? extends EcmObject> entityClass, Class<? extends IDto> dtoClass) {
        Table table = propertyPageService.getTable(tableName, entityClass, dtoClass);
        ModelMap modelMap = new ModelMap();

        modelMap.addAttribute("control", table);
        modelMap.addAttribute("readonly", false);
        modelMap.addAttribute("context",  BeanUtils.instantiateClass(table.getPropertyType()));

        return modelMap;
    }
}