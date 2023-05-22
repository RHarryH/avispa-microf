package com.avispa.ecm.model.ui.widget.list.config;

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.type.Type;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Rafał Hiszpański
 */
@Repository
public interface ListWidgetRepository extends EcmConfigRepository<ListWidgetConfig> {
    Optional<ListWidgetConfig> findByType(Type type);
}
