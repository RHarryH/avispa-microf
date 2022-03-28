package com.avispa.microf.model.base.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Rafał Hiszpański
 */
@Service
@Slf4j
public class DtoService {
    /**
     * Creates new empty instance of DTO class
     * @param contextClass class of DTO
     * @param <D>
     * @return
     */
    public <D extends Dto> D createNew(Class<D> contextClass) {
        D contextDto = null;
        try {
            contextDto = contextClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            log.error("Cannot instantiate context DTO object", e);
        }
        return contextDto;
    }
}
