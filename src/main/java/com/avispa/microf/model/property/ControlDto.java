package com.avispa.microf.model.property;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class ControlDto {
    private String label;

    private String type;
    private String attributes;

    private boolean required;
}
