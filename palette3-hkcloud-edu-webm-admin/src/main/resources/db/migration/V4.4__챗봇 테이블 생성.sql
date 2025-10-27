CREATE TABLE IF NOT EXISTS chatbot_block
(
    block_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    spst_block_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    block_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    sort_ord integer,
    reg_dttm timestamp with time zone,
    skill_yn character varying(1) COLLATE pg_catalog."default",
    skill_url character varying(300) COLLATE pg_catalog."default",
    basic_yn character varying(1) COLLATE pg_catalog."default",
    CONSTRAINT chatbot_block_pkey PRIMARY KEY (block_id)
);


COMMENT ON TABLE chatbot_block
    IS '시나리오 & 블록 테이블';

COMMENT ON COLUMN chatbot_block.basic_yn
    IS '기본시나리오 여부';

    
CREATE TABLE IF NOT EXISTS chatbot_card_button
(
    block_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    response_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    card_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    button_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    button_name character varying(30) COLLATE pg_catalog."default",
    button_type character varying(20) COLLATE pg_catalog."default",
    reg_dttm timestamp with time zone,
    upd_dttm timestamp with time zone,
    etc_info character varying(200) COLLATE pg_catalog."default",
    sort_ord integer,
    CONSTRAINT chatbot_card_button_pkey PRIMARY KEY (block_id, response_id, card_id, button_id)
);


COMMENT ON TABLE chatbot_card_button
    IS '봇 응답 카드의 버튼 테이블';

COMMENT ON COLUMN chatbot_card_button.button_type
    IS '블록, URL, 전화, 메시지 전송, 상담원 연결';

COMMENT ON COLUMN chatbot_card_button.etc_info
    IS '타입에 따라 블록id, url, 전화번호, 메시지 저장';
    
    
    
CREATE TABLE IF NOT EXISTS chatbot_channel
(
    channel_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    sender_key character varying(100) COLLATE pg_catalog."default" NOT NULL,
    channel_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    msg_cd character varying(10) COLLATE pg_catalog."default" NOT NULL,
    reg_dttm timestamp with time zone NOT NULL,
    CONSTRAINT chatbot_channel_pkey PRIMARY KEY (channel_id)
);


    

CREATE TABLE IF NOT EXISTS chatbot_file
(
    file_key character varying(30) COLLATE pg_catalog."default" NOT NULL,
    file_path character varying(200) COLLATE pg_catalog."default" NOT NULL,
    file_name character varying(1000) COLLATE pg_catalog."default" NOT NULL,
    file_original_name character varying(1000) COLLATE pg_catalog."default" NOT NULL,
    mime_type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    file_size integer NOT NULL,
    reg_dttm timestamp with time zone NOT NULL,
    CONSTRAINT chatbot_file_pkey PRIMARY KEY (file_key)
);


CREATE TABLE IF NOT EXISTS chatbot_history
(
    bot_id character varying(100) COLLATE pg_catalog."default" NOT NULL,
    user_key character varying(100) COLLATE pg_catalog."default" NOT NULL,
    message_id character varying(30) COLLATE pg_catalog."default" NOT NULL,
    sender character varying(10) COLLATE pg_catalog."default" NOT NULL,
    event character varying(20) COLLATE pg_catalog."default" NOT NULL,
    type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    content character varying(8000) COLLATE pg_catalog."default" NOT NULL,
    msg_cd character varying(10) COLLATE pg_catalog."default" NOT NULL,
    reg_dttm time with time zone NOT NULL,
    CONSTRAINT chatbot_history_pkey PRIMARY KEY (bot_id, user_key, message_id)
);

COMMENT ON TABLE chatbot_history
    IS '챗봇 대화이력(현재는 네이버톡톡만 사용)';
    
    
CREATE TABLE IF NOT EXISTS chatbot_quick_button
(
    block_id character varying(30) COLLATE pg_catalog."default" NOT NULL,
    button_id character varying(30) COLLATE pg_catalog."default" NOT NULL,
    button_name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    button_type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    etc_info character varying(200) COLLATE pg_catalog."default" NOT NULL,
    sort_ord integer NOT NULL,
    reg_dttm timestamp with time zone NOT NULL,
    upd_dttm timestamp with time zone NOT NULL,
    CONSTRAINT chatbot_quick_button_pkey PRIMARY KEY (block_id, button_id)
);


