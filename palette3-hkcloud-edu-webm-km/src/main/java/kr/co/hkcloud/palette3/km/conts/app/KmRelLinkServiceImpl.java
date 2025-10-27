package kr.co.hkcloud.palette3.km.conts.app;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("kmRelLinkService")
public class KmRelLinkServiceImpl implements KmRelLinkService
{
	private final TwbComDAO            mobjDao;
    @Autowired
	private InnbCreatCmmnService innbCreatCmmnService;
    private final String namespace = "kr.co.hkcloud.palette3.km.conts.dao.KmRelLinkMapper";
    
	/* (non-Javadoc)
	 * @see kr.co.hkcloud.palette3.km.guide.app.KmGuideRelLinkService#selectList(kr.co.hkcloud.palette3.common.twb.model.TelewebJSON)
	 */
	@Override
	public TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException {
		return mobjDao.select(namespace, "selectList", jsonParams);
	}
	/* (non-Javadoc)
	 * @see kr.co.hkcloud.palette3.km.guide.app.KmGuideRelLinkService#regRtn(kr.co.hkcloud.palette3.common.twb.model.TelewebJSON)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TelewebJSON regRtn(TelewebJSON jsonParams) throws TelewebAppException {
		
		this.checkRequireValid(jsonParams, "KMS_CONTS_ID", "정상적인 호출이 아닙니다. 키가 누락되었습니다.");
		
		TelewebJSON objRetParams = new TelewebJSON(jsonParams);
		
		int KMS_REL_LNK_ID = innbCreatCmmnService.createSeqNo("KMS_REL_LNK_ID");
		String userId = jsonParams.getString("USER_ID");
    	jsonParams.setInt("RGTR_ID", Integer.parseInt(userId));
    	jsonParams.setInt("KMS_REL_LNK_ID", KMS_REL_LNK_ID);
		
		objRetParams = mobjDao.insert(namespace, "insert", jsonParams);
		jsonParams.setString("KMS_REL_LNK_ID",KMS_REL_LNK_ID+"");
		objRetParams.setString("KMS_REL_LNK_ID", KMS_REL_LNK_ID+"");
		
		return objRetParams;
		
	}

	/* (non-Javadoc)
	 * @see kr.co.hkcloud.palette3.km.guide.app.Km#updateRtn(kr.co.hkcloud.palette3.common.twb.model.TelewebJSON)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebAppException {
		
		this.checkRequireValid(jsonParams, "KMS_CONTS_ID", "정상적인 호출이 아닙니다. 키가 누락되었습니다.");
		
		TelewebJSON objRetParams = new TelewebJSON(jsonParams);
		
		String KMS_REL_LNK_ID = jsonParams.getString("KMS_REL_LNK_ID");
		String userId = jsonParams.getString("USER_ID");
		jsonParams.setInt("MDFR_ID", Integer.parseInt(userId));
		
		objRetParams = mobjDao.update(namespace, "update", jsonParams);
		objRetParams.setString("KMS_REL_LNK_ID", KMS_REL_LNK_ID);
		
		return objRetParams;
	}

	/* (non-Javadoc)
	 * @see kr.co.hkcloud.palette3.km.guide.app.Km#deleteRtn(kr.co.hkcloud.palette3.common.twb.model.TelewebJSON)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TelewebJSON deleteRtn(TelewebJSON jsonParams) throws TelewebAppException {
		
		this.checkRequireValid(jsonParams, "KMS_CONTS_ID", "정상적인 호출이 아닙니다. 키가 누락되었습니다.");
		
		TelewebJSON objRetParams = new TelewebJSON(jsonParams);
		
		String userId = jsonParams.getString("USER_ID");
		jsonParams.setInt("MDFR_ID", Integer.parseInt(userId));
        String list = jsonParams.getString("LIST").toString();
        list = list.replace("&#91;", "[").replace("&#93;","]");
        jsonParams.setString("list", list);
		
		objRetParams = mobjDao.delete(namespace, "delete", jsonParams);
		
		return objRetParams;
	}
	
	private void checkRequireValid(TelewebJSON jsonParams , String key , String message) throws TelewebApiException 
	{
		try {
        	String val =  jsonParams.getString(key,0,"");
        	if("".equals(val)) throw new Exception(message);
        } catch (Exception e) {
        	if("".equals(e.getMessage())) throw new TelewebApiException();
        	throw new TelewebApiException(e.getMessage());
        }
	}

}
