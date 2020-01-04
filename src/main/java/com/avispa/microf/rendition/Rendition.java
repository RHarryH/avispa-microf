package com.avispa.microf.rendition;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.document.DocumentFormat;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeManager;
import org.jodconverter.office.OfficeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
public class Rendition {
    private static final Logger log = LoggerFactory.getLogger(Rendition.class);

    public static final String RENDITION_EXT = "pdf";

    private OfficeManager officeManager;

    public Rendition() {
        officeManager = LocalOfficeManager.builder()
                .officeHome("D:\\LibreOffice")
                .processManager("org.jodconverter.process.PureJavaProcessManager")
                .build();
    }

    /**
     * Generate PDF rendition based on the input file
     * @param inputPath
     * @param outputPath
     */
    public void generateRendition(String inputPath, String outputPath) {
        log.debug("Requested PDF rendition");

        String extension = FilenameUtils.getExtension(inputPath);

        try  {
            InputStream inputStream = new FileInputStream(inputPath);
            OutputStream outputStream = new FileOutputStream(outputPath);
            switch(extension) {
                case "docx": { // does not require soffice
                    IConverter converter = LocalConverter.builder().build();
                    converter.convert(inputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
                }
                    break;
                case "odt":
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

                    break;
            }

            inputStream.close();
            outputStream.close();

            log.debug("PDF rendition generated succesfully");
        } catch (Exception e) {
            log.error("PDF rendition cannot be generated", e);
        }
    }

    protected List<String> execute(String[] cmdarray) throws IOException {
        log.info("Process executing: " + Arrays.toString(cmdarray));
        Process process = Runtime.getRuntime().exec(cmdarray);;

        log.info("A");

        try {
            final int exitValue = process.waitFor();
            if (exitValue == 0) {
                System.out.println("Successfully executed the command");

                //try (final BufferedReader b = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                try (BufferedReader b = new BufferedReader(Channels.newReader(Channels.newChannel(process.getInputStream()), "UTF-8"))) {
                    String line;
                    if ((line = b.readLine()) != null)
                        System.out.println(line);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to execute the following command due to the following error(s):");
                try (final BufferedReader b = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    if ((line = b.readLine()) != null)
                        System.out.println(line);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //LinesPumpStreamHandler streamsHandler = new LinesPumpStreamHandler(process.getInputStream(), process.getErrorStream());
        //streamsHandler.start();
//
        //try {
        //    process.waitFor();
        //    streamsHandler.stop();
        //} catch (InterruptedException var7) {
        //    log.info("The current thread was interrupted while waiting for command execution output.", var7);
        //    Thread.currentThread().interrupt();
        //}
        //log.info("B");
//
        //List<String> outLines = streamsHandler.getOutputPumper().getLines();
        //if (log.isDebugEnabled()) {
        //    String out = this.buildOutput(outLines);
        //    String err = this.buildOutput(streamsHandler.getErrorPumper().getLines());
        //    if (!StringUtils.isBlank(out)) {
        //        log.info("Command Output: {}", out);
        //    }
//
        //    if (!StringUtils.isBlank(err)) {
        //        log.info("Command Error: {}", err);
        //    }
        //}

        //log.info("C" + outLines + " " + streamsHandler.getErrorPumper().getLines());
        //return outLines;
        return Collections.EMPTY_LIST;
    }

    private String buildOutput(List<String> lines) {
        Objects.requireNonNull(lines, "lines must not be null");
        return lines.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining("\n"));
    }
}
