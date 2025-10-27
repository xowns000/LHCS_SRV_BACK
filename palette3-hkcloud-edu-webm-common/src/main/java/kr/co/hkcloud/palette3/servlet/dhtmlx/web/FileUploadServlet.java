package kr.co.hkcloud.palette3.servlet.dhtmlx.web;


import lombok.extern.slf4j.Slf4j;


/**
 * * 사용안함 - 20210623
 * 
 * @classDescription 파일업로드 처리
 * @version          1.0
 */
@Slf4j
//@WebServlet(urlPatterns = {"/servlet/FileUploadServlet"})
public class FileUploadServlet //extends HttpServlet
{
//    /**
//     * 
//     */
//    private static final long serialVersionUID = 4845807500185534849L;
//
//    @Autowired
//    private PaletteUtils telewebUtil;
//
//    @Autowired
//    private TwbComBiz objComBiz;
//
//    @Autowired
//    private FileCmmnUtils fileCmmnUtils;
//
//    @Autowired
//    private FileCmmnMapper fileCmmnMapper;
//
//
//    @Override
//    public void init(ServletConfig config) throws ServletException
//    {
//        super.init(config);
//        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
//    }
//
//
//    @Autowired
//    private PaletteProperties paletteProperties;
//
//    private String FILE_MSG01 = "파일 업로드 처리시 다음과 같은 오류가 발생했습니다.";
//    private String FILE_MSG02 = "파일이 정상적으로 업로드 되었습니다.";
//    private String FILE_MSG03 = "파일 업로드시 FORM 객체속성을\nenctype=\"multipart/form-data\"로 설정해야 합니다.";
//    private String FILE_MSG04 = "업로드할 파일이 존재하지 않습니다.";
//
//
//    /**
//     * 파일 업로드 POST 호출
//     */
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException
//    {
//        String strCharSet = "UTF-8";
//
//        //반환문자의 타입을 정의한다.
//        resp.setContentType("text/json;charset=" + strCharSet);
//
//        TelewebJSON objUploadFileInfo = new TelewebJSON();
//        //String contentType = req.getContentType();
//        boolean fileFlag = ServletFileUpload.isMultipartContent(req);
//        //업로드된 파일정보 저장처리 호출
//        if(fileFlag) {
//            objUploadFileInfo = fileUpload(req, resp);
//        }
//        else {
//            //멀티파트 타입을 정의하지 않음
//            objUploadFileInfo.setHeader("ERROR_FLAG", true);
//            objUploadFileInfo.setHeader("COUNT", 0);
//            objUploadFileInfo.setHeader("TOT_COUNT", 0);
//            objUploadFileInfo.setHeader("ERROR_MSG", FILE_MSG03);
//        }
//
//        //처리결과 반환
//        resp.getWriter().print(objUploadFileInfo.toString());
//    }
//
//
//    /**
//     * 업로드된 파일정보를 이용하여 해당 위치에 파일을 생성하고 결과정보를 생성하여 반환한다.
//     * 
//     * @param  req  HttpServletRequest 객체
//     * @param  resp HttpServletResponse 객체
//     * @return      TelewebJSON 형식의 업로드된 파일 정보
//     * @author      MPC R&D Team
//     */
//    public TelewebJSON fileUpload(HttpServletRequest req, HttpServletResponse resp)
//    {
//        String strRepositoryTempDir = "";
//        String strUploadDir = "";
//        String strTargetType = paletteProperties.getRepository().getTrgtTypeCd().toString();	//파일 업로드 경로 (REPOSITORY / BLOB)
//        String strTempFilePath = "";					//임시파일업로드 경로
//        String strFileName = "";															//업로드 파일명
//        String strRealFileName = "";														//실제 업로드되는 파일명
//        String custcoId = "";                                                             //ASP 고객 키
//        String strUserID = "";																//업로드 하는 유져
//        String strRegDeptCode = "";															//업로드 하는 유저 부서코드
//        String strWorkGB = "";																//업로드 업무구분
//        //String strWorkGB = req.getParameter("workGb");
//        String strWorkKey = "";																//업로드 업무고유키(시퀀스)
//
//        String strFileGroupKey = "";                                                        //업로드 파일그룹키
//        int intFileSize = 0;																//파일사이즈
//        String strFileExt = "";																//업로드파일확장자
//        String strYear = telewebUtil.getYearString();
//        String strMonth = telewebUtil.getMonthString();
//        String strDay = telewebUtil.getDayString();
//        TelewebJSON objUploadFileInfo = new TelewebJSON();
//
//        HashMap<Integer, FileItem> objHashFilesData = new HashMap<>();
//        File objTempDir = new File(strTempFilePath);
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//
//        factory.setSizeThreshold(1024 * 1024 * 1000);
//        factory.setRepository(objTempDir);
//        ServletFileUpload upload = new ServletFileUpload(factory);
//
//        List<FileItem> objListFiles = null;
////        String kageFileUrl = "";
////        int kageFileSize = 0;
////        String KageAccessKey = "";
//
//        try {
//            objListFiles = upload.parseRequest(req);
//            int intFileCnt = 0;
//            int intSaveCnt = 0;
//            if(objListFiles != null) {
//                //파일객체정보와 필드정보를 분리한다.
//                @SuppressWarnings("rawtypes")
//                Iterator objIterator = objListFiles.iterator();
//                while(objIterator.hasNext()) {
//                    FileItem objFileItems = (FileItem) objIterator.next();
//                    if(objFileItems.isFormField()) {
//                        //파일을 제외한 나머지 파라미터 처리
//                        if(objFileItems.getFieldName().equals("USER_ID")) {
//                            strUserID = objFileItems.getString();
//                        }
//                        else if(objFileItems.getFieldName().equals("CUSTCO_ID")) {
//                            custcoId = objFileItems.getString();
//                        }
//                        else if(objFileItems.getFieldName().equals("WRTR_DRPT_CD")) {
//                            strRegDeptCode = objFileItems.getString();
//                        }
//                        else if(objFileItems.getFieldName().equals("FILE_GROUP_KEY")) {
//                            strFileGroupKey = objFileItems.getString();
//                        }
//                        else if(objFileItems.getFieldName().equals("WORK_GB_KEY")) {
//                            strWorkKey = objFileItems.getString();
//                        }
//                        else if(objFileItems.getFieldName().equals("WORK_GB")) {
//                            strWorkGB = objFileItems.getString();
//                        }
//                    }
//                    else {
//                        objHashFilesData.put(intFileCnt, objFileItems);
//                        intFileCnt++;
//                    }
//                }
//
//                strRepositoryTempDir = fileCmmnUtils.getWorkGbFilePath(strWorkGB, "tempDir");
//                strUploadDir = fileCmmnUtils.getWorkGbFilePath(strWorkGB, "uploadDir");
//
//                strTempFilePath = strRepositoryTempDir;					//임시파일업로드 경로
//
//                //저장될 디렉토리 전체 경로를 이용하여 디렉토리가 없을 경우 생성한다.
//                //strTempFilePath = strTempFilePath + "/" + strYear + "/" + strMonth + "/" + strDay + "/" + strWorkKey;
//                strTempFilePath = strTempFilePath + "/" + strYear + "/" + strMonth + "/" + strDay;
//                if(!"BLOB".equals(strTargetType)) {
//                    createDirectory(strTempFilePath);
//                }
//
//                //파일객체정보를 이용하여 업로드된 파일을 템플디렉토리에 저장한다.
//                for(int i = 0; i < objHashFilesData.size(); i++) {
//                    intFileSize = 0;
//                    strFileName = objHashFilesData.get(i).getName();
//                    if(strFileName.equals("")) {
//                        continue;
//                    }
//                    strFileName = strFileName.substring(strFileName.lastIndexOf("\\") + 1);                   //업로드된파일명
//                    intFileSize = (int) objHashFilesData.get(i).getSize();
//                    strFileExt = strFileName.substring(strFileName.lastIndexOf(".") + 1, strFileName.length());
//                    strRealFileName = telewebUtil.getTimeStampMilis() + "_" + strWorkGB + "_" + strUserID + "_" + UUID.randomUUID().toString() + "." + strFileName.substring(strFileName.lastIndexOf(".") + 1);       //실제저장보관될파일명
//
//                    if("BLOB".equals(strTargetType)) {
//                        byte[] file_blob = objHashFilesData.get(i).get();
//                        intSaveCnt++;
//
//                        TelewebJSON objJsonParams = new TelewebJSON();
//
//                        //파일그룹키 생성
//                        strFileGroupKey = "".equals(strFileGroupKey) ? objComBiz.getSeqNo(objJsonParams, "PLT") : strFileGroupKey;
//
//                        //파일키 생성
//                        String strFileKey = objComBiz.getSeqNo(objJsonParams, "PLT");
//
//                        String[] objFileName;				//'_'로 분리된 파일이름 정보[0]:YYYYMMDDHH24MISSMSC, [1]:WORK_GB, [2]:USER_ID, [3]:실제파일명
//
//                        //String strWorkKeyPath = "";			//첨부파일 폴더(업무별 업무고유키(시퀀스)
//                        //String strMiddlePath = "";			//Repository에 저장될 파일의 중간 패스정보
//
//                        objFileName = strRealFileName.split("_");
//                        //strWorkKeyPath  = "/" + strWorkKey;
//                        //strMiddlePath = strWorkKeyPath + "/" + objFileName[1] + "/" + objFileName[0].substring(0, 4) + "/" + objFileName[0].substring(4, 6) + "/";
//
//                        //공통파일테이블에 저장
//                        FileCmmnVO fileCmmnVO = new FileCmmnVO();
//                        fileCmmnVO.setCustcoId(custcoId);
//                        fileCmmnVO.setFileGroupKey(strFileGroupKey);
//                        fileCmmnVO.setFileKey(strFileKey);
//                        fileCmmnVO.setFileNm(strFileName);
//                        fileCmmnVO.setRealUploadNm(strRealFileName);
//                        //fileCmmnVO.setFileNm(strRealFileName);
//                        //fileCmmnVO.setRealUploadNm(strFileName);
//                        // fileCmmnVO.setFilePath(strMiddlePath);
//                        fileCmmnVO.setTaskTypeCd(objFileName[1]);
//                        fileCmmnVO.setFileSize(intFileSize);
//                        fileCmmnVO.setFileExts(strFileName.substring(strFileName.lastIndexOf(".") + 1));
//                        fileCmmnVO.setDnlodCnt(0);
//                        fileCmmnVO.setFileBlob(file_blob);
//                        fileCmmnVO.setWrtrId(strUserID);
//                        fileCmmnVO.setWrtrDeptCd(strRegDeptCode);
//                        fileCmmnVO.setProcId(strUserID);
//                        fileCmmnVO.setFileSaveStat("TEMP");
//                        fileCmmnMapper.insertFileCmmnVO(fileCmmnVO);
//
//                        //생성된 파일키 정보 추가
//                        //objUploadFileInfo.setString("FILE_GROUP_KEY", i, strFileGroupKey);
//                        objUploadFileInfo.setString("FILE_KEY", i, strFileKey);
//
//                    }
//                    else {
//                        try {
//                            //파일생성
//                            File objUploadFiles = new File(strTempFilePath, strRealFileName);
//                            objHashFilesData.get(i).write(objUploadFiles);
//                            intSaveCnt++;
//
//                            //파일그룹키 생성
//                            strFileGroupKey = "".equals(strFileGroupKey) ? objComBiz.getSeqNo(new TelewebJSON(), "PLT") : strFileGroupKey;
//
//                        }
//                        catch(Exception e) {
//                            throw e;
//                        }
//                    }
//
//                    //생성된 파일정보 반환
//                    objUploadFileInfo.setString("FILENAME_ORG", i, strFileName);
//                    objUploadFileInfo.setInt("FILE_SZ", i, intFileSize);
//                    objUploadFileInfo.setString("FILE_EXT", i, strFileExt);
//                    objUploadFileInfo.setString("ACCESS_CNT", i, "");
//                    //objUploadFileInfo.setString("PATH_DIR", i, "/" + strUploadDir + "/" + strYear + "/" + strMonth+ "/");   //중간디렉토리 패스명
//                    objUploadFileInfo.setString("PATH_DIR", i, strTempFilePath.replaceAll("\\\\", "/"));   //중간디렉토리 패스명
//                    objUploadFileInfo.setString("FILENAME", i, strRealFileName);
//                    objUploadFileInfo.setString("INPUTNAME", i, objHashFilesData.get(i).getFieldName());                    //해당 파일정보가 전송된 INPUT 박스ID 명
//                    objUploadFileInfo.setString("DATA_FLAG", i, "true");
//                    objUploadFileInfo.setString("FILE_GROUP_KEY", i, strFileGroupKey);
//                }
//            }
//            if(intSaveCnt == 0) {
//                //업로드할 파일이 없음
//                objUploadFileInfo.setHeader("ERROR_FLAG", true);
//                objUploadFileInfo.setHeader("COUNT", intSaveCnt);
//                objUploadFileInfo.setHeader("TOT_COUNT", intSaveCnt);
//                objUploadFileInfo.setHeader("ERROR_MSG", FILE_MSG04);
//            }
//            else {
//                //정상적으로 업로드 됨
//                objUploadFileInfo.setHeader("ERROR_FLAG", false);
//                objUploadFileInfo.setHeader("COUNT", intSaveCnt);
//                objUploadFileInfo.setHeader("TOT_COUNT", intSaveCnt);
//                objUploadFileInfo.setHeader("ERROR_MSG", FILE_MSG02);
//            }
//        }
//        catch(Exception e) {
//            log.error(e.getMessage(), e);
//
//            //업로드 시 시스템 오류가 발생함
//            objUploadFileInfo.setHeader("ERROR_FLAG", true);
//            objUploadFileInfo.setHeader("COUNT", 0);
//            objUploadFileInfo.setHeader("TOT_COUNT", 0);
//            objUploadFileInfo.setHeader("ERROR_MSG", FILE_MSG01 + "\n" + e.getMessage());
//        }
//        return objUploadFileInfo;
//    }
//
//
//    /**
//     * 전체 경로를 이용하여 디렉토리를 생성한다.
//     * 
//     * @param  strPath   생성한 디렉토리의 전체 경로
//     * @return           true/false
//     * @throws Exception
//     */
//    private void createDirectory(String strPath) throws Exception
//    {
//        try {
//            if(!strPath.endsWith(":\\")) {
//                File objFile = new File(strPath);
//                if(!objFile.exists() && !objFile.isDirectory()) {
//                    objFile.mkdirs();
//                }
//            }
//        }
//        catch(Exception e) {
//            throw e;
//        }
//    }

}
