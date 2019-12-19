package com.avispa.microf.gui;

import com.avispa.microf.InputParameters;
import com.avispa.microf.invoice.InvoiceFile;
import com.avispa.microf.MicroF;
import com.avispa.microf.util.FormatUtils;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

/**
 * @author Rafał Hiszpański
 */
public class MainPanel {
    private static final DateLabelFormatter DATE_LABEL_FORMATTER = new DateLabelFormatter();
    private static final Logger log = LoggerFactory.getLogger(MicroF.class);

    private JSpinner serialNumber;
    private JDatePickerImpl invoiceDate;
    public JPanel panel;
    private JDatePickerImpl serviceDate;
    private JFormattedTextField value;
    private JLabel serialNumberLabel;
    private JLabel invoiceDateLabel;
    private JLabel serviceDateLabel;
    private JLabel valueLabel;
    private JButton generate;
    private JButton generateAndPrint;

    public MainPanel() {
        generate.addActionListener(event -> processInvoice(false));
        generateAndPrint.addActionListener(e -> processInvoice(true));
    }

    private void processInvoice(boolean print) {
        try (InvoiceFile invoiceFile = new InvoiceFile(getInputs())){
            invoiceFile.generate();
            invoiceFile.save();
            if(print) {
                invoiceFile.print();
            }
        } catch(FileNotFoundException e){
            log.error("Cannot open template file", e);
        } catch(IOException e){
            log.error("Exception during invoice generation", e);
        } catch (PrinterException e) {
            log.error("Exception while printing", e);
        }
    }

    @NotNull
    private InputParameters getInputs() {
        InputParameters input = new InputParameters();
        input.setSerialNumber((Integer)serialNumber.getValue());
        input.setInvoiceDate(convertToLocalDateViaInstant((Date) invoiceDate.getModel().getValue()));
        input.setServiceDate(convertToLocalDateViaInstant((Date) serviceDate.getModel().getValue()));
        input.setValue(new BigDecimal(value.getText().replace(",",".")));
        return input;
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void createUIComponents() {
        invoiceDate = createDatePicker();
        serviceDate = createDatePicker();

        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);
        serialNumber = new JSpinner(model);
        serialNumber.setPreferredSize(new Dimension(120,25));

        value = new JFormattedTextField();
        value.setFormatterFactory(new DefaultFormatterFactory(
                new NumberFormatter(FormatUtils.getDecimalFormatter())));
        value.setPreferredSize(new Dimension(120,25));
        value.setValue(0);
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl picker = new JDatePickerImpl(datePanel, DATE_LABEL_FORMATTER);
        picker.setPreferredSize(new Dimension(120,25));

        return picker;
    }
}
