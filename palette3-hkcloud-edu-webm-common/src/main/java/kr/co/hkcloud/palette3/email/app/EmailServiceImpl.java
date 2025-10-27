package kr.co.hkcloud.palette3.email.app;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import kr.co.hkcloud.palette3.common.code.app.CodeCmmnService;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.app.FileDbMngService;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    private final TwbComDAO mobjDao;
    private final FileDbMngService fileDbMngService;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final CodeCmmnService codeCmmnService;

    @Autowired
    Environment environment;

    @Autowired
    private HcTeletalkEnvironment env;
    
    @Value("${palette.security.private.alg}")
    private String P_ALG;
    
    @Value("${palette.security.private.key}")
    private String P_KEY;
    
    private String commCodeTranPattern = "##\\{(.+?)\\}##";

//    @Autowired
//    public JavaMailSender mailSender;
    
    private String namespace = "kr.co.hkcloud.palette3.email.dao.EmailMapper";

    /**
     * 이메일 발송 및 이력 저장
     */
    @Override
    public TelewebJSON sendEmail(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = null;
        //SMTP 정보 조회
        TelewebJSON smtpObj = mobjDao.select(namespace, "selectCustcoSmtpSetting", mjsonParams);
        if(smtpObj.getHeaderInt("COUNT") == 0 || StringUtils.isEmpty(smtpObj.getString("SMTP_SRVR"))) {
            objRetParams = new TelewebJSON();
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "SMTP 설정 후 발송 가능합니다.");
        } else {
            String emalFrom = smtpObj.getString("SNDR_NM") + "<" + smtpObj.getString("SNDR_EML") + ">";// 송신자
//            String emlTo = mjsonParams.getString("RCVR_EML");
//            String subject = mjsonParams.getString("TTL");
//            String sendContents = mjsonParams.getString("CN");

            List<FileDbMngResponse.FileDbMngSelectListResponse> fileDbMngSelectListResponseList = null;
            if (!StringUtils.isEmpty(mjsonParams.getString("FILE_GROUP_KEY"))) {
                FileDbMngRequest.FileDbMngSelectRequest fileDbMngSelectRequest = new FileDbMngRequest.FileDbMngSelectRequest();
                fileDbMngSelectRequest.setFileGroupKey(mjsonParams.getString("FILE_GROUP_KEY"));
                fileDbMngSelectListResponseList = fileDbMngService.selectList(fileDbMngSelectRequest);  //첨부파일조회.
            }
            //이메일_발송_이력 저장
            String emlSndngId = String.valueOf(innbCreatCmmnService.createSeqNo("EML_SNDNG_ID"));
            mjsonParams.setString("EML_SNDNG_ID", emlSndngId);
            mjsonParams.setString("SNDR_EML", emalFrom);
            insertEmailSendingHistory(mjsonParams);
            
            //예약일시 값이 없을때만 즉시 발송
            if(StringUtils.isEmpty(mjsonParams.getString("RSVT_DT"))) {
                //이메일 발송
                objRetParams = sendMimeMessage(smtpObj, mjsonParams, fileDbMngSelectListResponseList);
                objRetParams.setString("CUSTCO_ID", mjsonParams.getString("CUSTCO_ID"));
                objRetParams.setString("USER_ID", mjsonParams.getString("USER_ID"));
                objRetParams.setString("EML_SNDNG_ID", emlSndngId);
                objRetParams.setString("RSLT_CD", objRetParams.getHeaderBoolean("ERROR_FLAG") ? "FAIL" : "SUCC");
                if(objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                    objRetParams.setString("RSLT_MSG", objRetParams.getHeaderString("ERROR_MSG"));
                }
                //발송_일시, 결과_코드 업데이트
                updateEmailSendingHistory(objRetParams);
            } else {
                objRetParams = new TelewebJSON();
                objRetParams.setHeader("ERROR_FLAG", false);
            }
        }
        

        return objRetParams;
    }
    
    @Override
    public TelewebJSON sendSrvyEmail(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = null;
        //SMTP 정보 조회
        TelewebJSON smtpObj = mobjDao.select(namespace, "selectCustcoSmtpSetting", mjsonParams);
        if(smtpObj.getHeaderInt("COUNT") == 0 || StringUtils.isEmpty(smtpObj.getString("SMTP_SRVR"))) {
            objRetParams = new TelewebJSON();
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "SMTP 설정 후 발송 가능합니다.");
        } else {
            mjsonParams.setString("SE", "SRVY");
            TelewebJSON expsnAttrListParams= mobjDao.select(namespace, "selectSrvyExpsnAttrList", mjsonParams); 
            
            String emalFrom = smtpObj.getString("SNDR_NM") + "<" + smtpObj.getString("SNDR_EML") + ">";// 송신자
            String subject = mjsonParams.getString("TTL");
            String contents = mjsonParams.getString("CN");
            String custcoId = mjsonParams.getString("CUSTCO_ID");
            String userId = mjsonParams.getString("USER_ID");
            String sndngTypeCd = mjsonParams.getString("SNDNG_TYPE_CD");
            String rsvtDt = mjsonParams.getString("RSVT_DT");
            String fileGroupKey = mjsonParams.getString("FILE_GROUP_KEY");
            

            List<FileDbMngResponse.FileDbMngSelectListResponse> fileDbMngSelectListResponseList = null;
            if (!StringUtils.isEmpty(mjsonParams.getString("FILE_GROUP_KEY"))) {
                FileDbMngRequest.FileDbMngSelectRequest fileDbMngSelectRequest = new FileDbMngRequest.FileDbMngSelectRequest();
                fileDbMngSelectRequest.setFileGroupKey(mjsonParams.getString("FILE_GROUP_KEY"));
                fileDbMngSelectListResponseList = fileDbMngService.selectList(fileDbMngSelectRequest);  //첨부파일조회.
            }
            JSONArray srvyTrgtJsonArr= mjsonParams.getDataObject();
            
            
            for(int i = 0; i < srvyTrgtJsonArr.size(); i++) {
                JSONObject srvyTrgtObj = srvyTrgtJsonArr.getJSONObject(i);
                
                //설문 이메일 발송 정보 세팅
                TelewebJSON srvyEmailParams = new TelewebJSON(); 
                String emlSndngId = String.valueOf(innbCreatCmmnService.createSeqNo("EML_SNDNG_ID"));
                srvyEmailParams.setString("EML_SNDNG_ID", emlSndngId);
                srvyEmailParams.setString("CUSTCO_ID", custcoId);
                srvyEmailParams.setString("USER_ID", userId);
                srvyEmailParams.setString("SNDR_EML", emalFrom);
                srvyEmailParams.setString("TTL", subject);
                srvyEmailParams.setString("SNDNG_TYPE_CD", sndngTypeCd);
                srvyEmailParams.setString("FILE_GROUP_KEY", fileGroupKey);
                srvyEmailParams.setString("CN", tranContent(contents, srvyTrgtObj, expsnAttrListParams, custcoId));
                srvyEmailParams.setString("RCVR_EML", srvyTrgtObj.getString("EML"));
                srvyEmailParams.setString("RSVT_DT", rsvtDt);
                srvyEmailParams.setString("SRVY_TRGT_ID", srvyTrgtObj.getString("SRVY_TRGT_ID"));
             
                //이메일_발송_이력 저장
                insertEmailSendingHistory(srvyEmailParams);
                
                //예약일시 값이 없을때만 즉시 발송
                if(StringUtils.isEmpty(rsvtDt)) {
                    //이메일 발송
                    objRetParams = sendMimeMessage(smtpObj, srvyEmailParams, fileDbMngSelectListResponseList);
                    objRetParams.setString("CUSTCO_ID", custcoId);
                    objRetParams.setString("USER_ID", userId);
                    objRetParams.setString("EML_SNDNG_ID", emlSndngId);
                    objRetParams.setString("RSLT_CD", objRetParams.getHeaderBoolean("ERROR_FLAG") ? "FAIL" : "SUCC");
                    if(objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                        objRetParams.setString("RSLT_MSG", objRetParams.getHeaderString("ERROR_MSG"));
                    }
                    //발송_일시, 결과_코드 업데이트
                    updateEmailSendingHistory(objRetParams);
                } else {
                    objRetParams = new TelewebJSON();
                    objRetParams.setHeader("ERROR_FLAG", false);
                }
            }
        }
        

        return objRetParams;
    }
    
    /**
     * 이메일 발송 내역 조회
     */
    public TelewebJSON selectSendEmail(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = mobjDao.select(namespace, "selectSendEmail", mjsonParams);
        if (objRetParams.getHeaderInt("COUNT") > 0 && !StringUtils.isEmpty(objRetParams.getString("FILE_GROUP_KEY"))) {
            objRetParams.setObject("FILE_LIST", 0, fileDbMngService.selectFileList(objRetParams).getDataObject());
        }
        return objRetParams;
    }

    /**
     * 실제 이메일 발송(SMTP or IMAP)
     */
    @Override
    public TelewebJSON sendMimeMessage(TelewebJSON smtpObj, TelewebJSON mjsonParams,
        List<FileDbMngResponse.FileDbMngSelectListResponse> fileDbMngSelectListResponseList) {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);    //반환 파라메터 생성
        try {
            
            String host = smtpObj.getString("SMTP_SRVR"); // SMTP 서버 주소
            String user = smtpObj.getString("ACNT");; // SMTP 로그인 ID
            String password = smtpObj.getString("PSWD"); // 발신자 로그인 비밀번호
            int port = smtpObj.getInt("SMTP_PORT");

            // Get the session object
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port); 
            props.put("mail.smtp.auth", "true");
            
            
            if(port == 587) {
                // TLS 사용 시 설정
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl.trust", host); // 서버 인증서 신뢰
            } else if(port == 465) {
                // SSL 사용 시 설정
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.port", port);
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.ssl.trust", host); // 한메일에서 발송시에는 설정시켰었음.
            } else if(port == 25) {
                props.put("mail.smtp.starttls.enable", "false");
            }

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });
            
            
            
            
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper helper;

            helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            InternetAddress toAddress = null;
