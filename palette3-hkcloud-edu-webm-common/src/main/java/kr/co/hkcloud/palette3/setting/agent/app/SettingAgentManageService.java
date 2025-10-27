package kr.co.hkcloud.palette3.setting.agent.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface SettingAgentManageService {

    TelewebJSON selectRtnUserInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    //    TelewebJSON selectRtnUserInfo_new(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON processRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON updatePasswordReset(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON updatePasswordUnLock(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectRtnUserBaseInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectTwbBas01(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectTwbBas01pWD(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON insertRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON mergeUserChatAgent(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON updateRtnUser(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON updateRtn(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectRtnUserPage(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectRtnUserPage_new(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectRtnUserInfoPop(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON processRtnAttr(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON UserBatchInterface(TelewebJSON mjsonParJson) throws TelewebAppException;

    TelewebJSON UserLogoutInterface(TelewebJSON mjsonParJson) throws TelewebAppException;

    TelewebJSON UserDpncChk(TelewebJSON mjsonParJson) throws TelewebAppException;

    TelewebJSON deleteUser(TelewebJSON mjsonParJson) throws TelewebAppException;

    TelewebJSON updateUserId(TelewebJSON mjsonParJson) throws TelewebAppException;

    TelewebJSON updateProfile(TelewebJSON mjsonParJson) throws TelewebAppException;

    TelewebJSON forceUpdatePassword(TelewebJSON mjsonParJson) throws TelewebAppException;

    /**
     *
     * 사용자관리 - 사용자 등록 및 수정 - 상담사 등록/수정 시 허용 사용자 계정 수 체크
     * 
     * @Method Name :
     * @date : 2023. 11. 03.
     * @author : NJY
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams 허용 계정 갯수 정보
     * @return
     * @throws TelewebAppException
     */
    boolean chkUserAcntCnt(TelewebJSON mjsonParams, TelewebJSON objRetParams) throws TelewebAppException;

    /**
     * 
     * 전체 스키마별 고객사 목록
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
	TelewebJSON schemaCustcoList(TelewebJSON mjsonParams) throws TelewebAppException;

	/**
	 * 
	 * 고객사별 상담원 목록
	 * @param mjsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON custcoCuslList(TelewebJSON mjsonParams) throws TelewebAppException;
}
