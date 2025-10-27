package kr.co.hkcloud.palette3.km.conts.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.km.conts.app.KmRelContsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "KmRelContsRestController",
     description = "지식 콘텐츠 - 관련 콘텐츠 REST 컨트롤러")
public class KmRelContsRestController
{
    private final KmRelContsService KmRelContsService;

    @ApiOperation(value = "관련 콘텐츠 리스트",	  notes = "관련 콘텐츠 리스트를 조회한다")
	@PostMapping("/api/km/conts/relconts/list")
	public Object list(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
	{
    	//필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();		//반환파라메터생성
        
        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //DAO검색 메서드 호출
        objRetParams = KmRelContsService.selectList(jsonParams);
        
        return objRetParams;
	}
	
	@ApiOperation(value = "관련 콘텐츠 등록/수정",	  notes = "관련 콘텐츠를 등록/수정 한다.")
	@PostMapping("/api/km/conts/relconts/update")
	public Object regRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
	{
		//필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();		//반환파라메터생성
        
        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        
       	objRetParams = KmRelContsService.regRtn(jsonParams);
        
        return objRetParams;
	}
	
	@ApiOperation(value = "관련 콘텐츠 삭제",	  notes = "관련 콘텐츠를 삭제한다.")
	@PostMapping("/api/km/conts/relconts/delete")
	public Object delRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
	{
		//필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();		//반환파라메터생성
        
        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        
        //DAO검색 메서드 호출
        objRetParams = KmRelContsService.deleteRtn(jsonParams);
        
        return objRetParams;
	} 

}
