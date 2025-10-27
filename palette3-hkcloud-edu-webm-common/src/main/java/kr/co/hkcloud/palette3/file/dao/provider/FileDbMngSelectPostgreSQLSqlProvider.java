package kr.co.hkcloud.palette3.file.dao.provider;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.enumer.FILE_DB_MNG;

// @formatter:off
/**
 * 파일 DB 관리 조회 Postgresql SQL Provider
 * Implements the ProviderMethodResolver on your provider class
 * 
 * @author leeiy
 *
 */
public class FileDbMngSelectPostgreSQLSqlProvider implements ProviderMethodResolver
{
    /**
     * 저장소대상유형 조회
     * @param fileDbMngSelectRequest
     * @return
     */
    public static String selectRepositoryTrgtTypeCd(final FileDbMngSelectRequest fileDbMngSelectRequest)
    {
        return new SQL() {{
            SELECT( FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name()                               //-- ASP고객키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name()    //AS   fileGroupKey      //-- 파일그룹키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name()          //AS   fileKey           //-- 파일키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_ACS_TYPE_CD.name()                           //-- 파일엑세스유형(public:공개,private:비공개)
                  , FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()       //AS   trgtTypeCd        //-- 저장소대상유형
                  , FILE_DB_MNG.COLUMN.PLT_FILE.ACTL_FILE_NM.name() //AS   actlFileNm  //-- 진짜 파일명
            );
            FROM(
                    FILE_DB_MNG.TABLE.PLT_FILE.name()
            );
            WHERE(
                    /*String.format("%s = #{custcoId}"  , FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name())
                  , */String.format("%s = #{fileGroupKey}"   , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name())
                  , String.format("%s = #{fileKey}"        , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name())
                  , String.format("%s = #{fileAcsTypeCd}" , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_ACS_TYPE_CD.name())
            );
            
            //업무유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTaskTypeCd()))
            {
                WHERE(String.format("%s = #{taskTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()));
            }
            //저장소 대상 유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTrgtTypeCd()))
            {
                WHERE(String.format("%s = #{trgtTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()));
            }
            //경로유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getPathTypeCd()))
            {
                WHERE(String.format("%s = #{pathTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()));
            }
            //MIME 유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getMimeTypeCd()))
            {
                WHERE(String.format("%s = #{mimeTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()));
            }

        }}.toString();
    }
    
    /**
     * 파일 갯수 조회
     * @param fileDbMngSelectRequest
     * @return
     */
    public static String selectCount(final FileDbMngSelectRequest fileDbMngSelectRequest)
    {
        return new SQL() {{
            SELECT(
                    "COUNT(*)  AS dbFileCount"  //-- 파일갯수
            );
            FROM(
                    FILE_DB_MNG.TABLE.PLT_FILE.name()
            );
            WHERE(
                    String.format("%s = #{fileGroupKey}", FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name())
            );
        }}.toString();
    }
    
    /**
     * 파일 목록 조회
     * @param fileDbMngSelectRequest
     * @return
     */
    public static String selectList(final FileDbMngSelectRequest fileDbMngSelectRequest)
    {
            return new SQL() {{
                
                SELECT(
                        FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name()                               //-- ASP고객키
                      , FILE_DB_MNG.COLUMN.PLT_FILE.ACTL_FILE_NM.name() //AS   actlFileNm  //-- 진짜 파일명
                      , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_SZ.name()         //AS   fileSz      //-- 파일크기
                      , FILE_DB_MNG.COLUMN.PLT_FILE.DWNLD_CNT.name()         //AS   dwnldCnt      //-- 다운로드수
                      , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_EXTN.name()         //AS   fileExtn      //-- 파일확장자
                      , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name()    //AS   fileGroupKey  //-- 파일그룹키
                      , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name()          //AS   fileKey //-- 파일키
                      , FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name()      //AS   custcoId // -- 회사키
                      , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_PATH.name()         //AS   file_path // -- 폴더경로
                      , FILE_DB_MNG.COLUMN.PLT_FILE.STRG_FILE_NM.name()     //AS   STRG_FILE_NM // -- 실제 저장 파일명
                      , FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()
                      , FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()
                      , FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()
                      , FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()
                );
             
                FROM(
                        FILE_DB_MNG.TABLE.PLT_FILE.name()
                );
               
                WHERE(
                        /*String.format("%s = #{custcoId}"  , FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name())
                      , */String.format("%s = #{fileGroupKey}", FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name())
                );
                
                //파일키
                if(!StringUtils.isEmpty(fileDbMngSelectRequest.getFileKey()))
                {
                    WHERE(String.format("%s = #{fileKey}"     , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name()));
                }
                //업무유형
                if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTaskTypeCd()))
                {
                    WHERE(String.format("%s = #{taskTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()));
                }
                //저장소 대상 유형
                if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTrgtTypeCd()))
                {
                    WHERE(String.format("%s = #{trgtTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()));
                }
                //경로유형
                if(!StringUtils.isEmpty(fileDbMngSelectRequest.getPathTypeCd()))
                {
                    WHERE(String.format("%s = #{pathTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()));
                }
                //MIME 유형
                if(!StringUtils.isEmpty(fileDbMngSelectRequest.getMimeTypeCd()))
                {
                    WHERE(String.format("%s = #{mimeTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()));
                }
            }}.toString();
    }
    
    /**
     * 데이터 단 건 조회
     * @param fileDbMngSelectRequest
     * @return
     */
    public static String select(final FileDbMngSelectRequest fileDbMngSelectRequest)
    {
        return new SQL() {{
            SELECT(
                    FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name()                               //-- ASP고객키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name()    //AS   fileGroupKey  //-- 파일그룹키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name()          //AS   fileKey       //-- 파일키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()         //AS   taskTypeCd      //-- 업무구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()       //AS   trgtTypeCd    //-- 저장소대상유형
                  , FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()         //AS   pathTypeCd      //-- 경로구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()         //AS   mimeTypeCd      //-- MIME구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.ACTL_FILE_NM.name() //AS   actlFileNm  //-- 진짜 파일명
                  , FILE_DB_MNG.COLUMN.PLT_FILE.STRG_FILE_NM.name()     //AS   strgFileNm  //-- 저장된 파일명
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_PATH.name()         //AS   filePath      //-- 파일경로(전체)
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_SZ.name()         //AS   fileSz      //-- 파일크기
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_EXTN.name()         //AS   fileExtn      //-- 파일확장자
                  , FILE_DB_MNG.COLUMN.PLT_FILE.DWNLD_CNT.name()         //AS   dwnldCnt      //-- 다운로드수
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_URL.name()          //AS   fileUrl       //-- 파일URL
                  , FILE_DB_MNG.COLUMN.PLT_FILE.ACS_KEY.name()        //AS   accessKey     //-- 접근키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.RGTR_ID.name()           //AS   rgtrId        //-- 작성자ID
                  , FILE_DB_MNG.COLUMN.PLT_FILE.REG_DT.name() 		//-- 작성일시
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MDFR_ID.name()           //AS   userId        //-- 처리자ID
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MDFCN_DT.name()  //-- 전산처리일시
            );
            FROM(
                    FILE_DB_MNG.TABLE.PLT_FILE.name()
            );
            WHERE(
                    /*String.format("%s = #{custcoId}"  , FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name())
                  , */String.format("%s = #{fileGroupKey}", FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name())
                  , String.format("%s = #{fileKey}"     , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name())
            );
            
            //업무유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTaskTypeCd()))
            {
                WHERE(String.format("%s = #{taskTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()));
            }
            //저장소 대상 유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTrgtTypeCd()))
            {
                WHERE(String.format("%s = #{trgtTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()));
            }
            //경로유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getPathTypeCd()))
            {
                WHERE(String.format("%s = #{pathTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()));
            }
            //MIME 유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getMimeTypeCd()))
            {
                WHERE(String.format("%s = #{mimeTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()));
            }

        }}.toString();
    }
    
    /**
     * 데이터 단 건 BLOB과 확장자만 조회
     * @param fileDbMngSelectRequest
     * @return
     */
    public static String selectOnlyBlobAndExts(final FileDbMngSelectRequest fileDbMngSelectRequest)
    {
        return new SQL() {{
            SELECT(
                    FILE_DB_MNG.COLUMN.PLT_FILE.ACTL_FILE_NM.name() //AS   actlFileNm  //-- 진짜 파일명
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()         //AS   mimeTypeCd      //-- MIME구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_EXTN.name()         //AS   fileExtn      //-- 파일확장자
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_BLOB.name()         //-- 파일
            );
            FROM(
                    FILE_DB_MNG.TABLE.PLT_FILE.name()
            );
            WHERE(
                    String.format("%s = #{fileGroupKey}", FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name())
                  , String.format("%s = #{fileKey}"     , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name())
            );
            
            //업무유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTaskTypeCd()))
            {
                WHERE(String.format("%s = #{taskTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()));
            }
            //저장소 대상 유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTrgtTypeCd()))
            {
                WHERE(String.format("%s = #{trgtTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()));
            }
            //경로유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getPathTypeCd()))
            {
                WHERE(String.format("%s = #{pathTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()));
            }
            //MIME 유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getMimeTypeCd()))
            {
                WHERE(String.format("%s = #{mimeTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()));
            }
        }}.toString();
    }
    
    /**
     * 데이터 단 건 BLOB 조회
     * @param fileDbMngSelectRequest
     * @return
     */
    public static String selectWithBlob(final FileDbMngSelectRequest fileDbMngSelectRequest)
    {
        return new SQL() {{
            SELECT(
                    FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name()                               //-- ASP고객키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name()    //AS   fileGroupKey  //-- 파일그룹키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name()          //AS   fileKey       //-- 파일키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()         //AS   taskTypeCd      //-- 업무구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()       //AS   trgtTypeCd    //-- 저장소대상유형
                  , FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()         //AS   pathTypeCd      //-- 경로구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()         //AS   mimeTypeCd      //-- MIME구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.ACTL_FILE_NM.name() //AS   actlFileNm  //-- 진짜 파일명
                  , FILE_DB_MNG.COLUMN.PLT_FILE.STRG_FILE_NM.name()     //AS   strgFileNm  //-- 저장된 파일명
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_PATH.name()         //AS   filePath      //-- 파일경로(전체)
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_SZ.name()         //AS   fileSz      //-- 파일크기
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_EXTN.name()         //AS   fileExtn      //-- 파일확장자
                  , FILE_DB_MNG.COLUMN.PLT_FILE.DWNLD_CNT.name()         //AS   dwnldCnt      //-- 다운로드수
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_URL.name()          //AS   fileUrl       //-- 파일URL
                  , FILE_DB_MNG.COLUMN.PLT_FILE.ACS_KEY.name()        //AS   accessKey     //-- 접근키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.RGTR_ID.name()           //AS   rgtrId        //-- 작성자ID
                  , FILE_DB_MNG.COLUMN.PLT_FILE.REG_DT.name() 			//-- 작성일시
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MDFR_ID.name()           //AS   userId        //-- 처리자ID
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MDFCN_DT.name()  		//-- 전산처리일시
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_BLOB.name()         //AS   fileBlob"  //-- 파일
            );
            FROM(
                    FILE_DB_MNG.TABLE.PLT_FILE.name()
            );
            WHERE(
                    String.format("%s = #{fileGroupKey}", FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name())
                  , String.format("%s = #{fileKey}"     , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name())
            );
            
            //업무유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTaskTypeCd()))
            {
                WHERE(String.format("%s = #{taskTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()));
            }
            //저장소 대상 유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getTrgtTypeCd()))
            {
                WHERE(String.format("%s = #{trgtTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()));
            }
            //경로유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getPathTypeCd()))
            {
                WHERE(String.format("%s = #{pathTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()));
            }
            //MIME 유형
            if(!StringUtils.isEmpty(fileDbMngSelectRequest.getMimeTypeCd()))
            {
                WHERE(String.format("%s = #{mimeTypeCd}", FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()));
            }
        }}.toString();
    }

}
//@formatter:on