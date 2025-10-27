package kr.co.hkcloud.palette3.setting.ipExt.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.ipExt.app.IpExtService;
import kr.co.hkcloud.palette3.setting.ipExt.util.IpExtValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Description : IP 내선번호 설정 Rest Controller
 * package  : kr.co.hkcloud.palette3.setting.ipExt.api
 * filename : IpExtRestController.java
 * Date : 2023. 6. 9.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 9., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "IpExtRestController", description = "확장속성 관리 REST 컨트롤러")
public class IpExtRestController {

    private final IpExtService ipExtService;
    private final IpExtValidator ipExtValidator;


    /**
     *
     * IP 내선번호 관리-목록
     * @Method Name    : ipExtList
     * @date            : 2023. 5. 15.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "IP 내선번호 관리-목록", notes = "IP 내선번호 관리 목록을 조회한다")
    @PostMapping("/api/setting/ipExt/ipExtList")
    public Object ipExtList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return ipExtService.ipExtList(mjsonParams);
    }


    /**
     *
     * IP 내선번호 관리-등록, 수정
     * @Method Name    : ipExtProc
     * @date            : 2023. 5. 16.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_IP_EXT_PROC", note = "설정 전화기설정 변경(등록,수정)")
    @ApiOperation(value = "IP 내선번호 관리-등록, 수정", notes = "IP 내선번호 관리 등록 및 수정을 한다")
    @PostMapping("/api/setting/ipExt/ipExtProc")
    public Object ipExtProc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return ipExtService.ipExtProc(mjsonParams);
    }

    /**
     *
     * 내선번호 중복 체크
     * @Method Name    : extNoDuplCheck
     * @date            : 2023. 6. 12.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "내선번호 중복 체크", notes = "내선번호 중복 체크를 한다")
    @PostMapping("/api/setting/ipExt/extNoDuplCheck")
    public Object extNoDuplCheck(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return ipExtService.extNoDuplCheck(mjsonParams);
    }

    /**
     *
     * IP 내선번호 관리-삭제
     * @Method Name    : ipExtDel
     * @date            : 2023. 5. 17.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_IP_EXT_DEL", note = "설정 전화기설정 삭제")
    @ApiOperation(value = "IP 내선번호 관리-삭제", notes = "IP 내선번호 관리 속성을 삭제 한다")
    @PostMapping("/api/setting/ipExt/ipExtDel")
    public Object ipExtDel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return ipExtService.ipExtDel(mjsonParams);
    }

    /**
     *
     * 내선 번호가 있는 사용자 목록
     * @Method Name    : extNotEmptyCuslList
     * @date            : 2023. 7. 27.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "내선 번호가 있는 사용자 목록", notes = "내선 번호가 있는 사용자 목록을 조회한다")
    @PostMapping("/api/setting/ipExt/extNotEmptyCuslList")
    public Object extNotEmptyCuslList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return ipExtService.extNotEmptyCuslList(mjsonParams);
    }
}
