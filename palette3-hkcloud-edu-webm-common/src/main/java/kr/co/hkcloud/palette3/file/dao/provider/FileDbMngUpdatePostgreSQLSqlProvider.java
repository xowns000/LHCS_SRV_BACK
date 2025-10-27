package kr.co.hkcloud.palette3.file.dao.provider;


import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngUpdateRequest;
import kr.co.hkcloud.palette3.file.dao.enumer.FILE_DB_MNG;


//@formatter:off
/**
 * 파일 DB 관리 갱신 Postgresql SQL Provider
 * Implements the ProviderMethodResolver on your provider class
 * 
 * @author leeiy
 *
 */
public class FileDbMngUpdatePostgreSQLSqlProvider implements ProviderMethodResolver
{
    /**
     * 다운로드 수 증가
     * @param fileDbMngSelectRequest
     * @return
     */
    public static String updateDnlodCnt(final FileDbMngUpdateRequest fileDbMngUpdateRequest)
    {
        return new SQL() {{
            UPDATE(
                    FILE_DB_MNG.TABLE.PLT_FILE.name()
            );
            SET(
                    String.format("%1$s = COALESCE(%1$s, 0) + 1" , FILE_DB_MNG.COLUMN.PLT_FILE.DWNLD_CNT.name())
                  , String.format("%s = #{userId}::INTEGER"  , FILE_DB_MNG.COLUMN.PLT_FILE.MDFR_ID.name())
                  , String.format("%s = TO_CHAR(NOW(),'YYYYMMDDHH24MISS')"      , FILE_DB_MNG.COLUMN.PLT_FILE.MDFCN_DT.name())
            );
            WHERE(
                  //  String.format("%s = #{custcoId}"  , FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name())
                   String.format("%s = #{fileGroupKey}", FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name())
                  , String.format("%s = #{fileKey}"     , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name())
            );
            
        }}.toString();
    }
    

}
//@formatter:on