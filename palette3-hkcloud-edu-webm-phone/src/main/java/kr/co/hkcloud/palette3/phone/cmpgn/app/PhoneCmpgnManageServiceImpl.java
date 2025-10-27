package kr.co.hkcloud.palette3.phone.cmpgn.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@Service("PhoneCmpgnManageService")
public class PhoneCmpgnManageServiceImpl implements PhoneCmpgnManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 캠페인 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "selectRtn", mjsonParams);
        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 상담원 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn2(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "selectRtn2", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "selectRtn2", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인데이터 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn3(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "selectRtn3", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "selectRtn3", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 고객데이터 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn4(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "selectRtn4", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "selectRtn4", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 불량데이터 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn5(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "selectRtn5", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "selectRtn5", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인콤보 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCombo01(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "selectRtnCombo01", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "selectRtnCombo01", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 파일템플릿콤보 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCombo02(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "selectRtnCombo02", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "selectRtnCombo02", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 등록
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlI

        String flgCd = objRetParams.getString("flgCd");

        if("U".equals(flgCd)) {
            //objRetParams = mobjDao.update("hccc.cos.CosCpn01", "UPDATE_UC_CMP_BASE", objRetParams, objRequest);
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "UPDATE_UC_CMP_BASE", mjsonParams);
        }
        else if("C".equals(flgCd)) {
            //objRetParams = mobjDao.insert("hccc.cos.CosCpn01", "INSERT_UC_CMP_BASE", objRetParams, objRequest);
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "INSERT_UC_CMP_BASE", mjsonParams);
        }

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 삭제
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlI

        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

        //objRetParams = mobjDao.insert("hccc.cos.CosCpn01", "DELETE_UC_CMP_BASE", objRetParams, objRequest);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "DELETE_UC_CMP_BASE", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * CTC 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCTC(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlI

        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "SELECT_UC_CMP_TPL_COLM", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "SELECT_UC_CMP_TPL_COLM", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인시퀀스 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnGetSEQ(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlI

        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "SELECT_GET_CMP_DATA_SEQ", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "SELECT_GET_CMP_DATA_SEQ", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnOBJLIST(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON objRetParams2 = new TelewebJSON();
        TelewebJSON objRetParams3 = new TelewebJSON();

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        String result = "";
        String result2 = "";
        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //JSONArray jArray = mjsonParams.getUserJSONArrayCopy("searchKeyField");
        JSONArray jArray = mjsonParams.getDataObject();

        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlI
        if(jArray != null && jArray.size() > 0) {
            for(int i = 0; i < jArray.size(); i++) {

                //objRetParams.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                //objRetParams.setBindObject(mjsonParams. getJSONObjectCopy(jArray.getJSONObject(i)));
                objRetParams.setDataObject("DATA", jArray.getJSONObject(i));

                //objRetParams = mobjDao.selectOne("retCustInfo","hccc.cos.CosCpn01", "SELECT_GET_CUST_NO", objRetParams, objRequest);
                objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "SELECT_GET_CUST_NO", mjsonParams);
                result = objRetParams.getString("CUST_NO");

                //objRetParams2.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                objRetParams2.setDataObject("DATA", jArray.getJSONObject(i));
                objRetParams2.setString("CUST_NO", result);

                objRetParams2.setString("MULTI_LANG", objRetParams2.getHeaderString("MULTI_LANG"));
                //objRetParams2.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                //objRetParams2 = mobjDao.insert("hccc.cos.CosCpn01", "INSERT_UC_CMP_OBJ_LIST", objRetParams2, objRequest);
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "INSERT_UC_CMP_OBJ_LIST", mjsonParams);

                //objRetParams3.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                objRetParams3.setDataObject("DATA", jArray.getJSONObject(i));
                objRetParams3.setString("KEY_NO", result2);
                objRetParams3.setString("MULTI_LANG", objRetParams3.getHeaderString("MULTI_LANG"));
                //objRetParams3.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                //objRetParams3 = mobjDao.insert("hccc.cos.CosCpn01", "INSERT_UC_CMP_OBJ_DTL", objRetParams3, objRequest);
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "INSERT_UC_CMP_OBJ_DTL", mjsonParams);

            }
        }

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnOBJDTL(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //JSONArray jArray = mjsonParams.getUserJSONArrayCopy("searchKeyField");
        JSONArray jArray = mjsonParams.getDataObject();
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlI
        if(jArray != null && jArray.size() > 0) {
            for(int i = 0; i < jArray.size(); i++) {
                //objRetParams.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                objRetParams.setDataObject("DATA", jArray.getJSONObject(i));
                //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                //objRetParams = mobjDao.insert("hccc.cos.CosCpn01", "INSERT_UC_CMP_OBJ_DTL", objRetParams, objRequest);
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "INSERT_UC_CMP_OBJ_DTL", mjsonParams);
            }
        }
        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnCmpData(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlI
        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
        //objRetParams = mobjDao.insert("hccc.cos.CosCpn01", "INSERT_UC_CMP_DATA", objRetParams, objRequest);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "INSERT_UC_CMP_DATA", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON upDataRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //JSONArray jArray = mjsonParams.getUserJSONArrayCopy("searchKeyField");
        JSONArray jArray = mjsonParams.getDataObject();
        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlI

        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

        if(jArray != null && jArray.size() > 0) {
            for(int i = 0; i < jArray.size(); i++) {
                //objRetParams.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                objRetParams.setDataObject("DATA", jArray.getJSONObject(i));
                //objRetParams = mobjDao.update("hccc.cos.CosCpn01", "upDataRtn", objRetParams, objRequest);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "upDataRtn", mjsonParams);
            }
        }

        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "SELECT_GET_CUST_NO", objRetParams, objRequest);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnErrorData(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //JSONArray jArray = mjsonParams.getUserJSONArrayCopy("searchKeyField");
        JSONArray jArray = mjsonParams.getDataObject();
        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlI

        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

        if(jArray != null && jArray.size() > 0) {
            for(int i = 0; i < jArray.size(); i++) {
                //objRetParams.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                objRetParams.setDataObject("DATA", jArray.getJSONObject(i));
                //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                //objRetParams = mobjDao.insert("hccc.cos.CosCpn01", "INSERT_UC_CMP_ERR_DATA", objRetParams, objRequest);
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.phoneCmpgnManageMapper", "INSERT_UC_CMP_ERR_DATA", mjsonParams);
            }
        }

        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn01", "SELECT_GET_CUST_NO", objRetParams, objRequest);

        /** 결과 반환 */
        return objRetParams;
    }

}
