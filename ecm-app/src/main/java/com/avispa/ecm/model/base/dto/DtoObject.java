package com.avispa.ecm.model.base.dto;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.Type;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Table(name = "dto_object")
@Getter
@Setter
public class DtoObject extends EcmObject {
    @ManyToOne(optional = false)
    private Type type;

    @Column(name = "dto_name", nullable = false, unique = true)
    private Class<? extends Dto> dtoClass;

    private String discriminator;
}
