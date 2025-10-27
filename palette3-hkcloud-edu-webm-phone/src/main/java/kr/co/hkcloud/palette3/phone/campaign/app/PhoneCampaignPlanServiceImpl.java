package kr.co.hkcloud.palette3.phone.campaign.app;


import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service("phoneCampaignPlanService")
public class PhoneCampaignPlanServiceImpl implements PhoneCampaignPlanService
{
    public final TwbComDAO mobjDao;
    public final InnbCreatCmmnService innbCreatCmmnService;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.phone.campaign.dao.PhoneCampaignPlanMapper";

    /**
     * 캠페인 계획을 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCpiPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "selectCpiPlan", jsonParams);
    }

    /**
     * 캠페인 계획을 저장한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Transactional(readOnly = false)
    public TelewebJSON upsertCpiPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        Boolean modFlag = Boolean.parseBoolean(jsonParams.getString("MOD_FLAG"));
        //수정인 경우 현재 상태를 체크한다.
        if(modFlag){
            //상태가 준비중인건인지 체크
            //준비중 아니어도 수정 가능
//            objRetParams = mobjDao.select(sqlNameSpace, "cpiPlanSttsChk", jsonParams);
//            if(Integer.parseInt(objRetParams.getString("CNT")) > 0){
//                objRetParams.setHeader("ERROR_FLAG", true);
//                objRetParams.setHeader("ERROR_MSG", "REG_STTS");
//                return objRetParams;
//            }
        	//진행중 또는 종료 캠페인 수정 시 배분된 대상자중 계획 회차에 도달한 인원이 있는지 체크
            objRetParams = mobjDao.select(sqlNameSpace, "cpiPlanLmtChk", jsonParams);
            if(Integer.parseInt(objRetParams.getString("CNT")) > 0){
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "LMT_CNT_ERR");
                return objRetParams;
            }
            objRetParams = mobjDao.update(sqlNameSpace, "updateCpiPlan", jsonParams);
//            if(!objRetParams.getHeaderBoolean("ERROR_FLAG") && jsonParams.getString("MDFCN_FLAG").equals("true")){
//                //기존 상담사내역 삭제
//                objRetParams = mobjDao.delete(sqlNameSpace, "deleteCuslCpiPlan", jsonParams);
//                if(!objRetParams.getHeaderBoolean("ERROR_FLAG")){
//                    //최신상태로 다시 저장
//                    objRetParams = mobjDao.update(sqlNameSpace, "insertCuslCpiPlan", jsonParams);
//                }
//            }
        }else{
            jsonParams.setString("CPI_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CPI_ID")));
            objRetParams = mobjDao.insert(sqlNameSpace, "insertCpiPlan", jsonParams);
            if(!objRetParams.getHeaderBoolean("ERROR_FLAG")){
                //상담사 내역 저장
                objRetParams = mobjDao.update(sqlNameSpace, "insertCuslCpiPlan", jsonParams);
            }
        }
        return objRetParams;
    }

    /**
     * 캠페인 계획을 삭제한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteCpiPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        JSONArray delArray = jsonParams.getDataObject();

        if(delArray != null && delArray.size() > 0) {
            TelewebJSON telewebJson = null;
            for(int i = 0; i < delArray.size(); i++) {
                JSONObject jsonObject = delArray.getJSONObject(i);
                telewebJson = new TelewebJSON();
                telewebJson.setString("CPI_ID", jsonObject.getString("CPI_ID"));
                telewebJson.setString("CUSTCO_ID", jsonObject.getString("CUSTCO_ID"));

                //항목이 등록되어 있는지 체크
                objRetParams = mobjDao.select(sqlNameSpace, "cpiPlanAltChk", telewebJson);
                if(Integer.parseInt(objRetParams.getString("CNT")) > 0){
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "DEL_ALT");
                    return objRetParams;
                }
                //상태가 준비중인건인지 체크
                objRetParams = mobjDao.select(sqlNameSpace, "cpiPlanSttsChk", telewebJson);
                if(Integer.parseInt(objRetParams.getString("CNT")) > 0){
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "DEL_STTS");
                    return objRetParams;
                }

                objRetParams = mobjDao.update(sqlNameSpace, "deleteCpiPlan", telewebJson);
            }
        }
        return objRetParams;
    }

    /**
     * 캠페인 계획을 종료한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Transactional(readOnly = false)
    public TelewebJSON closeCpiPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //상태가 진행중인건인지 체크
        objRetParams = mobjDao.select(sqlNameSpace, "cpiPlanCloseChk", jsonParams);
        if(Integer.parseInt(objRetParams.getString("CNT")) > 0){
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "CLS_STTS");
            return objRetParams;
        }

        return mobjDao.update(sqlNameSpace, "closeCpiPlan", jsonParams);
    }

    /**
     * 캠페인 계획을 즉시진행한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Transactional(readOnly = false)
    public TelewebJSON cpiPlanStrtNow(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.update(sqlNameSpace, "cpiPlanStrtNow", jsonParams);
        return objRetParams;
    }
}
