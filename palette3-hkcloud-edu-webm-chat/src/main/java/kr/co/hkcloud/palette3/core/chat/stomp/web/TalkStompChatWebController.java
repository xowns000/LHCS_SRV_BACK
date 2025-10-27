package kr.co.hkcloud.palette3.core.chat.stomp.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "TalkStompChatWebController",
     description = "텔레톡 STOMP 컨트롤러")
public class TalkStompChatWebController
{
//    /**
//     * 
//     * @return
//     */
//    @GetMapping("/stomp/web/room")
//    public String moveRoom() throws TelewebWebException
//    {
//        return "stomp/room";
//    }
//    
//    /**
//     * 
//     * @param model
//     * @param roomId
//     * @return
//     */
//    @GetMapping("/stomp/web/room/enter/{roomId}")
//    public String moveRoomDetail(Model model, @PathVariable String roomId) throws TelewebWebException
//    {
//        model.addAttribute("roomId", roomId);
//        return "stomp/roomdetail";
//    }

    /**
     * STOMP 상담채팅 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "이전 채팅상담 페이지",
                  notes = "STOMP 상담채팅 페이지로 이동한다")
    @GetMapping("/stomp/web/inout")
    public String moveStompChatInoutPage() throws TelewebWebException
    {
        log.trace("moveStompChatInoutPage");
        return "stomp/inout";
    }


    /**
     * STOMP 상담지원채팅 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "STOMP 상담지원채팅 페이지",
                  notes = "STOMP 상담지원채팅 페이지로 이동한다")
    @GetMapping("/stomp/web/consult")
    public String moveStompChatConsultPage() throws TelewebWebException
    {
        log.trace("moveStompChatConsultPage");
        return "stomp/consult";
    }


    /**
     * STOMP 상담대기 페이지로 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "STOMP 상담대기 페이지",
                  notes = "STOMP 상담대기 페이지로 이동한다")
    @GetMapping("/stomp/web/ready")
    public String moveStompChatReadyPage() throws TelewebWebException
    {
        log.trace("moveStompChatReadyPage");
        return "stomp/ready";
    }
}
