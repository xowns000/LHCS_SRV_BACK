package kr.co.hkcloud.palette3.core.chat.stomp.domain;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessage
{
    public ChatMessage() {
    }

    @Builder
    public ChatMessage(ChatType chatType, ChatEvent chatEvent, MessageEvent messageEvent
            , String roomId, String userId, String userName, String userNickname, String userKey, String message, long userCount, String telewebJsonString
            , String senderKey, String contactId, String sessionId, String custcoId)
    {
        this.chatType = chatType;
        this.chatEvent = chatEvent;
        this.messageEvent = messageEvent;
        this.roomId = roomId;
        this.userId = userId;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userKey = userKey;
        this.message = message;
        this.userCount = userCount;
        this.telewebJsonString = telewebJsonString;
        this.senderKey = senderKey;
        this.contactId = contactId;
        this.sessionId = sessionId;
        this.custcoId = custcoId;
    }
    
    /**
     * 채팅 구분 : 대기, 상담, 3자 
     */
    public enum ChatType
    {
        READY, INOUT, CONSULT
    }
    
    /**
     * 이벤트 구분 : 상담사대기, 배정알람, 전달배정알람,
     *               고객연결, 상담중
     *               지원연결, 지원중
     *               고객세션만료, 상담사세션만료, 3자세션만료, 고객무응답만료
     *               연결분리, 연결정리
     *               고객무응답
     */
    public enum ChatEvent
    {
        AGENT_READY, READY_ALRAM, TRANS_READY_ALRAM,
        INOUT_CONN, IN_OUT,
        CONSULT_CONN, CONSULT,
        EXPIRED_SESSION_CUST, EXPIRED_SESSION_AGENT, EXPIRED_AGENT_SESSION, EXPIRED_NORESPONSE,
        DISCONNECT, CLEAR_DISCONNECT,
        NO_CUSTOMER_RESPONSE,
        UNSUBSCRIBE
    }
    
    /**
     * 메시지 구분 : 입장, 퇴장, 채팅, 시스템 , 세션만료
     */
    public enum MessageEvent
    {
        ENTER, QUIT, TALK, SYSTEM, SESSIONOUT
    }

    @NotEmpty private ChatType     chatType;      //채팅 구분
    @NotEmpty private ChatEvent    chatEvent;     //채팅 이벤트
    @NotEmpty private MessageEvent messageEvent;  //메시지 이벤트
    @NotEmpty private String       userId;        //상담사ID
    
    private String       userName;           //상담사명
    private String       userNickname;       //상담사닉네임
    private String       roomId;             //방번호
    private String       userKey;            //유저키
    private String       message;            //메시지
    private String       senderKey;
    private String       contactId;
    private String       sessionId;
    private long         userCount;          //채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용
    private String       telewebJsonString;  //TelewebJSON 데이터
    private String       custcoId;         //CUSTCO_ID
}
