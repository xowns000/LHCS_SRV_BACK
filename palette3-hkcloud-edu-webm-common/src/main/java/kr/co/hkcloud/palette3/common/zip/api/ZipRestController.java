package kr.co.hkcloud.palette3.common.zip.api;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Description : 우편번호 API 컨트롤러
 * package  : kr.co.hkcloud.palette3.setting.system.api
 * filename : SettingSystemCommonCodeManageRestController.java
 * Date : 2023. 6. 12.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 12., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "ZipRestController",
     description = "우편번호 API 컨트롤러")
public class ZipRestController
{
	@Autowired
    Environment environment;
    
	/**
	 * 
	 * 우편번호 검색 결
	 * @Method Name  	: getAddrApi
	 * @date   			: 2023. 6. 12.
	 * @author   		: ryucease
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param req
	 * @param model
	 * @param response
	 * @return
	 * @throws Exception
	 */
    @ApiOperation(value = "우편번호 검색 결과",
                  notes = "우편번호 검색 결과 목록을 조회한다")
    @PostMapping("/api/common/searchZip")
    public Object getAddrApi(@TelewebJsonParam TelewebJSON mjsonParams) throws Exception {
		// 요청변수 설정
    	String currentPage = mjsonParams.getString("currentPage");    //요청 변수 설정 (현재 페이지. currentPage : n > 0)
		String countPerPage = mjsonParams.getString("countPerPage");  //요청 변수 설정 (페이지당 출력 개수. countPerPage 범위 : 0 < n <= 100)
		String resultType = mjsonParams.getString("resultType");      //요청 변수 설정 (검색결과형식 설정, json)
//		String confmKey = mjsonParams.getString("confmKey");          //요청 변수 설정 (승인키)
		String confmKey = environment.getProperty("palette.zip.confmKey");          //요청 변수 설정 (승인키)
		String keyword = mjsonParams.getString("keyword");            //요청 변수 설정 (키워드)
		// OPEN API 호출 URL 정보 설정
		String apiUrl = "https://business.juso.go.kr/addrlink/addrLinkApi.do?currentPage="+currentPage+"&countPerPage="+countPerPage+"&keyword="+URLEncoder.encode(keyword,"UTF-8")+"&confmKey="+confmKey+"&resultType="+resultType;
		URL url = new URL(apiUrl);
    	BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
    	StringBuffer sb = new StringBuffer();
    	String tempStr = null;

    	while(true){
    		tempStr = br.readLine();
    		if(tempStr == null) break;
    		sb.append(tempStr);								// 응답결과 JSON 저장
    	}
    	br.close();
    	
		//반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        
        objRetParams.setString("POST", sb.toString());

        //최종결과값 반환
        return objRetParams;
    }

}
