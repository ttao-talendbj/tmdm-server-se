package org.talend.mdm.webapp.browserecords.server.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.talend.mdm.webapp.base.server.util.CommonUtil;
import org.talend.mdm.webapp.base.shared.FileUtil;
import org.talend.mdm.webapp.browserecords.server.bizhelpers.ViewHelper;
import org.talend.mdm.webapp.browserecords.server.util.CSVReader;

import com.amalto.core.util.Messages;
import com.amalto.core.util.MessagesFactory;
import com.amalto.webapp.core.bean.Configuration;
import com.amalto.webapp.util.webservices.WSDataClusterPK;
import com.amalto.webapp.util.webservices.WSDataModelPK;
import com.amalto.webapp.util.webservices.WSPutItem;
import com.amalto.webapp.util.webservices.WSPutItemWithReport;

/**
 * 
 * @author asaintguilhem
 * 
 * read excel and csv file
 */

@SuppressWarnings("serial")
public class UploadData extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(UploadData.class);

    private static final Messages MESSAGES = MessagesFactory.getMessages(
            "org.talend.mdm.webapp.browserecords.client.i18n.BrowseRecordsMessages", UploadData.class.getClassLoader()); //$NON-NLS-1$

    public UploadData() {
        super();
    }

    public String getCurrentDataModel() throws Exception {
        Configuration config = Configuration.getConfiguration();
        return config.getModel();
    }

    public String getCurrentDataCluster() throws Exception {
        Configuration config = Configuration.getConfiguration();
        return config.getCluster();
    }

    @Override
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        doPost(arg0, arg1);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FileInputStream fileInputStream = null;
        POIFSFileSystem poiFSFile = null;
        CSVReader csvReader = null;
        String concept = "";//$NON-NLS-1$
        String viewPK = ""; //$NON-NLS-1$
        String fileType = "";//$NON-NLS-1$
        String sep = ",";//$NON-NLS-1$
        String textDelimiter = "\"";//$NON-NLS-1$
        String language = "en"; // default//$NON-NLS-1$
        String encoding = "utf-8";//$NON-NLS-1$
        String header = ""; //$NON-NLS-1$
        String mandatoryField = ""; //$NON-NLS-1$
        boolean cusExceptionFlag = false;

        boolean headersOnFirstLine = false;
        int lineNum = 0;
        response.setCharacterEncoding("UTF-8"); //$NON-NLS-1$
        PrintWriter writer = response.getWriter();

        request.setCharacterEncoding("UTF-8");//$NON-NLS-1$

        try {
            if (!FileUploadBase.isMultipartContent(request)) {
                throw new ServletException(MESSAGES.getMessage("error_upload"));//$NON-NLS-1$
            }
            // Create a new file upload handler
            DiskFileUpload upload = new DiskFileUpload();

            // Set upload parameters
            upload.setSizeThreshold(0);
            upload.setSizeMax(-1);

            // Parse the request
            List items = upload.parseRequest(request);

            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMyy-HHmmssSSS"); //$NON-NLS-1$     

            File file = null;
            // Process the uploaded items
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                // FIXME: should handle more than files in parts e.g. text passed as parameter
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    // we are not expecting any field just (one) file(s)
                    String name = item.getFieldName();
                    LOG.debug("doPost() Field: '" + name + "' - value:'" + item.getString() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    if (name.equals("concept"))//$NON-NLS-1$
                        viewPK = item.getString();
                    if (name.equals("sep"))//$NON-NLS-1$
                        sep = item.getString();
                    if (name.equals("delimiter"))//$NON-NLS-1$
                        textDelimiter = item.getString();
                    if (name.equals("language"))//$NON-NLS-1$
                        language = item.getString();
                    if (name.equals("encodings"))//$NON-NLS-1$
                        encoding = item.getString();
                    if (name.equals("header"))//$NON-NLS-1$
                        header = item.getString();
                    if (name.equals("mandatoryField"))//$NON-NLS-1$
                        mandatoryField = item.getString();
                    if (name.equals("headersOnFirstLine"))//$NON-NLS-1$
                        headersOnFirstLine = "on".equals(item.getString());//$NON-NLS-1$
                } else {
                    fileType = FileUtil.getFileType(item.getName());
                    file = File.createTempFile("upload", "tmp");//$NON-NLS-1$//$NON-NLS-2$
                    LOG.debug("doPost() data uploaded in " + file.getAbsolutePath()); //$NON-NLS-1$
                    file.deleteOnExit();
                    item.write(file);
                }// if field
            }// while item

            Locale locale = new Locale(language);
            concept = ViewHelper.getConceptFromDefaultViewName(viewPK);
            String[] fields = header.split("@"); //$NON-NLS-1$
            Set<String> mandatorySet = chechMandatoryField(mandatoryField, fields);

            if (mandatorySet.size() > 0) {
                cusExceptionFlag = true;
                throw new ServletException(MESSAGES.getMessage(locale, "error_missing_mandatory_field")); //$NON-NLS-1$
            }

            fileInputStream = new FileInputStream(file);
            List<String> xmlRecords = new ArrayList<String>();

            if ("xls".equals(fileType.toLowerCase()) || "xlsx".equals(fileType.toLowerCase())) {//$NON-NLS-1$ //$NON-NLS-2$
                Workbook wb;
                if ("xls".equals(fileType.toLowerCase())) { //$NON-NLS-1$
                    poiFSFile = new POIFSFileSystem(fileInputStream);
                    wb = new HSSFWorkbook(poiFSFile);
                } else {
                    wb = new XSSFWorkbook(new FileInputStream(file));
                }
                Sheet sh = wb.getSheetAt(0);
                Iterator<Row> it = sh.rowIterator();

                Map<String, Integer> headerIndex = null;
                while (it.hasNext()) {
                    Row row = (Row) it.next();
                    int count = row.getPhysicalNumberOfCells();

                    if (++lineNum == 1 && headersOnFirstLine) {
                        headerIndex = getHeaderIndex(row, header);
                        if (headerIndex.size() != fields.length) {
                            cusExceptionFlag = true;
                            throw new ServletException(MESSAGES.getMessage(locale,
                                    "error_column_header", fields.length, headerIndex.size())); //$NON-NLS-1$
                        }
                        continue;
                    }

                    if (fields.length != count) {
                        cusExceptionFlag = true;
                        throw new ServletException(MESSAGES.getMessage(locale,
                                "error_column_width", fields.length, count, lineNum)); //$NON-NLS-1$
                    }

                    StringBuffer xml = new StringBuffer();
                    boolean allCellsEmpty = true;
                    xml.append("<" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$
                    for (int i = 0; i < fields.length; i++) {
                        Cell tmpCell = null;
                        if (headersOnFirstLine) {
                            String fieldName = fields[i].split(":")[0]; //$NON-NLS-1$
                            tmpCell = row.getCell((short) headerIndex.get(fieldName).intValue());
                        } else {
                            tmpCell = row.getCell((short) i);
                        }

                        String fieldName = fields[i].split(":")[0]; //$NON-NLS-1$
                        boolean visible = Boolean.valueOf(fields[i].split(":")[1]); //$NON-NLS-1$
                        if (tmpCell != null) {
                            xml.append("<" + fieldName + ">");//$NON-NLS-1$//$NON-NLS-2$
                            int cellType = tmpCell.getCellType();
                            String cellValue = "";//$NON-NLS-1$
                            switch (cellType) {
                            case Cell.CELL_TYPE_NUMERIC: {
                                double tmp = tmpCell.getNumericCellValue();
                                cellValue = getStringRepresentation(tmp);
                                break;
                            }
                            case Cell.CELL_TYPE_STRING: {
                                cellValue = tmpCell.getRichStringCellValue().getString();
                                int result = org.talend.mdm.webapp.browserecords.server.util.CommonUtil.isFKFormat(cellValue);
                                if(result > 0){
                                    cellValue = org.talend.mdm.webapp.browserecords.server.util.CommonUtil.getForrignKeyId(cellValue, result);
                                }
                                break;
                            }
                            case Cell.CELL_TYPE_BOOLEAN: {
                                boolean tmp = tmpCell.getBooleanCellValue();
                                if (tmp)
                                    cellValue = "true";//$NON-NLS-1$
                                else
                                    cellValue = "false";//$NON-NLS-1$
                                break;
                            }
                            case Cell.CELL_TYPE_FORMULA: {
                                cellValue = tmpCell.getCellFormula();
                                break;
                            }
                            case Cell.CELL_TYPE_ERROR: {
                                break;
                            }
                            case Cell.CELL_TYPE_BLANK: {
                            }
                            default: {
                            }
                            }

                            if (cellValue != null && !"".equals(cellValue))//$NON-NLS-1$
                                allCellsEmpty = false;
                            if (visible) {
                                xml.append(StringEscapeUtils.escapeXml(cellValue));
                            }
                            xml.append("</" + fieldName + ">");//$NON-NLS-1$//$NON-NLS-2$
                        } else {
                            xml.append("<" + fieldName + "/>"); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                    }

                    xml.append("</" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$
                    // put document (except empty lines)
                    if (!allCellsEmpty)
                        xmlRecords.add(xml.toString());
                }
            } else if ("csv".equals(fileType.toLowerCase())) { //$NON-NLS-1$
                Map<String, Integer> headerIndex = null;
                char separator = ',';
                if ("semicolon".equals(sep))//$NON-NLS-1$
                    separator = ';';
                csvReader = new CSVReader(new InputStreamReader(fileInputStream, encoding), separator, textDelimiter.charAt(0));
                List<String[]> records = csvReader.readAll();
                for (int i = 0; i < records.size(); i++) {
                    String[] record = records.get(i);

                    if (++lineNum == 1 && headersOnFirstLine) {
                        headerIndex = getHeaderIndex(record, separator, header);
                        if (headerIndex.size() != fields.length) {
                            throw new ServletException(MESSAGES.getMessage(locale,
                                    "error_column_header", fields.length, headerIndex.size())); //$NON-NLS-1$ 
                        }
                        continue;
                    }

                    if (fields.length != record.length) {
                        cusExceptionFlag = true;
                        throw new ServletException(MESSAGES.getMessage(locale,
                                "error_column_width", fields.length, record.length, lineNum)); //$NON-NLS-1$
                    }

                    StringBuffer xml = new StringBuffer();
                    xml.append("<" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$            

                    // build xml
                    if (record.length > 0) {
                        for (int j = 0; j < fields.length; j++) {
                            String fieldName = fields[j].split(":")[0]; //$NON-NLS-1$
                            boolean visible = Boolean.valueOf(fields[j].split(":")[1]); //$NON-NLS-1$
                            xml.append("<" + fieldName + ">");//$NON-NLS-1$//$NON-NLS-2$
                            if (visible) {
                                if (headersOnFirstLine) {
                                    xml.append(StringEscapeUtils.escapeXml(record[headerIndex.get(fieldName)]));
                                } else {
                                    if (j < record.length)
                                        xml.append(StringEscapeUtils.escapeXml(record[j]));
                                }
                            }
                            xml.append("</" + fieldName + ">");//$NON-NLS-1$//$NON-NLS-2$
                        }
                    }
                    xml.append("</" + concept + ">");//$NON-NLS-1$//$NON-NLS-2$
                    xmlRecords.add(xml.toString());
                    LOG.debug("Added line " + lineNum);//$NON-NLS-1$
                    LOG.trace("--val:\n" + xml);//$NON-NLS-1$
                }
            }
            for (int i = 0; i < xmlRecords.size(); i++) {
                String xml = xmlRecords.get(i);
                putDocument(xml, language, concept, getCurrentDataModel(), getCurrentDataCluster());
            }
            writer.print("true");//$NON-NLS-1$
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            if (cusExceptionFlag) {
                writer.print(e.getMessage());
                throw (ServletException) e;
            } else {
                writer.print(MESSAGES.getMessage("error_import", lineNum, e.getClass().getName(), e//$NON-NLS-1$
                        .getLocalizedMessage()));
                throw new ServletException(MESSAGES.getMessage("error_import", lineNum, e.getClass().getName(), e//$NON-NLS-1$
                        .getLocalizedMessage()));
            }
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            writer.close();
        }
    }

    private Map<String, Integer> getHeaderIndex(String[] headerString, char separator, String header) {
        Map<String, Integer> fieldIndex = new HashMap<String, Integer>();
        for (int i = 0; i < headerString.length; i++) {
            String fieldName = headerString[i];
            if (header.contains(fieldName)) {
                fieldIndex.put(fieldName, i);
            }
        }
        return fieldIndex;
    }

    private Map<String, Integer> getHeaderIndex(Row headerRow, String header) {
        Map<String, Integer> fieldIndex = new HashMap<String, Integer>();
        Iterator<Cell> iter = headerRow.cellIterator();
        int i = 0;
        while (iter.hasNext()) {
            Cell cell = (Cell) iter.next();
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                String fieldName = cell.getRichStringCellValue().getString();
                if (header.contains(fieldName)) {
                    fieldIndex.put(fieldName, i);
                    i++;
                }
            }
        }
        return fieldIndex;
    }

    private Set<String> chechMandatoryField(String mandatoryField, String[] fields) {

        String[] mandatoryFields = mandatoryField.split("@"); //$NON-NLS-1$
        Set<String> mandatorySet = new HashSet<String>();
        for (String field : mandatoryFields)
            mandatorySet.add(field);

        for (String str : fields) {
            String fieldName = str.split(":")[0]; //$NON-NLS-1$
            if (mandatorySet.contains(fieldName))
                mandatorySet.remove(fieldName);
        }

        return mandatorySet;
    }

    private static void putDocument(String xml, String language, String concept, String model, String cluster)
            throws ServletException {
        try {
            WSPutItemWithReport wsPutItemWithReport = new WSPutItemWithReport(new WSPutItem(new WSDataClusterPK(cluster), xml,
                    new WSDataModelPK(model), false), "genericUI", true); //$NON-NLS-1$
            CommonUtil.getPort().putItemWithReport(wsPutItemWithReport);

        } catch (RemoteException e) {
            String err = MESSAGES.getMessage("save_fail", e.getMessage()); //$NON-NLS-1$ 
            if (e.getMessage() != null && e.getMessage().length() > 0) {
                err = e.getMessage();
            }

            throw new ServletException(err);
        } catch (Exception e) {
            throw new ServletException(e.getLocalizedMessage());
        }
    }

    /*
     * Returns a string corresponding to the double value given in parameter Exponent is removed and "0" are added at
     * the end of the string if necessary This method is useful when you import long itemid that you don't want to see
     * modified by importation method.
     */
    private String getStringRepresentation(double value) {
        String result = ""; //$NON-NLS-1$

        result = Double.toString(value);

        int index = result.indexOf("E");//$NON-NLS-1$

        String base = result;

        if (index > 0) {
            try {
                base = result.substring(0, index);
                String puissance = result.substring(index + 1);

                int puissanceValue = Integer.parseInt(puissance);

                int indexPoint = base.indexOf(".");//$NON-NLS-1$

                if (indexPoint > 0) {
                    String beforePoint = base.substring(0, indexPoint);
                    String afterPoint = base.substring(indexPoint + 1);

                    if (puissanceValue >= afterPoint.length()) {
                        base = beforePoint + "" + afterPoint;//$NON-NLS-1$
                        puissanceValue -= afterPoint.length();
                    } else {
                        String newBeforePoint = beforePoint + "" + afterPoint.substring(0, puissanceValue);//$NON-NLS-1$
                        String newAfterPoint = afterPoint.substring(puissanceValue);
                        base = newBeforePoint + "." + newAfterPoint;//$NON-NLS-1$
                        puissanceValue = 0;
                    }
                }

                for (int j = 0; j < puissanceValue; j++) {
                    base += "0";//$NON-NLS-1$
                }

                result = base;

            } catch (NumberFormatException e) {
            }
        }
        return result;
    }
}
