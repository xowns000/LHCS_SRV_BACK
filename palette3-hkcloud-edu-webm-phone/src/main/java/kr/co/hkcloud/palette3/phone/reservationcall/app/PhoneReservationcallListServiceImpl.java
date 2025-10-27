package kr.co.hkcloud.palette3.phone.reservationcall.app;


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
@Service("PhoneReservationcallListService")
public class PhoneReservationcallListServiceImpl implements PhoneReservationcallListService
{

    private final TwbComDAO mobjDao;


    /**
     * 예약콜정보 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectRtnResvCallInqInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.reservationcall.dao.PhoneReservationcallListMapper", "selectRtnResvCallInqInfo", mjsonParams);
    }


    /**
     * 예약콜 완료처리
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnResvCallInqInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.phone.reservationcall.dao.PhoneReservationcallListMapper", "updateRtnResvCallInqInfo", mjsonParams);
    }
    
    
    /**
     * 예약콜 삭제
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnResvCallInqInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {

        System.out.println("mjsonParams====>" + mjsonParams);
    	TelewebJSON objRetParams = new TelewebJSON();
    	String custcoId = mjsonParams.getString("ASP_NEWCUST_KEY");
        if(mjsonParams.getDataObject("RESV_DATA") != null) {

            // 캠페인고객대상정보조회
            JSONArray resvData = mjsonParams.getDataObject("RESV_DATA");

            // 캠페인대상 고객 정보가 있는 경우만 
            if(resvData.size() > 0) {
                @SuppressWarnings("unchecked")
                Iterator<JSONObject> resvIter = resvData.iterator();

                while(resvIter.hasNext()) {

                    JSONObject jsonObj = new JSONObject();
                    jsonObj = resvIter.next();

                    System.out.println("jsonObj====>" + jsonObj);
                    TelewebJSON objRetPension = new TelewebJSON();
                    objRetPension.setString("ASP_NEWCUST_KEY", custcoId);           						// 회사구분
                    objRetPension.setString("RESV_SEQ", jsonObj.getString("RESV_SEQ"));             		// 예약콜코드
                    objRetPension.setString("CNSL_HIST_NO", jsonObj.getString("CNSL_HIST_NO"));             // 상담이력번호

                    objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.phone.reservationcall.dao.PhoneReservationcallListMapper", "deleteRtnResvCallInqInfo", objRetPension);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }
    
}
