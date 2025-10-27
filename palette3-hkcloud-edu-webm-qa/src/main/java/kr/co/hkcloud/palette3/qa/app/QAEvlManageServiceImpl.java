package kr.co.hkcloud.palette3.qa.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections4.iterators.ArrayIterator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("qaEvlManageService")
public class QAEvlManageServiceImpl implements QAEvlManageService {

    private final TwbComDAO twbComDAO;
    public final InnbCreatCmmnService innbCreatCmmnService;
    
	@Transactional
    private TelewebJSON getQaPlanStts(TelewebJSON jsonParams) {
		TelewebJSON selJson = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "selectQaPlanStatus", jsonParams);
		
		String prgrsSttsCd = selJson.getString("PRGRS_STTS_CD");
		String rsltCnt = selJson.getString("RSLT_CNT");
		
		if("TERMIAT".equals(prgrsSttsCd)) {
			selJson.setHeader("ERROR_FLAG", true);
			selJson.setHeader("ERROR_MSG", "IS_TERMIAT");
			return selJson;
		}
		
		if(rsltCnt != null && Integer.valueOf(rsltCnt) > 0) {
			selJson.setHeader("ERROR_FLAG", true);
			selJson.setHeader("ERROR_MSG", "EVL_CNT_EXIST");
			return selJson;
		}
		return selJson;
    }
    
	@Override
	@Transactional(readOnly = false)
	public TelewebJSON insertQaPlanQlty(TelewebJSON jsonParams) throws TelewebDaoException {
		
		//처리하기전에 상태 체크. 1.평가 종료 여부 2.평가한 사용자가 있는지 여부
		TelewebJSON selJson = getQaPlanStts(jsonParams);
		if(selJson.getHeaderBoolean("ERROR_FLAG"))
			return selJson;
		
		//한건씩 처리
		//데이터가 있으면 update, 없으면 insert
		//나머지 데이터들은 delete flag
		
		TelewebJSON insTelewebJSON = new TelewebJSON(jsonParams);
		JSONArray arrEvl = jsonParams.getDataObject();
		
		List<String> execList = new ArrayList<String>();
		
		//Iterator<JSONObject> itrEvl = arrEvl.iterator();
		
		JSONObject oFirst = arrEvl.getJSONObject(0);
		
		String userId = oFirst.getString("USER_ID");
		String custcoId = oFirst.getString("CUSTCO_ID");
		String planId = oFirst.getString("QA_PLAN_ID");
		log.debug("userId:%s, custcoId:%s, planId:%s", userId, custcoId, planId);
		
		// 현재 데이터 Fetch 
		jsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "selectQaPlanQtyArtclId", jsonParams);
		JSONArray arrExist = jsonParams.getDataObject();
		//log.debug("insTelewebJSON:" + insTelewebJSON.toString());
		
		if(userId == null || custcoId == null) {
			jsonParams.setHeader("ERROR_FLAG", true);
			jsonParams.setHeader("ERROR_MSG", "DATA ERROR (USER_ID, CUSTCO_ID)");
			return jsonParams;
		}
		

		for(int i = 0 ; i < arrEvl.size(); i++) {
			JSONObject item = arrEvl.getJSONObject(i);
			if(!item.has("USER_ID"))
				item.put("USER_ID",  userId);
			
			if(!item.has("CUSTCO_ID"))
				item.put("CUSTCO_ID", custcoId);
			
			
			String evlArtclId = String.valueOf(item.get("EVL_ARTCL_ID"));

			JSONArray oParam = new JSONArray();
			oParam.add(item);

			insTelewebJSON.setDataObject(oParam);
			
			JSONObject tmp = new JSONObject();
			tmp.put("EVL_ARTCL_ID", evlArtclId);
			Boolean bExist = arrExist.contains(tmp);
			
			if(bExist) {	//있는 데이터면 update
				log.debug("exist - evlArtclId %s", evlArtclId);
				insTelewebJSON = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "updateQaPlanQlty", insTelewebJSON);
			} else {		//없는 데이터면 insert
				log.debug("not exist - evlArtclId %s", evlArtclId);
				insTelewebJSON = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "insertQaPlanQlty", insTelewebJSON);
			}
			
			arrExist.remove(tmp);	// 처리한 데이터는 목록에서 삭제 하고 나중에 arrExist에 있는 데이터는 삭제 처
			execList.add(String.valueOf(item.get("EVL_ARTCL_ID")));	
		}
		
		
		log.debug("execList:" + String.join(",", execList));
		log.debug("arrExist:" + arrExist.toString());
		
		//나머지는 삭제 처리 
		if(arrExist.size() > 0) {
			JSONObject oIdList = new JSONObject();
			oIdList.put("LIST_ID", arrExist);
			oIdList.put("QA_PLAN_ID", planId);
			
			
			JSONArray oParam = new JSONArray();
			oParam.add(oIdList);
			insTelewebJSON.setDataObject(oParam);
			insTelewebJSON = twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "updateQaPlanQltyDel", insTelewebJSON);
		}
		
		return insTelewebJSON;
	}

	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaTrgtSlctnList(TelewebJSON jsonParams) throws TelewebDaoException {
		return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "selectQaTrgtSlctnList", jsonParams);
	}
	
	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaTrgtAlmntList(TelewebJSON jsonParams) throws TelewebDaoException {
		TelewebJSON oJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "selectQaTrgtAlmntInfo", jsonParams);
		
		if(!oJsonParams.getHeaderBoolean("ERROR_FLAG")) {
			JSONObject oScltnInfo = oJsonParams.getDataObject().getJSONObject(0);
			oJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAMngrManageMapper", "selectMngrList", jsonParams);
			oJsonParams.setDataObject("SLCTN_INFO", oScltnInfo);
			//oJsonParams.setDataObject("SLCTN_LIST", oJsonParams.getDataObject());
		}
		return oJsonParams;
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON insertQaTrgtSlctn(TelewebJSON jsonParams) throws TelewebDaoException {
		TelewebJSON insJsonParams = new TelewebJSON(jsonParams);
		JSONArray oArrParams = jsonParams.getDataObject();
		JSONObject oParamsIn = new JSONObject();
		JSONObject oParamsUp = new JSONObject();
		JSONArray oArrInTmp = new JSONArray();
		JSONArray oArrUpTmp = new JSONArray();
		
		//공통 파라미터 저장 (QA_PLAN_ID, CUSTCO_ID, USER_ID)
		String qaPlanId = oArrParams.getJSONObject(0).getString("QA_PLAN_ID");
		String custCoId = oArrParams.getJSONObject(0).getString("CUSTCO_ID");
		String userId = oArrParams.getJSONObject(0).getString("USER_ID");
		
		for(int i = 0 ; i < oArrParams.size(); i++)	{
			JSONObject oParamTmp = new JSONObject();
			String qaTrgtId = null;
			if(oArrParams.getJSONObject(i).has("QA_TRGT_ID")) {
				qaTrgtId = oArrParams.getJSONObject(i).getString("QA_TRGT_ID");
			}
			if(qaTrgtId == null || qaTrgtId.isEmpty()) {
				oParamTmp.put("ITGRT_HSTRY_ID", oArrParams.getJSONObject(i).getString("ITGRT_HSTRY_ID"));
				oParamTmp.put("QA_TRGT_ID", String.valueOf(innbCreatCmmnService.createSeqNo("QA_TRGT_ID")));
				oArrInTmp.add(oParamTmp);
			} else {
				oParamTmp.put("QA_PLAN_ID", qaPlanId);
				oParamTmp.put("CUSTCO_ID", custCoId);
				oParamTmp.put("USER_ID", userId);
				oParamTmp.put("ITGRT_HSTRY_ID", oArrParams.getJSONObject(i).getString("ITGRT_HSTRY_ID"));
				oParamTmp.put("QA_TRGT_ID", qaTrgtId);
				oArrUpTmp.add(oParamTmp);
			}
		}
		
		for(int i = 0 ; i < oArrUpTmp.size() ; i++) {
			//업데이트는 한건씩.
			JSONArray oArrData = new JSONArray();
			oArrData.add(oArrUpTmp.getJSONObject(i));
			insJsonParams.setDataObject(oArrData);
			insJsonParams = twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "updateQaTrgtSlctn", insJsonParams);
		}

		if(oArrInTmp.size() > 0) {
			oParamsIn.put("QA_PLAN_ID", qaPlanId);
			oParamsIn.put("CUSTCO_ID", custCoId);
			oParamsIn.put("USER_ID", userId);
			oParamsIn.put("LIST_DATA", oArrInTmp);
			JSONArray oArrData = new JSONArray();
			oArrData.add(oParamsIn);
			insJsonParams.setDataObject(oArrData);
			insJsonParams = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "insertQaTrgtSlctn", insJsonParams);
		}
		
		return insJsonParams;
	}

