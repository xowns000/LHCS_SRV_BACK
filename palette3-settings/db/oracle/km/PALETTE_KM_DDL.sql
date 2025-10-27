
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_CONT    지식_컨텐츠관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_CONT CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_CONT (  
    CNTNT_ID                                                              VARCHAR2(30)      NOT NULL                  ,
    DIV_ID                                                                VARCHAR2(30)      NOT NULL                  ,
    CNTNT_NO                                                              VARCHAR2(20)                                ,
    TITL                                                                  VARCHAR2(200)                               ,
    CONTN                                                                 CLOB                                        ,
    SAVE_STAT_CD                                                          VARCHAR2(20)                                ,
    IDX_STAT_CD                                                           VARCHAR2(20)                                ,
    IDX_PROC_DTTM                                                         TIMESTAMP                                   ,
    SRCH_KWD                                                              VARCHAR2(500)                               ,
    FILE_GROUP_KEY                                                        VARCHAR2(90)                                ,
    INQ_CNT                                                               NUMBER(22)                                  ,
    FST_STRT_DT                                                           VARCHAR2(8)                                 ,
    REGR_DEPT_CD                                                          VARCHAR2(20)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    REG_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    APVP_DEPT_CD                                                          VARCHAR2(20)                                ,
    APVP_ID                                                               VARCHAR2(20)                                ,
    ADM_DTTM                                                              TIMESTAMP                                   ,
    AMDR_DEPT_CD                                                          VARCHAR2(20)      NOT NULL                  ,
    AMDR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    UPD_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    PROC_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_KM_CONT PRIMARY KEY (CNTNT_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_CONT                                     IS '지식_컨텐츠관리'                        ;
COMMENT ON COLUMN PLT_KM_CONT.CNTNT_ID                            IS '컨텐츠ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT.DIV_ID                              IS '분류ID'                                 ;
COMMENT ON COLUMN PLT_KM_CONT.CNTNT_NO                            IS '컨텐츠번호'                             ;
COMMENT ON COLUMN PLT_KM_CONT.TITL                                IS '제목'                                   ;
COMMENT ON COLUMN PLT_KM_CONT.CONTN                               IS '내용'                                   ;
COMMENT ON COLUMN PLT_KM_CONT.SAVE_STAT_CD                        IS '저장상태코드'                           ;
COMMENT ON COLUMN PLT_KM_CONT.IDX_STAT_CD                         IS '색인상태코드'                           ;
COMMENT ON COLUMN PLT_KM_CONT.IDX_PROC_DTTM                       IS '색인처리일시'                           ;
COMMENT ON COLUMN PLT_KM_CONT.SRCH_KWD                            IS '검색키워드'                             ;
COMMENT ON COLUMN PLT_KM_CONT.FILE_GROUP_KEY                      IS '파일그룹키'                             ;
COMMENT ON COLUMN PLT_KM_CONT.INQ_CNT                             IS '조회수'                                 ;
COMMENT ON COLUMN PLT_KM_CONT.FST_STRT_DT                         IS '최초개시일자'                           ;
COMMENT ON COLUMN PLT_KM_CONT.REGR_DEPT_CD                        IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT.REGR_ID                             IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT.REG_DTTM                            IS '등록일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT.APVP_DEPT_CD                        IS '승인자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT.APVP_ID                             IS '승인자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT.ADM_DTTM                            IS '승인일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT.AMDR_DEPT_CD                        IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT.AMDR_ID                             IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT.UPD_DTTM                            IS '수정일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT.PROC_ID                             IS '처리자'                                 ;
COMMENT ON COLUMN PLT_KM_CONT.IT_PROCESSING                       IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_CONT_HST    지식_컨텐츠이력관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_CONT_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_CONT_HST (  
    CNTNT_ID                                                              VARCHAR2(30)      NOT NULL                  ,
    SEQ                                                                   NUMBER(22)        NOT NULL                  ,
    DIV_ID                                                                VARCHAR2(30)      NOT NULL                  ,
    TITL                                                                  VARCHAR2(200)                               ,
    CONTN                                                                 CLOB                                        ,
    VER                                                                   VARCHAR2(20)      NOT NULL                  ,
    CHG_HIST_CONTN                                                        VARCHAR2(1000)                              ,
    REGR_DEPT_CD                                                          VARCHAR2(20)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    REG_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(20)                                ,
    AMDR_ID                                                               VARCHAR2(20)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_KM_CONT_HST PRIMARY KEY (CNTNT_ID,SEQ) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_CONT_HST                                 IS '지식_컨텐츠이력관리'                    ;
COMMENT ON COLUMN PLT_KM_CONT_HST.CNTNT_ID                        IS '컨텐츠ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_HST.SEQ                             IS '순번'                                   ;
COMMENT ON COLUMN PLT_KM_CONT_HST.DIV_ID                          IS '분류ID'                                 ;
COMMENT ON COLUMN PLT_KM_CONT_HST.TITL                            IS '제목'                                   ;
COMMENT ON COLUMN PLT_KM_CONT_HST.CONTN                           IS '내용'                                   ;
COMMENT ON COLUMN PLT_KM_CONT_HST.VER                             IS '버전'                                   ;
COMMENT ON COLUMN PLT_KM_CONT_HST.CHG_HIST_CONTN                  IS '변경이력내용'                           ;
COMMENT ON COLUMN PLT_KM_CONT_HST.REGR_DEPT_CD                    IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT_HST.REGR_ID                         IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_HST.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT_HST.AMDR_DEPT_CD                    IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT_HST.AMDR_ID                         IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_HST.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT_HST.PROC_ID                         IS '처리자'                                 ;
COMMENT ON COLUMN PLT_KM_CONT_HST.IT_PROCESSING                   IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_CONT_CNT    지식_컨텐츠연결관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_CONT_CNT CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_CONT_CNT (  
    CNTNT_ID                                                              VARCHAR2(30)      NOT NULL                  ,
    CONNECT_CNTNT_ID                                                      VARCHAR2(30)      NOT NULL                  ,
    SORT_ORD                                                              NUMBER(22)                                  ,
    REGR_DEPT_CD                                                          VARCHAR2(20)      NOT NULL                  ,
    REGR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    REG_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    AMDR_DEPT_CD                                                          VARCHAR2(20)                                ,
    AMDR_ID                                                               VARCHAR2(20)                                ,
    UPD_DTTM                                                              TIMESTAMP                                   ,
    PROC_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_KM_CONT_CNT PRIMARY KEY (CNTNT_ID,CONNECT_CNTNT_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_CONT_CNT                                 IS '지식_컨텐츠연결관리'                    ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.CNTNT_ID                        IS '컨텐츠ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.CONNECT_CNTNT_ID                IS '연결컨텐츠ID'                           ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.SORT_ORD                        IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.REGR_DEPT_CD                    IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.REGR_ID                         IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.AMDR_DEPT_CD                    IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.AMDR_ID                         IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.PROC_ID                         IS '처리자'                                 ;
COMMENT ON COLUMN PLT_KM_CONT_CNT.IT_PROCESSING                   IS '전산처리일시'                           ;

--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_CONT_CLS    지식_컨텐츠분류관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_CONT_CLS CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_CONT_CLS (  
    DIV_ID                                                                VARCHAR2(30)      NOT NULL                  ,
    DIV_NM                                                                VARCHAR2(100)                               ,
    SPST_DIV_ID                                                           VARCHAR2(30)                                ,
    TMPLAT_ID                                                             VARCHAR2(30)                                ,
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
    CONSTRAINT XPK_PLT_KM_CONT_CLS PRIMARY KEY (DIV_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_CONT_CLS                                 IS '지식_컨텐츠분류관리'                    ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.DIV_ID                          IS '분류ID'                                 ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.DIV_NM                          IS '분류명'                                 ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.SPST_DIV_ID                     IS '상위분류ID'                             ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.TMPLAT_ID                       IS '템플릿ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.SCR_DISP_YN                     IS '화면표시여부'                           ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.USE_YN                          IS '사용여부'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.SORT_ORD                        IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.REGR_DEPT_CD                    IS '등록자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.REGR_ID                         IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.REG_DTTM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.AMDR_DEPT_CD                    IS '수정자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.AMDR_ID                         IS '수정자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.UPD_DTTM                        IS '수정일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.PROC_ID                         IS '처리자'                                 ;
COMMENT ON COLUMN PLT_KM_CONT_CLS.IT_PROCESSING                   IS '전산처리일시'                           ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_CONT_FILE    지식_컨텐츠첨부파일관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_CONT_FILE CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_CONT_FILE (  
    CNTNT_ID                                                              VARCHAR2(30)      NOT NULL                  ,
    FILE_KEY                                                              VARCHAR2(30)      NOT NULL                  ,
    USE_YN                                                                CHAR(1)                                     ,
    SORT_ORD                                                              NUMBER(22)                                  ,
    WRTR_DEPT_CD                                                          VARCHAR2(20)      NOT NULL                  ,
    WRTR_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    WRT_DTTM                                                              TIMESTAMP         NOT NULL                  ,
    PROC_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    IT_PROCESSING                                                         TIMESTAMP         NOT NULL                  ,
    CONSTRAINT XPK_PLT_KM_CONT_FILE PRIMARY KEY (CNTNT_ID,FILE_KEY) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_CONT_FILE                                IS '지식_컨텐츠첨부파일관리'                ;
COMMENT ON COLUMN PLT_KM_CONT_FILE.CNTNT_ID                       IS '컨텐츠ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_FILE.FILE_KEY                       IS '파일키'                                 ;
COMMENT ON COLUMN PLT_KM_CONT_FILE.USE_YN                         IS '사용여부'                               ;
COMMENT ON COLUMN PLT_KM_CONT_FILE.SORT_ORD                       IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_KM_CONT_FILE.WRTR_DEPT_CD                   IS '작성자부서코드'                         ;
COMMENT ON COLUMN PLT_KM_CONT_FILE.WRTR_ID                        IS '작성자ID'                               ;
COMMENT ON COLUMN PLT_KM_CONT_FILE.WRT_DTTM                       IS '작성일시'                               ;
COMMENT ON COLUMN PLT_KM_CONT_FILE.PROC_ID                        IS '처리자'                                 ;
COMMENT ON COLUMN PLT_KM_CONT_FILE.IT_PROCESSING                  IS '전산처리일시'                           ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_CONT_RECOMEND    지식_컨텐츠추천관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_CONT_RECOMEND CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_CONT_RECOMEND (
    CNTNT_ID                                                            VARCHAR2(20)        NOT NULL                  , 
    USER_ID                                                             VARCHAR2(20)                                  , 
    RCMD_SCOR                                                           NUMBER(5)                                     , 
    RCMD_RSN                                                            VARCHAR2(300)                                 , 
    WRTR_DEPT_CD                                                        VARCHAR2(20)                                  , 
    WRTR_ID                                                             VARCHAR2(20)                                  , 
    WRT_DTTM                                                            TIMESTAMP(6)                                  , 
    PROC_ID                                                             VARCHAR2(20)                                  , 
    IT_PROCESSING                                                       TIMESTAMP(6)                                  ,
    CONSTRAINT XPK_PLT_KM_CONT_RECOMEND PRIMARY KEY (CNTNT_ID, USER_ID)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON COLUMN PLT_KM_CONT_RECOMEND.CNTNT_ID IS '컨텐츠ID';
COMMENT ON COLUMN PLT_KM_CONT_RECOMEND.USER_ID IS '사용자ID';
COMMENT ON COLUMN PLT_KM_CONT_RECOMEND.RCMD_SCOR IS '추천점수';
COMMENT ON COLUMN PLT_KM_CONT_RECOMEND.RCMD_RSN IS '추천사유';
COMMENT ON COLUMN PLT_KM_CONT_RECOMEND.WRTR_DEPT_CD IS '작성자부서코드';
COMMENT ON COLUMN PLT_KM_CONT_RECOMEND.WRTR_ID IS '작성자ID';
COMMENT ON COLUMN PLT_KM_CONT_RECOMEND.WRT_DTTM IS '작성일시';
COMMENT ON COLUMN PLT_KM_CONT_RECOMEND.PROC_ID IS '처리자';
COMMENT ON COLUMN PLT_KM_CONT_RECOMEND.IT_PROCESSING IS '전산처리일시';
   


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_TMPL    지식_템플릿관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_TMPL CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_TMPL (	
    TMPLAT_ID                                                               VARCHAR2(50)    NOT NULL                    , 
    TMPLAT_TYPE_CD                                                          VARCHAR2(20)    NOT NULL                    , 
    TITL                                                                    VARCHAR2(50)    NOT NULL                    , 
    CONTN CLOB                                                                                                          , 
    USE_YN                                                                  VARCHAR2(1)                                 , 
    REGR_DEPT_CD                                                            VARCHAR2(20)                                , 
    REGR_ID                                                                 VARCHAR2(20)                                , 
    REG_DTTM                                                                TIMESTAMP(6)                                , 
    AMDR_DEPT_CD                                                            VARCHAR2(20)                                , 
    AMDR_ID                                                                 VARCHAR2(20)                                , 
    UPD_DTTM                                                                TIMESTAMP(6)                                , 
    PROC_ID                                                                 VARCHAR2(20)                                , 
    IT_PROCESSING                                                           TIMESTAMP(6)                                ,
    CONSTRAINT PLT_KM_TMPL_PK PRIMARY KEY (TMPLAT_ID)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_TMPL                                IS '지식_템플릿관리'                         ;
COMMENT ON COLUMN PLT_KM_TMPL.TMPLAT_ID                      IS '템플릿ID'                                   ;
COMMENT ON COLUMN PLT_KM_TMPL.TMPLAT_TYPE_CD                 IS '템플릿구분코드'                              ;
COMMENT ON COLUMN PLT_KM_TMPL.TITL                           IS '제목'                                       ;
COMMENT ON COLUMN PLT_KM_TMPL.CONTN                          IS '내용'                                       ;
COMMENT ON COLUMN PLT_KM_TMPL.USE_YN                         IS '사용여부'                                   ;
COMMENT ON COLUMN PLT_KM_TMPL.REGR_DEPT_CD                   IS '등록자부서코드'                              ;
COMMENT ON COLUMN PLT_KM_TMPL.REGR_ID                        IS '등록자ID'                                   ;
COMMENT ON COLUMN PLT_KM_TMPL.REG_DTTM                       IS '등록일시'                                   ;
COMMENT ON COLUMN PLT_KM_TMPL.AMDR_DEPT_CD                   IS '수정자부서코드'                             ;
COMMENT ON COLUMN PLT_KM_TMPL.AMDR_ID                        IS '수정자ID'                                  ;
COMMENT ON COLUMN PLT_KM_TMPL.UPD_DTTM                       IS '수정일시'                                  ;
COMMENT ON COLUMN PLT_KM_TMPL.PROC_ID                        IS '처리자'                                    ;
COMMENT ON COLUMN PLT_KM_TMPL.IT_PROCESSING                  IS '전산처리일시'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_TMPL    지식_컨텐츠이력관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_CONT_RDCNT CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_CONT_RDCNT (	
    CNTNT_ID                                                                VARCHAR2(30)    NOT NULL                    , 
    VER                                                                     VARCHAR2(20)    NOT NULL                    , 
    DT                                                                      VARCHAR2(8)     NOT NULL                    , 
    INQ_CNT                                                                 NUMBER(22)                                  , 
    WRTR_DEPT_CD                                                            VARCHAR2(20)    NOT NULL                    , 
    WRTR_ID                                                                 VARCHAR2(20)    NOT NULL                    , 
    WRT_DTTM                                                                TIMESTAMP(6)    NOT NULL                    , 
    PROC_ID                                                                 VARCHAR2(20)    NOT NULL                    , 
    IT_PROCESSING                                                           TIMESTAMP(6)                                , 
    USER_ID                                                                 VARCHAR2(20)    NOT NULL                    , 
    CONSTRAINT PLT_KM_CONT_RDCNT_PK PRIMARY KEY (CNTNT_ID, VER, DT, USER_ID)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_CONT_RDCNT                         IS '지식_컨텐츠이력관리'                         ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.CNTNT_ID                IS '컨텐츠ID'                                       ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.VER                     IS '버전'                                           ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.DT                      IS '일자'                                           ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.INQ_CNT                 IS '조회수'                                         ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.WRTR_DEPT_CD            IS '작성자부서코드'                                  ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.WRTR_ID                 IS '작성자ID'                                       ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.WRT_DTTM                IS '작성일시'                                       ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.PROC_ID                 IS '처리자'                                         ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.IT_PROCESSING           IS '전산처리일시'                                    ;
COMMENT ON COLUMN PLT_KM_CONT_RDCNT.USER_ID                 IS '사용자ID'                                       ;

   

   
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_KNLG    지식마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_KNLG CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_KNLG (  
    CNSL_KMS_NO                                                           VARCHAR2(20)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CNSL_KMS_TY                                                           VARCHAR2(10)                                ,
    CNSL_LCLS_CODE                                                        VARCHAR2(10)                                ,
    CNSL_MDCLS_CODE                                                       VARCHAR2(10)                                ,
    CNSL_SCLS_CODE                                                        VARCHAR2(10)                                ,
    CNSL_KMS_TIT                                                          VARCHAR2(2000)                              ,
    QUST                                                                  CLOB                                        ,
    ANSW                                                                  CLOB                                        ,
    FILE_GROUP_KEY                                                        VARCHAR2(30)                                ,
    CNSL_KMS_REM                                                          VARCHAR2(2000)                              ,
    INQ_CNT                                                               NUMBER(10)                                  ,
    USE_YN                                                                VARCHAR2(1)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_KM_KNLG PRIMARY KEY (CNSL_KMS_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_KNLG                                        IS '지식마스터'                             ;
COMMENT ON COLUMN PLT_KM_KNLG.CNSL_KMS_NO                            IS '상담지식번호'                           ;
COMMENT ON COLUMN PLT_KM_KNLG.CENT_TY                                IS '센터구분'                               ;
COMMENT ON COLUMN PLT_KM_KNLG.CNSL_KMS_TY                            IS '상담지식유형'                           ;
COMMENT ON COLUMN PLT_KM_KNLG.CNSL_LCLS_CODE                         IS '상담대분류코드'                         ;
COMMENT ON COLUMN PLT_KM_KNLG.CNSL_MDCLS_CODE                        IS '상담중분류코드'                         ;
COMMENT ON COLUMN PLT_KM_KNLG.CNSL_SCLS_CODE                         IS '상담소분류코드'                         ;
COMMENT ON COLUMN PLT_KM_KNLG.CNSL_KMS_TIT                           IS '매뉴얼제목'                             ;
COMMENT ON COLUMN PLT_KM_KNLG.QUST                                   IS '질문'                                   ;
COMMENT ON COLUMN PLT_KM_KNLG.ANSW                                   IS '답변'                                   ;
COMMENT ON COLUMN PLT_KM_KNLG.FILE_GROUP_KEY                         IS '파일그룹키'                             ;
COMMENT ON COLUMN PLT_KM_KNLG.CNSL_KMS_REM                           IS '상담지식비고'                           ;
COMMENT ON COLUMN PLT_KM_KNLG.INQ_CNT                                IS '조회수'                                 ;
COMMENT ON COLUMN PLT_KM_KNLG.USE_YN                                 IS '사용여부'                               ;
COMMENT ON COLUMN PLT_KM_KNLG.REG_DTIM                               IS '등록일시'                               ;
COMMENT ON COLUMN PLT_KM_KNLG.REG_MAN                                IS '등록자'                                 ;
COMMENT ON COLUMN PLT_KM_KNLG.CHNG_DTIM                              IS '변경일시'                               ;
COMMENT ON COLUMN PLT_KM_KNLG.CHNG_MAN                               IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_KNLG_CMT    지식댓글
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_KNLG_CMT CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_KNLG_CMT (  
    CNSL_KMS_NO                                                           VARCHAR2(20)      NOT NULL                  ,
    CMT_NO                                                                NUMBER(10)        NOT NULL                  ,
    CMT_CNTN                                                              VARCHAR2(2000)                              ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_KM_KNLG_CMT PRIMARY KEY (CNSL_KMS_NO,CMT_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_KNLG_CMT                                    IS '지식댓글'                               ;
COMMENT ON COLUMN PLT_KM_KNLG_CMT.CNSL_KMS_NO                        IS '상담지식번호'                           ;
COMMENT ON COLUMN PLT_KM_KNLG_CMT.CMT_NO                             IS '댓글번호'                               ;
COMMENT ON COLUMN PLT_KM_KNLG_CMT.CMT_CNTN                           IS '댓글내용'                               ;
COMMENT ON COLUMN PLT_KM_KNLG_CMT.REG_DTIM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_KM_KNLG_CMT.REG_MAN                            IS '등록자'                                 ;
COMMENT ON COLUMN PLT_KM_KNLG_CMT.CHNG_DTIM                          IS '변경일시'                               ;
COMMENT ON COLUMN PLT_KM_KNLG_CMT.CHNG_MAN                           IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_KM_KNLG_INQ_HST    지식조회이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_KM_KNLG_INQ_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_KM_KNLG_INQ_HST (  
    CNSL_KMS_NO                                                           VARCHAR2(20)      NOT NULL                  ,
    USER_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    INQ_CNT                                                               NUMBER(10)                                  ,
    FIST_INQ_DTIM                                                         VARCHAR2(14)                                ,
    LAST_INQ_DTIM                                                         VARCHAR2(14)                                ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_KM_KNLG_INQ_HST PRIMARY KEY (CNSL_KMS_NO,USER_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_KM_KNLG_INQ_HST                                IS '지식조회이력'                           ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.CNSL_KMS_NO                    IS '상담지식번호'                           ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.USER_ID                        IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.CENT_TY                        IS '센터구분'                               ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.INQ_CNT                        IS '조회수'                                 ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.FIST_INQ_DTIM                  IS '최초조회일시'                           ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.LAST_INQ_DTIM                  IS '최종조회일시'                           ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_KM_KNLG_INQ_HST.CHNG_MAN                       IS '변경자'                                 ;

