package kr.co.hkcloud.palette3.board.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "BoardController",
     description = "게시판-화면 컨트롤러")
public class BoardController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "게시판-화면",
                  notes = "게시판 화면으로 이동한다")
    @GetMapping("/board/web/board-list")
    public String moveBoardList() throws TelewebWebException
    {
        log.debug("moveBoardList");
        return "board/board-list";
    }
}
