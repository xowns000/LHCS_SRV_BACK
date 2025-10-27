package kr.co.hkcloud.palette3.phone.history.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.phone.cutt.app.CuttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("phoneHistoryInqireService")
public class PhoneHistoryInqireServiceImpl implements PhoneHistoryInqireService {

    private final TwbComDAO mobjDao;
    private final CuttService cuttService;
    private final InnbCreatCmmnService innbCreatCmmnService;

    /**
     * 상담원그룹정보 조회
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectRtnTmKind(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "selectRtnTmKind", mjsonParams);
    }

    /**
     * 상담이력관리 목록 조회(공제지원)
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectRtnCnslHistMngDe(TelewebJSON mjsonParams) throws TelewebAppException {
        log.debug("mjsonParams ================       " + mjsonParams);
        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "selectRtnCnslHistMngDe", mjsonParams);
    }

    /**
     * 상담이력관리 수정(공제지원)
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnCnslHistMngDe(TelewebJSON mjsonParams) throws TelewebAppException {
        log.debug("!@#!@#!@#!@#++++++_______________-------------" + mjsonParams);
        return mobjDao.update("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "updateRtnCnslHistMngDe", mjsonParams);
    }

    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnCnslHistMngDe2(TelewebJSON mjsonParams) throws TelewebAppException {
        log.debug("mjsonParams" + mjsonParams);
        return mobjDao.update("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "updateRtnCnslHistMngDe2", mjsonParams);
    }

    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertCnslChngHistDe(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "insertCnslChngHistDe", mjsonParams);
    }

    /**
     * 상담이력조회 칭찬콜등록
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertCnslCmptCall(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "insertCnslCmptCall", mjsonParams);
    }

    /**
     * 녹취ID 기준의 조회고객별 상담저장여부 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslHistSaveYnDe(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "selectRtnCnslHistSaveYnDe",
            mjsonParams);
    }

    /**
     * 조합검색 팝업 조회 (기존 기간계) > 하드코딩
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON SELMCZ10106(TelewebJSON jsonParams) throws TelewebAppException {
        //TelewebJSON objRetParams = new TelewebJSON();

        jsonParams.setString("cuocNo", "03022");
        jsonParams.setString("crunNm", "믿음");
        jsonParams.setString("hgrOrzNm", "인천경기지역본부");
        jsonParams.setString("mnocBroDvCd", "1");
        jsonParams.setString("mnocBroDvNm", "본�");
        jsonParams.setString("crunSttCd", "�");
        jsonParams.setString("crunSttNm", "�1정상");
        jsonParams.setString("crunTelno", "042-720-1792");
        jsonParams.setString("orzCrtnDt", "");
        jsonParams.setString("orzClsnDt", "");
        jsonParams.setString("areHdqCd", "03");
        jsonParams.setString("areHdqNm", "인천경�");
        jsonParams.setString("hgrOrzCd", "�� 0");

        jsonParams.setHeader("TOT_COUNT", 1);
        jsonParams.setHeader("COUNT", 1);
        return jsonParams;
    }

    /**
     * 사용자 정보 목록 조회 팝업
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserInfoPop(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "selectRtnUserInfoPop", mjsonParams);
    }

    /**
     * 상담이력출력관리 등록
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertCnslHistOutputMng(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "insertCnslHistOutputMng", mjsonParams);
    }

    /**
     * 전화상담 이력 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuttHistList(TelewebJSON mjsonParams) throws TelewebAppException {
        //고객 확장 속성 검색
        String SCH_CUST_EXPSN_ATTR = mjsonParams.getString("SCH_CUST_EXPSN_ATTR").toString();
        SCH_CUST_EXPSN_ATTR = SCH_CUST_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        mjsonParams.setString("SCH_CUST_EXPSN_ATTR", SCH_CUST_EXPSN_ATTR);

        //상담 확장 속성 검색
        String SCH_CUTT_EXPSN_ATTR = mjsonParams.getString("SCH_CUTT_EXPSN_ATTR").toString();
        SCH_CUTT_EXPSN_ATTR = SCH_CUTT_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        mjsonParams.setString("SCH_CUTT_EXPSN_ATTR", SCH_CUTT_EXPSN_ATTR);

        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttHistList", mjsonParams);
    }

    /**
     * 전화상담 이력 관리 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuttHistMngList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttHistMngList", mjsonParams);
    }

    /**
     * 전화상담 내역 변경 정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuttChgInfo(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttChgInfo", mjsonParams);
    }

    /**
     * 전화상담 내역 변경 요청 승인
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON cuttChgAprvProc(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = this.cuttChgUpdate(mjsonParams); //변경 요청 상태 변경 처리

        objRetParams = this.cuttExpsnAttrUpdate(mjsonParams); // 상담 내용 확장 속성 변경 처리

        TelewebJSON getRsvtDt = mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "chgCuttRsvtDt",
            mjsonParams);
        if ("".equals(((JSONArray) getRsvtDt.getDataObject()).getJSONObject(0).getString("RSVT_DT"))) {
            objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttRsvtDtDel",
                mjsonParams);
        } else {
            mjsonParams.setString("RSVT_DT", ((JSONArray) getRsvtDt.getDataObject()).getJSONObject(0).getString("RSVT_DT"));

            TelewebJSON getCuttRsvtDtList = mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper",
                "cuttRsvtDtList", mjsonParams);
            JSONArray cuttRsvtDtList = getCuttRsvtDtList.getDataObject();

            if (cuttRsvtDtList.size() > 0) {
                objRetParams = this.cuttRsvtDtUpdate(mjsonParams); // 예약일 변경 처리
            } else {
                int CUTT_RSVT_ID = innbCreatCmmnService.createSeqNo("CUTT_RSVT_ID");
                mjsonParams.setInt("CUTT_RSVT_ID", CUTT_RSVT_ID);

                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttRsvtDtReg",
                    mjsonParams);
            }
        }

        objRetParams = this.cuttCnUpdate(mjsonParams); // 상담 내용 변경 처리

        return objRetParams;
    }

    /**
     * 전화상담 내역 일괄 변경 요청 승인
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON batchCuttChgAprvProc(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();

        TelewebJSON getPhnCuttChgAprvList = mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper",
            "getPhnCuttChgAprvList", mjsonParams);

        JSONArray aprvList = getPhnCuttChgAprvList.getDataObject(); //전화 상담 변경 대기 목록

        if (aprvList.size() > 0) {

            for (int i = 0; i < aprvList.size(); i++) {
                JSONObject dataObject = aprvList.getJSONObject(i);

                mjsonParams.setString("PHN_CUTT_ID", dataObject.getString("PHN_CUTT_ID"));
                mjsonParams.setString("CHG_HSTRY_ID", dataObject.getString("CHG_HSTRY_ID"));

                objRetParams = this.cuttChgUpdate(mjsonParams); //변경 요청 상태 변경 처리

                objRetParams = this.cuttExpsnAttrUpdate(mjsonParams); // 상담 내용 확장 속성 변경 처리

                objRetParams = this.cuttRsvtDtUpdate(mjsonParams); // 예약일 변경 처리

                objRetParams = this.cuttCnUpdate(mjsonParams); // 상담 내용 변경 처리
            }

        }

        return objRetParams;
    }

    /**
     * 상담 내용 확장 속성 변경 처리
     */
    public TelewebJSON cuttExpsnAttrUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttExpsnAttrUpdate", jsonParams);
    }

    /**
     * 상담 내용 변경 처리
     */
    public TelewebJSON cuttCnUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttCnUpdate", jsonParams);
    }

    /**
     * 변경 요청 상태 변경 처리
     */
    public TelewebJSON cuttChgUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttChgUpdate", jsonParams);
    }

    /**
     * 예약일 변경 처리
     */
    public TelewebJSON cuttRsvtDtUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttRsvtDtUpdate", jsonParams);
    }

    /**
     * 전화상담 이력 엑셀 다운로드
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON cuttHistExcelDwnld(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams); // 반환 파라미터 생성

        JSONArray arrExpsnAttrData = jsonParams.getDataObject();
        JSONObject expsnAttrObj = new JSONObject();
        expsnAttrObj.put("EXPSN_ATTR_LIST", arrExpsnAttrData);
        expsnAttrObj.put("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
        expsnAttrObj.put("USER_ID", jsonParams.getString("USER_ID"));
        expsnAttrObj.put("SCH_CUTT_ST_DTS", jsonParams.getString("SCH_CUTT_ST_DTS"));
        expsnAttrObj.put("SCH_CUTT_END_DTS", jsonParams.getString("SCH_CUTT_END_DTS"));

        JSONArray arrParam = new JSONArray();
        arrParam.add(expsnAttrObj);

        TelewebJSON joParams = new TelewebJSON(jsonParams);
        joParams.setDataObject(arrParam);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttHistExcelDwnldTbl",
            joParams);

        return objRetParams;
    }

    /**
     * 전화상담 이력 상담, 고객 확장속성 정보 조회
     */

    @Transactional(readOnly = true)
    public TelewebJSON cuttHistGetExpsnAttr(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "cuttHistGetExpsnAttr", jsonParams);
    }

    /**
     * 나의 상담 이력 관리 차트 정보
     */
    public TelewebJSON getMyCuttHistCnt(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.history.dao.PhoneHistoryInqireMapper", "getMyCuttHistCnt", jsonParams);
    }
}
