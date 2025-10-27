package kr.co.hkcloud.palette3.file.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkEnvironment;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.exception.PaletteAppException;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngInsertRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngResponse.FileDbMngSelectResponse;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequest;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileUploadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.RuleFilenameAndExtentResponse;
import kr.co.hkcloud.palette3.file.exception.FileNotFoundException;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service("fileStorageService")
@Api(value = "FileStorageServiceImpl", description = "파일 스토리지 서비스 구현체")
public class FileStorageServiceImpl implements FileStorageService {

    private final FileRuleUtils fileRuleUtils;
    private final FileDbMngService fileDbMngService;

    @Autowired
    private HcTeletalkEnvironment env;

    /**
     * 파일 저장
     */
    @Override
    @ApiOperation(value = "파일 저장", notes = "파일을 지정된 경로에 저장한다.")
    public FileUploadResponse store(final FilePropertiesResponse filePropertiesResponse,
        final FileUploadRequest fileUploadRequest) throws PaletteAppException {
        log.debug("111111111");
        MultipartFile file = fileUploadRequest.getFile();
        if (file.isEmpty()) {
            throw new FileNotFoundException();
        }
        final String actlFileNm = file.getOriginalFilename();
        //Check if the file's name contains invalid characters
        if (actlFileNm.contains("..")) {
            throw new PaletteAppException(String.format("Sorry! Filename contains invalid path sequence %1$s", actlFileNm));
        }
        log.debug("fileUploadRequest ===================================" + fileUploadRequest);

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

        //이미지인경우(PathTypeCd이 먼저) repository/web/images/bbs/2023/01/01 형태로 이외의 파일의 경우(TaskTypeCd이 먼저) repository/web/bbs/files/2023/01/01 저장됨.
        //        if (filePropertiesResponse.getPathTypeCd() == RepositoryPathTypeCd.images) {
        //            filePropertiesResponse.setDir(Paths.get(filePropertiesResponse.getCustcoTenantId() + "_" + filePropertiesResponse.getCustcoId()
        //                + "/" + filePropertiesResponse.getPathTypeCd() + "/" + filePropertiesResponse.getTaskTypeCd()));
        //        } else {
        //            filePropertiesResponse.setDir(Paths.get(filePropertiesResponse.getCustcoTenantId() + "_" + filePropertiesResponse.getCustcoId()
        //                + "/" + filePropertiesResponse.getTaskTypeCd() + "/" + filePropertiesResponse.getPathTypeCd()));
        //        }
        // ==> 2023.12.11 이후
        // << repository_root >>/<< pathTypeCd >>/<< tenantId + custcoId >>/<< taskTypeCd >>/년도/월/일/.... 로 변경함.
        filePropertiesResponse.setDir(Paths.get(
            filePropertiesResponse.getPathTypeCd() + "/" + TenantContext.getCurrentTenant() + "_" + TenantContext.getCurrentCustco() + "/"
                + filePropertiesResponse.getTaskTypeCd()));

        //비즈구분에 따른 경로 규칙 가져오기
        final Path taskTypeCdFilePath = new File(
            env.getString("file.repository.root-dir", "") + File.separator + filePropertiesResponse.getDir()).toPath();
        log.debug("taskTypeCdFilePath :{}", taskTypeCdFilePath);

        //파일 경로 규칙 가져오기
        final Path ruleStorePath = fileRuleUtils.getRuleStorePath(filePropertiesResponse);

        //저장할 파일명과 확장자 규칙 가져오기
        final RuleFilenameAndExtentResponse ruleFilenameAndExtentResponse = fileRuleUtils.getRuleFilenameAndExtent(filePropertiesResponse,
            actlFileNm, fileExtent);
        final String storeFilenameExtent = ruleFilenameAndExtentResponse.getRuleFilenameExtent();

        //전체 경로 생성
        final Path storeLocaltionFull = taskTypeCdFilePath.resolve(ruleStorePath).resolve(storeFilenameExtent).toAbsolutePath().normalize();
        log.debug("storeLocaltionFull :{}", storeLocaltionFull);

        //부모 디렉토리가 존재하지 않는 경우 생성
        final Path parent = storeLocaltionFull.getParent();
        try {
            if (parent != null && !parent.toFile().exists()) {
                Files.createDirectories(parent);
                log.debug("parent create directories :{}", parent);
            }

            //Copy file to the target location (Replacing existing file with the same name)
            Files.copy(file.getInputStream(), storeLocaltionFull, StandardCopyOption.REPLACE_EXISTING);
            log.debug("file copy done :{}", storeLocaltionFull);
        } catch (IOException e) {
            throw new PaletteAppException(e);
        }

        String saveFilePath = filePropertiesResponse.getDir().toString().replaceAll(Matcher.quoteReplacement(File.separator), "/") + "/" + ruleStorePath.toString()
                .replaceAll(Matcher.quoteReplacement(File.separator), "/");

        if (!StringUtils.isEmpty(filePropertiesResponse.getUserId())) {
            // @formatter:off
            FileDbMngInsertRequest fileDbMngInsertRequest = FileDbMngInsertRequest.builder()
                                                                                  .fileGroupKey(fileUploadRequest.getFileGroupKey())
                                                                                  .fileAcsTypeCd(fileUploadRequest.getFileAcsTypeCd())
                                                                                  .taskTypeCd(filePropertiesResponse.getTaskTypeCd())
                                                                                  .trgtTypeCd(filePropertiesResponse.getTrgtTypeCd())
                                                                                  .pathTypeCd(filePropertiesResponse.getPathTypeCd())
                                                                                  .mimeTypeCd(contentType)
                                                                                  .actlFileNm(cleanFilename)
                                                                                  .strgFileNm(storeFilenameExtent)
                                                                                  .fileSz(fileSz)
                                                                                  .filePath(saveFilePath)
                                                                                  .fileExtn(fileExtent)
                                                                                  .custcoId(fileUploadRequest.getCustcoId())
                                                                                  .userId(filePropertiesResponse.getUserId())
                                                                                  .rgtrId(filePropertiesResponse.getUserId())
                                                                                  .custcoId(fileUploadRequest.getCustcoId())
                                                                                  .build();
            log.debug("fileDbMngInsertRequest :{}", fileDbMngInsertRequest);
            
          //DB 삽입 처리
            fileDbMngService.insert(fileDbMngInsertRequest);

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
                                     .filePath(fileDbMngInsertRequest.getFilePath())
                                     .strgFileNm(fileDbMngInsertRequest.getStrgFileNm())
                                     .build();
         // @formatter:on
        } else {
            // @formatter:off
            FileDbMngInsertRequest fileDbMngInsertRequest = FileDbMngInsertRequest.builder()
                                                                                  .fileGroupKey(fileUploadRequest.getFileGroupKey())
                                                                                  .fileAcsTypeCd(fileUploadRequest.getFileAcsTypeCd())
                                                                                  .taskTypeCd(filePropertiesResponse.getTaskTypeCd())
                                                                                  .trgtTypeCd(filePropertiesResponse.getTrgtTypeCd())
                                                                                  .pathTypeCd(filePropertiesResponse.getPathTypeCd())
                                                                                  .mimeTypeCd(contentType)
                                                                                  .actlFileNm(cleanFilename)
                                                                                  .strgFileNm(storeFilenameExtent)
                                                                                  .fileSz(fileSz)
                                                                                  .filePath(saveFilePath)
                                                                                  .fileExtn(fileExtent)
                                                                                  .custcoId(fileUploadRequest.getCustcoId())
                                                                                  .userId(fileUploadRequest.getUserId())
                                                                                  .rgtrId(fileUploadRequest.getUserId())
                                                                                  .custcoId(fileUploadRequest.getCustcoId())
                                                                                  .build();
            log.debug("fileDbMngInsertRequest :{}", fileDbMngInsertRequest);
          //DB 삽입 처리
            fileDbMngService.insert(fileDbMngInsertRequest);

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
                                     .filePath(fileDbMngInsertRequest.getFilePath())
                                     .strgFileNm(fileDbMngInsertRequest.getStrgFileNm())
                                     .build();
         // @formatter:on
        }

    }

