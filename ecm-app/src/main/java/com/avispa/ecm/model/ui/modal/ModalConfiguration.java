package com.avispa.ecm.model.ui.modal;

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
    private String size;
    private ModalMode mode;

    public boolean isInsertionModal() {
        return this.mode.equals(ModalMode.INSERT);
    }

    public boolean isUpdateModal() {
        return this.mode.equals(ModalMode.UPDATE);
    }

    public boolean isCloneModal() {
        return this.mode.equals(ModalMode.CLONE);
    }

    public static ModalConfigurationBuilder builder(ModalMode mode) {
        return new ModalConfigurationBuilder().mode(mode);
    }
}
