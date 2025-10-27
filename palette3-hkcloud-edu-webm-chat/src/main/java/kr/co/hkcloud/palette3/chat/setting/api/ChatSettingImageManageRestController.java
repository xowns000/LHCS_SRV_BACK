package kr.co.hkcloud.palette3.chat.setting.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingImageManageService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingImageManageValidator;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.util.FileDownloadUtils;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatSettingImageManageRestController", description = "채팅설정이미지관리 REST 컨트롤러")
public class ChatSettingImageManageRestController {

    private final FileRulePropertiesUtils fileRulePropertiesUtils;
    private final ChatSettingImageManageValidator chatSettingImageManageValidator;
    private final ChatSettingImageManageService chatSettingImageManageService;
    private final FileDbMngService fileDbMngService;
    private final FileDownloadUtils fileDownloadUtils;


    /**
     * @param  mjsonParams
     * @param  result
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "채팅설정이미지관리-목록-조회", notes = "채팅설정이미지관리 목록을 조회한다")
    @PostMapping("/chat-api/setting/image-manage/list")
    public Object selectRtnImgMng(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //validation 체크
        chatSettingImageManageValidator.validate(mjsonParams, result);
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        objRetParams = chatSettingImageManageService.selectRtnImgMng(mjsonParams);
        return objRetParams;
    }


    /**
     * @param  Object HashMap
     * @return TelewebJSON 형식의 ERROR_FLAG
     */
    @NoBizLog
    @ApiOperation(value = "이미지관리-이미지조회(FILE_GROUP_KEY, FILE_KEY)", notes = "이미지관리 FILE_GROUP_KEY, FILE_KEY로 이미지를 조회한다")
    @RequestMapping(value = "/chat-api/setting/image-manage/image/inqire/{fileGroupKey}/{fileKey}", method = RequestMethod.GET)
    public ResponseEntity<Resource> selectImageByFileKey(@PathVariable(required = true) String fileGroupKey,
        @PathVariable(required = true) String fileKey) throws TelewebApiException {
        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat; //채팅
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images; //이미지
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);

        // @formatter:off
        FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder()
                                                                              .fileGroupKey(fileGroupKey)
                                                                              .fileKey(fileKey)
                                                                              .build();

        FileDownloadResponse fileDownloadResponse = fileDownloadUtils.loadAsResource(fileProperties, fileDbMngSelectRequest);
        // @formatter:on

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        switch (fileDownloadResponse.getFileExtn().toLowerCase()) {
            case "jpeg":
            case "jpg":
                headers.setContentType(MediaType.IMAGE_JPEG);
                break;
            case "png":
                headers.setContentType(MediaType.IMAGE_PNG);
                break;
            case "gif":
                headers.setContentType(MediaType.IMAGE_GIF);
                break;
            default:
                break;
        }

        // @formatter:off
        if(fileDownloadResponse != null && fileDownloadResponse.getResource() != null) {
            return ResponseEntity.ok()
                                 .headers(headers)
                                 .body(fileDownloadResponse.getResource());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .headers(headers)
                             .body(fileDownloadResponse.getResource());
        // @formatter:on
    }
}
