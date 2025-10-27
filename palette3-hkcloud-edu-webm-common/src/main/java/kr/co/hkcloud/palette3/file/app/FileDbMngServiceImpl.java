package kr.co.hkcloud.palette3.file.app;


import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.PaletteAppException;
import kr.co.hkcloud.palette3.exception.PaletteDaoException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.file.dao.FileDbMngMapper;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngDeleteRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngInsertRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngUpdateRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectCounteResponse;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectListResponse;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectResponse;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectTargetTypeResponse;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileDeleteRequest;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 파일 DB 관리 서비스 구현체
 * 
 * @author RNDa
 */
@Slf4j
@RequiredArgsConstructor
@Service("fileDbMngService")
public class FileDbMngServiceImpl implements FileDbMngService
{
    private final FileRuleUtils   fileRuleUtils;
    private final FileDbMngMapper fileDbMngMapper;
    private final TwbComDAO       twbComDAO;


    @Override
    public void insert(final FileDbMngInsertRequest fileDbMngInsertRequest) throws PaletteAppException
    {
        //파일키 생성
        String fileKey = fileRuleUtils.creatFileKey();

        fileDbMngInsertRequest.setFileKey(fileKey);
        log.debug("\n>>> fileDbMngInsertRequest :{}", fileDbMngInsertRequest);

        fileDbMngMapper.insert(fileDbMngInsertRequest);
    }


    @Override
    public void insertDb(final FileDbMngInsertRequest fileDbMngInsertRequest) throws PaletteAppException
    {
        //파일키 생성
        String fileKey = fileRuleUtils.creatFileKey();

        fileDbMngInsertRequest.setFileKey(fileKey);
        log.debug("\n>>> fileDbMngInsertRequest :{}", fileDbMngInsertRequest);

        fileDbMngMapper.insertWithBlob(fileDbMngInsertRequest);
    }


    @Override
    public FileDbMngSelectTargetTypeResponse selectRepositoryTrgtTypeCd(final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException
    {
        return fileDbMngMapper.selectRepositoryTrgtTypeCd(fileDbMngSelectRequest);
    }


    @Override
    public FileDbMngSelectCounteResponse selectCount(FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException
    {
        return fileDbMngMapper.selectCount(fileDbMngSelectRequest);
    }


    @Override
    public FileDbMngSelectResponse select(final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException
    {
        log.debug("fileDbMngSelectRequest ============" + fileDbMngSelectRequest);
        return fileDbMngMapper.select(fileDbMngSelectRequest);
    }


    @Override
    public void updateDnlodCnt(FileDbMngUpdateRequest fileDbMngUpdateRequest) throws PaletteAppException
    {
        fileDbMngMapper.updateDnlodCnt(fileDbMngUpdateRequest);
    }


    @Override
    @Transactional(readOnly = false)
    public void delete(final FileDeleteRequest fileDeleteRequest) throws PaletteAppException
    {
        //다 건 삭제
        if(!StringUtils.isEmpty(fileDeleteRequest.getDeleteFileKeys())) {
            for(FileDeleteRequest.DeleteFileKeys deleteFileKeys : fileDeleteRequest.getDeleteFileKeys()) {
                // @formatter:off
                FileDbMngDeleteRequest fileDbMngDeleteRequest = FileDbMngDeleteRequest.builder()
                                                                                      .fileGroupKey(deleteFileKeys.getFileGroupKey())
                                                                                      .fileKey(deleteFileKeys.getFileKey())
                                                                                      .custcoId(deleteFileKeys.getCustcoId())
                                                                                      .amdrId(deleteFileKeys.getAmdrId())
                                                                                      .build();
                log.debug("fileDbMngDeleteRequest={}", fileDbMngDeleteRequest);
                fileDbMngMapper.delete(fileDbMngDeleteRequest);
                // @formatter:on
            }
        }
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteFileGroup(TelewebJSON jsonParam) throws TelewebAppException {
        return twbComDAO.delete("kr.co.hkcloud.palette3.common.file.dao.FileDbMngMapper", "deleteFileGroup", jsonParam);
    }


    @Override
    public List<FileDbMngSelectListResponse> selectList(@Valid FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException
    {
        return fileDbMngMapper.selectList(fileDbMngSelectRequest);
    }


    @Override
    public FileDbMngSelectResponse selectOnlyBlobAndExts(FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteDaoException
    {
        return fileDbMngMapper.selectOnlyBlobAndExts(fileDbMngSelectRequest);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectFileList(TelewebJSON jsonParam) throws TelewebAppException
    {
        return twbComDAO.select("kr.co.hkcloud.palette3.common.file.dao.FileDbMngMapper", "selectFileList", jsonParam);
    }
}
