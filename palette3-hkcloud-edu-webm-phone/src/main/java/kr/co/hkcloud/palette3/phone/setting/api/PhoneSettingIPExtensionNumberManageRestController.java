package kr.co.hkcloud.palette3.phone.setting.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.setting.app.PhoneSettingIPExtensionNumberManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController("PhoneSettingIPExtensionNumberManageRestController")
@Api(value = "PhoneSettingIPExtensionNumberManageRestController",
     description = "ip내선번호관리 REST 컨트롤러")
public class PhoneSettingIPExtensionNumberManageRestController
{
    private final PhoneSettingIPExtensionNumberManageService PhoneSettingIPExtensionNumberManageService;


    /**
     * ip내선번호관리 목록 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 목록 조회",
                  notes = "ip내선번호관리 목록 조회")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/list")
    public Object selectRtnPhoneSettingIPExtensionNumberManage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = PhoneSettingIPExtensionNumberManageService.selectRtnPhoneSettingIPExtensionNumberManage(jsonParams);

        return objRetParams;
    }


    /*
     * ip 내선번호관리 내선번호 존재여부
     * 
     * @param mjsonParams
     * 
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 내선번호 존재여부",
                  notes = "ip내선번호관리 내선번호 존재여부")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/inlne_no_check")
    public Object insertInlneNoCheckRtnPhoneSettingIPExtensionNumberManage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = PhoneSettingIPExtensionNumberManageService.insertInlneNoCheckRtnPhoneSettingIPExtensionNumberManage(jsonParams);
        return objRetParams;
    }


    /**
     * ip내선번호관리 중복체크
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 중복체크",
                  notes = "ip내선번호관리 중복체크")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/check")
    public Object insertCheckRtnPhoneSettingIPExtensionNumberManage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = PhoneSettingIPExtensionNumberManageService.insertCheckRtnPhoneSettingIPExtensionNumberManage(jsonParams);

        return objRetParams;
    }


    /*
     * ip 내선번호관리 해당 사용자 id에 따른 내선번호관리 자동채우기
     * 
     * @param mjsonParams
     * 
     * @return
     */
    @ApiOperation(value = "ip 내선번호관리 해당 사용자 id에 따른 내선번호관리 자동채우기",
                  notes = "ip 내선번호관리 해당 사용자 id에 따른 내선번호관리 자동채우기")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/fill_Inlne_no")
    public Object insertInlneNoFillRtnPhoneSettingIPExtensionNumberManage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = PhoneSettingIPExtensionNumberManageService.insertInlneNoFillRtnPhoneSettingIPExtensionNumberManage(jsonParams);

        return objRetParams;
    }


    /*
     * ip내선번호관리 호분배
     * 
     * @param mjsonParams
     * 
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 호분배",
                  notes = "ip내선번호관리 호분배")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/userBatchImpl")
    public Object UserBatchInterface(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        return PhoneSettingIPExtensionNumberManageService.UserBatchInterface(mjsonParams);
    }


    /**
     * ip내선번호관리 등록
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 등록",
                  notes = "ip내선번호관리 등록")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/registe")
    public Object insertRtnPhoneSettingIPExtensionNumberManage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = PhoneSettingIPExtensionNumberManageService.insertRtnPhoneSettingIPExtensionNumberManage(jsonParams);

        return objRetParams;
    }


    /**
     * ip내선번호관리 수정
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 수정",
                  notes = "ip내선번호관리 수정")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/modify")
    public Object updateRtnPhoneSettingIPExtensionNumberManage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = PhoneSettingIPExtensionNumberManageService.updateRtnPhoneSettingIPExtensionNumberManage(jsonParams);

        return objRetParams;
    }


    /*
     * ip내선번호관리 삭제 전 CUSTCO_ID에 2개 이상의 회사명이 들어가있는지 확인
     * 
     * @param mjsonParams
     * 
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 삭제 전 CUSTCO_ID에 2개 이상의 회사명이 들어가있는지 확인",
                  notes = "ip내선번호관리 삭제 전 CUSTCO_ID에 2개 이상의 회사명이 들어가있는지 확인")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/aspkey_check")
    public Object selectAspkeyCheckPhoneSettingIPExtensionNumberManage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = PhoneSettingIPExtensionNumberManageService.selectAspkeyCheckPhoneSettingIPExtensionNumberManage(jsonParams);

        return objRetParams;
    }


    /*
     * ip내선번호관리 CUSTCO_ID에 2개 이상의 회사명 업데이트
     * 
     * @param mjsonParams
     * 
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 CUSTCO_ID에 2개 이상의 회사명 업데이트",
                  notes = "ip내선번호관리 CUSTCO_ID에 2개 이상의 회사명 업데이트")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/aspkey_update")
    public Object updateAspkeyCheckPhoneSettingIPExtensionNumberManage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = PhoneSettingIPExtensionNumberManageService.updateAspkeyCheckPhoneSettingIPExtensionNumberManage(jsonParams);

        return objRetParams;
    }


    /**
     * ip내선번호관리 삭제
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 삭제",
                  notes = "ip내선번호관리 삭제")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/delete")
    public Object deleteRtnPhoneSettingIPExtensionNumberManage(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        JSONArray objArry = mjsonParams.getDataObject();
        JSONObject objJson = new JSONObject();

        if(!objArry.isEmpty()) {
            for(int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if(!objJson.isEmpty() && !objJson.isNullObject()) {
                    objParams.setDataObject(JSONArray.fromObject(objJson));
                    PhoneSettingIPExtensionNumberManageService.deleteRtnPhoneSettingIPExtensionNumberManage(objParams);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * ip내선번호관리 삭제
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "ip내선번호관리 삭제",
                  notes = "ip내선번호관리 삭제")
    @PostMapping("/phone-api/setting/ip-lxtn-no-managee/delete2")
    public Object deleteRtnPhoneSettingIPExtensionNumberManage2(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = PhoneSettingIPExtensionNumberManageService.deleteRtnPhoneSettingIPExtensionNumberManage(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
}
