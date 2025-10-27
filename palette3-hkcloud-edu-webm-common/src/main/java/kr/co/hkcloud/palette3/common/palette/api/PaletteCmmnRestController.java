package kr.co.hkcloud.palette3.common.palette.api;


import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.hkcloud.palette3.config.aspect.PrvcAspectAnotation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.security.authentication.app.TalkAuthenticationService;
import kr.co.hkcloud.palette3.excel.api.ExcelDownController;
import kr.co.hkcloud.palette3.excel.app.ExcelService;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PaletteCmmnRestController",
     description = "팔레트공통 REST 컨트롤러")
public class PaletteCmmnRestController
{
    private final TalkAuthenticationService talkAuthenticationService;
    private final PaletteCmmnService        paletteCmmnService;
    private final ExcelService excelService;


    /**
     * 클라이언트 정보 반환
     * 
     * @param  mjsonParams
     * @return                     서버의 IP정보
     * @throws TelewebApiException
     * @author                     MPC R&D Team
     */
    @ApiOperation(value = "클라이언트 정보 반환",
                  notes = "request 정보를 이용하여 접속한 클라이언트의 정보를 반환한다.")
    @PostMapping("/api/palette/common/clnt-info/inqry")
    public Object getClientInfo(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest mobjRequest) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String strCase = mjsonParams.getString("CASE");

        //반환정보 설정
        if("REMOTE_HOST".equals(strCase)) {
            objRetParams.setString("RET_VAL", mobjRequest.getRemoteHost());
        }
        else if("REMOTE_ADDR".equals(strCase)) {
            objRetParams.setString("RET_VAL", mobjRequest.getRemoteAddr());
        }

