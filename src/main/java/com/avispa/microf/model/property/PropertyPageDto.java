package com.avispa.microf.model.property;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class PropertyPageDto {
    private boolean readonly;
    private List<PropertyDto> properties;
}
