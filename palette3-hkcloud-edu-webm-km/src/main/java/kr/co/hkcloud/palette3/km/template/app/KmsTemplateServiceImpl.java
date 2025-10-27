/**
 * 프로그램 설명
 * @date        : 2023. 6. 26.
 * @author      : ryucease
 * @version	: 1.0
 */
package kr.co.hkcloud.palette3.km.template.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Description : 클래스에 대한 설명을 입력해주세요.
 * package  : kr.co.hkcloud.palette3.kms.template.app
 * filename : KmsTemplateServiceImpl.java
 * Date : 2023. 6. 26.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 26., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Service("kmsTemplateService")
public class KmsTemplateServiceImpl implements KmsTemplateService {
	
	private final TwbComDAO mobjDao;
	private final String namespace = "kr.co.hkcloud.palette3.kms.template.dao.KmsTemplateMapper";
	
	@Autowired
	private InnbCreatCmmnService innbCreatCmmnService;
	
	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectPageList(TelewebJSON jsonParams) throws TelewebAppException {

		return mobjDao.select(namespace, "selectList", jsonParams);
	}
	
	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectRtnDetail(TelewebJSON jsonParams) throws TelewebAppException {
		
		return mobjDao.select(namespace, "selectDetail", jsonParams);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TelewebJSON regRtn(TelewebJSON jsonParams) throws TelewebAppException {
		
		TelewebJSON objRetParams = new TelewebJSON(jsonParams);
		
		int KMS_TMPL_ID = innbCreatCmmnService.createSeqNo("KMS_TMPL_ID");
		String userId = jsonParams.getString("USER_ID");
    	jsonParams.setInt("RGTR_ID", Integer.parseInt(userId));
    	jsonParams.setInt("KMS_TMPL_ID", KMS_TMPL_ID);
		
		objRetParams = mobjDao.insert(namespace, "insert", jsonParams);
		objRetParams.setString("KMS_TMPL_ID", KMS_TMPL_ID+"");
		return objRetParams;
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebAppException {
		
		TelewebJSON objRetParams = new TelewebJSON(jsonParams);
		
		String userId = jsonParams.getString("USER_ID");
		jsonParams.setInt("MDFR_ID", Integer.parseInt(userId));
		
		objRetParams = mobjDao.update(namespace, "update", jsonParams);
		objRetParams.setString("KMS_TMPL_ID", jsonParams.getString("KMS_TMPL_ID"));
		
		return objRetParams;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public TelewebJSON deleteRtn(TelewebJSON jsonParams) throws TelewebAppException {
		
		TelewebJSON objRetParams = new TelewebJSON(jsonParams);
		
		String userId = jsonParams.getString("USER_ID");
		jsonParams.setInt("MDFR_ID", Integer.parseInt(userId));
        String list = jsonParams.getString("list").toString();
        list = list.replace("&#91;", "[").replace("&#93;","]");
        jsonParams.setString("list", list);
		
		objRetParams = mobjDao.delete(namespace, "delete", jsonParams);
		
		return objRetParams;
	}

}
