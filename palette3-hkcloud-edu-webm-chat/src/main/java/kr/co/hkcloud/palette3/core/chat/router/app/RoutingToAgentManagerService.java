package kr.co.hkcloud.palette3.core.chat.router.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author Orange
 *
 */
public interface RoutingToAgentManagerService
{
    void consultationIitialization() throws TelewebAppException;
    TelewebJSON selectTalkEnv() throws TelewebAppException;
    TelewebJSON selectSystemMessage() throws TelewebAppException;
    void processInfoMsg() throws TelewebAppException;
    void processCustNoResponseMsg() throws TelewebAppException;
    void processInqryTypeNoContact() throws TelewebAppException;
    void processRoutingToAgent() throws TelewebAppException;
    void executeTeletalkRedisSettingJob() throws TelewebAppException;
    void cuttRdyReInsert() throws TelewebAppException;
    void cuttReInsert() throws TelewebAppException;
    void duplCuttDelete() throws TelewebAppException;
}
