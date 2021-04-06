package com.avispa.microf.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Rafał Hiszpański
 */
@Component
public class Version {
    private String number;

    public Version(@Value("${microf.version}") String number) {
        this.number = number;
    }

    public String getNumber() {
        return this.number;
    }

    public String getReleaseNumber() {
        int index = this.number.indexOf('-');
        if(index != -1) {
            return this.number.substring(0, index);
        }
        return this.number;
    }
}
