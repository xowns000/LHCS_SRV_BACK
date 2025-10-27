package kr.co.hkcloud.palette3.phone.cutt.api;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.phone.cutt.app.CuttService;
import kr.co.hkcloud.palette3.setting.customer.app.SettingCustomerInformationListService;
import kr.co.hkcloud.palette3.vst.app.VstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "CuttRestController", description = "상담 REST 컨트롤러")
public class CuttRestController {

    private final CuttService cuttService;
    private final VstService vstService;
    private final SettingCustomerInformationListService settingCustomerInformationListService;

    @ApiOperation(value = "상담내용-등록", notes = "상담내용을 등록 한다")
    @PostMapping("/phone-api/cutt/cuttProc")
    public Object cuttProc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException, TelewebAppException, ParseException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
        objRetParams = cuttService.cuttProc(mjsonParams); //상담 내용 저장

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 
     * PALETTE3 통합 접촉 이력
     * 
     * @Method Name : custIntegHistList
     * @date : 2023. 6. 21.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 통합 접촉 이력", notes = "PALETTE3 통합 접촉 이력")
    @PostMapping("/phone-api/cutt/custIntegHistList")
    public Object custIntegHistList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
        String sAsAltmnt = mjsonParams.getString("AS_ALTMNT");

        //        objRetParams.setDataObject("DATA", settingCustomerInformationListService.custSelect(mjsonParams).getDataObject("DATA"));

        TelewebJSON cuttHistList = (TelewebJSON) this.integCuttHistList(mjsonParams); //반환 파라메터 생성

        objRetParams.setDataObject("INTEG_CUTT_HIST_LIST", cuttHistList.getDataObject("DATA")); //상담이력

        objRetParams.setDataObject("CUSL_EXPSN_ATTR_LIST", cuttHistList.getDataObject("CUSL_EXPSN_ATTR_LIST")); //통합 상담 이력 노출 확장속성 목록

        TelewebJSON newObjParams = mjsonParams;
        newObjParams.setHeader("ROW_CNT", 0);
        newObjParams.setHeader("PAGES_CNT", 0);

        //        objRetParams.setDataObject("VOC_LIST", ((TelewebJSON) this.vocList(newObjParams)).getDataObject("DATA")); //VOC

        objRetParams.setDataObject("MSG_LIST", ((TelewebJSON) this.msgList(newObjParams)).getDataObject("DATA")); //문자발송 이력

        objRetParams.setDataObject("RSVT_CALL_LIST", ((TelewebJSON) this.rsvtCallList(newObjParams)).getDataObject("DATA")); //예약콜

        objRetParams.setDataObject("CALL_BACK_LIST", ((TelewebJSON) this.callBackList(newObjParams)).getDataObject("DATA")); //콜백

        objRetParams.setDataObject("CPI_LIST", ((TelewebJSON) this.cpiList(newObjParams)).getDataObject("DATA")); //캠페인
        
        if("Y".equals(sAsAltmnt)) {
        	objRetParams.setDataObject("VST_RSVT_LIST", ((TelewebJSON) this.vstRsvtList(newObjParams)).getDataObject("DATA")); //방문 상담 이력
        }

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 
     * PALETTE3 상담 이력 목록
     * 
     * @Method Name : integCuttHistList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 상담 이력 목록", notes = "PALETTE3 상담 이력 목록")
    @PostMapping("/phone-api/cutt/integCuttHistList")
    public Object integCuttHistList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
        int iRowCnt = mjsonParams.getHeaderInt("ROW_CNT");
        int iPagesCnt = mjsonParams.getHeaderInt("PAGES_CNT");

        JSONArray arrExpsnAttrData = cuttService.cuslExpsrExpsnAttrList(mjsonParams).getDataObject(); //통합 상담 이력 노출 확장속성 목록
        JSONObject expsnAttrObj = new JSONObject();
        expsnAttrObj.put("CUSL_EXPSN_ATTR_LIST", arrExpsnAttrData);
        expsnAttrObj.put("CUSTCO_ID", mjsonParams.getString("CUSTCO_ID"));
        expsnAttrObj.put("USER_ID", mjsonParams.getString("USER_ID"));
        expsnAttrObj.put("CUST_ID", mjsonParams.getString("CUST_ID"));
        expsnAttrObj.put("PP_ALG_PP", mjsonParams.getString("PP_ALG_PP"));
        expsnAttrObj.put("PP_KEY_PP", mjsonParams.getString("PP_KEY_PP"));
        expsnAttrObj.put("ST_DT", mjsonParams.getString("ST_DT"));
        expsnAttrObj.put("END_DT", mjsonParams.getString("END_DT"));
        expsnAttrObj.put("AS_ALTMNT", mjsonParams.getString("AS_ALTMNT"));

