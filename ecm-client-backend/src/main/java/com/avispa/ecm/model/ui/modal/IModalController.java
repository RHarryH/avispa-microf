/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.ecm.model.ui.modal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public interface IModalController {

    @GetMapping("/add/{type}")
    ModelAndView getAddModal(@PathVariable("type") String typeIdentifier);

    @GetMapping("/clone/{type}")
    ModelAndView getCloneModal(@PathVariable("type") String typeIdentifier);

    @GetMapping("/update/{type}/{id}")
    ModelAndView getUpdateModal(@PathVariable("type") String typeIdentifier, @PathVariable("id") UUID id);

    @GetMapping("/page/{pageNumber}")
    ModelAndView loadPage(HttpServletRequest request, @PathVariable("pageNumber") int pageNumber);

    @PostMapping(value = "/row/{type}/{tableName}")
    ModelAndView loadTableTemplate(@PathVariable("type") String typeIdentifier, @PathVariable("tableName") String tableName);
}
