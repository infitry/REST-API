package infitry.rest.api.common.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class SampleExcel extends ExcelProvider {

    @Override
    protected void createRow(SXSSFWorkbook workbook) {
        workbook.setCompressTempFiles(true);
        Sheet sheet = workbook.createSheet("sheet1");

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("header1");

        // 내용 생성
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("cell0");
        dataRow.createCell(1).setCellValue("cell1");
        dataRow.createCell(2).setCellValue("cell2");
        dataRow.createCell(3).setCellValue("cell3");
    }
}
