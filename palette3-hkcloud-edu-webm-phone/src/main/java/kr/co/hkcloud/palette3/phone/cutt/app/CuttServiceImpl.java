package kr.co.hkcloud.palette3.phone.cutt.app;

import java.text.ParseException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.cuttType.app.CuttTypeService;
import kr.co.hkcloud.palette3.sse.app.SseService;
import kr.co.hkcloud.palette3.sse.message.model.SseMessage;
import kr.co.hkcloud.palette3.vst.app.VstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("cuttService")
public class CuttServiceImpl implements CuttService {

    private final InnbCreatCmmnService innbCreatCmmnService;
    private final PaletteCmmnService paletteCmmnService;
    private final CuttTypeService cuttTypeService;
    private final TwbComDAO mobjDao;
    private final SseService sseService;
    private final VstService vstService;

    /**
     * 상담 내용 저장
     * @throws ParseException 
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON cuttProc(TelewebJSON jsonParams) throws TelewebAppException, ParseException {
        TelewebJSON objRetParams = new TelewebJSON();

        if (StringUtils.isBlank(jsonParams.getString("PHN_CUTT_ID")) || !StringUtils.isBlank(jsonParams.getString("CUTT_RSVT_ID"))) { //등록 이거나 예약 콜 인 경우
            jsonParams.setString("STAT", "REG");
            //예약콜인 경우 이전 전화 상담 ID 저장
            if (!StringUtils.isBlank(jsonParams.getString("CUTT_RSVT_ID")))
                jsonParams.setInt("RSVT_PHN_CUTT_ID", Integer.parseInt(jsonParams.getString("PHN_CUTT_ID")));

            //전화 상담 ID
            int PHN_CUTT_ID = innbCreatCmmnService.createSeqNo("PHN_CUTT_ID");
            jsonParams.setInt("PHN_CUTT_ID", PHN_CUTT_ID);

            // 1. 상담 기본 정보 저장(PLT_PHN_CUTT)
            if (StringUtils.isBlank(jsonParams.getString("CPLMT_CL_YN")))
                jsonParams.setString("CPLMT_CL_YN", "N");
            if (StringUtils.isBlank(jsonParams.getString("AUTO_CL_YN")))
                jsonParams.setString("AUTO_CL_YN", "N");
            objRetParams = this.cuttReg(jsonParams);
            
//            //이첩 대상이 있는 경우(스키마별 이첩이 발생할 경우 사용)
//            if(!StringUtils.isEmpty(jsonParams.getString("TRGT_CERT_CUSTCO_ID"))) {
//	            TelewebJSON trnsfParams = new TelewebJSON();
//	            trnsfParams.setString("ORGNL_CERT_CUSTCO_ID", jsonParams.getString("ORGNL_CERT_CUSTCO_ID")); //원본 인증 고객사 ID
//	            trnsfParams.setString("ORGNL_CUTT_TYPE_CD", jsonParams.getString("ORGNL_CUTT_TYPE_CD")); //원본 상담 유형 코드
//	            trnsfParams.setString("ORGNL_CUTT_ID", jsonParams.getString("PHN_CUTT_ID")); //원본 상담 ID
//	            trnsfParams.setString("USER_ID", jsonParams.getString("USER_ID")); //요청자 ID
//	            trnsfParams.setString("TRGT_CERT_CUSTCO_ID", jsonParams.getString("TRGT_CERT_CUSTCO_ID")); //대상 인증 고객사 ID
//	            trnsfParams.setString("TRPR_ID", jsonParams.getString("TRPR_ID")); //대상자 ID
//	            trnsfParams.setString("STTS_CD", "TST_REQST"); //요청
//	            
//	            objRetParams = this.trnsfCuttProc(trnsfParams);
//            }
        } else { //수정
            jsonParams.setString("STAT", "MOD");
            //1. 상담 기본 정보 수정
            if (StringUtils.isBlank(jsonParams.getString("CPLMT_CL_YN")))
                jsonParams.setString("CPLMT_CL_YN", "N");
            if (StringUtils.isBlank(jsonParams.getString("AUTO_CL_YN")))
                jsonParams.setString("AUTO_CL_YN", "N");
            jsonParams.setString("APRV_DMND_USER_ID", jsonParams.getString("USER_ID"));
            //			objRetParams = this.cuttMod(jsonParams);
        }

        //-------------------------------등록, 수정 공통(상담 변경 이력은 신규 생성 처리)-------------------------
        //전화 상담 변경 이력 ID
        int CHG_HSTRY_ID = innbCreatCmmnService.createSeqNo("CHG_HSTRY_ID");
        jsonParams.setInt("CHG_HSTRY_ID", CHG_HSTRY_ID);

        // 2. 상담 변경 이력 저장(PLT_PHN_CUTT_CHG_HSTRY)
        objRetParams = this.cuttChgHistReg(jsonParams);
        //-------------------------------등록, 수정 공통(상담 변경 이력은 신규 생성 처리)-------------------------

        //3. 상담 상세 확장 정보 저장 (PLT_PHN_CUTT_DTL_EXPSN)
        //4. 상담 변경 확장 정보 저장 (PLT_PHN_CUTT_CHG_DSCTN)
        JSONArray jsonObj = jsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
            JSONObject objData = jsonObj.getJSONObject(i);

            @SuppressWarnings("rawtypes") Iterator it = objData.keys();
            while (it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if (strKey.indexOf("EXPSN_ATTR") > -1 && StringUtils.isNotEmpty(strValue)) {
                    strValue = strValue.replace("&#91;", "[").replace("&#93;", "]");
                    JSONArray arryExpsnAttr = JSONArray.fromObject(strValue);

                    if (arryExpsnAttr.size() > 0) {
                        for (Object expsnAttr : arryExpsnAttr) {
                            TelewebJSON expsnAttrParams = new TelewebJSON();

                            expsnAttrParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                            expsnAttrParams.setString("PP_ALG_PP", jsonParams.getString("PP_ALG_PP"));
                            expsnAttrParams.setString("PP_KEY_PP", jsonParams.getString("PP_KEY_PP"));
                            expsnAttrParams.setString("PHN_CUTT_ID", jsonParams.getString("PHN_CUTT_ID"));
                            expsnAttrParams.setString("CHG_HSTRY_ID", jsonParams.getString("CHG_HSTRY_ID"));
                            expsnAttrParams.setString("EXPSN_ATTR_COL_ID", (String) ((JSONObject) expsnAttr).get("EXPSN_ATTR_COL_ID"));
                            expsnAttrParams.setString("ATTR_ID", (String) ((JSONObject) expsnAttr).get("ATTR_ID"));
                            expsnAttrParams.setString("ATTR_VL", (String) ((JSONObject) expsnAttr).get("V_POST_PARAM"));
                            expsnAttrParams.setString("INDI_INFO_ENCPT_YN", (String) ((JSONObject) expsnAttr).get("INDI_INFO_ENCPT_YN"));
                            expsnAttrParams.setString("RSVT_PHN_CUTT_ID", jsonParams.getString("RSVT_PHN_CUTT_ID"));
                            expsnAttrParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                            expsnAttrParams.setString("CLBK_ID", jsonParams.getString("CLBK_ID"));
                            expsnAttrParams.setString("CPI_ID", jsonParams.getString("CPI_ID"));
                            expsnAttrParams.setString("RSVT_ID", jsonParams.getString("RSVT_ID"));

                            if ("REG".equals(jsonParams.getString("STAT"))) { //등록 인 경우에만 확장 속성에 저장
                                objRetParams = this.cuttExpsnAttrReg(expsnAttrParams); //전화 상담 상세 확장 정보 저장
                            }

                            objRetParams = this.cuttChgExpsnAttrReg(expsnAttrParams); //전화 상담 변경 상세 확장 정보 저장
                        }
                    }
                }else if (strKey.indexOf("VST_RSVT_LIST") > -1 && StringUtils.isNotEmpty(strValue)) { //방문 상담 예약
                	strValue = strValue.replace("&#91;", "[").replace("&#93;", "]");
                    JSONArray arryVstRsvt = JSONArray.fromObject(strValue);

                    if (arryVstRsvt.size() > 0) {
                        for (Object vsrRsvt : arryVstRsvt) {
                            TelewebJSON vsrRsvtParams = new TelewebJSON();

                            vsrRsvtParams.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                            vsrRsvtParams.setString("PHN_CUTT_ID", jsonParams.getString("PHN_CUTT_ID"));
                            vsrRsvtParams.setString("CUST_NM", (String) ((JSONObject) vsrRsvt).get("CUST_NM"));
                            vsrRsvtParams.setString("REL_CD", (String) ((JSONObject) vsrRsvt).get("REL_CD"));
                            vsrRsvtParams.setString("CUST_TELNO", (String) ((JSONObject) vsrRsvt).get("CUST_TELNO"));
                            vsrRsvtParams.setString("ZIP", (String) ((JSONObject) vsrRsvt).get("ZIP"));
                            vsrRsvtParams.setString("ADDR", (String) ((JSONObject) vsrRsvt).get("ADDR"));
                            vsrRsvtParams.setString("ADDR_DTL", (String) ((JSONObject) vsrRsvt).get("ADDR_DTL"));
                            vsrRsvtParams.setString("SRVC_TYPE_CD", (String) ((JSONObject) vsrRsvt).get("SRVC_TYPE_CD"));
                            vsrRsvtParams.setString("PRDCT_TYPE_ID", (String) ((JSONObject) vsrRsvt).get("PRDCT_TYPE_ID"));
                            vsrRsvtParams.setString("RSVT_BGNG_DT", (String) ((JSONObject) vsrRsvt).get("RSVT_BGNG_DT"));
//                            vsrRsvtParams.setString("RSVT_END_DT", (String) ((JSONObject) vsrRsvt).get("RSVT_END_DT"));
                            vsrRsvtParams.setString("DMND_MTTR", (String) ((JSONObject) vsrRsvt).get("DMND_MTTR"));
                            vsrRsvtParams.setString("EXCPTN_MTTR", (String) ((JSONObject) vsrRsvt).get("EXCPTN_MTTR"));
                            vsrRsvtParams.setString("CUST_ID", jsonParams.getString("CUST_ID"));
                            vsrRsvtParams.setString("VSTR_ID", (String) ((JSONObject) vsrRsvt).get("VSTR_ID"));
                            vsrRsvtParams.setString("USER_ID", jsonParams.getString("USER_ID"));
                            vsrRsvtParams.setString("STTS_CD", "VRS_ALTMNT"); //상태 코드(배정)
                            vsrRsvtParams.setString("callback_number", jsonParams.getString("callback_number")); //sms 발송 번호
                            
                            objRetParams = vstService.vstRsvtProc(vsrRsvtParams); //AS 예약 접숙
                        }
                    }
                }
            }
        }

        //5. 상담 이력 변경 ID 상담 테이블에 UPDATE
        objRetParams = this.cuttChgHistIdUpdate(jsonParams);

        if ("REG".equals(jsonParams.getString("STAT"))) { //등록인 경우에만 실행
            //6. 상담 통합 이력 저장
            objRetParams = paletteCmmnService.cuttItgrtHistReg(jsonParams);
        }

        objRetParams.setDataObject(TwbCmmnConst.G_DATA, jsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 전화 상담 기본 정보 저장
     */
    public TelewebJSON cuttReg(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttReg", jsonParams);
    }

