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

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class ApiError {
    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final List<String> stackTrace;
    private final String path;

    public ApiError(HttpStatus status, String message, String path, String stackTrace) {
        this(status, message, path, List.of(stackTrace));
    }

    public ApiError(HttpStatus status, String message, String path, List<String> stackTrace) {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.stackTrace = stackTrace;
        this.path = path;
    }
}