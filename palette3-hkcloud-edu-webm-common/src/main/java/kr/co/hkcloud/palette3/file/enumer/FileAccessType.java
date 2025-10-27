package kr.co.hkcloud.palette3.file.enumer;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 파일 엑세스 유형
 * 
 * @author leeiy
 *
 */
@Getter
@AllArgsConstructor
public enum FileAccessType
{
    //비공개(기본값)
    PRIVATE,

    //공개
    //DMZ 구간 밖(외부)에서 접근이 필요한 경우 사용
    // - /public/api/file/{taskTypeCd}/{pathTypeCd}/{fileGroupKey}/{fileKey} URL 사용
    PUBLIC
}
