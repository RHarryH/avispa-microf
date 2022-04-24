package com.avispa.microf.util.validation;

import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VatIdentificationNumberValidator implements ConstraintValidator<VatIdentificationNumberConstraint, String> {
    @Override
    public boolean isValid(String nip, ConstraintValidatorContext cxt) {
        if(StringUtils.isEmpty(nip)) {
            return false;
        }

        if (nip.length() == 13) {
            nip = nip.replace("-", "");
        }
        if (nip.length() != 10) {
            return false;
        }
        int[] weights = {6, 5, 7, 2, 3, 4, 5, 6, 7};
        try {
            int sum = 0;
            for (int i = 0; i < weights.length; ++i) {
                sum += Integer.parseInt(nip.substring(i, i + 1)) * weights[i];
            }
            return (sum % 11) == Integer.parseInt(nip.substring(9, 10));
        } catch (NumberFormatException e) {
            return false;
        }
    }

}