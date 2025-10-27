package kr.co.hkcloud.palette3.phone.campaign.app;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("PhoneCampaignCustMngService")
public class PhoneCampaignCustMngServiceImpl implements PhoneCampaignCustMngService {

    public final TwbComDAO mobjDao;
    public final InnbCreatCmmnService innbCreatCmmnService;
    private final String splNameSpace = "kr.co.hkcloud.palette3.phone.campaign.dao.PhoneCampaignCustMngMapper";

    /**
     * 캠페인 계획리스트를 조회한다.
     *
     * @param jsonParams
     * @return objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectComboCpiCustMng(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(splNameSpace, "selectComboCpiCustMng", jsonParams);
    }

    /**
     * 캠페인 TOP리스트를 조회한다.
     *
     * @param jsonParams
     * @return objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTopCpiCustMng(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(splNameSpace, "selectTopCpiCustMng", jsonParams);
    }

    /**
     * 캠페인관리항목을 조회한다.
     *
     * @param jsonParams
     * @return objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAttrCustMng(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(splNameSpace, "selectAttrCustMng", jsonParams);
    }

    /**
     * 항목의 순서를 재 정의 한다.
     *
     * @param jsonParams
     * @return objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON reOrderAttrCustMng(TelewebJSON jsonParams) throws TelewebAppException {
        List<String> arrAttrId = new LinkedList<String>();
        JSONArray jsonObj = jsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
            JSONObject objData = jsonObj.getJSONObject(i);

            @SuppressWarnings("rawtypes") Iterator it = objData.keys();
            while (it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if (strKey.indexOf("arrAttrId") > -1 && StringUtils.isNotEmpty(strValue)) {
                    arrAttrId.add(strValue);
                }
            }
        }

        if (arrAttrId.size() != 0) {
            jsonParams.setObject("arrAttrId", 0, arrAttrId);
        }

        return mobjDao.update(splNameSpace, "reOrderAttrCustMng", jsonParams);
    }

    /**
     * 신규 관리항목등록을 한다.
     *
     * @param jsonParams
     * @return objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON upsertAttrCustMng(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams); // 반환 파라메터 생성

        //등록
        if (StringUtils.isBlank(jsonParams.getString("ATTR_ID"))) {
            int ATTR_ID = innbCreatCmmnService.createSeqNo("ATTR_ID");
            jsonParams.setInt("ATTR_ID", ATTR_ID);

            objRetParams = mobjDao.insert(splNameSpace, "insertAttrCustMng", jsonParams);
        } else { //수정
            objRetParams = mobjDao.update(splNameSpace, "updateAttrCustMng", jsonParams);
        }

        return objRetParams;
    }

    /**
     * 관리항목을삭제 한다.
     *
     * @param jsonParams
     * @return objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteAttrCustMng(TelewebJSON jsonParams) throws TelewebAppException {
        List<String> arrAttrId = new LinkedList<String>();
        JSONArray jsonObj = jsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int i = 0; i < jsonObj.size(); i++) {
            JSONObject objData = jsonObj.getJSONObject(i);

            @SuppressWarnings("rawtypes") Iterator it = objData.keys();
            while (it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if (strKey.indexOf("arrAttrId") > -1 && StringUtils.isNotEmpty(strValue)) {
                    arrAttrId.add(strValue);
                }
            }
        }

        if (arrAttrId.size() != 0) {
            jsonParams.setObject("arrAttrId", 0, arrAttrId);
        }

        return mobjDao.update(splNameSpace, "deleteAttrCustMng", jsonParams);
    }

    /**
     * 대상자를조회 한다.
     *
     * @param jsonParams
     * @return objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCustCustMng(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성

        TelewebJSON custExpsnInfo = this.custExpsnInfo(jsonParams);

        List<String> arrAttrColId = new LinkedList<String>();
        if (custExpsnInfo.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {

            JSONArray jsonObj = custExpsnInfo.getDataObject(TwbCmmnConst.G_DATA);
            for (int i = 0; i < jsonObj.size(); i++) {
                JSONObject objData = jsonObj.getJSONObject(i);

                @SuppressWarnings("rawtypes") Iterator it = objData.keys();
                while (it.hasNext()) {
                    String strKey = (String) it.next();
                    String strValue = objData.getString(strKey);

                    if (strKey.indexOf("EXPSN_ATTR_COL_ID") > -1 && StringUtils.isNotEmpty(strValue)) {
                        arrAttrColId.add(strValue);
                    }
                }
            }
        }
        if (arrAttrColId.size() > 0) {
            jsonParams.setObject("arrAttrColId", 0, arrAttrColId);
        }

        return mobjDao.select(splNameSpace, "selectCustCustMng", jsonParams);
    }

    /**
     * 대상자 항목을 조회 한다.
     *
     * @param jsonParams
     * @return objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON custExpsnInfo(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(); // 반환 파라메터 생성

        jsonParams.setString("SE", "CAMP");
        objRetParams = mobjDao.select(splNameSpace, "custExpsnInfo", jsonParams);

        return objRetParams;
    }

    /**
     * 대상자를 업로드 한다.
     *
     * @param jsonParams
     * @return objParam
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON uploadExcelCustMng(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON();

        //배분되어있는지 체크
        objRetParams = mobjDao.select(splNameSpace, "chkAltmntCustMng", jsonParams);
        if (objRetParams.getSize() > 0) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "ALT");
            return objRetParams;
        }
        //대상자 속성삭제
        objRetParams = mobjDao.delete(splNameSpace, "deleteCustDtlCustMng", jsonParams);
        if (!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
            //대상자 삭제 전 고객id를 가져온다.
            TelewebJSON oParams = mobjDao.select(splNameSpace, "selectCustMtCustMng", jsonParams);
            //대상자 삭제
            objRetParams = mobjDao.delete(splNameSpace, "deleteCustCustMng", jsonParams);
            if (!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                //고객정보가있다면 아래 수행
                if (oParams.getSize() > 0) {
                    //foreach를 위한 array설정
                    JSONArray arrData = oParams.getDataObject();
                    JSONObject oCustList = new JSONObject();
                    oCustList.put("CUST_LIST", arrData);
                    oCustList.put("CPI_ID", jsonParams.getString("CPI_ID"));
                    JSONArray oParam = new JSONArray();
                    oParam.add(oCustList);
                    TelewebJSON joParams = new TelewebJSON(jsonParams);
                    joParams.setDataObject(oParam);

                    //통화이력이있다면 모두 리턴, 롤백
                    objRetParams = mobjDao.select(splNameSpace, "chkCuttCustMng", joParams);
                    if (objRetParams.getSize() > 0) {
                        throw new TelewebAppException("CUT");
                    }
                    //고객마스터 속성 삭제
                    objRetParams = mobjDao.delete(splNameSpace, "deleteCustAttrCustMng", joParams);
                    if (!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                        //고객 통합 이력 삭제
                        objRetParams = mobjDao.delete(splNameSpace, "deleteCustItgrtHstry", joParams);

                        //고객 전화번호 통합 삭제
                        objRetParams = mobjDao.delete(splNameSpace, "deleteCustTelnoHstry", joParams);

                        //고객마스터 삭제
                        objRetParams = mobjDao.delete(splNameSpace, "deleteCustMtCustMng", joParams);
                    }
                }

                //upload시작
                JSONArray jsonArray = jsonParams.getDataObject();
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < jsonArray.size(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    TelewebJSON teleObjJson = new TelewebJSON();
                    teleObjJson.setString("CPI_ID", jsonObject.getString("CPI_ID"));
                    teleObjJson.setString("CUST_NM", jsonObject.getString("CUST_NM"));
                    teleObjJson.setString("CUST_PHN_NO", jsonObject.getString("CUST_PHN_NO"));
                    teleObjJson.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
                    teleObjJson.setString("USER_ID", jsonParams.getString("USER_ID"));

                    //                        //통화이력이있다면 모두 리턴, 롤백
                    //                        objRetParams = mobjDao.select(splNameSpace, "chkCuttCustMng", teleObjJson);
                    //                        if(objRetParams.getSize() > 0){
                    //                            throw new TelewebAppException("CUT");
                    //                        }

                    objRetParams = mobjDao.select(splNameSpace, "chkCustMtCustMng", teleObjJson);
                    //있다면 cust_id 셋팅
                    if (objRetParams.getSize() > 0) {
                        teleObjJson.setString("CUST_ID", objRetParams.getString("CUST_ID"));
                    } else {
                        //고객 전화번호 테이블에 데이터가 없을때만 저장
                        objRetParams = mobjDao.select(splNameSpace, "chkCustTelno", teleObjJson);
                        if (objRetParams.getSize() > 0) {
                            teleObjJson.setString("CUST_TELNO_ID", objRetParams.getString("CUST_TELNO_ID"));
                        } else {
                            teleObjJson.setString("CUST_TELNO_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CUST_TELNO_ID")));
                        }

                        //고객마스터에 데이터가 없을때만 저장
                        teleObjJson.setString("CUST_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CUST_ID")));
                        objRetParams = mobjDao.insert(splNameSpace, "insertCustMtCustMng", teleObjJson);
                    }

                    //캠페인 고객 저장
                    objRetParams = mobjDao.insert(splNameSpace, "insertCustCustMng", teleObjJson);
                    //고객 확장 정보 고객상태 정상 강제 적용
                    objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.customer.dao.SettingCustomerInformationListMapper",
                        "custExpsnAttrForceReg", teleObjJson);

                    Iterator<String> iterator = jsonArray.getJSONObject(0).keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        //속성항목 이외 컬럼 제외
                        if (!key.equals("CUST_NM") && !key.equals("CUST_PHN_NO") && !key.equals("CUSTCO_ID") && !key.equals("ROW_NUMBER")
                            && !key.equals("CPI_ID") && !key.equals("USER_ID") && !key.equals("PP_KEY_PP") && !key.equals("PP_ALG_PP")
                            && !key.equals("LOCALE")) {

                            teleObjJson.setString("ATTR_VL", jsonObject.getString(key));
                            teleObjJson.setString("EXPSN_ATTR_COL_ID", key);
                            objRetParams = mobjDao.select(splNameSpace, "chkEsntlCustMng", teleObjJson);
                            //필수인데 값이 없다면 리턴, 롤백
                            if (objRetParams.getString("ESNTL_YN").equals("Y") && jsonObject.getString(key).equals("")) {
                                throw new TelewebAppException("UPL|" + Integer.toString(i + 1) + "|" + key);
                            }
                            teleObjJson.setString("ATTR_ID", objRetParams.getString("ATTR_ID"));
                            objRetParams = mobjDao.insert(splNameSpace, "insertCustDtlCustMng", teleObjJson);
                        }
                    }
                }
            }
        }
        return objRetParams;
    }
}
