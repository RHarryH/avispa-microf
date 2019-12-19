package com.avispa.microf.util;

import com.avispa.microf.invoice.InvoiceFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Rafał Hiszpański
 */
public class PropertiesUtils {
    private static final Logger log = LoggerFactory.getLogger(InvoiceFile.class);
    private static final Properties properties = new Properties();

    static {

        try {
            properties.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("microf.properties"));
        } catch (IOException e) {
            log.error("Error while loading properties");
        }
    }

    public static String getApplicationVersion() {
        return properties.getProperty("microf.version");
    }
}
