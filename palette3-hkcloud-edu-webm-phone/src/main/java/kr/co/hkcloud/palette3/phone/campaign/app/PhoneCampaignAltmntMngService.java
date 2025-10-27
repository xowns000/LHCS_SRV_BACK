package kr.co.hkcloud.palette3.phone.campaign.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCampaignAltmntMngService
{
    TelewebJSON selectTopCpiAltmntMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectCuslAltmntMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON insertCuslAltmntMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON updateExlAltmntMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON returnCuslAltmntMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectCustAltmntMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON tkoverCuslAltmntMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON chgCuslAltmntMng(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON expsnAttrList(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON addIndiCustAltmntMng(TelewebJSON jsonParam) throws TelewebAppException;
    
    /**
     * 
     * 캠페인 이력 목록
     * @Method Name  	: cpiStatHistList
     * @date   			: 2023. 7. 18.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cpiStatHistList(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 상담원별 캠페인 상태
     * @Method Name  	: cuslCampaignMonitor
     * @date   			: 2023. 7. 18.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuslCampaignMonitor(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 상담원별 처리결과별 상태
     * @Method Name  	: cuslCpiDtlMonitor
     * @date   			: 2023. 7. 18.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuslCpiDtlMonitor(TelewebJSON jsonParam) throws TelewebAppException;
}
