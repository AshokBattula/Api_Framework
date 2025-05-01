package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ExcelLogger} class provides functionality to log API test results
 * and generate an Excel (.xlsx) report containing the logs.
 * <p>
 * It uses Apache POI to create a well-formatted Excel file with columns:
 * <ul>
 *     <li>API Name</li>
 *     <li>Status Code</li>
 *     <li>Response Time (ms)</li>
 *     <li>Response Body</li>
 * </ul>
 *
 * <p>Typical usage:
 * <pre>
 *     ExcelLogger.log("LoginAPI", 200, 123, "{\"status\":\"success\"}");
 *     ExcelLogger.generateReport("reports");
 * </pre>
 */
public class ExcelLogger {

    /**
     * Internal list to store all log entries.
     * Each entry is an Object array representing a row in the Excel sheet.
     */
    private static List<Object[]> logData = new ArrayList<>();

    /**
     * Logs a single API test result to the internal data structure.
     *
     * @param apiName      the name or endpoint of the API being tested
     * @param statusCode   the HTTP status code received from the response
     * @param responseTime the time taken for the API call in milliseconds
     * @param responseBody the body of the API response
     */
    public static void log(String apiName, int statusCode, long responseTime, String responseBody) {
        logData.add(new Object[]{apiName, statusCode, responseTime, responseBody});
    }

    /**
     * Generates an Excel report file containing all logged API test results.
     * The report is created under the specified folder with the name {@code APITestReport.xlsx}.
     * The method includes column headers, bold formatting for headers, and auto-sized columns.
     *
     * @param folderPath the directory where the Excel file should be saved
     */
    public static void generateReport(String folderPath) {
        String filePath = folderPath + "/APITestReport.xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("API Test Results");
            int rowCount = 0;

            // Define headers
            String[] headers = {"API Name", "Status Code", "Response Time (ms)", "Response Body"};

            // Create header row with styling
            Row header = sheet.createRow(rowCount++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);

                // Apply bold style to header
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                cell.setCellStyle(headerStyle);
            }

            // Add log entries to sheet
            for (Object[] rowData : logData) {
                Row row = sheet.createRow(rowCount++);
                for (int i = 0; i < rowData.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(rowData[i] != null ? rowData[i].toString() : "N/A");
                }
            }

            // Auto-size all columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write workbook to file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }

            System.out.println("✅ Excel report generated at: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
