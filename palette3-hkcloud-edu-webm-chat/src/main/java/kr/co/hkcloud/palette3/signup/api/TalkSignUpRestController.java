package kr.co.hkcloud.palette3.signup.api;


import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.security.crypto.Hash;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.signup.app.TalkSignUpService;
import kr.co.hkcloud.palette3.signup.util.TalkSignUpValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SignUpRestController",
     description = "ASP 회원가입 REST 컨트롤러")
public class TalkSignUpRestController
{
    private final TalkSignUpService   talkSignUpService;
    private final TalkSignUpValidator talkSignUpValidator;


    /**
     * 고객사 ID 중복 확인을 처리 한다.
     * 
     * @param            Object
     * @TelewebJsonParam        TelewebJSON mjsonParams, HttpServletRequest mobjRequest, HttpSession mobjSession
     * @return                  TelewebJSON 형식의 결과 데이터
     */
    @ApiOperation(value = "사용자ID 중복 확인",
                  notes = "사용자ID 중복 확인을 처리한다")
    @PostMapping("/api/SignUp/chkDupleUserId")
    public Object chkDupleUserId(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest mobjRequest, HttpSession mobjSession) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //처리 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //중복 확인
        objRetParams = talkSignUpService.chkDupleUserId(jsonParams);

        return objRetParams;

    }


    /**
     * 고객사 이메일 중복 확인을 처리 한다.
     * 
     * @param            Object
     * @TelewebJsonParam        TelewebJSON mjsonParams, HttpServletRequest mobjRequest, HttpSession mobjSession
     * @return                  TelewebJSON 형식의 결과 데이터
     */
    @ApiOperation(value = "메일 중복 확인",
                  notes = "메일 중복 확인을 처리한다")
    @PostMapping("/api/SignUp/chkDupleEmail")
    public Object chkDupleEmail(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest mobjRequest, HttpSession mobjSession) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //처리 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //중복 확인
        objRetParams = talkSignUpService.chkDupleEmail(jsonParams);

        return objRetParams;
    }


    /**
     * 회원 가입을 처리 한다.
     * 
     * @param            Object
     * @TelewebJsonParam        TelewebJSON mjsonParams, HttpServletRequest mobjRequest, HttpSession mobjSession
     * @return                  TelewebJSON 형식의 결과 데이터
     */
    @ApiOperation(value = "회원 가입 처리",
                  notes = "회원 가입, 인증 메일 발송  처리")
    @PostMapping("/api/SignUp/signUpProcess")
    public Object signUpProcess(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest mobjRequest, HttpSession mobjSession, BindingResult result) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //처리 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크
        talkSignUpValidator.validate(mjsonParams, result);
        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //사용자 인증 값 생성
        String userId = jsonParams.getString("USER_ID");
        String certiValue = userId + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        byte[] newpwdbytes = certiValue.getBytes(StandardCharsets.UTF_8);
        String strResult;
        try {
            strResult = Hash.encryptSHA256(newpwdbytes);
        }
        catch(NoSuchAlgorithmException e) {
            throw new TelewebApiException(e.getLocalizedMessage(), e);
        }
        jsonParams.setString("CERTI_YN", "N");
        jsonParams.setString("CERTI_VALUE", strResult);

        //고객사 등록
        objRetParams = talkSignUpService.insertRtnSignUp(jsonParams);

        //사용자 등록
        objRetParams = talkSignUpService.insertRtnTwbBas01(jsonParams);

        //메일 발송
        //String signUpMail = env.getString("signUpMail");

        objRetParams = talkSignUpService.sendMailSignUp(jsonParams);

        return objRetParams;

    }
}
