package kr.co.hkcloud.palette3.editor.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import kr.co.hkcloud.palette3.editor.args.EditorRuleProperties;
import kr.co.hkcloud.palette3.editor.domain.EditorResponse.EditorPropertiesResponse;
import kr.co.hkcloud.palette3.exception.PaletteValidationException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.DownloadImageResourceRequest;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.FileUploadLib;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileDownloadImageResourceValidator;
import kr.co.hkcloud.palette3.file.util.FileDownloadUtils;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "EditorDownloadRestController",
     description = "에디터 다운로드 REST 컨트롤러")
public class EditorDownloadRestController
{
    private final FileDownloadImageResourceValidator fileDownloadImageResourceValidator;
    private final FileDownloadUtils                  fileDownloadUtils;
    private final FileRuleUtils                      fileRuleUtils;


    // @formatter:off
    /**
     * 에디터 리소스 이미지 다운로드
     * 
     * @param filePropertiesResponse
     * @param downloadImageResourceRequest
     * @param bindingResult
     * @return
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "리소스 이미지 다운로드",
                  notes = "리소스 이미지를 다운로드한다")
    @GetMapping(value = "/api/editor/{taskTypeCd}/{pathTypeCd}/{fileGroupKey}/{fileKey}")
    public ResponseEntity<Resource> downloadImageResource(@EditorRuleProperties @Valid final EditorPropertiesResponse editorPropertiesResponse
                                                        , @Valid final DownloadImageResourceRequest downloadImageResourceRequest
                                                        , final BindingResult bindingResult) throws TelewebApiException
    {
        //이미지만 허용
        if(editorPropertiesResponse.getPathTypeCd() != RepositoryPathTypeCd.images) {
            throw new PaletteValidationException(PaletteValidationCode.REJECTED_ONLY_IMAGES);
        }
        
        final FilePropertiesResponse filePropertiesResponse = new FilePropertiesResponse();
        BeanUtils.copyProperties(editorPropertiesResponse, filePropertiesResponse);
        filePropertiesResponse.setFileUploadLib(FileUploadLib.ckeditor);
        filePropertiesResponse.setDownloadUri("dummy");

        //파일 정보 가져오기
        FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder()
                                                                              .fileGroupKey(downloadImageResourceRequest.getFileGroupKey())
                                                                              .fileKey(downloadImageResourceRequest.getFileKey())
                                                                              .build();
        // 리소스 로드
        if( filePropertiesResponse.getTrgtTypeCd() == null ) filePropertiesResponse.setTrgtTypeCd( RepositoryTrgtTypeCd.FILE );
        FileDownloadResponse fileDownloadResponse = fileDownloadUtils.loadAsResource(filePropertiesResponse, fileDbMngSelectRequest);
        if(fileDownloadResponse != null && fileDownloadResponse.getResource() != null) {
            
            //메타 정보 기반
            final MediaType mediaType = fileRuleUtils.getMediaType(fileDownloadResponse.getResource());
            log.debug(">>> mediaType :{}", mediaType);
            
            //Validation 체크
            final Map<String, Object> validateMap = new HashMap<String, Object>();
            validateMap.put("filePropertiesResponse", filePropertiesResponse);
            validateMap.put("fileDownloadResponse", fileDownloadResponse);
            fileDownloadImageResourceValidator.validate(validateMap, bindingResult);
            if(bindingResult.hasErrors()) {
                for(ObjectError error : bindingResult.getAllErrors()) {
                    log.error("error.toString : {}", error.toString());
                }
                throw new PaletteValidationException(bindingResult.getAllErrors());
            }
    
            //응답
            return ResponseEntity.ok()
                                 .contentType(mediaType)
                                 .body(fileDownloadResponse.getResource());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .body(fileDownloadResponse.getResource());
        
    }
    // @formatter:on
}
