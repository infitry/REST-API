package infitry.rest.api.repository;

import infitry.rest.api.configuration.TestJPAConfig;
import infitry.rest.api.dto.file.FileDto;
import infitry.rest.api.repository.domain.file.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestJPAConfig.class})
class FileRepositoryTest {

    @Autowired
    FileRepository fileRepository;

    @Test
    public void 파일_저장테스트() {
        String filePath = "~/download/";
        String fileName = "testFile";
        String extension = ".jpg";
        String savedFileName = UUID.randomUUID().toString();
        int fileSize = 21321321;

        // given
        File file = File.createFile(FileDto.builder()
                .filePath(filePath)
                .fileName(fileName)
                .extension(extension)
                .savedFileName(savedFileName)
                .fileSize(fileSize)
            .build());
        // when
        File savedFile = fileRepository.save(file);
        // then
        assertEquals(savedFile.getFileName(), fileName, "저장 된 데이터의 파일명은 일치하여야 한다.");
    }
}