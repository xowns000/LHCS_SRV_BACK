package kr.co.hkcloud.palette3.km.conts.api;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.util.PaletteJsonUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.km.conts.app.KmRelLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "KmRelLinkRestController",
     description = "지식 콘텐츠 - 관련 링크 REST 컨트롤러")
public class KmRelLinkRestController
{
    private final KmRelLinkService kmRelLinkService;

    @ApiOperation(value = "관련 링크 리스트",	  notes = "관련 링크 리스트를 조회한다")
	@PostMapping("/api/km/conts/rellink/list")
	public Object list(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
	{
    	//필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();		//반환파라메터생성
        
        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        //DAO검색 메서드 호출
        objRetParams = kmRelLinkService.selectList(jsonParams);
        
        return objRetParams;
	}
	
	@ApiOperation(value = "관련 링크 등록/수정",	  notes = "관련 링크 등록/수정 한다.")
	@PostMapping("/api/km/conts/rellink/update")
	public Object regRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
	{
		//필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();		//반환파라메터생성
        
        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        
        String KMS_REL_LNK_ID =  jsonParams.getString("KMS_REL_LNK_ID",0,"");
        if("".equals(KMS_REL_LNK_ID)) {
        	objRetParams = kmRelLinkService.regRtn(jsonParams);
        } else {
        	objRetParams = kmRelLinkService.updateRtn(jsonParams);
        }
        
        return objRetParams;
	}
	
	@ApiOperation(value = "관련 링크 삭제",	  notes = "관련 링크 삭제한다.")
	@PostMapping("/api/km/conts/rellink/delete")
	public Object delRtn(@TelewebJsonParam TelewebJSON mjsonParams, BindingResult result) throws TelewebApiException
	{
		//필수객체정의
        TelewebJSON objRetParams = new TelewebJSON();		//반환파라메터생성
        
        //전송된 파라메터 반환
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        
        //DAO검색 메서드 호출
        objRetParams = kmRelLinkService.deleteRtn(jsonParams);
        
        return objRetParams;
	} 
    
}
