package kr.co.hkcloud.palette3.phone.qa2.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteDataFormatUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.qa2.app.PhoneQAPlanManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneQAPlanManageRestController")
@Api(value = "PhoneQAPlanManageRestController",
     description = "QA평가기획 REST 컨트롤러")
public class PhoneQAPlanManageRestController
{
    private final PhoneQAPlanManageService phoneQAPlanManageService;

    private final PaletteDataFormatUtils paletteDataFormatUtils;


    @ApiOperation(value = "QA평가기획 목록 조회",
                  notes = "QA평가기획 목록 조회")
    @PostMapping("/phone-api/plan/qa-manage/list")
    Object selectRtnQa(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAPlanManageService.selectRtnQa(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가기획 등록",
                  notes = "QA평가기획 등록")
    @PostMapping("/phone-api/plan/qa-manage/regist")
    Object insertRtnQa(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAPlanManageService.insertRtnQa(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가기획 평가지 조회",
                  notes = "QA평가기획 평가지 조회")
    @PostMapping("/phone-api/plan/qa-manage/evl-paper/list")
    Object selectRtnEvlPaper(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAPlanManageService.selectRtnEvlPaper(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가기획 삭제",
                  notes = "QA평가기획 삭제")
    @PostMapping("/phone-api/plan/qa-manage/delete")
    Object deleteRtnQa(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParam = new TelewebJSON(mjsonParams);
//        jsonParam.setDataObject(mjsonParams.getDataObject("grid01Selected"));

        objRetParams = phoneQAPlanManageService.deleteRtnQa(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가기획 추가정보 등록",
                  notes = "QA평가기획 추가정보 등록")
    @PostMapping("/phone-api/plan/qa-manage/detail/regist")
    Object updateRtnQa(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParam = new TelewebJSON(mjsonParams);
        jsonParam.setDataObject(mjsonParams.getDataObject());

        objRetParams = phoneQAPlanManageService.updateRtnQa(jsonParam);

        return objRetParams;
    }


    @ApiOperation(value = "QA평가기획 대상 발췌 조회",
                  notes = "QA평가기획 대상 발췌 조회")
    @PostMapping("/phone-api/plan/qa-manage/cnsl/list")
    Object selectRtnCnslHist(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String qaTgTy = mjsonParams.getString("QA_TG_TY");

        if(qaTgTy.equals("10")) {
            //콜 상담 이력
            objRetParams = phoneQAPlanManageService.selectRtnPhoneCnslHist(mjsonParams);
        }
        else if(qaTgTy.equals("20")) {
            //채팅 상담 이력
            objRetParams = phoneQAPlanManageService.selectRtnChatCnslHist(mjsonParams);
            //고객명 마스킹 처리
            if(objRetParams != null) {
                int retSize = objRetParams.getHeaderInt("COUNT");

                if(retSize > 0) {
                    for(int i = 0; i < retSize; i++) {
                        String strCustNm = objRetParams.getString("CUSTOMER_NM", i);
                        objRetParams.setString("CUSTOMER_NM", i, paletteDataFormatUtils.getFormatData("class_nameEnc", strCustNm));
                    }
                }
            }
        }

        return objRetParams;
    }


    @ApiOperation(value = "QA기획 QA할당 건수",
                  notes = "QA기획 QA할당 건수")
    @PostMapping("/phone-api/plan/qa-manage/div/cnt/list")
    Object selectRtnQaDivCnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAPlanManageService.selectRtnQaDivCnt(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA기획 QA할당 목록",
                  notes = "QA기획 QA할당 목록")
    @PostMapping("/phone-api/plan/qa-manage/div/list")
    Object selectRtnQaDiv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAPlanManageService.selectRtnQaDiv(mjsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "QA기획 QA분배/회수 처리",
                  notes = "QA기획 QA분배/회수 처리")
    @PostMapping("/phone-api/plan/qa-manage/div/regist")
    Object processRtnQaDiv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneQAPlanManageService.processRtnQaDiv(mjsonParams);

        return objRetParams;
    }
}
