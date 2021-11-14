package com.avispa.microf.model.ui;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Rafał Hiszpański
 */
@Builder
@Getter
class ModalDto {
    private String id;
    private String title;
    private String action;
    private boolean insert;
}
