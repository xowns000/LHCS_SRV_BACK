package kr.co.hkcloud.palette3.login.dao;


import java.util.LinkedHashMap;

import kr.co.hkcloud.palette3.config.datasources.datasource.palette.PaletteConnMapper;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import kr.co.hkcloud.palette3.login.domain.LoginLogDTO;


/**
 * @author Orange
 *
 */
@PaletteConnMapper
public interface LoginMapper
{
    int insertLog(LoginLogDTO loginLogDTO) throws TelewebDaoException;
    int updateLoginSuccessInfo(LoginLogDTO loginLogDTO) throws TelewebDaoException;
    LinkedHashMap<String, String> selectLoginSuccessSessionInfo(LoginLogDTO loginLogDTO) throws TelewebDaoException;
}
