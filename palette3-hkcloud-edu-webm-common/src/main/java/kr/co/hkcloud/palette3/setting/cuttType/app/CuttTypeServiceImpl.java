package kr.co.hkcloud.palette3.setting.cuttType.app;


import java.sql.SQLException;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service("cuttTypeService")
public class CuttTypeServiceImpl implements CuttTypeService {

    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO mobjDao;


    /**
     * 상담유형 설정 Tree
     */
    @Transactional(readOnly = true)
    public TelewebJSON cuttTypeTreeList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "cuttTypeTreeList", mjsonParams);
    }

    /**
     * 상담유형 셋팅 정보 조회
     */
    @Transactional(readOnly = true)
    public TelewebJSON cuttTypeSettingInfo(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "cuttTypeSettingInfo", mjsonParams);
    }

    /**
     * 상담유형 등록 수정
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON cuttTypeProc(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성

        //등록
        if (StringUtils.isBlank(mjsonParams.getString("CUTT_TYPE_ID"))) {
            int CUTT_TYPE_ID = innbCreatCmmnService.createSeqNo("CUTT_TYPE_ID");
            mjsonParams.setInt("CUTT_TYPE_ID", CUTT_TYPE_ID);

            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "INSERT_CUTT_TYPE", mjsonParams);
        } else { //수정
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "UPDATE_CUTT_TYPE", mjsonParams);
        }

        return objRetParams;
    }

    /**
     * 상담유형 Tree 순서 변경
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON cuttTypeOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성

        if ("UP".equals(mjsonParams.getString("ORDER_TYPE"))) {
            if (Integer.parseInt(mjsonParams.getString("SORT_ORD")) > 1) {
                mjsonParams.setInt("ADD_NUM", 1);
                mjsonParams.setInt("SORT_ORD", Integer.parseInt(mjsonParams.getString("SORT_ORD")) - 1);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "UPDATE_CUTT_TYPE_OTHER_SORT_ORDER", mjsonParams);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "UPDATE_CUTT_TYPE_SORT_ORDER", mjsonParams);
            }
        } else if ("DOWN".equals(mjsonParams.getString("ORDER_TYPE"))) {
            if (Integer.parseInt(mjsonParams.getString("SORT_ORD")) < Integer.parseInt(mjsonParams.getString("MAX_SORT_ORD"))) {
                mjsonParams.setInt("ADD_NUM", -1);
                mjsonParams.setInt("SORT_ORD", Integer.parseInt(mjsonParams.getString("SORT_ORD")) + 1);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "UPDATE_CUTT_TYPE_OTHER_SORT_ORDER", mjsonParams);
                objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "UPDATE_CUTT_TYPE_SORT_ORDER", mjsonParams);
            }
        }

        return objRetParams;
    }

    /**
     * 상담유형 삭제
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON cuttTypeDel(TelewebJSON mjsonParams) throws TelewebAppException {
        mjsonParams.setString("DEL_YN", "Y");
        return mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "UPDATE_CUTT_TYPE", mjsonParams);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON cuttTypeSettingSave(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "UPDATE_CUTT_TYPE_SETTING", mjsonParams);
    }

    /**
     * 고객사 대표번호 목록
     */
    @Transactional(readOnly = true)
    public TelewebJSON dsptchNoList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "dsptchNoList", mjsonParams);
    }

    /**
     * 상담사 상담유형 북마크(즐겨찾기) 등록, 삭제
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON cuslCuttTypeBmkList(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "cuslCuttTypeBmkList", mjsonParams);
    }

    /**
     * 상담사 상담유형 북마크(즐겨찾기) 등록, 삭제
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON cuslCuttTypeBmkProc(TelewebJSON mjsonParams) throws TelewebAppException {
        if ("RECENT_REG".equals(mjsonParams.getString("REG_SE_CD"))) { //최근 등록된 상담유형 즐겨찾기

            TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "cuslCuttTypeBmkList", mjsonParams);

            if (objRetParams.getSize() > 0) {
                return mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "cuslCuttTypeBmkUpdate", mjsonParams);
            } else {
                return mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "cuslCuttTypeBmkReg", mjsonParams);
            }
        } else { //상담사 등록 상담유형 즐겨찾기
            if ("REG".equals(mjsonParams.getString("JOB_GB"))) {
                return mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "cuslCuttTypeBmkReg", mjsonParams);
            } else {
                return mobjDao.update("kr.co.hkcloud.palette3.setting.expsnAttr.dao.cuttTypeMapper", "cuslCuttTypeBmkDel", mjsonParams);
            }
        }

    }
}
