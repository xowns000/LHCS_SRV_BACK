package kr.co.hkcloud.palette3.qa.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
@Service("qaEvlEnforceService")
public class QAEvlEnforceServiceImpl implements QAEvlEnforceService {

    private final TwbComDAO twbComDAO;
    public final InnbCreatCmmnService innbCreatCmmnService;

    @Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaEnforceList(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON jsonCyclStts = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "selectQaCyclStats", jsonParams);
    	TelewebJSON jsonEnforceList = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "selectQaEnforceList", jsonParams);
    	
    	JSONArray arrEnforceList = jsonEnforceList.getDataObject();
		
		HashMap<String,JSONArray> mapEnforceList = new HashMap<String,JSONArray> ();
		
		for(int i = 0 ; i < arrEnforceList.size() ; i++) {
			JSONObject oJson = arrEnforceList.getJSONObject(i);
			String cyclNm = oJson.getString("CYCL_NM");
			if(mapEnforceList.get(cyclNm) == null) {
				JSONArray arrTmp = new JSONArray();
				oJson.put("ROW_NUMBER", "1");
				arrTmp.add(oJson);
				mapEnforceList.put(cyclNm, arrTmp);
			} else {
				oJson.put("ROW_NUMBER", mapEnforceList.get(cyclNm).getJSONObject(mapEnforceList.get(cyclNm).size() - 1).getInt("ROW_NUMBER") + 1);
				mapEnforceList.get(cyclNm).add(oJson);
			}
		}
		
		JSONArray arrData = jsonCyclStts.getDataObject();
		for(int i = 0 ; i < jsonCyclStts.getSize() ; i++)	{
			String cyclNm = arrData.getJSONObject(i).getString("CYCL_NM");
			if(mapEnforceList.get(cyclNm) != null)
				arrData.getJSONObject(i).put("CYCL_STATS", mapEnforceList.get(cyclNm));
		}
		
		jsonEnforceList.setDataObject(arrData);
		
		return jsonEnforceList;
	}

	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaEvlSheet(TelewebJSON jsonParams) throws TelewebDaoException {
		TelewebJSON jsonEvlSheet = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "selectQaEvlSheet", jsonParams);
		TelewebJSON jsonQaRsltOpnn = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "selectQaRsltOpnn", jsonParams);
		
		TelewebJSON jsonRtn = new TelewebJSON(jsonEvlSheet);
		jsonRtn.setDataObject("EVL_SHEET", jsonEvlSheet.getDataObject());
		jsonRtn.setDataObject("RSLT_OPNN", jsonQaRsltOpnn.getDataObject());
		
		return jsonRtn;
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON setEvlRslt(TelewebJSON jsonParams) throws TelewebDaoException {
		// 현재 저장 되어 있는 LIST를 가져온다.
		JSONArray aTmpParam = new JSONArray();
		JSONObject oTmpParam = new JSONObject();
		
		JSONArray aParams = jsonParams.getDataObject();
		
		String qaPlanId = jsonParams.getString("QA_PLAN_ID");
		String qaCyclId = jsonParams.getString("QA_CYCL_ID");
		String qaMngrId = jsonParams.getString("QA_MNGR_ID");
		String qaTrgtId = jsonParams.getString("QA_TRGT_ID");
		String mngrOpnn = jsonParams.getString("MNGR_OPNN");
		String objcCn   = jsonParams.getString("OBJC_CN");
		
		
		oTmpParam.put("QA_PLAN_ID", qaPlanId);
		oTmpParam.put("QA_CYCL_ID", qaCyclId);
		oTmpParam.put("QA_MNGR_ID", qaMngrId);
		oTmpParam.put("QA_TRGT_ID", qaTrgtId);
		oTmpParam.put("MNGR_OPNN" , mngrOpnn);
		oTmpParam.put("OBJC_CN"   , objcCn);
		
		
		aTmpParam.add(oTmpParam);
		
		// opnn 테이블 (의견테이블) 먼저 처리 
		TelewebJSON jsonRsltList = new TelewebJSON(jsonParams);
		jsonRsltList.setDataObject(aTmpParam);
		jsonRsltList = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "selectQaRsltOpnn", jsonRsltList);     
		
		TelewebJSON jsonInsertRslt = new TelewebJSON(jsonParams);
		Integer rsltCount = jsonRsltList.getHeaderInt("COUNT");
		if(rsltCount != null && rsltCount == 0) {
			//insert
			aTmpParam.clear(); aTmpParam.add(oTmpParam);
			jsonInsertRslt.setDataObject(aTmpParam);
			jsonInsertRslt = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "insertQaRsltOpnn", jsonInsertRslt);
		} else {
			//update
			aTmpParam.clear(); aTmpParam.add(oTmpParam);
			jsonInsertRslt.setDataObject(aTmpParam);
			jsonInsertRslt = twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "updateQaRsltOpnn", jsonInsertRslt);
		}
		
		//inser, update, delete 구분을 위해 기존 list를 가져온다.
		//TelewebJSON jsonRsltList = new TelewebJSON(jsonParams);
		jsonRsltList.setDataObject(aTmpParam);
		jsonRsltList = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "selectQaRsltIdList", jsonRsltList);     
		

		//TelewebJSON jsonInsertRslt = new TelewebJSON(jsonParams);
		rsltCount = jsonRsltList.getHeaderInt("COUNT");
		if(rsltCount != null && rsltCount == 0)	{
			//데이터가 없으면 한방에 insert
			//insert를 위한 파라미터 구성
			//oTmpParam.clear();
			aTmpParam.clear();
			JSONArray aTmpParam2 = new JSONArray();
			Iterator<JSONObject> iterator = aParams.iterator();
			while(iterator.hasNext()) {
				JSONObject itrJson = iterator.next();
				JSONObject oTmpParam2 = new JSONObject();
				oTmpParam2.put("EVL_ARTCL_ID", itrJson.getString("EVL_ARTCL_ID"));
				oTmpParam2.put("SCR", itrJson.getString("SCR"));
				aTmpParam2.add(oTmpParam2);
			}
			oTmpParam.put("LIST_DATA", aTmpParam2);
			aTmpParam.add(oTmpParam);
			jsonInsertRslt.setDataObject(aTmpParam);
			
			jsonInsertRslt = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "insertQaRsltMulti", jsonInsertRslt);
		} else {
			//데이터가 있으면 insert 건과 update건 분리 처리, 기존 데이터가 넘어온 데이터에 없으면 다른 항목을 선택한 것이므로 기존 데이터는 삭제 처리.
			JSONArray aRsltList = jsonRsltList.getDataObject();
			
			//존재 여부 확인을 위해 EVL_ARTCL_ID List를 만들어 준다.
			List<String> rsltList = new ArrayList<String> ();
			for(int i = 0; i < aRsltList.size(); i++) {
				log.debug("------ {}", aRsltList.getJSONObject(i).toString());
				rsltList.add(aRsltList.getJSONObject(i).getString("EVL_ARTCL_ID"));
			}
			
			
			for(int i = 0 ; i < aParams.size(); i++)	{
				String evlArtclId = aParams.getJSONObject(i).getString("EVL_ARTCL_ID");
				String scr = aParams.getJSONObject(i).getString("SCR");
				oTmpParam.put("EVL_ARTCL_ID", evlArtclId);
				oTmpParam.put("SCR", scr);
				aTmpParam.clear(); aTmpParam.add(oTmpParam);
				jsonInsertRslt.setDataObject(aTmpParam);
				//해당 EVL_ARTCL_ID가 rsltList 에 있으면 업데이트 목록에, 없으면 인서트 목록에 추가
				if(rsltList.contains(evlArtclId)) {
					jsonInsertRslt = twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "updateQaRslt", jsonInsertRslt);
					rsltList.remove(evlArtclId);
				}
				else {
					jsonInsertRslt = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "insertQaRslt", jsonInsertRslt);
					rsltList.remove(evlArtclId);
				}
			}

			if(rsltList != null && rsltList.size() > 0) {
				oTmpParam.put("LIST_ID",  rsltList);
				aTmpParam.clear(); aTmpParam.add(oTmpParam);
				jsonInsertRslt.setDataObject(aTmpParam);
				jsonInsertRslt = twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "deleteQaRslt", jsonInsertRslt);
			}
		}
		
