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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("qaQltyClsfManageService")
public class QAQltyClsfManageServiceImpl implements QAQltyClsfManageService {
	
    private final TwbComDAO twbComDAO;
    public final InnbCreatCmmnService innbCreatCmmnService;

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON insertQaQltyClsf(TelewebJSON jsonParams) throws TelewebAppException {
		boolean isUpdate = true;
		
        String qltyClsfId = jsonParams.getString("QLTY_CLSF_ID");
        if(qltyClsfId == null || "".equals(qltyClsfId)) {
        	qltyClsfId = String.valueOf(innbCreatCmmnService.createSeqNo("QLTY_CLSF_ID"));
        	jsonParams.setString("QLTY_CLSF_ID", qltyClsfId);
        	isUpdate = false;
        }
        
        TelewebJSON objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "upsertQaQltyClsf", jsonParams);
        
		return objRetParams;
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON selectQaQltyClsfTree(TelewebJSON jsonParams) throws TelewebAppException {
		//해당 cucstco_id에 대한 최상위 데이타가 없을 경우 초기 데이터를 넣어 준다.
		TelewebJSON selJsonParams = new TelewebJSON(jsonParams);
		selJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "selectQaQltyClsfTree", jsonParams);
		
		if(selJsonParams.getHeaderInt("COUNT") == 0) {
			TelewebJSON insJsonParams = new TelewebJSON(jsonParams);
			JSONObject oParam = jsonParams.getDataObject().getJSONObject(0);
			JSONArray oArr = new JSONArray();
			oParam.put("MAKE_TOP", "Y");
			oParam.put("QLTY_CLSF_ID", String.valueOf(innbCreatCmmnService.createSeqNo("QLTY_CLSF_ID")));
			oParam.put("USE_YN", "Y");
			oParam.put("DEL_YN", "N");
			oArr.add(oParam);
			insJsonParams.setDataObject(oArr);
	        insJsonParams = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "upsertQaQltyClsf", insJsonParams);
	        if(insJsonParams.getHeaderBoolean("ERROR_FLAG"))	{
	        	return insJsonParams;
	        } else
	        	return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "selectQaQltyClsfTree", jsonParams);
		} else
			return selJsonParams;
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON deleteQaQltyClsf(TelewebJSON jsonParams) throws TelewebAppException {
		//관련 하위 테이블 데이터 확인 및 삭제 로직 추가 필요.
		
		TelewebJSON selectEvlArtclCnt = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "selectEvlArtclCnt", jsonParams);
		Integer cnt = selectEvlArtclCnt.getInt("CNT");
		if(cnt > 0)	{
			selectEvlArtclCnt.setHeader("ERROR_FLAG", true);
			selectEvlArtclCnt.setHeader("ERROR_MSG","EXIST_EVL_ARTCL");
			return selectEvlArtclCnt;
		}
		
		return twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "deleteQaQltyClsf", jsonParams);
	}
	
    
//    selectQaQltyEvlArtcl
//    upsertQaQltyClsf
//    deleteQaQltyEvlArtcl

	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaQltyEvlArtcl(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "selectQaQltyEvlArtcl", jsonParams);
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON insertQaQltyEvlArtcl(TelewebJSON jsonParams) throws TelewebAppException {
		boolean isUpdate = true;
		
        String evlArtclId = jsonParams.getString("EVL_ARTCL_ID");
        if(evlArtclId == null || "".equals(evlArtclId)) {
        	evlArtclId = String.valueOf(innbCreatCmmnService.createSeqNo("EVL_ARTCL_ID"));
        	jsonParams.setString("EVL_ARTCL_ID", evlArtclId);
        	isUpdate = false;
        }
        
        TelewebJSON objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "upsertQaQltyEvlArtcl", jsonParams);
        
		return objRetParams;
	}

	@Override
	@Transactional(readOnly = false)
	public TelewebJSON deleteQaQltyEvlArtcl(TelewebJSON jsonParams) throws TelewebAppException {
		String strIds = jsonParams.getString("LIST_ID");
		String[] arrId = strIds.split(",");
		
		
		List<HashMap<String,BigDecimal>> idList = new ArrayList<HashMap<String,BigDecimal>>();
		
		for(String id:arrId) {
			HashMap<String,BigDecimal> oId = new HashMap<String,BigDecimal>();
			oId.put("EVL_ARTCL_ID", new BigDecimal(id));
			idList.add(oId);
		}

		//해당 ID로 이미 진행된 평가가 있는지 확인. 진행된 평가가 있는 경우 삭제 불가함.
		TelewebJSON chkJsonParams = new TelewebJSON(jsonParams);
		chkJsonParams.setObject("LIST_ID", 0, idList);
		chkJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "selectQaPlanQltyCnt", chkJsonParams);
		
		Integer cnt = chkJsonParams.getInt("CNT");
		if(cnt > 0)	{
			chkJsonParams.setHeader("ERROR_FLAG", true);
			chkJsonParams.setHeader("ERROR_MSG","USED_EVL_ARTCL");
			return chkJsonParams;
		}
		
		TelewebJSON deleteJsonParams = new TelewebJSON(jsonParams);
		deleteJsonParams.setObject("LIST_ID", 0, idList);
		return twbComDAO.delete("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "deleteQaQltyEvlArtcl", deleteJsonParams);
	}

	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaQltyClsfCode(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "selectQaQltyClsfCode", jsonParams);
	}
	
	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaQltyEvlArtclListWithClsf(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "selectQaQltyEvlArtclListWithClsf", jsonParams);
	}

	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaPlanQltyList(TelewebJSON jsonParams) throws TelewebAppException {
        return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "selectQaPlanQltyList", jsonParams);
	}

	@Override
	public TelewebJSON reOrderQltyClsf(TelewebJSON jsonParams) throws TelewebAppException {
		
		String strParam = jsonParams.getString("LIST_DATA");
		String[] arrParam = strParam.split(",");
		JSONArray oParams = new JSONArray();
		
		for( int i = 0 ; i < arrParam.length ; i++) {
			JSONObject oJson = new JSONObject();
			String[] arrTmp = arrParam[i].split("_");
			oJson.put("QLTY_CLSF_ID", arrTmp[0]);
			oJson.put("SORT_ORD", arrTmp[1]);
			oParams.add(oJson);
		}
		
		JSONObject oJson = new JSONObject();
		oJson.put("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
		oJson.put("USER_ID", jsonParams.getString("USER_ID"));
		oJson.put("LIST_DATA", oParams);
		
		TelewebJSON upParams = new TelewebJSON(jsonParams);
		upParams.setDataObject("DATA", oJson);
        return twbComDAO.update("kr.co.hkcloud.palette3.qa.dao.QAQltyClsfManageMapper", "updateQltyClsfOrder", upParams);
	}
	
}
