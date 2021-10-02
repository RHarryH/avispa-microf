package com.avispa.microf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
@AllArgsConstructor
public class AttachementDto {
    private String name;
    private String path;
    private long size;
}
