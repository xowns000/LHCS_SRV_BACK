package kr.co.hkcloud.palette3.infra.chat.util;


import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.core.palette.asp.app.TalkAspBizChannelService;
import kr.co.hkcloud.palette3.core.palette.asp.app.TalkAspCustChannelService;
import kr.co.hkcloud.palette3.core.palette.asp.app.TalkAspCustService;
import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspBizChannel;
import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCust;
import kr.co.hkcloud.palette3.core.palette.asp.domain.TwbAspCustChannel;
import kr.co.hkcloud.palette3.core.palette.asp.exception.AspCustChannelFailLineSignatureException;
import kr.co.hkcloud.palette3.core.security.crypto.Base64;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import kr.co.hkcloud.palette3.infra.palette.constants.InterfaceResultCode;
import kr.co.hkcloud.palette3.infra.palette.exception.InterfaceException;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Component
public class TeletalkReceiverUtils
{
    private final TalkAspBizChannelService  aspBizChannelService;
    private final TalkAspCustChannelService aspCustChannelService;
    private final TalkAspCustService        aspCustService;
    private final PaletteCmmnService        paletteCmmnService;


    /**
     * 메세지 수신 유효성 체크
     * <p>
     * <strong>각 메신저 URI 정의</strong><br/>
     * - 카카오상담톡 : https://kakao.hcteletalk.com<br/>
     * - 티톡 : https://ttalk.hcteletalk.com<br/>
     * - 라인 : https://line.hcteletalk.com<br/>
     * -- subdomain 값으로 무슨 메신저인지 식별<br/>
     * -- DSPTCH_PRF_KEY : 메신저에서 채널을 식별하는 값임<br/>
     * jsonBody data : user_key / session_id / sender_key / time / reference : extra, lastText, lastTextData, appUserId / last_reference : extra, bot, bot_event, created_at
     * 
     * @param  request
     * @param  rcvJson
     * @return                      JSONObject
     * @throws TelewebUtilException
     */
    public JSONObject checkValidationMessage(String eventType, HttpServletRequest request, @NotEmpty final JSONObject jsonBody) throws TelewebUtilException
    {
    	log.info("------------[메세지 수신 유효성 체크] 채팅 인입 시 처음 타는 곳 ("+ request.getRequestURI() +") -------------");
    	log.info("===>" + jsonBody.get("user_key") + ": eventType: " + eventType);
    	log.info("===>" + jsonBody.get("user_key") + ": request: " + request);
    	log.info("===>" + jsonBody.get("user_key") + ": jsonBody: " + jsonBody);
    	log.info("--------------------------");
    	
    	//채팅 테넌트 구분
    	TelewebJSON objRetParams = new TelewebJSON();
    	TelewebJSON params = new TelewebJSON();
			
	  	String senderKey = (String) jsonBody.get("sender_key");
	  	params.setString("DSPTCH_PRF_KEY", senderKey);
	  	objRetParams = paletteCmmnService.getCertCustcoId(params);
	  	TenantContext.setCurrentTenant(objRetParams.getString("CERT_CUSTCO_TENANT_ID"));

        log.info("===>" + jsonBody.get("user_key") + ": TenantContext.getCurrentTenant(): " + TenantContext.getCurrentTenant());

        String subdomain = null;
        if(jsonBody.containsKey("content")) {
            jsonBody.put("content", jsonBody.getString("content"));
            log.info("===>" + jsonBody.get("user_key") + ": content: " + jsonBody.getString("content"));
        }

        log.info("===>" + jsonBody.get("user_key") + ": jsonBody.containsKey(\"call_typ_cd\"): " + jsonBody.containsKey("call_typ_cd"));

        if(jsonBody.containsKey("call_typ_cd")) {
            String callTypeCd = jsonBody.getString("call_typ_cd");
            log.info("===>" + jsonBody.get("user_key") + ": callTypeCd: " + callTypeCd);

            if(callTypeCd != null && "TTALK".equals(callTypeCd)) {
                jsonBody.put("call_typ_cd", "TTALK"); //티톡
                subdomain = "ttalk".toUpperCase();
            }
            else if(callTypeCd != null && "LINE".equals(callTypeCd)) {
                jsonBody.put("call_typ_cd", "LINE"); //라인
                subdomain = "line".toUpperCase();
            }
            else if(callTypeCd != null && "NTT".equals(callTypeCd)) {
                jsonBody.put("call_typ_cd", "NTT"); //네이버 톡톡
                subdomain = "ntt".toUpperCase();
            }
            else {
                throw new InterfaceException(InterfaceResultCode.FAIL_INVAILD_PARAMETER);
            }
        }
        else {
            jsonBody.put("call_typ_cd", "KAKAO"); //카카오상담톡
            subdomain = "kakao".toUpperCase();

            log.info("===>" + jsonBody.get("user_key") + ": subdomain: " + subdomain);

        }
        //서브도메인 추출
        // - dev -> IP로 체크
        // - production -> subdomain으로 체크
//        String subdomain = 

        //비즈채널 확인
        TwbAspBizChannel aspBizChannel = aspBizChannelService.findByChnClsfCd(subdomain);

        log.info("===> " + jsonBody.get("user_key") + ": 비즈채널 확인");
        log.info("===>" + jsonBody.get("user_key") + ": chnClsfCd: " + aspBizChannel.getChnClsfCd());
        log.info("===>" + jsonBody.get("user_key") + ": chnUri: " + aspBizChannel.getChnUri());
        log.info("===>" + jsonBody.get("user_key") + ": chnNm: " + aspBizChannel.getChnNm());
        log.info("===>" + jsonBody.get("user_key") + ": chnCoNm: " + aspBizChannel.getChnCoNm());

//        //접속 도메인 체크
//        if(!domain.equals(aspBizChannel.getBizServicesUri()))
//        {
//            throw new AspBizChannelServiceURIMismatchException();
//        }

        //고객사비즈채널 확인
        String dsptchPrfKey = "";
        String talkUserKey = "";
        // line / ( kakao , ttalk ) json 구조가 다름  ( 메시지 이벤트일때만 체크 필요 ) 
        if(eventType.equals("message") && jsonBody.getString("call_typ_cd").equals("LINE")) {

            JSONArray events = jsonBody.getJSONArray("events");

            JSONObject obj = events.getJSONObject(0);
            JSONObject sourceObj = obj.getJSONObject("source");

            // 1;1 만 가능함. group room 은 불가 메시지 전송 필요.
            if(sourceObj.getString("type") != null && !sourceObj.getString("type").equals("user")) {

                return jsonBody;
            }

            String userId = sourceObj.getString("userId");
            talkUserKey = userId;

        } else if (jsonBody.getString("call_typ_cd").equals("NTT")) {
        	talkUserKey = jsonBody.getString("user");
        } else {
            talkUserKey = jsonBody.getString("user_key");
        }

        log.info("===> " + jsonBody.get("user_key") + ": 고객사채널관리 서비스 조회");
        log.info("===> " + jsonBody.get("user_key") + ": dsptchPrfKey: " + jsonBody.getString("sender_key"));
        log.info("===> " + jsonBody.get("user_key") + ": aspBizChannel.getChnClsfCd(): " + aspBizChannel.getChnClsfCd());
        dsptchPrfKey = jsonBody.getString("sender_key");
        TwbAspCustChannel aspCustChannel = aspCustChannelService.findByDsptchPrfKeyAndChnClsfCd(dsptchPrfKey, aspBizChannel.getChnClsfCd());

        //고객사 확인
        log.info("===> " + jsonBody.get("user_key") + ": 고객사 확인");
        log.info("===> " + jsonBody.get("user_key") + ": custcoId: " + Integer.toString(aspCustChannel.getCustcoId()));
        log.info("===> " + jsonBody.get("user_key") + ": aspBizChannel.getChnClsfCd(): " + aspBizChannel.getChnClsfCd());
        String custcoId = Integer.toString(aspCustChannel.getCustcoId());
        TwbAspCust aspCust = aspCustService.findByCustcoId(custcoId);

        //고객키 설정

        String userKey = talkUserKey;
        log.info("===> " + jsonBody.get("user_key") + ": 고객키 설정 : " + userKey);

        //정보 세팅
        jsonBody.put("asp_sndrKey", aspCustChannel.getSndrKey());
        jsonBody.put("sndrKey", aspCustChannel.getSndrKey());
        jsonBody.put("custco_id", aspCust.getCustcoId());
        jsonBody.put("biz_service_cd", aspCustChannel.getChnClsfCd());
        jsonBody.put("user_key", userKey);
        jsonBody.put("org_user_key", talkUserKey);
//        jsonBody.put("talk_access_token", aspCustChannel.getTalkAccessToken());	// 라인 데이타 송수신 시 필요 .
//        jsonBody.put("channel_secret", aspCustChannel.getChannelSecret());	// 라인 파라미터검증.

        return jsonBody;
    }


