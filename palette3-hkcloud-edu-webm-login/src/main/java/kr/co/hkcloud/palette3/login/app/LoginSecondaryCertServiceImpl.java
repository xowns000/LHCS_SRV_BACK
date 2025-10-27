package kr.co.hkcloud.palette3.login.app;

import javax.servlet.http.HttpServletRequest;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.support.PaletteServletRequestSupport;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.message.app.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * packageName    : kr.co.hkcloud.palette3.login.app
 * fileName       :
 * author         : njy
 * date           : 2024-02-01
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-01           njy            최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("LoginSecondaryCertService")
public class LoginSecondaryCertServiceImpl implements LoginSecondaryCertService{

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final MessageService messageService;

    @Override
    public TelewebJSON publishNo(TelewebJSON mjsonParam, HttpServletRequest request) throws TelewebAppException {


        TelewebJSON objRetParam = new TelewebJSON(mjsonParam); // 반환 객체 생성
        TelewebJSON objSmsParam = new TelewebJSON(); // sms 발송용 객체
        TelewebJSON objTempParam = new TelewebJSON(); // 정보조회용 객체

        String certNo = "";                                                                // 인증번호
        String UserPhnNo = "";                                                             // 사용자 전화번호
        String cntnIp = PaletteServletRequestSupport.getClientIp(request);                 // 현재 접속 IP
        String CertYn = "N";                                                               // 인증여부
        String message = "";                                                              // SMS 발송 내용
        String ReCertYn = "N";

        // 인증번호 발급
        try{
            certNo = RandomStringUtils.randomNumeric(6);
        }catch (Exception e){
            objRetParam.setHeader("ERROR_FLAG", true);
            objRetParam.setHeader("ERROR_MSG", "인증번호 발급에 실패했습니다.");

            return objRetParam;
        }
        //발송 메세지
        message = "[Palette3 로그인] 요청하신 인증번호는 ["+certNo+"] 입니다.";

        // LGN_ID로 사용자 정보 검색
        objTempParam = mobjDao.select("kr.co.hkcloud.palette3.login.dao.LoginMapper", "selectUserinfoByLgnid", mjsonParam);

        // sms발송용 정보
        objSmsParam.setString("message", message);
        objSmsParam.setString("SNDNG_SE_CD", "SMS");
        objSmsParam.setString("CUSTCO_ID", objTempParam.getString("CUSTCO_ID"));
        objSmsParam.setString("callback_number", objTempParam.getString("DSPTCH_NO"));
        objSmsParam.setString("phone_number", objTempParam.getString("MBL_PHN_NO"));
        objSmsParam.setString("USER_ID", "2");


        // SMS 발송
        try{
            objSmsParam = messageService.sendInfo(objSmsParam);

        }catch (Exception e){
            // 인증번호 발송 메서드에서 에러가 생겼을 때
            log.error("!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@@===========" + String.valueOf(e));

            objRetParam.setHeader("ERROR_FLAG", true);
            objRetParam.setHeader("ERROR_MSG", "인증번호 발송에 실패했습니다. 다시 시도해주세요.");

            return objRetParam;
        }

        // 인증 정보
        if(objSmsParam.getDataObject("DATA").size() > 0){

            // MTS 결과코드 성공( SMS : 00 , LMS(MMS) : 1000 , 정상 : 0000 ) 아닐 시 에러 메세지 리턴
            if("0000".equals(objSmsParam.getString("code")) || "00".equals(objSmsParam.getString("code")) || "1000".equals(objSmsParam.getString("code"))){

                int CertHstryId = innbCreatCmmnService.createSeqNo("CERT_SMS_HSTRY_ID"); // 인증이력 시퀀스 정보 생성

                mjsonParam.setInt("CERT_SMS_HSTRY_ID", CertHstryId);
                mjsonParam.setString("CNTN_IP", cntnIp);
                mjsonParam.setInt("USER_ID", objTempParam.getInt("USER_ID"));
                mjsonParam.setString("MTS_SNDNG_HSTRY_ID", objSmsParam.getString("MTS_SNDNG_HSTRY_ID"));
                mjsonParam.setString("CERT_NO", certNo);
                mjsonParam.setString("CERT_YN", CertYn);

                objRetParam = mobjDao.insert("kr.co.hkcloud.palette3.login.dao.LoginMapper", "insertCertNo", mjsonParam);

            }else{

                objRetParam.setHeader("ERROR_FLAG", true);
                objRetParam.setHeader("ERROR_MSG", "인증번호 발송에 실패했습니다. 다시 시도해주세요.");

                return objRetParam;
            }

        }

        return objRetParam;
    }

    @Override
    public TelewebJSON VerificateCode(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException{
        TelewebJSON objRetParams = new TelewebJSON();
        TelewebJSON objVrfcParams = new TelewebJSON();

        objVrfcParams = mobjDao.select("kr.co.hkcloud.palette3.login.dao.LoginMapper", "verificateCertNo", mjsonParams);

        if(objVrfcParams.getSize() != 0){
            if(Long.parseLong(objVrfcParams.getString("NOW")) > Long.parseLong(objVrfcParams.getString("CERT_NO_EXPRY_DT"))){

                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "인증번호가 만료되었습니다.");

            }else if(!StringUtils.equals(mjsonParams.getString("CERT_CODE"),objVrfcParams.getString("CERT_NO"))){

                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "인증번호가 일치하지 않습니다.");

            }else if("Y".equals(objVrfcParams.getString("CERT_YN"))){

                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "이미 인증된 번호입니다.");

            }else{

                objRetParams.setHeader("ERROR_FLAG", false);
                objRetParams.setHeader("ERROR_MSG", "인증에 성공했습니다.");
                mobjDao.update("kr.co.hkcloud.palette3.login.dao.LoginMapper", "updateSuccessCert", mjsonParams);
            }
        }else{
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "인증정보를 찾을 수 없습니다.");
        }
        return objRetParams;
    }

}
