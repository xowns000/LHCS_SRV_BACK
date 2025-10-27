package kr.co.hkcloud.palette3.file.domain;

import java.nio.file.Path;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.core.io.Resource;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import kr.co.hkcloud.palette3.file.constraint.FileGroupKeyConstraint;
import kr.co.hkcloud.palette3.file.enumer.FileUploadLib;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 파일 응답 도메인
 * 
 * @author leeiy
 *
 */
public class FileResponse {

    /**
     * 파일 업로드 응답
     * 
     * @author leeiy
     *
     */
    @Getter
    @Builder
    public static final class FileUploadResponse {

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        @NotBlank
        @FileGroupKeyConstraint
        private String fileKey; // 파일키

        @NotBlank
        private String actlFileNm; // 파일명

        @PositiveOrZero
        private long fileSz; // 크기

        @NotBlank
        private String fileSzDisplay; //크기(KB)

        @PositiveOrZero
        private int dwnldCnt; // 다운로드수

        @NotBlank
        private String fileExtn; // 확장자

        private String custcoId;

        private String filePath;
        private String strgFileNm;

    }

    /**
     * 파일 다운로드 응답
     * 
     * @author leeiy
     *
     */
    @Getter
    @Builder
    public static final class FileDownloadResponse {

        @NotBlank
        private String actlFileNm;  //진짜 파일명 (업로드한...)

        @NotBlank
        private String mimeTypeCd; // Mime Type

        @NotBlank
        private String fileExtn; // 확장자

        @NotNull
        private Resource resource; //리소스
    }

    /**
     * 파일명과 확장자 규칙 응답
     * 
     * @author leeiy
     *
     */
    @Getter
    @Builder
    @ToString
    public static final class RuleFilenameAndExtentResponse {

        @NotBlank
        private String actlFileNm;  //진짜 파일명 (업로드한...)

        @NotBlank
        private String cleanFilename;  //클린 파일명

        @NotBlank
        private String cleanFileNameWithOutExt;  //클린 확장자를 제외한 파일명

        @NotBlank
        private String cleanFileExtent;  //클린 확장자

        @NotBlank
        private String ruleFilenameExtent;  //파일명과 확장자 (저장할 ..)
    }

    /**
     * 경로구분 프로퍼티 응답
     * 
     * @author leeiy
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static final class FilePropertiesResponse {

        private String custcoId;   // 회사구분

        @NotNull
        private RepositoryTaskTypeCd taskTypeCd;

        @NotNull
        private RepositoryPathTypeCd pathTypeCd;

        private RepositoryTrgtTypeCd trgtTypeCd;

        @NotNull
        private boolean enabled;

        private FileUploadLib fileUploadLib;

        @NotNull
        private int maxFiles;

        //@NotNull
        @DataSizeUnit(DataUnit.MEGABYTES)
        private DataSize maxFilesize;

        private List<String> acceptedFiles;

        private Path dir;

        private Path tempDir;

        private String uploadUri;

        private String downloadUri;

        private int userId;

    }

    /**
     * 다운로드 프로퍼티 응답
     * 
     * @author leeiy
     */
    @Getter
    @Builder
    @ToString
    public static final class FileDownloadPropertiesResponse {

        @NotNull
        private RepositoryTaskTypeCd taskTypeCd;

        @NotNull
        private RepositoryPathTypeCd pathTypeCd;

        @NotNull
        private boolean enabled;

        private String downloadUri;

    }
}
