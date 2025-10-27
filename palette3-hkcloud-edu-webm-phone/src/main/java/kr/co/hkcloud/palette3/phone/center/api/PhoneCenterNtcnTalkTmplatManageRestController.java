package kr.co.hkcloud.palette3.phone.center.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.center.app.PhoneCenterNtcnTalkTmplatManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * <pre>
 * 시스템명 : 신협 공제 고객센터 시스템(S/W)
 * 업무구분 : 알림톡템플릿관리
 * 파 일 명 : PhoneCenterNtcnTalkTmplatManageRestController.java
 * 작 성 자 : 현은지
 * 작 성 일 : 2021.04.29
 * 설    명 : 알림톡템플릿관리 컨트롤러 클래스
 * --------------------------------------------------------------------------------
 * 변경일            변경자  변경내역
 * --------------------------------------------------------------------------------
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneCenterNtcnTalkTmplatManageRestController",
     description = "알림톡템플릿관리 REST 컨트롤러")
public class PhoneCenterNtcnTalkTmplatManageRestController
{
    /** 알림톡템플릿관리 서비스 */
    private final PhoneCenterNtcnTalkTmplatManageService phoneCenterNtcnTalkTmplatManageService;
    private final InnbCreatCmmnService                   innbCreatCmmnService;


    /**
     * 데이터 조회 템플릿
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "알림톡템플릿관리 데이터 조회",
                  notes = "알림톡템플릿관리 데이터 조회")
    @PostMapping("/phone-api/center/ntcn-talk/manage/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        // 알림톡템플릿관리 목록 조회
        objRetParams = phoneCenterNtcnTalkTmplatManageService.selectNtcnTalkMngList(mjsonParams);
        log.debug("objRetParams========" + objRetParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 데이터 처리 템플릿
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "알림톡템플릿관리 데이터 처리",
                  notes = "알림톡템플릿관리 데이터 처리")
    @PostMapping("/phone-api/center/ntcn-talk/manage/process")
    public Object processRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정
        String seq = innbCreatCmmnService.getSeqNo("PLT_PHN_SMS_TMPL");
        // 센터구분, SMS유형, SMS템플릿 존재 여부 확인 용도
        TelewebJSON rtnMap = new TelewebJSON(mjsonParams);                      //반환 파라메터 생성

        // 알림톡템플릿관리 건수 조회
        rtnMap = phoneCenterNtcnTalkTmplatManageService.selectNtcnTalkMngCnt(mjsonParams);
        int cnt = Integer.parseInt(rtnMap.getString("CNT"));
        mjsonParams.setString("SEQ", seq);
        // 신규/수정 상태에 따라 insert, update 함수 호출
        if(strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
            if(cnt < 1) {
                // 알림톡템플릿관리 등록
                objRetParams = phoneCenterNtcnTalkTmplatManageService.insertNtcnTalkMng(mjsonParams);
            }
        }
        else if(strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
            if(cnt < 1) {
                // 알림톡템플릿관리 수정
                mjsonParams.setBlankToNull(0);                      //빈값에 대한 NULL 처리
                objRetParams = phoneCenterNtcnTalkTmplatManageService.updateNtcnTalkMng(mjsonParams);
            }
        }

        objRetParams.setString("NTCN_TALK_CNT", Integer.toString(cnt));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 데이터 삭제 템플릿
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "알림톡템플릿관리 데이터 삭제",
                  notes = "알림톡템플릿관리 데이터 삭제")
    @PostMapping("/phone-api/center/ntcn-talk/manage/delete")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        objRetParams = phoneCenterNtcnTalkTmplatManageService.deleteNtcnTalkMng(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
