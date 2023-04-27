package com.avispa.microf.model.ui.widget.list.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class ListDataDto {
    private UUID id;
    private Map<String, Object> values;
}
