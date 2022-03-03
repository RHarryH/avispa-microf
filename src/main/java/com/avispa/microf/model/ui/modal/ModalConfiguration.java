package com.avispa.microf.model.ui.modal;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Rafał Hiszpański
 */
@Builder
@Getter
public class ModalConfiguration {
    private String id;
    private String title;
    private String action;
    private ModalMode mode;

    public static ModalConfigurationBuilder builder(ModalMode mode) {
        return new ModalConfigurationBuilder().mode(mode);
    }
}
