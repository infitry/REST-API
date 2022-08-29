package infitry.rest.api.common.excel;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
public abstract class ExcelProvider {

    private static final String FILE_EXTENSION = ".xlsx";
    private static final String FILE_CONTENT_TYPE = "application/octet-stream";

    public void excelDownload(HttpServletResponse response, final String fileName) {
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + FILE_EXTENSION);
        response.setContentType(FILE_CONTENT_TYPE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            workbook.setCompressTempFiles(true);
            createRow(workbook);
            workbook.write(outputStream);
            IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 생성 중 에러가 발생하였습니다.");
        }
    }
    protected abstract void createRow(SXSSFWorkbook workbook);
}
