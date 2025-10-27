package kr.co.hkcloud.palette3.editor.domain;


import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.file.constraint.FileGroupKeyConstraint;
import kr.co.hkcloud.palette3.file.domain.FileRequest;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * 엑셀 요청 도메인
 * 
 * @author leeiy
 *
 */
public class EditorRequest
{
    /**
     * 에디터 업로드 단 건 요청
     * 
     * @author leeiy
     *
     */
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString(exclude = "upload")
    public static final class EditorUploadRequest
    {
        @NotNull
        private MultipartFile upload;  //에디터 Upload Key

        @FileGroupKeyConstraint(groups = {FileRequest.GroupService.class})
        private String fileGroupKey; // 파일그룹키

        @Setter(AccessLevel.NONE)  //수정하지 말 것!
        @Builder.Default
        @NotNull(groups = {FileRequest.GroupService.class})
        private FileAccessType fileAcsTypeCd = FileAccessType.PRIVATE;  //파일엑세스유형(기본값:비공개)

    }
}