//		rsltCount = jsonInsertRslt.getHeaderInt("COUNT");
//		if(rsltCount != null && rsltCount > 0) {
//			//데이터 처리가 되었으면 평가 상태를 업데이트 해준다. (준비중->진행중)
//			oTmpParam.clear(); aTmpParam.clear();
//			List<String> idList = new ArrayList<String>();
//			idList.add("TERMIAT"); idList.add("ONGONG");
//			oTmpParam.put("QA_PLAN_ID", qaPlanId);
//			oTmpParam.put("PRGRS_STTS_CD", "ONGONG");
//			oTmpParam.put("NOT_INCLUDE", "Y");
//			oTmpParam.put("LIST_ID", idList);
//			aTmpParam.add(oTmpParam);
//			jsonInsertRslt.setDataObject(aTmpParam);
//			jsonInsertRslt = twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAPlanManageMapper", "updateQaPlanStts", jsonInsertRslt);
//			rsltCount = jsonInsertRslt.getHeaderInt("COUNT");
//			//헤더에 상태 변경 여부 표시
//			if(rsltCount != null & rsltCount > 0) {
//				jsonInsertRslt.setHeader("MODIFY_STTS_YN", "Y");
//				jsonInsertRslt.setHeader("MODIFY_STTS", "ONGONG");
//			}
//			else {
//				jsonInsertRslt.setHeader("MODIFY_STTS_YN", "N");
//			}
//		}
		
		return jsonInsertRslt;
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON setCyclEvlComplete(TelewebJSON jsonParams) throws TelewebDaoException {
		return twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAEvlEnforceMapper", "updateQCyclSttsComplete", jsonParams);
	}
}
