package kr.co.hkcloud.palette3.phone.history.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.history.app.PhoneHistoryChangeManageService;
import kr.co.hkcloud.palette3.phone.history.app.PhoneHistoryInqireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneHistoryChangeManageRestController",
     description = "상담이력변경관리 REST 컨트롤러")
public class PhoneHistoryChangeManageRestController
{

    private final PhoneHistoryInqireService       phoneHistoryListService;
    private final PhoneHistoryChangeManageService phoneHistoryChangeManageService;


    /**
     * 상담이력변경관리 조회(공제지원)
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "상담이력변경관리 목록 조회",
                  notes = "상담이력변경관리 목록 조회")
    @PostMapping("/phone-api/history/change-manage/list")
    public Object selectRtnCnslHistChngMngDe(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        log.debug("mjsonParams ==============" + mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams = phoneHistoryChangeManageService.selectRtnCnslHistChngMngDe(jsonParams);

        return objRetParams;
    }


    @ApiOperation(value = "상담이력변경관리 일괄승인",
                  notes = "상담이력변경관리 일괄승인")
    @PostMapping("phone-api/history/change-manage/confm/process")
    public Object updateRtnCnslHistChngMngAllDe(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonAllParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

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
        String visTerm = mjsonParams.getString("AF_VIS_TERM");
        String chgCustNm = mjsonParams.getString("AF_CUST_NM");

        String manDrinver = mjsonParams.getString("AF_MAN_REAL_DRIVER");
        String manCustTy = mjsonParams.getString("AF_MAN_CUST_TY");
        String manPhoneNo = mjsonParams.getString("AF_MAN_PHONE_NO");
        String manVehicleTy = mjsonParams.getString("AF_MAN_VEHICLE_TY");
        String manSite = mjsonParams.getString("AF_MAN_SITE");
        String manCenter = mjsonParams.getString("AF_MAN_CENTER");

        String plen = mjsonParams.getString("AF_PLEN");
        String rcvr = mjsonParams.getString("AF_RCVR");
        String rcvrInfo = mjsonParams.getString("AF_RCVR_INFO");
        String interest = mjsonParams.getString("AF_INTEREST");
        String nonHope = mjsonParams.getString("AF_NON_HOPE");
        
        for(int i = 0; i < jsonParams.getSize(); i++) {

            // 체크된 변경목록 파라미터
            jsonAllParams.setString("CHNG_HIST_SEQ", jsonParams.getString("CHNG_HIST_SEQ", i));
            jsonAllParams.setString("CHNG_MAN", jsonParams.getString("CHNG_MAN", i));
            jsonAllParams.setString("TRANS_STATUS", jsonParams.getString("TRANS_STATUS", i));
            jsonAllParams.setString("CNSL_HIST_DTL_NO", jsonParams.getString("CNSL_HIST_DTL_NO", i));
            jsonAllParams.setString("CNSL_HIST_NO", jsonParams.getString("CNSL_HIST_NO", i));
            jsonAllParams.setString("RDWT_FILE_NM", jsonParams.getString("RDWT_FILE_NM", i));
            jsonAllParams.setString("CNSL_TYP_CD", jsonParams.getString("CNSL_TYP_CD", i));
            jsonAllParams.setString("CNSL_TYP_CD_2", jsonParams.getString("CNSL_TYP_CD_2", i));
            jsonAllParams.setString("CNSL_TYP_CD_3", jsonParams.getString("CNSL_TYP_CD_3", i));
            jsonAllParams.setString("CNSL_TYP_CD_4", jsonParams.getString("CNSL_TYP_CD_4", i));
            jsonAllParams.setString("CNSL_CNTN", jsonParams.getString("CNSL_CNTN", i));
            jsonAllParams.setString("RDWT_ID", jsonParams.getString("RDWT_ID", i));
            jsonAllParams.setString("PROC_CODE_LCLS", jsonParams.getString("PROC_CODE_LCLS", i));
            jsonAllParams.setString("CALL_TY", jsonParams.getString("CALL_TY", i));
            jsonAllParams.setString("REQ_CSLT_NM", jsonParams.getString("REQ_CSLT_NM", i));
            jsonAllParams.setString("APRV_CSLT_ID", jsonParams.getString("APRV_CSLT_ID", i));
            jsonAllParams.setString("AF_CNSL_TYP_CD", jsonParams.getString("AF_CNSL_TYP_CD", i));
            jsonAllParams.setString("AF_CNSL_TYP_CD_2", jsonParams.getString("AF_CNSL_TYP_CD_2", i));
            jsonAllParams.setString("AF_CNSL_TYP_CD_3", jsonParams.getString("AF_CNSL_TYP_CD_3", i));
            jsonAllParams.setString("AF_CNSL_TYP_CD_4", jsonParams.getString("AF_CNSL_TYP_CD_4", i));
            jsonAllParams.setString("AF_CNSL_CNTN", jsonParams.getString("AF_CNSL_CNTN", i));
            jsonAllParams.setString("AF_CALL_TY", jsonParams.getString("AF_CALL_TY", i));
            jsonAllParams.setString("AF_UN_PROC_RESN", jsonParams.getString("AF_UN_PROC_RESN", i));
            jsonAllParams.setString("AF_PROC_RSLT", jsonParams.getString("AF_PROC_RSLT", i));

            jsonAllParams.setString("CNSL_CONT", cnslCont);
            jsonAllParams.setString("VIS_PATH", visPath);
            jsonAllParams.setString("DISA_ITEM", disaItem);
            jsonAllParams.setString("CNSL_BOOK", cnslBook);
            jsonAllParams.setString("CNSL_BOOK_DT", cnslBookDT);
            jsonAllParams.setString("CONS_BOOK", consBook);
            jsonAllParams.setString("CONS_BOOK_DT", consBookDT);
            jsonAllParams.setString("OPER_BOOK", operBook);
            jsonAllParams.setString("OPER_BOOK_DT", operBookDT);
            jsonAllParams.setString("OPERATION", operation);
            jsonAllParams.setString("OPERATION_DT", operationDT);
            jsonAllParams.setString("ETC", etc);
            jsonAllParams.setString("OPERATION_COST", operationCost);
            jsonAllParams.setString("RDWT_FILE_NM", rdwtFile);
            jsonAllParams.setString("VIS_BOOK", visBook);
            jsonAllParams.setString("VIS_BOOK_DT", visBookDT);
            jsonAllParams.setString("CONDITION", condition);
            jsonAllParams.setString("VIS_TERM", visTerm);

            jsonAllParams.setString("MAN_REAL_DRIVER", manDrinver);
            jsonAllParams.setString("MAN_CUST_TY", manCustTy);
            jsonAllParams.setString("MAN_PHONE_NO", manPhoneNo);
            jsonAllParams.setString("MAN_VEHICLE_TY", manVehicleTy);
            jsonAllParams.setString("MAN_SITE", manSite);
            jsonAllParams.setString("MAN_CENTER", manCenter);

            jsonAllParams.setString("PLEN", plen);
            jsonAllParams.setString("RCVR", rcvr);
            jsonAllParams.setString("RCVR_INFO", rcvrInfo);
            jsonAllParams.setString("INTEREST", interest);
            jsonAllParams.setString("NON_HOPE", nonHope);
            
            jsonAllParams.setString("CHG_CUST_NM", chgCustNm);
            
            log.debug("jsonAllParams" + jsonAllParams);

            objRetParams = phoneHistoryChangeManageService.updateRtnCnslHistChngMng(jsonAllParams);
            phoneHistoryListService.updateRtnCnslHistMngDe(jsonAllParams);
            phoneHistoryListService.updateRtnCnslHistMngDe2(jsonAllParams);

        }

        return objRetParams;
    }
}
