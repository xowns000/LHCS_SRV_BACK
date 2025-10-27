package kr.co.hkcloud.palette3.chat.setting.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingMessageManageService;
import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingSystemMessageManageService;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingSystemMessageManageUtils;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingSystemMessageManageValidator;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerService;
import kr.co.hkcloud.palette3.core.chat.router.domain.CustcoId;
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
@Api(value = "ChatSettingSystemMessageManageRestController", description = "채팅설정시스템메시지관리 REST 컨트롤러")
public class ChatSettingSystemMessageManageRestController {

    private final RoutingToAgentManagerService routingToAgentManagerService;
    private final ChatSettingSystemMessageManageService chatSettingSystemMessageManageService;
    private final ChatSettingSystemMessageManageUtils chatSettingSystemMessageManageUtils;
    private final ChatSettingMessageManageService chatSettingMessageManageService;
    private final ChatSettingSystemMessageManageValidator chatSettingSystemMessageManageValidator;


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅설정시스템메시지관리-타입조회", notes = "채팅설정시스템메시지관리-타입조회")
    @PostMapping("/chat-api/setting/system-mssage-manage/ty/inqire")
    public Object selectSystemMsgType(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingSystemMessageManageService.selectSystemMsgType(jsonParams);

        return objRetParams;
    }


    /**
     * 시스템메시지 삭제 - 트랜젝션 시작 시점 주의 필요
     */
    @ApiOperation(value = "시스템메시지 삭제", notes = "시스템메시지 삭제")
    @PostMapping("/chat-api/setting/system-mssage-manage/delete")
    public Object deleteSystemMsg(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        objRetParams.setHeader("ERROR_FLAG", false);

        // validation check
        //        int ErrorCode = chatSettingSystemMessageManageUtils.deleteSystemMsgValidationCheck(mjsonParams.getDataObject("DATA"));
        int ErrorCode = 0;

        //validation 체크
        //            chatSettingSystemMessageManageValidator.validate(mjsonParams, result);
        //            if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        ErrorCode = chatSettingSystemMessageManageService.deleteSystemMsg(objRetParams, mjsonParams);

        if (ErrorCode == 0) {
            //시스템메세지 삭제 후 다시 채우자~ - 20200521 liy
            //                TelewebJSON sysMsgJson = routingToAgentManager.selectSystemMessage();
            //                HcTeletalkDbSystemMessage.getInstance().clear();
            //                HcTeletalkDbSystemMessage.getInstance().setSystemMessage(sysMsgJson);

            //해당 메시지만 삭제, 전체 clear는 오류 발생함 (필드값없으면 오류 발생)
            HcTeletalkDbSystemMessage.getInstance()
                .clear(new CustcoId(mjsonParams.getString("CUSTCO_ID"), mjsonParams.getString("SYS_MSG_ID")));
        }
        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "시스템 메시지 조회", notes = "시스템 메시지 조회.")
    @PostMapping("/chat-api/setting/system-mssage-manage/list")
    public Object selectSystemMsgList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //validation 체크

        //        chatSettingSystemMessageManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingSystemMessageManageService.selectSystemMsgList(jsonParams);

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅설정시스템메시지관리-링크정보조회", notes = "채팅설정시스템메시지관리-링크정보조회")
    @PostMapping("/chat-api/setting/system-mssage-manage/link-info/inqire")
    public Object selectSystemMsgLinkData(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingSystemMessageManageService.selectSystemMsgLinkData(jsonParams);

        return objRetParams;
    }


    /**
     * 시스템메시지 등록 - 트랜젝션 시작 시점 주의 필요
     */
    @SystemEventLogAspectAnotation(value = "CHT_SYS-MSG-MNG_PROC", note = "채팅시스템메시지 변경(등록,수정)")
    @ApiOperation(value = "시스템메시지 등록", notes = "시스템메시지 등록")
    @PostMapping("/chat-api/setting/system-mssage-manage/regist")
    public Object insertNewSystemMsg(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setDataObject("ASP_NEWCUST_KEY", mjsonParams.getDataObject("CUSTCO_ID"));

        int ErrorCode = 0;

        //validation 체크

        //        chatSettingSystemMessageManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        ErrorCode = chatSettingSystemMessageManageService.insertNewSystemMsg(objRetParams, mjsonParams);

        if (ErrorCode == 0) {
            //시스템메시지 설정
            TelewebJSON sysMsgJson = routingToAgentManagerService.selectSystemMessage();
            HcTeletalkDbSystemMessage.getInstance().setSystemMessage(sysMsgJson);
        }
        return objRetParams;
    }


    /**
     * 시스템메시지 수정 (TextType)
     */
    @SystemEventLogAspectAnotation(value = "CHT_SYS-MSG-MNG_PROC", note = "채팅시스템메시지 변경(등록,수정)")
    @ApiOperation(value = "시스템메시지 수정", notes = "시스템메시지 수정 (TextType)")
    @PostMapping("/chat-api/setting/system-mssage-manage/text/modify")
    public Object modifySystemMsgByText(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        objRetParams.setHeader("ERROR_FLAG", false);
        int ErrorCode = 0;

        // validation check 1
        ErrorCode = chatSettingSystemMessageManageUtils.systemMsgValidation("UPD", mjsonParams.getDataObject("DATA"));

        objRetParams = chatSettingMessageManageService.selectRtnCustMsgDuplicatedCnt(mjsonParams);
        if (objRetParams.getInt("CNT_DUPLICAE_MSG_TIME") > 0) {
            //사용자 안내 용 멘트는 화면으로 메시지 전달하여 처리 (exception으로 처리시 "시스템 장애로... " 문구 출력됨
            //        	throw new AspCnslMsgDuplicatedMsgTimeException("지연시간은 중복으로 설정할 수 없습니다.");
            objRetParams = new TelewebJSON(mjsonParams);
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "지연시간은 중복으로 설정할 수 없습니다.");
            return objRetParams;
        }

