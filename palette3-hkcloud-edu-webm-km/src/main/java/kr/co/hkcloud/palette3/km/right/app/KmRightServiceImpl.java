package kr.co.hkcloud.palette3.km.right.app;

import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.km.conts.app.KmContsService;
import kr.co.hkcloud.palette3.km.conts.app.KmRelContsService;
import kr.co.hkcloud.palette3.km.conts.app.KmRelLinkService;
import kr.co.hkcloud.palette3.setting.shortcut.app.ShortcutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("kmRightService")
public class KmRightServiceImpl implements KmRightService {

    //    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;
    private final KmContsService kmContsService;
    private final KmRelContsService kmRelContsService;
    private final KmRelLinkService kmRelLinkService;
    private final ShortcutService shortcutService;
    private final String namespace = "kr.co.hkcloud.palette3.km.clsf.dao.KmRightMapper";

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rightContsList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "rightContsList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rightContsTreeList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "rightContsTreeList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rightContsGuideList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "rightContsGuideList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rightContsGuideCuttList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "rightContsGuideCuttList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rightContsRelGuideList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "rightContsRelGuideList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rightContsRelLinkList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "rightContsRelLinkList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rightContsRelFileList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "rightContsRelFileList", mjsonParams);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON rightRegUserConts(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.insert(namespace, "INSERT_KMS_USER_CONTS", mjsonParams);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON rightDelUserConts(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.delete(namespace, "DELETE_KMS_USER_CONTS", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rightContsBadgeConut(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "rightContsBadgeConut", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON searchKeyword(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "rightSearchKeyword", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON rightContsInfo(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams.setDataObject("CONTENT_INFO", rightContsList(mjsonParams));           // 컨텐츠 상세 정보
        objRetParams.setDataObject("REL_CONTENT_LIST", kmRelContsService.selectList(mjsonParams));    // 관련 콘텐츠 목록
        objRetParams.setDataObject("REL_LINK_LIST", kmRelLinkService.selectList(mjsonParams));       // 관련 링크 목록
        objRetParams.setDataObject("ATTACH_FILE_LIST", rightContsRelFileList(mjsonParams));    // 첨부 파일 목록
        return objRetParams;
    }
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON mergeShortcutKmsScript(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //ALT + 단축키 저장
        mjsonParams.setString("SHORT_TYPE_CD", "KMS_SCRIPT");
        mjsonParams.setString("ALT_KEY_YN", "Y");
        mjsonParams.setString("SHIFT_KEY_YN", "N");
        shortcutService.mergeShortcut(mjsonParams);
        
        //ALT + SHIFT 단축키 저장
        mjsonParams.setString("SHIFT_KEY_YN", "Y");
        shortcutService.mergeShortcut(mjsonParams);
        return objRetParams;
    }

}
