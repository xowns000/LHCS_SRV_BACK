package kr.co.hkcloud.palette3.config.properties.file;


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
 * 파일(file) 속성
 * 
 * @author RND
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "file")
public class FileProperties
{
    @NotNull
    private final boolean enabled;

    @NotNull
    private final FileUploadLib fileUploadLib;

    private final Repository repository;


    @Getter
    @RequiredArgsConstructor
    public static final class Repository
    {
        @NotNull
        private final File rootDir;

        private final Palette palette;
        private final Phone   phone;
        private final Chat    chat;
        private final Km      km;
        private final Bbs     bbs;


        @Getter
        @RequiredArgsConstructor
        public static final class Palette
        {
            private final Files files;


            @Getter
            @RequiredArgsConstructor
            public static final class Files
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


        @Getter
        @RequiredArgsConstructor
        public static final class Phone
        {
            private final Files files;


            @Getter
            @RequiredArgsConstructor
            public static final class Files
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


        @Getter
        @RequiredArgsConstructor
        public static final class Chat
        {
            private final Publics        publics;
            private final Files          files;
            private final Images         images;
            private final Videos         videos;

            @Getter
            @RequiredArgsConstructor
            public static final class Publics
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


            @Getter
            @RequiredArgsConstructor
            public static final class Files
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


            @Getter
            @RequiredArgsConstructor
            public static final class Images
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


            @Getter
            @RequiredArgsConstructor
            public static final class Videos
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


        @Getter
        @RequiredArgsConstructor
        public static final class Km
        {
            private final Files files;
            private final Images images;

            @Getter
            @RequiredArgsConstructor
            public static final class Files
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
            
            @Getter
            @RequiredArgsConstructor
            public static final class Images
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


        @Getter
        @RequiredArgsConstructor
        public static final class Bbs
        {
            private final Files files;


            @Getter
            @RequiredArgsConstructor
            public static final class Files
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