    /**
     * 전화 상담 상세 확장 정보 저장
     */
    public TelewebJSON cuttExpsnAttrReg(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON();

        if ("RSVT_CALL".equals(jsonParams.getString("EXPSN_ATTR_COL_ID"))) { //예약 콜
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttExpsnAttrReg", jsonParams);
            if (!"".equals(jsonParams.getString("ATTR_VL"))) {
                jsonParams.setString("RSVT_DT", jsonParams.getString("ATTR_VL"));
                objRetParams = this.cuttRsvtReg(jsonParams);
            }
        } else if ("CUSL_CN".equals(jsonParams.getString("EXPSN_ATTR_COL_ID"))) { //상담 내용
            TelewebJSON cuttCnParams = new TelewebJSON();
            cuttCnParams.setString("PP_ALG_PP", jsonParams.getString("PP_ALG_PP"));
            cuttCnParams.setString("PP_KEY_PP", jsonParams.getString("PP_KEY_PP"));
            cuttCnParams.setString("INDI_INFO_ENCPT_YN", jsonParams.getString("INDI_INFO_ENCPT_YN"));
            cuttCnParams.setString("PHN_CUTT_ID", jsonParams.getString("PHN_CUTT_ID"));
            cuttCnParams.setString("ATTR_ID", jsonParams.getString("ATTR_ID"));
            cuttCnParams.setString("CUTT_CN", ""); //상담 내용은 PLT_PHN_CUTT_DTL_CN 테이블에 별도 저장

            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttExpsnAttrReg", cuttCnParams);

            jsonParams.setString("CUTT_CN", jsonParams.getString("ATTR_VL"));
            objRetParams = this.cuttCnReg(jsonParams);
        } else {
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttExpsnAttrReg", jsonParams);

            if ("CUSL_TP_CL".equals(jsonParams.getString("EXPSN_ATTR_COL_ID"))) { //상담유형
                jsonParams.setString("REG_SE_CD", "RECENT_REG");
                jsonParams.setString("CUTT_TYPE_ID", jsonParams.getString("ATTR_VL"));
                objRetParams = cuttTypeService.cuslCuttTypeBmkProc(jsonParams);
            }

            if ("CUSL_RS".equals(jsonParams.getString("EXPSN_ATTR_COL_ID"))) { //상담 결과
                if (!StringUtils.isBlank(jsonParams.getString("RSVT_PHN_CUTT_ID")) && "COMPLETED".equals(jsonParams.getString("ATTR_VL"))) { //예약콜 이면서 처리 완료인 경우 원본 상담 내용 상태 업데이트
                    objRetParams = this.cuttRsvtOriginStatUpdate(jsonParams);
                }
                if (!StringUtils.isBlank(jsonParams.getString("CLBK_ID")) && "COMPLETED".equals(jsonParams.getString("ATTR_VL"))) { //콜백 이면서 처리 완료인 경우 원본 상담 내용 상태 업데이트
                    objRetParams = this.cuttClbkOriginStatUpdate(jsonParams);
                }
                if (!StringUtils.isBlank(jsonParams.getString("CPI_ID")) && "COMPLETED".equals(jsonParams.getString("ATTR_VL"))) { //콜백 이면서 처리 완료인 경우 원본 상담 내용 상태 업데이트
                    objRetParams = this.cuttCpiOriginStatUpdate(jsonParams);
                }
                if (!StringUtils.isBlank(jsonParams.getString("RSVT_ID"))) { //예약상담 상담저장
                	this.insertRsvtCutt(jsonParams);
                }
                	
            }
        }

        return objRetParams;
    }

