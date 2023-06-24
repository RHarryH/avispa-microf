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

package com.avispa.ecm.util.api;

import com.avispa.ecm.util.exception.EcmException;
import com.avispa.ecm.util.api.exception.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException e, HttpServletRequest request) {
        return handleException(e, e.getHttpStatus(), request);
    }

    @ExceptionHandler(EcmException.class)
    public ResponseEntity<Object> handleEcmException(EcmException e, HttpServletRequest request) {
        return handleException(e, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<Object> handleException(Exception e, HttpStatus statusCode, HttpServletRequest request) {
        List<String> stackTrace = new ArrayList<>();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            stackTrace.add(stackTraceElement.toString());
        }

        ApiError error = new ApiError(statusCode, e.getLocalizedMessage(), request.getRequestURI(), stackTrace);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));

        return new ResponseEntity<>(error, headers, statusCode);
    }
}