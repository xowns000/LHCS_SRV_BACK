package kr.co.hkcloud.palette3.core.cron.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.core.chat.busy.dao.TalkBusyDAO;
import kr.co.hkcloud.palette3.core.chat.busy.util.TalkBusyUtils;
import kr.co.hkcloud.palette3.core.chat.messenger.domain.ChatOnMessageEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngInsertRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequests;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.RuleFilenameAndExtentResponse;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
@Slf4j
@RequiredArgsConstructor
@Service("cronChatEmailService")
public class CronChatEmailServiceImpl implements CronChatEmailService {

    private final FileRuleUtils    fileRuleUtils;
    private final FileDbMngService fileDbMngService;
    private final InnbCreatCmmnService innbCreatCmmnService;
    
    private final TalkBusyUtils             talkBusyUtils;
    private final TalkBusyDAO               busyDAO;
    
    private final TwbComDAO mobjDao;
    
    private final ApplicationEventPublisher eventPublisher;
    
    private String namespace = "kr.co.hkcloud.palette3.core.cron.dao.CronChatEmailMapper";
    
    
    @Autowired
    private HcTeletalkEnvironment env;
    
    @Value("${stomp.allow.origin}")
    public String SERVER_DOMAIN;
    
    private String logDevider = "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-";
    
    
   
    
    /**
     * 채팅_상담_이메일 수집
     */
    @Override
    public TelewebJSON collectChatCuttEmail(TelewebJSON emailSettingParams) throws TelewebAppException {
        //
        TelewebJSON retObj = new TelewebJSON();
        try {
            int emailSettingCount = emailSettingParams.getHeaderInt("TOT_COUNT");
            if(emailSettingCount > 0) {
                JSONObject emailSettingObj = (JSONObject)emailSettingParams.getDataObject().get(0);
                String custcoId = emailSettingObj.getString("CUSTCO_ID");
                
                
                //휴일이 아닐때 수집
                if(!"N".equals(emailSettingObj.getString("HLDY_YN"))) {
                    //미수집 - 휴일
                    retObj.setString("JOB_SCS_YN", "Y");
                    retObj.setString("JOB_RSLT_MSG", "미수집 - 휴일");
                } else if(!talkBusyUtils.isAvailableWorkTime(custcoId)) {
                    //미수집 - 채팅 상담 시간 아님
                    retObj.setString("JOB_SCS_YN", "Y");
                    retObj.setString("JOB_RSLT_MSG", "미수집 - 채팅 상담 시간 아님");
                } else if("Y".equals(busyDAO.selectLunchTime("", "", custcoId))) {
                    //미수집 - 상담원 점심시간
                    retObj.setString("JOB_SCS_YN", "Y");
                    retObj.setString("JOB_RSLT_MSG", "미수집 - 상담원 점심시간");
                } else {
                    log.info("+=+=+=+=+=+=+=+=+=+=+=+=___이메일 수집___ ::: Start");
                    String dsptchPrfNm = emailSettingObj.getString("DSPTCH_PRF_NM");
                    String host = emailSettingObj.getString("SRVR");
                    log.info("+=+=+=+=+=+=+=+=+=+=+=+=___CUSTCO_ID___ ::: " + custcoId);
                    log.info("+=+=+=+=+=+=+=+=+=+=+=+=___DSPTCH_PRF_NM___ ::: " + dsptchPrfNm);
                    log.info("+=+=+=+=+=+=+=+=+=+=+=+=___SRVR___ ::: " + host);
                    
                    retObj = collectEmail(emailSettingObj);
                    log.info("+=+=+=+=+=+=+=+=+=+=+=+=___이메일 수집___ ::: End");
                }
                
            }
        } catch(Exception e) {
            retObj.setString("JOB_SCS_YN", "N");
            retObj.setString("JOB_RSLT_MSG", e.toString());
        }
        
        if(!retObj.containsKey("JOB_SCS_YN")) {
            retObj.setString("JOB_SCS_YN", "Y");
        }
        
        return retObj;
        
    }
    
    
    /**
     * 고객사_채널_이메일_설정 목록 조회
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCustcoChannelEmailSettingList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectCustcoChannelEmailSettingList", jsonParams);
    }
    
    
    /**
     * 채팅_상담_이메일 - 마지막 수신 일시 조회
     * @param paramJson
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    private TelewebJSON selectMaxRcptnDt(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectMaxRcptnDt", jsonParams);
    }
    
    
    /**
     * 이메일 수집
     * @param settingObj
     * @return
     */
    private TelewebJSON collectEmail(JSONObject settingObj) {
        TelewebJSON retObj = new TelewebJSON();
        //다음 : 앱 비밀번호 생성해야 함 - https://imweb.me/faq?mode=view&category=29&category2=34&idx=72050
        //이메일 세션 설정(imap or imaps)
//        String user = "hjh@hkcloud.co.kr";
//        String password = "gothf1010!!";
//        String host = "mail.hkcloud.co.kr";
//        String port = "143";
//        String protocol = "imap";
//        String sslYn = "N";
//        String custcoId = "1";
//        String aspCustKey = "pallet3";
        String sndrKey = settingObj.getString("SNDR_KEY");
        String custcoId = settingObj.getString("CUSTCO_ID");
        String host = settingObj.getString("SRVR");
        String port = settingObj.getString("PORT");
        String user = settingObj.getString("ACNT");
        String password = settingObj.getString("PSWD");
        String protocol = settingObj.getString("PROTOCOL_CD");
        String chnClsfCd = settingObj.getString("CHN_CLSF_CD");
        
        String logPrefix = logDevider + ".collectEmail" + "___" + sndrKey + "___"+ custcoId + "___ ::: ";
        
        // Setup properties for the mail session.
        Properties props = new Properties();
        if ( "imaps".equals(protocol) ) {
            props.put("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.imaps.socketFactory.fallback", "false");
            props.put("mail.imaps.socketFactory.port", port);
        }

        props.put("mail.store.protocol", protocol);
        props.put("mail." + protocol + ".host", host);
        props.put("mail." + protocol + ".port", port);
//        props.put("mail." + protocol + ".partialfetch", "false");
//        props.put("mail.mime.base64.ignoreerrors", "true");
        
        Store store = null;
        Folder inbox = null;
        
        String mailUid = null;
        String mailSubject = null;
        try {

            // 이전배치에서 CHT_CUTT_ID 적용안된 경우 다시 처리하기 위함.
            this.retryChatOnMessageEvent();

            // Creating mail session.
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);
            store = session.getStore(protocol);
            store.connect(host, user, password);
    
            // 5. Get folder and open the INBOX folder in the store.
            inbox = store.getFolder("INBOX");
            if("imap.daum.net".equals(host)) {
                inbox.open(Folder.READ_WRITE);
            } else {
                inbox.open(Folder.READ_ONLY);
            }
//            SearchTerm dateTerm = null;
            SearchTerm searchTerm = getStartDateTerm(sndrKey);
            
            // 최근 1일 동안의 메일 검색
            //        long oneDayInMillis = 24 * 60 * 60 * 1000 * 1; // 1일의 밀리초
            //        long sinceTime = System.currentTimeMillis() - oneDayInMillis;
            //        SearchTerm searchTerm = new ReceivedDateTerm(ComparisonTerm.GT, new java.util.Date(sinceTime));
    
            Message[] messages = null;
    
//            log.info("inbox.search(dateTerm) ::: ");
            //            messages = inbox.getMessages(); //전체
            messages = inbox.search(searchTerm);
            log.info("inbox.search(searchTerm, messages) messages.length ::: " + messages.length);
            int collectCount = 0;
            if (messages != null) {
                // 6. Retrieve the messages from the folder.
                for (Message message : messages) {
                    mailSubject = "";
                    mailUid = "";
                    mailSubject = message.getSubject();
                    String mailReceiveDate = DateFormatUtils.format(message.getReceivedDate(), "yyyyMMddHHmmss");
                    mailUid = message.getHeader("message-id") != null ? message.getHeader("message-id")[0] : mailReceiveDate + "_" + mailSubject;
                    mailUid = mailUid.replace("<", "").replace(">", "");
                    //이메일 중복 저장 여부
                    if(isDupEmail(sndrKey, mailUid)) {
                        continue;
                    }
                    
                    
                    String from = getAddress(message.getFrom());
                    String recipientTo = getAddress(message.getRecipients(RecipientType.TO));
                    String recipientCc = getAddress(message.getRecipients(RecipientType.CC));
                    String recipientBcc = getAddress(message.getRecipients(RecipientType.BCC));
                    
                    String mailContentHtml = "";
//                    String mailContentText = "";
                    String fileGroupKey = "";
                    //본문 내 이미지 정보 - html 내 src="cid:image001.xxxx" 형태를 다운 받은 이미지 주소로 대체하기 위함.
                    List<Map<String, String>> inlineImages = new ArrayList<Map<String, String>>();
                        
                    if (message.isMimeType("multipart/*")) {
                        MimeMultipart multipart = (MimeMultipart) message.getContent();
//                        log.info("multipart.getCount() ::: " + multipart.getCount());
                        for (int i = 0; i < multipart.getCount(); i++) {
                            MimeBodyPart bodyPart = (MimeBodyPart)multipart.getBodyPart(i);
//                            log.info("bodyPart(" + i + ") bodyPart.getContentType() ::: " + bodyPart.getContentType());
//                            log.info("bodyPart(" + i + ") bodyPart.getDisposition() ::: " + bodyPart.getDisposition());
                            if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                                
                                String actlFileNm = MimeUtility.decodeText(bodyPart.getFileName());
                                String fileExtent = actlFileNm.substring(actlFileNm.lastIndexOf("."));
                                
                                FilePropertiesResponse filePropertiesResponse = new FilePropertiesResponse();
                                filePropertiesResponse.setCustcoId(custcoId);
                                filePropertiesResponse.setUserId(2);
                                filePropertiesResponse.setTrgtTypeCd(RepositoryTrgtTypeCd.FILE);
                                filePropertiesResponse.setPathTypeCd(RepositoryPathTypeCd.files);
                                filePropertiesResponse.setTaskTypeCd(RepositoryTaskTypeCd.chatemail);
                                filePropertiesResponse.setDir(Paths.get(
                                    filePropertiesResponse.getPathTypeCd() + "/" + TenantContext.getCurrentTenant() + "_" + TenantContext.getCurrentCustco() + "/"
                                        + filePropertiesResponse.getTaskTypeCd()));
                                
                                //비즈구분에 따른 경로 규칙 가져오기
                                //ex : /paletteVolume/web/files/hkcloud_1/chatemail/
                                Path taskTypeCdFilePath = new File(env.getString("file.repository.root-dir","") + File.separator + filePropertiesResponse.getDir() ).toPath();

                                //파일 경로 규칙 가져오기
                                Path ruleStorePath = fileRuleUtils.getRuleStorePath(filePropertiesResponse);

                                //저장할 파일명과 확장자 규칙 가져오기
                                RuleFilenameAndExtentResponse ruleFilenameAndExtentResponse = fileRuleUtils.getRuleFilenameAndExtent(filePropertiesResponse, actlFileNm, fileExtent);
                                String storeFilenameExtent = ruleFilenameAndExtentResponse.getRuleFilenameExtent();

                                //전체 경로 생성
                                Path storeLocaltionPath = taskTypeCdFilePath.resolve(ruleStorePath).toAbsolutePath().normalize();
                                Path storeLocaltionFull = storeLocaltionPath.resolve(storeFilenameExtent).toAbsolutePath().normalize();
                                
                                
                                File fileDir = new File(storeLocaltionPath.toUri());
                                if(!fileDir.isDirectory()) {
                                    fileDir.mkdirs();
                                }
//                                    
                                bodyPart.saveFile(storeLocaltionFull.toString());
                                
                                long fileSize = Files.size(storeLocaltionFull);
                                String fileMimeType = new Tika().detect(storeLocaltionFull);
                                fileGroupKey = fileRuleUtils.ifCreatFileGroupKey(FileUploadRequests.builder()
                                    .fileGroupKey(fileGroupKey)
                                    .build());
                                
                                ///////////////////////////////////////////////////////////////////////////
//                                log.info("///////////////////////////////////////////////////////////////////////////");
//                                log.info("filePropertiesResponse ::: " + filePropertiesResponse.toString());
//                                log.info("ruleFilenameAndExtentResponse ::: " + ruleFilenameAndExtentResponse.toString());
//                                log.info("storeLocaltionFull ::: " + storeLocaltionFull);
//                                log.info("bodyPart.getContentType() ::: " + bodyPart.getDataHandler().getContentType());
//                                log.info("fileSize::: " + fileSize);
//                                log.info("fileMimeType ::: " + fileMimeType);
//                                log.info("fileGroupKey ::: " + fileGroupKey);
//                                log.info("///////////////////////////////////////////////////////////////////////////");
                                ///////////////////////////////////////////////////////////////////////////
                                
                                insertFile(filePropertiesResponse, ruleFilenameAndExtentResponse, ruleStorePath, fileGroupKey, fileSize, fileMimeType);
                                
                                
                            } else if(bodyPart.isMimeType("image/*")) {
                                //인라인 이미지 repository에 저장
                                attachInlineImage(mailUid, bodyPart, inlineImages);
                            } else {
                                //메일 본문 - multipart/*
//                                log.info("메일 본문 - multipart/* ::: ");
                                mailContentHtml = getContentFromMimeBodyPart(bodyPart, "text/html", mailUid, inlineImages);
//                                mailContentText = getContentFromMimeBodyPart(bodyPart, "text/plain");
                            }
                        }
                    } else {
                        //메일 본문 - text/plain, text/html
//                        log.info("메일 본문 - text/plain, text/html ::: ");
                        try {
//                            log.info("message.getContentType() ::: " + message.getContentType());
//                            log.info("message.isMimeType(\"text/plain\") ::: " + message.isMimeType("text/plain"));
//                            log.info("message.isMimeType(\"text/html\") ::: " + message.isMimeType("text/html"));
                            mailContentHtml = message.getContent().toString();
                        } catch(Exception e) {
                            mailContentHtml = "<div>이메일 본문 수집에 실패하였습니다. ::: " + e.toString() + "</div>";
                        }
                    }
                    
                    log.info("==================================================================================================");
//                        log.info("flag ::: " + message.getFlags());
//                        log.info("getContentType() ::: " + message.getContentType());
//                        log.info("CUSTCO_ID ::: " + custcoId);
                    log.info("mailUid ::: " + mailUid);
//                        log.info("mailReceiveDate ::: " + mailReceiveDate);
                    log.info("mailSubject ::: " + mailSubject);
//                        log.info("from ::: " + from);
//                        log.info("recipientTo ::: " + recipientTo);
//                        log.info("recipientCc ::: " + recipientCc);
//                        log.info("recipientBcc ::: " + recipientBcc);
//                        log.info("mailContentHtml ::: " + mailContentHtml);
//                        log.info("mailContentText ::: " + mailContentText);
                    
                    log.info("==================================================================================================\n\n\n\n\n");
                    log.info("inlineImages.size ::: " + inlineImages.size());
                    mailContentHtml = convertContentHtml(mailContentHtml, inlineImages);
                    mailContentHtml = mailContentHtml.replaceAll("\0", "");
                    if("".equals(mailContentHtml)) {
                        mailContentHtml = "<div>본문이 조회되지 않았습니다.</div>";
                    }
                    TelewebJSON emailParams = new TelewebJSON();
                    String chtCuttEmlId = String.valueOf(innbCreatCmmnService.createSeqNo("CHT_CUTT_EML_ID"));
                    emailParams.setString("CHT_CUTT_EML_ID", chtCuttEmlId);
                    emailParams.setString("SNDR_KEY", sndrKey);
                    emailParams.setString("SRVR", host);
                    emailParams.setString("MSG_ID", mailUid);
                    emailParams.setString("RCPTN_DT", mailReceiveDate);
                    emailParams.setString("TTL", mailSubject);
                    emailParams.setString("CN", mailContentHtml.replaceAll("\0", ""));
//                    emailParams.setString("MAIL_CONTENT_TEXT", mailContentText);
                    emailParams.setString("SNDR_EML", from);
                    emailParams.setString("RCVR_EML", recipientTo);
                    emailParams.setString("RFRNC_EML", recipientCc);
                    emailParams.setString("HID_RFRNC_EML", recipientBcc);
                    emailParams.setString("FILE_GROUP_KEY", fileGroupKey);
                    insertChatCuttEmail(emailParams);
                    
                    
                    //chat 서비스 실행 - 고객 조회/생성, 채팅 상담 생성, 이메일에 채팅 상담 ID 업데이트, 상담원 배정 대기 등록
                    log.info(logPrefix + "이메일 메시지 생성 및 ChatOnMessageEvent 이벤트 발행");
                    JSONObject messageJson = new JSONObject();
                    String pureEmail = from;
                    if(pureEmail.indexOf("<") > -1) {
                        pureEmail = pureEmail.substring(pureEmail.indexOf("<")+1, pureEmail.indexOf(">"));
                    }
                    messageJson.put("call_typ_cd", chnClsfCd);
                    messageJson.put("type", "EMAIL");
                    messageJson.put("asp_sndrKey", sndrKey);
                    messageJson.put("sndrKey", sndrKey);
                    messageJson.put("custco_id", custcoId);
                    messageJson.put("biz_service_cd", chnClsfCd);
                    messageJson.put("user_key", pureEmail);
                    messageJson.put("org_user_key", pureEmail);
                    messageJson.put("content", mailSubject);
                    messageJson.put("chtCuttEmlId", chtCuttEmlId);
                    
                    //이메일 상담 등록 및 상담원 배정 대기 이벤트 발행
                    eventPublisher.publishEvent(ChatOnMessageEvent.builder().messageJson(messageJson).build());
                    
                    collectCount ++;
                }
            }
            inbox.close(false);
            store.close();
            
