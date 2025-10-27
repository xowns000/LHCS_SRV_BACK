package kr.co.hkcloud.palette3.board.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface BoardService
{
    //mng/bbs/bbs02/dao/TwbBbs02Mapper.java관련 서비스들
    TelewebJSON selectRtnSystemNotice(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnBrdSystem(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnBrdList(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnBrdCheck(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectIsRtNoticeBoad(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectHaveCanceledYN(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnCancelNoice(TelewebJSON jsonParams) throws TelewebAppException;

    //mng/bbs/bbs02/dao/Bbs02Mapper.java관련 서비스

    TelewebJSON INSERT_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON UPDATE_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON DELETE_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON SELECT_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON SELECT_PAGE_PLT_BLBD_MST(TelewebJSON jsonParams) throws TelewebAppException;

    //mng/bbs/bbs03/dao/TwbBbs03Mapper.java관련 서비스
    TelewebJSON selectRtnBrdRmk(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON deleteRtnBrdAll(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertRtnBrdRmk(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnSelectNo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON DELETE_MEW_BRD_RMK(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON DELETE_MEW_BRD_MASTER(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtBtnAuthRole(TelewebJSON jsonParams) throws TelewebAppException;

    //mng/bbs/bbs03/dao/Bbs03Mapper.java관련 서비스
    TelewebJSON INSERT_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON UPDATE_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON DELETE_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON SELECT_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON SELECT_PAGE_PLT_BLBD_CMT(TelewebJSON jsonParams) throws TelewebAppException;

    //mng/bbs/bbs04/dao/TwbBbs04Mapper.java관련 서비스
    TelewebJSON DELETE_MEW_BRD_FILE(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnBrdMaxBrd(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertRtnBrd(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnBrd(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateBfAttachDataUpDate(TelewebJSON jsonParams) throws TelewebAppException;

    //mng/bbs/bbs05/dao/TwbBbs05Mapper.java관련 서비스
    TelewebJSON selectRtnBrdDetail(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnBrdFile(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON INSERT_MEW_BRD_FILE(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON UPDATE_BRD_FILEGRPCNT(TelewebJSON jsonParams) throws TelewebAppException;

    // Board Function (게시판 기능)조회 서비스
    TelewebJSON selectRtnBoardFunction(TelewebJSON jsonParams) throws TelewebAppException;

    // BRD_ID에 따른 메뉴명 조회 서비스
    TelewebJSON selectRtnMenuName(TelewebJSON jsonParams) throws TelewebAppException;
    
    // 응원메시지 수정 서비스
    TelewebJSON updateTopmsgRtn(TelewebJSON jsonParams) throws TelewebAppException;
    
    // 프로필아이콘 수정 서비스
    TelewebJSON updateTopiconRtn(TelewebJSON jsonParams) throws TelewebAppException;

    
    
    // 게시글관리 서비스
    
    /**
     * 
     * 메서드 설명		: 게시글 조회
     * @Method Name  	: selectBoardList
     * @date   			: 2023. 6. 15
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
	TelewebJSON selectBoardList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     *
     * 메서드 설명		: 게시글 조회
     * @Method Name  	: selectPstDetail
     * @date   			: 2023. 6. 15
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
	TelewebJSON selectPstDetail(TelewebJSON mjsonParams) throws TelewebAppException;
	
    /**
     * 
     * 메서드 설명		: 게시글 유형별 개수 조회
     * @Method Name  	: selectBrdTpCnt
     * @date   			: 2023. 7. 18
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
	TelewebJSON selectBoardTpCnt(TelewebJSON jsonParams) throws TelewebAppException;


    /**
     * 
     * 메서드 설명		: 게시글 신규 등록 및 수정
     * @Method Name  	: upsertBoard
     * @date   			: 2023. 6. 15
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
	TelewebJSON upsertBoard(TelewebJSON mjsonParams) throws TelewebAppException;
	
    /**
     * 
     * 메서드 설명		: 게시글 삭제
     * @Method Name  	: deleteBoard
     * @date   			: 2023. 6. 15
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON deleteBoard(TelewebJSON mjsonParams) throws TelewebAppException;
	
	/**
     * 
     * 메서드 설명		: 조회수 증가
     * @Method Name  	: increaseCnt
     * @date   			: 2023. 6. 15
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON increaseCnt(TelewebJSON jsonParams) throws TelewebAppException;
	
    /**
     * 
     * 메서드 설명		: 조회수 조회
     * @Method Name  	: selectBoardList
     * @date   			: 2023. 7. 07
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @param result
     * @return objRetParams
     * @throws TelewebApiException
     */
	TelewebJSON selectCnt(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
     * 
     * 메서드 설명		: 이미지 파일 그룹키 통일
     * @Method Name  	: increaseCnt
     * @date   			: 2023. 6. 16
     * @author   		: 김태준
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON fileKeyUnity(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
     * 
     * 메서드 설명		: 이미지 파일 리스트
     * @Method Name  	: increaseCnt
     * @date   			: 2023. 6. 16
     * @author   		: 김태준
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON chatImageList(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
     * 
     * 메서드 설명		: 이미지파일 단일 삭제
     * @Method Name  	: increaseCnt
     * @date   			: 2023. 6. 16
     * @author   		: 김태준
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON deleteChatImage(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 메서드 설명		: 파일 리스트 조회
     * @Method Name  	: boardFileList
     * @date   			: 2023. 6. 20
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON boardFileList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 메서드 설명		: 게시글파일 전체 삭제
     * @Method Name  	: deleteBoardFile
     * @date   			: 2023. 6. 16
     * @author   		: 김태준
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON deleteBoardFile(TelewebJSON jsonParams) throws TelewebAppException;

	
	
    /**
     * 
     * 메서드 설명		: 게시판 문의 조회(테스트)
     * @Method Name  	: pstQstnSelect
     * @date   			: 2023. 10. 05
     * @author   		: 김태준
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON pstQstnSelect(TelewebJSON jsonParams) throws TelewebAppException;
	
    /**
     * 
     * 메서드 설명		: 게시판 문의 답변(테스트)
     * @Method Name  	: pstQstnInsert
     * @date   			: 2023. 10.05
     * @author   		: 김태준
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON pstQstnInsert(TelewebJSON jsonParams) throws TelewebAppException;

	
}
