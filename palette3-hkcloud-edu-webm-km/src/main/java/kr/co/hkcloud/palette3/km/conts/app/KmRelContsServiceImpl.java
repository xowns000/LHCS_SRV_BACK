package kr.co.hkcloud.palette3.km.conts.app;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("kmRelContsService")
public class KmRelContsServiceImpl implements KmRelContsService
{
	private final TwbComDAO mobjDao;
    private final String namespace = "kr.co.hkcloud.palette3.km.conts.dao.KmRelContsMapper";
    
	@Override
	public TelewebJSON selectList(TelewebJSON jsonParams) throws TelewebAppException {
		return mobjDao.select(namespace, "selectList", jsonParams);
	}

	@Override
	@Transactional(rollbackFor = TelewebAppException.class)
	public TelewebJSON regRtn(TelewebJSON jsonParams) throws TelewebAppException {
		
		this.checkRequireValid(jsonParams, "KMS_CONTS_ID", "정상적인 호출이 아닙니다. 키가 누락되었습니다.");
		
		TelewebJSON objRetParams = new TelewebJSON(jsonParams);
		
		String userId = jsonParams.getString("USER_ID");
    	jsonParams.setInt("RGTR_ID", Integer.parseInt(userId));
    	String list = jsonParams.getString("LIST").toString();
        list = list.replace("&#91;", "[").replace("&#93;","]");

        try {
	        List<String> addList = (List<String>)(new ObjectMapper().readValue(list, List.class));
	        for(String KMS_CONTS_ID : addList) {
	        	jsonParams.setString("KMS_REL_CONTS_ID", KMS_CONTS_ID);
		    	objRetParams = mobjDao.select(namespace, "selectList", jsonParams);
		    	if(objRetParams.getSize() == 0) {
		    		objRetParams = mobjDao.insert(namespace, "insert", jsonParams);
		    	}
	        }
        } catch (Exception e) {
        	throw new TelewebAppException(e.getLocalizedMessage());
        }
		return objRetParams;
	}
	
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
