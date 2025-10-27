
package kr.co.hkcloud.palette3.common.phone.util;



import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

/**
 * palette.common.phone.util.StringUtil 사용여부 체크 필요
 * @author leeiy
 *
 */
public class StringUtil{
 
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    
    /**
     *  파라미터 스트링이 null 이 아니고, "" 이 아니면 true, 아니면 false
     *
     *  @param param        검사 문자열
     *
     *  @return 검사결과
     */
    public static boolean isNotNull(String param){
        if(param != null && "".equals(param) == false) return true;
        else return false;
    }

    
    /**********************************************************
    * 8859_1 에서 EUC-KR 로 인코딩 한다.
    * @param str 원본 String
    * @return String 변환된 내용 
    **********************************************************/
    public static String toKOR(String str){
        String rstr=null;
        try{
            rstr=(str==null)?"":new String(str.getBytes("8859_1"),"euc-kr");
        }catch(java.io.UnsupportedEncodingException e){ }        
        return rstr;
    }

    /********************************************************
    * EUC-KR 에서 8859_1 로 인코딩 한다.
    * @param str 원본 String
    * @return String 변환된 내용 
    *********************************************************/
    public static String toUSA(String str){
        String rstr=null;
        try{
            rstr=(str==null)?"":new String(str.getBytes("euc-kr"),"8859_1");
        }catch(java.io.UnsupportedEncodingException e) {}
        return rstr;
    }
    
