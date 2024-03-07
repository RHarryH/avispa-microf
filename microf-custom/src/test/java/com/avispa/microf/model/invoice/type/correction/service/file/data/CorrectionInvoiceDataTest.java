/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.microf.model.invoice.type.correction.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.invoice.service.file.data.PaymentData;
import com.avispa.microf.model.invoice.service.file.data.PositionData;
import com.avispa.microf.model.invoice.service.file.data.VatMatrix;
import com.avispa.microf.model.invoice.service.file.data.VatRowData;
import com.avispa.microf.util.InvoiceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class CorrectionInvoiceDataTest {
    @Mock
    private Dictionary unitDict;

    @Mock
    private Dictionary vatRateDict;

    @Mock
    private Dictionary paymentTypeDict;

    @Test
    void convertToData() {
        when(unitDict.isEmpty()).thenReturn(false);
        when(unitDict.getLabel(anyString())).thenReturn("godz.");

        when(vatRateDict.getLabel(anyString())).thenReturn("5%");
        when(vatRateDict.getColumnValue(anyString(), anyString())).thenReturn("0.05");

        when(paymentTypeDict.isEmpty()).thenReturn(false);
        when(paymentTypeDict.getLabel(anyString())).thenReturn("przelew bankowy");

        CorrectionInvoiceData invoiceData = new CorrectionInvoiceData(InvoiceUtils.getCorrectionInvoice(), unitDict, vatRateDict, paymentTypeDict);

        assertAll(
                "Positions rows size",
                () -> assertEquals(1, invoiceData.getOriginalPositions().size()),
                () -> assertEquals(1, invoiceData.getCorrectedPositions().size()),
                () -> assertEquals(1, invoiceData.getPositionsDiff().size())
        );

        assertEquals("Corrected 123", invoiceData.getInvoiceName());

        PaymentData payment = invoiceData.getPayment();
        assertAll(
                "Payment checks",
                () -> assertEquals("2022-07-31", payment.getDeadlineDate()),
                () -> assertThat(payment.getPaidAmount()).isEqualByComparingTo("1"),
                () -> assertEquals("2022-07-19", payment.getPaidAmountDate()),
                () -> assertThat(payment.getAmount()).isEqualByComparingTo("1.079")
        );

        assertOriginalPosition(invoiceData);
        assertCorrectedPosition(invoiceData);
        assertPositionsDiff(invoiceData);

        assertOriginalVatMatrix(invoiceData);
        assertCorrectedVatMatrix(invoiceData);
        assertVatMatrixDiff(invoiceData);
    }

    private static void assertOriginalPosition(CorrectionInvoiceData invoiceData) {
        PositionData originalPosition = invoiceData.getOriginalPositions().get(0);
        assertAll(
                "Original position check",
                () -> assertEquals("Position", originalPosition.getPositionName()),
                () -> assertEquals("godz.", originalPosition.getUnit()),
                () -> assertEquals("5%", originalPosition.getVatRateLabel()),
                () -> assertThat(originalPosition.getUnitPrice()).isEqualByComparingTo("10"),
                () -> assertThat(originalPosition.getDiscount()).isEqualByComparingTo("0"),
                () -> assertThat(originalPosition.getQuantity()).isEqualByComparingTo("1"),

                () -> assertThat(originalPosition.getPrice()).isEqualByComparingTo("10.00"),
                () -> assertThat(originalPosition.getNetValue()).isEqualByComparingTo("10.00"),
                () -> assertThat(originalPosition.getVat().stripTrailingZeros()).isEqualByComparingTo("0.5"),
                () -> assertThat(originalPosition.getGrossValue().stripTrailingZeros()).isEqualByComparingTo("10.5"),
                () -> assertThat(originalPosition.getVatRate()).isEqualByComparingTo("0.05")
        );
    }

    private static void assertCorrectedPosition(CorrectionInvoiceData invoiceData) {
        PositionData correctedPosition = invoiceData.getCorrectedPositions().get(0);
        assertAll(
                "Corrected position check",
                () -> assertEquals("Corrected position", correctedPosition.getPositionName()),
                () -> assertEquals("godz.", correctedPosition.getUnit()),
                () -> assertEquals("5%", correctedPosition.getVatRateLabel()),
                () -> assertThat(correctedPosition.getUnitPrice()).isEqualByComparingTo("1"),
                () -> assertThat(correctedPosition.getDiscount()).isEqualByComparingTo("1"),
                () -> assertThat(correctedPosition.getQuantity()).isEqualByComparingTo("2"),

                () -> assertThat(correctedPosition.getPrice()).isEqualByComparingTo("0.99"),
                () -> assertThat(correctedPosition.getNetValue()).isEqualByComparingTo("1.98"),
                () -> assertThat(correctedPosition.getVat()).isEqualByComparingTo("0.099"),
                () -> assertThat(correctedPosition.getGrossValue()).isEqualByComparingTo("2.079"),
                () -> assertThat(correctedPosition.getVatRate()).isEqualByComparingTo("0.05")
        );
    }

    private static void assertPositionsDiff(CorrectionInvoiceData invoiceData) {
        PositionData positionsDiff = invoiceData.getPositionsDiff().get(0);
        assertAll(
                "Original and corrected positions difference",
                () -> assertEquals("Corrected position", positionsDiff.getPositionName()),
                () -> assertEquals("godz.", positionsDiff.getUnit()),
                () -> assertEquals("5%", positionsDiff.getVatRateLabel()),
                () -> assertThat(positionsDiff.getUnitPrice()).isEqualByComparingTo("-9"),
                () -> assertThat(positionsDiff.getDiscount()).isEqualByComparingTo("1"),
                () -> assertThat(positionsDiff.getQuantity()).isEqualByComparingTo("1"),

                () -> assertThat(positionsDiff.getPrice()).isEqualByComparingTo("-9.01"),
                () -> assertThat(positionsDiff.getNetValue()).isEqualByComparingTo("-8.02"),
                () -> assertThat(positionsDiff.getVat()).isEqualByComparingTo("-0.401"),
                () -> assertThat(positionsDiff.getGrossValue()).isEqualByComparingTo("-8.421"),
                () -> assertThat(positionsDiff.getVatRate()).isEqualByComparingTo("0.05")
        );
    }

    private static void assertOriginalVatMatrix(CorrectionInvoiceData invoiceData) {
        VatMatrix vatMatrix = invoiceData.getOriginalVatMatrix();

        List<VatRowData> vatRates = vatMatrix.vatRatesList();
        VatRowData vatRate = vatRates.get(0);
        assertAll(
                "Original 5% VAT",
                () -> assertThat(vatRate.getVat()).isEqualByComparingTo("0.5"),
                () -> assertEquals("5%", vatRate.getVatRate()),
                () -> assertThat(vatRate.getNetValue()).isEqualByComparingTo("10.0"),
                () -> assertThat(vatRate.getGrossValue()).isEqualByComparingTo("10.5")
        );

        VatRowData summary = vatMatrix.summary();
        assertAll(
                "Original VAT summary",
                () -> assertThat(summary.getVat()).isEqualByComparingTo("0.5"),
                () -> assertNull(summary.getVatRate()),
                () -> assertThat(summary.getNetValue()).isEqualByComparingTo("10.0"),
                () -> assertThat(summary.getGrossValue()).isEqualByComparingTo("10.5")
        );
    }

    private static void assertCorrectedVatMatrix(CorrectionInvoiceData invoiceData) {
        VatMatrix vatMatrix = invoiceData.getCorrectedVatMatrix();

        List<VatRowData> vatRates = vatMatrix.vatRatesList();
        VatRowData vatRate = vatRates.get(0);
        assertAll(
                "Corrected VAT 5%",
                () -> assertThat(vatRate.getVat()).isEqualByComparingTo("0.099"),
                () -> assertEquals("5%", vatRate.getVatRate()),
                () -> assertThat(vatRate.getNetValue()).isEqualByComparingTo("1.98"),
                () -> assertThat(vatRate.getGrossValue()).isEqualByComparingTo("2.079")
        );

        VatRowData summary = vatMatrix.summary();
        assertAll(
                "Corrected VAT summary",
                () -> assertThat(summary.getVat()).isEqualByComparingTo("0.099"),
                () -> assertNull(summary.getVatRate()),
                () -> assertThat(summary.getNetValue()).isEqualByComparingTo("1.98"),
                () -> assertThat(summary.getGrossValue()).isEqualByComparingTo("2.079")
        );
    }

    private static void assertVatMatrixDiff(CorrectionInvoiceData invoiceData) {
        VatMatrix vatMatrix = invoiceData.getVatMatrixDiff();

        List<VatRowData> vatRates = vatMatrix.vatRatesList();
        VatRowData vatRate = vatRates.get(0);
        assertAll(
                "VAT 5% difference",
                () -> assertThat(vatRate.getVat()).isEqualByComparingTo("-0.401"),
                () -> assertEquals("5%", vatRate.getVatRate()),
                () -> assertThat(vatRate.getNetValue()).isEqualByComparingTo("-8.02"),
                () -> assertThat(vatRate.getGrossValue()).isEqualByComparingTo("-8.421")
        );

        VatRowData summary = vatMatrix.summary();
        assertAll(
                "VAT summary difference",
                () -> assertThat(summary.getVat()).isEqualByComparingTo("-0.401"),
                () -> assertNull(summary.getVatRate()),
                () -> assertThat(summary.getNetValue()).isEqualByComparingTo("-8.02"),
                () -> assertThat(summary.getGrossValue()).isEqualByComparingTo("-8.421")
        );
    }

    @Test
    void givenCorrectionInvoice_whenCorrectionHasNewTaxRate_thenVatMatricesAreCorrect() {
        when(unitDict.isEmpty()).thenReturn(false);
        when(unitDict.getLabel(anyString())).thenReturn("godz.");

        when(vatRateDict.getLabel("VAT_05")).thenReturn("5%");
        when(vatRateDict.getColumnValue(eq("VAT_05"), anyString())).thenReturn("0.05");
        when(vatRateDict.getLabel("VAT_23")).thenReturn("23%");
        when(vatRateDict.getColumnValue(eq("VAT_23"), anyString())).thenReturn("0.23");

        when(paymentTypeDict.isEmpty()).thenReturn(false);
        when(paymentTypeDict.getLabel(anyString())).thenReturn("przelew bankowy");

        var originalPositions = List.of(InvoiceUtils.getPosition1(), InvoiceUtils.getPosition2("VAT_05"));
        var correctedPositions = List.of(InvoiceUtils.getCorrectedPosition1(), InvoiceUtils.getCorrectedPosition2("VAT_23"));

        CorrectionInvoiceData invoiceData = new CorrectionInvoiceData(
                InvoiceUtils.getCorrectionInvoice(originalPositions, correctedPositions),
                unitDict, vatRateDict, paymentTypeDict);

        assertOriginalVatMatrixNewTaxRate(invoiceData);
        assertCorrectedVatMatrixNewTaxRate(invoiceData);
        assertVatMatrixDiffNewTaxRate(invoiceData);
    }

    private static void assertOriginalVatMatrixNewTaxRate(CorrectionInvoiceData invoiceData) {
        VatMatrix vatMatrix = invoiceData.getOriginalVatMatrix();

        assertEquals(1, vatMatrix.vatRatesList().size());

        List<VatRowData> vatRates = vatMatrix.vatRatesList();
        VatRowData vatRate = vatRates.get(0);
        assertAll(
                "Original VAT 5%",
                () -> assertThat(vatRate.getVat()).isEqualByComparingTo("5.5"),
                () -> assertEquals("5%", vatRate.getVatRate()),
                () -> assertThat(vatRate.getNetValue()).isEqualByComparingTo("110.0"),
                () -> assertThat(vatRate.getGrossValue()).isEqualByComparingTo("115.5")
        );

        VatRowData summary = vatMatrix.summary();
        assertAll(
                "Original VAT summary",
                () -> assertThat(summary.getVat()).isEqualByComparingTo("5.5"),
                () -> assertNull(summary.getVatRate()),
                () -> assertThat(summary.getNetValue()).isEqualByComparingTo("110.0"),
                () -> assertThat(summary.getGrossValue()).isEqualByComparingTo("115.5")
        );
    }

    private static void assertCorrectedVatMatrixNewTaxRate(CorrectionInvoiceData invoiceData) {
        VatMatrix vatMatrix = invoiceData.getCorrectedVatMatrix();

        assertEquals(2, vatMatrix.vatRatesList().size());

        List<VatRowData> vatRates = vatMatrix.vatRatesList();
        VatRowData vatRate0 = vatRates.get(0);
        assertAll(
                "Corrected VAT 23%",
                () -> assertThat(vatRate0.getVat()).isEqualByComparingTo("2.3"),
                () -> assertEquals("23%", vatRate0.getVatRate()),
                () -> assertThat(vatRate0.getNetValue()).isEqualByComparingTo("10"),
                () -> assertThat(vatRate0.getGrossValue()).isEqualByComparingTo("12.3")
        );

        VatRowData vatRate1 = vatRates.get(1);
        assertAll(
                "Corrected VAT 5%",
                () -> assertThat(vatRate1.getVat()).isEqualByComparingTo("0.099"),
                () -> assertEquals("5%", vatRate1.getVatRate()),
                () -> assertThat(vatRate1.getNetValue()).isEqualByComparingTo("1.98"),
                () -> assertThat(vatRate1.getGrossValue()).isEqualByComparingTo("2.079")
        );

        VatRowData summary = vatMatrix.summary();
        assertAll(
                "Corrected VAT summary",
                () -> assertThat(summary.getVat()).isEqualByComparingTo("2.399"),
                () -> assertNull(summary.getVatRate()),
                () -> assertThat(summary.getNetValue()).isEqualByComparingTo("11.98"),
                () -> assertThat(summary.getGrossValue()).isEqualByComparingTo("14.379")
        );
    }

    private static void assertVatMatrixDiffNewTaxRate(CorrectionInvoiceData invoiceData) {
        VatMatrix vatMatrix = invoiceData.getVatMatrixDiff();

        assertEquals(2, vatMatrix.vatRatesList().size());

        List<VatRowData> vatRates = vatMatrix.vatRatesList();
        VatRowData vatRate0 = vatRates.get(0);
        assertAll(
                "VAT 23% difference",
                () -> assertThat(vatRate0.getVat()).isEqualByComparingTo("2.3"),
                () -> assertEquals("23%", vatRate0.getVatRate()),
                () -> assertThat(vatRate0.getNetValue()).isEqualByComparingTo("10"),
                () -> assertThat(vatRate0.getGrossValue()).isEqualByComparingTo("12.3")
        );

        VatRowData vatRate1 = vatRates.get(1);
        assertAll(
                "VAT 5% difference",
                () -> assertThat(vatRate1.getVat()).isEqualByComparingTo("-5.401"),
                () -> assertEquals("5%", vatRate1.getVatRate()),
                () -> assertThat(vatRate1.getNetValue()).isEqualByComparingTo("-108.02"),
                () -> assertThat(vatRate1.getGrossValue()).isEqualByComparingTo("-113.4210")
        );

        VatRowData summary = vatMatrix.summary();
        assertAll(
                "VAT summary difference",
                () -> assertThat(summary.getVat()).isEqualByComparingTo("-3.101"),
                () -> assertNull(summary.getVatRate()),
                () -> assertThat(summary.getNetValue()).isEqualByComparingTo("-98.02"),
                () -> assertThat(summary.getGrossValue()).isEqualByComparingTo("-101.121")
        );
    }

    @Test
    void givenCorrectionInvoice_whenCorrectionDoesNotHaveExistingTaxRate_thenVatMatricesAreCorrect() {
        when(unitDict.isEmpty()).thenReturn(false);
        when(unitDict.getLabel(anyString())).thenReturn("godz.");

        when(vatRateDict.getLabel("VAT_05")).thenReturn("5%");
        when(vatRateDict.getColumnValue(eq("VAT_05"), anyString())).thenReturn("0.05");
        when(vatRateDict.getLabel("VAT_23")).thenReturn("23%");
        when(vatRateDict.getColumnValue(eq("VAT_23"), anyString())).thenReturn("0.23");

        when(paymentTypeDict.isEmpty()).thenReturn(false);
        when(paymentTypeDict.getLabel(anyString())).thenReturn("przelew bankowy");

        var originalPositions = List.of(InvoiceUtils.getPosition1(), InvoiceUtils.getPosition2("VAT_23"));
        var correctedPositions = List.of(InvoiceUtils.getCorrectedPosition1(), InvoiceUtils.getCorrectedPosition2("VAT_05"));

        CorrectionInvoiceData invoiceData = new CorrectionInvoiceData(
                InvoiceUtils.getCorrectionInvoice(originalPositions, correctedPositions),
                unitDict, vatRateDict, paymentTypeDict);

        assertOriginalVatMatrixRemoveTaxRate(invoiceData);
        assertCorrectedVatMatrixRemoveTaxRate(invoiceData);
        assertVatMatrixDiffRemoveTaxRate(invoiceData);
    }

    private static void assertOriginalVatMatrixRemoveTaxRate(CorrectionInvoiceData invoiceData) {
        VatMatrix vatMatrix = invoiceData.getOriginalVatMatrix();

        assertEquals(2, vatMatrix.vatRatesList().size());

        List<VatRowData> vatRates = vatMatrix.vatRatesList();
        VatRowData vatRate0 = vatRates.get(0);
        assertAll(
                "Original VAT 23%",
                () -> assertThat(vatRate0.getVat()).isEqualByComparingTo("23"),
                () -> assertEquals("23%", vatRate0.getVatRate()),
                () -> assertThat(vatRate0.getNetValue()).isEqualByComparingTo("100"),
                () -> assertThat(vatRate0.getGrossValue()).isEqualByComparingTo("123")
        );

        VatRowData vatRate1 = vatRates.get(1);
        assertAll(
                "Original VAT 5%",
                () -> assertThat(vatRate1.getVat()).isEqualByComparingTo("0.5"),
                () -> assertEquals("5%", vatRate1.getVatRate()),
                () -> assertThat(vatRate1.getNetValue()).isEqualByComparingTo("10"),
                () -> assertThat(vatRate1.getGrossValue()).isEqualByComparingTo("10.5")
        );

        VatRowData summary = vatMatrix.summary();
        assertAll(
                "Original VAT summary",
                () -> assertThat(summary.getVat()).isEqualByComparingTo("23.5"),
                () -> assertNull(summary.getVatRate()),
                () -> assertThat(summary.getNetValue()).isEqualByComparingTo("110"),
                () -> assertThat(summary.getGrossValue()).isEqualByComparingTo("133.5")
        );
    }

    private static void assertCorrectedVatMatrixRemoveTaxRate(CorrectionInvoiceData invoiceData) {
        VatMatrix vatMatrix = invoiceData.getCorrectedVatMatrix();

        assertEquals(1, vatMatrix.vatRatesList().size());

        List<VatRowData> vatRates = vatMatrix.vatRatesList();
        VatRowData vatRate = vatRates.get(0);
        assertAll(
                "Corrected VAT 5%",
                () -> assertThat(vatRate.getVat()).isEqualByComparingTo("0.599"),
                () -> assertEquals("5%", vatRate.getVatRate()),
                () -> assertThat(vatRate.getNetValue()).isEqualByComparingTo("11.98"),
                () -> assertThat(vatRate.getGrossValue()).isEqualByComparingTo("12.579")
        );

        VatRowData summary = vatMatrix.summary();
        assertAll(
                "Corrected VAT summary",
                () -> assertThat(summary.getVat()).isEqualByComparingTo("0.599"),
                () -> assertNull(summary.getVatRate()),
                () -> assertThat(summary.getNetValue()).isEqualByComparingTo("11.98"),
                () -> assertThat(summary.getGrossValue()).isEqualByComparingTo("12.579")
        );
    }

    private static void assertVatMatrixDiffRemoveTaxRate(CorrectionInvoiceData invoiceData) {
        VatMatrix vatMatrix = invoiceData.getVatMatrixDiff();

        assertEquals(2, vatMatrix.vatRatesList().size());

        List<VatRowData> vatRates = vatMatrix.vatRatesList();
        VatRowData vatRate0 = vatRates.get(0);
        assertAll(
                "VAT 23% difference",
                () -> assertThat(vatRate0.getVat()).isEqualByComparingTo("-23"),
                () -> assertEquals("23%", vatRate0.getVatRate()),
                () -> assertThat(vatRate0.getNetValue()).isEqualByComparingTo("-100"),
                () -> assertThat(vatRate0.getGrossValue()).isEqualByComparingTo("-123")
        );

        VatRowData vatRate1 = vatRates.get(1);
        assertAll(
                "VAT 5% difference",
                () -> assertThat(vatRate1.getVat()).isEqualByComparingTo("0.099"),
                () -> assertEquals("5%", vatRate1.getVatRate()),
                () -> assertThat(vatRate1.getNetValue()).isEqualByComparingTo("1.98"),
                () -> assertThat(vatRate1.getGrossValue()).isEqualByComparingTo("2.0790")
        );

        VatRowData summary = vatMatrix.summary();
        assertAll(
                "VAT summary difference",
                () -> assertThat(summary.getVat()).isEqualByComparingTo("-22.901"),
                () -> assertNull(summary.getVatRate()),
                () -> assertThat(summary.getNetValue()).isEqualByComparingTo("-98.02"),
                () -> assertThat(summary.getGrossValue()).isEqualByComparingTo("-120.921")
        );
    }
}