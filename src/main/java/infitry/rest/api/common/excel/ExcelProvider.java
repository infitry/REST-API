package infitry.rest.api.common.excel;

import infitry.rest.api.exception.ServiceException;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
public abstract class ExcelProvider {

    private static final String FILE_EXTENSION = ".xlsx";
    private static final String FILE_CONTENT_TYPE = "application/octet-stream";

    public void excelDownload(HttpServletResponse response, final String fileName) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + FILE_EXTENSION);
        response.setContentType(FILE_CONTENT_TYPE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            workbook.setCompressTempFiles(true);
            createRow(workbook);
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 생성 중 에러가 발생하였습니다.");
        }
        IOUtils.copy(new ByteArrayInputStream(outputStream.toByteArray()), response.getOutputStream());
    }
    protected abstract void createRow(SXSSFWorkbook workbook);
}
