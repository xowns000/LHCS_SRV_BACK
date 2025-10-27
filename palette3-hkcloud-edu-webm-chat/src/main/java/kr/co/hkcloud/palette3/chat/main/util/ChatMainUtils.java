package kr.co.hkcloud.palette3.chat.main.util;


import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.config.security.properties.ChatSecurityProperties;
import kr.co.hkcloud.palette3.core.security.crypto.Base64;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebMainParamSignatureException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class ChatMainUtils
{
    private final ChatSecurityProperties chatSecurityProperties;

//    public void signatureValidation(LinkedHashMap<Object, Object> headerobj , JSONObject bodyobj) throws TelewebUtilException {
//    	
//    	String privateKey = chatSecurityProperties.getChatMain().getParamKey();
////    	String channelSecret = "aa10ece34e13340b7c9caf7a40f31829"; // ex
//    	
//    	String httpRequestBody = bodyobj.toString(); // Request body string
//    	SecretKeySpec key = new SecretKeySpec(privateKey.getBytes(), "HmacSHA512");
//    	Mac mac = Mac.getInstance("HmacSHA512");
//    	mac.init(key);
//    	byte[] source = httpRequestBody.getBytes("UTF-8");
//    	String signature 		= Base64.encode(mac.doFinal(source));
//    	String headerSignature 	= (String)headerobj.get("x-teletalk-signature");
//    	
//    	log.debug("signatureValidation httpRequestBody - {} header - {} signature {}", httpRequestBody, signature, headerSignature);
//    	
//    	if(!signature.equals(headerSignature)) {
//    		throw new MainParamSignatureException();
//    	}
//    	
//    }


    public void signatureValidation(String headerSignature, String httpRequestBody) throws TelewebUtilException
    {

        String privateKey = chatSecurityProperties.getChatMain().getParamKey();

//    	String httpRequestBody = bodyJson.toString();
//        
//    	httpRequestBody = jsonUtils.slashToValue(httpRequestBody);

        SecretKeySpec key = new SecretKeySpec(privateKey.getBytes(), "HmacSHA512");
        Mac mac;
        String signature = null;
        try {
            mac = Mac.getInstance("HmacSHA512");
            mac.init(key);
            byte[] source = httpRequestBody.getBytes(StandardCharsets.UTF_8);
            signature = Base64.encode(mac.doFinal(source));
        }
        catch(NoSuchAlgorithmException | InvalidKeyException e) {
            throw new TelewebUtilException(e.getLocalizedMessage());
        }

        log.debug("signatureValidation httpRequestBody - {} header - {} signature {}", httpRequestBody, signature, headerSignature);

        if(!signature.equals(headerSignature)) { throw new TelewebMainParamSignatureException(); }

    }
}
