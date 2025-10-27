--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_ANNC    공지마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_ANNC CASCADE CONSTRAINTS;

CREATE TABLE PLT_ANNC (  
    ANNC_NO                                                               VARCHAR2(30)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    ANNC_TY                                                               VARCHAR2(10)                                ,
    ANNC_BEGIN_DATE                                                       VARCHAR2(8)                                 ,
    ANNC_EOT_DATE                                                         VARCHAR2(8)                                 ,
    ANNC_TIT                                                              VARCHAR2(200)                               ,
    ANNC_CNTN                                                             CLOB                                        ,
    FILE_GROUP_KEY                                                        VARCHAR2(30)                                ,
    INQ_CNT                                                               NUMBER(10)                                  ,
    SYMB_YN                                                               VARCHAR2(1)                                 ,
    EMER_YN                                                               VARCHAR2(1)                                 ,
    USE_YN                                                                VARCHAR2(1)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_ANNC PRIMARY KEY (ANNC_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_ANNC                                        IS '공지마스터'                             ;
COMMENT ON COLUMN PLT_ANNC.ANNC_NO                                IS '공지번호'                               ;
COMMENT ON COLUMN PLT_ANNC.CENT_TY                                IS '센터구분'                               ;
COMMENT ON COLUMN PLT_ANNC.ANNC_TY                                IS '공지유형'                               ;
COMMENT ON COLUMN PLT_ANNC.ANNC_BEGIN_DATE                        IS '공지시작일자'                           ;
COMMENT ON COLUMN PLT_ANNC.ANNC_EOT_DATE                          IS '공지종료일자'                           ;
COMMENT ON COLUMN PLT_ANNC.ANNC_TIT                               IS '공지제목'                               ;
COMMENT ON COLUMN PLT_ANNC.ANNC_CNTN                              IS '공지내용'                               ;
COMMENT ON COLUMN PLT_ANNC.FILE_GROUP_KEY                         IS '파일그룹키'                             ;
COMMENT ON COLUMN PLT_ANNC.INQ_CNT                                IS '조회수'                                 ;
COMMENT ON COLUMN PLT_ANNC.SYMB_YN                                IS '상단표시여부'                           ;
COMMENT ON COLUMN PLT_ANNC.EMER_YN                                IS '긴급여부'                               ;
COMMENT ON COLUMN PLT_ANNC.USE_YN                                 IS '사용여부'                               ;
COMMENT ON COLUMN PLT_ANNC.REG_DTIM                               IS '등록일시'                               ;
COMMENT ON COLUMN PLT_ANNC.REG_MAN                                IS '등록자'                                 ;
COMMENT ON COLUMN PLT_ANNC.CHNG_DTIM                              IS '변경일시'                               ;
COMMENT ON COLUMN PLT_ANNC.CHNG_MAN                               IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_ANNC_INQ_HST    공지조회이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_ANNC_INQ_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_ANNC_INQ_HST (  
    ANNC_NO                                                               VARCHAR2(30)      NOT NULL                  ,
    USER_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_ANNC_INQ_HST PRIMARY KEY (ANNC_NO,USER_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_ANNC_INQ_HST                                IS '공지조회이력'                           ;
COMMENT ON COLUMN PLT_ANNC_INQ_HST.ANNC_NO                        IS '공지번호'                               ;
COMMENT ON COLUMN PLT_ANNC_INQ_HST.USER_ID                        IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_ANNC_INQ_HST.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_ANNC_INQ_HST.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_ANNC_INQ_HST.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_ANNC_INQ_HST.CHNG_MAN                       IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_BAT    배치마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_BAT CASCADE CONSTRAINTS;

CREATE TABLE PLT_BAT (  
    BATCH_KEY                                                             VARCHAR2(30)      NOT NULL                  ,
    BATCH_NM                                                              VARCHAR2(200)                               ,
    BATCH_EXECUT_CLASS                                                    VARCHAR2(200)                               ,
    BATCH_EXECUT_CYCLE                                                    VARCHAR2(100)                               ,
    BATCH_STTUS                                                           VARCHAR2(20)                                ,
    BATCH_EXECUT_LC                                                       VARCHAR2(100)                               ,
    BATCH_PARAMTR_SMPLE                                                   VARCHAR2(200)                               ,
    USE_YN                                                                CHAR(1)                                     ,
    LAST_EXECUT_DT                                                        TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(20)                                ,
    IT_PROCESSING                                                         TIMESTAMP                                   ,
    CONSTRAINT XPK_PLT_BAT PRIMARY KEY (BATCH_KEY) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_BAT                                         IS '배치마스터'                             ;
COMMENT ON COLUMN PLT_BAT.BATCH_KEY                               IS '배치키'                                 ;
COMMENT ON COLUMN PLT_BAT.BATCH_NM                                IS '배치명'                                 ;
COMMENT ON COLUMN PLT_BAT.BATCH_EXECUT_CLASS                      IS '배치실행클래스'                         ;
COMMENT ON COLUMN PLT_BAT.BATCH_EXECUT_CYCLE                      IS '배치실행주기'                           ;
COMMENT ON COLUMN PLT_BAT.BATCH_STTUS                             IS '배치상태코드'                           ;
COMMENT ON COLUMN PLT_BAT.BATCH_EXECUT_LC                         IS '배치실행위치'                           ;
COMMENT ON COLUMN PLT_BAT.BATCH_PARAMTR_SMPLE                     IS '배치파라미터샘플'                       ;
COMMENT ON COLUMN PLT_BAT.USE_YN                                  IS '사용여부'                               ;
COMMENT ON COLUMN PLT_BAT.LAST_EXECUT_DT                          IS '마지막실행일시'                         ;
COMMENT ON COLUMN PLT_BAT.PROC_ID                                 IS '처리자'                                 ;
COMMENT ON COLUMN PLT_BAT.IT_PROCESSING                           IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_BAT_LOG    배치로그
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_BAT_LOG CASCADE CONSTRAINTS;

CREATE TABLE PLT_BAT_LOG (  
    BATCH_KEY                                                             VARCHAR2(30)      NOT NULL                  ,
    BATCH_ID                                                              VARCHAR2(30)      NOT NULL                  ,
    TRNSMIT_INSTT                                                         VARCHAR2(20)                                ,
    RECPTN_INSTT                                                          VARCHAR2(20)                                ,
    BATCH_BEGIN_DE                                                        VARCHAR2(8)                                 ,
    BATCH_BEGIN_TIME                                                      VARCHAR2(10)                                ,
    BATCH_END_DE                                                          VARCHAR2(8)                                 ,
    BATCH_END_TIME                                                        VARCHAR2(10)                                ,
    BATCH_TOT_CO                                                          NUMBER(10)                                  ,
    BATCH_SUCCES_CO                                                       NUMBER(10)                                  ,
    BATCH_FAILR_CO                                                        NUMBER(10)                                  ,
    BATCH_ERROR_LOG_FILE_NM                                               VARCHAR2(200)                               ,
    BATCH_EXECUT_LC                                                       VARCHAR2(100)                               ,
    PROCESS_CODE                                                          VARCHAR2(20)                                ,
    BATCH_PROCESS_RESULT                                                  VARCHAR2(1000)                              ,
    PROC_ID                                                               VARCHAR2(20)                                ,
    IT_PROCESSING                                                         TIMESTAMP                                   ,
    CONSTRAINT XPK_PLT_BAT_LOG PRIMARY KEY (BATCH_KEY,BATCH_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_BAT_LOG                                     IS '배치로그'                               ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_KEY                           IS '배치키'                                 ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_ID                            IS '배치ID'                                 ;
COMMENT ON COLUMN PLT_BAT_LOG.TRNSMIT_INSTT                       IS '송신기관'                               ;
COMMENT ON COLUMN PLT_BAT_LOG.RECPTN_INSTT                        IS '수신기관'                               ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_BEGIN_DE                      IS '배치시작일자'                           ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_BEGIN_TIME                    IS '배치시작시간'                           ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_END_DE                        IS '배치종료일자'                           ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_END_TIME                      IS '배치종료시간'                           ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_TOT_CO                        IS '배치총건수'                             ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_SUCCES_CO                     IS '배치성공건수'                           ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_FAILR_CO                      IS '배치실패건수'                           ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_ERROR_LOG_FILE_NM             IS '배치오류로그파일명'                     ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_EXECUT_LC                     IS '배치실행위치'                           ;
COMMENT ON COLUMN PLT_BAT_LOG.PROCESS_CODE                        IS '처리코드'                               ;
COMMENT ON COLUMN PLT_BAT_LOG.BATCH_PROCESS_RESULT                IS '배치처리결과'                           ;
COMMENT ON COLUMN PLT_BAT_LOG.PROC_ID                             IS '처리자'                                 ;
COMMENT ON COLUMN PLT_BAT_LOG.IT_PROCESSING                       IS '전산처리일시'                           ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_BTN_AUTH    버튼권한
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_BTN_AUTH CASCADE CONSTRAINTS;

CREATE TABLE PLT_BTN_AUTH (  
    ATRT_GROUP_ID                                                         VARCHAR2(30)      NOT NULL                  ,
    MENU_ID                                                               VARCHAR2(30)      NOT NULL                  ,
    BTN_ID                                                                VARCHAR2(30)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_BTN_AUTH PRIMARY KEY (ATRT_GROUP_ID,MENU_ID,BTN_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_BTN_AUTH                                    IS '버튼권한'                               ;
COMMENT ON COLUMN PLT_BTN_AUTH.ATRT_GROUP_ID                      IS '권한그룹ID'                             ;
COMMENT ON COLUMN PLT_BTN_AUTH.MENU_ID                            IS '메뉴ID'                                 ;
COMMENT ON COLUMN PLT_BTN_AUTH.BTN_ID                             IS '버튼ID'                                 ;
COMMENT ON COLUMN PLT_BTN_AUTH.REGR_ID                            IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_BTN_AUTH.REG_DTTM                           IS '등록일시'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_BTN    버튼
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_BTN CASCADE CONSTRAINTS;

CREATE TABLE PLT_BTN (  
    BTN_ID                                                                VARCHAR2(30)      NOT NULL                  ,
    MENU_ID                                                               VARCHAR2(30)      NOT NULL                  ,
    SCR_BTN_ID                                                            VARCHAR2(30)      NOT NULL                  ,
    SCR_BTN_NM                                                            VARCHAR2(100)                               ,
    BTN_TYPE                                                              VARCHAR2(30)                                ,
    REGR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_ID                                                               VARCHAR2(20)                                ,
    UPD_DTTM                                                              DATE                                        ,
    CONSTRAINT XPK_PLT_BTN PRIMARY KEY (BTN_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_BTN                                         IS '버튼'                                   ;
COMMENT ON COLUMN PLT_BTN.BTN_ID                                  IS '버튼ID'                                 ;
COMMENT ON COLUMN PLT_BTN.MENU_ID                                 IS '메뉴ID'                                 ;
COMMENT ON COLUMN PLT_BTN.SCR_BTN_ID                              IS '화면버튼ID'                             ;
COMMENT ON COLUMN PLT_BTN.SCR_BTN_NM                              IS '화면버튼명'                             ;
COMMENT ON COLUMN PLT_BTN.BTN_TYPE                                IS '버튼타입'                               ;
COMMENT ON COLUMN PLT_BTN.REGR_ID                                 IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_BTN.REG_DTTM                                IS '등록일시'                               ;
COMMENT ON COLUMN PLT_BTN.AMDR_ID                                 IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_BTN.UPD_DTTM                                IS '수정일시'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHN    비즈채널관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CHN CASCADE CONSTRAINTS;

CREATE TABLE PLT_CHN (  
    CHN_CLSF_CD                                                       VARCHAR2(10)      NOT NULL                  ,
    CHN_URI                                                      VARCHAR2(60)      NOT NULL                  ,
    CHN_NM                                                       VARCHAR2(60)      NOT NULL                  ,
    CHN_CO_NM                                                           VARCHAR2(60)      NOT NULL                  ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    AMDR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    UPD_DTTM                                                              DATE              NOT NULL                  ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         DATE                                        ,
    CONSTRAINT XPK_PLT_CHN PRIMARY KEY (CHN_CLSF_CD) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CHN                                 IS '비즈채널관리'                           ;
COMMENT ON COLUMN PLT_CHN.CHN_CLSF_CD                 IS '비즈_서비스_코드'                       ;
COMMENT ON COLUMN PLT_CHN.CHN_URI                IS '비즈_서비스_URI'                        ;
COMMENT ON COLUMN PLT_CHN.CHN_NM                 IS '비즈_서비스_명'                         ;
COMMENT ON COLUMN PLT_CHN.CHN_CO_NM                     IS '비즈회사명'                             ;
COMMENT ON COLUMN PLT_CHN.REGR_DEPT_CD                    IS '등록자부서'                             ;
COMMENT ON COLUMN PLT_CHN.REGR_ID                         IS '등록자아이디'                           ;
COMMENT ON COLUMN PLT_CHN.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CHN.AMDR_DEPT_CD                    IS '수정자부서'                             ;
COMMENT ON COLUMN PLT_CHN.AMDR_ID                         IS '수정자아이디'                           ;
COMMENT ON COLUMN PLT_CHN.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CHN.PROC_ID                         IS '처리자아이디'                           ;
COMMENT ON COLUMN PLT_CHN.IT_PROCESSING                   IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_ASP_CUS    고객사관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_ASP_CUS CASCADE CONSTRAINTS;

CREATE TABLE PLT_ASP_CUS (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    CO_NM                                                            VARCHAR2(60)      NOT NULL                  ,
    PHN_NO                                                             VARCHAR2(60)                                ,
    EML                                                                 VARCHAR2(60)                                ,
    HOMEPAGE_ADDR                                                         VARCHAR2(60)                                ,
    PRODUCT_CD                                                            VARCHAR2(60)      NOT NULL                  ,
    SRVC_BGNG_DT                                                       VARCHAR2(12)      NOT NULL                  ,
    SRVC_END_DT                                                         VARCHAR2(12)      NOT NULL                  ,
    SRVC_MAINT_YN                                                          VARCHAR2(1)       NOT NULL                  ,
    BZMN_COR                                                           VARCHAR2(120)                               ,
    CUST_AGREE_YN                                                         CHAR(1)           NOT NULL                  ,
    CUST_AGREE_DTTM                                                       DATE              NOT NULL                  ,
    ACCOUNT_CREATION_LIMIT                                                NUMBER(22)        NOT NULL                  ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    AMDR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    UPD_DTTM                                                              DATE              NOT NULL                  ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_ASP_CUS PRIMARY KEY (CUSTCO_ID)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_ASP_CUS                                     IS '고객사관리'                             ;
COMMENT ON COLUMN PLT_ASP_CUS.CUSTCO_ID                        IS 'ASP_고객사_KEY'                         ;
COMMENT ON COLUMN PLT_ASP_CUS.CO_NM                          IS '회사명'                                 ;
COMMENT ON COLUMN PLT_ASP_CUS.PHN_NO                           IS '전화번호'                               ;
COMMENT ON COLUMN PLT_ASP_CUS.EML                               IS '이메일'                                 ;
COMMENT ON COLUMN PLT_ASP_CUS.HOMEPAGE_ADDR                       IS '홈페이지주소'                           ;
COMMENT ON COLUMN PLT_ASP_CUS.PRODUCT_CD                          IS '상품코드'                               ;
COMMENT ON COLUMN PLT_ASP_CUS.SRVC_BGNG_DT                     IS '서비스시작일시'                         ;
COMMENT ON COLUMN PLT_ASP_CUS.SRVC_END_DT                       IS '서비스종료일시'                         ;
COMMENT ON COLUMN PLT_ASP_CUS.SRVC_MAINT_YN                        IS '서비스유지여부'                         ;
COMMENT ON COLUMN PLT_ASP_CUS.BZMN_COR                         IS '사업자등록증'                           ;
COMMENT ON COLUMN PLT_ASP_CUS.CUST_AGREE_YN                       IS '약관동의여부'                           ;
COMMENT ON COLUMN PLT_ASP_CUS.CUST_AGREE_DTTM                     IS '약관동의일자'                           ;
COMMENT ON COLUMN PLT_ASP_CUS.ACCOUNT_CREATION_LIMIT              IS '계정생성제한건수'                       ;
COMMENT ON COLUMN PLT_ASP_CUS.REGR_DEPT_CD                        IS '등록자부서'                             ;
COMMENT ON COLUMN PLT_ASP_CUS.REGR_ID                             IS '등록자아이디'                           ;
COMMENT ON COLUMN PLT_ASP_CUS.REG_DTTM                            IS '등록일시'                               ;
COMMENT ON COLUMN PLT_ASP_CUS.AMDR_DEPT_CD                        IS '수정자부서'                             ;
COMMENT ON COLUMN PLT_ASP_CUS.AMDR_ID                             IS '수정자아이디'                           ;
COMMENT ON COLUMN PLT_ASP_CUS.UPD_DTTM                            IS '수정일시'                               ;
COMMENT ON COLUMN PLT_ASP_CUS.PROC_ID                             IS '처리자아이디'                           ;
COMMENT ON COLUMN PLT_ASP_CUS.IT_PROCESSING                       IS '전산처리일시'                           ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_ASP_CUS_CHN    고객사채널관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_ASP_CUS_CHN CASCADE CONSTRAINTS;

CREATE TABLE PLT_ASP_CUS_CHN (  
    SNDR_KEY                                                        VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    CHN_CLSF_CD                                                       VARCHAR2(10)      NOT NULL                  ,
    UUID                                                             VARCHAR2(60)      NOT NULL                  ,
    SERV_AGREE_YN                                                         CHAR(1)                                     ,
    SERV_AGREE_DTTM                                                       DATE                                        ,
    DSPTCH_PRF_KEY                                                       VARCHAR2(60)                                ,
    DSPTCH_PRF_NM                                                        VARCHAR2(60)                                ,
    TALK_SENDER_ETC_INFO01                                                VARCHAR2(100)                               ,
    TALK_ACCESS_TOKEN                                                     VARCHAR2(300)                               ,
    CHANNEL_SECRET                                                        VARCHAR2(300)                               ,
    SRVC_MAINT_YN                                                          VARCHAR2(100)                               ,
    SORT_ORD                                                              NUMBER(22)                                  ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    AMDR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    UPD_DTTM                                                              DATE              NOT NULL                  ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         DATE                                        ,
    CONSTRAINT XPK_PLT_ASP_CUS_CHN PRIMARY KEY (SNDR_KEY)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_ASP_CUS_CHN                                 IS '고객사채널관리'                         ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.SNDR_KEY                  IS 'ASP_센더_키'                            ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.CHN_CLSF_CD                 IS '비즈_서비스_코드'                       ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.UUID                       IS 'UUID'                              ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.SERV_AGREE_YN                   IS '위탁 동의여부'                          ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.SERV_AGREE_DTTM                 IS '위탁 동의일시'                          ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.DSPTCH_PRF_KEY                 IS 'TALK_센더_키'                           ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.DSPTCH_PRF_NM                  IS 'TALK_센더_명'                           ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.TALK_SENDER_ETC_INFO01          IS 'TALK_센더_추가정보01'                   ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.TALK_ACCESS_TOKEN               IS '라인메신저 송신 ACESS KEY'              ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.CHANNEL_SECRET                  IS '라인메신저 파라미터 인증키'             ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.SRVC_MAINT_YN                    IS '서비스유지여부'                         ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.SORT_ORD                        IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.REGR_DEPT_CD                    IS '등록자부서'                             ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.REGR_ID                         IS '등록자아이디'                           ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.AMDR_DEPT_CD                    IS '수정자부서'                             ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.AMDR_ID                         IS '수정자아이디'                           ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.PROC_ID                         IS '처리자아이디'                           ;
COMMENT ON COLUMN PLT_ASP_CUS_CHN.IT_PROCESSING                   IS '전산처리일시'                           ;

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_ASP_PRD    이용상품
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_ASP_PRD CASCADE CONSTRAINTS;

CREATE TABLE PLT_ASP_PRD (  
    PRODUCT_CD                                                            VARCHAR2(60)      NOT NULL                  ,
    PRODUCT_NAME                                                          VARCHAR2(40)      NOT NULL                  ,
    PRODUCT_DESCRIPTION                                                   VARCHAR2(4000)                              ,
    COST_PER_ACCOUNT                                                      NUMBER(22)        NOT NULL                  ,
    PAYMENT_CYCLE_CD                                                      VARCHAR2(60)      NOT NULL                  ,
    SALES_OR_NOT                                                          CHAR(1)           NOT NULL                  ,
    USE_YN                                                                CHAR(1)           NOT NULL                  ,
    SORT_ORDER                                                            NUMBER(22)        NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_ASP_PRD PRIMARY KEY (PRODUCT_CD)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_ASP_PRD                                     IS '이용상품'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.PRODUCT_CD                          IS '상품코드'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.PRODUCT_NAME                        IS '상품명'                                 ;
COMMENT ON COLUMN PLT_ASP_PRD.PRODUCT_DESCRIPTION                 IS '상품설명'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.COST_PER_ACCOUNT                    IS '계정당비용'                             ;
COMMENT ON COLUMN PLT_ASP_PRD.PAYMENT_CYCLE_CD                    IS '결제주기그룹코드'                       ;
COMMENT ON COLUMN PLT_ASP_PRD.SALES_OR_NOT                        IS '판매여부'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.USE_YN                              IS '사용여부'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.SORT_ORDER                          IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.REGR_ID                             IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.REG_DTTM                            IS '등록일시'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.AMDR_ID                             IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.UPD_DTTM                            IS '수정일시'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.PROC_ID                             IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_ASP_PRD.IT_PROCESSING                       IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_USER    사용자정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_USER CASCADE CONSTRAINTS;

CREATE TABLE PLT_USER (  
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    USER_NM                                                               VARCHAR2(300)                               ,
    USER_NICK                                                             VARCHAR2(300)                               ,
    PWD                                                                   VARCHAR2(300)                               ,
    DEPT_CD                                                               VARCHAR2(60)                                ,
    BRCH_CD                                                               VARCHAR2(60)                                ,
    CEN_TYPE_CD                                                           VARCHAR2(60)                                ,
    USER_GROUP_CD                                                         VARCHAR2(60)                                ,
    USER_TEAM_PTTN_CD                                                     VARCHAR2(60)                                ,
    CNNCT_IP                                                              VARCHAR2(60)                                ,
    PSNT_LOGIN_YN                                                         CHAR(1)                                     ,
    PWD_FAIL_FREQ                                                         NUMBER(22)                                  ,
    BDAY                                                                  VARCHAR2(24)                                ,
    HP_NO                                                                 VARCHAR2(300)                               ,
    INLN_NO                                                               VARCHAR2(60)                                ,
    EML_ADDR                                                            VARCHAR2(300)                               ,
    SOCAL_TYPE                                                            VARCHAR2(60)                                ,
    ENTCO_DT                                                              VARCHAR2(24)                                ,
    RETR_DT                                                               VARCHAR2(24)                                ,
    USER_TYPE_CD                                                          VARCHAR2(60)                                ,
    USER_DUTY_CD                                                          VARCHAR2(60)                                ,
    ETC_INFO01                                                            VARCHAR2(600)                               ,
    ETC_INFO02                                                            VARCHAR2(600)                               ,
    ETC_INFO03                                                            VARCHAR2(600)                               ,
    ETC_INFO04                                                            VARCHAR2(600)                               ,
    ETC_INFO05                                                            VARCHAR2(600)                               ,
    ETC_INFO06                                                            VARCHAR2(600)                               ,
    ETC_INFO07                                                            VARCHAR2(600)                               ,
    ETC_INFO08                                                            VARCHAR2(600)                               ,
    ETC_INFO09                                                            VARCHAR2(600)                               ,
    ETC_INFO10                                                            VARCHAR2(600)                               ,
    USE_YN                                                                CHAR(1)                                     ,
    SPEC_CNSL_CD                                                          VARCHAR2(120)                               ,
    PWD_UPD_DTTM                                                          DATE                                        ,
    PWD_STATUS                                                            VARCHAR2(60)      NOT NULL                  ,
    USER_ATTR_A                                                           VARCHAR2(20)                                ,
    USER_ATTR_B                                                           VARCHAR2(20)                                ,
    USER_ATTR_C                                                           VARCHAR2(20)                                ,
    USER_ATTR_D                                                           VARCHAR2(20)                                ,
    CERTI_YN                                                              CHAR(1)                                     ,
    CERTI_VALUE                                                           VARCHAR2(100)                               ,
    ENCRYPT_KEY                                                           VARCHAR2(300)                               ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_USER PRIMARY KEY (USER_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_USER                                        IS '사용자정보'                             ;
COMMENT ON COLUMN PLT_USER.USER_ID                                IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_USER.CUSTCO_ID                           IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_USER.USER_NM                                IS '사용자이름'                             ;
COMMENT ON COLUMN PLT_USER.USER_NICK                              IS '사용자닉네임'                           ;
COMMENT ON COLUMN PLT_USER.PWD                                    IS '비밀번호'                               ;
COMMENT ON COLUMN PLT_USER.DEPT_CD                                IS '부서코드'                               ;
COMMENT ON COLUMN PLT_USER.BRCH_CD                                IS '지사코드'                               ;
COMMENT ON COLUMN PLT_USER.CEN_TYPE_CD                            IS '센터구분코드'                           ;
COMMENT ON COLUMN PLT_USER.USER_GROUP_CD                          IS '사용자그룹코드'                         ;
COMMENT ON COLUMN PLT_USER.USER_TEAM_PTTN_CD                      IS '사용자팀유형코드'                       ;
COMMENT ON COLUMN PLT_USER.CNNCT_IP                               IS '접속아이피'                             ;
COMMENT ON COLUMN PLT_USER.PSNT_LOGIN_YN                          IS '현재로그인여부'                         ;
COMMENT ON COLUMN PLT_USER.PWD_FAIL_FREQ                          IS '비밀번호실패횟수'                       ;
COMMENT ON COLUMN PLT_USER.BDAY                                   IS '생년월일'                               ;
COMMENT ON COLUMN PLT_USER.HP_NO                                  IS '핸드폰번호'                             ;
COMMENT ON COLUMN PLT_USER.INLN_NO                                IS '내선번호'                               ;
COMMENT ON COLUMN PLT_USER.EML_ADDR                             IS 'E-MAIL주소'                             ;
COMMENT ON COLUMN PLT_USER.SOCAL_TYPE                             IS '양력구분코드'                           ;
COMMENT ON COLUMN PLT_USER.ENTCO_DT                               IS '입사일자'                               ;
COMMENT ON COLUMN PLT_USER.RETR_DT                                IS '퇴사일자'                               ;
COMMENT ON COLUMN PLT_USER.USER_TYPE_CD                           IS '사용자구분코드'                         ;
COMMENT ON COLUMN PLT_USER.USER_DUTY_CD                           IS '사용자직책코드'                         ;
COMMENT ON COLUMN PLT_USER.ETC_INFO01                             IS '기타정보01'                             ;
COMMENT ON COLUMN PLT_USER.ETC_INFO02                             IS '기타정보02'                             ;
COMMENT ON COLUMN PLT_USER.ETC_INFO03                             IS '기타정보03'                             ;
COMMENT ON COLUMN PLT_USER.ETC_INFO04                             IS '기타정보04'                             ;
COMMENT ON COLUMN PLT_USER.ETC_INFO05                             IS '기타정보05'                             ;
COMMENT ON COLUMN PLT_USER.ETC_INFO06                             IS '기타정보06'                             ;
COMMENT ON COLUMN PLT_USER.ETC_INFO07                             IS '기타정보07'                             ;
COMMENT ON COLUMN PLT_USER.ETC_INFO08                             IS '기타정보08'                             ;
COMMENT ON COLUMN PLT_USER.ETC_INFO09                             IS '기타정보09'                             ;
COMMENT ON COLUMN PLT_USER.ETC_INFO10                             IS '기타정보10'                             ;
COMMENT ON COLUMN PLT_USER.USE_YN                                 IS '사용여부'                               ;
COMMENT ON COLUMN PLT_USER.SPEC_CNSL_CD                           IS '전문상담코드'                           ;
COMMENT ON COLUMN PLT_USER.PWD_UPD_DTTM                           IS '비밀번호 수정일시'                      ;
COMMENT ON COLUMN PLT_USER.PWD_STATUS                             IS '비밀번호 상태'                          ;
COMMENT ON COLUMN PLT_USER.USER_ATTR_A                            IS '사용자소속A'                            ;
COMMENT ON COLUMN PLT_USER.USER_ATTR_B                            IS '사용자소속B'                            ;
COMMENT ON COLUMN PLT_USER.USER_ATTR_C                            IS '사용자소속C'                            ;
COMMENT ON COLUMN PLT_USER.USER_ATTR_D                            IS '사용자소속D'                            ;
COMMENT ON COLUMN PLT_USER.CERTI_YN                               IS '사용자인증여부'                         ;
COMMENT ON COLUMN PLT_USER.CERTI_VALUE                            IS '사용자인증값'                           ;
COMMENT ON COLUMN PLT_USER.ENCRYPT_KEY                            IS '패스워드암호키'                         ;
COMMENT ON COLUMN PLT_USER.REGR_DEPT_CD                           IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_USER.REGR_ID                                IS '등록일시'                               ;
COMMENT ON COLUMN PLT_USER.REG_DTTM                               IS '사용여부'                               ;
COMMENT ON COLUMN PLT_USER.AMDR_DEPT_CD                           IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_USER.AMDR_ID                                IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_USER.UPD_DTTM                               IS '수정일시'                               ;
COMMENT ON COLUMN PLT_USER.PROC_ID                                IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_USER.IT_PROCESSING                          IS '전산처리일시'                           ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_COMN_CD    공통코드정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_COMN_CD CASCADE CONSTRAINTS;

CREATE TABLE PLT_COMN_CD (  
    GROUP_CD                                                              VARCHAR2(60)      NOT NULL                  ,
    CD                                                                    VARCHAR2(120)     NOT NULL                  ,
    CD_TYPE                                                               VARCHAR2(60)      NOT NULL                  ,
    CD_NM                                                                 VARCHAR2(300)                               ,
    CD_EXPLN                                                               VARCHAR2(600)                               ,
    CD_USE_FR_DT                                                          VARCHAR2(24)                                ,
    CD_USE_TO_DT                                                          VARCHAR2(24)                                ,
    CD_PRE_TYPE                                                           VARCHAR2(60)                                ,
    ETC_INFO01                                                            VARCHAR2(600)                               ,
    ETC_INFO02                                                            VARCHAR2(600)                               ,
    ETC_INFO03                                                            VARCHAR2(600)                               ,
    USE_YN                                                                CHAR(1)                                     ,
    SORT_ORD                                                              NUMBER(22)                                  ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_COMN_CD PRIMARY KEY (GROUP_CD,CD,CD_TYPE) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_COMN_CD                                     IS '공통코드정보'                           ;
COMMENT ON COLUMN PLT_COMN_CD.GROUP_CD                            IS '그룹코드'                               ;
COMMENT ON COLUMN PLT_COMN_CD.CD                                  IS '코드'                                   ;
COMMENT ON COLUMN PLT_COMN_CD.CD_TYPE                             IS '코드타입'                               ;
COMMENT ON COLUMN PLT_COMN_CD.CD_NM                               IS '코드명'                                 ;
COMMENT ON COLUMN PLT_COMN_CD.CD_EXPLN                             IS '코드설명'                               ;
COMMENT ON COLUMN PLT_COMN_CD.CD_USE_FR_DT                        IS '코드사용시작일자'                       ;
COMMENT ON COLUMN PLT_COMN_CD.CD_USE_TO_DT                        IS '코드사용종료일자'                       ;
COMMENT ON COLUMN PLT_COMN_CD.CD_PRE_TYPE                         IS '코드생성구분'                           ;
COMMENT ON COLUMN PLT_COMN_CD.ETC_INFO01                          IS '기타정보01'                             ;
COMMENT ON COLUMN PLT_COMN_CD.ETC_INFO02                          IS '기타정보02'                             ;
COMMENT ON COLUMN PLT_COMN_CD.ETC_INFO03                          IS '기타정보03'                             ;
COMMENT ON COLUMN PLT_COMN_CD.USE_YN                              IS '사용여부'                               ;
COMMENT ON COLUMN PLT_COMN_CD.SORT_ORD                            IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_COMN_CD.REGR_DEPT_CD                        IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_COMN_CD.REGR_ID                             IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_COMN_CD.REG_DTTM                            IS '등록일시'                               ;
COMMENT ON COLUMN PLT_COMN_CD.AMDR_DEPT_CD                        IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_COMN_CD.AMDR_ID                             IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_COMN_CD.UPD_DTTM                            IS '수정일시'                               ;
COMMENT ON COLUMN PLT_COMN_CD.PROC_ID                             IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_COMN_CD.IT_PROCESSING                       IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_MENU    메뉴관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_MENU CASCADE CONSTRAINTS;

CREATE TABLE PLT_MENU (  
    MENU_ID                                                               VARCHAR2(90)      NOT NULL                  ,
    SPST_MENU_ID                                                          VARCHAR2(90)                                ,
    LOWRNK_MENU_ID                                                        VARCHAR2(90)                                ,
    MENU_DIV_CD                                                           CHAR(2)                                     ,
    MENU_NM                                                               VARCHAR2(300)                               ,
    MENU_XPLN                                                             VARCHAR2(1500)                              ,
    PUP_FILE_NM                                                           VARCHAR2(600)                               ,
    PGM_PARM                                                              VARCHAR2(600)                               ,
    VIEW_TRGT                                                             VARCHAR2(300)                               ,
    PATH_NM                                                               VARCHAR2(300)                               ,
    PUP_WIDTH_SIZE                                                        NUMBER(22)                                  ,
    PUP_HGHT_SIZE                                                         NUMBER(22)                                  ,
    ICON_CLASS_NM                                                         VARCHAR2(120)                               ,
    SELECT_ATRT                                                           NUMBER(22)                                  ,
    PROC_ATRT                                                             NUMBER(22)                                  ,
    DEL_ATRT                                                              NUMBER(22)                                  ,
    OUTPUT_XPLN                                                           NUMBER(22)                                  ,
    EXCEL_ATRT                                                            NUMBER(22)                                  ,
    ADT_ATRT                                                              NUMBER(22)                                  ,
    USE_YN                                                                CHAR(1)                                     ,
    SORT_ORD                                                              NUMBER(22)                                  ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              DATE                                        ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         DATE                                        ,
    CONSTRAINT XPK_PLT_MENU PRIMARY KEY (MENU_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_MENU                                        IS '메뉴관리'                               ;
COMMENT ON COLUMN PLT_MENU.MENU_ID                                IS '메뉴ID'                                 ;
COMMENT ON COLUMN PLT_MENU.SPST_MENU_ID                           IS '상위메뉴ID'                             ;
COMMENT ON COLUMN PLT_MENU.LOWRNK_MENU_ID                         IS '하위메뉴ID'                             ;
COMMENT ON COLUMN PLT_MENU.MENU_DIV_CD                            IS '메뉴구분코드_레벨'                      ;
COMMENT ON COLUMN PLT_MENU.MENU_NM                                IS '메뉴명'                                 ;
COMMENT ON COLUMN PLT_MENU.MENU_XPLN                              IS '메뉴설명'                               ;
COMMENT ON COLUMN PLT_MENU.PUP_FILE_NM                            IS '팝업파일명'                             ;
COMMENT ON COLUMN PLT_MENU.PGM_PARM                               IS '프로그램파라미터'                       ;
COMMENT ON COLUMN PLT_MENU.VIEW_TRGT                              IS '뷰타겟'                                 ;
COMMENT ON COLUMN PLT_MENU.PATH_NM                                IS '경로명'                                 ;
COMMENT ON COLUMN PLT_MENU.PUP_WIDTH_SIZE                         IS '팝업가로사이즈'                         ;
COMMENT ON COLUMN PLT_MENU.PUP_HGHT_SIZE                          IS '팝업세로사이즈'                         ;
COMMENT ON COLUMN PLT_MENU.ICON_CLASS_NM                          IS '아이콘클래스명'                         ;
COMMENT ON COLUMN PLT_MENU.SELECT_ATRT                            IS '조회권한'                               ;
COMMENT ON COLUMN PLT_MENU.PROC_ATRT                              IS '처리권한'                               ;
COMMENT ON COLUMN PLT_MENU.DEL_ATRT                               IS '삭제권한'                               ;
COMMENT ON COLUMN PLT_MENU.OUTPUT_XPLN                            IS '출력권한'                               ;
COMMENT ON COLUMN PLT_MENU.EXCEL_ATRT                             IS '엑셀권한'                               ;
COMMENT ON COLUMN PLT_MENU.ADT_ATRT                               IS '추가권한'                               ;
COMMENT ON COLUMN PLT_MENU.USE_YN                                 IS '사용여부'                               ;
COMMENT ON COLUMN PLT_MENU.SORT_ORD                               IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_MENU.REGR_DEPT_CD                           IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_MENU.REGR_ID                                IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_MENU.REG_DTTM                               IS '등록일시'                               ;
COMMENT ON COLUMN PLT_MENU.AMDR_DEPT_CD                           IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_MENU.AMDR_ID                                IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_MENU.UPD_DTTM                               IS '수정일시'                               ;
COMMENT ON COLUMN PLT_MENU.PROC_ID                                IS '처리자'                                 ;
COMMENT ON COLUMN PLT_MENU.IT_PROCESSING                          IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_AUTH    권한그룹
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_AUTH CASCADE CONSTRAINTS;

CREATE TABLE PLT_AUTH (  
    ATRT_GROUP_ID                                                         VARCHAR2(90)      NOT NULL                  ,
    ATRT_GROUP_NM                                                         VARCHAR2(300)                               ,
    GROUP_XPLN                                                            VARCHAR2(600)                               ,
    MNGR_YN                                                               CHAR(1)                                     ,
    ETC_INFO01                                                            VARCHAR2(600)                               ,
    ETC_INFO02                                                            VARCHAR2(600)                               ,
    ETC_INFO03                                                            VARCHAR2(600)                               ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_AUTH PRIMARY KEY (ATRT_GROUP_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_AUTH                                        IS '권한그룹'                               ;
COMMENT ON COLUMN PLT_AUTH.ATRT_GROUP_ID                          IS '권한그룹ID'                             ;
COMMENT ON COLUMN PLT_AUTH.ATRT_GROUP_NM                          IS '권한그룹명'                             ;
COMMENT ON COLUMN PLT_AUTH.GROUP_XPLN                             IS '그룹설명'                               ;
COMMENT ON COLUMN PLT_AUTH.MNGR_YN                                IS '관리자여부'                             ;
COMMENT ON COLUMN PLT_AUTH.ETC_INFO01                             IS '기타정보01'                             ;
COMMENT ON COLUMN PLT_AUTH.ETC_INFO02                             IS '기타정보02'                             ;
COMMENT ON COLUMN PLT_AUTH.ETC_INFO03                             IS '기타정보03'                             ;
COMMENT ON COLUMN PLT_AUTH.REGR_DEPT_CD                           IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_AUTH.REGR_ID                                IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_AUTH.REG_DTTM                               IS '수정일시'                               ;
COMMENT ON COLUMN PLT_AUTH.AMDR_DEPT_CD                           IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_AUTH.AMDR_ID                                IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_AUTH.UPD_DTTM                               IS '수정일시'                               ;
COMMENT ON COLUMN PLT_AUTH.PROC_ID                                IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_AUTH.IT_PROCESSING                          IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_AUTH_PRG    권한그룹별프로그램
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_AUTH_PRG CASCADE CONSTRAINTS;

CREATE TABLE PLT_AUTH_PRG (  
    ATRT_GROUP_ID                                                         VARCHAR2(90)      NOT NULL                  ,
    MENU_ID                                                               VARCHAR2(90)      NOT NULL                  ,
    SELECT_ATRT                                                           NUMBER(22)                                  ,
    PROC_ATRT                                                             NUMBER(22)                                  ,
    DEL_ATRT                                                              NUMBER(22)                                  ,
    OUTPUT_XPLN                                                           NUMBER(22)                                  ,
    EXCEL_ATRT                                                            NUMBER(22)                                  ,
    ADT_ATRT                                                              NUMBER(22)                                  ,
    REGR_DEPT_CD                                                          VARCHAR2(90)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(90)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(90)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_AUTH_PRG PRIMARY KEY (ATRT_GROUP_ID,MENU_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_AUTH_PRG                                    IS '권한그룹별프로그램'                     ;
COMMENT ON COLUMN PLT_AUTH_PRG.ATRT_GROUP_ID                      IS '권한그룹ID'                             ;
COMMENT ON COLUMN PLT_AUTH_PRG.MENU_ID                            IS '메뉴ID'                                 ;
COMMENT ON COLUMN PLT_AUTH_PRG.SELECT_ATRT                        IS '조회권한'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.PROC_ATRT                          IS '처리권한'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.DEL_ATRT                           IS '삭제권한'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.OUTPUT_XPLN                        IS '출력권한'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.EXCEL_ATRT                         IS '엑셀권한'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.ADT_ATRT                           IS '추가권한'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.REGR_DEPT_CD                       IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_AUTH_PRG.REGR_ID                            IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.REG_DTTM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.AMDR_DEPT_CD                       IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_AUTH_PRG.AMDR_ID                            IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.UPD_DTTM                           IS '수정일시'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.PROC_ID                            IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_AUTH_PRG.IT_PROCESSING                      IS '전산처리일시'                           ;

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_USER_AUTH    사용자권한
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_USER_AUTH CASCADE CONSTRAINTS;

CREATE TABLE PLT_USER_AUTH (  
    ATRT_GROUP_ID                                                         VARCHAR2(90)      NOT NULL                  ,
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_USER_AUTH PRIMARY KEY (ATRT_GROUP_ID,USER_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_USER_AUTH                                   IS '사용자권한'                             ;
COMMENT ON COLUMN PLT_USER_AUTH.ATRT_GROUP_ID                     IS '권한그룹ID'                             ;
COMMENT ON COLUMN PLT_USER_AUTH.USER_ID                           IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_USER_AUTH.REGR_DEPT_CD                      IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_USER_AUTH.REGR_ID                           IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_USER_AUTH.REG_DTTM                          IS '등록일시'                               ;
COMMENT ON COLUMN PLT_USER_AUTH.AMDR_DEPT_CD                      IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_USER_AUTH.AMDR_ID                           IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_USER_AUTH.UPD_DTTM                          IS '수정일시'                               ;
COMMENT ON COLUMN PLT_USER_AUTH.PROC_ID                           IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_USER_AUTH.IT_PROCESSING                     IS '전산처리일시'                           ;

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_USER_LOG    사용자접속로그정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_USER_LOG CASCADE CONSTRAINTS;

CREATE TABLE PLT_USER_LOG (  
    LOG_ID                                                                VARCHAR2(90)      NOT NULL                  ,
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    BIZ_DIV                                                               VARCHAR2(60)                                ,
    CNNCT_IP                                                              VARCHAR2(60)                                ,
    ETC_INFO01                                                            VARCHAR2(600)                               ,
    ETC_INFO02                                                            VARCHAR2(600)                               ,
    ETC_INFO03                                                            VARCHAR2(600)                               ,
    ETC_INFO04                                                            VARCHAR2(600)                               ,
    ETC_INFO05                                                            VARCHAR2(600)                               ,
    WRTR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    WRTR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    WRT_DTTM                                                              DATE              NOT NULL                  ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_USER_LOG PRIMARY KEY (LOG_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_USER_LOG                                    IS '사용자접속로그정보'                     ;
COMMENT ON COLUMN PLT_USER_LOG.LOG_ID                             IS '로그ID'                                 ;
COMMENT ON COLUMN PLT_USER_LOG.USER_ID                            IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_USER_LOG.BIZ_DIV                            IS '업무구분'                               ;
COMMENT ON COLUMN PLT_USER_LOG.CNNCT_IP                           IS '접속아이피'                             ;
COMMENT ON COLUMN PLT_USER_LOG.ETC_INFO01                         IS '기타정보01'                             ;
COMMENT ON COLUMN PLT_USER_LOG.ETC_INFO02                         IS '기타정보02'                             ;
COMMENT ON COLUMN PLT_USER_LOG.ETC_INFO03                         IS '기타정보03'                             ;
COMMENT ON COLUMN PLT_USER_LOG.ETC_INFO04                         IS '기타정보04'                             ;
COMMENT ON COLUMN PLT_USER_LOG.ETC_INFO05                         IS '기타정보05'                             ;
COMMENT ON COLUMN PLT_USER_LOG.WRTR_DEPT_CD                       IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_USER_LOG.WRTR_ID                            IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_USER_LOG.WRT_DTTM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_USER_LOG.PROC_ID                            IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_USER_LOG.IT_PROCESSING                      IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_FILE    공통첨부파일정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_FILE CASCADE CONSTRAINTS;

CREATE TABLE PLT_FILE (  
    CUSTCO_ID                                                        VARCHAR2(10)         NOT NULL                     , 
    FILE_GROUP_KEY                                                      VARCHAR2(90)         NOT NULL                     , 
    FILE_KEY                                                            VARCHAR2(90)         NOT NULL                     ,
    FILE_ACS_TYPE_CD                                                    VARCHAR2(20)         DEFAULT 'PRIVATE' NOT NULL   ,
    TASK_TYPE_CD                                                           VARCHAR2(60)         NOT NULL                     ,
    TRGT_TYPE_CD                                                         VARCHAR2(20)         NOT NULL                     ,
    PATH_TYPE_CD                                                           VARCHAR2(60)         NOT NULL                     ,
    MIME_TYPE_CD                                                           VARCHAR2(200)        NOT NULL                     ,
    ACTL_FILE_NM                                                   VARCHAR2(600)        NOT NULL                     ,
    STRG_FILE_NM                                                       VARCHAR2(600)                                     , 
    FILE_PATH                                                           VARCHAR2(2000)                                    , 
    FILE_SZ                                                           NUMBER(11,0)         DEFAULT 0 NOT NULL           , 
    FILE_EXTN                                                           VARCHAR2(60)         NOT NULL                     , 
    DWNLD_CNT                                                           NUMBER(11,0)         DEFAULT 0 NOT NULL           , 
    FILE_URL                                                            VARCHAR2(1000)                                    , 
    ACS_KEY                                                          VARCHAR2(1000)                                    , 
    USE_YN                                                              CHAR(1)              DEFAULT 'Y' NOT NULL         , 
    REGR_DEPT_CD                                                        VARCHAR2(60)         NOT NULL                     ,
    REGR_ID                                                             VARCHAR2(60)         NOT NULL                     , 
    REG_DTTM                                                            TIMESTAMP (6)        DEFAULT SYSTIMESTAMP NOT NULL, 
    AMDR_DEPT_CD                                                        VARCHAR2(60)                                      , 
    AMDR_ID                                                             VARCHAR2(60)                                      , 
    UPD_DTTM                                                            TIMESTAMP (6)                                     , 
    PROC_ID                                                             VARCHAR2(60)         NOT NULL                     , 
    IT_PROCESSING                                                       TIMESTAMP (6)        DEFAULT SYSTIMESTAMP NOT NULL, 
    FILE_BLOB                                                           BLOB                                              ,
    CONSTRAINT XPK_PLT_FILE PRIMARY KEY (CUSTCO_ID, FILE_GROUP_KEY, FILE_KEY, FILE_ACS_TYPE_CD)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE PLT_FILE                                         IS '공통첨부파일정보';
COMMENT ON COLUMN PLT_FILE.CUSTCO_ID                           IS 'ASP_고객사_키';
COMMENT ON COLUMN PLT_FILE.FILE_GROUP_KEY                         IS '파일그룹키';
COMMENT ON COLUMN PLT_FILE.FILE_KEY                               IS '파일키';
COMMENT ON COLUMN PLT_FILE.FILE_ACS_TYPE_CD                       IS '파일엑세스유형(PUBLIC:공개,PRIVATE:비공개)';
COMMENT ON COLUMN PLT_FILE.TASK_TYPE_CD                              IS '업무구분';
COMMENT ON COLUMN PLT_FILE.TRGT_TYPE_CD                            IS '저장소대상구분';
COMMENT ON COLUMN PLT_FILE.PATH_TYPE_CD                              IS '경로구분';
COMMENT ON COLUMN PLT_FILE.MIME_TYPE_CD                              IS 'MIME유형';
COMMENT ON COLUMN PLT_FILE.ACTL_FILE_NM                      IS '진짜파일명';
COMMENT ON COLUMN PLT_FILE.STRG_FILE_NM                          IS '저장된파일명';
COMMENT ON COLUMN PLT_FILE.FILE_PATH                              IS '파일경로';
COMMENT ON COLUMN PLT_FILE.FILE_SZ                              IS '파일크기';
COMMENT ON COLUMN PLT_FILE.FILE_EXTN                              IS '파일확장자';
COMMENT ON COLUMN PLT_FILE.DWNLD_CNT                              IS '다운로드수';
COMMENT ON COLUMN PLT_FILE.FILE_URL                               IS '파일URL';
COMMENT ON COLUMN PLT_FILE.ACS_KEY                             IS '접근키';
COMMENT ON COLUMN PLT_FILE.USE_YN                                 IS '사용여부';
COMMENT ON COLUMN PLT_FILE.REGR_DEPT_CD                           IS '등록자부서코드';
COMMENT ON COLUMN PLT_FILE.REGR_ID                                IS '등록자ID';
COMMENT ON COLUMN PLT_FILE.REG_DTTM                               IS '등록일시';
COMMENT ON COLUMN PLT_FILE.AMDR_DEPT_CD                           IS '수정자부서코드';
COMMENT ON COLUMN PLT_FILE.AMDR_ID                                IS '수성자ID';
COMMENT ON COLUMN PLT_FILE.UPD_DTTM                               IS '수성일시';
COMMENT ON COLUMN PLT_FILE.PROC_ID                                IS '처리자ID';
COMMENT ON COLUMN PLT_FILE.IT_PROCESSING                          IS '전산처리일시';
COMMENT ON COLUMN PLT_FILE.FILE_BLOB                              IS '파일';





--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PWD_LOG    비밀번호변경로그정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PWD_LOG CASCADE CONSTRAINTS;

CREATE TABLE PLT_PWD_LOG (  
    LOG_ID                                                                VARCHAR2(90)      NOT NULL                  ,
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    BIZ_DIV                                                               VARCHAR2(60)                                ,
    CNNCT_IP                                                              VARCHAR2(60)                                ,
    ETC_INFO01                                                            VARCHAR2(600)                               ,
    ETC_INFO02                                                            VARCHAR2(600)                               ,
    ETC_INFO03                                                            VARCHAR2(600)                               ,
    ETC_INFO04                                                            VARCHAR2(600)                               ,
    ETC_INFO05                                                            VARCHAR2(600)                               ,
    WRTR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    WRTR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    WRT_DTTM                                                              DATE              NOT NULL                  ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_PWD_LOG PRIMARY KEY (LOG_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PWD_LOG                                     IS '비밀번호변경로그정보'                   ;
COMMENT ON COLUMN PLT_PWD_LOG.LOG_ID                              IS '로그ID'                                 ;
COMMENT ON COLUMN PLT_PWD_LOG.USER_ID                             IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_PWD_LOG.BIZ_DIV                             IS '업무구분'                               ;
COMMENT ON COLUMN PLT_PWD_LOG.CNNCT_IP                            IS '접속아이피'                             ;
COMMENT ON COLUMN PLT_PWD_LOG.ETC_INFO01                          IS '기타정보01'                             ;
COMMENT ON COLUMN PLT_PWD_LOG.ETC_INFO02                          IS '기타정보02'                             ;
COMMENT ON COLUMN PLT_PWD_LOG.ETC_INFO03                          IS '기타정보03'                             ;
COMMENT ON COLUMN PLT_PWD_LOG.ETC_INFO04                          IS '기타정보04'                             ;
COMMENT ON COLUMN PLT_PWD_LOG.ETC_INFO05                          IS '기타정보05'                             ;
COMMENT ON COLUMN PLT_PWD_LOG.WRTR_DEPT_CD                        IS '작성자부서코드'                         ;
COMMENT ON COLUMN PLT_PWD_LOG.WRTR_ID                             IS '작성자ID'                               ;
COMMENT ON COLUMN PLT_PWD_LOG.WRT_DTTM                            IS '작성일시'                               ;
COMMENT ON COLUMN PLT_PWD_LOG.PROC_ID                             IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_PWD_LOG.IT_PROCESSING                       IS '전산처리일시'                           ;



-- 2021.06.04 BRD_TY 게시판유형 컬럼 추가
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_BLBD_MNG    통합게시판마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_BLBD_MNG CASCADE CONSTRAINTS;

CREATE TABLE PLT_BLBD_MNG (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    BRD_TY                                                                VARCHAR2(20)                                ,
    BRD_ID                                                                VARCHAR2(60)      NOT NULL                  ,
    BRD_NAME                                                              VARCHAR2(150)     NOT NULL                  ,
    BRD_RMK                                                               VARCHAR2(300)                               ,
    NOTI_YN                                                               CHAR(1)                                     ,
    REPLE_YN                                                              CHAR(1)                                     ,
    RMK_YN                                                                CHAR(1)                                     ,
    FILE_YN                                                               CHAR(1)                                     ,
    NEW_DAY                                                               NUMBER(22)                                  ,
    USE_YN                                                                CHAR(1)                                     ,
    ETC01                                                                 NUMBER(22)                                  ,
    ETC02                                                                 NUMBER(22)                                  ,
    ETC03                                                                 VARCHAR2(300)                               ,
    ETC04                                                                 VARCHAR2(300)                               ,
    ETC05                                                                 VARCHAR2(300)                               ,
    RT_NOTI_YN                                                            CHAR(1)                                     ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE                                        ,
    CONSTRAINT XPK_PLT_BLBD_MNG PRIMARY KEY (CUSTCO_ID,BRD_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_BLBD_MNG                                    IS '통합게시판마스터'                       ;
COMMENT ON COLUMN PLT_BLBD_MNG.CUSTCO_ID                       IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_BLBD_MNG.BRD_TY                             IS '게시판유형'                           ;
COMMENT ON COLUMN PLT_BLBD_MNG.BRD_ID                             IS '게시판ID'                               ;
COMMENT ON COLUMN PLT_BLBD_MNG.BRD_NAME                           IS '게시판명'                               ;
COMMENT ON COLUMN PLT_BLBD_MNG.BRD_RMK                            IS '통합게시판상세설명'                     ;
COMMENT ON COLUMN PLT_BLBD_MNG.NOTI_YN                            IS '공지사항여부'                           ;
COMMENT ON COLUMN PLT_BLBD_MNG.REPLE_YN                           IS '답변허용여부'                           ;
COMMENT ON COLUMN PLT_BLBD_MNG.RMK_YN                             IS '덧글허용여부'                           ;
COMMENT ON COLUMN PLT_BLBD_MNG.FILE_YN                            IS '파일첨부여부'                           ;
COMMENT ON COLUMN PLT_BLBD_MNG.NEW_DAY                            IS 'NEW표시일수'                            ;
COMMENT ON COLUMN PLT_BLBD_MNG.USE_YN                             IS '사용여부'                               ;
COMMENT ON COLUMN PLT_BLBD_MNG.ETC01                              IS '기타정보01(숫자)'                       ;
COMMENT ON COLUMN PLT_BLBD_MNG.ETC02                              IS '기타정보02(숫자)'                       ;
COMMENT ON COLUMN PLT_BLBD_MNG.ETC03                              IS '기타정보03'                             ;
COMMENT ON COLUMN PLT_BLBD_MNG.ETC04                              IS '기타정보04'                             ;
COMMENT ON COLUMN PLT_BLBD_MNG.ETC05                              IS '기타정보05'                             ;
COMMENT ON COLUMN PLT_BLBD_MNG.RT_NOTI_YN                         IS '실시간공지여부'                         ;
COMMENT ON COLUMN PLT_BLBD_MNG.PROC_ID                            IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_BLBD_MNG.IT_PROCESSING                      IS '전산처리일시'                           ;




--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_BLBD_MST    게시물마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_BLBD_MST CASCADE CONSTRAINTS;

CREATE TABLE PLT_BLBD_MST (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    BRD_ID                                                                VARCHAR2(60)      NOT NULL                  ,
    BRD_NO                                                                VARCHAR2(90)      NOT NULL                  ,
    UPPER_BRD_NO                                                          VARCHAR2(90)                                ,
    LVL_NO                                                                NUMBER(5,0)       DEFAULT '0'               ,
    BRD_TIT                                                               VARCHAR2(300)                               ,
    BRD_RMK_NO                                                            VARCHAR2(75)                                ,
    BRD_RMK                                                               CLOB                                        ,
    FILE_GROUP_KEY                                                        VARCHAR2(90)                                ,
    FST_USER_ID                                                           VARCHAR2(60)                                ,
    FST_BRD_DT                                                            DATE                                        ,
    LAST_USER_ID                                                          VARCHAR2(60)                                ,
    LAST_BRD_DT                                                           DATE                                        ,
    ACCESS_IP                                                             VARCHAR2(60)                                ,
    SELECT_NO                                                             NUMBER(19)        DEFAULT '0'               ,
    ORD_SEQ                                                               NUMBER(10)        DEFAULT '0'               ,
    USE_YN                                                                CHAR(1)                                     ,
    KAGE_FILE_URL                                                         VARCHAR2(1000)                              ,
    KAGE_ACS_KEY                                                       VARCHAR2(1000)                              ,
    BULTN_FR_DT                                                           DATE                                        ,
    BULTN_TO_DT                                                           DATE                                        ,
    BULTN_SPNS_DT                                                         DATE                                        ,
    PUP_YN                                                                CHAR(1)           DEFAULT 'N'               ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         DATE              DEFAULT SYSTIMESTAMP      ,
    CONSTRAINT XPK_PLT_BLBD_MST PRIMARY KEY (CUSTCO_ID,BRD_ID,BRD_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_BLBD_MST                                    IS '게시물마스터'                           ;
COMMENT ON COLUMN PLT_BLBD_MST.CUSTCO_ID                       IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_BLBD_MST.BRD_ID                             IS '게시판ID'                               ;
COMMENT ON COLUMN PLT_BLBD_MST.BRD_NO                             IS '게시판번호'                             ;
COMMENT ON COLUMN PLT_BLBD_MST.UPPER_BRD_NO                       IS '상위게시물번호'                         ;
COMMENT ON COLUMN PLT_BLBD_MST.LVL_NO                             IS '게시물레벨'                             ;
COMMENT ON COLUMN PLT_BLBD_MST.BRD_TIT                            IS '게시물제목'                             ;
COMMENT ON COLUMN PLT_BLBD_MST.BRD_RMK_NO                         IS '게시물내용번호'                         ;
COMMENT ON COLUMN PLT_BLBD_MST.BRD_RMK                            IS '게시물내용'                             ;
COMMENT ON COLUMN PLT_BLBD_MST.FILE_GROUP_KEY                     IS '파일그룹키'                             ;
COMMENT ON COLUMN PLT_BLBD_MST.FST_USER_ID                        IS '최초등록자ID'                           ;
COMMENT ON COLUMN PLT_BLBD_MST.FST_BRD_DT                         IS '최초등록일'                             ;
COMMENT ON COLUMN PLT_BLBD_MST.LAST_USER_ID                       IS '마지막등록자ID'                         ;
COMMENT ON COLUMN PLT_BLBD_MST.LAST_BRD_DT                        IS '마지막등록일'                           ;
COMMENT ON COLUMN PLT_BLBD_MST.ACCESS_IP                          IS '등록자IP'                               ;
COMMENT ON COLUMN PLT_BLBD_MST.SELECT_NO                          IS '조회수'                                 ;
COMMENT ON COLUMN PLT_BLBD_MST.ORD_SEQ                            IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_BLBD_MST.USE_YN                             IS '사용여부'                               ;
COMMENT ON COLUMN PLT_BLBD_MST.KAGE_FILE_URL                      IS '케이지파일경로'                         ;
COMMENT ON COLUMN PLT_BLBD_MST.KAGE_ACS_KEY                    IS '케이지접근키'                           ;
COMMENT ON COLUMN PLT_BLBD_MST.BULTN_FR_DT                        IS '게시시작일자'                           ;
COMMENT ON COLUMN PLT_BLBD_MST.BULTN_TO_DT                        IS '게시종료일자'                           ;
COMMENT ON COLUMN PLT_BLBD_MST.BULTN_SPNS_DT                      IS '게시중지일자'                           ;
COMMENT ON COLUMN PLT_BLBD_MST.PUP_YN                             IS '팝업여부'                               ;
COMMENT ON COLUMN PLT_BLBD_MST.PROC_ID                            IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_BLBD_MST.IT_PROCESSING                      IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_BLBD_CMT    게시물덧글
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_BLBD_CMT CASCADE CONSTRAINTS;

CREATE TABLE PLT_BLBD_CMT (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    BRD_ID                                                                VARCHAR2(60)      NOT NULL                  ,
    BRD_NO                                                                VARCHAR2(60)      NOT NULL                  ,
    RMK_NO                                                                NUMBER(22)        NOT NULL                  ,
    RMK                                                                   VARCHAR2(3000)    NOT NULL                  ,
    REG_USER_ID                                                           VARCHAR2(60)                                ,
    REG_DT                                                                DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              DEFAULT SYSTIMESTAMP      ,
    CONSTRAINT XPK_PLT_BLBD_CMT PRIMARY KEY (CUSTCO_ID,BRD_ID,BRD_NO,RMK_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_BLBD_CMT                                    IS '게시물덧글'                             ;
COMMENT ON COLUMN PLT_BLBD_CMT.CUSTCO_ID                       IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_BLBD_CMT.BRD_ID                             IS '게시판ID'                               ;
COMMENT ON COLUMN PLT_BLBD_CMT.BRD_NO                             IS '게시판번호'                             ;
COMMENT ON COLUMN PLT_BLBD_CMT.RMK_NO                             IS '댓글번호'                               ;
COMMENT ON COLUMN PLT_BLBD_CMT.RMK                                IS '댓글'                                   ;
COMMENT ON COLUMN PLT_BLBD_CMT.REG_USER_ID                        IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_BLBD_CMT.REG_DT                             IS '등록일'                                 ;
COMMENT ON COLUMN PLT_BLBD_CMT.PROC_ID                            IS 'PROC_ID'                                ;
COMMENT ON COLUMN PLT_BLBD_CMT.IT_PROCESSING                      IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CAL    칼렌더
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CAL CASCADE CONSTRAINTS;

CREATE TABLE PLT_CAL (  
    YYYYMMDD                                                              VARCHAR2(8)       NOT NULL                  ,
    CONSTRAINT XPK_PLT_CAL PRIMARY KEY (YYYYMMDD) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CAL                                         IS '칼렌더'                                 ;
COMMENT ON COLUMN PLT_CAL.YYYYMMDD                                IS '날짜'                                   ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_EXCL    대용량엑셀파일정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_EXCL CASCADE CONSTRAINTS;

CREATE TABLE PLT_EXCL (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    EXCEL_KEY                                                             VARCHAR2(75)      NOT NULL                  ,
    SHEET_KEY                                                             VARCHAR2(75)      NOT NULL                  ,
    FILE_TYPE                                                             VARCHAR2(30)                                ,
    MENU_ID                                                               VARCHAR2(75)                                ,
    MENU_INFO                                                             VARCHAR2(600)                               ,
    SHEET_INFO                                                            VARCHAR2(240)                               ,
    USER_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    DEPT_CODE                                                             VARCHAR2(60)                                ,
    REQUEST_DT                                                            DATE                                        ,
    COMPLETE_DT                                                           DATE                                        ,
    FILE_KEY                                                              VARCHAR2(75)      NOT NULL                  ,
    DATA_CNT                                                              NUMBER(22)                                  ,
    EXECUT_QUERY                                                          CLOB                                        ,
    ETC_INFO001                                                           CLOB                                        ,
    ETC_INFO002                                                           CLOB                                        ,
    ETC_INFO003                                                           CLOB                                        ,
    PROCESS_CODE                                                          VARCHAR2(60)                                ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         DATE              NOT NULL                  ,
    CONSTRAINT XPK_PLT_EXCL PRIMARY KEY (CUSTCO_ID,EXCEL_KEY,SHEET_KEY) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_EXCL                                        IS '대용량엑셀파일정보'                     ;
COMMENT ON COLUMN PLT_EXCL.CUSTCO_ID                           IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_EXCL.EXCEL_KEY                              IS '엑셀키'                                 ;
COMMENT ON COLUMN PLT_EXCL.SHEET_KEY                              IS '시트키'                                 ;
COMMENT ON COLUMN PLT_EXCL.FILE_TYPE                              IS '파일타입[CSV, XLSX ]'                   ;
COMMENT ON COLUMN PLT_EXCL.MENU_ID                                IS '메뉴ID [ 화면ID ]'                      ;
COMMENT ON COLUMN PLT_EXCL.MENU_INFO                              IS '메뉴명'                                 ;
COMMENT ON COLUMN PLT_EXCL.SHEET_INFO                             IS '시트명'                                 ;
COMMENT ON COLUMN PLT_EXCL.USER_ID                                IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_EXCL.DEPT_CODE                              IS '부서코드'                               ;
COMMENT ON COLUMN PLT_EXCL.REQUEST_DT                             IS '요청일시'                               ;
COMMENT ON COLUMN PLT_EXCL.COMPLETE_DT                            IS '완료일시'                               ;
COMMENT ON COLUMN PLT_EXCL.FILE_KEY                               IS '파일키'                                 ;
COMMENT ON COLUMN PLT_EXCL.DATA_CNT                               IS '개수'                                   ;
COMMENT ON COLUMN PLT_EXCL.EXECUT_QUERY                           IS '쿼리문'                                 ;
COMMENT ON COLUMN PLT_EXCL.ETC_INFO001                            IS '모든파라메터정보(EXCEL 해더정보)'       ;
COMMENT ON COLUMN PLT_EXCL.ETC_INFO002                            IS '비고2'                                  ;
COMMENT ON COLUMN PLT_EXCL.ETC_INFO003                            IS '비고3'                                  ;
COMMENT ON COLUMN PLT_EXCL.PROCESS_CODE                           IS '상태코드'                               ;
COMMENT ON COLUMN PLT_EXCL.PROC_ID                                IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_EXCL.IT_PROCESSING                          IS '전산처리일시'                           ;





--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CUS    고객마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
-- 2021.05.18 이동욱 통합 고객마스터 테이블 생성
-- 2021.05.24 이동욱 사업자등록번호 컬럼 추가
DROP   TABLE PLT_CUS CASCADE CONSTRAINTS;

CREATE TABLE PLT_CUS (  
    CUSTOMER_ID                                                           VARCHAR2(80)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    SNDR_KEY                                                        VARCHAR2(60)      NOT NULL                  ,
    TALK_USER_KEY                                                         VARCHAR2(80)      NOT NULL                  ,
    CUSTOMER_NM                                                           VARCHAR2(300)                               ,
    CUSTOMER_PHONE_NO                                                     VARCHAR2(60)                                ,
    CUSTOMER_USER_MEMO                                                    VARCHAR2(1500)                              ,
    GENDER_CD                                                             VARCHAR2(60)                                ,
    CUSTOMER_BIRTH                                                        VARCHAR2(24)                                ,
    CUSTOMER_STAT_CD                                                      VARCHAR2(60)                                ,
    UPDATEYN                                                              VARCHAR2(3)       DEFAULT 'Y'               ,
    EML                                                                 VARCHAR2(300)                               ,
    CUSTOMER_MEMO                                                         VARCHAR2(4000)                              ,
    ATENT_CUSTOMER                                                        VARCHAR2(3)       DEFAULT 'N'               ,
    MEMO_REGR_DATE                                                        DATE                                        ,
    MEMO_REGR_ID                                                          VARCHAR2(60)                                ,
    MEMO_AMDR_DATE                                                        DATE                                        ,
    MEMO_AMDR_ID                                                          VARCHAR2(60)                                ,
    ATENT_REGR_DATE                                                       DATE                                        ,
    ATENT_REGR_ID                                                         VARCHAR2(60)                                ,
    ATENT_AMDR_DATE                                                       DATE                                        ,
    ATENT_AMDR_ID                                                         VARCHAR2(60)                                ,
    HEAR_IMPAIRED_PERSON                                                  VARCHAR2(3)       DEFAULT 'N'               ,
    AGREE_PERSON_INFO                                                     VARCHAR2(3)       DEFAULT 'N'               ,
    CUSTOMER_SEQ                                                          VARCHAR2(12)                                ,
    MEMBER_YN                                                             CHAR(1)           DEFAULT 'N'               ,
    CUSTOMER_TYPE                                                         VARCHAR2(6)                                 ,
    CUSTOMER_GENDER                                                       VARCHAR2(6)                                 ,
    CUSTOMER_CAREER_STATUS                                                VARCHAR2(9)                                 ,
    CUSTOMER_EDU_CHK_YN                                                   VARCHAR2(3)                                 ,
    CUSTOMER_EDU_LEV                                                      VARCHAR2(6)                                 ,
    CUSTOMER_EDU_MAJOR                                                    VARCHAR2(9)                                 ,
    CUSTOMER_JOIN_POLICY_YN                                               VARCHAR2(3)                                 ,
    CUSTOMER_JOIN_POLICY                                                  VARCHAR2(300)                               ,
    CUSTOMER_REGION                                                       VARCHAR2(6)                                 ,
    REFERENCE_CUSTOMER_KEY                                                VARCHAR2(90)                                ,
    TID                                                                   VARCHAR2(100)                               ,
    ACDT_PRSN_REL                                                         VARCHAR2(6)                                 ,
    ATENT_CD                                                              VARCHAR2(60)                                ,
    ATENT_PRE_PLAN                                                        VARCHAR2(4000)                              ,
    REGR_DEPT_CD                                                          VARCHAR2(60)                                ,
    REGR_ID                                                               VARCHAR2(60)                                ,
    REG_DTTM                                                              DATE                                        ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)                                ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)                                ,
    IT_PROCESSING                                                         DATE             DEFAULT SYSTIMESTAMP       ,
    
    /* 새로 추가된 컬럼들 */
    DEL_YN                                                                VARCHAR2(1)      DEFAULT 'N'                ,
    CARE_VALD_BEGIN_DATE                                                  DATE                                        ,
    CARE_VALD_EOT_DATE                                                    DATE                                        ,
    OWNHOM_ADR                                                            VARCHAR2(200)                               ,
    OWNHOM_DETAIL_ADR                                                     VARCHAR2(200)                               ,
    OLD_OWNHOM_ADR                                                        VARCHAR2(200)                               ,
    OLD_OWNHOM_DETAIL_ADR                                                 VARCHAR2(200)                               ,
    WRC_ADR                                                               VARCHAR2(200)                               ,
    WRC_DETAIL_ADR                                                        VARCHAR2(200)                               ,
    OLD_WRC_ADR                                                           VARCHAR2(200)                               ,
    OLD_WRC_DETAIL_ADR                                                    VARCHAR2(200)                               ,
    OWNHOM_TEL_NO                                                         VARCHAR2(60)                                ,
    WRC_TEL_NO                                                            VARCHAR2(60)                                ,
    RES_NO                                                                VARCHAR2(64)                                ,
    BIZRNO                                                                VARCHAR2(20)                                ,
    SCRITS_NO                                                             VARCHAR2(30)                                ,
    CUSTOMER_STATE                                                        VARCHAR2(20)                                ,
    CUSTOMER_SECTION                                                      VARCHAR2(10)                                ,
    CUSTOMER_TYPE_CD                                                      VARCHAR2(20)                                ,
    
    CONSTRAINT XPK_PLT_CUS PRIMARY KEY (CUSTOMER_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_CUS                                     IS '고객마스터'                        ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_ID                         IS '고객ID'                                 ;
COMMENT ON COLUMN PLT_CUS.CUSTCO_ID                        IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_CUS.SNDR_KEY                      IS 'ASP_센더_키'                            ;
COMMENT ON COLUMN PLT_CUS.TALK_USER_KEY                       IS 'TALK_사용자_키'                         ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_NM                         IS '고객명'                                 ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_PHONE_NO                   IS '고객전화번호'                           ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_USER_MEMO                  IS '고객메모'                               ;
COMMENT ON COLUMN PLT_CUS.GENDER_CD                           IS '성별코드'                               ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_BIRTH                      IS '생년월일'                               ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_STAT_CD                    IS '회원상태코드'                           ;
COMMENT ON COLUMN PLT_CUS.UPDATEYN                            IS '개인정보입력요청갱신여부'               ;
COMMENT ON COLUMN PLT_CUS.EML                               IS '전자우편주소'                           ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_MEMO                       IS '고객메모'                               ;
COMMENT ON COLUMN PLT_CUS.ATENT_CUSTOMER                      IS '주의고객여부'                           ;
COMMENT ON COLUMN PLT_CUS.MEMO_REGR_DATE                      IS '고객메모등록일자'                       ;
COMMENT ON COLUMN PLT_CUS.MEMO_REGR_ID                        IS '고객메모등록자ID'                       ;
COMMENT ON COLUMN PLT_CUS.MEMO_AMDR_DATE                      IS '고객메모수정일자'                       ;
COMMENT ON COLUMN PLT_CUS.MEMO_AMDR_ID                        IS '고객메모수정자ID'                       ;
COMMENT ON COLUMN PLT_CUS.ATENT_REGR_DATE                     IS '주의고객등록일자'                       ;
COMMENT ON COLUMN PLT_CUS.ATENT_REGR_ID                       IS '주의고객등록자ID'                       ;
COMMENT ON COLUMN PLT_CUS.ATENT_AMDR_DATE                     IS '주의고객수정일자'                       ;
COMMENT ON COLUMN PLT_CUS.ATENT_AMDR_ID                       IS '주의고객수정자ID'                       ;
COMMENT ON COLUMN PLT_CUS.HEAR_IMPAIRED_PERSON                IS '청각장애인여부'                         ;
COMMENT ON COLUMN PLT_CUS.AGREE_PERSON_INFO                   IS '개인정보활용동의여부'                   ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_SEQ                        IS '고객사_고객번호'                        ;
COMMENT ON COLUMN PLT_CUS.MEMBER_YN                           IS '회원여부'                               ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_TYPE                       IS '고객유형'                               ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_GENDER                     IS '고객성별'                               ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_CAREER_STATUS              IS '고객경력상태'                           ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_EDU_CHK_YN                 IS '고객교육체크여부'                       ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_EDU_LEV                    IS '고객교육수준'                           ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_EDU_MAJOR                  IS '고객교육전공'                           ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_JOIN_POLICY_YN             IS '고객동의여부'                           ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_JOIN_POLICY                IS '고객동의'                               ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_REGION                     IS '고객거주지역'                           ;
COMMENT ON COLUMN PLT_CUS.REFERENCE_CUSTOMER_KEY              IS '고객참조KEY'                            ;
COMMENT ON COLUMN PLT_CUS.TID                                 IS 'TID'                                    ;
COMMENT ON COLUMN PLT_CUS.ACDT_PRSN_REL                       IS '사고자 관계'                            ;
COMMENT ON COLUMN PLT_CUS.ATENT_CD                            IS '주의고객코드'                           ;
COMMENT ON COLUMN PLT_CUS.ATENT_PRE_PLAN                      IS '주의고객대처방안'                       ;
COMMENT ON COLUMN PLT_CUS.REGR_DEPT_CD                        IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_CUS.REGR_ID                             IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_CUS.REG_DTTM                            IS '등록일시'                               ;
COMMENT ON COLUMN PLT_CUS.AMDR_DEPT_CD                        IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_CUS.AMDR_ID                             IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_CUS.UPD_DTTM                            IS '수정일시'                               ;
COMMENT ON COLUMN PLT_CUS.PROC_ID                             IS '처리자'                                 ;
COMMENT ON COLUMN PLT_CUS.IT_PROCESSING                       IS '전산처리일시'                           ;


COMMENT ON COLUMN PLT_CUS.DEL_YN                              IS '삭제여부'                           ;
COMMENT ON COLUMN PLT_CUS.CARE_VALD_BEGIN_DATE                IS '주의유효시작일자'                           ;
COMMENT ON COLUMN PLT_CUS.CARE_VALD_EOT_DATE                  IS '주의유효종료일자'                           ;
COMMENT ON COLUMN PLT_CUS.OWNHOM_ADR                          IS '자택주소'                           ;
COMMENT ON COLUMN PLT_CUS.OWNHOM_DETAIL_ADR                   IS '자택상세주소'                           ;
COMMENT ON COLUMN PLT_CUS.OLD_OWNHOM_ADR                      IS '자택구주소'                           ;
COMMENT ON COLUMN PLT_CUS.OLD_OWNHOM_DETAIL_ADR               IS '자택상세구주소'                           ;
COMMENT ON COLUMN PLT_CUS.WRC_ADR                             IS '직장주소'                           ;
COMMENT ON COLUMN PLT_CUS.WRC_DETAIL_ADR                      IS '직장상세주소'                           ;
COMMENT ON COLUMN PLT_CUS.OLD_WRC_ADR                         IS '직장구주소'                           ;
COMMENT ON COLUMN PLT_CUS.OLD_WRC_DETAIL_ADR                  IS '직장상세구주소'                           ;
COMMENT ON COLUMN PLT_CUS.OWNHOM_TEL_NO                       IS '자택전화'                           ;
COMMENT ON COLUMN PLT_CUS.WRC_TEL_NO                          IS '직장전화'                           ;
COMMENT ON COLUMN PLT_CUS.RES_NO                              IS '주민등록번호'                           ;
COMMENT ON COLUMN PLT_CUS.BIZRNO                              IS '사업자등록번호'                           ;
COMMENT ON COLUMN PLT_CUS.SCRITS_NO                           IS '증권번호'                           ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_STATE                      IS '고객상태'                           ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_SECTION                    IS '고객구분'                           ;
COMMENT ON COLUMN PLT_CUS.CUSTOMER_TYPE_CD                    IS '고객유형코드'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_USER_BLNG    사용자소속항목
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_USER_BLNG CASCADE CONSTRAINTS;

CREATE TABLE PLT_USER_BLNG (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                  ,
    ATTR_CD                                                               VARCHAR2(20)      NOT NULL                  ,
    ATTR_DIV_CD                                                           VARCHAR2(20)      NOT NULL                  ,
    ATTR_DIV_NM                                                           VARCHAR2(100)                               ,
    USE_YN                                                                VARCHAR2(1)       DEFAULT 'Y'               ,
    SORT_ORD                                                              NUMBER(22)        DEFAULT '1'               ,
    REGR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_ID                                                               VARCHAR2(20)                                ,
    UPD_DTTM                                                              DATE                                        ,
    CONSTRAINT XPK_PLT_USER_BLNG PRIMARY KEY (CUSTCO_ID,ATTR_CD,ATTR_DIV_CD) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_USER_BLNG                                   IS '사용자소속항목'                         ;
COMMENT ON COLUMN PLT_USER_BLNG.CUSTCO_ID                      IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_USER_BLNG.ATTR_CD                           IS '사용자소속코드'                         ;
COMMENT ON COLUMN PLT_USER_BLNG.ATTR_DIV_CD                       IS '사용자소속구분코드'                     ;
COMMENT ON COLUMN PLT_USER_BLNG.ATTR_DIV_NM                       IS '사용자소속구분명'                       ;
COMMENT ON COLUMN PLT_USER_BLNG.USE_YN                            IS '사용여부'                               ;
COMMENT ON COLUMN PLT_USER_BLNG.SORT_ORD                          IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_USER_BLNG.REGR_ID                           IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_USER_BLNG.REG_DTTM                          IS '등록일시'                               ;
COMMENT ON COLUMN PLT_USER_BLNG.AMDR_ID                           IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_USER_BLNG.UPD_DTTM                          IS '수정일시'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_USER_ENV    사용자환경정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_USER_ENV CASCADE CONSTRAINTS;

CREATE TABLE PLT_USER_ENV (  
    USER_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    SCR_RESOLT_CD                                                         VARCHAR2(20)                                ,
    PAG_LIST_CNT_CD                                                       VARCHAR2(20)                                ,
    SHORT_CUT_1_BGE_YN                                                    CHAR(1)                                     ,
    SHORT_CUT_2_BGE_YN                                                    CHAR(1)                                     ,
    SHORT_CUT_3_BGE_YN                                                    CHAR(1)                                     ,
    SHORT_CUT_4_BGE_YN                                                    CHAR(1)                                     ,
    SHORT_CUT_5_BGE_YN                                                    CHAR(1)                                     ,
    SHORT_CUT_6_BGE_YN                                                    CHAR(1)                                     ,
    SHORT_CUT_7_BGE_YN                                                    CHAR(1)                                     ,
    SHORT_CUT_8_BGE_YN                                                    CHAR(1)                                     ,
    WRTR_DEPT_CD                                                          VARCHAR2(20)      NOT NULL                  ,
    WRTR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    WRT_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    PROC_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_USER_ENV PRIMARY KEY (USER_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_USER_ENV                                    IS '사용자환경정보'                         ;
COMMENT ON COLUMN PLT_USER_ENV.USER_ID                            IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_USER_ENV.SCR_RESOLT_CD                      IS '화면해상도코드'                         ;
COMMENT ON COLUMN PLT_USER_ENV.PAG_LIST_CNT_CD                    IS '페이지당목록수코드'                     ;
COMMENT ON COLUMN PLT_USER_ENV.SHORT_CUT_1_BGE_YN                 IS '퀵메뉴1배지여부'                        ;
COMMENT ON COLUMN PLT_USER_ENV.SHORT_CUT_2_BGE_YN                 IS '퀵메뉴2배지여부'                        ;
COMMENT ON COLUMN PLT_USER_ENV.SHORT_CUT_3_BGE_YN                 IS '퀵메뉴3배지여부'                        ;
COMMENT ON COLUMN PLT_USER_ENV.SHORT_CUT_4_BGE_YN                 IS '퀵메뉴4배지여부'                        ;
COMMENT ON COLUMN PLT_USER_ENV.SHORT_CUT_5_BGE_YN                 IS '퀵메뉴5배지여부'                        ;
COMMENT ON COLUMN PLT_USER_ENV.SHORT_CUT_6_BGE_YN                 IS '퀵메뉴6배지여부'                        ;
COMMENT ON COLUMN PLT_USER_ENV.SHORT_CUT_7_BGE_YN                 IS '퀵메뉴7배지여부'                        ;
COMMENT ON COLUMN PLT_USER_ENV.SHORT_CUT_8_BGE_YN                 IS '퀵메뉴8배지여부'                        ;
COMMENT ON COLUMN PLT_USER_ENV.WRTR_DEPT_CD                       IS '작성자부서코드'                         ;
COMMENT ON COLUMN PLT_USER_ENV.WRTR_ID                            IS '작성자ID'                               ;
COMMENT ON COLUMN PLT_USER_ENV.WRT_DTTM                           IS '작성일시'                               ;
COMMENT ON COLUMN PLT_USER_ENV.PROC_ID                            IS '처리자'                                 ;
COMMENT ON COLUMN PLT_USER_ENV.IT_PROCESSING                      IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_ORGZ    조직정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_ORGZ CASCADE CONSTRAINTS;

CREATE TABLE PLT_ORGZ (  
    DEPT_CD                                                               VARCHAR2(20)      NOT NULL                  ,
    DEPT_NM                                                               VARCHAR2(100)                               ,
    SPST_DEPT_CD                                                          VARCHAR2(20)                                ,
    DEPT_DIV_CD                                                           VARCHAR2(20)                                ,
    DEPT_CRE_DT                                                           VARCHAR2(8)                                 ,
    DEPT_DSUS_DT                                                          VARCHAR2(8)                                 ,
    SCR_DISP_YN                                                           CHAR(1)                                     ,
    USE_YN                                                                CHAR(1)                                     ,
    SORT_ORD                                                              NUMBER(22)                                  ,
    REGR_DEPT_CD                                                          VARCHAR2(20)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    REG_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(20)                                ,
    AMDR_ID                                                               VARCHAR2(20)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_ORGZ PRIMARY KEY (DEPT_CD) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_ORGZ                                        IS '조직정보'                               ;
COMMENT ON COLUMN PLT_ORGZ.DEPT_CD                                IS '부서코드'                               ;
COMMENT ON COLUMN PLT_ORGZ.DEPT_NM                                IS '부서명'                                 ;
COMMENT ON COLUMN PLT_ORGZ.SPST_DEPT_CD                           IS '상위부서코드'                           ;
COMMENT ON COLUMN PLT_ORGZ.DEPT_DIV_CD                            IS '부서구분코드'                           ;
COMMENT ON COLUMN PLT_ORGZ.DEPT_CRE_DT                            IS '부서생성일자'                           ;
COMMENT ON COLUMN PLT_ORGZ.DEPT_DSUS_DT                           IS '부서폐지일자'                           ;
COMMENT ON COLUMN PLT_ORGZ.SCR_DISP_YN                            IS '화면표시여부'                           ;
COMMENT ON COLUMN PLT_ORGZ.USE_YN                                 IS '사용여부'                               ;
COMMENT ON COLUMN PLT_ORGZ.SORT_ORD                               IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_ORGZ.REGR_DEPT_CD                           IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_ORGZ.REGR_ID                                IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_ORGZ.REG_DTTM                               IS '등록일시'                               ;
COMMENT ON COLUMN PLT_ORGZ.AMDR_DEPT_CD                           IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_ORGZ.AMDR_ID                                IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_ORGZ.UPD_DTTM                               IS '수정일시'                               ;
COMMENT ON COLUMN PLT_ORGZ.PROC_ID                                IS '처리자'                                 ;
COMMENT ON COLUMN PLT_ORGZ.IT_PROCESSING                          IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_ASP_CODE    ASP코드정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_ASP_CODE CASCADE CONSTRAINTS;

CREATE TABLE PLT_ASP_CODE (  
    ASP_GROUP_CD                                                          VARCHAR2(60)      NOT NULL                  ,
    ASP_CD                                                                VARCHAR2(120)     NOT NULL                  ,
    ASP_CD_TYPE                                                           VARCHAR2(60)      NOT NULL                  ,
    CD_NM                                                                 VARCHAR2(300)     NOT NULL                  ,
    CD_EXPLN                                                               VARCHAR2(600)                               ,
    CD_USE_FR_DT                                                          VARCHAR2(24)                                ,
    CD_USE_TO_DT                                                          VARCHAR2(24)                                ,
    CD_PRE_TYPE                                                           VARCHAR2(60)                                ,
    ETC_INFO01                                                            VARCHAR2(600)                               ,
    ETC_INFO02                                                            VARCHAR2(600)                               ,
    ETC_INFO03                                                            VARCHAR2(600)                               ,
    USE_YN                                                                CHAR(1)           NOT NULL                  ,
    SORT_ORD                                                              NUMBER(22)        NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    REG_DTTM                                                              DATE              NOT NULL                  ,
    AMDR_ID                                                               VARCHAR2(60)                                ,
    UPD_DTTM                                                              DATE                                        ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_ASP_CODE PRIMARY KEY (ASP_GROUP_CD,ASP_CD,ASP_CD_TYPE) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_ASP_CODE                                    IS 'ASP코드정보'                            ;
COMMENT ON COLUMN PLT_ASP_CODE.ASP_GROUP_CD                       IS 'ASP그룹코드'                            ;
COMMENT ON COLUMN PLT_ASP_CODE.ASP_CD                             IS 'ASP코드'                                ;
COMMENT ON COLUMN PLT_ASP_CODE.ASP_CD_TYPE                        IS 'ASP코드타입'                            ;
COMMENT ON COLUMN PLT_ASP_CODE.CD_NM                              IS '코드명'                                 ;
COMMENT ON COLUMN PLT_ASP_CODE.CD_EXPLN                            IS '코드설명'                               ;
COMMENT ON COLUMN PLT_ASP_CODE.CD_USE_FR_DT                       IS '코드사용시작일자'                       ;
COMMENT ON COLUMN PLT_ASP_CODE.CD_USE_TO_DT                       IS '코드사용종료일자'                       ;
COMMENT ON COLUMN PLT_ASP_CODE.CD_PRE_TYPE                        IS '코드생성구분'                           ;
COMMENT ON COLUMN PLT_ASP_CODE.ETC_INFO01                         IS '기타정보01'                             ;
COMMENT ON COLUMN PLT_ASP_CODE.ETC_INFO02                         IS '기타정보02'                             ;
COMMENT ON COLUMN PLT_ASP_CODE.ETC_INFO03                         IS '기타정보03'                             ;
COMMENT ON COLUMN PLT_ASP_CODE.USE_YN                             IS '사용여부'                               ;
COMMENT ON COLUMN PLT_ASP_CODE.SORT_ORD                           IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_ASP_CODE.REGR_ID                            IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_ASP_CODE.REG_DTTM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_ASP_CODE.AMDR_ID                            IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_ASP_CODE.UPD_DTTM                           IS '수정일시'                               ;
COMMENT ON COLUMN PLT_ASP_CODE.PROC_ID                            IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_ASP_CODE.IT_PROCESSING                      IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_IDS    테이블ID정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_IDS CASCADE CONSTRAINTS;

CREATE TABLE PLT_IDS (  
    TABLE_NAME                                                            VARCHAR2(100)     NOT NULL                  ,
    NEXT_ID                                                               NUMBER(22)        NOT NULL                  ,
    CONSTRAINT XPK_PLT_IDS PRIMARY KEY (TABLE_NAME) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_IDS                                         IS '테이블ID정보'                           ;
COMMENT ON COLUMN PLT_IDS.TABLE_NAME                              IS '테이블명'                               ;
COMMENT ON COLUMN PLT_IDS.NEXT_ID                                 IS '다음 값(ID)'                            ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CNSL_UNITY_HST    상담통합이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_CNSL_UNITY_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_CNSL_UNITY_HST (	
    UNITY_HST_ID                                                          VARCHAR2(30)      NOT NULL                  , 
    CUSTCO_ID                                                          VARCHAR2(20)                                , 
    JOBBY_CNSL_HST_ID                                                     VARCHAR2(30)                                , 
    CNSL_DIV                                                              VARCHAR2(10)                                , 
    CSTMR_TELNO                                                           VARCHAR2(64)                                , 
    CSTMR_ID                                                              VARCHAR2(80)                                , 
    REGR_DEPT_CD                                                          VARCHAR2(60)                                , 
    REGR_ID                                                               VARCHAR2(60)                                , 
    REG_DTTM                                                              TIMESTAMP(6)                                , 
    PROC_ID                                                               VARCHAR2(60)                                , 
    IT_PROCESSING                                                         TIMESTAMP(6)                                ,
    CONSTRAINT XPK_PLT_CNSL_UNITY_HST PRIMARY KEY (UNITY_HST_ID) 
)
--TABLESPACE TS_PALETTE
;
COMMENT ON TABLE  PLT_CNSL_UNITY_HST                               IS '상담통합이력'                           ;
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.UNITY_HST_ID                  IS '통합이력ID';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.CUSTCO_ID                  IS 'ASP_고객사_키';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.JOBBY_CNSL_HST_ID             IS '업무별 상담 이력 ID';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.CNSL_DIV                      IS '상담 구분';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.CSTMR_TELNO                   IS '고객 전화번호';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.CSTMR_ID                      IS '고객_ID';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.REGR_DEPT_CD                  IS '등록자부서코드';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.REGR_ID                       IS '등록자ID';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.REG_DTTM                      IS '등록일시';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.PROC_ID                       IS '처리자';
COMMENT ON COLUMN PLT_CNSL_UNITY_HST.IT_PROCESSING                 IS '전산처리일시';



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_FILE    공통첨부파일정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP TABLE PLT_FILE CASCADE CONSTRAINTS;

CREATE TABLE PLT_FILE (  
    CUSTCO_ID                                                          VARCHAR2(10)      NOT NULL                       ,
    FILE_GROUP_KEY                                                        VARCHAR2(90)      NOT NULL                       ,
    FILE_KEY                                                              VARCHAR2(90)      NOT NULL                       ,
    TASK_TYPE_CD                                                             VARCHAR2(60)      NOT NULL                       ,
    TRGT_TYPE_CD                                                           VARCHAR2(20)      NOT NULL                       ,
    PATH_TYPE_CD                                                             VARCHAR2(60)      NOT NULL                       ,
    MIME_TYPE_CD                                                             VARCHAR2(200)     NOT NULL                       ,
    ACTL_FILE_NM                                                     VARCHAR2(600)     NOT NULL                       ,
    STRG_FILE_NM                                                         VARCHAR2(600)     NULL                           ,
    FILE_PATH                                                             VARCHAR2(2000)    NULL                           ,
    FILE_SZ                                                             NUMBER(11)        DEFAULT 0 NOT NULL             ,
    FILE_EXTN                                                             VARCHAR2(60)      NOT NULL                       ,
    DWNLD_CNT                                                             NUMBER(11)        DEFAULT 0 NOT NULL             ,
    FILE_URL                                                              VARCHAR2(1000)                                   ,
    FILE_BLOB                                                             BLOB              NULL                           ,
    ACS_KEY                                                            VARCHAR2(1000)                                   ,
    FILE_ACS_TYPE_CD                                                      VARCHAR2(20)      DEFAULT 'PRIVATE' NOT NULL     ,
    USE_YN                                                                CHAR(1)           DEFAULT 'Y' NOT NULL           ,
    REGR_DEPT_CD                                                          VARCHAR2(60)      NOT NULL                       ,
    REGR_ID                                                               VARCHAR2(60)      NOT NULL                       ,
    REG_DTTM                                                              TIMESTAMP         DEFAULT SYSTIMESTAMP NOT NULL  ,
    AMDR_DEPT_CD                                                          VARCHAR2(60)      NULL                           ,
    AMDR_ID                                                               VARCHAR2(60)      NULL                           ,
    UPD_DTTM                                                              TIMESTAMP         NULL                           ,
    PROC_ID                                                               VARCHAR2(60)      NOT NULL                       ,
    IT_PROCESSING                                                         TIMESTAMP         DEFAULT SYSTIMESTAMP NOT NULL  ,
    CONSTRAINT XPK_PLT_FILE PRIMARY KEY (CUSTCO_ID, FILE_GROUP_KEY, FILE_KEY)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_FILE                                        IS '공통첨부파일정보'                       ;
COMMENT ON COLUMN PLT_FILE.CUSTCO_ID                           IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_FILE.FILE_GROUP_KEY                         IS '파일그룹키'                             ;
COMMENT ON COLUMN PLT_FILE.FILE_KEY                               IS '파일키'                                 ;
COMMENT ON COLUMN PLT_FILE.TASK_TYPE_CD                              IS '업무구분'                               ;
COMMENT ON COLUMN PLT_FILE.TRGT_TYPE_CD                            IS '저장소대상구분'                         ;
COMMENT ON COLUMN PLT_FILE.PATH_TYPE_CD                              IS '경로구분'                               ;
COMMENT ON COLUMN PLT_FILE.MIME_TYPE_CD                              IS 'MIME유형'                               ;
COMMENT ON COLUMN PLT_FILE.ACTL_FILE_NM                      IS '진짜파일명'                             ;
COMMENT ON COLUMN PLT_FILE.STRG_FILE_NM                          IS '저장된파일명'                           ;
COMMENT ON COLUMN PLT_FILE.FILE_PATH                              IS '파일경로'                               ;
COMMENT ON COLUMN PLT_FILE.FILE_SZ                              IS '파일크기'                               ;
COMMENT ON COLUMN PLT_FILE.FILE_EXTN                              IS '파일확장자'                             ;
COMMENT ON COLUMN PLT_FILE.DWNLD_CNT                              IS '다운로드수'                             ;
COMMENT ON COLUMN PLT_FILE.FILE_URL                               IS '파일URL'                                ;
COMMENT ON COLUMN PLT_FILE.ACS_KEY                             IS '접근키'                                 ;
COMMENT ON COLUMN PLT_FILE.USE_YN                                 IS '사용여부'                               ;
COMMENT ON COLUMN PLT_FILE.REGR_DEPT_CD                           IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_FILE.REGR_ID                                IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_FILE.REG_DTTM                               IS '등록일시'                               ;
COMMENT ON COLUMN PLT_FILE.AMDR_DEPT_CD                           IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_FILE.AMDR_ID                                IS '수성자ID'                               ;
COMMENT ON COLUMN PLT_FILE.UPD_DTTM                               IS '수성일시'                               ;
COMMENT ON COLUMN PLT_FILE.PROC_ID                                IS '처리자ID'                               ;
COMMENT ON COLUMN PLT_FILE.IT_PROCESSING                          IS '전산처리일시'                           ;
COMMENT ON COLUMN PLT_FILE.FILE_BLOB                              IS '파일'                                   ;
COMMENT ON COLUMN PLT_FILE.FILE_ACS_TYPE_CD                       IS '파일엑세스유형(PUBLIC:공개,PRIVATE:비공개)' ;
