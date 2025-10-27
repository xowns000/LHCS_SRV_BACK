package kr.co.hkcloud.palette3.file.dao.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import kr.co.hkcloud.palette3.core.domain.DeleteDbBase;
import kr.co.hkcloud.palette3.core.domain.InsertDbBase;
import kr.co.hkcloud.palette3.core.domain.SelectDbBase;
import kr.co.hkcloud.palette3.core.domain.UpdateDbBase;
import kr.co.hkcloud.palette3.file.constraint.FileAccessTypeConstraint;
import kr.co.hkcloud.palette3.file.constraint.FileGroupKeyConstraint;
import kr.co.hkcloud.palette3.file.enumer.FileAccessType;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTrgtTypeCd;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 공통 첨부파일 디비 관리 요청 도메인
 * 
 * @author leeiy
 */
public class FileDbMngRequest {

    // @formatter:off
    public interface GroupServiceInsert {};
    public interface GroupServiceUpdate {};
    public interface GroupServiceInsertDb {};
    public interface GroupServiceUpdateDb {};
    
    public interface GroupMapperInsert {};
    public interface GroupMapperUpdate {};
    public interface GroupMapperInsertDb {};
    public interface GroupMapperUpdateDb {};

    public interface GroupMapperBlobResponse {};
    public interface GroupServiceSelectTargetType {};
    public interface GroupServiceSelectCount {};
    public interface GroupServiceDelete {}; //단 건 삭제 시
    // @formatter:on

    /**
     * 데이터 삽입 요청
     * 
     * @author leeiy
     *
     */
    @Getter
    @Setter
    @SuperBuilder
    @ToString(callSuper = true, exclude = "fileBlob")
    public static final class FileDbMngInsertRequest extends InsertDbBase {

        @NotNull
        private FileAccessType fileAcsTypeCd;  //파일엑세스유형

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        @NotBlank(groups = {FileDbMngRequest.GroupMapperInsert.class, FileDbMngRequest.GroupMapperInsertDb.class})
        @FileGroupKeyConstraint
        private String fileKey; // 파일키

        @NotNull
        private RepositoryTaskTypeCd taskTypeCd; // 업무구분

        @NotNull
        private RepositoryTrgtTypeCd trgtTypeCd; // 저장소대상구분

        @NotNull
        private RepositoryPathTypeCd pathTypeCd; // 경로구분

        @NotBlank
        private String mimeTypeCd; // Mime Type

        @NotBlank
        private String actlFileNm; // 진짜 파일명

        @NotBlank(groups = {FileDbMngRequest.GroupServiceInsert.class, FileDbMngRequest.GroupServiceUpdate.class,
            FileDbMngRequest.GroupMapperInsert.class, FileDbMngRequest.GroupMapperUpdate.class})
        private String strgFileNm; // 저장된 파일명

        @NotBlank(groups = {FileDbMngRequest.GroupServiceInsert.class, FileDbMngRequest.GroupServiceUpdate.class,
            FileDbMngRequest.GroupMapperInsert.class, FileDbMngRequest.GroupMapperUpdate.class})
        private String filePath; // 파일경로

        @PositiveOrZero
        private long fileSz; // 파일크기

        @NotBlank
        private String fileExtn; // 파일확장자

        @PositiveOrZero
        private int dwnldCnt; // 다운로드수

        @NotNull(groups = {FileDbMngRequest.GroupServiceInsertDb.class, FileDbMngRequest.GroupServiceUpdateDb.class,
            FileDbMngRequest.GroupMapperInsertDb.class, FileDbMngRequest.GroupMapperUpdateDb.class}) // 그룹을 지정할 수 있다. (기본 값은 javax.validation.groups.Default)
        private byte[] fileBlob; // FILE_BLOB

        private String fileUrl; // 파일URL
        private String accessKey; // 접근키

        private String custcoId;
    }

    /**
     * 파일 조회 요청
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @Getter
    @Setter
    @SuperBuilder
    public static final class FileDbMngSelectRequest extends SelectDbBase {

        @NotBlank(groups = {FileDbMngRequest.GroupServiceSelectTargetType.class, FileDbMngRequest.GroupServiceSelectCount.class})
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        @NotBlank(groups = {FileDbMngRequest.GroupServiceSelectTargetType.class, FileDbMngRequest.GroupServiceSelectCount.class})
        private String custcoId; // 회사구분키

        @NotBlank(groups = {FileDbMngRequest.GroupServiceSelectTargetType.class})
        @FileGroupKeyConstraint
        private String fileKey;  // 파일키

        @NotNull
        @Builder.Default
        @FileAccessTypeConstraint
        private FileAccessType fileAcsTypeCd = FileAccessType.PRIVATE; //파일엑세스유형(기본값:비공개)

        private RepositoryTaskTypeCd taskTypeCd; // 업무구분
        private RepositoryPathTypeCd pathTypeCd; // 경로구분
        private RepositoryTrgtTypeCd trgtTypeCd; // 저장소대상구분
        private String mimeTypeCd; // Mime Type

    }

    /**
     * 데이터 업데이트 요청
     * 
     * @author leeiy
     *
     */
    @NoArgsConstructor
    @Getter
    @Setter
    @SuperBuilder
    @ToString(callSuper = true, exclude = "fileBlob")
    public static final class FileDbMngUpdateRequest extends UpdateDbBase {

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        @NotBlank
        @FileGroupKeyConstraint
        private String fileKey; // 파일키

        private String taskTypeCd; // 업무구분
        private String pathTypeCd; // 경로구분
        private String mimeTypeCd; // Mime Type
        private String actlFileNm; // 진짜 파일명
        private String strgFileNm; // 저장된 파일명
        private String filePath; // 파일경로
        private long fileSz; // 파일크기
        private String fileExtn; // 파일확장자
        private int dwnldCnt; // 다운로드수
        private String fileUrl; // 파일URL
        private String accessKey; // 접근키
        private int userId; // 처리자ID (기본값: SYSTEM)
        private byte[] fileBlob; // FILE_BLOB
    }

    /**
     * 데이터 삭제 요청
     * 
     * @author leeiy
     *
     */
    @Getter
    @Setter
    @SuperBuilder
    @ToString(callSuper = true)
    public static final class FileDbMngDeleteRequest extends DeleteDbBase {

        @NotBlank
        @FileGroupKeyConstraint
        private String fileGroupKey; // 파일그룹키

        private String custcoId; // 파일그룹키

        @NotBlank
        @FileGroupKeyConstraint
        private String fileKey;// 파일키
    }
}
