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

package com.avispa.ecm.model.ui.modal.button;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rafał Hiszpański
 */
@AllArgsConstructor
public class ModalButton {
    @Getter
    private String text;
    private ModalButtonType type;

    public static ModalButton createAccept(String text) {
        return new ModalButton(text, ModalButtonType.ACCEPT);
    }

    public static ModalButton createReject() {
        return new ModalButton("Reject", ModalButtonType.REJECT);
    }

    public static ModalButton createPrevious() {
        return new ModalButton("Previous", ModalButtonType.PREVIOUS);
    }

    public static ModalButton createNext() {
        return new ModalButton("Next", ModalButtonType.NEXT);
    }

    public static ModalButton createReset() {
        return new ModalButton("Reset", ModalButtonType.RESET);
    }

    public boolean isAcceptButton() {
        return type.equals(ModalButtonType.ACCEPT);
    }

    public boolean isRejectButton() {
        return type.equals(ModalButtonType.REJECT);
    }

    public boolean isPreviousButton() {
        return type.equals(ModalButtonType.PREVIOUS);
    }

    public boolean isNextButton() {
        return type.equals(ModalButtonType.NEXT);
    }

    public boolean isResetButton() {
        return type.equals(ModalButtonType.RESET);
    }
}
