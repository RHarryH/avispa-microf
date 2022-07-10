package com.avispa.microf.model.ui.configuration.columns;

import com.avispa.ecm.model.configuration.EcmConfig;
import com.avispa.ecm.model.type.Type;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class ListWidgetConfig extends EcmConfig {
    @OneToOne(optional = false)
    private Type type;

    private String caption;
    private String emptyMessage;

    @ElementCollection
    @OrderColumn
    private List<String> properties;
}
