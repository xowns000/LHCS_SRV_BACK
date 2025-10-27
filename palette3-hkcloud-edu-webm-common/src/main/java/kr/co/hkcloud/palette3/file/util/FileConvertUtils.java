package kr.co.hkcloud.palette3.file.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.validation.constraints.NotNull;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import kr.co.hkcloud.palette3.exception.PaletteUtilException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 파일 변환 유틸
 * 
 * @author RND
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@Component
public class FileConvertUtils
{
    /**
     * java.io.File to org.springframework.web.multipart.MultipartFile
     * 
     * @param  file
     * @return
     * @throws PaletteUtilException
     */
    public MultipartFile toMultipartFile(@NotNull final File file) throws TelewebUtilException
    {
        if(!file.isFile()) { return null; }

        FileItem fileItem = null;
        try {
            // @formatter:off
            fileItem = new DiskFileItem("File"
                                       , Files.probeContentType(file.toPath())
                                       , false
                                       , file.getName()
                                       , (int) file.length()
                                       , file.getParentFile());
            // @formatter:on

//                InputStream input = new FileInputStream(file);
//                OutputStream os = fileItem.getOutputStream();
//                IOUtils.copy(input, os);
            // Or faster..
            IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
        }
        catch(IOException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new TelewebUtilException(e.getLocalizedMessage(), e);
        }
        return fileItem != null ? new CommonsMultipartFile(fileItem) : null;
    }


    /**
     * org.springframework.core.io.Resource to Byte[]
     * 
     * @param  resource
     * @return
     * @throws TelewebUtilException
     */
    public byte[] toBytes(@NotNull final Resource resource) throws TelewebUtilException
    {
        if(!resource.exists()) { return null; }

        byte[] resourceByte;
        try {
            resourceByte = FileCopyUtils.copyToByteArray(resource.getFile());
        }
        catch(IOException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new TelewebUtilException(e.getLocalizedMessage(), e);
        }
        return resourceByte;

    }
}