    /**
     * 파일 리소스 로드
     */
    @Override
    @ApiOperation(value = "파일 리소스 로드", notes = "파일 리소스를 로드한다.")
    public FileDownloadResponse loadAsResource(final FilePropertiesResponse filePropertiesResponse,
        final FileDbMngSelectRequest fileDbMngSelectRequest) throws PaletteAppException {
        //DB 정보 조회
        FileDbMngSelectResponse fileDbMngSelectResponse = fileDbMngService.select(fileDbMngSelectRequest);
        log.debug("fileDbMngSelectResponse : {}", fileDbMngSelectResponse);

        final String cleanFileName = StringUtils.cleanPath(fileDbMngSelectResponse.getStrgFileNm());

        // Check if the file's name contains invalid characters
        if (cleanFileName.contains("..")) {
            throw new PaletteAppException(String.format("Sorry! Filename contains invalid path sequence %s", cleanFileName));
        }

        //저장된 경로 규칙
        final Path storePath = Paths.get(env.getString("file.repository.root-dir", "") + File.separator  + fileDbMngSelectResponse.getFilePath());
        log.debug("storePath : {}", storePath);

        //저장된 파일명과 확장자
        final Path storeFilenameExtent = Paths.get(fileDbMngSelectResponse.getStrgFileNm());
        log.debug("storeFilenameExtent : {}", storeFilenameExtent);

        //전체 경로 생성
        final Path storeLocaltionFull = storePath.resolve(storeFilenameExtent).toAbsolutePath().normalize();
        log.debug("storeLocaltionFull : {}", storeLocaltionFull);

        Resource resource;
        try {
            final URI resourceUri = storeLocaltionFull.toUri();
            log.debug("resourceUri : {}", resourceUri);
            resource = new UrlResource(resourceUri);
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException(String.format("Reading the file %s", storeLocaltionFull), ex);
        }

        if (resource.exists() && resource.isReadable()) {

            // @formatter:off
            return FileDownloadResponse.builder()
                                       .actlFileNm(fileDbMngSelectResponse.getActlFileNm())
                                       .mimeTypeCd(fileDbMngSelectResponse.getMimeTypeCd())
                                       .fileExtn(fileDbMngSelectResponse.getFileExtn())
                                       .resource(resource)
                                       .build();
           // @formatter:on
        }
        throw new FileNotFoundException(String.format("File doesn't exist or not readable %s", storeLocaltionFull));
    }
}
