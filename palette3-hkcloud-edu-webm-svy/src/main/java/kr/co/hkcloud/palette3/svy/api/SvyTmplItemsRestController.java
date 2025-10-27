package kr.co.hkcloud.palette3.svy.api;


import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.SystemEventLogAspectAnotation;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.svy.app.SvyMakeItemsService;
import kr.co.hkcloud.palette3.svy.app.SvyTmplItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * 설문 분류 관리 Controller
 * @author hjh
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SvyClsfRestController",
     description = "설문분류 관리 REST 컨트롤러")
public class SvyTmplItemsRestController
{
    private final PaletteJsonUtils     paletteJsonUtils;
    private final SvyTmplItemsService        svyTmplItemsService;
    private final SvyMakeItemsService svyMakeItemsService;


    /**
     * 설문분류 Tree
     * 
     * @Method                      Name : clsfTreeList
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @ApiOperation(value = "설문분류 Tree",
                  notes = "설문분류 Tree를 조회한다")
    @PostMapping("/api/svy/tmpl/clsfTreeList")
    public Object clsfTreeList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = svyTmplItemsService.clsfTreeList(mjsonParams);
        List<Map<String, Object>> clsfList = null;

        if(objRetParams.getDataObject("DATA").size() > 0) {
            String upSrvyClsfId = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_SRVY_CLSF_ID");
            clsfList = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), upSrvyClsfId, "SRVY_CLSF_ID", "UP_SRVY_CLSF_ID", "SRVY_CLSF_NM_F", "SORT_ARR");
            objRetParams.setDataObject("SRVY_CLSF_TREE", paletteJsonUtils.getJsonArrayFromList(clsfList));
        }
        return objRetParams;
    }


    /**
     * 설문분류 상세정보를 조회한다.
     * 
     * @Method                      Name : clsfInfo
     * @version                     : 1.0 ---------------------------------------- 사용 여부 확인 할 것 ~!
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @ApiOperation(value = "설문분류 상세조회",
                  notes = "설문분류 상세정보를 조회한다")
    @PostMapping("/api/svy/tmpl/clsfInfo")
    public Object clsfInfo(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams.setDataObject("SRVY_CLSF_INFO", svyTmplItemsService.clsfInfo(mjsonParams));
        return objRetParams;
    }


    /**
     * 설문분류 등록 OR 수정
     * 
     * @Method                      Name : clsfProc
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_SVY-CLSF_PROC", note = "설문분류 변경(등록,수정)")
    @ApiOperation(value = "설문분류 등록 OR 수정",
                  notes = "설문분류를 등록, 수정 한다")
    @PostMapping("/api/svy/tmpl/clsfProc")
    public Object clsfProc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return svyTmplItemsService.clsfProc(mjsonParams);
    }

    /**
     * 설문분류 Tree 순서 변경
     * 
     * @Method                      Name : clsfDel
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_SVY-CLSF_ORD_UPDATE", note = "설문분류 순서변경")
    @ApiOperation(value = "설문분류 Tree 순서 변경",
    		notes = "설문분류 Tree Node 순서를 변경 한다")
    @PostMapping("/api/svy/tmpl/clsfOrderUpdate")
    public Object clsfOrderUpdate(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
    	return svyTmplItemsService.clsfOrderUpdate(mjsonParams);
    }


    /**
     * 설문분류 삭제
     * 
     * @Method                      Name : clsfDel
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @SystemEventLogAspectAnotation(value = "COM_SVY-CLSF_DEL", note = "설문분류 삭제")
    @ApiOperation(value = "설문분류 삭제",
                  notes = "설문분류를 삭제 한다")
    @PostMapping("/api/svy/tmpl/clsfDel")
    public Object clsfDel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return svyTmplItemsService.clsfDel(mjsonParams);
    }
    
    
    @ApiOperation(value = "설문문항 관리 - 설문문항 조회", notes = "설문문항 관리 - 설문문항을 조회한다.")
    @PostMapping("/api/svy/tmpl/selectTmplItemList")
    public Object selectTmplItemList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyTmplItemsService.selectTmplItemList(jsonParam);
    }
    
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-TMPL_PROC", note = "설문문항 변경(등록,수정)")
    @ApiOperation(value = "설문문항 관리 - 설문문항 등록 OR 수정", notes = "설문문항 관리 - 설문문항을 등록, 수정한다.")
    @PostMapping("/api/svy/tmpl/upsertTmplItemList")
    public Object upsertTmplItemList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyMakeItemsService.upsertItemsMakeItems(jsonParam);
    }
    
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-TMPL_DEL", note = "설문문항 삭제")
    @ApiOperation(value = "설문문항 관리 - 설문문항 삭제", notes = "설문문항 관리 - 설문문항을 삭제한다.")
    @PostMapping("/api/svy/tmpl/deleteTmplItemList")
    public Object deleteTmplItemList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyTmplItemsService.deleteTmplItemList(jsonParam);
    }
    
    @SystemEventLogAspectAnotation(value = "COM_SVY-TMPL_LOAD", note = "설문문항 불러오기")
    @ApiOperation(value = "설문문항 불러오기 - 미리보기 항목 조회", notes = "설문문항 불러오기 - 미리보기 항목을 조회한다.")
    @PostMapping("/api/svy/tmpl/selectPreviewQitem")
    public Object selectPreviewQitem(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyTmplItemsService.selectPreviewQitem(jsonParam);
    }
    

}
