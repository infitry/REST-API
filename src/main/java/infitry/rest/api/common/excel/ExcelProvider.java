package infitry.rest.api.common.excel;

import infitry.rest.api.configuration.aop.timer.Timer;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ExcelProvider {

    private static final String FILE_EXTENSION = ".xlsx";
    private static final String FILE_CONTENT_TYPE = MediaType.APPLICATION_OCTET_STREAM_VALUE;
    protected static final int MAX_ROW_SIZE = 1000000;

    @Timer
    public void excelDownload(HttpServletResponse response, final String fileName) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName + FILE_EXTENSION);
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

    protected <T> List<List<T>> createPartition(List<T> collection) {
        return IntStream.rangeClosed(0, (collection.size() - 1) / MAX_ROW_SIZE)
                .mapToObj(i -> collection.subList(i * MAX_ROW_SIZE,
                        Math.min((i + 1) * MAX_ROW_SIZE, collection.size())))
                .collect(Collectors.toList());
    }
}
