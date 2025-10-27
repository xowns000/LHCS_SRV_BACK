package kr.co.hkcloud.palette3.phone.script.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneScriptInqireService")
public class PhoneScriptInqireServiceImpl implements PhoneScriptInqireService
{
    private final TwbComDAO twbComDao;


    /**
     * 스크립트트리 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnScriptTree(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptInqireMapper", "selectRtnScriptTree", mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트트리 리스트 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnScriptList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptInqireMapper", "selectRtnScriptList", mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트 리스트 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectMainScriptList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);

        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptInqireMapper", "selectMainScriptList", mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트상세조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnScriptDetail(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptInqireMapper", "selectRtnScriptDetail", mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트하위리스트조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    public TelewebJSON selectRtnLowScript(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = twbComDao.select("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptInqireMapper", "selectRtnLowScript", mjsonParams);

        return objRetParams;
    }

}
