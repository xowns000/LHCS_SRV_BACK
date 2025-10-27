package kr.co.hkcloud.palette3.setting.customer.dao;


import java.util.HashMap;
import java.util.List;

import kr.co.hkcloud.palette3.config.datasources.datasource.palette.PaletteConnMapper;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import kr.co.hkcloud.palette3.setting.customer.dto.CustomerVO;


@PaletteConnMapper
public interface SettingCustomerInformationListMapper
{
    void mergeCustomerBaseInfo(CustomerVO customerVO) throws TelewebDaoException;

    void mergeCustomerBaseInfoBySony(CustomerVO customerVOBySony) throws TelewebDaoException;

    void mergeCustomerBaseInfoBySsg(CustomerVO customerVOBySsg) throws TelewebDaoException;

    void insertCustomerBaseInfo(CustomerVO customerVO) throws TelewebDaoException;
    
    void custExpsnAttrForceRegVO(CustomerVO customerVO) throws TelewebDaoException;

    void insertChatCustomer(CustomerVO customerVO) throws TelewebDaoException;
    
    void updateCustIdSeq(CustomerVO customerVO) throws TelewebDaoException;
    
    void insertCustomerInfoChg(CustomerVO customerVO) throws TelewebDaoException;
    
    void updateInfoChgIdSeq(CustomerVO customerVO) throws TelewebDaoException;
    
    void insertCustomerInfoChgDtl(CustomerVO customerVO) throws TelewebDaoException;
    
    int updateCustomerBaseInfo(CustomerVO customerVO) throws TelewebDaoException;

    void insertCustomerBaseInfoByKaom(CustomerVO customerVoByKaom) throws TelewebDaoException;

    int updateCustomerBaseInfoByKaom(CustomerVO customerVoByKaom) throws TelewebDaoException;
}
