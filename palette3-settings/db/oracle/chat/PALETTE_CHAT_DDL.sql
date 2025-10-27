--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_PRH_WRD    채팅_금칙어설정
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_PRH_WRD CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_PRH_WRD (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    PROH_CODE                                                             VARCHAR2(90)      NOT NULL                  ,
    FBDWD                                                             VARCHAR2(300)     NOT NULL                  ,
    SSTTT                                                           VARCHAR2(300)                               ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REGR_NAME                                                             VARCHAR2(60)                                ,
    REGR_DTTM                                                             DATE                                        ,
    USE_YN                                                                VARCHAR2(3)                                 ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         DATE                                        ,
    CONSTRAINT XPK_PLT_CHT_PRH_WRD PRIMARY KEY (CUSTCO_ID,PROH_CODE) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_PRH_WRD                                 IS '채팅_금칙어설정'                        ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.PROH_CODE                       IS '금칙어코드'                             ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.FBDWD                       IS '금칙어'                                 ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.SSTTT                     IS '치환어'                                 ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.REGR_DEPT_CD                    IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.REGR_ID                         IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.REGR_NAME                       IS '등록자명'                               ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.REGR_DTTM                       IS '등록일자'                               ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.USE_YN                          IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.AMDR_DEPT_CD                    IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.AMDR_ID                         IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.UPD_DTTM                        IS '수정일자'                               ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.PROC_ID                         IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CHT_PRH_WRD.IT_PROCESSING                   IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_CUTT_TYP    채팅_상담유형
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_CUTT_TYP CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_CUTT_TYP (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    CNSL_TYP_CD                                                           VARCHAR2(60)      NOT NULL                  ,
    CNSL_TYP_NM                                                           VARCHAR2(300)                               ,
    SPST_CNSL_TYP_CD                                                      VARCHAR2(60)                                ,
    CNSL_TYP_DIV_CD                                                       VARCHAR2(60)                                ,
    USE_YN                                                                CHAR(1)          DEFAULT 'Y'                ,
    SORT_ORD                                                              NUMBER(10,0)     DEFAULT '1'                ,
    INQRY_USE_YN                                                          CHAR(1)                                     ,
    INQRY_ALIAS_NM                                                        VARCHAR2(150)                               ,
    INQRY_TYPE                                                            VARCHAR2(60)                                ,
    INQRY_DESC                                                            VARCHAR2(4000)                              ,
    INQRY_USE_CHANNEL                                                     NUMBER(22)                                  ,
    TEMPLATE_ID                                                           VARCHAR2(90)                                ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_CUTT_TYP PRIMARY KEY (CUSTCO_ID,CNSL_TYP_CD) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_CUTT_TYP                                IS '채팅_상담유형'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.CUSTCO_ID                   IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.CNSL_TYP_CD                    IS '상담유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.CNSL_TYP_NM                    IS '상담유형명'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.SPST_CNSL_TYP_CD               IS '상위상담유형코드'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.CNSL_TYP_DIV_CD                IS '상담유형레벨'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.USE_YN                         IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.SORT_ORD                       IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.INQRY_USE_YN                   IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.INQRY_ALIAS_NM                 IS '문의유형별칭'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.INQRY_TYPE                     IS '상담타입'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.INQRY_DESC                     IS '문의유형 안내메시지'                    ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.INQRY_USE_CHANNEL              IS 'INQRY_USE_CHANNEL'                      ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.TEMPLATE_ID                    IS '문의유형명'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.REGR_DEPT_CD                   IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.REGR_ID                        IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.REG_DTTM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.AMDR_DEPT_CD                   IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.AMDR_ID                        IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.UPD_DTTM                       IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.PROC_ID                        IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_TYP.IT_PROCESSING                  IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_CUTT    채팅_고객상담이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_CUTT CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_CUTT (  
    TALK_CONTACT_ID                                                       VARCHAR2(90)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SNDR_KEY                                                        VARCHAR2(60)      NOT NULL                  ,
    TALK_USER_KEY                                                         VARCHAR2(80)      NOT NULL                  ,
    CUSTOMER_ID                                                           VARCHAR2(80)                                ,
    TALK_START_DT                                                         VARCHAR2(42)                                ,
    TALK_END_DT                                                           VARCHAR2(42)                                ,
    TALK_SESSION_CD                                                       VARCHAR2(9)                                 ,
    CALL_TYP_CD                                                           VARCHAR2(60)                                ,
    CNSL_TYP_CD                                                           VARCHAR2(60)                                ,
    CNSL_TYP_CD_2                                                         VARCHAR2(60)                                ,
    CNSL_TYP_CD_3                                                         VARCHAR2(60)                                ,
    CNSL_TYP_CD_4                                                         VARCHAR2(60)                                ,
    TALK_QST                                                              VARCHAR2(1000)                              ,
    TALK_ANS                                                              VARCHAR2(4000)                              ,
    TALK_TITLE                                                            VARCHAR2(450)                               ,
    OWN_CHECK_YN                                                          VARCHAR2(3)                                 ,
    EXEC_RST_CD                                                           VARCHAR2(60)                                ,
    CNSL_RST_CD                                                           VARCHAR2(60)                                ,
    CST_TYP_CD                                                            VARCHAR2(60)                                ,
    TALK_STAT_CD                                                          VARCHAR2(60)                                ,
    USER_ID                                                               VARCHAR2(60)                                ,
    TALK_DIST_DT                                                          VARCHAR2(42)                                ,
    TALK_READ_DT                                                          VARCHAR2(42)                                ,
    TALK_CONTACT_DT                                                       VARCHAR2(42)                                ,
    TALK_ENT_CD                                                           VARCHAR2(60)                                ,
    CHATBOT_YN                                                            CHAR(1)                                     ,
    CHATBOT_STAT_CD                                                       VARCHAR2(30)                                ,
    TALK_POST_DT                                                          VARCHAR2(42)                                ,
    TALK_INQRY_CD                                                         VARCHAR2(120)                               ,
    CUST_MSG_TIME                                                         NUMBER(22)        DEFAULT '0'               ,
    AUTO_GREETING_YN                                                      CHAR(1)           DEFAULT 'N'               ,
    ORDER_NUMBER                                                          VARCHAR2(60)                                ,
    SYSMSG_SKIP_YN                                                        VARCHAR2(3)       DEFAULT 'N'               ,
    MEMBER_YN                                                             VARCHAR2(3)       DEFAULT 'N'               ,
    CUSTOMER_SEQ                                                          NUMBER(22)                                  ,
    ORI_TALK_CONTACT_ID                                                   VARCHAR2(90)                                ,
    CHANNEL                                                               VARCHAR2(3)                                 ,
    CALLBACK_YN                                                           VARCHAR2(3)                                 ,
    CALLBACK_DT                                                           VARCHAR2(42)                                ,
    CALLBACK_START_DT                                                     VARCHAR2(42)                                ,
    BOT_SERVICE_NM                                                        VARCHAR2(180)                               ,
    BOT_SERVICE_CD                                                        VARCHAR2(120)                               ,
    BOT_INQRY_NM                                                          VARCHAR2(180)                               ,
    BOT_INQRY_CD                                                          VARCHAR2(120)                               ,
    BOT_CALL_ID                                                           VARCHAR2(100)                               ,
    TALK_READY_DT                                                         VARCHAR2(42)                                ,
    CUST_MSG_TIME_SUM                                                     NUMBER(22)                                  ,
    CUST_RES_TIME                                                         NUMBER(22)                                  ,
    AGENT_RES_TIME                                                        NUMBER(22)                                  ,
    CUST_NON_RES_TIME                                                     NUMBER(22)                                  ,
    AGENT_NON_RES_TIME                                                    NUMBER(22)                                  ,
    FLAG_ID                                                               VARCHAR2(120)                               ,
    DESIGNATED_USER_ID                                                    VARCHAR2(60)                                ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              TIMESTAMP                                   ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         TIMESTAMP                                   ,
    UNTACT_URL_COUNT                                                      NUMBER(22)                                  ,
    IF_NGS_YN                                                             CHAR(1)                                     ,
    IF_NGS_RESULT                                                         CHAR(1)                                     ,
    CONSTRAINT XPK_PLT_CHT_CUTT PRIMARY KEY (TALK_CONTACT_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_CUTT                                    IS '채팅_고객상담이력'                      ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_CONTACT_ID                    IS '상담이력ID'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT.CUSTCO_ID                       IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT.SNDR_KEY                     IS 'ASP_센더_키'                            ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_USER_KEY                      IS 'TALK_사용자_키'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT.CUSTOMER_ID                        IS '고객ID'                                 ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_START_DT                      IS '상담시작일시'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_END_DT                        IS '상담종료일시'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_SESSION_CD                    IS '상담세션종료구분'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT.CALL_TYP_CD                        IS '통화유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.CNSL_TYP_CD                        IS '상담유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.CNSL_TYP_CD_2                      IS '상담유형코드2'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT.CNSL_TYP_CD_3                      IS '상담유형코드3'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT.CNSL_TYP_CD_4                      IS '상담유형코드4'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_QST                           IS '상담질문'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_ANS                           IS '상담답변'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_TITLE                         IS '상담제목'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.OWN_CHECK_YN                       IS '본인확인여부'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.EXEC_RST_CD                        IS '처리결과코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.CNSL_RST_CD                        IS '상담완료코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.CST_TYP_CD                         IS '고객성향코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_STAT_CD                       IS '상담상태코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.USER_ID                            IS '상담원ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_DIST_DT                       IS '배분대기등록시간'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_READ_DT                       IS '상담대기등록시간'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_CONTACT_DT                    IS '상담이력등록일자'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_ENT_CD                        IS '인입경로코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.CHATBOT_YN                         IS '챗봇여부'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.CHATBOT_STAT_CD                    IS '챗봇상태코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_POST_DT                       IS '후처리등록시간'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_INQRY_CD                      IS '문의유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.CUST_MSG_TIME                      IS '고객 무응답 메시지 시간'                ;
COMMENT ON COLUMN PLT_CHT_CUTT.AUTO_GREETING_YN                   IS '자동인사여부'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.ORDER_NUMBER                       IS '주문번호'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.SYSMSG_SKIP_YN                     IS '시스템메세지스킵여부'                   ;
COMMENT ON COLUMN PLT_CHT_CUTT.MEMBER_YN                          IS '회원여부'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.CUSTOMER_SEQ                       IS '회원번호'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.ORI_TALK_CONTACT_ID                IS '원본상담이력ID'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT.CHANNEL                            IS '채널'                                   ;
COMMENT ON COLUMN PLT_CHT_CUTT.CALLBACK_YN                        IS '콜백여부'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.CALLBACK_DT                        IS '콜백일자'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.CALLBACK_START_DT                  IS '콜백시작일자'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.BOT_SERVICE_NM                     IS '챗봇서비스명'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.BOT_SERVICE_CD                     IS '챗봇서비스코드'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT.BOT_INQRY_NM                       IS '챗봇문의유형명'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT.BOT_INQRY_CD                       IS '챗봇문의유형코드'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT.BOT_CALL_ID                        IS '챗봇요청ID'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT.TALK_READY_DT                      IS '문의유형 선택 시간'                     ;
COMMENT ON COLUMN PLT_CHT_CUTT.CUST_MSG_TIME_SUM                  IS '고객무응답시간합계'                     ;
COMMENT ON COLUMN PLT_CHT_CUTT.CUST_RES_TIME                      IS '고객응답시간'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.AGENT_RES_TIME                     IS '상담사응답시간'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT.CUST_NON_RES_TIME                  IS '고객무응답시간'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT.AGENT_NON_RES_TIME                 IS '상담사무응답시간'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT.FLAG_ID                            IS 'FLAG아이디'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT.DESIGNATED_USER_ID                 IS '지정된 상담사아이디'                    ;
COMMENT ON COLUMN PLT_CHT_CUTT.REGR_DEPT_CD                       IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT.REGR_ID                            IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.REG_DTTM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.AMDR_DEPT_CD                       IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT.AMDR_ID                            IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.UPD_DTTM                           IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT.PROC_ID                            IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CHT_CUTT.IT_PROCESSING                      IS '전산처리일시'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT.UNTACT_URL_COUNT                   IS '비대면URL전송수'                        ;
COMMENT ON COLUMN PLT_CHT_CUTT.IF_NGS_YN                          IS 'NGS 연동 여부'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT.IF_NGS_RESULT                      IS 'NGS 연동 결과'                          ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_CUTT_DTL    채팅_고객상담이력상세
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_CUTT_DTL CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_CUTT_DTL (  
    TALK_CONTACT_ID                                                       VARCHAR2(90)      NOT NULL                  ,
    TALK_SERIAL_NUMBER                                                    VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    USER_ID                                                               VARCHAR2(60)                                ,
    SNDRCV_CD                                                             VARCHAR2(30)                                ,
    SNDRCV_ID                                                             VARCHAR2(60)                                ,
    TYPE                                                                  VARCHAR2(60)                                ,
    CONTENT                                                               CLOB                                        ,
    IMAGE_URL                                                             VARCHAR2(1500)                              ,
    IMAGE_TALK_PATH                                                       VARCHAR2(1500)                              ,
    FILE_GROUP_KEY                                                        VARCHAR2(90)                                ,
    AUTO_ANSWER                                                           VARCHAR2(30)                                ,
    LINKS                                                                 VARCHAR2(1500)                              ,
    VIDEO_TALK_PATH                                                       VARCHAR2(1500)                              ,
    VIDEO_URL                                                             VARCHAR2(1500)                              ,
    VIDEO_THUMNAIL_PATH                                                   VARCHAR2(1500)                              ,
    READ_YN                                                               NUMBER(22)                                  ,
    SYS_MSG_ID                                                         VARCHAR2(25)                                ,
    ORG_CONT_ID                                                           NUMBER(22)                                  ,
    ORG_FILE_ID                                                           NUMBER(22)                                  ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              TIMESTAMP                                   ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         TIMESTAMP                                   ,
    CONSTRAINT XPK_PLT_CHT_CUTT_DTL PRIMARY KEY (TALK_CONTACT_ID,TALK_SERIAL_NUMBER)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_CUTT_DTL                                IS '채팅_고객상담이력상세'                  ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.TALK_CONTACT_ID                IS '고객상담이력ID'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.TALK_SERIAL_NUMBER             IS 'TALK_일련_번호'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.CUSTCO_ID                   IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.USER_ID                        IS '상담사ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.SNDRCV_CD                      IS '수신발신코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.SNDRCV_ID                      IS '수발신자ID'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.TYPE                           IS '메시지타입'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.CONTENT                        IS '수신발신메시지'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.IMAGE_URL                      IS '이미지URL'                              ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.IMAGE_TALK_PATH                IS '이미지파일경로'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.AUTO_ANSWER                    IS '자동응답 메시지'                        ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.LINKS                          IS '링크정보'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.VIDEO_TALK_PATH                IS '비디오경로'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.VIDEO_URL                      IS '비디오URL'                              ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.VIDEO_THUMNAIL_PATH            IS '비디오썸네일경로'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.READ_YN                        IS '읽음여부'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.SYS_MSG_ID                  IS '발송된 시스템메시지 ID'                 ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.ORG_CONT_ID                    IS '대화내용원본테이블 KEY'                 ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.ORG_FILE_ID                    IS '파일원본ID'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.REGR_DEPT_CD                   IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.REGR_ID                        IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.REG_DTTM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.AMDR_DEPT_CD                   IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.AMDR_ID                        IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.UPD_DTTM                       IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.PROC_ID                        IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.IT_PROCESSING                  IS '전산처리일시'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_DTL.FILE_GROUP_KEY                 IS '파일그룹키';

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_CUTT_HST    채팅_고객상담이력HIST
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_CUTT_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_CUTT_HST (  
    TALK_CONTACT_ID                                                       VARCHAR2(90)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SNDR_KEY                                                        VARCHAR2(60)      NOT NULL                  ,
    TALK_USER_KEY                                                         VARCHAR2(80)      NOT NULL                  ,
    CUSTOMER_ID                                                           VARCHAR2(80)                                ,
    TALK_START_DT                                                         VARCHAR2(42)                                ,
    TALK_END_DT                                                           VARCHAR2(42)                                ,
    TALK_SESSION_CD                                                       VARCHAR2(9)                                 ,
    CALL_TYP_CD                                                           VARCHAR2(60)                                ,
    CNSL_TYP_CD                                                           VARCHAR2(60)                                ,
    TALK_QST                                                              CLOB                                        ,
    TALK_ANS                                                              CLOB                                        ,
    TALK_TITLE                                                            VARCHAR2(450)                               ,
    OWN_CHECK_YN                                                          VARCHAR2(3)                                 ,
    EXEC_RST_CD                                                           VARCHAR2(60)                                ,
    CNSL_RST_CD                                                           VARCHAR2(60)                                ,
    CST_TYP_CD                                                            VARCHAR2(60)                                ,
    TALK_STAT_CD                                                          VARCHAR2(60)                                ,
    USER_ID                                                               VARCHAR2(60)                                ,
    TALK_DIST_DT                                                          VARCHAR2(42)                                ,
    TALK_READ_DT                                                          VARCHAR2(42)                                ,
    TALK_CONTACT_DT                                                       VARCHAR2(42)                                ,
    TALK_ENT_CD                                                           VARCHAR2(60)                                ,
    CHATBOT_YN                                                            CHAR(1)                                     ,
    CHATBOT_STAT_CD                                                       VARCHAR2(30)                                ,
    TALK_POST_DT                                                          VARCHAR2(42)                                ,
    TALK_INQRY_CD                                                         VARCHAR2(120)                               ,
    CUST_MSG_TIME                                                         NUMBER(22)        DEFAULT '0'               ,
    AUTO_GREETING_YN                                                      CHAR(1)           DEFAULT 'N'               ,
    ORDER_NUMBER                                                          VARCHAR2(60)                                ,
    SYSMSG_SKIP_YN                                                        VARCHAR2(3)       DEFAULT 'N'               ,
    MEMBER_YN                                                             VARCHAR2(3)       DEFAULT 'N'               ,
    CUSTOMER_SEQ                                                          NUMBER(22)                                  ,
    ORI_TALK_CONTACT_ID                                                   VARCHAR2(90)                                ,
    CNSL_TYP_CD_2                                                         VARCHAR2(60)                                ,
    CNSL_TYP_CD_3                                                         VARCHAR2(60)                                ,
    CHANNEL                                                               VARCHAR2(3)                                 ,
    CALLBACK_YN                                                           VARCHAR2(3)                                 ,
    CALLBACK_DT                                                           VARCHAR2(42)                                ,
    CALLBACK_START_DT                                                     VARCHAR2(42)                                ,
    TALK_READY_DT                                                         VARCHAR2(42)                                ,
    CUST_MSG_TIME_SUM                                                     NUMBER(22)                                  ,
    FLAG_ID                                                               VARCHAR2(120)                               ,
    DESIGNATED_USER_ID                                                    VARCHAR2(60)                                ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              TIMESTAMP                                   ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_CUTT_HST PRIMARY KEY (TALK_CONTACT_ID, IT_PROCESSING) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_CUTT_HST                                IS '채팅_고객상담이력HIST'                  ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_CONTACT_ID                IS '상담이력ID'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CUSTCO_ID                   IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.SNDR_KEY                 IS 'ASP_센더_키'                            ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_USER_KEY                  IS 'TALK_사용자_키'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CUSTOMER_ID                    IS '고객ID'                                 ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_START_DT                  IS '상담시작일시'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_END_DT                    IS '상담종료일시'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_SESSION_CD                IS '상담세션종료구분'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CALL_TYP_CD                    IS '통화유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CNSL_TYP_CD                    IS '상담유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_QST                       IS '상담질문'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_ANS                       IS '상담답변'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_TITLE                     IS '상담제목'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.OWN_CHECK_YN                   IS '본인확인여부'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.EXEC_RST_CD                    IS '처리결과코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CNSL_RST_CD                    IS '상담완료코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CST_TYP_CD                     IS '고객성향코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_STAT_CD                   IS '상담상태코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.USER_ID                        IS '상담원ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_DIST_DT                   IS '배분대기등록시간'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_READ_DT                   IS '상담대기등록시간'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_CONTACT_DT                IS '상담이력등록일자'                       ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_ENT_CD                    IS '인입경로코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CHATBOT_YN                     IS '챗봇여부'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CHATBOT_STAT_CD                IS '챗봇상태코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_POST_DT                   IS '채팅종료시간'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_INQRY_CD                  IS '문의유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CUST_MSG_TIME                  IS '고객 무응답 메시지 시간'                ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.AUTO_GREETING_YN               IS '자동인사여부'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.ORDER_NUMBER                   IS '주문번호'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.SYSMSG_SKIP_YN                 IS '시스템메세지스킵여부'                   ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.MEMBER_YN                      IS '회원여부'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CUSTOMER_SEQ                   IS '회원번호'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.ORI_TALK_CONTACT_ID            IS '원본상담이력ID'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CNSL_TYP_CD_2                  IS '상담유형코드2'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CNSL_TYP_CD_3                  IS '상담유형코드3'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CHANNEL                        IS '채널'                                   ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CALLBACK_YN                    IS '콜백여부'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CALLBACK_DT                    IS '콜백일자'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CALLBACK_START_DT              IS '콜백시작일자'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.TALK_READY_DT                  IS '문의유형 선택 시간'                     ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.CUST_MSG_TIME_SUM              IS '고객무응답시간합계'                     ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.FLAG_ID                        IS 'FLAG아이디'                             ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.DESIGNATED_USER_ID             IS '지정된 상담사아이디'                    ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.REGR_DEPT_CD                   IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.REGR_ID                        IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.REG_DTTM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.AMDR_DEPT_CD                   IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.AMDR_ID                        IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.UPD_DTTM                       IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.PROC_ID                        IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CHT_CUTT_HST.IT_PROCESSING                  IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_CUTT_CONT_ORG    채팅_대화내용원본테이블
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_CUTT_CONT_ORG CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_CUTT_CONT_ORG (  
    ORG_CONT_ID                                                           NUMBER(22)        NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    ORG_CONTENT                                                           CLOB              NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP                                   ,
    CONSTRAINT XPK_PLT_CHT_CUTT_CONT_ORG PRIMARY KEY (ORG_CONT_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_CUTT_CONT_ORG                           IS '채팅_대화내용원본테이블'                ;
COMMENT ON COLUMN PLT_CHT_CUTT_CONT_ORG.ORG_CONT_ID               IS '대화내용원본테이블 KEY'                 ;
COMMENT ON COLUMN PLT_CHT_CUTT_CONT_ORG.CUSTCO_ID              IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_CONT_ORG.ORG_CONTENT               IS '대화내용원본'                           ;
COMMENT ON COLUMN PLT_CHT_CUTT_CONT_ORG.IT_PROCESSING             IS '등록일시'                               ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_ENV    채팅_텔레톡설정정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_ENV CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_ENV (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    STNG_CD                                                                VARCHAR2(150)     NOT NULL                  ,
    STNG_CD_NM                                                             VARCHAR2(300)                               ,
    STNG_VL                                                         VARCHAR2(600)                               ,
    USE_YN                                                                CHAR(1)           DEFAULT 'Y'               ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              DATE                                        ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_ENV PRIMARY KEY (CUSTCO_ID,STNG_CD) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_ENV                                     IS '채팅_텔레톡설정정보'                    ;
COMMENT ON COLUMN PLT_CHT_ENV.CUSTCO_ID                        IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_ENV.STNG_CD                              IS '환경설정코드'                           ;
COMMENT ON COLUMN PLT_CHT_ENV.STNG_CD_NM                           IS '환경설정코드명'                         ;
COMMENT ON COLUMN PLT_CHT_ENV.STNG_VL                       IS '환경설정값'                             ;
COMMENT ON COLUMN PLT_CHT_ENV.USE_YN                              IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_ENV.REGR_DEPT_CD                        IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_ENV.REGR_ID                             IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_ENV.REG_DTTM                            IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_ENV.AMDR_DEPT_CD                        IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_ENV.AMDR_ID                             IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_ENV.UPD_DTTM                            IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_ENV.PROC_ID                             IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_ENV.IT_PROCESSING                       IS '처리일시'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_HDY    채팅_휴일관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_HDY CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_HDY (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    TALK_HOLIDAY_ID                                                       VARCHAR2(90)      NOT NULL                  ,
    HOLIDAY_NM                                                            VARCHAR2(300)                               ,
    HOLIDAY_GB_CD                                                         VARCHAR2(60)                                ,
    HOLIDAY_DT                                                            VARCHAR2(24)                                ,
    HOLIDAY_DESC                                                          VARCHAR2(4000)                              ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              DATE                                        ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         DATE              DEFAULT SYSTIMESTAMP      ,
    CONSTRAINT XPK_PLT_CHT_HDY PRIMARY KEY (CUSTCO_ID,TALK_HOLIDAY_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_HDY                                     IS '채팅_휴일관리'                          ;
COMMENT ON COLUMN PLT_CHT_HDY.CUSTCO_ID                        IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_HDY.TALK_HOLIDAY_ID                     IS '휴일ID'                                 ;
COMMENT ON COLUMN PLT_CHT_HDY.HOLIDAY_NM                          IS '휴일명'                                 ;
COMMENT ON COLUMN PLT_CHT_HDY.HOLIDAY_GB_CD                       IS '휴일구분코드'                           ;
COMMENT ON COLUMN PLT_CHT_HDY.HOLIDAY_DT                          IS '휴일일자'                               ;
COMMENT ON COLUMN PLT_CHT_HDY.HOLIDAY_DESC                        IS '휴일설명'                               ;
COMMENT ON COLUMN PLT_CHT_HDY.REGR_DEPT_CD                        IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_HDY.REGR_ID                             IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_HDY.REG_DTTM                            IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_HDY.AMDR_DEPT_CD                        IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_HDY.AMDR_ID                             IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_HDY.UPD_DTTM                            IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_HDY.PROC_ID                             IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CHT_HDY.IT_PROCESSING                       IS '전산처리일시'                           ;

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_INFO_MSG    채팅_안내메세지 관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_INFO_MSG CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_INFO_MSG (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    MSG_ID                                                                VARCHAR2(75)      NOT NULL                  ,
    MSG_TIME                                                              VARCHAR2(12)                                ,
    MSG_CN                                                           VARCHAR2(3000)                              ,
    USE_YN                                                                CHAR(1)           DEFAULT 'Y'               ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              DATE                                        ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_INFO_MSG PRIMARY KEY (CUSTCO_ID,MSG_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_INFO_MSG                                IS '채팅_안내메세지 관리'                   ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.CUSTCO_ID                   IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.MSG_ID                         IS '메시지ID'                               ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.MSG_TIME                       IS '메세지 시간'                            ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.MSG_CN                    IS '메세지 내용'                            ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.USE_YN                         IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.REGR_DEPT_CD                   IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.REGR_ID                        IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.REG_DTTM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.AMDR_DEPT_CD                   IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.AMDR_ID                        IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.UPD_DTTM                       IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.PROC_ID                        IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_INFO_MSG.IT_PROCESSING                  IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_CUTT_ING    채팅_상담대기
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_CUTT_ING CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_CUTT_ING (  
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    TALK_USER_KEY                                                         VARCHAR2(80)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    TALK_GB                                                               VARCHAR2(30)      NOT NULL                  ,
    TALK_START_DT                                                         VARCHAR2(42)                                ,
    CONSTRAINT XPK_PLT_CHT_CUTT_ING PRIMARY KEY (USER_ID,TALK_USER_KEY) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_CUTT_ING                                IS '채팅_상담대기'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_ING.USER_ID                        IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_ING.TALK_USER_KEY                  IS 'TALK_사용자_키'                         ;
COMMENT ON COLUMN PLT_CHT_CUTT_ING.CUSTCO_ID                   IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_ING.TALK_GB                        IS '고객구분'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_ING.TALK_START_DT                  IS '상담시작일자'                           ;

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_INQ_TYP    채팅_문의유형
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_INQ_TYP CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_INQ_TYP (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    INQRY_TYP_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    INQRY_TYP_NM                                                          VARCHAR2(450)                               ,
    INQRY_DESC                                                            VARCHAR2(4000)                              ,
    INQRY_TYPE                                                            VARCHAR2(60)                                ,
    INQRY_USE_CHANNEL                                                     VARCHAR2(60)                                ,
    SPST_INQRY_TYP_CD                                                     VARCHAR2(60)                                ,
    INQRY_TYP_DIV_CD                                                      VARCHAR2(60)                                ,
    USE_YN                                                                CHAR(1)           DEFAULT 'Y'               ,
    SORT_ORD                                                              NUMBER(22)        DEFAULT '1'               ,
    INQRY_TYP_ENG_NM                                                      VARCHAR2(450)                               ,
    CALL_TYP_CD                                                           VARCHAR2(60)                                ,
    CNSL_TYP_CD_1                                                         VARCHAR2(60)                                ,
    CNSL_TYP_CD_2                                                         VARCHAR2(60)                                ,
    CNSL_TYP_CD_3                                                         VARCHAR2(60)                                ,
    PRIORITY_ROUTE_ORD                                                    NUMBER(22)                                  ,
    IMAGE_FILE_KEY                                                        VARCHAR2(90)                                ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_INQ_TYP PRIMARY KEY (CUSTCO_ID,INQRY_TYP_CD)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_INQ_TYP                                 IS '채팅_문의유형'                          ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.INQRY_TYP_CD                    IS '문의유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.INQRY_TYP_NM                    IS '문의유형코드명'                         ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.INQRY_DESC                      IS '문의유형설명'                           ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.INQRY_TYPE                      IS '문의유형종류'                           ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.INQRY_USE_CHANNEL               IS '문의유형사용채널'                       ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.SPST_INQRY_TYP_CD               IS '상위문의유형코드'                       ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.INQRY_TYP_DIV_CD                IS '문의유형구분코드'                       ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.USE_YN                          IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.SORT_ORD                        IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.INQRY_TYP_ENG_NM                IS '문의유형영문명'                         ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.CALL_TYP_CD                     IS '사용채널'                               ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.CNSL_TYP_CD_1                   IS '상담유형코드1'                          ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.CNSL_TYP_CD_2                   IS '상담유형코드2'                          ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.CNSL_TYP_CD_3                   IS '상담유형코드3'                          ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.PRIORITY_ROUTE_ORD              IS '배분순서'                               ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.IMAGE_FILE_KEY                  IS '이미지 파일키'                          ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.REGR_DEPT_CD                    IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.REGR_ID                         IS '등록자'                                 ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.AMDR_DEPT_CD                    IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.AMDR_ID                         IS '수정자'                                 ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.PROC_ID                         IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_INQ_TYP.IT_PROCESSING                   IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_RDY    채팅_상담사_대기
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_RDY CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_RDY (  
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    TALK_START_DT                                                         VARCHAR2(42)                                ,
    READY_TYPE                                                            VARCHAR2(100)                               ,
    CONSTRAINT XPK_PLT_CHT_RDY PRIMARY KEY (USER_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_RDY                                     IS '채팅_상담사_대기'                       ;
COMMENT ON COLUMN PLT_CHT_RDY.USER_ID                             IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_CHT_RDY.CUSTCO_ID                        IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_RDY.TALK_START_DT                       IS '상담시작일시'                           ;
COMMENT ON COLUMN PLT_CHT_RDY.READY_TYPE                          IS '상담사대기유형'                         ;

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_RDY_HST    채팅_상담사내역
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_RDY_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_RDY_HST (  
    SEQ                                                                   NUMBER(22)        NOT NULL                  ,
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    USER_STATUS_CD                                                        VARCHAR2(60)                                ,
    TALK_START_DT                                                         DATE              NOT NULL                  ,
    TALK_END_DT                                                           DATE                                        ,
    IT_PROCESSING                                                         DATE                                        ,
    CONSTRAINT XPK_PLT_CHT_RDY_HST PRIMARY KEY (SEQ)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_RDY_HST                                 IS '채팅_상담사내역'                        ;
COMMENT ON COLUMN PLT_CHT_RDY_HST.SEQ                             IS '시퀀스'                                 ;
COMMENT ON COLUMN PLT_CHT_RDY_HST.USER_ID                         IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_CHT_RDY_HST.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_RDY_HST.USER_STATUS_CD                  IS '상태코드'                               ;
COMMENT ON COLUMN PLT_CHT_RDY_HST.TALK_START_DT                   IS '시작시간'                               ;
COMMENT ON COLUMN PLT_CHT_RDY_HST.TALK_END_DT                     IS '종료시간'                               ;
COMMENT ON COLUMN PLT_CHT_RDY_HST.IT_PROCESSING                   IS '처리시간'                               ;

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_RDY_ONF    채팅_상담사ONOFF이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_RDY_ONF CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_RDY_ONF (  
    SEQ                                                                   NUMBER(22)        NOT NULL                  ,
    USER_ID                                                               VARCHAR2(60)                                ,
    CUSTCO_ID                                                          VARCHAR2(10)                                ,
    USER_STATUS_CD                                                        VARCHAR2(60)                                ,
    TALK_START_DT                                                         DATE                                        ,
    TALK_END_DT                                                           DATE                                        ,
    IT_PROCESSING                                                         DATE                                        ,
    CONSTRAINT XPK_PLT_CHT_RDY_ONF PRIMARY KEY (SEQ)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_RDY_ONF                                 IS '채팅_상담사ONOFF이력'                   ;
COMMENT ON COLUMN PLT_CHT_RDY_ONF.SEQ                             IS '시퀀스'                                 ;
COMMENT ON COLUMN PLT_CHT_RDY_ONF.USER_ID                         IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_CHT_RDY_ONF.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_RDY_ONF.USER_STATUS_CD                  IS '상태코드'                               ;
COMMENT ON COLUMN PLT_CHT_RDY_ONF.TALK_START_DT                   IS '상담시작일시'                           ;
COMMENT ON COLUMN PLT_CHT_RDY_ONF.TALK_END_DT                     IS '상담종료일시'                           ;
COMMENT ON COLUMN PLT_CHT_RDY_ONF.IT_PROCESSING                   IS '처리시간'                               ;

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_SCRT    채팅_스크립트마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_SCRT CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_SCRT (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SCRIPT_ID                                                             VARCHAR2(90)      NOT NULL                  ,
    UPPER_SCRIPT_ID                                                       NUMBER(22)        DEFAULT '0'               ,
    INQRY_TYP_CD                                                          VARCHAR2(60)                                ,
    INQRY_TYP_CD_2                                                        VARCHAR2(60)                                ,
    LVL_NO                                                                NUMBER(22)        DEFAULT '0'               ,
    SCRIPT_TIT                                                            VARCHAR2(600)                               ,
    SCRIPT_RMK_NO                                                         VARCHAR2(75)                                ,
    SCRIPT_RMK                                                            CLOB                                        ,
    FILE_GROUP_KEY                                                        VARCHAR2(90)                                ,
    FST_USER_ID                                                           VARCHAR2(60)                                ,
    FST_SCRIPT_DT                                                         DATE                                        ,
    LAST_USER_ID                                                          VARCHAR2(60)                                ,
    LAST_SCRIPT_DT                                                        DATE                                        ,
    ACCESS_IP                                                             VARCHAR2(60)                                ,
    SELECT_NO                                                             NUMBER(22)        DEFAULT '0'               ,
    ORD_SEQ                                                               NUMBER(22)        DEFAULT '0'               ,
    USE_YN                                                                CHAR(1)                                     ,
    SCRIPT_AUTH_TYPE                                                      VARCHAR2(30)                                ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              DEFAULT SYSTIMESTAMP      ,
    CONSTRAINT XPK_PLT_CHT_SCRT PRIMARY KEY (CUSTCO_ID,SCRIPT_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_SCRT                                    IS '채팅_스크립트마스터'                    ;
COMMENT ON COLUMN PLT_CHT_SCRT.CUSTCO_ID                       IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_SCRT.SCRIPT_ID                          IS '스크립트ID'                             ;
COMMENT ON COLUMN PLT_CHT_SCRT.UPPER_SCRIPT_ID                    IS '상위스크립트ID'                         ;
COMMENT ON COLUMN PLT_CHT_SCRT.INQRY_TYP_CD                       IS '문의유형1'                              ;
COMMENT ON COLUMN PLT_CHT_SCRT.INQRY_TYP_CD_2                     IS '문의유형2'                              ;
COMMENT ON COLUMN PLT_CHT_SCRT.LVL_NO                             IS '스크립트레벨'                           ;
COMMENT ON COLUMN PLT_CHT_SCRT.SCRIPT_TIT                         IS '스크립트제목'                           ;
COMMENT ON COLUMN PLT_CHT_SCRT.SCRIPT_RMK_NO                      IS '스크립트내용번호'                       ;
COMMENT ON COLUMN PLT_CHT_SCRT.SCRIPT_RMK                         IS '스크립트내용'                           ;
COMMENT ON COLUMN PLT_CHT_SCRT.FILE_GROUP_KEY                     IS '파일그룹키'                             ;
COMMENT ON COLUMN PLT_CHT_SCRT.FST_USER_ID                        IS '최초등록자ID'                           ;
COMMENT ON COLUMN PLT_CHT_SCRT.FST_SCRIPT_DT                      IS '최초등록일'                             ;
COMMENT ON COLUMN PLT_CHT_SCRT.LAST_USER_ID                       IS '마지막등록자ID'                         ;
COMMENT ON COLUMN PLT_CHT_SCRT.LAST_SCRIPT_DT                     IS '마지막등록일'                           ;
COMMENT ON COLUMN PLT_CHT_SCRT.ACCESS_IP                          IS '등록자IP'                               ;
COMMENT ON COLUMN PLT_CHT_SCRT.SELECT_NO                          IS '조회수'                                 ;
COMMENT ON COLUMN PLT_CHT_SCRT.ORD_SEQ                            IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_CHT_SCRT.USE_YN                             IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_SCRT.SCRIPT_AUTH_TYPE                   IS '스크립트권한타입'                       ;
COMMENT ON COLUMN PLT_CHT_SCRT.PROC_ID                            IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_SCRT.IT_PROCESSING                      IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_SRT_KEY    채팅_상담사단축키설정
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_SRT_KEY CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_SRT_KEY (  
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    SHORT_KEY                                                             VARCHAR2(6)       NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SHORT_KEY_VALUE                                                       VARCHAR2(4000)                              ,
    SCRIPT_ID                                                             VARCHAR2(90)                                ,
    ACTION_TYPE                                                           VARCHAR2(45)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              DATE                                        ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         DATE                                        ,
    CONSTRAINT XPK_PLT_CHT_SRT_KEY PRIMARY KEY (USER_ID,SHORT_KEY)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_SRT_KEY                                 IS '채팅_상담사단축키설정'                  ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.USER_ID                         IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.SHORT_KEY                       IS '단축키KEY'                              ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.SHORT_KEY_VALUE                 IS '단축키VALUE'                            ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.SCRIPT_ID                       IS '스크립트아이디'                         ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.ACTION_TYPE                     IS '액션유형'                               ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.REGR_ID                         IS '등록자'                                 ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.AMDR_ID                         IS '수정자'                                 ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.PROC_ID                         IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CHT_SRT_KEY.IT_PROCESSING                   IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_SYS_MSG    채팅_시스템메시지
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_SYS_MSG CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_SYS_MSG (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SYS_MSG_ID                                                         VARCHAR2(75)      NOT NULL                  ,
    MSG_CL                                                                VARCHAR2(60)      NOT NULL                  ,
    SNDRCV_CD                                                             VARCHAR2(30)      NOT NULL                  ,
    MSG_TYPE                                                              VARCHAR2(6)       NOT NULL                  ,
    MSG_TIME                                                              NUMBER(22)                                  ,
    MSG_CN                                                           VARCHAR2(3000)    NOT NULL                  ,
    MSG_DESC                                                              VARCHAR2(600)                               ,
    LINKS_TYPE                                                            VARCHAR2(6)                                 ,
    USE_YN                                                                CHAR(1)           NOT NULL                  ,
    REG_DEPT_CD                                                           VARCHAR2(60)                                ,
    REG_ID                                                                VARCHAR2(60)                                ,
    REG_DTTM                                                              DATE                                        ,
    UPD_DEPT_CD                                                           VARCHAR2(60)                                ,
    UPD_ID                                                                VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_SYS_MSG PRIMARY KEY (CUSTCO_ID,SYS_MSG_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_SYS_MSG                                 IS '채팅_시스템메시지'                      ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.SYS_MSG_ID                   IS '시스템메시지ID'                         ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.MSG_CL                          IS '메시지구분'                             ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.SNDRCV_CD                       IS '수신발신코드'                           ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.MSG_TYPE                        IS '메시지타입'                             ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.MSG_TIME                        IS '메시지 시간(시분초)'                    ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.MSG_CN                     IS '메시지내용'                             ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.MSG_DESC                        IS '메시지 설명'                            ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.LINKS_TYPE                      IS '링크 버튼 타입'                         ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.USE_YN                          IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.REG_DEPT_CD                     IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.REG_ID                          IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.UPD_DEPT_CD                     IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.UPD_ID                          IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.PROC_ID                         IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_MSG.IT_PROCESSING                   IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_SYS_LNK    채팅_시스템메시지링크
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_SYS_LNK CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_SYS_LNK (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SYSTEM_MSG_LINKS_ID                                                   VARCHAR2(75)      NOT NULL                  ,
    SYS_MSG_ID                                                         VARCHAR2(75)      NOT NULL                  ,
    BTN_NM                                                                VARCHAR2(84)      NOT NULL                  ,
    EXTRA                                                                 VARCHAR2(600)                               ,
    URL_MOBILE                                                            VARCHAR2(3000)                              ,
    URL_PC                                                                VARCHAR2(3000)                              ,
    SCHEME_ADROID                                                         VARCHAR2(3000)                              ,
    SCHEME_IOS                                                            VARCHAR2(3000)                              ,
    SORT_ORD                                                              NUMBER(22)        NOT NULL                  ,
    USE_YN                                                                CHAR(1)           NOT NULL                  ,
    REG_DEPT_CD                                                           VARCHAR2(60)                                ,
    REG_ID                                                                VARCHAR2(60)                                ,
    REG_DTTM                                                              DATE                                        ,
    UPD_DEPT_CD                                                           VARCHAR2(60)                                ,
    UPD_ID                                                                VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_SYS_LNK PRIMARY KEY (CUSTCO_ID,SYSTEM_MSG_LINKS_ID,SYS_MSG_ID,BTN_NM) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_SYS_LNK                                 IS '채팅_시스템메시지링크'                  ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.SYSTEM_MSG_LINKS_ID             IS '시스템메시지링크ID'                     ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.SYS_MSG_ID                   IS '시스템메시지ID'                         ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.BTN_NM                          IS '링크버튼명'                             ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.EXTRA                           IS '링크버튼코드'                           ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.URL_MOBILE                      IS '모바일 웹 URL'                          ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.URL_PC                          IS 'PC 웹 URL'                              ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.SCHEME_ADROID                   IS 'SCHEME_ADROID'                          ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.SCHEME_IOS                      IS 'SCHEME_IOS'                             ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.SORT_ORD                        IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.USE_YN                          IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.REG_DEPT_CD                     IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.REG_ID                          IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.UPD_DEPT_CD                     IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.UPD_ID                          IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.PROC_ID                         IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_SYS_LNK.IT_PROCESSING                   IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_USER_INQ    채팅_사용자별전문상담관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_USER_INQ CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_USER_INQ (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    TALK_INQRY_CD                                                         VARCHAR2(60)      NOT NULL                  ,
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    SKILL_RANK                                                            NUMBER(22)                                  ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         DEFAULT SYSTIMESTAMP  ,
    CONSTRAINT XPK_PLT_CHT_USER_INQ PRIMARY KEY (CUSTCO_ID,TALK_INQRY_CD,USER_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_USER_INQ                                IS '채팅_사용자별전문상담관리'              ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.CUSTCO_ID                   IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.TALK_INQRY_CD                  IS '문의유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.USER_ID                        IS '상담원ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.SKILL_RANK                     IS '스킬우선순위'                           ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.REGR_DEPT_CD                   IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.REGR_ID                        IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.REG_DTTM                       IS '등록일자'                               ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.AMDR_DEPT_CD                   IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.AMDR_ID                        IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.UPD_DTTM                       IS '수정일자'                               ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.PROC_ID                        IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CHT_USER_INQ.IT_PROCESSING                  IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_USER_RDY    채팅_사용자배분대기
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_USER_RDY CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_USER_RDY (  
    TALK_USER_KEY                                                         VARCHAR2(80)      NOT NULL                  ,
    TALK_SERIAL_NUMBER                                                    VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SNDR_KEY                                                        VARCHAR2(60)      NOT NULL                  ,
    TALK_READY_CD                                                         VARCHAR2(30)      NOT NULL                  ,
    TALK_DIST_DT                                                          VARCHAR2(42)      NOT NULL                  ,
    TALK_READ_DT                                                          VARCHAR2(42)                                ,
    USER_ID                                                               VARCHAR2(60)                                ,
    MSG_TIME                                                              NUMBER(22)        DEFAULT '0'               ,
    CHATBOT_YN                                                            CHAR(1)                                     ,
    CHATBOT_STAT_CD                                                       VARCHAR2(30)                                ,
    TRANS_TALK_CONTACT_ID                                                 VARCHAR2(180)                               ,
    TALK_INQRY_CD                                                         VARCHAR2(120)                               ,
    ORDER_NUMBER                                                          VARCHAR2(60)                                ,
    MEMBER_YN                                                             VARCHAR2(3)       DEFAULT 'N'               ,
    CUSTOMER_SEQ                                                          NUMBER(22)                                  ,
    CUST_MSG_TIME                                                         NUMBER(22)        DEFAULT '0'               ,
    CHANNEL                                                               VARCHAR2(3)                                 ,
    EXEC_RST_CD                                                           VARCHAR2(3)                                 ,
    CALLBACK_DT                                                           VARCHAR2(42)                                ,
    CALLBACK_START_DT                                                     VARCHAR2(42)                                ,
    CALLBACK_YN                                                           VARCHAR2(3)                                 ,
    RETRY_ROUNTING_CNT                                                    NUMBER(22)                                  ,
    BOT_SERVICE_NM                                                        VARCHAR2(180)                               ,
    BOT_SERVICE_CD                                                        VARCHAR2(120)                               ,
    BOT_INQRY_NM                                                          VARCHAR2(180)                               ,
    BOT_INQRY_CD                                                          VARCHAR2(120)                               ,
    BOT_CALL_ID                                                           VARCHAR2(100)                               ,
    TALK_READY_DT                                                         VARCHAR2(42)                                ,
    CALL_TYP_CD                                                           VARCHAR2(60)                                ,
    DESIGNATED_USER_ID                                                    VARCHAR2(60)                                ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    AMDR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    UPD_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         TIMESTAMP                                   ,
    CONSTRAINT XPK_PLT_CHT_USER_RDY PRIMARY KEY (TALK_USER_KEY) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_USER_RDY                                IS '채팅_사용자배분대기'                    ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.TALK_USER_KEY                  IS 'TALK_사용자_키'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.TALK_SERIAL_NUMBER             IS 'TALK_일련_번호'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CUSTCO_ID                   IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.SNDR_KEY                 IS 'ASP_센더_키'                            ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.TALK_READY_CD                  IS '배분_상담_구분'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.TALK_DIST_DT                   IS '배분대기등록시간'                       ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.TALK_READ_DT                   IS '상담대기등록시간'                       ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.USER_ID                        IS '상담사ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.MSG_TIME                       IS '메세지 시간'                            ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CHATBOT_YN                     IS '챗봇여부'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CHATBOT_STAT_CD                IS '챗봇상태코드'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.TRANS_TALK_CONTACT_ID          IS '전달상담이력ID'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.TALK_INQRY_CD                  IS '문의유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.ORDER_NUMBER                   IS '주문번호'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.MEMBER_YN                      IS '회원여부'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CUSTOMER_SEQ                   IS '고객시퀀스'                             ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CUST_MSG_TIME                  IS '고객메시지타임'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CHANNEL                        IS '채널'                                   ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.EXEC_RST_CD                    IS '실행사유코드'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CALLBACK_DT                    IS '콜백일자'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CALLBACK_START_DT              IS '콜백시작일자'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CALLBACK_YN                    IS '콜백여부'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.RETRY_ROUNTING_CNT             IS '라우팅재시도횟수'                       ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.BOT_SERVICE_NM                 IS '챗봇서비스명'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.BOT_SERVICE_CD                 IS '챗봇서비스코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.BOT_INQRY_NM                   IS '챗봇문의유형명'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.BOT_INQRY_CD                   IS '챗봇문의유형코드'                       ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.BOT_CALL_ID                    IS '챗봇요청ID'                             ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.TALK_READY_DT                  IS '문의유형 선택 시간'                     ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.CALL_TYP_CD                    IS '채널유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.DESIGNATED_USER_ID             IS '지정된 상담사아이디'                    ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.REGR_DEPT_CD                   IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.REGR_ID                        IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.REG_DTTM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.AMDR_DEPT_CD                   IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.AMDR_ID                        IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.UPD_DTTM                       IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.PROC_ID                        IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CHT_USER_RDY.IT_PROCESSING                  IS '전산처리일시'                           ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_USER_RDY_DTL    채팅_사용자배분대기상세
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_USER_RDY_DTL CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_USER_RDY_DTL (  
    TALK_USER_KEY                                                         VARCHAR2(80)      NOT NULL                  ,
    TALK_SERIAL_NUMBER                                                    VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SNDR_KEY                                                        VARCHAR2(60)      NOT NULL                  ,
    TALK_API_CD                                                           VARCHAR2(60)                                ,
    SESSION_ID                                                            VARCHAR2(60)                                ,
    TYPE                                                                  VARCHAR2(60)                                ,
    CONTENT                                                               CLOB                                        ,
    IMAGE_URL                                                             VARCHAR2(1500)                              ,
    IMAGE_TALK_PATH                                                       VARCHAR2(1500)                              ,
    FILE_GROUP_KEY                                                        VARCHAR2(90)                                ,
    LINKS                                                                 VARCHAR2(1500)                              ,
    VIDEO_TALK_PATH                                                       VARCHAR2(1500)                              ,
    VIDEO_URL                                                             VARCHAR2(1500)                              ,
    VIDEO_THUMNAIL_PATH                                                   VARCHAR2(1500)                              ,
    SYS_MSG_ID                                                         VARCHAR2(25)                                ,
    ORG_CONT_ID                                                           NUMBER(22)                                  ,
    ORG_FILE_ID                                                           NUMBER(22)                                  ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              TIMESTAMP                                   ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_USER_RDY_DTL PRIMARY KEY (TALK_USER_KEY,TALK_SERIAL_NUMBER) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_USER_RDY_DTL                            IS '채팅_사용자배분대기상세'                ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.TALK_USER_KEY              IS 'TALK_사용자_키'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.TALK_SERIAL_NUMBER         IS 'TALK_일련_번호'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.CUSTCO_ID               IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.SNDR_KEY             IS 'ASP_센더_키'                            ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.TALK_API_CD                IS 'TALK_API_구분'                          ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.SESSION_ID                 IS '세션아이디'                             ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.TYPE                       IS '메시지_TYPE'                            ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.CONTENT                    IS '컨텐츠'                                 ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.IMAGE_URL                  IS '이미지URL'                              ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.IMAGE_TALK_PATH            IS '이미지파일경로'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.LINKS                      IS '링크정보'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.VIDEO_TALK_PATH            IS '비디오경로'                             ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.VIDEO_URL                  IS '비디오URL'                              ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.VIDEO_THUMNAIL_PATH        IS '비디오썸네일경로'                       ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.SYS_MSG_ID              IS '발송된 시스템메시지 ID'                 ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.ORG_CONT_ID                IS '대화내용원본테이블 KEY'                 ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.ORG_FILE_ID                IS '파일원본ID'                             ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.REGR_DEPT_CD               IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.REGR_ID                    IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.REG_DTTM                   IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.AMDR_DEPT_CD               IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.AMDR_ID                    IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.UPD_DTTM                   IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.PROC_ID                    IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.IT_PROCESSING              IS '처리일시'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.FILE_GROUP_KEY             IS '파일그룹키';



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_WRK_TIME_HST    채팅_업무시간내역
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_WRK_TIME_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_WRK_TIME_HST (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    ID                                                                    VARCHAR2(90)      NOT NULL                  ,
    MANAGE_DATE_FROM                                                      CHAR(8)           NOT NULL                  ,
    MANAGE_DATE_TO                                                        CHAR(8)           NOT NULL                  ,
    WORK_TIME_FROM                                                        CHAR(4)           NOT NULL                  ,
    WORK_TIME_TO                                                          CHAR(4)           NOT NULL                  ,
    USE_YN                                                                VARCHAR2(3)       NOT NULL                  ,
    REG_ID                                                                VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    UPD_ID                                                                VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         DATE                                        ,
    CONSTRAINT XPK_PLT_CHT_WRK_TIME_HST PRIMARY KEY (CUSTCO_ID,ID)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_WRK_TIME_HST                            IS '채팅_업무시간내역'                      ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.CUSTCO_ID               IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.ID                         IS '키값'                                   ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.MANAGE_DATE_FROM           IS '관리 시작일(FROM)'                      ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.MANAGE_DATE_TO             IS '관리 시작일(TO)'                        ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.WORK_TIME_FROM             IS '상담가능시간(FROM)'                     ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.WORK_TIME_TO               IS '상담가능시간(TO)'                       ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.USE_YN                     IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.REG_ID                     IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.REG_DTTM                   IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.UPD_ID                     IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.UPD_DTTM                   IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.PROC_ID                    IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_WRK_TIME_HST.IT_PROCESSING              IS '처리일시'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_UNT_LNK    채팅_비대면링크관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_UNT_LNK CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_UNT_LNK (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    UNTACT_ID                                                             VARCHAR2(75)      NOT NULL                  ,
    SYSTEM_INTERFACE_CD                                                   VARCHAR2(100)                               ,
    SNDRCV_CD                                                             VARCHAR2(30)      NOT NULL                  ,
    MSG_TYPE                                                              VARCHAR2(6)       NOT NULL                  ,
    MSG_CN                                                           VARCHAR2(3000)    NOT NULL                  ,
    LINKS_TYPE                                                            VARCHAR2(6)       NOT NULL                  ,
    BTN_NM                                                                VARCHAR2(84)      NOT NULL                  ,
    EXTRA                                                                 VARCHAR2(600)                               ,
    URL_MOBILE                                                            VARCHAR2(3000)                              ,
    URL_PC                                                                VARCHAR2(3000)                              ,
    SCHEME_ADROID                                                         VARCHAR2(3000)                              ,
    SCHEME_IOS                                                            VARCHAR2(3000)                              ,
    SORT_ORD                                                              NUMBER(22)        NOT NULL                  ,
    USE_YN                                                                CHAR(2)           NOT NULL                  ,
    REG_DEPT_CD                                                           VARCHAR2(60)                                ,
    REG_ID                                                                VARCHAR2(60)                                ,
    REG_DTTM                                                              DATE                                        ,
    UPD_DEPT_CD                                                           VARCHAR2(60)                                ,
    UPD_ID                                                                VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_UNT_LNK PRIMARY KEY (CUSTCO_ID,UNTACT_ID)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_UNT_LNK                                 IS '채팅_비대면링크관리'                    ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.UNTACT_ID                       IS '비대면링크ID'                           ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.SYSTEM_INTERFACE_CD             IS '시스템연동코드'                         ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.SNDRCV_CD                       IS '수신발신코드'                           ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.MSG_TYPE                        IS '메시지타입'                             ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.MSG_CN                     IS '메시지내용'                             ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.LINKS_TYPE                      IS '링크 버튼 타입'                         ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.BTN_NM                          IS '링크버튼명'                             ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.EXTRA                           IS '링크버튼코드'                           ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.URL_MOBILE                      IS '모바일 웹 URL'                          ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.URL_PC                          IS 'PC웹 URL'                               ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.SCHEME_ADROID                   IS 'SCHEME_ADROID'                          ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.SCHEME_IOS                      IS 'SCHEME_IOS'                             ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.SORT_ORD                        IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.USE_YN                          IS '사용여부'                               ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.REG_DEPT_CD                     IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.REG_ID                          IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.UPD_DEPT_CD                     IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.UPD_ID                          IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.PROC_ID                         IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_UNT_LNK.IT_PROCESSING                   IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_USER_CHAT_SET    채팅_상담사채팅허용설정
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_USER_CHAT_SET CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_USER_CHAT_SET (  
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    MAX_CHAT_AGENT                                                        NUMBER(22)                                  ,
    VIEW_BASESCRIPT_YN                                                    VARCHAR2(3)       DEFAULT 'Y'               ,
    CONSTRAINT XPK_PLT_CHT_USER_CHAT_SET PRIMARY KEY (USER_ID)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_USER_CHAT_SET                           IS '채팅_상담사채팅허용설정'                ;
COMMENT ON COLUMN PLT_CHT_USER_CHAT_SET.USER_ID                   IS '상담사ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_CHAT_SET.CUSTCO_ID              IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_USER_CHAT_SET.MAX_CHAT_AGENT            IS '채팅제한수'                             ;
COMMENT ON COLUMN PLT_CHT_USER_CHAT_SET.VIEW_BASESCRIPT_YN        IS '공통스크립트노출여부'                   ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_CUTT_ORG_FILE    채팅_파일원본테이블
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_CUTT_ORG_FILE CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_CUTT_ORG_FILE (  
    ORG_FILE_ID                                                           NUMBER(22)        NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    ORG_FILE                                                              BLOB              NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP                                   ,
    FILE_EXTN                                                             VARCHAR2(60)                                ,
    CONSTRAINT XPK_PLT_CHT_CUTT_ORG_FILE PRIMARY KEY (ORG_FILE_ID)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_CUTT_ORG_FILE                           IS '채팅_파일원본테이블'                    ;
COMMENT ON COLUMN PLT_CHT_CUTT_ORG_FILE.ORG_FILE_ID               IS '파일KEY(시퀀스)'                        ;
COMMENT ON COLUMN PLT_CHT_CUTT_ORG_FILE.CUSTCO_ID              IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_CUTT_ORG_FILE.ORG_FILE                  IS '파일'                                   ;
COMMENT ON COLUMN PLT_CHT_CUTT_ORG_FILE.IT_PROCESSING             IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_CUTT_ORG_FILE.FILE_EXTN                 IS '파일확장자'                             ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_LAST    채팅_최종접촉상담사
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_LAST CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_LAST (  
    TALK_USER_KEY                                                         VARCHAR2(80)      NOT NULL                  ,
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SNDR_KEY                                                        VARCHAR2(60)      NOT NULL                  ,
    TALK_START_DT                                                         VARCHAR2(42)                                ,
    CONSTRAINT XPK_PLT_CHT_LAST PRIMARY KEY (TALK_USER_KEY,USER_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_LAST                                    IS '채팅_최종접촉상담사'                    ;
COMMENT ON COLUMN PLT_CHT_LAST.TALK_USER_KEY                      IS 'TALK_사용자_키'                         ;
COMMENT ON COLUMN PLT_CHT_LAST.USER_ID                            IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_CHT_LAST.CUSTCO_ID                       IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_LAST.SNDR_KEY                     IS 'ASP_센더_키'                            ;
COMMENT ON COLUMN PLT_CHT_LAST.TALK_START_DT                      IS '상담시작일시'                           ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_STACK    채팅_상담수행건수
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_STACK CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_STACK (  
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    TALK_GB                                                               VARCHAR2(30)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    TALK_SUM                                                              NUMBER(22)        NOT NULL                  ,
    TALK_START_DT                                                         VARCHAR2(42)                                ,
    CONSTRAINT XPK_PLT_CHT_STACK PRIMARY KEY (CUSTCO_ID,USER_ID,TALK_GB) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_STACK                                   IS '채팅_상담수행건수'                      ;
COMMENT ON COLUMN PLT_CHT_STACK.USER_ID                           IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_CHT_STACK.TALK_GB                           IS '구분'                                   ;
COMMENT ON COLUMN PLT_CHT_STACK.CUSTCO_ID                      IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_STACK.TALK_SUM                          IS '합계'                                   ;
COMMENT ON COLUMN PLT_CHT_STACK.TALK_START_DT                     IS '최종 상담시작일자'                      ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_USER_RDY_DET_HST    채팅_사용자분배대기상세내역
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_USER_RDY_DET_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_USER_RDY_DET_HST (  
    TALK_USER_KEY                                                         VARCHAR2(80)      NOT NULL                  ,
    TALK_SERIAL_NUMBER                                                    VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SNDR_KEY                                                        VARCHAR2(60)      NOT NULL                  ,
    TALK_API_CD                                                           VARCHAR2(60)                                ,
    SESSION_ID                                                            VARCHAR2(60)                                ,
    TYPE                                                                  VARCHAR2(60)                                ,
    CONTENT                                                               CLOB                                        ,
    IMAGE_URL                                                             VARCHAR2(1500)                              ,
    IMAGE_TALK_PATH                                                       VARCHAR2(1500)                              ,
    LINKS                                                                 VARCHAR2(1500)                              ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              TIMESTAMP                                   ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_USER_RDY_DET_HST                        IS '채팅_사용자분배대기상세내역'            ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.TALK_USER_KEY          IS 'TALK_사용자_키'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.TALK_SERIAL_NUMBER     IS 'TALK_일련_번호'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.CUSTCO_ID           IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.SNDR_KEY         IS 'ASP_센더_키'                            ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.TALK_API_CD            IS 'TALK_API_구분'                          ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.SESSION_ID             IS '세션아이디'                             ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.TYPE                   IS '메시지_TYPE'                            ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.CONTENT                IS '컨텐츠'                                 ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.IMAGE_URL              IS '이미지URL'                              ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.IMAGE_TALK_PATH        IS '이미지파일경로'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.LINKS                  IS '링크정보'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.REGR_DEPT_CD           IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.REGR_ID                IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.REG_DTTM               IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.AMDR_DEPT_CD           IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.AMDR_ID                IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.UPD_DTTM               IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.PROC_ID                IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_DET_HST.IT_PROCESSING          IS '처리일시'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_USER_RDY_HST    채팅_사용자배분대기내역
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_USER_RDY_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_USER_RDY_HST (  
    TALK_USER_KEY                                                         VARCHAR2(80)      NOT NULL                  ,
    TALK_SERIAL_NUMBER                                                    VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SNDR_KEY                                                        VARCHAR2(60)      NOT NULL                  ,
    TALK_READY_CD                                                         VARCHAR2(30)                                ,
    TALK_DIST_DT                                                          VARCHAR2(42)                                ,
    TALK_READ_DT                                                          VARCHAR2(42)                                ,
    USER_ID                                                               VARCHAR2(60)                                ,
    MSG_TIME                                                              VARCHAR2(12)      DEFAULT '0'               ,
    CHATBOT_YN                                                            CHAR(1)                                     ,
    CHATBOT_STAT_CD                                                       VARCHAR2(30)                                ,
    TALK_INQRY_CD                                                         VARCHAR2(120)                               ,
    ORDER_NUMBER                                                          VARCHAR2(60)                                ,
    MEMBER_YN                                                             VARCHAR2(3)       DEFAULT 'N'               ,
    CUSTOMER_SEQ                                                          NUMBER(22)                                  ,
    RETRY_ROUNTING_CNT                                                    NUMBER(22)                                  ,
    EXEC_RST_CD                                                           VARCHAR2(3)                                 ,
    CALLBACK_DT                                                           VARCHAR2(42)                                ,
    CALLBACK_START_DT                                                     VARCHAR2(42)                                ,
    CALLBACK_YN                                                           VARCHAR2(3)                                 ,
    BOT_SERVICE_NM                                                        VARCHAR2(180)                               ,
    BOT_SERVICE_CD                                                        VARCHAR2(120)                               ,
    BOT_INQRY_NM                                                          VARCHAR2(180)                               ,
    BOT_INQRY_CD                                                          VARCHAR2(120)                               ,
    BOT_CALL_ID                                                           VARCHAR2(100)                               ,
    TALK_READY_DT                                                         VARCHAR2(42)                                ,
    CALL_TYP_CD                                                           VARCHAR2(60)                                ,
    DESIGNATED_USER_ID                                                    VARCHAR2(60)                                ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              TIMESTAMP                                   ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_CHT_USER_RDY_HST PRIMARY KEY (TALK_USER_KEY,TALK_SERIAL_NUMBER, IT_PROCESSING) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHT_USER_RDY_HST                            IS '채팅_사용자배분대기내역'                ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.TALK_USER_KEY              IS 'TALK_사용자_키'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.TALK_SERIAL_NUMBER         IS 'TALK_일련_번호'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.CUSTCO_ID               IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.SNDR_KEY             IS 'ASP_센더_키'                            ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.TALK_READY_CD              IS '배분_상담_구분'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.TALK_DIST_DT               IS '배분대기등록시간'                       ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.TALK_READ_DT               IS '상담대기등록시간'                       ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.USER_ID                    IS '상담사ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.MSG_TIME                   IS '메세지 시간'                            ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.CHATBOT_YN                 IS '챗봇여부'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.CHATBOT_STAT_CD            IS '챗봇상태코드'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.TALK_INQRY_CD              IS '문의유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.ORDER_NUMBER               IS '주문번호'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.MEMBER_YN                  IS '회원여부'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.CUSTOMER_SEQ               IS '고객시퀀스'                             ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.RETRY_ROUNTING_CNT         IS '배분재시도횟수'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.EXEC_RST_CD                IS '처리결과코드'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.CALLBACK_DT                IS '콜백일자'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.CALLBACK_START_DT          IS '콜백시작일자'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.CALLBACK_YN                IS '콜백여부'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.BOT_SERVICE_NM             IS '챗봇서비스명'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.BOT_SERVICE_CD             IS '챗봇서비스코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.BOT_INQRY_NM               IS '챗봇문의유형명'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.BOT_INQRY_CD               IS '챗봇문의유형코드'                       ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.BOT_CALL_ID                IS '챗봇요청ID'                             ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.TALK_READY_DT              IS '문의유형 선택 시간'                     ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.CALL_TYP_CD                IS '채널유형코드'                           ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.DESIGNATED_USER_ID         IS '지정된 상담사아이디'                    ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.REGR_DEPT_CD               IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.REGR_ID                    IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.REG_DTTM                   IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.AMDR_DEPT_CD               IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.AMDR_ID                    IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.UPD_DTTM                   IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.PROC_ID                    IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CHT_USER_RDY_HST.IT_PROCESSING              IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_QA_EVAL    채팅_QA평가
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_QA_EVAL CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_QA_EVAL (
    CUSTCO_ID                                                          VARCHAR2(10)                             , 
	ID                                                                    NUMBER(20)        NOT NULL               , 
	QA_YM                                                                 VARCHAR2(6)                              , 
	QA_TY_CD                                                              VARCHAR2(10)                             , 
	QA_SEQ                                                                NUMBER(2)                                , 
	USER_ID                                                               VARCHAR2(60)                             , 
	QA_USER_ID                                                            VARCHAR2(20)                             , 
	TALK_CONTACT_ID                                                       VARCHAR2(90)                             , 
	QA_CN                                                                 VARCHAR2(600)                            , 
	QA_EXT_CHK                                                            VARCHAR2(1)       DEFAULT '0'            , 
	QA_END                                                                VARCHAR2(10)      DEFAULT 'N'            , 
	QA_NOTIN                                                              VARCHAR2(10)      DEFAULT 'N'            , 
	QA_FIN                                                                VARCHAR2(10)      DEFAULT 'N'            , 
	REGR_ID                                                               VARCHAR2(20)                             , 
	REG_DTTM                                                              VARCHAR2(20)                             , 
	AMDR_ID                                                               VARCHAR2(20)                             , 
	UPD_DTTM                                                              VARCHAR2(20)                             ,
    CONSTRAINT XPK_PLT_CHT_QA_EVAL PRIMARY KEY (ID) 
   )
   --TABLESPACE TS_PALETTE
   ;

   COMMENT ON COLUMN PLT_CHT_QA_EVAL.CUSTCO_ID                 IS 'ASP_고객사_키'                            ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.ID                           IS 'QA_ID'                                   ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.QA_YM                        IS 'QA_년월'                                 ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.QA_TY_CD                     IS 'QA구분코드'                              ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.QA_SEQ                       IS 'QA회차'                                  ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.USER_ID                      IS '상담원ID'                                ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.QA_USER_ID                   IS '평가자ID'                                ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.TALK_CONTACT_ID              IS '상담ID'                                  ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.QA_CN                        IS 'QA의견'                                  ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.QA_EXT_CHK                   IS 'QA추출대상'                              ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.QA_END                       IS 'QA추출마감'                              ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.QA_NOTIN                     IS 'QA제외대상'                              ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.QA_FIN                       IS 'QA평가마감'                              ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.REGR_ID                      IS '등록자ID'                                ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.REG_DTTM                     IS '등록일시'                                ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.AMDR_ID                      IS '수정자ID'                                ;
   COMMENT ON COLUMN PLT_CHT_QA_EVAL.UPD_DTTM                     IS '수정일시'                                ;
   COMMENT ON TABLE PLT_CHT_QA_EVAL                               IS '채팅_QA평가'                             ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_QA_EVAL_RST    채팅_QA결과
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_QA_EVAL_RST CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_QA_EVAL_RST (	
    CUSTCO_ID                                                         VARCHAR2(10)                               , 
    ID                                                                   NUMBER(20)           NOT NULL              , 
    QA_YM                                                                VARCHAR2(6)                                , 
    QA_TY_CD                                                             VARCHAR2(10)                               , 
    QA_TY_ID                                                             VARCHAR2(30)                               , 
    QA_SEQ                                                               NUMBER(3)                                  , 
    TALK_CONTACT_ID                                                      VARCHAR2(90)                               , 
    USER_ID                                                              VARCHAR2(60)                               , 
    SCORE_CHK                                                            VARCHAR2(1)                                , 
    EVAL_CN                                                              VARCHAR2(600)                              , 
    REGR_ID                                                              VARCHAR2(20)                               , 
    REG_DTTM                                                             VARCHAR2(20)                               , 
    AMDR_ID                                                              VARCHAR2(20)                               , 
    UPD_DTTM                                                             VARCHAR2(20)                               ,
CONSTRAINT XPK_PLT_CHT_QA_EVAL_RST PRIMARY KEY (ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.CUSTCO_ID              IS 'ASP_고객사_키'                             ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.ID                        IS 'QA_ID'                                    ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.QA_YM                     IS 'QA_년월'                                  ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.QA_TY_CD                  IS 'QA구분코드'                                ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.QA_TY_ID                  IS 'QA_타입ID'                                 ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.QA_SEQ                    IS 'QA회차'                                    ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.TALK_CONTACT_ID           IS '상담ID'                                    ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.USER_ID                   IS '상담원ID'                                   ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.SCORE_CHK                 IS '점수체크'                                   ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.EVAL_CN                   IS '평가COMMENT'                               ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.REGR_ID                   IS '등록자ID'                                   ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.REG_DTTM                  IS '등록일시'                                   ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.AMDR_ID                   IS '수정자ID'                                   ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_RST.UPD_DTTM                  IS '수정일시'                                   ;
COMMENT ON TABLE PLT_CHT_QA_EVAL_RST                            IS '채팅_QA결과'                                ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_QA_EVAL_SHT    채팅_QA평가지
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHT_QA_EVAL_SHT CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHT_QA_EVAL_SHT (	
    CUSTCO_ID                                                         VARCHAR2(10)                                 , 
    ID                                                                   NUMBER(20)       NOT NULL                    , 
    QA_YM                                                                VARCHAR2(6)                                  , 
    QA_TY_CD                                                             VARCHAR2(10)                                 , 
    QA_TY_ID                                                             VARCHAR2(30)                                 , 
    QA_TY_L_CD                                                           VARCHAR2(30)                                 , 
    QA_TY_L                                                              VARCHAR2(30)                                 , 
    QA_TY_M_CD                                                           VARCHAR2(30)                                 , 
    QA_TY_M                                                              VARCHAR2(30)                                 , 
    QA_TY_S                                                              VARCHAR2(100)                                , 
    QA_TY_S_P                                                            NUMBER(3)                                    , 
    REG_DTTM                                                             VARCHAR2(20)                                 , 
    REGR_ID                                                              VARCHAR2(20)                                 , 
    UPD_DTTM                                                             VARCHAR2(20)                                 , 
    AMDR_ID                                                              VARCHAR2(20)                                 ,
    CONSTRAINT XPK_PLT_CHT_QA_EVAL_SHT PRIMARY KEY (ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.CUSTCO_ID              IS 'ASP_고객사_키'                                ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.ID                        IS 'QA_ID'                                       ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.QA_YM                     IS 'QA_년월'                                     ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.QA_TY_CD                  IS 'QA구분코드'                                  ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.QA_TY_ID                  IS 'QA_타입ID'                                   ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.QA_TY_L_CD                IS 'QA대분류_코드'                                ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.QA_TY_L                   IS 'QA유형1'                                     ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.QA_TY_M_CD                IS 'QA중분류_코드'                               ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.QA_TY_M                   IS 'QA유형2'                                     ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.QA_TY_S                   IS 'QA유형3'                                     ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.QA_TY_S_P                 IS 'QA유형3_배점'                                ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.REG_DTTM                  IS '등록일시'                                     ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.REGR_ID                   IS '등록자ID'                                     ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.AMDR_ID                   IS '수정자ID'                                     ;
COMMENT ON COLUMN PLT_CHT_QA_EVAL_SHT.UPD_DTTM                  IS '수정일시'                                     ;
COMMENT ON TABLE PLT_CHT_QA_EVAL_SHT                            IS '채팅_QA평가지'                                ;