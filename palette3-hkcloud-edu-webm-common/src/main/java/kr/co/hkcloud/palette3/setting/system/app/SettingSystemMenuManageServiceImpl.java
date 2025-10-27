package kr.co.hkcloud.palette3.setting.system.app;


import java.sql.SQLException;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 메뉴관리 서비스 인터페이스 구현체
 *
 * @author R&D
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("settingSystemMenuManageService")
public class SettingSystemMenuManageServiceImpl implements SettingSystemMenuManageService {

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;

    /**
     * 설정시스템메뉴관리 트리목록을 조회한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnMenuTree(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "selectRtnMenuTree", jsonParams);
    }


    /**
     * 설정시스템메뉴관리 그룹목록을 조회한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnMenuGroup(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "selectRtnMenuGroup", jsonParams);
    }


    /**
     * 설정시스템메뉴관리 ID중복체크한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectDupMenuID(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "selectDupMenuID", jsonParams);
    }


    /**
     * 설정시스템메뉴관리 URL경로로 정보를 조회한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectMenuIdByMenuPath(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "selectMenuIdByMenuPath",
            jsonParams);
    }


    /**
     * 설정시스템메뉴관리 목록을 조회한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectTwbBas04(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "SELECT_PLT_MENU", jsonParams);
    }


    /**
     * 설정시스템메뉴관리 게시판유형을 조회한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectBoardPath(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "selectBoardPath", jsonParams);
    }


    /**
     * 설정시스템메뉴관리 게시판유형을 조회한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectBoardPathDetail(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "selectBoardPathDetail",
            jsonParams);
    }


    /**
     * 설정시스템메뉴관리 메뉴를 등록한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON processRtnMenu(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        String menuId = jsonParams.getString("MENU_ID");
        if (menuId.equals("")) {
            menuId = Integer.toString(innbCreatCmmnService.createSeqNo("MENU_ID"));
            jsonParams.setString("MENU_ID", menuId);
            //추가
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "INSERT_PLT_MENU",
                jsonParams);
        } else {
            //수정
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "updateRtnMenu",
                jsonParams);
        }
        objRetParams.setString("MENU_ID", menuId);

        return objRetParams;
    }

    /**
     * 메뉴 순서 변경
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(readOnly = false)
    public TelewebJSON changeMenuOrder(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "changingMenuOrder",
            jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "changedMenuOrder",
            jsonParams);
        return objRetParams;
    }


    /**
     * 설정시스템메뉴관리 게시판유형을 조회한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON deleteRtnMenu(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "deleteRtnMenu",
            jsonParams); //메뉴 삭제처리
    }


    @Transactional
    public TelewebJSON deleteRtnBtnByMeneId(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        String MENU_ID = jsonParams.getString("LOWRNK_MENU_ID");
        if (StringUtils.isEmpty(MENU_ID)) {
            throw new IllegalArgumentException("LOWRNK_MENU_ID is necessary");
        }

        TelewebJSON jsonSelect = new TelewebJSON(jsonParams);
        jsonSelect.setString("MENU_ID", jsonParams.getString("LOWRNK_MENU_ID"));

        TelewebJSON jsonBtnId = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper",
            "selectRtnBtnIdByMenuId", jsonSelect);
        for (int idx = 0, iTimes = jsonBtnId.getHeaderInt("TOT_COUNT"); idx < iTimes; ++idx) {
            TelewebJSON jsonDelete = new TelewebJSON(jsonParams);
            jsonDelete.setString("BTN_ID", jsonBtnId.getString("BTN_ID", idx));

            objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "deleteRtnBtnByMeneId",
                jsonDelete);
        }

        return objRetParams;
    }


    @Transactional
    private TelewebJSON deleteRtnAuthBtnByMeneId(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        String MENU_ID = jsonParams.getString("LOWRNK_MENU_ID");

        if (StringUtils.isEmpty(MENU_ID)) {
            throw new IllegalArgumentException("LOWRNK_MENU_ID is necessary");
        }

        TelewebJSON jsonSelect = new TelewebJSON(jsonParams);
        jsonSelect.setString("MENU_ID", jsonParams.getString("LOWRNK_MENU_ID"));

        TelewebJSON jsonBtnId = mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper",
            "selectRtnAuthBtnIdByMenuId", jsonSelect);
        for (int idx = 0, iTimes = jsonBtnId.getHeaderInt("TOT_COUNT"); idx < iTimes; ++idx) {
            TelewebJSON jsonDelete = new TelewebJSON(jsonParams);
            jsonDelete.setString("ATRT_GROUP_ID", jsonBtnId.getString("ATRT_GROUP_ID", idx));
            jsonDelete.setString("MENU_ID", jsonBtnId.getString("MENU_ID", idx));
            jsonDelete.setString("BTN_ID", jsonBtnId.getString("BTN_ID", idx));

            objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper",
                "deleteRtnAuthBtnByMeneId", jsonDelete);
        }

        return objRetParams;
    }


    /**
     * 게시판메뉴관리 버튼을 삭제한다
     *
     * @author R&D
     * @Transactional Auto Commit
     * @return TelewebJSON 형식의 처리 결과 데이터
     * @since 2021.03.23
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON deleteRtnBtn(TelewebJSON jsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonMapper", "DELETE_BTN",
            jsonParams);                        //BTN(버튼자원)
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuButtonAuthorityMapper",
            "DELETE_AUTH_BTN_BYBTN", jsonParams);    //AUTH_BTN(버튼권한)

        return objRetParams;
    }

    @Transactional(readOnly = true)
    @Override
    public TelewebJSON selectLkagLayoutList(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemMenuManageMapper", "selectLkagLayoutList", jsonParams);
    }

    //    /**
    //     * 게시판메뉴관리 입력받은 경로의 하위 모든 폴더정보를 리턴한다
    //     *
    //     * @author        R&D
    //     * @Transactional Auto Commit
    //     * @return        TelewebJSON 형식의 처리 결과 데이터
    //     * @since         2021.03.23
    //     */
    //    public TelewebJSON selectRtnFolderInfo(TelewebJSON jsonParams) throws TelewebAppException
    //    {
    //        //반환 파라메터 생성
    //        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
    //
    //        String folderKind = jsonParams.getString("FOLDER_KIND");
    //
    //        jsonFolder = new TelewebJSON(jsonParams);
    //        jsonFolderIndex = 0;
    //
    //        if(folderKind.equals("ui")) {
    //            subDirList(getFolderPath(folderKind), 2, "");
    //        }
    //        else {
    //            subDirList(getFolderPath(folderKind), 1, "");
    //        }
    //        jsonFolder.setHeader("COUNT", jsonFolderIndex);
    //
    //        objRetParams = jsonFolder;
    //
    //        return objRetParams;
    //    }

