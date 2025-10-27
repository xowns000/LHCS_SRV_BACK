package kr.co.hkcloud.palette3.common.chat.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.chat.app.ChatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ChatCmmnRestController",
     description = "채팅공통 REST 컨트롤러")
public class ChatCmmnRestController
{
    private final ChatCmmnService chatCmmnService;


    /**
     * 채팅전달 에이전트 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "채팅전달 에이전트 조회",
                  notes = "채팅을 전달할 에이전트를 조회한다")
    @PostMapping("/chat-api/common/chat-delvry-agent/inqry")
    public Object selectRtnPageAgentDeliver(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatCmmnService.selectRtnPageAgentDeliver(jsonParams);

        return objRetParams;
    }


    /**
     * 상담설정정보 리스트 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "상담설정정보 리스트 조회",
                  notes = "상담설정정보 리스트 조회")
    @PostMapping("/chat-api/common/cnslt-set-info-ll//inqry")
    public Object selectRtnCnslProp(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatCmmnService.selectRtnCnslProp(jsonParams);

        return objRetParams;
    }


    /**
     * 업무시작시간 선택
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "업무 시작시간 조회",
                  notes = "업무 시작시간 조회")
    @PostMapping("/api/statistics/chat/common/start-job-time/inqire")
    public Object selectSTJobTime(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatCmmnService.selectSTJobTime(jsonParams);

        return objRetParams;
    }


    /**
     * 업무종료시간 선택
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "업무 종료시간 조회",
                  notes = "업무종료시간 조회")
    @PostMapping("/api/statistics/chat/common/end-job-time/inqire")
    public Object selectENDJobTime(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = chatCmmnService.selectENDJobTime(jsonParams);

        return objRetParams;
    }

}
