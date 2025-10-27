package kr.co.hkcloud.palette3.message.app;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.sse.app.SseService;
import kr.co.hkcloud.palette3.sse.message.model.SseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Slf4j
@RequiredArgsConstructor
@Service("messageService")
public class MessageServiceImpl implements MessageService{
	@Autowired
    Environment environment;

	private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
	private final SseService sseService;
    
    private static final String _API_AUTH_CODE = "6R5XPUIkrGrZUFuCTlLjwQ==";
    
    
    // 쪽지보내기 및 회신
 	@Transactional(readOnly = false)
 	public TelewebJSON sendMsg(TelewebJSON mjsonParams) throws TelewebAppException {
 		
 		// 쪽지ID 채번
 		if(mjsonParams.getString("MSG_ID").isEmpty()) {
 			mjsonParams.setString("MSG_ID", Integer.toString(innbCreatCmmnService.createSeqNo("MSG_ID")));			
 		};
 		
 		// 쪽지그룹ID 채번 - 쪽지 회신인 경우 채번 X
 		if(mjsonParams.getString("MSG_GROUP_ID").isEmpty()) {
 			mjsonParams.setString("MSG_GROUP_ID", Integer.toString(innbCreatCmmnService.createSeqNo("MSG_GROUP_ID")));
 		};

 		String msgCn = mjsonParams.getString("MSG_CN");
 		msgCn = msgCn.replaceAll("&lt;", "<");
 		msgCn = msgCn.replaceAll("&gt;", ">");
 		mjsonParams.setString("MSG_CN", msgCn);
 		
 		// 쪽지 INSERT
		mobjDao.insert("kr.co.hkcloud.palette3.message.dao.MessageMapper", "sendMsg", mjsonParams);
		TelewebJSON cnJson = mobjDao.insert("kr.co.hkcloud.palette3.message.dao.MessageMapper", "sendMsgCn", mjsonParams);
 		// 쪽지내용 INSERT
		
		/* TODO 실시간 알림메시지 보내기 */
		if(cnJson.getHeaderBoolean("ERROR_FLAG") == false) {
			SseMessage sseMessage = new SseMessage();
			sseMessage.setType(SseMessage.MessageType.SYSTEM_MESSAGE); /* 입장(ENTER), 퇴장(QUIT),대화(TALK), 강제로그아웃(SYSTEM_LOGOUT),  // 시스템점검(SYSTEM_CHECK),   //메시지(SYSTEM_MESSAGE)  */
			sseMessage.setSender( mjsonParams.getString("USER_ID") );
			sseMessage.setReceiver( mjsonParams.getString("RCPTN_USER_ID") );	// ALL은 전체 , userId 개인별
			sseMessage.setRoomId( TenantContext.getCurrentTenant() +"_"+ TenantContext.getCurrentCustco() );
			sseMessage.setSecond( 5 ); //필수 아님. 기본이 5초
			sseMessage.setPos("top"); //필수 아님. 기본이 top / bottom
			sseMessage.setMessage("[알림] 쪽지가 도착하였습니다.");	//메시지
			try{ sseService.sendMessage( sseMessage ); }catch(Exception e){ log.error("sseMessage is exception : " + e.getMessage() ); }
		}
		
 		return cnJson;
 	}
    
 	
    // 받은쪽지 및 보낸쪽지 조회
  	@Transactional(readOnly = true)
  	public TelewebJSON selectMsgList(TelewebJSON mjsonParams) throws TelewebAppException {
  		return mobjDao.select("kr.co.hkcloud.palette3.message.dao.MessageMapper", "selectMsgList", mjsonParams);
  	}

  	// 회신쪽지 조회
  	@Transactional(readOnly = true)
	public TelewebJSON selectMsgList2(TelewebJSON mjsonParams) throws TelewebAppException {
		return mobjDao.select("kr.co.hkcloud.palette3.message.dao.MessageMapper", "selectMsgList2", mjsonParams);	
	}

  	// 사용자 조회
	@Override
	public TelewebJSON selectUser(TelewebJSON mjsonParams) throws TelewebAppException {
		return mobjDao.select("kr.co.hkcloud.palette3.message.dao.MessageMapper", "selectUser", mjsonParams);
	}

	// 최신 받은 쪽지 조회
	@Override
	public TelewebJSON selectNewMsg(TelewebJSON mjsonParams) throws TelewebAppException {
		return mobjDao.select("kr.co.hkcloud.palette3.message.dao.MessageMapper", "selectNewMsg", mjsonParams);
	}

	// 쪽지 개수 조회
	@Override
	public TelewebJSON selectMsgCnt(TelewebJSON mjsonParams) throws TelewebAppException {
		return mobjDao.select("kr.co.hkcloud.palette3.message.dao.MessageMapper", "selectMsgCnt", mjsonParams);
	}

	// 쪽지 조회 여부 업데이트
	@Override
	public TelewebJSON updateInqMsg(TelewebJSON mjsonParams) throws TelewebAppException {
		return mobjDao.update("kr.co.hkcloud.palette3.message.dao.MessageMapper", "updateInqMsg", mjsonParams);
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
        String sndngSeCd = jsonObject.getString("SNDNG_SE_CD");
        if(sndngSeCd.equals("SMS")) {
        	url = "https://api.mtsco.co.kr/sndng/sms/sendMessage";
        }else if(sndngSeCd.equals("LMS") || sndngSeCd.equals("MMS")){
        	url = "https://api.mtsco.co.kr/sndng/mms/sendMessage";
        }else if(sndngSeCd.equals("ATALK")){
        	url = "https://api.mtsco.co.kr/sndng/atk/sendMessage";
        }

        // 발송 이력 ID 채번
        String mtsSndngHstryId = Integer.toString(innbCreatCmmnService.createSeqNo("MTS_SNDNG_HSTRY_ID"));
		
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

		log.info("jsonParams>>>>>"+jsonParams);
		tTmpParam.put("add_etc1", jsonParams.getString("tenantId")); 				// 발송정보 구분용 tenantId
		tTmpParam.put("add_etc2", jsonParams.getString("CUSTCO_ID")); 				// 발송정보 분용 custcoId
		tTmpParam.put("add_etc3", mtsSndngHstryId); 								// 발송정보 구분용 문자발송이력 id
		
		
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
			UriComponentsBuilder builderImg = UriComponentsBuilder.fromUriString("https://api.mtsco.co.kr/img/upload_image");
			
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
        jsonParams.setString("RSLT_MSG", sndngSeCd.equals("ATALK") ? objRetParams.getString("message") + "||senderKey:" + jsonParams.getString("sender_key") + "||tmplCd:" + jsonParams.getString("template_code") : objRetParams.getString("message"));		// 응답 메시지 : 0000 = 성공
        jsonParams.setString("SNDNG_DT", objRetParams.getString("received_at"));	// 메시지를 수신한 시간
        
        // 발송 이력 ID 세팅
        jsonParams.setString("MTS_SNDNG_HSTRY_ID", mtsSndngHstryId);

        // 발송 이력 테이블에 저장
        mjsonParams = mobjDao.insert("kr.co.hkcloud.palette3.message.dao.MessageMapper", "sendInfo", jsonParams);

		objRetParams.setString("MTS_SNDNG_HSTRY_ID", mtsSndngHstryId);

		return objRetParams;
	}

}
