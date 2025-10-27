package kr.co.hkcloud.palette3.file.app;


import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.exception.PaletteAppException;
import kr.co.hkcloud.palette3.file.dao.FileDbMngMapper;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngInsertRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectResponse;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequest;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileUploadResponse;
import kr.co.hkcloud.palette3.file.exception.FileNotFoundException;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("fileStorageDbService")
@Api(value = "FileStorageDbServiceImpl",
     description = "파일 스토리지 DB 서비스 구현체")
public class FileStorageDbServiceImpl implements FileStorageService
{
    private final FileDbMngService fileDbMngService;
    private final FileDbMngMapper  fileDbMngMapper;
    private final FileRuleUtils    fileRuleUtils;


    /**
     * DB 저장
     * 
     * @param  uploadRequest
     * @return
     * @throws PaletteAppException
     */
    @Override
    @ApiOperation(value = "DB 저장",
                  notes = "파일을 DB에 저장한다.")
    public FileUploadResponse store(final FilePropertiesResponse filePropertiesResponse, final FileUploadRequest fileUploadRequest) throws PaletteAppException
    {
        MultipartFile file = fileUploadRequest.getFile();
        if(file.isEmpty()) { throw new FileNotFoundException(); }

        final String actlFileNm = file.getOriginalFilename();
        //Check if the file's name contains invalid characters
        if(actlFileNm.contains("..")) { throw new PaletteAppException(String.format("Sorry! Filename contains invalid path sequence %1$s", actlFileNm)); }

        final String cleanFilename = StringUtils.cleanPath(actlFileNm);
        final String fileNameWithOutExt = FilenameUtils.removeExtension(cleanFilename);
        final String fileExtent = fileRuleUtils.getIfFileExtent(file);
        final long fileSz = file.getSize();
        final String contentType = fileRuleUtils.getIfContentType(file);

        log.debug("actlFileNm   :{}", actlFileNm);
        log.debug("cleanFilename      :{}", cleanFilename);
        log.debug("fileNameWithOutExt :{}", fileNameWithOutExt);
        log.debug("fileExtent         :{}", fileExtent);
        log.debug("fileSz           :{}", fileSz);
        log.debug("contentType        :{}", contentType);

        byte[] fileBlob = null;
        try {
            fileBlob = file.getBytes();
        }
        catch(IOException e) {
            throw new PaletteAppException(e);
        }
        // @formatter:off
        FileDbMngInsertRequest fileDbMngInsertRequest = FileDbMngInsertRequest.builder()
                                                                              .fileGroupKey(fileUploadRequest.getFileGroupKey())
                                                                              .fileAcsTypeCd(fileUploadRequest.getFileAcsTypeCd())
                                                                              .taskTypeCd(filePropertiesResponse.getTaskTypeCd())
                                                                              .trgtTypeCd(filePropertiesResponse.getTrgtTypeCd())
                                                                              .pathTypeCd(filePropertiesResponse.getPathTypeCd())
                                                                              .mimeTypeCd(contentType)
                                                                              .actlFileNm(cleanFilename)
                                                                              .fileSz(fileSz)
                                                                              .fileExtn(fileExtent)
                                                                              .fileBlob(fileBlob != null ? fileBlob : null)
                                                                             // .custcoId(fileUploadRequest.getCustcoId())
                                                                              .custcoId(fileUploadRequest.getCustcoId())
                                                                              .userId(fileUploadRequest.getUserId())
                                                                              .rgtrId(fileUploadRequest.getUserId())
                                                                              .build();
        log.debug("fileDbMngInsertRequest :{}", fileDbMngInsertRequest);

        //DB 삽입 처리
        fileDbMngService.insertDb(fileDbMngInsertRequest);

        //응답
        return FileUploadResponse.builder()
                                 .fileGroupKey(fileDbMngInsertRequest.getFileGroupKey())
                                 .fileKey(fileDbMngInsertRequest.getFileKey())
                                 .actlFileNm(fileDbMngInsertRequest.getActlFileNm())
                                 .fileSz(fileDbMngInsertRequest.getFileSz())
                                 .fileSzDisplay(FileUtils.byteCountToDisplaySize(fileDbMngInsertRequest.getFileSz()))
                                 .dwnldCnt(0)
                                 .fileExtn(fileDbMngInsertRequest.getFileExtn())
                                 .custcoId(fileDbMngInsertRequest.getCustcoId())
                                 .build();
       // @formatter:on
    }


    /**
     * 
     */
    @Override
    @ApiOperation(value = "DB 리소스 로드",
                  notes = "DB 리소스를 로드한다.")
    public FileDownloadResponse loadAsResource(final FilePropertiesResponse filePropertiesResponse, final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException
    {
        //DB 정보 조회 (BLOB 포함)
        FileDbMngSelectResponse fileDbMngSelectResponse = fileDbMngMapper.selectOnlyBlobAndExts(fileDbMngSelectRequest);
        log.debug("fileDbMngSelectResponse : {}", fileDbMngSelectResponse);

        // @formatter:off
        return FileDownloadResponse.builder()
                                   .actlFileNm(fileDbMngSelectResponse.getActlFileNm())
                                   .mimeTypeCd(fileDbMngSelectResponse.getMimeTypeCd())
                                   .fileExtn(fileDbMngSelectResponse.getFileExtn())
                                   .resource(new ByteArrayResource(fileDbMngSelectResponse.getFileBlob()))
                                   .build();
       // @formatter:on
    }
}
