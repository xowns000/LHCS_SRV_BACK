package kr.co.hkcloud.palette3.phone.outbound.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneOutboundRegistSurveyPopupService")
public class PhoneOutboundRegistSurveyPopupServiceImpl implements PhoneOutboundRegistSurveyPopupService
{

    public final TwbComDAO mobjDao;


    /**
     * 설문통계를 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectQuestSttcList(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao
            .select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundRegistSurveyPopupMapper", "selectQuestSttcList", jsonParam);

        return objParam;
    }


    /**
     * 설문지 목록(팝업)을 조회한다.
     * 
     * @param  jsonParam
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectQuestListPop(TelewebJSON jsonParam) throws TelewebAppException
    {
        TelewebJSON objParam = new TelewebJSON();

        objParam = mobjDao
            .select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyManageMapper", "selectQuestListPop", jsonParam);

        return objParam;
    }
}
