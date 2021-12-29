package com.avispa.microf.constants;

import com.avispa.ecm.util.Displayable;
import lombok.Getter;

/**
 * Unit that can be used on the invoice position
 * Examples for the future:
 *
 * Waga: gram, dekagram, kilogram, tona
 * Długość: milimetr, centymetr, metr, kilometr
 * Objętość: mililitr, litr, decymetr sześcienny, metr sześcienny
 * Powierzchnia: milimetr kwadratowy, centymetr kwadratowy, decymetr kwadratowy, metr kwadratowy
 * Czas: sekunda, minuta, godzina, doba
 * Sztuka: sztuka, dziesięć sztuk, sto sztuk, tysiąc sztuk
 * Inne: usługa
 * @author Rafał Hiszpański
 */
@Getter
public enum Unit implements Displayable {
    PIECE("szt"),
    HOUR("godz");

    private String displayValue;

    Unit(String displayValue) {
        this.displayValue = displayValue;
    }
}
