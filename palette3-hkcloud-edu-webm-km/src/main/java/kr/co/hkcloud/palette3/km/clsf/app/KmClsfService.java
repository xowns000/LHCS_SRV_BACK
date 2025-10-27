package kr.co.hkcloud.palette3.km.clsf.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * Description : 지식분류관리 package : kr.co.hkcloud.palette3.km.clsf.app filename : KmClsfService.java Date : 2023. 6. 20. History : - 작성자 : yabong, 날짜 : 2023. 6. 20., 설명 : 최초작성<br>
 *
 * @author  yabong
 * @version 1.0
 */
public interface KmClsfService
{
    /**
     * 지식분류 트리 조회
     * 
     * @Method                      Name : clsfTreeList
     * @date                        : 2023. 6. 20.
     * @author                      : yabong
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    TelewebJSON clsfTreeList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 지식분류 상세 조회
     * 
     * @Method                      Name : clsfInfo
     * @date                        : 2023. 6. 28.
     * @author                      : yabong
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    TelewebJSON clsfInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 지식분류 저장 수정
     * 
     * @Method                      Name : clsfProc
     * @date                        : 2023. 6. 20.
     * @author                      : yabong
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    TelewebJSON clsfProc(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 지식분류 순번 수정
     * 
     * @Method                      Name : clsfOrderUpdate
     * @date                        : 2023. 6. 20.
     * @author                      : yabong
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    TelewebJSON clsfOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 지식분류 삭제(UPDATE)
     * 
     * @Method                      Name : clsfDel
     * @date                        : 2023. 6. 20.
     * @author                      : yabong
     * @version                     : 1.0 ----------------------------------------
     * @param   mjsonParams
     * @return
     * @throws  TelewebAppException
     */
    TelewebJSON clsfDel(TelewebJSON mjsonParams) throws TelewebAppException;

}
