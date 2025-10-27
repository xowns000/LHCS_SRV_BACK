package kr.co.hkcloud.palette3.setting.expsnAttr.api;

import kr.co.hkcloud.palette3.common.code.app.CodeCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.exception.PaletteApiException;
import kr.co.hkcloud.palette3.setting.ognz.app.OgnzService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.setting.agent.app.SettingAgentManageService;
import kr.co.hkcloud.palette3.setting.cuttType.app.CuttTypeService;
import kr.co.hkcloud.palette3.setting.expsnAttr.app.ExpsnAttrService;
import kr.co.hkcloud.palette3.setting.expsnAttr.util.ExpsnAttrValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * Description : 확장속성 관리 REST 컨트롤러
 * package : kr.co.hkcloud.palette3.setting.expsnAttr.api
 * filename : ExpsnAttrRestController.java
 * Date : 2023. 5. 15.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 5. 15., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ExpsnAttrRestController", description = "확장속성 관리 REST 컨트롤러")
public class ExpsnAttrRestController {

    private final InnbCreatCmmnService innbCreatCmmnService;
    private final ExpsnAttrService expsnAttrService;
    private final ExpsnAttrValidator expsnAttrValidator;
    private final CuttTypeService cuttTypeService;
    private final SettingAgentManageService settingAgentManageService;
    private final OgnzService ognzService;
    private final CodeCmmnService codeCmmnService;
    private final PaletteCmmnService paletteCmmnService;

    /**
     *
     * 확장속성관리-목록
     * 
     * @Method Name : expsnAttrList
     * @date : 2025. 01. 21
     * @author : ldu
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams 요청 파라미터
     *                    - SE               확장항목 유형         TRNSF
     *                    - BSC_PVSN_ATTR_YN 기본 제공 속성 여부    N
     *                    - USE_YN           사용여부             Y
     *                    - AUTHRT_GROUP_ID  사용자 권한 그룹 ID   7(담당자 그룹)
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "확장속성관리-목록", notes = "확장속성관리 목록을 조회한다")
    @PostMapping("/api/setting/expsnAttr/expsnAttrList")
    public Object expsnAttrList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = expsnAttrService.expsnAttrList(mjsonParams);
        objRetParams.setDataObject("CUTT_TYPE_SETTING", cuttTypeService.cuttTypeSettingInfo(mjsonParams));

        // 이관여부 on인 경우
        if("TRNSF".equals(mjsonParams.getString("SE"))) {
            TelewebJSON trnsfTrgtOptInfo = paletteCmmnService.getTrnsfTargetOptCd(mjsonParams);
            String targetOptCd = trnsfTrgtOptInfo.getHeaderInt("TOT_COUNT") > 0 ? trnsfTrgtOptInfo.getString("TARGET_OPT_CD") : "";

            switch (targetOptCd) {
                case "USER": // CASE 1 : 상담사
                    mjsonParams.setString("USER_STTS_CD", "WORK");
                    objRetParams.setDataObject("TRNSF_USER_LIST", settingAgentManageService.selectRtnUserInfo(mjsonParams));
                    break;
                case "DEPT": // CASE 2 : 부서
                    mjsonParams.setString("CHILD_CNT", "0"); // 마지막 Depth 조직만 보여주기
                    objRetParams.setDataObject("TRNSF_OGNZ_LIST", ognzService.ognzTreeList(mjsonParams));
                    break;
                case "CODE": // CASE 3 : 사용자정의(코드)
                    mjsonParams.setString("GROUP_CD_ID", "TRNSF_TRGT_CD");
                    objRetParams.setDataObject("TRNSF_CODE_LIST", codeCmmnService.selectRtnCodeBook(mjsonParams));
                    break;
                default:     // CASE 4 : 예외
                    throw new PaletteApiException("Palette Api Exception :::::: 정의되지 않은 이관 담당자 옵션 코드 입니다.");
            }
        }

        return objRetParams;
    }

    /**
     *
     * 확장속성관리-등록, 수정
     * 
     * @Method Name : expsnAttrProc
     * @date : 2023. 5. 16.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_EXPSN_ATTR_PROC", note = "확장속성관리 항목 변경(등록,수정)")
    @ApiOperation(value = "확장속성관리 항목 변경(등록,수정)", notes = "확장속성관리 신규 확장속성을 등록 한다")
    @PostMapping("/api/setting/expsnAttr/expsnAttrProc")
    public Object expsnAttrProc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return expsnAttrService.expsnAttrProc(mjsonParams);
    }

    /**
     *
     * 확장속성관리-삭제
     * 
     * @Method Name : expsnAttrDel
     * @date : 2023. 5. 17.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_EXPSN_ATTR_DEL", note = "확장속성관리 항목 삭제")
    @ApiOperation(value = "확장속성관리 항목 삭제", notes = "확장속성관리 속성을 삭제 한다")
    @PostMapping("/api/setting/expsnAttr/expsnAttrDel")
    public Object expsnAttrDel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return expsnAttrService.expsnAttrDel(mjsonParams);
    }

    /**
     *
     * 확장항목 순서 재정의
     * 
     * @Method Name : expsnAttrReOrder
     * @date : 2023. 5. 30.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_EXPSN_ATTR_ORD_UPDATE", note = "확장속성관리 항목 순서 변경")
    @ApiOperation(value = "확장속성관리 항목 순서 변경", notes = "확장항목의 순서를 재 정의 한다.")
    @PostMapping("/api/setting/expsnAttr/expsnAttrReOrder")
    public Object expsnAttrReOrder(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        return expsnAttrService.expsnAttrReOrder(mjsonParams);
    }

    /**
     *
     * 확장속성관리-항목ID 중복 체크
     * 
     * @Method Name : expsnAttrColIdDupleChk
     * @date : 2023. 11. 1.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "확장속성관리-항목ID 중복 체크", notes = "확장속성 항목ID를 중복 체크 한다")
    @PostMapping("/api/setting/expsnAttr/expsnAttrColIdDupleChk")
    public Object expsnAttrColIdDupleChk(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setBoolean("expsnAttrColIdBoolean", 0, expsnAttrService.expsnAttrColIdDupleChk(mjsonParams));

        return objRetParams;
    }
}
