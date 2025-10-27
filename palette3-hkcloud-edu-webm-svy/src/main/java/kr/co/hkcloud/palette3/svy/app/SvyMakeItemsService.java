package kr.co.hkcloud.palette3.svy.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface SvyMakeItemsService
{
    TelewebJSON selectComboMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectGrpListMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectChcMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectTrgtList(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectSettingList(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON updateHeaderMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON upsertBlockMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON deleteBlockMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON udpateGroupMvmnSrvyQitemGroup(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON updateBlockSortOrd(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON upsertItemsMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON deleteItemMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON deleteItemChcMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON copySrvy(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON uploadExcelMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON updateSettingPlan(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON updateSrvyOpen(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON moveItemMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON aesUrlEncrypt(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON updateImgMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON deleteHeaderImgMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    
    TelewebJSON uploadSingleMakeItems(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON delTrgt(TelewebJSON jsonParam) throws TelewebAppException;
    TelewebJSON selectSrvyExpsnAttrList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectPossibleCopyYn(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteForceSrvyItem(TelewebJSON jsonParams) throws TelewebAppException;
}
