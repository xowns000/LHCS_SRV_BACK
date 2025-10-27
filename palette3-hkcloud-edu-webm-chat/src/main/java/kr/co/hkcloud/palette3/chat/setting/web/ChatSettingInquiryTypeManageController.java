package kr.co.hkcloud.palette3.chat.setting.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "ChatSettingInquiryTypeManageController",
     description = "채팅설정문의유형관리-화면 컨트롤러")
public class ChatSettingInquiryTypeManageController
{
    private final FileRulePropertiesUtils fileRulePropertiesUtils;


    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅설정문의유형관리-화면",
                  notes = "채팅설정문의유형관리 화면으로 이동한다")
    @GetMapping("/chat/setting/web/inqry-ty-manage")
    public String moveTalkMngInqryType(Model model) throws TelewebWebException
    {
        log.debug("moveTalkMngInqryType");

        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //문의유형이미지
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);
        model.addAttribute("fileProperties", fileProperties);

        model.addAttribute("timestamp", DateCmmnUtils.toEpochMilli());
        return "chat/setting/chat-setting-inquiry-type-manage";
    }
}
