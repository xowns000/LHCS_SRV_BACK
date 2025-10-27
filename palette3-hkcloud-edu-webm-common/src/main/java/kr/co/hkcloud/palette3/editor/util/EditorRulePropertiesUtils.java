package kr.co.hkcloud.palette3.editor.util;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.config.properties.editor.EditorProperties;
import kr.co.hkcloud.palette3.config.properties.editor.EditorProperties.Repository;
import kr.co.hkcloud.palette3.config.properties.editor.EditorProperties.Repository.Bbs;
import kr.co.hkcloud.palette3.config.properties.editor.EditorProperties.Repository.Km;
import kr.co.hkcloud.palette3.config.properties.editor.enumer.PaletteEditor;
import kr.co.hkcloud.palette3.editor.domain.EditorResponse.EditorPropertiesResponse;
import kr.co.hkcloud.palette3.exception.PaletteUtilException;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 엑셀 규칙 유틸
 * 
 * @author RND
 *
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@Component
public class EditorRulePropertiesUtils
{
    private final EditorProperties editorProperties;


    /**
     * 파일 업무구분에 따른 경로구분 프로퍼티
     * 
     * @param  taskTypeCd
     * @param  pathTypeCd
     * @return                   EditorPropertiesResponse
     * @throws FileRuleException
     */
    @Valid
    public EditorPropertiesResponse getProperties(@NotNull final RepositoryTaskTypeCd taskTypeCd, @NotNull final RepositoryPathTypeCd pathTypeCd) throws PaletteUtilException
    {
        EditorPropertiesResponse editorPropertiesResponse = null;
        final Repository repository = editorProperties.getRepository();
        switch(taskTypeCd)
        {
            //지식
            case km:
            {
                final Km km = repository.getKm();
                switch(pathTypeCd)
                {
                    //이미지
                    case images:
                    {
                        final Km.Images images = km.getImages();
                        editorPropertiesResponse = new EditorPropertiesResponse();
                        BeanUtils.copyProperties(images, editorPropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, editorPropertiesResponse={}", taskTypeCd, pathTypeCd, editorPropertiesResponse);
                        break;
                    }
                    default:
                    {
                        throw new PaletteUtilException(String.format("에디터 레포지토리 경로 구분이 유효하지 않습니다. %1$s>%2$s", taskTypeCd.name(), pathTypeCd.name()));
                    }
                }
                break;
            }
            //게시판
            case bbs:
            {
                final Bbs bbs = repository.getBbs();
                switch(pathTypeCd)
                {
                    //이미지
                    case images:
                    {
                        final Bbs.Images images = bbs.getImages();
                        editorPropertiesResponse = new EditorPropertiesResponse();
                        BeanUtils.copyProperties(images, editorPropertiesResponse);
                        log.debug("taskTypeCd={}, pathTypeCd={}, editorPropertiesResponse={}", taskTypeCd, pathTypeCd, editorPropertiesResponse);
                        break;
                    }
                    default:
                    {
                        throw new PaletteUtilException(String.format("에디터 레포지토리 경로 구분이 유효하지 않습니다. %1$s>%2$s", taskTypeCd.name(), pathTypeCd.name()));
                    }
                }
                break;
            }
            default:
            {
                throw new PaletteUtilException(String.format("에디터 레포지토리 업무 구분이 유효하지 않습니다. %1$s", taskTypeCd.name()));
            }
        }
        if(editorPropertiesResponse != null) {
            //파일 업로드 라이브러리
            final PaletteEditor name = editorProperties.getName();

            // @formatter:off
            //업로드 경로 .append("/").append(taskTypeCd.name()).append("/").append(pathTypeCd.name())
            final StringBuffer sbUploadUri = new StringBuffer("/api/editor").append("/")
                                                      .append(taskTypeCd.name()).append("/")
                                                      .append(pathTypeCd.name()).append("/")
                                                      .append("upload");

            editorPropertiesResponse.setUploadUri(sbUploadUri.toString());
            editorPropertiesResponse.setTaskTypeCd(taskTypeCd);
            editorPropertiesResponse.setPathTypeCd(pathTypeCd);
            editorPropertiesResponse.setName(name);
            editorPropertiesResponse.setVersion(editorProperties.getVersion());
           // @formatter:on
        }
        return editorPropertiesResponse;
    }

}
