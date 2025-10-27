package kr.co.hkcloud.palette3.phone.sms.app;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("phoneAtalkTmplManageService")
public class PhoneAtalkTmplManageServiceImpl implements PhoneAtalkTmplManageService
{
    private final TwbComDAO            twbComDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final SendSmsService sendSmsService;
    
    //MTS 한국클라우드 인증코드
	private static final String _API_AUTH_CODE = "6R5XPUIkrGrZUFuCTlLjwQ==";
	//MTS 알림톡 API URL
	private static final String _API_ATALK_URL = "https://talks.mtsco.co.kr/mts/api";
    
    /**
     * 알림톡 발신프로필키 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectSendProfileKeys(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "selectSendProfileKeys", mjsonParams);
    }
    
    /**
     * SMS 템플릿 리스트 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON checktmplCd(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON result = new TelewebJSON();
    	// 템플릿코드 중복 검사
    	TelewebJSON tmplCd = 
    			twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper"
    					, "checktmplCd", mjsonParams);
    	// 템플릿명 중복 검사
    	TelewebJSON tmplNm = 
    			twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper"
    					, "checktmplNm", mjsonParams);
    	
    	result.setString("tmplCdCnt", tmplCd.getString("CNT"));
    	result.setString("tmplNmCnt", tmplNm.getString("CNT"));
    	return result;
    }
    /**
     * 알림톡 템플릿 저장
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON saveAtalkTmpl(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON result = new TelewebJSON();
    	try {
	    	// 파라미터 추출 및 재조합
	    	makeparam(mjsonParams);
	    	// 템플릿 유형 ID
	    	String atalkId = Integer.toString(innbCreatCmmnService.createSeqNo("ATALK_ID"));
	    	mjsonParams.setString("ATALK_ID", atalkId);
	    	twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper"
	    			, "saveAtalkTmpl"
	    			, mjsonParams);
	    	result.setString("code", "200");
    	}catch(Exception e) {
    		log.debug("Message = " + e.getMessage());
    		result.setString("code", "300");
    	}
    	return result;
    }
    
    /**
     * 알림톡 템플릿 수정
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON modifyAtalkTmpl(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON result = new TelewebJSON();
    	// mts 템플릿 수정 요청 결과 코드
    	String mtsCode = "";
    	try {
    		// 파라미터 추출 및 재조합
	    	this.makeparam(mjsonParams);
	    	// 템플릿 시스템 업데이트
	    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateAtalkTmpl", mjsonParams);
	    	result.setString("code", "200");
    	}catch(Exception e) {
    		log.debug("Message = " + e.getMessage());
    		result.setString("code", "300");
    	}
    	TelewebJSON tmplStatus = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "selectTmplStts", mjsonParams);
    	
    	if("R".equals(tmplStatus.getString("TMPL_STTS")) && 
    			("REG".equals(tmplStatus.getString("TMPL_IGI_STTS"))
    			|| "REJ".equals(tmplStatus.getString("TMPL_IGI_STTS")))) {// 템플릿 대기, 검수상태 등록/반려
    		// 템플릿 MTS 업데이트
    		mtsCode = this.mtsModifyAtalkTmpl(mjsonParams);
    	}
    	result.setString("mtsCode", mtsCode);
    	return result;
    }
    
    /**
     * 
     * @param mjsonParams
     * @return mts result code
     * @throws TelewebAppException
     */
    private String mtsModifyAtalkTmpl(TelewebJSON mjsonParams)
    {
    	// mts 수정 요청 응답 코드
    	String mtsModifyResultCode = "";
    	try {
	    	log.debug("MTS 수정 요청");
			// mts 업데이트
	    	List<String> atalkIdList = new ArrayList<>();
	    	String atalkId = mjsonParams.getString("ATALK_ID");
	    	
	    	atalkIdList.add(atalkId);
	    	mjsonParams.setObject("ATALK_ID_LIST", 0, atalkIdList);
	    	
	    	TelewebJSON atalkTmplList = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "selectAtalkTmplForMtsRegister", mjsonParams);
	    	JSONArray jsonArray = atalkTmplList.getDataObject(TwbCmmnConst.G_DATA);
	    	
	    	
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			MultiValueMap<String, Object> mtsParam = new LinkedMultiValueMap<>();
	    	String tmplMsgType = jsonObject.getString("TEMPLATE_MESSAGE_TYPE");
	    	String tmplEpszType = jsonObject.getString("TEMPLATE_EMPHASIZE_TYPE");
	    	String buttons = jsonObject.getString("BUTTONS");
	    	
	    	mtsParam.add("senderKey", jsonObject.getString("SENDER_KEY"));
	    	mtsParam.add("newSenderKey", jsonObject.getString("SENDER_KEY"));
	    	
	    	mtsParam.add("templateCode", jsonObject.getString("TEMPLATE_CODE"));
	    	mtsParam.add("newTemplateCode", jsonObject.getString("TEMPLATE_CODE"));
	    	
	    	mtsParam.add("newTemplateName", jsonObject.getString("TEMPLATE_NAME"));
	    	mtsParam.add("newTemplateMessageType", tmplMsgType);
	    	mtsParam.add("newTemplateEmphasizeType", tmplEpszType);
	    	mtsParam.add("newTemplateContent", jsonObject.getString("TEMPLATE_CONTENT"));
	    	// 메시지 유형 부가형 또는 복합형이면
	    	if("EX".equals(tmplMsgType) || "MI".equals(tmplMsgType)) {
	    		mtsParam.add("newTemplateExtra", jsonObject.getString("TEMPLATE_EXTRA"));
	    	}
	    	// 강조 유형 강조 표기형
	    	if("TEXT".equals(tmplEpszType)) {
	    		mtsParam.add("newTemplateTitle", jsonObject.getString("TEMPLATE_TITLE"));
	    		mtsParam.add("newTemplateSubtitle", jsonObject.getString("TEMPLATE_SUB_TITLE"));
	    	}
	    	// 강조 유형 이미지형 또는 아이템 리스트형
	    	if("IMAGE".equals(tmplEpszType)) {
	    		mtsParam.add("newTemplateImageName", "메인이미지");
	    		mtsParam.add("newTemplateImageUrl", jsonObject.getString("TEMPLATE_IMAGE_URL"));
	    	}
	    	// 강조 유형 아이템 리스트형
	    	if("ITEM_LIST".equals(tmplEpszType)) {		// 강조 유형 아이템 리스트형
	    		mtsParam.add("newTemplateHeader", jsonObject.getString("TEMPLATE_HEADER"));
	    		mtsParam.add("newTemplateItemHighlight", jsonObject.getString("TEMPLATE_ITEM_HIGHLIGHT"));
	    		mtsParam.add("newTemplateItem", jsonObject.getString("TEMPLATE_ITEM"));
	    	}
	    	if(!"".equals(buttons)) {
	    		mtsParam.add("buttons", buttons);
	    	}
	    	log.debug("mtsParam = " + mtsParam);
	    	Map<String, Object> mtsResult = sendSmsService.mtsModify(mtsParam);
	    	mtsModifyResultCode = mtsResult.get("code").toString();
	    	
	    	// mts 수정 요청 결과가 성공 아니면
	    	if(!"200".equals(mtsModifyResultCode)) {
	    		log.debug("MTS 수정 요청 이력 저장");
	    		// mts 결과 업데이트
	    		TelewebJSON param = new TelewebJSON();
	            // 알림톡 ID
	    		param.setString("ATALK_ID", atalkId);
	    		
		    	@SuppressWarnings("unchecked")
				Map<String, Object> mtsData = (Map<String, Object>)mtsResult.get("data");
		    	// 검수 결과
	        	@SuppressWarnings("unchecked")
				List<Map<String, String>> sResult = 
	        			(List<Map<String, String>>)mtsData.get("comments");
	        	Map<String, Object> mtsComments = new HashMap<>();
	        	
	        	mtsComments.put("code", mtsResult.get("code"));
	        	mtsComments.put("comments", sResult);
	        	param.setString("MTS_COMMENTS", mtsComments.toString());
	        	
	        	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper"
	    				, "updateTmplStatusWithMtsStatus", param);
	    	}
	    	
    	}catch(Exception e) {
    		log.debug("Message = " + e.getMessage());
    		mtsModifyResultCode = "300";
    	}
	    
    	return mtsModifyResultCode;
    }
    
    /**
     * 알림톡 템플릿 삭제
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON deleteAtalkTmpl(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON result = new TelewebJSON();
    	try {
	    	List<String> atalkIdList = new ArrayList<>();
	    	JSONArray paramArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
	    	for (int n = 0; n < paramArray.size(); n++) {
	        	JSONObject objData = paramArray.getJSONObject(n);
	        	
	        	@SuppressWarnings("rawtypes")
	            Iterator it = objData.keys();
	            while(it.hasNext()) {
	                String strKey = (String) it.next();
	                String strValue = objData.getString(strKey);
	                
	                if("ATALK_ID_LIST".equals(strKey)) {			// 알림톡 아이디 리스트
	                	atalkIdList.add(strValue);
	                }
	            }
	        }
	    	mjsonParams.setObject("ATALK_ID_LIST", 0, atalkIdList);
    	
	    	twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "deleteAtalkTmpl", mjsonParams);
    		result.setString("code", "200");
    	}catch(Exception e) {
    		result.setString("code", "300");
    		log.debug("Message = " + e.getMessage());
    	}
    	
    	return result;
    }
        
    private void makeparam(TelewebJSON mjsonParams) throws TelewebAppException
    {
//    	List<Map<String, String>> itemList = null;
//    	List<Map<String, String>> buttonList = null;
    	
    	List<String> items = new ArrayList<>();			// 강조 유형 - 아이템 리스트 - 아이템 목록
    	List<Map<String, String>> itemList = new ArrayList<>();
    	// 버튼 리스트
    	List<Map<String, String>> buttonList = new ArrayList<>();
//    	List<String> buttons = new ArrayList<>();		// 버튼 목록
//    	String[] items = {}, buttons = {};
    	JSONArray jsonArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	ObjectMapper mapper = new ObjectMapper();
    	
    	String highLightTitle = "", highLightDscp = "";
    	String mainImageGroupKey = "", highLightImageGroupKey = "";
    	if(jsonArray.size() > 0){
	    	for (int n = 0; n < jsonArray.size(); n++) {
	        	JSONObject objData = jsonArray.getJSONObject(n);
	        	
	        	@SuppressWarnings("rawtypes")
	            Iterator it = objData.keys();
	            while(it.hasNext()) {
	                String strKey = (String) it.next();
	                String strValue = objData.getString(strKey);
	                
	                if("TMPL_ITEM".equals(strKey)) {			// 아이템 목록	                	
	                	// 배열 문자 치환
	                	String itemString = 
	                			strValue.replace("&#91;", "[").replace("&#93;", "]");
	                	try {
	                		// json string to list map
		                	List<Map<String, String>> itemsMap = 
		                			mapper.readValue(
		                					itemString
		                					, new TypeReference<List<Map<String, String>>>(){});
		                	log.debug("itemsMap = " + itemsMap);
		                	if(itemsMap.size() > 0) {
		                		itemList.addAll(itemsMap);
		                	}
	                	}catch(Exception e) {
	                		log.debug(e.getMessage());
	                	}
	                }
	                
	                if("TMPL_BTNS".equals(strKey)) {			// 버튼 목록
	                	// 배열 문자 치환
	                	String buttonString = 
	                			strValue.replace("&#91;", "[").replace("&#93;", "]");
	                	try {
	                		// json string to list map
		                	List<Map<String, String>> buttonMapList = 
		                			mapper.readValue(
		                					buttonString
		                					, new TypeReference<List<Map<String, String>>>(){});
		                	log.debug("buttonMap = " + buttonMapList);
		                	// mts 요청에 필요 없는 항목 제거, 화면에서만 사용 하던 항목
		                	if(buttonMapList.size() > 0) {
		                		for(Map<String, String> item : buttonMapList) {
		                			if(item.containsKey("title")) {
		                				item.remove("title");
		                			}
		                			
		                			if(item.containsKey("msg")) {
		                				item.remove("msg");
		                			}
		                		}
		                		buttonList.addAll(buttonMapList);
		                	}
	                	}catch(Exception e) {
	                		log.debug(e.getMessage());
	                	}
	                }
	                
	                // 강조 유형 - 아이템 리스트 - 하이라이트 타이틀
	                if("HIGH_LIGHT_TITLE".equals(strKey)) {
	                	highLightTitle = strValue;	                	
	                }
	                
	                // 강조 유형 - 아이템 리스트 - 하이라이트 설명
	                if("HIGH_LIGHT_DSCP".equals(strKey)) {
	                	highLightDscp = strValue;	                	
	                }
	                
	                // 강조 유형 - 이미지형  or 아이템 리스트형 - 이미지
	                if("IMAGE_FILE_GROUP_KEY".equals(strKey)) {
	                	mainImageGroupKey = strValue;	                	
	                }
	                
	                // 강조 유형 - 아이템 리스트형 - 하이라이트 이미지
	                if("HIGH_FILE_GROUP_KEY".equals(strKey)) {
	                	highLightImageGroupKey = strValue;	                	
	                }
	            }
	        }
    	}
    	
    	String fileGroupKey = "";
    	String mainImageKey = "", highLightImageKey = "";
    	// 강조유형 이미지 파일 그룹키 동기화
    	// 강조유형 아이템리스트 - 메인, 하이라이트 이미지
    	if(!"".equals(mainImageGroupKey)) {
    		// 메인 이미지 파일키 가져오기
    		TelewebJSON param = new TelewebJSON();
    		param.setString("FILE_GROUP_KEY", mainImageGroupKey);
    		
    		TelewebJSON mainImageKeyJSON = 
    				twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "selectFileKeyForMtsFileUpload", param);
    		// 메인 이미지 파일 키
    		mainImageKey = mainImageKeyJSON.getString("FILE_KEY");
    	}
    	
    	if(!"".equals(highLightImageGroupKey)) {
    		// 하이라이트 이미지 파일키 가져오기
    		TelewebJSON param = new TelewebJSON();
    		param.setString("FILE_GROUP_KEY", highLightImageGroupKey);
    		TelewebJSON mainImageKeyJSON = 
    				twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "selectFileKeyForMtsFileUpload", param);
    		// 하이라이트 이미지 파일 키
    		highLightImageKey = mainImageKeyJSON.getString("FILE_KEY");
    	}
    	
    	if(!"".equals(mainImageGroupKey) && !"".equals(highLightImageGroupKey)) {
	    	// 메인, 하이라이트 이미지그룹키 동기화
    		log.debug("11111111111111111111");
			twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateFileGroupKeyForMtsHighLightImage", mjsonParams);
			fileGroupKey = mainImageGroupKey;
    	}else {
	    	// 이미지 파일이 있으면 mts 업로드 실행
	    	if(!"".equals(mainImageGroupKey)) {
	    		fileGroupKey = mainImageGroupKey;
	    	}else {
		    	if(!"".equals(highLightImageGroupKey)) {
		    		fileGroupKey = highLightImageGroupKey;
		    	}
	    	}
    	}
    	log.debug("fileGroupKey = " + fileGroupKey);
    	
    	mjsonParams.setString("FILE_GROUP_KEY", fileGroupKey);
    	    	
    	if(!"".equals(fileGroupKey)) {
    		// 파일 정보 조회
    		List<Map<String, String>> attachedFile = 
    				sendSmsService.selectAttachedFile(fileGroupKey);
    		
    		String mainImagePath = "", mainImageLnk = "";
    		String highlightImagePath = "", highlightImageLnk = "";
    		
    		for(int i = 0; i < attachedFile.size(); i++) {
    			String fileKey = attachedFile.get(i).get("FILE_KEY");
    			String lastPath = mainImageKey.equals(fileKey) ? "MAIN" : "HIGHLIGHT";
    			Map<String, String> result = this.requestFileUpload(
    					attachedFile.get(i), mjsonParams.getString("SEND_PROFILE_KEY"), lastPath);
    			
    			if("MAIN".equals(lastPath)) {
    				mainImagePath = result.get("IMAGE_PATH");
    				mainImageLnk = result.get("IMAGE_LNK");
    			}else {
    				highlightImagePath = result.get("IMAGE_PATH");
    				highlightImageLnk = result.get("IMAGE_LNK");
    			}
    		}
    		mjsonParams.setString("MAIN_IMAGE_PATH", mainImagePath);
			mjsonParams.setString("MAIN_IMAGE_LNK", mainImageLnk);
			mjsonParams.setString("HIGHLIGHT_IMAGE_PATH", highlightImagePath);
			mjsonParams.setString("HIGHLIGHT_IMAGE_LNK", highlightImageLnk);
    	}else {
    		mjsonParams.setString("MAIN_IMAGE_PATH", "");
			mjsonParams.setString("MAIN_IMAGE_LNK", "");
			mjsonParams.setString("HIGHLIGHT_IMAGE_PATH", "");
			mjsonParams.setString("HIGHLIGHT_IMAGE_LNK", "");
    	}
    	
    	JSONObject jsonObject = jsonArray.getJSONObject(0);
//    	log.debug("item size = " + items.size());
    	
//    	if(items.size() > 0) {
    	if(itemList.size() > 0) {
    		
//    		for(String item : items) {
//    			log.debug("item = " + item);
//    			String[] itemArray =item.split("#//---#");
//    			log.debug("itemArray = " + itemArray);
//    			log.debug("itemArray length = " + itemArray.length);
//    			Map<String, String> itemMap = new HashMap<>();
//    			itemMap.put("title", itemArray[0]);
//    			itemMap.put("description", itemArray[1]);
//    			itemList.add(itemMap);
//    		}
    		
    		Map<String, String> summary = new HashMap<>();
    		
    		String summaryTitle = jsonObject.getString("ITEM_SUMRY_TITLE");
    		String summaryDescription = jsonObject.getString("ITEM_SUMRY_DSCP");
    		
    		summary.put("title", summaryTitle);
    		summary.put("description", summaryDescription);
    		
    		
    		Map<String, Object> templateItem = new HashMap<>();
    		templateItem.put("summary", summary);
    		templateItem.put("list", itemList);    		
    		
    		
    		String templateItemJson = null;
    		try {
    			templateItemJson = mapper.writeValueAsString(templateItem);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}    		
    		jsonObject.put("TMPL_ITEM", "'" + templateItemJson + "'");
    	}else {
    		jsonObject.put("TMPL_ITEM", "");
    	}
    	
//    	if(buttons.size() > 0) {
   		if(buttonList.size() > 0) { 
   			/*
    		for(String button : buttons) {
    			String[] buttonArray =button.split("#//---#");
    			Map<String, String> buttonMap = new HashMap<>();
    			
    			log.debug("buttonArray length = " + buttonArray.length);
    			log.debug("buttonArray = " + buttonArray);
    			String name = buttonArray[0];
    			String linkType = buttonArray[1];
    			String ordering = buttonArray[2];
    			
    			buttonMap.put("name", name);
    			buttonMap.put("linkType", linkType);
    			buttonMap.put("ordering", ordering);
    			
    			switch(linkType) {
    				case "WL" :
    					if(buttonArray.length > 3) {
	    					buttonMap.put("linkMo", buttonArray[3]);
    					}else {
    						buttonMap.put("linkMo", "");
    					}
    					
    					if(buttonArray.length > 4) {
    						buttonMap.put("linkPc", buttonArray[4]);
    					}else {
    						buttonMap.put("linkPc", "");
    					}
    					break;
    				case "AL" :
    					if(buttonArray.length > 3) {
	    					buttonMap.put("linkMo", buttonArray[3]);
    					}else {
    						buttonMap.put("linkMo", "");
    					}
    					
    					if(buttonArray.length > 4) {
    						buttonMap.put("linkPc", buttonArray[4]);
    					}else {
    						buttonMap.put("linkPc", "");
    					}
    					
    					if(buttonArray.length > 5) {
	    					buttonMap.put("linkAnd", buttonArray[5]);
    					}else {
    						buttonMap.put("linkAnd", "");
    					}
    					
    					if(buttonArray.length > 6) {
    						buttonMap.put("linkIos", buttonArray[6]);
    					}else {
    						buttonMap.put("linkIos", "");
    					}
    			}
    			buttonList.add(buttonMap);
    		}
    		*/    		
    		try {
    			String templateButtonJson = mapper.writeValueAsString(buttonList);
    			jsonObject.put("BUTTONS", "'" + templateButtonJson + "'");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    	}else {
    		jsonObject.put("BUTTONS", "");
    	}
    	
    	Map<String, Object> highLightMap = new HashMap<>();
    	boolean isHighLight = false;
    	if(!"".equals(highLightTitle)) {
    		highLightMap.put("title", highLightTitle);
    		isHighLight = true;	
    	}
    	
    	
    	if(isHighLight) {
    		if(!"".equals(highLightDscp)) {
        		highLightMap.put("description", highLightDscp);
        	}
    		
    		// 하이라이트 이미지 추가
    		if(mjsonParams.getString("HIGHLIGHT_IMAGE_LNK") != null) {
    			highLightMap.put("imageUrl", mjsonParams.getString("HIGHLIGHT_IMAGE_LNK"));
    		}
    		
    		String highLightJson = null;
    		try {
    			highLightJson = mapper.writeValueAsString(highLightMap);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    		
    		jsonObject.put("ITEM_HIGH_LIGHT", "'" + highLightJson + "'");
    	}else {
    		jsonObject.put("ITEM_HIGH_LIGHT", "");
    	}
    	jsonArray.add(jsonObject);
    	
    }
    
    private Map<String, String> requestFileUpload(Map<String, String> attachedFile, String sendProfileKey, String lastPath) throws TelewebAppException
    {
    	Map<String, String> result = new HashMap<>();
    	
		String filePath = "/" + attachedFile.get("PATH_TYPE_CD") + "/" 
					+ attachedFile.get("TASK_TYPE_CD") + "/"
					+ attachedFile.get("FILE_PATH") + "/"
					+ attachedFile.get("STRG_FILE_NM");
		log.debug("filePath = " + filePath);
		log.debug("lastPath = " + lastPath);
		
		Map<String, String> mtsFileUploadResult = 
				sendSmsService.mtsAtalkFileUpload(filePath, sendProfileKey, lastPath);
		log.debug("mtsFileUploadResult = " + mtsFileUploadResult.toString());
		if("0000".equals(mtsFileUploadResult.get("code"))) {
			String imageUrl = mtsFileUploadResult.get("image");
			result.put("IMAGE_PATH", filePath);
			result.put("IMAGE_LNK", imageUrl);
		}
		return result;
    }
    
    /*
    private void makeButtonParam(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	List<Map<String, String>> buttonList = null;
    	// 버튼 목록
    	String[] buttons = {};
    	JSONArray jsonArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	ObjectMapper mapper = new ObjectMapper();
    	
    	if(jsonArray.size() > 0){
	    	for (int n = 0; n < jsonArray.size(); n++) {
	        	JSONObject objData = jsonArray.getJSONObject(n);
	        	
	        	@SuppressWarnings("rawtypes")
	            Iterator it = objData.keys();
	            while(it.hasNext()) {
	                String strKey = (String) it.next();
	                String strValue = objData.getString(strKey);
	                
	                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("TMPL_BTNS") > -1) {
	                	buttons[buttons.length] = strValue;	                	
	                }
	                
	            }
	        }
    	}
    	
    	JSONObject jsonObject = jsonArray.getJSONObject(0);
    	if(buttons.length> 0) {
    		buttonList = new ArrayList<>();
    		String linkMo = "", linkPc = "", linkAnd = "", linkIos = "";
    		
    		for(int i = 0; i < buttons.length; i++) {
    			String[] buttonArray =buttons[i].split("#//---#");
    			Map<String, String> buttonMap = new HashMap<>();
    			
    			String name = buttonArray[0];
    			String linkType = buttonArray[1];
    			String ordering = buttonArray[2];
    			
    			buttonMap.put("name", name);
    			buttonMap.put("linkType", linkType);
    			buttonMap.put("ordering", ordering);
    			
    			switch(linkType) {
    				case "WL" :
    					linkMo = buttonArray[3];
    					buttonMap.put("linkMo", linkMo);
    					
    					linkPc = buttonArray[4];
    					if(!"".equals(linkPc)) {
    						buttonMap.put("linkPc", linkPc);
    					}
    					break;
    				case "AL" :
    					linkMo = buttonArray[3];
    					if(!"".equals(linkMo)) {
    						buttonMap.put("linkMo", linkMo);
    					}
    					
    					linkPc = buttonArray[4];
    					if(!"".equals(linkPc)) {
    						buttonMap.put("linkPc", linkPc);
    					}
    					
    					linkAnd = buttonArray[5];
    					if(!"".equals(linkAnd)) {
    						buttonMap.put("linkAnd", linkAnd);
    					}
    					
    					linkIos = buttonArray[6];
    					if(!"".equals(linkIos)) {
    						buttonMap.put("linkIos", linkIos);
    					}
    			}
    			buttonList.add(buttonMap);
    		}
    		
    		String templateButtonJson = null;
    		try {
    			templateButtonJson = mapper.writeValueAsString(buttonList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    		
    		jsonObject.put("buttons", templateButtonJson);
    	}
    }
    */
    /**
     * 알림톡 템플릿 등록 및 수정 파라미터 추출 후 Map으로 반환
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    /*
    private Map<String, Object> extractParameter(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException
    {
    	Map<String, Object> atalkTmpl = new HashMap<>();
    	JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	for (int n = 0; n < jsonObj.size(); n++) {
        	JSONObject objData = jsonObj.getJSONObject(n);
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);
                
                // 발송 프로필 키
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("SEND_PROFILE_KEY") > -1) {
                	atalkTmpl.put("senderKey", strValue);
                }
                
                // UUID
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("UUID") > -1) {
                	atalkTmpl.put("uuid", strValue);
                }
                
                // 알림톡 템플릿 코드
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("TMPL_CD") > -1) {
                	atalkTmpl.put("templateCode", strValue);
                }
                
                // 알림톡 템플릿명
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("TMPL_NM") > -1) {
                	atalkTmpl.put("templateName", strValue);
                }
                
                // 알림톡 템플릿 내용
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("TMPL_CN") > -1) {
                	atalkTmpl.put("templateContent", strValue);
                }
                
                // 고객사
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("CUSTCO_ID") > -1) {
                	atalkTmpl.put("custcoId", strValue);
                }
                
                // 발송 요청자
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("USER_ID") > -1) {
                	atalkTmpl.put("userId", strValue);
                }
                
                // 템플릿 메시지 유형
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("TMPL_MSG_TYPE") > -1) {
                	atalkTmpl.put("templateMessageType", strValue);
                }
                
                // 템플릿 강조 유형
                if(StringUtils.isNotEmpty(strValue) && strKey.indexOf("TMPL_EPSZ_TYPE") > -1) {
                	atalkTmpl.put("templateEmphasizeType", strValue);
                }
            }
        }
    	return atalkTmpl;
    }
    */
    /**
     * 알림톡 목록 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON selectAtalkTmplList(TelewebJSON mjsonParams) throws TelewebAppException
    {    	
    	TelewebJSON result = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper"
    			, "selectAtalkTmplList", mjsonParams);
//    	this.convertDetailDataStringToObject(result);
    	return result;
    }
    
    /**
     * 알림톡 템플릿 상태 업데이트 후 상태 리스트 가져오기
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON atalkTmplSttsUpdateAndSelect(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON mtsRequest = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper"
    			, "selectAtalkTmplListForMtsRequest", mjsonParams);
    	
    	JSONArray jsonArray = mtsRequest.getDataObject(TwbCmmnConst.G_DATA);
    	for (int n = 0; n < jsonArray.size(); n++) {
        	JSONObject jsonObject = jsonArray.getJSONObject(n);
        	log.debug("jsonObject = " + jsonObject);
        	
        	// 알림톡 ID
            String atalkId = "";
            // 템플릿 코드
            String tmplCd = "";
            // 템플릿 상태
            String tmplStts = "";
            // 검수 상태
            String tmplIgiStts = "";
            // 휴면 상태
            String tmplDmcyYn = "";
            
        	@SuppressWarnings("rawtypes")
            Iterator it = jsonObject.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = jsonObject.getString(strKey);
                
                // 알림톡 ID
    			if(("ATALK_ID").equals(strKey)) {
    				atalkId = strValue;
    			}
    			
    			// 템플릿 코드
                if(("TMPL_CD").equals(strKey)) {
                	tmplCd = strValue;
                }
                
                // 템플릿 상태
                if(("TMPL_STTS").equals(strKey)) {
                	tmplStts = strValue;
                }
                
                // 템플릿 검수 상태
                if(("TMPL_IGI_STTS").equals(strKey)) {
                	tmplIgiStts = strValue;
                }
                
                // 템플릿 휴면 상태
                if(("TMPL_DMCY_YN").equals(strKey)) {
                	tmplDmcyYn = strValue;
                }
            }
            
            MultiValueMap<String, Object> mtsParam = new LinkedMultiValueMap<>();
			mtsParam.add("senderKey", mjsonParams.getString("SEND_PROFILE_KEY"));
			mtsParam.add("templateCode", tmplCd);
			
            log.debug("TMPL_CD = " + tmplCd + ", tmplStts = " 
            + tmplStts + ", TMPL_IGI_STTS = " + tmplIgiStts + ", TMPL_DMCY_YN = " + tmplDmcyYn);
            
            if(!"NOTREG".equals(tmplIgiStts)) {
                Map<String, Object> mtsResult = sendSmsService.mtsAtalkTmplStatus(mtsParam);
                
                
                @SuppressWarnings("unchecked")
				Map<String, Object> mtsData = (Map<String, Object>)mtsResult.get("data");
                
            	// 템플릿 상태, 검수 상태가 MTS 정보와 다르면 MTS 정보로 update
            	// 템플릿 상태
            	String status = (String)mtsData.get("status");
            	// 검수 상태
            	String inspectionStatus = (String)mtsData.get("inspectionStatus");
            	// 휴면 상태
            	String dormant = "true".equals(mtsData.get("dormant").toString())?"Y":"N";
            	
            	if(status != null && !"".equals(status) 
            			&& inspectionStatus != null && !"".equals(inspectionStatus)
            			&& dormant != null && !"".equals(dormant)) {
                	if(!tmplStts.equals(status) || !tmplIgiStts.equals(inspectionStatus)
                			|| !tmplDmcyYn.equals(dormant)) {
                		
                		log.debug("mts 정보 변경 감지");
                		
                        TelewebJSON param = new TelewebJSON();
                        // 알림톡 ID
                		param.setString("ATALK_ID", atalkId);
                		
                		// 검수 결과
                    	@SuppressWarnings("unchecked")
        				List<Map<String, String>> sResult = 
                    			(List<Map<String, String>>)mtsData.get("comments");
                    	Map<String, Object> mtsComments = new HashMap<>();
                    	
                    	mtsComments.put("code", mtsResult.get("code"));
                    	mtsComments.put("comments", sResult);
                    	param.setString("MTS_COMMENTS", mtsComments.toString());
                    	
                    	if(!tmplStts.equals(status)){
                    		param.setString("TMPL_STTS", status);
                    	}
                    	if(!tmplIgiStts.equals(inspectionStatus)){
                    		param.setString("TMPL_IGI_STTS", inspectionStatus);
                    	}
                    	if(!tmplDmcyYn.equals(dormant)){
                    		param.setString("TMPL_DMCY_YN", dormant);
                    	}
                		
                		String printLog = "MTS INFO -> atalkId = " + atalkId + 
                    			", MTS_COMMENTS = " + mtsComments + 
                    			", status = " + status + 
                    			", inspectionStatus = " + inspectionStatus + 
                    			", dormant = " + dormant;
                		log.debug("TMPL_DMCY_YN = " + param.getString("TMPL_DMCY_YN"));
                		log.debug("printLog = " + printLog);
                		twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper"
                				, "updateTmplStatusWithMtsStatus", param);
                	}
            	}
            }
        }
    	
    	TelewebJSON result = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper"
    			, "selectAtalkTmplSttsList", mjsonParams);
//    	this.convertDetailDataStringToObject(result);
    	return result;
    }
    
//    private void convertDetailDataStringToObject(TelewebJSON result) throws TelewebAppException
//    {
//    	JSONArray jsonArray = result.getDataObject(TwbCmmnConst.G_DATA);
//    	for (int n = 0; n < jsonArray.size(); n++) {
//	    	JSONObject jsonObject = jsonArray.getJSONObject(n);
//	    	ObjectMapper mapper = new ObjectMapper();
//	    	
//	    	@SuppressWarnings("rawtypes")
//            Iterator it = jsonObject.keys();
//            while(it.hasNext()) {
//                String strKey = (String) it.next();
//                String strValue = jsonObject.getString(strKey);
//	    	
//                if("ITEM_HIGH_LIGHT".equals(strKey)) {
//			    	if(!"".equals(strValue)) {
//			    		log.debug("itemHighLightString = " + strValue);
//			    		try {
//							Map<String, String> itemHighLight = 
//							mapper.readValue("'" + strValue + "'", new TypeReference<Map<String, String>>(){});
//							
//							result.setObject("ITEM_HIGH_LIGHT", 0, itemHighLight);
//						} catch (JsonMappingException e) {
//							e.printStackTrace();
//						} catch (JsonProcessingException e) {
//							e.printStackTrace();
//						}
//			    	}
//                }
//                if("TMPL_ITEM".equals(strKey)) {
//			    	if(!"".equals(strValue)) {
//			    		log.debug("tmplItemString = " + strValue);
//			    		try {
//			    			Map<String, Object> tmplItem = 
//			    					mapper.readValue("'" + strValue + "'", new TypeReference<Map<String, Object>>(){});
//			    			
//			    			result.setObject("TMPL_ITEM", 0, tmplItem);
//			    		} catch (JsonMappingException e) {
//			    			e.printStackTrace();
//			    		} catch (JsonProcessingException e) {
//			    			e.printStackTrace();
//			    		}
//			    	}
//                }
//                if("BUTTONS".equals(strKey)) {
//			    	if(!"".equals(strValue)) {
//			    		log.debug("buttonsString = " + strValue);
//			    		try {
//			    			List<Map<String, String>> buttons = 
//			    					mapper.readValue("'" + strValue + "'", new TypeReference<List<Map<String, String>>>(){});
//			    			
//			    			result.setObject("BUTTONS", 0, buttons);
//			    		} catch (JsonMappingException e) {
//			    			e.printStackTrace();
//			    		} catch (JsonProcessingException e) {
//			    			e.printStackTrace();
//			    		}
//			    	}
//                }
//            }
//    	}
//    }
    
    /**
     * 알림톡 목록 조회
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
	@Transactional(readOnly = false)
    public TelewebJSON mtsRegister(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	boolean isSuccess = true;	// 요청 성공 여부
    	List<String> atalkIdList = new ArrayList<>();
    	JSONArray paramArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject paramObject = null;
    	for (int n = 0; n < paramArray.size(); n++) {
        	JSONObject objData = paramArray.getJSONObject(n);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);
                
                if("ATALK_ID_LIST".equals(strKey)) {			// 알림톡 아이디
                	atalkIdList.add(strValue);
                }
            }
        }
    	paramObject = paramArray.getJSONObject(0);
    	paramObject.put("ATALK_ID_LIST", atalkIdList);
    	paramArray.add(paramObject);
    	
    	TelewebJSON atalkTmplList = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "selectAtalkTmplForMtsRegister", mjsonParams);
    	JSONArray jsonArray = atalkTmplList.getDataObject(TwbCmmnConst.G_DATA);
    	
    	List<Map<String, String>> resultList = new ArrayList<>();
    	
    	for(int i = 0; i < jsonArray.size(); i++) {
    		JSONObject jsonObject = jsonArray.getJSONObject(i);
    		MultiValueMap<String, Object> mtsParam = new LinkedMultiValueMap<>();
        	String tmplMsgType = jsonObject.getString("TEMPLATE_MESSAGE_TYPE");
        	String tmplEpszType = jsonObject.getString("TEMPLATE_EMPHASIZE_TYPE");
        	String buttons = jsonObject.getString("BUTTONS");
        	
        	mtsParam.add("senderKey", jsonObject.getString("SENDER_KEY"));
        	mtsParam.add("templateCode", jsonObject.getString("TEMPLATE_CODE"));
        	mtsParam.add("templateName", jsonObject.getString("TEMPLATE_NAME"));
        	mtsParam.add("templateMessageType", tmplMsgType);
        	mtsParam.add("templateEmphasizeType", tmplEpszType);
        	mtsParam.add("templateContent", jsonObject.getString("TEMPLATE_CONTENT").replace("\n", "").replace("\r", ""));
        	// 메시지 유형 부가형 또는 복합형이면
        	if("EX".equals(tmplMsgType) || "MI".equals(tmplMsgType)) {
        		mtsParam.add("templateExtra", jsonObject.getString("TEMPLATE_EXTRA"));
        	}
        	// 강조 유형 강조 표기형
        	if("TEXT".equals(tmplEpszType)) {
        		mtsParam.add("templateTitle", jsonObject.getString("TEMPLATE_TITLE"));
        		mtsParam.add("templateSubtitle", jsonObject.getString("TEMPLATE_SUB_TITLE"));
        	}
        	// 강조 유형 이미지형 또는 아이템 리스트형
        	if("IMAGE".equals(tmplEpszType)) {
        		mtsParam.add("templateImageName", "메인이미지");
        		mtsParam.add("templateImageUrl", jsonObject.getString("TEMPLATE_IMAGE_URL"));
        	}
        	// 강조 유형 아이템 리스트형
        	if("ITEM_LIST".equals(tmplEpszType)) {		// 강조 유형 아이템 리스트형
        		mtsParam.add("templateHeader", jsonObject.getString("TEMPLATE_HEADER"));
        		mtsParam.add("templateItemHighlight", jsonObject.getString("TEMPLATE_ITEM_HIGHLIGHT"));
        		log.debug("templateItem = " + jsonObject.getString("TEMPLATE_ITEM"));
        		mtsParam.add("templateItem", jsonObject.getString("TEMPLATE_ITEM"));
        	}
        	if(!"".equals(buttons)) {
        		mtsParam.add("buttons", buttons);
        	}
        	
        	Map<String, String> result = sendSmsService.mtsAtalkTmplRegister(mtsParam);
        	resultList.add(result);
        	String atalkId = jsonObject.getString("ATALK_ID");
        	paramObject.put("ATALK_ID", atalkId);
        	paramObject.put("MTS_RESULT", result);
        	
    		if("200".equals(result.get("code"))) {
        		// 검수상태 REG로 변경
        		paramObject.put("TMPL_IGI_STTS", "REG");
        	}else {
        		isSuccess = false;
        	}
    		twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateAtalkIgiStts", mjsonParams);
    	}
    	
    	TelewebJSON result = new TelewebJSON();
    	JSONArray resultArray = result.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject resultObject = resultArray.getJSONObject(0);
    	resultObject.put("code", isSuccess?"200":"300");
    	resultObject.put("result", resultList);
    	resultArray.add(resultObject);
    	
    	return result;
    }
    
    public TelewebJSON selectImageList(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	return twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "selectImageList", mjsonParams);
    }
    
    
    public TelewebJSON mtsRequest(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	boolean isSuccess = true;	// 요청 성공 여부
    	List<String> tmplCdList = new ArrayList<>();
    	JSONArray paramArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	for (int n = 0; n < paramArray.size(); n++) {
        	JSONObject objData = paramArray.getJSONObject(n);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);
                
                if("TMPL_CD_LIST".equals(strKey)) {			// 알림톡 아이디+템플릿코드 리스트
                	tmplCdList.add(strValue);
                }
            }
        }
    	
    	List<Map<String, String>> resultList = new ArrayList<>();
    	if(tmplCdList.size() > 0) {
    		for(String tmplCd : tmplCdList) {
    			MultiValueMap<String, Object> mtsParam = new LinkedMultiValueMap<>();
    			
//    			String[] param = tmplCd.split("/#/");
//    			String atalkId = param[0];
    			mtsParam.add("senderKey", mjsonParams.getString("SEND_PROFILE_KEY"));
    			mtsParam.add("templateCode", tmplCd);
    			Map<String, String> result = sendSmsService.mtsAtalkTmplRequest(mtsParam);
    			resultList.add(result);
    			
    			/*
    			if("200".equals(result.get("code"))){
    				// 검수상태 REQ로 변경
            		paramObject.put("TMPL_IGI_STTS", "REQ");
    			}else{
    			*/
    			if(!"200".equals(result.get("code"))){
    				isSuccess = false;
            	}
    			
    			JSONObject paramObject = paramArray.getJSONObject(0);
    			paramObject.put("TMPL_CD", tmplCd);
    			paramObject.put("MTS_RESULT", result);
    			twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateAtalkIgiStts", mjsonParams);
    		}
    	}
    	TelewebJSON result = new TelewebJSON();
    	JSONArray resultArray = result.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject resultObject = resultArray.getJSONObject(0);
    	resultObject.put("code", isSuccess?"200":"300");
    	resultObject.put("result", resultList);
    	resultArray.add(resultObject);
    	
    	return result;
    }
    
    public TelewebJSON mtsCancelRequest(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	boolean isSuccess = true;	// 요청 성공 여부
    	List<String> tmplCdList = new ArrayList<>();
    	JSONArray paramArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	for (int n = 0; n < paramArray.size(); n++) {
    		JSONObject objData = paramArray.getJSONObject(n);
    		
    		@SuppressWarnings("rawtypes")
    		Iterator it = objData.keys();
    		while(it.hasNext()) {
    			String strKey = (String) it.next();
    			String strValue = objData.getString(strKey);
    			
    			if("TMPL_CD_LIST".equals(strKey)) {			// 알림톡 아이디+템플릿코드 리스트
    				tmplCdList.add(strValue);
    			}
    		}
    	}
    	
    	List<Map<String, String>> resultList = new ArrayList<>();
    	if(tmplCdList.size() > 0) {
    		for(String tmplCd : tmplCdList) {
    			MultiValueMap<String, Object> mtsParam = new LinkedMultiValueMap<>();
    			
//    			String[] param = tmplCd.split("/#/");
//    			String atalkId = param[0];
    			mtsParam.add("senderKey", mjsonParams.getString("SEND_PROFILE_KEY"));
    			mtsParam.add("templateCode", tmplCd);
    			Map<String, String> result = sendSmsService.mtsAtalkTmplCancelRequest(mtsParam);
    			resultList.add(result);
    			
//    			if("200".equals(result.get("code"))){
//    				JSONObject paramObject = paramArray.getJSONObject(0);
//    				// 검수상태 REQ로 변경
//    				paramObject.put("ATALK_ID", atalkId);
//    				paramObject.put("TMPL_IGI_STTS", "REQ");
//    				twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateAtalkIgiStts", mjsonParams);
//    			}else{
    			if(!"200".equals(result.get("code"))){
    				isSuccess = false;
    			}
    			
    			JSONObject paramObject = paramArray.getJSONObject(0);
    			paramObject.put("TMPL_CD", tmplCd);
    			paramObject.put("MTS_RESULT", result);
    			twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateAtalkIgiStts", mjsonParams);
    		}
    	}
    	TelewebJSON result = new TelewebJSON();
    	JSONArray resultArray = result.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject resultObject = resultArray.getJSONObject(0);
    	resultObject.put("code", isSuccess?"200":"300");
    	resultObject.put("result", resultList);
    	resultArray.add(resultObject);
    	
    	return result;
    }
    
    public TelewebJSON mtsCancelApproval(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	boolean isSuccess = true;	// 요청 성공 여부
    	List<String> tmplCdList = new ArrayList<>();
    	JSONArray paramArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	for (int n = 0; n < paramArray.size(); n++) {
    		JSONObject objData = paramArray.getJSONObject(n);
    		
    		@SuppressWarnings("rawtypes")
    		Iterator it = objData.keys();
    		while(it.hasNext()) {
    			String strKey = (String) it.next();
    			String strValue = objData.getString(strKey);
    			
    			if("TMPL_CD_LIST".equals(strKey)) {			// 알림톡 아이디+템플릿코드 리스트
    				tmplCdList.add(strValue);
    			}
    		}
    	}
    	
    	List<Map<String, String>> resultList = new ArrayList<>();
    	if(tmplCdList.size() > 0) {
    		for(String tmplCd : tmplCdList) {
    			MultiValueMap<String, Object> mtsParam = new LinkedMultiValueMap<>();
    			
    			mtsParam.add("senderKey", mjsonParams.getString("SEND_PROFILE_KEY"));
    			mtsParam.add("templateCode", tmplCd);
    			Map<String, String> result = sendSmsService.mtsCancelApproval(mtsParam);
    			resultList.add(result);
    			
    			if(!"200".equals(result.get("code"))){
    				isSuccess = false;
    			}
    			
    			JSONObject paramObject = paramArray.getJSONObject(0);
    			paramObject.put("TMPL_CD", tmplCd);
    			paramObject.put("MTS_RESULT", result);
    			twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateAtalkIgiStts", mjsonParams);
    		}
    	}
    	TelewebJSON result = new TelewebJSON();
    	JSONArray resultArray = result.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject resultObject = resultArray.getJSONObject(0);
    	resultObject.put("code", isSuccess?"200":"300");
    	resultObject.put("result", resultList);
    	resultArray.add(resultObject);
    	
    	return result;
    }
    
    public TelewebJSON mtsStop(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	boolean isSuccess = true;	// 요청 성공 여부
    	List<String> tmplCdList = new ArrayList<>();
    	JSONArray paramArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	for (int n = 0; n < paramArray.size(); n++) {
    		JSONObject objData = paramArray.getJSONObject(n);
    		
    		@SuppressWarnings("rawtypes")
    		Iterator it = objData.keys();
    		while(it.hasNext()) {
    			String strKey = (String) it.next();
    			String strValue = objData.getString(strKey);
    			
    			if("TMPL_CD_LIST".equals(strKey)) {			// 알림톡 아이디+템플릿코드 리스트
    				tmplCdList.add(strValue);
    			}
    		}
    	}
    	
    	List<Map<String, String>> resultList = new ArrayList<>();
    	if(tmplCdList.size() > 0) {
    		for(String tmplCd : tmplCdList) {
    			MultiValueMap<String, Object> mtsParam = new LinkedMultiValueMap<>();
    			
    			mtsParam.add("senderKey", mjsonParams.getString("SEND_PROFILE_KEY"));
    			mtsParam.add("templateCode", tmplCd);
    			Map<String, String> result = sendSmsService.mtsStop(mtsParam);
    			resultList.add(result);
    			
    			if(!"200".equals(result.get("code"))){
    				isSuccess = false;
    			}
    			
    			JSONObject paramObject = paramArray.getJSONObject(0);
    			paramObject.put("TMPL_CD", tmplCd);
    			paramObject.put("MTS_RESULT", result);
    			twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateAtalkIgiStts", mjsonParams);
    		}
    	}
    	TelewebJSON result = new TelewebJSON();
    	JSONArray resultArray = result.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject resultObject = resultArray.getJSONObject(0);
    	resultObject.put("code", isSuccess?"200":"300");
    	resultObject.put("result", resultList);
    	resultArray.add(resultObject);
    	
    	return result;
    }
    
    public TelewebJSON mtsReuse(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	boolean isSuccess = true;	// 요청 성공 여부
    	List<String> tmplCdList = new ArrayList<>();
    	JSONArray paramArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	for (int n = 0; n < paramArray.size(); n++) {
    		JSONObject objData = paramArray.getJSONObject(n);
    		
    		@SuppressWarnings("rawtypes")
    		Iterator it = objData.keys();
    		while(it.hasNext()) {
    			String strKey = (String) it.next();
    			String strValue = objData.getString(strKey);
    			
    			if("TMPL_CD_LIST".equals(strKey)) {			// 알림톡 아이디+템플릿코드 리스트
    				tmplCdList.add(strValue);
    			}
    		}
    	}
    	
    	List<Map<String, String>> resultList = new ArrayList<>();
    	if(tmplCdList.size() > 0) {
    		for(String tmplCd : tmplCdList) {
    			MultiValueMap<String, Object> mtsParam = new LinkedMultiValueMap<>();
    			
    			mtsParam.add("senderKey", mjsonParams.getString("SEND_PROFILE_KEY"));
    			mtsParam.add("templateCode", tmplCd);
    			Map<String, String> result = sendSmsService.mtsReuse(mtsParam);
    			resultList.add(result);
    			
    			if(!"200".equals(result.get("code"))){
    				isSuccess = false;
    			}
    			
    			JSONObject paramObject = paramArray.getJSONObject(0);
    			paramObject.put("TMPL_CD", tmplCd);
    			paramObject.put("MTS_RESULT", result);
    			twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateAtalkIgiStts", mjsonParams);
    		}
    	}
    	TelewebJSON result = new TelewebJSON();
    	JSONArray resultArray = result.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject resultObject = resultArray.getJSONObject(0);
    	resultObject.put("code", isSuccess?"200":"300");
    	resultObject.put("result", resultList);
    	resultArray.add(resultObject);
    	
    	return result;
    }
    
    public TelewebJSON mtsRelease(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	boolean isSuccess = true;	// 요청 성공 여부
    	List<String> tmplCdList = new ArrayList<>();
    	JSONArray paramArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
    	for (int n = 0; n < paramArray.size(); n++) {
    		JSONObject objData = paramArray.getJSONObject(n);
    		
    		@SuppressWarnings("rawtypes")
    		Iterator it = objData.keys();
    		while(it.hasNext()) {
    			String strKey = (String) it.next();
    			String strValue = objData.getString(strKey);
    			
    			if("TMPL_CD_LIST".equals(strKey)) {			// 알림톡 아이디+템플릿코드 리스트
    				tmplCdList.add(strValue);
    			}
    		}
    	}
    	
    	List<Map<String, String>> resultList = new ArrayList<>();
    	if(tmplCdList.size() > 0) {
    		for(String tmplCd : tmplCdList) {
    			MultiValueMap<String, Object> mtsParam = new LinkedMultiValueMap<>();
    			
    			mtsParam.add("senderKey", mjsonParams.getString("SEND_PROFILE_KEY"));
    			mtsParam.add("templateCode", tmplCd);
    			Map<String, String> result = sendSmsService.mtsRelease(mtsParam);
    			resultList.add(result);
    			
    			if(!"200".equals(result.get("code"))){
    				isSuccess = false;
    			}
    			
    			JSONObject paramObject = paramArray.getJSONObject(0);
    			paramObject.put("TMPL_CD", tmplCd);
    			paramObject.put("MTS_RESULT", result);
    			twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "updateAtalkIgiStts", mjsonParams);
    		}
    	}
    	TelewebJSON result = new TelewebJSON();
    	JSONArray resultArray = result.getDataObject(TwbCmmnConst.G_DATA);
    	JSONObject resultObject = resultArray.getJSONObject(0);
    	resultObject.put("code", isSuccess?"200":"300");
    	resultObject.put("result", resultList);
    	resultArray.add(resultObject);
    	
    	return result;
    }
    
    /**
     * 알림톡 채널 카테고리 리스트 가져오기
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON getAtalkCat(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON result = new TelewebJSON();
    	
    	//mts로 보낼 파라미터
		Map<String, Object> mtsParameter = new HashMap<>();
		//mts에서 받은 결과값
		Map<String, Object> mtsResult = null;
		//결과값을 string 형으로 변환
		String mtsResultToString = "";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		String url = this._API_ATALK_URL + "/category/all?authCode=" + this._API_AUTH_CODE;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI endUri = builder.build().toUri();
        
        
		try {
			ObjectMapper mapper = new ObjectMapper();
			String mtsParamString = mapper.writeValueAsString(mtsParameter);
			
			RestTemplate restTemplate = new RestTemplate();
			
			HttpEntity<?> requestEntity = new HttpEntity<>(mtsParamString, headers);
			log.info("url" + endUri);
			ResponseEntity<Map> response = restTemplate.exchange(endUri, HttpMethod.POST, requestEntity, Map.class);
			mtsResult = response.getBody();
			
			mtsResultToString = mapper.writeValueAsString(mtsResult);
			
			result.setString("mtsResult", mtsResultToString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return result;
    }
    
    /**
     * 알림톡 채널 관리자 인증번호 발급
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON getToken(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON result = new TelewebJSON();
    	
    	//mts로 보낼 파라미터
		Map<String, Object> mtsParameter = new HashMap<>();
		//mts에서 받은 결과값
		Map<String, Object> mtsResult = null;
		//결과값을 string 형으로 변환
		String mtsResultToString = "";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		String url = this._API_ATALK_URL + "/sender/token?authCode=" + this._API_AUTH_CODE
				+ "&yellowId=" + mjsonParams.getString("UUID") + "&phoneNumber=" + mjsonParams.getString("MNG_PHN_NO");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI endUri = builder.build().toUri();
        
        
		try {
			ObjectMapper mapper = new ObjectMapper();
			String mtsParamString = mapper.writeValueAsString(mtsParameter);
			
			RestTemplate restTemplate = new RestTemplate();
			
			HttpEntity<?> requestEntity = new HttpEntity<>(mtsParamString, headers);
			log.info("url" + endUri);
			ResponseEntity<Map> response = restTemplate.exchange(endUri, HttpMethod.POST, requestEntity, Map.class);
			mtsResult = response.getBody();
			
			mtsResultToString = mapper.writeValueAsString(mtsResult);
			
			result.setString("mtsResult", mtsResultToString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return result;
    }
    
    /**
     * 알림톡 채널 발급
     *
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON getAtalkChn(TelewebJSON mjsonParams) throws TelewebAppException
    {
    	TelewebJSON result = new TelewebJSON();

        // mts로 보낼 파라미터
        MultiValueMap<String, Object> mtsParameter = new LinkedMultiValueMap<>();
        // mts에서 받은 결과값
        Map<String, Object> mtsResult = null;
        // 결과값을 string 형으로 변환
        String mtsResultToString = "";

        mtsParameter.add("authCode", this._API_AUTH_CODE);
        mtsParameter.add("token", mjsonParams.getString("TOKEN"));
        mtsParameter.add("phoneNumber", mjsonParams.getString("MNG_PHN_NO"));
        mtsParameter.add("yellowId", mjsonParams.getString("UUID"));
        mtsParameter.add("categoryCode", mjsonParams.getString("CAT"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Charset", "UTF-8");

        String url = this._API_ATALK_URL + "/create/new/senderKey";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI endUri = builder.build().toUri();

        try {
            RestTemplate restTemplate = new RestTemplate();
            
            // HttpEntity에 mtsParameter 추가
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(mtsParameter, headers);

            log.info("url >> " + endUri);
            log.info("requestEntity >> " + requestEntity);
            // POST 방식으로 요청
            ResponseEntity<Map> response = restTemplate.exchange(endUri, HttpMethod.POST, requestEntity, Map.class);
            mtsResult = response.getBody();

            ObjectMapper mapper = new ObjectMapper();
            mtsResultToString = mapper.writeValueAsString(mtsResult);

            result.setString("mtsResult", mtsResultToString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
    
}
