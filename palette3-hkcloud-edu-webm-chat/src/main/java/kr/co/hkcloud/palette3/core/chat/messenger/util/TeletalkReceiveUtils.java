package kr.co.hkcloud.palette3.core.chat.messenger.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.common.chat.domain.OrgContentVO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.config.properties.palette.enumer.PaletteLicense;
import kr.co.hkcloud.palette3.config.properties.proxy.ProxyProperties;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequests;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileUploadResponse;
import kr.co.hkcloud.palette3.file.util.FileConvertUtils;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import kr.co.hkcloud.palette3.file.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Component
public class TeletalkReceiveUtils
{
    private final TransToKakaoService    transToKakaoService;
    private final TalkDataProcessService talkDataProcessService;
    private final ProxyProperties        proxyProperties;
    private final PaletteProperties      paletteProperties;
    private final ChatProperties         chatProperties;
    private final RestTemplate           restTemplate;
    private final FileUploadUtils        fileUploadUtils;
    private final FileConvertUtils       fileConvertUtils;
    private final FileRuleUtils          fileRuleUtils;


    /**
     * 수신가능 type에 따라 지원가능/불가능 check
     * 
     * @param  type
     * @return
     */
    public boolean isAvailableType(String type, JSONObject rcvJson) throws TelewebUtilException
    {

        PaletteLicense serviceVersion = paletteProperties.getLicense();
        if("photo".equals(type) || "image".equals(type)) {
            //상담설정 관리에서 이미지/장문 설정여부 N
            String strRcvImgYn = HcTeletalkDbEnvironment.getInstance().getString(rcvJson.getString("CUSTCO_ID"), "RECEIVE_IMAGE_YN");
            return (strRcvImgYn == null || strRcvImgYn == "" || "N".equals(strRcvImgYn)) ? false : true;
        }
        else if("video".equals(type)) {
//            //standard 버전의 경우 동영상 지원을 하지 않는것으로 보여준다.
//            switch(serviceVersion)
//            {
//                case STANDARD:
//                {
//                    return false;
//                }
//                default:
//                {
//                    return true;
//                }
//            }
            //상담설정 관리에서 동영상 설정여부 N
            String strRcvVodYn = HcTeletalkDbEnvironment.getInstance().getString(rcvJson.getString("CUSTCO_ID"), "RECEIVE_VIDEO_YN");
            return (strRcvVodYn == null || strRcvVodYn == "" || "N".equals(strRcvVodYn)) ? false : true;
        	
        }
        else if("file".equals(type)) {
            //standard 버전의 경우 파일 지원을 하지 않는것으로 보여준다.
            switch(serviceVersion)
            {
                case STANDARD:
                {
                    return false;
                }
                default:
                {
                    return true;
                }
            }
        }
        else if("text".equals(type)) {
            if(rcvJson.containsKey("attachment") || rcvJson.getString("content").length() > 1000) {
                //장문 수신여부 카카오톡 //네이버톡톡은 1000글자 체크
                String strRcvLongTxtYn = HcTeletalkDbEnvironment.getInstance().getString(rcvJson.getString("CUSTCO_ID"), "RECEIVE_LONG_TXT_YN");
                return (strRcvLongTxtYn == null || strRcvLongTxtYn == "" || "N".equals(strRcvLongTxtYn)) ? false : true;
            }

            return true;
        }
        else if("product".equals(type)) {
        	//네이버톡톡 상품문의
        	return true;
        }
        else if("custInfo".equals(type)) {
        	//카카오톡 고객정보 입력 개발중
        	return true;
        }

        //정의 되지 않은 포맷 , 지원 불가 
        return false;
    }


