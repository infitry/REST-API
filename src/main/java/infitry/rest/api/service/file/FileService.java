package infitry.rest.api.service.file;

import infitry.rest.api.dto.file.FileDto;
import infitry.rest.api.repository.FileRepository;
import infitry.rest.api.repository.domain.file.File;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;
    private final static String FILE_SAVE_PATH = "~/download/";

    /** 파일 저장하기 */
    public List<FileDto> save(List<MultipartFile> multipartFiles) {
        List<File> files = multipartFiles.stream()
                .map(file -> FileDto.builder()
                        .filePath(FILE_SAVE_PATH)
                        .fileName(file.getOriginalFilename())
                        .savedFileName(UUID.randomUUID().toString())
                        .extension(FilenameUtils.getExtension(file.getOriginalFilename()))
                        .fileSize(Long.valueOf(file.getSize()).intValue())
                    .build())
                .map(fileDto -> File.createFile(fileDto))
                .collect(Collectors.toList());
        
        return fileRepository.saveAll(files).stream()
                .map(file -> modelMapper.map(file, FileDto.class))
                .collect(Collectors.toList());
    }
}
