package com.avispa.microf.model.ui.modal;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.control.Control;
import com.avispa.ecm.model.configuration.propertypage.content.control.Table;
import com.avispa.microf.model.base.IBaseService;
import com.avispa.microf.model.base.IEntityDtoMapper;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.base.dto.DtoService;
import com.avispa.microf.model.ui.modal.context.ModalContext;
import com.avispa.microf.model.ui.modal.page.ModalPage;
import com.avispa.microf.model.ui.modal.page.ModalPageService;
import com.avispa.microf.model.ui.modal.page.ModalPageType;
import com.avispa.microf.model.ui.propertypage.PropertyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final DtoService dtoService;
    private final ModalPageService modalPageService;
    private final PropertyPageService propertyPageService;

    /**
     * Returns modal instance. Useful for inserts. Creates new empty instance of DTO object.
     * @param dtoClass class of DTO object
     * @param modal modal configuration
     * @param <D>
     * @return
     */
    public <T extends EcmObject, D extends Dto> Map<String, Object> constructModal(Class<T> objectClass, Class<D> dtoClass, ModalConfiguration modal) {
        D dto = dtoService.createNew(dtoClass);
        return constructModal(objectClass, dto, modal);
    }

    /**
     * Returns modal instance. This version of method works with already existing DTOs. Useful for
     * updates.
     * @param contextTypedDto DTO object
     * @param modal modal configuration
     * @param <D>
     * @return
     */
    public <T extends EcmObject, D extends Dto> ModelMap constructModal(Class<T> objectClass, D contextTypedDto, ModalConfiguration modal) {
        ModelMap modelMap = new ModelMap();
        List<ModalPage> modalPages = new ArrayList<>();

        ModalContext<D> context = new ModalContext<>();
        context.setTypeName(objectClass.getSimpleName());

        if(modal.isCloneModal()) {
            initCloneModal(modelMap, modalPages, context);
        } else {
            initUpsertModal(objectClass, contextTypedDto, modal, modelMap, modalPages, context);
        }

        addNavigationButtons(modalPages);

        context.setPages(modalPages.stream().map(ModalPage::getType).collect(Collectors.toList()));

        modelMap.addAttribute("pages", modalPages);
        modelMap.addAttribute("modal", modal);

        modelMap.addAttribute("context", context);

        return modelMap;
    }

    public <T extends EcmObject, D extends Dto> ModelMap constructModal(T object, D contextTypedDto, ModalConfiguration modal) {
        ModelMap modelMap = new ModelMap();
        List<ModalPage> modalPages = new ArrayList<>();

        ModalContext<D> context = new ModalContext<>();
        context.setTypeName(object.getClass().getSimpleName());

        if(modal.isCloneModal()) {
            initCloneModal(modelMap, modalPages, context);
        } else {
            initUpsertModal(object, contextTypedDto, modal, modelMap, modalPages, context);
        }

        addNavigationButtons(modalPages);

        context.setPages(modalPages.stream().map(ModalPage::getType).collect(Collectors.toList()));

        modelMap.addAttribute("pages", modalPages);
        modelMap.addAttribute("modal", modal);

        modelMap.addAttribute("context", context);

        return modelMap;
    }



    private <D extends Dto> void initCloneModal(ModelMap modelMap, List<ModalPage> modalPages, ModalContext<D> context) {
        modalPages.add(modalPageService.createSourceModalPage());
        modalPages.add(modalPageService.createInsertionModalPage());

        modalPageService.createSelectSourcePropertyPage(modelMap, context);
    }

    private <T extends EcmObject, D extends Dto> void initUpsertModal(Class<T> objectClass, D contextTypedDto, ModalConfiguration modal, ModelMap modelMap, List<ModalPage> modalPages, ModalContext<D> context) {
        if(modal.isUpdateModal()) {
            modalPages.add(modalPageService.createUpdateModalPage());
        } else {
            modalPages.add(modalPageService.createInsertionModalPage());
        }

        modalPageService.createPropertiesPropertyPage(objectClass, contextTypedDto, modelMap, context);
    }

    private <T extends EcmObject, D extends Dto> void initUpsertModal(T object, D contextTypedDto, ModalConfiguration modal, ModelMap modelMap, List<ModalPage> modalPages, ModalContext<D> context) {
        if(modal.isUpdateModal()) {
            modalPages.add(modalPageService.createUpdateModalPage());
        } else {
            modalPages.add(modalPageService.createInsertionModalPage());
        }

        modalPageService.createPropertiesPropertyPage(object, contextTypedDto, modelMap, context);
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

    public <T extends EcmObject, D extends Dto, C extends ModalContext<D>> ModelMap loadPage(int pageNumber, C context, IBaseService<T, D> service, IEntityDtoMapper<T, D> mapper) {
        ModelMap modelMap = new ModelMap();
        ModalPageType pageType = context.getPageType(pageNumber);

        switch(pageType) {
            case SELECT_SOURCE:
                modalPageService.createSelectSourcePropertyPage(modelMap, context);
                break;
            case PROPERTIES:
                T entity = service.findById(context.getSourceId());
                D dto = mapper.convertToDto(entity);

                modalPageService.createPropertiesPropertyPage(entity.getClass(), dto, modelMap, context);
                break;
            default:
                break;
        }

        modelMap.addAttribute("context", context);

        return modelMap;
    }

    public ModelMap getTemplateRow(String tableName, Class<? extends EcmObject> objectClass, Class<? extends Dto> contextDto, Class<? extends Dto> rowDto) {
        ModelMap modelMap = new ModelMap();
        Dto dto = dtoService.createNew(contextDto);

        PropertyPageContent propertyPageContent = propertyPageService.getPropertyPage(objectClass, dto);

        Control table = propertyPageContent.getControls().stream()
                .filter(Table.class::isInstance)
                .map(Table.class::cast)
                .filter(t -> t.getProperty().equals(tableName))
                .findFirst().orElseThrow();

        modelMap.addAttribute("control", table);
        modelMap.addAttribute("readonly", propertyPageContent.isReadonly());
        modelMap.addAttribute("context", dtoService.createNew(rowDto));

        return modelMap;
    }
}