package com.avispa.microf.util.validation;

import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VATINConstraintValidator implements ConstraintValidator<VATINConstraint, String> {
    @Override
    public boolean isValid(String vatin, ConstraintValidatorContext cxt) {
        if(Strings.isEmpty(vatin)) {
            return false;
        }

        if (vatin.length() == 13) {
            vatin = vatin.replace("-", "");
        }
        if (vatin.length() != 10) {
            return false;
        }
        int[] weights = {6, 5, 7, 2, 3, 4, 5, 6, 7};
        try {
            int sum = 0;
            for (int i = 0; i < weights.length; ++i) {
                sum += Integer.parseInt(vatin.substring(i, i + 1)) * weights[i];
            }
            return (sum % 11) == Integer.parseInt(vatin.substring(9, 10));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}