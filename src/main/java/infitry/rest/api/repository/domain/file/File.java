package infitry.rest.api.repository.domain.file;

import infitry.rest.api.dto.file.FileDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Entity(name = "tb_file")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long fileId;

    @Column(columnDefinition = "varchar(100)")
    String filePath;

    @Column(columnDefinition = "varchar(100)")
    String fileName;

    @Column(columnDefinition = "varchar(100)")
    String savedFileName;

    @Column(columnDefinition = "varchar(5)")
    String extension;

    @Column
    int fileSize;

    private File(FileDto fileDto) {
        this.filePath = fileDto.getFilePath();
        this.fileName = fileDto.getFileName();
        this.savedFileName = fileDto.getSavedFileName();
        this.extension = fileDto.getExtension();
        this.fileSize = fileDto.getFileSize();
    }

    public static File createFile(FileDto fileDto) {
        return new File(fileDto);
    }
}