        return objRetParams;
    }


    /**
     * 서버의 정보를 반환
     * 
     * @param  mjsonParams
     * @return                     서버의 IP정보
     * @throws TelewebApiException
     * @author                     MPC R&D Team
     */
    @ApiOperation(value = "서버의 정보를 반환",
                  notes = "InetAddress정보를 이용하여 접속한 서버의 정보를 반환한다.")
    @PostMapping("/api/palette/common/server-info/inqry")
    public Object getServerInfo(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest mobjRequest) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String strCase = mjsonParams.getString("CASE");

        //접속된 서버의 IP 정보 검색
        InetAddress objAddress;
        try {
            objAddress = InetAddress.getLocalHost();
        }
        catch(UnknownHostException e) {
            throw new TelewebApiException(e.getLocalizedMessage(), e);
        }

        //반환정보 설정
        if("HOST_NAME".equals(strCase)) {
            objRetParams.setString("RET_VAL", objAddress.getHostName());
        }
        else if("HOST_ADDR".equals(strCase)) {
            objRetParams.setString("RET_VAL", objAddress.getHostAddress());
        }
        else if("HOST_PORT".equals(strCase)) {
            objRetParams.setString("RET_VAL", String.valueOf(mobjRequest.getServerPort()));
        }
        else if("SERVLET_PATH".equals(strCase)) {
            objRetParams.setString("RET_VAL", mobjRequest.getServletPath());
        }
        else if("REQUEST_URL".equals(strCase)) {
            objRetParams.setString("RET_VAL", mobjRequest.getRequestURL().toString());
        }

        return objRetParams;
    }


    /**
     * 세션 정보를 반환
     * 
     * @param  mjsonParams
     * @param  mobjSession
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "세션 정보를 반환",
                  notes = "로그인한 세션 정보를 반환한다.")
    @PostMapping("/api/palette/common/sesion-info/inqry")
    public Object getSessionInfo(@TelewebJsonParam TelewebJSON mjsonParams, HttpSession mobjSession) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "세션정보를 전송했습니다.");
        objRetParams.setString("SESSION", talkAuthenticationService.getSessionInfo(mobjSession));

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * ASP고객키 세션 정보를 수정
     * 
     * @param  mjsonParams
     * @param  mobjSession
     * @return             TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "ASP고객키 세션 정보를 수정",
                  notes = "ASP고객키 세션 정보를 수정한다.")
    @PostMapping("/api/palette/common/asp-cstmr-key-sesion-Info/modify")
    public Object updateAspCstmrKeySesionInfo(@TelewebJsonParam TelewebJSON mjsonParams, HttpSession mobjSession) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        mobjSession.setAttribute("ASP_CUST_INFO", mjsonParams.getString("ASP_CUST_INFO"));

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "세션정보를 변경했습니다.");

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 특정 사용자소속 ComboBox
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "특정 사용자소속 ComboBox",
                  notes = "특정 사용자소속 ComboBox 추가한다.")
    @PostMapping("/api/palette/common/attr-div/list")
    public Object selectRtnAttrDiv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        log.debug("mjsonParams ====================" + mjsonParams);
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        for(int i = 0; i < mjsonParams.getSize(); i++) {
            String objAttrCd = mjsonParams.getString("ATTR_CD", i);
            String objAttrDivCd = mjsonParams.getString("ATTR_DIV_CD", i);
            jsonParams.setString("ATTR_CD", objAttrCd);             //소속코드에 대한 키값 설정
            jsonParams.setString("ASP_NEWCUST_KEY", ((JSONObject) (mjsonParams.getDataObject().get(0))).getString("ASP_NEWCUST_KEY"));
            if(!"".equals(objAttrDivCd)) {
                jsonParams.setString("ATTR_DIV_CD", objAttrDivCd);  //특정 소속에 대한 키값 설정
            }
            //DAO검색 메서드 호출         
            objRetParams.setDataObject(objAttrCd, paletteCmmnService.selectRtnAttrDiv(jsonParams));
        }

        //반환정보 세팅
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상조회되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 사용자소속 ComboBox
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "사용자소속 ComboBox",
                  notes = "사용자소속 ComboBox 추가한다.")
    @PostMapping("/api/palette/common/get-attr-div/list")
    public Object getRtnAttrDiv(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = paletteCmmnService.selectRtnAttrDiv(mjsonParams);

        return objRetParams;
    }


    /**
     * 사용자소속 ComboBox
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "최상위 ComboBox",
                  notes = "최상위 ComboBox 추가한다.")
    @PostMapping("/api/palette/common/getCompanyNM/inqire")
    public Object getCompanyNM(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = paletteCmmnService.getCompanyNM(mjsonParams);

        return objRetParams;
    }
    
    /**
     * 
     * 전화 콜 이력 저장 처리
     * @Method Name  	: phnCallHistSave
     * @date   			: 2023. 7. 13.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화 콜 이력 저장 처리",
            notes = "전화 콜 이력을 저장 처리 한다")
	@PostMapping("/api/palette/common/phnCallHistReg")
	public Object phnCallHistReg(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
	{
    	//반환 파라메터 생성
  	  	TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
  	  	objRetParams = paletteCmmnService.phnCallHistReg(mjsonParams); //상담 내용 저장

  	  	//최종결과값 반환
  	  	return objRetParams;
	}

    /**
     * 
     * 개인정보 조회 이력 저장 처리
     * @Method Name  	: prvcInqLog
     * @date   			: 2023. 9. 7.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
    @PrvcAspectAnotation //개인정보 Logging관련
    @ApiOperation(value = "개인정보 조회 이력 저장 처리", notes = "개인정보 조회 이력을 저장 처리 한다")
    @PostMapping("/api/palette/common/prvcInqLog")
    public Object prvcInqLog(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
    {
    	//반환 파라메터 생성
    	TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
    	//최종결과값 반환
    	return objRetParams;
    }
    
    /**
     * 
     * 상담 이관 이력 목록
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "상담 이관 이력 목록", notes = "상담 이관 이력 목록을 조회 한다.")
	@PostMapping("/api/palette/common/cuttTrnsfHstryList")
	public Object cuttTrnsfHstryList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
	  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
	  
	  //고객 확장 속성 검색
      String SCH_CUST_EXPSN_ATTR = mjsonParams.getString("SCH_CUST_EXPSN_ATTR").toString();
      SCH_CUST_EXPSN_ATTR = SCH_CUST_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
      mjsonParams.setString("SCH_CUST_EXPSN_ATTR", SCH_CUST_EXPSN_ATTR);

      //이관 확장 속성 검색
      String SCH_TRNSF_EXPSN_ATTR = mjsonParams.getString("SCH_TRNSF_EXPSN_ATTR").toString();
      SCH_TRNSF_EXPSN_ATTR = SCH_TRNSF_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
      mjsonParams.setString("SCH_TRNSF_EXPSN_ATTR", SCH_TRNSF_EXPSN_ATTR);
	
	  objRetParams = paletteCmmnService.cuttTrnsfHstryList(mjsonParams);
	
	  return objRetParams;
	}
    
    @ApiOperation(value = "상담 이관 상태 변경", notes = "상담 이관 상태 변경을 한다.")
	@PostMapping("/api/palette/common/cuttTrnsfHstryReg")
	public Object cuttTrnsfHstryReg(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
	  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
	
	  objRetParams = paletteCmmnService.cuttTrnsfHstryReg(mjsonParams);
	
	  return objRetParams;
	}
    
    @ApiOperation(value = "상담 이관 저장", notes = "상담 이관을 저장 한다.")
	@PostMapping("/api/palette/common/cuttTrnsfReg")
	public Object cuttTrnsfReg(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
	  TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
	
	  objRetParams = paletteCmmnService.cuttTrnsfReg(mjsonParams);
	
	  return objRetParams;
	}
    
    @ApiOperation(value = "이관 처리현황 이관 및 고객 확장속성 정보 조회", notes = "이관 처리현황 이관 및 고객 확장속성 정보 조회한다")
    @PostMapping("/api/palette/common/cuttTrnsfHistGetExpsnAttr")
    public Object cuttTrnsfHistGetExpsnAttr(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = paletteCmmnService.cuttTrnsfHistGetExpsnAttr(mjsonParams);

        return objRetParams;
    }
    
    @PrvcAspectAnotation(value = "TRNSF_HIST_DOWN")   //개인정보 Logging관련
    @ApiOperation(value = "이관 처리현황 이력 엑셀 다운로드", notes = "이관 처리현황 엑셀 다운로드한다")
    @PostMapping("/api/palette/common/cuttTrnsfHistExcelDwnld")
    public void cuttHistExcelDwnld(HttpServletRequest request, HttpServletResponse response,
        @TelewebJsonParam TelewebJSON jsonParams) throws Exception {
        ExcelDownController bCon = new ExcelDownController(excelService);

        //다운로드 항목
        String CUSTOM_ATTR = jsonParams.getString("CUSTOM_ATTR").toString();
        CUSTOM_ATTR = CUSTOM_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("EXPSN_ATTR_LIST", CUSTOM_ATTR);

        //고객 확장 속성 검색
        String SCH_CUST_EXPSN_ATTR = jsonParams.getString("SCH_CUST_EXPSN_ATTR").toString();
        SCH_CUST_EXPSN_ATTR = SCH_CUST_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("SCH_CUST_EXPSN_ATTR", SCH_CUST_EXPSN_ATTR);

        //이관 확장 속성 검색
        String SCH_TRNSF_EXPSN_ATTR = jsonParams.getString("SCH_TRNSF_EXPSN_ATTR").toString();
        SCH_TRNSF_EXPSN_ATTR = SCH_TRNSF_EXPSN_ATTR.replace("&#91;", "[").replace("&#93;", "]");
        jsonParams.setString("SCH_TRNSF_EXPSN_ATTR", SCH_TRNSF_EXPSN_ATTR);

        bCon.excel_tmpl(request, response, jsonParams);
    }
    
    @ApiOperation(value = "(설문)부서 리스트 조회", notes = "(설문)부서 리스트 조회")
    @PostMapping("/api/palette/common/getDeptList")
    public Object getDeptList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        objRetParams = paletteCmmnService.getDeptList(mjsonParams);

        return objRetParams;
    }
}
