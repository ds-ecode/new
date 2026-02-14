package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    public static void writeToExcel(String filePath, String sheetName, List<String[]> data, String[] headers) throws IOException {
        Workbook workbook;
        File file = new File(filePath);

        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            workbook = WorkbookFactory.create(fis);
            fis.close();
        } else {
            workbook = new XSSFWorkbook();
        }

        if (workbook.getSheet(sheetName) != null) {
            workbook.removeSheetAt(workbook.getSheetIndex(sheetName));
        }
        
        Sheet sheet = workbook.createSheet(sheetName);
        
        // Header Styling (Grey background as per your image)
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);

        // Generate TimeStamp (e.g., Feb 12, 2026 13:23:03)
        String timeStamp = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(new Date());

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Write Data
        int rowNum = 1;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(timeStamp);
            
            for (int i = 1; i < headers.length; i++) {
                // rowData[i] corresponds to the columns after TimeStamp
                if(i < rowData.length) {
                    row.createCell(i).setCellValue(rowData[i]);
                }
            }
        }

        // Auto-size all columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}