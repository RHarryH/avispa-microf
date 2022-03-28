package com.avispa.microf.model.ui.modal.page;

import lombok.Getter;

/**
 * @author Rafał Hiszpański
 */
@Getter
public enum ModalPageType {
    SELECT_SOURCE("Select source"),
    PROPERTIES("Properties");

    private final String name;

    ModalPageType(String name) {
        this.name = name;
    }
}
