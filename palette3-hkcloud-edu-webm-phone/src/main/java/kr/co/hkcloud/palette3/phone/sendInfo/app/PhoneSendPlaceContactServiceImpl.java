package kr.co.hkcloud.palette3.phone.sendInfo.app;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import java.io.File;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
@Service("phoneSendPlaceContactService")
public class PhoneSendPlaceContactServiceImpl implements PhoneSendPlaceContactService{
	@Autowired
    Environment environment;
	
    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    
    private static final String _API_AUTH_CODE = "6R5XPUIkrGrZUFuCTlLjwQ==";

    // 위치정보, 연락처정보 조회
	@Override
	public TelewebJSON selectPlace(TelewebJSON jsonParams) throws TelewebAppException {
		TelewebJSON objRetParams = new TelewebJSON();
		
		if(Boolean.parseBoolean(jsonParams.getString("TYPE"))){
			// 위치정보 조회
			objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.sendInfo.dao.PhoneSendPlaceContactMapper", "selectPlace", jsonParams);
		}else {
			// 연락처정보 조회
			objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.sendInfo.dao.PhoneSendPlaceContactMapper", "selectContact", jsonParams);
		}
        return objRetParams;
	}
	
	// 문자,알림톡 단건 전송
	@Override
	public TelewebJSON sendInfo(TelewebJSON jsonParams) throws TelewebAppException, UnsupportedEncodingException, JsonProcessingException {
		
		TelewebJSON objRetParams = new TelewebJSON();
		
		TelewebJSON mjsonParams = new TelewebJSON();
		JSONArray jsonArray = jsonParams.getDataObject();
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		
		
		JSONArray aTmpParam = new JSONArray();
		JSONArray aTmpParam2 = new JSONArray();
		JSONObject iuTmpParam = new JSONObject();
		JSONObject iTmpParam = new JSONObject();
		JSONObject tTmpParam = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		
		// 헤더
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Type", "multipart/form-data");
		
		// SMS / LMS / MMS / ATALK 구분
        String url = "";
        if(jsonObject.getString("SNDNG_SE_CD").equals("SMS")) {
        	url = "http://api.mtsco.co.kr/sndng/sms/sendMessage";
        }else if(jsonObject.getString("SNDNG_SE_CD").equals("LMS") || jsonObject.getString("SNDNG_SE_CD").equals("MMS")){
        	url = "http://api.mtsco.co.kr/sndng/mms/sendMessage";
        }else if(jsonObject.getString("SNDNG_SE_CD").equals("ATALK")){
        	url = "http://api.mtsco.co.kr/sndng/atk/sendMessage";
        }
		
		// 내용 치환
        String desc = jsonParams.getString("message");
        desc = desc.replaceAll("&lt;", "<");
        desc = desc.replaceAll("&gt;", ">");
        jsonParams.setString("message", desc);
		
		// 문자 + 알림톡 - 전송할 공통 정보
		tTmpParam.put("auth_code", this._API_AUTH_CODE); 							// 인증코드
		tTmpParam.put("send_date", jsonParams.getString("send_date")); 				// 발송일시
		tTmpParam.put("callback_number", jsonParams.getString("callback_number")); 	// 발신번호
		tTmpParam.put("phone_number", jsonParams.getString("phone_number")); 		// 수신번호
		tTmpParam.put("message", jsonParams.getString("message")); 					// 내용
		tTmpParam.put("subject", jsonParams.getString("subject")); 					// 제목(LMS/MMS 발송시)
		
		
		// 알림톡 템플릿 정보
		tTmpParam.put("template_code", jsonParams.getString("template_code"));		// 템플릿코드
		tTmpParam.put("sender_key", jsonParams.getString("sender_key"));			// 발신프로필키
		tTmpParam.put("tran_message", jsonParams.getString("tran_message")); 		// 전환전송내용(알림톡 발송 실패시)
		tTmpParam.put("title", jsonParams.getString("title"));						// 강조표기형 - 강조제목
		tTmpParam.put("tran_type", jsonParams.getString("tran_type"));				// 전환전송 여부
		tTmpParam.put("imageFlag", jsonParams.getString("imageFlag"));				// 이미지형 여부
		
		
		// MMS 전송(이미지파일 존재시) - 이미지파일 정보 파라미터 세팅
		MultiValueMap<String, Resource> param = new LinkedMultiValueMap<>();
		
		if(!jsonParams.getString("img_url").isEmpty()) {
			
	        for (int n = 0; n < jsonArray.size(); n++) {
	        	JSONObject objData = jsonArray.getJSONObject(n);
	        	
	        	@SuppressWarnings("rawtypes")
	            Iterator it = objData.keys();
	            while(it.hasNext()) {
	                String strKey = (String) it.next();
	                String strValue = objData.getString(strKey);
	                if(strKey.equals("img_url")) {
	                	iuTmpParam.put("img_url", strValue);
	                	aTmpParam.add(iuTmpParam);
	                	iTmpParam.put("image", aTmpParam);
	                }
	            }
	        }


			
			// uri
			UriComponentsBuilder builderImg = UriComponentsBuilder.fromUriString("http://api.mtsco.co.kr/img/upload_image");
			
			URI endUriImg = builderImg.build().toUri();
//			Resource resource = null;
			RestTemplate restTemplateImg = new RestTemplate();
			HttpEntity<?> requestEntityImg = new HttpEntity<>(param);
			ResponseEntity<String> responseImg = null;
			String bodyImg = null;
			JsonParser parser = new JsonParser();
			JsonObject obj = null;
			
			// MTS response
			// MTS에서 img_url 각각 가져오기
			for(int i=0; i<aTmpParam.size(); i++) {
				
				String path = aTmpParam.getJSONObject(i).getString("img_url");
				File file = new File(environment.getProperty("file.repository.root-dir") + "/" + path);
				FileSystemResource resource = new FileSystemResource(file);
				param.set("images", resource);
				
				responseImg = restTemplateImg.exchange(endUriImg, HttpMethod.POST, requestEntityImg, String.class);
				bodyImg = responseImg.getBody();
				obj = (JsonObject)parser.parse(bodyImg.toString());
	
				// img_url 가져오기 성공
				if(obj.get("code").getAsString().equalsIgnoreCase("0000")) {
	            	iuTmpParam.put("img_url", obj.get("image").getAsString());
	            	aTmpParam2.add(iuTmpParam);
					iTmpParam.put("image", aTmpParam2);
					tTmpParam.put("attachment", mapper.writeValueAsString(iTmpParam));
				}
			}
		}
		
		// 알림톡 전송 (버튼 존재시) - 버튼 정보 파라미터 세팅
		if(!jsonParams.getString("type").isEmpty()) {
			List<Map<String, Object>> mtsParamList = new ArrayList<>();
			for (int n = 0; n < jsonArray.size(); n++) {
	        	JSONObject objData = jsonArray.getJSONObject(n);
	        	
	        	@SuppressWarnings("rawtypes")
	            Iterator it = objData.keys();
	            while(it.hasNext()) {
	                String strKey = (String) it.next();
	                String strValue = objData.getString(strKey);
	                if(strKey.equals("type")) {
	                	iuTmpParam.put("type", strValue);
	                	aTmpParam.add(iuTmpParam);
	
	                }
	            }
	        }
			mtsParamList.addAll(aTmpParam);
			
			for(int i=0; i<aTmpParam.size(); i++) {
				switch(mtsParamList.get(i).get("type").toString()) {
					case "WL" :
						iuTmpParam.put("type", jsonArray.getJSONObject(i).get("type").toString());
						iuTmpParam.put("name", jsonArray.getJSONObject(i).get("name").toString());
						iuTmpParam.put("url_mobile", jsonArray.getJSONObject(i).get("url_mobile").toString());
						iuTmpParam.put("url_pc", jsonArray.getJSONObject(i).get("url_pc").toString());
						
						aTmpParam2.add(iuTmpParam);
						iuTmpParam.remove("type");
						iuTmpParam.remove("name");
						iuTmpParam.remove("url_mobile");
						iuTmpParam.remove("url_pc");
						iuTmpParam.remove("ordering");
						break;
					case "AL" : 
						iuTmpParam.put("type", jsonArray.getJSONObject(i).get("type").toString());
						iuTmpParam.put("name", jsonArray.getJSONObject(i).get("name").toString());
						iuTmpParam.put("scheme_android", jsonArray.getJSONObject(i).get("scheme_android").toString());
						iuTmpParam.put("scheme_ios", jsonArray.getJSONObject(i).get("scheme_ios").toString());
						iuTmpParam.put("url_mobile", jsonArray.getJSONObject(i).get("url_mobile").toString());
						iuTmpParam.put("url_pc", jsonArray.getJSONObject(i).get("url_pc").toString());
						
						aTmpParam2.add(iuTmpParam);
						iuTmpParam.remove("type");
						iuTmpParam.remove("name");
						iuTmpParam.remove("scheme_android");
						iuTmpParam.remove("scheme_ios");
						iuTmpParam.remove("url_mobile");
						iuTmpParam.remove("url_pc");
						iuTmpParam.remove("ordering");
						break;
					case "BK" : 
						iuTmpParam.put("type", jsonArray.getJSONObject(i).get("type").toString());
						iuTmpParam.put("name", jsonArray.getJSONObject(i).get("name").toString());
						iuTmpParam.put("chat_extra", jsonArray.getJSONObject(i).get("chat_extra").toString());
						
						aTmpParam2.add(iuTmpParam);
						iuTmpParam.remove("type");
						iuTmpParam.remove("name");
						iuTmpParam.remove("chat_extra");
						iuTmpParam.remove("ordering");
						break;
					case "MD" : 
						iuTmpParam.put("type", jsonArray.getJSONObject(i).get("type").toString());
						iuTmpParam.put("name", jsonArray.getJSONObject(i).get("name").toString());
						iuTmpParam.put("chat_event", jsonArray.getJSONObject(i).get("chat_event").toString());
						
						aTmpParam2.add(iuTmpParam);
						iuTmpParam.remove("type");
						iuTmpParam.remove("name");
						iuTmpParam.remove("chat_event");
						iuTmpParam.remove("ordering");
						break;
					case "AC" : 
						iuTmpParam.put("type", jsonArray.getJSONObject(i).get("type").toString());
						iuTmpParam.put("name", "채널 추가");
						
						aTmpParam2.add(iuTmpParam);
						iuTmpParam.remove("type");
						iuTmpParam.remove("name");
						iuTmpParam.remove("ordering");
						break;
					default :
						iuTmpParam.put("type", jsonArray.getJSONObject(i).get("type").toString());
						iuTmpParam.put("name", jsonArray.getJSONObject(i).get("name").toString());
						
						aTmpParam2.add(iuTmpParam);
						iuTmpParam.remove("type");
						iuTmpParam.remove("name");
						iuTmpParam.remove("ordering");
						break;
				}
				iTmpParam.put("button", aTmpParam2);
				tTmpParam.put("attachment", iTmpParam);
			}
		}
		
		// MTS api 전송
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI endUri = builder.build().toUri();

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(tTmpParam, headers);
        log.debug("requestEntity:::"+ requestEntity);

        ResponseEntity<String> response = restTemplate.exchange(endUri, HttpMethod.POST, requestEntity, String.class);
        log.info("response ::  "+response);
        String body = response.getBody();
        
		// MTS response
        if(body != null) {
	        jsonArray = JSONArray.fromObject("["+ body+ "]");
	        jsonObject = jsonArray.getJSONObject(0);
	        objRetParams.setDataObject(jsonArray);
		}
        
        jsonParams.setString("RSLT_CD", objRetParams.getString("code"));			// 처리결과 코드
        jsonParams.setString("RSLT_MSG", objRetParams.getString("message"));		// 응답 메시지 : 0000 = 성공
        jsonParams.setString("SNDNG_DT", objRetParams.getString("received_at"));	// 메시지를 수신한 시간

        // 발송 이력 ID 채번
        jsonParams.setString("MTS_SNDNG_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("MTS_SNDNG_HSTRY_ID")));

        // 발송 이력 테이블에 저장
        mjsonParams = mobjDao.insert("kr.co.hkcloud.palette3.phone.sendInfo.dao.PhoneSendPlaceContactMapper", "sendInfo", jsonParams);
		
		return objRetParams;
	}

	// 위치정보,연락처정보 전용 템플릿 조회
	@Override
	public TelewebJSON selectTempleteInfo(TelewebJSON jsonParams) throws TelewebAppException {
		TelewebJSON objRetParams = new TelewebJSON();
		if(jsonParams.getString("TMPL_TY").equals("SMS")) {
			objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.sendInfo.dao.PhoneSendPlaceContactMapper", "selectSmsTempleteInfo", jsonParams);
		} else if(jsonParams.getString("TMPL_TY").equals("KAKAO")) {
			objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.sendInfo.dao.PhoneSendPlaceContactMapper", "selectAtalkTempleteInfo", jsonParams);
		}
		return objRetParams;
	}

	// 알림톡 단건 전송 템플릿 조회
	@Override
	public TelewebJSON selectAtalkTemplete(TelewebJSON jsonParams) throws TelewebAppException {
		TelewebJSON objRetParams = new TelewebJSON();
		objRetParams = mobjDao.select("kr.co.hkcloud.palette3.phone.sendInfo.dao.PhoneSendPlaceContactMapper", "selectAtalkTemplete", jsonParams);
		return objRetParams;
	}

}
