package kr.co.hkcloud.palette3.fcm.app;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

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

import kr.co.hkcloud.palette3.fcm.message.FcmMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("fcmService")
public class FcmServiceImpl implements FcmService {

    @Value("${fcm.message.url}")
    private String fcmMessageUrl;

    @Override
    public ResponseEntity<String> sendMessage(
        FcmMessage message) throws IOException, ParseException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        String sUrl = fcmMessageUrl + "/send/message";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("token", message.getToken()); //받는 사람
        parameters.add("title", message.getTitle()); //제목
        parameters.add("body", message.getBody()); //내용

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", message.getToken());
        HttpEntity formEntity = new HttpEntity<>(parameters, headers);

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(3000); //타임아웃 설정 3초
        factory.setReadTimeout(3000);//타임아웃 설정 3초
        RestTemplate restTemplate = new RestTemplate(factory);
        ResponseEntity<String> response = restTemplate.postForEntity(sUrl, formEntity, String.class);
        return response;
    }

    @Override
    public ResponseEntity<String> getFirebaseToken(
        String jwtToken) throws IOException, ParseException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        String sUrl = fcmMessageUrl + "/getFirebaseToken";
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", jwtToken);
        HttpEntity formEntity = new HttpEntity<>(parameters, headers);

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(3000); //타임아웃 설정 3초
        factory.setReadTimeout(3000);//타임아웃 설정 3초
        RestTemplate restTemplate = new RestTemplate(factory);
        ResponseEntity<String> response = restTemplate.postForEntity(sUrl, formEntity, String.class);

        System.out.println("response.body==========>" + response.getBody());
        return response;
    }
}
