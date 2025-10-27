package kr.co.hkcloud.palette3.qa.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("qaMngrManageService")
public class QAMngrManageServiceImpl implements QAMngrManageService {

    private final TwbComDAO twbComDAO;
    public final InnbCreatCmmnService innbCreatCmmnService;
	
	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectUserList(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAMngrManageMapper", "selectUserList", jsonParams);
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON insertQaMngr(TelewebJSON jsonParams) throws TelewebDaoException {
		//String qa_mngr_id = String.valueOf(innbCreatCmmnService.createSeqNo("QA_MNGR_ID"));
        //jsonParams.setString("qa_mngr_id", qa_mngr_id);
		String strMngrIds = jsonParams.getString("MNGR_LIST");
		String[] arrMngrIds = strMngrIds.split(",");
		
		List<HashMap<String,BigDecimal>> idList = new ArrayList<HashMap<String,BigDecimal>>();
		
		for(String id:arrMngrIds) {
			HashMap<String,BigDecimal> oId = new HashMap<String,BigDecimal>();
			oId.put("QA_MNGR_ID", new BigDecimal(id));
			idList.add(oId);
		}
		
		//추가할 ID가 이미 들어 있는지 확인.
		TelewebJSON chkJsonParams = new TelewebJSON(jsonParams);
		
		chkJsonParams.setObject("LIST_ID", 0, idList);
		chkJsonParams.setObject("QA_PLAN_ID", 0, jsonParams.getString("QA_PLAN_ID"));
		chkJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAMngrManageMapper", "selectMngrListWithMngrId", chkJsonParams);
		if(chkJsonParams.getHeaderBoolean("ERROR_FLAG")) {
			return chkJsonParams;
		} else if (chkJsonParams.getHeaderInt("COUNT") > 0) {
			//이미 있는 값은 제외
			JSONArray arr = chkJsonParams.getDataObject();
			for(int i = 0 ; i < arr.size(); i++) {
				String strMgrId = arr.getJSONObject(i).getString("QA_MNGR_ID");
				idList.removeIf(item -> strMgrId.equals(item.get("QA_MNGR_ID").toString()));
			}
		}
		
		if(idList.size() == 0)	// 추가할 데이터가 없으면 리턴
			return chkJsonParams;
		
		TelewebJSON insJsonParams = new TelewebJSON(jsonParams);
		
		insJsonParams.setObject("LIST_ID", 0, idList);
		insJsonParams.setObject("USER_ID", 0, jsonParams.getString("USER_ID"));
		insJsonParams.setObject("QA_PLAN_ID", 0, jsonParams.getString("QA_PLAN_ID"));
		
		twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAMngrManageMapper", "insertQaMngr", insJsonParams);
		
		//차수상태 초기 데이터도 같이 입력 입력해준다.
		return twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAMngrManageMapper", "insertCyclStts", insJsonParams);
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON deleteQaMngr(TelewebJSON jsonParams) throws TelewebDaoException {
		
		//해당계획에 평가 대상자가 할당 되어 있는지 확인.
		/*
		TelewebJSON chkAltmntJson = new TelewebJSON();
		JSONArray aTmpJson = new JSONArray();
		JSONObject oTmpJson = new JSONObject();
		
		oTmpJson.put("QA_PLAN_ID", jsonParams.getString("QA_PLAN_ID"));
		aTmpJson.add(oTmpJson);
		chkAltmntJson.setDataObject(aTmpJson);
		chkAltmntJson = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "selectQaTrgtAltmntMngrList", chkAltmntJson);
		Integer altmntCnt = Integer.valueOf(chkAltmntJson.getHeaderString("COUNT"));
		if(altmntCnt != null && altmntCnt > 0) {
			chkAltmntJson.setHeader("ERROR_FLAG", true);
			chkAltmntJson.setHeader("ERROR_MSG", "ALTMNT_MNGR_EXIST");
			return chkAltmntJson;
		}
		*/
		
		String strMngrIds = jsonParams.getString("MNGR_LIST");
		String[] arrMngrIds = strMngrIds.split(",");
		
		List<HashMap<String,BigDecimal>> idList = new ArrayList<HashMap<String,BigDecimal>>();
		
		for(String id:arrMngrIds) {
			HashMap<String,BigDecimal> oId = new HashMap<String,BigDecimal>();
			oId.put("QA_MNGR_ID", new BigDecimal(id));
			idList.add(oId);
		}
		
		TelewebJSON delJsonParams = new TelewebJSON(jsonParams);
		
		delJsonParams.setObject("MNGR_LIST", 0, idList);
		delJsonParams.setObject("QA_PLAN_ID", 0, jsonParams.getString("QA_PLAN_ID"));
		
		//이미 평가대상자가 할당된 대상이 있는지 먼저 확인.
		TelewebJSON chkAltmntJson = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlManageMapper", "selectQaTrgtAltmntCntWithMngrId", delJsonParams);
		Integer altmntCnt = Integer.valueOf(chkAltmntJson.getHeaderString("COUNT"));
		if(altmntCnt != null && altmntCnt > 0) {
			chkAltmntJson.setHeader("ERROR_FLAG", true);
			chkAltmntJson.setHeader("ERROR_MSG", "ALTMNT_MNGR_EXIST");
			return chkAltmntJson;
		}
		
		//차수 초기 데이터 삭제
		twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAMngrManageMapper", "deleteCyclStts", delJsonParams);
		
		return twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAMngrManageMapper", "deleteQaMngr", delJsonParams);
	}

	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectMngrList(TelewebJSON jsonParams) throws TelewebDaoException {
        return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAMngrManageMapper", "selectMngrList", jsonParams);
	}
}
