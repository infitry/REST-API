package infitry.rest.api.common.excel;

import infitry.rest.api.repository.UserRepository;
import infitry.rest.api.repository.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleExcelProvider extends ExcelProvider {

    private final UserRepository userRepository;
    @Override
    protected void createRow(SXSSFWorkbook workbook) {
        List<User> userList = userRepository.findAll();
        // 시트 생성
        Sheet sheet = workbook.createSheet("sheet1");

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("아이디");
        headerRow.createCell(1).setCellValue("이름");

        // 내용 생성
        Row dataRow = sheet.createRow(1);
        userList.forEach(user -> {
            dataRow.createCell(0).setCellValue(user.getUserId());
            dataRow.createCell(1).setCellValue(user.getUsername());
        });
    }
}
