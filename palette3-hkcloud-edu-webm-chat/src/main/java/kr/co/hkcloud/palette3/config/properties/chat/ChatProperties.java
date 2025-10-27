package kr.co.hkcloud.palette3.config.properties.chat;


import java.net.URI;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import kr.co.hkcloud.palette3.config.properties.chat.enumer.ChatAwayStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 채팅(chat) 속성
 * 
 * @author Orange
 *
 */
@Getter
@RequiredArgsConstructor
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "chat")
public class ChatProperties
{
    @NotNull
    private final boolean enabled;

    @NotNull
    private final boolean routerEnabled;

    // 문의 유형 "이전단계로" 이모티콘 설정 , 문의유형 메시지/챗봇 이벤트명 이모티콘은 관리화면에서 직접설정한다. 예 (윙크)
    @NotBlank
    private final String inqryPreStepMsg;
    //# 이석상태
    @NotNull
    private final ChatAwayStatus chatOn;
    @NotNull
    private final ChatAwayStatus chatOff;
    //# 비정상종료 시 상담중 채팅창의 TimeOut 시간( minute ) , 상담원 비정상 종료시 상담중이던 채팅칭의 대기 시간 설정  (분)
    @NotNull
    private final int agentNonResponseTimeout = 3; //Duration.ofMinutes(3);
    //# 상담사 수락대기 최대 건 수
    @NotNull
    private final int maxAgentChat = 3;

    @NotBlank
    private final String workStartTime = "0800";
    //# 상담 종료 시간 (사용안하는 지 확인 후 삭제??)
    @NotBlank
    private final String workEndTime = "0100";

    private final Messenger messenger;
    private final Support   support;
    private final Chatbot   chatbot;


    //메신저 속성
    @Getter
    @RequiredArgsConstructor
    public static final class Messenger
    {
        private final Kakaotalk kakaotalk;
        private final Ttalk     ttalk;
        private final Line      line;
        private final Alimtalk  alimtalk;
        private final Navertalktalk navertalktalk;


        @Getter
        @RequiredArgsConstructor
        public static final class Kakaotalk
        {
            @NotNull
            private final boolean enabled;

            @NotBlank
            private final String senderkey;

            @NotNull
            private final URI baseUrl;

            private final Urls urls;


            @Getter
            @RequiredArgsConstructor
            public static final class Urls
            {
                @NotNull
                private final URI profileChatActivate;

                @NotNull
                private final URI profileChatDeactivate;

                @NotNull
                private final URI profileUserBlock;

                @NotNull
                private final URI profileUserUnblock;

                @NotNull
                private final URI chatWrite;

                @NotNull
                private final URI chatEnd;

                @NotNull
                private final URI chatEndWithBot;

                @NotNull
                private final URI imageUpload;
            }
        }


        @Getter
        @RequiredArgsConstructor
        public static final class Ttalk
        {
            @NotNull
            private final boolean enabled;

            @NotBlank
            private final String senderkey;

            @NotNull
            private final URI baseUrl;

            private final Urls urls;


            @Getter
            @RequiredArgsConstructor
            public static final class Urls
            {
                @NotNull
                private final URI chatOpen;

                @NotNull
                private final URI profile;

                @NotNull
                private final URI chatWrite;

                @NotNull
                private final URI chatEnd;

                @NotNull
                private final URI imageUpload;
            }

        }


        @Getter
        @RequiredArgsConstructor
        public static final class Line
        {
            @NotNull
            private final boolean enabled;

            @NotNull
            private final URI baseUrl;

            private final Urls urls;


            @Getter
            @RequiredArgsConstructor
            public static final class Urls
            {
                @NotNull
                private final URI botMessagePush;

                @NotNull
                private final URI botMessageContent;

                @NotNull
                private final URI expiredSession;
            }
        }
        
        
        @Getter
        @RequiredArgsConstructor
        public static final class Navertalktalk
        {
            @NotNull
            private final boolean enabled;

            @NotNull
            private final URI baseUrl;

            private final Urls urls;


            @Getter
            @RequiredArgsConstructor
            public static final class Urls
            {
                @NotNull
                private final URI expiredSession;

                @NotNull
                private final URI imageUpload;
            }
        }


        @Getter
        @RequiredArgsConstructor
        public static final class Alimtalk
        {
            @NotNull
            private final boolean enabled;

            @NotBlank
            private final String senderkey;

            @NotBlank

            private final String authCode;
            @NotBlank

            private final String tranType;

            @NotBlank
            private final String testMode;

            private final URI baseUrl;

            private final Urls urls;


            @Getter
            @RequiredArgsConstructor
            public static final class Urls
            {
                @NotNull
                private final URI TargetUrl;

            }
        }
    }


    //지원 속성
    @Getter
    @RequiredArgsConstructor
    public static final class Support
    {
        @NotNull
        private final boolean enabled;

        private final Urls urls;


        @Getter
        @RequiredArgsConstructor
        public static final class Urls
        {
            private final URI reqCustBaseInfo;

            private final URI mediationAddressMobile;

            private final URI mediationAddressWeb;
        }

    }


    //챗봇 속성
    @Getter
    @RequiredArgsConstructor
    public static final class Chatbot
    {
        private final Skill skill;


        //스킬 속성
        @Getter
        @RequiredArgsConstructor
        public static final class Skill
        {
            @NotNull
            private final boolean enabled;

            private final URI domain;

            private final String sndrKey;

            private final URI svrUrlTskill;

            private final String url;
        }

    }
}
