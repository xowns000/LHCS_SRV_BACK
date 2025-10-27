package kr.co.hkcloud.palette3.phone.setting.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneSettingIPExtensionNumberManageService
{
    /**
     * ip내선번호관리 목록 조회
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON selectRtnPhoneSettingIPExtensionNumberManage(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * ip내선번호관리 등록
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON insertCheckRtnPhoneSettingIPExtensionNumberManage(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnPhoneSettingIPExtensionNumberManage(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * ip내선번호관리 수정
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON updateRtnPhoneSettingIPExtensionNumberManage(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * ip내선번호관리 삭제
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON deleteRtnPhoneSettingIPExtensionNumberManage(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * ip내선번호관리 호분배
     * 
     * @param  mjsonParams
     * @return
     */
    TelewebJSON UserBatchInterface(TelewebJSON jsonParams) throws TelewebAppException;
    /*
     * ip내선번호관리 내선번호 존재여부
     * 
     * @param mjsonParams
     * 
     * @return
     */
    TelewebJSON insertInlneNoCheckRtnPhoneSettingIPExtensionNumberManage(TelewebJSON jsonParams) throws TelewebAppException;

    /*
     * ip내선번호관리 해당 사용자 id에 따른 내선번호관리 자동채우기
     * 
     * @param mjsonParams
     * 
     * @return
     */
    TelewebJSON insertInlneNoFillRtnPhoneSettingIPExtensionNumberManage(TelewebJSON jsonParams) throws TelewebAppException;
    /*
     * ip내선번호관리 삭제 전 CUSTCO_ID에 2개 이상의 회사명이 들어가있는지 확인
     * 
     * @param mjsonParams
     * 
     * @return
     */
    TelewebJSON selectAspkeyCheckPhoneSettingIPExtensionNumberManage(TelewebJSON jsonParams) throws TelewebAppException;
    /*
     * ip내선번호관리 CUSTCO_ID에 2개 이상의 회사명 업데이트
     * 
     * @param mjsonParams
     * 
     * @return
     */
    TelewebJSON updateAspkeyCheckPhoneSettingIPExtensionNumberManage(TelewebJSON jsonParams) throws TelewebAppException;
}
