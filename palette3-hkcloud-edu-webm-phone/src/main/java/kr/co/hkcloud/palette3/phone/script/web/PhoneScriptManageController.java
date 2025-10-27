package kr.co.hkcloud.palette3.phone.script.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "phoneScriptManageController",
     description = "스크립트관리 컨트롤러")
public class PhoneScriptManageController
{
    private final FileRulePropertiesUtils fileRulePropertiesUtils;


    /**
     * 스크립트관리 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "스크립트관리 페이지",
                  notes = "스크립트관리 이동한다")
    @GetMapping("/phone/script/web/manage")
    public String moveCnslScrtMng(Model model) throws TelewebWebException
    {
        log.debug("moveCnslScrtMng");

        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.phone;    //게시판
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.files;  //파일
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);
        model.addAttribute("fileProperties", fileProperties);

        return "phone/script/phone-script-manage";
    }
}