    /**
     * //고객에게 발송할 수 없다는 시스템 메시지 발송
     * 
     * @param  type
     * @return
     */
    public void noSendSystemMsg(String type, TelewebJSON objParams, JSONObject rcvJson, String callTypCd) throws TelewebUtilException
    {
        String msgId = "21"; // 기본 지원불가 메시지 
        if("photo".equals(type))                   // 이미지 
        {
            msgId = "22";
        }
        else if("video".equals(type))              // video 
        {
            msgId = "23";
        }
        else if("text".equals(type)) {
            if(rcvJson.containsKey("attachment") || rcvJson.getString("content").length() > 1000)      //장문 
            {
                msgId = "24";
            }
        }

        objParams.setString("CONTENT", HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(rcvJson.getString("CUSTCO_ID"), msgId));
        rcvJson.put("content", HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(rcvJson.getString("CUSTCO_ID"), msgId));
        transToKakaoService
            .noSendSystemMsg(rcvJson.getString("user_key"), rcvJson.getString("sndrKey"), HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId(rcvJson.getString("CUSTCO_ID"), msgId), callTypCd);

//        objParams.setString("CONTENT", sysMsgConfig.getMsgContentByMsgId(msgId));
//        rcvJson.put("content", sysMsgConfig.getMsgContentByMsgId(msgId));
//        TransToKakao.noSendSystemMsg(talkDataProcess, rcvJson.getString("user_key"), rcvJson.getString("sndrKey"), sysMsgConfig.getMsgContentByMsgId(msgId), "");
    }


    /**
     * // 영업시간 전송
     * 
     * @param  type
     * @return
     */
    public void sendWorktimeMsg(String type, TelewebJSON objParams, JSONObject rcvJson, String callTypCd) throws TelewebUtilException
    {
        transToKakaoService.sendWorktimeMsg(rcvJson.getString("user_key"), rcvJson.getString("sndrKey"), objParams, callTypCd);
    }


    /**
     * 전송 받은 이미지 저장 ( 서버 repository )
     * 
     * @param  type
     * @return      JSONObject 파일그룹키, 파일키 정보
     */
    public JSONObject savePhototoRepository(JSONObject rcvJson, TelewebJSON objParams, String callTypCd, FilePropertiesResponse fileProperties) throws TelewebUtilException
    {
        JSONObject retJson = new JSONObject();
        String imageTalkUrl = rcvJson.getString("IMAGE_URL");

    	log.info("#@@@@@@@@@@@!@@@@@@@@@@#@@@@@@@@" + rcvJson);
    	log.info("#@@@@@@@@@@@!@@@@@@@@@@#@@@@@@@@" + rcvJson.getString("CUSTCO_ID"));
    	log.info("#@@@@@@@@@@@!@@@@@@@@@@#@@@@@@@@" + objParams);
    	String custcoId = rcvJson.getString("CUSTCO_ID");
        //URI 생성
        URI imageUri;
        try {
            imageUri = new URI(imageTalkUrl);
        }
        catch(URISyntaxException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new TelewebUtilException(e.getLocalizedMessage(), e);
        }

        //임시파일 생성
        File tempFile = restTemplate.execute(imageUri, HttpMethod.GET, null, clientHttpResponse -> {
            File ret = File.createTempFile("image_download", "tmp");
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });

        // 이미지 크기 확인 후 5M 이상이면 수신 불가 메시지 발송
        log.info("_____고객이 보낸 이미지 크기_____" + tempFile.length());
        log.info("_____팔레트 이미지 크기 제한_____" + fileProperties.getMaxFilesize().toBytes());
        if(tempFile.length() > fileProperties.getMaxFilesize().toBytes()) {
            TelewebJSON msgJson = HcTeletalkDbSystemMessage.getInstance().getTelewebJsonBySystemMsgId(rcvJson.getString("CUSTCO_ID"), "25");

            transToKakaoService.sendSystemMsg(rcvJson.getString("user_key"), rcvJson.getString("sndrKey"), msgJson, callTypCd);
            log.info("_____시스템 메시지 발송 완료_____" + fileProperties.getMaxFilesize().toBytes());

            objParams.setString("SYS_MSG", msgJson != null ? msgJson.getString("MSG_CN") : "");
            objParams.setString("SYS_MSG_ID", "25");
            //파일 용량이 커서 파일을 저장하지 않음
            retJson.put("FILE_GROUP_KEY", "NO_FILE");
            retJson.put("FILE_KEY", "NO_FILE");
            return retJson;
        }

        // @formatter:off
        //파일 to 멀티파트 변환
        final MultipartFile file = fileConvertUtils.toMultipartFile(tempFile);

        switch(fileProperties.getTrgtTypeCd())
        {
            default:
            {
                final MultipartFile[] files = {file};
                
                //그룹키 생성 (있으면 사용하고, 없으면 생성한다)
                String fileGroupKey = fileRuleUtils.ifCreatFileGroupKey(new FileUploadRequests());
                FileUploadRequests fileUploadRequests = FileUploadRequests.builder()
                                                                          .userfiles(files)
                                                                          .custcoId(custcoId)
                                                                          .fileGroupKey(fileGroupKey)
                                                                          .userId(2)
                                                                          .build();
                
                //파일 저장
                List<FileUploadResponse> fileUploadResponseList = fileUploadUtils.store(fileProperties, fileUploadRequests);
                retJson.put("FILE_GROUP_KEY", fileUploadResponseList.get(0).getFileGroupKey());
                retJson.put("FILE_KEY", fileUploadResponseList.get(0).getFileKey());

                //주소 생성 (/파일그룹키/파일키)
//                String imgLocalPath = new StringBuffer("/").append(fileUploadResponse.getFileGroupKey()).append("/").append(fileUploadResponse.getFileKey()).toString();
                break;
            }
        }
        // @formatter:on
        return retJson;

    }


