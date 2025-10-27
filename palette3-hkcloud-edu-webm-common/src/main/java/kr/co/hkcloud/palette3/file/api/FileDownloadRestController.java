package kr.co.hkcloud.palette3.file.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.core.model.PaletteValidationCode;
import kr.co.hkcloud.palette3.exception.PaletteApiException;
import kr.co.hkcloud.palette3.exception.PaletteValidationException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.file.args.FileRuleProperties;
import kr.co.hkcloud.palette3.file.dao.domain.FileDbMngRequest.FileDbMngSelectRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.DownloadImageResourceRequest;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileDownloadRequest;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import kr.co.hkcloud.palette3.file.util.FileDownloadImageResourceValidator;
import kr.co.hkcloud.palette3.file.util.FileDownloadUtils;
import kr.co.hkcloud.palette3.file.util.FileRuleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "FileDownloadRestController", description = "파일다운로드 REST 컨트롤러")
public class FileDownloadRestController {

    private final FileDownloadImageResourceValidator fileDownloadImageResourceValidator;
    private final FileDownloadUtils fileDownloadUtils;
    private final FileRuleUtils fileRuleUtils;

    // @formatter:off
    /**
     * 파일 다운로드
     * 
     * @param  filePropertiesResponse
     * @param  fileDownloadoadRequest
     * @param  httpServletRequest
     * @return
     * @throws PaletteApiException
     */
    @ApiOperation(value = "파일 다운로드", notes = "지정된 파일을 다운로드한다.")
    @PostMapping("/api/file/{taskTypeCd}/{pathTypeCd}/download")
    public ResponseEntity<Resource> downloadFile(@FileRuleProperties @Valid final FilePropertiesResponse filePropertiesResponse
                                               , @Valid final FileDownloadRequest fileDownloadoadRequest
                                               , final HttpServletRequest httpServletRequest) throws PaletteApiException {
        log.debug("FileDownloadRequest : {}", fileDownloadoadRequest);
        
        //파일 정보 가져오기
        FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder()
                                                                              .fileGroupKey(fileDownloadoadRequest.getFileGroupKey())
                                                                              .fileKey(fileDownloadoadRequest.getFileKey())
                                                                              .custcoId(fileDownloadoadRequest.getCustcoId())
                                                                              .userId(filePropertiesResponse.getUserId())
                                                                              .build();

        log.debug("fileDbMngSelectRequest : {}", fileDbMngSelectRequest);
        // 리소스 로드

        if( filePropertiesResponse.getTrgtTypeCd() == null ) filePropertiesResponse.setTrgtTypeCd( RepositoryTrgtTypeCd.FILE );
        FileDownloadResponse fileDownloadResponse = fileDownloadUtils.loadAsResource(filePropertiesResponse, fileDbMngSelectRequest);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = httpServletRequest.getServletContext()
                                            .getMimeType(fileDownloadResponse.getResource()
                                                                             .getFile()
                                                                             .getAbsolutePath());
            log.debug("httpServletRequest contentType :{}", contentType);
        }
        catch(IOException ex) {
            log.info("Could not determine file type. {}", contentType);
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        //Media Type
        final MediaType mediaType = MediaType.parseMediaType(contentType);
        log.debug("mediaType :{}", mediaType);
        String fileNameOrg2 = "";
        //Content Disposition
     // 한글 깨짐 발생 하여 조치 =================js에서도 변경 해야함==================================================
        try {
            String fileNameOrg = fileDownloadResponse.getActlFileNm();
                   fileNameOrg2 =  URLEncoder.encode(fileNameOrg, "UTF-8");
        }
        catch(Exception e) {
            //  handle exception
        }
        
        // 한글 깨짐 발생 하여 조치 =================js에서도 변경 해야함==================================================
        
        log.debug("fileNameOrg2 ==================="+fileNameOrg2);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                                                        .filename(fileNameOrg2)
                                                        .build());
        //응답
        return ResponseEntity.ok()
                             .contentType(mediaType)
                             .headers(headers)
                             .body(fileDownloadResponse.getResource());
    }

    /**
     * 리소스 이미지 다운로드
     * 
     * @param filePropertiesResponse
     * @param downloadImageResourceRequest
     * @param bindingResult
     * @return
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "리소스 이미지 다운로드", notes = "리소스 이미지를 다운로드한다")
    @GetMapping(value = "/api/file/{taskTypeCd}/{pathTypeCd}/{fileGroupKey}/{fileKey}")
    public ResponseEntity<Resource> downloadImageResource(@FileRuleProperties @Valid final FilePropertiesResponse filePropertiesResponse
                                                        , @Valid final DownloadImageResourceRequest downloadImageResourceRequest
                                                        , final BindingResult bindingResult) throws TelewebApiException
    {
        //이미지, 문의유형이미지만 허용
        if(filePropertiesResponse.getPathTypeCd() != RepositoryPathTypeCd.images) {
            throw new PaletteValidationException(PaletteValidationCode.REJECTED_ONLY_IMAGES);
        }
        
        //파일 정보 가져오기
        FileDbMngSelectRequest fileDbMngSelectRequest = FileDbMngSelectRequest.builder()
                                                                              .fileGroupKey(downloadImageResourceRequest.getFileGroupKey())
                                                                              .fileKey(downloadImageResourceRequest.getFileKey())
                                                                              .build();
        // 리소스 로드
        FileDownloadResponse fileDownloadResponse = fileDownloadUtils.loadAsResource(filePropertiesResponse, fileDbMngSelectRequest);
        
        if(fileDownloadResponse != null && fileDownloadResponse.getResource() != null) {
            
            //메타 정보 기반
            final MediaType mediaType = fileRuleUtils.getMediaType(fileDownloadResponse.getResource());
            log.debug(">>> mediaType :{}", mediaType);
            
            //Validation 체크
            final Map<String, Object> validateMap = new HashMap<String, Object>();
            validateMap.put("filePropertiesResponse", filePropertiesResponse);
            validateMap.put("fileDownloadResponse", fileDownloadResponse);
            fileDownloadImageResourceValidator.validate(validateMap, bindingResult);
            if(bindingResult.hasErrors()) {
                for(ObjectError error : bindingResult.getAllErrors()) {
                    log.error("error.toString : {}", error.toString());
                }
                throw new PaletteValidationException(bindingResult.getAllErrors());
            }
    
            //응답
            return ResponseEntity.ok()
                                 .contentType(mediaType)
                                 .body(fileDownloadResponse.getResource());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .body(null);
    }

    /**
     * ipcc 녹취파일 다운로드 용도.
     * @param request
     * @param response
     * @throws TelewebApiException
     * @throws IOException
     */
    @NoBizLog
    @GetMapping(value = "/api/file-ipcc-rec/download")
    public void downloadIpccRec( HttpServletRequest request, HttpServletResponse response) throws TelewebApiException, IOException {

        String src = request.getParameter("src");
        OutputStream outputStream = response.getOutputStream();
        InputStream inputStream = new URL( src ).openStream();

        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + getFileNameFromURL(src) + "\";");

        BufferedInputStream fin = null;
        BufferedOutputStream outs = null;

        try {
            fin = new BufferedInputStream( inputStream );
            outs = new BufferedOutputStream( outputStream );
            int read = 0;
            byte[] buffer = new byte[8192];

            while ((read = fin.read(buffer)) != -1) {
                outs.write(buffer, 0, read);
            }
        } catch (IOException ex) {
            // Tomcat ClientAbortException을 잡아서 무시하도록 처리해주는게 좋다.
        } finally {
            try { outs.close();} catch (Exception ex1) {}
            try { fin.close(); } catch (Exception ex2) {}
        }
    }

    public static String getFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }
    // @formatter:on
}
