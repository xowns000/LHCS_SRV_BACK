package kr.co.hkcloud.palette3.phone.cmpgn.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("PhoneCmpgnResultService")
public class PhoneCmpgnResultServiceImpl implements PhoneCmpgnResultService
{
    private final TwbComDAO mobjDao;


    /**
     * 목록 조회
     */
    /* 실제쿼리없음. 정말 사용되는건지 확인필요 */
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
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn12", "selectRtn", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnResultMapper", "selectRtn", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPrj(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(mjsonParams);
        String result = "";
        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.selectOne("retCustSEQ","hccc.cos.CosCpn12", "selectRtnQtnaSEQ", objRetParams, objRequest);
        objRetParams2 = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnResultMapper", "selectRtnQtnaSEQ", mjsonParams);
        result = objRetParams2.getString("SCRT_QTNA_SEQ");

        //objRetParams2.setBindObject(mjsonParams2.getUserJSONObjectCopy("searchKeyField"));
        //objRetParams2.setBindString("QTNA_SEQ", result);
        mjsonParams.setString("QTNA_SEQ", result);
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn12", "selectRtnPrj", objRetParams2, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnResultMapper", "selectRtnPrj", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnDetail(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON(mjsonParams);
        String result = "";
        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.selectOne("retCustSEQ","hccc.cos.CosCpn12", "selectRtnQtnaSEQ", objRetParams, objRequest);
        //result = objRetParams.getUserJSONObject("retCustSEQ").get("SCRT_QTNA_SEQ").toString();
        objRetParams2 = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnResultMapper", "selectRtnQtnaSEQ", mjsonParams);
        result = objRetParams2.getString("SCRT_QTNA_SEQ");

        //objRetParams2.setBindObject(mjsonParams2.getUserJSONObjectCopy("searchKeyField"));
        //objRetParams2.setBindString("QTNA_SEQ", result);
        mjsonParams.setString("QTNA_SEQ", result);
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn12", "selectRtnDetail", objRetParams2, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnResultMapper", "selectRtnDetail", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnStep3(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn12", "selectRtnStep3", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnResultMapper", "selectRtnStep3", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPm(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn12", "selectRtnPm", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnResultMapper", "selectRtnPm", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPm01(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn12", "selectRtnPm01", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnResultMapper", "selectRtnPm01", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnHappyCallGroup(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCustList","hccc.cos.CosCpn12", "selectRtnHappyCallGroup", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnResultMapper", "selectRtnHappyCallGroup", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }

}
