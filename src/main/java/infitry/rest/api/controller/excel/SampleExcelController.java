package infitry.rest.api.controller.excel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "ExcelController", description = "샘플 엑셀 컨트롤러")
@RestController
@RequestMapping("/sample/excel")
public class SampleExcelController {

    @Operation(summary = "엑셀 다운로드 샘플", description = "엑셀 다운로드 샘플 API")
    @GetMapping("/download")
    public void excelDownload(HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment;filename=testExcel1.xlsx");
        response.setContentType("application/octet-stream");
        
    }
}
