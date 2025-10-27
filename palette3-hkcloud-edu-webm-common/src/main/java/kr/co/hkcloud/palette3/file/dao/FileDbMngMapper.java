package kr.co.hkcloud.palette3.file.dao;


import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.config.datasources.datasource.palette.PaletteConnMapper;
import kr.co.hkcloud.palette3.exception.PaletteDaoException;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngDeleteRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngInsertRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngUpdateRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectCounteResponse;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectListResponse;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectResponse;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectTargetTypeResponse;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngDeleteOracleSqlProvider;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngDeletePostgreSQLSqlProvider;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngInsertMysqlSqlProvider;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngInsertOracleSqlProvider;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngInsertPostgreSQLSqlProvider;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngSelectMysqlSqlProvider;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngSelectOracleSqlProvider;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngSelectPostgreSQLSqlProvider;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngUpdateOracleSqlProvider;
import kr.co.hkcloud.palette3.file.dao.provider.FileDbMngUpdatePostgreSQLSqlProvider;


/**
 * 파일 DB 관리 매퍼 인터페이스
 * 
 * @author leeiy
 *
 */
@PaletteConnMapper
@Validated
public interface FileDbMngMapper
{
    /**
     * 데이터 삽입
     * 
     * @param  fileDbMngInsertRequest
     * @throws PaletteDaoException
     */
    @InsertProvider(value = FileDbMngInsertOracleSqlProvider.class,
                    method = "insert",
                    databaseId = "Oracle")
    @InsertProvider(value = FileDbMngInsertPostgreSQLSqlProvider.class,
				    method = "insert",
				    databaseId = "PostgreSQL")
    @InsertProvider(value = FileDbMngInsertMysqlSqlProvider.class,
                    method = "insert",
                    databaseId = "Mysql")
    void insert(@Valid @Validated(FileDbMngRequest.GroupMapperInsert.class) final FileDbMngInsertRequest fileDbMngInsertRequest) throws PaletteDaoException;

    /**
     * 데이터 삽입 (첨부파일 포함)
     * 
     * @param  fileDbMngInsertRequest
     * @throws PaletteDaoException
     */
    @InsertProvider(value = FileDbMngInsertOracleSqlProvider.class,
                    method = "insertWithBlob",
                    databaseId = "Oracle")
    @InsertProvider(value = FileDbMngInsertPostgreSQLSqlProvider.class,
				    method = "insertWithBlob",
				    databaseId = "PostgreSQL")
    @InsertProvider(value = FileDbMngInsertMysqlSqlProvider.class,
                    method = "insertWithBlob",
                    databaseId = "Mysql")
    void insertWithBlob(@Valid @Validated(FileDbMngRequest.GroupMapperInsertDb.class) final FileDbMngInsertRequest fileDbMngInsertRequest) throws PaletteDaoException;

