package kr.co.hkcloud.palette3.phone.campaign.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.phone.campaign.app.PhoneCampaignAltmntMngService;
import kr.co.hkcloud.palette3.phone.campaign.app.PhoneCampaignCustMngService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneCampaignAltmntMngRestController", description = "캠페인 배분관리 컨트롤러")
public class PhoneCampaignAltmntMngRestController {

    private final PhoneCampaignAltmntMngService phoneCampaignAltmntMngService;
    private final PhoneCampaignCustMngService phoneCampaignCustMngService;

    @ApiOperation(value = "캠페인 TOP리스트", notes = "캠페인 TOP리스트를 조회한다.")
    @PostMapping("/phone-api/campaign/altmntmng/selecttopcpialtmntmng")
    public Object selectTopCpiAltmntMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return phoneCampaignAltmntMngService.selectTopCpiAltmntMng(jsonParam);
    }

    @ApiOperation(value = "상담직원 조회", notes = "등록된 상담직원을 조회한다.")
    @PostMapping("/phone-api/campaign/altmntmng/selectcuslaltmntmng")
    public Object selectCuslAltmntMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParam);
        objRetParams = phoneCampaignAltmntMngService.selectCuslAltmntMng(jsonParam);
        objRetParams.setDataObject("EXPSN_ATTR", phoneCampaignCustMngService.custExpsnInfo(jsonParam));
        return objRetParams;
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-ALTMNT_PROC", note = "캠페인 배분처리")
    @ApiOperation(value = "배분처리", notes = "대상자를 상담원에게 배분한다.")
    @PostMapping("/phone-api/campaign/altmntmng/insertcuslaltmntmng")
    public Object insertCuslAltmntMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return phoneCampaignAltmntMngService.insertCuslAltmntMng(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-ALTMNT_EXL_CHG", note = "캠페인 제외사유 변경")
    @ApiOperation(value = "제외사유 변경", notes = "제외사유를 변경한다.")
    @PostMapping("/phone-api/campaign/altmntmng/updateexlaltmntmng")
    public Object updateExlAltmntMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return phoneCampaignAltmntMngService.updateExlAltmntMng(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-ALTMNT_NOTRY_WITHDRAW", note = "캠페인 할당된 미시도건 회수")
    @ApiOperation(value = "회수", notes = "상담사에게 할당된 미시도를 회수한다.")
    @PostMapping("/phone-api/campaign/altmntmng/returncuslaltmntmng")
    public Object returnIndiCuslAltmntMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return phoneCampaignAltmntMngService.returnCuslAltmntMng(jsonParam);
    }

    @ApiOperation(value = "배분된 정보 조회", notes = "배분된 정보를 조회한다.")
    @PostMapping("/phone-api/campaign/altmntmng/selectcustaltmntmng")
    public Object selectCustAltmntMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return phoneCampaignAltmntMngService.selectCustAltmntMng(jsonParam);
    }

    @ApiOperation(value = "인수인계", notes = "미처리건수를 인수인계 한다.")
    @PostMapping("/phone-api/campaign/altmntmng/tkovercuslaltmntmng")
    public Object tkoverCuslAltmntMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return phoneCampaignAltmntMngService.tkoverCuslAltmntMng(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-PHN_CPI-ALTMNT_CUSL_CHG", note = "캠페인 상담원 변경")
    @ApiOperation(value = "상담원 변경", notes = "상담원을 변경 한다.")
    @PostMapping("/phone-api/campaign/altmntmng/chgcuslaltmntmng")
    public Object chgCuslAltmntMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return phoneCampaignAltmntMngService.chgCuslAltmntMng(jsonParam);
    }

    @ApiOperation(value = "확장속성관리-목록", notes = "확장속성관리 목록을 조회한다")
    @PostMapping("/phone-api/campaign/custmng/expsnattrlist")
    public Object expsnAttrList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignAltmntMngService.expsnAttrList(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-ALTMNT_INDI_ADD", note = "캠페인 개별 고객 추가")
    @ApiOperation(value = "개별 고객 추가", notes = "개별로 고객을 추가한다.")
    @PostMapping("/phone-api/campaign/altmntmng/addindicustaltmntmng")
    public Object addIndiCustAltmntMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebAppException {
        return phoneCampaignAltmntMngService.addIndiCustAltmntMng(jsonParam);
    }

    /**
     *
     * 캠페인 이력 목록
     * @Method Name    : cpiStatHistList
     * @date            : 2023. 7. 18.
     * @author        : ryucease
     * @version        : 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인 이력 목록", notes = "캠페인 이력 목록을 조회한다.")
    @PostMapping("/phone-api/campaign/altmntmng/cpiStatHistList")
    public Object cpiStatHistList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParam); //반환 파라메터 생성
        objRetParams = phoneCampaignAltmntMngService.cpiStatHistList(jsonParam); //상담원별 캠페인 목록

        objRetParams.setDataObject("MONITOR", phoneCampaignAltmntMngService.cuslCampaignMonitor(jsonParam)); //상담원별 캠페인 상태

        objRetParams.setDataObject("MONITOR_RS", phoneCampaignAltmntMngService.cuslCpiDtlMonitor(jsonParam)); //상담원별 처리결과별 상태

        //최종결과값 반환
        return objRetParams;
    }
}
