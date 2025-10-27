package kr.co.hkcloud.palette3.setting.system.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebValidationException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemMenuManageService;
import kr.co.hkcloud.palette3.setting.system.util.SettingSystemMenuManageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SettingSystemMenuManageRestController", description = "설정시스템메뉴관리 REST 컨트롤러")
public class SettingSystemMenuManageRestController {

    private final SettingSystemMenuManageService settingSystemMenuManageService;
    private final SettingSystemMenuManageValidator settingSystemMenuManageValidator;
    private final PaletteJsonUtils paletteJsonUtils;


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "설정시스템메뉴관리-트리목록", notes = "설정시스템메뉴관리 트리목록을 조회한다.")
    @PostMapping("/api/setting/system/menu-manage/tr/inqire")
    public Object selectRtnMenuTree(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = settingSystemMenuManageService.selectRtnMenuTree(mjsonParams);

        List<Map<String, Object>> menuList = null;

        if (objRetParams.getDataObject("DATA").size() > 0) {
            String UP_MENU_ID = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_MENU_ID");
            menuList = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), UP_MENU_ID, "MENU_ID", "UP_MENU_ID", "MENU_NM",
                "SORT");
            objRetParams.setDataObject("MENU_TREE", paletteJsonUtils.getJsonArrayFromList(menuList));
        }

        return objRetParams;
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "설정시스템메뉴관리-그룹목록", notes = "설정시스템메뉴관리 그룹목록을 조회한다")
    @PostMapping("/api/setting/system/menu-manage/group/inqire")
    public Object selectRtnMenuGroup(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemMenuManageService.selectRtnMenuGroup(jsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "설정시스템메뉴관리-ID중복체크", notes = "설정시스템메뉴관리 ID중복체크한다")
    @PostMapping("/api/setting/system/menu-manage/id-dplct-ceck/inqire")
    public Object selectDupMenuID(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemMenuManageService.selectDupMenuID(jsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "설정시스템메뉴관리-정보조회(URL경로)", notes = "설정시스템메뉴관리 URL경로로 정보를 조회한다")
    @PostMapping("/api/setting/system/menu-manage/info-by-url-cours/inqire")
    public Object selectMenuIdByMenuPath(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemMenuManageService.selectMenuIdByMenuPath(jsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "설정시스템메뉴관리-목록", notes = "설정시스템메뉴관리 목록을 조회한다")
    @PostMapping("/api/setting/system/menu-manage/inqire")
    public Object selectTwbBas04(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        //Validation 체크 
        settingSystemMenuManageValidator.validate(mjsonParams, result);
        if (result.hasErrors()) {
            throw new TelewebValidationException(result.getAllErrors());
        }

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemMenuManageService.selectTwbBas04(jsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "설정시스템메뉴관리-게시판유형조회", notes = "설정시스템메뉴관리 게시판유형을 조회한다")
    @PostMapping("/api/setting/system/menu-manage/bbs-ty/inqire")
    public Object selectBoardPath(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemMenuManageService.selectBoardPath(jsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "설정시스템메뉴관리-게시판유형코드조회", notes = "설정시스템메뉴관리 게시판유형코드를 조회한다")
    @PostMapping("/api/setting/system/menu-manage/bbs-ty-code/inqire")
    public Object selectBoardPathDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        return settingSystemMenuManageService.selectBoardPathDetail(jsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return TelewebJSON 형식의 등록결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_MENU-MNG_PROC", note = "설정 메뉴관리 변경(등록,수정)")
    @ApiOperation(value = "설정시스템메뉴관리-등록", notes = "설정시스템메뉴관리 메뉴를 등록한다")
    @PostMapping("/api/setting/system/menu-manage/regist")
    public Object processRtnMenu(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //Validation 체크 
        //        settingSystemMenuManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //최종결과값 반환
        return settingSystemMenuManageService.processRtnMenu(mjsonParams);
    }

    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return TelewebJSON 형식의 등록결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_MENU-MNG_ORD_UPDATE", note = "설정 메뉴관리 순서 변경")
    @ApiOperation(value = "설정시스템메뉴관리 순서 변경", notes = "설정시스템메뉴관리 순서 변경")
    @PostMapping("/api/setting/system/menu-manage/change-order/regist")
    public Object changeMenuOrder(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //최종결과값 반환
        return settingSystemMenuManageService.changeMenuOrder(mjsonParams);
    }


    /**
     *
     * @param  mjsonParams
     * @param  result
     * @return TelewebJSON 형식의 삭제결과 데이터
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_MENU-MNG_DEL", note = "설정 메뉴관리 삭제")
    @ApiOperation(value = "게시판메뉴관리-삭제", notes = "게시판메뉴관리 메뉴를 삭제한다")
    @PostMapping("/api/setting/system/menu-manage/delete")
    public Object deleteRtnMenu(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        //Validation 체크 
        //        settingSystemMenuManageValidator.validate(mjsonParams, result);
        //        if(result.hasErrors()) { throw new TelewebValidationException(result.getAllErrors()); }

        //최종결과값 반환
        return settingSystemMenuManageService.deleteRtnMenu(mjsonParams);
    }

    /**
     * 사용가능한 연동 레이아웃목록 조회
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @PostMapping("/api/setting/system/menu-manage/selectLkagLayoutList")
    public Object selectLkagLayoutList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return settingSystemMenuManageService.selectLkagLayoutList(mjsonParams);
    }

    //    /**
    //     * 메뉴관리 화면연결부문(프로그램 경로 검색 팝업)
    //     *
    //     * @return
    //     * @throws TelewebApiException
    //     */
    //    @ApiOperation(value = "설정시스템메뉴관리/프로그램-경로-조회-팝업/하위폴더/조회",
    //                  notes = "메뉴관리 입력받은 경로의 하위 모든 폴더정보를 리턴한다")
    //    @PostMapping("/api/setting/system/menu-manage/progrm-path-inqire-popup/lwprt-folder/inqire")
    //    public Object selectRtnFolderInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    //    {
    //        return settingSystemMenuManageService.selectRtnFolderInfo(mjsonParams);
    //    }
    //
    //
    //    /**
    //     *
    //     * @return
    //     * @throws TelewebApiException
    //     */
    //    @ApiOperation(value = "설정시스템메뉴관리-폴더목록",
    //                  notes = "설정시스템메뉴관리 폴더목록을 조회한다")
    //    @PostMapping("/api/setting/system/menu-manage/progrm-path-inqire-popup/folder/list")
    //    public Object selectRtnFileList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    //    {
    //        return settingSystemMenuManageService.selectRtnFileList(mjsonParams);
    //    }

}
