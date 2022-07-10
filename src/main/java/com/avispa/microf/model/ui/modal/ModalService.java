package com.avispa.microf.model.ui.modal;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.content.control.Table;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.microf.model.base.dto.Dto;
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
    private final TypeService typeService;

    /**
     * Returns modal instance. This version of method works with already existing DTOs. Useful for
     * updates.
     * @param modal modal configuration
     * @param entity ECM object
     * @param contextTypedDto DTO object
     * @return
     */
    public ModelMap constructModal(ModalConfiguration modal, EcmObject entity, Dto contextTypedDto) {
        ModelMap modelMap = new ModelMap();
        List<ModalPage> modalPages = new ArrayList<>();

        MicroFContext<Dto> context = new MicroFContext<>();
        context.setTypeName(typeService.getTypeName(entity.getClass()));
        context.setObject(contextTypedDto);

        if(modal.isCloneModal()) {
            initCloneModal(modelMap, modalPages, context);
        } else {
            initUpsertModal(modal, modelMap, modalPages, entity, context);
        }

        addNavigationButtons(modalPages);

        context.setPages(modalPages.stream().map(ModalPage::getType).collect(Collectors.toList()));

        modelMap.addAttribute("pages", modalPages);
        modelMap.addAttribute("modal", modal);

        modelMap.addAttribute("context", context);

        return modelMap;
    }

    private void initCloneModal(ModelMap modelMap, List<ModalPage> modalPages, MicroFContext<Dto> context) {
        modalPages.add(modalPageService.createSourceModalPage());
        modalPages.add(modalPageService.createInsertionModalPage());

        modalPageService.createSelectSourcePropertyPage(modelMap, context);
    }

    private void initUpsertModal(ModalConfiguration modal, ModelMap modelMap, List<ModalPage> modalPages, EcmObject entity, MicroFContext<Dto> context) {
        if(modal.isUpdateModal()) {
            modalPages.add(modalPageService.createUpdateModalPage());
        } else {
            modalPages.add(modalPageService.createInsertionModalPage());
        }

        modalPageService.createPropertiesPropertyPage(modelMap, entity, context);
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

    public ModelMap loadPage(MicroFContext<Dto> context, EcmObject entity, int pageNumber) {
        ModelMap modelMap = new ModelMap();
        ModalPageType pageType = context.getPageType(pageNumber);

        switch(pageType) {
            case SELECT_SOURCE:
                modalPageService.createSelectSourcePropertyPage(modelMap, context);
                break;
            case PROPERTIES:
                modalPageService.createPropertiesPropertyPage(modelMap, entity, context);
                break;
            default:
                break;
        }

        modelMap.addAttribute("context", context);

        return modelMap;
    }

    public ModelMap getTableTemplateRow(String tableName, Class<? extends EcmObject> entityClass, Class<? extends Dto> dtoClass) {
        Table table = propertyPageService.getTable(tableName, entityClass, dtoClass);
        ModelMap modelMap = new ModelMap();

        modelMap.addAttribute("control", table);
        modelMap.addAttribute("readonly", false);
        modelMap.addAttribute("context",  BeanUtils.instantiateClass(table.getPropertyType()));

        return modelMap;
    }
}