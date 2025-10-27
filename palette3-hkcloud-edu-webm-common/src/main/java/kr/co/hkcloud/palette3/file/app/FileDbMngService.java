package kr.co.hkcloud.palette3.file.app;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.PaletteAppException;
import kr.co.hkcloud.palette3.exception.PaletteDaoException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngInsertRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngUpdateRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectCounteResponse;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectListResponse;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectResponse;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectTargetTypeResponse;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileDeleteRequest;


/**
 * 파일 DB 관리서비스
 * 
 * @author RND
 *
 */
@Validated
public interface FileDbMngService
{
    void insert(@Valid @Validated(FileDbMngRequest.GroupServiceInsert.class) final FileDbMngInsertRequest fileDbMngInsertRequest) throws PaletteAppException;

    void insertDb(@Valid @Validated(FileDbMngRequest.GroupServiceInsertDb.class) final FileDbMngInsertRequest fileDbMngInsertRequest) throws PaletteAppException;

    @Valid
    FileDbMngSelectTargetTypeResponse selectRepositoryTrgtTypeCd(@Validated(FileDbMngRequest.GroupServiceSelectTargetType.class) final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException;

    @NotNull
    @Valid
    FileDbMngSelectCounteResponse selectCount(@Validated(FileDbMngRequest.GroupServiceSelectCount.class) final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException;

    @NotNull
    @Valid
    FileDbMngSelectResponse select(@Valid final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException;

    @NotNull
    @Valid
    List<FileDbMngSelectListResponse> selectList(@Valid final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException;

    @NotNull
    @Validated(FileDbMngRequest.GroupMapperBlobResponse.class)
    FileDbMngSelectResponse selectOnlyBlobAndExts(@Valid final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteDaoException;

    void updateDnlodCnt(@Valid final FileDbMngUpdateRequest fileDbMngUpdateRequest) throws PaletteAppException;

    void delete(@Valid final FileDeleteRequest fileDeleteRequest) throws PaletteAppException;
    
    TelewebJSON deleteFileGroup(TelewebJSON jsonParam) throws TelewebAppException;

    TelewebJSON selectFileList(TelewebJSON jsonParam) throws TelewebAppException;
//  List<FileDbMngSelectResponse> selects(FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException;
//
//	void update(FileDbMngUpdateRequest fileDbMngUpdateRequest) throws PaletteAppException;
//	
//	void updateDnlodCnt(FileDbMngUpdateDnlodCntRequest fileDbMngUpdateDnlodCntRequest) throws PaletteAppException;

}
