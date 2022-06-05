package com.avispa.microf.model.base.dto;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.Type;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Dto extends EcmObject {
    @ManyToOne(optional = false)
    private Type type;

    @Column(name = "dto_name", nullable = false, unique = true)
    private Class<? extends IDto> dtoClass;

    private String discriminator;
}
