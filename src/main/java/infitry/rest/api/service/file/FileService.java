package infitry.rest.api.service.file;

import infitry.rest.api.common.constant.FileConstant;
import infitry.rest.api.dto.file.FileDownloadDto;
import infitry.rest.api.dto.file.FileDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.FileRepository;
import infitry.rest.api.repository.domain.file.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        return multipartFiles.stream().map(file -> {
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
            return modelMapper.map(savedFile, FileDto.class);
        }).collect(Collectors.toList());
    }

    /** 파일 다운로드 */
    public FileDownloadDto downloadFile(Long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new ServiceException("파일을 찾을 수 없습니다."));
        String savedFileName = file.getSavedFileName() + FileConstant.DOT + file.getExtension();
        Path path = FILE_SAVE_PATH.resolve(savedFileName).normalize();
        String contentDisposition = "attachment; filename=\"" + file.getFileName() + "\"";
        return FileDownloadDto.builder()
                    .mediaType(getMediaType(path))
                    .contentDisposition(contentDisposition)
                    .resource(getFileResource(path))
                .build();
    }

    private MediaType getMediaType(Path path) {
        String mediaType;
        try {
            mediaType = Files.probeContentType(path);
            System.out.println("mediaType = " + mediaType);
        } catch (IOException e) {
            log.error("get mediaType Error : ", e);
            throw new ServiceException("");
        }
        return StringUtils.hasText(mediaType) ? MediaType.parseMediaType(mediaType) : MediaType.APPLICATION_OCTET_STREAM;
    }

    private Resource getFileResource(Path path) {
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ServiceException("정상적인 파일이 아닙니다.");
            }
        } catch (MalformedURLException e) {
            log.info("Download File Error reason : ", e);
            throw new ServiceException("파일을 찾을 수 없습니다.");
        }
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
            throw new ServiceException("파일을 업로드 할 수 없습니다. (name: " + file.getName() + ")");
        }
    }

    private void createDefaultDirectories() {
        try {
            Files.createDirectories(FILE_SAVE_PATH);
        } catch (IOException e) {
            log.info("Create default directory fail reason : ", e);
            throw new ServiceException("파일 업로드 폴더를 생성할 수 없습니다.");
        }
    }
}
