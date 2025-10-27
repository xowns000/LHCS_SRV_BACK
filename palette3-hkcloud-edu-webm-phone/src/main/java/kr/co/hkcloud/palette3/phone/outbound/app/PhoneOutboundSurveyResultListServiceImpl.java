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
@Service("phoneOutboundSurveyResultListService")
public class PhoneOutboundSurveyResultListServiceImpl implements PhoneOutboundSurveyResultListService
{
    private final TwbComDAO mobjDao;


    /**
     * 설문지 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnServayList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyResultListMapper", "selectRtnServayList", jsonParams);

        return objRetParams;
    }


    /**
     * 설문지 문항 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnServayQList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyResultListMapper", "selectRtnServayQList", jsonParams);

        return objRetParams;
    }


    /**
     * 설문지결과 조회
     * 
     * @param  inHashMap
     * @return           objRetParams
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnResult(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON retTotCntJson = new TelewebJSON();
        int totCnt = 0;
        String ansType = "";

        retTotCntJson = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyResultListMapper", "selectRtnResultQAnsTotCnt", jsonParams);
        totCnt = retTotCntJson.getInt("TOT_CNT");

        ansType = jsonParams.getString("ANS_TYPE");
        if(ansType.equals("01")) {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyResultListMapper", "selectRtnResult_multi", jsonParams);
        }
        else if(ansType.equals("02")) {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyResultListMapper", "selectRtnResult_short", jsonParams);
        }

        objRetParams.setInt("TOT_CNT", totCnt);

        return objRetParams;
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnResultQAnsTotCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyResultListMapper", "selectRtnResultQAnsTotCnt", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnResult_multi(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyResultListMapper", "selectRtnResult_multi", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnResult_short(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.phone.outbound.dao.PhoneOutboundSurveyResultListMapper", "selectRtnResult_short", jsonParams);
    }

}
