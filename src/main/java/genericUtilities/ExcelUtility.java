package genericUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Provides generic utility methods for reading and writing Excel files.
 * <p>
 * Supports reading single cells, getting row/column counts, writing data,
 * and fetching row data as key-value pairs based on a unique identifier.
 * </p>
 * 
 * @author: Bandi Saiteja
 */
public class ExcelUtility {

    private String path;

    /**
     * Initializes the utility with the Excel file path.
     * 
     * @param path The absolute or relative path to the Excel file.
     */
    public ExcelUtility(String path) {
        this.path = path;
    }

    /**
     * Reads data from a specific cell in the Excel sheet.
     * 
     * @param sheetName Name of the sheet.
     * @param rowNumber Row index (0-based).
     * @param cellNumber Column index (0-based).
     * @return Cell value as String.
     * @throws EncryptedDocumentException if the Excel file is encrypted.
     * @throws IOException if file cannot be read.
     */
    public String readDataFromExcel(String sheetName, int rowNumber, int cellNumber) 
            throws EncryptedDocumentException, IOException {
        try (Workbook wb = WorkbookFactory.create(new File(path))) {
            return wb.getSheet(sheetName).getRow(rowNumber).getCell(cellNumber).getStringCellValue();
        }
    }

    /**
     * Returns the index of the last row in the given sheet.
     * 
     * @param sheetName Name of the sheet.
     * @return Last row index (0-based).
     * @throws EncryptedDocumentException if the Excel file is encrypted.
     * @throws IOException if file cannot be read.
     */
    public int getLastRowNumber(String sheetName) throws EncryptedDocumentException, IOException {
        try (Workbook wb = WorkbookFactory.create(new File(path))) {
            return wb.getSheet(sheetName).getLastRowNum();
        }
    }

    /**
     * Returns the number of columns in a specific row.
     * 
     * @param sheetName Name of the sheet.
     * @param rowNumber Row index (0-based).
     * @return Number of columns in the row.
     * @throws EncryptedDocumentException if the Excel file is encrypted.
     * @throws IOException if file cannot be read.
     */
    public int getLastColumnNumber(String sheetName, int rowNumber) throws EncryptedDocumentException, IOException {
        try (Workbook wb = WorkbookFactory.create(new File(path))) {
            return wb.getSheet(sheetName).getRow(rowNumber).getLastCellNum();
        }
    }

    /**
     * Writes a value to a specific cell in the Excel sheet.
     * Creates the row or cell if they do not exist.
     * 
     * @param sheetName Name of the sheet.
     * @param rowNumber Row index (0-based).
     * @param cellNumber Column index (0-based).
     * @param value Value to write.
     * @throws EncryptedDocumentException if the Excel file is encrypted.
     * @throws IOException if file cannot be written.
     */
    public void writeDataBackToExcel(String sheetName, int rowNumber, int cellNumber, String value) 
            throws EncryptedDocumentException, IOException {
        File file = new File(path);
        try (Workbook wb = WorkbookFactory.create(file);
             FileOutputStream fos = new FileOutputStream(file)) {

            Sheet sheet = wb.getSheet(sheetName);
            if (sheet.getRow(rowNumber) == null) {
                sheet.createRow(rowNumber);
            }
            sheet.getRow(rowNumber).createCell(cellNumber).setCellValue(value);
            wb.write(fos);
        }
    }

    /**
     * Retrieves a row's data as a key-value map based on a unique cell value.
     * The map keys are taken from the header row above the matched row.
     * 
     * @param sheetName Name of the sheet.
     * @param uniqueData The unique value to match in the row.
     * @return A Map containing header-value pairs. Returns empty map if not found.
     * @throws EncryptedDocumentException if the Excel file is encrypted.
     * @throws IOException if file cannot be read.
     */
    public Map<String, String> getData(String sheetName, String uniqueData) 
            throws EncryptedDocumentException, IOException {
        Map<String, String> map = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(path);
             Workbook wb = WorkbookFactory.create(fis)) {

            Sheet sheet = wb.getSheet(sheetName);
            int lastRow = sheet.getLastRowNum();

            for (int i = 1; i <= lastRow; i++) {
                Row row = sheet.getRow(i);

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    if (row.getCell(j).getStringCellValue().equals(uniqueData)) {
                        Row header = sheet.getRow(i - 1);
                        Row values = sheet.getRow(i);

                        for (int k = 0; k < header.getLastCellNum(); k++) {
                            map.put(header.getCell(k).getStringCellValue(),
                                    values.getCell(k).getStringCellValue());
                        }
                        return map;
                    }
                }
            }
        }
        return map;
    }
    
    // -------------------- Private Excel Method -------------------- //

    /**
     * Reads data from a private Excel file (optionally password-protected)
     * @param sheetName Excel sheet name
     * @param uniqueData Unique value to match
     * @param password Password for the file, null if none
     * @return Map of header-value pairs
     */
    public Map<String, String> getDataFromPrivateExcel(String sheetName, String uniqueData, String password) 
            throws IOException, EncryptedDocumentException {
        Map<String, String> map = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(path);
             Workbook wb = (password == null || password.isEmpty()) ?
                     WorkbookFactory.create(fis) :
                     WorkbookFactory.create(fis, password)) {

            Sheet sheet = wb.getSheet(sheetName);
            int lastRow = sheet.getLastRowNum();

            for (int i = 1; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    if (row.getCell(j).getStringCellValue().equals(uniqueData)) {
                        Row header = sheet.getRow(i - 1);
                        Row values = sheet.getRow(i);

                        for (int k = 0; k < header.getLastCellNum(); k++) {
                            map.put(header.getCell(k).getStringCellValue(),
                                    values.getCell(k).getStringCellValue());
                        }
                        return map;
                    }
                }
            }
        }

        return map;
    }

    /**
     * Fetches Google Sheet data using public CSV export and returns a key–value map
     * based on a matched unique value.
     *
     * <p>How it works:
     * - Loads the sheet as CSV using spreadsheetId and sheetGid.
     * - Searches each row for the given uniqueData.
     * - The row above the matched row is treated as the header (keys).
     * - The matched row is treated as the values.
     * - Both rows are combined into a LinkedHashMap and returned.
     *
     * @param spreadsheetId Google Sheet ID
     * @param sheetGid Sheet/tab GID
     * @param uniqueData Cell value to search for
     * @return Map of header → row values, or null if uniqueData is not found
     * @throws Exception If sheet cannot be read or header row is missing
     */
    public static Map<String, String> getDataFromGSheet(String spreadsheetId, String sheetGid, String uniqueData) throws Exception {
        String csvUrl = "https://docs.google.com/spreadsheets/d/" + spreadsheetId + "/export?format=csv&gid=" + sheetGid;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(csvUrl).openStream()))) {
            String line;
            String[] previousRow = null;

            while ((line = br.readLine()) != null) {
                String[] currentRow = line.split(",", -1);

                boolean found = false;
                for (String cell : currentRow) {
                    if (cell.equals(uniqueData)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    if (previousRow == null) {
                        throw new Exception("No header row present above the matched row.");
                    }

                    Map<String, String> result = new LinkedHashMap<>();
                    int length = Math.min(previousRow.length, currentRow.length);
                    for (int i = 0; i < length; i++) {
                        result.put(previousRow[i], currentRow[i]);
                    }
                    return result;
                }

                previousRow = currentRow;
            }
        }

        return null;
    }

}
