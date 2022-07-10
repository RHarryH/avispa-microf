package com.avispa.microf.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * @author Rafał Hiszpański
 */
public class TypeNameUtils {

    private TypeNameUtils() {}

    public static String convertResourceIdToTypeName(String resourceId) {
        return StringUtils.capitalize(resourceId.trim().toLowerCase(Locale.ROOT).replace("-", " "));
    }
}
