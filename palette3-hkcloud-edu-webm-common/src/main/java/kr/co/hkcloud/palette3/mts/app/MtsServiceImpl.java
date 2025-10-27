package kr.co.hkcloud.palette3.mts.app;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Slf4j
@RequiredArgsConstructor
@Service("mtsService")
public class MtsServiceImpl implements MtsService {
	
	private final TwbComDAO mobjDao;
	
	private final String _API_AUTH_CODE = "6R5XPUIkrGrZUFuCTlLjwQ==";
	private final String _API_URL = "https://api.mtsco.co.kr";

	/**
	 * MTS 발송 결과 업데이트
	 * @return
	 * @throws TelewebAppException
	 */
	public TelewebJSON cronMtsSendingResult() throws TelewebAppException {
		TelewebJSON retParams = new TelewebJSON();
		
		
		//업데이트 대상 목록 조회
		TelewebJSON updateTargetListJson = selectUpdateTargetList();
		
		JSONArray targetArray = updateTargetListJson.getDataObject();
        JSONObject targetObj = new JSONObject();
        for(int i = 0; i < targetArray.size(); i++) {
        	targetObj = targetArray.getJSONObject(i);
        	
        	log.debug("=======targetObj============== ::: " + targetObj.getString("MTS_SNDNG_HSTRY_ID") + " ::: " + targetObj.toString());
        	
        	TelewebJSON mjsonParams = new TelewebJSON();
        	mjsonParams.setDataObject("DATA", targetObj);
        	mjsonParams.setString("TENANT_ID", TenantContext.getCurrentTenant());
        	
        	updateMtsSendingResult(mjsonParams);
        	
        	
        }
		
		retParams.setHeader("ERROR_FLAG", false);
        retParams.setHeader("ERROR_MSG", "");
        
        return retParams;
	}
	
	
	public TelewebJSON updateMtsSendingResult(TelewebJSON mjsonParams) throws TelewebAppException {
		String sendType = mjsonParams.getString("SEND_TYPE");
		
		//MTS 발송 결과 조회 및  MTS_발송_이력 업데이트
    	TelewebJSON retParams = getMtsRspnsAndUpdate(mjsonParams);
    	if("ATALK".equals(sendType)) {
    		//알림톡 전환전송 체크건인지 여부 - 'Y'일 시 MTS_발송_이력 업데이트 항목이 달라진다.
    		mjsonParams.setString("ATALK_TRAN_SNDNG_CHECK_YN", "Y");
    		//알림톡일 시, SMS 전환전송 건 조회 및 업데이트
    		mjsonParams.setString("SEND_TYPE", "SMS");
    		getMtsRspnsAndUpdate(mjsonParams);
    		
    		//알림톡일 시, LMS 전환전송 건 조회 및 업데이트
    		mjsonParams.setString("SEND_TYPE", "LMS");
    		getMtsRspnsAndUpdate(mjsonParams);
    	}
    	return retParams;
	}
	
	
	/**
	 * MTS 발송 결과 업데이트 대상 목록 조회
	 * @return
	 * @throws TelewebAppException
	 */
	@Transactional(readOnly = true)
	private TelewebJSON selectUpdateTargetList() throws TelewebAppException {
		return mobjDao.select("kr.co.hkcloud.palette3.mts.dao.MtsMapper", "selectUpdateTargetList", new TelewebJSON());
	}
	
	
	/**
	 * MTS 발송결과 API 호출
	 * @param mjsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	@Transactional(readOnly = false)
	private TelewebJSON getMtsRspnsAndUpdate(TelewebJSON mjsonParams) throws TelewebAppException {
		log.info("============================= getMtsRspnsAndUpdate Start =============================");
		TelewebJSON retParams = new TelewebJSON();
		
		// 현재 시간 가져오기
        LocalDateTime currentTime = LocalDateTime.now();
        // 출력 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        // 현재 시간을 지정된 형식으로 출력
        String formattedTime = currentTime.format(formatter);
        String sendType = mjsonParams.getString("SEND_TYPE");
        String phnNo = mjsonParams.getString("PHN_NO");
        boolean isPhnNo = StringUtils.isNotEmpty(phnNo) ? true : false;
        String atalktranSndngCheckYn = StringUtils.defaultIfBlank(mjsonParams.getString("ATALK_TRAN_SNDNG_CHECK_YN"), "N");
        log.info("atalktranSndngCheckYn ::: " + atalktranSndngCheckYn);
        
		String sendDate = mjsonParams.getString("SEND_DATE");
		String apiPath = "";
		String mtsParamString = "";
		if("SMS".equals(sendType) || "LMS".equals(sendType) ) {
			if("SMS".equals(sendType)) {
				apiPath = "/rspns/sms/rspnsMessages";
			} else {
				apiPath = "/rspns/mms/rspnsMessages";
			}
			mtsParamString = "{\"auth_code\":\"" + _API_AUTH_CODE + "\",\"send_date\":\"" + sendDate + "\",\"count\":100000,\"add_etc1\":\"" + mjsonParams.getString("TENANT_ID") + "\",\"add_etc2\":\"" + mjsonParams.getString("CUSTCO_ID") + "\",\"add_etc3\":\"" + (mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID").equals("") ? mjsonParams.getString("MTS_SNDNG_HSTRY_ID") : mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID")) + "\"}";
		} else {
			apiPath = "/rspns/atk/rspnsMessages";
			mtsParamString = "{\"auth_code\":\"" + _API_AUTH_CODE + "\",\"sender_key\":\"" + mjsonParams.getString("SENDER_KEY") + "\",\"send_date\":\"" + sendDate + "\",\"count\":100000,\"add_etc1\":\"" + mjsonParams.getString("TENANT_ID") + "\",\"add_etc2\":\"" + mjsonParams.getString("CUSTCO_ID") + "\",\"add_etc3\":\"" + (mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID").equals("") ? mjsonParams.getString("MTS_SNDNG_HSTRY_ID") : mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID")) + "\"}";
		}
		log.info("mtsParamString ::: " + mtsParamString);
		
		Map<String, String> result = null;
		String mtsResult = "";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		log.info("apiPath ::: " + apiPath);
		String url = this._API_URL + apiPath;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI endUri = builder.build().toUri();
        
        
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			result = this.apiCall(headers, endUri, mtsParamString);
			
			mtsResult = mapper.writeValueAsString(result);
//			log.info("mtsResult" + mtsResult);
			
			Map<String, Object> mtsParameter = new HashMap<>();
			mtsParameter.put("code", result.get("code"));
			mtsParameter.put("sndngDt", result.get("received_at").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
			mtsParameter.put("resultMessage", result.get("message"));
			mtsParameter.put("hasNext", result.get("hasNext"));
//			mtsParameter.put("data", result.get("data"));
			mtsParameter.put("custcoId", mjsonParams.getString("TENANT_ID"));
			mtsParameter.put("custcoId", mjsonParams.getString("CUSTCO_ID"));
			
			log.info("====mts결과 받기====" + mtsParameter);
			
			if(result.get("data") != null) {
				String mtsResultData = null;
				mtsResultData = mapper.writeValueAsString(result.get("data"));
//				log.info("====result data====" + mtsResultData);
				
				JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(mtsResultData);
	
	            for (Object object : jsonArray) {
	                JSONObject jsonObj = (JSONObject) object;
//	    			log.info("====result data array====" + jsonObj);
//	    			log.info("result_code : " + jsonObj.getString("result_code"));						//mts 전송결과 결과코드
//	    			log.info("result_date : " + jsonObj.getString("result_date"));						//mts 전송결과 전송결과일자
//	    			log.info("real_send_date : " + jsonObj.getString("real_send_date"));				//mts 전송결과 실제발송일자
//	    			log.info("sender_key : " + jsonObj.getString("sender_key"));						//mts 전송결과 발신프로필키
//	    			log.info("send_date : " + jsonObj.getString("send_date"));							//mts 전송결과 전송요청일자
//	    			log.info("nation_phone_number : " + jsonObj.getString("nation_phone_number"));		//mts 전송결과 전화번호국가코드
//	    			log.info("phone_number : " + jsonObj.getString("phone_number"));					//mts 전송결과 수신번호(받는 번호)
//	    			log.info("callback_number : " + jsonObj.getString("callback_number"));				//mts 전송결과 발신번호(보내는 번호)
//	    			log.info("app_user_id : " + jsonObj.getString("app_user_id"));						//mts 전송결과 어플 사용자 id
//	    			log.info("template_code : " + jsonObj.getString("template_code"));					//mts 전송결과 알림톡 템플릿코드
//	    			log.info("message : " + jsonObj.getString("message"));								//mts 전송결과 전송 메시지
//	    			log.info("title : " + jsonObj.getString("title"));									//mts 전송결과 전송 제목
//	    			log.info("tran_type : " + jsonObj.getString("tran_type"));							//mts 전송결과 전환전송 타입
//	    			log.info("tran_message : " + jsonObj.getString("tran_message"));					//mts 전송결과 전환전송 메시지
//	    			log.info("callback_url : " + jsonObj.getString("callback_url"));					//mts 전송결과 콜백url
//	    			log.info("add_etc1 : " + jsonObj.getString("add_etc1"));							//mts 전송결과 기타코드1
//	    			log.info("add_etc2 : " + jsonObj.getString("add_etc2"));							//mts 전송결과 기타코드2
//	    			log.info("add_etc3 : " + jsonObj.getString("add_etc3"));							//mts 전송결과 기타코드3
//	    			log.info("subject : " + jsonObj.getString("subject"));								//mts 전송결과 전환전송 제목
	    			
	    			TelewebJSON mtsResultParam = new TelewebJSON();
	    			
	    			//mts 발송결과값 세팅
	    			for (Object key : jsonObj.keySet()) {
//	    				log.info(key + " : " + jsonObj.get(key));
	    				mtsResultParam.setString(key.toString(), jsonObj.get(key).toString());
	    	        }
	    			String sendMsg = mtsResultParam.getString("message");
	    			
	    			//알림톡일 시 title 체크
	    			if("ATALK".equals(sendType) && mtsResultParam.getString("title") != null && !mtsResultParam.getString("title").equals("")) {
	    				sendMsg = sendMsg + "||title:" + mtsResultParam.getString("title");
					}
	    			
	    			//SMS, LMS일 시 subject 체크
//	    			if(!"ATALK".equals(sndngSeCd) && mtsResultParam.getString("subject") != null && !mtsResultParam.getString("subject").equals("")) {
//	    				sendMsg = sendMsg + "||subject:" + mtsResultParam.getString("subject");
//					}
	    			if(mtsResultParam.getString("attachment") != null && !mtsResultParam.getString("attachment").equals("")) {
	    				sendMsg = sendMsg + "||attachment:" + mtsResultParam.getString("attachment");
					}
	    			//필요한 파라메터 세팅
	    			mtsResultParam.setString("MTS_SNDNG_HSTRY_GROUP_ID", mjsonParams.getString("MTS_SNDNG_HSTRY_GROUP_ID"));
	    			mtsResultParam.setString("MTS_SNDNG_HSTRY_ID", mjsonParams.getString("MTS_SNDNG_HSTRY_ID"));
	    			mtsResultParam.setString("RSLT_MSG", result.get("message"));
	    			mtsResultParam.setString("ETC_MSG", mjsonParams.getString("ETC_MSG"));
	    			mtsResultParam.setString("send_msg", sendMsg);
	    			if("Y".equals(atalktranSndngCheckYn)) {
	    				mtsResultParam.setString("ATALK_TRAN_SNDNG_CD", sendType.equals("LMS") ? "L" : "S");
		    			mtsResultParam.setString("ATALK_TRAN_SNDNG_YN", "Y");
	    			}
	    			
    				log.info("mtsResultParam : " + mtsResultParam);
//					log.info("phone_number : " + mtsResultParam.getString("phone_number"));
//					log.info("RSLT_MSG : " + mtsResultParam.getString("RSLT_MSG") + mtsResultParam.getString("ETC_MSG") + "||result:Y");//#{RSLT_MSG} || #{ETC_MSG} || '||result:Y'
    				//내가 선택한 전화번호의 결과 가져오기
    				if(isPhnNo) {
	    				if(mtsResultParam.getString("phone_number").equals(phnNo)) {
	    					retParams.setString("result_code", mtsResultParam.getString("result_code"));
	    					retParams.setString("real_send_date", mtsResultParam.getString("real_send_date"));
	    					retParams.setString("msg", mtsResultParam.getString("message"));
	    					if(mtsResultParam.getString("title") != null && !mtsResultParam.getString("title").equals("")) {
	    						retParams.setString("title", mtsResultParam.getString("title"));
	    					}
	    					if(mtsResultParam.getString("attachment") != null && !mtsResultParam.getString("attachment").equals("")) {
	    						retParams.setString("attachment", mtsResultParam.getString("attachment"));
	    					}
	    				}
    				}
    				mobjDao.update("kr.co.hkcloud.palette3.mts.dao.MtsMapper", "updateSndngRslt", mtsResultParam);
	            }
			} else if(!result.get("code").equals("0000") && !result.get("code").equals("1000")) {
				retParams.setString("result_code", result.get("code"));
			}
			log.info("resultParam" + retParams);
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		retParams.setHeader("ERROR_FLAG", false);
        retParams.setHeader("ERROR_MSG", "");
        
        return retParams;
	}
	
	
	                                     
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, String> apiCall (HttpHeaders headers, URI uri, Object param) throws TelewebAppException {
		log.debug("uri = " + uri);
        RestTemplate restTemplate = new RestTemplate();
        
        HttpEntity<?> requestEntity = new HttpEntity<>(param, headers);
        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Map.class);

        Map<String, String> body = response.getBody();
        
        return body;
	}
}
