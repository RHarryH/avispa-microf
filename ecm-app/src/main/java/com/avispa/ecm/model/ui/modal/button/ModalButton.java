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
