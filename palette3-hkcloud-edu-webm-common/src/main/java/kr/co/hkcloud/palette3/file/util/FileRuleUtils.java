package kr.co.hkcloud.palette3.file.util;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.common.date.util.DateCmmnUtils;
import kr.co.hkcloud.palette3.common.innb.util.InnbCreateUtils;
import kr.co.hkcloud.palette3.exception.PaletteAppException;
import kr.co.hkcloud.palette3.exception.PaletteUtilException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import kr.co.hkcloud.palette3.file.domain.FileRequest.FileUploadRequests;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.RuleFilenameAndExtentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 파일 규칙 유틸
 * 
 * @author RND
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FileRuleUtils
{
    private final InnbCreateUtils innbCreateUtils;


    /**
     * 저장 경로 규칙
     * 
     * @param  filePropertiesResponse
     * @param  파일경로
     * @return
     */
    public Path getRuleStorePath(@Valid final FilePropertiesResponse filePropertiesResponse) throws PaletteUtilException
    {
        //대상구분이 FILE/에디터인 경우만
        switch(filePropertiesResponse.getTrgtTypeCd())
        {
            case FILE:
            case EDITOR:
            {
                //업무구분에 따라 룰을 다르게 사용하려는 경우 사용
                switch(filePropertiesResponse.getTaskTypeCd())
                {
                    default:
                    {
                        final String year = DateCmmnUtils.getCurrentTime("yyyy");
                        final String month = DateCmmnUtils.getCurrentTime("MM");
                        final String day = DateCmmnUtils.getCurrentTime("dd");

                        //파일 디렉터리 생성은 최종 저장 시 생성할 것
                        final Path storePath = Paths.get(year, month, day);
                        log.debug("storePath :{}", storePath);
                        return storePath;
                    }
                }
            }
            default:
                throw new PaletteUtilException(String.format("레포지토리 대상 구분이 FILE인 경우만 유효합니다. %1$s", filePropertiesResponse.getTrgtTypeCd()));
        }
    }


    /**
     * 파일명과 확장자 규칙 (실제 저장할 파일명 및 확장자)
     * 
     * @param  filePropertiesResponse
     * @param  filename
     * @return
     * @throws TelewebUtilException
     */
    public RuleFilenameAndExtentResponse getRuleFilenameAndExtent(@Valid final FilePropertiesResponse filePropertiesResponse, @NotBlank final String actlFileNm, String fileExtent) throws PaletteUtilException
    {
        String ruleFilenameExtent = null;

        //대상구분이 FILE과 에디터인 경우만
        switch(filePropertiesResponse.getTrgtTypeCd())
        {
            case FILE:
            case EDITOR:
            {
                final String cleanFilename = StringUtils.cleanPath(actlFileNm);
                final String cleanFileNameWithOutExt = FilenameUtils.removeExtension(cleanFilename);
                final String cleanFileExtent = StringUtils.getFilenameExtension(cleanFilename);

                //업무구분에 따라 룰을 다르게 사용하려는 경우 사용
                switch(filePropertiesResponse.getTaskTypeCd())
                {
                    //기본값: 밀리초_UUID.확장자
                    default:
                    {
                        //경로구분에 따른 처리
                        switch(filePropertiesResponse.getPathTypeCd())
                        {
                            //이미지: 확장자 붙임 - 밀리초_UUID.확장자 (/repository-{업무}-image 로 엑세스 가능)
                            case images: case videos:
                            {
                                //ruleFilenameExtent = new StringBuffer().append(DateCmmnUtils.toEpochMilli()).append("_").append(innbCreateUtils.getUUID()).append(".").append(cleanFileExtent).toString();
                                ruleFilenameExtent = new StringBuffer().append(DateCmmnUtils.toEpochMilli()).append("_").append(innbCreateUtils.getUUID()).append(".").append(fileExtent).toString();
                                break;
                            }
                            //기본값: 확장자 안붙임 - 밀리초_UUID (확장자 없음, 확장자는 디비에서 관리)
                            default:
                            {
                                //확장자 체크

                                ruleFilenameExtent = new StringBuffer().append(DateCmmnUtils.toEpochMilli()).append("_").append(innbCreateUtils.getUUID()).toString();
                                break;
                            }
                        }
                    }
                }
                log.debug("=> taskTypeCd: {}, trgtTypeCd: {}, pathTypeCd: {}, ruleFilenameExtent: {}", filePropertiesResponse.getTaskTypeCd(), filePropertiesResponse.getTrgtTypeCd(), filePropertiesResponse
                    .getPathTypeCd(), ruleFilenameExtent);

                return RuleFilenameAndExtentResponse.builder().actlFileNm(actlFileNm).cleanFilename(cleanFilename).cleanFileNameWithOutExt(cleanFileNameWithOutExt).cleanFileExtent(cleanFileExtent)
                    .ruleFilenameExtent(ruleFilenameExtent).build();
            }
            default:
            {
                throw new PaletteUtilException(String.format("레포지토리 대상 구분이 FILE인 경우만 유효합니다. %1$s", filePropertiesResponse.getTrgtTypeCd()));
            }
        }
    }


    /**
     * 파일그룹키 생성 규칙
     * 
     * @param  fileUploadRequests
     * @return                            생성된 파일그룹키
     * @throws FileRuleInnbCreatException
     */
    public String ifCreatFileGroupKey(@Valid final FileUploadRequests fileUploadRequests) throws PaletteUtilException
    {
        final String inNowFileGroupKey = fileUploadRequests.getFileGroupKey();

        //없으면 생성 후 리턴
        if(StringUtils.isEmpty(inNowFileGroupKey)) {
            //생성 규칙 : {밀리초}-{업무구분(대문자)}-{UUID}
            final String outFileGroupKey = new StringBuffer().append(DateCmmnUtils.toEpochMilli()).append("-").append(innbCreateUtils.getUUID()).toString();
            log.debug("outFileGroupKey :{}", outFileGroupKey);
            return outFileGroupKey;
        }
        //있으면 그대로 리턴
        log.debug("inNowFileGroupKey :{}", inNowFileGroupKey);
        return inNowFileGroupKey;
    }


    /**
     * 파일키 생성 규칙
     * 
     * @return                            생성된 파일키
     * @throws FileRuleInnbCreatException
     */
    public String creatFileKey() throws PaletteUtilException
    {
        //생성 규칙 : {밀리초}-{경로구분(대문자)}-{UUID}
        final String outFileKey = new StringBuffer().append(DateCmmnUtils.toEpochMilli()).append("-").append(innbCreateUtils.getUUID()).toString();
        log.debug("outFileKey :{}", outFileKey);
        return outFileKey;
    }


    /**
     * MediaType
     * 
     * @param  resource
     * @return
     * @throws PaletteUtilException
     */
    public MediaType getMediaType(Resource resource) throws PaletteUtilException
    {
        //https://www.baeldung.com/java-file-mime-type
        //https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
        final Tika tika = new Tika();
        //메타 정보 기반
        String tikaMimeType = null;
        try(InputStream inputStream = resource.getInputStream()) {
            tikaMimeType = tika.detect(inputStream);
            log.debug("tikaMimeType: {}", tikaMimeType);

            return MediaType.parseMediaType(tikaMimeType);
        }
        catch(IOException e1) {
            log.error(e1.getLocalizedMessage(), e1);
            throw new PaletteUtilException(e1.getLocalizedMessage(), e1);
        }
    }


    /**
     * 파일확장자 생성 규칙
     * 
     * @return                            생성된 파일키
     * @throws FileRuleInnbCreatException
     */
    public String getIfContentType(MultipartFile file) throws PaletteUtilException
    {
        String contentType = file.getContentType();

        if(StringUtils.isEmpty(contentType)) {
            contentType = getMediaType(file.getResource()).toString();
        }

        return contentType;
    }


    /**
     * 파일확장자 생성 규칙
     * 
     * @return                            생성된 파일키
     * @throws FileRuleInnbCreatException
     */
    public String getIfFileExtent(MultipartFile file) throws PaletteUtilException
    {
        final String actlFileNm = file.getOriginalFilename();
        //Check if the file's name contains invalid characters
        if(actlFileNm.contains("..")) { throw new PaletteAppException(String.format("Sorry! Filename contains invalid path sequence %$1s", actlFileNm)); }
        final String cleanFilename = StringUtils.cleanPath(actlFileNm);
        String fileExtent = StringUtils.getFilenameExtension(cleanFilename);

        if(StringUtils.isEmpty(fileExtent) || "null".equals(fileExtent)) {
            final TikaConfig config = TikaConfig.getDefaultConfig();
            final MediaType mediaType = getMediaType(file.getResource());
            final MimeType mimeTypeCd;
            try {
                mimeTypeCd = config.getMimeRepository().forName(mediaType.toString());
            }
            catch(MimeTypeException e) {
                throw new PaletteUtilException(e);
            }
            fileExtent = mimeTypeCd.getExtension().replaceAll("\\.", "");
        }

        return fileExtent;
    }


    /**
     * 파일확장자 생성 규칙
     * 
     * @return                            생성된 파일키
     * @throws FileRuleInnbCreatException
     */
    public String getIfFileExtent(FileDownloadResponse fileDownloadResponse) throws PaletteUtilException
    {
        final String actlFileNm = fileDownloadResponse.getActlFileNm();
        //Check if the file's name contains invalid characters
        if(actlFileNm.contains("..")) { throw new PaletteAppException(String.format("Sorry! Filename contains invalid path sequence %$1s", actlFileNm)); }
        final String cleanFilename = StringUtils.cleanPath(actlFileNm);
        String fileExtent = StringUtils.getFilenameExtension(cleanFilename);

        if(StringUtils.isEmpty(fileExtent) || "null".equals(fileExtent)) {
            final TikaConfig config = TikaConfig.getDefaultConfig();
            final MediaType mediaType = MediaType.parseMediaType(fileDownloadResponse.getMimeTypeCd());
            final MimeType mimeTypeCd;
            try {
                mimeTypeCd = config.getMimeRepository().forName(mediaType.toString());
            }
            catch(MimeTypeException e) {
                throw new PaletteUtilException(e);
            }
            fileExtent = mimeTypeCd.getExtension().replaceAll("\\.", "");
        }

        return fileExtent;
    }
}
