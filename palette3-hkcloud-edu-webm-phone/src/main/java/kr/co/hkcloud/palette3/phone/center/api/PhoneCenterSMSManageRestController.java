package kr.co.hkcloud.palette3.phone.center.api;


import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.center.app.PhoneCenterSMSManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * <pre>
 * 시스템명 : 신협 공제 고객센터 시스템(S/W)
 * 업무구분 : SMS관리
 * 파 일 명 : SmsMngRestController.java
 * 작 성 자 : 김대찬
 * 작 성 일 : 2020.01.06
 * 설    명 : SMS관리 컨트롤러 클래스
 * --------------------------------------------------------------------------------
 * 변경일            변경자  변경내역
 * --------------------------------------------------------------------------------
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneCenterSMSManageRestController",
     description = "SMS관리 REST 컨트롤러")
public class PhoneCenterSMSManageRestController
{
    /** SMS관리 서비스 */
    private final PhoneCenterSMSManageService phoneCenterSMSManageService;

    private final InnbCreatCmmnService innbCreatCmmnService;


    /**
     * 데이터 조회 템플릿
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "SMS관리 데이터 조회",
                  notes = "SMS관리 데이터 조회")
    @PostMapping("/phone-api/center/sms-manage/list")
    public Object selectRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        // SMS관리 목록 조회
        objRetParams = phoneCenterSMSManageService.selectSmsMngList(mjsonParams);
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
    @ApiOperation(value = "SMS관리 데이터 처리",
                  notes = "SMS관리 데이터 처리")
    @PostMapping("/phone-api/center/sms-manage/process")
    public Object processRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정
        String seq = innbCreatCmmnService.getSeqNo("PLT_PHN_SMS_TMPL");
        // 센터구분, SMS유형, SMS템플릿 존재 여부 확인 용도
        TelewebJSON rtnMap = new TelewebJSON(mjsonParams);                      //반환 파라메터 생성

        // SMS관리 건수 조회
        rtnMap = phoneCenterSMSManageService.selectSmsMngCnt(mjsonParams);
        int cnt = Integer.parseInt(rtnMap.getString("CNT"));
        mjsonParams.setString("SEQ", seq);
        // 신규/수정 상태에 따라 insert, update 함수 호출
        if(strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
            if(cnt < 1) {
                // SMS관리 등록
                objRetParams = phoneCenterSMSManageService.insertSmsMng(mjsonParams);
            }
        }
        else if(strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
            if(cnt < 1) {
                // SMS관리 수정
                mjsonParams.setBlankToNull(0);                      //빈값에 대한 NULL 처리
                objRetParams = phoneCenterSMSManageService.updateSmsMng(mjsonParams);
            }
        }

        objRetParams.setString("SMS_CNT", Integer.toString(cnt));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 데이터 삭제 템플릿
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "SMS관리 데이터 삭제",
                  notes = "SMS관리 데이터 삭제")
    @PostMapping("/phone-api/center/sms-manage/delete")
    public Object deleteRtn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        objRetParams = phoneCenterSMSManageService.deleteSmsMng(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


   
}
