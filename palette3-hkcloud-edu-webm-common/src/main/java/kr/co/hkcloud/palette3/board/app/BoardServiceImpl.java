package kr.co.hkcloud.palette3.board.app;


import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



@Slf4j
@RequiredArgsConstructor
@Service("boardService")
public class BoardServiceImpl implements BoardService
{
    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;


    /**
     * 시스템공지사항 데이터 조회 템플릿
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnSystemNotice(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnSystemNotice", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdSystem(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnBrdSystem", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdList(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnBrdList", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdCheck(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnBrdCheck", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectIsRtNoticeBoad(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectIsRtNoticeBoad", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectHaveCanceledYN(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectHaveCanceledYN", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnCancelNoice(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "updateRtnCancelNoice", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON INSERT_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.board.dao.BoardMapper", "INSERT_PLT_BLBD_MST", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON UPDATE_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "UPDATE_PLT_BLBD_MST", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON DELETE_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.board.dao.BoardMapper", "DELETE_PLT_BLBD_MST", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON SELECT_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "SELECT_PLT_BLBD_MST", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON SELECT_PAGE_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "SELECT_PAGE_PLT_BLBD_MST", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdRmk(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnBrdRmk", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteRtnBrdAll(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "deleteRtnBrdAll", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnBrdRmk(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.board.dao.BoardMapper", "insertRtnBrdRmk", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnSelectNo(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "updateRtnSelectNo", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON DELETE_MEW_BRD_RMK(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.board.dao.BoardMapper", "DELETE_MEW_BRD_RMK", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtBtnAuthRole(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtBtnAuthRole", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON DELETE_MEW_BRD_MASTER(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.board.dao.BoardMapper", "DELETE_MEW_BRD_MASTER", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON INSERT_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.board.dao.BoardMapper", "INSERT_PLT_BLBD_CMT", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON UPDATE_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "UPDATE_PLT_BLBD_CMT", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON DELETE_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.board.dao.BoardMapper", "DELETE_PLT_BLBD_CMT", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON SELECT_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "SELECT_PLT_BLBD_CMT", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON SELECT_PAGE_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "SELECT_PAGE_PLT_BLBD_CMT", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON DELETE_MEW_BRD_FILE(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.board.dao.BoardMapper", "DELETE_MEW_BRD_FILE", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdMaxBrd(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnBrdMaxBrd", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertRtnBrd(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.board.dao.BoardMapper", "insertRtnBrd", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnBrd(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "updateRtnBrd", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateBfAttachDataUpDate(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "updateBfAttachDataUpDate", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdDetail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnBrdDetail", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBrdFile(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnBrdFile", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON INSERT_MEW_BRD_FILE(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.board.dao.BoardMapper", "INSERT_MEW_BRD_FILE", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON UPDATE_BRD_FILEGRPCNT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "UPDATE_BRD_FILEGRPCNT", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnBoardFunction(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnBoardFunction", jsonParams);
    }


    @Override
    public TelewebJSON selectRtnMenuName(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectRtnMenuName", jsonParams);
    }

    
    @Override
    public TelewebJSON updateTopmsgRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "updateTopmsgRtn", jsonParams);
    }
    
    @Override
    public TelewebJSON updateTopiconRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "updateTopiconRtn", jsonParams);
    }

    // 여기부터
    
    // 게시글 목록 조회
 	@Transactional(readOnly = true)
 	public TelewebJSON selectBoardList(TelewebJSON mjsonParams) throws TelewebAppException {
 		TelewebJSON objRetData = new TelewebJSON();
 		

 		if(!mjsonParams.getString("AL_NEW_YN").isEmpty()){
 	 		// 최신글 조회 - 알림센터 알림 아이콘 활성화 설정용 조회
 			objRetData = mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectGetNewBrd", mjsonParams);
 		}else {
 		
	 		// 게시글 제목 조회시 특수문자 파라미터 치환
	 		String pstTtl = mjsonParams.getString("PST_TTL");
			pstTtl = pstTtl.replaceAll("&lt;", "<");
			pstTtl = pstTtl.replaceAll("&gt;", ">");
			mjsonParams.setString("PST_TTL", pstTtl);
			
			objRetData = mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectBoardList", mjsonParams);
 		}
 		
 		return objRetData;
 	}
     // 게시글 상세 조회
 	@Transactional(readOnly = true)
 	public TelewebJSON selectPstDetail(TelewebJSON mjsonParams) throws TelewebAppException {

 		TelewebJSON objRetData = new TelewebJSON();

		objRetData = mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectBoardList", mjsonParams);


 		return objRetData;
 	}
 	
 	// 게시글 유형별 개수 조회
 	@Override
 	public TelewebJSON selectBoardTpCnt(TelewebJSON jsonParams) throws TelewebAppException {
 		TelewebJSON objRetParams = new TelewebJSON(jsonParams);
 		
 		try {
 			
 			objRetParams = mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectBoardTpCnt", jsonParams);
 		
 		} catch(NullPointerException ex) {
 			
 			if(objRetParams.getString("GENR_CNT").isEmpty()) {
 				objRetParams.setString("GENR_CNT", "0");
 			}
 			if(objRetParams.getString("NOTI_CNT").isEmpty()) {
 				objRetParams.setString("NOTI_CNT", "0");
 			}
 			if(objRetParams.getString("EMRG_CNT").isEmpty()) {
 				objRetParams.setString("EMRG_CNT", "0");
 			}
 		}
 		
 		 return objRetParams;
 	}

 	// 게시글 등록/수정
 	@Transactional(readOnly = false)
 	public TelewebJSON upsertBoard(TelewebJSON mjsonParams) throws TelewebAppException {
 		TelewebJSON objRetData = new TelewebJSON(mjsonParams);     //부분반환파라미터생성
 		
 		// 게시글 제목/내용 특수문자 파라미터 치환
 		String pstTtl = mjsonParams.getString("PST_TTL");
		String pstcn = mjsonParams.getString("PST_CN");
		pstTtl = pstTtl.replaceAll("&lt;", "<");
		pstTtl = pstTtl.replaceAll("&gt;", ">");
		pstcn = pstcn.replaceAll("&lt;", "<");
		pstcn = pstcn.replaceAll("&gt;", ">");
		pstcn = pstcn.replaceAll("&#39;", "\'");
		pstcn = pstcn.replaceAll("<p>", "");
		pstcn = pstcn.replaceAll("</p>", "");
			
	    mjsonParams.setString("PST_TTL", pstTtl);
	    mjsonParams.setString("PST_CN", pstcn);
 		
 		if(Boolean.parseBoolean(mjsonParams.getString("FLAG_DATA"))){
 			// 게시글ID 채번 - 게시글 신규 등록
 			mjsonParams.setString("PST_ID", Integer.toString(innbCreatCmmnService.createSeqNo("PST_ID")));
 		    
 	 		objRetData = mobjDao.insert("kr.co.hkcloud.palette3.board.dao.BoardMapper", "insertBoard", mjsonParams);
 	 		
 		} else if(!Boolean.parseBoolean(mjsonParams.getString("FLAG_DATA"))) {
 			// 게시글 수정
 			objRetData = mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "updateBoard", mjsonParams);
 		}
 		return objRetData;
 	}

 	// 게시글 조회수 증가
 	@Transactional(readOnly = false)
 	public TelewebJSON increaseCnt(TelewebJSON jsonParams) throws TelewebAppException {
 		return mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "increaseCnt", jsonParams);
 	}
 	
	// 조회수 조회
	@Override
	public TelewebJSON selectCnt(TelewebJSON jsonParams) throws TelewebAppException {
		return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "selectCnt", jsonParams);
	}

 	// 게시글 삭제
 	@Transactional(readOnly = false)
	public TelewebJSON deleteBoard(TelewebJSON mjsonParams) throws TelewebAppException {
		return mobjDao.delete("kr.co.hkcloud.palette3.board.dao.BoardMapper", "deleteBoard", mjsonParams);
	}    
 	
    @Override
    public TelewebJSON fileKeyUnity(TelewebJSON jsonParams) throws TelewebAppException
    {
    	TelewebJSON objRetParams = new TelewebJSON(jsonParams);
    	
    	List<String> arrFileKey = new LinkedList<String>();
    	JSONArray jsonObj = jsonParams.getDataObject(TwbCmmnConst.G_DATA);
        for (int n = 0; n < jsonObj.size(); n++) {
        	JSONObject objData = jsonObj.getJSONObject(n);
        	
        	@SuppressWarnings("rawtypes")
            Iterator it = objData.keys();
            while(it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if(strKey.indexOf("arrFileKey") > -1 && StringUtils.isNotEmpty(strValue)) {
                	arrFileKey.add(strValue);
                }
            }
        }
        //신규설정메뉴가 있으면
        if(arrFileKey.size() != 0) {
        	jsonParams.setObject("arrFileKey", 0, arrFileKey);
		
        	objRetParams = mobjDao.update("kr.co.hkcloud.palette3.board.dao.BoardMapper", "fileKeyUnity", jsonParams);
		}

        return objRetParams;
    }

 	// 채팅이미지리스트 조회
 	@Transactional(readOnly = false)
 	public TelewebJSON chatImageList(TelewebJSON jsonParams) throws TelewebAppException {
 		return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "chatImageList", jsonParams);
 	}

 	// 채팅 이미지파일 단일 삭제
 	@Transactional(readOnly = false)
	public TelewebJSON deleteChatImage(TelewebJSON mjsonParams) throws TelewebAppException {
		return mobjDao.delete("kr.co.hkcloud.palette3.board.dao.BoardMapper", "deleteChatImage", mjsonParams);
	}

 	// 게시글 파일 리스트 조회
	@Override
	public TelewebJSON boardFileList(TelewebJSON jsonParams) throws TelewebAppException {
		return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "boardFileList", jsonParams);
	}

	// 파일그룹키로 파일 전체 삭제
	@Override
	public TelewebJSON deleteBoardFile(TelewebJSON jsonParams) throws TelewebAppException {
		return mobjDao.delete("kr.co.hkcloud.palette3.board.dao.BoardMapper", "deleteBoardFile", jsonParams);
	}

	
	
	// 게시판 문의 조회(테스트)
	@Override
	public TelewebJSON pstQstnSelect(TelewebJSON jsonParams) throws TelewebAppException {
		return mobjDao.select("kr.co.hkcloud.palette3.board.dao.BoardMapper", "pstQstnSelect", jsonParams);
	}
	// 게시판 문의 답변(테스트)
	@Override
	public TelewebJSON pstQstnInsert(TelewebJSON jsonParams) throws TelewebAppException {
		return mobjDao.insert("kr.co.hkcloud.palette3.board.dao.BoardMapper", "pstQstnInsert", jsonParams);
	}



}
