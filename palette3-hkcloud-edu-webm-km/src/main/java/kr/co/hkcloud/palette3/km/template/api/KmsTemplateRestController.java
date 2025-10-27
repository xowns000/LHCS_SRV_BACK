package kr.co.hkcloud.palette3.km.template.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.km.template.app.KmsTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "KmsTemplateRestController", description = "지식템플릿상세 REST 컨트롤러")
public class KmsTemplateRestController {

    private final KmsTemplateService kmsTemplateService;


    @ApiOperation(value = "지식템플릿-조회", notes = "지식템플릿 리스트 조회")
    @PostMapping("/api/km/template/list")
    public Object list(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();        //반환파라메터생성

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //DAO검색 메서드 호출
        objRetParams = kmsTemplateService.selectPageList(jsonParams);

        return objRetParams;

    }

    @ApiOperation(value = "지식템플릿상세-조회", notes = "지식템플릿상세 템플릿 조회")
    @PostMapping("/api/km/template/detail")
    public Object detail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();        //반환파라메터생성

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //DAO검색 메서드 호출
        objRetParams = kmsTemplateService.selectRtnDetail(jsonParams);

        return objRetParams;

    }

    @SystemEventLogAspectAnotation(value = "KM_TEMPLATE_PROC", note = "지식 템플릿 변경(등록,수정)")
    @ApiOperation(value = "지식템플릿-등록,수정", notes = "지식템플릿상세 템플릿 등록,수정")
    @PostMapping("/api/km/template/update")
    public Object regRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();        //반환파라메터생성

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        String kmsTmplId = jsonParams.getString("KMS_TMPL_ID", 0, "");

        if ("".equals(kmsTmplId)) {
            objRetParams = kmsTemplateService.regRtn(jsonParams);
        } else {
            objRetParams = kmsTemplateService.updateRtn(jsonParams);
        }

        return objRetParams;

    }

    @SystemEventLogAspectAnotation(value = "KM_TEMPLATE_DEL", note = "지식 템플릿 삭제")
    @ApiOperation(value = "지식템플릿상세-삭제", notes = "지식템플릿상세 템플릿 삭제")
    @PostMapping("/api/km/template/delete")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        //필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();        //반환파라메터생성

        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        String userId = jsonParams.getString("USER_ID");
        jsonParams.setInt("MDFR_ID", Integer.parseInt(userId));
        String list = jsonParams.getString("list").toString();
        list = list.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("list", list);

        //DAO검색 메서드 호출
        objRetParams = kmsTemplateService.deleteRtn(jsonParams);

        return objRetParams;

    }

}