        objRetParams = chatSettingMessageManageService.selectRtnCustcoMsgDuplicatedCnt(mjsonParams);
        if (objRetParams.getInt("CNT") > 0) {
            //사용자 안내 용 멘트는 화면으로 메시지 전달하여 처리 (exception으로 처리시 "시스템 장애로... " 문구 출력됨
            //        	throw new AspCnslMsgDuplicatedMsgTimeException("지연시간은 중복으로 설정할 수 없습니다.");
            objRetParams = new TelewebJSON(mjsonParams);
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 해당 시스템메시지가 존재합니다");
            return objRetParams;
        }

        if (ErrorCode == 0) {
            //validation 체크
            chatSettingSystemMessageManageValidator.validate(mjsonParams, result);
            if (result.hasErrors()) {
                throw new TelewebValidationException(result.getAllErrors());
            }

            objRetParams.setDataObject("DATA", mjsonParams.getDataObject("DATA"));
            chatSettingSystemMessageManageService.updateSystemMsgByText(objRetParams);

            //시스템메세지 수정 후 다시 채우자~ - 20200521 liy
            TelewebJSON sysMsgJson = routingToAgentManagerService.selectSystemMessage();
            HcTeletalkDbSystemMessage.getInstance().setSystemMessage(sysMsgJson);
        } else if (ErrorCode > 0) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", chatSettingSystemMessageManageUtils.getErrorMsg(ErrorCode));
        }

        return objRetParams;
    }


    /**
     * 시스템메시지 수정 (LinkType)
     */
    @SystemEventLogAspectAnotation(value = "CHT_SYS-MSG-MNG_LNK_UPDATE", note = "채팅시스템메시지 링크 수정")
    @ApiOperation(value = "채팅설정시스템메시지관리-링크수정", notes = "채팅설정시스템메시지관리-링크수정")
    @PostMapping("/chat-api/setting/system-mssage-manage/link/modify")
    public Object modifySystemMsgByLink(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //mjsonParams 해더에 UUID가 필요하기 때문에 파라메터로 넘겨줘야 한다.
        TelewebJSON linksParams = new TelewebJSON(mjsonParams);

        objRetParams.setHeader("ERROR_FLAG", false);

        int ErrorCode = 0;

        // validation check 1
        ErrorCode = chatSettingSystemMessageManageUtils.systemMsgValidation("", mjsonParams.getDataObject("DATA"));

        if (ErrorCode == 0) {
            //validation 체크
            chatSettingSystemMessageManageValidator.validate(mjsonParams, result);
            if (result.hasErrors()) {
                throw new TelewebValidationException(result.getAllErrors());
            }

            objRetParams.setDataObject("DATA", mjsonParams.getDataObject("DATA"));

            chatSettingSystemMessageManageService.modifySystemMsgByLink(objRetParams);

            JSONObject jsonObj = (JSONObject) objRetParams.getDataObject("DATA").get(0);
            // validation check 2
            ErrorCode = chatSettingSystemMessageManageUtils.systemMsgValidationByLinkMsg(mjsonParams.getDataObject("LINKDATA"));
            if (ErrorCode == 0) {
                // 링크 메시지가 있을 경우, 링크메시지 등록.
                JSONArray objArray = mjsonParams.getDataObject("LINKDATA");
                if (objArray != null && objArray.size() > 0) {
                    for (int i = 0; i < objArray.size(); i++) {

                        linksParams.setDataObject("DATA", (JSONObject) mjsonParams.getDataObject("LINKDATA").get(i));
                        linksParams.setString("SYS_MSG_ID", jsonObj.getString("SYS_MSG_ID"));
                        linksParams.setString("REG_ID", jsonObj.getString("LINK_UPD_ID"));
                        linksParams.setString("DEPT_CODE", jsonObj.getString("LINK_UPD_DEPT_CODE"));
                        TelewebJSON objRetParams01 = new TelewebJSON(mjsonParams);
                        objRetParams01 = chatSettingSystemMessageManageService.selectSystemMsgLinkData(linksParams);
                        if (objRetParams01.getHeaderInt("COUNT") == 0) {
                            chatSettingSystemMessageManageService.insertSystemLinksMsg(linksParams);
                        } else {
                            chatSettingSystemMessageManageService.updateSystemLinksMsg(linksParams);
                        }
                    }
                }
            } else if (ErrorCode > 0) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", chatSettingSystemMessageManageUtils.getErrorMsg(ErrorCode));
            }
        } else if (ErrorCode > 0) {
            //            String errorMsg = getErrorMsg(ErrorCode);
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", chatSettingSystemMessageManageUtils.getErrorMsg(ErrorCode));
        }

        return objRetParams;

    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅설정시스템메시지 기본 시스템 메시지 리스트", notes = "채팅설정시스템메시지 기본 시스템 메시지 리스트")
    @PostMapping("/chat-api/setting/system-mssage-manage/common-sysMsg/list")
    public Object commonSysMsgList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatSettingSystemMessageManageService.commonSysMsgList(jsonParams);

        return objRetParams;
    }

}
