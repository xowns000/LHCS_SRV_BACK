package kr.co.hkcloud.palette3.setting.cuttType.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.cuttType.app.CuttTypeService;
import kr.co.hkcloud.palette3.setting.cuttType.util.CuttTypeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Description : 상담유형 설정 관리 REST 컨트롤러
 * package  : kr.co.hkcloud.palette3.setting.cuttType.api
 * filename : CuttTypeRestController.java
 * Date : 2023. 5. 22.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 5. 22., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "CuttTypeRestController", description = "상담유형 설정 관리 REST 컨트롤러")
public class CuttTypeRestController {

    private final PaletteJsonUtils paletteJsonUtils;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final CuttTypeService cuttTypeService;
    private final CuttTypeValidator cuttTypeValidator;


    /**
     *
     * 상담유형 설정 Tree
     * @Method Name    : cuttTypeTreeList
     * @date            : 2023. 5. 22.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담유형 Tree", notes = "상담유형 Tree를 조회한다")
    @PostMapping("/api/setting/cuttType/cuttTypeTreeList")
    public Object cuttTypeTreeList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = cuttTypeService.cuttTypeTreeList(mjsonParams);

        List<Map<String, Object>> cuttTypeList = null;

        if (objRetParams.getDataObject("DATA").size() > 0) {
            String UP_CUTT_TYPE_ID = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_CUTT_TYPE_ID");
            cuttTypeList = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), UP_CUTT_TYPE_ID, "CUTT_TYPE_ID",
                "UP_CUTT_TYPE_ID", "CUTT_TYPE_NM", "SORT", mjsonParams.getString("ALL_VIEW"));
            objRetParams.setDataObject("CUTT_TYPE_TREE", paletteJsonUtils.getJsonArrayFromList(cuttTypeList));
        }

        objRetParams.setDataObject("CUTT_TYPE_SETTING", cuttTypeService.cuttTypeSettingInfo(mjsonParams));

        objRetParams.setDataObject("DSPTCH_NO_LIST", cuttTypeService.dsptchNoList(mjsonParams)); //고객사 대표번호 목록

        return objRetParams;
    }

    /**
     *
     * 상담유형-등록,수정
     * @Method Name    : cuttTypeProc
     * @date            : 2023. 5. 25.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_CUTT_TYPE_PROC", note = "상담유형 변경(등록,수정)") //Logging관련
    @ApiOperation(value = "상담유형-등록", notes = "상담유형을 등록 한다")
    @PostMapping("/api/setting/cuttType/cuttTypeProc")
    public Object cuttTypeProc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return cuttTypeService.cuttTypeProc(mjsonParams);
    }

    /**
     *
     * 상담유형 Tree 순서 변경
     * @Method Name    : cuttTypeOrderUpdate
     * @date            : 2023. 5. 25.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_CUTT_TYPE_ORD_UPDATE", note = "상담유형 순서 변경")
    @ApiOperation(value = "상담유형 Tree 순서 변경", notes = "상담유형 Tree Node 순서를 변경 한다")
    @PostMapping("/api/setting/cuttType/cuttTypeOrderUpdate")
    public Object cuttTypeOrderUpdate(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return cuttTypeService.cuttTypeOrderUpdate(mjsonParams);
    }

    /**
     *
     * 상담유형 삭제
     * @Method Name    : cuttTypeDel
     * @date            : 2023. 5. 25.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_CUTT_TYPE_DEL", note = "상담유형 삭제")
    @ApiOperation(value = "상담유형 삭제", notes = "상담유형을 삭제 한다")
    @PostMapping("/api/setting/cuttType/cuttTypeDel")
    public Object cuttTypeDel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return cuttTypeService.cuttTypeDel(mjsonParams);
    }

    /**
     *
     * 상담유형 설정 저장
     * @Method Name    : cuttTypeSettingSave
     * @date            : 2023. 5. 25.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담유형 설정 저장", notes = "상담유형 설정을 저장 한다")
    @PostMapping("/api/setting/cuttType/cuttTypeSettingSave")
    public Object cuttTypeSettingSave(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return cuttTypeService.cuttTypeSettingSave(mjsonParams);
    }

    /**
     *
     * 상담사 상담유형 북마크(즐겨찾기) 목록
     * @Method Name    : cuslCuttTypeBmkList
     * @date            : 2023. 11. 6.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담사 상담유형 북마크(즐겨찾기) 목록", notes = "상담사 상담유형 북마크(즐겨찾기) 목록을 조회 한다")
    @PostMapping("/api/setting/cuttType/cuslCuttTypeBmkList")
    public Object cuslCuttTypeBmkList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return cuttTypeService.cuslCuttTypeBmkList(mjsonParams);
    }

    /**
     *
     * 상담사 상담유형 북마크(즐겨찾기) 등록, 삭제
     * @Method Name    : cuslCuttTypeBmkProc
     * @date            : 2023. 11. 7.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담사 상담유형 북마크(즐겨찾기) 등록, 삭제", notes = "상담사 상담유형 북마크(즐겨찾기) 등록 또는 삭제를 처리 한다")
    @PostMapping("/api/setting/cuttType/cuslCuttTypeBmkProc")
    public Object cuslCuttTypeBmkProc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return cuttTypeService.cuslCuttTypeBmkProc(mjsonParams);
    }
}
