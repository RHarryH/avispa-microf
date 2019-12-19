package com.avispa.microf.invoice.replacer;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.PositionInParagraph;
import org.apache.poi.xwpf.usermodel.TextSegment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.List;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public class DocxReplacer implements ITemplateReplacer{
    private static final String VARIABLE_TEMPLATE = "${%s}";
    private XWPFDocument document;

    public DocxReplacer(XWPFDocument document) {
        this.document = document;
    }

    @Override
    public void replaceVariables(Map<String, String> variables){
        // replace all headers
        for (XWPFHeader header : document.getHeaderList()) {
            replaceAllBodyElements(header.getBodyElements(), variables);
        }

        // replace body
        replaceAllBodyElements(document.getBodyElements(), variables);

        // replace all footers
        for (XWPFFooter footer : document.getFooterList()) {
            replaceAllBodyElements(footer.getBodyElements(), variables);
        }
    }

    private void replaceAllBodyElements(List<IBodyElement> bodyElements, Map<String, String> variables){
        for (IBodyElement bodyElement : bodyElements) {
            replaceBodyElement(variables, bodyElement);
        }
    }

    private void replaceTable(XWPFTable table, Map<String, String> variables) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                for (IBodyElement bodyElement : cell.getBodyElements()) {
                    replaceBodyElement(variables, bodyElement);
                }
            }
        }
    }

    /**
     * Replace body element. If the body element is paragraph go to final processing. If the body
     * element is table, go to table processing.
     * @param variables
     * @param bodyElement
     */
    private void replaceBodyElement(Map<String, String> variables, IBodyElement bodyElement) {
        if (bodyElement.getElementType().compareTo(BodyElementType.PARAGRAPH) == 0) {
            replaceParagraph((XWPFParagraph) bodyElement, variables);
        }
        if (bodyElement.getElementType().compareTo(BodyElementType.TABLE) == 0) {
            replaceTable((XWPFTable) bodyElement, variables);
        }
    }

    /**
     * Replace values found on paragraph level. This method finds variables in ${} format even if
     * they span multiple runs. Requires variables names with ${}. Covers cases where variable symbols are
     * in the middle of run.
     * @param p
     * @param variables
     */
    private long replaceParagraph(XWPFParagraph p, Map<String, String> variables) {
        long count = 0;
        List<XWPFRun> runs = p.getRuns();

        for (Map.Entry<String, String> variable : variables.entrySet()) {
            String name = String.format(VARIABLE_TEMPLATE, variable.getKey());
            String value = variable.getValue();
            TextSegment found = p.searchText(name, new PositionInParagraph());
            if (found != null) {
                count++;
                if ( found.getBeginRun() == found.getEndRun() ) {
                    // whole search string is in one Run
                    XWPFRun run = runs.get(found.getBeginRun());
                    String runText = run.getText(run.getTextPosition());
                    String replaced = runText.replace(name, value);
                    run.setText(replaced, 0);
                } else {
                    // The search string spans over more than one Run
                    // Put the Strings together
                    StringBuilder b = new StringBuilder();
                    for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
                        XWPFRun run = runs.get(runPos);
                        b.append(run.getText(run.getTextPosition()));
                    }
                    String connectedRuns = b.toString();
                    String replaced = connectedRuns.replace(name, value);

                    // The first Run receives the replaced String of all connected Runs
                    XWPFRun partOne = runs.get(found.getBeginRun());
                    partOne.setText(replaced, 0);
                    // Removing the text in the other Runs.
                    for (int runPos = found.getBeginRun() + 1; runPos <= found.getEndRun(); runPos++) {
                        XWPFRun partNext = runs.get(runPos);
                        partNext.setText("", 0);
                    }
                }
            }
        }

        return count;
    }

    private String convertToVariable(String name) {
        return String.format(VARIABLE_TEMPLATE, name);
    }
}
