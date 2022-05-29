package com.avispa.microf.model.ui.modal.context;

import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.ui.modal.page.ModalPageType;
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
public final class MicroFContext<D extends Dto> {
    private List<ModalPageType> pages;

    @Valid private D object;

    private String typeName;
    private UUID sourceId;

    public ModalPageType getPageType(int pageNumber) {
        return pages.get(pageNumber);
    }
}
