package com.avispa.microf.util.validation.account;

import org.apache.commons.validator.routines.IBANValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The IBAN consists of up to 34 alphanumeric characters, as follows:
 * - country code using ISO 3166-1 alpha-2 – two letters,
 * - check digits – two digits, and
 * - Basic Bank Account Number (BBAN) – up to 30 alphanumeric characters that are country-specific.
 * The check digits represent the checksum of the bank account number which is used by banking systems to confirm that
 * the number contains no simple errors.
 * @author Rafał Hiszpański
 */
public class IBANConstraintValidator implements ConstraintValidator<IBANConstraint, String> {
    private static final IBANValidator validator = IBANValidator.DEFAULT_IBAN_VALIDATOR;

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext cxt) {
        if(iban == null) {
            return false;
        }

        String cleanIban = iban.replaceAll("[^a-zA-Z0-9]", "");
        return validator.isValid(cleanIban);
    }
}
