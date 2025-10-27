package kr.co.hkcloud.palette3.file.dao.provider;


import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngDeleteRequest;
import kr.co.hkcloud.palette3.file.dao.enumer.FILE_DB_MNG;


//@formatter:off
/**
 * 파일 DB 관리 삭제 Oracle SQL Provider
 * Implements the ProviderMethodResolver on your provider class
 * 
 * @author leeiy
 *
 */
public class FileDbMngDeleteOracleSqlProvider implements ProviderMethodResolver
{
    /**
     * 파일 단 건 삭제
     * @param fileDbMngDeleteRequests
     * @return
     */
    public static String delete(final FileDbMngDeleteRequest fileDbMngDeleteRequest)
    {
        return new SQL() {{
            DELETE_FROM(
                    FILE_DB_MNG.TABLE.PLT_FILE.name()
            );
            WHERE(
            //        String.format("%s = #{custcoId}"  , FILE_DB_MNG.COLUMN.PLT_FILE.CUSTCO_ID.name())
                   String.format("%s = #{fileGroupKey}", FILE_DB_MNG.COLUMN.PLT_FILE.FILE_GROUP_KEY.name())
                  , String.format("%s = #{fileKey}"     , FILE_DB_MNG.COLUMN.PLT_FILE.FILE_KEY.name())
            );
            
        }}.toString();
    }
}
//@formatter:on