//	@Override
//	@Transactional(readOnly = false)
//	public TelewebJSON updateQaTrgtSlctn(TelewebJSON jsonParams) throws TelewebDaoException {
//		return null;
//	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON deleteQaTrgtSlctn(TelewebJSON jsonParams) throws TelewebDaoException {
		
		TelewebJSON delJsonParam = new TelewebJSON(jsonParams);
		
		JSONArray oArrParams = jsonParams.getDataObject();
		JSONObject oParams = new JSONObject();
		JSONArray oArrTmp = new JSONArray();
		
		//String qaPlanId = oArrParams.getJSONObject(0).getString("QA_PLAN_ID");
	
		for(int i = 0 ; i < oArrParams.size(); i++)	{
			JSONObject oParamTmp = new JSONObject();
			oParamTmp.put("QA_TRGT_ID", oArrParams.getJSONObject(i).getString("QA_TRGT_ID"));
			oArrTmp.add(oParamTmp);
		}

		oParams.put("LIST_ID", oArrTmp);
		oParams.put("QA_PLAN_ID", jsonParams.getString("QA_PLAN_ID"));
		
		JSONArray oArrData = new JSONArray();
		oArrData.add(oParams);
		delJsonParam.setDataObject(oArrData);
		
		return twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "deleteQaTrgtSlctn", delJsonParam);
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON execQaTrgtAlmnt(TelewebJSON jsonParams) throws TelewebDaoException {
		

		
		String assignType = jsonParams.getString("TYPE");
	    switch(assignType) {
	    	case "EQUAL":
	    		//처리하기전에 상태 체크. 1.평가 종료 여부 2.평가한 사용자가 있는지 여부
	    		TelewebJSON selJson0 = getQaPlanStts(jsonParams);
	    		if(selJson0.getHeaderBoolean("ERROR_FLAG"))
	    			return selJson0;
	        	log.debug("assignType.switch1 {}", assignType);
	        	jsonParams = execEqual(jsonParams);
	        	break;
	        case "ADD":
	        	log.debug("assignType.switch2 {}", assignType);
	        	jsonParams = execAdd(jsonParams);
	        	break;
	        case "WITHDRAW":
	    		//처리하기전에 상태 체크. 1.평가 종료 여부 2.평가한 사용자가 있는지 여부
	    		//TelewebJSON selJson1 = getQaPlanStts(jsonParams);
	    		//if(selJson1.getHeaderBoolean("ERROR_FLAG"))
	    		//	return selJson1;
	        	log.debug("assignType.switch3 {}", assignType);
	        	jsonParams = execWithdraw(jsonParams);
	        	break;
	        case "WITHDRAWALL":
	    		//처리하기전에 상태 체크. 1.평가 종료 여부 2.평가한 사용자가 있는지 여부
	    		TelewebJSON selJson2 = getQaPlanStts(jsonParams);
	    		if(selJson2.getHeaderBoolean("ERROR_FLAG"))
	    			return selJson2;
	        	log.debug("assignType.switch4 {}", assignType);
	        	jsonParams = execWithdrawAll(jsonParams);
	        	break;
	        default :
	        	log.debug("assignType.switch5 {}", assignType);
	      };
		return jsonParams;
	}
	
	private TelewebJSON execEqual(TelewebJSON jsonParams) {
		
		JSONArray oArrParams = jsonParams.getDataObject();
		String qaPlanId = oArrParams.getJSONObject(0).getString("QA_PLAN_ID");
		String custcoId = oArrParams.getJSONObject(0).getString("CUSTCO_ID");
		String userId = oArrParams.getJSONObject(0).getString("USER_ID");
		
		TelewebJSON execJsonParams = new TelewebJSON(jsonParams);
		//현재 할당된 목록
		execJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "getQaTargetInfo", jsonParams);
		
		JSONArray rstTargetInfo = execJsonParams.getDataObject();
		
		int iTargetCnt = 0;
		int iMgrCnt = 0;
		
		Map<String, Integer> mManagerInfo = new HashMap<String, Integer> ();
		
		for(int i = 0; i < rstTargetInfo.size(); i++) {
			log.debug("------ {}", rstTargetInfo.getJSONObject(i).toString());
			String qaMngrId = rstTargetInfo.getJSONObject(i).getString("QA_MNGR_ID");
			int iCnt = Integer.valueOf(rstTargetInfo.getJSONObject(i).getString("CNT"));
			if(qaMngrId != null && "-1".equals(qaMngrId))
				iTargetCnt = Integer.valueOf(rstTargetInfo.getJSONObject(i).getString("CNT"));
			else {
				iMgrCnt++;
				mManagerInfo.put(qaMngrId, iCnt);
			}
		}
		
		int iAvg = iTargetCnt / iMgrCnt;
		int iRest = iTargetCnt % iMgrCnt;
		
		log.debug("iAvg {}, iMod {}", iAvg, iRest);
		
		int iSum = 0;
		
		//회수 부터 진행 한 후 할당 진행 (중간에 할당할 건수가 부족 할 수 있음)
		List<JSONObject> oAddList = new ArrayList<JSONObject>();
		List<JSONObject> oDelList = new ArrayList<JSONObject>();

		for (Map.Entry<String, Integer> entry : mManagerInfo.entrySet()) {
			//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			String qaMgrId = entry.getKey();
			Integer iCurrCnt = entry.getValue();
			int iCorrect = 0;
			iCorrect = iAvg - iCurrCnt;
			if(iRest > 0) {
				iCorrect += 1;
				iRest -= 1;
			}
			
			int iCnt = iCurrCnt + iCorrect;
			
			iSum += iCnt;

			log.debug("QA_MGR_ID {}, iCurrCnt {}, iCorrect {}, iCnt {}, iRest {}", qaMgrId, iCurrCnt, iCorrect, iCnt, iRest);
		
			JSONObject oJsonData = new JSONObject();
			oJsonData.put("MNGR_ID", qaMgrId);
			oJsonData.put("QA_PLAN_ID", qaPlanId);
			oJsonData.put("CUSTCO_ID", custcoId);
			oJsonData.put("USER_ID", userId);
			
			
			if(iCorrect > 0) {
				oJsonData.put("addInput", iCorrect);
				oAddList.add(oJsonData);
				//jsonParams.setDataObject(aJsonData);
				//execJsonParams = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "insertQaTrgtAltmntWithCnt", jsonParams);
			} else if(iCorrect < 0) {
				oJsonData.put("addInput", -iCorrect);
				oDelList.add(oJsonData);
//				jsonParams.setDataObject(aJsonData);
//				execJsonParams = twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "deleteQaTrgtAltmntWithCnt", jsonParams);
			}
		
		}
		
		log.debug("iSum {} {} {} ", iSum, oDelList.size(), oAddList.size()); 
		
		ListIterator<JSONObject> delItr = oDelList.listIterator();
		
		while(delItr.hasNext()) {
			JSONArray array = new JSONArray();
			array.add(delItr.next());
			execJsonParams.setDataObject(array);
			execJsonParams = twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "deleteQaTrgtAltmntWithCnt", execJsonParams);
		}
		
		ListIterator<JSONObject> addItr = oAddList.listIterator();
		
		while(addItr.hasNext()) {
			JSONArray array = new JSONArray();
			array.add(addItr.next());
			execJsonParams.setDataObject(array);
			execJsonParams = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "insertQaTrgtAltmntWithCnt", execJsonParams);
		}
		
		return execJsonParams;
	}

	private TelewebJSON execAdd(TelewebJSON jsonParams) {
		
		//새로 할당해야 할 건수 계산
		JSONArray oArrParams = jsonParams.getDataObject();
		List<JSONArray> oListIns = new ArrayList<JSONArray>();
		
		String qaPlanId = oArrParams.getJSONObject(0).getString("QA_PLAN_ID");
		String userId = oArrParams.getJSONObject(0).getString("USER_ID");
		
		int addCnt = 0;
		for(int i = 0 ; i < oArrParams.size() ; i++)	{
			if(oArrParams.getJSONObject(i).has("addInput") && !oArrParams.getJSONObject(i).getString("addInput").isEmpty()) {
				addCnt += Integer.valueOf(oArrParams.getJSONObject(i).getString("addInput"));
				JSONObject oTmp = oArrParams.getJSONObject(i);
				oTmp.put("QA_PLAN_ID", qaPlanId);
				oTmp.put("USER_ID", userId);
				JSONArray aTmp = new JSONArray();
				aTmp.add(oTmp);
				oListIns.add(aTmp);
			}
		}
		
		//현재 할당 현황을 가져옴
		TelewebJSON oJsonParams = new TelewebJSON(jsonParams);
		oJsonParams.setString("QA_PLAN_ID",  qaPlanId);
		oJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "selectQaTrgtAlmntInfo", oJsonParams);
		
		int freeCnt = oJsonParams.getInt("FREE_CNT");
		
		log.debug("AlmntInfo free:{}, add_to:{}", freeCnt, addCnt);

		
		if (addCnt < 1) {
			oJsonParams.setHeader("ERROR_FLAG", true);
			oJsonParams.setHeader("ERROR_MSG", "CHK_ADDINPUT");
		} else if(freeCnt >= addCnt ) { //미할당 건수가 신규 할당 건수 보다 크거나 같으면 진행
			for(int i = 0 ; i < oListIns.size() ; i++) {
				oJsonParams.setDataObject(oListIns.get(i));
				oJsonParams = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "insertQaTrgtAltmntWithCnt", oJsonParams);
			}

		} else { //미할당 건수가 신규 할당 건수 보다 작으면 오류 리턴
			oJsonParams.setHeader("ERROR_FLAG", true);
			oJsonParams.setHeader("ERROR_MSG", "OVER_CNT_ADD");
//			return oJsonParams;
		}	
		
		
		//추가할 총 건수가 미할당 건수보다 작거나 같은지 체크.
		//twbComDAO.select(null, null, jsonParams)
		
		return oJsonParams;
	}
	
	private TelewebJSON execWithdraw(TelewebJSON jsonParams) {
		//새로 할당해야 할 건수 계산
		JSONArray oArrParams = jsonParams.getDataObject();
		List<JSONArray> oListIns = new ArrayList<JSONArray>();
		
		String qaPlanId = oArrParams.getJSONObject(0).getString("QA_PLAN_ID");
		String userId = oArrParams.getJSONObject(0).getString("USER_ID");
		
		for(int i = 0 ; i < oArrParams.size() ; i++)	{
			if(oArrParams.getJSONObject(i).has("addInput") && !oArrParams.getJSONObject(i).getString("addInput").isEmpty()) {
				JSONObject oTmp = oArrParams.getJSONObject(i);
				oTmp.put("QA_PLAN_ID", qaPlanId);
				oTmp.put("USER_ID", userId);
				JSONArray aTmp = new JSONArray();
				aTmp.add(oTmp);
				oListIns.add(aTmp);
			}
		}
		
		TelewebJSON oJsonParams = new TelewebJSON(jsonParams);

		for(int i = 0 ; i < oListIns.size() ; i++) {
			oJsonParams.setDataObject(oListIns.get(i));
			oJsonParams = twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "deleteQaTrgtAltmntWithCnt", oJsonParams);
		}
		
		return oJsonParams;
	}

	private TelewebJSON execWithdrawAll(TelewebJSON jsonParams) {
		return twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "deleteQaTrgtAltmntAll", jsonParams);
	}
}
