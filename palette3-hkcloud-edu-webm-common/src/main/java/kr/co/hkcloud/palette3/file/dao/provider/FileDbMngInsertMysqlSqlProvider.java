package kr.co.hkcloud.palette3.file.dao.provider;


import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngInsertRequest;
import kr.co.hkcloud.palette3.file.dao.enumer.FILE_DB_MNG;


//@formatter:off
/**
 * 파일 DB 관리 삽입 Mysql SQL Provider
 * Implements the ProviderMethodResolver on your provider class
 * 
 * @author leeiy
 *
 */
public class FileDbMngInsertMysqlSqlProvider implements ProviderMethodResolver
{
    /**
     * PLT_FILE(공통첨부파일정보) 데이터 삽입
     * 
     * @param fileDbMngInsertRequest
     * @return
     */
    public static String insert(final FileDbMngInsertRequest fileDbMngInsertRequest)
    {
        return new SQL() {{
            INSERT_INTO(FILE_DB_MNG.TABLE.PLT_FILE.name());
            INTO_COLUMNS(
                  FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name()     //파일그룹키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name()           //파일키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name()       //ASP_고객사_키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()          //업무구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()        //저장소대상구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()          //경로구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()          //MIME구분
                  , FILE_DB_MNG.COLUMN.PLT_FILE.ACTL_FILE_NM.name()  //진짜파일명
                  , FILE_DB_MNG.COLUMN.PLT_FILE.STRG_FILE_NM.name()      //저장한파일명
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_PATH.name()          //파일경로
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_SZ.name()          //파일사이즈
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_EXTN.name()          //파일확장자
                  , FILE_DB_MNG.COLUMN.PLT_FILE.DWNLD_CNT.name()          //다운로드수
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_URL.name()           //파일URL
                  , FILE_DB_MNG.COLUMN.PLT_FILE.ACS_KEY.name()         //접근키
                  , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_ACS_TYPE_CD.name()   //파일엑세스유형(public:공개,private:비공개)
                  , FILE_DB_MNG.COLUMN.PLT_FILE.USE_YN.name()   //파일엑세스유형(public:공개,private:비공개)
                  , FILE_DB_MNG.COLUMN.PLT_FILE.RGTR_ID.name()   //파일엑세스유형(public:공개,private:비공개)
                  , FILE_DB_MNG.COLUMN.PLT_FILE.REG_DT.name()   //파일엑세스유형(public:공개,private:비공개)
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MDFR_ID.name()   //파일엑세스유형(public:공개,private:비공개)
                  , FILE_DB_MNG.COLUMN.PLT_FILE.MDFCN_DT.name()   //파일엑세스유형(public:공개,private:비공개)
            );
            INTO_VALUES(
                  "#{fileGroupKey}"
                  , "#{fileKey}"
                  , "#{custcoId}::INTEGER"
                  , "#{taskTypeCd}"
                  , "#{trgtTypeCd}"
                  , "#{pathTypeCd}"
                  , "#{mimeTypeCd}"
                  , "#{actlFileNm}"
                  , "#{strgFileNm}"
                  , "#{filePath}"
                  , "#{fileSz}"
                  , "#{fileExtn}"
                  , "COALESCE(#{dwnldCnt}, 0)"
                  , "#{fileUrl}"
                  , "#{accessKey}"
                  , "#{fileAcsTypeCd}"
                  , "'Y'"
                  , "#{userId}::INTEGER"
                  , "TO_CHAR(NOW(),'YYYYMMDDHH24MISS')"
                  , "#{userId}::INTEGER"
                  , "TO_CHAR(NOW(),'YYYYMMDDHH24MISS')"
            );
        }}.toString();
    }
    
    /**
     * PLT_FILE(공통첨부파일정보) 데이터 삽입 (첨부파일 포함)
     * 
     * @param fileDbMngInsertRequest
     * @return
     */
    public static String insertWithBlob(final FileDbMngInsertRequest fileDbMngInsertRequest)
    {
        return new SQL() {{
            INSERT_INTO(FILE_DB_MNG.TABLE.PLT_FILE.name());
            INTO_COLUMNS(
                FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name()     //파일그룹키
                , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name()           //파일키
                , FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name()       //ASP_고객사_키
                , FILE_DB_MNG.COLUMN.PLT_FILE.TASK_TYPE_CD.name()          //업무구분
                , FILE_DB_MNG.COLUMN.PLT_FILE.TRGT_TYPE_CD.name()        //저장소대상구분
                , FILE_DB_MNG.COLUMN.PLT_FILE.PATH_TYPE_CD.name()          //경로구분
                , FILE_DB_MNG.COLUMN.PLT_FILE.MIME_TYPE_CD.name()          //MIME구분
                , FILE_DB_MNG.COLUMN.PLT_FILE.ACTL_FILE_NM.name()  //진짜파일명
                , FILE_DB_MNG.COLUMN.PLT_FILE.STRG_FILE_NM.name()      //저장한파일명
                , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_PATH.name()          //파일경로
                , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_SZ.name()          //파일사이즈
                , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_EXTN.name()          //파일확장자
                , FILE_DB_MNG.COLUMN.PLT_FILE.DWNLD_CNT.name()          //다운로드수
                , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_URL.name()           //파일URL
                , FILE_DB_MNG.COLUMN.PLT_FILE.ACS_KEY.name()         //접근키
                , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_ACS_TYPE_CD.name()   //파일엑세스유형(public:공개,private:비공개)
                , FILE_DB_MNG.COLUMN.PLT_FILE.USE_YN.name()   //파일엑세스유형(public:공개,private:비공개)
                , FILE_DB_MNG.COLUMN.PLT_FILE.RGTR_ID.name()   //파일엑세스유형(public:공개,private:비공개)
                , FILE_DB_MNG.COLUMN.PLT_FILE.REG_DT.name()   //파일엑세스유형(public:공개,private:비공개)
                , FILE_DB_MNG.COLUMN.PLT_FILE.MDFR_ID.name()   //파일엑세스유형(public:공개,private:비공개)
                , FILE_DB_MNG.COLUMN.PLT_FILE.MDFCN_DT.name()   //파일엑세스유형(public:공개,private:비공개)
                , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_BLOB.name()          //첨부파일
            );
            INTO_VALUES(
                "#{fileGroupKey}"
                , "#{fileKey}"
                , "#{custcoId}::INTEGER"
                , "#{taskTypeCd}"
                , "#{trgtTypeCd}"
                , "#{pathTypeCd}"
                , "#{mimeTypeCd}"
                , "#{actlFileNm}"
                , "#{strgFileNm}"
                , "#{filePath}"
                , "#{fileSz}"
                , "#{fileExtn}"
                , "COALESCE(#{dwnldCnt}, 0)"
                , "#{fileUrl}"
                , "#{accessKey}"
                , "#{fileAcsTypeCd}"
                , "'Y'"
                , "#{userId}::INTEGER"
                , "TO_CHAR(NOW(),'YYYYMMDDHH24MISS')"
                , "#{userId}::INTEGER"
                , "TO_CHAR(NOW(),'YYYYMMDDHH24MISS')"
                , "#{fileBlob, jdbcType=BLOB}"
            );
        }}.toString();
    }
}
//@formatter:on