            //수집 결과 저장
            retObj.setString("JOB_SCS_YN", "Y");
            retObj.setString("JOB_RSLT_MSG", "이메일 수집 완료 - " + collectCount + " 건");
            
        } catch(Exception e) {
            log.error("ERROR collectCsMail ::: " + mailUid + " ::: " + mailSubject + "::: " + e.toString());
            //수집 결과 저장
            retObj.setString("JOB_SCS_YN", "N");
            retObj.setString("JOB_RSLT_MSG", "에러 발생 - " + e.toString() + "\nmailUid - " + mailUid + "\nmailSubject - " + mailSubject);
            // Close folder and close store.
            try {
                if(inbox != null && inbox.isOpen()) inbox.close(false);
                if(store != null && store.isConnected()) store.close();
            } catch (MessagingException ee) {
                // TODO Auto-generated catch block
                log.error("ERROR collectCsMail close ::: " + ee.toString());
            }
        } finally {
            //Close folder and close store.
            try {
                if(inbox != null && inbox.isOpen()) inbox.close(false);
                if(store != null && store.isConnected()) store.close();
            } catch (MessagingException e) {
                // TODO Auto-generated catch block
                log.error("ERROR collectCsMail ::: " + e.toString());
            }
            
        }
        
        return retObj;
    }
    
    
    /**
     * 채팅_상담_이메일에 채팅_상담_ID 업데이트 - ChatReceiveEventListener에서 상담 배분 대기 등록 후 호출함.
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateChtCuttId(TelewebJSON paramJson) throws TelewebAppException {
        return mobjDao.select(namespace, "updateChtCuttId", paramJson);
    }
    
    /**
     * 이메일 중복 저장 여부
     * @param msgId
     * @return
     */
    private boolean isDupEmail(String sndrKey, String msgId) {
        TelewebJSON paramJson = new TelewebJSON();
        paramJson.setString("SNDR_KEY", sndrKey);
        paramJson.setString("MSG_ID", msgId);
        
        TelewebJSON returnJson = mobjDao.select(namespace, "selectDupEmailCount", paramJson);
        int dupCnt = returnJson.getInt("DUP_CNT");
        return dupCnt > 0 ? true : false;
    }
    
    
    /**
     * 수집 시작일시 조회.
     * @param sndrKey
     * @return
     */
    private SearchTerm getStartDateTerm(String sndrKey) {
        //채팅 이메일 마지막 수신일시 조회
        TelewebJSON paramJson = new TelewebJSON();
        paramJson.setString("SNDR_KEY", sndrKey);
        String lastDateStr = selectMaxRcptnDt(paramJson).getString("MAX_RCPTN_DT");
//        lastDateStr = "20240215020000";
        SearchTerm startDateTerm = null;
//        SearchTerm endDateTerm = null;
//        SearchTerm searchTerm = null;
        Date lastDate = null;
        if (lastDateStr != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");         // 문자열 -> Date        
            try {
                lastDate = formatter.parse(lastDateStr);
            } catch(ParseException e) {
                
            }
            Calendar c = Calendar.getInstance();
            c.setTime(lastDate);
            //lastDate 하루 전 날짜로 설정 - timezone이 다른 경우(예: 미국 이메일 서버) 오늘 수신한 메일도 하루 전 날짜로 들어가서 수집되지 않는다.
            c.add(Calendar.DATE, -1); 
            startDateTerm = new ReceivedDateTerm(ComparisonTerm.GE, c.getTime());
//            endDateTerm = new ReceivedDateTerm(ComparisonTerm.LE, c.getTime());
//            searchTerm = new AndTerm(startDateTerm, endDateTerm);
            log.info("selectStartDateTerm ::: " + lastDateStr);
        }
//        return startDateTerm;
        return startDateTerm;
    }
    
    /**
     * 메일 주소 가져오기
     * @param recipientsAddress
     * @return
     */
    private String getAddress(Address[] recipientsAddress) {
        String address = "";
        try {
            if(recipientsAddress != null) {
                for(Address recipient : recipientsAddress) {
                    if(!"".equals(address)) {
                        address += ",";
                    }
                    address += MimeUtility.decodeText(recipient.toString());
                }
            }
        } catch(Exception e) {
            log.error("ERROR getAddress : " + e.toString());
        }
        
        return address;
    }
    
    /**
     * 메일 본문 가져오기.
     * @param bodyPart
     * @return
     * @throws Exception
     */
    private String getContentFromMimeBodyPart(
        MimeBodyPart bodyPart, String mimeType, String mailUid, List<Map<String, String>> inlineImages) throws Exception{
        boolean isMultipart = false;
        String content = "";
        
        MimeMultipart multipart = null;
        
        try {
//            log.info("getContentFromMimeBodyPart ::: bodyPart.getContent() instanceof MimeMultipart ::: " + (bodyPart.getContent() instanceof MimeMultipart));
            if ( bodyPart.getContent() instanceof MimeMultipart ) {
                multipart = getMultipart((MimeMultipart)bodyPart.getContent());
                isMultipart = true;
            }
//            log.info("getContentFromMimeBodyPart ::: isMultipart ::: " + isMultipart);
            
            if( isMultipart ) {
                int count = multipart.getCount();
//                log.info("getContentFromMimeBodyPart ==== ::: isMultipart count ::: " + count);
                //text/html 이 뒤에 있어 for문 역순으로 돌림 
                for ( int i = count - 1; i >= 0; i-- ) {
                  MimeBodyPart childBodyPart = (MimeBodyPart)multipart.getBodyPart(i);
//                  log.info("getContentFromMimeBodyPart ==== ::: (" + i + ") ::: getContentID : " + childBodyPart.getContentID() + ", getContentType : " + childBodyPart.getContentType() + ", getDisposition : " + childBodyPart.getDisposition());
                  if(Part.INLINE.equalsIgnoreCase(childBodyPart.getDisposition())) {
                      attachInlineImage(mailUid, childBodyPart, inlineImages);
                  } else {
                      //for문 돌면서 이미 컨텐츠를 가져 온 경우 if문을 타지 않는다.
                      if("".equals(content)) {
                          content = getMailContent(childBodyPart, mimeType);
                      }
                  }
//                  인라인 이미지가 추가로 있을 수 있어 break 처리 제외.
//                  if(!"".equals(content)) {
//                      break;
//                  }
                }
            } else {
                content = getMailContent(bodyPart, mimeType);
            }
        } catch(FolderClosedException e) {
            log.error("ERROR : " + e.toString());
            content =  "이메일 본문 수집에 실패하였습니다. ::: " + e.toString();
        }
        
        return content ;
    }
    
    
    private MimeMultipart getMultipart(MimeMultipart multipart) {
        try {
            int count = multipart.getCount();
//            log.info("getMultipart ::: count ::: " + count);
            for ( int i = 0; i < count; i++ ) {
                BodyPart childBodyPart = multipart.getBodyPart(i);
                if ( childBodyPart.getContent() instanceof MimeMultipart ){
                    return getMultipart((MimeMultipart)childBodyPart.getContent());
                }
            }
        } catch(Exception e) {
            log.error("ERROR getMultipart : " + e.toString());
        }
        return multipart;
    }
    
    private String getMailContent(MimeBodyPart bodyPart, String mimeType) {
        String content = "";
        try {
            if ( bodyPart.isMimeType(mimeType) ) {
                content = (String)bodyPart.getContent();
            }
            
        } catch(Exception e) {
            log.error("ERROR getMailContent : " + e.toString());
        }
        return content;
    }
    
    
    private String convertContentHtml(String html, List<Map<String, String>> inlineImages) {
        //HTML 본문 내 이미지 정보 - html 내 src="cid:image001.xxxx" 형태를 다운 받은 이미지 주소로 대체하기 위함.
        if(!"".equals(html) && inlineImages.size() > 0) {
            for(Map<String, String> inlineImage : inlineImages) {
                html = html.replaceAll("src=\"cid:" + inlineImage.get("cid") + "\"", "src=\"" + java.util.regex.Matcher.quoteReplacement(inlineImage.get("link")) + "\"");
            }
        }
        //html Content XSS 처리(script, link)
        //mailContentHtml
        
        return html;
    }
    
    /**
     * 채팅_상담_이메일 저장
     * @param pramsJson
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    private TelewebJSON insertChatCuttEmail(TelewebJSON pramsJson) throws TelewebAppException {
        return mobjDao.insert(namespace, "insertChatCuttEmail", pramsJson);
    }
    
    
    /**
     * 파일 저장
     * @param filePropertiesResponse
     * @param ruleFilenameAndExtentResponse
     * @param ruleStorePath
     * @param fileGroupKey
     * @param fileSize
     * @param fileMimeType
     */
    @Transactional(readOnly = false)
    private void insertFile(FilePropertiesResponse filePropertiesResponse, RuleFilenameAndExtentResponse ruleFilenameAndExtentResponse,
        Path ruleStorePath, String fileGroupKey, long fileSize, String fileMimeType) {
        
        String saveFilePath = filePropertiesResponse.getDir().toString().replaceAll(Matcher.quoteReplacement(File.separator), "/") + "/" + ruleStorePath.toString()
        .replaceAll(Matcher.quoteReplacement(File.separator), "/");
   
        FileDbMngInsertRequest fileDbMngInsertRequest = 
            FileDbMngInsertRequest.builder()
                                  .fileGroupKey(fileGroupKey)
                                  .fileAcsTypeCd(FileAccessType.PRIVATE)
                                  .taskTypeCd(filePropertiesResponse.getTaskTypeCd())
                                  .trgtTypeCd(filePropertiesResponse.getTrgtTypeCd())
                                  .pathTypeCd(filePropertiesResponse.getPathTypeCd())
                                  .mimeTypeCd(fileMimeType)
                                  .actlFileNm(ruleFilenameAndExtentResponse.getActlFileNm())
                                  .strgFileNm(ruleFilenameAndExtentResponse.getRuleFilenameExtent())
                                  .fileSz(fileSize)
                                  .filePath(saveFilePath)
                                  .fileExtn(ruleFilenameAndExtentResponse.getCleanFileExtent())
                                  .custcoId(filePropertiesResponse.getCustcoId())
                                  .userId(filePropertiesResponse.getUserId())
                                  .rgtrId(filePropertiesResponse.getUserId())
                                  .build();
//        log.info("fileDbMngInsertRequest :{}", fileDbMngInsertRequest);
        
        //첨부 파일 저장
        fileDbMngService.insert(fileDbMngInsertRequest);
    }
    
    
    /**
     * 인라인 이미지 repository에 저장
     * @param mailUid
     * @param bodyPart
     * @param inlineImages
     * @throws IOException
     * @throws MessagingException
     */
    private void attachInlineImage(String mailUid, MimeBodyPart bodyPart, List<Map<String, String>> inlineImages) throws IOException, MessagingException {
        FilePropertiesResponse imagePropertiesResponse = new FilePropertiesResponse();
        imagePropertiesResponse.setPathTypeCd(RepositoryPathTypeCd.images);
        imagePropertiesResponse.setTaskTypeCd(RepositoryTaskTypeCd.chatemail);
        imagePropertiesResponse.setDir(Paths.get(
            imagePropertiesResponse.getPathTypeCd() + "/" + TenantContext.getCurrentTenant() + "_" + TenantContext.getCurrentCustco() + "/"
                + imagePropertiesResponse.getTaskTypeCd() + "/" + mailUid));
        
        Path taskTypeCdImagePath = new File(env.getString("file.repository.root-dir","") + File.separator + imagePropertiesResponse.getDir() ).toPath();
        
        //Part.INLINE 이미지 - 본문 내 이미지가 INLINE 으로 들어오진 않아서 bodyPart.isMimeType("image/*") 으로 체크.
        String cid = bodyPart.getContentID().replace("<", "").replace(">", "");
        String link = SERVER_DOMAIN + "/upload/" + imagePropertiesResponse.getDir().toString().replaceAll(Pattern.quote("\\"), "/") + "/" + cid;
        
        Path storeLocaltionPath = taskTypeCdImagePath.toAbsolutePath().normalize();
        Path storeLocaltionFull = storeLocaltionPath.resolve(cid).toAbsolutePath().normalize();
        File inlineImageDir = new File(storeLocaltionPath.toUri());
        if(!inlineImageDir.isDirectory()) {
            inlineImageDir.mkdirs();
        }
//        log.info("===================================== cidImageSave :::" + storeLocaltionFull);
        bodyPart.saveFile(storeLocaltionFull.toString());
        
        Map<String, String> inlineImage = new HashMap<String, String>();
        inlineImage.put("cid", cid);
        inlineImage.put("link", link);
        inlineImages.add(inlineImage);
    }

    /* 이전배치에서 CHT_CUTT_ID 적용안된 경우 다시 처리하기 위함.*/
    public void retryChatOnMessageEvent() {
        TelewebJSON retJson = mobjDao.select(namespace, "selectNotMatchChtCuttId", new TelewebJSON());
        if( retJson.getHeaderInt("COUNT") > 0 ) {
            JSONArray retArray = retJson.getDataObject(TwbCmmnConst.G_DATA);
            for(int j = 0; j < retArray.size(); j++) {
                JSONObject retryObj = retArray.getJSONObject(j);
                JSONObject messageJson = new JSONObject();
                messageJson.put("call_typ_cd", retryObj.getString("CHN_CLSF_CD"));
                messageJson.put("type", "EMAIL");
                messageJson.put("asp_sndrKey", retryObj.getString("SNDR_KEY"));
                messageJson.put("sndrKey", retryObj.getString("SNDR_KEY"));
                messageJson.put("custco_id", retryObj.getString("CUSTCO_ID"));
                messageJson.put("biz_service_cd", retryObj.getString("CHN_CLSF_CD"));
                messageJson.put("user_key", retryObj.getString("SNDR_EML"));
                messageJson.put("org_user_key", retryObj.getString("SNDR_EML"));
                messageJson.put("content", retryObj.getString("TTL"));
                messageJson.put("chtCuttEmlId", retryObj.getString("CHT_CUTT_EML_ID"));

                //이메일 상담 등록 및 상담원 배정 대기 이벤트 발행
                eventPublisher.publishEvent(ChatOnMessageEvent.builder().messageJson(messageJson).build());

            }
        }
    }
}
