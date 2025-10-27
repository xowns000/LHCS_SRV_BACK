package kr.co.hkcloud.palette3.chat.setting.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatSettingInquiryTypeManageService
{
    TelewebJSON selectRtnTalkMngInqryTypeTreeViewByChannel(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnTwbTalkInqryTypTreeClear(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnInqryTypChannel(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteQstnType(TelewebJSON jsonParams) throws TelewebAppException;
    void deleteRecursiveInqryTyp(String InqryCode, int curTalkInqryLvl, String custcoId) throws TelewebAppException;
    TelewebJSON processRtnTwbTalkInqryTyp(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON processRtnTwbTalkInqryTypChild(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON selectRtnChannelNodeDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnChildrenNode(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnNodeDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnInqryCdLastSortOrd(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnInqryCd(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnDupSortOrd(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnTwbTalkInqryTyp(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectDeletingChildInqryCode(String InqryCode, int curTalkInqryLvl, String custcoId) throws TelewebAppException;
    TelewebJSON deleteInqryTyp(String InqryCode, String custcoId) throws TelewebAppException;
    TelewebJSON selectRtnDupInqryTyp(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON uploadInqryImage(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateRtnInqryTypImageFileKey(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON processRtnInqryType(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnNodeDetailMst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnChannelNodeDetailMst(TelewebJSON jsonParams) throws TelewebAppException;

    //엑셀에 매핑된 ChatSettingInquiryTypeManageRestController에서 호출하지 않는 함수
    void updateRecursiveInqryTypUseChannel(String InqryCode, int curTalkInqryLvl, String inqryUseChannel) throws TelewebAppException;
    TelewebJSON updateRtnInqryTypUseChannel(String InqryCode, String inqryUseChannel) throws TelewebAppException;
    TelewebJSON selectChildInqryCode(String InqryCode, int curTalkInqryLvl) throws TelewebAppException;

    TelewebJSON selectInfoMsg(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON infoMsgRegist(TelewebJSON jsonParams) throws TelewebAppException;
}
