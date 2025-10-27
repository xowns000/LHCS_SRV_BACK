package kr.co.hkcloud.palette3.editor.util;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.editor.domain.EditorRequest.EditorUploadRequest;
import kr.co.hkcloud.palette3.editor.domain.EditorResponse.EditorPropertiesResponse;
import kr.co.hkcloud.palette3.exception.PaletteAppException;
import kr.co.hkcloud.palette3.file.app.FileStorageService;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequests;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileUploadResponse;
import kr.co.hkcloud.palette3.file.enumer.FileUploadLib;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 에디터 파일 업로드 유틸
 * 
 * @author leeiy
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@Component
public class EditorUploadUtils
{
    private final FileRuleUtils      fileRuleUtils;
    private final FileStorageService fileStorageService;


    /**
     * 에디터 파일 저장
     * 
     * @param  editorPropertiesResponse
     * @param  editorUploadRequest
     * @return
     * @throws PaletteAppException
     */
    @NotNull
    public FileUploadResponse store(@Valid final EditorPropertiesResponse editorPropertiesResponse, @Valid final EditorUploadRequest editorUploadRequest) throws PaletteAppException
    {
        //파일 empty 체크
        if(editorUploadRequest.getUpload().isEmpty()) {
            //무시한다.
            log.info("{} file empty skip!", editorUploadRequest.getUpload().getOriginalFilename());
            return null;
        }

        final FilePropertiesResponse filePropertiesResponse = new FilePropertiesResponse();
        BeanUtils.copyProperties(editorPropertiesResponse, filePropertiesResponse);
        filePropertiesResponse.setTrgtTypeCd(RepositoryTrgtTypeCd.EDITOR);
        filePropertiesResponse.setFileUploadLib(FileUploadLib.dropzone);
        filePropertiesResponse.setDownloadUri("dummy");

        //그룹키 생성 (있으면 사용하고, 없으면 생성한다)
        // @formatter:off
        String fileGroupKey = fileRuleUtils.ifCreatFileGroupKey(FileUploadRequests.builder()
                                                                                  .fileGroupKey(editorUploadRequest.getFileGroupKey())
                                                                                  .build());
        
        return fileStorageService.store(filePropertiesResponse, FileUploadRequest.builder()
                                                                                 .file(editorUploadRequest.getUpload())
                                                                                 .fileGroupKey(fileGroupKey)
                                                                                 .fileAcsTypeCd(editorUploadRequest.getFileAcsTypeCd())
                                                                                 .build());
        // @formatter:on
    }
}
