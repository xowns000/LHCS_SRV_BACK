package kr.co.hkcloud.palette3.phone.transfer.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface PhoneTransferManageService {

    /**
     * 이관 배분 현황 count 조회
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectTransferDstbCount(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 상담사 별 이관 배분 현황 COUNT 조회
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectTransferAgentDstbCount(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 이관 상담사들에게 배분
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertTransferManageDstb(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 이관 상담사 회수
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON deleteTransferManageDstb(TelewebJSON jsonParams) throws TelewebAppException;

}
