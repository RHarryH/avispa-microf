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

package com.avispa.ecm.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * @author Rafał Hiszpański
 */
public class TypeNameUtils {

    private TypeNameUtils() {}

    public static String convertResourceNameToTypeName(String resourceType) {
        return StringUtils.capitalize(resourceType.trim().toLowerCase(Locale.ROOT).replace("-", " "));
    }

    public static String convertTypeNameToResourceName(String typeName) {
        return typeName.trim().toLowerCase(Locale.ROOT).replace(" ", "-");
    }
}
