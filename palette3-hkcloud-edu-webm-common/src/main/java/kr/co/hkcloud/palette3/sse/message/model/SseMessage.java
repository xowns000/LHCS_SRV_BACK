package kr.co.hkcloud.palette3.sse.message.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : kr.co.hkcloud.palette3.sse.message.model
 * fileName       : SseMessage
 * author         : KJD
 * date           : 2023-11-09
 * description    : << 여기 설명 >>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-09        KJD       최초 생성
 */
@Getter
@Setter
public class SseMessage {

    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String receiver;
    private String message; // 메시지
    private String pos; //토스트 표시 위치, nullable, default = top
    private String apiKey;
    private int second; //토스트 표시 시간, nullable, default = 5
    private long userCount; // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용

    public SseMessage() {
    }

    @Builder
    public SseMessage(MessageType type, String roomId, String sender, String receiver, String message, String position, int second, long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.pos = position;
        this.second = second;
        this.userCount = userCount;
    }

    // 메시지 타입 : 입장, 퇴장, 메시지
    public enum MessageType {
        ENTER, QUIT, TALK,

        SYSTEM_LOGOUT,  // 로그아웃
        SYSTEM_CHECK,   //점검
        SYSTEM_MESSAGE,
        ENV_SETTING,
    }
}
