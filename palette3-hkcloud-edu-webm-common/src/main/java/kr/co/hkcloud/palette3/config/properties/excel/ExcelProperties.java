package kr.co.hkcloud.palette3.config.properties.excel;


import kr.co.hkcloud.palette3.file.enumer.FileUploadLib;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.nio.file.Path;
import java.util.List;


/**
 * 팔레트 엑셀(excel) 속성
 * 
 * @author Orange
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "excel")
public class ExcelProperties
{
    @NotNull
    private final boolean enabled;

    @NotNull
    private final FileUploadLib excelUploadLib;

    private final Repository repository;


    @Getter
    @RequiredArgsConstructor
    public static final class Repository
    {
        @NotNull
        private final File rootDir;

        private final Phone phone;


        @Getter
        @RequiredArgsConstructor
        public static final class Phone
        {
            private final Outbound outbound;


            @Getter
            @RequiredArgsConstructor
            public static final class Outbound
            {
                //@NotNull
                private final RepositoryTrgtTypeCd trgtTypeCd;
                @NotNull
                private final boolean              enabled;
                @NotNull
                private final int                  maxFiles;
                @DataSizeUnit(DataUnit.MEGABYTES)
                @NotNull
                private final DataSize             maxFilesize;
                @NotNull
                private final List<String>         acceptedFiles;
                @NotNull
                private final Path                 dir;

                private final Path                 tempDir;
            }
        }
    }
}
