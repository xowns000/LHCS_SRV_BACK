package kr.co.hkcloud.palette3.editor.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import kr.co.hkcloud.palette3.editor.domain.EditorRequest.EditorUploadRequest;
import kr.co.hkcloud.palette3.editor.domain.EditorResponse.EditorPropertiesResponse;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectCounteResponse;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import kr.co.hkcloud.palette3.file.enumer.FileTikaMediaType;
import kr.co.hkcloud.palette3.file.exception.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 에디터업로드 유효성 검증
 * 
 * @author leeiy
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class EditorUploadValidator implements Validator
{
    private final FileDbMngService fileDbMngService;


    @Override
    public boolean supports(Class<?> clazz)
    {
        return Map.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors)
    {
        @SuppressWarnings("unchecked")
        final Map<String, Object> validateMap = (Map<String, Object>) target;
        final EditorPropertiesResponse editorPropertiesResponse = (EditorPropertiesResponse) validateMap.get("editorPropertiesResponse");
        final EditorUploadRequest editorUploadRequest = (EditorUploadRequest) validateMap.get("editorUploadRequest");

        //비공개만 업로드 가능
        if(editorUploadRequest.getFileAcsTypeCd() != FileAccessType.PRIVATE) {
            //업로드가 거부되었습니다. (비공개가 아님)
            errors.reject(PaletteValidationCode.REJECTED_FOR_UPLOAD_PRIVATE.name(), PaletteValidationCode.REJECTED_FOR_UPLOAD_PRIVATE.getMessage());
            return;
        }

        //활성화 여부 체크
        if(!editorPropertiesResponse.isEnabled()) {
            //저장소가 비활성 상태입니다
            errors.reject(PaletteValidationCode.STORAGE_INACTIVE.name(), PaletteValidationCode.STORAGE_INACTIVE.getMessage());
            return;
        }

        MultipartFile file = editorUploadRequest.getUpload();
        if(file == null || file.isEmpty()) { throw new FileNotFoundException(); }

        //허용된 갯수 체크
        FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder().fileGroupKey(editorUploadRequest.getFileGroupKey()).build();
        FileDbMngSelectCounteResponse fileDbMngSelectCounteResponse = fileDbMngService.selectCount(fileDbMngSelectRequest);
        if(fileDbMngSelectCounteResponse.getDbFileCount() >= 0) {
            final int totalFileLength = fileDbMngSelectCounteResponse.getDbFileCount();
            log.debug(">>> dbFileCount :{}, totalFileLength: {}, maxFiles :{}", fileDbMngSelectCounteResponse.getDbFileCount(), totalFileLength, editorPropertiesResponse.getMaxFiles());
            if(totalFileLength > editorPropertiesResponse.getMaxFiles()) {
                //허용된 첨부파일 갯수를 초과했습니다
                errors.reject(PaletteValidationCode.EXCEEDED_THE_NUMBER_ALLOWED_ATTACH.name(), PaletteValidationCode.EXCEEDED_THE_NUMBER_ALLOWED_ATTACH.getMessage());
                return;
            }
        }
        else {
            //정의되지 않은 서비스입니다.
            errors.reject(PaletteValidationCode.UNDEFINED_SERVICE.name(), PaletteValidationCode.UNDEFINED_SERVICE.getMessage());
            return;
        }

        //확장자가 없으면 업로드 거부
        String actlFileNm = file.getOriginalFilename();
        String cleanFilename = StringUtils.cleanPath(actlFileNm);
        String cleanFileExtent = StringUtils.getFilenameExtension(cleanFilename);

        log.debug("actlFileNm : {}", actlFileNm);
        log.debug("cleanFilename    : {}", cleanFilename);
        log.debug("cleanFileExtent  : {}", cleanFileExtent);
        if(StringUtils.isEmpty(cleanFileExtent)) {
            //확장자가 없는 파일은 업로드할 수 없습니다
            errors.reject(PaletteValidationCode.FILES_WITHOUT_EXTENSION.name(), PaletteValidationCode.FILES_WITHOUT_EXTENSION.getMessage());
            return;
        }

        //https://www.baeldung.com/java-file-mime-type
        //https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
        Tika tika = new Tika();
        //메타 정보 기반
        String tikaMimeType = null;
        try(InputStream inputStream = file.getInputStream()) {
            tikaMimeType = tika.detect(inputStream);
            log.debug("tikaMimeType: {}", tikaMimeType);
        }
        catch(IOException e1) {
            log.error(e1.getLocalizedMessage(), e1);

            //허용된 파일 확인 중 오류가 발생했습니다
            errors.reject(PaletteValidationCode.ERROR_CHECKING_ALLOWED_FILES.name(), PaletteValidationCode.ERROR_CHECKING_ALLOWED_FILES.getMessage());
            return;
        }

        if(StringUtils.isEmpty(tikaMimeType)) {
            //파일 메타정보가 확인되지 않습니다
            errors.reject(PaletteValidationCode.FILE_META_INFO_NOT_CHECKED.name(), PaletteValidationCode.FILE_META_INFO_NOT_CHECKED.getMessage());
            return;
        }

        //확장자에 따른 MIME TYPE 체크
        if(!this.isAllowedMIMEType(cleanFileExtent, tikaMimeType, editorPropertiesResponse.getAcceptedFiles())) {
            //업로드가 거부된 파일이 있습니다
            errors.reject(PaletteValidationCode.REJECTED_FOR_UPLOAD.name(), PaletteValidationCode.REJECTED_FOR_UPLOAD.getMessage());
            return;
        }

        //파일크기 체크
        long uploadFilesize = DataSize.of(file.getSize(), DataUnit.BYTES).toBytes();
        long maxFilesize = editorPropertiesResponse.getMaxFilesize().toBytes();
        log.debug("uploadFilesize : {}", uploadFilesize);
        log.debug("maxFilesize    : {}", maxFilesize);
        if(maxFilesize <= uploadFilesize) {
            //허용된 용량을 초과했습니다
            errors.reject(PaletteValidationCode.ALLOWED_SIZE_EXCEEDED.name(), PaletteValidationCode.ALLOWED_SIZE_EXCEEDED.getMessage());
            return;
        }
    }


    /**
     * 확장자에 따른 MIME TYPE 및 허용된 파일 체크
     * 
     * @param  i
     * @param  ext
     * @param  mimeTypeCd
     * @param  acceptedFiles
     * @return
     */
    private boolean isAllowedMIMEType(String ext, String mimeTypeCd, List<String> acceptedFiles)
    {

        if(StringUtils.isEmpty(ext))
            return false;
        if(StringUtils.isEmpty(mimeTypeCd))
            return false;
        if(StringUtils.isEmpty(acceptedFiles))
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
        log.debug("Enum Tika        : {}", fileTikaMimeType.getTika());

        boolean allowed = false;
        if(mimeTypeCd.equals(fileTikaMimeType.getTika()) && ext.equals(fileTikaMimeType.getExtents())) {
            log.debug("Check MimeType - PASS>>>>");
            allowed = true;
        }
        else {
            //alias 체크
            int j = 0;
            for(String alias : fileTikaMimeType.getAlias()) {
                log.debug("[{}] Upload Ext : {}", j, ext);
                log.debug("[{}] Enum Ext   : {}", j, fileTikaMimeType.getExtents());
                log.debug("[{}] Enum Alias : {}", j, alias);
                if(!StringUtils.isEmpty(alias) && mimeTypeCd.equals(alias) && ext.equals(fileTikaMimeType.getExtents())) {
                    log.debug("[{}] Check Alias MimeType - PASS>>>>", j);
                    allowed = true;
                    break;
                }
                j++;
            }
        }

        //허용된 확장자 체크
        if(allowed) {
            int j = 0;
            for(String acceptedFile : acceptedFiles) {
                log.debug("[{}] Upload Ext    : {}", j, ext);
                log.debug("[{}] Accepted File : {}", j, acceptedFile);
                if(!StringUtils.isEmpty(acceptedFile) && ext.equals(acceptedFile)) {
                    log.debug("[{}] Check acceptedFile - PASS>>>>", j);
                    return true;
                }
                j++;
            }
        }
        return false;
    }
}
