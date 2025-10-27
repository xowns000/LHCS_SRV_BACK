package kr.co.hkcloud.palette3.core.security.authentication.dao;


import kr.co.hkcloud.palette3.config.datasources.datasource.palette.PaletteConnMapper;
import kr.co.hkcloud.palette3.core.security.authentication.domain.TalkAuthenticationVO;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;


/**
 * @author Orange
 *
 */
@PaletteConnMapper
public interface TalkAuthenticationMapper
{
    int selectChkAuthMenu(TalkAuthenticationVO talkAuthenticationVO) throws TelewebDaoException;
}
