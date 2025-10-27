package kr.co.hkcloud.palette3.svy.app;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("SvyExclusionTargetService")
public class SvyExclusionTargetServiceImpl implements SvyExclusionTargetService {

    public final TwbComDAO mobjDao;
    public final InnbCreatCmmnService innbCreatCmmnService;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.svy.dao.SvyExclusionTargetMapper";
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTargetList(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectTargetList", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON checkCustPhnNo(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "checkCustPhnNo", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON mergeTarget(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objParam = new TelewebJSON();
        if(StringUtils.isEmpty(jsonParam.getString("SRVY_EXL_TRGT_ID"))) {
            objParam = insertTarget(jsonParam);
        } else {
            objParam = updateTarget(jsonParam);
        }
        return objParam;
    }
    
    @Transactional(readOnly = false)
    public TelewebJSON insertTarget(TelewebJSON jsonParam) throws TelewebAppException {
        jsonParam.setString("SRVY_EXL_TRGT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_EXL_TRGT_ID")));
        return mobjDao.insert(sqlNameSpace, "insertTarget", jsonParam);
    }
    
    @Transactional(readOnly = false)
    public TelewebJSON updateTarget(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.update(sqlNameSpace, "updateTarget", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteTarget(TelewebJSON jsonParam) throws TelewebAppException {
        
        List<String> srvyExlTrgtList = new LinkedList<String>();
        JSONArray jsonObj = jsonParam.getDataObject(TwbCmmnConst.G_DATA);
        for (int n = 0; n < jsonObj.size(); n++) {
            JSONObject objData = jsonObj.getJSONObject(n);
            
            srvyExlTrgtList.add(objData.getString("SRVY_EXL_TRGT_ID"));
            
        }
        //신규설정메뉴가 있으면
        if(srvyExlTrgtList.size() > 0) {
            jsonParam.setObject("SRVT_EXL_TRGT_LIST", 0, srvyExlTrgtList);
        }
        return mobjDao.update(sqlNameSpace, "deleteTarget", jsonParam);
    }
}
