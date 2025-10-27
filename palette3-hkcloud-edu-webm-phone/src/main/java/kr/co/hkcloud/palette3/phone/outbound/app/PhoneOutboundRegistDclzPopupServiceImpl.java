package kr.co.hkcloud.palette3.phone.outbound.app;


import java.util.Iterator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("phoneOutboundRegistDclzPopupService")
public class PhoneOutboundRegistDclzPopupServiceImpl implements PhoneOutboundRegistDclzPopupService
{
    private final TwbComDAO mobjDao;


    /**
     * 상담원근태 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectObndDilceList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        // 배분변경 상담원 조회
        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.dashboard.dao.PhoneOutboundRegistDclzPopupMapper", "selectObndDilceList", mjsonParams);

        return objRetParams;

    }


    /**
     * 상담원근태 변경
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON mergeObndDilce(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        if(mjsonParams.getDataObject("OBND_DILCE_ARR") != null) {

            // 상담원 근태 데이터
            JSONArray obndDilceArr = mjsonParams.getDataObject("OBND_DILCE_ARR");

            if(obndDilceArr.size() > 0) {
                @SuppressWarnings("unchecked")
                Iterator<JSONObject> obndDilceIter = obndDilceArr.iterator();

                while(obndDilceIter.hasNext()) {

                    JSONObject jsonObj = new JSONObject();
                    jsonObj = obndDilceIter.next();

                    TelewebJSON objRetDilce = new TelewebJSON();

                    objRetDilce.setString("HALFD_YN", jsonObj.getString("HALFD_YN"));   // 반차
                    objRetDilce.setString("ANNL_YN", jsonObj.getString("ANNL_YN"));   // 연차
                    objRetDilce.setString("AGENT_ID", jsonObj.getString("AGENT_ID"));   // 상담사ID
                    objRetDilce.setString("BASE_DATE", mjsonParams.getString("BASE_DATE"));// 등록일자
                    objRetDilce.setString("REG_MAN", mjsonParams.getString("REG_MAN"));// 등록자
                    objRetDilce.setString("CHNG_MAN", mjsonParams.getString("CHNG_MAN"));// 수정자

                    // 상담원 근태 변경
                    objRetParams = mobjDao
                        .update("kr.co.hkcloud.palette3.phone.dashboard.dao.PhoneOutboundRegistDclzPopupMapper", "mergeObndDilce", objRetDilce);
                }
            }
        }

        return objRetParams;

    }

}
