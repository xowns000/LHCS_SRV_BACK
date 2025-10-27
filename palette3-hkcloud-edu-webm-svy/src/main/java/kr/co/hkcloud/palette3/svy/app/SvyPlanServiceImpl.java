package kr.co.hkcloud.palette3.svy.app;


import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;


@Slf4j
@RequiredArgsConstructor
@Service("svyPlanService")
public class SvyPlanServiceImpl implements SvyPlanService
{
    public final TwbComDAO mobjDao;
    public final InnbCreatCmmnService innbCreatCmmnService;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.svy.dao.SvyPlanMapper";
    

    /**
     * 설문조사 계획을 조회한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectListPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select(sqlNameSpace, "selectListPlan", jsonParams);
    }

    /**
     * 설문조사 계획을 저장한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Transactional(readOnly = false)
    public TelewebJSON upsertListPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        Boolean modFlag = Boolean.parseBoolean(jsonParams.getString("MOD_FLAG"));
        //수정인 경우 현재 상태를 체크한다.
        if(modFlag){
            //상태가 준비중인건인지 체크 => 준비중 아니어도 수정 가능
//            objRetParams = mobjDao.select(sqlNameSpace, "listPlanSttsChk", jsonParams);
//            if(Integer.parseInt(objRetParams.getString("CNT")) > 0){
//                objRetParams.setHeader("ERROR_FLAG", true);
//                objRetParams.setHeader("ERROR_MSG", "REG_STTS");
//                return objRetParams;
//            }
            objRetParams = mobjDao.update(sqlNameSpace, "updateListPlan", jsonParams);
            if(!objRetParams.getHeaderBoolean("ERROR_FLAG")){
                //대상지정여부가 n이면 기존 등록되어있는 데이터를 모두 삭제처리한다.
                if(jsonParams.getString("TRGT_DSGN_YN").equals("N")){
                    objRetParams = mobjDao.update("kr.co.hkcloud.palette3.svy.dao.SvyMakeItemsMapper", "deleteExcelMakeItems", jsonParams);
                }
            }
            //설문조사명 = PLT_SRVY_QITEM_GROUP(설문지)테이블의 SRVY_QITEM_GROUP_NM(설문_문항_그룹_명)
            //해당 항목도 업데이트 해줘야함
            objRetParams = mobjDao.update(sqlNameSpace, "updateListSrvyQitemGroupNm", jsonParams);
        }else{
            jsonParams.setString("SRVY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_ID")));
            objRetParams = mobjDao.insert(sqlNameSpace, "insertListPlan", jsonParams);
            //확장 항목이 있을 시 가장 마지막 확장 항목을 복제한다. 
            objRetParams = mobjDao.insert(sqlNameSpace, "insertExpsnAttr", jsonParams);
            
            //설문_상태_이력 저장
            objRetParams = insertSttsHstry(jsonParams);
        }

        //신규이고 에러가 아닌경우 설문지 그룹헤더를 추가한다.
        if(!modFlag && !objRetParams.getHeaderBoolean("ERROR_FLAG")){
            jsonParams.setString("SRVY_QITEM_GROUP_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_QITEM_GROUP_ID")));
            objRetParams = mobjDao.insert(sqlNameSpace, "insertGrpHdMakeItems", jsonParams);
        }
        return objRetParams;
    }

    /**
     * 설문조사 계획을 삭제한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteListPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        JSONArray delArray = jsonParams.getDataObject();

        if(delArray != null && delArray.size() > 0) {
            TelewebJSON telewebJson = null;
            for(int i = 0; i < delArray.size(); i++) {
                JSONObject jsonObject = delArray.getJSONObject(i);
                telewebJson = new TelewebJSON();
                telewebJson.setString("SRVY_ID", jsonObject.getString("SRVY_ID"));
                telewebJson.setString("USER_ID", jsonParams.getString("USER_ID"));

                //항목이 등록되어 있는지 체크
                objRetParams = mobjDao.select(sqlNameSpace, "listPlanGrpChk", telewebJson);
                if(Integer.parseInt(objRetParams.getString("CNT")) > 0){
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "DEL_GRP");
                    return objRetParams;
                }
                //상태가 준비중인건인지 체크
                objRetParams = mobjDao.select(sqlNameSpace, "listPlanSttsChk", telewebJson);
                if(Integer.parseInt(objRetParams.getString("CNT")) > 0){
                    objRetParams.setHeader("ERROR_FLAG", true);
                    objRetParams.setHeader("ERROR_MSG", "DEL_STTS");
                    return objRetParams;
                }

                objRetParams = mobjDao.update(sqlNameSpace, "deleteListPlan", telewebJson);
            }
            //정상 삭제면 헤더도 삭제한다.
            if(!objRetParams.getHeaderBoolean("ERROR_FLAG")){
                objRetParams = mobjDao.update(sqlNameSpace, "deleteGrpHdMakeItems", telewebJson);
            }
        }

        return objRetParams;
    }

    /**
     * 설문조사 계획을 종료한다.
     *
     * @param  jsonParams
     * @return           objParam
     */
    @Transactional(readOnly = false)
    public TelewebJSON closeListPlan(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //상태가 진행중인건인지 체크
        objRetParams = mobjDao.select(sqlNameSpace, "listPlanCloseChk", jsonParams);
        if(Integer.parseInt(objRetParams.getString("CNT")) > 0){
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "CLS_STTS");
            return objRetParams;
        }
        
        objRetParams = mobjDao.update(sqlNameSpace, "closeListPlan", jsonParams);
        objRetParams = insertSttsHstry(jsonParams);
        return objRetParams;
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON insertSttsHstry(TelewebJSON jsonParams) throws TelewebAppException {
        jsonParams.setString("SRVY_STTS_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_STTS_HSTRY_ID")));
        return mobjDao.insert(sqlNameSpace, "insertSttsHstry", jsonParams);
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectSttsHistory(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectSttsHistory", jsonParams);
    }

}
