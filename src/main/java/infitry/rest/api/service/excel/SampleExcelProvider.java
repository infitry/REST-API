package infitry.rest.api.service.excel;

import infitry.rest.api.common.excel.ExcelProvider;
import infitry.rest.api.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SampleExcelProvider extends ExcelProvider {

    /** 엑셀 데이터 생성 */
    @Override
    protected void createRow(SXSSFWorkbook workbook) {
        // 데이터 불러오기
        List<UserDto> userList = getExcelData();
        // 목록 파티션
        List<List<UserDto>> partitionList = createPartition(userList);
        // 파티션 목록 마다 sheet 만듬
        int sheetNum = 0;
        for (List<UserDto> userDtos : partitionList) {
            Sheet sheet = workbook.createSheet("sheet" + sheetNum);
            // 헤더 생성
            createHeader(sheet);
            // 내용 생성
            createRow(userDtos, sheet);
            sheetNum++;
        }
    }

    /** 엑셀 Header 생성 */
    private void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("아이디");
        headerRow.createCell(1).setCellValue("이름");
        headerRow.createCell(2).setCellValue("이메일");
    }

    /** 엑셀 Body 생성 */
    private void createRow(List<UserDto> userList, Sheet sheet) {
        int rowNum = 1;
        for (UserDto user : userList) {
            Row dataRow = sheet.createRow(rowNum);
            dataRow.createCell(0).setCellValue(user.getId());
            dataRow.createCell(1).setCellValue(user.getName());
            dataRow.createCell(2).setCellValue(user.getEmail());
            rowNum++;
        }
    }

    /** 엑셀 데이터 불러오기 */
    private List<UserDto> getExcelData() {
        return IntStream.range(0, 3000000).mapToObj(i ->
                UserDto.builder()
                        .id("아이디" + i)
                        .name("이름" + i)
                        .email("이메일" + i)
                        .build()
        ).collect(Collectors.toList());
    }
}
