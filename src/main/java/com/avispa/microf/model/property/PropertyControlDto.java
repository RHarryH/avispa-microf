package com.avispa.microf.model.property;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class PropertyControlDto extends ControlDto {
    private String name;
    private Object value;
}
