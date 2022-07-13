package com.avispa.microf.model.base.dto;

/**
 * @author Rafał Hiszpański
 */
public interface CommonDto extends Dto {
    String getObjectName();
    void setObjectName(String name);

    default boolean hasPdfRendition() {
        return false;
    }
}
