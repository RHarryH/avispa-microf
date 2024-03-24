CREATE TABLE IF NOT EXISTS application
(
    id         CHAR(36) NOT NULL PRIMARY KEY,
    full_name  VARCHAR(255),
    short_name VARCHAR(255),
    description VARCHAR(255),
    CONSTRAINT fk_application_ecm_config_id FOREIGN KEY (id) REFERENCES ecm_config
);

CREATE TABLE IF NOT EXISTS list_widget
(
    caption        VARCHAR(255),
    empty_message  VARCHAR(255),
    id             CHAR(36) NOT NULL,
    type_id        CHAR(36) NOT NULL,
    items_per_page INTEGER  NOT NULL DEFAULT 10,
    PRIMARY KEY (id),
    CONSTRAINT uk_list_widget_type_id UNIQUE (type_id),
    CONSTRAINT fk_list_widget_type_id FOREIGN KEY (type_id) REFERENCES type,
    CONSTRAINT fk_list_widget_ecm_config_id FOREIGN KEY (id) REFERENCES ecm_config
);

CREATE TABLE IF NOT EXISTS list_widget_properties
(
    list_widget_id   CHAR(36) NOT NULL,
    properties_order INT      NOT NULL,
    properties_id CHAR(36) NOT NULL,
    PRIMARY KEY (list_widget_id, properties_order),
    CONSTRAINT uk_list_widget_properties_properties_id UNIQUE (properties_id),
    CONSTRAINT fk_list_widget_properties_list_widget_id FOREIGN KEY (list_widget_id) REFERENCES list_widget
);

CREATE TABLE IF NOT EXISTS list_widget_property
(
    id    CHAR(36) NOT NULL,
    label VARCHAR(255),
    name  VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS layout
(
    id CHAR(36) NOT NULL PRIMARY KEY,
    CONSTRAINT fk_layout_ecm_config_id FOREIGN KEY (id) REFERENCES ecm_config
);

CREATE TABLE IF NOT EXISTS menu
(
    id CHAR(36) NOT NULL PRIMARY KEY,
    CONSTRAINT fk_menu_ecm_config_id FOREIGN KEY (id) REFERENCES ecm_config
);

CREATE TABLE IF NOT EXISTS menu_item
(
    action VARCHAR(255),
    label  VARCHAR(255),
    id     CHAR(36) NOT NULL PRIMARY KEY,
    CONSTRAINT fk_menu_item_ecm_config_id FOREIGN KEY (id) REFERENCES ecm_config
);

CREATE TABLE IF NOT EXISTS menu_items
(
    menu_id     CHAR(36) NOT NULL,
    items_id    CHAR(36) NOT NULL,
    items_order INTEGER  NOT NULL,
    PRIMARY KEY (menu_id, items_order),
    CONSTRAINT fk_menu_items_menu_menu_id FOREIGN KEY (menu_id) REFERENCES menu,
    CONSTRAINT fk_menu_items_menu_item_items_id FOREIGN KEY (items_id) REFERENCES menu_item
);

CREATE TABLE IF NOT EXISTS menu_item_items
(
    menu_item_id CHAR(36) NOT NULL,
    items_id     CHAR(36) NOT NULL,
    items_order  INTEGER  NOT NULL,
    PRIMARY KEY (menu_item_id, items_order),
    CONSTRAINT fk_menu_item_items_menu_item_menu_item_id FOREIGN KEY (menu_item_id) REFERENCES menu_item,
    CONSTRAINT fk_menu_item_items_menu_item_items_id FOREIGN KEY (items_id) REFERENCES menu_item
);

CREATE TABLE IF NOT EXISTS link_document
(
    id            CHAR(36)     NOT NULL PRIMARY KEY,
    link_property VARCHAR(255) NOT NULL,
    type_id       CHAR(36)     NOT NULL,
    title         VARCHAR(50),
    message       VARCHAR(50),
    CONSTRAINT uk_link_document_type_id UNIQUE (type_id),
    CONSTRAINT fk_link_document_type_id FOREIGN KEY (type_id) REFERENCES type
);