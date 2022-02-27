package com.avispa.microf.model;

import com.avispa.ecm.model.EcmObject;

/**
 * @author Rafał Hiszpański
 */
public interface TypedDto<T extends EcmObject> extends Dto {
    Class<T> getEntityClass();
}