        expsnAttrObj.put("CHNL", mjsonParams.getString("CHNL"));
        expsnAttrObj.put("CUSL_RS", mjsonParams.getString("CUSL_RS"));

        JSONArray arrParam = new JSONArray();
        arrParam.add(expsnAttrObj);
        mjsonParams.setDataObject(arrParam);

        mjsonParams.setHeader("ROW_CNT", iRowCnt);
        mjsonParams.setHeader("PAGES_CNT", iPagesCnt);
        objRetParams = cuttService.integCuttHistList(mjsonParams); //상담 이력 목록

        objRetParams.setDataObject("CUSL_EXPSN_ATTR_LIST", arrExpsnAttrData);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 
     * PALETTE3 VOC 목록
     * 
     * @Method Name : vocList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 VOC 목록", notes = "PALETTE3 VOC 목록")
    @PostMapping("/phone-api/cutt/vocList")
    public Object vocList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = cuttService.vocList(mjsonParams); //VOC 목록

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 
     * 문자발송 이력 목록
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 문자발송 이력 목록", notes = "PALETTE3 문자발송 이력 목록")
    @PostMapping("/phone-api/cutt/msgList")
    public Object msgList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = cuttService.msgList(mjsonParams); //문자발송 이력 목록

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 
     * PALETTE3 예약 콜 목록
     * 
     * @Method Name : rsvtCallList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 예약 콜 목록", notes = "PALETTE3 예약 콜 목록")
    @PostMapping("/phone-api/cutt/rsvtCallList")
    public Object rsvtCallList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        if (StringUtils.isEmpty(mjsonParams.getString("IS_ALL_ENV")))
            mjsonParams.setString("IS_ALL_ENV", "false");

        objRetParams = cuttService.rsvtCallList(mjsonParams); //예약 콜 목록
        if (StringUtils.isBlank(mjsonParams.getString("CUST_ID")) && StringUtils.isBlank(mjsonParams.getString("CHILD_DATA_YN")))
            objRetParams.setDataObject("MONITOR", cuttService.cuslRsvtCallMonitor(mjsonParams)); //상담원별 예약콜 처리 상태

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 
     * PALETTE3 콜백 목록
     * 
     * @Method Name : callBackList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 콜백 목록", notes = "PALETTE3 콜백 목록")
    @PostMapping("/phone-api/cutt/callBackList")
    public Object callBackList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = cuttService.callBackList(mjsonParams); //콜백 목록

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 
     * PALETTE3 캠페인 목록
     * 
     * @Method Name : cpiStatHistList
     * @date : 2023. 6. 22.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 캠페인 목록", notes = "PALETTE3 캠페인 목록")
    @PostMapping("/phone-api/cutt/cpiList")
    public Object cpiList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = cuttService.cpiList(mjsonParams); //예약 콜 목록

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 
     * 방문 예약 상담 이력
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 캠페인 목록", notes = "PALETTE3 캠페인 목록")
    @PostMapping("/phone-api/cutt/vstRsvtList")
    public Object vstRsvtList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
    	//반환 파라메터 생성
    	TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
    	
    	objRetParams = vstService.vstRsvtList(mjsonParams); //방문 상담 예약 목록
    	
    	//최종결과값 반환
    	return objRetParams;
    }

    /**
     * 
     * My 데스크 콜백, 예약콜, 캠페인 진행 현황
     * 
     * @Method Name : myDeskStat
     * @date : 2023. 7. 28.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 My 데스크 콜백, 예약콜, 캠페인 진행 현황", notes = "PALETTE3 My 데스크 콜백, 예약콜, 캠페인 진행 현황")
    @PostMapping("/phone-api/cutt/myDeskStat")
    public Object myDeskStat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성

        objRetParams = cuttService.myDeskStat(mjsonParams); //My 데스크 콜백, 예약콜, 캠페인 진행 현황

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 처리해야할 예약콜 있는지 여부.
     */
    @ApiOperation(value = "예약콜 알람 모니터링용", notes = "예약콜이 있을 경우 상담직원이 인지할 수 있도록 우측 사이드바의 해당 아이콘에 에니메이션 효과를 적용 용도.")
    @PostMapping("/phone-api/cutt/inqire/rsvtCallAlarmMonitor")
    public Object rsvtCallAlarmMonitor(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TelewebJSON objRetParams = cuttService.rsvtCallNoCompletedCnt(jsonParam); //반환 파라메터 생성
        return objRetParams;
    }
}