package kr.co.hkcloud.palette3.excel.domain;


import java.nio.file.Path;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import kr.co.hkcloud.palette3.excel.enumer.ExcelBusiType;
import kr.co.hkcloud.palette3.excel.enumer.ExcelPathType;
import kr.co.hkcloud.palette3.file.enumer.FileUploadLib;
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
public class ExcelResponse
{
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
    public static final class ExcelPropertiesResponse
    {
        @NotNull
        private ExcelBusiType taskTypeCd;

        @NotNull
        private ExcelPathType pathTypeCd;

        @NotNull
        private RepositoryTrgtTypeCd trgtTypeCd;

        @NotNull
        private boolean enabled;

        @NotNull
        private FileUploadLib excelUploadLib;

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

        @NotBlank
        private String downloadUri;

    }
}
