package com.avispa.ecm.model.base.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
interface IBaseModalableController {
    void add(HttpServletRequest request);
    void update(HttpServletRequest request, UUID id);
}
