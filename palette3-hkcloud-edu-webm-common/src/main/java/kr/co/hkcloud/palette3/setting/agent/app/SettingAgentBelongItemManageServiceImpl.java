package kr.co.hkcloud.palette3.setting.agent.app;


import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("settingAgentBelongItemManageService")
public class SettingAgentBelongItemManageServiceImpl implements SettingAgentBelongItemManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 설정사용자소속항목관리 목록을 조회한다
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectAttrView(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectAttrView", jsonParams);
    }


    /**
     * 설정사용자소속항목관리 항목을 등록한다
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertAttr(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "insertAttr", jsonParams);
    }


    /**
     * 설정사용자소속항목관리 항목을 수정한다
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateAttr(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "updateAttr", jsonParams);
    }


    /**
     * 설정사용자소속항목관리 항목을 삭제한다
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteAttr(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "deleteAttr", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectChkDeptCode(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectChkDeptCode", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectNewDeptData(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectNewDeptData", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertNewDeptInfo(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "insertNewDeptInfo", jsonParams);
    }


    /**
     * 조직정보 조회(트리뷰)
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnDeptTreeView(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        if("Y".equals(jsonParams.getString("DISP_YN"))) {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectDeptTreeViewAllDisplay", jsonParams);
        }
        else {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectDeptTreeView", jsonParams);
        }
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 조직정보 상세내역 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnNodeDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectRtnNodeDetail", jsonParams);
    }


    /**
     * 조직관리 노출여부 업데이트
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON updateRtnDeptDisplay(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //화면표시여부에 따라 업데이트 구문을 다르게 적용
        if("Y".equals(jsonParams.getString("SCR_DISP_YN"))) {

            // 부모 조직도 YES 전환해야함.
            String fullDeptCd = (String) jsonParams.getString("FULL_DEPT_CD");
            String[] buf = fullDeptCd.split("/");
            jsonParams.setObject("FULL_DEPT_CD_ARRAY", 0, buf);

            //조직관리 상세내역 반환
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "updateRtnDeptYesDisplay", jsonParams);

        }
        else {
            //조직관리 상세내역 반환
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "updateRtnDeptNoDisplay", jsonParams);
        }

        //정렬순서 업데이트
        mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "UPDATE_PLT_ORGZ", jsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 조직정보 조회(페이지) - 조직정보 조회 팝업
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnDeptListPage(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectDeptListPage", jsonParams);
    }


    /**
     * 부서정보 조회(wfms)
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserDeptCdInfo1(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectRtnUserDeptCdInfo1", mjsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserDeptCdInfo2(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectRtnUserDeptCdInfo2", mjsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserDeptCdInfo3(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentBelongItemManageMapper", "selectRtnUserDeptCdInfo3", mjsonParams);
    }
}