    /**
     * 라인 메신저 파라미터 검증 수행 ( 라인 메시지 API 제공 ) - 라인 : https://line.hcteletalk.com<br/>
     * 
     * @param  request
     * @param  messageJson
     * @return                      orgRcvBody
     * @throws TelewebUtilException
     */
    public void signatureValidation(HttpServletRequest request, JSONObject messageJson, JSONObject orgRcvBody) throws TelewebUtilException
    {

        String channelSecret = messageJson.getString("channel_secret"); // Channel secret string
        if(channelSecret == null || channelSecret.equals("")) {

            throw new AspCustChannelFailLineSignatureException("Asp Cust Channel Secret doesn't exist");
        }

        String httpRequestBody = orgRcvBody.toString(); // Request body string
        SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256");
        Mac mac;
        String signature;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            byte[] source = httpRequestBody.getBytes(StandardCharsets.UTF_8);
            signature = Base64.encode(mac.doFinal(source));
        }
        catch(NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getLocalizedMessage());
            throw new TelewebUtilException(e.getLocalizedMessage());
        }

        String headerSignature = request.getHeader("X-Line-Signature");

        log.debug("signatureValidation header - {} signature {}", signature, headerSignature);

        if(!signature.equals(headerSignature)) { throw new AspCustChannelFailLineSignatureException(); }

    }


    /**
     * extra 파라미터 체크 , 고객사에 따라 extra (추가정보를 ) 배열,string 형태로 정의해서 보낼수 있다. 지정상담배분 시 파라미터 처리
     * "reference":{"extra":"[{"routerTarget" : "test01"}]"} , 암복호화 x
     * 
     * @param  request
     * @param  rcvJson
     * @return                      JSONObject
     * @throws TelewebUtilException
     */
    public JSONArray convertExtraParamToJson(@NotEmpty final JSONObject referenceJson) throws TelewebUtilException
    {
        JSONArray extraJson = new JSONArray();
        JSONObject first = referenceJson.getJSONObject("reference");

        try {
            if(first != null && first.containsKey("extra")) {
                String param = first.getString("extra");
                if(!"".equals(first.getString("extra"))) {
                    // jsonbody 파라미터는 string 으로 전송옴.
                    String extraData = first.getString("extra");
                    if(extraData.startsWith("[")) {			// array 전달 됨.
                        extraJson = JSONArray.fromObject(extraData);
                    }
                    else if(extraData.startsWith("{")) {	// json 객체 
                        extraJson.add(JSONObject.fromObject(extraData));
                    }
                }

            }

        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
        }

        return extraJson;
    }
}
