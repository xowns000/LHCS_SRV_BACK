package kr.co.hkcloud.palette3.signup.web;


import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import kr.co.hkcloud.palette3.signup.app.TalkSignUpService;
import kr.co.hkcloud.palette3.signup.domain.TtalkInitChannelEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 회원가입 컨트롤러
 * 
 * @author CHY
 *
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "SignUpController",
     description = "회원가입 컨트롤러")
public class TalkSignUpController
{

    private final TalkSignUpService     talkSignUpService;
    private final RoutingToAgentManagerService routingToAgentManagerService;

    private final ApplicationEventPublisher eventPublisher;


    /**
     * Security 회원가입 페이지
     * 
     * @param  error
     * @return
     */
    @ApiOperation(value = "회원가입 페이지",
                  notes = "회원가입 페이지로 이동한다")
    @RequestMapping("/signup/signup-form")
    public String moveSignUpForm(Model model) throws TelewebWebException
    {
        log.debug("moveSignUpForm");

        return "signup/signup-form";
    }


    /**
     * Security 회원가입 확인 페이지
     * 
     * @param  error
     * @return
     */
    @ApiOperation(value = "회원가입 확인 페이지",
                  notes = "회원가입 확인 페이지로 이동한다")
    @RequestMapping("/signup/member-auth")
    public String moveMemberAuth(HttpServletRequest hsr, Model model) throws TelewebWebException
    {
        log.debug("moveMemberAuth");

        if(!"POST".equals(hsr.getMethod())) { return "signup/member-auth"; }

        String userId = hsr.getParameter("USER_ID");
        String userNm = hsr.getParameter("USER_NM");
        String eml = hsr.getParameter("EML");
        if(null == userId || null == userNm || "".equals(userId) || "".equals(userNm)) { return "signup/member-auth"; }

        model.addAttribute("USER_ID", userId);
        model.addAttribute("USER_NM", userNm);
        model.addAttribute("EML", eml);

        return "signup/member-auth";
    }


    /**
     * Security 이메일 인증 재 전송 페이지
     * 
     * @param  error
     * @return
     */
    @ApiOperation(value = "이메일 인증 재전송 페이지",
                  notes = "이메일 인증 재전송 페이지로 이동한다")
    @RequestMapping("/signup/request-certi")
    public String moveRequestCerti(HttpServletRequest hsr, Model model) throws TelewebWebException
    {
        log.debug("moveRequestCerti");

        if(!"POST".equals(hsr.getMethod())) { return "signup/request-certi"; }

        String userId = hsr.getParameter("USER_ID");
        String pwd = hsr.getParameter("PWD_SHA");

        if(null == userId || null == userId || "".equals(pwd) || "".equals(pwd)) { return "signup/request-certi"; }

        TelewebJSON jsonParams = new TelewebJSON();

        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("PWD", pwd);

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = talkSignUpService.sendEmailRequest(jsonParams, hsr);

        model.addAttribute("TALK_MSG", objRetParams.getString("TALK_MSG"));

        return "signup/request-certi";
    }


    /**
     * Security 이메일 인증 페이지
     * 
     * @param  error
     * @return
     */
    @ApiOperation(value = "인증 확인 페이지",
                  notes = "이메일 인증 완료를 확인한다.")
    @RequestMapping("/signup/eml-certi/{certiValue}")
    public String moveEmailCerti(@PathVariable @NotEmpty String certiValue, Model model) throws TelewebWebException
    {
        log.debug("emlCertiValue = {}", certiValue);
        log.debug("emlCertiForm");

        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("CERTI_VALUE", certiValue);

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        //이메일 인증 조회
        objRetParams = talkSignUpService.processSignUpUserNmId(jsonParams);

//        // 티톡 채널 연동 ( 실패 하더라도 계정은 생성 하도록함 ) 
//        new Thread(()->{
//        	try {
//        		talkSignUpService.initTtalkChannel(jsonParams);
//        	} catch(Exception e) {
//        		log.error(e.getMessage(), e);
//        	}
//        }).start();

        // 티톡 채널 연동 ( 실패 하더라도 계정은 생성 하도록함 )
        // 스프링 이벤트로 처리
        eventPublisher.publishEvent(TtalkInitChannelEvent.builder().jsonParams(jsonParams).build());

        //시스템메시지, 설정 캐싱처리
        if(objRetParams.getHeaderBoolean("ERROR_FLAG") == false) {
            //시스템메세지 다시 채우자~ - 20200618 liy
            TelewebJSON sysMsgJson = routingToAgentManagerService.selectSystemMessage();
            HcTeletalkDbSystemMessage.getInstance().setSystemMessage(sysMsgJson);

            //상담설정 정보 다시 채우자~ - 20200618 liy
            TelewebJSON outJson = routingToAgentManagerService.selectTalkEnv();
            HcTeletalkDbEnvironment.getInstance().setDbEnvironment(outJson);
        }

        model.addAttribute("USER_NM", objRetParams.getString("USER_NM"));
        model.addAttribute("USER_ID", objRetParams.getString("USER_ID"));
        model.addAttribute("CERTI_MSG", objRetParams.getString("CERTI_MSG"));
        model.addAttribute("TALK_MSG", objRetParams.getString("TALK_MSG"));

        return "signup/eml-certi";
    }


    /**
     * Security ID 찾기
     * 
     * @param  error
     * @return
     */
    @ApiOperation(value = "ID 찾기 페이지",
                  notes = "ID 찾기 페이지로 이동한다")
    @RequestMapping(value = "/signup/find-id",
                    method = {RequestMethod.GET, RequestMethod.POST})
    public String moveFindId(HttpServletRequest hsr, Model model) throws TelewebWebException
    {
        log.debug("fmoveFindId");

        if(!"POST".equals(hsr.getMethod())) { return "signup/find-id"; }

        //화면에서 값을 수정한 다음 다시 찾을 수 있다
        String eml = hsr.getParameter("EML");
        String pwd = hsr.getParameter("PWD_SHA");
        if(null == eml || null == pwd || "".equals(eml) || "".equals(pwd)) { return "signup/find-id"; }

        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("EML_ADDR", eml);
        jsonParams.setString("PWD", pwd);

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = talkSignUpService.selectFindUserId(jsonParams, hsr);
        model.addAttribute("TALK_MSG", objRetParams.getString("TALK_MSG"));

        return "signup/find-id";
    }


    /**
     * Security 비밀번호 찾기
     * 
     * @param  error
     * @return
     */
    @ApiOperation(value = "비밀번호 찾기 페이지",
                  notes = "비밀번호 찾기 페이지로 이동한다")
    @RequestMapping("/signup/find-pwd")
    public String moveFindPwd(HttpServletRequest hsr, Model model) throws TelewebWebException
    {
        log.debug("moveFindPwd");

        if(!"POST".equals(hsr.getMethod())) { return "signup/find-pwd"; }

        //값을 수정한 다음 다시 찾을 수 있다
        String eml = hsr.getParameter("EML");
        String userId = hsr.getParameter("USER_ID");
        if(null == eml || null == userId || "".equals(eml) || "".equals(userId)) { return "signup/find-pwd"; }

        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setString("EML_ADDR", eml);
        jsonParams.setString("USER_ID", userId);

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = talkSignUpService.selectFindPwd(jsonParams);
        model.addAttribute("TALK_MSG", objRetParams.getString("TALK_MSG"));

        return "signup/find-pwd";
    }
}
