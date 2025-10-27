package kr.co.hkcloud.palette3.phone.script.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("phoneScriptChangeManageService")
public class PhoneScriptChangeManageServiceImpl implements PhoneScriptChangeManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 스크립트변경이력 조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectScrtChngList(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptChangeManageMapper", "selectScrtChngList", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 스크립트변경이력 상세조회
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectScrtChngDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        objRetParams = mobjDao
            .select("kr.co.hkcloud.palette3.phone.script.dao.PhoneScriptChangeManageMapper", "selectScrtChngDetail", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
