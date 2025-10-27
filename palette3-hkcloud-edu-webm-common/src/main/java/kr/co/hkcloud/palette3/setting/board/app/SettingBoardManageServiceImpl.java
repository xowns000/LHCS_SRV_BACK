package kr.co.hkcloud.palette3.setting.board.app;


import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("settingBoardManageService")
public class SettingBoardManageServiceImpl implements SettingBoardManageService
{
    private final TwbComDAO mobjDao;


    /**
     * 설정게시판관리 목록을 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.26
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.board.dao.SettingBoardManageMapper", "selectRtnBrdMaster", mjsonParams);
    }


    /**
     * 설정게시판관리 게시판을 등록한다
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); // 반환 파라메터 생성
        String strTransFlag = mjsonParams.getString("DATA_FLAG"); // 화면에서 전송된 플래그 설정

        //신규/수정 상태에 따라 insert, update 함수 호출
        if(strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
            //게시판정보 추가
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.board.dao.SettingBoardManageMapper", "INSERT_MEW_BRD_MASTER", mjsonParams);

        }
        else if(strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
            //게시판정보 수정
            mjsonParams.setBlankToNull(0);                      //빈값에 대한 NULL 처리
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.board.dao.SettingBoardManageMapper", "UPDATE_MEW_BRD_MASTER", mjsonParams);
        }

        return objRetParams;
    }


    /**
     * 설정게시판관리 게시판을 등록한다
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.setting.board.dao.SettingBoardManageMapper", "DELETE_MEW_BRD_MASTER", mjsonParams);
    }


    /**
     * 설정게시판관리 실시간공지 사용여부를 조회한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.26
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtNotiRtn(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.board.dao.SettingBoardManageMapper", "selectRtNotiRtn", mjsonParams);
    }


    /**
     * 게시판 등록 시 중복을 체크한다
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.26
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnDupBrdMaster(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.board.dao.SettingBoardManageMapper", "selectRtnDupBrdMaster", mjsonParams);
    }


    /**
     * 게시물 건수 조회
     * 
     * @author        R&D
     * @Transactional Auto Commit
     * @return        TelewebJSON 형식의 처리 결과 데이터
     * @since         2021.03.26
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdDataCnt(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.board.dao.SettingBoardManageMapper", "selectRtnBrdDataCnt", mjsonParams);
    }

}
