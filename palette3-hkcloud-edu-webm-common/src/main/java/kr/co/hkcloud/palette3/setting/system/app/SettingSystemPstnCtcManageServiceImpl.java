package kr.co.hkcloud.palette3.setting.system.app;

import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("SettingSystemPstnCtcManageService")
public class SettingSystemPstnCtcManageServiceImpl implements SettingSystemPstnCtcManageService
{

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
	
    
    // 위치정보 리스트 조회
 	@Override
 	public TelewebJSON selectPstn(TelewebJSON mjsonParams) throws TelewebAppException {
 		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
 		objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemPstnCtcManageMapper", "selectPstn", mjsonParams);
 		return objRetParams;
 	}
    
 	// 위치정보 등록 및 수정
	@Override
	public TelewebJSON upsertPstn(TelewebJSON mjsonParams) throws TelewebAppException {
		
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

		String pstnNM = mjsonParams.getString("PSTN_NM");
		String dtlAddr = mjsonParams.getString("DTL_ADDR");
		String expln = mjsonParams.getString("EXPLN");
		pstnNM = pstnNM.replaceAll("&lt;", "<");
		pstnNM = pstnNM.replaceAll("&gt;", ">");
		dtlAddr = dtlAddr.replaceAll("&lt;", "<");
		dtlAddr = dtlAddr.replaceAll("&gt;", ">");
		expln = expln.replaceAll("&lt;", "<");
		expln = expln.replaceAll("&gt;", ">");
	    mjsonParams.setString("PSTN_NM", pstnNM);
	    mjsonParams.setString("DTL_ADDR", dtlAddr);
	    mjsonParams.setString("EXPLN", expln);
		
		
		if(Boolean.parseBoolean(mjsonParams.getString("FLAG_DATA"))){
			// 위치정보 신규 등록 - 위치ID 채번
			mjsonParams.setString("PSTN_ID", Integer.toString(innbCreatCmmnService.createSeqNo("PSTN_ID")));
			objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemPstnCtcManageMapper", "insertPstn", mjsonParams);
		}else {
			// 위치정보 수정
			objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemPstnCtcManageMapper", "updatePstn", mjsonParams);
		}
		return objRetParams;
	}

	// 위치정보 삭제
	@Override
	public TelewebJSON deletePstn(TelewebJSON mjsonParams) throws TelewebAppException {
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemPstnCtcManageMapper", "deletePstn", mjsonParams);
		return objRetParams;
	}

	// 연락처정보 리스트 조회
	@Override
	public TelewebJSON selectCtc(TelewebJSON mjsonParams) throws TelewebAppException {
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemPstnCtcManageMapper", "selectCtc", mjsonParams);
		return objRetParams;
	}

	// 연락처정보 신규등록 및 수정
	@Override
	public TelewebJSON upsertCtc(TelewebJSON mjsonParams) throws TelewebAppException {
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		
		String taskNM = mjsonParams.getString("TASK_NM");
		String tkcgDeptNm = mjsonParams.getString("TKCG_DEPT_NM");
		String expln = mjsonParams.getString("EXPLN");
		taskNM = taskNM.replaceAll("&lt;", "<");
		taskNM = taskNM.replaceAll("&gt;", ">");
		tkcgDeptNm = tkcgDeptNm.replaceAll("&lt;", "<");
		tkcgDeptNm = tkcgDeptNm.replaceAll("&gt;", ">");
		expln = expln.replaceAll("&lt;", "<");
		expln = expln.replaceAll("&gt;", ">");
	    mjsonParams.setString("TASK_NM", taskNM);
	    mjsonParams.setString("TKCG_DEPT_NM", tkcgDeptNm);
	    mjsonParams.setString("EXPLN", expln);
		
		if(Boolean.parseBoolean(mjsonParams.getString("FLAG_DATA"))){
			// 연락처정보 신규 등록 - 연락처정보ID 채번
			mjsonParams.setString("CTC_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CTC_ID")));
			objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemPstnCtcManageMapper", "insertCtc", mjsonParams);
		}else {
			// 연락처정보 수정
			objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemPstnCtcManageMapper", "updateCtc", mjsonParams);
		}
		return objRetParams;
	}

	// 연락처정보 삭제
	@Override
	public TelewebJSON deleteCtc(TelewebJSON mjsonParams) throws TelewebAppException {
		TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
		objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemPstnCtcManageMapper", "deleteCtc", mjsonParams);
		return objRetParams;
	}

}
