package kr.co.hkcloud.palette3.excel.domain;


import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import kr.co.hkcloud.palette3.file.constraint.FileGroupKeyConstraint;
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
public class ExcelRequest
{
    //서비스단(@Validated groups용으로 Request단에만 존재시킨다)
    public interface GroupService
    {};


    /**
     * 엑셀 업로드 단 건 요청
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
    @ToString(exclude = "excelfile")
    public static final class ExcelUploadRequest
    {
        @NotNull
        private MultipartFile excelfile;

        @FileGroupKeyConstraint(groups = {ExcelRequest.GroupService.class})
        private String fileGroupKey; // 파일그룹키
    }
}
