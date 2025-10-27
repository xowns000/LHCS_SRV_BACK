package kr.co.hkcloud.palette3.config.properties.bbs;


import java.io.File;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 게시판(bbs) 속성
 * 
 * @author Orange
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "bbs")
public class BbsProperties
{
    @NotNull
    private final boolean enabled;

    private final Repository repository;


    @Getter
    @RequiredArgsConstructor
    public static final class Repository
    {

        private final File tempDir;

        @NotNull
        private final File dir;

        @NotNull
        private final File uploadDir;
    }

}
