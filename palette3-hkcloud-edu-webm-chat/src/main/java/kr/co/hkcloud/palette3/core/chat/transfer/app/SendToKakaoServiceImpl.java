package kr.co.hkcloud.palette3.core.chat.transfer.app;


import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import kr.co.hkcloud.palette3.config.properties.proxy.ProxyProperties;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.util.FileDownloadUtils;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;


/**
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("sendToKakaoService")
public class SendToKakaoServiceImpl implements SendToKakaoService
{
    private final FileRulePropertiesUtils fileRulePropertiesUtils;
    private final ProxyProperties         proxyProperties;
    private final PaletteJsonUtils        paletteJsonUtils;
    private final FileDownloadUtils       fileDownloadUtils;


    /**
     * 카카오로 텍스트 전송
     */
    @Override
    public JSONObject sendTextToKakao(final URI writeUri, final JSONObject writeData) throws TelewebAppException
    {
    	log.info("sendTextToKakao >> " + writeData);
        String serial = writeData.getString("CHT_CUTT_DTL_ID");
        if(serial.length()>15) {
        	serial = serial.substring(0,14);
        }
        
        writeData.put("user_key", writeData.getString("CHT_USER_KEY"));
        writeData.put("sender_key", writeData.getString("sndrKey"));
        writeData.put("serial_number", serial + "_" + System.currentTimeMillis());

        if(writeData.containsKey("active")) {
        	if( writeData.getString("active") != "profile" && StringUtils.isEmpty( writeData.get("user_key").toString())) {
                JSONObject er = new JSONObject();
                er.put("code", "-999");
                return er;
            }
        } else {
        	if(StringUtils.isEmpty( writeData.get("user_key").toString())) {
                JSONObject er = new JSONObject();
                er.put("code", "-999");
                return er;
        	}
        }


        StringBuffer sb = new StringBuffer("\n");
        StringBuffer sbf = new StringBuffer("\n");
        sb.append("\tTEXT URL\t:").append(writeUri.toString()).append("\n");
        sb.append("\tSERIAL\t\t:").append(serial).append("\n");
        sb.append("\tDATA\t\t:").append(writeData.toString()).append("\n");

        log.info("\n-------------------\nTEXT SEND INFORMATION\n-------------------\n{}\n", sb.toString());

        JSONObject retJson = null;
        SSLContextBuilder builder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslsf;
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslsf = new SSLConnectionSocketFactory(builder.build());
        }
        catch(NoSuchAlgorithmException | KeyStoreException | KeyManagementException e1) {
            throw new TelewebAppException(e1.getLocalizedMessage(), e1);
        }

        CloseableHttpResponse writeResponse = null;
        HttpEntity writeResponseEntity = null;
