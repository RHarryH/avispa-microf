package com.avispa.ecm.model.ui.modal.page;

import com.avispa.ecm.model.ui.modal.button.ModalButton;
import lombok.Value;

import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Value
public class ModalPage {
    ModalPageType type;
    List<ModalButton> buttons;

    public void addPreviousButton() {
        this.buttons.add(0, ModalButton.createPrevious());
    }

    public void addNextButton() {
        this.buttons.add(ModalButton.createNext());
    }
}
