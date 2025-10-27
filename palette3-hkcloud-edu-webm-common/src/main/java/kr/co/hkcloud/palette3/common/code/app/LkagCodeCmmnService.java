package kr.co.hkcloud.palette3.common.code.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 연동 코드공통 서비스 인터페이스
 *
 * @author Orange
 *
 */
public interface LkagCodeCmmnService {

    TelewebJSON selectRtnCodeBook(TelewebJSON jsonParams) throws TelewebAppException;
}
