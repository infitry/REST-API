package infitry.rest.api.service.file;

import infitry.rest.api.common.constant.FileConstant;
import infitry.rest.api.dto.file.FileDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.FileRepository;
import infitry.rest.api.repository.domain.file.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;
    private final static Path FILE_SAVE_PATH = Path.of(System.getProperty("user.home"), "download");

    /** 파일 저장하기 */
    @Transactional
    public List<FileDto> save(List<MultipartFile> multipartFiles) {
        if (!Files.exists(FILE_SAVE_PATH)) {
            createDefaultDirectories();
        }

        List<FileDto> results = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            String savedFileName = getRandomFileName();
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());

            File fileEntity = File.createFile(FileDto.builder()
                    .filePath(FILE_SAVE_PATH.toString())
                    .fileName(file.getOriginalFilename())
                    .savedFileName(savedFileName)
                    .extension(extension)
                    .fileSize(Long.valueOf(file.getSize()).intValue())
                .build());

            File savedFile = fileRepository.save(fileEntity);
            uploadFile(file, savedFileName + FileConstant.DOT + extension);
            results.add(modelMapper.map(savedFile, FileDto.class));
        }

        return results;
    }

    private String getRandomFileName() {
        return UUID.randomUUID().toString();
    }

    private void uploadFile(MultipartFile file, String savedFileName) {
        try {
            Files.copy(file.getInputStream(), FILE_SAVE_PATH.resolve(savedFileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.info("Upload file fail reason :", e);
            throw new ServiceException("Could not upload file (name: " + file.getName() + ")");
        }
    }

    private void createDefaultDirectories() {
        try {
            Files.createDirectories(FILE_SAVE_PATH);
        } catch (IOException e) {
            log.info("Create default directory fail reason : ", e);
            throw new ServiceException("Could not create upload Directories!");
        }
    }
}