    /**
     * 상담 예약 콜 저장
     */
    public TelewebJSON cuttRsvtReg(TelewebJSON jsonParams) throws TelewebAppException {
        int CUTT_RSVT_ID = innbCreatCmmnService.createSeqNo("CUTT_RSVT_ID");
        jsonParams.setInt("CUTT_RSVT_ID", CUTT_RSVT_ID);

        JSONObject messageJson = new JSONObject();
        StringBuffer sInfo = new StringBuffer();
        sInfo.append("예약 콜 등록");
        messageJson.put("RSVTCALL_INFO", sInfo.toString());

        String roomId = TenantContext.getCurrentTenant() + "_" + jsonParams.getString("CUSTCO_ID");
        SseMessage sseMessage = new SseMessage();
        sseMessage.setType(SseMessage.MessageType.ENV_SETTING);
        sseMessage.setSender("2");
        sseMessage.setReceiver("ALL");    // ALL은 전체 , userId 개인별
        sseMessage.setRoomId(roomId);
        sseMessage.setSecond(-1); //필수 아님. 기본이 5초
        sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
        sseMessage.setMessage(messageJson.toString());    //메시지
        try {
            sseService.sendMessage(sseMessage);
            Thread.sleep(100);
        } catch (Exception e) {
            log.error("sseMessage is exception : " + e.getMessage());
        }

        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttRsvtReg", jsonParams);
    }

