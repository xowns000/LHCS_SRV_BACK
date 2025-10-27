package kr.co.hkcloud.palette3.vst.api;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.vst.app.VstService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RestController("VstRestController")
@Api(value = "VstRestController", description = "방문(기사) 예약 컨트롤러")
public class VstRestController {

	private final VstService vstService;
	private final PaletteJsonUtils paletteJsonUtils;
	
	@Autowired
    private HcTeletalkEnvironment env;
	
	/**
	 * 
	 * AS 제품 유형 목록
	 * @param jsonParam
	 * @return
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "AS 제품 유형 목록")
	@PostMapping("/api/vst/prdctTypeList")
	public Object prdctTypeList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
		TelewebJSON objRetParams = new TelewebJSON(jsonParam); // 반환 파라메터 생성
        objRetParams = vstService.prdctTypeList(jsonParam);

        List<Map<String, Object>> prdctTypeList = null;

        if (objRetParams.getDataObject("DATA").size() > 0) {
            String UP_PRDCT_TYPE_ID = ((JSONObject) objRetParams.getDataObject("DATA").get(0)).getString("UP_PRDCT_TYPE_ID");
            prdctTypeList = paletteJsonUtils.convertorTreeMap(objRetParams.getDataObject("DATA"), UP_PRDCT_TYPE_ID, "PRDCT_TYPE_ID",
                "UP_PRDCT_TYPE_ID", "PRDCT_NM", "SORT");
            objRetParams.setDataObject("PRDCT_TYPE_TREE", paletteJsonUtils.getJsonArrayFromList(prdctTypeList));
        }

        return objRetParams;
	}
	
	/**
	 * 
	 * 일자별 시간 목록
	 * @param jsonParam
	 * @return
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "일자별 시간 목록")
    @PostMapping("/api/vst/vstDayTimeList")
    public Object vstDayTimeList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return vstService.vstDayTimeList(jsonParam);
    }

	/**
	 * 
	 * 일자 시간별 방문 가능자 목록
	 * @param jsonParam
	 * @return
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "일자 시간별 방문 가능자 목록 조회")
	@PostMapping("/api/vst/vstDayTimeVstrList")
	public Object vstDayTimeVstrList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
		String TIME_LIST = jsonParam.getString("TIME_LIST").toString();
		TIME_LIST = TIME_LIST.replace("&#91;", "[").replace("&#93;", "]");
		jsonParam.setString("TIME_LIST", TIME_LIST);
        
		return vstService.vstDayTimeVstrList(jsonParam);
	}

	/**
	 * 
	 * 선택된 일자 시간의 방문자 상태 체크
	 * @param jsonParam
	 * @return
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "선택된 일자 시간의 방문자 상태 체크")
	@PostMapping("/api/vst/vstVstrRdyStatChk")
	public Object vstVstrRdyStatChk(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
		TelewebJSON objRetParams = new TelewebJSON(jsonParam);
		objRetParams = vstService.vstVstrRdyStatChk(jsonParam);
		return objRetParams;
	}
	
	/**
	 * 
	 * 방문자 대기 처리
	 * @param jsonParam
	 * @return
	 * @throws TelewebApiException
	 */
	@ApiOperation(value = "방문자 대기 처리")
	@PostMapping("/api/vst/vstVstrRdyProc")
	public Object vstVstrRdyProc(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
		TelewebJSON objRetParams = new TelewebJSON();
		jsonParam.setString("CUSL_ID", jsonParam.getString("USER_ID"));
		objRetParams = vstService.vstVstrRdyProc(jsonParam);
		
		return objRetParams;
	}
	
	/**
	 * 
	 * 방문자 등록 수정 처리
	 * @param jsonParam
	 * @return
	 * @throws TelewebApiException
	 * @throws ParseException 
	 * @throws TelewebAppException 
	 */
	@ApiOperation(value = "방문자 등록 수정 처리")
	@PostMapping("/api/vst/vstRsvtProc")
	public Object vstRsvtProc(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException, TelewebAppException, ParseException {
		TelewebJSON objRetParams = new TelewebJSON();
		
		objRetParams = vstService.vstRsvtProc(jsonParam);
		
		return objRetParams;
	}
	
	/**
     * 
     * 방문 예약 상담 이력
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "PALETTE3 캠페인 목록", notes = "PALETTE3 캠페인 목록")
    @PostMapping("/api/vst/vstRsvtList")
    public Object vstRsvtList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
    	//반환 파라메터 생성
    	TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
    	
    	objRetParams = vstService.vstRsvtList(mjsonParams); //방문 상담 예약 목록
    	
    	String sRootPath = env.getString("file.repository.root-dir","") + "/videos/";
    	File folder = new File(sRootPath);
    	
    	if(objRetParams.getDataObject().size() > 0) {
    		for(int i=0; i<objRetParams.getDataObject().size(); i++) {
    			JSONObject obj = (JSONObject) objRetParams.getDataObject().get(i);
    			
    			folder = new File(sRootPath + obj.getString("VIDEO_FILE_PATH") + "/recordings");
    			
    			if (folder.exists() && folder.isDirectory()) {
    				File[] fileList = folder.listFiles();
    				
    				List<String> fileNameList = new ArrayList<>();
    	            if (fileList != null) {
    	                for (File file : fileList) {
    	                    fileNameList.add(file.getName());
    	                }
    	            }
    	            
    	            obj.put("REC_FILE", fileNameList);
    			}

    			folder = new File(sRootPath + obj.getString("VIDEO_FILE_PATH") + "/captures");
    			
    			if (folder.exists() && folder.isDirectory()) {
    				File[] fileList = folder.listFiles();
    				
    				List<String> fileNameList = new ArrayList<>();
    				if (fileList != null) {
    					for (File file : fileList) {
    						fileNameList.add(file.getName());
    					}
    				}
    				
    				obj.put("CAPTURE_FILE", fileNameList);
    			}
    		}
    	}
    	
    	//최종결과값 반환
    	return objRetParams;
    }
	
	@ApiOperation(value = "배정 및 처리이력 목록 조회")
	@PostMapping("/api/vst/vstRsvtHistList")
	public Object vstRsvtHistList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {        
		return vstService.vstRsvtHistList(jsonParam);
	}
}