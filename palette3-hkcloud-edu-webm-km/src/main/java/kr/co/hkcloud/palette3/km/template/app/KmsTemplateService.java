package kr.co.hkcloud.palette3.km.template.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface KmsTemplateService
{
	TelewebJSON selectPageList(TelewebJSON jsonParams) throws TelewebAppException;
	TelewebJSON selectRtnDetail(TelewebJSON jsonParams) throws TelewebAppException;
	TelewebJSON regRtn(TelewebJSON jsonParams) throws TelewebAppException;
	TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtn(TelewebJSON jsonParams) throws TelewebAppException;
}
