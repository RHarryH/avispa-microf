package com.avispa.microf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Rafał Hiszpański
 */
public final class InternalConfiguration {
    private static final Logger log = LoggerFactory.getLogger(InternalConfiguration.class);
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(InternalConfiguration.class.getClassLoader().getResourceAsStream("microf.properties"));
        } catch (IOException e) {
            log.error("Error while loading properties");
        }
    }

    private InternalConfiguration() {

    }

    public static String getApplicationVersion() {
        return properties.getProperty("microf.version");
    }
}
