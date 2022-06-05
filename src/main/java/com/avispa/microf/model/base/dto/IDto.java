package com.avispa.microf.model.base.dto;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public interface IDto {
    String EMPTY_STRING_REGEX = "^$|";

    UUID getId();
    void setId(UUID id);
}
