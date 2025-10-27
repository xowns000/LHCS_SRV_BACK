package kr.co.hkcloud.palette3.infra.alimtalk.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/*
 * 서비스설정 서비스
 */
public interface InfraAlimtalkSndngService
{

    JSONObject trnsmisAlimtalk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebAppException;
}