    /**
     * 상담 예약 콜 시간 변경
     */
    public TelewebJSON cuttRsvtUpdate(TelewebJSON jsonParams) throws TelewebAppException {

        JSONObject messageJson = new JSONObject();
        StringBuffer sInfo = new StringBuffer();
        sInfo.append("예약 콜 시간 변경");
        messageJson.put("RSVTCALL_INFO", sInfo.toString());

        String roomId = TenantContext.getCurrentTenant() + "_" + jsonParams.getString("CUSTCO_ID");
        SseMessage sseMessage = new SseMessage();
        sseMessage.setType(SseMessage.MessageType.ENV_SETTING);
        sseMessage.setSender("2");
        sseMessage.setReceiver("ALL");    // ALL은 전체 , userId 개인별
        sseMessage.setRoomId(roomId);
        sseMessage.setSecond(-1); //필수 아님. 기본이 5초
        sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
        sseMessage.setMessage(messageJson.toString());    //메시지
        try {
            sseService.sendMessage(sseMessage);
            Thread.sleep(100);
        } catch (Exception e) {
            log.error("sseMessage is exception : " + e.getMessage());
        }

        return mobjDao.update("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttRsvtUpdate", jsonParams);
    }

    /**
     * 상담 예약 콜 원본 상태 변경
     */
    public TelewebJSON cuttRsvtOriginStatUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        JSONObject messageJson = new JSONObject();
        StringBuffer sInfo = new StringBuffer();
        sInfo.append("예약 콜 완료");
        messageJson.put("RSVTCALL_INFO", sInfo.toString());

