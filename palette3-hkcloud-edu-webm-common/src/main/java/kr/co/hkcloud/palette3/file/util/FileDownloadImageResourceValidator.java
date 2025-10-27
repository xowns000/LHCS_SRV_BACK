package kr.co.hkcloud.palette3.file.util;


import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.FileTikaMediaType;
import kr.co.hkcloud.palette3.file.enumer.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 다운로드 이미지 리소스 유효성 검증
 * 
 * @author leeiy
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FileDownloadImageResourceValidator implements Validator
{
    private final FileRuleUtils fileRuleUtils;


    @Override
    public boolean supports(Class<?> clazz)
    {
        return MultipartFile.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors)
    {
        @SuppressWarnings("unchecked")
        final Map<String, Object> validateMap = (Map<String, Object>) target;
        final FilePropertiesResponse filePropertiesResponse = (FilePropertiesResponse) validateMap.get("filePropertiesResponse");
        final FileDownloadResponse fileDownloadResponse = (FileDownloadResponse) validateMap.get("fileDownloadResponse");

        //활성화 여부 체크
        if(!filePropertiesResponse.isEnabled()) {
            //저장소가 비활성 상태입니다
            errors.reject(PaletteValidationCode.STORAGE_INACTIVE.name(), PaletteValidationCode.STORAGE_INACTIVE.getMessage());
            return;
        }

        //확장자가 없으면 업로드 거부
        String actlFileNm = fileDownloadResponse.getResource().getFilename();
        String cleanFilename = StringUtils.cleanPath(actlFileNm);
        String cleanFileExtent = fileRuleUtils.getIfFileExtent(fileDownloadResponse);

        log.debug("Filename         : {}", actlFileNm);
        log.debug("cleanFilename    : {}", cleanFilename);
        log.debug("cleanFileExtent  : {}", cleanFileExtent);

        //메타 정보 기반
        final MediaType tikaMimeType = fileRuleUtils.getMediaType(fileDownloadResponse.getResource());
        final String mediaType = tikaMimeType.getType();

        if(StringUtils.isEmpty(mediaType)) {
            //파일 메타정보가 확인되지 않습니다
            errors.reject(PaletteValidationCode.FILE_META_INFO_NOT_CHECKED.name(), PaletteValidationCode.FILE_META_INFO_NOT_CHECKED.getMessage());
            return;
        }

        //이미지 확장자에 따른 MIME TYPE 체크
        if(!this.isAllowedImageMIMEType(cleanFileExtent, mediaType)) {
            //다운로드가 거부된 파일이 있습니다
            errors.reject(PaletteValidationCode.REJECTED_FOR_DOWNLOAD.name(), PaletteValidationCode.REJECTED_FOR_DOWNLOAD.getMessage());
            return;
        }
    }


    /**
     * 이미지 확장자에 따른 MIME TYPE 및 허용된 파일 체크
     * 
     * @param  i
     * @param  ext
     * @param  mimeTypeCd
     * @param  acceptedFiles
     * @return
     */
    private boolean isAllowedImageMIMEType(String ext, String mimeTypeCd)
    {
        if(StringUtils.isEmpty(ext))
            return false;
        if(StringUtils.isEmpty(mimeTypeCd))
            return false;

        FileTikaMediaType fileTikaMimeType;
        if("7z".equals(ext)) {
            fileTikaMimeType = FileTikaMediaType.x7z;
        }
        else {
            fileTikaMimeType = FileTikaMediaType.valueOf(ext);
        }

        log.debug("Upload MimeType  : {}", mimeTypeCd);
        log.debug("Enum Extends     : {}", fileTikaMimeType.getExtents());
        log.debug("Enum Real        : {}", fileTikaMimeType.getReal());
        log.debug("Enum Tika        : {}", fileTikaMimeType.getTika());
        log.debug("Enum Alias       : {}", fileTikaMimeType.getAlias());
        log.debug("Enum description : {}", fileTikaMimeType.getDescription());
        log.debug("Enum file type   : {}", fileTikaMimeType.getFileType());

        //이미지만 허용
        if(fileTikaMimeType.getFileType() == FileType.image) { return true; }
        return false;
    }
}