    /**
     * 전송 받은 이미지 저장 ( DB BLOB )
     * 
     * @param  type
     * @return
     */
    public void savePhototoBlob(JSONObject rcvJson, TelewebJSON objParams, String callTypCd, FilePropertiesResponse fileProperties) throws TelewebUtilException
    {
        this.savePhototoRepository(rcvJson, objParams, callTypCd, fileProperties);

//        String imageTalkUrl = rcvJson.getString("IMAGE_URL");
//        String custcoId = rcvJson.getString("custco_id");
//        byte[] byteImage = null;
//
//        int intPos = imageTalkUrl.indexOf("?");
//        String tmpImageTalkUrl = "";
//        if(intPos > 0) {
//            tmpImageTalkUrl = imageTalkUrl.substring(0, intPos);
//        }
//        else {
//            tmpImageTalkUrl = imageTalkUrl;
//        }
//
//        ConnectionConfig connConfig = ConnectionConfig.custom().setBufferSize(1024 * 8).build();
//        CloseableHttpClient imgClient = null;
//        if(proxyProperties.isEnabled()) {
//            String proxyDomain = proxyProperties.getDomain();
//            int proxyPort = proxyProperties.getPort();
//            String proxySchema = proxyProperties.getSchema();
//            HttpHost proxy = new HttpHost(proxyDomain, proxyPort, proxySchema);
//            log.debug("PROXY INFO:\n\n\tHOST: {}\n\tPORT: {}\n\tSCHEMA: {}\n\n", proxy.getHostName(), proxy
//                .getPort(), proxy.getSchemeName());
//
//            imgClient = HttpClients.custom().setProxy(proxy).setDefaultConnectionConfig(connConfig).build();
//        }
//        else {
//            imgClient = HttpClients.custom().setDefaultConnectionConfig(connConfig).build();
//        }
//
//        HttpResponse imgResponse = null;
//        HttpEntity imgResEntity = null;
//        HttpGet imgGet = new HttpGet(imageTalkUrl);
//
//        try {
//            imgResponse = imgClient.execute(imgGet);
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//        }
//        imgResEntity = imgResponse.getEntity();
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            IOUtils.copy(imgResEntity.getContent(), baos);
//        }
//        catch(UnsupportedOperationException | IOException e) {
//            e.printStackTrace();
//        }
//
//        byteImage = baos.toByteArray();
//        DataSize byteImage1 = DataSize.ofBytes(byteImage.length);
//        DataSize mgByteImage = DataSize.ofMegabytes(byteImage1.toBytes());
//
//        // 이미지 크기 확인 후 5M 이상이면 수신 불가 메시지 발송
//        if(byteImage.length > fileProperties.getMaxFilesize().toBytes()) {
//            TelewebJSON msgJson = HcTeletalkDbSystemMessage.getInstance()
//                .getTelewebJsonBySystemMsgId(rcvJson.getString("CUSTCO_ID"), "25");
//            
//            transToKakaoService
//                .sendSystemMsg(rcvJson.getString("user_key"), rcvJson.getString("sndrKey"), msgJson, callTypCd);
//
//            objParams.setString("SYS_MSG", msgJson != null ? msgJson.getString("MSG_CN") : "");
//            objParams.setString("SYS_MSG_ID", "25");
//            return;
//        }
//        String orgFileId = innbCreatCmmnService.getSeqNo("TWB_SEQ_FILE");
//
//        OrgFileVO orgFileVO = new OrgFileVO();
//        orgFileVO.setCustcoId(custcoId);
//        orgFileVO.setOrgFileId(orgFileId);
//        orgFileVO.setOrgFile(byteImage);
//        orgFileVO.setFileExts(tmpImageTalkUrl.substring(tmpImageTalkUrl.lastIndexOf('.') + 1));
//        talkDataProcess.insertOrgFile(orgFileVO);
//
//        objParams.setString("IMAGE_URL", imageTalkUrl);
//        objParams.setString("IMAGE_TALK_PATH", "");
//        objParams.setString("ORG_FILE_ID", orgFileId);
//
//        rcvJson.put("IMAGE_URL", imageTalkUrl);
//        rcvJson.put("IMAGE_TALK_PATH", "");
//        rcvJson.put("ORG_FILE_ID", orgFileId);

    }


