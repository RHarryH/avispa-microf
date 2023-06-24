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

/**
 * @author Rafał Hiszpański
 */
public class Version {
    private final String number;

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
