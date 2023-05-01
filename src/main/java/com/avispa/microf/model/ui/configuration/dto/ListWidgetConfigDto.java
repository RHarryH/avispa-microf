package com.avispa.microf.model.ui.configuration.dto;

import com.avispa.ecm.model.configuration.load.dto.EcmConfigDto;
import lombok.Data;

import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Data
public class ListWidgetConfigDto implements EcmConfigDto {
    private String name;

    private String type;
    private String caption;
    private String emptyMessage;
    private List<String> properties;
}
