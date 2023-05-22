package com.avispa.ecm.model.content;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class ContentDto {
    private String name;
    private String path;
    private long size;
}
