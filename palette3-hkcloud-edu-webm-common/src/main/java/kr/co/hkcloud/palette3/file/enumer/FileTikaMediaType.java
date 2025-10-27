package kr.co.hkcloud.palette3.file.enumer;


import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 파일 Tika 미디어 유형 정의 ㄴ 여기에 정의되지 않은 MIME_TYPE_CD은 팔레트에서 인식하지 않는다. ~/tika-core-1.26.jar/org/apache/tika/mime/tika-mimetypes.xml
 * 
 * @author leeiy
 *
 */
@Getter
@AllArgsConstructor
public enum FileTikaMediaType
{
    // @formatter:off
    //텍스트 파일 (이진 데이터 미포함)
    txt("txt" , "text/plain"      , "text/plain" , Arrays.asList("") , "텍스트 파일"                  , FileType.text),
    rtf("rtf" , "application/rtf" , "text/plain" , Arrays.asList("") , "Rich Text Format (RTF)"       , FileType.text),
    csv("csv" , "text/csv"        , "text/plain" , Arrays.asList("") , "Comma-separated values (CSV)" , FileType.text),
//    log("log" , "text/x-log"      , "text/plain" , Arrays.asList("") , "application log" , FileType.text),
    
    //이미지 파일
    png( "png"  , "image/png"  , "image/png"  , Arrays.asList("") , "Portable Network Graphics (PNG)"  , FileType.image),
    jpg( "jpg"  , "image/jpeg" , "image/jpeg" , Arrays.asList("") , "Joint Photographic Experts Group (JPEG)" , FileType.image),
    jpeg("jpeg" , "image/jpeg" , "image/jpeg" , Arrays.asList("") , "Joint Photographic Experts Group (JPEG)" , FileType.image),
    gif( "gif"  , "image/gif"  , "image/gif"  , Arrays.asList("") , "Graphics Interchange Format (GIF)", FileType.image),

    //비디오 파일
    avi("avi" , "video/x-msvideo" , "video/x-msvideo" , Arrays.asList("video/avi","video/msvideo") , "Audio Video Interleave File" , FileType.video),
    
    //문서 파일
    doc("doc"   , "application/msword"            
                                                  , "application/x-tika-msoffice" , Arrays.asList("application/vnd.ms-word")  , "Microsoft Word Document"          , FileType.office),
    xls("xls"   , "application/vnd.ms-excel"      
                                                  , "application/x-tika-msoffice" , Arrays.asList("application/msexcel")       , "Microsoft Excel Spreadsheet"      , FileType.office),
    ppt("ppt"   , "application/vnd.ms-powerpoint" 
                                                  , "application/x-tika-msoffice" , Arrays.asList("application/mspowerpoint") , "Microsoft Powerpoint Presentation", FileType.office),
    docx("docx" , "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                                  , "application/x-tika-ooxml"    , Arrays.asList("") , "Office Open XML Document" , FileType.office),
    xlsx("xlsx" , "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                                                  , "application/x-tika-ooxml"    , Arrays.asList("") , "Office Open XML Workbook" , FileType.office),
    pptx("pptx" , "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                                                  , "application/x-tika-ooxml"    , Arrays.asList("") , "Office Open XML Presentation", FileType.office),
//    thmx("thmx" , "application/vnd.openxmlformats-officedocument.presentationml.presentation"
//                                                , "application/x-tika-ooxml"    , Arrays.asList("") , "Office Open XML Presentation", FileType.office),
    pdf("pdf"   , "application/pdf" , "application/pdf"             , Arrays.asList("application/x-pdf") ,  "Adobe Portable Document Format (PDF)", FileType.office),


    //압축 파일
    zip("zip", "application/zip"              , "application/zip"              , Arrays.asList("application/x-zip-compressed") , "Compressed Archive File", FileType.archive),
    rar("rar", "application/x-rar-compressed" , "application/x-rar-compressed" , Arrays.asList("application/x-rar") , "RAR archive", FileType.archive),
    x7z("7z" , "application/x-7z-compressed"  , "application/x-7z-compressed"  , Arrays.asList("") , "7-zip archive", FileType.archive),
    
    //다른 모든 경우를 위한 기본값
    other("*" , "application/octet-stream" , "application/octet-stream" , Arrays.asList("application/octet-stream") , "아카이브 문서 (인코딩된 다중 파일)" , FileType.wildcard)
    ;

    private final String       extents;      //확장자
    private final String       real;         //원래 MIME Type
    private final String       tika;         //Tika MIME Type
    private final List<String> alias;        //Alias MIME Type
    private final String       description;  //설명
    private final FileType     fileType;     //파일유형
    // @formatter:on
}
