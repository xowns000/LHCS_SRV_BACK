package kr.co.hkcloud.palette3.external.dao;

import kr.co.hkcloud.palette3.config.datasources.datasource.external.ExternalConnMapper;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;

import java.util.HashMap;
import java.util.List;

@ExternalConnMapper
public interface ExternalMapper {

    List<HashMap<String, Object>> selectRtnSystemNotice(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnBrdSystem(HashMap<String, Object> inHashMap) throws TelewebDaoException;

    List<HashMap<String, Object>> selectRtnBrdList(HashMap<String, Object> inHashMap) throws TelewebDaoException;
//
    List<HashMap<String, Object>> selectRtnBrdCheck(HashMap<String, Object> inHashMap) throws TelewebDaoException;

}
