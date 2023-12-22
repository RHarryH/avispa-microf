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
ALTER TABLE ecm_entity
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE ecm_object
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE ecm_object
    ALTER COLUMN folder_id TYPE CHAR(36);
ALTER TABLE format
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE content
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE content
    ALTER COLUMN format_id TYPE CHAR(36);
ALTER TABLE content
    ALTER COLUMN related_entity_id TYPE CHAR(36);
ALTER TABLE document
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE file_store
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE folder
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE folder_ancestors
    ALTER COLUMN folder_id TYPE CHAR(36);
ALTER TABLE folder_ancestors
    ALTER COLUMN ancestors_id TYPE CHAR(36);
ALTER TABLE type
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE dto_object
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE dto_object
    ALTER COLUMN type_id TYPE CHAR(36);
ALTER TABLE ecm_config
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE autolink
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE autolink_rules
    ALTER COLUMN autolink_id TYPE CHAR(36);
ALTER TABLE autoname
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE context
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE context
    ALTER COLUMN type_id TYPE CHAR(36);
ALTER TABLE context_ecm_configs
    ALTER COLUMN context_id TYPE CHAR(36);
ALTER TABLE context_ecm_configs
    ALTER COLUMN ecm_configs_id TYPE CHAR(36);
ALTER TABLE dictionary
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE dictionary_value
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE dictionary_value
    ALTER COLUMN dictionary_id TYPE CHAR(36);
ALTER TABLE dictionary_value_columns
    ALTER COLUMN dictionary_value_id TYPE CHAR(36);
ALTER TABLE property_page
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE template
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE upsert
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE upsert
    ALTER COLUMN property_page_id TYPE CHAR(36);

-- Avispa μF
ALTER TABLE address
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE bank_account
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE customer
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE customer
    ALTER COLUMN address_id TYPE CHAR(36);
ALTER TABLE payment
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE payment
    ALTER COLUMN bank_account_id TYPE CHAR(36);
ALTER TABLE position
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE invoice
    ALTER COLUMN id TYPE CHAR(36);
ALTER TABLE invoice
    ALTER COLUMN buyer_id TYPE CHAR(36);
ALTER TABLE invoice
    ALTER COLUMN payment_id TYPE CHAR(36);
ALTER TABLE invoice
    ALTER COLUMN seller_id TYPE CHAR(36);
ALTER TABLE invoice_positions
    ALTER COLUMN invoice_id TYPE CHAR(36);
ALTER TABLE invoice_positions
    ALTER COLUMN positions_id TYPE CHAR(36);