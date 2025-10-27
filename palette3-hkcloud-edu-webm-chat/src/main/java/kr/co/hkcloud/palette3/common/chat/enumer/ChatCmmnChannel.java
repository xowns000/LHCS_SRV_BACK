package kr.co.hkcloud.palette3.common.chat.enumer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatCmmnChannel
{
    KAKAO ("1", true, "카카오톡메신저"),
    TTALK ("2", true, "티톡메신저")
    ;
    
    private final String code;
    private final boolean use;
    private final String description;
}
