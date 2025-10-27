package kr.co.hkcloud.palette3.phone.script.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.script.app.PhoneScriptManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneScriptManageRestController",
     description = "스크립트관리 REST 컨트롤러")
public class PhoneScriptManageRestController
{
    private final PhoneScriptManageService phoneScriptManageService;


    /**
     * 스크립트 저장
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트 저장",
                  notes = "스크립트 저장")
    @PostMapping("/phone-api/script/manage/regist")
    public Object insertRtnScriptMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptManageService.insertRtnScriptMng(mjsonParams);

        return objRetParams;

    }


    /**
     * 스크립트 업데이트
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트 업데이트",
                  notes = "스크립트 업데이트.")
    @PostMapping("/phone-api/script/manage/modify")
    public Object updateRtnScriptMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptManageService.updateRtnScriptMng(mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트정보 삭제
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트정보 삭제",
                  notes = "스크립트정보 삭제")
    @PostMapping("/phone-api/script/manage/delete")
    public Object deleteRtnScriptMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptManageService.deleteRtnScriptMng(mjsonParams);

        return objRetParams;
    }

    /**
     * 첨부파일 삭제
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
//    @ApiOperation(value = "첨부파일 삭제",
//                  notes = "첨부파일을 삭제한다")
//    @PostMapping("/phone-api/script/manage/atchmnfl/delete")
//    public Object deleteRtnAttachFiles(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
//    {
//        //client에서 전송받은 파라메터 정의
////        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
//        //반환 파라메터 생성
//        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
//        //DB Access 파라메터 생성
//        TelewebJSON objParams01 = new TelewebJSON(mjsonParams);
//        TelewebJSON objParams02 = new TelewebJSON(mjsonParams);
//
//        objRetParams.setHeader("ERROR_FLAG", false);
//        int _idx01 = 0;
//        int _idx02 = 0;
//
//        JSONArray objArray = mjsonParams.getDataObject();
//        JSONObject objJson = new JSONObject();
//        if(!objArray.isEmpty()) {
//            for(int i = 0; i < objArray.size(); i++) {
//                objJson = objArray.getJSONObject(i);
//                if(!objJson.isEmpty() && !objJson.isNullObject()) {
//                    if(objJson.containsKey("FILE_GROUP_KEY") && !"".equals(objJson.getString("FILE_GROUP_KEY")) && objJson.containsKey("FILE_KEY") && !"".equals(objJson.getString("FILE_KEY"))) {
//                        objParams01.setString("FILE_GROUP_KEY", _idx01, objJson.getString("FILE_GROUP_KEY"));
//                        objParams01.setString("FILE_KEY", _idx01, objJson.getString("FILE_KEY"));
//                        _idx01++;
//                    }
//                    else if(objJson.containsKey("FILENAME") && !"".equals(objJson.getString("FILENAME"))) {
//                        objParams02.setString("FILENAME", _idx02, objJson.getString("FILENAME"));
//                        _idx02++;
//                    }
//                }
//            }
//        }
//        if(_idx01 > 0) {
//            //첨부파일 삭제(키 삭제)
//            // [파일] objComFileBiz.deleteFile: 전화-스크립트-첨부파일삭제
//            objComFileBiz.deleteFile(objParams01);
//        }
//        if(_idx02 > 0) {
//            //첨부파일 삭제(임시파일 삭제)
//            // [파일] objComFileBiz.deleteTempFile: 전화-스크립트-임시파일삭제
//            objComFileBiz.deleteTempFile(objParams02);
//        }
//
//        //테이블의 그룹파일키 널 업데이터 처리 => 타겟테이블 지정으로 변경
//        checkAttachFilCnt(mjsonParams);
//
//        //최종결과값 반환
//        return objRetParams;
//    }
//
//
//    /**
//     * 데이터 처리시 데이터체크 템플릿
//     * 
//     * @param  jsonParams          전송된 파라메터 데이터
//     * @param  objRetParams        반환파라메터
//     * @return                     true:처리가능상태, false:처리불가능상태
//     * @throws TelewebApiException
//     */
//    private void checkAttachFilCnt(TelewebJSON jsonParams) throws TelewebApiException
//    {
//        //반환 파라메터 생성
//        TelewebJSON objRetParams = new TelewebJSON();
//        phoneScriptManageService.checkAttachFilCnt(jsonParams);
//    }


    /**
     * 스크립트 변경요청
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트 변경요청",
                  notes = "스크립트 변경요청")
    @PostMapping("/phone-api/script/manage/request")
    public Object requestRtnScriptMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptManageService.requestRtnScriptMng(mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트 변경승인
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트 변경승인",
                  notes = "스크립트 변경승인")
    @PostMapping("/phone-api/script/manage/fin-request")
    public Object finRequestRtnScriptMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptManageService.finRequestRtnScriptMng(mjsonParams);

        return objRetParams;
    }

}
