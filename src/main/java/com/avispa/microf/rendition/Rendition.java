package com.avispa.microf.rendition;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Rafał Hiszpański
 */
public class Rendition {
    private static final Logger log = LoggerFactory.getLogger(Rendition.class);

    public static final String RENDITION_EXT = "pdf";

    /**
     * Generate PDF rendition based on DOCX input file
     * @param inputPath
     * @param outputPath
     */
    public void generateRendition(String inputPath, String outputPath) {
        log.debug("Requested PDF rendition");
        try  {
            InputStream docxInputStream = new FileInputStream(inputPath);
            OutputStream outputStream = new FileOutputStream(outputPath);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
            log.debug("PDF rendition generated succesfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
