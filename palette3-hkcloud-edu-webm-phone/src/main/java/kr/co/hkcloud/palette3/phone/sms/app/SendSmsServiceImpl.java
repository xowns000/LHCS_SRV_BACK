package kr.co.hkcloud.palette3.phone.sms.app;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.common.code.app.CodeCmmnService;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.mts.app.MtsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("sendSmsService")
public class SendSmsServiceImpl implements SendSmsService
{
    private final CodeCmmnService codeCmmnService;
    private String commCodeTranPattern = "##\\{(.+?)\\}##";
    private final MtsService mtsService;
    
	private static String _REPOSITORY;

    @Value("${spring.app.palette.repository-root}")
    public void setKey(String repository) {
    	_REPOSITORY = repository;
    }
    
	private final String _API_AUTH_CODE = "6R5XPUIkrGrZUFuCTlLjwQ==";
	private final String _API_URL = "https://api.mtsco.co.kr";
	private static final String _API_ATALK_URL = "https://talks.mtsco.co.kr/mts/api";
	
//	private static final String _API_SEND_SMS_ONE = "/sndng/sms/sendMessage";
	private static final String _API_SEND_SMS_MULTI = "/sndng/sms/sendMessages";
//	private static final String _API_SEND_MMS_ONE = "/sndng/mms/sendMessage";
	private static final String _API_SEND_MMS_MULTI = "/sndng/mms/sendMessages";
	private static final String _API_SEND_ATALK_MULTI = "/sndng/atk/sendMessages";
	private static final String _API_FILE_UPLOAD_PATH = "/img/upload_image";
	
	private static final String _API_ATALK_FILE_UPLOAD_PATH = "/image/alimtalk/";
	
	private static final String _FILE_PATH = File.separator + "web";
	
	private final String _BASE_PHN_NO = "07071730397";
	
	private final TwbComDAO twbComDao;
	private final InnbCreatCmmnService innbCreatCmmnService;
	
	/**
	 * 1. 문자 발송 파라미터 추출
	 * 2. MTS 요청 파라미터 생성
	 * 3. 문자 발송 요청
	 * 4. 이력 저장
	 */
	public TelewebJSON sendSMS(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException
	{
		TelewebJSON result = new TelewebJSON();
		result.setString("DATA", this.extractParameter(mjsonParams));
		
		return result;
	}
	
	/**
	 * 1. 문자 발송 파라미터 추출
	 * 2. MTS 요청 파라미터 생성
	 * 3. 문자 발송 요청
	 * 4. 이력 저장
	 */
	public TelewebJSON sendAtalk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException
	{
		TelewebJSON returnValue = new TelewebJSON();
		JSONArray paramArray = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
		List<String> phoneNumberList = new ArrayList<>();
		List<String> svyTrgtList = new ArrayList<>();
		List<String> contentsList = new ArrayList<>();
		List<String> transContentsList = new ArrayList<>();
		
		String atalkId = mjsonParams.getString("ATALK_ID");
		String callbackNumber = mjsonParams.getString("CALLBACK_NUMBER");
		String sendDate = mjsonParams.getString("SEND_DATE");
		int sendIntervalPeople = mjsonParams.getInt("SEND_INTERVAL_PEOPLE");
		int sendIntervalMin = mjsonParams.getInt("SEND_INTERVAL_MIN");
		
		String transMessage = mjsonParams.getString("TRANS_MESSAGE");
		String tenantId = mjsonParams.getString("TENANT_ID");
		String custcoId = mjsonParams.getString("CUSTCO_ID");
		String userId = mjsonParams.getString("USER_ID");
		String tranSendYn = mjsonParams.getString("TRAN_SEND");
		String mtsResult = "";
		String senderKey = "";
		
		//발신번호 유형 - DEFAULT : 기본(발신번호 항목 값으로 전송), ##{참여자_항목_ID,PLT_COMM_CD.GROUP_CD_ID}## : 참여자 항목_ID를 key로하는 공통코드의 값(전화번호) 설정 (ex : ##{CUTT_TYPE_1,CENTER_TEL}##)
        //LH주거복지정보 사업에서 추가됨. by hjh.
		String envSrvyDsptchNoTp = mjsonParams.getString("ENV_SRVY_DSPTCH_NO_TP");
		
		//대량발송 그룹키 채번
		String mtsSndngHstryGroupId = Integer.toString(innbCreatCmmnService.createSeqNo("MTS_SNDNG_HSTRY_GROUP_ID"));
		
    	for (int n = 0; n < paramArray.size(); n++) {
        	JSONObject objData = paramArray.getJSONObject(n);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);
                
                if("PHONE_NUMBER".equals(strKey)) {			// 알림톡 발송 대상 전화번호 목록
                	phoneNumberList.add(strValue.replaceAll("-", ""));
                }
                
                if("SRVY_TRGT_ID".equals(strKey)) {			// 알림톡 발송 대상 설문 참여ID
                	svyTrgtList.add(strValue);
                }
                
                if("CONTENTS".equals(strKey)) {			// 알림톡 발송 메시지 변수 리스트
                	contentsList.add(strValue);
                }
                
                if("TRANS_CONTENTS".equals(strKey)) {			// 알림톡 발송 메시지 변수 전환 문구 리스트
                	transContentsList.add(strValue);
                }
            }
        }
    	log.debug("atalkId = " + atalkId + ", callbackNumber = " + callbackNumber
    			+ ", sendDate = " + sendDate
    			+ ", transMessage = " + transMessage
    			+ ", phoneNumberList = " + phoneNumberList.size());
    	
    	List<String> atalkIdList = new ArrayList<>();
		atalkIdList.add(atalkId);
		mjsonParams.setObject("ATALK_ID_LIST", 0, atalkIdList);
    	TelewebJSON atalkTmplList = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.PhoneAtalkTmplManageMapper", "selectAtalkTmplForMtsRegister", mjsonParams);
    	String tmplCd = atalkTmplList.getString("TEMPLATE_CODE");
    	
