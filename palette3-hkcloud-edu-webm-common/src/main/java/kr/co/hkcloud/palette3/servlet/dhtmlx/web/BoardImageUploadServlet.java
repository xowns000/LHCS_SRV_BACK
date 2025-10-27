package kr.co.hkcloud.palette3.servlet.dhtmlx.web;


import javax.servlet.http.HttpServlet;

import lombok.extern.slf4j.Slf4j;


/**
 * 사용안함 - 20210623
 * 
 * @classDescription 파일업로드 처리
 * @version          1.0
 */
@Slf4j
//@WebServlet(urlPatterns = {"/servlet/BoardImageUploadServlet"})
public class BoardImageUploadServlet extends HttpServlet
{
//    /**
//     * 
//     */
//    private static final long serialVersionUID = -6769593364783891329L;
//    
//    private String FILE_MSG01 = "파일 업로드 처리시 다음과 같은 오류가 발생했습니다.";
//    private String FILE_MSG02 = "파일이 정상적으로 업로드 되었습니다.";
//    private String FILE_MSG03 = "파일 업로드시 FORM 객체속성을\nenctype=\"multipart/form-data\"로 설정해야 합니다.";
//    private String FILE_MSG04 = "업로드할 파일이 존재하지 않습니다.";
//    private String FILE_MSG05 = "이미지 확장자를 확인해주세요.(bmp, jpg, jpeg, png, gif)";
//    
//    @Value("${spring.http.encoding.charset}")
//    String strCharSet;		// 문자 타입 세팅
//    
//    @Autowired
//    private BbsProperties bbsProperties;
//    
//    @Autowired
//    private PaletteCmmnUtils       telewebUtil;
//    
//    @Override
//    public void init(ServletConfig config) throws ServletException
//    {
//        super.init(config);
//        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
//    }
//    /**
//     * 파일 업로드 POST 호출
//     */
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException
//    {
//
//        TelewebJSON objUploadFileInfo = new TelewebJSON();
//        boolean fileFlag = ServletFileUpload.isMultipartContent(req);
//
//        //업로드된 파일정보 저장처리 호출
//        if (fileFlag)
//        {
//            objUploadFileInfo = fileUpload(req, resp);
//        }
//        else
//        {
//            //멀티파트 타입을 정의하지 않음
//            objUploadFileInfo.setHeader("ERROR_FLAG", true);
//            objUploadFileInfo.setHeader("COUNT", 0);
//            objUploadFileInfo.setHeader("TOT_COUNT", 0);
//            objUploadFileInfo.setHeader("ERROR_MSG", FILE_MSG03);
//        }
//
//        //문자셋 세팅
//        String strRespType = req.getParameter("responseType");
//        if (!strRespType.equals("json"))
//        {
//            strRespType = "html";
//        }					//Drag and Drop 처리시 반환 값이 json 형태 그외 일반 처리는 html
//        resp.setContentType("text/" + strRespType + ";charset=" + strCharSet);	//반환문자의 타입을 정의한다.
//
//        //파일이 정상적으로 생성되었을 경우
//        if (!objUploadFileInfo.getHeaderBoolean("ERROR_FLAG"))
//        {
//
//            //처리결과 세팅
//            String funcNum = req.getParameter("CKEditorFuncNum");
//            String fileUrl = "/Repository/BBSIMG" + objUploadFileInfo.getString("PATH_DIR") + objUploadFileInfo.getString("FILENAME");
//            String strFilename = objUploadFileInfo.getString("FILENAME");
//
//            //CK에디터로 보내는 반환 값
//            if (strRespType.equals("json"))
//            {
//                //Drag and Drop 시에는 json 문자열로 반환
//                resp.getWriter().print("{\"uploaded\":\"1\", \"fileName\":\"" + strFilename + "\", \"url\":\"" + fileUrl + "\"}");
//
//            }
//            else
//            {
//                //일반 이미지 업로드 시에는 html javascript 형태로 반환
//                resp.getWriter().print("<script>window.parent.CKEDITOR.tools.callFunction(" + funcNum + ", '" + fileUrl + "', '정상적으로 처리되었습니다.');</script>");
//            }
//
//            //오류가 생겼을 경우
//        }
//        else
//        {
//
//            //CK에디터로 보내는 반환 값
//            if (strRespType.equals("json"))
//            {
//                //Drag and Drop 시에는 json 문자열로 반환
//                resp.getWriter().print("{\"uploaded\":\"1\", \"fileName\":\"" + objUploadFileInfo.getHeaderString("ERROR_MSG") + "\", \"url\":\"" + objUploadFileInfo.getHeaderString("ERROR_MSG") + "\"}");
//
//            }
//            else
//            {
//                //일반 이미지 업로드 시에는 html javascript 형태로 반환
//                resp.getWriter().print("<script>window.parent.CKEDITOR.tools.callFunction(1, '', '" + objUploadFileInfo.getHeaderString("ERROR_MSG") + "');</script>");
//            }
//        }
//    }
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
//        File strRepositoryTempDir = bbsProperties.getRepository().getDir();			//게시판 경로
//        String strUploadDir = req.getParameter("BRD_ID") + "_" + req.getParameter("BRD_NO");	//게시판 고유 폴더
//        String strTempFilePath = strRepositoryTempDir + "/" + strUploadDir;					//임시파일업로드 경로
//        String strFileName = "";															//업로드 파일명
//        String strRealFileName = "";														//실제 업로드되는 파일명
//        String strUserID = "";																//업로드 하는 유져
//        String strWorkGB = "";																//업로드 업무구분
//        int intFileSize = 0;																//파일사이즈
//        String strFileExt = "";																//업로드파일확장자
//        String strYear = telewebUtil.getYearString();
//        String strMonth = telewebUtil.getMonthString();
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
//
//        try
//        {
//            objListFiles = upload.parseRequest(req);
//            int intFileCnt = 0;
//            int intSaveCnt = 0;
//            if (objListFiles != null)
//            {
//                //파일객체정보와 필드정보를 분리한다.
//                @SuppressWarnings("rawtypes")
//                Iterator objIterator = objListFiles.iterator();
//                while (objIterator.hasNext())
//                {
//                    FileItem objFileItems = (FileItem) objIterator.next();
//                    if (objFileItems.isFormField())
//                    {
//                        //파일을 제외한 나머지 파라미터 처리
//                        if (objFileItems.getFieldName().equals("USER_ID"))
//                        {
//                            strUserID = objFileItems.getString();
//                        }
//                        else if (objFileItems.getFieldName().equals("WORK_GB"))
//                        {
//                            strWorkGB = objFileItems.getString();
//                        }
//                    }
//                    else
//                    {
//                        objHashFilesData.put(intFileCnt, objFileItems);
//                        intFileCnt++;
//                    }
//                }
//
//                //이미지 파일인지 체크
//                String strExtList[] =
//                { "bmp", "jpg", "jpeg", "png", "gif" };
//
//                for (int i = 0; i < objHashFilesData.size(); i++)
//                {
//                    strFileName = objHashFilesData.get(i).getName();//파일명
//                    int pos = strFileName.lastIndexOf(".");			//마지막 '.'의 위치
//                    strFileExt = strFileName.substring(pos + 1);	//확장자 추출
//                    strFileExt = strFileExt.toLowerCase();			//모든 문자를 소문자로 변경
//
//                    if (!Arrays.asList(strExtList).contains(strFileExt))
//                    {	//이미지 확장자가 아닐 경우
//                        //이미지 확장자가 아님
//                        objUploadFileInfo.setHeader("ERROR_FLAG", true);
//                        objUploadFileInfo.setHeader("COUNT", intSaveCnt);
//                        objUploadFileInfo.setHeader("TOT_COUNT", intSaveCnt);
//                        objUploadFileInfo.setHeader("ERROR_MSG", FILE_MSG05);
//                        return objUploadFileInfo;
//                    }
//                }
//
//                //저장될 디렉토리 전체 경로를 이용하여 디렉토리가 없을 경우 생성한다.
//                strTempFilePath = strTempFilePath + "/" + strYear + "/" + strMonth;
//                createDirectory(strTempFilePath);
//
//                //파일객체정보를 이용하여 업로드된 파일을 게시판이미지디렉토리에 저장한다.
//                for (int i = 0; i < objHashFilesData.size(); i++)
//                {
//                    intFileSize = 0;
//                    strFileName = objHashFilesData.get(i).getName();
//                    if (strFileName.equals(""))
//                    {
//                        continue;
//                    }
//                    strFileName = strFileName.substring(strFileName.lastIndexOf("\\") + 1);					//업로드된파일명
//                    intFileSize = (int) objHashFilesData.get(i).getSize();
//                    strFileExt = strFileName.substring(strFileName.lastIndexOf(".") + 1, strFileName.length());
//                    strRealFileName = telewebUtil.getTimeStampMilis() + "_" + strWorkGB + "_" + strUserID + "." + strFileName.substring(strFileName.lastIndexOf(".") + 1);		//실제저장보관될파일명
//
//                    try
//                    {
//                        //파일생성
//                        File objUploadFiles = new File(strTempFilePath, strRealFileName);
//                        objHashFilesData.get(i).write(objUploadFiles);
//                        intSaveCnt++;
//                    }
//                    catch (Exception e)
//                    {
//                        throw e;
//                    }
//
//                    //생성된 파일정보 반환
//                    objUploadFileInfo.setString("FILENAME_ORG", i, strFileName);
//                    objUploadFileInfo.setInt("FILE_SZ", i, intFileSize);
//                    objUploadFileInfo.setString("FILE_EXT", i, strFileExt);
//                    objUploadFileInfo.setString("ACCESS_CNT", i, "");
//                    objUploadFileInfo.setString("PATH_DIR", i, "/" + strUploadDir + "/" + strYear + "/" + strMonth + "/");	//중간디렉토리 패스명
//                    objUploadFileInfo.setString("FILENAME", i, strRealFileName);
//                    objUploadFileInfo.setString("INPUTNAME", i, objHashFilesData.get(i).getFieldName());					//해당 파일정보가 전송된 INPUT 박스ID 명
//                    objUploadFileInfo.setString("DATA_FLAG", i, "true");
//                }
//            }
//            if (intSaveCnt == 0)
//            {
//                //업로드할 파일이 없음
//                objUploadFileInfo.setHeader("ERROR_FLAG", true);
//                objUploadFileInfo.setHeader("COUNT", intSaveCnt);
//                objUploadFileInfo.setHeader("TOT_COUNT", intSaveCnt);
//                objUploadFileInfo.setHeader("ERROR_MSG", FILE_MSG04);
//            }
//            else
//            {
//                //정상적으로 업로드 됨
//                objUploadFileInfo.setHeader("ERROR_FLAG", false);
//                objUploadFileInfo.setHeader("COUNT", intSaveCnt);
//                objUploadFileInfo.setHeader("TOT_COUNT", intSaveCnt);
//                objUploadFileInfo.setHeader("ERROR_MSG", FILE_MSG02);
//            }
//        }
//        catch (Exception e)
//        {
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
//    /**
//     * 전체 경로를 이용하여 디렉토리를 생성한다.
//     * 
//     * @param  strPath   생성한 디렉토리의 전체 경로
//     * @return           true/false
//     * @throws Exception
//     */
//    private void createDirectory(String strPath) throws Exception
//    {
//        try
//        {
//            if (!strPath.endsWith(":\\"))
//            {
//                File objFile = new File(strPath);
//                if (!objFile.exists() && !objFile.isDirectory())
//                {
//                    objFile.mkdirs();
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            throw e;
//        }
//    }

}
