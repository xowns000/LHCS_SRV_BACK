package kr.co.hkcloud.palette3.file.dao.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import kr.co.hkcloud.palette3.file.constraint.FileGroupKeyConstraint;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 공통 첨부파일 디비 관리 응답 도메인
 * 
 * @author leeiy
 */
public final class FileDbMngResponse {

    /**
     * 파일 조회 저장소대상유형 응답
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @Getter
    @ToString
    public static class FileDbMngSelectTargetTypeResponse {

        @NotBlank
        private String custcoId;  //ASP고객사키

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        @NotBlank
        @FileGroupKeyConstraint
        private String fileKey; // 파일키

        @NotNull
        private FileAccessType fileAcsTypeCd;  //파일엑세스유형(PUBLIC/PRIVATE)

        @NotNull
        private RepositoryTrgtTypeCd trgtTypeCd; // 저장소대상유형

        @NotBlank
        private String actlFileNm; // 진짜 파일명
    }

    /**
     * 파일 조회 카운트 응답
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @Getter
    @ToString
    public static class FileDbMngSelectCounteResponse {

        @PositiveOrZero
        private int dbFileCount; // 파일갯수

        private String custcoId;
    }

    /**
     * 파일 조회 응답
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @Getter
    @ToString(exclude = "fileBlob")
    public static class FileDbMngSelectResponse {

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        @NotBlank
        @FileGroupKeyConstraint
        private String fileKey; // 파일키

        @NotNull
        private RepositoryTaskTypeCd taskTypeCd; // 업무구분

        @NotNull
        private RepositoryTrgtTypeCd trgtTypeCd; // 저장소대상유형

        @NotNull
        private RepositoryPathTypeCd pathTypeCd; // 경로구분

        @NotBlank
        private String mimeTypeCd; // Mime Type

        @NotBlank
        private String actlFileNm; // 진짜 파일명

        @NotBlank
        private String strgFileNm; // 저장된 파일명

        @NotBlank
        private String filePath; // 파일경로

        @PositiveOrZero
        private long fileSz; // 파일크기

        @NotBlank
        private String fileExtn; // 파일확장자

        @PositiveOrZero
        private int dwnldCnt; // 다운로드수

        @NotBlank
        private String rgtrId; // 작성자ID

        @NotBlank
        private String custcoId; // 고객사ID

        private String fileUrl; // 파일URL
        private String accessKey; // 접근키

        @NotNull(groups = FileDbMngRequest.GroupMapperBlobResponse.class)
        private byte[] fileBlob; // FILE_BLOB
    }

    /**
     * 파일 목록 조회 응답
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class FileDbMngSelectListResponse {

        @NotBlank
        private String actlFileNm; // 진짜 파일명

        @PositiveOrZero
        private long fileSz; // 파일크기

        @PositiveOrZero
        private int dwnldCnt; // 다운로드수

        @NotBlank
        private String fileExtn; // 파일확장자

        @NotBlank
        private String fileGroupKey; // 파일그룹키

        private String custcoId; // 파일그룹키

        @NotBlank
        private String fileKey; // 파일키

        private String fileSzDisplay; //크기(KB)

        private String dnlodBtn = "다운로드";  //다운로드 버튼 노출용

        private String filePath;
        private String strgFileNm;

        private String taskTypeCd;
        private String pathTypeCd;
        private String trgtTypeCd;
        private String mimeTypeCd;
    }
}
