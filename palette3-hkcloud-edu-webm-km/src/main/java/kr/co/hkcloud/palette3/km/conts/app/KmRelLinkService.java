package kr.co.hkcloud.palette3.km.conts.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface KmRelLinkService
{
	TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException;

	TelewebJSON regRtn(TelewebJSON jsonParams) throws TelewebAppException;

	TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebAppException;

	TelewebJSON deleteRtn(TelewebJSON jsonParams) throws TelewebAppException;
}
