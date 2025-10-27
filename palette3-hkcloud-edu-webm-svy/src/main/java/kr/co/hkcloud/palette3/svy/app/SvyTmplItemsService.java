package kr.co.hkcloud.palette3.svy.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author hjh
 *
 */
public interface SvyTmplItemsService
{
    /**
     * 설문분류 트리 조회
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    public TelewebJSON clsfTreeList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 설문분류 상세 조회
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    public TelewebJSON clsfInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 설문분류 저장 수정
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    public TelewebJSON clsfProc(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 설문분류 순번 수정
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    public TelewebJSON clsfOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 설문분류 삭제(UPDATE)
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    public TelewebJSON clsfDel(TelewebJSON mjsonParams) throws TelewebAppException;
    
    public TelewebJSON selectTmplItemList(TelewebJSON mjsonParams) throws TelewebAppException;
    
    public TelewebJSON deleteTmplItemList(TelewebJSON mjsonParams) throws TelewebAppException;
    public TelewebJSON deleteTmplItem(TelewebJSON mjsonParams) throws TelewebAppException;
    public TelewebJSON selectPreviewQitem(TelewebJSON mjsonParams) throws TelewebAppException;
}