    	List<Map<String, Object>> mtsParamList = new ArrayList<>();    	
    	for(int i = 0; i < phoneNumberList.size(); i++) {
    		Map<String, Object> mtsParam = new HashMap<>();
    		mtsParam.put("sender_key", atalkTmplList.getString("SENDER_KEY"));
    		senderKey = atalkTmplList.getString("SENDER_KEY");
    		if( i > 0 && i % sendIntervalPeople == 0) {
    		    sendDate = incrementDateTime(sendDate, sendIntervalMin);
    		}
    		mtsParam.put("send_date", sendDate);
    		
    		//발신번호 변환 - envSrvyDsptchNoTp 이 빈값 또는 DEFAULT가 아닐 시, 
            if(StringUtils.isNotEmpty(envSrvyDsptchNoTp) && !"DEFAULT".equals(envSrvyDsptchNoTp)) {
                mtsParam.put("callback_number", tranCallbackNumberAtalk(envSrvyDsptchNoTp, callbackNumber, contentsList, transContentsList, i, custcoId));
            } else {
                mtsParam.put("callback_number", callbackNumber);
            }
    		
    		mtsParam.put("phone_number", phoneNumberList.get(i));
    		mtsParam.put("add_etc1", tenantId);
    		mtsParam.put("add_etc2", custcoId);
    		mtsParam.put("add_etc3", mtsSndngHstryGroupId);
    		
    		if(svyTrgtList.size() != 0) {
    			mtsParam.put("svyTrgt", svyTrgtList.get(i));
    		}

    		String tmplMsg = atalkTmplList.getString("TEMPLATE_CONTENT");
    		//템플릿의 변환 문자를 실제 값으로 변환한다.
    		String tranMsg = tranContentAtalk(tmplMsg, contentsList, transContentsList, i, custcoId);
    		
    		
    		mtsParam.put("template_code", tmplCd);
    		mtsParam.put("message", tranMsg);
    		if("TEXT".equals(atalkTmplList.getString("TEMPLATE_EMPHASIZE_TYPE"))) {
    			mtsParam.put("title", atalkTmplList.getString("TEMPLATE_TITLE"));
    		}else if("IMAGE".equals(atalkTmplList.getString("TEMPLATE_EMPHASIZE_TYPE"))
    				|| "ITEM_LIST".equals(atalkTmplList.getString("TEMPLATE_EMPHASIZE_TYPE"))) {
    			mtsParam.put("imageFlag", "Y");
    		}
    		String buttonString = atalkTmplList.getString("BUTTONS");
    		if(!"".equals(buttonString)) {
    			try {
    				ObjectMapper mapper = new ObjectMapper();
	    			Map<String, Object> buttonList = new HashMap<>();
	    			
	    			List<Map<String, String>> buttons = 
	    					mapper.readValue(buttonString, new TypeReference<List<Map<String, String>>>(){});
	    			
	    			for(Map<String, String> button : buttons) {
	    				switch(button.get("linkType")) {
	    					case "WL" : 
	    						button.put("type", button.get("linkType"));
	    						button.put("url_mobile", button.get("linkMo"));
	    						button.put("url_pc", button.get("linkPc"));
	    						
	    						button.remove("linkType");
	    						button.remove("linkMo");
	    						button.remove("linkPc");
	    						button.remove("ordering");
	    						break;
	    					case "AL" : 
	    						button.put("type", button.get("linkType"));
	    						button.put("scheme_android", button.get("linkAnd"));
	    						button.put("scheme_ios", button.get("linkIos"));
	    						button.put("url_mobile", button.get("linkMo"));
	    						button.put("url_pc", button.get("linkPc"));
	    						
	    						button.remove("linkType");
	    						button.remove("linkAnd");
	    						button.remove("linkIos");
	    						button.remove("linkMo");
	    						button.remove("linkPc");
	    						button.remove("ordering");
	    						break;
	    					case "BK" : 
	    						button.put("type", button.get("linkType"));
	    						button.put("chat_extra", button.get("name"));
	    						
	    						button.remove("linkType");
	    						button.remove("ordering");
	    						break;
	    					case "MD" : 
	    						button.put("type", button.get("linkType"));
	    						button.put("chat_event", button.get("name"));
	    						
	    						button.remove("linkType");
	    						button.remove("ordering");
	    						break;
	    					case "AC" : 
	    						button.put("type", button.get("linkType"));
	    						button.put("name", "채널 추가");
	    						
	    						button.remove("linkType");
	    						button.remove("ordering");
	    						break;
	    					default :
	    						button.put("type", button.get("linkType"));
	    						
	    						button.remove("linkType");
	    						button.remove("ordering");
	    						break;
	    				}
	    			}
	    			
	    			buttonList.put("button", buttons);
	    			mtsParam.put("attachment", buttonList);
    			}catch(Exception e) {
    				log.debug(e.getMessage());
    			}
    		}
    		String tranType = "N", tranMessage = "";

    		//발송번호가 없다면 전화번호를 기본전화번호로 넣어주고 전환전송하지 않는다
    		//기본번호는 팔레트 개발용 전화번호
    		if(mtsParam.get("callback_number").equals("")) {
    			mtsParam.put("callback_number", this._BASE_PHN_NO);
    		} else {
    		    if("Y".equals(tranSendYn)) {
    		        if(!"".equals(transMessage)) {
                        int transMessageSize = 0;
                        try {
                            transMessageSize = this.replaceString(transMessage).getBytes("euc-kr").length;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if(transMessageSize > 90) {
                            tranType = "L";
                        }else {
                            tranType = "S";
                        }
                        tranMessage = transMessage;
                    }else {
                        int transMessageSize = 0;
                        try {
                            transMessageSize = 
                                    this.replaceString(tranMsg)
                                            .getBytes("euc-kr").length;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if(transMessageSize > 90) {
                            tranType = "L";
                        }else {
                            tranType = "S";
                        }
                        tranMessage = tranMsg;
                    }
    		    }
    		}
    		
    		mtsParam.put("tran_type", tranType);
    		mtsParam.put("tran_message", tranMessage);

    		mtsParamList.add(mtsParam);
    	}
    	
    	Map<String, Object> mtsParameter = new HashMap<>();
    	mtsParameter.put("auth_code", this._API_AUTH_CODE);
    	mtsParameter.put("data", mtsParamList);
    	
		mtsParameter.put("add_etc1", tenantId);
		mtsParameter.put("add_etc2", custcoId);
		mtsParameter.put("add_etc3", mtsSndngHstryGroupId);
		mtsParameter.put("sender_key", senderKey);
    	
    	// mts 알림톡 발송 호출
    	try {
			HttpHeaders headers = new HttpHeaders();
    		headers.add("Content-Type", "application/json");
    		
            String url = this._API_URL + _API_SEND_ATALK_MULTI;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
            URI endUri = builder.build().toUri();
            
			ObjectMapper mapper = new ObjectMapper();
			String mtsParamString = mapper.writeValueAsString(mtsParameter);
//			log.debug("mtsParam = " + mtsParam);
			
			log.debug("mtsParamString = " + mtsParamString);
            Map<String, String> result = this.apiCall(headers, endUri, mtsParamString);
            
            mtsResult = mapper.writeValueAsString(result);
			returnValue.setString("DATA", mtsResult);
			
			log.debug("mtsResult = " + mtsResult);
			
			for(int i = 0; i < mtsParamList.size(); i++) {
				// 발송 이력 ID 채번
				String mtsSndngHstryId = Integer.toString(innbCreatCmmnService.createSeqNo("MTS_SNDNG_HSTRY_ID"));
				log.debug("mtsSndngHstryId = " + mtsSndngHstryId);
				
				String svyTrgt = "";
				if(mtsParamList.get(i).get("svyTrgt") != null) {
					svyTrgt = mtsParamList.get(i).get("svyTrgt").toString();
				}
				
				TelewebJSON objParam = this.makeSaveHistoryParameter(
						mtsSndngHstryId
						,"ATALK"
						, mtsParamList.get(i).get("phone_number").toString()
						, mtsParamList.get(i).get("callback_number").toString()
						, mtsParamList.get(i).get("message").toString()
                        , result.get("code")
                        , result.get("message") + "||group:" + mtsSndngHstryGroupId + "||senderKey:" + senderKey + "||tmplCd:" + tmplCd
                        //, result.get("received_at").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "")
                        , mtsParamList.get(i).get("send_date").toString().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "")
                        , userId
                        , custcoId
                        , svyTrgt);
				twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.SendSmsMapper", "saveSendHistory", objParam);
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	
    	return returnValue;
	}
	
	private String replaceString(String message) {
		return message.replaceAll("(\r\n|\r|\n|\n\r)", " ");
	}
	
	/**
	 * 1. 문자 발송 파라미터 추출
	 * 2. MTS 요청 파라미터 생성
	 * 3. 문자 발송 요청
	 * 4. 이력 저장
	 */
	@SuppressWarnings("static-access")
	public Map<String, String> mtsAtalkTmplRegister(MultiValueMap<String, Object> atalkTmp) throws TelewebAppException
	{
//		String resultCode = "";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		String url = this._API_ATALK_URL+ "/create/template";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI endUri = builder.build().toUri();
        
//        int successCount = 0, failCount = 0;
//        for(MultiValueMap<String, String> atalkTmpl : atalkTmplList) {
        log.debug("atalkTmp = " + atalkTmp);
			Map<String, String> result = this.apiCall(headers, endUri, atalkTmp);
			
//			if("200".equals(code)) {
//				successCount++;
//			}else {
//				failCount++;
//			}
//        }
        
        log.debug("result = " + result);
//        if(successCount > 0 && failCount == 0) {
//        	// 모두 성공
//        	resultCode = "200";
//        }else if(successCount == 0 && failCount > 0) {
//        	// 모두 실패
//        	resultCode = "300";
//        }else {
//        	// 일부 성공
//        	resultCode = "201";
//        }
        return result;
	}
	
	public Map<String, String> mtsAtalkTmplRequest(MultiValueMap<String, Object> atalkTmp) throws TelewebAppException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		@SuppressWarnings("static-access")
		String url = this._API_ATALK_URL+ "/template/request";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI endUri = builder.build().toUri();
        
        log.debug("atalkTmp = " + atalkTmp);
		Map<String, String> result = this.apiCall(headers, endUri, atalkTmp);
		log.debug("result = " + result);
		return result;
	}
	
	public Map<String, String> mtsAtalkTmplCancelRequest(MultiValueMap<String, Object> atalkTmp) throws TelewebAppException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		@SuppressWarnings("static-access")
		String url = this._API_ATALK_URL+ "/template/cancel_request";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		URI endUri = builder.build().toUri();
		
		log.debug("atalkTmp = " + atalkTmp);
		Map<String, String> result = this.apiCall(headers, endUri, atalkTmp);
		log.debug("result = " + result);
		return result;
	}
	
