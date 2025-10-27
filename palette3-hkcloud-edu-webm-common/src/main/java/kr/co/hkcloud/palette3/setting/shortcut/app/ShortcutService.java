package kr.co.hkcloud.palette3.setting.shortcut.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface ShortcutService {
    /**
     * 사용자 단축키 조회
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectShortcutList(TelewebJSON mjsonParams) throws TelewebAppException;
    /**
     * 단축키 키 목록 조회
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON selectShortcutKeyList(TelewebJSON mjsonParams) throws TelewebAppException;
    /**
     * 단축키 merge(저장/수정/삭제)
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON mergeShortcut(TelewebJSON mjsonParams) throws TelewebAppException;
    /**
     * 단축키 삭제
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON deleteShortcut(TelewebJSON mjsonParams) throws TelewebAppException;
}