COMMENT ON COLUMN chatbot_quick_button.button_type
    IS '블록, 메시지 전송';

COMMENT ON COLUMN chatbot_quick_button.etc_info
    IS '타입에 따라 블록id,  메시지 저장';
    
    
    
CREATE TABLE IF NOT EXISTS chatbot_response
(
    block_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    response_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    level integer,
    reg_dttm timestamp with time zone,
    upd_dttm timestamp with time zone,
    CONSTRAINT chatbot_response_pkey PRIMARY KEY (block_id, response_id)
);



COMMENT ON TABLE chatbot_response
    IS '봇응답 테이블';
    
    
    
CREATE TABLE IF NOT EXISTS chatbot_response_card
(
    block_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    response_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    card_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    file_key character varying(30) COLLATE pg_catalog."default",
    text character varying(3200) COLLATE pg_catalog."default",
    reg_dttm timestamp with time zone NOT NULL,
    upd_dttm timestamp with time zone,
    sort_ord integer,
    CONSTRAINT chatbot_response_card_pkey PRIMARY KEY (block_id, response_id, card_id)
);


COMMENT ON TABLE chatbot_response_card
    IS '봇 응답 카드정보(상세정보)';
    
    
    
CREATE TABLE IF NOT EXISTS chatbot_user
(
    bot_id character varying(100) COLLATE pg_catalog."default" NOT NULL,
    user_key character varying(100) COLLATE pg_catalog."default" NOT NULL,
    auth_yn character varying(1) COLLATE pg_catalog."default",
    reg_dttm timestamp with time zone,
    auth_dttm timestamp with time zone,
    member_no character varying(100) COLLATE pg_catalog."default",
    user_name character varying(100) COLLATE pg_catalog."default",
    user_email character varying(100) COLLATE pg_catalog."default",
    member_id character varying(80) COLLATE pg_catalog."default",
    process_block_id character varying(60) COLLATE pg_catalog."default",
    msg_cd character varying(5) COLLATE pg_catalog."default",
    CONSTRAINT user_pkey PRIMARY KEY (bot_id, user_key)
);


COMMENT ON COLUMN chatbot_user.auth_dttm
    IS '인증일시';

COMMENT ON COLUMN chatbot_user.member_no
    IS '회원번호';

COMMENT ON COLUMN chatbot_user.member_id
    IS '회원아이디';

COMMENT ON COLUMN chatbot_user.process_block_id
    IS '인증 전 진행했던 블록id';

COMMENT ON COLUMN chatbot_user.msg_cd
    IS '메신저코드(KAKAO, NAVER)';
    
    
CREATE TABLE IF NOT EXISTS chatbot_utterance
(
    block_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    utterance_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    utterance_name character varying(400) COLLATE pg_catalog."default" NOT NULL,
    reg_dttm timestamp with time zone,
    CONSTRAINT chatbot_utterance_pkey PRIMARY KEY (block_id, utterance_id)
);

COMMENT ON TABLE chatbot_utterance
    IS '발화';
    
    
    
    
CREATE SEQUENCE IF NOT EXISTS seq_block_id
    CYCLE
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999
    CACHE 1;
    
CREATE SEQUENCE IF NOT EXISTS seq_button_id
    CYCLE
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999
    CACHE 1;
    
CREATE SEQUENCE IF NOT EXISTS seq_card_id
    CYCLE
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999
    CACHE 1;
    
CREATE SEQUENCE IF NOT EXISTS seq_channel_id
    CYCLE
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999
    CACHE 1;
    
CREATE SEQUENCE IF NOT EXISTS seq_file_key
    CYCLE
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999
    CACHE 1;
    
CREATE SEQUENCE IF NOT EXISTS seq_message_id
    CYCLE
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999
    CACHE 1;
    
CREATE SEQUENCE IF NOT EXISTS seq_quick_button_id
    CYCLE
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999
    CACHE 1;
    
CREATE SEQUENCE IF NOT EXISTS seq_response_id
    CYCLE
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999
    CACHE 1;
    
CREATE SEQUENCE IF NOT EXISTS seq_utterance_id
    CYCLE
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999
    CACHE 1;