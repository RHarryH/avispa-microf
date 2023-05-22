package com.avispa.ecm.model.ui.modal.context;

import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.ui.modal.page.ModalPageType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public final class EcmAppContext<D extends Dto> {
    private List<ModalPageType> pages;

    @Valid private D object;

    private String typeName;
    private UUID sourceId;

    public ModalPageType getPageType(int pageNumber) {
        return pages.get(pageNumber);
    }
}
