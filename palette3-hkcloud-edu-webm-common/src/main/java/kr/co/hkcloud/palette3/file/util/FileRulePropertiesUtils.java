package kr.co.hkcloud.palette3.file.util;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import kr.co.hkcloud.palette3.config.properties.file.FileProperties;
import kr.co.hkcloud.palette3.config.properties.file.FileProperties.Repository;
import kr.co.hkcloud.palette3.config.properties.file.FileProperties.Repository.Bbs;
import kr.co.hkcloud.palette3.config.properties.file.FileProperties.Repository.Chat;
import kr.co.hkcloud.palette3.config.properties.file.FileProperties.Repository.Km;
import kr.co.hkcloud.palette3.config.properties.file.FileProperties.Repository.Palette;
import kr.co.hkcloud.palette3.config.properties.file.FileProperties.Repository.Phone;
import kr.co.hkcloud.palette3.exception.PaletteUtilException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FileDownloadPropertiesResponse;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.FileUploadLib;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


/**
 * 파일 규칙 유틸
 *
 * @author RND
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@Component
public class FileRulePropertiesUtils {

    private final FileProperties fileProperties;


    /**
     * 파일 업무구분에 따른 경로구분 프로퍼티
     *
     * @return RulePathTypePropertiesResponse
     */
    @Valid
    public FilePropertiesResponse getProperties(@NotNull final RepositoryTaskTypeCd taskTypeCd,
        @NotNull final RepositoryPathTypeCd pathTypeCd) throws PaletteUtilException {
        FilePropertiesResponse filePropertiesResponse = null;
        final Repository repository = fileProperties.getRepository();
        switch (taskTypeCd) {
            //팔레트
            case palette: {
                final Palette palette = repository.getPalette();
                switch (pathTypeCd) {
                    //파일
                    case files: {
                        final Palette.Files files = palette.getFiles();
                        filePropertiesResponse = new FilePropertiesResponse();
                        BeanUtils.copyProperties(files, filePropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, rulePathTypePropertiesResponse={}", taskTypeCd, pathTypeCd,
                            filePropertiesResponse);
                        break;
                    }
                    default: {
                        throw new PaletteUtilException(
                            String.format("레포지토리 경로 구분이 유효하지 않습니다. %1$s>%2$s", taskTypeCd.name(), pathTypeCd.name()));
                    }
                }
                break;
            }
            //전화
            case phone: {
                final Phone phone = repository.getPhone();
                switch (pathTypeCd) {
                    //파일
                    case files: {
                        final Phone.Files files = phone.getFiles();
                        filePropertiesResponse = new FilePropertiesResponse();
                        BeanUtils.copyProperties(files, filePropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, rulePathTypePropertiesResponse={}", taskTypeCd, pathTypeCd,
                            filePropertiesResponse);
                        break;
                    }
                    default: {
                        throw new PaletteUtilException(
                            String.format("레포지토리 경로 구분이 유효하지 않습니다. %1$s>%2$s", taskTypeCd.name(), pathTypeCd.name()));
                    }
                }
                break;
            }
            //채팅
            case chat: {
                final Chat chat = repository.getChat();
                switch (pathTypeCd) {
                    //티톡 공유용
                    case publics: {
                        final Chat.Publics publics = chat.getPublics();
                        filePropertiesResponse = new FilePropertiesResponse();
                        BeanUtils.copyProperties(publics, filePropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, rulePathTypePropertiesResponse={}", taskTypeCd, pathTypeCd,
                            filePropertiesResponse);
                        break;
                    }
                    //파일
                    case files: {
                        final Chat.Files files = chat.getFiles();
                        filePropertiesResponse = new FilePropertiesResponse();
                        BeanUtils.copyProperties(files, filePropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, rulePathTypePropertiesResponse={}", taskTypeCd, pathTypeCd,
                            filePropertiesResponse);
                        break;
                    }
                    //이미지 (URI 허용 - /repository-chat-image)
                    case images: {
                        final Chat.Images images = chat.getImages();
                        filePropertiesResponse = new FilePropertiesResponse();
                        BeanUtils.copyProperties(images, filePropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, rulePathTypePropertiesResponse={}", taskTypeCd, pathTypeCd,
                            filePropertiesResponse);
                        break;
                    }
                    //비디오 (URI 허용 - /repository-chat-video)
                    case videos: {
                        final Chat.Videos videos = chat.getVideos();
                        filePropertiesResponse = new FilePropertiesResponse();
                        BeanUtils.copyProperties(videos, filePropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, rulePathTypePropertiesResponse={}", taskTypeCd, pathTypeCd,
                            filePropertiesResponse);
                        break;
                    }
                    default: {
                        throw new PaletteUtilException(
                            String.format("레포지토리 경로 구분이 유효하지 않습니다. %1$s>%2$s", taskTypeCd.name(), pathTypeCd.name()));
                    }
                }
                break;
            }
            //지식
            case km: {
                final Km km = repository.getKm();
                switch (pathTypeCd) {
                    //파일
                    case files: {
                        final Km.Files files = km.getFiles();
                        filePropertiesResponse = new FilePropertiesResponse();
                        BeanUtils.copyProperties(files, filePropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, rulePathTypePropertiesResponse={}", taskTypeCd, pathTypeCd,
                            filePropertiesResponse);
                        break;
                    }
                    case images: {
                        final Km.Images images = km.getImages();
                        filePropertiesResponse = new FilePropertiesResponse();
                        BeanUtils.copyProperties(images, filePropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, rulePathTypePropertiesResponse={}", taskTypeCd, pathTypeCd,
                            filePropertiesResponse);
                        break;
                    }
                    default: {
                        throw new PaletteUtilException(
                            String.format("레포지토리 경로 구분이 유효하지 않습니다. %1$s>%2$s", taskTypeCd.name(), pathTypeCd.name()));
                    }
                }
                break;
            }
            //게시판
            case bbs: {
                final Bbs bbs = repository.getBbs();
                switch (pathTypeCd) {
                    //파일
                    case files: {
                        final Bbs.Files files = bbs.getFiles();
                        filePropertiesResponse = new FilePropertiesResponse();
                        BeanUtils.copyProperties(files, filePropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, rulePathTypePropertiesResponse={}", taskTypeCd, pathTypeCd,
                            filePropertiesResponse);
                        break;
                    }
                    default: {
                        throw new PaletteUtilException(
                            String.format("레포지토리 경로 구분이 유효하지 않습니다. %1$s>%2$s", taskTypeCd.name(), pathTypeCd.name()));
                    }
                }
                break;
            }
            default: {
                throw new PaletteUtilException(String.format("레포지토리 업무 구분이 유효하지 않습니다. %1$s", taskTypeCd.name()));
            }
        }
        if (filePropertiesResponse != null) {
            //파일 업로드 라이브러리
            final FileUploadLib fileUploadLib = fileProperties.getFileUploadLib();

            //기본 경로
            final StringBuffer baseUri = new StringBuffer("/api/file").append("/").append(taskTypeCd.name()).append("/")
                .append(pathTypeCd.name());

            //업로드 경로
            final String uploadUri = new StringBuffer(baseUri).append("/uploads").toString();

            //다운로드 경로
            final String downloadUri = new StringBuffer(baseUri).append("/download").toString();

            filePropertiesResponse.setUploadUri(uploadUri);
            filePropertiesResponse.setDownloadUri(downloadUri);
            filePropertiesResponse.setTaskTypeCd(taskTypeCd);
            filePropertiesResponse.setPathTypeCd(pathTypeCd);
            filePropertiesResponse.setFileUploadLib(fileUploadLib);

            //타겟속성(일반파일 또는 blob 또는 clob등등 정의 ==> 현재 일반파일만 가능)
            final RepositoryTrgtTypeCd trgtTypeCd = RepositoryTrgtTypeCd.FILE;  //이미지(고객)
            filePropertiesResponse.setTrgtTypeCd(trgtTypeCd);
        }
        return filePropertiesResponse;
    }


    /**
     * 파일 다운로드 프로퍼티
     *
     * @return RulePathTypePropertiesResponse
     */
    @Valid
    public FileDownloadPropertiesResponse getDownloadProperties(@NotNull final RepositoryTaskTypeCd taskTypeCd,
        @NotNull final RepositoryPathTypeCd pathTypeCd) throws PaletteUtilException {
        //기본 경로
        final StringBuffer baseUri = new StringBuffer("/api/file").append("/").append(taskTypeCd.name()).append("/")
            .append(pathTypeCd.name());

        //다운로드 경로
        final String downloadUri = new StringBuffer(baseUri).append("/download").toString();

        // @formatter:off
        return FileDownloadPropertiesResponse.builder()
                                             .enabled(true)
                                             .downloadUri(downloadUri)
                                             .taskTypeCd(taskTypeCd)
                                             .pathTypeCd(pathTypeCd)
                                             .build();
        // @formatter:on
    }


    /**
     * 파일 사이즈 display
     *
     * @return human file size display
     */
    public String toFileSizeDisplay(Long bytes) throws PaletteUtilException {
        return FileUtils.byteCountToDisplaySize(bytes);
    }

}