    //    /**
    //     * 디렉토리 정보를 가지고온다.
    //     *
    //     * @param  path
    //     * @param  level
    //     * @throws TelewebAppException
    //     */
    //    private void subDirList(String path, int level, String parentPath) throws TelewebAppException
    //    {
    //        File dir = new File(path);
    //        //소스코드보안취약점 보완 적용(널(Null) 포인터 역참조)
    //        if(dir != null) {
    //            File[] fileList = dir.listFiles();
    //            //소스코드보안취약점 보완 적용(널(Null) 포인터 역참조)
    //            if(fileList != null) {
    //                int tmpLvl = level;
    //                for(int i = 0; i < fileList.length; i++) {
    //                    File file = fileList[i];
    //                    if(file.isDirectory()) {
    //                        jsonFolder.setString("P_NODE_NO", jsonFolderIndex, parentPath);
    //                        jsonFolder.setString("C_NODE_NO", jsonFolderIndex, file.getCanonicalPath().toString()
    //                            .replace("\\", "/") + "/");
    //                        jsonFolder.setString("NODE_ID", jsonFolderIndex, file.getCanonicalPath().toString()
    //                            .replace("\\", "/") + "/");
    //                        jsonFolder.setString("NODE_KEY", jsonFolderIndex, file.getCanonicalPath().toString()
    //                            .replace("\\", "/") + "/");
    //                        jsonFolder.setString("NODE_TITLE", jsonFolderIndex, file.getName());
    //                        jsonFolder.setInt("NODE_LVL", jsonFolderIndex, tmpLvl);
    //                        jsonFolder.setInt("NODE_SORT", jsonFolderIndex, (i + 1));
    //                        jsonFolder.setString("NODE_TYPE", jsonFolderIndex, "D");
    //                        jsonFolderIndex++;
    //                        subDirList(file.getCanonicalPath()
    //                            .toString(), (tmpLvl + 1), file.getCanonicalPath().toString().replace("\\", "/") + "/");
    //                    }
    //                }
    //            }
    //        }
    //    }

