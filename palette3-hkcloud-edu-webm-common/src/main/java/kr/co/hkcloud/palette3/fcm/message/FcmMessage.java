package kr.co.hkcloud.palette3.fcm.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmMessage {

    private String token;
    private String title; // 제목
    private String body; // 내용

    public FcmMessage() {
    }

    @Builder
    public FcmMessage(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }
}
