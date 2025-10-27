package kr.co.hkcloud.palette3.core.model;


import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 공통 에러메시지
 * 
 * @author R&D
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PaletteValidationCode
{
    //@formatter:off
    //COMMON 에러 메시지
    DATA_NOT_EXIST      (400, "V001", "데이터가 존재하지 않습니다."),
    DATA_NOT_INPUT      (400, "V002", "데이터가 입력되지 않았습니다."),
    EXCEED_INPUT        (400, "V003", "제한된 길이를 초과했습니다."),
    IS_NOT_NUMBER       (400, "V004", "숫자가 아닙니다."),
    IS_NOT_CHAR         (400, "V005", "문자가 아닙니다."),
    WRONG_DATA_FORMAT   (400, "V006", "잘못된 입력입니다."),
    FORM_NO_EXIST       (400, "V007", "폼객체가 존재하지 않습니다."),
    EMPTY               (400, "V008", "파라미터 전달이 잘못되었습니다."),
    
    DATA_IS_NOT_ALLOWED (400, "V101", "허용되지 않는 데이터입니다."),
    
    //Storage is inactive.
    STORAGE_INACTIVE    (400, "E200", "저장소가 비활성 상태입니다."),

    //You have exceeded the number of allowed attachments.
    EXCEEDED_THE_NUMBER_ALLOWED_ATTACH    (400, "E210", "허용된 첨부파일 갯수를 초과했습니다."),

    //Files without extension cannot be uploaded.
    FILES_WITHOUT_EXTENSION    (400, "E220", "확장자가 없는 파일은 업로드할 수 없습니다."),

    //Error checking allowed files.
    ERROR_CHECKING_ALLOWED_FILES    (400, "E230", "허용된 파일 확인 중 오류가 발생했습니다."),

    //File meta information is not checked.
    FILE_META_INFO_NOT_CHECKED    (400, "E240", "파일 메타정보가 확인되지 않습니다."),

    //There are files that have been rejected for upload.
    REJECTED_FOR_UPLOAD    (400, "E250", "업로드가 거부된 파일이 있습니다."),
    
    //There are files that have been rejected for upload.
    REJECTED_FOR_UPLOAD_PRIVATE    (400, "E251", "업로드가 거부되었습니다. (비공개가 아님)"),
    
    //There are files that have been rejected for download.
    REJECTED_FOR_DOWNLOAD    (400, "E251", "다운로드가 거부된 파일이 있습니다."),
    
    REJECTED_ONLY_IMAGES    (400, "E252", "이미지 리소스만 다운로드 가능합니다."),

    //The allowed capacity has been exceeded.
    ALLOWED_SIZE_EXCEEDED    (400, "E260", "허용된 용량을 초과했습니다."),

    UNDEFINED_TYPE      (400, "V990", "정의되지 않은 유형입니다."),
    UNDEFINED_METHOD    (400, "V991", "정의되지 않은 메서드입니다."),
    NO_WORKING_TIME     (400, "V992", "업무시간중에는 처리 할 수 없습니다."),
    UNDEFINED_SERVICE   (400, "V993", "정의되지 않은 서비스입니다.")
    ;
    
    private final int status;
    private final String code;
    private final String message;
    
    //에러코드로 TelewebMssage 리턴
    public static PaletteValidationCode getTwbMsgByCode(String errorCode) {
        return Arrays.stream(PaletteValidationCode.values())
                     .filter(code -> code.getCode().equals(errorCode))
                     .findAny()
                     .orElse(PaletteValidationCode.EMPTY);
    }
 // @formatter:on

}
