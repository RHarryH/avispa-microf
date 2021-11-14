package com.avispa.microf.model.ui.modal;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Rafał Hiszpański
 */
@Builder
@Getter
class Modal {
    private String id;
    private String title;
    private String action;
    private boolean insert;
}
