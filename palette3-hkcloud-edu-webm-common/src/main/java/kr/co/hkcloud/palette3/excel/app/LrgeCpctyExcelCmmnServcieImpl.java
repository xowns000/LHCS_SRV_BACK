package kr.co.hkcloud.palette3.excel.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 대용량 엑셀 공통 서비스 구현체
 * @author RND
 */
@Slf4j
@RequiredArgsConstructor
@Service("lrgeCpctyExcelCmmnServcie")
public class LrgeCpctyExcelCmmnServcieImpl implements LrgeCpctyExcelCmmnServcie
{
    private final static String REQUEST_LARGE_EXCEL_SUCC = "대용량 엑셀 요청이 처리되었습니다.\n\n처리결과는 '대용량 엑셀요청현황' 화면에서\n확인할 수 있습니다.";
    private final static String REQUEST_LARGE_EXCEL_FAIL = "대용량 엑셀 요청을 실패했습니다.\n다시 요청 바랍니다.";

    private final HcTeletalkEnvironment env;
    private final PaletteUtils          paletteCmmnUtils;
    private final TwbComDAO             twbComDAO;
    private final InnbCreatCmmnService  innbCreatCmmnService;


    /**
     * 대용량 엑셀파일을 요청하기 위해 관련 정보를 생성한다.
     * 
     * @param  jsonParams
     * @param  strExt
     * @param  strSQLStatement
     * @return                     TelewebJSON
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON requestLargeExcelDown(String strSqlNameSpace, String strSqlName, TelewebJSON jsonParams) throws TelewebAppException
    {
        //파일관련 정보를 생성한다.
        String strRepositoryPath = env.getString("REPOSITORY_PATH");
        String strYear = paletteCmmnUtils.getYearString();		//년도
        String strMonth = paletteCmmnUtils.getMonthString();	//월
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);			//반환 파라메터 생성

        //처리할 SQL문 구문을 생성한다.
        String strSQLStatement = twbComDAO.getSqlStatement(strSqlNameSpace, strSqlName, jsonParams);

        //공통파일정보에 파일정보를 등록한다.
        String strExt = jsonParams.getString("FILE_TYPE");			  						//파일타입(확장자명 csv, xlsx)
        String strWorkGB = jsonParams.getString("EXCEL_WORK_GB");								//업무구분

        String strFileGroupKey = innbCreatCmmnService.getSeqNo(jsonParams, "TWB");											//파일그룹키
        String strFileKey = innbCreatCmmnService.getSeqNo(jsonParams, "TWB");											//파일키

        String strUserID = jsonParams.getString("EXCEL_USER_ID");								//처리자
        String strUserDeptCode = jsonParams.getString("EXCEL_DEPT_CODE");								//부서코드
        String strFileName = jsonParams.getString("FILE_NM") + "." + strExt;						//파일명
        String strRealFileName = strFileKey + "_" + strWorkGB + "_" + strUserID + "_" + strFileName; 	//실제업로드명

        String strMiddlePath = "/ExcelDown/" + strWorkGB + "/" + strYear + "/" + strMonth + "/";	//상대경로

        String strFileType = jsonParams.getString("FILE_TYPE");									//파일타입
        String strSheetName = jsonParams.getString("SHEET_NM");										//시트명

        //[파일] fileCmmnUtils.createDirectory: 대용량엑셀 요청
        //폴더를 생성한다.
//        fileCmmnUtils.createDirectory(strRepositoryPath + strMiddlePath);

        jsonParams.setString("FILE_GROUP_KEY", strFileGroupKey);											//파일그룹키
        jsonParams.setString("FILE_KEY", strFileKey);												//파일키
//		jsonParams.setString("FILE_NAME" 		, strFileName);												//파일명
        jsonParams.setString("FILE_NM", strFileName);												//파일명
        jsonParams.setString("REAL_UPLOAD_NM", strRealFileName);											//실제업로드명
        jsonParams.setString("FILE_PATH", strMiddlePath);											//경로
        jsonParams.setString("WORK_GB", strWorkGB);												//업무구분
        jsonParams.setString("FILE_SZ", "0");														//파일사이즈
        jsonParams.setString("FILE_EXT", strExt);													//파일확장자
        jsonParams.setString("ACCESS_CNT", "0");														//접근회수(다운로드)
        jsonParams.setString("PROC_ID", strUserID);												//처리자
        jsonParams.setString("IT_PROCESSING", "SYSDATE");												//전산처리일시

        jsonParams.setString("MENU_ID", jsonParams.getString("EXCEL_MENU_ID"));								//메뉴ID [ 화면ID ]
        // 메뉴아이디가 없을경우 메뉴명을 사용함
        if("".equals(jsonParams.getString("EXCEL_MENU_ID"))) {
            jsonParams.setString("MENU_INFO", jsonParams.getString("EXCEL_MENU_NM"));						//메뉴명
        }

        jsonParams.setString("EXCEL_KEY", innbCreatCmmnService.getSeqNo(jsonParams, "EXC"));								//엑셀키
        jsonParams.setString("SHEET_KEY", "1");														//시트키(SHEET_KEY)
        jsonParams.setString("FILE_TYPE", strFileType);												//파일타입
        jsonParams.setString("USER_ID", strUserID);												//요청자 [ 사원코드 ]
        jsonParams.setString("WRTR_ID", strUserID);												//요청자 [ 사원코드 ]
        jsonParams.setString("WRTR_DEPT_CD", strUserDeptCode);											//요청자 [ 부서코드 ]
        jsonParams.setString("SHEET_INFO", strSheetName);											//시트명
        jsonParams.setString("DEPT_CODE", strUserDeptCode);											//부서코드
        jsonParams.setString("DATA_CNT", jsonParams.getString("DATA_CNT"));														//건수
        jsonParams.setString("EXECUT_QUERY", strSQLStatement);											//SQL문
        jsonParams.setString("ETC_INFO001", jsonParams.getString("COL_INFO"));						//비고1 - 모든파라메터정보(Excel 해더정보)
        jsonParams.setString("ETC_INFO002", "");														//비고2
        jsonParams.setString("ETC_INFO003", "");														//비고3
        jsonParams.setString("PROCESS_CODE", "10");													//상태코드를 요청중(01)으로 설정

        jsonParams.setBlankToNull(0);

        //[파일] 대용량엑셀 요청
        objRetParams = twbComDAO.insert("kr.co.hkcloud.palette3.common.file.dao.FileCmmnMapper", "INSERT_PLT_FILE", jsonParams);

        if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
            //엑셀파일정보에 파일정보를 등록한다.
            objRetParams = twbComDAO.insert("com.hcteletalk.teletalk.mng.excel.dao.Excel01Mapper", "INSERT_TWB_EXCEL01", jsonParams);
        }

        if(!objRetParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setHeader("ERROR_MSG", REQUEST_LARGE_EXCEL_SUCC);
        }
        else {
            objRetParams.setHeader("ERROR_MSG", REQUEST_LARGE_EXCEL_FAIL);
        }
        return objRetParams;
    }
}
