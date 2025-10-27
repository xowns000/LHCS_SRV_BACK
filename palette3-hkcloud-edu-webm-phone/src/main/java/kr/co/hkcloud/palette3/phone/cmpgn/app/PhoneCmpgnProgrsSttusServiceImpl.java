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
@Service("PhoneCmpgnProgrsSttusService")
public class PhoneCmpgnProgrsSttusServiceImpl implements PhoneCmpgnProgrsSttusService
{
    private final TwbComDAO mobjDao;


    /**
     * 캠페인 기본 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnComboBox(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);
        //세션 값이 필요할 경우 "setBindObject" 후 다음과 같이 설정, 7개 까지 설정, 설정 값은 개발 진행에 따라 변경 예정
        //objRetParams.setBindSession(REGR_ID);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCmpList","hccc.cos.CosCpn06", "selectRtnComboBox", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "selectRtnComboBox", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 기본 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnStep01(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);
        //세션 값이 필요할 경우 "setBindObject" 후 다음과 같이 설정, 7개 까지 설정, 설정 값은 개발 진행에 따라 변경 예정
        //objRetParams.setBindSession(REGR_ID);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCmpList","hccc.cos.CosCpn06", "selectRtnStep01", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "selectRtnStep01", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 기본 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnStep02(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);
        //세션 값이 필요할 경우 "setBindObject" 후 다음과 같이 설정, 7개 까지 설정, 설정 값은 개발 진행에 따라 변경 예정
        //objRetParams.setBindSession(REGR_ID);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCmpList","hccc.cos.CosCpn06", "selectRtnStep02", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "selectRtnStep02", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 데이타 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnData(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);
        //세션 값이 필요할 경우 "setBindObject" 후 다음과 같이 설정, 7개 까지 설정, 설정 값은 개발 진행에 따라 변경 예정
        //objRetParams.setBindSession(REGR_ID);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCmpList","hccc.cos.CosCpn04", "selectRtnData", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "selectRtnData", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 미할당 상담원 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUser(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);
        //세션 값이 필요할 경우 "setBindObject" 후 다음과 같이 설정, 7개 까지 설정, 설정 값은 개발 진행에 따라 변경 예정
        //objRetParams.setBindSession(REGR_ID);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCmpList","hccc.cos.CosCpn04", "selectRtnUser", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "selectRtnData", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 할당 상담원 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnAllc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);
        //세션 값이 필요할 경우 "setBindObject" 후 다음과 같이 설정, 7개 까지 설정, 설정 값은 개발 진행에 따라 변경 예정
        //objRetParams.setBindSession(REGR_ID);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCmpList","hccc.cos.CosCpn04", "selectRtnAllc", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "selectRtnData", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 프로젝트별 데이타 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPrjNo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);
        //세션 값이 필요할 경우 "setBindObject" 후 다음과 같이 설정, 7개 까지 설정, 설정 값은 개발 진행에 따라 변경 예정
        //objRetParams.setBindSession(REGR_ID);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCmpList","hccc.cos.CosCpn04", "selectRtnPrjNo", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "selectRtnPrjNo", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 대상_리스트 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);
        //세션 값이 필요할 경우 "setBindObject" 후 다음과 같이 설정, 7개 까지 설정, 설정 값은 개발 진행에 따라 변경 예정
        //objRetParams.setBindSession(REGR_ID);

        /** 조회 페이징 */
        //DB 바인드 데이터를 설정 , user.searchKeyField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("searchKeyField"));
        //데이터 조회, user.retUserList(JSONArray)로 반환, hccc.HellowHcc:네임스페이스, selectRtnUserInfo: sqlID
        //objRetParams = mobjDao.select("retCmpList","hccc.cos.CosCpn04", "selectRtnInfo", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "selectRtnInfo", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * 캠페인 할당 데이터 저장 처리
     */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);
        // 발신쪽지 시퀀스 조회

        /** CUD 단건 */
        //DB 바인드 데이터를 설정 , user.listField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("CmpData"));  // searchKeyField
        objJsonParams.setDataObject(mjsonParams.getDataObject("CmpData"));  // searchKeyField

        //세션 값이 필요할 경우 설정한다.
        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

        //objRetParams = mobjDao.insert("hccc.tables.UC_CMP_OBJ_LIST", "INSERT_UC_CMP_OBJ_LIST", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "INSERT_UC_CMP_OBJ_LIST", objJsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 캠페인 데이터 삭제 처리
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** CUD 단건 */
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("CmpData"));  // searchKeyField
        objJsonParams.setDataObject(mjsonParams.getDataObject("CmpData"));  // searchKeyField

        //validationProcess(objRetParams, objRequest);
        //objRetParams = mobjDao.update("hccc.tables.UC_CMP_OBJ_LIST", "DELETE_UC_CMP_OBJ_LIST", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnProgrsSttusMapper", "DELETE_UC_CMP_OBJ_LIST", objJsonParams);

        //최종결과값 반환
        return objRetParams;
    }
}
