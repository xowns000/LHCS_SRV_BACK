package kr.co.hkcloud.palette3.chat.setting.web;


import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
@Api(value = "ChatSettingImageManageController",
     description = "채팅설정이미지관리-화면 컨트롤러")
public class ChatSettingImageManageController
{
    private final FileRulePropertiesUtils fileRulePropertiesUtils;


    /**
     * 
     * @return
     */
    @ApiOperation(value = "채팅설정이미지관리-화면",
                  notes = "채팅설정이미지관리 화면으로 이동한다")
    @GetMapping("/chat/setting/web/image-manage")
    public String moveTalkMngImg(Model model) throws TelewebWebException
    {
        log.debug("moveTalkMngImg");

        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);
        model.addAttribute("fileProperties", fileProperties);

        model.addAttribute("timestamp", DateCmmnUtils.toEpochMilli());
        return "chat/setting/chat-setting-image-manage";
    }


    /**
     * 이미지 읽어오기
     * 
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "상담이미지 읽어오기",
                  notes = "상담이미지를 읽어온다")
    @RequestMapping(value = "/mng/imgs/web/getImg/{fileid}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> getImg(@PathVariable("fileid") String fileid, Model model) throws IOException
    {
        ClassPathResource imgFile = new ClassPathResource("image/sid.jpg");

        //BRD_ID mng 정보 일어오고

        //
        // model.addAttribute("BLD_MNG", attributeValue);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(imgFile.getInputStream()));
    }
}
