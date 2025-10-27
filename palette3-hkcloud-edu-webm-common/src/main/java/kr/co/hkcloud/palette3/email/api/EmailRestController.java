package kr.co.hkcloud.palette3.email.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.common.twb.model.TelewebRequestDto;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.email.app.EmailService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 공통 모듈 - 이메일 발송
 *
 * @author HJH
 * @since 2024-02-16
 * @version 1.0
 * <pre>
 * ===================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2024-02-16 HJH 최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailRestController {

    private final EmailService emailService;

    @ApiOperation(value = "공통 모듈 - 이메일 발송", notes = "이메일을 발송한다")
    @PostMapping("/api/email/sendEmail")
    public Object sendEmail(TelewebRequestDto requestDto) throws TelewebApiException {
        //    public Object sendEmail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //TelewebJSON 으로 받을 시 태그가 치환되서 넘어와서 requestDto 로 받아서 TelewebJSON 변환하여 처리
        TelewebJSON jsonParams = requestDto.getTelewebJSON();
        TelewebJSON objRetParams = null;
        if("Y".equals(jsonParams.getString("SRVY_YN"))) {
            //설문조사 이메일 발송 서비스 호출
            objRetParams = emailService.sendSrvyEmail(jsonParams);
        } else {
            //이메일 발송 서비스 호출
            objRetParams = emailService.sendEmail(jsonParams);
        }
        
        return objRetParams;
    }

    @ApiOperation(value = "공통 모듈 - 이메일 템플릿 조회", notes = "이메일 템플릿을 조회한다")
    @PostMapping("/api/email/getEmailTemplate")
    public Object getEmailTemplate(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        return emailService.selectEmailTemplate(mjsonParams);
    }
    
    @ApiOperation(value = "공통 모듈 - 이메일 발송 내역 조회", notes = "이메일을 발송 내역을 조회한다")
    @PostMapping("/api/email/getSendEmail")
    public Object getSendEmail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        return emailService.selectSendEmail(mjsonParams);
    }
    
    @ApiOperation(value = "공통 모듈 - 이메일 발송 이력 조회", notes = "이메일 발송 이력을 조회한다")
    @PostMapping("/api/email/selectEmailSendingHistory")
    public Object selectEmailSendingHistory(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        return emailService.selectEmailSendingHistory(mjsonParams);
    }
    
}
