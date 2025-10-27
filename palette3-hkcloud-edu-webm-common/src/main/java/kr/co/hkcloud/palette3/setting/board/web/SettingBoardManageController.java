package kr.co.hkcloud.palette3.setting.board.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@Api(value = "SettingBoardManageController",
     description = "설정게시판관리 컨트롤러")
public class SettingBoardManageController
{
    /**
     * 
     * @return
     */
    @ApiOperation(value = "설정게시판관리-화면",
                  notes = "설정게시판관리 화면으로 이동한다")
    @GetMapping("/setting/board/web/manage")
    public String moveSettingBoardManage() throws TelewebWebException
    {
        log.debug("moveSettingBoardManage");
        return "setting/board/setting-board-manage";
    }
}
