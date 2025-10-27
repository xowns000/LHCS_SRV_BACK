package kr.co.hkcloud.palette3.setting.consulttype.app;


import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Slf4j
@RequiredArgsConstructor
@Service("settingConsulttypeManageService")
public class SettingConsulttypeManageServiceImpl implements SettingConsulttypeManageService
{

    private final TwbComDAO mobjDao;


    /**
     * 회사이름을 조회한다.
     * 
     * @author        정성현
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.10.12
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCompanyName(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "selectCompanyName", jsonParams);
    }


    /**
     * 설정상담유형관리 1레벨목록을 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCnslTypLevelByTopLevel(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "selectCnslTypLevelByTopLevel", jsonParams);
    }


    /**
     * 설정상담유형관리 목록(1레벨제외)을 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectCnslTypByNotTopLevel(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "selectCnslTypByNotTopLevel", jsonParams);
    }


    /**
     * 상담유형 저장
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRtnTwbTalkCnslTyp(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON();

        //20191025 HYG :: MCRM 에서 없는 상담유형코드를 UPDATE 하는 경우, 기존에는 INSERT 했지만 하지 않음
        TelewebJSON objTempParams = mobjDao.select("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "selectRtnDupCnslTyp", jsonParams);

        if(jsonParams.getString("TRANS_STATUS").equals(TwbCmmnConst.TRANS_INS)) {
            if(objTempParams.getHeaderInt("TOT_COUNT") > 0) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "MCS에 해당 상담유형코드가 이미 존재합니다.");
            }
            else {
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "updateRtnTwbTalkCnslTyp", jsonParams);
            }
        }
        else {
            if(objTempParams.getHeaderInt("TOT_COUNT") == 0) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "MCS에 수정할 상담유형코드가 존재하지 않습니다.");
            }
            else {
                //상담이력 테이블에 저장
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "updateRtnTwbTalkCnslTyp", jsonParams);

                //MCRM 표시 -> 상담유형 수정 정책에 따라 상위 상담유형이 미표시로 수정되는 경우 하위 상담유형까지 미표시로 수정 
                if("N".equals(jsonParams.getString("USE_YN"))) {
                    updateRecursiveICnslTypNoUse(jsonParams.getString("ASP_NEWCUST_KET"), jsonParams.getString("CNSL_TYP_CD"), 1);
                }
            }

        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * MCRM 상담유형 동기화 작업 :: MCRM 로직에 맞게 상담유형 표시 → 미표시 수정 시 하위 상담유형도 미표시 처리한다. 상담유형 표시 → 미표시 ( recursive )
     * 
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void updateRecursiveICnslTypNoUse(String custcoId, String curCnslTypCode, int flag) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        String telIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);
        jsonParams.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
        jsonParams.setString("ASP_NEWCUST_KEY", custcoId);
        jsonParams.setString("CNSL_TYP_CD", curCnslTypCode);

        TelewebJSON childJson = mobjDao.select("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "selectCnslTypByNotTopLevel", jsonParams);

        // 1. 현재 선택된 상담유형의 하위레벨 상담유형이 있는지 확인 (없을 경우, 현재 상담유형이 마지막 상담유형)
        if(childJson.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < childJson.getHeaderInt("TOT_COUNT"); i++) {
                JSONArray objArry = childJson.getDataObject();
                String custcoId2 = objArry.getJSONObject(i).getString("NEWCUSTCO_ID");
                String pCnslTypCode = objArry.getJSONObject(i).getString("CNSL_TYP_CD");

                updateRecursiveICnslTypNoUse(custcoId2, pCnslTypCode, 0);
            }
        }
        if(flag != 1)
            updateCnslTypUseNo(curCnslTypCode);
    }


    public TelewebJSON updateCnslTypUseNo(String curCnslTypCode) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        String telIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);
        jsonParams.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
        jsonParams.setString("CNSL_TYP_CD", curCnslTypCode);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "updateCnslTypUseNo", jsonParams);
        return objRetParams;
    }


    public TelewebJSON deleteRtnCnslTyp(TelewebJSON mjsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String curTalkCnslTypCd = mjsonParams.getString("CNSL_TYP_CD");
        String custcoId = mjsonParams.getString("ASP_NEWCUST_KEY");
        int curTalkCnslTypLvl = mjsonParams.getInt("CNSL_TYP_DIV_CD");

        deleteRecursiveICnslTyp(custcoId, curTalkCnslTypCd, curTalkCnslTypLvl);

        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "정상 삭제 되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }


    @Override
    public TelewebJSON selectRtnNodeDetail(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "selectRtnNodeDetail", mjsonParams);
    }


    /**
     * 상담유형 삭제 ( recursive )
     * 
     * @param  custcoId
     * @param  curCnslTypCode
     * @param  curTalkCnslTypLvl
     * @throws TelewebAppException
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    private void deleteRecursiveICnslTyp(String custcoId, String curCnslTypCode, int curTalkCnslTypLvl) throws TelewebAppException
    {

        TelewebJSON childJson = selectChildCnslTypCode(custcoId, curCnslTypCode, curTalkCnslTypLvl);

        // 1. 현재 선택된 문의유형의 하위레벨 문의유형이 있는지 확인 (없을 경우, 현재 문의유형이 마지막 문의유형)
        if(childJson.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < childJson.getHeaderInt("TOT_COUNT"); i++) {
                JSONArray objArry = childJson.getDataObject();

                String pCnslTypCode = objArry.getJSONObject(i).getString("CNSL_TYP_CD");
                int pTalkCnslTypLvl = objArry.getJSONObject(i).getInt("CNSL_TYP_DIV_CD");

                deleteRecursiveICnslTyp(custcoId, pCnslTypCode, pTalkCnslTypLvl);
            }
        }

        deleteCnslTyp(custcoId, curCnslTypCode);

    }


    /**
     * 하위레벨의 문의유형을 가지고온다.
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    private TelewebJSON selectChildCnslTypCode(String custcoId, String curCnslTypCode, int curTalkCnslTypLvl) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        String telIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);
        jsonParams.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("CNSL_TYP_CD", curCnslTypCode);
        jsonParams.setInt("CNSL_TYP_DIV_CD", curTalkCnslTypLvl);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "selectCnslTypByNotTopLevel", jsonParams);
        return objRetParams;
    }


    /**
     * 문의유형을 삭제 처리
     * 
     * @author        kmg
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2018.11.08
     */
    private TelewebJSON deleteCnslTyp(String custcoId, String curCnslTypCode) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        String telIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);
        jsonParams.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
        jsonParams.setString("ASP_NEWCUST_KEY", custcoId);
        jsonParams.setString("CNSL_TYP_CD", curCnslTypCode);

        //DAO검색 메서드 호출
        TelewebJSON objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.consulttype.dao.SettingConsulttypeManageMapper", "deleteRtnCnslTyp", jsonParams);
        return objRetParams;
    }
}
