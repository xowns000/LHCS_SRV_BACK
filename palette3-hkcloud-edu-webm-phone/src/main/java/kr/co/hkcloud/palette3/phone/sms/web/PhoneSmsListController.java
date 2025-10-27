package kr.co.hkcloud.palette3.phone.sms.web;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebWebException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@RequiredArgsConstructor
@Controller
@Api(value = "phoneSmsListController",
     description = "SMS리스트 컨트롤러")
public class PhoneSmsListController
{
    private final FileRulePropertiesUtils fileRulePropertiesUtils;


    /**
     * SMS리스트 이동한다
     * 
     * @return
     */
    @ApiOperation(value = "SMS리스트 페이지",
                  notes = "SMS리스트 이동한다")
    @GetMapping("/phone/sms/web/list")
    public String moveSmsList(Model model) throws TelewebWebException
    {
        log.debug("moveCnslScrtMng");

        final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.phone;    //게시판
        final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.files;  //파일
        final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);
        log.debug("fileProperties>>>{}", fileProperties);
        model.addAttribute("fileProperties", fileProperties);

        return "phone/sms/phone-sms-list";
    }
}
