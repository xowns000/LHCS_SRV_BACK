package kr.co.hkcloud.palette3.phone.history.api;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.PrvcAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.excel.api.ExcelDownController;
import kr.co.hkcloud.palette3.excel.app.ExcelService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.history.app.PhoneHistoryInqireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneHistoryInqireRestController", description = "상담이력관리 REST 컨트롤러")
public class PhoneHistoryInqireRestController {

    private final PhoneHistoryInqireService phoneHistoryInqireService;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final ExcelService excelService;

    /**
     * 상담원그룹정보 조회
     * 
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "상담원그룹정보 목록 조회", notes = "상담원그룹정보 목록 조회")
    @PostMapping("/phone-api/history/inqire/agent-group/inqire")
    public Object selectRtnTmKind(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = phoneHistoryInqireService.selectRtnTmKind(jsonParams);

        return objRetParams;
    }

    /**
     * //
     * 상담이력관리 목록 조회(공제지원)
     * 
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "상담이력관리 목록 조회", notes = "상담이력관리 목록 조회")
    @PostMapping("/phone-api/history/inqire/list")
    public Object selectRtnCnslHistMngDe(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //조회유형(SEARCH_TYPE)
        //String SEARCH_TYPE = mjsonParams.getString("SEARCH_TYPE");

        //조회유형(SEARCH_TYPE)이 시간(TIME)일 경우
        //if(StringUtils.equals(SEARCH_TYPE, "TIME")) {
        //조회시간 파라메터 배열 생성 후 조회파라메터에 설정
        List<String> arrSearchTime = new LinkedList<>();
        for (int idx = 0, iTimes = 23; idx <= iTimes; ++idx) {
            String SEARCH_TIME = mjsonParams.getString("SEARCH_TIME_" + idx);

            if (StringUtils.isNotEmpty(SEARCH_TIME)) {
                arrSearchTime.add(SEARCH_TIME);
            }
        }
        mjsonParams.setObject("arrSearchTime", 0, arrSearchTime);
        //}

        objRetParams = phoneHistoryInqireService.selectRtnCnslHistMngDe(mjsonParams);

        return objRetParams;
    }

    /**
     * 상담이력관리 수정(공제지원)
     * 
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "상담이력관리 수정", notes = "상담이력관리 수정")
    @PostMapping("/phone-api/history/manage/modify")
    public Object updateRtnCnslHistMngDe(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        log.debug("!@#!@#$$$$@@@@$$$$$$$$$$$$$$" + mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        String SEQ = innbCreatCmmnService.getSeqNo("CHNG_HIST_SEQ");
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        log.debug("jsonParams^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + jsonParams);

        java.util.Date now = new java.util.Date();
        SimpleDateFormat vans = new SimpleDateFormat("yyyyMMdd");
        String currentDate = vans.format(now);
        String regDate = mjsonParams.getString("REG_DTIM");
        String regDtim = regDate.substring(0, 8);

        String cnslCont = mjsonParams.getString("AF_CNSL_CONT");
        String visPath = mjsonParams.getString("AF_VIS_PATH");
        String disaItem = mjsonParams.getString("AF_DISA_ITEM");
        String cnslBook = mjsonParams.getString("AF_CNSL_BOOK");
        String cnslBookDT = mjsonParams.getString("AF_CNSL_BOOK_DT");
        String consBook = mjsonParams.getString("AF_CONS_BOOK");
        String consBookDT = mjsonParams.getString("AF_CONS_BOOK_DT");
        String operBook = mjsonParams.getString("AF_OPER_BOOK");
        String operBookDT = mjsonParams.getString("AF_OPER_BOOK_DT");
        String operation = mjsonParams.getString("AF_OPERATION");
        String operationDT = mjsonParams.getString("AF_OPERATION_DT");
        String etc = mjsonParams.getString("AF_ETC");
        String operationCost = mjsonParams.getString("AF_OPERATION_COST");
        String rdwtFile = mjsonParams.getString("AF_RDWT_FILE_NM");
        String visBook = mjsonParams.getString("AF_VIS_BOOK");
        String visBookDT = mjsonParams.getString("AF_VIS_BOOK_DT");
        String condition = mjsonParams.getString("AF_CONDITION");
        String chgCustNm = mjsonParams.getString("AF_CUST_NM");

        String manDrinver = mjsonParams.getString("AF_MAN_REAL_DRIVER");
        String manCustTy = mjsonParams.getString("AF_MAN_CUST_TY");
        String manPhoneNo = mjsonParams.getString("AF_MAN_PHONE_NO");
        String manVehicleTy = mjsonParams.getString("AF_MAN_VEHICLE_TY");
        String manSite = mjsonParams.getString("AF_MAN_SITE");
        String manCenter = mjsonParams.getString("AF_MAN_CENTER");

        jsonParams.setString("CNSL_CONT", cnslCont);
        jsonParams.setString("VIS_PATH", visPath);
        jsonParams.setString("DISA_ITEM", disaItem);
        jsonParams.setString("CNSL_BOOK", cnslBook);
        jsonParams.setString("CNSL_BOOK_DT", cnslBookDT);
        jsonParams.setString("CONS_BOOK", consBook);
        jsonParams.setString("CONS_BOOK_DT", consBookDT);
        jsonParams.setString("OPER_BOOK", operBook);
        jsonParams.setString("OPER_BOOK_DT", operBookDT);
        jsonParams.setString("OPERATION", operation);
        jsonParams.setString("OPERATION_DT", operationDT);
        jsonParams.setString("VIS_BOOK", visBook);
        jsonParams.setString("VIS_BOOK_DT", visBookDT);
        jsonParams.setString("ETC", etc);
        jsonParams.setString("OPERATION_COST", operationCost);
        jsonParams.setString("RDWT_FILE_NM", rdwtFile);
        jsonParams.setString("CONDITION", condition);
        jsonParams.setString("CHG_CUST_NM", chgCustNm);

        jsonParams.setString("MAN_REAL_DRIVER", manDrinver);
        jsonParams.setString("MAN_CUST_TY", manCustTy);
        jsonParams.setString("MAN_PHONE_NO", manPhoneNo);
        jsonParams.setString("MAN_VEHICLE_TY", manVehicleTy);
        jsonParams.setString("MAN_SITE", manSite);
        jsonParams.setString("MAN_CENTER", manCenter);

        jsonParams.setString("SEQ", SEQ);

        log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + jsonParams);

        objRetParams = phoneHistoryInqireService.insertCnslChngHistDe(jsonParams);
        objRetParams = phoneHistoryInqireService.updateRtnCnslHistMngDe(jsonParams);
        objRetParams = phoneHistoryInqireService.updateRtnCnslHistMngDe2(jsonParams);

        return objRetParams;
    }

    /**
     * 상담이력조회 칭찬콜등록
     * 
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "상담이력조회 칭찬콜등록", notes = "상담이력조회 칭찬콜등록")
    @PostMapping("/phone-api/history/inqire/regist")
    public Object insertCnslCmptCall(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneHistoryInqireService.insertCnslCmptCall(mjsonParams);

        return objRetParams;
    }

    /**
     * 조합검색 팝업 조회
     * 
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "조합검색 팝업 조회", notes = "조합검색 팝업 조회")
    @PostMapping("/phone-api/history/assc/inqire-pop")
    public Object selectRtnUserInfoPop(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneHistoryInqireService.selectRtnUserInfoPop(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 상담이력출력관리 등록
     * 
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "상담이력출력관리 등록 조회", notes = "상담이력출력관리 등록 조회")
    @PostMapping("/phone-api/history/outpt/manage/regist")
    public Object insertCnslHistOutputMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        String seq = innbCreatCmmnService.getSeqNo("PLT_PHN_CNSL_PRT_HST");
        mjsonParams.setString("SEQ", seq);
        objRetParams = phoneHistoryInqireService.insertCnslHistOutputMng(mjsonParams);

        return objRetParams;
    }

    /**
     * 
     * 전화상담 이력 목록
     * 
     * @Method Name : cuttHistList
     * @date : 2023. 5. 15.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 이력 -목록", notes = "전화상담 이력 목록을 조회한다")
    @PostMapping("/phone-api/history/cuttHistList")
    public Object cuttHistList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = phoneHistoryInqireService.cuttHistList(mjsonParams);

        return objRetParams;
    }

    /**
     * 
     * 전화상담 이력 관리 목록
     * 
     * @Method Name : cuttHistList
     * @date : 2023. 6. 29.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 이력 관리-목록", notes = "전화상담 이력 관리 목록을 조회한다")
    @PostMapping("/phone-api/history/cuttHistMngList")
    public Object cuttHistMngList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = phoneHistoryInqireService.cuttHistMngList(mjsonParams);

        return objRetParams;
    }

    /**
     * 
     * 전화상담 내역 변경 정보 조회
     * 
     * @Method Name : cuttChgInfo
     * @date : 2023. 6. 29.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 내역 변경 정보 조회", notes = "전화상담 내역 변경 정보를 조회한다")
    @PostMapping("/phone-api/history/cuttChgInfo")
    public Object cuttChgInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = phoneHistoryInqireService.cuttChgInfo(mjsonParams);

        return objRetParams;
    }

    /**
     * 
     * 전화상담 내역 변경 요청 승인
     * 
     * @Method Name : cuttChgAprvProc
     * @date : 2023. 6. 29.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 내역 변경 요청 승인", notes = "전화상담 내역 변경 요청 승인 처리한다")
    @PostMapping("/phone-api/history/cuttChgAprvProc")
    public Object cuttChgAprvProc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = phoneHistoryInqireService.cuttChgAprvProc(mjsonParams);

        return objRetParams;
    }

    /**
     * 
     * 전화상담 내역 일괄 변경 요청 승인
     * 
     * @Method Name : cuttChgAprvProc
     * @date : 2023. 6. 29.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 내역 일괄 변경 요청 승인", notes = "전화상담 내역 일괄 변경 요청 승인 처리한다")
    @PostMapping("/phone-api/history/batchCuttChgAprvProc")
    public Object batchCuttChgAprvProc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = phoneHistoryInqireService.batchCuttChgAprvProc(mjsonParams);

        return objRetParams;
    }

    /**
     *
     * 전화상담 이력 엑셀 다운로드
     * 
     * @Method Name : cuttHistExcelDwnld
     * @date : 2023. 11. 08.
     * @author : njy
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws Exception
     */
    @PrvcAspectAnotation(value = "CUTT_HIST_DOWN")   //개인정보 Logging관련
    @ApiOperation(value = "전화상담 이력 엑셀 다운로드", notes = "전화상담 이력 엑셀 다운로드한다")
    @PostMapping("/phone-api/history/cuttHistExcelDwnld")
    public void cuttHistExcelDwnld(HttpServletRequest request, HttpServletResponse response,
        @TelewebJsonParam TelewebJSON jsonParams) throws Exception {
        ExcelDownController bCon = new ExcelDownController(excelService);

        //다운로드 항목
        String CUSTOM_ATTR = jsonParams.getString("CUSTOM_ATTR").toString();
        CUSTOM_ATTR = CUSTOM_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("EXPSN_ATTR_LIST", CUSTOM_ATTR);

        //고객 확장 속성 검색
        String SCH_CUST_EXPSN_ATTR = jsonParams.getString("SCH_CUST_EXPSN_ATTR").toString();
        SCH_CUST_EXPSN_ATTR = SCH_CUST_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("SCH_CUST_EXPSN_ATTR", SCH_CUST_EXPSN_ATTR);

        //상담 확장 속성 검색
        String SCH_CUTT_EXPSN_ATTR = jsonParams.getString("SCH_CUTT_EXPSN_ATTR").toString();
        SCH_CUTT_EXPSN_ATTR = SCH_CUTT_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("SCH_CUTT_EXPSN_ATTR", SCH_CUTT_EXPSN_ATTR);

        bCon.excel_tmpl(request, response, jsonParams);
    }

    /**
     *
     * 전화상담 이력 상담, 고객 확장속성 정보 조회
     * 
     * @Method Name : cuttHistExpsnAttr
     * @date : 2023. 11. 08.
     * @author : njy
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화상담 이력 상담, 고객 확장속성 정보 조회", notes = "전화상담 이력 상담, 고객 확장속성 정보 조회한다")
    @PostMapping("/phone-api/history/cuttHistGetExpsnAttr")
    public Object cuttHistGetExpsnAttr(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = phoneHistoryInqireService.cuttHistGetExpsnAttr(mjsonParams);

        return objRetParams;
    }

    /**
     *
     * 나의 상담 이력 관리 차트 정보
     *
     * @Method Name : getMyCuttHistCnt
     * @date : 2024. 01. 05.
     * @author : NJY
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "나의 상담 이력 관리 - 차트 정보", notes = "나의 상담 이력 관리 - 차트 정보 조회")
    @PostMapping("/phone-api/history/getMyCuttHistCnt")
    public Object getMyCuttHistCnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = phoneHistoryInqireService.getMyCuttHistCnt(mjsonParams);

        return objRetParams;
    }
}