	public Map<String, String> mtsCancelApproval(MultiValueMap<String, Object> atalkTmp) throws TelewebAppException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		@SuppressWarnings("static-access")
		String url = this._API_ATALK_URL+ "/template/cancel_approval";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		URI endUri = builder.build().toUri();
		
		log.debug("atalkTmp = " + atalkTmp);
		Map<String, String> result = this.apiCall(headers, endUri, atalkTmp);
		log.debug("result = " + result);
		return result;
	}
	
	public Map<String, String> mtsStop(MultiValueMap<String, Object> atalkTmp) throws TelewebAppException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		@SuppressWarnings("static-access")
		String url = this._API_ATALK_URL+ "/template/stop";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		URI endUri = builder.build().toUri();
		
		log.debug("atalkTmp = " + atalkTmp);
		Map<String, String> result = this.apiCall(headers, endUri, atalkTmp);
		log.debug("result = " + result);
		return result;
	}
	
	public Map<String, String> mtsReuse(MultiValueMap<String, Object> atalkTmp) throws TelewebAppException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		@SuppressWarnings("static-access")
		String url = this._API_ATALK_URL+ "/template/reuse";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		URI endUri = builder.build().toUri();
		
		log.debug("atalkTmp = " + atalkTmp);
		Map<String, String> result = this.apiCall(headers, endUri, atalkTmp);
		log.debug("result = " + result);
		return result;
	}
	
	public Map<String, String> mtsRelease(MultiValueMap<String, Object> atalkTmp) throws TelewebAppException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		@SuppressWarnings("static-access")
		String url = this._API_ATALK_URL+ "/template/release";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		URI endUri = builder.build().toUri();
		
		log.debug("atalkTmp = " + atalkTmp);
		Map<String, String> result = this.apiCall(headers, endUri, atalkTmp);
		log.debug("result = " + result);
		return result;
	}
	
	public Map<String, Object> mtsModify(MultiValueMap<String, Object> atalkTmp) throws TelewebAppException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		@SuppressWarnings("static-access")
		String url = this._API_ATALK_URL+ "/modify/template";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
		URI endUri = builder.build().toUri();
		
		log.debug("atalkTmp = " + atalkTmp);
		Map<String, Object> result = this.apiCallForStatus(headers, endUri, atalkTmp);
		log.debug("result = " + result);
		return result;
	}
	
	public Map<String, Object> mtsAtalkTmplStatus(MultiValueMap<String, Object> atalkTmp) throws TelewebAppException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		@SuppressWarnings("static-access")
		String url = this._API_ATALK_URL+ "/state/template";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI endUri = builder.build().toUri();
        
        log.debug("atalkTmp = " + atalkTmp);
		Map<String, Object> result = this.apiCallForStatus(headers, endUri, atalkTmp);
		log.debug("result = " + result);
		return result;
	}
	
	/**
	 * MTS 요청 파라미터 추출
	 * @param mjsonParams
	 * @throws TelewebAppException
	 */
	private String extractParameter(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException
	{
		JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
		
		// 추출 파라미터 Map
		Map<String, Object> essenceParameter = new HashMap<>();
		// 발송 문자 수신 대상자 리스트
//		List<String> phoneNumberArray = new ArrayList<String>();
		// 발송자 전화번호
//		String callbackNumber = "";
		// 발송 메시지
		String message = mjsonParams.getString("MESSAGE");
		// 발송 요청 일자
		String sendDate = "";
		int sendIntervalPeople = mjsonParams.getInt("SEND_INTERVAL_PEOPLE");
        int sendIntervalMin = mjsonParams.getInt("SEND_INTERVAL_MIN");
		// 고객사 ID
		String custcoId = mjsonParams.getString("CUSTCO_ID");
		// 테넌트 ID
		String tenantId = "";
		// 사용자(발송자) ID
		String userId = "";
		// 첨부파일 그룹키
		String fileGroupKey = "";
		// 문자 제목
		String subject = mjsonParams.getString("SUBJECT");
		
		//발신번호 유형 - DEFAULT : 기본(발신번호 항목 값으로 전송), ##{참여자_항목_ID,PLT_COMM_CD.GROUP_CD_ID}## : 참여자 항목_ID를 key로하는 공통코드의 값(전화번호) 설정 (ex : ##{CUTT_TYPE_1,CENTER_TEL}##)
		//LH주거복지정보 사업에서 추가됨. by hjh.
		String envSrvyDsptchNoTp = mjsonParams.getString("ENV_SRVY_DSPTCH_NO_TP");
		
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, String>> smsParam = new ArrayList<>();
		for (int n = 0; n < jsonObj.size(); n++) {
        	JSONObject objData = jsonObj.getJSONObject(n);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);
                
                // 수신 대상 전화번호
//                if("PHONE_NUMBER".equals(strKey)) {
//                	phoneNumberArray.add(strValue.replaceAll("-", ""));
//                }
                
                // 발송자 전화번호
//                if("CALLBACK_NUMBER".equals(strKey)) {
//                	callbackNumber = strValue.replaceAll("-", "");
//                }
                
                // 발송 메세지
//                if("MESSAGE".equals(strKey)) {
//                	message = strValue;
//                }
                
                // 발송 요청 일자
                if("SEND_DATE".equals(strKey)) {
                	log.debug("SEND_DATE = " + strValue);
                	sendDate = strValue;
                }
                
                // 고객사
                if("CUSTCO_ID".equals(strKey)) {
                	custcoId = strValue;
                }
                
                // 고객사
                if("TENANT_ID".equals(strKey)) {
                	tenantId = strValue;
                }
                
                // 발송 요청자
                if("USER_ID".equals(strKey)) {
                	userId = strValue;
                }
                
                // 첨부파일 그룹키
                if("FILE_GROUP_KEY".equals(strKey)) {
                	fileGroupKey = strValue;
                }
                
                if("SMS_PARAM".equals(strKey)) {
                	// 배열 문자 치환
                	String itemString = 
                			strValue.replace("&#91;", "[").replace("&#93;", "]");
                	try {
                		// json string to list map
	                	List<Map<String, String>> itemsMap = 
	                			mapper.readValue(
	                					itemString
	                					, new TypeReference<List<Map<String, String>>>(){});
	                	
	                	if(itemsMap.size() > 0) {
	                		for(int i=0;i<itemsMap.size();i++) {
	                			Map<String, String> item = itemsMap.get(i);
		                		String cdCnt = mjsonParams.getString("CD_CNT");
		                		
		                		//메시지 치환 처리
		                		String tranMessage = tranContentSMS(message, item, cdCnt, custcoId);
		                		
		                		//발신번호 변환 - envSrvyDsptchNoTp 이 빈값 또는 DEFAULT가 아닐 시, 
		                		if(StringUtils.isNotEmpty(envSrvyDsptchNoTp) && !"DEFAULT".equals(envSrvyDsptchNoTp)) {
		                		    item.replace("callback_number", tranCallbackNumberSMS(envSrvyDsptchNoTp, item.get("callback_number"), item, custcoId));
		                		}
		                		
	                			item.put("message",tranMessage);
	                			item.put("subject",subject);
	                			
		                		smsParam.add(item);
	                		}
	                		
//	                		log.info("itemsMap"+itemsMap);
//	                		smsParam.addAll(itemsMap);
	                	}
                	}catch(Exception e) {
                		log.debug(e.getMessage());
                	}
                }
            }
        }
