-- add Invoice type & Invoice DTO type
DO $$
DECLARE
    InvoiceTypeId UUID := gen_random_uuid();
    InvoiceDtoTypeId UUID := gen_random_uuid();
BEGIN
    INSERT INTO ecm_entity (id, object_name, version) VALUES (InvoiceTypeId, 'Invoice', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (InvoiceTypeId, current_timestamp, current_timestamp);
    INSERT INTO "type" (id, class_name) VALUES (InvoiceTypeId, 'com.avispa.microf.model.invoice.Invoice');

    INSERT INTO ecm_entity (id, object_name, version) VALUES (InvoiceDtoTypeId, 'Invoice DTO', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (InvoiceDtoTypeId, current_timestamp, current_timestamp);
    INSERT INTO dto_object (id, type_id, dto_name) VALUES (InvoiceDtoTypeId, InvoiceTypeId, 'com.avispa.microf.model.invoice.InvoiceDto');
END $$;

-- add Position type & Position DTO type
DO $$
DECLARE
    PositionTypeId UUID := gen_random_uuid();
    PositionDtoTypeId UUID := gen_random_uuid();
BEGIN
    INSERT INTO ecm_entity (id, object_name, version) VALUES (PositionTypeId, 'Position', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (PositionTypeId, current_timestamp, current_timestamp);
    INSERT INTO "type" (id, class_name) VALUES (PositionTypeId, 'com.avispa.microf.model.invoice.position.Position');

    INSERT INTO ecm_entity (id, object_name, version) VALUES (PositionDtoTypeId, 'Position DTO', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (PositionDtoTypeId, current_timestamp, current_timestamp);
    INSERT INTO dto_object (id, type_id, dto_name) VALUES (PositionDtoTypeId, PositionTypeId, 'com.avispa.microf.model.invoice.position.PositionDto');
END $$;

-- add
--- Customer type
--- Customer Common DTO type
--- Retail Customer DTO type
--- Corporate Customer DTO type
DO $$
DECLARE
    CustomerTypeId UUID := gen_random_uuid();
    CustomerCommonDtoTypeId UUID := gen_random_uuid();
    RetailCustomerDtoTypeId UUID := gen_random_uuid();
    CorporateCustomerDtoTypeId UUID := gen_random_uuid();
BEGIN
    INSERT INTO ecm_entity (id, object_name, version) VALUES (CustomerTypeId, 'Customer', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (CustomerTypeId, current_timestamp, current_timestamp);
    INSERT INTO "type" (id, class_name) VALUES (CustomerTypeId, 'com.avispa.microf.model.customer.Customer');

    INSERT INTO ecm_entity (id, object_name, version) VALUES (CustomerCommonDtoTypeId, 'Customer Common DTO', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (CustomerCommonDtoTypeId, current_timestamp, current_timestamp);
    INSERT INTO dto_object (id, type_id, dto_name) VALUES (CustomerCommonDtoTypeId, CustomerTypeId, 'com.avispa.microf.model.customer.CustomerCommonDto');

    INSERT INTO ecm_entity (id, object_name, version) VALUES (RetailCustomerDtoTypeId, 'Retail Customer DTO', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (RetailCustomerDtoTypeId, current_timestamp, current_timestamp);
    INSERT INTO dto_object (id, type_id, discriminator, dto_name) VALUES (RetailCustomerDtoTypeId, CustomerTypeId, 'RETAIL', 'com.avispa.microf.model.customer.retail.RetailCustomerDto');

    INSERT INTO ecm_entity (id, object_name, version) VALUES (CorporateCustomerDtoTypeId, 'Corporate Customer DTO', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (CorporateCustomerDtoTypeId, current_timestamp, current_timestamp);
    INSERT INTO dto_object (id, type_id, discriminator, dto_name) VALUES (CorporateCustomerDtoTypeId, CustomerTypeId, 'CORPORATE', 'com.avispa.microf.model.customer.corporate.CorporateCustomerDto');
END $$;

-- add Bank Account type & Bank Account DTO type
DO $$
DECLARE
    BankAccountTypeId UUID := gen_random_uuid();
    BankAccountDtoTypeId UUID := gen_random_uuid();
BEGIN
    INSERT INTO ecm_entity (id, object_name, version) VALUES (BankAccountTypeId, 'Bank account', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (BankAccountTypeId, current_timestamp, current_timestamp);
    INSERT INTO "type" (id, class_name) VALUES (BankAccountTypeId, 'com.avispa.microf.model.bankaccount.BankAccount');

    INSERT INTO ecm_entity (id, object_name, version) VALUES (BankAccountDtoTypeId, 'Bank account DTO', 0);
    INSERT INTO ecm_object (id, creation_date, modification_date) VALUES (BankAccountDtoTypeId, current_timestamp, current_timestamp);
    INSERT INTO dto_object (id, type_id, dto_name) VALUES (BankAccountDtoTypeId, BankAccountTypeId, 'com.avispa.microf.model.bankaccount.BankAccountDto');
END $$;