    //-----------------------------------------------------------------------------
    //  ASCII을 한글캐릭터셋으로 변환
    //-----------------------------------------------------------------------------
    public static String    ksc2asc(String str) {
        String  result              =   "";
            try {
                result              =   new String(str.getBytes("KSC5601"),"8859_1");
                result              =   new String(str.getBytes("KSC5601"),"UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                
                result              =   str;
            }
        
        return  result;
    }

    /********************************************************
    * null 값을 "" 공백 문자로 바꿔준다.
    * @param : String pm_sContent;
    * @return : String type pm_sContent;
    ********************************************************/
    public String replaceNull(String pm_sContent){
        if(pm_sContent == null) {
            return "";
        }
        return pm_sContent;
    }


    /**********************************************************
    * 제한된 길이를 초과하면 뒷부분을 짜르고 "..." 으로 대치한다.
    * @param : String title(문자열)
    * @param : int max( 보여줄 문자열 수)
    * @return : String title.substring(0,max-3)+"..."
    **********************************************************/
    public static String formatTitle(String title,int max){
        if (title==null){
            return null;
        }

        if (title.length() <= max){
            return title;
        }else{
            return title.substring(0,max-3)+"...";
        }
    }

    /***************************************************************
    * 문자열중에 원하는 문자를 제거한다.
    * @param : String c_str(입력 받을 문자열)
    * @param : String c_type(제거할 문자)
    * @return : String str
    ***************************************************************/
    public String cut_String(String c_str,String c_type) {
        String str = c_str;
        StringTokenizer st = new StringTokenizer(str, c_type, false);
        str = "";
        while(st.hasMoreTokens()) {
            str += st.nextToken();
        }
        return str;
    }

    /**
     *  스트링 치환 함수
     *  
     *  주어진 문자열(buffer)에서 특정문자열('src')를 찾아 특정문자열('dst')로 치환
     *
     */
    public static String ReplaceAll(String buffer, String src, String dst){
        if(buffer == null) return null;
        if(buffer.indexOf(src) < 0) return buffer;
        
        int bufLen = buffer.length();
        int srcLen = src.length();
        StringBuffer result = new StringBuffer();

        int i = 0; 
        int j = 0;
        for(; i < bufLen; ){
            j = buffer.indexOf(src, j);
            if(j >= 0) {
                result.append(buffer.substring(i, j));
                result.append(dst);
                
                j += srcLen;
                i = j;
            }else break;
        }
        result.append(buffer.substring(i));
        return result.toString();
    }

    /***************************************************************
    * Null을 ""로 변환
    ***************************************************************/
    public String checkNull(String str) {
        String strTmp;
        if (str == null){
            strTmp = "";
        }else{
            strTmp = str.trim();
        }
        return strTmp;
    }

    /***************************************************************
    * Null을 0로 변환
    ***************************************************************/
    public String checkNull2(String str) {
        String strTmp;
        if (str == null){
            strTmp = "0";
        }else{
            strTmp = str;
        }
        return strTmp;
    }


    /***************************************************************
    * 공백을 &nbsp;로 변환
    ****************************************************************/
    public String null2nbsp(String str) {
        String strTmp;
        if (str == null || str.equals("")){
            strTmp = "&nbsp;";
        }else{
            strTmp = str;
        }
        return strTmp;
    }

    /***************************************************************
    * &lt; 를 &amp;lt; 로 변경, 
    * &gt; 를 &amp;gt; 로 변경, 
    * 공백을 &amp;nbsp; 로 변경
    * @param contents 내용
    * @return String 변환된 내용
    ***************************************************************/
    public static String toHtmlTag(String contents) {
        int len = contents.length();
        int linenum = 0, i = 0;
        for (i = 0; i < len; i++){
            if ((contents.charAt(i) == '<') | (contents.charAt(i) == '>') | (contents.charAt(i) == '&') | (contents.charAt(i) == ' ')){
                linenum++;
            }
        }

        StringBuffer result = new StringBuffer(len + linenum * 5);
        for (i = 0; i < len; i++) {
            if (contents.charAt(i) == '<') {
                result.append("&lt;");
            }else if (contents.charAt(i) == '>') {
                result.append("&gt;");
            }else if (contents.charAt(i) == '&') {
                result.append("&amp;");
            }else if (contents.charAt(i) == ' ') {
                if (i == 0) {
                    result.append("&nbsp;");
                }else {
                    if (contents.substring(i - 1, i).equals(" ") | contents.substring(i - 1, i).equals("\n")) {
                        if (contents.substring(i + 1, i + 2).equals(" ")) {
                            result.append("&nbsp;");
                        }else {
                            if (contents.substring(i - 2, i - 1).equals(" ")) {
                                result.append(" ");
                            }else {
                                result.append("&nbsp;");
                            }
                        }
                    } else {
                        result.append(" ");
                    }
                }
            } else {
                result.append(contents.charAt(i));
            }
        }
        return result.toString();
    }     
      
    /***************************************************************
    * <  ==>  &#60; 로, 
    * >  ==>  &#62; 로, 
    * "  ==>  &#34; 로,
    * &  ==>  &amp; 로,
    * '  ==>  &#39; 로 치환;
    * @param contents 내용
    * @param subject     제목 == html tag 안에 뿌려지는 값을 넣을땐 위와 같이 변환시켜서 DB에 넣어줘야 함.
    * @return String 변환된 내용
    ****************************************************************/
    public static String toHtmlTag01(String contents) {
        int len = contents.length();
        int linenum = 0, i = 0;
        for (i = 0; i < len; i++){
            if ((contents.charAt(i) == '<') | (contents.charAt(i) == '>') | (contents.charAt(i) == '\'') | (contents.charAt(i) == '\"') | (contents.charAt(i) == '&') | (contents.charAt(i) == ' ')){
                linenum++;
            }
        }

        StringBuffer result = new StringBuffer(len + linenum * 5);

        for (i = 0; i < len; i++) {
            if (contents.charAt(i) == '<') {
                result.append("&#60;");
            } else if (contents.charAt(i) == '>') {
                result.append("&#62;");
            } else if (contents.charAt(i) == '\'') {
                result.append("&#39;");
            } else if (contents.charAt(i) == '\"') {
                result.append("&#34;");
            }else if (contents.charAt(i) == '&') {
                result.append("&amp;"); 
            }else {
                result.append(contents.charAt(i));
            }
        }
        return result.toString();
    }     
      
    /********************************************************************************
    * <  ==  &#60; 로, 
    * >  ==  &#62; 로, 
    * "  ==  &#34; 로,
    * &  ==  &amp; 로,
    * '  ==  &#39; 로 치환;
    *
    * @param contents 내용
    * @return String 변환된 내용
    ********************************************************************************/
    public static String origin_toHtmlTag01(String contents) {
        String str = contents;
        String origin_str = "";
        origin_str = replace(str, "&#60;", "<");
        origin_str = replace(origin_str, "&#62;", ">");
        origin_str = replace(origin_str, "&#34;", "\"");
        origin_str = replace(origin_str, "&#39;", "\'");
        origin_str = replace(origin_str, "&amp;", "&");
        return origin_str;
    }
      
      
    /********************************************************************************
    * 엔터값을 &lt;BR&gt; 로 바꿔준다.
    * @param contents 내용
    * @return String 변환된 내용
    *********************************************************************************/
    public static String toBR(String contents) {
        int len = contents.length();
        int linenum = 0, i = 0;
        for (i = 0; i < len; i++){
            if (contents.charAt(i) == '\n'){
                linenum++;
            }
        }

        StringBuffer result = new StringBuffer(len + linenum * 3);
        for (i = 0; i < len; i++) {
            if (contents.charAt(i) == '\n'){
                result.append("<br>");
            }else{
                result.append(contents.charAt(i));
            }
        }
        return result.toString();
    }     

    /********************************************************************************
    * 엔터값을 &lt;BR&gt; 로 바꿔준다.
    * @param contents 내용
    * @return String 변환된 내용
    *********************************************************************************/
    public static String toBR01(String contents) {
        int len = contents.length();
        int linenum = 0, i = 0;
        for (i = 0; i < len; i++){
            if (contents.charAt(i) == '\n' || contents.charAt(i) == '\r'){
                linenum++;
            }
        }

        StringBuffer result = new StringBuffer(len + linenum * 3);
        for (i = 0; i < len; i++) {
            if (contents.charAt(i) == '\n' || contents.charAt(i) == '\r'){
                result.append(" ");
            }else{
                result.append(contents.charAt(i));
            }
        }
        return result.toString();
    }     
      
    /**********************************************************************************
    * 게시판의 답변글 작성시 원문인용시에 사용되는 메소드로 각줄의 맨 앞에 &gt;를 붙여 준다.
    * @param contents 내용
    * @return String 변환된 내용
    *********************************************************************************/
    public static String reply(String contents) {
        int len = contents.length();
        int linenum = 0, i = 0;
        for (i = 0; i < len; i++){
            if (contents.charAt(i) == '\n'){
                linenum++;
            }
        }
        StringBuffer result = new StringBuffer(len + linenum + 1);
        result.append("> ");

        for (i = 0; i < len; i++) {
            if (contents.charAt(i) == '\n'){
                result.append("> ");
            }else{
                result.append(contents.charAt(i));
            }
        }
        return result.toString();
    }   
    

    /**
     * 문자열을 Form의 Input Text에 삽입할때 문자 변환
     * @param str
     * @return
     */
    public static String    fn_html_text_convert (String str) {
        if (str == null || str.equals("")) {
            return  "";
        }
        else {
            char    schr1           =   '\'';
            char    schr2           =   '\"';
            char    schr3           =   '<';
            char    schr4           =   '>';
            char    schr5           =   '&';
            StringBuffer    sb      =   new StringBuffer(str);
            int     idx_str         =   0;
            int     edx_str         =   0;

            while (idx_str >= 0) {
                idx_str             =   str.indexOf(schr1, edx_str);
                if (idx_str < 0) {
                    break;
                }
                str                 =   sb.replace(idx_str, idx_str+1, "&#39;").toString();
                edx_str             =   idx_str + 5;
            }

            sb                      =   new StringBuffer(str);
            idx_str                 =   0;
            edx_str                 =   0;
            while (idx_str >= 0) {
                idx_str             =   str.indexOf(schr2, edx_str);
                if (idx_str < 0) {
                    break;
                }
                str                 =   sb.replace(idx_str, idx_str+1, "&quot;").toString();
                edx_str             =   idx_str + 6;
            }
            
            sb                      =   new StringBuffer(str);
            idx_str                 =   0;
            edx_str                 =   0;
            while (idx_str >= 0) {
                idx_str             =   str.indexOf(schr3, edx_str);
                if (idx_str < 0) {
                    break;
                }
                str                 =   sb.replace(idx_str, idx_str+1, "&lt;").toString();
                edx_str             =   idx_str + 4;
            }
            sb                      =   new StringBuffer(str);
            idx_str                 =   0;
            edx_str                 =   0;
            while (idx_str >= 0) {
                idx_str             =   str.indexOf(schr4, edx_str);
                if (idx_str < 0) {
                    break;
                }
                str                 =   sb.replace(idx_str, idx_str+1, "&gt;").toString();
                edx_str             =   idx_str + 4;
            }
            
            sb                      =   new StringBuffer(str);
            idx_str                 =   0;
            edx_str                 =   0;
            while (idx_str >= 0) {
                idx_str             =   str.indexOf(schr5, edx_str);
                if (idx_str < 0) {
                    break;
                }
                str                 =   sb.replace(idx_str, idx_str+1, "&amp").toString();
                edx_str             =   idx_str + 4;
            }

            return  str;
        }
    }
    /**
     * 문자열을 Form의 Input Text에 삽입할때 문자 변환
     * @param str
     * @return
     */
    public static String    fn_input_text (String str) {
        if (str == null || str.equals("")) {
            return  "";
        }
        else {
            char    schr1           =   '\'';
            char    schr2           =   '\"';
            StringBuffer    sb      =   new StringBuffer(str);
            int     idx_str         =   0;
            int     edx_str         =   0;

            while (idx_str >= 0) {
                idx_str             =   str.indexOf(schr1, edx_str);
                if (idx_str < 0) {
                    break;
                }
                str                 =   sb.replace(idx_str, idx_str+1, "&#39;").toString();
                edx_str             =   idx_str + 5;
            }

            sb                      =   new StringBuffer(str);
            idx_str                 =   0;
            edx_str                 =   0;
            while (idx_str >= 0) {
                idx_str             =   str.indexOf(schr2, edx_str);
                if (idx_str < 0) {
                    break;
                }
                str                 =   sb.replace(idx_str, idx_str+1, "&quot;").toString();
                edx_str             =   idx_str + 6;
            }
        

            return  str;
        }
    }


    public static String getHtmlContents(String src)
    {
        src = ReplaceAll(src, "\n", "<br>");
        src = ReplaceAll(src, "&quot;", "\"");
        return src;
    }

    /**
     * 데이타를 구분자로 나누어 배열로 리턴 
     * @param str
     * @param sepe_str
     * @return
     */
    public static String[] split(String str, String sepe_str) {
        int     index               =   0;
        String[] result             =   new String[search(str,sepe_str)+1];
        String  strCheck            =   new String(str);
        while (strCheck.length() != 0) {
            int     begin           =   strCheck.indexOf(sepe_str);
            if (begin == -1) {
                result[index]       =   strCheck;
                break;
            } 
            else {
                int end             =   begin + sepe_str.length();
                if(true) {
                    result[index++] =   strCheck.substring(0, begin);
                }
                strCheck            =   strCheck.substring(end);
                if(strCheck.length()==0 && true) {
                    result[index]   =   strCheck;
                    break;
                }
            }
        }
        return result;
    }

    public static int search(String strTarget, String strSearch) {
        int     result              =   0;
        String  strCheck            =   new String(strTarget);
        for(int i = 0; i < strTarget.length();) {
            int     loc             =   strCheck.indexOf(strSearch);
            if(loc == -1) {
                break;
            } 
            else {
                result++;
                i                   =   loc + strSearch.length();
                strCheck            =   strCheck.substring(i);
            }
        }
        return result;  
    }
     
       
    /********************************************************************************
    * 문자열의 특정 패턴을 원하는 패턴으로 바꾼다.
    * @param str 원본 String
    * @param pattern 원본 패턴
    * @param replace 원본 패턴에 대치될 패턴
    * @return String 변환된 내용 
    *********************************************************************************/
public static String replace(String str, String pattern, String replace){
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = str.indexOf(pattern, s)) >= 0){
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    /********************************************************************************
    * 주민번호에 '-'가 있는 경우 '-'를 없이 반환하며 숫자만 있는 경우 '-'를 넣어준다.    
    * @param jumin 주민번호 String
    *********************************************************************************/
    public static String ConvJuminNo(String jumin) {
        String result = "";
        if(jumin.length()== 13) {
            result = jumin.substring(0,6) + "-" + jumin.substring(6);
        }else if(jumin.length() == 14) {
            result = replace(jumin, "-", "");
        }else{
            result = jumin;
        }
        return result;
    }

    /********************************************************************************
    * 주민번호에 '-'가 있는 경우 '-'를 없이 반환한다 
    * @param jumin 주민번호 String
    *********************************************************************************/
    public static String ConvJuminNo01(String jumin) {
        String result = "";
        if(jumin != null){
            if(jumin.length()== 14) {
                jumin = replace(jumin, "-", "");
            }
        }
        return jumin;
    }


    /********************************************************************************
    * 숫자값에 3자리마다 콤마를 찍는다.   
    * @param strNum 숫자 String
    *********************************************************************************/
    public String Comma_Conv(String strNum ) { 

        int i = 0; 
        int j = 0; 
        int mok = 0; 
        int div = 0; 
        int size = strNum.length(); 
        String str = ""; 

        if ( strNum == null || strNum == "" ) 
            str = "0";

        if ( size <= 3 ) 
            str = strNum;

        div = size / 3; 
        mok = size % 3; 
        str = strNum.substring( 0, mok ); 

        for (i = 0, j = 1; i < div; i++ ) { 
            if ( i == 0 && mok == 0 ) 
                str = strNum.substring( (i * 3) + mok, (i * 3) + mok + 3 );  
            else 
                str = str + "," + strNum.substring( (i * 3) + mok, (i * 3) + mok + 3 );  
        } 
        return str; 
    }

    

      // 현재 게시판의 페이지 인덱스 설정
    public String indexList(int current_page, int total_page, String list_url) {
        int pagenumber;     // 화면에 보여질 페이지 인덱스 수
        int startpage;      // 화면에 보여질 시작페이지 번호
        int endpage;        // 화면에 보여질 마지막페이지 번호
        int curpage;        // 이동하고자 하는 페이지 번호
        String strList="";  // 리턴될 페이지 인덱스 리스트

        pagenumber = 10;    // 한 화면의 페이지 인덱스 수 

        // 시작 페이지번호 구하기
        startpage = ((current_page - 1) / pagenumber) * pagenumber + 1;
        // 마지막 페이지번호 구하기
        endpage = (((startpage - 1) +  pagenumber) / pagenumber) * pagenumber;

        // 총 페이지 수가 계산된 마지막페이지 번호보다 작을경우 
        // 총 페이지 수가 마지막페이지 번호가 됨
        if (total_page <= endpage)
        {
            endpage = total_page;
        }

        // 첫번째 페이지 인덱스 화면이 아닌경우
        if ( current_page > pagenumber) {
            curpage = startpage - 1;    // 시작페이지 번호보다 1 적은 페이지로 이동
            strList = strList + "<a href='"+list_url+"current_page="+curpage+"'><img src=../../../JNSHOME/images/bu_pre.gif  height= border=0></a>";
        }else{
            strList = strList+"<img src=../../../JNSHOME/images/bbs/bu_pre.gif  height= border=0>";
        }
        //strList = strList + " ... ";

        // 시작페이지 번호부터 마지막페이지 번호까지 화면에 표시
        curpage = startpage;
        while (curpage <= endpage){
            if (curpage == current_page) {
                strList = strList + "["+current_page+"]";
            } else {
                strList = strList +"<a href='"+list_url+"current_page="+curpage+"'>["+curpage+"]</a>";
            }
            curpage++;
        }
        //strList = strList + " ... ";
     
        // 뒤에 페이지가 더 있는경우
        if ( total_page > endpage) {
            curpage = endpage + 1;  
            strList = strList + "<a href='"+list_url+"current_page="+curpage+"'><img src=../../../JNSHOME/images/bbs/bu_next.gif  height= border=0></a>";
        }else{
            strList = strList+"<img src=../../../JNSHOME/images/bu_next.gif  height= border=0>";
        }

        return strList;
    }

    /*******************************************************
     * 전화번호자리를 체크하여 전화번호 사이에 -를 붙여준다.
     * RETURN TYPE : STRING Tel;
    *******************************************************/
    public String showTel(String tel1){
        String Tel="";
        if(tel1!=null){
        String telNum = deleteChar(tel1);

            Tel = telNum;
        
            switch(telNum.length()){
                case 7: //000-0000
                    Tel=  "02-" + telNum.substring(0,3) +  "-" + telNum.substring(3);

                case 8: //0000-0000
                    if(telNum.substring(0,1).equals("2")){ //서울인경우~ //02-0000-0000
                        Tel = telNum.substring(1,4) + "-" + telNum.substring(4);
                    }
                    break;
                case 9: //'0-0000-0000'
                    if(telNum.substring(0,1).equals("2")){//서울인경우~
                        Tel = "0" + telNum.substring(0,1) + "-" + telNum.substring(1,5) + "-" + telNum.substring(5);
                    }
                    else{ //'00-000-0000'
                        Tel = telNum.substring(0,2)+"-"+telNum.substring(2,5)+"-"+telNum.substring(5);
                    }
                    break;
                case 10: //'000-000-0000'
                    if(telNum.substring(0,2).equals("02")){
                        Tel= telNum.substring(0,2)+"-"+telNum.substring(2,6)+"-"+telNum.substring(6);
                    }else{
                        Tel= telNum.substring(0,3)+"-"+telNum.substring(3,6)+"-"+telNum.substring(6);
                    }
                    break;
                case 11: //'000-0000-0000'
                    Tel= telNum.substring(0,3)+"-"+telNum.substring(3,7)+"-"+telNum.substring(7);
                    break;
                case 12: //'0000-0000-0000'
                    Tel= telNum.substring(0,4)+"-"+telNum.substring(4,8)+"-"+telNum.substring(8);
                    break;

            }
        }
        return Tel;
    }

    /**********************************************************
     * 우편가 6 자리 인경우 -를 넣어 리턴하는 메소드.
     * @param : String title(문자열)
     * @param : int max( 보여줄 문자열 수)
     * @return : String title.substring(0,max-3)+"..."
    **********************************************************/
    public String setZip(String st_Zip)
     {
        if(st_Zip.length()==6){
            String st_Temp =  st_Zip.substring(0,3) + "-"+st_Zip.substring(3);
            return st_Temp;
        }
        else{
            return st_Zip;
        }
     }

    /*******************************************************
     * 우편번호를 체크하여 우편번호 사이에 -를 붙여준다.
     * RETURN TYPE : String
    *******************************************************/
    public String showZip(String zip){

        String temp="";

        if(zip != null){

        String newZip = deleteChar(zip);
        
            switch(zip.length()){               
                case 6: //'000-000'
                    temp = newZip.substring(0,3) + "-" + newZip.substring(3);
                    break;
            }
        }
        return temp;
    }

    /*******************************************************
     * 스트링에서 숫자만을 걸러낸다. 
     * RETURN TYPE : STRING Tel;
    *******************************************************/
    public String deleteChar(String str){
        String strNumber = "0123456789";
        String addString = "";
        if (str.length() > 0 ) {

            for(int i=0; i<str.length();i++){
                if( strNumber.indexOf(str.charAt(i))>= 0){
                    addString = addString + str.charAt(i);
                }
            }
        }
        return addString;
    }

    /* 10원 미만 절사 */
    public long isTen(double d){
        long il = 0;
        il = (long)(d * 0.1);
        il = il * 10;
        return il;
    }

    /* 100원 미만 절사 */
    public long isHen(double d){
        long il = 0;
        il = (long)(d * 0.01);
        il = il * 100;
        return il;
    }
    /* 1000원 미만 절사 */
    public long isTh(double d){
        long il = 0;
        il = (long)(d * 0.001);
        il = il * 1000;
        return il;
    }
    /* 100000원 미만 절사 */
    public long isMan(double d){
        long il = 0;
        il = (long)(d * 0.00001);
        il = il * 100000;
        return il;
    }

    /* null 일때 "" 리턴 */
    public String isnull(String str){
        String strTmp;
        if (str == null){
            strTmp = "";
        }else{
            strTmp = str.trim();
        }

        return strTmp;
    }

    /*******************************************************
     * 비밀번호 앞자리 몇개만 남기고 나머지는 별표로한다.
     * RETURN TYPE : STRING sReturn;
    *******************************************************/
    public String FormatPassWord(String str, int cnt){
        int pwlength = 0;
        String sReturn = "";
        String sfor = "";

        pwlength = str.length();

        if(pwlength > cnt) {
            sReturn  = str.substring(0, cnt);
            if(str.substring(cnt).length() > 0) {
                for(int i = 0; i < str.substring(cnt).length(); i++) {
                    sfor = sfor + "*";
                }
            }
            sReturn = sReturn + sfor; 
        }
        return sReturn;
    }

    /**
     * String.substring(int start, int end) 대체
     * NullPointException 방지
     */
    public static String substring(String src, int start, int end){
        if(src == null || "".equals(src) || start > src.length() || start > end || start < 0) return "";
        if(end > src.length()) end = src.length();

        return src.substring(start, end);
    }

    
/*******************************************************
  * byte수 리턴하기 
  * RETURN TYPE : int
 *******************************************************/
    public int getByteCnt(String str){

        int reLength = 0;

        if(str.length() > 0) {
            try {

                byte sByte[] = str.getBytes("MS949");
                reLength = sByte.length;

            } catch(java.io.UnsupportedEncodingException e) {}
        }

        return reLength;
    }


 /*******************************************************
  * byte수 리턴하기 및 데이타 붙여서 Return
  * RETURN TYPE : int
 *******************************************************/
    public String getByteCntData(String str){

        int reLength = 0;

        if(null == str || "".equals(str)) {
            str = " ";
        }

        if(str.length() > 0) {
            try {
                byte sByte[] = str.getBytes("MS949");
                reLength = sByte.length;

            }catch(java.io.UnsupportedEncodingException e) {}
        }
        return ""+reLength+","+str;
    }

 /********************************************************
 * null 값을 "0" 문자로 바꿔준다.
 * @param : String pm_sContent;
 * @return : String type pm_sContent;
 ********************************************************/
 public String replaceNum(String pm_sContent){
  if(pm_sContent == null || pm_sContent.equals("")) {
   return "0";
  }
  return pm_sContent;
 }
 
/********************************************************
 * 전문 항목별 자리수 포멧
 * 인가번호 : 5자리 2-3
 * 최종월차 : 6자리 4-2
 * 증서번호 : 10자리 2-2-6 
 * 주민번호 : 13자리 6-7
 * 날      짜  : 8자리 4-2-2
 ********************************************************/
 public String getDataFormat( String st_value ) {

     String format = ""; 
     String sReturn = "";
     if(st_value == null) st_value = "";
     else 
     {
         if(st_value == null) st_value = "";
         StringTokenizer st = new StringTokenizer(st_value, "-");
         while (st.hasMoreTokens())
         {
             format += st.nextToken();
         }
         st_value = format;
     }

     sReturn = getDataFormat(st_value, "default");
     return sReturn;

 }

 
 /********************************************************
 * 전문 항목별 자리수 포멧
 * 인가번호 : 5자리 2-3
 * 최종월차 : 6자리 4-2
 * 증서번호 : 10자리 2-2-6 
 * 주민번호 : 13자리 6-7
 * 날      짜  : 8자리 4-2-2
 * Parameter : st_value (포맷 적용 값), type (jStar : 주민번호 뒷자리 별표 표시, default : 그 이외의 포맷)
 ********************************************************/
 public String getDataFormat( String st_value, String type )
 {  
     String format = "";
     if(st_value == null) st_value = "";

     format = st_value;
     

     // 인가번호
     if(st_value.length() == 5)
     {
         try{ format = st_value.substring(0, 2)+"-"+st_value.substring(2, 5); }catch(Exception e){  }
     }
     // 최종월차
     else if(st_value.length() == 6)
     {
         try{ format = st_value.substring(0, 4)+"-"+st_value.substring(4, 6); }catch(Exception e){  }   
     }
     // 날짜
     else if(st_value.length() == 8)
     {
         try{ format = st_value.substring(0, 4)+"-"+st_value.substring(4, 6)+"-"+st_value.substring(6, 8); }catch(Exception e){  }
     }
     // 증서번호
     else if(st_value.length() == 10)
     {
         try{ format = st_value.substring(0, 2)+"-"+st_value.substring(2, 4)+"-"+st_value.substring(4, 10); }catch(Exception e){  }
     }
     // 주민번호
     else if(st_value.length() == 13)
     {
         try{ format = st_value.substring(0, 6)+"-"+st_value.substring(6, 13); }catch(Exception e){  }  
     }// 시간체크
     else if(st_value.length() == 8)
     {
     }
    //주민번호 별표처리
    if(type.equals("jStar") && st_value.length() == 13) {
        try{ format = st_value.substring(0, 6)+"-"+st_value.substring(6, 7)+"******"; }catch(Exception e){ }
    }
    //시간별 처리1
    if(type.equals("time") && st_value.length() == 8) {
         try{ format = st_value.substring(0, 2)+":"+st_value.substring(2, 4)+":"+st_value.substring(4, 6)+"."+st_value.substring(6, 8); }catch(Exception e){  } 
    }
    //시간별 처리2
    if(type.equals("time") && st_value.length() == 6) {
         try{ format = st_value.substring(0, 2)+":"+st_value.substring(2, 4)+":"+st_value.substring(4, 6); }catch(Exception e){  }  
    }
    //계좌번호처리
    if(type.equals("acctno") && st_value.length() == 13) {
        st_value = st_value.substring(0, 10)+"***";
         try{ format = st_value.substring(0, 5)+"-"+st_value.substring(5, 7)+"-"+st_value.substring(7, 13); }catch(Exception e){  } 
    }
    //계좌번호처리
    if(type.equals("acctno") && st_value.length() == 16) {
        st_value = st_value.substring(0, 13)+"***";
         try{ format = st_value.substring(0, 5)+"-"+st_value.substring(5, 7)+"-"+st_value.substring(7, 16); }catch(Exception e){  } 
    }

    //계좌번호 별표
    /*if(type.equals("accStar")) {
        int valueLen = 0;
        valueLen = st_value.length();
        if(valueLen = 7) {
         try{ format = st_value.substring(0, 5)+"-"+st_value.substring(5, 7); }catch(Exception e){  }   
        }else if(7 < valueLen < 11) {
            try{ format = st_value.substring(0, 5)+"-"+st_value.substring(5, 7)+"-***"; }catch(Exception e){  } 
        }else if(10 < valueLen) {
            try{ format = st_value.substring(0, 5)+"-"+st_value.substring(5, 7)+"-***"; }catch(Exception e){  }
        }
    }*/


     return format;
 }

 /********************************************************
 * 전문 항목별 자리수 포멧
 * 계좌번호 : 13자리 5-2-6 , 16자리 5-2-9
 * Parameter : st_value (포맷 적용 값), type (jStar : 주민번호 뒷자리 별표 표시, default : 그 이외의 포맷)
 ********************************************************/
 public String getAcctNo( String st_value, String type )
 {  
     String format = "";
     if(st_value == null) st_value = "";

     format = st_value;  

    //계좌번호처리
    if(type.equals("acctno") && st_value.length() == 13) {
         try{ format = st_value.substring(0, 5)+"-"+st_value.substring(5, 7)+"-"+st_value.substring(7, 13); }catch(Exception e){  } 
    }
    //계좌번호처리
    if(type.equals("acctno") && st_value.length() == 16) {
         try{ format = st_value.substring(0, 5)+"-"+st_value.substring(5, 7)+"-"+st_value.substring(7, 16); }catch(Exception e){  } 
    }
    
    return format;
 }



 /********************************************************
 * 전문 항목별 자리수 포멧
 * 인가번호 : 5자리 2-3
 * 최종월차 : 6자리 4-2
 * 증서번호 : 10자리 2-2-6 
 * 주민번호 : 13자리 6-7
 * 날      짜  : 8자리 4-2-2
 * Parameter : st_value (포맷 적용 값), type (jStar : 주민번호 뒷자리 별표 표시, default : 그 이외의 포맷)
 ********************************************************/
 public String getTimeFormat( String st_value )
 {  
     String format = "";
     if(st_value == null) st_value = "";
     
     // 최종월차
     else if(st_value.length() == 6)
     {
         try{ format = st_value.substring(0, 2)+":"+st_value.substring(2, 4)+":"+st_value.substring(4, 6); }catch(Exception e){ }   
     }
     return format;
 }

 /********************************************************
 * 전문 항목별 자리수 포멧
 * 인가번호 : 5자리 2-3
 * 최종월차 : 6자리 4-2
 * 증서번호 : 8자리 2-2-4 
 * 주민번호 : 13자리 6-7
 ********************************************************/
 public String setDataFormat( String st_value )
 {  
     if(st_value == null) st_value = "";
     String format = ""; 
     StringTokenizer st = new StringTokenizer(st_value, "-");
     while (st.hasMoreTokens())
     {
         format += st.nextToken();
     }
     return format;
 }

/********************************************************
 * 숫자 금액(콤마) 표시 
 ********************************************************/
 public String getMoneyFormat( String st_value )
 {  
    String money = "";
    try{ money = new DecimalFormat("###,###,###,###,###,###,###,###").format(Long.parseLong(st_value)); }catch(Exception e){  }
    return money;
 }
/********************************************************
 * 숫자 금액(콤마) 제거 
 ********************************************************/
 public String setMoneyFormat( String st_value )
 {  
     if(st_value == null) st_value = "";
     String format = ""; 
     StringTokenizer st = new StringTokenizer(st_value, ",");
     while (st.hasMoreTokens())
     {
         format += st.nextToken();
     }
     return format;
 }
 /********************************************************
 * -금액 표시(0000-10000)
 ********************************************************/
 public String getMiFormat(String money){
     String sReturn="";
     if(money == null || money.equals("")){
         return "";
     }
     String str = money;
     str = ReplaceAll(str,"-","");
     if(str.length() == money.length()){
         sReturn = getMoneyFormat(""+Integer.parseInt(str));
     }else{
         sReturn = "-"+getMoneyFormat(str);
     }
     return sReturn;
 }

 public static String asHex(byte[] buf)
 {
     char[] chars = new char[2 * buf.length];
     for (int i = 0; i < buf.length; ++i)
     {
         chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
         chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
     }
     return new String(chars);
 } 
  // 전각 문자 처리
 public String JunGakData(String valueData)
 {
         String tmp = ""; 
         String value1 = "";
         for(int i = 0; i < valueData.length(); i++)
         {
                 value1 = String.valueOf(valueData.charAt(i));
                 value1 = ReplaceAll(value1, "　", "");
                 tmp += value1;
         }
         return tmp;
 }


public static String toKor(String property)
{
    //20200206
    return null;
}

/********************************************************
 * EAI
* -오른쪽문자열지움
* 20200213
********************************************************/
public static String rtrim(String source, String trimStr)
{

    if(source != null && source.endsWith(trimStr))

        return source.substring(0, source.length() - trimStr.length());

    else

        return source;

}

}
