-- add Invoice type
SET @InvoiceTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@InvoiceTypeId, 'Invoice', 0);
insert into ecm_object (id, creation_date, modification_date) values (@InvoiceTypeId, current_timestamp, current_timestamp);
insert into type (id, class_name)
values (@InvoiceTypeId, 'com.avispa.microf.model.invoice.type.vat.Invoice');

-- add Invoice DTO type
SET @InvoiceDtoTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@InvoiceDtoTypeId, 'Invoice DTO', 0);
insert into ecm_object (id, creation_date, modification_date) values (@InvoiceDtoTypeId, current_timestamp, current_timestamp);
insert into dto_object (id, type_id, dto_name)
values (@InvoiceDtoTypeId, @InvoiceTypeId, 'com.avispa.microf.model.invoice.type.vat.InvoiceDto');

-- add Correction Invoice type
SET @CorrectionInvoiceTypeId=random_uuid();
insert into ecm_entity (id, object_name, version)
values (@CorrectionInvoiceTypeId, 'Correction Invoice', 0);
insert into ecm_object (id, creation_date, modification_date)
values (@CorrectionInvoiceTypeId, current_timestamp, current_timestamp);
insert into type (id, class_name)
values (@CorrectionInvoiceTypeId, 'com.avispa.microf.model.invoice.type.correction.CorrectionInvoice');

-- add Correction Invoice DTO type
SET @CorrectionInvoiceDtoTypeId=random_uuid();
insert into ecm_entity (id, object_name, version)
values (@CorrectionInvoiceDtoTypeId, 'Correction Invoice DTO', 0);
insert into ecm_object (id, creation_date, modification_date)
values (@CorrectionInvoiceDtoTypeId, current_timestamp, current_timestamp);
insert into dto_object (id, type_id, dto_name)
values (@CorrectionInvoiceDtoTypeId, @CorrectionInvoiceTypeId,
        'com.avispa.microf.model.invoice.type.correction.CorrectionInvoiceDto');

-- add Position type
SET @PositionTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@PositionTypeId, 'Position', 0);
insert into ecm_object (id, creation_date, modification_date) values (@PositionTypeId, current_timestamp, current_timestamp);
insert into type (id, class_name) values (@PositionTypeId, 'com.avispa.microf.model.invoice.position.Position');

-- add Position DTO type
SET @PositionDtoTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@PositionDtoTypeId, 'Position DTO', 0);
insert into ecm_object (id, creation_date, modification_date) values (@PositionDtoTypeId, current_timestamp, current_timestamp);
insert into dto_object (id, type_id, dto_name) values (@PositionDtoTypeId, @PositionTypeId, 'com.avispa.microf.model.invoice.position.PositionDto');

-- add Customer type
SET @CustomerTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@CustomerTypeId, 'Customer', 0);
insert into ecm_object (id, creation_date, modification_date) values (@CustomerTypeId, current_timestamp, current_timestamp);
insert into type (id, class_name) values (@CustomerTypeId, 'com.avispa.microf.model.customer.Customer');

-- add Customer Common DTO type
SET @CustomerCommonDtoTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@CustomerCommonDtoTypeId, 'Customer Common DTO', 0);
insert into ecm_object (id, creation_date, modification_date) values (@CustomerCommonDtoTypeId, current_timestamp, current_timestamp);
insert into dto_object (id, type_id, dto_name) values (@CustomerCommonDtoTypeId, @CustomerTypeId, 'com.avispa.microf.model.customer.CustomerCommonDto');

-- add Retail Customer DTO type
SET @RetailCustomerDtoTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@RetailCustomerDtoTypeId, 'Retail Customer DTO', 0);
insert into ecm_object (id, creation_date, modification_date) values (@RetailCustomerDtoTypeId, current_timestamp, current_timestamp);
insert into dto_object (id, type_id, discriminator, dto_name) values (@RetailCustomerDtoTypeId, @CustomerTypeId, 'RETAIL', 'com.avispa.microf.model.customer.retail.RetailCustomerDto');

-- add Corporate Customer DTO type
SET @CorporateCustomerDtoTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@CorporateCustomerDtoTypeId, 'Corporate Customer DTO', 0);
insert into ecm_object (id, creation_date, modification_date) values (@CorporateCustomerDtoTypeId, current_timestamp, current_timestamp);
insert into dto_object (id, type_id, discriminator, dto_name) values (@CorporateCustomerDtoTypeId, @CustomerTypeId, 'CORPORATE', 'com.avispa.microf.model.customer.corporate.CorporateCustomerDto');

-- add Bank Account type
SET @BankAccountTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@BankAccountTypeId, 'Bank account', 0);
insert into ecm_object (id, creation_date, modification_date) values (@BankAccountTypeId, current_timestamp, current_timestamp);
insert into type (id, class_name) values (@BankAccountTypeId, 'com.avispa.microf.model.bankaccount.BankAccount');

-- add Bank Account DTO type
SET @BankAccountDtoTypeId=random_uuid();
insert into ecm_entity (id, object_name, version) values (@BankAccountDtoTypeId, 'Bank account DTO', 0);
insert into ecm_object (id, creation_date, modification_date) values (@BankAccountDtoTypeId, current_timestamp, current_timestamp);
insert into dto_object (id, type_id, dto_name) values (@BankAccountDtoTypeId, @BankAccountTypeId, 'com.avispa.microf.model.bankaccount.BankAccountDto');