    /**
     * 저장소대상유형 조회
     * 
     * @param  fileDbMngSelectRequest
     * @return
     * @throws PaletteDaoException
     */
    @SelectProvider(value = FileDbMngSelectOracleSqlProvider.class,
                    method = "selectRepositoryTrgtTypeCd",
                    databaseId = "Oracle")
    @SelectProvider(value = FileDbMngSelectPostgreSQLSqlProvider.class,
				    method = "selectRepositoryTrgtTypeCd",
				    databaseId = "PostgreSQL")
    @SelectProvider(value = FileDbMngSelectMysqlSqlProvider.class,
                    method = "selectRepositoryTrgtTypeCd",
                    databaseId = "Mysql")
    @Valid
    FileDbMngSelectTargetTypeResponse selectRepositoryTrgtTypeCd(@Validated(FileDbMngRequest.GroupServiceSelectTargetType.class) final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteDaoException;

    /**
     * 파일 갯수 조회
     * 
     * @param  fileDbMngSelectRequest
     * @return
     * @throws PaletteDaoException
     */
    @SelectProvider(value = FileDbMngSelectOracleSqlProvider.class,
                    method = "selectCount",
                    databaseId = "Oracle")
    @SelectProvider(value = FileDbMngSelectPostgreSQLSqlProvider.class,
				    method = "selectCount",
				    databaseId = "PostgreSQL")
    @SelectProvider(value = FileDbMngSelectMysqlSqlProvider.class,
                    method = "selectCount",
                    databaseId = "Mysql")
    @Valid
    FileDbMngSelectCounteResponse selectCount(@Validated(FileDbMngRequest.GroupServiceSelectCount.class) final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteDaoException;

    /**
     * 데이터 단 건 조회
     * 
     * @param  fileDbMngSelectRequest
     * @return
     * @throws PaletteDaoException
     */
    @SelectProvider(value = FileDbMngSelectOracleSqlProvider.class,
                    method = "select",
                    databaseId = "Oracle")
    @SelectProvider(value = FileDbMngSelectPostgreSQLSqlProvider.class,
				    method = "select",
				    databaseId = "PostgreSQL")
    @SelectProvider(value = FileDbMngSelectMysqlSqlProvider.class,
                    method = "select",
                    databaseId = "Mysql")
    @Valid
    FileDbMngSelectResponse select(@Valid final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteDaoException;

    /**
     * 데이터 단 건 BLOB과 진짜파일명/확장자만 조회
     * 
     * @param  fileDbMngSelectRequest
     * @return
     * @throws PaletteDaoException
     */
    @SelectProvider(value = FileDbMngSelectOracleSqlProvider.class,
                    method = "selectOnlyBlobAndExts",
                    databaseId = "Oracle")
    @SelectProvider(value = FileDbMngSelectPostgreSQLSqlProvider.class,
				    method = "selectOnlyBlobAndExts",
				    databaseId = "PostgreSQL")
    @SelectProvider(value = FileDbMngSelectMysqlSqlProvider.class,
                    method = "selectOnlyBlobAndExts",
                    databaseId = "Mysql")
    @Validated(FileDbMngRequest.GroupMapperBlobResponse.class)
    FileDbMngSelectResponse selectOnlyBlobAndExts(@Valid final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteDaoException;

    /**
     * 데이터 단 건 BLOB 조회
     * 
     * @param  fileDbMngSelectRequest
     * @return
     * @throws PaletteDaoException
     */
    @SelectProvider(value = FileDbMngSelectOracleSqlProvider.class,
                    method = "selectWithBlob",
                    databaseId = "Oracle")
    @SelectProvider(value = FileDbMngSelectPostgreSQLSqlProvider.class,
				    method = "selectWithBlob",
				    databaseId = "PostgreSQL")
    @SelectProvider(value = FileDbMngSelectMysqlSqlProvider.class,
                    method = "selectWithBlob",
                    databaseId = "Mysql")
    @Valid
    @Validated(FileDbMngRequest.GroupMapperBlobResponse.class)
    FileDbMngSelectResponse selectWithBlob(@Valid final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteDaoException;

    /**
     * 다운로드 수 증가
     * 
     * @param  fileDbMngUpdateRequest
     * @throws PaletteDaoException
     */
    @UpdateProvider(value = FileDbMngUpdateOracleSqlProvider.class,
                    method = "updateDnlodCnt",
                    databaseId = "Oracle")
    @UpdateProvider(value = FileDbMngUpdatePostgreSQLSqlProvider.class,
				    method = "updateDnlodCnt",
				    databaseId = "PostgreSQL")
    @UpdateProvider(value = FileDbMngUpdateOracleSqlProvider.class,
                    method = "updateDnlodCnt",
                    databaseId = "Mysql")
    void updateDnlodCnt(@Valid FileDbMngUpdateRequest fileDbMngUpdateRequest) throws PaletteDaoException;

    /**
     * 파일 단 건 삭제
     * 
     * @param  fileDbMngDeleteRequests
     * @throws PaletteDaoException
     */
    @DeleteProvider(value = FileDbMngDeleteOracleSqlProvider.class,
                    method = "delete",
                    databaseId = "Oracle")
    @DeleteProvider(value = FileDbMngDeletePostgreSQLSqlProvider.class,
				    method = "delete",
				    databaseId = "PostgreSQL")
    @DeleteProvider(value = FileDbMngDeleteOracleSqlProvider.class,
                    method = "delete",
                    databaseId = "Mysql")
    void delete(@Valid final FileDbMngDeleteRequest fileDbMngDeleteRequests) throws PaletteDaoException;

    /**
     * 파일 목록 조회
     * 
     * @param  fileDbMngSelectRequest
     * @return
     * @throws PaletteDaoException
     */
    @SelectProvider(value = FileDbMngSelectOracleSqlProvider.class,
                    method = "selectList",
                    databaseId = "Oracle")
    @SelectProvider(value = FileDbMngSelectPostgreSQLSqlProvider.class,
				    method = "selectList",
				    databaseId = "PostgreSQL")
    @SelectProvider(value = FileDbMngSelectMysqlSqlProvider.class,
                    method = "selectList",
                    databaseId = "Mysql")
    @Valid
    List<FileDbMngSelectListResponse> selectList(@Valid final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteDaoException;

//	List<FileDbMngSelectResponse> selects(FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteDaoException;
//	

//
//	void update(FileDbMngUpdateRequest fileDbMngUpdateRequest) throws PaletteDaoException;
//	

}