//        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        CloseableHttpClient httpclient = null;
        if(proxyProperties.isEnabled()) {
            String proxyDomain = proxyProperties.getDomain();
            int proxyPort = proxyProperties.getPort();
            String proxySchema = proxyProperties.getSchema();
            HttpHost proxy = new HttpHost(proxyDomain, proxyPort, proxySchema);
            log.debug("PROXY INFO:\n\n\tHOST: {}\n\tPORT: {}\n\tSCHEMA: {}\n\n", proxy.getHostName(), proxy.getPort(), proxy.getSchemeName());

            httpclient = HttpClients.custom().setProxy(proxy).setSSLSocketFactory(sslsf).build();
        }
        else {
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        }

        try {
        	log.info("writeData::::::::::" + writeData);
            HttpPost writePost = new HttpPost(writeUri);
            writePost.setHeader("User-Agent", "Mozilla/5.0");
            writePost.setHeader("Accept", "application/json");
            writePost.setHeader("Content-Type", "application/json");

            // access token만 접근 허용 ( line )
            if(writeData.containsKey("talk_access_token") && !"".equals(writeData.getString("talk_access_token"))) {
//                writePost.setHeader("Authorization", "Bearer {" + writeData.getString("talk_access_token") + "}");
                writePost.setHeader("Authorization", writeData.getString("talk_access_token"));
                writeData.remove("talk_access_token");
            } else if(writeData.containsKey("send_api_token") && !"".equals(writeData.getString("send_api_token"))) {
                writePost.setHeader("Authorization", writeData.getString("send_api_token"));
                writeData.remove("send_api_token");
            }

            JSONObject sendData = new JSONObject();

            String json = paletteJsonUtils.jsonToString(writeData.toString());
        	log.info("json::::::::::" + json);

            StringEntity postEntity = new StringEntity(json, StandardCharsets.UTF_8);
            writePost.setEntity(postEntity);
            writeResponse = httpclient.execute(writePost);
            writeResponseEntity = writeResponse.getEntity();
            String writeJsonString = EntityUtils.toString(writeResponseEntity);
            retJson = JSONObject.fromObject(writeJsonString);
        	log.info("retJson::::::::::" + retJson);

            sbf.append("\tSERIAL\t:").append(serial).append("\n");
            sbf.append("\tRETURN\t:").append(retJson.toString()).append("\n");
            // kakao,ttalk 정상 code =0 , line 정상 빈값이옴 (오류 메시지 닮김.)
            if(!retJson.has("code")) {
                if(retJson.isEmpty())
                    retJson.put("code", "0");
                else {
                    retJson.put("code", "-999");
                }
            }
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            log.error("\n-------------------\nTEXT SEND INFORMATION\n-------------------\n{}\n", sb.toString());
            log.error("\n-------------------\nTEXT RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        finally {
            if(log.isDebugEnabled()) {
                log.debug("\n-------------------\nTEXT RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            }
            try {
                EntityUtils.consume(writeResponseEntity);  // 필요함.
                writeResponse.close();
                httpclient.close();
            }
            catch(IOException e) {
                log.error(e.getLocalizedMessage(), e);
//                throw new TelewebAppException(e.getLocalizedMessage(), e);
            }
        }

        
        log.debug("\n-123123123-\n{}\n", retJson.toString());
        return retJson;
    }


    /**
     * 카카오로 텍스트 전송
     */
    @Override
    public JSONObject sendLinkTextToKakao(final URI writeUri, final URI imageUri, final JSONObject writeData) throws TelewebAppException
    {
        String serial = writeData.getString("CHT_CUTT_DTL_ID");
        
        if(serial.length()>15) {
        	serial = serial.substring(0,14);
        }
        
        writeData.put("user_key", writeData.getString("CHT_USER_KEY"));
        writeData.put("sender_key", writeData.getString("sndrKey"));
        writeData.put("serial_number", serial + "_" + System.currentTimeMillis());
        writeData.put("CHT_CUTT_DTL_ID", serial);

        StringBuffer sb = new StringBuffer("\n");
        StringBuffer sbf = new StringBuffer("\n");
        sb.append("\tTEXT URL\t:").append(writeUri.toString()).append("\n");
        sb.append("\tIMAGE URL\t:").append(imageUri.toString()).append("\n");
        sb.append("\tSERIAL\t\t:").append(serial).append("\n");
        sb.append("\tDATA\t\t:").append(writeData.toString()).append("\n");

        if(log.isDebugEnabled()) {
            log.debug("\n-------------------\nLINK TEXT SEND INFORMATION\n-------------------\n{}\n", sb.toString());
            log.info("\n-------------------\nLINK TEXT SEND INFORMATION\n-------------------\n{}\n", sb.toString());
        }

        JSONObject retJson = null;
        SSLContextBuilder builder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslsf;
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslsf = new SSLConnectionSocketFactory(builder.build());
        }
        catch(NoSuchAlgorithmException | KeyStoreException | KeyManagementException e1) {
            throw new TelewebAppException(e1.getLocalizedMessage(), e1);
        }

        CloseableHttpResponse writeResponse = null;
        HttpEntity writeResponseEntity = null;
//        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        CloseableHttpClient httpclient = null;
        if(proxyProperties.isEnabled()) {
            String proxyDomain = proxyProperties.getDomain();
            int proxyPort = proxyProperties.getPort();
            String proxySchema = proxyProperties.getSchema();
            HttpHost proxy = new HttpHost(proxyDomain, proxyPort, proxySchema);
            log.debug("PROXY INFO:\n\n\tHOST: {}\n\tPORT: {}\n\tSCHEMA: {}\n\n", proxy.getHostName(), proxy.getPort(), proxy.getSchemeName());

            httpclient = HttpClients.custom().setProxy(proxy).setSSLSocketFactory(sslsf).build();
        }
        else {
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        }

        try {
            if(writeData.has("FILE_GROUP_KEY") && writeData.has("FILE_KEY")) {
                if(!StringUtils.isEmpty(writeData.getString("FILE_GROUP_KEY")) && !StringUtils.isEmpty(writeData.getString("FILE_KEY"))) {
                    retJson = uploadImageToKakao(imageUri, writeData, "link");
                    if(retJson.containsKey("image")) {
                        writeData.put("image_url", retJson.getString("image"));
                    }
                }
            }

            writeData.put("message_type", "LI");

            HttpPost writePost = new HttpPost(writeUri);
            writePost.setHeader("User-Agent", "Mozilla/5.0");
            writePost.setHeader("Accept", "application/json");
            writePost.setHeader("Content-Type", "application/json");

            // access token만 접근 허용 ( line ) 
            if(writeData.containsKey("talk_access_token") && !"".equals(writeData.getString("talk_access_token"))) {
                writePost.setHeader("Authorization", "Bearer {" + writeData.getString("talk_access_token") + "}");
                writeData.remove("talk_access_token");
            }

            StringEntity postEntity = new StringEntity(writeData.toString(), StandardCharsets.UTF_8);
            writePost.setEntity(postEntity);
            writeResponse = httpclient.execute(writePost);
            writeResponseEntity = writeResponse.getEntity();
            String writeJsonString = EntityUtils.toString(writeResponseEntity);
            retJson = JSONObject.fromObject(writeJsonString);

            sbf.append("\tSERIAL\t:").append(serial).append("\n");
            sbf.append("\tRETURN\t:").append(retJson.toString()).append("\n");

            // kakao,ttalk 정상 code =0 , line 정상 빈값이옴 (오류 메시지 닮김.)
            if(!retJson.has("code")) {
                if(retJson.isEmpty())
                    retJson.put("code", "0");
                else {
                    retJson.put("code", "-999");
                }
            }
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            log.error("\n-------------------\nLINK TEXT SEND INFORMATION\n-------------------\n{}\n", sb.toString());
            log.error("\n-------------------\nLINK TEXT RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        finally {
            if(log.isDebugEnabled()) {
                log.debug("\n-------------------\nLINK TEXT RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            }
            try {
                EntityUtils.consume(writeResponseEntity); // 필요함.
                writeResponse.close();
                httpclient.close();
            }
            catch(IOException e) {
                throw new TelewebAppException(e.getLocalizedMessage(), e);
            }
        }
        return retJson;
    }


    /**
     * 카카오로 이미지 전송
     */
    @Override
    public JSONObject sendImageToKakao(final URI writeUri, final URI imageUri, final JSONObject writeData) throws TelewebAppException
    {
        String serial = writeData.getString("CHT_CUTT_DTL_ID");
        
        if(serial.length()>15) {
        	serial = serial.substring(0,14);
        }
        
        writeData.put("user_key", writeData.getString("CHT_USER_KEY"));
        writeData.put("sender_key", writeData.getString("sndrKey"));
        writeData.put("serial_number", serial + "_" + System.currentTimeMillis());
        writeData.put("CHT_CUTT_DTL_ID", serial);

        StringBuffer sb = new StringBuffer("\n");
        StringBuffer sbf = new StringBuffer("\n");
        sb.append("\tTEXT URL\t:").append(writeUri.toString()).append("\n");
        sb.append("\tIMAGE URL\t:").append(imageUri.toString()).append("\n");
        sb.append("\tSERIAL\t\t:").append(serial).append("\n");
        sb.append("\tDATA\t\t:").append(writeData.toString()).append("\n");

        if(log.isDebugEnabled()) {
            log.info("\n-------------------\nIMAGE SEND INFORMATION\n-------------------\n{}\n", sb.toString());
        }

        JSONObject retJson = null;
        SSLContextBuilder builder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslsf;
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslsf = new SSLConnectionSocketFactory(builder.build());
        }
        catch(NoSuchAlgorithmException | KeyStoreException | KeyManagementException e1) {
            throw new TelewebAppException(e1.getLocalizedMessage(), e1);
        }

        CloseableHttpResponse writeResponse = null;
        HttpEntity writeResponseEntity = null;
//        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        CloseableHttpClient httpclient = null;
        if(proxyProperties.isEnabled()) {
            String proxyDomain = proxyProperties.getDomain();
            int proxyPort = proxyProperties.getPort();
            String proxySchema = proxyProperties.getSchema();
            HttpHost proxy = new HttpHost(proxyDomain, proxyPort, proxySchema);
            log.debug("PROXY INFO:\n\n\tHOST: {}\n\tPORT: {}\n\tSCHEMA: {}\n\n", proxy.getHostName(), proxy.getPort(), proxy.getSchemeName());

            httpclient = HttpClients.custom().setProxy(proxy).setSSLSocketFactory(sslsf).build();
        }
        else {
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        }

        try {
            retJson = uploadImageToKakao(imageUri, writeData, "IM");

            writeData.put("image_url", retJson.getString("image"));
            writeData.put("message_type", "IM");

            HttpPost writePost = new HttpPost(writeUri);
            writePost.setHeader("User-Agent", "Mozilla/5.0");
            writePost.setHeader("Accept", "application/json");
            writePost.setHeader("Content-Type", "application/json");

            StringEntity writeStringEntity = new StringEntity(writeData.toString(), StandardCharsets.UTF_8);
            writePost.setEntity(writeStringEntity);
            writeResponse = httpclient.execute(writePost);
            writeResponseEntity = writeResponse.getEntity();
            String writeJsonString = EntityUtils.toString(writeResponseEntity);
            retJson = JSONObject.fromObject(writeJsonString);

            sbf.append("\tSERIAL\t:").append(serial).append("\n");
            sbf.append("\tRETURN\t:").append(retJson.toString()).append("\n");
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            log.error("\n-------------------\nIMAGE SEND INFORMATION\n-------------------\n{}\n", sb.toString());
            log.error("\n-------------------\nIMAGE RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        finally {
            if(log.isDebugEnabled()) {
                log.debug("\n-------------------\nIMAGE RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            }
            try {
                EntityUtils.consume(writeResponseEntity);
                writeResponse.close();
                httpclient.close();
            }
            catch(IOException e) {
                throw new TelewebAppException(e.getLocalizedMessage(), e);
            }
        }
        return retJson;
    }


    /**
     * 카카오 챗봇으로 전환(문의유형)
     */
    @Override
    public JSONObject endWithBot(final URI boteventUri, final JSONObject writeData) throws TelewebAppException
    {
        String serial = writeData.getString("CHT_CUTT_DTL_ID");
        
        if(serial.length()>15) {
        	serial = serial.substring(0,14);
        }
        
        writeData.put("user_key", writeData.getString("CHT_USER_KEY"));
        writeData.put("sender_key", writeData.getString("sndrKey"));
        writeData.put("serial_number", serial + "_" + System.currentTimeMillis());
        writeData.put("CHT_CUTT_DTL_ID", serial);

        StringBuffer sb = new StringBuffer("\n");
        StringBuffer sbf = new StringBuffer("\n");
        sb.append("\tBOTEVENT URL\t:").append(boteventUri.toString()).append("\n");
        sb.append("\tSERIAL\t\t:").append(serial).append("\n");
        sb.append("\tDATA\t\t:").append(writeData.toString()).append("\n");

        if(log.isDebugEnabled()) {
            log.debug("\n-------------------\nEND WITH BOT INFORMATION\n-------------------\n{}\n", sb.toString());
        }

        JSONObject retJson = null;
        SSLContextBuilder builder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslsf;
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslsf = new SSLConnectionSocketFactory(builder.build());
        }
        catch(NoSuchAlgorithmException | KeyStoreException | KeyManagementException e1) {
            throw new TelewebAppException(e1.getLocalizedMessage(), e1);
        }

        CloseableHttpResponse writeResponse = null;
        HttpEntity writeResponseEntity = null;
//        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        CloseableHttpClient httpclient = null;
        if(proxyProperties.isEnabled()) {
            String proxyDomain = proxyProperties.getDomain();
            int proxyPort = proxyProperties.getPort();
            String proxySchema = proxyProperties.getSchema();
            HttpHost proxy = new HttpHost(proxyDomain, proxyPort, proxySchema);
            log.debug("PROXY INFO:\n\n\tHOST: {}\n\tPORT: {}\n\tSCHEMA: {}\n\n", proxy.getHostName(), proxy.getPort(), proxy.getSchemeName());

            httpclient = HttpClients.custom().setProxy(proxy).setSSLSocketFactory(sslsf).build();
        }
        else {
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        }

        try {
            HttpPost writePost = new HttpPost(boteventUri);
            writePost.setHeader("User-Agent", "Mozilla/5.0");
            writePost.setHeader("Accept", "application/json");
            writePost.setHeader("Content-Type", "application/json");

            StringEntity postEntity = new StringEntity(writeData.toString(), StandardCharsets.UTF_8.name());
            writePost.setEntity(postEntity);
            writeResponse = httpclient.execute(writePost);
            writeResponseEntity = writeResponse.getEntity();
            String writeJsonString = EntityUtils.toString(writeResponseEntity);
            retJson = JSONObject.fromObject(writeJsonString);

            sbf.append("\tSERIAL\t:").append(serial).append("\n");
            sbf.append("\tRETURN\t:").append(retJson.toString()).append("\n");
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            log.error("\n-------------------\nEND WITH BOT SEND INFORMATION\n-------------------\n{}\n", sb.toString());
            log.error("\n-------------------\nEND WITH BOT RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        finally {
            if(log.isDebugEnabled()) {
                log.debug("\n-------------------\nEND WITH BOT RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            }
            try {
                EntityUtils.consume(writeResponseEntity);
                writeResponse.close();
                httpclient.close();
            }
            catch(IOException e) {
                throw new TelewebAppException(e.getLocalizedMessage(), e);
            }
        }
        return retJson;
    }


    /**
     * 카카오로 이미지 업로드
     * 
     * @param imageUri  카카오 이미지 업로드 API URI
     * @param writeData JSON 파라미터
     * @param imgType   카카오 이미지 타입 (IM 타입, LI 타입-link)
     */
    @Override
    public JSONObject uploadImageToKakao(final URI imageUri, final JSONObject writeData, final String imgType) throws TelewebAppException
    {
        String serial = writeData.getString("CHT_CUTT_DTL_ID");
        
        if(serial.length()>15) {
        	serial = serial.substring(0,14);
        }
        
        writeData.put("user_key", writeData.getString("CHT_USER_KEY"));
        writeData.put("sender_key", writeData.getString("sndrKey"));
        writeData.put("serial_number", serial + "_" + System.currentTimeMillis());
        writeData.put("CHT_CUTT_DTL_ID", serial);

        StringBuffer sb = new StringBuffer("\n");
        StringBuffer sbf = new StringBuffer("\n");
        sb.append("\tIMAGE URL\t:").append(imageUri.toString()).append("\n");
        sb.append("\tSERIAL\t\t:").append(serial).append("\n");
        sb.append("\tDATA\t\t:").append(writeData.toString()).append("\n");

        if(log.isDebugEnabled()) {
            log.info("\n-------------------\nIMAGE UPLOAD INFORMATION\n-------------------\n{}\n", sb.toString());
        }

        JSONObject retJson = null;
        SSLContextBuilder builder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslsf;
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslsf = new SSLConnectionSocketFactory(builder.build());
        }
        catch(NoSuchAlgorithmException | KeyStoreException | KeyManagementException e1) {
            throw new TelewebAppException(e1.getLocalizedMessage(), e1);
        }

        CloseableHttpResponse imageResponse = null;
        HttpEntity imageResponseEntity = null;
//        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        CloseableHttpClient httpclient = null;
        if(proxyProperties.isEnabled()) {
            String proxyDomain = proxyProperties.getDomain();
            int proxyPort = proxyProperties.getPort();
            String proxySchema = proxyProperties.getSchema();
            HttpHost proxy = new HttpHost(proxyDomain, proxyPort, proxySchema);
            log.debug("PROXY INFO:\n\n\tHOST: {}\n\tPORT: {}\n\tSCHEMA: {}\n\n", proxy.getHostName(), proxy.getPort(), proxy.getSchemeName());

            httpclient = HttpClients.custom().setProxy(proxy).setSSLSocketFactory(sslsf).build();
        }
        else {
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        }

        try {
            // @formatter:off
            final String imageType = imgType;

            
            // [파일] 테스트 필요 카카오톡 이미지 업로드: 채팅-이미지관리
            final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
            final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지관리

            final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
            log.debug("fileProperties>>>{}", fileProperties);
            
            // 문의유형 이미지 발송시 사용됨, userId system 고정
            final String userId =  writeData.getString("USER_ID");
            FileDbMngSelectRequest fileDbMngSelectRequest;
            if ("system".equals(userId))
            {
                fileDbMngSelectRequest =  FileDbMngSelectRequest.builder()
                    .fileGroupKey(writeData.getString("FILE_GROUP_KEY"))
                    .fileKey(writeData.getString("FILE_KEY"))
                    .build();
            }
            else
            {
                fileDbMngSelectRequest =  FileDbMngSelectRequest.builder()
                    .fileGroupKey(writeData.getString("FILE_GROUP_KEY"))
                    .fileKey(writeData.getString("FILE_KEY"))
                    .build();
            }
                
            FileDownloadResponse fileDownloadResponse = fileDownloadUtils.loadAsResource(fileProperties, fileDbMngSelectRequest);
            
            MultipartEntityBuilder MEbuilder = MultipartEntityBuilder.create()
                                                                     .setMode(HttpMultipartMode.BROWSER_COMPATIBLE); // 웹 호환 모드
            
            final String strBoundary = "----besthcBoundary";    //헤더에 들어갈 Boundary, MultipartEntityBuilder와 헤더는 같은 이름의 Boundary 를 써야함
            MEbuilder.setBoundary(strBoundary);                 // 구분자 문자 설정
            MEbuilder.setCharset(StandardCharsets.UTF_8);     // 인코딩 설정
            MEbuilder.addBinaryBody("image"
                                   , fileDownloadResponse.getResource().getInputStream()
                                   , org.apache.http.entity.ContentType.DEFAULT_BINARY
                                   , fileDownloadResponse.getActlFileNm()); // 프로필 이미지 셋팅
            MEbuilder.addTextBody("sndrKey", writeData.getString("sndrKey"));
            MEbuilder.addTextBody("sender_key", writeData.getString("sndrKey"));
            MEbuilder.addTextBody("image_type", imageType);
            final HttpEntity imageHttpEntity = MEbuilder.build();

            HttpPost imagePost = new HttpPost(imageUri);
            imagePost.setHeader("Content-type", "multipart/form-data;boundary=" + strBoundary); // 컨텐츠 타입 설정
            imagePost.setEntity(imageHttpEntity);

            imageResponse = httpclient.execute(imagePost);
            imageResponseEntity = imageResponse.getEntity();
            String imageJsonString = EntityUtils.toString(imageResponseEntity);
            retJson = JSONObject.fromObject(imageJsonString);

            sbf.append("\tSERIAL\t:").append(serial).append("\n");
            sbf.append("\tRETURN\t:").append(retJson.toString()).append("\n");
            // @formatter:on
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
            log.error("\n-------------------\nIMAGE UPLOAD INFORMATION\n-------------------\n{}\n", sb.toString());
            log.error("\n-------------------\nIMAGE UPLOAD RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        finally {
            if(log.isDebugEnabled()) {
                log.debug("\n-------------------\nIMAGE UPLOAD RESPONSE INFORMATION\n-------------------\n{}\n", sbf.toString());
            }
            try {
                EntityUtils.consume(imageResponseEntity);  //필요함.
                imageResponse.close();
                httpclient.close();
            }
            catch(IOException e) {
                throw new TelewebAppException(e.getLocalizedMessage(), e);
            }
        }
        return retJson;
    }
}
