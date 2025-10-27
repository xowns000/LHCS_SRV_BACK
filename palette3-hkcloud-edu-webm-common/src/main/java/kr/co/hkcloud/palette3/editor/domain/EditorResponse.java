package kr.co.hkcloud.palette3.editor.domain;


import kr.co.hkcloud.palette3.config.properties.editor.enumer.PaletteEditor;
import kr.co.hkcloud.palette3.file.constraint.FileGroupKeyConstraint;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import lombok.*;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;


/**
 * 에디터 파일 응답 도메인
 * 
 * @author leeiy
 *
 */
public class EditorResponse
{
    /**
     * 에디터 파일 업로드 응답
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static final class EditorUploadResponse
    {
        @PositiveOrZero
        private int uploaded; // 업로드 성공 1 / 실패 0

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        @NotBlank
        @FileGroupKeyConstraint
        private String fileKey; // 파일키

        @NotBlank
        private String fileName; // 파일명

        @PositiveOrZero
        private long fileSz; // 크기

        @NotBlank
        private String fileSzDisplay; //크기(KB)

        @PositiveOrZero
        private int dwnldCnt; // 다운로드수

        @NotBlank
        private String fileExtn; // 확장자

        @NotBlank
        private URI url; // URL
    }


    /**
     * 에디터 경로구분 프로퍼티 응답
     * 
     * @author leeiy
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static final class EditorPropertiesResponse
    {
        @NotNull
        private RepositoryTaskTypeCd taskTypeCd;

        @NotNull
        private RepositoryPathTypeCd pathTypeCd;

        //@NotNull
        private RepositoryTrgtTypeCd trgtTypeCd;

        @NotNull
        private boolean enabled;

        @NotNull
        private PaletteEditor name;

        @NotEmpty
        private String version;

        @NotNull
        private int maxFiles;

        @NotNull
        @DataSizeUnit(DataUnit.MEGABYTES)
        private DataSize maxFilesize;

        @NotNull
        private List<String> acceptedFiles;

        @NotNull
        private Path dir;

        private Path tempDir;

        @NotBlank
        private String uploadUri;
    }
}
