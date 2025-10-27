package kr.co.hkcloud.palette3.infra.tplex.cti.util;


import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * http 통신을 위한 RestTemplate 유틸  사용하는데 확인 필요
 * 
 * @author R&D
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class IvrRestTemplateUtils
{

    private final RestTemplate      restTemplate;
    private final PaletteProperties paletteProperties;


    public String sendRestTemplate(MultiValueMap<String, String> parameters)
    {
        // http 통신 결과가 들어오는 문자열 변수
        String result = null;
        // http 통신의 대상이 되는 URL (CTI서버) 

        //String Uri = "http://121.67.187.236:60080/API/";
        // Properties 가져오는 것으로 변경
        String Uri = paletteProperties.getCtiServer().getUrl() + ":" + paletteProperties.getCtiServer().getRecordingPort() + "/API/";
        // http 통신의 헤더값
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0");
        headers.add("Accept-Charset", StandardCharsets.UTF_8.toString());

        // http 통신의 value값
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        //POST 방식으로 cti 서버로 리퀘스트를 전송한다. 인자 (URL, HttpEntity request, 받을 클래스 형식);
        result = restTemplate.postForObject(URI.create(Uri), request, String.class);

        return result;
    }


    public String sendRestObjectTemplate(MultiValueMap<String, Object> parameters)
    {
        // http 통신 결과가 들어오는 문자열 변수
        String result = null;
        // http 통신의 대상이 되는 URL (CTI서버) 

        //String Uri = "http://121.67.187.236:60080/API/";
        // Properties 가져오는 것으로 변경
        String Uri = paletteProperties.getCtiServer().getUrl() + ":" + paletteProperties.getCtiServer().getRecordingPort() + "/API/";
        // http 통신의 헤더값
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0");
        headers.add("Accept-Charset", StandardCharsets.UTF_8.toString());

        /*
         * System.out.println("parametersparametersparametersparametersparametersparametersparametersparametersparametersparametersparameters");
         * System.out.println("parametersparametersparametersparametersparametersparametersparametersparametersparametersparametersparameters");
         * System.out.println("parametersparametersparametersparametersparametersparametersparametersparametersparametersparametersparameters"); System.out.println("parameters 2-1 : " + String.valueOf(parameters));
         */

        // http 통신의 value값
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, headers);

        //POST 방식으로 cti 서버로 리퀘스트를 전송한다. 인자 (URL, HttpEntity request, 받을 클래스 형식);
        //result = restTemplate.postForObject(URI.create(Uri), request, String.class);
        result = "";
        return result;
    }

}
