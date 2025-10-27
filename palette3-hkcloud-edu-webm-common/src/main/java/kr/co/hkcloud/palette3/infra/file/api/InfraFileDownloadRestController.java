package kr.co.hkcloud.palette3.infra.file.api;


import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import kr.co.hkcloud.palette3.exception.PaletteValidationException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.file.args.FileRuleProperties;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.util.FileDownloadImageResourceValidator;
import kr.co.hkcloud.palette3.file.util.FileDownloadUtils;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import kr.co.hkcloud.palette3.infra.file.domain.InfraFileRequest.InfraDownloadImageResourceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "InfraFileDownloadRestController",
     description = "대외 다운로드 REST 컨트롤러")
public class InfraFileDownloadRestController
{
    private final FileDownloadImageResourceValidator fileDownloadImageResourceValidator;
    private final FileDownloadUtils                  fileDownloadUtils;
    private final FileRuleUtils                      fileRuleUtils;


    // @formatter:off
    /**
     * 대외 리소스 이미지 다운로드
     * 
     * @param filePropertiesResponse
     * @param downloadImageResourceRequest
     * @param bindingResult
     * @return
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "대외 리소스 이미지 다운로드",
                  notes = "대외 리소스 이미지를 다운로드한다")
    @GetMapping(value = "/infra/api/file/{custcoId}/{taskTypeCd}/{pathTypeCd}/{fileGroupKey}/{fileKey}")
    public ResponseEntity<Resource> downloadImageResourceInfra(@FileRuleProperties @Valid final FilePropertiesResponse filePropertiesResponse
                                                             , @Valid final InfraDownloadImageResourceRequest infraDownloadImageResourceRequest
                                                             , final BindingResult bindingResult) throws TelewebApiException
    {
        log.debug(">>> filePropertiesResponse :{}", filePropertiesResponse);
        log.debug(">>> infraDownloadImageResourceRequest :{}", infraDownloadImageResourceRequest);
        
        //공개만 허용
        if(infraDownloadImageResourceRequest.getFileAcsTypeCd() != FileAccessType.PUBLIC) {
            //허용되지 않는 데이터입니다.
            throw new PaletteValidationException(PaletteValidationCode.DATA_IS_NOT_ALLOWED);
        }
        
        //이미지만 허용
        if(filePropertiesResponse.getPathTypeCd() != RepositoryPathTypeCd.images) {
            //이미지 리소스만 다운로드 가능합니다
            throw new PaletteValidationException(PaletteValidationCode.REJECTED_ONLY_IMAGES);
        }
        
        //파일 정보 가져오기
        FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder()
                                                                              .custcoId(infraDownloadImageResourceRequest.getCustcoId())
                                                                              .fileGroupKey(infraDownloadImageResourceRequest.getFileGroupKey())
                                                                              .fileKey(infraDownloadImageResourceRequest.getFileKey())
                                                                              .fileAcsTypeCd(infraDownloadImageResourceRequest.getFileAcsTypeCd())
                                                                              .userId(1)
                                                                              .build();
        // 리소스 로드
        FileDownloadResponse fileDownloadResponse = fileDownloadUtils.loadAsResource(filePropertiesResponse, fileDbMngSelectRequest);
        log.debug("fileDownloadResponse>>> {}", fileDownloadResponse);
        
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
                             .body(null);
        
    }
    // @formatter:on
}
