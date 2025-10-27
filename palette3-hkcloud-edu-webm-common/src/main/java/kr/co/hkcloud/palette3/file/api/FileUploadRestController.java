package kr.co.hkcloud.palette3.file.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.model.PaletteApiResponse;
import kr.co.hkcloud.palette3.exception.PaletteApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.file.args.FileRuleProperties;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequests;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileUploadResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import kr.co.hkcloud.palette3.file.util.FileUploadUtils;
import kr.co.hkcloud.palette3.file.util.FileUploadValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "FileUploadRestController", description = "파일업로드 REST 컨트롤러")
public class FileUploadRestController {

    private final FileUploadValidator fileUploadValidator;
    private final FileUploadUtils fileUploadUtils;
    private final FileRuleUtils fileRuleUtils;
    public static String FileASP = "init";


    /**
     * 멀티 파일 업로드
     */
    @ApiOperation(value = "멀티 파일 업로드", notes = "멀티 파일을 업로드한다.")
    @PostMapping("/api/file/{taskTypeCd}/{pathTypeCd}/uploads")
    public ResponseEntity<PaletteApiResponse<?>> uploadFiles(HttpServletRequest request,
        @FileRuleProperties @Valid final FilePropertiesResponse filePropertiesResponse, @Valid final FileUploadRequests fileUploadRequests,
        final BindingResult bindingResult) throws PaletteApiException {

        if (filePropertiesResponse.getTrgtTypeCd() == null) {
            filePropertiesResponse.setTrgtTypeCd(RepositoryTrgtTypeCd.FILE);
        }

        log.debug("filePropertiesResponse: {}", filePropertiesResponse);
        log.debug("uploadRequests: {}", fileUploadRequests);

        //Validation 체크
        Map<String, Object> validateMap = new HashMap<String, Object>();
        validateMap.put("filePropertiesResponse", filePropertiesResponse);
        validateMap.put("fileUploadRequests", fileUploadRequests);

        fileUploadValidator.validate(validateMap, bindingResult);

        List<FileUploadResponse> uploadResponseList = fileUploadUtils.store(filePropertiesResponse, fileUploadRequests);

        log.debug("uploadResponseList=============" + uploadResponseList);
        //응답
        
        return new ResponseEntity<PaletteApiResponse<?>>(PaletteApiResponse.res(uploadResponseList), HttpStatus.OK);
    }


    @ApiOperation(value = "FILE_GROUP_KEY 조회", notes = "FILE_GROUP_KEY 조회")
    @PostMapping("/api/file/file-group-key/inqire")
    public Object getFileGroupKey(@TelewebJsonParam TelewebJSON mjsonParam) throws TelewebApiException {
        TelewebJSON rtnJson = new TelewebJSON();

        String fileGroupKey = mjsonParam.getString("FILE_GROUP_KEY");

        if (fileGroupKey == null || fileGroupKey.equals("")) {
            fileGroupKey = fileRuleUtils.creatFileKey();
        }

        rtnJson.setString("FILE_GROUP_KEY", fileGroupKey);

        return rtnJson;
    }
}
