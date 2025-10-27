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
@Api(value = "phoneScriptInqireController",
     description = "상담스크립트조회 컨트롤러")
public class PhoneScriptInqireController
{
    private final FileRulePropertiesUtils fileRulePropertiesUtils;


    /**
     * 상담스크립트조회 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "상담스크립트조회 페이지",
                  notes = "상담스크립트조회 이동한다")
    @GetMapping("/phone/script/web/inqire")
    public String moveCnslScrtInq(Model model) throws TelewebWebException
    {
        log.debug("moveCnslScrtInq");
        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.phone;    //게시판
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.files;  //파일
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        model.addAttribute("fileProperties", fileProperties);

        return "phone/script/phone-script-inqire";
    }
}
