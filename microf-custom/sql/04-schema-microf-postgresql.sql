CREATE TABLE IF NOT EXISTS address
(
    place    VARCHAR(255) NOT NULL,
    street   VARCHAR(255) NOT NULL,
    zip_code VARCHAR(6)   NOT NULL,
    id       CHAR(36)     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_address_ecm_object_id FOREIGN KEY (id) REFERENCES ecm_object
);

CREATE TABLE IF NOT EXISTS bank_account
(
    account_number VARCHAR(28),
    bank_name VARCHAR(50),
    id CHAR(36) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_bank_account_ecm_object_id FOREIGN KEY (id) REFERENCES ecm_object
);

CREATE TABLE IF NOT EXISTS customer
(
    company_name VARCHAR(255),
    email     VARCHAR(150),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(255),
    type      VARCHAR(255),
    vat_identification_number VARCHAR(13),
    id         CHAR(36) NOT NULL,
    address_id CHAR(36),
    PRIMARY KEY (id),
    CONSTRAINT fk_customer_address_id FOREIGN KEY (address_id) REFERENCES address,
    CONSTRAINT fk_customer_ecm_object_id FOREIGN KEY (id) REFERENCES ecm_object
);

CREATE TABLE IF NOT EXISTS payment
(
    deadline INT,
    method   VARCHAR(255) NOT NULL,
    paid_amount NUMERIC(9, 2) NOT NULL,
    paid_amount_date DATE,
    id       CHAR(36)     NOT NULL,
    bank_account_id CHAR(36),
    PRIMARY KEY (id),
    CONSTRAINT fk_payment_bank_account_id FOREIGN KEY (bank_account_id) REFERENCES bank_account,
    CONSTRAINT fk_payment_ecm_object_id FOREIGN KEY (id) REFERENCES ecm_object
);

CREATE TABLE IF NOT EXISTS position
(
    discount NUMERIC(5, 2),
    quantity NUMERIC(8, 3),
    unit VARCHAR(255),
    unit_price NUMERIC(9, 2),
    vat_rate VARCHAR(255),
    id CHAR(36) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_position_ecm_object_id FOREIGN KEY (id) REFERENCES ecm_object
);

CREATE TABLE IF NOT EXISTS invoice
(
    id       CHAR(36) NOT NULL,
    comments VARCHAR(200),
    issue_date DATE,
    serial_number INT,
    service_date DATE,
    buyer_id   CHAR(36) NOT NULL,
    payment_id CHAR(36),
    seller_id  CHAR(36) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_invoice_document_id FOREIGN KEY (id) REFERENCES document
);

CREATE TABLE IF NOT EXISTS invoice_positions
(
    invoice_id      CHAR(36) NOT NULL,
    positions_id    CHAR(36) NOT NULL,
    positions_order INT      NOT NULL,
    PRIMARY KEY (invoice_id, positions_order),
    CONSTRAINT uk_invoice_positions_positions_id UNIQUE (positions_id),
    CONSTRAINT fk_invoice_positions_position_id FOREIGN KEY (positions_id) REFERENCES position,
    CONSTRAINT fk_invoice_positions_invoice_id FOREIGN KEY (invoice_id) REFERENCES invoice
);

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