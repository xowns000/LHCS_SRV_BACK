package kr.co.hkcloud.palette3.chat.setting.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingProCounselByAgentManageService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingProCounselByAgentManageValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatSettingProCounselByAgentManageRestController", description = "채팅설정사용자별전문상담관리 REST 컨트롤러")
public class ChatSettingProCounselByAgentManageRestController {

    private final ChatSettingProCounselByAgentManageValidator chatSettingProCounselByAgentManageValidator;
    private final ChatSettingProCounselByAgentManageService chatSettingProCounselByAgentManageService;


    /**
     *
     * @Transactional 트랜잭션 사용
     * @param         inHashMap 전송된 파라메터
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @SystemEventLogAspectAnotation(value = "CHT_PRO-CUSL-MNG_INQRY-TY_PROC", note = "상담직원문의유형관리 상담사설정 변경(등록,수정)")
    @ApiOperation(value = "상담직원문의유형관리 상담사설정 변경(등록,수정)", notes = "상담직원문의유형관리 상담사설정 변경(등록,수정)")
    @PostMapping("/chat-api/setting/pro-counsel-by-agent-manage/inqry-ty/process")
    public Object processRtnUserInqry(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");

        if ("QSTN".equals(mjsonParams.getString("PROC_TYPE"))) {
            return processRtnUserInqryToInqry(mjsonParams);
        } else {
            return processRtnUserInqryToUser(mjsonParams);
        }
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     *
     */
    @SystemEventLogAspectAnotation(value = "CHT_PRO-CUSL-MNG_INQRY-TY_DEL", note = "상담직원문의유형관리 상담사설정 삭제")
    @ApiOperation(value = "채팅설정사용자별전문상담관리-사용자삭제", notes = "권한그룹별 할당사용자 삭제")
    @PostMapping("/chat-api/setting/pro-counsel-by-agent-manage/agent/delete")
    public Object deleteRtnUserInqry(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);

        JSONArray objArry = mjsonParams.getDataObject();
        JSONObject objJson = new JSONObject();

        if (!objArry.isEmpty()) {
            for (int i = 0; i < objArry.size(); i++) {
                objJson = objArry.getJSONObject(i);
                if (!objJson.isEmpty() && !objJson.isNullObject()) {
                    objParams.setDataObject(JSONArray.fromObject(objJson));
                    chatSettingProCounselByAgentManageService.DELETE_TALK_USER_INQRY(objParams);
                }
            }
        }
        //최종결과값 반환
        return objRetParams;
    }


    private TelewebJSON processRtnUserInqryToInqry(TelewebJSON mjsonParams) throws TelewebApiException {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = chatSettingProCounselByAgentManageService.STNG_CHT_USER_SKILL(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    private TelewebJSON processRtnUserInqryToUser(TelewebJSON mjsonParams) throws TelewebApiException {

        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = chatSettingProCounselByAgentManageService.STNG_CHT_SKILL_USER(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     *
     */
    @ApiOperation(value = "채팅설정사용자별전문상담관리-스킬우선순위변경", notes = "사용자 문의유형 스킬 우선순위 변경")
    @PostMapping("/chat-api/setting/pro-counsel-by-agent-manage/skill-priort/modify")
    public Object updateUserInqrySkillRank(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //client에서 전송받은 파라메터 생성
        //        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        objParams.setString("CUSTCO_ID", mjsonParams.getString("CUSTCO_ID"));
        objParams.setString("ASP_NEWCUST_KEY", mjsonParams.getString("ASP_NEWCUST_KEY"));

        String strFlag = mjsonParams.getString("FLAG");
        for (int i = 0; i < mjsonParams.getDataObject("DATA").size(); i++) {
            if ("up".equals(strFlag)) {
                objParams.setString("TALK_INQRY_CD", mjsonParams.getString("TALK_INQRY_CD", i));
                objParams.setString("USER_ID", mjsonParams.getString("USER_ID", i));

                if (mjsonParams.getInt("SKILL_RANK", i) > 1) {
                    objParams.setInt("SKILL_RANK", (mjsonParams.getInt("SKILL_RANK", i) - 1));
                } else {
                    objParams.setInt("SKILL_RANK", 1);
                }

                chatSettingProCounselByAgentManageService.updateUserSkillRank(objParams);       // user_id 조회 전체 삭제
            } else {

                objParams.setString("TALK_INQRY_CD", mjsonParams.getString("TALK_INQRY_CD", i));
                objParams.setString("USER_ID", mjsonParams.getString("USER_ID", i));
                objParams.setInt("SKILL_RANK", (mjsonParams.getInt("SKILL_RANK", i) + 1));

                chatSettingProCounselByAgentManageService.updateUserSkillRank(objParams);       // user_id 조회 전체 삭제
            }

        }

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     *
     */
    @ApiOperation(value = "채팅설정사용자별전문상담관리-문의유형미할당유저조회", notes = "문의유형 미할당된 유져를 조회한다.")
    @PostMapping("/chat-api/setting/pro-counsel-by-agent-manage/inqry-ty-unasgn-user/inqire")
    public Object selectRtnUserInqry(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        chatSettingProCounselByAgentManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("ASP_NEWCUST_KEY", mjsonParams.getString("ASP_NEWCUST_KEY"));

        objRetParams = chatSettingProCounselByAgentManageService.selectRtnUserInqry(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     *
     */
    @ApiOperation(value = "채팅설정사용자별전문상담관리-문의유형할당유저조회", notes = "문의유형 할당된 유져를 조회한다.")
    @PostMapping("/chat-api/setting/pro-counsel-by-agent-manage/inqry-ty-asgn-user/inqire")
    public Object selectRtnUserAllocInqry(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //chatSettingProCounselByAgentManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        jsonParams.setString("ASP_NEWCUST_KEY", mjsonParams.getString("ASP_NEWCUST_KEY"));

        objRetParams = chatSettingProCounselByAgentManageService.selectRtnUserAllocInqry(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     *
     */
    @ApiOperation(value = "채팅설정사용자별전문상담관리-미할당문의유형조회", notes = "해당 사용자의 미할당 문의유형을 조회한다.")
    @PostMapping("/chat-api/setting/pro-counsel-by-agent-manage/unasgn-inqry-ty/inqire")
    public Object selectRtnUserInqryByUser(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //        chatSettingProCounselByAgentManageValidator.validate(mjsonParams, result); //VALIDATION 체크
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingProCounselByAgentManageService.selectRtnUserInqryByUser(jsonParams);

        return objRetParams;
    }

}
