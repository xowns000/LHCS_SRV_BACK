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
@Service("PhoneCmpgnAsgnService")
public class PhoneCmpgnAsgnServiceImpl implements PhoneCmpgnAsgnService
{
    private final TwbComDAO mobjDao;


    /**
     * ASIS 캠페인 기본 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn(TelewebJSON mjsonParams) throws TelewebAppException
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
        //objRetParams = mobjDao.select("retCmpList","hccc.cos.CosCpn04", "selectRtn", objRetParams, objRequest);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "selectRtn", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * ASIS 캠페인 데이타 조회
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
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "selectRtnData", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * ASIS 캠페인 미할당 상담원 조회
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
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "selectRtnUser", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * ASIS 캠페인 할당 상담원 조회
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
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "selectRtnAllc", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * ASIS 캠페인 프로젝트별 데이타 조회
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
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "selectRtnPrjNo", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * ASIS 캠페인 대상_리스트 조회
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
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "selectRtnInfo", mjsonParams);

        /** 결과 반환 */
        return objRetParams;
    }


    /**
     * ASIS 캠페인 할당 데이터 저장 처리
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
        objJsonParams.setDataObject("DATA", mjsonParams.getDataObject("CmpData"));

        //세션 값이 필요할 경우 설정한다.
        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

        //objRetParams = mobjDao.insert("hccc.tables.UC_CMP_OBJ_LIST", "INSERT_UC_CMP_OBJ_LIST", objRetParams, objRequest);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "INSERT_UC_CMP_OBJ_LIST", objJsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * ASIS 캠페인할당 실행
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtn1(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** CUD 단건 */
        //DB 바인드 데이터를 설정 , user.listField 추출
        // objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("CmpData"));  // searchKeyField

        //세션 값이 필요할 경우 설정한다.
        // objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

        //JSONArray jArray = mjsonParams.getUserJSONArrayCopy("CmpData");
        JSONArray jArray = mjsonParams.getDataObject("CmpData");
        if(jArray != null && jArray.size() > 0) {
            for(int i = 0; i < jArray.size(); i++) {
                //objRetParams.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                objJsonParams.setDataObject("DATA", jArray.getJSONObject(i));
                //세션 값이 필요할 경우 설정한다.
                // objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

                //String flgCd = objRetParams.getBindString("flgCd");
                String flgCd = mjsonParams.getString("flgCd");
                if("U".equals(flgCd)) {
                    //objRetParams = mobjDao.update("hccc.tables.UC_CMP_OBJ_LIST", "UPDATE_UC_CMP_OBJ_LIST_1", objRetParams, objRequest);
                    objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "UPDATE_UC_CMP_OBJ_LIST_1", objJsonParams);
                }
            }
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * ASIS 캠페인할당 실행 프로젝트별
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtn2(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** CUD 단건 */
        //DB 바인드 데이터를 설정 , user.listField 추출
        //objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("CmpData"));  // searchKeyField
        objJsonParams.setDataObject(mjsonParams.getDataObject("CmpData"));

        //세션 값이 필요할 경우 설정한다.
        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

        //objRetParams = mobjDao.update("hccc.tables.UC_CMP_OBJ_LIST", "UPDATE_UC_CMP_OBJ_LIST_2", objRetParams, objRequest);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "UPDATE_UC_CMP_OBJ_LIST_2", objJsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 캠페인할당 회수
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtn3(TelewebJSON mjsonParams) throws TelewebAppException
    {
        /** 필수 선언 */
        //Response TelewebJSON 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objJsonParams = new TelewebJSON(mjsonParams);

        /** 세션 정보 */
        //TelewebGetSession tSession = new TelewebGetSession(objRequest);

        /** CUD 단건 */
        //DB 바인드 데이터를 설정 , user.listField 추출
        // objRetParams.setBindObject(mjsonParams.getUserJSONObjectCopy("CmpData"));  // searchKeyField

        //세션 값이 필요할 경우 설정한다.
        //objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);
        //JSONArray jArray = mjsonParams.getUserJSONArrayCopy("CmpData");
        JSONArray jArray = mjsonParams.getDataObject("CmpData");

        if(jArray != null && jArray.size() > 0) {
            for(int i = 0; i < jArray.size(); i++) {
                //objRetParams.setBindObject(mjsonParams.getJSONObjectCopy(jArray.getJSONObject(i)));
                objJsonParams.setDataObject("DATA", jArray.getJSONObject(i));
                //세션 값이 필요할 경우 설정한다.
                // objRetParams.setBindSession(tSession.REG_USER_ID,tSession.CHG_USER_ID);

                //String flgCd = objRetParams.getBindString("flgCd");
                String flgCd = mjsonParams.getString("flgCd");

                if("U".equals(flgCd)) {
                    //objRetParams = mobjDao.update("hccc.tables.UC_CMP_OBJ_LIST", "UPDATE_UC_CMP_OBJ_LIST_3", objRetParams, objRequest);
                    objRetParams = mobjDao.update("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "UPDATE_UC_CMP_OBJ_LIST_3", objJsonParams);
                }
            }
        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * ASIS 캠페인 데이터 삭제 처리
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
        objJsonParams.setDataObject(mjsonParams.getDataObject("CmpData"));

        //validationProcess(objRetParams, objRequest);
        //objRetParams = mobjDao.update("hccc.tables.UC_CMP_OBJ_LIST", "DELETE_UC_CMP_OBJ_LIST", objRetParams, objRequest);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.cmpgn.dao.PhoneCmpgnAsgnMapper", "DELETE_UC_CMP_OBJ_LIST", objJsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
