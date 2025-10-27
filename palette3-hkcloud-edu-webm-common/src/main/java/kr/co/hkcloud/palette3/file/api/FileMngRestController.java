package kr.co.hkcloud.palette3.file.api;


import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.model.PaletteApiResponse;
import kr.co.hkcloud.palette3.exception.PaletteApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngUpdateRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectListResponse;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileDeleteRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileSelectListRequst;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "FileMngRestController",
     description = "파일관리 REST 컨트롤러")
public class FileMngRestController
{
    private final FileDbMngService fileDbMngService;


    /**
     * 다운로드 수 증가
     * 
     * @param  fileUpdateRequest
     * @return
     * @throws PaletteApiException
     */
    @ApiOperation(value = "다운로드 수 증가",
                  notes = "파일 다운로드 수를 증가 처리한다.")
    @PostMapping("/api/file/update-dnlod-cnt")
    public ResponseEntity<PaletteApiResponse<?>> updateDnlodCnt(@Valid final FileUpdateRequest fileUpdateRequest) throws PaletteApiException
    {
        log.debug("fileUpdateRequest: {}", fileUpdateRequest);

        //다운로드 증가 요청
        FileDbMngUpdateRequest fileDbMngUpdateRequest = FileDbMngUpdateRequest.builder().fileGroupKey(fileUpdateRequest.getFileGroupKey()).fileKey(fileUpdateRequest.getFileKey()).amdrId(fileUpdateRequest.getUserId())
            .custcoId(fileUpdateRequest.getCustcoId()).userId(1).build();
        //BeanUtils.copyProperties(fileUpdateRequest, fileDbMngUpdateRequest);
        log.debug("fileDbMngUpdateRequest={}", fileDbMngUpdateRequest);

        fileDbMngService.updateDnlodCnt(fileDbMngUpdateRequest);

        //응답
        return new ResponseEntity<PaletteApiResponse<?>>(PaletteApiResponse.res(), HttpStatus.OK);
    }


    /**
     * 파일 삭제
     * 
     * @param  fileDeleteRequests
     * @return
     * @throws PaletteApiException
     */
    @ApiOperation(value = "파일 삭제",
                  notes = "파일을 그룹키와 파일키로 삭제한다")
    @PostMapping("/api/file/delete")
    public ResponseEntity<PaletteApiResponse<?>> delete(@Valid @RequestBody final FileDeleteRequest fileDeleteRequest) throws PaletteApiException
    {
        log.debug("fileDeleteRequest: {}", fileDeleteRequest);

        fileDbMngService.delete(fileDeleteRequest);

        //응답
        return new ResponseEntity<PaletteApiResponse<?>>(PaletteApiResponse.res(), HttpStatus.OK);
    }


    /**
     * 파일 목록 조회
     * 
     * @param  fileSelectListRequst
     * @return
     * @throws PaletteApiException
     */
    @ApiOperation(value = "파일 목록 조회",
                  notes = "파일 목록을 조회한다")
    @PostMapping("/api/file/select-list")
    public ResponseEntity<PaletteApiResponse<?>> selectList(@Valid @RequestBody final FileSelectListRequst fileSelectListRequst) throws PaletteApiException
    {
        log.debug("fileSelectListRequst: {}", fileSelectListRequst);

        FileDbMngSelectRequest fileDbMngSelectRequest = new FileDbMngSelectRequest();
        BeanUtils.copyProperties(fileSelectListRequst, fileDbMngSelectRequest);
        log.debug("fileDbMngSelectRequest={}", fileDbMngSelectRequest);

        List<FileDbMngSelectListResponse> fileDbMngSelectListResponseList = fileDbMngService.selectList(fileDbMngSelectRequest);
        log.debug("fileDbMngSelectListResponseList={}", fileDbMngSelectListResponseList);

        //크기(for human)
        for(FileDbMngSelectListResponse fileDbMngSelectListResponse : fileDbMngSelectListResponseList) {
            fileDbMngSelectListResponse.setFileSzDisplay(FileUtils.byteCountToDisplaySize(fileDbMngSelectListResponse.getFileSz()));
        }

        //응답
        return new ResponseEntity<PaletteApiResponse<?>>(PaletteApiResponse.res(fileDbMngSelectListResponseList), HttpStatus.OK);
    }


    @ApiOperation(value = "파일 목록 조회 (TelewebJson)",
                  notes = "파일 목록 조회(TelewebJson)")
    @PostMapping("/api/file/json/list")
    public Object selectFileList(@TelewebJsonParam TelewebJSON mjsonParam) throws TelewebApiException
    {
        TelewebJSON rtnJson = new TelewebJSON();

        rtnJson = fileDbMngService.selectFileList(mjsonParam);

        return rtnJson;
    }
}