//            if (to.indexOf("&lt;") > -1) {
//                String pureToEmail = to.substring(to.indexOf("&lt;") + 4, to.indexOf("&gt;"));
//                log.info("===================---------------------================ email : " + pureToEmail + ", name : " + to.replaceAll(
//                    "&lt;" + pureToEmail + "&gt;", "").trim());
//                toAddress = new InternetAddress(pureToEmail, to.replaceAll("&lt;" + pureToEmail + "&gt;", "").trim());
//            } else {
                toAddress = new InternetAddress(mjsonParams.getString("RCVR_EML"));
//            }

            helper.setText(HtmlUtils.htmlUnescape(mjsonParams.getString("CN")), true); //***HTML 적용
            helper.setTo(toAddress);
            helper.setSubject(mjsonParams.getString("TTL"));
            helper.setFrom(new InternetAddress(smtpObj.getString("SNDR_EML"), smtpObj.getString("SNDR_NM")));

            if (fileDbMngSelectListResponseList != null) {
                for (int i = 0; i < fileDbMngSelectListResponseList.size(); i++) {

                    FileDbMngResponse.FileDbMngSelectListResponse responseObj = fileDbMngSelectListResponseList.get(i);
                    String originalFileNm = responseObj.getActlFileNm();

                    //이미지인경우(PathTypeCd이 먼저) repository/web/images/bbs/2023/01/01 형태로 이외의 파일의 경우(TaskTypeCd이 먼저) repository/web/bbs/files/2023/01/01 저장됨.
//                    String typeDir = responseObj.getTaskTypeCd() + File.separator + responseObj.getPathTypeCd();

                    String pathfile = env.getString("file.repository.root-dir", "") + File.separator + responseObj.getFilePath()
                        + File.separator + responseObj.getStrgFileNm();
                    FileDataSource fds = new FileDataSource(pathfile);

                    //B는 Base64, Q는 quoted-printable
                    helper.addAttachment(MimeUtility.encodeText(originalFileNm, "UTF-8", "B"), fds);
                }
            }
            Transport.send(mimeMessage);
            objRetParams.setHeader("ERROR_FLAG", false);
            objRetParams.setHeader("ERROR_MSG", "");
        } catch (UnsupportedEncodingException | MessagingException e) {
            log.error("sendMimeMessage ERROR : " + e.toString());
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", e.toString());
        }
        
        return objRetParams;
    }

    /**
     * 이메일_발송_이력 저장
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertEmailSendingHistory(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.insert(namespace, "insertEmailSendingHistory", mjsonParams);
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectEmailTemplate(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectEmailTemplate", mjsonParams);
    }
    
    
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON sendReservationEmail() throws TelewebAppException {
        TelewebJSON retParams = new TelewebJSON();
        
        
        //예약 발송 이메일 조회
        TelewebJSON reservationEmailJSON = mobjDao.select(namespace, "selectReservationEmailList", new TelewebJSON());
        
        JSONArray reservationEmailArr = reservationEmailJSON.getDataObject();
        for(int i = 0; i < reservationEmailArr.size();  i++) {
            JSONObject emailObj = reservationEmailArr.getJSONObject(i);
            emailObj.put("PP_KEY_PP", P_KEY);
            emailObj.put("PP_ALG_PP", P_ALG);
            JSONArray emaiArr = new JSONArray();
            emaiArr.add(emailObj);
            TelewebJSON mjsonParams = new TelewebJSON();
            mjsonParams.setDataObject(emaiArr);
            
            List<FileDbMngResponse.FileDbMngSelectListResponse> fileDbMngSelectListResponseList = null;
            if (!StringUtils.isEmpty(mjsonParams.getString("FILE_GROUP_KEY"))) {
                FileDbMngRequest.FileDbMngSelectRequest fileDbMngSelectRequest = new FileDbMngRequest.FileDbMngSelectRequest();
                fileDbMngSelectRequest.setFileGroupKey(mjsonParams.getString("FILE_GROUP_KEY"));
                fileDbMngSelectListResponseList = fileDbMngService.selectList(fileDbMngSelectRequest);  //첨부파일조회.
            }
            
            
            //SMTP 정보 조회
            TelewebJSON smtpObj = mobjDao.select(namespace, "selectCustcoSmtpSetting", mjsonParams);
            
            //이메일 발송
            TelewebJSON objRetParams = sendMimeMessage(smtpObj, mjsonParams, fileDbMngSelectListResponseList);
            objRetParams.setString("CUSTCO_ID", mjsonParams.getString("CUSTCO_ID"));
            objRetParams.setString("USER_ID", "2");
            objRetParams.setString("EML_SNDNG_ID", mjsonParams.getString("EML_SNDNG_ID"));
            objRetParams.setString("RSLT_CD", objRetParams.getHeaderBoolean("ERROR_FLAG") ? "FAIL" : "SUCC");
            if(objRetParams.getHeaderBoolean("ERROR_FLAG")) {
                objRetParams.setString("RSLT_MSG", objRetParams.getHeaderString("ERROR_MSG"));
            }
            //발송_일시, 결과_코드 업데이트
            updateEmailSendingHistory(objRetParams);
            
        }
        
        retParams.setHeader("ERROR_FLAG", false);
        retParams.setHeader("ERROR_MSG", "");
        
        return retParams;
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectEmailSendingHistory(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectEmailSendingHistory", mjsonParams);
    }
    
    
    /**
     * 이메일_발송_이력 수정
     */
    @Transactional(readOnly = false)
    private TelewebJSON updateEmailSendingHistory(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.update(namespace, "updateEmailSendingHistory", mjsonParams);
    }
    
    
    /**
     * 설문조사 이메일 본문의 치환 키워드 치환 (예: #{CUST_NM})
     * @param contents
     * @param srvyTrgtObj
     * @param expsnAttrListParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    private String tranContent(String contents, JSONObject srvyTrgtObj, TelewebJSON expsnAttrListParams, String custcoId) throws TelewebAppException {
        String retContents = new String(contents.replaceAll("#\\{CUS_NM\\}", srvyTrgtObj.getString("CUST_NM")));
        retContents = retContents.replaceAll("#\\{LINK\\}", srvyTrgtObj.getString("URL_KEY"));
        
        //#{A} 패턴 변환(일반적)
        JSONArray expsnAttrJsonArr = expsnAttrListParams.getDataObject();
        for(int i = 0; i <  expsnAttrJsonArr.size(); i++) {
            JSONObject expsnAttrObj = expsnAttrJsonArr.getJSONObject(i);
            retContents = retContents.replaceAll("#\\{" + expsnAttrObj.getString("EXPSN_ATTR_COL_ID") + "\\}", srvyTrgtObj.getString(expsnAttrObj.getString("EXPSN_ATTR_COL_ID")));
        }
        
        //##{A,B}## 패턴 변환 - 공통코드에서 값을 찾아와야 함.
        Pattern p = Pattern.compile(commCodeTranPattern);
        Matcher matcher = p.matcher(retContents);
        

        while (matcher.find()) {
            String extractedText = matcher.group(1);
            String[] splitText = extractedText.split(",");
            
            if(splitText != null && splitText.length == 2) {
                String cdId = srvyTrgtObj.getString(splitText[0].trim());
                if(cdId != null) {
                    TelewebJSON mjsonParams = new TelewebJSON();
                    mjsonParams.setString("CUSTCO_ID", custcoId);
                    mjsonParams.setString("CD_ID", cdId);
                    mjsonParams.setString("GROUP_CD_ID", splitText[1].trim());
                    TelewebJSON retObjParams = codeCmmnService.selectTranCommCode(mjsonParams);
                    if(retObjParams.getHeaderInt("TOT_COUNT") > 0 && StringUtils.isNotEmpty(retObjParams.getString("CD_NM"))) {
                        retContents = retContents.replaceAll("##\\{" + extractedText + "\\}##", retObjParams.getString("CD_NM"));
                    }
                } else {
                    retContents = retContents.replaceAll("##\\{" + extractedText + "\\}##", "");
                }
            } else {
                retContents = retContents.replaceAll("##\\{" + extractedText + "\\}##", "");
            }
            
        }
        
        
        return retContents;
    }
    
    
}
