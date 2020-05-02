package com.avispa.microf.util;

/**
 * @author Rafał Hiszpański
 */
public class Version {
    private String number;

    public Version(String number) {
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
