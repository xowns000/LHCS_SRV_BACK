package kr.co.hkcloud.palette3.external.app;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ExternalService {

//    TelewebJSON sampleSelect (TelewebJSON jsonParams) throws TelewebAppException;
//    TelewebJSON sampleInsert (TelewebJSON jsonParams) throws TelewebAppException;
//    TelewebJSON sampleUpdate (TelewebJSON jsonParams) throws TelewebAppException;
    
    
    TelewebJSON selectRtnSystemNotice1(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnBrdSystem1(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnBrdList1(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnBrdCheck1(TelewebJSON jsonParams) throws TelewebAppException;
    
}
