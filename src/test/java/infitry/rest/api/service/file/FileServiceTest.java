package infitry.rest.api.service.file;

import infitry.rest.api.dto.file.FileDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {

    @Autowired
    FileService fileService;

    @Test
    public void 파일_저장하기() {
        // given
        String writerData = "test1,test2,test3,test4";
        String originalFilename = "테스트_1.csv";
        String files = "files";
        String contentType = "text/plain";
        MultipartFile file = new MockMultipartFile(files, originalFilename, contentType, writerData.getBytes(StandardCharsets.UTF_8));
        // when
        List<FileDto> fileDtos = fileService.save(List.of(file));
        fileDtos.stream().forEach(System.out::println);
        // then
        assertEquals(originalFilename, fileDtos.get(0).getFileName(), "파일 명이 같아야 한다.");
    }

    @Test
    public void 다중파일_저장하기() {
        // given
        String writerData = "test1,test2,test3,test4";
        String originalFilename = "테스트_1.csv";
        String files = "files";
        String contentType = "text/plain";
        MultipartFile file = new MockMultipartFile(files, originalFilename, contentType, writerData.getBytes(StandardCharsets.UTF_8));

        String writerData2 = "test1,test2,test3,test4";
        String originalFilename2 = "테스트_2.csv";
        String files2 = "files2";
        String contentType2 = "text/plain";
        MultipartFile file2 = new MockMultipartFile(files2, originalFilename2, contentType2, writerData2.getBytes(StandardCharsets.UTF_8));
        // when
        List<FileDto> fileDtos = fileService.save(List.of(file, file2));
        fileDtos.stream().forEach(System.out::println);
        // then
        assertEquals(originalFilename, fileDtos.get(0).getFileName(), "파일 명이 같아야 한다.");
        assertEquals(originalFilename2, fileDtos.get(1).getFileName(), "파일 명이 같아야 한다.");
    }
}