    /**
     * 전송 받은 이미지 저장 ( 서버 repository )
     * 
     * @param  type
     * @return
     */
    public void savePhototoRepositoryGetMethod(JSONObject rcvJson, TelewebJSON objParams, String callTypCd, FilePropertiesResponse fileProperties) throws TelewebUtilException
    {
        String msgId = rcvJson.getString("messageId");

//      String imageTalkUrl = "https://api-data.line.me/v2/bot/message/"+msgId+"/content"; // rcvJson.getString("IMAGE_URL");
        String imageTalkUrl = chatProperties.getMessenger().getLine().getUrls().getBotMessageContent().toString();
        imageTalkUrl = String.format(imageTalkUrl, msgId);

        rcvJson.put("content", imageTalkUrl);
        rcvJson.put("IMAGE_URL", imageTalkUrl);
        rcvJson.put("TYPE", "photo");

        objParams.setString("CONTENT", imageTalkUrl);
        objParams.setString("IMAGE_URL", imageTalkUrl);

        this.savePhototoRepository(rcvJson, objParams, callTypCd, fileProperties);

    }


    /**
     * 장문 저장
     * 
     * @param  rcvJson
     * @throws TelewebUtilException
     */
    public void insertOrgContent(JSONObject rcvJson, TelewebJSON objParams, OrgContentVO orgContentVO) throws TelewebUtilException
    {
        String url = ((JSONObject) rcvJson.get("attachment")).getString("url");

        HttpPost post = new HttpPost(url);

//        CloseableHttpClient httpclient = HttpClients.custom().build();
        CloseableHttpClient httpclient = null;
        if(proxyProperties.isEnabled()) {
            String proxyDomain = proxyProperties.getDomain();
            int proxyPort = proxyProperties.getPort();
            String proxySchema = proxyProperties.getSchema();
            HttpHost proxy = new HttpHost(proxyDomain, proxyPort, proxySchema);
            log.debug("PROXY INFO:\n\n\tHOST: {}\n\tPORT: {}\n\tSCHEMA: {}\n\n", proxy.getHostName(), proxy.getPort(), proxy.getSchemeName());

            httpclient = HttpClients.custom().setProxy(proxy).build();
        }
        else {
            httpclient = HttpClients.custom().build();
        }

        // restTemplate 으로 변경 가능한 지 / 오류 처리 체크
        CloseableHttpResponse imgResponse = null;
        HttpEntity responseEntity = null;
        try {
            imgResponse = httpclient.execute(post);
            responseEntity = imgResponse.getEntity();
            String x = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            String subContent = x.length() >= 1000 ? x.substring(0, 1000) : x;

            rcvJson.put("content", subContent);
            rcvJson.put("org_content", x);

            objParams.setString("LINKS", url);
            objParams.setString("CONTENT", subContent);
            objParams.setString("ORG_CONTENT", x);

            //상담내용 원본 저장 ( 장문인경우만 저장 하도록 )
            orgContentVO.setOrgContent(x);
            talkDataProcessService.insertOrgContent(orgContentVO);

            objParams.setString("ORG_CONT_ID", orgContentVO.getOrgContId());
            ((JSONObject) rcvJson.get("attachment")).put("org_cont_id", orgContentVO.getOrgContId());

        }
        catch(ParseException e) {
            log.error(e.getMessage(), e);
        }
        catch(IOException e) {
            log.error(e.getMessage(), e);
        }
        finally {
            try {
                EntityUtils.consume(responseEntity);
            }
            catch(Exception e) {
                log.error(e.getMessage(), e);
            }
            try {
                imgResponse.close();
            }
            catch(Exception e) {
                log.error(e.getMessage(), e);
            }
            try {
                httpclient.close();
            }
            catch(Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    /**
     * 장문 저장 (1000 자이상 저장)
     * 
     * @param  rcvJson
     * @throws TelewebUtilException
     */
    public void insertOrgContentWithoutUrl(JSONObject rcvJson, TelewebJSON objParams, OrgContentVO orgContentVO) throws TelewebUtilException
    {
        String orgContent = objParams.getString("CONTENT");
        String subContent = orgContent.length() >= 1000 ? orgContent.substring(0, 1000) : orgContent;

        rcvJson.put("content", subContent);
        rcvJson.put("org_content", orgContent);

        objParams.setString("LINKS", "");
        objParams.setString("CONTENT", subContent);
        objParams.setString("ORG_CONTENT", orgContent);

        //상담내용 원본 저장 ( 장문인경우만 저장 하도록 )
        orgContentVO.setOrgContent(orgContent);
        talkDataProcessService.insertOrgContent(orgContentVO);

        objParams.setString("ORG_CONT_ID", orgContentVO.getOrgContId());
        objParams.setString("LINKS", orgContentVO.getOrgContId());

        rcvJson.put("attachment", new JSONObject());
        ((JSONObject) rcvJson.get("attachment")).put("org_cont_id", orgContentVO.getOrgContId());
        ((JSONObject) rcvJson.get("attachment")).put("url", orgContentVO.getOrgContId());

    }


    /**
     * post 방식으로 넘어온 extra , array 파라미터를 넘기도록 가이드 해야함.
     * 
     * @param  rcvJson
     * @throws TelewebUtilException
     */
    public JSONObject postExtraParamMapping(JSONArray txtJsonArray) throws TelewebUtilException
    {
        JSONObject result = new JSONObject();
        String content = new String();

        // 지정상담배분 시 파라미터 처리 "reference":{"extra":"[{"routerTarget" : "test01"}]"} , 암복호화 x
        if(txtJsonArray != null && txtJsonArray.size() > 0) {

            result = (JSONObject) txtJsonArray.get(0);

            // 지정상담배분 시 파라미터 처리 "reference":{"extra":"[{"routerTargetEnc" : "test01"}]"} , 암복호화 0
            if(txtJsonArray.contains("ROUTERTARGET_ENC")) {
                content = txtJsonArray.getJSONObject(0).getString("ROUTERTARGET_ENC");
                // 복호화 수행 

                result.put("routerTarget", content);

            }
        }

        return result;
    }


    /**
     * get 방식으로 넘어온 extra , string 파라미터를 받음.
     * 
     * @param  rcvJson
     * @throws TelewebUtilException
     */
    public JSONObject getExtraParamMapping(JSONObject first) throws TelewebUtilException
    {
        // Obj 넘어옴 ( get 방식 ) , 암호화 필요 ( 고객사 협의 ) -> 지정상담사 아이디로 사용 
        String content = first.getString("extra");
        JSONObject result = new JSONObject();
        //예시 - https://bizmessage.kakao.com/chat/open/@nze3mzc4mwjlc3r?extra=G__INQRY__20200811143154567INQRY124460
        //    	예시 - https://bizmessage.kakao.com/chat/open/@nze3mzc4mwjlc3r?extra=G__ROUTERTARGET__20200811143154567INQRY124460
        // 암호화 넘어옴 

        if(content != null && content.startsWith("G__")) {
            result.put(content.split("__")[1], content.split("__")[2]);
        }
        else {
            result.put("content", content);
        }

        return result;
    }

}
