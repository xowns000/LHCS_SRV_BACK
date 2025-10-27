package kr.co.hkcloud.palette3.servlet.dhtmlx.web;


import lombok.extern.slf4j.Slf4j;


/**
 * 사용안함 - 20210623
 * 
 * @classDescription 파일다운로드 처리
 * @version          1.0
 */
@Slf4j
//@WebServlet(urlPatterns = {"/servlet/FileDownloadServlet"})
public class FileDownloadServlet //extends HttpServlet
{
//    /**
//     * 
//     */
//    private static final long serialVersionUID = -3109678072503046841L;
//
//    @Autowired
//    private PaletteUtils paletteUtil;
//
//    @Autowired
//    private FileCmmnService fileCmmnService;
//
//    @Autowired
//    private FileCmmnUtils fileCmmnUtils;
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
//    private final String IMG_LIST   = "GIF,JPG,PNG,BMP,PCX";
//    private final String DOC_LIST   = "PDF";
//    private final String FILE_MSG01 = "요청하신 파일키(그룹키:{0},파일키:{1})는 존재하지만,<br />키에 해당하는 파일이 없습니다.<br /><br />파일키 데이터를 확인하세요!";
//    private final String FILE_MSG02 = "요청하신 파일키(그룹키:{0},파일키:{1})에<br />해당하는 데이터가 없습니다.<br /><br />파일키를 확인하세요!";
//    private final String FILE_MSG03 = "요청하신 파일({0})이 존재하지 않습니다.<br /><br />파일명과 경로를 확인하세요!";
//    private final String FILE_MSG04 = "필수 파라메터가 없습니다.<br />다운받은 파일의 파라메터를 확인하세요!";
//    private final String FILE_MSG05 = "이미지 파일이 아닙니다.<br />이미지확장자(GIF,JPG,PNG,BMP,PCX)<br />파일만 뷰어를 통해 볼 수 있습니다.";
//    private final String FILE_MSG06 = "문서 파일이 아닙니다.<br />문서확장자(PDF)<br />파일만 뷰어를 통해 볼 수 있습니다.";
//
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException
//    {
//        try {
//            writeStreamFile(req, resp);
//        }
//        catch(Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException
//    {
//        try {
//            writeStreamFile(req, resp);
//        }
//        catch(Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//
//    /**
//     * 전송된 파라메터를 이용하여 파일을 다운로드 한다. downFileKey가 전송될 경우 파일키를 이용하여 다운로드 처리를 한다. downFileName가 전송될 경우 상대디렉토리와 파일명을 이용하여 다운로드 처리를 한다.
//     * 
//     * @param  req       Request 객체
//     * @param  resp      Response 객체
//     * @throws Exception
//     * @author           MPC R&D Team
//     */
//    private void writeStreamFile(HttpServletRequest req, HttpServletResponse resp) throws Exception
//    {
//        TelewebJSON jsonParams = new TelewebJSON();
//
//        String strFileGroupKey = req.getParameter("downFileGroupKey") == null ? "" : req.getParameter("downFileGroupKey");		//파일그룹키
//        String strFileKey = req.getParameter("downFileKey") == null ? "" : req.getParameter("downFileKey");		//파일키
//        //String strFileName = req.getParameter("downFileName") == null ? "" : new String(req.getParameter("downFileName").getBytes("euc-kr"), "8859_1"); //req.getParameter("downFileName");	//파일패스와이름
//        String strFileName = req.getParameter("downFileName") == null ? "" : req.getParameter("downFileName"); //파일패스와이름
//        String strDownCase = req.getParameter("downCase") == null ? "DOWN" : req.getParameter("downCase");		//다운구분(DOWN, IMG_VIEW, PDF_VIEW)
//        String strWorkGB = req.getParameter("downWorkGB") == null ? "" : req.getParameter("downWorkGB"); //업무구분
//        //String strRepositoryPath = telewebProperty.getString("REPOSITORY_PATH");			//Repository Path
//        //String strRepositoryPath = paletteProperties.getRepository().getRootDir().toString() + "/" + strWorkGB;				//업무별 Repository Path
//        String strRepositoryPath = "";				//업무별 Repository Path
//        String telIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);
//        String strErrorMsg = "";
//        String strKeyFileName = "";
//        String strFileExt = "";
//        File objFile = null;
//
//        String strTargetType = paletteProperties.getRepository().getTrgtTypeCd().toString();						//파일 업로드 타입 (FILE / BLOB)
//        byte[] byteFileBlob = null;
//
//        strRepositoryPath = fileCmmnUtils.getWorkGbFilePath(strWorkGB, "dir");
//
//        if(!"".equals(strFileGroupKey) && !"".equals(strFileKey)) {
//            HttpSession objSession = req.getSession();
//            String aspCustInfo = (String) objSession.getAttribute("ASP_CUST_INFO");
//
//            jsonParams.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
//            jsonParams.setString("FILE_GROUP_KEY", strFileGroupKey);
//            jsonParams.setString("FILE_KEY", strFileKey);
//            jsonParams.setString("CUSTCO_ID", aspCustInfo);
//
//            if(!"BLOB".equals(strTargetType)) {
//
//                //파일키로 처리할 경우
//                TelewebJSON jsonFile = fileCmmnService.selectFileAttach(jsonParams, strFileGroupKey, strFileKey);     //파일키를 이용하여 파일정보를 검색한다.
//                if(jsonFile.getHeaderInt("COUNT") == 1) {
//
//                    strKeyFileName = jsonFile.getString("FILE_NM");
//                    objFile = new File(strRepositoryPath + "/" + jsonFile.getString("FILE_PATH") + "/" + jsonFile.getString("REAL_UPLOAD_NM")); //jsonFile.getString("REAL_UPLOAD_NM"));
//
//                    if(!objFile.exists()) {
//                        strErrorMsg = paletteUtil.getMessageFormet(FILE_MSG01, strFileGroupKey, strFileKey);
//                    }
//                    else {
//                        strFileExt = strKeyFileName.substring(strKeyFileName.lastIndexOf(".") + 1).toUpperCase();
//                        if(strDownCase.equals("IMG_VIEW")) {
//                            //이미지 파일여부 체크
//                            if(IMG_LIST.indexOf(strFileExt) == -1) {
//                                strErrorMsg = FILE_MSG05;
//                            }
//                        }
//                        else if(strDownCase.equals("PDF_VIEW")) {
//                            //PDF 파일여부 체크
//                            if(DOC_LIST.indexOf(strFileExt) == -1) {
//                                strErrorMsg = FILE_MSG06;
//                            }
//                        }
//                    }
//
//                }
//                else {
//                    strErrorMsg = paletteUtil.getMessageFormet(FILE_MSG02, strFileGroupKey, strFileKey);
//                }
//
//            }
//            else {
//
//                //파일키로 처리할 경우
//                FileCmmnVO fileCmmnVOtmp = fileCmmnService.selectFileAttachBlob(jsonParams);     //파일키를 이용하여 파일정보를 검색한다.
//                if(fileCmmnVOtmp.getFileBlob() != null) {
//
//                    strKeyFileName = fileCmmnVOtmp.getFileNm();
//                    byteFileBlob = fileCmmnVOtmp.getFileBlob();
//
//                    if(byteFileBlob == null) {
//                        strErrorMsg = paletteUtil.getMessageFormet(FILE_MSG01, strFileGroupKey, strFileKey);
//                    }
//                    else {
//                        strFileExt = fileCmmnVOtmp.getFileExtn().toUpperCase();
//                        if(strDownCase.equals("IMG_VIEW")) {
//                            //이미지 파일여부 체크
//                            if(IMG_LIST.indexOf(strFileExt) == -1) {
//                                strErrorMsg = FILE_MSG05;
//                            }
//                        }
//                        else if(strDownCase.equals("PDF_VIEW")) {
//                            //PDF 파일여부 체크
//                            if(DOC_LIST.indexOf(strFileExt) == -1) {
//                                strErrorMsg = FILE_MSG06;
//                            }
//                        }
//                    }
//
//                }
//                else {
//                    strErrorMsg = paletteUtil.getMessageFormet(FILE_MSG02, strFileGroupKey, strFileKey);
//                }
//
//            }
//
//        }
//        else if(!strFileName.equals("")) {
//            //파일명과 패스로 처리할 경우
//            objFile = new File(strRepositoryPath + strFileName);
//            if(!objFile.exists()) {
//                strErrorMsg = paletteUtil.getMessageFormet(FILE_MSG03, strFileName, "");
//            }
//            else {
//                strFileExt = strFileName.substring(strFileName.lastIndexOf(".") + 1).toUpperCase();
//                if(strDownCase.equals("IMG_VIEW")) {
//                    //이미지 파일여부 체크
//                    if(IMG_LIST.indexOf(strFileExt) == -1) {
//                        strErrorMsg = FILE_MSG05;
//                    }
//                }
//                else if(strDownCase.equals("PDF_VIEW")) {
//                    //PDF 파일여부 체크
//                    if(DOC_LIST.indexOf(strFileExt) == -1) {
//                        strErrorMsg = FILE_MSG06;
//                    }
//                }
//            }
//        }
//        else {
//            strErrorMsg = FILE_MSG04;
//        }
//
//        //오류가 없을 경우 파일다운로드 처리를 한다.
//        if(strErrorMsg.equals("")) {
//            //다운일 경우만 파일회수 증가
//            if(strDownCase.equals("DOWN")) {
//                //파일다운로드회수 증가 처리
//                fileCmmnService.updateFileDownCnt(jsonParams, strFileGroupKey, strFileKey);
//            }
//
//            String strDownFileName = strKeyFileName;
//            if("".equals(strFileGroupKey) || "".equals(strFileKey)) {
//                strDownFileName = strFileName.substring(strFileName.lastIndexOf("/") + 1);
//            }
//
//            //브라우저별로 한글 엔코딩 타입을 정의한다.
//            String strBrowser = req.getHeader("User-Agent");
//            if(strBrowser.contains("MSIE") || strBrowser.contains("Trident") || strBrowser.contains("Chrome")) {
//                strDownFileName = URLEncoder.encode(strDownFileName.replaceAll(" ", "_"), StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
//            }
//            else {
//                strDownFileName = new String(strDownFileName.getBytes(StandardCharsets.UTF_8), "ISO-8859-1");
//            }
//
//            //파일을 Write한다.
//            if(!"BLOB".equals(strTargetType)) {
//                writeFile(strDownFileName, resp, objFile);
//            }
//            else {
//                writeFile(strDownFileName, resp, byteFileBlob);
//            }
//        }
//        else {
//            //오류가 존재할 경우 오류확인 화면으로 오류내용을 전송한다.
//            if(strDownCase.equals("IMG_VIEW")) {
//                //오류 관련 관련 이미지를 링크해서 보여준다
//                //17.06.08일 새로운 구성에 맞추어 변경함 아래 구성에 맞추어 변경함. hy-cho
//                //String strDefaultImgPath = req.getServletContext().getRealPath("/") + "html/img/ui/img_blank02.png";
//                String strDefaultImgPath = req.getSession().getServletContext().getRealPath("/") + "html/img/ui/img_blank01.png";
//                if(strErrorMsg.equals(FILE_MSG05)) {
//                    strDefaultImgPath = req.getSession().getServletContext().getRealPath("/") + "html/img/ui/img_blank01.png";
//                }
//                //파일을 Write한다.
//                writeFile("defaultImg", resp, new File(strDefaultImgPath));
//            }
//            else if(strDownCase.equals("PDF_VIEW")) {
//                //오류 관련 관련 PDF를 링크해서 보여준다
//                String strDefaultPdfPath = req.getSession().getServletContext().getRealPath("/") + "html/doc/pdf/pdf_blank02.pdf";
//                //if(strErrorMsg.equals(FILE_MSG06)){strDefaultPdfPath = req.getServletContext().getRealPath("/") + "html/doc/pdf/pdf_blank01.pdf";}
//                if(strErrorMsg.equals(FILE_MSG06)) {
//                    strDefaultPdfPath = req.getSession().getServletContext().getRealPath("/") + "html/doc/pdf/pdf_blank01.pdf";
//                }
//                //파일을 Write한다.
//                writeFile("defaultPdf", resp, new File(strDefaultPdfPath));
//            }
//            else {
//
//                strErrorMsg = new String(Base64.encodeBase64(strErrorMsg.getBytes()));
//
//                resp.sendRedirect("/web/error/excp05?ERR_MSG=" + strErrorMsg);
//            }
//        }
//
//    }
//
//
//    private void writeFile(String strDownFileName, HttpServletResponse resp, File objFile) throws Exception
//    {
//        log.debug("strDownFileName : {}", strDownFileName);
//
//        FileInputStream objFileInStream = null;
//        ServletOutputStream objFileOutStream = null;
//        try {
//            resp.reset();
//            resp.setContentType("application/octet-stream");
//            resp.setHeader("Content-Disposition", "attachment; filename=" + strDownFileName);
//            objFileInStream = new FileInputStream(objFile);
//            objFileOutStream = resp.getOutputStream();
//            int leng = 0;
//            byte bytes[] = new byte[(int) objFile.length()];
//            while((leng = objFileInStream.read(bytes)) > 0) {
//                objFileOutStream.write(bytes, 0, leng);
//            }
//        }
//        catch(Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        finally {
//            if(objFileInStream != null) {
//                objFileInStream.close();
//            }
//            if(objFileOutStream != null) {
//                objFileOutStream.close();
//            }
//            if(objFile != null) {
//                objFile = null;
//            }
//        }
//    }
//
//
//    private void writeFile(String strDownFileName, HttpServletResponse resp, byte[] byteFileBlob) throws Exception
//    {
//        log.debug("strDownFileName : {}", strDownFileName);
//
//        ByteArrayInputStream bin = null;
//        ServletOutputStream objFileOutStream = null;
//        try {
//            resp.reset();
//            resp.setContentType("application/octet-stream");
//            resp.setHeader("Content-Disposition", "attachment; filename=" + strDownFileName);
//            bin = new ByteArrayInputStream(byteFileBlob);
//            objFileOutStream = resp.getOutputStream();
//            int leng = 0;
//            while((leng = bin.read(byteFileBlob)) > 0) {
//                objFileOutStream.write(byteFileBlob, 0, leng);
//            }
//        }
//        catch(Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        finally {
//            if(bin != null) {
//                bin.close();
//            }
//            if(objFileOutStream != null) {
//                objFileOutStream.close();
//            }
//            if(byteFileBlob != null) {
//                byteFileBlob = null;
//            }
//        }
//    }
}
