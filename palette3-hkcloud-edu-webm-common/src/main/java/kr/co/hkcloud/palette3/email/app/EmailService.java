package kr.co.hkcloud.palette3.email.app;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse;

public interface EmailService {
    /**
     * 이메일 발송 및 이력 저장
     */
    public TelewebJSON sendEmail(TelewebJSON mjsonParams) throws TelewebAppException;
    /**
     * 설문조사 이메일 발송 및 이력 저장
     */
    public TelewebJSON sendSrvyEmail(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 이메일 발송 내역 조회
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectSendEmail(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 실제 이메일 발송(SMTP or IMAP)
     */
    public TelewebJSON sendMimeMessage(TelewebJSON smtpObj, TelewebJSON mjsonParams, List<FileDbMngResponse.FileDbMngSelectListResponse> fileDbMngSelectListResponseList) throws MessagingException, UnsupportedEncodingException ;
    
    /**
     * 이메일_발송_이력 저장
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON insertEmailSendingHistory(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 이메일 템플릿을 조회한다.
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectEmailTemplate(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 예약 메일 발송
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON sendReservationEmail() throws TelewebAppException;
    
    /**
     * 이메일 발송 이력 조회
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectEmailSendingHistory(TelewebJSON mjsonParams) throws TelewebAppException;
}
