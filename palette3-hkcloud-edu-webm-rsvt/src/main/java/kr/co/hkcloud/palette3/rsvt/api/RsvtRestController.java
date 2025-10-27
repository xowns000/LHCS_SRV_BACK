package kr.co.hkcloud.palette3.rsvt.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.rsvt.app.RsvtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@RestController("RsvtRestController")
@Api(value = "RsvtRestController",
description = "상담예약 컨트롤러")
public class RsvtRestController {

	private final RsvtService rsvtService;
	

//	private String extUrl = "http://localhost:8445/v1/sw/";
//	private String token = "bearer FHeyMNl6jNknvgTGfT3RS5a3w1EQd1fqLVriEH3F/Jql7rNDbu5mqRyVwPbLH0NEGdsGt2eS+wAz0Dx+3iM2H5YEEpDzjEushrjU4f/B5Mp0onp0O4s2l3AA96sucDzCaQQw1qrPcSIKBrHkbSbEfUNNDwwZGobPkHiZyqQCfBAqLNRqMlBvDOEFnewI1iQJJ7NL7QPZP375Ys2QcFqUQQ==";

    @Value("${palette.external-api.sw.url}")
    private String extUrl;
    
    @Value("${palette.external-api.sw.token}")
    private String token;
    
	@ApiOperation(value = "상담예약 목록 조회",
				  notes = "상담예약 목록 조회")
	@PostMapping("/api/rsvt/getRsvtList")
	Object getRsvtList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = rsvtService.getRsvtList(mjsonParams);
		return objRetParams;
	}
	
	@ApiOperation(value = "상담예약 내용 조회",
				  notes = "상담예약 내용 조회")
	@PostMapping("/api/rsvt/getRsvtCnList")
	Object getRsvtCnList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = rsvtService.getRsvtCnList(mjsonParams);
		return objRetParams;
	}
	@ApiOperation(value = "상담예약 내용 입력",
				  notes = "상담예약 내용 입력")
	@PostMapping("/api/rsvt/regRsvtCn")
	Object regRsvtCn(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = rsvtService.regRsvtCn(mjsonParams);
		return objRetParams;
	}
	

	@ApiOperation(value = "상담예약 상세 조회",
				  notes = "상담예약 ")
	@PostMapping("/api/rsvt/getRsvtDtl")
	Object getRsvtDtl(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		String apiUrl = mjsonParams.getString("API_URL")+"?BOOKING_ID="+mjsonParams.getString("BOOKING_ID")+"&ANALYSIS_ID="+mjsonParams.getString("ANALYSIS_ID");
		objRetParams = getExtApi(apiUrl);
		
		return objRetParams;
	}
	

	@ApiOperation(value = "상담예약 배정상담원 변경",
				  notes = "상담예약 배정상담원 변경")
	@PostMapping("/api/rsvt/setCuslAltmnt")
	Object setCuslAltmnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = rsvtService.setCuslAltmnt(mjsonParams);
		
		//고객사 상담원ID 조회
		objRetParams = rsvtService.selectCustcoCuslId(mjsonParams);
		log.info("selectBookStat > " + objRetParams);
		String custcoCuslId = objRetParams.getString("CUSTCO_CUSL_ID");
		
		String bookingId = mjsonParams.getString("BOOKING_ID");
		
		String apiUrl = mjsonParams.getString("API_URL")+"?BOOKING_ID="+bookingId+"&ADMIN_LGN_ID="+custcoCuslId;
		log.info("apiUrl" + apiUrl);
		objRetParams = postExtApi(apiUrl);
		return objRetParams;
	}
	

	@ApiOperation(value = "상담결과 sw DB update",
				  notes = "상담결과 sw DB update")
	@PostMapping("/api/rsvt/updateRsvtRs")
	Object updateRsvtRs(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		//팔레트 RSVT_ID로 SW예약KEY 가져오기
		objRetParams = rsvtService.selectBookingId(mjsonParams);
		log.info("selectBookingId > " + objRetParams);
		String bookingId = objRetParams.getString("BOOKING_ID");
		
		//예약처리결과 코드 가져오기
		objRetParams = rsvtService.selectBookStat(mjsonParams);
		log.info("selectBookStat > " + objRetParams);
		String bookStat = objRetParams.getString("CD_VL");
		
		String apiUrl = mjsonParams.getString("API_URL")+"?BOOKING_ID="+bookingId+"&BOOK_STAT="+bookStat;
		log.info("apiUrl" + apiUrl);
		objRetParams = postExtApi(apiUrl);
		return objRetParams;
	}
	

	@ApiOperation(value = "sw 배분변경이력",
				  notes = "sw 배분변경이력")
	@PostMapping("/api/rsvt/getRsvtAltmntChgLog")
	Object getRsvtAltmntChgLog(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		//팔레트 RSVT_ID로 SW예약KEY 가져오기
		objRetParams = rsvtService.getRsvtAltmntChgLog(mjsonParams);
		return objRetParams;
	}
	
	
	TelewebJSON getExtApi(String param) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Authorization", token);
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		String url = extUrl + param;
		log.info("ext api url >> " + url);
		log.info("ext api headers >> " + headers);
		String response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        
        log.info("getExtApi response" + response);
        
        // JSON 문자열을 JSONObject 객체로 변환
        JSONObject jsonObject = JSONObject.fromObject(response);
        // "responseData" 키로 JSONArray 추출
        JSONArray responseData = jsonObject.getJSONArray("responseData");
        
		objRetParams.setDataObject("DATA", responseData);
        
        log.info("getExtApi objRetParams" + objRetParams);
        
		return objRetParams;
	}
	
	TelewebJSON postExtApi(String param) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Authorization", token);
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		String url = extUrl + param;
		log.info("ext api url >> " + url);
		log.info("ext api headers >> " + headers);
		String response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();
        
        log.info("getExtApi response" + response);
        
        // JSON 문자열을 JSONObject 객체로 변환
        JSONObject jsonObject = JSONObject.fromObject(response);
        // "responseData" 키로 JSONArray 추출
        JSONArray responseData = jsonObject.getJSONArray("responseData");
        
		objRetParams.setDataObject("DATA", responseData);
        
        log.info("getExtApi objRetParams" + objRetParams);
        
		return objRetParams;
	}
	

	@ApiOperation(value = "sw 상담원 리스트",
				  notes = "sw 상담원 리스트")
	@PostMapping("/api/rsvt/getRsvtCuslList")
	Object getRsvtCuslList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = rsvtService.getRsvtCuslList(mjsonParams);
		return objRetParams;
	}

	
	@ApiOperation(value = "sw 월간 상담일정",
				  notes = "sw 월간 상담일정")
	@PostMapping("/api/rsvt/monthSchedule")
	Object monthSchedule(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
	{
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = rsvtService.monthSchedule(mjsonParams);
		return objRetParams;
	}
}