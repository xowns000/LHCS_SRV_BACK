package kr.co.hkcloud.palette3.setting.system.app;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 공통코드 서비스 인터페이스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("settingSystemCommonCodeManageService")
public class SettingSystemCommonCodeManageServiceImpl implements SettingSystemCommonCodeManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 설정시스템공통코드관리 목록을 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCodeType(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCommonCodeManageMapper", "selectRtnCodeType", jsonParams);
    }


    /**
     * 설정시스템공통코드관리 공통코드 중복체크한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON dpcnCheck(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCommonCodeManageMapper", "dpcnCheck", jsonParams);
    }


    /**
     * 설정시스템공통코드관리 상세를 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCodeDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCommonCodeManageMapper", "selectRtnCodeDetail", jsonParams);
    }


    /**
     * 공통코드 등록 (캐싱 처리)
     * 
     * @version 5.0
     * @see     com.ChatCmmnService.teletalk.common.app.ProjectCommonService.selectRtnCachingCodeBook
     */
    @Override
    @CacheEvict(value = "palette:cache:code-book",
                key = "T(kr.co.hkcloud.palette3.config.cache.generates.CacheCodeBookKeyGenerate).generate(#jsonParams)")
    @Transactional(readOnly = false)
    public TelewebJSON insertTwbBas03(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCommonCodeManageMapper", "INSERT_PLT_COMN_CD", jsonParams);
    }


    /**
     * 공통코드 수정 (캐싱 처리)
     * 
     * @version 5.0
     * @see     com.ChatCmmnService.teletalk.common.app.ProjectCommonService.selectRtnCachingCodeBook
     */
    @Override
    @CacheEvict(value = "palette:cache:code-book",
                key = "T(kr.co.hkcloud.palette3.config.cache.generates.CacheCodeBookKeyGenerate).generate(#jsonParams)")
    @Transactional(readOnly = false)
    public TelewebJSON updateTwbBse03(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCommonCodeManageMapper", "UPDATE_PLT_COMN_CD", jsonParams);
    }


    /**
     * 공통코드 삭제 (캐싱 처리)
     * 
     * @version 5.0
     * @see     com.ChatCmmnService.teletalk.common.app.ProjectCommonService.selectRtnCachingCodeBook
     */
    @Override
    @CacheEvict(value = "palette:cache:code-book",
                key = "T(kr.co.hkcloud.palette3.config.cache.generates.CacheCodeBookKeyGenerate).generate(#jsonParams)")
    @Transactional(readOnly = false)
    public void deleteRtnCodeInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemCommonCodeManageMapper", "DELETE_PLT_COMN_CD", jsonParams);
    }

}
