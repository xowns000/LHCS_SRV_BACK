package kr.co.hkcloud.palette3.config.properties.editor;


import java.io.File;
import java.nio.file.Path;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.config.properties.editor.enumer.PaletteEditor;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 에디터(editor) 속성
 * 
 * @author Orange
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "editor")
public class EditorProperties
{
    @NotNull
    private final boolean enabled;

    @NotNull
    private final PaletteEditor name;  //이름

    @NotBlank
    private final String version; //버전

    private final Repository repository;


    @Getter
    @RequiredArgsConstructor
    public static final class Repository
    {
        @NotNull
        private final File rootDir;

        private final Km  km;
        private final Bbs bbs;


        @Getter
        @RequiredArgsConstructor
        public static final class Km
        {
            private final Images images;


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
            private final Images images;


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
    }

}
