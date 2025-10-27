package kr.co.hkcloud.palette3.signup.app;


import javax.servlet.http.HttpServletRequest;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.signup.domain.TtalkInitChannelEvent;


/*
 * ASP고객채널 서비스
 */
public interface TalkSignUpService
{
    TelewebJSON chkDupleUserId(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON chkDupleEmail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON sendMailSignUp(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON sendEmailRequest(TelewebJSON jsonParams, HttpServletRequest hsr) throws TelewebAppException;
    TelewebJSON insertRtnSignUp(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnTwbBas01(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processSignUpUserNmId(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectFindPwd(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectFindUserId(TelewebJSON jsonParams, HttpServletRequest hsr) throws TelewebAppException;

    TelewebJSON selectSignUpTrial(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectSignUpServiceKeep(TelewebJSON jsonParams) throws TelewebAppException;

//    public void initTtalkChannel(TelewebJSON jsonParams) throws TelewebAppException;
    void initTtalkChannel(final TtalkInitChannelEvent ttalkInitChannelEvent) throws TelewebAppException;
}