    //    /**
    //     * 각 구분별로 경로를 리턴한다.
    //     *
    //     * @param  folder
    //     * @return
    //     * @throws TelewebAppException
    //     */
    //    private String getFolderPath(String folder) throws TelewebAppException
    //    {
    //
    //        // 서버의 절대경로를 가지고온다.
    //        String strPath = batchProperties.getAppDir().toString();
    //
    //        if(folder.equals("SQL")) {
    //            strPath = strPath + "/WEB-INF/xml/sql/";
    //        }
    //        else if(folder.equals("xml")) {
    //            strPath = strPath + "/WEB-INF/xml/";
    //        }
    //        else if(folder.equals("ui")) {
    //            strPath = strPath + "/webapps/";
    //            jsonFolder.setString("P_NODE_NO", jsonFolderIndex, "");
    //            jsonFolder.setString("C_NODE_NO", jsonFolderIndex, strPath);
    //            jsonFolder.setString("NODE_ID", jsonFolderIndex, strPath);
    //            jsonFolder.setString("NODE_KEY", jsonFolderIndex, strPath);
    //            jsonFolder.setString("NODE_TITLE", jsonFolderIndex, "webapps");
    //            jsonFolder.setInt("NODE_LVL", jsonFolderIndex, 1);
    //            jsonFolder.setInt("NODE_SORT", jsonFolderIndex, 1);
    //            jsonFolder.setString("NODE_TYPE", jsonFolderIndex, "D");
    //            jsonFolderIndex++;
    //        }
    //        else if(folder.equals("js")) {
    //            strPath = strPath + "/html/js/";
    //        }
    //        else if(folder.equals("img")) {
    //            strPath = strPath + "/common/img/";
    //        }
    //        else if(folder.equals("thema")) {
    //            strPath = strPath + "/webapps/include/thema/";
    //        }
    //        else if(folder.equals("pattern")) {
    //            strPath = strPath + "/TwbPattern/jsp/";
    //        }
    //        else if(folder.equals("pattern_js")) {
    //            strPath = strPath + "/TwbPattern/js/";
    //        }
    //        else if(folder.equals("class")) {
    //            strPath = strPath + "/src";
    //        }
    //        else if(folder.equals("class_pattern")) {
    //            strPath = strPath + "/TwbPattern/java";
    //        }
    //        else if(folder.equals("sql_pattern")) {
    //            strPath = strPath + "/TwbPattern/sql";
    //        }
    //        else if(folder.equals("pattern_img")) {
    //            strPath = strPath + "/common/img/pattern/jsp/";
    //        }
    //        else if(folder.equals("pattern_img_root")) {
    //            strPath = strPath + "/common/img/";
    //        }
    //
    //        return strPath;
    //    }

    //    /**
    //     * 설정시스템메뉴관리 폴더목록을 조회한다
    //     *
    //     * @author        R&D
    //     * @Transactional Auto Commit
    //     * @return        TelewebJSON 형식의 처리 결과 데이터
    //     * @since         2021.03.23
    //     */
    //    public TelewebJSON selectRtnFileList(TelewebJSON jsonParams) throws TelewebAppException
    //    {
    //        //반환 파라메터 생성
    //        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
    //
    //        String folderPath = jsonParams.getString("FOLDER_PATH");   //파일검색할 폴더경로
    //        objRetParams = getFileList(folderPath, jsonParams);
    //
    //        return objRetParams;
    //    }

    //    /**
    //     * 입력받은 경로의 파일리스트를 조회한다.
    //     *
    //     * @param  folderPath
    //     * @return
    //     * @throws TelewebAppException
    //     */
    //    private TelewebJSON getFileList(String folderPath, TelewebJSON mjsonParams) throws TelewebAppException
    //    {
    //        TelewebJSON retJson = new TelewebJSON(mjsonParams);
    //        File dir = new File(folderPath);
    //
    //        if(dir.exists()) {
    //            File[] fileList = dir.listFiles();
    //            int index = 0;
    //            //소스코드보안취약점 보완 적용(널(Null) 포인터 역참조)
    //            if(fileList != null) {
    //                String strFileName = "";
    //                String strFilePath = "";
    //                String strImgExt = "JPG|PNG|GIF|BMP";
    //                String strExt = "";
    //                for(int i = 0; i < fileList.length; i++) {
    //                    File file = fileList[i];
    //                    //소스코드보안취약점 보완 적용(널(Null) 포인터 역참조)
    //                    if(file != null) {
    //                        if(file.isFile()) {
    //                            strFileName = file.getName();
    //                            if(strFileName != null) {
    //                                strExt = strFileName.substring(strFileName.lastIndexOf(".") + 1);
    //                                strFilePath = file.getAbsolutePath().toString().substring(40).replace("\\", "/");
    //                                strFilePath = file.getAbsolutePath();
    //                                strFilePath = strFilePath.substring(strFilePath.indexOf("kaom_TELETALKWeb") + 16)
    //                                    .replace("\\", "/");
    //
    //                                retJson.setString("FILE_NAME", index, strFileName);
    //                                retJson.setString("PATH_NM", index, strFilePath);
    //                                if(strImgExt.indexOf(strExt.toUpperCase()) >= 0) {
    //                                    //이미지 일 경우 미리보기 기능을 제공하기 위해 이미지 태그를 생성한다.
    //                                    retJson
    //                                        .setString("IMG_VIEW", index, "<img src='" + strFilePath + "' style='width:40px;height:20px;' />");
    //                                }
    //                                else {
    //                                    retJson.setString("IMG_VIEW", index, "-");
    //                                }
    //                                index++;
    //                            }
    //                        }
    //                    }
    //                }
    //            }
    //            retJson.setHeader("TOT_COUNT", index);
    //            retJson.setHeader("COUNT", index);
    //            retJson.setHeader("ERROR_FLAG", false);
    //            retJson.setHeader("ERROR_MSG", "정상조회되었습니다!");
    //        }
    //        else {
    //            retJson.setHeader("ERROR_FLAG", true);
    //            retJson.setHeader("ERROR_MSG", "해당 경로가 존재하지 않습니다!");
    //        }
    //
    //        return retJson;
    //    }
}
