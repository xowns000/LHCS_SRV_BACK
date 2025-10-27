package kr.co.hkcloud.palette3.phone.campaign.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.campaign.app.PhoneCampaignCustMngService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneCampaignCustMngRestController", description = "캠페인 대상자관리 컨트롤러")
public class PhoneCampaignCustMngRestController {

    private final PhoneCampaignCustMngService phoneCampaignCustMngService;

    @ApiOperation(value = "캠페인 계획리스트", notes = "캠페인 계획리스트를 조회한다.")
    @PostMapping("/phone-api/campaign/custmng/selectcombocpicustmng")
    public Object selectComboCpiCustMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignCustMngService.selectComboCpiCustMng(jsonParam);
    }

    @ApiOperation(value = "캠페인 TOP리스트", notes = "캠페인 TOP리스트를 조회한다.")
    @PostMapping("/phone-api/campaign/custmng/selecttopcpicustmng")
    public Object selectTopCpiCustMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignCustMngService.selectTopCpiCustMng(jsonParam);
    }

    @ApiOperation(value = "캠페인관리항목", notes = "캠페인관리항목을 조회한다.")
    @PostMapping("/phone-api/campaign/custmng/selectattrcustmng")
    public Object selectAttrCustMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignCustMngService.selectAttrCustMng(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-CUST_ATTR_ORD_UPDATE", note = "캠페인 관리항목 순서 재정의")
    @ApiOperation(value = "항목 순서 재정의", notes = "항목의 순서를 재 정의 한다.")
    @PostMapping("/phone-api/campaign/custmng/reorderattrcustmng")
    public Object reOrderAttrCustMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignCustMngService.reOrderAttrCustMng(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-CUST_ATTR_PROC", note = "캠페인 관리항목 변경(등록,수정)")
    @ApiOperation(value = "관리항목등록", notes = "신규 관리항목등록을 한다.")
    @PostMapping("/phone-api/campaign/custmng/upsertattrcustmng")
    public Object upsertattrcustmng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignCustMngService.upsertAttrCustMng(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "PHN_CPI-CUST_ATTR_DEL", note = "캠페인 관리항목 삭제")
    @ApiOperation(value = "관리항목삭제", notes = "관리항목을삭제 한다.")
    @PostMapping("/phone-api/campaign/custmng/deleteattrcustmng")
    public Object deleteAttrCustMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignCustMngService.deleteAttrCustMng(jsonParam);
    }

    @ApiOperation(value = "대상자조회", notes = "대상자를조회 한다.")
    @PostMapping("/phone-api/campaign/custmng/selectcustcustmng")
    public Object selectCustCustMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParam);
        objRetParams.setString("CPI_ID", jsonParam.getString("CPI_ID"));
        objRetParams.setString("CUSTCO_ID", jsonParam.getString("CUSTCO_ID"));
        objRetParams = phoneCampaignCustMngService.selectCustCustMng(jsonParam);
        objRetParams.setDataObject("EXPSN_ATTR", phoneCampaignCustMngService.custExpsnInfo(jsonParam));
        return objRetParams;
    }

    @ApiOperation(value = "대상자 업로드", notes = "대상자를 업로드 한다.")
    @PostMapping("/phone-api/campaign/custmng/uploadexcelcustmng")
    public Object uploadExcelCustMng(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return phoneCampaignCustMngService.uploadExcelCustMng(jsonParam);
    }
}
