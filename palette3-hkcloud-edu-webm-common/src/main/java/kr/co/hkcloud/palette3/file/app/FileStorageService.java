package kr.co.hkcloud.palette3.file.app;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.exception.PaletteAppException;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequest;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileUploadResponse;


/**
 * 파일 스토리지 서비스
 * 
 * @author RND
 *
 */
@Validated
public interface FileStorageService
{
    @NotNull
    @Valid
    FileUploadResponse store(@Valid final FilePropertiesResponse filePropertiesResponse, @Valid @Validated({FileRequest.GroupService.class}) final FileUploadRequest fileUploadRequest) throws PaletteAppException;

    @NotNull
    FileDownloadResponse loadAsResource(@Valid final FilePropertiesResponse filePropertiesResponse, @Valid final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException;
}
