package kr.co.hkcloud.palette3.core.chat.router.dao;


import java.util.HashMap;
import java.util.List;

import kr.co.hkcloud.palette3.common.chat.domain.OrgContentVO;
import kr.co.hkcloud.palette3.common.chat.domain.OrgFileVO;
import kr.co.hkcloud.palette3.config.datasources.datasource.palette.PaletteConnMapper;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;


@PaletteConnMapper
public interface TalkRouteMapper
{
    void insertOrgContent(OrgContentVO orgContentVO) throws TelewebDaoException;

    void insertOrgFile(OrgFileVO orgFileVO) throws TelewebDaoException;

    OrgFileVO selectOrgFileVo(OrgFileVO orgFileVO) throws TelewebDaoException;
}
