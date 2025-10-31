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
@Service("SvyExclusionConditionService")
public class SvyExclusionConditionServiceImpl implements SvyExclusionConditionService {

    public final TwbComDAO mobjDao;
    public final InnbCreatCmmnService innbCreatCmmnService;
    private final String sqlNameSpace = "kr.co.hkcloud.palette3.svy.dao.SvyExclusionConditionMapper";
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectConditionList(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.select(sqlNameSpace, "selectConditionList", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON mergeCondition(TelewebJSON jsonParam) throws TelewebAppException {
        TelewebJSON objParam = new TelewebJSON();
        if(StringUtils.isEmpty(jsonParam.getString("SRVY_EXL_COND_ID"))) {
            objParam = insertCondition(jsonParam);
        } else {
            objParam = updateCondition(jsonParam);
        }
        return objParam;
    }
    
    @Transactional(readOnly = false)
    public TelewebJSON insertCondition(TelewebJSON jsonParam) throws TelewebAppException {
        jsonParam.setString("SRVY_EXL_COND_ID", Integer.toString(innbCreatCmmnService.createSeqNo("SRVY_EXL_COND_ID")));
        return mobjDao.insert(sqlNameSpace, "insertCondition", jsonParam);
    }
    
    @Transactional(readOnly = false)
    public TelewebJSON updateCondition(TelewebJSON jsonParam) throws TelewebAppException {
        return mobjDao.update(sqlNameSpace, "updateCondition", jsonParam);
    }
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteCondition(TelewebJSON jsonParam) throws TelewebAppException {
        
        List<String> srvyExlCondList = new LinkedList<String>();
        JSONArray jsonObj = jsonParam.getDataObject(TwbCmmnConst.G_DATA);
        for (int n = 0; n < jsonObj.size(); n++) {
            JSONObject objData = jsonObj.getJSONObject(n);
            
            srvyExlCondList.add(objData.getString("SRVY_EXL_COND_ID"));
            
        }
        //신규설정메뉴가 있으면
        if(srvyExlCondList.size() > 0) {
            jsonParam.setObject("SRVT_EXL_COND_LIST", 0, srvyExlCondList);
        }
        return mobjDao.update(sqlNameSpace, "deleteCondition", jsonParam);
    }
}