//		essenceParameter.put("phone_number", phoneNumberArray);
//		essenceParameter.put("callback_number", callbackNumber);
//		essenceParameter.put("message", message);
		essenceParameter.put("smsParam", smsParam);
		essenceParameter.put("send_date", sendDate);
		essenceParameter.put("sendIntervalPeople", sendIntervalPeople);
		essenceParameter.put("sendIntervalMin", sendIntervalMin);
		essenceParameter.put("tenantId", tenantId);
		essenceParameter.put("custcoId", custcoId);
		essenceParameter.put("userId", userId);
		essenceParameter.put("fileGroupKey", fileGroupKey);
		
		return this.makeMtsCallParameter(essenceParameter);
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	private String makeMtsCallParameter(Map<String, Object> essenceParameter) throws TelewebAppException
	{
		// api 요청 분기 url
		String apiPath = "";
		// 발송 type, MMS/LMS/SMS
		String sendType = "";
		// MMS, LMS 여부
		boolean isMms = false, isLms = false;
		
		// 첨부파일 디렉토리 경로
		String[] filePath = null;
		// 수신자 번호
//		List<String> phoneNumberList = (List<String>)essenceParameter.get("phone_number");
		// 발신자 번호
//		String callbackNumber = (String)essenceParameter.get("callback_number");
		// 발신 메시지
//		String message = (String)essenceParameter.get("message");
		// sms 정보
		List<Map<String, String>> smsParams = 
				(List<Map<String, String>>)essenceParameter.get("smsParam");
		
		// 발신 요청 일자
		String sendDate = (String)essenceParameter.get("send_date");
		int sendIntervalPeople = (Integer)essenceParameter.get("sendIntervalPeople");
		int sendIntervalMin = (Integer)essenceParameter.get("sendIntervalMin");
		log.debug("sendDate = " + sendDate);
		String tenantId = (String)essenceParameter.get("tenantId");
		String custcoId = (String)essenceParameter.get("custcoId");
		String userId = (String)essenceParameter.get("userId");
		// 첨부파일 그룹키
		String fileGroupKey = (String)essenceParameter.get("fileGroupKey");
		
		//대량발송 그룹키 채번
		String mtsSndngHstryGroupId = Integer.toString(innbCreatCmmnService.createSeqNo("MTS_SNDNG_HSTRY_GROUP_ID"));
		
		// 첨부파일 정보 목록
		List<Map<String, String>> attachedFile = new ArrayList<>();
		
		// 첨부파일 MTS 업로드 객체
		Map<String, List<Map<String, String>>> attachment = null;
		// MTS 업로드 객체 안의 파일 리스트
		List<Map<String, String>> imgUrlList = null;
		
		// api 요청 파라미터 객체
		Map<String, Object> mtsParameter = new HashMap<>();
		
		mtsParameter.put("auth_code", this._API_AUTH_CODE);
		mtsParameter.put("custcoId", custcoId);
		mtsParameter.put("userId", userId);
		
		// fileGroupKey로 첨부파일  조회
		if(!"".equals(fileGroupKey)) {
			attachedFile = this.selectAttachedFile(fileGroupKey);
			log.debug("attachedFile = " + attachedFile);
			log.debug("attachedFile = " + attachedFile.size());
			if(attachedFile.size() > 0) {
				filePath = new String[attachedFile.size()];
				
				for(int i = 0; i < attachedFile.size(); i++) {
					Map<String, String> imageFile = attachedFile.get(i);
					log.debug("TASK_TYPE_CD = " + imageFile.get("TASK_TYPE_CD"));
					log.debug("PATH_TYPE_CD = " + imageFile.get("PATH_TYPE_CD"));
					filePath[i] = "/" 
//								+ imageFile.get("PATH_TYPE_CD") + "/" 
//								+ imageFile.get("TASK_TYPE_CD") + "/"
								+ imageFile.get("FILE_PATH") + "/"
								+ imageFile.get("STRG_FILE_NM");
					log.debug("filePath = " + filePath[i]);
				}
				
				attachment = new HashMap<>();
				imgUrlList = new ArrayList<>();
				for(String path : filePath) {
					log.debug("path = " + path);
					Map<String, String> mtsFileUploadResult = this.mtsRequestFileUpload(path);
					log.debug("mtsFileUploadResult = " + mtsFileUploadResult.toString());
					log.debug("code = " + mtsFileUploadResult.get("code"));
					if("0000".equals(mtsFileUploadResult.get("code"))) {
						Map<String, String> imgUrl = new HashMap<>();
						log.debug("images = " + mtsFileUploadResult.get("image"));
						imgUrl.put("img_url", mtsFileUploadResult.get("image"));
						imgUrlList.add(imgUrl);
					}else {
						//  업로드 결과 코드 실패면 어떻게 할지 확인 필요
					}
				}
			}
		}
		/*
		if(attachedFile.size() > 0) {		// 첨부파일 있으면
			// MMS 발송
			sendType= "MMS";
		}else {
			// LMS or SMS
			int messageSize = message.getBytes().length;
			log.debug("message = " + message);
			log.debug("messageSize = " + messageSize);
			if(messageSize > 90) {
				sendType = "LMS";
			}else {
				sendType = "SMS";
			}
		}
		*/
//		log.debug("sendType = " + sendType);
		
		// SMS/LMS/MMS
//		mtsParameter.put("sendType", sendType);
		
//		if(phoneNumberList.size() > 1) {
			// 다건 발송
			List<Map<String, Object>> dataListMap = new ArrayList<>();
			
//			for(String phoneNumber : phoneNumberList) {
			int smsIdx = 0;
			for(Map<String, String> smsParam : smsParams) {
				
				Map<String, Object> dataMap = new HashMap<>();
				
				if(attachedFile.size() > 0) {		// 첨부파일 있으면
					// MMS 발송
//					dataMap.put("sendType", "MMS");
					if(!isMms) {
						isMms = true;
					}
				}else {
					// LMS or SMS
					String message = smsParam.get("message");
					int messageSize = 0;
					try {
						messageSize = this.replaceString(message).getBytes("euc-kr").length;
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
					if(messageSize > 90) {
//						dataMap.put("sendType", "LMS");
						if(!isLms) {
							isLms = true;
						}
					}else {
//						dataMap.put("sendType", "SMS");
					}
				}
				
				dataMap.put("phone_number", smsParam.get("phone_number"));
				dataMap.put("callback_number", smsParam.get("callback_number"));
				dataMap.put("message", smsParam.get("message"));
				dataMap.put("subject", smsParam.get("subject"));
				
				if(smsParam.get("svyTrgt") != null && !smsParam.get("svyTrgt").equals("")) {
					dataMap.put("svyTrgt", smsParam.get("svyTrgt"));
				}
				
				if(smsIdx > 0 && smsIdx % sendIntervalPeople == 0) {
				    sendDate = incrementDateTime(sendDate, sendIntervalMin);
				}
				dataMap.put("send_date", sendDate);
				
				// 첨부파일 존재
				if(filePath != null) {
					// 첨부파일 MTS 업로드
//					Map<String, List<Map<String, String>>> attachment = new HashMap<>();
//					List<Map<String, String>> imgUrlList = new ArrayList<>();
//					for(String path : filePath) {
//						Map<String, String> mtsFileUploadResult = this.mtsRequestFileUpload(path);
//						log.debug("mtsFileUploadResult = " + mtsFileUploadResult.toString());
//						log.debug("code = " + mtsFileUploadResult.get("code"));
//						if("0000".equals(mtsFileUploadResult.get("code"))) {
//							Map<String, String> imgUrl = new HashMap<>();
//							log.debug("images = " + mtsFileUploadResult.get("image"));
//							imgUrl.put("img_url", mtsFileUploadResult.get("image"));
//							imgUrlList.add(imgUrl);
//						}else {
//						}
//					}
					if(attachment != null) {
						attachment.put("image", imgUrlList);
						dataMap.put("attachment", attachment);
						log.debug("dataMap = " + dataMap);
					}
				}

				dataMap.put("add_etc1", tenantId);
				dataMap.put("add_etc2", custcoId);
				dataMap.put("add_etc3", mtsSndngHstryGroupId);
				
				dataListMap.add(dataMap);
				smsIdx ++;
			}
			if(isMms) {
				sendType = "MMS";
			}else if(isLms) {
				sendType = "LMS";
			}else {
				sendType = "SMS";
			}
			
			mtsParameter.put("sendType", sendType);
			mtsParameter.put("data", dataListMap);
			
			if("LMS".equals(sendType)|| "MMS".equals(sendType)) {
				apiPath = this._API_SEND_MMS_MULTI;
			}else {
				apiPath = this._API_SEND_SMS_MULTI;
			}
		/*}else {
			// 단건 발송
			mtsParameter.put("phone_number", phoneNumberList.get(0));
			mtsParameter.put("callback_number", callbackNumber);
			mtsParameter.put("message", message);
			mtsParameter.put("send_date", sendDate);
			
			// 첨부파일 존재
			if(filePath != null) {
				// 첨부파일 MTS 업로드
//				Map<String, List<Map<String, String>>> attachment = new HashMap<>();
//				List<Map<String, String>> imgUrlList = new ArrayList<>();
//				for(String path : filePath) {
//					log.debug("path = " + path);
//					Map<String, String> mtsFileUploadResult = this.mtsRequestFileUpload(path);
//					log.debug("mtsFileUploadResult = " + mtsFileUploadResult.toString());
//					log.debug("code = " + mtsFileUploadResult.get("code"));
//					if("0000".equals(mtsFileUploadResult.get("code"))) {
//						Map<String, String> imgUrl = new HashMap<>();
//						log.debug("images = " + mtsFileUploadResult.get("image"));
//						imgUrl.put("img_url", mtsFileUploadResult.get("image"));
//						imgUrlList.add(imgUrl);
//					}else {
//					}
//				}
				if(attachment != null) {
					attachment.put("image", imgUrlList);
					mtsParameter.put("attachment", attachment);
				}
			}
			
			if("LMS".equals(sendType)|| "MMS".equals(sendType)) {
				apiPath = this._API_SEND_MMS_ONE;
			}else {
				apiPath = this._API_SEND_SMS_ONE;
			}
		}*/
		
			
//		TelewebJSON getRsltParam = new TelewebJSON();
//		getRsltParam.setString("SEND_DATE", sendDate);
//		getRsltParam.setString("SEND_TYPE", sendType);

		mtsParameter.put("add_etc1", tenantId);
		mtsParameter.put("add_etc2", custcoId);
		mtsParameter.put("add_etc3", mtsSndngHstryGroupId);
			
	    return this.send(mtsParameter, apiPath);
		
	}
	
	
	@SuppressWarnings("static-access")
	private String send(Map<String, Object> mtsParameter, String apiPath) throws TelewebAppException
	{
		String custcoId = (String)mtsParameter.get("custcoId");
		String userId = (String)mtsParameter.get("userId");
		String sendType = (String)mtsParameter.get("sendType");
		mtsParameter.remove("custcoId");
		mtsParameter.remove("userId");
		mtsParameter.remove("sendType");
		
		//설문지 발송내역 체크
		String svyTrgt = "";
		if((String)mtsParameter.get("sendType") != null) {
			svyTrgt = (String)mtsParameter.get("svyTrgt");
			mtsParameter.remove("svyTrgt");
		}
		
		Map<String, String> result = null;
		String mtsResult = "";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		log.debug("apiPath = " + apiPath);
		String url = this._API_URL + apiPath;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI endUri = builder.build().toUri();
        
        
		try {
			ObjectMapper mapper = new ObjectMapper();
			String mtsParamString = mapper.writeValueAsString(mtsParameter);
			log.debug("mtsParamString = " + mtsParamString);
			
			result = this.apiCall(headers, endUri, mtsParamString);
			
			mtsResult = mapper.writeValueAsString(result);
			
			// 이력 저장
			mtsParameter.put("custcoId", custcoId);
			mtsParameter.put("userId", userId);
			mtsParameter.put("sendType", sendType);
			
			if(!svyTrgt.equals("") && svyTrgt != null) {
				mtsParameter.put("svyTrgt", svyTrgt);
			}
			
			this.saveSendHistory(mtsParameter, result);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    
		
		
        return mtsResult;
	}
		
	@SuppressWarnings("static-access")
	public Map<String, String> mtsRequestFileUpload(String filepath) throws TelewebAppException {
		Resource resource = new FileSystemResource(this._REPOSITORY + this._FILE_PATH + filepath);
	    
	    // 헤더
	    HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "multipart/form-data");
		
		// 파라미터
	    MultiValueMap<String, Resource> param = new LinkedMultiValueMap<>();
	    param.add("images", resource);
	    
	    // uri
        UriComponentsBuilder builder = 
        		UriComponentsBuilder.fromUriString(this._API_URL + this._API_FILE_UPLOAD_PATH);
        URI endUri = builder.build().toUri();
		
        // request
        Map<String, String> body = this.apiCall(headers, endUri, param);
        
        return body;
	}
	
	@SuppressWarnings("static-access")
	public Map<String, String> mtsAtalkFileUpload(String filepath, String senderKey, String imageType) throws TelewebAppException {
		Resource resource = new FileSystemResource(this._REPOSITORY + this._FILE_PATH + filepath);
		
	    // 헤더
	    HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "multipart/form-data");
		
		// 파라미터
	    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
	    param.add("image", resource);
	    param.add("senderKey", senderKey);
	    
	    // uri
	    String lastPath = "MAIN".equals(imageType)?"template":"itemHighlight";
        UriComponentsBuilder builder = 
        		UriComponentsBuilder.fromUriString(
        				this._API_ATALK_URL + this._API_ATALK_FILE_UPLOAD_PATH + lastPath);
        URI endUri = builder.build().toUri();
		
        // request
        Map<String, String> body = this.apiCall(headers, endUri, param);
        
        return body;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, String> apiCall (HttpHeaders headers, URI uri, Object param) throws TelewebAppException {
		log.debug("uri = " + uri);
        RestTemplate restTemplate = new RestTemplate();
        
        HttpEntity<?> requestEntity = new HttpEntity<>(param, headers);
        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Map.class);
//        log.debug("response = " + response.getBody());
        
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, String> result = null;
//        try {
//        	result = mapper.readValue(response.getBody(), new TypeReference<Map<String, String>>(){});
//        }catch(Exception e) {
//        	log.debug(e.getMessage());
//        }
        Map<String, String> body = response.getBody();
        
        return body;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> apiCallForStatus (HttpHeaders headers, URI uri, Object param) throws TelewebAppException {
//		log.debug("uri = " + uri);
		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<?> requestEntity = new HttpEntity<>(param, headers);
		ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Map.class);
		Map<String, Object> body = response.getBody();
		
		return body;
	}
	
	private void saveSendHistory(Map<String, Object> mtsParameter, Map<String, String> mtsResult) throws TelewebAppException
	{
        mtsParameter.put("resultCode", mtsResult.get("code"));
        mtsParameter.put("sndngDt", 
                mtsResult.get("received_at").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
        mtsParameter.put("resultMessage", mtsResult.get("message"));
		mtsParameter.put("mtsSndngHstryGroupId", mtsParameter.get("add_etc3"));
				
		this.saveSendHistory(mtsParameter);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> selectAttachedFile(String fileGroupKey) throws TelewebAppException
	{
		TelewebJSON objParam = new TelewebJSON();
		objParam.setString("FILE_GROUP_KEY", fileGroupKey);
		
		TelewebJSON result = twbComDao.select("kr.co.hkcloud.palette3.phone.sms.dao.SendSmsMapper", "selectAttachedFile", objParam);		
		
		return (List<Map<String, String>>)result.getDataObject();
	}
	
	
	@SuppressWarnings("unchecked")
	private void saveSendHistory(Map<String, Object> mtsParameter) throws TelewebAppException
	{
		String sendType = "";
		String phoneNumber = "";
		String callback_number = "";
		String message = "";
		String resultCode = "";
		String resultMessage = "";
		String sndngDt = "";
		String userId = "";
		String custcoId = "";
		String mtsSndngHstryGroupId = "";
		
		//설문지 문자 발송내역 확인용
		String svyTrgt = "";
		
		List<Map<String, String>> dataList = (List<Map<String, String>>)mtsParameter.get("data");
		
		if(dataList != null) {
			for(Map<String, String> data : dataList) {
				sendType = (String)mtsParameter.get("sendType");
//				sendType = (String)data.get("sendType");
				phoneNumber = (String)data.get("phone_number");
				callback_number = (String)data.get("callback_number");
				message = (String)data.get("message");
				resultCode = (String)mtsParameter.get("resultCode");
				resultMessage = (String)mtsParameter.get("resultMessage");
				//sndngDt = (String)mtsParameter.get("sndngDt");
				sndngDt = (String)data.get("send_date").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
				userId = (String)mtsParameter.get("userId");
				custcoId = (String)mtsParameter.get("custcoId");
				mtsSndngHstryGroupId = (String)mtsParameter.get("mtsSndngHstryGroupId");
				
				if((String)data.get("svyTrgt") != null){
					svyTrgt = (String)data.get("svyTrgt");
				}
				
				// 발송 이력 ID 채번
				String mtsSndngHstryId = Integer.toString(innbCreatCmmnService.createSeqNo("MTS_SNDNG_HSTRY_ID"));
				log.info("mtsSndngHstryGroupId = " + mtsSndngHstryGroupId);
				log.info("mtsSndngHstryId = " + mtsSndngHstryId);
				
				TelewebJSON objParam = this.makeSaveHistoryParameter(
						mtsSndngHstryId
						,sendType
						, phoneNumber
						, callback_number
						, message
						, resultCode
						, mtsSndngHstryGroupId.equals("") ? resultMessage : resultMessage + "||group:" + mtsSndngHstryGroupId
						, sndngDt
						, userId
						, custcoId
						, svyTrgt);
				twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.SendSmsMapper", "saveSendHistory", objParam);
			}
		}else {
			sendType = (String)mtsParameter.get("sendType");
			phoneNumber = (String)mtsParameter.get("phone_number");
			callback_number = (String)mtsParameter.get("callback_number");
			message = (String)mtsParameter.get("message");
			resultCode = (String)mtsParameter.get("resultCode");
			resultMessage = (String)mtsParameter.get("resultMessage");
			sndngDt = (String)mtsParameter.get("sndngDt");
			userId = (String)mtsParameter.get("userId");
			custcoId = (String)mtsParameter.get("custcoId");
			mtsSndngHstryGroupId = (String)mtsParameter.get("mtsSndngHstryGroupId");
			
			if((String)mtsParameter.get("svyTrgt") != null){
				svyTrgt = (String)mtsParameter.get("svyTrgt");
			}
			
			// 발송 이력 ID 채번
			String mtsSndngHstryId = Integer.toString(innbCreatCmmnService.createSeqNo("MTS_SNDNG_HSTRY_ID"));
			log.info("mtsSndngHstryGroupId = " + mtsSndngHstryGroupId);
			log.info("mtsSndngHstryId = " + mtsSndngHstryId);
			
			TelewebJSON objParam = this.makeSaveHistoryParameter(
					mtsSndngHstryId
					,sendType
					, phoneNumber
					, callback_number
					, message
					, resultCode
					, mtsSndngHstryGroupId.equals("") ? resultMessage : resultMessage + "||group:" + mtsSndngHstryGroupId
					, sndngDt
					, userId
					, custcoId
					, svyTrgt);
			
			twbComDao.insert("kr.co.hkcloud.palette3.phone.sms.dao.SendSmsMapper", "saveSendHistory", objParam);
		}
	}
	
	/**
	 *  문자 발송 결과 받아오기
	 */
	public TelewebJSON getSndngRslt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException
	{
		TelewebJSON resultParam = new TelewebJSON();
		// 현재 시간 가져오기
        LocalDateTime currentTime = LocalDateTime.now();
        // 출력 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        // 현재 시간을 지정된 형식으로 출력
        String formattedTime = currentTime.format(formatter);
        
		String sendDate = "";
		if(mjsonParams.getString("SEND_DATE") == null || mjsonParams.getString("SEND_DATE").equals("")) {
			sendDate = formattedTime;
		} else {
			sendDate = mjsonParams.getString("SEND_DATE");
		}
		mjsonParams.setString("SEND_DATE", sendDate);
		
		//!!!스케줄러 로직으로 통일 시킴 - by hjh. 20240830
		resultParam = mtsService.updateMtsSendingResult(mjsonParams);
		
        
//		String apiPath = "";
//		String mtsParamString = "";
//		if(mjsonParams.getString("SENDER_KEY")== null || mjsonParams.getString("SENDER_KEY").equals("")) {
//			if(mjsonParams.getString("SEND_TYPE").equals("SMS")) {
//				apiPath = "/rspns/sms/rspnsMessages";
//			} else {
//				apiPath = "/rspns/mms/rspnsMessages";
//			}
//			mtsParamString = "{\"auth_code\":\"" + _API_AUTH_CODE + "\",\"send_date\":\"" + sendDate + "\",\"add_etc1\":\"" + mjsonParams.getString("TENANT_ID") + "\",\"add_etc2\":\"" + mjsonParams.getString("CUSTCO_ID") + "\",\"add_etc3\":\"" + (mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID").equals("") ? mjsonParams.getString("MTS_SNDNG_HSTRY_ID") : mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID")) + "\"}";
//		} else {
//			apiPath = "/rspns/atk/rspnsMessages";
//			mtsParamString = "{\"auth_code\":\"" + _API_AUTH_CODE + "\",\"sender_key\":\"" + mjsonParams.getString("SENDER_KEY") + "\",\"send_date\":\"" + sendDate + "\",\"add_etc1\":\"" + mjsonParams.getString("TENANT_ID") + "\",\"add_etc2\":\"" + mjsonParams.getString("CUSTCO_ID") + "\",\"add_etc3\":\"" + (mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID").equals("") ? mjsonParams.getString("MTS_SNDNG_HSTRY_ID") : mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID")) + "\"}";
//		}
//		log.info("mtsParamString = " + mtsParamString);
//		
//		Map<String, String> result = null;
//		String mtsResult = "";
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Type", "application/json");
//		
//		log.info("apiPath = " + apiPath);
//		String url = this._API_URL + apiPath;
//        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
//        URI endUri = builder.build().toUri();
//        
//        
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			
//			result = this.apiCall(headers, endUri, mtsParamString);
//			
//			mtsResult = mapper.writeValueAsString(result);
//			log.info("mtsResult" + mtsResult);
//			
//			Map<String, Object> mtsParameter = new HashMap<>();
//			mtsParameter.put("code", result.get("code"));
//			mtsParameter.put("sndngDt", result.get("received_at").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
//			mtsParameter.put("resultMessage", result.get("message"));
//			mtsParameter.put("hasNext", result.get("hasNext"));
//			mtsParameter.put("data", result.get("data"));
//			mtsParameter.put("custcoId", mjsonParams.getString("CUSTCO_ID"));
//			mtsParameter.put("userId", mjsonParams.getString("USER_ID"));
//			log.info("====mts결과 받기====" + mtsParameter);
//			
//			if(result.get("data") != null) {
//				String mtsResultData = null;
//				mtsResultData = mapper.writeValueAsString(result.get("data"));
//				log.info("====result data====" + mtsResultData);
//				
//				JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(mtsResultData);
//	
//	            for (Object object : jsonArray) {
//	                JSONObject jsonObj = (JSONObject) object;
//	    			log.info("====result data array====" + jsonObj);
////	    			log.info("result_code : " + jsonObj.getString("result_code"));						//mts 전송결과 결과코드
////	    			log.info("result_date : " + jsonObj.getString("result_date"));						//mts 전송결과 전송결과일자
////	    			log.info("real_send_date : " + jsonObj.getString("real_send_date"));				//mts 전송결과 실제발송일자
////	    			log.info("sender_key : " + jsonObj.getString("sender_key"));						//mts 전송결과 발신프로필키
////	    			log.info("send_date : " + jsonObj.getString("send_date"));							//mts 전송결과 전송요청일자
////	    			log.info("nation_phone_number : " + jsonObj.getString("nation_phone_number"));		//mts 전송결과 전화번호국가코드
////	    			log.info("phone_number : " + jsonObj.getString("phone_number"));					//mts 전송결과 수신번호(받는 번호)
////	    			log.info("callback_number : " + jsonObj.getString("callback_number"));				//mts 전송결과 발신번호(보내는 번호)
////	    			log.info("app_user_id : " + jsonObj.getString("app_user_id"));						//mts 전송결과 어플 사용자 id
////	    			log.info("template_code : " + jsonObj.getString("template_code"));					//mts 전송결과 알림톡 템플릿코드
////	    			log.info("message : " + jsonObj.getString("message"));								//mts 전송결과 전송 메시지
////	    			log.info("title : " + jsonObj.getString("title"));									//mts 전송결과 전송 제목
////	    			log.info("tran_type : " + jsonObj.getString("tran_type"));							//mts 전송결과 전환전송 타입
////	    			log.info("tran_message : " + jsonObj.getString("tran_message"));					//mts 전송결과 전환전송 메시지
////	    			log.info("callback_url : " + jsonObj.getString("callback_url"));					//mts 전송결과 콜백url
////	    			log.info("add_etc1 : " + jsonObj.getString("add_etc1"));							//mts 전송결과 기타코드1
////	    			log.info("add_etc2 : " + jsonObj.getString("add_etc2"));							//mts 전송결과 기타코드2
////	    			log.info("add_etc3 : " + jsonObj.getString("add_etc3"));							//mts 전송결과 기타코드3
////	    			log.info("subject : " + jsonObj.getString("subject"));								//mts 전송결과 전환전송 제목
//	    			
//	    			TelewebJSON mtsResultParam = new TelewebJSON();
//	    			//필요한 파라메터 세팅
//	    			mtsResultParam.setString("MTS_SNDNG_HSTRY_GROUP_ID", mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID"));
//	    			mtsResultParam.setString("MTS_SNDNG_HSTRY_ID", mjsonParams.getString("MTS_SNDNG_HSTRY_ID"));
//	    			mtsResultParam.setString("RSLT_MSG", result.get("message"));
//	    			mtsResultParam.setString("ETC_MSG", mjsonParams.getString("ETC_MSG"));
//	    			//mts 발송결과값 세팅
//	    			for (Object key : jsonObj.keySet()) {
//	    				log.info(key + " : " + jsonObj.get(key));
//	    				mtsResultParam.setString(key.toString(), jsonObj.get(key).toString());
//	    	        }
//	    			String sendMsg = mtsResultParam.getString("message");
//	    			
//	    			if(mtsResultParam.getString("title") != null && !mtsResultParam.getString("title").equals("")) {
//	    				sendMsg = sendMsg + "||title:" + mtsResultParam.getString("title");
//					}
//	    			if(mtsResultParam.getString("attachment") != null && !mtsResultParam.getString("attachment").equals("")) {
//	    				sendMsg = sendMsg + "||attachment:" + mtsResultParam.getString("attachment");
//					}
//	    			mtsResultParam.setString("send_msg", sendMsg);
//    				log.info("mtsResultParam : " + mtsResultParam);
//
//					log.info("phone_number" + mtsResultParam.getString("phone_number"));
//					log.info("PHN_NO" + mjsonParams.getString("PHN_NO"));
//    				//내가 선택한 전화번호의 결과 가져오기
//    				if(mtsResultParam.getString("phone_number").equals(mjsonParams.getString("PHN_NO"))) {
//    					resultParam.setString("result_code", mtsResultParam.getString("result_code"));
//    					resultParam.setString("real_send_date", mtsResultParam.getString("real_send_date"));
//    					resultParam.setString("msg", mtsResultParam.getString("message"));
//    					if(mtsResultParam.getString("title") != null && !mtsResultParam.getString("title").equals("")) {
//    						resultParam.setString("title", mtsResultParam.getString("title"));
//    					}
//    					if(mtsResultParam.getString("attachment") != null && !mtsResultParam.getString("attachment").equals("")) {
//    						resultParam.setString("attachment", mtsResultParam.getString("attachment"));
//    					}
//    				}
//    				twbComDao.update("kr.co.hkcloud.palette3.phone.sms.dao.SendSmsMapper", "updateSndngRslt", mtsResultParam);
//	            }
//			} else if(!result.get("code").equals("0000") && !result.get("code").equals("1000")) {
//				resultParam.setString("result_code", result.get("code"));
//			}
//			log.info("resultParam" + resultParam);
//			
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
	    
		return resultParam;
	}
	
	private TelewebJSON makeSaveHistoryParameter(
			String mtsSndngHstryId
			,String sendType
			,String phoneNumber
			,String callback_number
			,String message
			,String resultCode
			,String resultMessage
			,String sndngDt
			,String userId
			,String custcoId
			,String svyTrgt) throws TelewebAppException
	{
		TelewebJSON objParam = new TelewebJSON();		
		objParam.setString("mtsSndngHstryId", mtsSndngHstryId);
		objParam.setString("sendType", sendType);
		objParam.setString("phone_number", phoneNumber);
		objParam.setString("callback_number", callback_number);
		objParam.setString("message", message);
		objParam.setString("resultCode", resultCode);
		objParam.setString("resultMessage", resultMessage);
		objParam.setString("sndngDt", sndngDt);
		objParam.setString("userId", userId);
		objParam.setString("custcoId", custcoId);
		objParam.setString("svyTrgt", svyTrgt);
		
		return objParam;
	}
	
	/**
	 * 알림톡 문자 치환 #{AA}, ##{AA,BB}##
	 */
	private String tranContentAtalk(String tmplMsg, List<String> contentsList, List<String> transContentsList, int i, String custcoId) throws TelewebAppException {
        String retContents = new String(tmplMsg);
        
        for(int n = 0; n < contentsList.size(); n++) {
            retContents = retContents.replaceAll("#\\{" + contentsList.get(n) + "\\}",transContentsList.get((i*contentsList.size())+n));
        }
        
        //##{A,B}## 패턴 변환 - 공통코드에서 값을 찾아와야 함.
        Pattern p = Pattern.compile(commCodeTranPattern);
        Matcher matcher = p.matcher(retContents);
        
        while (matcher.find()) {
            String extractedText = matcher.group(1);
            String[] splitText = extractedText.split(",");
            
            if(splitText != null && splitText.length == 2) {
                String cdId = transContentsList.get((i*contentsList.size())+contentsList.indexOf(splitText[0].trim()));
                if(cdId != null) {
                    TelewebJSON mjsonParams = new TelewebJSON();
                    mjsonParams.setString("CUSTCO_ID", custcoId);
                    mjsonParams.setString("CD_ID", cdId);
                    mjsonParams.setString("GROUP_CD_ID", splitText[1].trim());
                    TelewebJSON retObjParams = codeCmmnService.selectTranCommCode(mjsonParams);
                    if(StringUtils.isNotEmpty(retObjParams.getString("CD_NM"))) {
                        retContents = retContents.replaceAll("##\\{" + extractedText + "\\}##", retObjParams.getString("CD_NM"));
                    }
                } else {
                    retContents = retContents.replaceAll("##\\{" + extractedText + "\\}##", "");
                }
            } else {
                retContents = retContents.replaceAll("##\\{" + extractedText + "\\}##", "");
            }
        }
        return retContents;
    }
	
	
	/**
	 * 문자메시지(SMS) 발신번호 조회
     * 발신번호 유형 - DEFAULT : 기본(발신번호 항목 값으로 전송), ##{참여자_항목_ID,PLT_COMM_CD.GROUP_CD_ID}## : 참여자 항목_ID를 key로하는 공통코드의 값(전화번호) 설정 (ex : ##{CUTT_TYPE_1,CENTER_TEL}##)
     * LH주거복지정보 사업에서 추가됨. by hjh.
	 */
	private String tranCallbackNumberAtalk(String envSrvyDsptchNoTp, String callbackNumber, List<String> contentsList, List<String> transContentsList, int i, String custcoId) throws TelewebAppException {
	    String tranCallbackNumber = callbackNumber;
        if(StringUtils.isNotEmpty(envSrvyDsptchNoTp) && !"DEFAULT".equals(envSrvyDsptchNoTp)) {
            Pattern p = Pattern.compile(commCodeTranPattern);
            Matcher matcher = p.matcher(envSrvyDsptchNoTp);
            while (matcher.find()) {
                String extractedText = matcher.group(1);
                String[] splitText = extractedText.split(",");
                
                if(splitText != null && splitText.length == 2) {
                    String cdId = transContentsList.get((i*contentsList.size())+contentsList.indexOf(splitText[0].trim()));
                    if(cdId != null) {
                        TelewebJSON mjsonParams = new TelewebJSON();
                        mjsonParams.setString("CUSTCO_ID", custcoId);
                        mjsonParams.setString("CD_ID", cdId);
                        mjsonParams.setString("GROUP_CD_ID", splitText[1].trim());
                        TelewebJSON retObjParams = codeCmmnService.selectTranCommCode(mjsonParams);
                        if(StringUtils.isNotEmpty(retObjParams.getString("CD_NM"))) {
                            tranCallbackNumber = retObjParams.getString("CD_NM");
                            return tranCallbackNumber;
                        }
                    }
                }
            }
            
        } else {
            return tranCallbackNumber;
        }
        return tranCallbackNumber;
	}
	
	/**
	 * 문자메시지(SMS) 문자 치환 #{AA}, ##{AA,BB}##
	 * @param message
	 * @param item
	 * @param cdCnt
	 * @param custcoId
	 * @return
	 * @throws TelewebAppException
	 */
	private String tranContentSMS(String message, Map<String, String> item, String cdCnt, String custcoId) throws TelewebAppException {
        String retContents = new String(message);
        
        for(int j=0;j<Integer.parseInt(cdCnt);j++) {
            String cd = "#\\{" + item.get("CD"+j) + "\\}";
            String vl = item.get("VL"+j);
            retContents = retContents.replaceAll(cd,vl);
        }
        
        
        //##{A,B}## 패턴 변환 - 공통코드에서 값을 찾아와야 함.
        Pattern p = Pattern.compile(commCodeTranPattern);
        Matcher matcher = p.matcher(retContents);
        
        while (matcher.find()) {
            String extractedText = matcher.group(1);
            String[] splitText = extractedText.split(",");
            
            if(splitText != null && splitText.length == 2) {
                TelewebJSON mjsonParams = new TelewebJSON();
                mjsonParams.setString("CUSTCO_ID", custcoId);
                
                String key = getKeyFromValue(item, splitText[0].trim());
                String cdId = item.get("VL" + key.replace("CD", ""));
                if(cdId != null) {
                    mjsonParams.setString("CD_ID", cdId);
                    mjsonParams.setString("GROUP_CD_ID", splitText[1].trim());
                    TelewebJSON retObjParams = codeCmmnService.selectTranCommCode(mjsonParams);
                    if(StringUtils.isNotEmpty(retObjParams.getString("CD_NM"))) {
                        retContents = retContents.replaceAll("##\\{" + extractedText + "\\}##", retObjParams.getString("CD_NM"));
                    }
                } else {
                    retContents = retContents.replaceAll("##\\{" + extractedText + "\\}##", "");
                }
            } else {
                retContents = retContents.replaceAll("##\\{" + extractedText + "\\}##", "");
            }
        }
        return retContents;
    }
	
	
	/**
	 * 문자메시지(SMS) 발신번호 조회
	 * 발신번호 유형 - DEFAULT : 기본(발신번호 항목 값으로 전송), ##{참여자_항목_ID,PLT_COMM_CD.GROUP_CD_ID}## : 참여자 항목_ID를 key로하는 공통코드의 값(전화번호) 설정 (ex : ##{CUTT_TYPE_1,CENTER_TEL}##)
	 * LH주거복지정보 사업에서 추가됨. by hjh.
	 * @param envSrvyDsptchNoTp
	 * @param callbackNumber
	 * @param item
	 * @param custcoId
	 * @return
	 * @throws TelewebAppException
	 */
	private String tranCallbackNumberSMS(String envSrvyDsptchNoTp, String callbackNumber, Map<String, String> item, String custcoId) throws TelewebAppException {
	    String tranCallbackNumber = callbackNumber;
	    if(StringUtils.isNotEmpty(envSrvyDsptchNoTp) && !"DEFAULT".equals(envSrvyDsptchNoTp)) {
	        Pattern p = Pattern.compile(commCodeTranPattern);
	        Matcher matcher = p.matcher(envSrvyDsptchNoTp);
	        while (matcher.find()) {
	            String extractedText = matcher.group(1);
	            String[] splitText = extractedText.split(",");
	            
	            if(splitText != null && splitText.length == 2) {
	                TelewebJSON mjsonParams = new TelewebJSON();
	                mjsonParams.setString("CUSTCO_ID", custcoId);
	                
	                String key = getKeyFromValue(item, splitText[0].trim());
	                String cdId = item.get("VL" + key.replace("CD", ""));
	                if(cdId != null) {
	                    mjsonParams.setString("CD_ID", cdId);
	                    mjsonParams.setString("GROUP_CD_ID", splitText[1].trim());
	                    TelewebJSON retObjParams = codeCmmnService.selectTranCommCode(mjsonParams);
	                    if(StringUtils.isNotEmpty(retObjParams.getString("CD_NM"))) {
	                        tranCallbackNumber = retObjParams.getString("CD_NM");
	                        return tranCallbackNumber;
	                    }
	                }
	            }
	        }
	        
	    } else {
	        return tranCallbackNumber;
	    }
        return tranCallbackNumber;
	    
	}
	
	
	public String getKeyFromValue(Map<String, String> map, String value) {
        Optional<String> optionalKey = map.entrySet().stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
        return optionalKey.orElse(null);
    }
	
	
	/**
	 * 일시(dateTimeStr)를 받아서 분(min)을 증가해 리턴
	 * @param dateTimeStr
	 * @param min
	 * @return
	 */
	private String incrementDateTime(String dateTimeStr, int min) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        LocalDateTime incrementedDateTime = dateTime.plusMinutes(min);
        
        return incrementedDateTime.format(formatter);
    }
	
}
