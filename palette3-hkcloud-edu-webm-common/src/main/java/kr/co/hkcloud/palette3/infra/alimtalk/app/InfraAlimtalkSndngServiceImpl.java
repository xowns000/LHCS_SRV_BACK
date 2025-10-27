package kr.co.hkcloud.palette3.infra.alimtalk.app;


import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.properties.alimtalk.AlimtalkProperties;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("InfraAlimtalkSndngService")
public class InfraAlimtalkSndngServiceImpl implements InfraAlimtalkSndngService
{

    private final PaletteJsonUtils   paletteJsonUtils;
    private final AlimtalkProperties alimtalkProperties;


    /**
     * <pre>
     * 업무구분 : 알림톡 전송
     * 파 일 명 : InfraAlimtalkSndngServiceImpl.java
     * 작 성 자 : 현은지
     * 작 성 일 : 2021.04.30
     * 설    명 : 알림톡 전송 서비스 구현체
     * </pre>
     */
    @Override
    @Transactional(readOnly = false)
    public JSONObject trnsmisAlimtalk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException
    {
        JSONObject retJson = new JSONObject();
        JSONObject PostData = new JSONObject();
        SSLContextBuilder builder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslsf;
        try {
            sslsf = new SSLConnectionSocketFactory(builder.build());
        }
        catch(KeyManagementException | NoSuchAlgorithmException e) {
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        String writeJsonString = "";
        HttpResponse writeResponse = null;
        HttpEntity writeResponseEntity = null;

        URI apiurl = alimtalkProperties.getUrls().getTargetUrl();
        String auth_code = alimtalkProperties.getAuthCode();
        String sndrKey = alimtalkProperties.getSenderkey();
        boolean is_tran_type = alimtalkProperties.isTranType();
        boolean is_test_mode = alimtalkProperties.isTestMode();

        //알림톡 전송 restTemplate으로 변경 필요
        HttpPost writePost = new HttpPost(apiurl);

        writePost.setHeader("Content-type", "application/json");
        writePost.setHeader("Accept", "application/json");

        PostData.put("auth_code", auth_code);
        PostData.put("sndrKey", sndrKey);
        PostData.put("callback_number", mjsonParams.getString("CUST_TEL_NO"));
        PostData.put("phone_number", mjsonParams.getString("CUST_TEL_NO"));
        PostData.put("message", mjsonParams.getString("NTCN_TALK_CNTN"));
        PostData.put("template_code", "test201912091836"); // 추후 템플릿 관련 변경 필요 ( 테스트용)
        PostData.put("tran_type", is_tran_type ? "Y" : "N"); //협의후 변동가능 프로퍼티
        PostData.put("test_mode", is_test_mode ? "Y" : "N"); // 카카오알릶톡 개발서버 지원 중지 > 운영으로 api 요청

        String json = paletteJsonUtils.jsonToString(PostData.toString());
        StringEntity postEntity = new StringEntity(json, StandardCharsets.UTF_8);

        writePost.setEntity(postEntity);
        try {
            writeResponse = httpclient.execute(writePost);
        }
        catch(IOException e) {
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }

        writeResponseEntity = writeResponse.getEntity();
        try {
            writeJsonString = EntityUtils.toString(writeResponseEntity);
        }
        catch(ParseException | IOException e) {
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        retJson = JSONObject.fromObject(writeJsonString);

        return retJson;

    }
}