package kr.co.hkcloud.palette3.admin.lkag.mst.app;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.lkag.mst.app
 * fileName       : LkagMstServiceImpl
 * author         : KJD
 * date           : 2024-03-20
 * description    : << 여기 설명 >>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-20        KJD       최초 생성
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("lkagMstService")
public class LkagMstServiceImpl implements LkagMstService {

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;

    @Override
    public TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mst.dao.LkagMstMapper", "selectList", jsonParams);
    }

    @Override
    public TelewebJSON selectBox(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mst.dao.LkagMstMapper", "selectBox", jsonParams);
    }

    @Override
    public TelewebJSON insert(TelewebJSON jsonParams) throws TelewebAppException {
        jsonParams.setInt("LKAG_MST_ID", innbCreatCmmnService.createSeqNo("LKAG_MST_ID"));
        return mobjDao.insert("kr.co.hkcloud.palette3.admin.lkag.mst.dao.LkagMstMapper", "insert", jsonParams);
    }

    @Override
    public TelewebJSON update(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mst.dao.LkagMstMapper", "update", jsonParams);
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
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.admin.lkag.mst.dao.LkagMstMapper", "delete", jsonParams);
        }else {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "선택된 항목이 없습니다.");
        }
        return objRetParams;
    }

    @Override
    public TelewebJSON dpcnChkMstNm(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.admin.lkag.mst.dao.LkagMstMapper", "dpcnChkMstNm", jsonParams);
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
