package infitry.rest.api.controller.excel;

import infitry.rest.api.common.excel.SampleExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Tag(name = "ExcelController", description = "샘플 엑셀 컨트롤러")
@RestController
@RequestMapping("/v1/sample/excel")
public class SampleExcelController {

    @Operation(summary = "엑셀 다운로드 샘플", description = "엑셀 다운로드 샘플 API")
    @GetMapping("/download")
    public void excelDownload(HttpServletResponse response) throws IOException {
        SampleExcel sampleExcel = new SampleExcel();
        sampleExcel.excelDownload(response, "sample.xlsx");
    }
}
