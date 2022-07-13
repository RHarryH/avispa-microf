package com.avispa.microf.model.ui.widget.list;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class ListWidgetDto {
    private String resourceId;
    private String typeName;

    private String caption;
    private String emptyMessage;
    private boolean isDocument;

    private List<String> headers;
    private List<ListDataDto> dataList;
}
