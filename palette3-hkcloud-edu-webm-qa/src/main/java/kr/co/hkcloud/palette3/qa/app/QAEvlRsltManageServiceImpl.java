package kr.co.hkcloud.palette3.qa.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections4.iterators.ArrayIterator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebDaoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@Service("qaEvlRsltManageService")
public class QAEvlRsltManageServiceImpl implements QAEvlRsltManageService {

    private final TwbComDAO twbComDAO;
    public final InnbCreatCmmnService innbCreatCmmnService;
    
	@Override
	@Transactional(readOnly = true)
	public TelewebJSON selectQaEvlRsltMngrList(TelewebJSON jsonParams) throws TelewebDaoException {
		//selectQaEvlRsltStat
		//selectQaEvlRsltMngrList
		TelewebJSON selListJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlRsltManageMapper", "selectQaEvlRsltMngrList", jsonParams);
		if(selListJsonParams.getHeaderBoolean("ERROR_FLAG"))
			return selListJsonParams;
			
		TelewebJSON selStatJsonParams = twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlRsltManageMapper", "selectQaEvlRsltStat", jsonParams);
		if(selStatJsonParams.getHeaderBoolean("ERROR_FLAG"))
			return selStatJsonParams;
		
		selListJsonParams.setDataObject("RSLT_STAT", selStatJsonParams.getDataObject());
		
		return selListJsonParams;
	}

	@Override
	public TelewebJSON selectQaEvlRsltTrgtList(TelewebJSON jsonParams) throws TelewebDaoException {
		return twbComDAO.select("kr.co.hkcloud.palette3.qa.dao.QAEvlRsltManageMapper", "selectQaEvlRsltTrgtList", jsonParams);
	}

}
