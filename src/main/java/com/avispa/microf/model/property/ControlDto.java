package com.avispa.microf.model.property;

import com.avispa.microf.model.Dto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class ControlDto implements Dto {
    private String label;

    private String type;
    private String attributes;

    private boolean required;
}
