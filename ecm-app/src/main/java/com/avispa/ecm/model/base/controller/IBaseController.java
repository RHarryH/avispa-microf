package com.avispa.ecm.model.base.controller;

import com.avispa.ecm.model.base.dto.Dto;
import org.springframework.validation.BindingResult;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
interface IBaseController<D extends Dto> {
    void add(D dto, BindingResult result);
    void update(D dto, BindingResult result);
    void delete(UUID id);
}