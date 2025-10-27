package kr.co.hkcloud.palette3.km.clsf.api;


import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.km.clsf.app.KmClsfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * Description : 지식 분류 관리 REST 컨트롤러 package : kr.co.hkcloud.palette3.km.clsf.api filename : KmClsfRestController.java Date : 2023. 6. 22. History : - 작성자 : yabong, 날짜 : 2023. 6. 22., 설명 : 최초작성<br>
 *
 * @author  yabong
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "KmClsfRestController",
     description = "지식분류 관리 REST 컨트롤러")
public class KmClsfRestController
{
    private final PaletteJsonUtils     paletteJsonUtils;
//    private final InnbCreatCmmnService innbCreatCmmnService;
    private final KmClsfService        kmClsfService;


    /**
     * 지식분류 Tree
     * 
     * @Method                      Name : clsfTreeList
     * @date                        : 2023. 6. 22.
     * @author                      : yabong
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @ApiOperation(value = "지식분류 Tree",
                  notes = "지식분류 Tree를 조회한다")
    @PostMapping("/api/km/clsf/clsfTreeList")
    public Object clsfTreeList(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = kmClsfService.clsfTreeList(mjsonParams);
        List<Map<String, Object>> clsfList = null;

        if(objRetParams.getDataObject("DATA").size() > 0) {
            String UP_KMS_CLSF_ID = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_KMS_CLSF_ID");
            clsfList = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), UP_KMS_CLSF_ID, "KMS_CLSF_ID", "UP_KMS_CLSF_ID", "KMS_CLSF_NM_F", "SORT_ARR");
            objRetParams.setDataObject("KM_CLSF_TREE", paletteJsonUtils.getJsonArrayFromList(clsfList));
        }
        return objRetParams;
    }


    /**
     * 지식분류 상세정보를 조회한다.
     * 
     * @Method                      Name : clsfInfo
     * @date                        : 2023. 6. 29.
     * @author                      : yabong
     * @version                     : 1.0 ---------------------------------------- 사용 여부 확인 할 것 ~!
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @ApiOperation(value = "지식분류 상세조회",
                  notes = "지식분류 상세정보를 조회한다")
    @PostMapping("/api/km/clsf/clsfInfo")
    public Object clsfInfo(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams.setDataObject("KM_CLSF_INFO", kmClsfService.clsfInfo(mjsonParams));
        return objRetParams;
    }


    /**
     * 지식분류 등록 OR 수정
     * 
     * @Method                      Name : clsfProc
     * @date                        : 2023. 6. 29.
     * @author                      : yabong
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @ApiOperation(value = "지식분류 등록 OR 수정",
                  notes = "지식분류를 등록, 수정 한다")
    @PostMapping("/api/km/clsf/clsfProc")
    public Object clsfProc(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return kmClsfService.clsfProc(mjsonParams);
    }

    /**
     * 지식분류 Tree 순서 변경
     * 
     * @Method                      Name : clsfDel
     * @date                        : 2023. 6. 29.
     * @author                      : yabong
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @ApiOperation(value = "지식분류 Tree 순서 변경",
    		notes = "지식분류 Tree Node 순서를 변경 한다")
    @PostMapping("/api/km/clsf/clsfOrderUpdate")
    public Object clsfOrderUpdate(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
    	return kmClsfService.clsfOrderUpdate(mjsonParams);
    }


    /**
     * 지식분류 삭제
     * 
     * @Method                      Name : clsfDel
     * @date                        : 2023. 6. 29.
     * @author                      : yabong
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @param   result
     * @return
     * @throws  TelewebApiException
     */
    @ApiOperation(value = "지식분류 삭제",
                  notes = "지식분류를 삭제 한다")
    @PostMapping("/api/km/clsf/clsfDel")
    public Object clsfDel(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
        return kmClsfService.clsfDel(mjsonParams);
    }

}
