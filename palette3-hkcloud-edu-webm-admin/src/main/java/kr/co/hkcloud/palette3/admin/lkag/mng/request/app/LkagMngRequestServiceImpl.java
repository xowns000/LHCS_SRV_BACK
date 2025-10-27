package kr.co.hkcloud.palette3.admin.lkag.mng.request.app;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.redis.RedisCacheCustcoLkagRepository;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.mng.app
 * fileName       : LkagMngServiceImpl
 * author         : KJD
 * date           : 2024-03-25
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-25        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("lkagMngRequestService")
public class LkagMngRequestServiceImpl implements LkagMngRequestService {

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final RedisCacheCustcoLkagRepository redisCacheCustcoLkagRepository;

    @Override
    public TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mng.request.dao.LkagMngRequestMapper", "selectList", jsonParams);
    }

    @Override
    public TelewebJSON insert(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        jsonParams.setInt("PARAM_ARTCL_ID", innbCreatCmmnService.createSeqNo("PARAM_ARTCL_ID"));
        jsonParams.setInt("__KEY_ID", jsonParams.getInt("PARAM_ARTCL_ID"));
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.admin.lkag.mng.request.dao.LkagMngRequestMapper", "insert", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON update(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.request.dao.LkagMngRequestMapper", "update", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON delete(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.request.dao.LkagMngRequestMapper", "delete", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON dpcnChk(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mng.request.dao.LkagMngRequestMapper", "dpcnChk", jsonParams);
        if (objRetParams.getHeaderInt("COUNT") == 0) {
            objRetParams.setHeader("ERROR_FLAG", false);
            return objRetParams;
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 사용중입니다.");
        }
        return objRetParams;
    }

    /**
     * 순서변경.
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    public TelewebJSON orderUpdate(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        if ("UP".equals(jsonParams.getString("ORDER_TYPE"))) {
            if (Integer.parseInt(jsonParams.getString("SORT_ORD")) > 1) {
                jsonParams.setInt("ADD_NUM", 1);
                jsonParams.setInt("SORT_ORD", Integer.parseInt(jsonParams.getString("SORT_ORD")) - 1);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.request.dao.LkagMngRequestMapper", "UPDATE_OTHER_SORT_ORDER", jsonParams);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.request.dao.LkagMngRequestMapper", "UPDATE_SORT_ORDER", jsonParams);
            }
        } else if ("DOWN".equals(jsonParams.getString("ORDER_TYPE"))) {
            if (Integer.parseInt(jsonParams.getString("SORT_ORD")) < Integer.parseInt(jsonParams.getString("MAX_SORT_ORD"))) {
                jsonParams.setInt("ADD_NUM", -1);
                jsonParams.setInt("SORT_ORD", Integer.parseInt(jsonParams.getString("SORT_ORD")) + 1);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.request.dao.LkagMngRequestMapper", "UPDATE_OTHER_SORT_ORDER", jsonParams);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.request.dao.LkagMngRequestMapper", "UPDATE_SORT_ORDER", jsonParams);
            }
        }
        return objRetParams;
    }
}
