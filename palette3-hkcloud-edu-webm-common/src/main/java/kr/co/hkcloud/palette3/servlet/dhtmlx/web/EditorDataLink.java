package kr.co.hkcloud.palette3.servlet.dhtmlx.web;


import javax.servlet.http.HttpServlet;

import lombok.extern.slf4j.Slf4j;


/**
 * 사용안함 - 20210623
 * 
 * @classDescription 웹에디터를 통해 등록된 파일에 대해서 수정 및 View 모드에서 해당 파일을 연결시켜주는 서블릿
 * @version          1.0
 */
@Slf4j
//@WebServlet(urlPatterns = {"/servlet/EditorDataLink"})
public class EditorDataLink extends HttpServlet
{
//    /**
//     * 
//     */
//    private static final long serialVersionUID = -5490753682089697183L;
//
//    private final String IMG_LIST   = "GIF,JPG,PNG,BMP,PCX";
//    private final String DOC_LIST   = "PDF";
//    private final String FILE_MSG01 = "요청하신 파일키({0})는 존재하지만,<br />키에 해당하는 파일이 없습니다.<br /><br />파일키 데이터를 확인하세요!";
//    private final String FILE_MSG02 = "요청하신 파일키({0})에<br />해당하는 데이터가 없습니다.<br /><br />파일키를 확인하세요!";
//    private final String FILE_MSG03 = "요청하신 파일({0})이 존재하지 않습니다.<br /><br />파일명과 경로를 확인하세요!";
//    private final String FILE_MSG04 = "필수 파라메터가 없습니다.<br />다운받은 파일의 파라메터를 확인하세요!";
//    private final String FILE_MSG05 = "이미지 파일이 아닙니다.<br />이미지확장자(GIF,JPG,PNG,BMP,PCX)<br />파일만 뷰어를 통해 볼 수 있습니다.";
//    private final String FILE_MSG06 = "문서 파일이 아닙니다.<br />문서확장자(PDF)<br />파일만 뷰어를 통해 볼 수 있습니다.";
//
//    @Autowired
//    private PaletteProperties paletteProperties;
//
//    @Autowired
//    private PaletteUtils telewebUtil;
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
//
//        String strErrorMsg = "";
//        File objFile = null;
//        String strKeySeq = req.getParameter("KeySeq") == null ? "" : req.getParameter("KeySeq");			//파일패스와이름
//        String strFileName = req.getParameter("FILE") == null ? "" : req.getParameter("FILE");			//파일명
//
//        //필수파라메터가 없을 경우 처리
//        if(strKeySeq.equals("") && strFileName.equals("")) {
//            //오류 관련 관련 이미지를 링크해서 보여준다
//            String strDefaultImgPath = req.getServletContext().getRealPath("/") + "html/img/ui/img_blank01.png";
//            //파일을 Write한다.
//            writeFile("defaultImg", resp, new File(strDefaultImgPath));
//            return;
//        }
//
//        //디렉토리정보 추출
//        File strRepositoryPath = paletteProperties.getRepository().getRootDir();								//Repository Path
//
//        String strExt = strFileName.substring(strFileName.lastIndexOf(".") + 1).toUpperCase();
//        String strBizCase = strKeySeq.substring(17, 21);			//업무구분
//        String strYear = strKeySeq.substring(0, 4);				//년도
//        String strMonth = strKeySeq.substring(4, 6);				//버전
//        String strContentKey = strKeySeq.substring(0, 26);		//컨텐츠키
//
//        //소스코드보안취약점 보완 적용(경로 조작 및 자원 삽입)
//        String strFullPath = "/" + strBizCase + "/" + strYear + "/" + strMonth + "/" + strContentKey + "/FILES/" + strFileName;
//
//        try {
//            //파일명과 패스로 처리할 경우
//            objFile = new File(strRepositoryPath + strFullPath);
//
//            if(!objFile.exists()) {
//                strErrorMsg = telewebUtil.getMessageFormet(FILE_MSG03, strFileName, "");
//            }
//
//            //오류가 없을 경우 파일다운로드 처리를 한다.
//            if(strErrorMsg.equals("")) {
//                //브라우저별로 한글 엔코딩 타입을 정의한다.
//                String strBrowser = req.getHeader("User-Agent");
//                if(strBrowser.contains("MSIE") || strBrowser.contains("Trident") || strBrowser.contains("Chrome")) {
//                    strFileName = URLEncoder.encode(strFileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
//                }
//                else {
//                    strFileName = new String(strFileName.getBytes(StandardCharsets.UTF_8), "ISO-8859-1");
//                }
//                //파일을 Write한다.
//                writeFile(strFileName, resp, objFile);
//            }
//        }
//        catch(Exception e) {
//            log.error(e.getMessage(), e);
//
//            if(e.getMessage() != null) {
//                strErrorMsg = e.getMessage().replaceAll("\n", "<br />");
//            }
//        }
//        finally {
//            //오류가 존재할 경우 오류확인 화면으로 오류내용을 전송한다.
//            if(!strErrorMsg.equals("")) {
//
//                //이미지일 경우
//                if(IMG_LIST.indexOf(strExt) >= 0) {
//                    String strDefaultImgPath = req.getServletContext().getRealPath("/") + "html/img/ui/img_blank01.png";
//                    writeFile("defaultImg", resp, new File(strDefaultImgPath));
//                }
//
//            }
//        }
//    }
//
//
//    private void writeFile(String strDownFileName, HttpServletResponse resp, File objFile) throws Exception
//    {
//        FileInputStream objFileInStream = null;
//        ServletOutputStream objFileOutStream = null;
//        try {
//            resp.reset();
//            resp.setContentType("application/octet-stream");
//            resp.setHeader("Content-Disposition", "attachment; filename=" + strDownFileName);
//            objFileInStream = new FileInputStream(objFile);
//            //소스코드보안취약점 보완 적용(널(Null) 포인터 역참조)
//            if(objFileInStream != null) {
//                objFileOutStream = resp.getOutputStream();
//                //소스코드보안취약점 보완 적용(널(Null) 포인터 역참조)
//                if(objFileOutStream != null) {
//                    int leng = 0;
//                    byte bytes[] = new byte[(int) objFile.length()];
//                    while((leng = objFileInStream.read(bytes)) > 0) {
//                        objFileOutStream.write(bytes, 0, leng);
//                    }
//                }
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

}
