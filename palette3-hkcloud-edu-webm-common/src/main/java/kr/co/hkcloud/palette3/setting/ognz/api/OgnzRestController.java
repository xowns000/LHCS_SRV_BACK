package kr.co.hkcloud.palette3.setting.ognz.api;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.ognz.app.OgnzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 *
 * Description : 조직 관리
 * package : kr.co.hkcloud.palette3.setting.ognz.api
 * filename : OgnzRestController.java
 * Date : 2023. 6. 7.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 7., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "OgnzRestController", description = "조직 관리 REST 컨트롤러")
public class OgnzRestController {

    private final PaletteJsonUtils paletteJsonUtils;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final OgnzService ognzService;

    @ApiOperation(value = "조직 Tree", notes = "조직 Tree를 조회한다")
    @PostMapping("/api/setting/ognz/ognzTreeList")
    public Object cuttTypeTreeList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = ognzService.ognzTreeList(mjsonParams);

        List<Map<String, Object>> ognzList = null;

        if (objRetParams.getDataObject("DATA").size() > 0) {
            String UP_DEPT_ID = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_DEPT_ID");
            ognzList = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), UP_DEPT_ID, "DEPT_ID", "UP_DEPT_ID", "DEPT_NM",
                "SORT");
            objRetParams.setDataObject("OGNZ_TREE", paletteJsonUtils.getJsonArrayFromList(ognzList));
        }

        return objRetParams;
    }

    /**
     *
     * 조직 관리-등록, 수정
     * 
     * @Method Name : ognzProc
     * @date : 2023. 6. 7.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_OGNZ_PROC", note = "조직정보설정 변경(등록,수정)")
    @ApiOperation(value = "조직정보설정 변경(등록,수정)", notes = "확장속성관리 신규 확장속성을 등록 한다")
    @PostMapping("/api/setting/ognz/ognzProc")
    public Object ognzProc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return ognzService.ognzProc(mjsonParams);
    }

    /**
     *
     * 조직 관리-삭제
     * 
     * @Method Name : ognzDel
     * @date : 2023. 6. 7.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_OGNZ_DEL", note = "조직정보설정 삭제")
    @ApiOperation(value = "조직정보설정 삭제", notes = "확장속성관리 속성을 삭제 한다")
    @PostMapping("/api/setting/ognz/ognzDel")
    public Object ognzDel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return ognzService.ognzDel(mjsonParams);
    }

    /**
     *
     * 조직 순서 재정의
     * 
     * @Method Name : ognzReOrder
     * @date : 2023. 6. 7.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_OGNZ_ORD_UPDATE", note = "조직정보설정 순서 변경")
    @ApiOperation(value = "조직정보설정 순서 변경", notes = "확장항목의 순서를 재 정의 한다.")
    @PostMapping("/api/setting/ognz/ognzOrderUpdate")
    public Object ognzOrderUpdate(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return ognzService.ognzOrderUpdate(mjsonParams);
    }

    /**
     * 
     * 지역 Tree
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "지역 Tree", notes = "조직 Tree를 조회한다")
    @PostMapping("/api/setting/ognz/rgnList")
    public Object rgnList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = ognzService.rgnList(mjsonParams);

        List<Map<String, Object>> ognzList = null;

        if (objRetParams.getDataObject("DATA").size() > 0) {
            String UP_RGN_CD = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_RGN_CD");
            ognzList = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), UP_RGN_CD, "RGN_CD", "UP_RGN_CD", "RGN_NM",
                "SORT");
            objRetParams.setDataObject("RGN_TREE", paletteJsonUtils.getJsonArrayFromList(ognzList));
        }

        return objRetParams;
    }
}
