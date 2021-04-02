package com.avispa.microf.service.rendition;

import com.avispa.microf.util.Configuration;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.commons.io.FilenameUtils;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.document.DocumentFormat;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.jodconverter.office.OfficeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Rafał Hiszpański
 */
@Service
public class RenditionService {
    private static final Logger log = LoggerFactory.getLogger(RenditionService.class);

    public static final String RENDITION_EXT = "pdf";

    private OfficeManager officeManager;

    public RenditionService() {
        officeManager = LocalOfficeManager.builder()
                .officeHome(Configuration.getInstance().getOfficePath())
                .processManager("org.jodconverter.process.PureJavaProcessManager")
                .build();
    }

    /**
     * Generate PDF rendition based on the input file
     * @param inputPath
     * @param outputPath
     */
    public void generate(String inputPath, String outputPath) {
        log.debug("Requested PDF rendition");

        String extension = FilenameUtils.getExtension(inputPath);

        try  {
            try(InputStream inputStream = new FileInputStream(inputPath);
                OutputStream outputStream = new FileOutputStream(outputPath)) {

                if (Configuration.getInstance().getRenditionOfficeAlways()) {
                    generateUsingSoffice(extension, inputStream, outputStream);
                } else {
                    switch (extension) {
                        case "docx": // does not require soffice
                            IConverter converter = LocalConverter.builder().build();
                            converter.convert(inputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
                        break;
                        case "odt":
                            generateUsingSoffice(extension, inputStream, outputStream);
                            break;
                        default:
                            log.error("Unsupported extension: {}.", extension);

                    }
                }
            }

            log.debug("PDF rendition generated successfully");
        } catch (Exception e) {
            log.error("PDF rendition cannot be generated", e);
        }
    }

    /**
     * Generates pdf rendition using soffice from LibreOffice or OpenOffice using JODConverter helper library
     * @param extension source extension
     * @param inputStream original file stream
     * @param outputStream rendition file stream
     * @throws OfficeException
     */
    private void generateUsingSoffice(String extension, InputStream inputStream, OutputStream outputStream) throws OfficeException {
        final DocumentFormat targetFormat =
                DefaultDocumentFormatRegistry.getFormatByExtension(RENDITION_EXT);

        try {
            officeManager.start();

            org.jodconverter.LocalConverter
                    .make(officeManager)
                    .convert(inputStream)
                    .as(DefaultDocumentFormatRegistry.getFormatByExtension(
                            extension))
                    .to(outputStream)
                    .as(targetFormat)
                    .execute();
        } finally {
            OfficeUtils.stopQuietly(officeManager);
        }
    }
}
