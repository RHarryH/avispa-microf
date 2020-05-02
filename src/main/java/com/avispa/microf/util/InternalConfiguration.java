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
    private static Version version;

    static {
        try {
            Properties properties = new Properties();
            properties.load(InternalConfiguration.class.getClassLoader().getResourceAsStream("microf.properties"));
            version = new Version(properties.getProperty("microf.version"));
        } catch (IOException e) {
            log.error("Error while loading properties");
        }
    }

    private InternalConfiguration() {

    }

    public static String getReleaseApplicationVersion() {
        return version.getReleaseNumber();
    }
}
