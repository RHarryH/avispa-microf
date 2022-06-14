package com.avispa.microf.util.validation.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rafał Hiszpański
 */
class NRBConstraintValidatorTest {
    private NRBConstraintValidator nrbConstraintValidator = new NRBConstraintValidator();

    @Test
    void NRBAsIBANTest() {
        assertTrue(nrbConstraintValidator.isValid("PL00103019441234567890123456", null));
    }

    @Test
    void NRBAsIBANTest2() {
        assertTrue(nrbConstraintValidator.isValid("PL00116022021234567890123456", null));
    }

    @Test
    void NRBTest() {
        assertTrue(nrbConstraintValidator.isValid("00116022021234567890123456", null));
    }

    @Test
    void nullTest() {
        assertFalse(nrbConstraintValidator.isValid(null, null));
    }

    @Test
    void wrongLengthTest() {
        assertFalse(nrbConstraintValidator.isValid("123", null));
    }
}