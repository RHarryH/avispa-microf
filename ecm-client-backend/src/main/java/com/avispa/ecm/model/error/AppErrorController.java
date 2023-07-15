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

package com.avispa.ecm.model.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {
    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        ModelAndView errorPage = new ModelAndView("error/error");
        String errorMessage = "";
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 400: {
                    errorMessage = "Http Error Code: 400. Bad Request";
                    break;
                }
                case 401: {
                    errorMessage = "Http Error Code: 401. Unauthorized";
                    break;
                }
                case 404: {
                    errorMessage = "Http Error Code: 404. Resource not found";
                    break;
                }
                case 500: {
                    errorMessage = "Http Error Code: 500. Internal Server Error";
                    break;
                }
                default:
                    errorMessage = "Http Error Code: " + statusCode;
            }
        }

        errorPage.addObject("errorMessage", errorMessage);

        return errorPage;
    }
}