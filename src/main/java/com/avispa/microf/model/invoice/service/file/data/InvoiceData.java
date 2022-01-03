package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.microf.constants.VatRate;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.numeral.NumeralToStringConverter;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Rafał Hiszpański
 */
@Getter
public class InvoiceData {
    private final String invoiceName;
    private final String seller;
    private final String buyer;

    private final LocalDate issueDate;
    private final LocalDate serviceDate;

    private final List<PositionData> positions;
    private final List<VatRowData> vatMatrix;

    private String grossValueInWords;
    private LocalDate paymentDate;

    private final String comments;

    public InvoiceData(Invoice invoice) {
        this.invoiceName = invoice.getObjectName();

        this.seller = invoice.getSeller().format();
        this.buyer = invoice.getBuyer().format();

        this.issueDate = invoice.getIssueDate();
        this.serviceDate = invoice.getServiceDate();

        this.positions = invoice.getPositions().stream().map(PositionData::new).collect(Collectors.toList());

        this.vatMatrix = generateVatMatrix();
        setGrossValueInWords(getVatSum().getGrossValue());

        this.comments = invoice.getComments();

        setPaymentDate();
    }

    private List<VatRowData> generateVatMatrix() {
        Map<VatRate, VatRowData> vatMatrixMap = new EnumMap<>(VatRate.class);
        for(PositionData position : this.positions) {
            VatRate vatRate = position.getVatRate();
            VatRowData vatRowData = getVatRow(vatMatrixMap, vatRate);

            vatRowData.setVatRate(vatRate);
            vatRowData.setNetValue(vatRowData.getNetValue().add(position.getNetValue()));
            vatRowData.setVat(vatRowData.getVat().add(position.getVat()));
            vatRowData.setGrossValue(vatRowData.getGrossValue().add(position.getGrossValue()));
        }

        VatRowData vatSum = getVatSum(vatMatrixMap);

        return Stream.concat(
                vatMatrixMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue),
                        Stream.of(vatSum))
                .collect(Collectors.toList());
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

    private VatRowData getVatSum(Map<VatRate, VatRowData> vatMatrixMap) {
        VatRowData vatSum = new VatRowData();

        for(VatRowData vatRowData : vatMatrixMap.values()) {
            vatSum.accumulate(
                    vatRowData.getNetValue(),
                    vatRowData.getVat(),
                    vatRowData.getGrossValue());
        }

        return vatSum;
    }

    public VatRowData getVatSum() {
        return vatMatrix.get(vatMatrix.size() - 1);
    }

    private void setGrossValueInWords(BigDecimal grossValueSum) {
        this.grossValueInWords = NumeralToStringConverter.convert(grossValueSum);
    }

    public void setPaymentDate() {
        this.paymentDate = this.issueDate.plusDays(14);
    }
}
