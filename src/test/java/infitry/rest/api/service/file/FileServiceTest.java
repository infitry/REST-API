package infitry.rest.api.service.file;

import infitry.rest.api.common.constant.FileConstant;
import infitry.rest.api.dto.file.FileDownloadDto;
import infitry.rest.api.dto.file.FileDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional(readOnly = true)
@SpringBootTest
class FileServiceTest {

    @Autowired
    FileService fileService;
    private List<FileDto> fileDtos;
    private final static Path FILE_SAVE_PATH = Path.of(System.getProperty("user.home"), "download");

    /** Test 파일제거 */
    private static void deleteTestFiles(List<FileDto> fileDtos) {
        fileDtos.forEach(delete -> {
            String deleteFileName = delete.getSavedFileName() + FileConstant.DOT + delete.getExtension();
            File deleteFile = FILE_SAVE_PATH.resolve(deleteFileName).toFile();
            if (deleteFile.exists()) {
                if (deleteFile.delete()) {
                    System.out.println("파일삭제 성공 FileName : " + deleteFile.getName());
                } else {
                    System.out.println("파일존재 삭제실패 FileName : " + deleteFile.getName());
                }
            }
        });
    }

    @AfterEach
    public void afterTest() {
        deleteTestFiles(fileDtos);  // file 삭제처리
        fileDtos.clear();           // 목록 비우기
    }

    @Test
    @Transactional
    public void 파일_저장하기() {
        // given
        String writerData = "test1,test2,test3,test4";
        String originalFilename = "테스트_1.csv";
        String files = "files";
        String contentType = "text/plain";
        MultipartFile file = new MockMultipartFile(files, originalFilename, contentType, writerData.getBytes(StandardCharsets.UTF_8));
        // when
        fileDtos = fileService.save(List.of(file));
        fileDtos.forEach(System.out::println);

        // then
        assertEquals(originalFilename, fileDtos.get(0).getFileName(), "파일 명이 같아야 한다.");
    }

    @Test
    @Transactional
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
        fileDtos = fileService.save(List.of(file, file2));
        fileDtos.forEach(System.out::println);
        // then
        assertEquals(originalFilename, fileDtos.get(0).getFileName(), "파일 명이 같아야 한다.");
        assertEquals(originalFilename2, fileDtos.get(1).getFileName(), "파일 명이 같아야 한다.");
    }

    @Test
    @Transactional
    public void 파일_다운로드() {
        // given
        String writerData = "test1,test2,test3,test4";
        String originalFilename = "테스트_1.csv";
        String files = "files";
        String contentType = "text/plain";
        MultipartFile file = new MockMultipartFile(files, originalFilename, contentType, writerData.getBytes(StandardCharsets.UTF_8));
        // when
        fileDtos = fileService.save(List.of(file));
        FileDownloadDto fileDownloadDto = fileService.downloadFile(fileDtos.get(0).getFileId());
        System.out.println("fileDownloadDto = " + fileDownloadDto);
        Resource resource = fileDownloadDto.getResource();
        // then
        assertTrue(resource.exists());
        assertTrue(resource.isFile());
        assertTrue(resource.isReadable());
    }
}