        String roomId = TenantContext.getCurrentTenant() + "_" + jsonParams.getString("CUSTCO_ID");
        SseMessage sseMessage = new SseMessage();
        sseMessage.setType(SseMessage.MessageType.ENV_SETTING);
        sseMessage.setSender("2");
        sseMessage.setReceiver("ALL");    // ALL은 전체 , userId 개인별
        sseMessage.setRoomId(roomId);
        sseMessage.setSecond(-1); //필수 아님. 기본이 5초
        sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
        sseMessage.setMessage(messageJson.toString());    //메시지
        try {
            sseService.sendMessage(sseMessage);
            Thread.sleep(100);
        } catch (Exception e) {
            log.error("sseMessage is exception : " + e.getMessage());
        }

        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttRsvtOriginStatUpdate", jsonParams);
    }

    /**
     * 상담 내용 저장
     */
    public TelewebJSON cuttCnReg(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttCnReg", jsonParams);
    }

    /**
     * 상담 변경 이력 저장
     */
    public TelewebJSON cuttChgHistReg(TelewebJSON jsonParams) throws TelewebAppException {
        if ("REG".equals(jsonParams.getString("STAT"))) {
            jsonParams.setString("APRV_STTS_CD", "APRV"); //승인
        } else {
            jsonParams.setString("APRV_STTS_CD", "RDY"); //대기
        }

        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttChgHistReg", jsonParams);
    }

    /**
     * 상담 변경 내역(확장속성) 저장
     */
    public TelewebJSON cuttChgExpsnAttrReg(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();

        if ("CUSL_CN".equals(jsonParams.getString("EXPSN_ATTR_COL_ID"))) { //상담 내용
            TelewebJSON cuttCnParams = new TelewebJSON();
            cuttCnParams.setString("PP_ALG_PP", jsonParams.getString("PP_ALG_PP"));
            cuttCnParams.setString("PP_KEY_PP", jsonParams.getString("PP_KEY_PP"));
            cuttCnParams.setString("INDI_INFO_ENCPT_YN", jsonParams.getString("INDI_INFO_ENCPT_YN"));
            cuttCnParams.setString("CHG_HSTRY_ID", jsonParams.getString("CHG_HSTRY_ID"));
            cuttCnParams.setString("ATTR_ID", jsonParams.getString("ATTR_ID"));
            cuttCnParams.setString("CUTT_CN", ""); //상담 내용은 PLT_PHN_CUTT_CHG_CN 테이블에 별도 저장

            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttChgExpsnAttrReg", cuttCnParams);

            jsonParams.setString("CUTT_CN", jsonParams.getString("ATTR_VL"));
            objRetParams = this.cuttChgCnReg(jsonParams);
        } else {
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttChgExpsnAttrReg", jsonParams);
        }

        return objRetParams;
    }

    /**
     * 상담 변경 내용 저장
     */
    public TelewebJSON cuttChgCnReg(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttChgCnReg", jsonParams);
    }

    /**
     * 상담 변경 이력 ID UPDATE
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON cuttChgHistIdUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON chgHistParams = new TelewebJSON();
        chgHistParams.setString("STAT", jsonParams.getString("STAT"));
        chgHistParams.setString("USER_ID", jsonParams.getString("USER_ID"));
        chgHistParams.setString("PHN_CUTT_ID", jsonParams.getString("PHN_CUTT_ID"));
        chgHistParams.setString("CHG_HSTRY_ID", jsonParams.getString("CHG_HSTRY_ID"));
        chgHistParams.setString("CUTT_BGNG_DT", jsonParams.getString("CUTT_BGNG_DT"));
        chgHistParams.setString("CUTT_END_DT", jsonParams.getString("CUTT_END_DT"));

        return mobjDao.update("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttChgHistIdUpdate", chgHistParams);
    }

    /**
     * PALETTE3 상담 이력 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON integCuttHistList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "integCuttHistList", mjsonParams);
    }

    /**
     * PALETTE3 VOC 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON vocList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "vocList", mjsonParams);
    }

    /**
     * PALETTE3 문자발송 이력
     */
    @Transactional(readOnly = true)
    public TelewebJSON msgList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "msgList", mjsonParams);
    }

    /**
     * PALETTE3 예약 콜 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON rsvtCallList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "rsvtCallList", mjsonParams);
    }

    @Transactional(readOnly = true)
    public TelewebJSON cuslRsvtCallMonitor(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuslRsvtCallMonitor", mjsonParams);
    }

    /**
     * PALETTE3 콜백 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON callBackList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "callBackList", mjsonParams);
    }

    /**
     * PALETTE3 캠페인 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON cpiList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cpiList", mjsonParams);
    }

    /**
     * My 데스크 콜백, 예약콜, 캠페인 진행 현황
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON myDeskStat(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "myDeskStat", jsonParams);
    }

    /* 예약콜이 있을 경우 상담직원이 인지할 수 있도록 우측 사이드바의 해당 아이콘에 에니메이션 효과를 적용 용도 */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rsvtCallNoCompletedCnt(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "rsvtCallNoCompletedCnt", jsonParams);
    }

    /**
     * 상담 콜백 원본 상태 변경
     */
    public TelewebJSON cuttClbkOriginStatUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        JSONObject messageJson = new JSONObject();
        StringBuffer sInfo = new StringBuffer();
        sInfo.append("콜백 상담 완료");
        messageJson.put("CALLBACK_INFO", sInfo.toString());

        String roomId = TenantContext.getCurrentTenant() + "_" + jsonParams.getString("CUSTCO_ID");
        SseMessage sseMessage = new SseMessage();
        sseMessage.setType(SseMessage.MessageType.ENV_SETTING);
        sseMessage.setSender("2");
        sseMessage.setReceiver("ALL");    // ALL은 전체 , userId 개인별
        sseMessage.setRoomId(roomId);
        sseMessage.setSecond(-1); //필수 아님. 기본이 5초
        sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
        sseMessage.setMessage(messageJson.toString());    //메시지
        try {
            sseService.sendMessage(sseMessage);
            Thread.sleep(100);
        } catch (Exception e) {
            log.error("sseMessage is exception : " + e.getMessage());
        }

        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttClbkOriginStatUpdate", jsonParams);
    }

    /**
     * 상담 캠페인 원본 상태 변경
     */
    public TelewebJSON cuttCpiOriginStatUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttCpiOriginStatUpdate", jsonParams);
    }

    /**
     * 통합 상담 이력 노출 확장속성 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuslExpsrExpsnAttrList(TelewebJSON jsonParams) throws TelewebAppException {
        jsonParams.setHeader("ROW_CNT", 0);
        jsonParams.setHeader("PAGES_CNT", 0);
        return mobjDao.select("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuslExpsrExpsnAttrList", jsonParams);
    }

    /**
     * 상담 예약 상담 결과 저장
     */
    public TelewebJSON insertRsvtCutt(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "insertRsvtCutt", jsonParams);
    }
    
    /**
     * 이관 처리
     */
    public TelewebJSON cuttTrnsfExpsnAttrReg(TelewebJSON jsonParams) throws TelewebAppException {

        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "cuttExpsnAttrReg", jsonParams);

        return objRetParams;
    }
    
    
    /**
     * 이첩 상담 처리 프로세스
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON trnsfCuttProc(TelewebJSON jsonParams) throws TelewebAppException {
    	TelewebJSON objRetParams = new TelewebJSON();
    	
    	if("TST_REQST".equals(jsonParams.getString("STTS_CD"))) { //요청
            jsonParams.setInt("TRNSF_CUTT_ID", innbCreatCmmnService.createSeqNo("TRNSF_CUTT_ID")); //이관 상담 ID
            
    		objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "trnsfCuttReg", jsonParams);
    	}
    	
    	objRetParams = this.trnsfCuttHistReg(jsonParams);

        return objRetParams;
    }
    
    /**
     * 이첩 상담 이력 저장
     */
    public TelewebJSON trnsfCuttHistReg(TelewebJSON jsonParams) throws TelewebAppException {
    	
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.cutt.dao.CuttMapper", "trnsfCuttHistReg", jsonParams);
    }
}
