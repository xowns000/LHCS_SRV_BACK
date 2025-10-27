package kr.co.hkcloud.palette3.qa.app;

import java.util.HashMap;
import java.util.List;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;

public interface QAQltyClsfManageService {
    TelewebJSON insertQaQltyClsf(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectQaQltyClsfTree(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteQaQltyClsf(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectQaQltyEvlArtcl(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertQaQltyEvlArtcl(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteQaQltyEvlArtcl(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectQaQltyClsfCode(TelewebJSON jsonParams) throws TelewebAppException;
	TelewebJSON selectQaQltyEvlArtclListWithClsf(TelewebJSON jsonParams) throws TelewebAppException;
	TelewebJSON selectQaPlanQltyList(TelewebJSON jsonParams) throws TelewebAppException;
	TelewebJSON reOrderQltyClsf(TelewebJSON jsonParams) throws TelewebAppException;
}
