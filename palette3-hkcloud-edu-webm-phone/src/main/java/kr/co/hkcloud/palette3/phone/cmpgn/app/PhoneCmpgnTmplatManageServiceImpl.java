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
@Service("PhoneCmpgnTmplatManageService")
public class PhoneCmpgnTmplatManageServiceImpl implements PhoneCmpgnTmplatManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 캠페인 조회
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
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn02", "selectRtn", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTmplatManageMapper", "selectRtn", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 템플릿 항목 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn2(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn02", "selectRtn2", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTmplatManageMapper", "selectRtn2", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 템플릿 파일 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn3(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn02", "selectRtn3", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTmplatManageMapper", "selectRtn3", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 템플릿 컬럼 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON processRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objJsonParams = new TelewebJSON();
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        //JSONArray jArray = mjsonParams.getUserJSONArrayCopy("CmpData");
        JSONArray jArray = mjsonParams.getDataObject();

        if(jArray != null && jArray.size() > 0) {
            for(int i = 0; i < jArray.size(); i++) {
                //objRetParams.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                objJsonParams.setDataObject("DATA", jArray.getJSONObject(i));
                //세션 값이 필요할 경우 설정한다.
                //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                String flgCd = mjsonParams.getString("flgCd");

                if("D".equals(flgCd)) {
                    //objRetParams = mobjDao.delete("hccc.cos.CosCpn02", "DELETE_UC_CMP_TPL_COLM", objRetParams, objRequest);
                    objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTmplatManageMapper", "DELETE_UC_CMP_TPL_COLM", objJsonParams);
                }
                else if("C".equals(flgCd)) {
                    //objRetParams = mobjDao.insert("hccc.cos.CosCpn02", "INSERT_UC_CMP_TPL_COLM", objRetParams, objRequest);
                    objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTmplatManageMapper", "INSERT_UC_CMP_TPL_COLM", objJsonParams);
                }
            }
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 캠페인 템플릿 저장
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON processRtnMain(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objJsonParams = new TelewebJSON();
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        //JSONArray jArray = mjsonParams.getUserJSONArrayCopy("CmpData");
        JSONArray jArray = mjsonParams.getDataObject();

        if(jArray != null && jArray.size() > 0) {
            for(int i = 0; i < jArray.size(); i++) {
                //objRetParams.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                objJsonParams.setDataObject("DATA", jArray.getJSONObject(i));
                //세션 값이 필요할 경우 설정한다.
                //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
                String flgCd = mjsonParams.getString("flgCd");

                if("D".equals(flgCd)) {
                    //objRetParams = mobjDao.delete("hccc.cos.CosCpn02", "DELETE_UC_CMP_TPL", objRetParams, objRequest);
                    objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTmplatManageMapper", "DELETE_UC_CMP_TPL", objJsonParams);
                }
                else if("C".equals(flgCd)) {
                    //objRetParams = mobjDao.insert("hccc.cos.CosCpn02", "INSERT_UC_CMP_TPL", objRetParams, objRequest);
                    objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnTmplatManageMapper", "INSERT_UC_CMP_TPL", objJsonParams);
                }
            }
        }

        //최종결과값 반환
        return objRetParams;
    }

}
