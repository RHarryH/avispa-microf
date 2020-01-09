package com.avispa.microf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Rafał Hiszpański
 */
public final class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);
    private static final String PROPERTIES_FILE = "microf.properties";
    private static final String OFFICE_HOME_PATH = "office.home";
    private static final String RENDITION_OFFICE_ALWAYS = "rendition.office.always";
    private Properties properties = new Properties();

    private static Configuration configuration;

    private Configuration() {
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);
            if(log.isDebugEnabled()) {
                properties.forEach((key, value) -> log.debug("Key : {}, Value : {}", key, value));
            }
        } catch (IOException e) {
            log.info("Properties file not found. Creating new one.");

            setOfficePath("D:\\LibreOffice");
            setRenditionOfficeAlways(true);
            save();
        }
    }

    /**
     * Get instance of configuration file.
     * @return
     */
    public static Configuration getInstance() {
        if(configuration == null) {
            configuration = new Configuration();
        }

        return configuration;
    }

    public String getOfficePath() {
        return properties.getProperty(OFFICE_HOME_PATH);
    }

    public void setOfficePath(String officePath) {
        properties.setProperty(OFFICE_HOME_PATH, officePath);
    }

    public boolean getRenditionOfficeAlways() {
        return Boolean.getBoolean(properties.getProperty(RENDITION_OFFICE_ALWAYS));
    }

    public void setRenditionOfficeAlways(boolean officePath) {
        properties.setProperty(RENDITION_OFFICE_ALWAYS, Boolean.toString(officePath));
    }

    public void save() {
        try (OutputStream output = new FileOutputStream(PROPERTIES_FILE)) {
            properties.store(output, null);
        } catch (IOException e) {
            log.error("Error while saving properties file", e);
        }
    }
}
