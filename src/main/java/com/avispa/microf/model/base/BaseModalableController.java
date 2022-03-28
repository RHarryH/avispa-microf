package com.avispa.microf.model.base;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.ui.modal.ModalConfiguration;
import com.avispa.microf.model.ui.modal.ModalService;
import com.avispa.microf.model.ui.modal.context.ModalContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public abstract class BaseModalableController<T extends EcmObject, D extends Dto, M extends IEntityDtoMapper<T, D>, S extends BaseService<T, D>, C extends ModalContext<D>> extends BaseController<T, D, M, S> implements IModalableController<D, C> {
    private final ModalService modalService;

    private final Map<String, Class<? extends Dto>> tableFieldsMap;

    protected BaseModalableController(S service,
                                      M entityDtoMapper,
                                      ModalService modalService) {
        super(service, entityDtoMapper);
        this.modalService = modalService;

        this.tableFieldsMap = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        registerTableFields(tableFieldsMap);
    }

    protected ModelAndView getModal(Class<T> objectClass, D dto, ModalConfiguration modal) {
        return new ModelAndView("fragments/modal :: upsertModal",
                modalService.constructModal(objectClass, dto, modal));
    }

    protected ModelAndView getModal(Class<T> objectClass, Class<D> dtoClass, ModalConfiguration modal) {
        return new ModelAndView("fragments/modal :: upsertModal",
                modalService.constructModal(objectClass, dtoClass, modal));
    }

    @Override
    public void addFromModal(C context, BindingResult result) {
        add(context.getObject(), result);
    }

    @Override
    public void updateFromModal(C context, BindingResult result) {
        update(context.getObject(), result);
    }

    @Override
    public ModelAndView loadPage(int pageNumber, C context) {
        return new ModelAndView("fragments/modal :: modalBody",
                modalService.loadPage(pageNumber, context, getService(), getEntityDtoMapper()));
    }

    @Override
    public ModelAndView loadTableTemplate(String tableName) {
        return new ModelAndView("fragments/property-page :: table-template-row",
                modalService.getTemplateRow(tableName, getObjectClass(), getDtoClass(), tableFieldsMap.get(tableName)));
    }

    protected abstract Class<T> getObjectClass();
    protected abstract Class<D> getDtoClass();

    protected void registerTableFields(Map<String, Class<? extends Dto>> tableFieldsMap) {
        // No fields are registered by default
    }
}