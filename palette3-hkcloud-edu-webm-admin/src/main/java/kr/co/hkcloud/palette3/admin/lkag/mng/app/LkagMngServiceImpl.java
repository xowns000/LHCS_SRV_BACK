package kr.co.hkcloud.palette3.admin.lkag.mng.app;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.redis.RedisCacheCustcoLkagRepository;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
@Service("lkagMngService")
public class LkagMngServiceImpl implements LkagMngService{

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final RedisCacheCustcoLkagRepository redisCacheCustcoLkagRepository;

    @Override
    public TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mng.dao.LkagMngMapper", "selectList", jsonParams);
    }

    @Override
    public TelewebJSON insert(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        jsonParams.setInt("LKAG_ID", innbCreatCmmnService.createSeqNo("LKAG_ID"));
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.admin.lkag.mng.dao.LkagMngMapper", "insert", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON update(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.dao.LkagMngMapper", "update", jsonParams);
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON delete(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON( jsonParams );
        if (jsonParams.getDataObject(TwbCmmnConst.G_DATA).size() > 0) {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("ITEM_LIST", jsonParams.getDataObject(TwbCmmnConst.G_DATA));
            JSONArray arrParam = new JSONArray();
            arrParam.add(jsonObj);
            jsonParams.setDataObject(arrParam);
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mng.dao.LkagMngMapper", "delete", jsonParams);
        }else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "선택된 항목이 없습니다.");
        }
        redisCacheCustcoLkagRepository.remove(jsonParams.getString("LKAG_ID"));
        return objRetParams;
    }

    @Override
    public TelewebJSON dpcnChk(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mng.dao.LkagMngMapper", "dpcnChk", jsonParams);
        if (objRetParams.getHeaderInt("COUNT") == 0) {
            objRetParams.setHeader("ERROR_FLAG", false);
            return objRetParams;
        } else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 사용중입니다.");
        }
        return objRetParams;
    }
}
