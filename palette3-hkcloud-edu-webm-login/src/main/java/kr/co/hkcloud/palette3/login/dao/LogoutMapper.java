package kr.co.hkcloud.palette3.login.dao;


import kr.co.hkcloud.palette3.config.datasources.datasource.palette.PaletteConnMapper;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import kr.co.hkcloud.palette3.login.domain.LoginLogDTO;


/**
 * @author Orange
 *
 */
@PaletteConnMapper
public interface LogoutMapper
{
    int updateLogoutSuccessInfo(LoginLogDTO loginLogDTO) throws TelewebDaoException;
}
