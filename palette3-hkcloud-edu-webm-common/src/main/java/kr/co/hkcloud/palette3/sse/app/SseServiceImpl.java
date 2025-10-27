package kr.co.hkcloud.palette3.sse.app;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.sse.message.model.SseMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * packageName    : kr.co.hkcloud.palette3.sse.app
 * fileName       : SseServiceImpl
 * author         : KJD
 * date           : 2023-11-10
 * description    : SSE서버로의 메시지 전송.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-10        KJD       최초 생성
 */
@Slf4j
@RequiredArgsConstructor
@Service("sseService")
public class SseServiceImpl implements SseService{


    @Value("${sse.message.url}")
    private String sseMessageUrl;

    @Override
    public void sendMessage(@NonNull SseMessage.MessageType type, @NonNull String sender, @NonNull String receiver, @NonNull String message, @NonNull final String toastShowPosition,
        final int toastShowSecond) {
        final String roomId = TenantContext.getCurrentTenant() + "_" + TenantContext.getCurrentCustco();
        SseMessage sseMessage = new SseMessage();

        sseMessage.setType(type);
        sseMessage.setRoomId(roomId);
        sseMessage.setSender(sender);
        sseMessage.setReceiver(receiver);
        sseMessage.setMessage(message);
        sseMessage.setPos(toastShowPosition);
        sseMessage.setSecond(toastShowSecond);

        try {
            sendMessage(sseMessage);
        } catch (Exception e) {
            log.error("failed to send SSE Message. roomId: {}, sender: {},  receiver: {}, message: {}, errorMessage: {}", roomId, sender, receiver, message, e.getMessage());
        }
    }


    @Override
    public ResponseEntity<String> sendMessage(SseMessage message) throws IOException, ParseException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("type", message.getType().toString() );
        parameters.add("sender", message.getSender() );
        parameters.add("receiver", message.getReceiver() );
        parameters.add("roomId", message.getRoomId() );
        parameters.add("second", String.valueOf(message.getSecond()) );
        parameters.add("pos", String.valueOf(message.getPos()) );
        parameters.add("message", message.getMessage() );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Palette3AK {api key}");
        HttpEntity formEntity = new HttpEntity<>(parameters, headers);

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(3000); //타임아웃 설정 3초
        factory.setReadTimeout(3000);//타임아웃 설정 3초
        RestTemplate restTemplate = new RestTemplate(factory);
        ResponseEntity<String> response =  restTemplate.postForEntity(sseMessageUrl, formEntity, String.class);
        //log.info("SseService.send :: response.getStatusCode() :: " + response.getStatusCode());
        //log.info("SseService.send :: response.getBody() :: " + response.getBody());
        return response;
    }
}
