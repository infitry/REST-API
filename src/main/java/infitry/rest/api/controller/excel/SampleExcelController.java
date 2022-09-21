package infitry.rest.api.controller.excel;

import infitry.rest.api.common.excel.SampleExcelProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "Excel API", description = "샘플 엑셀 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sample/excel")
public class SampleExcelController {
    private final SampleExcelProvider  sampleExcelProvider;
    @Operation(summary = "엑셀 다운로드 샘플", description = "엑셀 다운로드 샘플 API")
    @GetMapping
    public void excelDownload(HttpServletResponse response) {
        sampleExcelProvider.excelDownload(response, "sample");
    }
}
