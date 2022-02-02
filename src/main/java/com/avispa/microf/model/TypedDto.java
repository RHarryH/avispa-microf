package com.avispa.microf.model;

import com.avispa.ecm.model.EcmObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
public interface TypedDto<T extends EcmObject> extends Dto {
    Class<T> getEntityClass();

    default Class<? extends Dto> getListType(String listName) {
        try {
            Field field = this.getClass().getDeclaredField(listName);
            if(field.getType().equals(List.class)) {
                ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                return (Class<? extends Dto>) stringListType.getActualTypeArguments()[0];
            } else {

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }
}
