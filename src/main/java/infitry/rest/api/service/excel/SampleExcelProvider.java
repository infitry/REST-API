package infitry.rest.api.service.excel;

import infitry.rest.api.common.excel.ExcelProvider;
import infitry.rest.api.configuration.aop.timer.Timer;
import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.repository.UserRepository;
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

    private final UserRepository userRepository;
    @Override
    protected void createRow(SXSSFWorkbook workbook) {
        List<UserDto> userList = IntStream.range(0, 10000000).mapToObj(i ->
            UserDto.builder()
                .id("아이디" + i)
                .name("이름" + i)
                .email("이메일" + i)
                .build()
        ).collect(Collectors.toList());

        // 시트 생성
        Sheet sheet = workbook.createSheet("sheet1");

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("아이디");
        headerRow.createCell(1).setCellValue("이름");
        headerRow.createCell(2).setCellValue("이메일");

        // 내용 생성
        int rowNum = 1;
        for (UserDto user : userList) {
            Row dataRow = sheet.createRow(rowNum);
            dataRow.createCell(0).setCellValue(user.getId());
            dataRow.createCell(1).setCellValue(user.getName());
            dataRow.createCell(2).setCellValue(user.getEmail());
            rowNum++;
        }
    }
}
