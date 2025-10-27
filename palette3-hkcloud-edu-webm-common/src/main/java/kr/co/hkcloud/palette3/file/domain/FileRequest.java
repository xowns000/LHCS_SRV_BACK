package kr.co.hkcloud.palette3.file.domain;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.file.constraint.FileGroupKeyConstraint;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 파일 요청 도메인
 * 
 * @author leeiy
 *
 */
public class FileRequest {

    //서비스단(@Validated groups용으로 Request단에만 존재시킨다)
    public interface GroupService {
    };

    /**
     * 파일 업로드 단 건 요청
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString(exclude = "file")
    public static final class FileUploadRequest {

        @NotNull
        private MultipartFile file;

        @FileGroupKeyConstraint(groups = {FileRequest.GroupService.class})
        private String fileGroupKey; // 파일그룹키

        @NotNull(groups = {FileRequest.GroupService.class})
        private FileAccessType fileAcsTypeCd;  //파일엑세스유형

        //@Setter(AccessLevel.PUBLIC)  //수정하지 말 것! 제한적으로 builder()로만 엑세스 하세요.
        //@Builder.Default
        //private String custcoId = ((PaletteUserDetailsVO) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getCustcoId();
        //@Setter(AccessLevel.PUBLIC)
        //@Builder.Default
        //private String custcoId = "1000";

        @Setter(AccessLevel.NONE)  //수정하지 말 것! 제한적으로 builder()로만 엑세스 하세요.
        //@Builder.Default
        private int userId;

        @Setter(AccessLevel.NONE)
        private String custcoId;

        private String aspNewreKey;
    }

    /**
     * 파일 업로드 다 건 요청
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @ToString(exclude = "userfiles")
    public static final class FileUploadRequests {

        @NotNull
        private MultipartFile[] userfiles;

        @FileGroupKeyConstraint(groups = {FileRequest.GroupService.class})
        private String fileGroupKey; // 파일그룹키

        @Setter(AccessLevel.NONE)  //수정하지 말 것!
        @Builder.Default
        @NotNull(groups = {FileRequest.GroupService.class})
        private FileAccessType fileAcsTypeCd = FileAccessType.PRIVATE;  //파일엑세스유형(기본값:비공개)

        @Setter(AccessLevel.NONE)  //수정하지 말 것! 제한적으로 builder()로만 엑세스 하세요.
        private String custcoId;

        @Setter(AccessLevel.NONE)  //수정하지 말 것! 제한적으로 builder()로만 엑세스 하세요.
        private int userId;

    }

    /**
     * 파일 다운로드 단 건 요청
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
    public static final class FileDownloadRequest {

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        private String custcoId;

        @NotBlank
        @FileGroupKeyConstraint
        private String fileKey;  // 파일키
    }

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
    public static final class DownloadImageResourceRequest {

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
    }

    /**
     * 파일 수정 요청
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
    public static final class FileUpdateRequest {

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        @NotBlank
        @FileGroupKeyConstraint
        private String fileKey;  // 파일키

        private String custcoId;

        private int userId;
    }

    /**
     * 파일 삭제 요청
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
    public static final class FileDeleteRequest {

        public List<DeleteFileKeys> deleteFileKeys;

        @Getter
        @Setter
        @ToString
        public static final class DeleteFileKeys {

            private String custcoId; // 회사구분

            @NotBlank
            @FileGroupKeyConstraint
            private String fileGroupKey; // 파일그룹키

            @NotBlank
            @FileGroupKeyConstraint
            private String fileKey;  // 파일키

            private String amdrId;

        }

    }

    /**
     * 파일 목록 조회 요청
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
    public static final class FileSelectListRequst {

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        private String custcoId; // 회사구분

        @FileGroupKeyConstraint
        private String fileKey;  // 파일키

        private RepositoryTaskTypeCd taskTypeCd; // 업무구분
        private RepositoryPathTypeCd pathTypeCd; // 경로구분
        private RepositoryTrgtTypeCd trgtTypeCd; // 저장소대상구분
        private String mimeTypeCd; // Mime Type
    }
}
