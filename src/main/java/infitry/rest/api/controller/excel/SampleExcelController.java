package infitry.rest.api.controller.excel;

import infitry.rest.api.util.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

@Tag(name = "ExcelController", description = "샘플 엑셀 컨트롤러")
@RestController
@RequestMapping("/sample/excel")
public class SampleExcelController {

    @Operation(summary = "엑셀 다운로드 샘플", description = "엑셀 다운로드 샘플 API")
    @GetMapping("/download")
    public void excelDownload(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=test.xlsx");
        response.setContentType("application/octet-stream");

        ByteArrayInputStream stream = ExcelUtil.createListToExcel(new ArrayList<>(), new ArrayList<>());
        IOUtils.copy(stream, response.getOutputStream());
    }
}
