package com.avispa.microf.model.ui.modal;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectService;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.base.dto.DtoObject;
import com.avispa.microf.model.base.dto.DtoService;
import com.avispa.microf.model.ui.modal.context.MicroFContext;
import com.avispa.microf.model.ui.modal.page.ModalPageType;
import com.avispa.microf.util.TypeNameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/modal")
@RequiredArgsConstructor
@Slf4j
public class ModalController implements IModalController {

    private final ModalService modalService;
    private final DtoService dtoService;
    private final EcmObjectService ecmObjectService;

    @Override
    public ModelAndView getAddModal(String typeIdentifier) {
        String typeName = TypeNameUtils.convertURLIdentifierToTypeName(typeIdentifier);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.INSERT)
                .id(typeIdentifier + "-add-modal")
                .title("Add new " + typeName)
                .action(typeIdentifier + "/add")
                .size("large")
                .build();

        return getModal(modal, typeName);
    }

    @Override
    public ModelAndView getCloneModal(String typeIdentifier) {
        String typeName = TypeNameUtils.convertURLIdentifierToTypeName(typeIdentifier);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.CLONE)
                .id(typeIdentifier + "-clone-modal")
                .title("Clone existing " + typeName)
                .action(typeIdentifier + "/add")
                .size("extra-large")
                .build();

        return getModal(modal, typeName);
    }

    protected ModelAndView getModal(ModalConfiguration modal, String typeName) {
        // in these cases we're creating an empty instance of entity and dto so there there is no need
        // to check the discriminator - we want to select common Dto object every time
        DtoObject dtoObject = dtoService.getDtoObjectFromTypeName(typeName);

        EcmObject entity = BeanUtils.instantiateClass(dtoObject.getType().getEntityClass());
        Dto dto = BeanUtils.instantiateClass(dtoObject.getDtoClass());

        return new ModelAndView("fragments/modal :: upsertModal",
                modalService.constructModal(modal, entity, dto));
    }

    @Override
    public ModelAndView getUpdateModal(String typeIdentifier, UUID id) {
        String typeName = TypeNameUtils.convertURLIdentifierToTypeName(typeIdentifier);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.UPDATE)
                .id(typeIdentifier + "-update-modal")
                .title("Update " + typeName)
                .action(typeIdentifier + "/update/" + id)
                .size("large")
                .build();

        return getModal(modal, id, typeName);
    }

    private ModelAndView getModal(ModalConfiguration modal, UUID id, String typeName) {
        EcmObject entity = ecmObjectService.getEcmObjectFrom(id, typeName);
        Dto dto = dtoService.convertEntityToDto(entity);

        return new ModelAndView("fragments/modal :: upsertModal",
                modalService.constructModal(modal, entity, dto));
    }

    @Override
    public ModelAndView loadPage(HttpServletRequest request, int pageNumber) {
        MicroFContext<Dto> context = new MicroFContext<>();

        String sourceId = request.getParameter("sourceId");
        context.setSourceId(sourceId != null ? UUID.fromString(request.getParameter("sourceId")) : null);
        context.setPages(Arrays.stream(request.getParameter("pages").split(","))
                .map(ModalPageType::valueOf)
                .collect(Collectors.toList()));
        context.setTypeName(request.getParameter("typeName"));

        EcmObject entity = null;
        if(null != context.getSourceId()) {
            entity = ecmObjectService.getEcmObjectFrom(context.getSourceId(), context.getTypeName());
            Dto dto = dtoService.convertEntityToDto(entity);
            context.setObject(dto);
        }

        return new ModelAndView("fragments/modal :: modal-body",
                modalService.loadPage(context, entity, pageNumber));
    }

    @Override
    public ModelAndView loadTableTemplate(String typeIdentifier, String tableName) {
        String typeName = TypeNameUtils.convertURLIdentifierToTypeName(typeIdentifier);

        /* TODO: include discriminator? here we should probably operate on discriminators
         * example: we have selected Customer type, let's suppose Retail customer has a table
         * we'd like to load - it would be good to use specific Dto
         * NOTE: common Dto will have this field anyway. The concept of specific Dtos is useful
         * only in context of validation and/or default values (debatable)
         */
        DtoObject dtoObject = dtoService.getDtoObjectFromTypeName(typeName);
        Class<? extends Dto> dtoClass = dtoObject.getDtoClass();

        return new ModelAndView("fragments/property-page :: table-template-row",
                modalService.getTableTemplateRow(tableName, dtoObject.getType().getEntityClass(), dtoClass));
    }
}
