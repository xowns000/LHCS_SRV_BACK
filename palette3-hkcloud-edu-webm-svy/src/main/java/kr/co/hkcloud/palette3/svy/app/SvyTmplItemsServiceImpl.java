package kr.co.hkcloud.palette3.svy.app;


import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("svyTmplItemsService")
public class SvyTmplItemsServiceImpl implements SvyTmplItemsService
{
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO            mobjDao;
    private String namespace = "kr.co.hkcloud.palette3.svy.dao.svyTmplItemsMapper";

    /*
     * 설문분류 Tree 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON clsfTreeList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select(namespace, "clsfTreeList", mjsonParams);
    }

    @Override
    @Transactional(readOnly = true)
    public TelewebJSON clsfInfo(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select(namespace, "clsfInfo", mjsonParams);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON clsfProc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        
        //  등록
        if(StringUtils.isBlank(mjsonParams.getString("SRVY_CLSF_ID"))) {
            
            // 1. 설문분류 등록
            int srvyClsfId = innbCreatCmmnService.createSeqNo("SRVY_CLSF_ID");
            mjsonParams.setInt("SRVY_CLSF_ID", srvyClsfId);
            objRetParams = mobjDao.insert(namespace, "insertSrvyClsf", mjsonParams);
        }
        //  수정
        else {
            // 1. 설문분류 수정
            objRetParams = mobjDao.update(namespace, "updateSrvyClsf", mjsonParams);
        }

        return objRetParams;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON clsfOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException
    {
        // 바꾸려는 Key 값 조회
        TelewebJSON selectInfoParam = new TelewebJSON();
        selectInfoParam.setString("UP_SRVY_CLSF_ID", mjsonParams.getString("UP_SRVY_CLSF_ID"));
        selectInfoParam.setString("SORT_ORD", mjsonParams.getString("CHG_ORD"));
        
        selectInfoParam = mobjDao.select(namespace, "clsfInfo", selectInfoParam);
        String otherKmsClsfId = selectInfoParam.getString("SRVY_CLSF_ID");
        
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        
        // 1. 바꾸려는 Key 값의 SortOrd 변경
        TelewebJSON udtParam = new TelewebJSON();
        udtParam.setString("SRVY_CLSF_ID", otherKmsClsfId);
        udtParam.setString("SORT_ORD", mjsonParams.getString("SORT_ORD"));
        objRetParams = mobjDao.update(namespace, "updateSrvyClsf", udtParam);
        
        // 선택한 분류의 순서변경
        objRetParams = mobjDao.update(namespace, "updateSrvyClsfSortOrder", mjsonParams);
        
        return objRetParams;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON clsfDel(TelewebJSON mjsonParams) throws TelewebAppException {
        mjsonParams.setString("DEL_YN", "Y");
        // 설문분류 삭제여부 Update
        return mobjDao.update(namespace, "updateSrvyClsf", mjsonParams);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTmplItemList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectTmplItemList", mjsonParams);
    }
    
    public TelewebJSON deleteTmplItemList(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성

        String srvyClsfId = mjsonParams.getString("SRVY_CLSF_ID");

        JSONArray srvyQitemIdList = mjsonParams.getDataObject();

        if (!srvyQitemIdList.isEmpty()) {
            for (int i = 0; i < srvyQitemIdList.size(); i++) {
                JSONObject tmpConts = srvyQitemIdList.getJSONObject(i);
                TelewebJSON contsJson = new TelewebJSON();
                contsJson.setString("SRVY_CLSF_ID", srvyClsfId);
                contsJson.setString("SRVY_QITEM_ID", tmpConts.getString("SRVY_QITEM_ID"));
                objRetParams = this.deleteTmplItem(contsJson);
            }
        }

        return objRetParams;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteTmplItem(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.update(namespace, "deleteTmplItem", mjsonParams);
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectPreviewQitem(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select(namespace, "selectPreviewQitem", mjsonParams);
    }
    

}
