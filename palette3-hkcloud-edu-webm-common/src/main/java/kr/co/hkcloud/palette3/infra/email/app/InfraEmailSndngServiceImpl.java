package kr.co.hkcloud.palette3.infra.email.app;


import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.util.PaletteMailUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("infraEmailSndngService")
public class InfraEmailSndngServiceImpl implements InfraEmailSndngService
{

    private final PaletteMailUtils paletteMailUtils;


    /**
     * <pre>
     * 업무구분 : 이메일 전송
     * 파 일 명 : InfraEmailSndngServiceImpl.java
     * 작 성 자 : 이동욱
     * 작 성 일 : 2021.05.13
     * 설    명 : 이메일 전송 서비스 구현체
     * </pre>
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON trnsmisEmail(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);	//반환 파라메터 생성

        String emlTo = mjsonParams.getString("CUST_EML");		// 수신자
        String subject = "이메일 발송";								// eml 제목
        String emalFrom = "no-reply@hkcloud.co.kr";					// 송신자
        String EML_CNTN = mjsonParams.getString("EML_CNTN");	// eml 내용
        String sendContents = EML_CNTN;

        try {
            paletteMailUtils.sendMimeMessage(emlTo, subject, sendContents, emalFrom, mjsonParams);
        }
        catch(UnsupportedEncodingException | MessagingException e) {
            throw new TelewebAppException(e.getLocalizedMessage());
        }

        objRetParams.setHeader("ERROR_FLAG", false); // errcode를 asp-000으로 해서 메시지는 화면에서 처리하자

        return objRetParams;
    }

}
