package kr.co.hkcloud.palette3.file.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;


/**
 *  파일 관련 유틸
 *
 * @author leeiy
 *
 */
@Component
public class FileMngUtils
{
    /**
     * 파일속성
     */
    public final static int FILE_CONST_ATTR        = 0;
    /**
     * 생성일
     */
    public final static int FILE_CONST_CREATE_TIME = 1;
    /**
     * 디렉토리
     */
    public final static int FILE_CONST_DIR         = 2;
    /**
     * 확장자
     */
    public final static int FILE_CONST_EXT         = 3;
    /**
     * 존재여부
     */
    public final static int FILE_CONST_EXIST       = 4;
    /**
     * 파일명을 포함한 전체경로
     */
    public final static int FILE_CONST_FULLNAME    = 5;
    /**
     * 파일명
     */
    public final static int FILE_CONST_NAME        = 6;
    /**
     * 파일크기
     */
    public final static int FILE_CONST_SIZE        = 7;
    /**
     * 마지막 쓰기일시
     */
    public final static int FILE_CONST_WRITE_TIME  = 8;


    /**
     * 파일ID를 요청 시점의 정보를 이용하여 생성한 후 반환한다.
     * 
     * @return 생성된 파일ID
     */
    public String getFileID()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssMs", java.util.Locale.KOREA);
        return formatter.format(new java.util.Date());
    }


    /**
     * 해당 파일이 존재하는 체크
     * 
     * @param  strFileName 파일경로 전체정보(파일명포함)
     * @return             true:파일이 존재, false:파일이 없음
     */
    public boolean existFile(String strFileName)
    {
        File objFile = new File(strFileName);
        if(objFile.exists()) {
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * 특정위치 파일을 Byte Array 로 읽어서 리턴한다.
     * 
     * @param  strFileName 파일경로 전체정보(파일명포함)
     * @return             Byte Array
     * @throws IOException
     */
    public byte[] readFile(String strFileName) throws IOException
    {
        File objFile = new File(strFileName);
        if(!objFile.exists()) { return null; }
        Long lngSize = new Long(objFile.length());
        byte[] bytData = new byte[lngSize.intValue()];

        FileInputStream objReader = new FileInputStream(objFile);
        objReader.read(bytData);
        objReader.close();

        return bytData;
    }


    /**
     * Byte Array 상태로 읽은 파일정보를 특정 위치에 파일로 저장한다.
     * 
     * @param  strFileName 저장할 파일경로 전체정보(파일명포함)
     * @param  bytData     저장할 파일데이터
     * @throws IOException
     */
    public void saveFile(String strFileName, byte[] bytData) throws IOException
    {
        File objFile = new File(strFileName);
        FileOutputStream objWriter = new FileOutputStream(objFile);
        if(bytData != null) {
            objWriter.write(bytData);
        }
        objWriter.close();
    }


    /**
     * String 문자열을 특정 위치에 파일로 저장한다.
     * 
     * @param  strFileName 저장할 파일경로 전체정보(파일명포함)
     * @param  strData     문자열 데이터
     * @throws IOException
     */
    public void saveFile(String strFileName, String strData) throws IOException
    {
        File objFile = new File(strFileName);
        FileOutputStream objWriter = new FileOutputStream(objFile);
        if(strData != null) {
            objWriter.write(strData.getBytes());
        }
        objWriter.close();
    }


    /**
     * 전체 경로를 이용하여 디렉토리를 생성한다.
     * 
     * @param  strPath 생성한 디렉토리의 전체 경로
     * @return         true/false
     */
    public boolean createDirectory(String strPath)
    {
        boolean blnRtn = true;
        if(strPath.endsWith(":\\")) { return blnRtn; }
        File objFile = new File(strPath);
        if(!objFile.exists() && !objFile.isDirectory()) {

            return objFile.mkdirs();
        }
        else {
            return false;
        }
    }


    /**
     * 디렉토리 삭제
     * 
     * @param  strPath      삭제할 디렉토리 위치
     * @param  blnRecursive 하위 디렉토리 모두 삭제여부
     * @return              true/false
     */
    public boolean deleteDirectory(String strPath, boolean blnRecursive)
    {
        boolean blnRtn = true;

        if(strPath.endsWith(":\\")) { return blnRtn; }
        File objFile = new File(strPath);
        if(objFile.exists() && objFile.isDirectory()) {
            if(objFile.list() == null || objFile.list().length == 0) {
                objFile.delete();
            }
            else {
                if(blnRecursive) {
                    deleteDir(objFile);
                    objFile.delete();
                }
                else {
                    return false;
                }
            }
        }
        blnRtn = true;

        return blnRtn;
    }


    /**
     * 폴더를 삭제한다
     * 
     * @param objFile
     */
    private void deleteDir(File objFile)
    {
        if(!objFile.exists()) { return; }
        File objTar = null;
        File[] files = objFile.listFiles();
        if(files == null || files.length == 0) {
            objFile.delete();
        }
        else {
            for(int i = 0; i < files.length; i++) {
                objTar = files[i];
                if(objTar.isDirectory()) {
                    if(objTar.list().length == 0) {
                        objTar.delete();
                    }
                    else {
                        deleteDir(objTar);
                        objTar.delete();
                    }
                }
                else {
                    objTar.delete();
                }
            }
        }
    }


    /**
     * 특정 디렉토리하위에 있는 파일을 검색하여 특정 조건(확장자, 생성일시)에 해당하는 것만 모두 삭제한다.
     * 
     * @param  strPath     루트디렉토리
     * @param  strExtList  삭제할 특정확장자리스트(빈문자:모두삭제, 확장자,확장자,....)
     * @param  intAfterMin 기준 분(예를 들어 60으로 설정할 경우 처리시간 기준으로 60분 이전에 생성된 모든 파일이 삭제 대상이 됨)
     * @return             삭제파일건수
     */
    public int deleteFilesInDir(String strPath, String strExtList, int intAfterMin)
    {
        int intDelCnt = 0;

        //파일객체 목록을 가져온다.
        File objFile = new File(strPath);
        File[] objFileList = objFile.listFiles();
        if( objFileList != null ) {
            for (int i = 0; i < objFileList.length; i++) {
                //객체가 파일이면서 읽을 수 있는 상태 파일만 처리한다.
                if (objFileList[i].isFile() && objFileList[i].canWrite()) {
                    //조건 확장자 일치할 경우 처리한다.
                    if (findFileExt(strExtList, FilenameUtils.getExtension(objFileList[i].getName()))) {
                        //특정시간 이전 파일만 처리한다.
                        if ((System.currentTimeMillis() - intAfterMin * 60 * 1000) > objFileList[i].lastModified()) {
                            //해당 파일을 삭제한다.
                            objFileList[i].delete();
                            intDelCnt += 1;
                        }
                    }
                }
            }
        }

        return intDelCnt;
    }


    /**
     * 파일확장자 리스트에 찾을 확장자가 존재할 경우 true를 반환한다.
     * 
     * @param  strFileExt
     * @param  strExtList
     * @return
     */
    private boolean findFileExt(String strExtList, String strFindExt)
    {
        strFindExt = strFindExt.toUpperCase();
        if(!strExtList.equals("")) {
            String[] strArrayExt = strExtList.split(",");
            for(int i = 0; i < strArrayExt.length; i++) {
                if(strFindExt.equals(strArrayExt[i].toUpperCase())) { return true; }
            }
            return false;
        }
        else {
            return true;
        }
    }


    /**
     * 파일을 Move 한다.
     * 
     * @param  strSrcFile  잘라내기 할 파일경로 전체정보(파일명포함)
     * @param  strDestFile 옮겨질 위치의 파일경로 전체정보(파일명포함)
     * @return
     */
    public boolean moveFile(String strSrcFile, String strDestFile)
    {
        File objSrcFile = new File(strSrcFile);
        createDirectory((String) getFileInfo(strDestFile, FILE_CONST_DIR));
        return objSrcFile.renameTo(new File(strDestFile));
    }


    /**
     * 파일을 복사한다.
     * 
     * @param  strSrcFile  복사할 파일경로 전체정보(파일명포함)
     * @param  strDestFile 복사될 위치의 파일경로 전체정보(파일명포함)
     * @return
     */
    public boolean copyFile(String strSrcFile, String strDestFile)
    {
        boolean blnRtn = true;
        FileInputStream objSrcFr = null;
        BufferedInputStream objSrcBr = null;
        FileOutputStream objDestFr = null;
        BufferedOutputStream objSrcBw = null;
        byte[] bteF = new byte[2048];
        int i = 0;
        try {
            File objSrcFile = new File(strSrcFile);

            if(!objSrcFile.exists()) { throw new Exception("Sourcefile not found.."); }

            File objDestFile = new File(strDestFile);
            if(objDestFile.exists()) { throw new Exception("Destfile aleady exists..!"); }
            createDirectory((String) getFileInfo(strDestFile, FILE_CONST_DIR));
            if(objSrcFile.exists()) {
                objSrcFr = new FileInputStream(objSrcFile);
                objSrcBr = new BufferedInputStream(objSrcFr);
                objDestFr = new FileOutputStream(objDestFile);
                objSrcBw = new BufferedOutputStream(objDestFr);

                while((i = objSrcBr.read(bteF)) != -1) {
                    objSrcBw.write(bteF, 0, i);
                }

                objSrcBw.flush();
                objSrcBw.close();
                objSrcBr.close();
            }
            blnRtn = true;
        }
        catch(Exception e) {
            blnRtn = false;
        }
        return blnRtn;
    }


    /**
     * 디렉토리를 복사한다.
     * 
     * @param  objFileSource 복사할 디렉토리 전체 경로
     * @param  objFileTarget 복사될 위치의 디렉토리 전체 경로
     * @return               true/false
     * @throws IOException
     */
    public boolean copyDirectory(File objFileSource, File objFileTarget) throws IOException, NullPointerException
    {
        if(objFileSource.isDirectory()) {
            //디렉토리인 경우
            //복사 대상 디렉토리가  없을 경우 생성
            if(!objFileTarget.exists()) {
                objFileTarget.mkdirs();
            }

            String[] objList = objFileSource.list();
            if( objList != null ) {
                for (int i = 0; i < objList.length; i++) {
                    copyDirectory(new File(objFileSource, objList[i]), new File(objFileTarget, objList[i]));
                }
            }
        }
        else {
            //파일인 경우
            InputStream objInputStream = new FileInputStream(objFileSource);
            OutputStream objOutputStream = new FileOutputStream(objFileTarget);

            byte[] buf = new byte[1024];
            int len;
            while((len = objInputStream.read(buf)) > 0) {
                objOutputStream.write(buf, 0, len);
            }
            objInputStream.close();
            objOutputStream.close();
        }

        return true;
    }


    /**
     * 파일을 삭제한다.
     * 
     * @param  strFilename
     * @return
     */
    public boolean deleteFile(String strFilename)
    {
        boolean blnRtn = true;
        try {
            File objSrcFile = new File(strFilename);
            if(objSrcFile.exists()) {
                if(objSrcFile.delete()) {
                    blnRtn = true;
                }
                else {
                    blnRtn = false;
                }
            }
            return blnRtn;
        }
        catch(Exception e) {
            blnRtn = false;
        }
        return blnRtn;
    }


    /**
     * 파일속성정보 반환
     * 
     * @param  strFilename  파일경로 전체정보(파일명포함)
     * @param  intAttribute 파일속성구분
     * @return              파일속성구분에 따른 파일속성정보
     */
    public Object getFileInfo(String strFilename, int intAttribute)
    {
        try {
            File objFile = new File(strFilename);
            switch(intAttribute)
            {
                case FILE_CONST_ATTR:  		 	//지원하지 않음
                    return null;
                case FILE_CONST_CREATE_TIME: 		//생성시간(지원하지 않음)
                    return null;
                case FILE_CONST_DIR:              //파일이 존재하는 디렉토리
                    return objFile.getParent();
                case FILE_CONST_NAME:              //파일명
                    return objFile.getName();
                case FILE_CONST_EXT:               //파일의 확장명
                    return objFile.getName().split("\\.")[1];
                case FILE_CONST_EXIST:           	//파일의 존재여부
                    return new Boolean(objFile.exists());
                case FILE_CONST_FULLNAME:   		//디렉토리+파일명
                    return objFile.getPath();
                case FILE_CONST_SIZE:            //파일크기
                    return new Long(objFile.length());
                case FILE_CONST_WRITE_TIME:  //파일의 최종변경일자
                    return new java.util.Date(objFile.lastModified());
                default:
                    return null;
            }
        }
        catch(Exception e) {
            return new Boolean(false);
        }
    }

}
