package infitry.rest.api.util;

import infitry.rest.api.exception.ServiceException;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@UtilityClass
public class ExcelUtil {

    public ByteArrayInputStream createListToExcel(List<T> excelHeader, List<T> excelTestList) {
        ByteArrayOutputStream outputStream;
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("sheet1");

            // 헤더 생성
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("header1");

            // 내용 생성
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("cell0");
            dataRow.createCell(1).setCellValue("cell1");
            dataRow.createCell(2).setCellValue("cell2");
            dataRow.createCell(3).setCellValue("cell3");

            outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new ServiceException("엑셀 파일 생성 중 에러가 발생하였습니다.");
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}