package kr.co.hkcloud.palette3.qa.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("qaPlanManageService")
public class QAPlanManageServiceImpl implements QAPlanManageService {

    private final TwbComDAO twbComDAO;
    public final InnbCreatCmmnService innbCreatCmmnService;
	
	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaPlanList(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "selectQaPlan", jsonParams);
	}
	/*
	public TelewebJSON selectQaPlanList(TelewebJSON jsonParams) throws TelewebAppException {
		
		String withCyclStts = jsonParams.getString("WITH_CYCL_STTS");
		
		TelewebJSON oJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "selectQaPlan", jsonParams);
		
		if(withCyclStts != null && "Y".equals(withCyclStts)) {
			log.debug("============selectQaPlanList with cycl stts");
			TelewebJSON oCyclSttsParam = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "selectQaCyclStats", jsonParams);
			//oJsonParams.setDataObject("CYCL_STTS", oCyclSttsParam.getDataObject());
			JSONArray arrCyclsStts = oCyclSttsParam.getDataObject();
			
			HashMap<String,JSONArray> cyclSttsMap = new HashMap<String,JSONArray> ();
			
			for(int i = 0 ; i < arrCyclsStts.size() ; i++) {
				JSONObject oJson = arrCyclsStts.getJSONObject(i);
				String qaPlanId = oJson.getString("QA_PLAN_ID");
				if(cyclSttsMap.get(qaPlanId) == null) {
					JSONArray arrTmp = new JSONArray();
					arrTmp.add(oJson);
					cyclSttsMap.put(qaPlanId, arrTmp);
				} else {
					cyclSttsMap.get(qaPlanId).add(oJson);
				}
			}
			
			JSONArray arrData = oJsonParams.getDataObject();
			for(int i = 0 ; i < oJsonParams.getSize() ; i++)	{
				String qaPlanId = arrData.getJSONObject(i).getString("QA_PLAN_ID");
				if(cyclSttsMap.get(qaPlanId) != null)
					arrData.getJSONObject(i).put("CYCL_STATS", cyclSttsMap.get(qaPlanId));
			}
			oJsonParams.setDataObject(arrData);
		}
		
        return oJsonParams;
	}
	*/

	@Transactional(readOnly = false)
	public TelewebJSON insertQaPlan(TelewebJSON jsonParams) throws TelewebAppException {
	//public TelewebJSON insertQaPlan(TelewebJSON jsonParams, boolean isUpdate) throws TelewebAppException {
		
		boolean isUpdate = true;
		
        //String qaId = jsonParams.getString("QA_PLAN_ID");
		String qaId = jsonParams.getString("QA_PLAN_ID");
        if(qaId == null || "".equals(qaId)) {
        	qaId = String.valueOf(innbCreatCmmnService.createSeqNo("QA_PLAN_ID"));
        	jsonParams.setString("QA_PLAN_ID", qaId);
        	isUpdate = false;
        }
        
        TelewebJSON rstInsPlanJson = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "upsertQaPlan", jsonParams);

        if(!rstInsPlanJson.getHeaderBoolean("ERROR_FLAG"))	{ //insert 성공시 처리
        	if(isUpdate)	{
            	List<String> cyclIdList = new ArrayList<String>();	//처리한 cyclid를 저장해 두었다가 나중에 cycl 삭제시 사
        		JSONArray arrParams = jsonParams.getDataObject();
        		for(Object oItem : arrParams) {
        			JSONObject oJson = (JSONObject)oItem;
        			
        			String qaCyclId = oJson.containsKey("QA_CYCL_ID")?oJson.getString("QA_CYCL_ID"):"";
        			Boolean bInsert = false;

    				if(qaCyclId == null || qaCyclId.isEmpty()) {
        				qaCyclId = String.valueOf(innbCreatCmmnService.createSeqNo("QA_CYCL_ID"));
        				bInsert = true;
    				}

    				JSONArray aTmpParam = new JSONArray();
    				JSONObject oTmpParam = new JSONObject();
    				oTmpParam.put("QA_PLAN_ID", qaId);
    				oTmpParam.put("CYCL_NM", oJson.getString("CYCL_NM"));
    				oTmpParam.put("CYCL_BGNG_YMD", oJson.getString("CYCL_BGNG_YMD"));
    				oTmpParam.put("CYCL_END_YMD", oJson.getString("CYCL_END_YMD"));
    				oTmpParam.put("QA_CYCL_ID", qaCyclId);
    				aTmpParam.add(oTmpParam);
    				
    				TelewebJSON insCyclJsonParam = new TelewebJSON();
    				insCyclJsonParam.setDataObject(aTmpParam);

    				if(bInsert)
    					insCyclJsonParam = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "insertQaCycl", insCyclJsonParam);
    				else 
    					insCyclJsonParam = twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "updateQaCycl", insCyclJsonParam);
        			
        			cyclIdList.add(qaCyclId);

        		}
        		
        		//처리가 다 되었으면 삭제할 데이터 삭제 (업데이트인데 기존에 있던 cycl_id 중 파라미터로 넘어오지 않은건 삭제대상임)
            	
            	TelewebJSON delCyclJsonParam = new TelewebJSON();
				JSONArray aTmpParam = new JSONArray();
				JSONObject oTmpParam = new JSONObject();
				
				oTmpParam.put("QA_PLAN_ID", qaId);
				oTmpParam.put("LIST_ID", cyclIdList);
				aTmpParam.add(oTmpParam);
				delCyclJsonParam.setDataObject(aTmpParam);
				
				TelewebJSON rtnJson = twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "deleteQaCyclSttsNotExist", delCyclJsonParam);
				twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "deleteQaCyclNotExist", delCyclJsonParam);
        	} else {
        		JSONArray jsonArray = jsonParams.getDataObject();
        		JSONArray jaQACycl = new JSONArray();

        		for(int i = 0 ; i < jsonArray.size() ; i++) {
        			JSONObject jo = jsonArray.getJSONObject(i);
        			jo.put("QA_PLAN_ID", qaId);
        			jo.put("QA_CYCL_ID", String.valueOf(innbCreatCmmnService.createSeqNo("QA_CYCL_ID")));
        			jaQACycl.add(jo); 
        		}
        		
        		TelewebJSON modJsonParams = new TelewebJSON(jsonParams);
        		JSONObject joData = new JSONObject();
        		JSONArray jaData = new JSONArray();
        		joData.put("QA_CYCL", jaQACycl);
        		jaData.add(joData);
        		
        		modJsonParams.setDataObject(jaData);
        		
        		TelewebJSON rstInsCyclJson = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "insertQaCycls", modJsonParams);
            	if(rstInsCyclJson.getHeaderBoolean("ERROR_FLAG"))	{
            		return rstInsCyclJson;
            	}
        	}
        	
        }
    	return rstInsPlanJson;
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON deleteQaPlan(TelewebJSON jsonParams) throws TelewebAppException {


		//평가관리지 지정여부 및 종료여부(진행상태) 확인.
		TelewebJSON chkJsonParam = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "selectQaPlanStatus", jsonParams);
		JSONObject oCheckData = chkJsonParam.getDataObject().getJSONObject(0);
		Integer mngrCnt = Integer.valueOf(oCheckData.getString("MNGR_CNT"));
		String prgrsSttsCd = oCheckData.getString("PRGRS_STTS_CD");
		
		if("ONGONG".equals(prgrsSttsCd) || "TERMIAT".equals(prgrsSttsCd)) {
			chkJsonParam.setHeader("ERROR_FLAG", true);
			chkJsonParam.setHeader("ERROR_MSG", "PRGRS_STTS_CD:" + prgrsSttsCd);
			return chkJsonParam;
		}

		if(mngrCnt > 0) {
			chkJsonParam.setHeader("ERROR_FLAG", true);
			chkJsonParam.setHeader("ERROR_MSG", "MNGR_ASSIGNED");
			return chkJsonParam;
		}
		
		
		/* 플래그만 변경하는 것으로 하여 체크 필요 없음.
    	TelewebJSON rstDelQAPlanJsonParams = new TelewebJSON(jsonParams);
    	//차수를 먼저 삭제
    	rstDelQAPlanJsonParams = twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "deleteQaCycls", jsonParams);
    	if(rstDelQAPlanJsonParams.getHeaderBoolean("ERROR_FLAG"))	{
    		return rstDelQAPlanJsonParams;
    	}
    	
    	//오류가 없으면 계획 삭제
    	rstDelQAPlanJsonParams = twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "deleteQaPlan", jsonParams);
		*/
		
    	TelewebJSON rstDelQAPlanJsonParams = new TelewebJSON(jsonParams);
    	
    	rstDelQAPlanJsonParams = twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "updateQaPlanDel", jsonParams);
		
		return rstDelQAPlanJsonParams;
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON updateQaPlanStts(TelewebJSON jsonParams) throws TelewebAppException {
		return twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "updateQaPlanStts", jsonParams);
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON updateQaPlanSttsBatch(TelewebJSON jsonParams) throws TelewebAppException {
		TelewebJSON upJsonParams = new TelewebJSON(jsonParams);
		upJsonParams = twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "updateQaPlanSttsAllToOngong", jsonParams);
		//upJsonParams = twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "updateQaPlanSttsAllToTerminat", jsonParams);
		return upJsonParams;
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON qaPlanStrtNow(TelewebJSON jsonParams) throws TelewebAppException {
		TelewebJSON resultParams = new TelewebJSON(jsonParams);
		resultParams = twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "qaPlanStrtNow", jsonParams);
		return resultParams;
	}
}
