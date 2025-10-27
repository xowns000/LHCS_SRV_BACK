package kr.co.hkcloud.palette3.svy.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.svy.app.SvyMakeItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SvyMakeItemsRestController", description = "설문조사 항목생성 컨트롤러")
public class SvyMakeItemsRestController {

    private final SvyMakeItemsService svyMakeItemsService;

    @ApiOperation(value = "설문조사 항목생성(계획리스트콤보)", notes = "설문조사 계획리스트를 조회한다.")
    @PostMapping("/api/svy/makeitems/selectcombomakeitems")
    public Object selectComboMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.selectComboMakeItems(jsonParam);
    }

    @ApiOperation(value = "설문그룹 조회", notes = "설문그룹을 조회한다.")
    @PostMapping("/api/svy/makeitems/selectgrplistmakeitems")
    public Object selectGrpListMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.selectGrpListMakeItems(jsonParam);
    }

    @ApiOperation(value = "설문보기내용 조회", notes = "설문보기내용을 조회한다.")
    @PostMapping("/api/svy/makeitems/selectchcmakeitems")
    public Object selectChcMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.selectChcMakeItems(jsonParam);
    }

    @ApiOperation(value = "설문참여자 조회", notes = "설문참여자를 조회한다.")
    @PostMapping("/api/svy/makeitems/selecttrgtlist")
    public Object selectTrgtList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.selectTrgtList(jsonParam);
    }

    @ApiOperation(value = "웅답설정 조회", notes = "웅답설정을 조회한다.")
    @PostMapping("/api/svy/makeitems/selectsettinglist")
    public Object selectSettingList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.selectSettingList(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-HEADER_PROC", note = "설문조사 설문그룹 헤더 변경(등록,수정)")
    @ApiOperation(value = "설문그룹 헤더 저장", notes = "설문그룹헤더를 저장 한다.")
    @PostMapping("/api/svy/makeitems/updateheadermakeitems")
    public Object updateHeaderMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.updateHeaderMakeItems(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-BLOCK_PROC", note = "설문조사 설문그룹 블록 변경(등록,수정)")
    @ApiOperation(value = "설문그룹 블록 저장", notes = "설문그룹블록을 저장 한다.")
    @PostMapping("/api/svy/makeitems/upsertblockmakeitems")
    public Object upsertBlockMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.upsertBlockMakeItems(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-BLOCK_DEL", note = "설문조사 설문그룹 블록 삭제")
    @ApiOperation(value = "설문그룹 블록 삭제", notes = "설문그룹블록을 삭제 한다.")
    @PostMapping("/api/svy/makeitems/deleteblockmakeitems")
    public Object deleteBlockMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.deleteBlockMakeItems(jsonParam);
    }
    
    
    @ApiOperation(value = "설문_문항_그룹 - 다음 블록 정보 저장", notes = "설문_문항_그룹 - 다음 블록 정보를 저장 한다.")
    @PostMapping("/api/svy/makeitems/udpateGroupMvmnSrvyQitemGroup")
    public Object udpateGroupMvmnSrvyQitemGroup(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.udpateGroupMvmnSrvyQitemGroup(jsonParam);
    }
    
    
    @ApiOperation(value = "설문_그룹 - 순서 변경 저장", notes = "설문_문항_그룹 - 순서 변경 저장.")
    @PostMapping("/api/svy/makeitems/updateBlockSortOrd")
    public Object updateBlockSortOrd(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.updateBlockSortOrd(jsonParam);
    }
    

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-PROC", note = "설문조사 설문아이템 변경(등록,수정)")
    @ApiOperation(value = "설문아이템 저장", notes = "설문아이템을 저장 한다.")
    @PostMapping("/api/svy/makeitems/upsertitemsmakeitems")
    public Object upsertItemsMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.upsertItemsMakeItems(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-DEL", note = "설문조사 설문아이템 삭제")
    @ApiOperation(value = "설문질문 삭제", notes = "설문질문을 삭제 한다.")
    @PostMapping("/api/svy/makeitems/deleteitemmakeitems")
    public Object deleteItemMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.deleteItemMakeItems(jsonParam);
    }
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-COPY", note = "설문지 복제")
    @ApiOperation(value = "설문지 복제", notes = "설문지를 복제한다.")
    @PostMapping("/api/svy/makeitems/copySrvy")
    public Object copySvy(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.copySrvy(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-CHC_DEL", note = "설문조사 설문질문항목 삭제")
    @ApiOperation(value = "설문질문항목 삭제", notes = "설문질문항목 삭제 한다.")
    @PostMapping("/api/svy/makeitems/deleteItemChcMakeItems")
    public Object deleteItemChcMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.deleteItemChcMakeItems(jsonParam);
    }

    @ApiOperation(value = "설문참여자 엑셀 업로드", notes = "설문참여자 엑셀 업로드 한다.")
    @PostMapping("/api/svy/makeitems/uploadexcelmakeitems")
    public Object uploadExcelMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.uploadExcelMakeItems(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-SETTING-PLAN_PROC", note = "설문조사 설문계획의 설정 변경(등록,수정)")
    @ApiOperation(value = "설문계획 설정 저장", notes = "설문계획의 설정을 저장 한다.")
    @PostMapping("/api/svy/makeitems/updatesettingplan")
    public Object updateSettingPlan(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.updateSettingPlan(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-STTS_UPDATE", note = "설문 상태 변경")
    @ApiOperation(value = "설문 상태를 변경한다.", notes = "설문 상태를 변경한다.")
    @PostMapping("/api/svy/makeitems/updatesrvyopen")
    public Object updateSrvyOpen(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.updateSrvyOpen(jsonParam);
    }
    
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-APPRMNG-STTS_UPDATE", note = "설문 승인 관리 상태 변경")
    @ApiOperation(value = "설문 승인 관리 상태 변경", notes = "설문 승인 관리 상태를 변경한다.")
    @PostMapping("/api/svy/apprmng/updateStts")
    public Object updateStts(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.updateSrvyOpen(jsonParam);
    }
    

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM_ORD_UPDATE", note = "설문조사 설문아이템 순서변경")
    @ApiOperation(value = "설문아이템 순서변경", notes = "설문아이템의 순서를 변경한다.")
    @PostMapping("/api/svy/makeitems/moveitemmakeitems")
    public Object moveItemMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.moveItemMakeItems(jsonParam);
    }

    @ApiOperation(value = "암호화테스트(지워야함.)", notes = "암호화테스트(지워야함.)")
    @PostMapping("/api/svy/makeitems/aesurlencrypt")
    public Object aesUrlEncrypt(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.aesUrlEncrypt(jsonParam);
    }

    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-HEADER-IMG_PROC", note = "설문조사 헤더 이미지 변경(등록,수정)")
    @ApiOperation(value = "헤더 이미지 저장", notes = "헤더에 이미지를 저장 한다.")
    @PostMapping("/api/svy/makeitems/updateimgmakeitems")
    public Object updateImgMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.updateImgMakeItems(jsonParam);
    }
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-HEADER-IMG_PROC", note = "설문조사 헤더 이미지 삭제")
    @ApiOperation(value = "헤더 이미지 삭제", notes = "헤더에 이미지를 삭제 한다.")
    @PostMapping("/api/svy/makeitems/deleteheaderimgmakeitems")
    public Object deleteheaderimgmakeitems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.deleteHeaderImgMakeItems(jsonParam);
    }


    @ApiOperation(value = "설문참여자 개별 업로드", notes = "설문참여자 개별 업로드 한다.")
    @PostMapping("/api/svy/makeitems/uploadSinglemakeitems")
    public Object uploadSingleMakeItems(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.uploadSingleMakeItems(jsonParam);
    }


    @SystemEventLogAspectAnotation(value = "COM_SVY-MKITM-TRGT_DEL", note = "설문조사 설문참여자 삭제")
    @ApiOperation(value = "설문참여자 삭제", notes = "설문참여자를 삭제 한다.")
    @PostMapping("/api/svy/delTrgt")
    public Object delTrgt(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.delTrgt(jsonParam);
    }
    
    
    @ApiOperation(value = "설문참여자 확장 항목 조회", notes = "설문참여자 확장 항목을 조회한다.")
    @PostMapping("/api/svy/makeitems/selectSrvyExpsnAttrList")
    public Object selectSrvyExpsnAttrList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.selectSrvyExpsnAttrList(jsonParam);
    }
    
    
    @ApiOperation(value = "설문참여자 확장 항목 조회", notes = "설문참여자 확장 항목을 조회한다.")
    @PostMapping("/api/svy/makeitems/selectPossibleCopyYn")
    public Object selectPossibleCopyYn(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.selectPossibleCopyYn(jsonParam);
    }
    
    
    @ApiOperation(value = "설문지 생성관리 설문 항목 삭제", notes = "설문지 생성관리 설문 항목을 삭제한다.")
    @PostMapping("/api/svy/makeitems/deleteForceSrvyItem")
    public Object deleteForceSrvyItem(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.deleteForceSrvyItem(jsonParam);
    }
}
