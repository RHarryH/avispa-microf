/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

-- Avispa ECM
ALTER TABLE list_widget
    ADD COLUMN items_per_page INTEGER DEFAULT 10;

ALTER TABLE list_widget_properties
    ADD CONSTRAINT uk_list_widget_properties_properties_id UNIQUE (properties_id);

CREATE TABLE IF NOT EXISTS list_widget_property
(
    id    CHAR(36) NOT NULL,
    label VARCHAR(255),
    name  VARCHAR(255),
    PRIMARY KEY (id)
);

-- start list widget properties migration
INSERT INTO list_widget_property (id, name)
SELECT gen_random_uuid(), properties
FROM list_widget_properties;

ALTER TABLE list_widget_properties
    ADD COLUMN properties_id CHAR(36);

UPDATE list_widget_properties AS old
SET properties_id = new.id
FROM list_widget_property AS new
WHERE new.name = old.properties;

ALTER TABLE list_widget_properties
    ALTER COLUMN properties_id SET NOT NULL;

ALTER TABLE list_widget_properties
    DROP COLUMN properties;
-- end list widget properties migration

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

-- Avispa μF
ALTER TABLE position
    ALTER COLUMN discount TYPE NUMERIC(3);

CREATE TABLE IF NOT EXISTS correction_invoice
(
    id                  CHAR(36) NOT NULL,
    comments            VARCHAR(200),
    correction_reason   VARCHAR(200),
    issue_date          DATE,
    serial_number       INT,
    payment_id          CHAR(36),
    original_invoice_id CHAR(36) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_invoice_document_id FOREIGN KEY (id) REFERENCES document
);

CREATE TABLE IF NOT EXISTS correction_invoice_positions
(
    correction_invoice_id CHAR(36) NOT NULL,
    positions_id          CHAR(36) NOT NULL,
    positions_order       INT      NOT NULL,
    PRIMARY KEY (correction_invoice_id, positions_order),
    CONSTRAINT uk_correction_invoice_positions_positions_id UNIQUE (positions_id),
    CONSTRAINT fk_correction_invoice_positions_position_id FOREIGN KEY (positions_id) REFERENCES position,
    CONSTRAINT fk_correction_invoice_positions_invoice_id FOREIGN KEY (correction_invoice_id) REFERENCES correction_invoice
);

-- repository migration
UPDATE file_store
SET root_path = '/opt/ecm-client/repository'
WHERE root_path = '/opt/microf/repository';

UPDATE content
SET file_store_path = REPLACE(file_store_path, 'microf', 'ecm-client');

-- type migration
UPDATE type
SET class_name = 'com.avispa.microf.model.invoice.type.vat.Invoice'
WHERE class_name = 'com.avispa.microf.model.invoice.Invoice';

UPDATE dto_object
SET dto_name = 'com.avispa.microf.model.invoice.type.vat.InvoiceDto'
WHERE dto_name = 'com.avispa.microf.model.invoice.InvoiceDto';

-- new type registration
-- add Correction Invoice type & Correction Invoice DTO type
DO
$$
    DECLARE
        CorrectionInvoiceTypeId    UUID := gen_random_uuid();
        CorrectionInvoiceDtoTypeId UUID := gen_random_uuid();
    BEGIN
        INSERT INTO ecm_entity (id, object_name, version) VALUES (CorrectionInvoiceTypeId, 'Correction Invoice', 0);
        INSERT INTO ecm_object (id, creation_date, modification_date)
        VALUES (CorrectionInvoiceTypeId, current_timestamp, current_timestamp);
        INSERT INTO "type" (id, class_name)
        VALUES (CorrectionInvoiceTypeId, 'com.avispa.microf.model.invoice.type.correction.CorrectionInvoice');

        INSERT INTO ecm_entity (id, object_name, version)
        VALUES (CorrectionInvoiceDtoTypeId, 'Correction Invoice DTO', 0);
        INSERT INTO ecm_object (id, creation_date, modification_date)
        VALUES (CorrectionInvoiceDtoTypeId, current_timestamp, current_timestamp);
        INSERT INTO dto_object (id, type_id, dto_name)
        VALUES (CorrectionInvoiceDtoTypeId, CorrectionInvoiceTypeId,
                'com.avispa.microf.model.invoice.type.correction.CorrectionInvoiceDto');
    END
$$;

-- please clear and reload whole microf ZIP configuration

