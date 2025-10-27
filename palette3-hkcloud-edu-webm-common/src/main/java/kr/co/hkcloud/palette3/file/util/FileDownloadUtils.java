package kr.co.hkcloud.palette3.file.util;


import javax.validation.Valid;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.exception.PaletteUtilException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.app.FileStorageService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectTargetTypeResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 파일 다운로드 유틸
 * 
 * @author leeiy
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@Component
public class FileDownloadUtils
{
    private final FileDbMngService   fileDbMngService;
    private final FileStorageService fileStorageService;
    private final FileStorageService fileStorageDbService;


    /**
     * 리소스 로드
     * 
     * @param  filePropertiesResponse
     * @param  fileDbMngSelectRequest
     * @return
     * @throws PaletteUtilException
     */
    public FileDownloadResponse loadAsResource(@Valid final FilePropertiesResponse filePropertiesResponse, @Valid final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteUtilException
    {
        final FileDbMngSelectTargetTypeResponse fileDbMngSelectTargetTypeResponse = fileDbMngService.selectRepositoryTrgtTypeCd(fileDbMngSelectRequest);
        log.debug("fileDbMngSelectTargetTypeResponse : {}", fileDbMngSelectTargetTypeResponse);

        // Load file as Resource
        FileDownloadResponse fileDownloadResponse = null;
        if(fileDbMngSelectTargetTypeResponse != null) {
            switch(fileDbMngSelectTargetTypeResponse.getTrgtTypeCd())
            {
                case FILE:
                case EDITOR:
                {
                    fileDownloadResponse = fileStorageService.loadAsResource(filePropertiesResponse, fileDbMngSelectRequest);
                    break;
                }
                case DB:
                {
                    fileDownloadResponse = fileStorageDbService.loadAsResource(filePropertiesResponse, fileDbMngSelectRequest);
                    break;
                }
            }
        }
        return fileDownloadResponse;
    }
}
