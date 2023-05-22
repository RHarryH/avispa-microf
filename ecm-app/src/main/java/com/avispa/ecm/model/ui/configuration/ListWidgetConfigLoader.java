package com.avispa.ecm.model.ui.configuration;

import com.avispa.ecm.model.configuration.load.GenericLoader;
import com.avispa.ecm.model.content.ContentService;
import com.avispa.ecm.model.ui.configuration.dto.ListWidgetConfigDto;
import com.avispa.ecm.model.ui.configuration.mapper.ListWidgetConfigMapper;
import com.avispa.ecm.model.ui.widget.list.config.ListWidgetConfig;
import com.avispa.ecm.model.ui.widget.list.config.ListWidgetRepository;
import org.springframework.stereotype.Component;

/**
 * @author Rafał Hiszpański
 */
@Component
class ListWidgetConfigLoader extends GenericLoader<ListWidgetConfig, ListWidgetConfigDto, ListWidgetConfigMapper> {
    protected ListWidgetConfigLoader(ListWidgetRepository ecmConfigRepository, ListWidgetConfigMapper ecmConfigMapper, ContentService contentService) {
        super(ecmConfigRepository, ecmConfigMapper, contentService);
    }
}
