package com.avispa.microf.model.ui.widget.list.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class ListDataDto {
    private UUID id;
    private List<String> values;

    @Getter(AccessLevel.NONE)
    private boolean hasPdfRendition;

    public boolean hasPdfRendition() {
        return hasPdfRendition;
    }
}