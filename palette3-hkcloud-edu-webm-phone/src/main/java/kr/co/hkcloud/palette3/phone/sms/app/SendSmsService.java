package kr.co.hkcloud.palette3.phone.sms.app;


import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface SendSmsService
{
	TelewebJSON sendSMS(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException;
	
	TelewebJSON sendAtalk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException;
	
	Map<String, String> mtsAtalkTmplRegister(MultiValueMap<String, Object> atalkTmpl) throws TelewebAppException;
	
	Map<String, String> mtsAtalkTmplRequest(MultiValueMap<String, Object> atalkTmpl) throws TelewebAppException;
	
	Map<String, String> mtsAtalkTmplCancelRequest(MultiValueMap<String, Object> atalkTmpl) throws TelewebAppException;
	
	Map<String, String> mtsCancelApproval(MultiValueMap<String, Object> atalkTmpl) throws TelewebAppException;
	
	Map<String, String> mtsStop(MultiValueMap<String, Object> atalkTmpl) throws TelewebAppException;
	
	Map<String, String> mtsReuse(MultiValueMap<String, Object> atalkTmpl) throws TelewebAppException;
	
	Map<String, String> mtsRelease(MultiValueMap<String, Object> atalkTmpl) throws TelewebAppException;
	
	Map<String, Object> mtsModify(MultiValueMap<String, Object> atalkTmpl) throws TelewebAppException;
	
	Map<String, Object> mtsAtalkTmplStatus(MultiValueMap<String, Object> atalkTmpl) throws TelewebAppException;
	
	Map<String, String> mtsAtalkFileUpload(String filepath, String senderKey, String imageType) throws TelewebAppException;
	
	TelewebJSON getSndngRslt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException;
	
	List<Map<String, String>> selectAttachedFile(String fileGroupKey) throws TelewebAppException;
}
