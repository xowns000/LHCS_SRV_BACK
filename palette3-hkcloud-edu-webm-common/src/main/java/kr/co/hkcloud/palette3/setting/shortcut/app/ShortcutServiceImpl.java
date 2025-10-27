package kr.co.hkcloud.palette3.setting.shortcut.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("shortcutService")
public class ShortcutServiceImpl implements ShortcutService {
    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final String namespace = "kr.co.hkcloud.palette3.setting.shortcut.dao.ShortcutMapper";
    
    
    /**
     * 사용자 단축키 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectShortcutList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectShortcutList", mjsonParams);
    }
    
    
    /**
     * 단축키 키 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectShortcutKeyList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectShortcutKeyList", mjsonParams);
    }
    
    
    /**
     * 단축키 merge(저장/수정/삭제)
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON mergeShortcut(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON dupRetParams = selectDupCheck(mjsonParams);
        if(dupRetParams.getHeaderInt("TOT_COUNT") > 0) {
            mjsonParams.setInt("SHORT_KEY_ID", dupRetParams.getInt("SHORT_KEY_ID"));
            if(!"NO_USE".equals(mjsonParams.getString("KEY_CD"))) {
                objRetParams = mobjDao.update(namespace, "updateShortcut", mjsonParams);
            } else {
                objRetParams = mobjDao.delete(namespace, "deleteShortcutOne", mjsonParams);
            }
            
            //두 건이 검색되면 1번 인덱스(dup_lnk_yn='N') 단축키를 삭제한다.(lnk_id, key_cd 중복 저장 방지)
            if(dupRetParams.getHeaderInt("TOT_COUNT") == 2) {
                mjsonParams.setInt("SHORT_KEY_ID", dupRetParams.getInt("SHORT_KEY_ID", 1));
                objRetParams = mobjDao.delete(namespace, "deleteShortcutOne", mjsonParams);
            }
        } else {
            if(!"NO_USE".equals(mjsonParams.getString("KEY_CD"))) {
                mjsonParams.setInt("SHORT_KEY_ID", innbCreatCmmnService.createSeqNo("SHORT_KEY_ID"));
                objRetParams = mobjDao.insert(namespace, "insertShortcut", mjsonParams);
            }
        }
        return objRetParams;
    }
    
    public TelewebJSON deleteShortcut(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.delete(namespace, "deleteShortcut", mjsonParams);
    }
    
    /**
     * 중복 체크
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    private TelewebJSON selectDupCheck(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectDupCheck", mjsonParams);
    }
}
