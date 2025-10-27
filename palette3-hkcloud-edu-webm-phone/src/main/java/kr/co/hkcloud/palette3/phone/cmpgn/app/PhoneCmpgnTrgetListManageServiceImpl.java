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
@Service("PhoneCmpgnTrgetListManageService")
public class PhoneCmpgnTrgetListManageServiceImpl implements PhoneCmpgnTrgetListManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 캠페인 대상 리스트 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn05", "selectRtn", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTrgetListManageMapper", "selectRtn", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 할당상담원 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn01(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn05", "selectRtn01", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTrgetListManageMapper", "selectRtn01", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 처리결과 콤보
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnComboBox(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn05", "selectRtnComboBox", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTrgetListManageMapper", "selectRtnComboBox", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 고객데이터파일콤보
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnComboBox01(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn05", "selectRtnComboBox01", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTrgetListManageMapper", "selectRtnComboBox01", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 회수실행
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtn01(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = new TelewebJSON();
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
                objJsonParams.setDataObject("DATA", jArray.getJSONObject(i));
                //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                //objRetParams = mobjDao.update("hccc.cos.CosCpn05", "updateRtn01", objRetParams, objRequest);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTrgetListManageMapper", "updateRtn01", objJsonParams);
                //objRetParams = mobjDao.insert("hccc.cos.CosCpn05", "insertRtn01", objRetParams, objRequest);
            }
        }
        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 회수실행이력저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtn01(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = new TelewebJSON();

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
                objJsonParams.setDataObject("DATA", jArray.getJSONObject(i));
                //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                //objRetParams = mobjDao.insert("hccc.cos.CosCpn05", "insertRtn01", objRetParams, objRequest);
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTrgetListManageMapper", "insertRtn01", objJsonParams);
            }
        }
        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 할당실행
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtn02(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = new TelewebJSON();

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
                objJsonParams.setDataObject("DATA", jArray.getJSONObject(i));
                //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                //objRetParams = mobjDao.update("hccc.cos.CosCpn05", "updateRtn02", objRetParams, objRequest);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTrgetListManageMapper", "updateRtn02", objJsonParams);

            }
        }
        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 할당실행이력저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtn02(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = new TelewebJSON();

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
                objJsonParams.setDataObject("DATA", jArray.getJSONObject(i));
                //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                //objRetParams = mobjDao.insert("hccc.cos.CosCpn05", "insertRtn02", objRetParams, objRequest);
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTrgetListManageMapper", "insertRtn02", objJsonParams);
            }
        }
        /** 결과 반환 */
        return objRetParams;
    }

}
