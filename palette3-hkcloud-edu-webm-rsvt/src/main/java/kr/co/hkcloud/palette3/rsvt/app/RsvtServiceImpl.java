package kr.co.hkcloud.palette3.rsvt.app;

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
@Service("rsvtService")
public class RsvtServiceImpl implements RsvtService {

    private final TwbComDAO twbComDAO;
    public final InnbCreatCmmnService innbCreatCmmnService;

    @Override
	@Transactional(readOnly = true)
	public TelewebJSON getRsvtList(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON retParam = twbComDAO.select("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "getRsvtList", jsonParams);
		
		return retParam;
	}

    @Override
	@Transactional(readOnly = true)
	public TelewebJSON getRsvtCnList(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON retParam = twbComDAO.select("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "getRsvtCnList", jsonParams);
		
		return retParam;
	}

    @Override
	@Transactional(readOnly = false)
	public TelewebJSON regRsvtCn(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON retParam = new TelewebJSON ();
    	if(jsonParams.getString("RSVT_CUTT_GUIDE_ID").equals("0")) {
        	jsonParams.setString("RSVT_CUTT_GUIDE_ID", Integer.toString(innbCreatCmmnService.createSeqNo("RSVT_CUTT_GUIDE_ID")));
        	retParam = twbComDAO.insert("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "insertRsvtCn", jsonParams);
    	} else {
        	retParam = twbComDAO.update("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "updateRsvtCn", jsonParams);
    	}
		
		return retParam;
	}

    @Override
	@Transactional(readOnly = false)
	public TelewebJSON setCuslAltmnt(TelewebJSON jsonParams) throws TelewebDaoException {
		//예약배정변경이력
    	TelewebJSON retParam = twbComDAO.insert("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "setRsvtAltmntChgLog", jsonParams);
    	
    	retParam = twbComDAO.update("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "setCuslAltmnt", jsonParams);
		
		return retParam;
	}

    @Override
	@Transactional(readOnly = false)
	public TelewebJSON selectCustcoCuslId(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON retParam = twbComDAO.select("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "selectCustcoCuslId", jsonParams);
		
		return retParam;
	}

    @Override
	@Transactional(readOnly = false)
	public TelewebJSON selectBookingId(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON retParam = twbComDAO.select("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "selectBookingId", jsonParams);
		
		return retParam;
	}

    @Override
	@Transactional(readOnly = false)
	public TelewebJSON selectBookStat(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON retParam = twbComDAO.select("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "selectBookStat", jsonParams);
		
		return retParam;
	}

    @Override
	@Transactional(readOnly = false)
	public TelewebJSON getRsvtAltmntChgLog(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON retParam = twbComDAO.select("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "getRsvtAltmntChgLog", jsonParams);
		
		return retParam;
	}

    @Override
	@Transactional(readOnly = false)
	public TelewebJSON getRsvtCuslList(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON retParam = twbComDAO.select("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "getRsvtCuslList", jsonParams);
		
		return retParam;
	}

    @Override
	@Transactional(readOnly = false)
	public TelewebJSON monthSchedule(TelewebJSON jsonParams) throws TelewebDaoException {
    	TelewebJSON retParam = twbComDAO.select("kr.co.hkcloud.palette3.rsvt.dao.RsvtMapper", "monthSchedule", jsonParams);
		
		return retParam;
	}
    
}
