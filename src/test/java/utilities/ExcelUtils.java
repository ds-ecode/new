package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    public static void writeToExcel(String filePath, String sheetName, List<String[]> data) throws IOException {
        Workbook workbook;
        File file = new File(filePath);

        // Check if file exists to avoid overriding
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            workbook = WorkbookFactory.create(fis);
            fis.close();
        } else {
            workbook = new XSSFWorkbook();
        }

        // Remove sheet if it already exists to prevent "Duplicate sheet" error
        if (workbook.getSheet(sheetName) != null) {
            workbook.removeSheetAt(workbook.getSheetIndex(sheetName));
        }
        
        Sheet sheet = workbook.createSheet(sheetName);

        // Header Styling
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Create Header
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Bike Name", "Price", "Launch Date"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Write Data
        int rowNum = 1;
        for (String[] bike : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(bike[0]);
            row.createCell(1).setCellValue(bike[1]);
            row.createCell(2).setCellValue(bike[2]);
        }

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Save file
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}