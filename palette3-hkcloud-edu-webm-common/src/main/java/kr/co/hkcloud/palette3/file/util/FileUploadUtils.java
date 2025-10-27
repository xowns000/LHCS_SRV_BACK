package kr.co.hkcloud.palette3.file.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.exception.PaletteAppException;
import kr.co.hkcloud.palette3.file.app.FileStorageService;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequests;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileUploadResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 파일 업로드 유틸
 * 
 * @author leeiy
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@Component
public class FileUploadUtils {

    private final FileRuleUtils fileRuleUtils;
    private final FileStorageService fileStorageService;
    private final FileStorageService fileStorageDbService;

    /**
     * 파일 저장
     * 
     * @param filePropertiesResponse
     * @param fileUploadRequests
     * @return
     * @throws PaletteAppException
     */
    @NotNull
    public List<FileUploadResponse> store(@Valid final FilePropertiesResponse filePropertiesResponse,
        @Valid final FileUploadRequests fileUploadRequests) throws PaletteAppException {

        log.debug("fileUploadRequests ============" + fileUploadRequests);
        log.debug("filePropertiesResponse ============" + filePropertiesResponse);

        //그룹키 생성 (있으면 사용하고, 없으면 생성한다)
        String fileGroupKey = fileRuleUtils.ifCreatFileGroupKey(fileUploadRequests);

        //응답용
        List<FileUploadResponse> uploadResponseList = new ArrayList<>();

        //파일처리
        MultipartFile[] files = fileUploadRequests.getUserfiles();
        Arrays.asList(files).stream().forEach(file -> {
            //파일 empty 체크
            if (file.isEmpty()) {
                //무시한다.
                log.info("{} file empty skip!", file.getOriginalFilename());
                return; // only skips this iteration.
            }

            // @formatter:off
            FileUploadRequest fileUploadRequest;
            log.debug("fileUploadRequests ====================="+fileUploadRequests);
            if ( !StringUtils.isEmpty(fileUploadRequests.getCustcoId()) && !StringUtils.isEmpty(fileUploadRequests.getUserId()))
            {
                log.debug("here1");
                fileUploadRequest = FileUploadRequest.builder()
                                                     .file(file)
                                                     .fileAcsTypeCd(fileUploadRequests.getFileAcsTypeCd())
                                                     .fileGroupKey(fileGroupKey)
                                                     .custcoId(filePropertiesResponse.getCustcoId())
                                                     .userId(fileUploadRequests.getUserId())
                                                     .build();
                System.out.println("TEST7 ====================="+fileUploadRequest);
            }
            else
            {
                log.debug("here2");
                fileUploadRequest = FileUploadRequest.builder()
                                                     .file(file)
                                                     .fileAcsTypeCd(fileUploadRequests.getFileAcsTypeCd())
                                                     .fileGroupKey(fileGroupKey)
                                                     .custcoId(filePropertiesResponse.getCustcoId())
                                                     .build();           
                System.out.println("TEST8 ====================="+fileUploadRequest);
              //  System.out.println("TEST9 ====================="+FileUploadRestController.FileASP);
            }
           
            
            
            // @formatter:on            

            //저장소대상구분
            final RepositoryTrgtTypeCd trgtTypeCd = filePropertiesResponse.getTrgtTypeCd();
            switch (trgtTypeCd) {
                //파일 / 에디터
                case FILE:
                case EDITOR: {
                    uploadResponseList.add(fileStorageService.store(filePropertiesResponse, fileUploadRequest));
                    break;
                }
                //DB
                case DB: {
                    uploadResponseList.add(fileStorageDbService.store(filePropertiesResponse, fileUploadRequest));
                    break;
                }
            }

        });
        log.debug("uploadResponseList :{}", uploadResponseList);
        return uploadResponseList;
    }
}
