CREATE TABLE IF NOT EXISTS list_widget_config (
    caption VARCHAR(255),
    empty_message VARCHAR(255),
    id VARCHAR(255) NOT NULL,
    type_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
	CONSTRAINT uk_list_widget_config_type_id UNIQUE (type_id),
	CONSTRAINT fk_list_widget_config_type_id FOREIGN KEY (type_id) REFERENCES type,
	CONSTRAINT fk_list_widget_config_ecm_config_id FOREIGN KEY (id) REFERENCES ecm_config
);

CREATE TABLE IF NOT EXISTS list_widget_config_properties (
    list_widget_config_id VARCHAR(255) NOT NULL,
    properties VARCHAR(255),
    properties_order INT NOT NULL,
    PRIMARY KEY (list_widget_config_id, properties_order),
	CONSTRAINT fk_list_widget_config_properties_list_widget_config_id FOREIGN KEY (list_widget_config_id) REFERENCES list_widget_config
);