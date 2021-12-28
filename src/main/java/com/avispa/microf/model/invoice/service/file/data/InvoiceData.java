package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.microf.constants.VatRate;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.numeral.NumeralToStringConverter;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
@Getter
public class InvoiceData {
    private final String invoiceName;
    private final String seller;
    private final String buyer;

    private final LocalDate invoiceDate;
    private final LocalDate serviceDate;

    private final PositionData[] positions;
    private Map<VatRate, VatRowData> vatMatrix;
    private VatRowData vatSum;

    private String grossValueInWords;
    private LocalDate paymentDate;

    private final String comments;

    public InvoiceData(Invoice invoice) {
        this.invoiceName = invoice.getObjectName();

        this.seller = invoice.getSeller().format();
        this.buyer = invoice.getBuyer().format();

        this.invoiceDate = invoice.getInvoiceDate();
        this.serviceDate = invoice.getServiceDate();

        positions = new PositionData[invoice.getPositions().size()];
        for(int i = 0; i < positions.length; i++) {
            positions[i] = new PositionData(invoice.getPositions().get(i), i);
        }

        setVatMatrix();
        setVatSum();
        setGrossValueInWords(this.vatSum.getGrossValue());

        this.comments = invoice.getComments();

        setPaymentDate();
    }

    private void setVatMatrix() {
        this.vatMatrix = new EnumMap<>(VatRate.class);
        for(PositionData position : this.positions) {
            VatRate vatRate = position.getVatRate();
            VatRowData vatRowData = getVatRow(this.vatMatrix, vatRate);

            vatRowData.setNetValue(vatRowData.getNetValue().add(position.getNetValue()));
            vatRowData.setVat(vatRowData.getVat().add(position.getVat()));
            vatRowData.setGrossValue(vatRowData.getGrossValue().add(position.getGrossValue()));
        }
    }

    private VatRowData getVatRow(Map<VatRate, VatRowData> vatMatrix, VatRate vatRate) {
        VatRowData vatRowData;

        if(vatMatrix.containsKey(vatRate)) {
            vatRowData = vatMatrix.get(vatRate);
        } else {
            vatRowData = new VatRowData();
            vatMatrix.put(vatRate, vatRowData);
        }
        return vatRowData;
    }

    private void setVatSum() {
        this.vatSum = new VatRowData();

        for(VatRowData vatRowData : this.vatMatrix.values()) {
            this.vatSum.setNetValue(this.vatSum.getNetValue().add(vatRowData.getNetValue()));
            this.vatSum.setVat(this.vatSum.getVat().add(vatRowData.getVat()));
            this.vatSum.setGrossValue(this.vatSum.getGrossValue().add(vatRowData.getGrossValue()));
        }
    }

    private void setGrossValueInWords(BigDecimal grossValueSum) {
        this.grossValueInWords = NumeralToStringConverter.convert(grossValueSum);
    }

    public void setPaymentDate() {
        this.paymentDate = this.invoiceDate.plusDays(14);
    }
}
