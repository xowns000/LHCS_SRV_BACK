package kr.co.hkcloud.palette3.infra.file.domain;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import kr.co.hkcloud.palette3.file.constraint.FileAccessTypeConstraint;
import kr.co.hkcloud.palette3.file.constraint.FileGroupKeyConstraint;
import kr.co.hkcloud.palette3.file.domain.FileRequest;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * 대외 파일 요청 도메인
 * 
 * @author leeiy
 *
 */
public class InfraFileRequest
{
    public interface GroupServiceSelectTargetTypeInfra
    {};


    /**
     * 리소스 이미지 다운로드 단 건 요청
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString
    public static final class InfraDownloadImageResourceRequest
    {
        @NotBlank
        private String custcoId; // ASP고객사키

        @NotNull
        private RepositoryTaskTypeCd taskTypeCd; // 업무구분

        @NotNull
        private RepositoryPathTypeCd pathTypeCd; // 경로구분

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        @NotBlank
        @FileGroupKeyConstraint
        private String fileKey;  // 파일키

        @FileAccessTypeConstraint
        @Setter(AccessLevel.NONE)  //수정하지 말 것!
        @Builder.Default
        @NotNull(groups = {FileRequest.GroupService.class})
        private FileAccessType fileAcsTypeCd = FileAccessType.PUBLIC;  //파일엑세스유형(기본값:공개)
    }
}
