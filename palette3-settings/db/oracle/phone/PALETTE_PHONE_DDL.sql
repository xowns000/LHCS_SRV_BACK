
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CAMP    전화_캠페인마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CAMP CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CAMP (  
    CAM_ID                                                                NUMBER(10)        NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CAM_NM                                                                VARCHAR2(100)                               ,
    CAM_TY                                                                VARCHAR2(10)                                ,
    BUSI_TY                                                               VARCHAR2(10)                                ,
    CAM_BEGIN_DATE                                                        VARCHAR2(8)                                 ,
    CAM_BEGIN_TIME                                                        VARCHAR2(6)                                 ,
    CAM_EOT_DATE                                                          VARCHAR2(8)                                 ,
    CAM_EOT_TIME                                                          VARCHAR2(6)                                 ,
    FISH_YN                                                               VARCHAR2(1)                                 ,
    FISH_DATE                                                             VARCHAR2(8)                                 ,
    FISH_TIME                                                             VARCHAR2(6)                                 ,
    REM                                                                   VARCHAR2(500)                               ,
    QUEST_ID                                                              NUMBER(10)                                  ,
    USE_YN                                                                VARCHAR2(1)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CAMP PRIMARY KEY (CAM_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CAMP                                    IS '전화_캠페인마스터'                      ;
COMMENT ON COLUMN PLT_PHN_CAMP.CAM_ID                             IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.CENT_TY                            IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.CAM_NM                             IS '캠페인명'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.CAM_TY                             IS '캠페인구분'                             ;
COMMENT ON COLUMN PLT_PHN_CAMP.BUSI_TY                            IS '업무구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.CAM_BEGIN_DATE                     IS '캠페인시작일자'                         ;
COMMENT ON COLUMN PLT_PHN_CAMP.CAM_BEGIN_TIME                     IS '캠페인시작시간'                         ;
COMMENT ON COLUMN PLT_PHN_CAMP.CAM_EOT_DATE                       IS '캠페인종료일자'                         ;
COMMENT ON COLUMN PLT_PHN_CAMP.CAM_EOT_TIME                       IS '캠페인종료시간'                         ;
COMMENT ON COLUMN PLT_PHN_CAMP.FISH_YN                            IS '완료여부'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.FISH_DATE                          IS '완료일자'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.FISH_TIME                          IS '완료시간'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.REM                                IS '비고'                                   ;
COMMENT ON COLUMN PLT_PHN_CAMP.QUEST_ID                           IS '설문지ID'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.USE_YN                             IS '사용여부'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.REG_DTIM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.REG_MAN                            IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CAMP.CHNG_DTIM                          IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP.CHNG_MAN                           IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CAMP_CUS    전화_캠페인대상고객
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CAMP_CUS CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CAMP_CUS (  
    CAM_ID                                                                NUMBER(10)        NOT NULL                  ,
    CUST_NO                                                               VARCHAR2(64)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CUST_TY                                                               VARCHAR2(10)                                ,
    DSTR_YN                                                               VARCHAR2(1)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CAMP_CUS PRIMARY KEY (CAM_ID,CUST_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CAMP_CUS                                IS '전화_캠페인대상고객'                    ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS.CAM_ID                         IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS.CUST_NO                        IS '고객번호'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS.CENT_TY                        IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS.CUST_TY                        IS '고객구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS.DSTR_YN                        IS '배분여부'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS.CHNG_MAN                       IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CAMP_CUS_CNT    전화_캠페인고객접촉
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CAMP_CUS_CNT CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CAMP_CUS_CNT (  
    CNTC_SEQ                                                              NUMBER(10)        NOT NULL                  ,
    CAM_ID                                                                NUMBER(10)                                  ,
    CUST_NO                                                               VARCHAR2(64)                                ,
    CSLT_ID                                                               VARCHAR2(20)                                ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CUST_TY                                                               VARCHAR2(10)                                ,
    CNTC_DATE                                                             VARCHAR2(8)                                 ,
    CNTC_CNT                                                              NUMBER(10)                                  ,
    USE_YN                                                                VARCHAR2(1)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CAMP_CUS_CNT PRIMARY KEY (CNTC_SEQ) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CAMP_CUS_CNT                            IS '전화_캠페인고객접촉'                    ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CNTC_SEQ                   IS '접촉일련번호'                           ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CAM_ID                     IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CUST_NO                    IS '고객번호'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CSLT_ID                    IS '상담사ID'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CENT_TY                    IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CUST_TY                    IS '고객구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CNTC_DATE                  IS '접촉일자'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CNTC_CNT                   IS '접촉건수'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.USE_YN                     IS '사용여부'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.REG_DTIM                   IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.REG_MAN                    IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CHNG_DTIM                  IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_CNT.CHNG_MAN                   IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CAMP_CUS_DST    전화_캠페인고객배분
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CAMP_CUS_DST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CAMP_CUS_DST (  
    CAM_ID                                                                NUMBER(10)        NOT NULL                  ,
    CUST_NO                                                               VARCHAR2(64)      NOT NULL                  ,
    CSLT_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CUST_TY                                                               VARCHAR2(10)                                ,
    DSTR_DATE                                                             VARCHAR2(8)                                 ,
    WDRW_YN                                                               VARCHAR2(1)                                 ,
    WDRW_DATE                                                             VARCHAR2(8)                                 ,
    TRY_CNT                                                               NUMBER(10)                                  ,
    FISH_TY                                                               VARCHAR2(10)                                ,
    PROC_TY                                                               VARCHAR2(10)                                ,
    PROC_UNABL_TY                                                         VARCHAR2(10)                                ,
    CNSL_HIST_NO                                                          VARCHAR2(30)                                ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CAMP_CUS_DST PRIMARY KEY (CAM_ID,CUST_NO,CSLT_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CAMP_CUS_DST                            IS '전화_캠페인고객배분'                    ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.CAM_ID                     IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.CUST_NO                    IS '고객번호'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.CSLT_ID                    IS '상담사ID'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.CENT_TY                    IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.CUST_TY                    IS '고객구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.DSTR_DATE                  IS '배분일자'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.WDRW_YN                    IS '회수여부'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.WDRW_DATE                  IS '회수일자'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.TRY_CNT                    IS '시도건수'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.FISH_TY                    IS '완료구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.PROC_TY                    IS '처리구분'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.PROC_UNABL_TY              IS '처리불가구분'                           ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.CNSL_HIST_NO               IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.REG_DTIM                   IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.REG_MAN                    IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.CHNG_DTIM                  IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CAMP_CUS_DST.CHNG_MAN                   IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CLBK    전화_콜백
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CLBK CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CLBK (  
    CLBK_NO                                                               NUMBER(10)        NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CUST_TY                                                               VARCHAR2(10)                                ,
    IN_DATE                                                               VARCHAR2(8)                                 ,
    IN_TIME                                                               VARCHAR2(6)                                 ,
    IN_TY                                                                 VARCHAR2(10)                                ,
    SEND_MAN_NO                                                           VARCHAR2(64)                                ,
    CUST_NM                                                               VARCHAR2(100)                               ,
    INGNO                                                                 VARCHAR2(5)                                 ,
    CUST_NO                                                               VARCHAR2(64)                                ,
    CLBK_TEL_NO                                                           VARCHAR2(64)                                ,
    DSTR_YN                                                               VARCHAR2(1)                                 ,
    DSTR_DATE                                                             VARCHAR2(8)                                 ,
    DSTR_TIME                                                             VARCHAR2(6)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CLBK PRIMARY KEY (CLBK_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CLBK                                    IS '전화_콜백'                              ;
COMMENT ON COLUMN PLT_PHN_CLBK.CLBK_NO                            IS '콜백번호'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.CENT_TY                            IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.CUST_TY                            IS '고객구분'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.IN_DATE                            IS '인입일자'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.IN_TIME                            IS '인입시간'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.IN_TY                              IS '인입구분'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.SEND_MAN_NO                        IS '발신자번호'                             ;
COMMENT ON COLUMN PLT_PHN_CLBK.CUST_NM                            IS '고객명'                                 ;
COMMENT ON COLUMN PLT_PHN_CLBK.INGNO                              IS '인가번호'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.CUST_NO                            IS '고객번호'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.CLBK_TEL_NO                        IS '콜백전화번호'                           ;
COMMENT ON COLUMN PLT_PHN_CLBK.DSTR_YN                            IS '배분여부'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.DSTR_DATE                          IS '배분일자'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.DSTR_TIME                          IS '배분시간'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.REG_DTIM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.REG_MAN                            IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CLBK.CHNG_DTIM                          IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK.CHNG_MAN                           IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CLBK_DST    전화_콜백배분
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CLBK_DST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CLBK_DST (  
    CLBK_NO                                                               NUMBER(10)        NOT NULL                  ,
    DSTR_CSLT_ID                                                          VARCHAR2(20)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    TRY_CNT                                                               NUMBER(10)                                  ,
    EOT_TY                                                                VARCHAR2(10)                                ,
    PROC_CODE                                                             VARCHAR2(10)                                ,
    PROC_UNABL_CODE                                                       VARCHAR2(10)                                ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CLBK_DST PRIMARY KEY (CLBK_NO,DSTR_CSLT_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CLBK_DST                                IS '전화_콜백배분'                          ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.CLBK_NO                        IS '콜백번호'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.DSTR_CSLT_ID                   IS '배분상담사ID'                           ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.CENT_TY                        IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.TRY_CNT                        IS '시도건수'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.EOT_TY                         IS '종료구분'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.PROC_CODE                      IS '처리코드'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.PROC_UNABL_CODE                IS '처리불가코드'                           ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CLBK_DST.CHNG_MAN                       IS '변경자'                                 ;


-- 2021.05.12 삭제여부 (DEL_YN) 컬럼 추가
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL_CHG_HST    전화_상담변경이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL_CHG_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL_CHG_HST (  
    CHNG_HIST_SEQ                                                         NUMBER(10)        NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CNSL_HIST_NO                                                          VARCHAR2(30)                                ,
    CNSL_HIST_DTL_NO                                                      NUMBER(10)                                  ,
    REQ_CSLT_ID                                                           VARCHAR2(20)                                ,
    APRV_CSLT_ID                                                          VARCHAR2(20)                                ,
    CHNG_REQ_CNTN                                                         VARCHAR2(5)                                 ,
    CHNG_PROC_TY                                                          VARCHAR2(10)                                ,
    BF_CNSL_TYP_CD                                                        VARCHAR2(10)                                ,
    BF_CNSL_TYP_CD_2                                                      VARCHAR2(10)                                ,
    BF_CNSL_TYP_CD_3                                                      VARCHAR2(10)                                ,
    BF_CNSL_TYP_CD_4                                                      VARCHAR2(10)                                ,
    BF_PROC_RSLT                                                          VARCHAR2(10)                                ,
    BF_UN_PROC_RESN                                                       VARCHAR2(10)                                ,
    AF_CNSL_TYP_CD                                                        VARCHAR2(10)                                ,
    AF_CNSL_TYP_CD_2                                                      VARCHAR2(10)                                ,
    AF_CNSL_TYP_CD_3                                                      VARCHAR2(10)                                ,
    AF_CNSL_TYP_CD_4                                                      VARCHAR2(10)                                ,
    AF_PROC_RSLT                                                          VARCHAR2(10)                                ,
    AF_UN_PROC_RESN                                                       VARCHAR2(5)                                 ,
    ICDT_NO                                                               VARCHAR2(30)                                ,
    BF_SUMM                                                               VARCHAR2(200)                               ,
    BF_DTL_MATT                                                           CLOB                                        ,
    BF_SOLT                                                               CLOB                                        ,
    BF_CLAS_STRT                                                          VARCHAR2(100)                               ,
    BF_CNSL_CNTN                                                          VARCHAR2(4000)                              ,
    AF_SUMM                                                               VARCHAR2(200)                               ,
    AF_DTL_MATT                                                           CLOB                                        ,
    AF_SOLT                                                               CLOB                                        ,
    AF_CLAS_STRT                                                          VARCHAR2(100)                               ,
    AF_CNSL_CNTN                                                          VARCHAR2(4000)                              ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    DEL_YN                                                                VARCHAR2(1) DEFAULT 'N'                     ,
    CONSTRAINT XPK_PLT_PHN_CNSL_CHG_HST PRIMARY KEY (CHNG_HIST_SEQ) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL_CHG_HST                            IS '전화_상담변경이력'                      ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.CHNG_HIST_SEQ              IS '변경이력일련번호'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.CENT_TY                    IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.CNSL_HIST_NO               IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.CNSL_HIST_DTL_NO           IS '상담이력상세번호'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.REQ_CSLT_ID                IS '요청상담사ID'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.APRV_CSLT_ID               IS '승인상담사ID'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.CHNG_REQ_CNTN              IS '변경요청내용'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.CHNG_PROC_TY               IS '변경처리구분'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_CNSL_TYP_CD             IS '변경전상담유형'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_CNSL_TYP_CD_2           IS '변경전상담유형2'                        ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_CNSL_TYP_CD_3           IS '변경전상담유형3'                        ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_CNSL_TYP_CD_4           IS '변경전상담유형4'                        ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_PROC_RSLT               IS '변경전처리결과'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_UN_PROC_RESN            IS '변경전미처리사유'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_CNSL_TYP_CD             IS '변경후상담유형'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_CNSL_TYP_CD_2           IS '변경후상담유형2'                        ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_CNSL_TYP_CD_3           IS '변경후상담유형3'                        ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_CNSL_TYP_CD_4           IS '변경후상담유형4'                        ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_PROC_RSLT               IS '변경후처리결과'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_UN_PROC_RESN            IS '변경후미처리사유'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.ICDT_NO                    IS '접수번호'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_SUMM                    IS '변경전요약'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_DTL_MATT                IS '변경전세부사항'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_SOLT                    IS '변경전해결'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_CLAS_STRT               IS '변경전분류구조'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.BF_CNSL_CNTN               IS '변경전상담내용'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_SUMM                    IS '변경후요약'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_DTL_MATT                IS '변경후세부사항'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_SOLT                    IS '변경후해결'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_CLAS_STRT               IS '변경후분류구조'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.AF_CNSL_CNTN               IS '변경후상담내용'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.REG_DTIM                   IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.REG_MAN                    IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.CHNG_DTIM                  IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_CHG_HST.CHNG_MAN                   IS '변경자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL.DEL_YN                             IS '삭제여부'                           ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL    전화_상담이력마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL (  
    CNSL_HIST_NO                                                          VARCHAR2(30)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    ORGZ_ID                                                               VARCHAR2(10)                                ,
    CSLT_ID                                                               VARCHAR2(20)                                ,
    CNSL_BEGIN_DATE                                                       VARCHAR2(8)                                 ,
    CNSL_BEGIN_TIME                                                       VARCHAR2(6)                                 ,
    CNSL_EOT_DATE                                                         VARCHAR2(8)                                 ,
    CNSL_EOT_TIME                                                         VARCHAR2(6)                                 ,
    CALL_TY                                                               VARCHAR2(10)                                ,
    TEL_TIME                                                              NUMBER(20)                                  ,
    CNSL_SAVE_DTIM                                                        VARCHAR2(14)                                ,
    AF_PROC_TIME                                                          NUMBER(10)                                  ,
    CUST_TEL_NO                                                           VARCHAR2(64)                                ,
    INGNO                                                                 VARCHAR2(30)                                ,
    CU_NM                                                                 VARCHAR2(100)                               ,
    CUST_NO                                                               VARCHAR2(64)                                ,
    SECU_NO                                                               VARCHAR2(20)                                ,
    CUST_NM                                                               VARCHAR2(100)                               ,
    RDWT_SEND_YN                                                          VARCHAR2(1)                                 ,
    RDWT_ID                                                               VARCHAR2(150)                               ,
    RDWT_FILE_NM                                                          VARCHAR2(255)                               ,
    RDWT_FILE_PATH                                                        VARCHAR2(255)                               ,
    SEND_MAN_NO                                                           VARCHAR2(64)                                ,
    CALL_ID                                                               VARCHAR2(40)                                ,
    ICDT_NO                                                               VARCHAR2(30)                                ,
    CPMT_CALL_YN                                                          VARCHAR2(1)                                 ,
    TEL_TY                                                                VARCHAR2(10)                                ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    DEL_YN                                                                VARCHAR2(1)      DEFAULT 'N'                ,
    CONSTRAINT XPK_PLT_PHN_CNSL PRIMARY KEY (CNSL_HIST_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL                                    IS '전화_상담이력마스터'                    ;
COMMENT ON COLUMN PLT_PHN_CNSL.CNSL_HIST_NO                       IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL.CENT_TY                            IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.ORGZ_ID                            IS '조직ID'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL.CSLT_ID                            IS '상담사ID'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.CNSL_BEGIN_DATE                    IS '상담시작일자'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL.CNSL_BEGIN_TIME                    IS '상담시작시간'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL.CNSL_EOT_DATE                      IS '상담종료일자'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL.CNSL_EOT_TIME                      IS '상담종료시간'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL.CALL_TY                            IS '콜유형'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL.TEL_TIME                           IS '통화시간'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.CNSL_SAVE_DTIM                     IS '상담저장일시'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL.AF_PROC_TIME                       IS '후처리시간'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL.CUST_TEL_NO                        IS '고객전화번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL.INGNO                              IS '인가번호'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.CU_NM                              IS '조합명'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL.CUST_NO                            IS '고객번호'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.SECU_NO                            IS '증권번호'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.CUST_NM                            IS '고객명'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL.RDWT_SEND_YN                       IS '녹취전송여부'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL.RDWT_ID                            IS '녹취ID'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL.RDWT_FILE_NM                       IS '녹취파일명'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL.RDWT_FILE_PATH                     IS '녹취경로'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL.SEND_MAN_NO                        IS '발신자번호'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL.CALL_ID                            IS '콜ID'                                   ;
COMMENT ON COLUMN PLT_PHN_CNSL.ICDT_NO                            IS '접수번호'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.CPMT_CALL_YN                       IS '칭찬콜여부'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL.TEL_TY                             IS '전화구분'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.REG_DTIM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.REG_MAN                            IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL.CHNG_DTIM                          IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL.CHNG_MAN                           IS '변경자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL.DEL_YN                             IS '삭제여부'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL_ADD    전화_상담이력부가정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL_ADD CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL_ADD (  
    CNSL_HIST_NO                                                          VARCHAR2(30)      NOT NULL                  ,
    CNSL_HIST_ADD_NO                                                      NUMBER(10)        NOT NULL                  ,
    TRNS_YN                                                               VARCHAR2(1)                                 ,
    TRNS_CSLT_ID                                                          VARCHAR2(20)                                ,
    CAM_ID                                                                NUMBER(10)                                  ,
    QUEST_ID                                                              NUMBER(10)                                  ,
    IVR_SERV_NO                                                           VARCHAR2(20)                                ,
    TRNS_NO                                                               VARCHAR2(20)                                ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CNSL_ADD PRIMARY KEY (CNSL_HIST_NO,CNSL_HIST_ADD_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL_ADD                                IS '전화_상담이력부가정보'                  ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.CNSL_HIST_NO                   IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.CNSL_HIST_ADD_NO               IS '상담이력부가번호'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.TRNS_YN                        IS '호전환여부'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.TRNS_CSLT_ID                   IS '호전환상담사ID'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.CAM_ID                         IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.QUEST_ID                       IS '설문지ID'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.IVR_SERV_NO                    IS 'IVR서비스번호'                          ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.TRNS_NO                        IS '호전환번호'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_ADD.CHNG_MAN                       IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL_DTL    전화_상담이력상세정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL_DTL CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL_DTL (  
    CNSL_HIST_NO                                                          VARCHAR2(30)      NOT NULL                  ,
    CNSL_HIST_DTL_NO                                                      VARCHAR2(30)      NOT NULL                  ,
    CNSL_TYP_CD                                                           VARCHAR2(10)                                ,
    CNSL_TYP_CD_2                                                         VARCHAR2(10)                                ,
    CNSL_TYP_CD_3                                                         VARCHAR2(10)                                ,
    CNSL_TYP_CD_4                                                         VARCHAR2(10)                                ,
    PROC_CODE_LCLS                                                        VARCHAR2(10)                                ,
    PROC_CODE_MDCLS                                                       VARCHAR2(10)                                ,
    PROC_CODE_SCLS                                                        VARCHAR2(10)                                ,
    CNSL_CNTN                                                             VARCHAR2(4000)                              ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CNSL_DTL PRIMARY KEY (CNSL_HIST_NO,CNSL_HIST_DTL_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL_DTL                                IS '전화_상담이력상세정보'                   ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.CNSL_HIST_NO                   IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.CNSL_HIST_DTL_NO               IS '상담이력상세번호'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.CNSL_TYP_CD                    IS '상담유형코드'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.CNSL_TYP_CD_2                  IS '상담유형코드2'                          ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.CNSL_TYP_CD_3                  IS '상담유형코드3'                          ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.CNSL_TYP_CD_4                  IS '상담유형코드4'                          ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.PROC_CODE_LCLS                 IS '처리코드대분류'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.PROC_CODE_MDCLS                IS '처리코드중분류'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.PROC_CODE_SCLS                 IS '처리코드소분류'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.CNSL_CNTN                      IS '상담내용'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_DTL.CHNG_MAN                       IS '변경자'                                 ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_RESVE_SCHDUL    전화_스케줄 예약 정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_RESVE_SCHDUL CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_RESVE_SCHDUL (	
    RESV_SCHE_NO                                                          NUMBER(10)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                              , 
    CUST_TY                                                               VARCHAR2(10)                              , 
    CUST_NO                                                               VARCHAR2(64)                              , 
    CUST_NM                                                               VARCHAR2(100)                             , 
    CJOB_CSLT_ID                                                          VARCHAR2(20)                              , 
    RESV_TY_CODE                                                          VARCHAR2(10)                              , 
    RESV_DATE                                                             VARCHAR2(8)                               , 
    RESV_TIME                                                             VARCHAR2(6)                               , 
    RESV_REM                                                              VARCHAR2(4000)                            , 
    RESV_TEL_NO                                                           VARCHAR2(64)                              , 
    CNSL_HIST_NO                                                          VARCHAR2(30)                              , 
    CSLT_ALOC_DTIM                                                        VARCHAR2(14)                              , 
    LAST_CNSL_HIST_NO                                                     VARCHAR2(30)                              , 
    LAST_TEL_CSLT_ID                                                      VARCHAR2(20)                              , 
    LAST_TEL_DATE                                                         VARCHAR2(8)                               , 
    LAST_TEL_TIME                                                         VARCHAR2(6)                               , 
    LAST_TEL_RSLT_CODE                                                    VARCHAR2(10)                              , 
    PROC_STAT_CODE                                                        VARCHAR2(10)                              , 
    PROC_DTIM                                                             VARCHAR2(14)                              , 
    PROC_CSLT_ID                                                          VARCHAR2(20)                              , 
    SERV_CODE                                                             VARCHAR2(20)                              , 
    SERV_NM                                                               VARCHAR2(200)                             , 
    INDI_CORP_TY                                                          VARCHAR2(10)                              , 
    IVR_DN_NO                                                             VARCHAR2(16)                              , 
    TRX_NO                                                                NUMBER(9)                                 , 
    CLBK_TY                                                               VARCHAR2(10)                              , 
    REG_DTIM                                                              VARCHAR2(14)                              , 
    REG_MAN                                                               VARCHAR2(20)                              , 
    CHNG_DTIM                                                             VARCHAR2(14)                              , 
    CHNG_MAN                                                              VARCHAR2(20)                              ,
    CONSTRAINT XPK_PLT_PHN_RESVE_SCHDUL PRIMARY KEY (RESV_SCHE_NO) 
  ) 
  --TABLESPACE TS_PALETTE
  ;

  COMMENT ON TABLE  PLT_PHN_RESVE_SCHDUL                                IS '전화_스케줄예약정보'                   ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.RESV_SCHE_NO                   IS '예약일정번호'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CENT_TY                        IS '센터구분'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CUST_TY                        IS '고객구분'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CUST_NO                        IS '고객번호'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CUST_NM                        IS '고객명'                               ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CJOB_CSLT_ID                   IS '담당상담사ID'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.RESV_TY_CODE                   IS '예약유형코드'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.RESV_DATE                      IS '예약일자'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.RESV_TIME                      IS '예약시간'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.RESV_REM                       IS '예약비고'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.RESV_TEL_NO                    IS '예약전화번호'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CNSL_HIST_NO                   IS '상담이력번호'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CSLT_ALOC_DTIM                 IS '상담사할당일시'                       ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.LAST_CNSL_HIST_NO              IS '최종상담이력번호'                     ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.LAST_TEL_CSLT_ID               IS '최종통화상담사ID'                     ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.LAST_TEL_DATE                  IS '최종통화일자'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.LAST_TEL_TIME                  IS '최종통화시간'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.LAST_TEL_RSLT_CODE             IS '최종통화결과코드'                     ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.PROC_STAT_CODE                 IS '처리상태코드'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.PROC_DTIM                      IS '처리일시'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.RROC_CSLT_ID                   IS '처리상담사ID'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.SERV_CODE                      IS '서비스코드'                           ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.SERV_NM                        IS '서비스명'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.INDI_CORP_TY                   IS '개인법인구분'                         ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.IVR_DN_NO                      IS 'IVR DN번호'                          ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.TRX_NO                         IS '비대면거래번호'                       ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CLBK_TY                        IS '콜백구분'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.REG_DTIM                       IS '등록일시'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.REG_MAN                        IS '등록자'                               ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CHNG_DTIM                      IS '변경일시'                             ;
  COMMENT ON COLUMN PLT_PHN_RESVE_SCHDUL.CHNG_MAN                       IS '변경자'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_OBD_DCLZ    전화_아웃바운드근태관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP TABLE PLT_PHN_OBD_DCLZ CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_OBD_DCLZ (
  BASE_DATE                                                               VARCHAR2(8) NOT NULL                        ,
  CSLT_ID                                                                 VARCHAR2(20) NOT NULL                       ,
  ANNL_YN                                                                 VARCHAR2(1) DEFAULT 'N'                     , 
  HALFD_YN                                                                VARCHAR2(1) DEFAULT 'N'                     , 
  REG_DTIM                                                                VARCHAR2(14)                                , 
  REG_MAN                                                                 VARCHAR2(20)                                , 
  CHNG_DTIM                                                               VARCHAR2(14)                                , 
  CHNG_MAN                                                                VARCHAR2(20)                                , 
  CONSTRAINT XPK_PLT_PHN_OBD_DCLZ PRIMARY KEY (BASE_DATE, CSLT_ID)
) ;


COMMENT ON TABLE PLT_PHN_OBD_DCLZ                                        IS '전화_아웃바운드근태관리'                ;
COMMENT ON COLUMN PLT_PHN_OBD_DCLZ.BASE_DATE                             IS '기준일자'                              ;
COMMENT ON COLUMN PLT_PHN_OBD_DCLZ.CSLT_ID                               IS '상담사ID'                              ;
COMMENT ON COLUMN PLT_PHN_OBD_DCLZ.ANNL_YN                               IS '연차여부'                              ;
COMMENT ON COLUMN PLT_PHN_OBD_DCLZ.HALFD_YN                              IS '반차여부'                              ;
COMMENT ON COLUMN PLT_PHN_OBD_DCLZ.REG_DTIM                              IS '등록일시'                              ;
COMMENT ON COLUMN PLT_PHN_OBD_DCLZ.REG_MAN                               IS '등록자'                                ;
COMMENT ON COLUMN PLT_PHN_OBD_DCLZ.CHNG_DTIM                             IS '변경일시'                              ;
COMMENT ON COLUMN PLT_PHN_OBD_DCLZ.CHNG_MAN                              IS '변경자'                                ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL_PRT_HST    전화_상담정보출력이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL_PRT_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL_PRT_HST (  
    PRT_HIST_SEQ                                                          NUMBER(10)        NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CNSL_HIST_NO                                                          VARCHAR2(30)                                ,
    CNSL_HIST_DTL_NO                                                      NUMBER(10)                                  ,
    PRT_DATE                                                              VARCHAR2(8)                                 ,
    PRT_TIME                                                              VARCHAR2(6)                                 ,
    PRT_RESN                                                              VARCHAR2(10)                                ,
    PRT_CSLT_ID                                                           VARCHAR2(20)                                ,
    PRT_CSLT_NM                                                           VARCHAR2(20)                                ,
    PRT_REM                                                               VARCHAR2(500)                               ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CNSL_PRT_HST PRIMARY KEY (PRT_HIST_SEQ) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL_PRT_HST                            IS '전화_상담정보출력이력'                  ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.PRT_HIST_SEQ               IS '출력이력일련번호'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.CENT_TY                    IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.CNSL_HIST_NO               IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.CNSL_HIST_DTL_NO           IS '상담이력상세번호'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.PRT_DATE                   IS '출력일자'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.PRT_TIME                   IS '출력시간'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.PRT_RESN                   IS '출력사유'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.PRT_CSLT_ID                IS '출력상담사ID'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.PRT_CSLT_NM                IS '출력상담사명'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.PRT_REM                    IS '출력비고'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.REG_DTIM                   IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.REG_MAN                    IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.CHNG_DTIM                  IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_PRT_HST.CHNG_MAN                   IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL_RSV    전화_상담예약
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL_RSV CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL_RSV (  
    RESV_SEQ                                                              VARCHAR2(20)      NOT NULL                  ,
    CNSL_HIST_NO                                                          VARCHAR2(30)                                ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CUST_TY                                                               VARCHAR2(10)                                ,
    RESV_DATE                                                             VARCHAR2(8)                                 ,
    RESV_TIME                                                             VARCHAR2(6)                                 ,
    RESV_TEL_NO                                                           VARCHAR2(64)                                ,
    FISH_YN                                                               VARCHAR2(1)                                 ,
    TRY_CNT                                                               NUMBER(10)                                  ,
    PROC_DATE                                                             VARCHAR2(8)                                 ,
    PROC_TIME                                                             VARCHAR2(6)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CNSL_RSV PRIMARY KEY (RESV_SEQ) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL_RSV                                IS '전화_상담예약'                          ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.RESV_SEQ                       IS '예약일련번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.CNSL_HIST_NO                   IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.CENT_TY                        IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.CUST_TY                        IS '고객구분'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.RESV_DATE                      IS '예약일자'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.RESV_TIME                      IS '예약시간'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.RESV_TEL_NO                    IS '예약전화번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.FISH_YN                        IS '완료여부'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.TRY_CNT                        IS '시도건수'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.PROC_DATE                      IS '처리일자'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.PROC_TIME                      IS '처리시간'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_RSV.CHNG_MAN                       IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL_SR    전화_상담SR
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL_SR CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL_SR (  
    CNSL_HIST_NO                                                          VARCHAR2(30)      NOT NULL                  ,
    CNSL_HIST_DTL_NO                                                      NUMBER(10)        NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)      NOT NULL                  ,
    SR_NO                                                                 VARCHAR2(20)      NOT NULL                  ,
    ORG_SR_NO                                                             VARCHAR2(20)                                ,
    REQ_TY                                                                VARCHAR2(10)                                ,
    REQ_USER_ID                                                           VARCHAR2(20)                                ,
    SR_TIT                                                                VARCHAR2(300)                               ,
    SR_CNTN                                                               VARCHAR2(4000)                              ,
    HOPE_ACTN_BEGIN_DATE                                                  VARCHAR2(8)                                 ,
    HOPE_ACTN_EOT_DATE                                                    VARCHAR2(8)                                 ,
    FILE_GROUP_KEY                                                        VARCHAR2(30)                                ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CNSL_SR PRIMARY KEY (CNSL_HIST_NO,CNSL_HIST_DTL_NO,CENT_TY,SR_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL_SR                                 IS '전화_상담SR'                            ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.CNSL_HIST_NO                    IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.CNSL_HIST_DTL_NO                IS '상담이력상세번호'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.CENT_TY                         IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.SR_NO                           IS 'SR번호'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.ORG_SR_NO                       IS '원SR번호'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.REQ_TY                          IS '요청유형'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.REQ_USER_ID                     IS '요청사용자ID'                           ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.SR_TIT                          IS 'SR제목'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.SR_CNTN                         IS 'SR내용'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.HOPE_ACTN_BEGIN_DATE            IS '희망조치시작일자'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.HOPE_ACTN_EOT_DATE              IS '희망조치종료일자'                       ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.FILE_GROUP_KEY                  IS '파일그룹키'                             ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.REG_DTIM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.REG_MAN                         IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.CHNG_DTIM                       IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_SR.CHNG_MAN                        IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL_TYP_LCLS    전화_상담유형대분류
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL_TYP_LCLS CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL_TYP_LCLS (  
    CNSL_TY_LCLS                                                          VARCHAR2(10)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CLAS_NM                                                               VARCHAR2(75)                                ,
    CNSL_TY_CODE                                                          VARCHAR2(12)                                ,
    USE_YN                                                                VARCHAR2(1)                                 ,
    SORT_SEQ                                                              NUMBER(5)                                   ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CNSL_TYP_LCLS PRIMARY KEY (CNSL_TY_LCLS) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL_TYP_LCLS                               IS '전화_상담유형대분류'                    ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.CNSL_TY_LCLS                  IS '상담유형대분류'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.CENT_TY                       IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.CLAS_NM                       IS '분류명'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.CNSL_TY_CODE                  IS '상담타입코드(참고)'                     ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.USE_YN                        IS '사용여부'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.SORT_SEQ                      IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.REG_DTIM                      IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.REG_MAN                       IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.CHNG_DTIM                     IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_LCLS.CHNG_MAN                      IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL_TYP_MCLS    전화_상담유형중분류
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL_TYP_MCLS CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL_TYP_MCLS (  
    CNSL_TY_LCLS                                                          VARCHAR2(10)      NOT NULL                  ,
    CNSL_TY_MDCLS                                                         VARCHAR2(10)      NOT NULL                  ,
    CLAS_NM                                                               VARCHAR2(75)                                ,
    CNSL_TY_CODE                                                          VARCHAR2(12)                                ,
    USE_YN                                                                VARCHAR2(1)                                 ,
    SORT_SEQ                                                              NUMBER(5)                                   ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CNSL_TYP_MCLS PRIMARY KEY (CNSL_TY_LCLS,CNSL_TY_MDCLS) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL_TYP_MCLS                               IS '전화_상담유형중분류'                    ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.CNSL_TY_LCLS                  IS '상담유형대분류'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.CNSL_TY_MDCLS                 IS '상담유형중분류'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.CLAS_NM                       IS '분류명'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.CNSL_TY_CODE                  IS '상담타입코드(참고)'                     ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.USE_YN                        IS '사용여부'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.SORT_SEQ                      IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.REG_DTIM                      IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.REG_MAN                       IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.CHNG_DTIM                     IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_MCLS.CHNG_MAN                      IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CNSL_TYP_SCLS    전화_상담유형소분류
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CNSL_TYP_SCLS CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CNSL_TYP_SCLS (  
    CNSL_TY_LCLS                                                          VARCHAR2(10)      NOT NULL                  ,
    CNSL_TY_MDCLS                                                         VARCHAR2(10)      NOT NULL                  ,
    CNSL_TY_SCLS                                                          VARCHAR2(10)      NOT NULL                  ,
    CLAS_NM                                                               VARCHAR2(75)                                ,
    CNSL_TY_CODE                                                          VARCHAR2(12)                                ,
    USE_YN                                                                VARCHAR2(1)                                 ,
    SORT_SEQ                                                              NUMBER(5)                                   ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CNSL_TYP_SCLS PRIMARY KEY (CNSL_TY_LCLS,CNSL_TY_MDCLS,CNSL_TY_SCLS) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CNSL_TYP_SCLS                               IS '전화_상담유형소분류'                    ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.CNSL_TY_LCLS                  IS '상담유형대분류'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.CNSL_TY_MDCLS                 IS '상담유형중분류'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.CNSL_TY_SCLS                  IS '상담유형소분류'                         ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.CLAS_NM                       IS '분류명'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.CNSL_TY_CODE                  IS '상담타입코드(참고)'                     ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.USE_YN                        IS '사용여부'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.SORT_SEQ                      IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.REG_DTIM                      IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.REG_MAN                       IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.CHNG_DTIM                     IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CNSL_TYP_SCLS.CHNG_MAN                      IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_CMPT_CALL    전화_칭찬콜
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_CMPT_CALL CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_CMPT_CALL (  
    CPMT_CALL_NO                                                          NUMBER(10)        NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CNSL_HIST_NO                                                          VARCHAR2(30)                                ,
    CSLT_ID                                                               VARCHAR2(20)                                ,
    RDWT_ID                                                               VARCHAR2(150)                               ,
    RDWT_FILE_NM                                                          VARCHAR2(255)                               ,
    CAM_ID                                                                NUMBER(10)                                  ,
    CPMT_CALL_REM                                                         VARCHAR2(500)                               ,
    CPMT_CALL_APRV_RSLT                                                   VARCHAR2(10)                                ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_CMPT_CALL PRIMARY KEY (CPMT_CALL_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_CMPT_CALL                               IS '전화_칭찬콜'                            ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.CPMT_CALL_NO                  IS '칭찬콜번호'                             ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.CENT_TY                       IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.CNSL_HIST_NO                  IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.CSLT_ID                       IS '상담사ID'                               ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.RDWT_ID                       IS '녹취ID'                                 ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.RDWT_FILE_NM                  IS '녹취파일명'                             ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.CAM_ID                        IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.CPMT_CALL_REM                 IS '칭찬콜비고'                             ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.CPMT_CALL_APRV_RSLT           IS '칭찬콜승인결과'                         ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.REG_DTIM                      IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.REG_MAN                       IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.CHNG_DTIM                     IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_CMPT_CALL.CHNG_MAN                      IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_IP_INLN    전화_IP내선관리
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_IP_INLN CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_IP_INLN (  
    IP_ADDR                                                               VARCHAR2(15)      NOT NULL                  ,
    INLNE_NO                                                              VARCHAR2(20)                                ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    USER_ID                                                               VARCHAR2(20)                                ,
    REM                                                                   VARCHAR2(500)                               ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_IP_INLN PRIMARY KEY (IP_ADDR) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_IP_INLN                                 IS '전화_IP내선관리'                        ;
COMMENT ON COLUMN PLT_PHN_IP_INLN.IP_ADDR                         IS 'IP주소'                                 ;
COMMENT ON COLUMN PLT_PHN_IP_INLN.INLNE_NO                        IS '내선번호'                               ;
COMMENT ON COLUMN PLT_PHN_IP_INLN.CENT_TY                         IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_IP_INLN.USER_ID                         IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_PHN_IP_INLN.REM                             IS '비고'                                   ;
COMMENT ON COLUMN PLT_PHN_IP_INLN.REG_DTIM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_IP_INLN.REG_MAN                         IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_IP_INLN.CHNG_DTIM                       IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_IP_INLN.CHNG_MAN                        IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_IVR_HST    전화_IVR이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_IVR_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_IVR_HST (  
    CNSL_HIST_NO                                                          VARCHAR2(30)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)      NOT NULL                  ,
    RDWT_ID                                                               VARCHAR2(150)                               ,
    CALL_ID                                                               VARCHAR2(40)                                ,
    CNSL_DATE                                                             VARCHAR2(8)                                 ,
    CNSL_TIME                                                             VARCHAR2(6)                                 ,
    IVR_IN_NO                                                             VARCHAR2(64)                                ,
    IVR_IN_PATH1                                                          VARCHAR2(100)                               ,
    IVR_IN_PATH2                                                          VARCHAR2(100)                               ,
    IVR_IN_PATH3                                                          VARCHAR2(100)                               ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_IVR_HST PRIMARY KEY (CNSL_HIST_NO,CENT_TY) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_IVR_HST                                 IS '전화_IVR이력'                           ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.CNSL_HIST_NO                    IS '상담이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.CENT_TY                         IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.RDWT_ID                         IS '녹취ID'                                 ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.CALL_ID                         IS '콜ID'                                   ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.CNSL_DATE                       IS '상담일자'                               ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.CNSL_TIME                       IS '상담시간'                               ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.IVR_IN_NO                       IS 'IVR인입번호'                            ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.IVR_IN_PATH1                    IS 'IVR인입경로1'                           ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.IVR_IN_PATH2                    IS 'IVR인입경로2'                           ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.IVR_IN_PATH3                    IS 'IVR인입경로3'                           ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.REG_DTIM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.REG_MAN                         IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.CHNG_DTIM                       IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_IVR_HST.CHNG_MAN                        IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_OBD_CUS    전화_아웃바운드고객
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_OBD_CUS CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_OBD_CUS (  
    CUST_NO                                                               VARCHAR2(64)                                ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CUST_TY                                                               VARCHAR2(10)                                ,
    INGNO                                                                 VARCHAR2(5)                                 ,
    MOBIL_NO                                                              VARCHAR2(64)                                ,
    HOUSE_TEL_NO                                                          VARCHAR2(64)                                ,
    CO_TEL_NO                                                             VARCHAR2(64)                                ,
    TEL_TY                                                                VARCHAR2(10)                                ,
    IN_DATE                                                               VARCHAR2(8)                                 ,
    UPDT_DATE                                                             VARCHAR2(8)                                 ,
    CUST_NM                                                               VARCHAR2(100)                               ,
    REM                                                                   VARCHAR2(500)                               ,
    CAM_ID                                                                NUMBER(10)                                  ,
    CAM_TY                                                                VARCHAR2(10)                                ,
    IFLW_TY                                                               VARCHAR2(10)                                ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_OBD_CUS                                 IS '전화_아웃바운드고객'                    ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.CUST_NO                         IS '고객번호'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.CENT_TY                         IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.CUST_TY                         IS '고객구분'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.INGNO                           IS '인가번호'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.MOBIL_NO                        IS '핸드폰번호'                             ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.HOUSE_TEL_NO                    IS '집전화번호'                             ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.CO_TEL_NO                       IS '회사전화번호'                           ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.TEL_TY                          IS '전화구분'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.IN_DATE                         IS '인입일자'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.UPDT_DATE                       IS '갱신일자'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.CUST_NM                         IS '고객명'                                 ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.REM                             IS '비고'                                   ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.CAM_ID                          IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.CAM_TY                          IS '캠페인구분'                             ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.IFLW_TY                         IS '유입구분'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.REG_DTIM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.REG_MAN                         IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.CHNG_DTIM                       IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS.CHNG_MAN                        IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_OBD_CUS_ADD    전화_아웃바운드고객부가
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_OBD_CUS_ADD CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_OBD_CUS_ADD (  
    CAM_ID                                                                NUMBER(10)                                  ,
    CUST_NO                                                               VARCHAR2(64)                                ,
    CAM_TY                                                                VARCHAR2(10)                                ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    SECU_NO                                                               VARCHAR2(20)                                ,
    JOB_DTL_NM                                                            VARCHAR2(100)                               ,
    SALE_START_DATE                                                       VARCHAR2(8)                                 ,
    UPDT_DATE                                                             VARCHAR2(8)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_OBD_CUS_ADD                             IS '전화_아웃바운드고객부가'                ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.CAM_ID                      IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.CUST_NO                     IS '고객번호'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.CAM_TY                      IS '캠페인구분'                             ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.CENT_TY                     IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.SECU_NO                     IS '증권번호'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.JOB_DTL_NM                  IS '직업상세명'                             ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.SALE_START_DATE             IS '판매개시일자'                           ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.UPDT_DATE                   IS '갱신일자'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.REG_DTIM                    IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.REG_MAN                     IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.CHNG_DTIM                   IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_OBD_CUS_ADD.CHNG_MAN                    IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_SURV    전화_설문지마스터
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_SURV CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_SURV (  
    QUEST_ID                                                              NUMBER(10)        NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    QUEST_NM                                                              VARCHAR2(50)                                ,
    QUEST_CNTN                                                            VARCHAR2(300)                               ,
    REM                                                                   VARCHAR2(500)                               ,
    USE_YN                                                                VARCHAR2(1)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_SURV PRIMARY KEY (QUEST_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_SURV                                    IS '전화_설문지마스터'                      ;
COMMENT ON COLUMN PLT_PHN_SURV.QUEST_ID                           IS '설문지ID'                               ;
COMMENT ON COLUMN PLT_PHN_SURV.CENT_TY                            IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_SURV.QUEST_NM                           IS '설문지명'                               ;
COMMENT ON COLUMN PLT_PHN_SURV.QUEST_CNTN                         IS '설문지내용'                             ;
COMMENT ON COLUMN PLT_PHN_SURV.REM                                IS '비고'                                   ;
COMMENT ON COLUMN PLT_PHN_SURV.USE_YN                             IS '사용여부'                               ;
COMMENT ON COLUMN PLT_PHN_SURV.REG_DTIM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_SURV.REG_MAN                            IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_SURV.CHNG_DTIM                          IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_SURV.CHNG_MAN                           IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_SURV_ANS    전화_설문지답안
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_SURV_ANS CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_SURV_ANS (  
    QUEST_ID                                                              NUMBER(10)        NOT NULL                  ,
    Q_NO                                                                  NUMBER(10)        NOT NULL                  ,
    ANS_NO                                                                NUMBER(10)        NOT NULL                  ,
    ANS_CNTN                                                              VARCHAR2(200)                               ,
    NEXT_Q_NO                                                             NUMBER(10)                                  ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_SURV_ANS PRIMARY KEY (QUEST_ID,Q_NO,ANS_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_SURV_ANS                                IS '전화_설문지답안'                        ;
COMMENT ON COLUMN PLT_PHN_SURV_ANS.QUEST_ID                       IS '설문지ID'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_ANS.Q_NO                           IS '문항번호'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_ANS.ANS_NO                         IS '답안번호'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_ANS.ANS_CNTN                       IS '답안내용'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_ANS.NEXT_Q_NO                      IS '다음문항번호'                           ;
COMMENT ON COLUMN PLT_PHN_SURV_ANS.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_ANS.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_SURV_ANS.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_ANS.CHNG_MAN                       IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_SURV_QST    전화_설문지문항
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_SURV_QST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_SURV_QST (  
    QUEST_ID                                                              NUMBER(10)        NOT NULL                  ,
    Q_NO                                                                  NUMBER(10)        NOT NULL                  ,
    Q_CNTN                                                                VARCHAR2(200)                               ,
    ANS_FORM                                                              VARCHAR2(10)                                ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_SURV_QST PRIMARY KEY (QUEST_ID,Q_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_SURV_QST                                IS '전화_설문지문항'                        ;
COMMENT ON COLUMN PLT_PHN_SURV_QST.QUEST_ID                       IS '설문지ID'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_QST.Q_NO                           IS '문항번호'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_QST.Q_CNTN                         IS '문항내용'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_QST.ANS_FORM                       IS '답안형태'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_QST.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_QST.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_SURV_QST.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_QST.CHNG_MAN                       IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_SURV_RST    전화_설문지결과
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_SURV_RST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_SURV_RST (  
    QUEST_ID                                                              NUMBER(10)        NOT NULL                  ,
    Q_NO                                                                  NUMBER(10)        NOT NULL                  ,
    CUST_NO                                                               VARCHAR2(64)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CAM_ID                                                                NUMBER(10)                                  ,
    CUST_TY                                                               VARCHAR2(10)                                ,
    OBJ_RESP                                                              NUMBER(10)                                  ,
    SBJ_RESP                                                              VARCHAR2(300)                               ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_SURV_RST PRIMARY KEY (QUEST_ID,Q_NO,CUST_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_SURV_RST                                IS '전화_설문지결과'                        ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.QUEST_ID                       IS '설문지ID'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.Q_NO                           IS '문항번호'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.CUST_NO                        IS '고객번호'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.CENT_TY                        IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.CAM_ID                         IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.CUST_TY                        IS '고객구분'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.OBJ_RESP                       IS '객관식응답'                             ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.SBJ_RESP                       IS '주관식응답'                             ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_SURV_RST.CHNG_MAN                       IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_SCRT    전화_스크립트
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_SCRT CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_SCRT (  
    SCRIPT_ID                                                             VARCHAR2(30)      NOT NULL                  ,
    UPPER_SCRIPT_ID                                                       VARCHAR2(30)                                ,
    LVL_NO                                                                NUMBER(3)                                   ,
    SCRIPT_TIT                                                            VARCHAR2(200)                               ,
    SCRIPT_RMK_NO                                                         VARCHAR2(25)                                ,
    SCRIPT_RMK                                                            CLOB                                        ,
    FILE_GROUP_KEY                                                        VARCHAR2(90)                                ,
    FST_USER_ID                                                           VARCHAR2(20)                                ,
    FST_SCRIPT_DT                                                         DATE                                        ,
    LAST_USER_ID                                                          VARCHAR2(20)                                ,
    LAST_SCRIPT_DT                                                        DATE                                        ,
    ACCESS_IP                                                             VARCHAR2(20)                                ,
    SELECT_NO                                                             NUMBER(10)                                  ,
    ORD_SEQ                                                               NUMBER(5)                                   ,
    USE_YN                                                                CHAR(1)                                     ,
    SCRIPT_AUTH_TYPE                                                      VARCHAR2(10)                                ,
    INQRY_TYP_CD                                                          VARCHAR2(40)                                ,
    INQRY_TYP_CD_2                                                        VARCHAR2(40)                                ,
    PROC_ID                                                               VARCHAR2(20)                                ,
    IT_PROCESSING                                                         TIMESTAMP                                   ,
    CNSL_TYP_CD                                                           VARCHAR2(20)                                ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    BEGIN_DATE                                                            VARCHAR2(8)                                 ,
    EOT_DATE                                                              VARCHAR2(8)                                 ,
    CONSTRAINT XPK_PLT_PHN_SCRT PRIMARY KEY (SCRIPT_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_SCRT                                    IS '전화_스크립트'                          ;
COMMENT ON COLUMN PLT_PHN_SCRT.SCRIPT_ID                          IS '스크립트ID'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT.UPPER_SCRIPT_ID                    IS '상위스크립트ID'                         ;
COMMENT ON COLUMN PLT_PHN_SCRT.LVL_NO                             IS '스크립트레벨'                           ;
COMMENT ON COLUMN PLT_PHN_SCRT.SCRIPT_TIT                         IS '스크립트제목'                           ;
COMMENT ON COLUMN PLT_PHN_SCRT.SCRIPT_RMK_NO                      IS '스크립트내용번호'                       ;
COMMENT ON COLUMN PLT_PHN_SCRT.SCRIPT_RMK                         IS '스크립트내용'                           ;
COMMENT ON COLUMN PLT_PHN_SCRT.FILE_GROUP_KEY                     IS '파일그룹키'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT.FST_USER_ID                        IS '최초등록자ID'                           ;
COMMENT ON COLUMN PLT_PHN_SCRT.FST_SCRIPT_DT                      IS '최초등록일'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT.LAST_USER_ID                       IS '마지막등록자ID'                         ;
COMMENT ON COLUMN PLT_PHN_SCRT.LAST_SCRIPT_DT                     IS '마지막등록일'                           ;
COMMENT ON COLUMN PLT_PHN_SCRT.ACCESS_IP                          IS '등록자IP'                               ;
COMMENT ON COLUMN PLT_PHN_SCRT.SELECT_NO                          IS '조회수'                                 ;
COMMENT ON COLUMN PLT_PHN_SCRT.ORD_SEQ                            IS '정렬순서'                               ;
COMMENT ON COLUMN PLT_PHN_SCRT.USE_YN                             IS '사용여부'                               ;
COMMENT ON COLUMN PLT_PHN_SCRT.SCRIPT_AUTH_TYPE                   IS '스크립트권한타입'                       ;
COMMENT ON COLUMN PLT_PHN_SCRT.INQRY_TYP_CD                       IS '대분류유형'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT.INQRY_TYP_CD_2                     IS '중분류유형'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT.PROC_ID                            IS '처리자'                                 ;
COMMENT ON COLUMN PLT_PHN_SCRT.IT_PROCESSING                      IS '전산처리일시'                           ;
COMMENT ON COLUMN PLT_PHN_SCRT.CNSL_TYP_CD                        IS '상담유형코드'                           ;
COMMENT ON COLUMN PLT_PHN_SCRT.CENT_TY                            IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_SCRT.BEGIN_DATE                         IS '시작일자'                               ;
COMMENT ON COLUMN PLT_PHN_SCRT.EOT_DATE                           IS '종료일자'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_SCRT_CHG_HST    전화_스크립트이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_SCRT_CHG_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_SCRT_CHG_HST (  
    CHNG_HIST_NO                                                          NUMBER(10)        NOT NULL                  ,
    REQ_USER_ID                                                           VARCHAR2(20)                                ,
    SCRIPT_ID                                                             VARCHAR2(30)                                ,
    CHNG_BF_TIT                                                           VARCHAR2(200)                               ,
    CHNG_BF_CNTN                                                          CLOB                                        ,
    CHNG_AF_TIT                                                           VARCHAR2(200)                               ,
    CHNG_AF_CNTN                                                          CLOB                                        ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_SCRT_CHG_HST PRIMARY KEY (CHNG_HIST_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_SCRT_CHG_HST                            IS '전화_스크립트이력'                      ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.CHNG_HIST_NO               IS '변경이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.REQ_USER_ID                IS '요청사용자ID'                           ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.SCRIPT_ID                  IS '스크립트ID'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.CHNG_BF_TIT                IS '변경전제목'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.CHNG_BF_CNTN               IS '변경전내용'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.CHNG_AF_TIT                IS '변경후제목'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.CHNG_AF_CNTN               IS '변경후내용'                             ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.REG_DTIM                   IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.REG_MAN                    IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.CHNG_DTIM                  IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_SCRT_CHG_HST.CHNG_MAN                   IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_STS_CALL_TY    전화_인입콜유형통계
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  DROP   TABLE PLT_PHN_STS_CALL_TY CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_STS_CALL_TY (	
    CNSL_DATE                                                             VARCHAR2(8)        NOT NULL                , 
    CNSL_TIME                                                             VARCHAR2(2)        NOT NULL                , 
    USER_ID                                                               VARCHAR2(20)       NOT NULL                , 
    TEL_TY_CODE                                                           VARCHAR2(10)       NOT NULL                , 
    CNSL_TY_CODE                                                          VARCHAR2(10)       NOT NULL                , 
    VER_NO                                                                NUMBER(3,2)        NOT NULL                , 
    CENT_TY                                                               VARCHAR2(10)                               , 
    CNSL_TY_LCLS                                                          VARCHAR2(10)                               , 
    CNSL_TY_MDCLS                                                         VARCHAR2(10)                               , 
    CNSL_TY_SCLS                                                          VARCHAR2(10)                               , 
    ORGZ_ID                                                               VARCHAR2(10)                               , 
    CNT                                                                   NUMBER(10,0)                               , 
    REG_DTIM                                                              VARCHAR2(14)                               , 
    REG_MAN                                                               VARCHAR2(20)                               , 
    CHNG_DTIM                                                             VARCHAR2(14)                               , 
    CHNG_MAN                                                              VARCHAR2(20)                               ,
    CONSTRAINT XPK_PLT_PHN_STS_CALL_TY PRIMARY KEY (CNSL_DATE, CNSL_TIME, USER_ID, TEL_TY_CODE, CNSL_TY_CODE, VER_NO)
) 
--TABLESPACE TS_PALETTE
;
 
   COMMENT ON TABLE PLT_PHN_STS_CALL_TY                           IS '전화_인입콜유형통계'                      ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CNSL_DATE                IS '상담일자'                                ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CNSL_TIME                IS '상담시간'                                ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.USER_ID                  IS '사용자ID'                                ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.TEL_TY_CODE              IS '통화유형코드'                            ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CNSL_TY_CODE             IS '상담유형코드'                            ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.VER_NO                   IS '버전번호'                                ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CENT_TY                  IS '센터구분'                                ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CNSL_TY_LCLS             IS '상담유형대분류'                           ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CNSL_TY_MDCLS            IS '상담유형중분류'                           ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CNSL_TY_SCLS             IS '상담유형소분류'                           ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.ORGZ_ID                  IS '조직ID'                                  ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CNT                      IS '건수'                                    ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.REG_DTIM                 IS '등록일시'                                ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.REG_MAN                  IS '등록자'                                  ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CHNG_DTIM                IS '변경일시'                                ;
   COMMENT ON COLUMN PLT_PHN_STS_CALL_TY.CHNG_MAN                 IS '변경자'                                  ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_QA_EVAL_RST    전화_QA결과
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_QA_EVAL_RST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_QA_EVAL_RST (  
    QA_ID                                                                 NUMBER(10)        NOT NULL                  ,
    QA_YM                                                                 VARCHAR2(6)       NOT NULL                  ,
    QA_SEQ                                                                NUMBER(3)         NOT NULL                  ,
    USER_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    CNSL_ID                                                               NUMBER(10)        NOT NULL                  ,
    QA_TY_ID                                                              VARCHAR2(30)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(20)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)      NOT NULL                  ,
    SCORE_CHK                                                             VARCHAR2(1)                                 ,
    EVAL_CN                                                               VARCHAR2(600)                               ,
    REG_DTIM                                                              VARCHAR2(14)      NOT NULL                  ,
    REG_ID                                                                VARCHAR2(20)      NOT NULL                  ,
    CHNG_DTIM                                                             VARCHAR2(14)      NOT NULL                  ,
    CHNG_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    CONSTRAINT XPK_PLT_PHN_QA_EVAL_RST PRIMARY KEY (QA_ID,QA_YM,QA_SEQ,USER_ID,CNSL_ID,QA_TY_ID,CUSTCO_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_QA_EVAL_RST                             IS '전화_QA결과'                            ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.QA_ID                       IS 'QA_ID'                                  ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.QA_YM                       IS 'QA_년월'                                ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.QA_SEQ                      IS 'QA회차'                                 ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.USER_ID                     IS '상담원ID'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.CNSL_ID                     IS '상담ID'                                 ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.QA_TY_ID                    IS 'QA_타입ID'                              ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.CUSTCO_ID                IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.CENT_TY                     IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.SCORE_CHK                   IS '점수체크'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.EVAL_CN                     IS '평가COMMENT'                            ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.REG_DTIM                    IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.REG_ID                      IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.CHNG_DTIM                   IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_RST.CHNG_ID                     IS '변경자ID'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_QA_EVAL    전화_QA평가
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_QA_EVAL CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_QA_EVAL (  
    QA_ID                                                                 NUMBER(10)        NOT NULL                  ,
    QA_YM                                                                 VARCHAR2(6)       NOT NULL                  ,
    QA_SEQ                                                                NUMBER(3)         NOT NULL                  ,
    QA_TY_CD                                                              VARCHAR2(10)      NOT NULL                  ,
    USER_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    CNSL_ID                                                               NUMBER(10)        NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(20)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)      NOT NULL                  ,
    QA_USER_ID                                                            VARCHAR2(20)                                ,
    QA_CN                                                                 VARCHAR2(600)                               ,
    QA_EXT_CHK                                                            VARCHAR2(1)                                 ,
    QA_END                                                                VARCHAR2(10)                                ,
    QA_NOTIN                                                              VARCHAR2(10)                                ,
    QA_FIN                                                                VARCHAR2(10)                                ,
    REG_DTIM                                                              VARCHAR2(14)      NOT NULL                  ,
    REG_ID                                                                VARCHAR2(20)      NOT NULL                  ,
    CHNG_DTIM                                                             VARCHAR2(14)      NOT NULL                  ,
    CHNG_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    CONSTRAINT XPK_PLT_PHN_QA_EVAL PRIMARY KEY (QA_ID,QA_YM,QA_SEQ,USER_ID,CNSL_ID,CUSTCO_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_QA_EVAL                                 IS '전화_QA평가'                            ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_ID                           IS 'QA_ID'                                  ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_YM                           IS 'QA_년월'                                ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_SEQ                          IS 'QA회차'                                 ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_TY_CD                        IS 'QA유형코드'                             ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.USER_ID                         IS '상담원ID'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.CNSL_ID                         IS '상담ID'                                 ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.CUSTCO_ID                    IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.CENT_TY                         IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_USER_ID                      IS '평가자ID'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_CN                           IS 'QA의견'                                 ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_EXT_CHK                      IS 'QA추출대상'                             ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_END                          IS 'QA추출마감'                             ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_NOTIN                        IS 'QA제외대상'                             ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.QA_FIN                          IS 'QA평가마감'                             ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.REG_DTIM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.REG_ID                          IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.CHNG_DTIM                       IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL.CHNG_ID                         IS '변경자ID'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_QA_EVAL_SHT    전화_QA평가지
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_QA_EVAL_SHT CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_QA_EVAL_SHT (  
    QA_ID                                                                 NUMBER(10)        NOT NULL                  ,
    QA_YM                                                                 VARCHAR2(6)       NOT NULL                  ,
    QA_TY_ID                                                              VARCHAR2(30)      NOT NULL                  ,
    CUSTCO_ID                                                          VARCHAR2(20)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)      NOT NULL                  ,
    QA_TY_L_CD                                                            VARCHAR2(30)                                ,
    QA_TY_L                                                               VARCHAR2(30)                                ,
    QA_TY_M_CD                                                            VARCHAR2(30)                                ,
    QA_TY_M                                                               VARCHAR2(30)      NOT NULL                  ,
    QA_TY_S                                                               VARCHAR2(100)     NOT NULL                  ,
    QA_TY_S_P                                                             NUMBER(3)         NOT NULL                  ,
    REG_DTIM                                                              VARCHAR2(14)      NOT NULL                  ,
    REG_ID                                                                VARCHAR2(20)      NOT NULL                  ,
    CHNG_DTIM                                                             VARCHAR2(14)      NOT NULL                  ,
    CHNG_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    CONSTRAINT XPK_PLT_PHN_QA_EVAL_SHT PRIMARY KEY (QA_ID,QA_YM,QA_TY_ID,CUSTCO_ID) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_QA_EVAL_SHT                             IS '전화_QA평가지'                          ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.QA_ID                       IS 'QA_ID'                                  ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.QA_YM                       IS 'QA_년월'                                ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.QA_TY_ID                    IS 'QA_타입ID'                              ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.CUSTCO_ID                IS 'ASP_고객사_키'                          ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.CENT_TY                     IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.QA_TY_L_CD                  IS 'QA대분류_코드'                          ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.QA_TY_L                     IS 'QA유형1'                                ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.QA_TY_M_CD                  IS 'QA중분류_코드'                          ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.QA_TY_M                     IS 'QA유형2'                                ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.QA_TY_S                     IS 'QA유형3'                                ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.QA_TY_S_P                   IS 'QA유형3_배점'                           ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.REG_DTIM                    IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.REG_ID                      IS '등록자ID'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.CHNG_DTIM                   IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_QA_EVAL_SHT.CHNG_ID                     IS '변경자ID'                               ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_STS_CLBK    전화_콜백통계
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_STS_CLBK CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_STS_CLBK (  
    BASE_DATE                                                             VARCHAR2(8)       NOT NULL                  ,
    ORGZ_ID                                                               VARCHAR2(10)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)      NOT NULL                  ,
    VER_NO                                                                NUMBER(3)         NOT NULL                  ,
    TRY_CNT                                                               NUMBER(10)                                  ,
    SUCCES_CNT                                                            NUMBER(10)                                  ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_STS_CLBK PRIMARY KEY (BASE_DATE,ORGZ_ID,CENT_TY,VER_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_STS_CLBK                                    IS '전화_콜백통계'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.BASE_DATE                          IS '기준일자'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.ORGZ_ID                            IS '조직ID'                                 ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.CENT_TY                            IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.VER_NO                             IS '버전번호'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.TRY_CNT                            IS '시도건수'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.SUCCES_CNT                         IS '성공건수'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.REG_DTIM                           IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.REG_MAN                            IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.CHNG_DTIM                          IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CLBK.CHNG_MAN                           IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_STS_CNSL_TYP    전화_상담유형통계
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_STS_CNSL_TYP CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_STS_CNSL_TYP (  
    CNSL_DATE                                                             VARCHAR2(8)       NOT NULL                  ,
    CNSL_TY_CODE                                                          VARCHAR2(10)      NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)      NOT NULL                  ,
    VER_NO                                                                NUMBER(3)         NOT NULL                  ,
    CNSL_TY_LCLS                                                          VARCHAR2(10)                                ,
    CNSL_TY_MDCLS                                                         VARCHAR2(10)                                ,
    CNSL_TY_SCLS                                                          VARCHAR2(10)                                ,
    CNT                                                                   NUMBER(10)                                  ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_STS_CNSL_TYP PRIMARY KEY (CNSL_DATE,CNSL_TY_CODE,CENT_TY,VER_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_STS_CNSL_TYP                                IS '상담유형통계'                           ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.CNSL_DATE                      IS '상담일자'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.CNSL_TY_CODE                   IS '상담유형코드'                           ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.CENT_TY                        IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.VER_NO                         IS '버전번호'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.CNSL_TY_LCLS                   IS '상담유형대분류'                         ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.CNSL_TY_MDCLS                  IS '상담유형중분류'                         ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.CNSL_TY_SCLS                   IS '상담유형소분류'                         ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.CNT                            IS '건수'                                   ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_STS_CNSL_TYP.CHNG_MAN                       IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_STS_OBD_DD    전화_아웃바운드일일통계
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_STS_OBD_DD CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_STS_OBD_DD (  
    CAM_ID                                                                NUMBER(10)        NOT NULL                  ,
    BASE_DATE                                                             VARCHAR2(8)       NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)      NOT NULL                  ,
    VER_NO                                                                NUMBER(3)         NOT NULL                  ,
    CAM_TY                                                                VARCHAR2(10)                                ,
    FISH_YN                                                               VARCHAR2(1)                                 ,
    TOT_CNT                                                               NUMBER(10)                                  ,
    TDD_PROC_FISH_CNT                                                     NUMBER(10)                                  ,
    SUM_PROC_FISH_CNT                                                     NUMBER(10)                                  ,
    PROC_ING_CNT                                                          NUMBER(10)                                  ,
    UNTRY_CNT                                                             NUMBER(10)                                  ,
    TDD_FAIL_CNT                                                          NUMBER(10)                                  ,
    SUM_FAIL_CNT                                                          NUMBER(10)                                  ,
    TDD_PROC_UNABL_CNT                                                    NUMBER(10)                                  ,
    SUM_PROC_UNABL_CNT                                                    NUMBER(10)                                  ,
    TDD_ETC_CNT                                                           NUMBER(10)                                  ,
    SUM_ETC_CNT                                                           NUMBER(10)                                  ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_STS_OBD_DD PRIMARY KEY (CAM_ID,BASE_DATE,CENT_TY,VER_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_STS_OBD_DD                                  IS '아웃바운드일일통계'                     ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.CAM_ID                           IS '캠페인ID'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.BASE_DATE                        IS '기준일자'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.CENT_TY                          IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.VER_NO                           IS '버전번호'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.CAM_TY                           IS '캠페인구분'                             ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.FISH_YN                          IS '마감여부'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.TOT_CNT                          IS '전체건수'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.TDD_PROC_FISH_CNT                IS '당일처리완료건수'                       ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.SUM_PROC_FISH_CNT                IS '누적처리완료건수'                       ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.PROC_ING_CNT                     IS '처리중건수'                             ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.UNTRY_CNT                        IS '미시도건수'                             ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.TDD_FAIL_CNT                     IS '당일실패건수'                           ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.SUM_FAIL_CNT                     IS '누적실패건수'                           ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.TDD_PROC_UNABL_CNT               IS '당일처리불가건수'                       ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.SUM_PROC_UNABL_CNT               IS '누적처리불가건수'                       ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.TDD_ETC_CNT                      IS '당일기타건수'                           ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.SUM_ETC_CNT                      IS '누적기타건수'                           ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.REG_DTIM                         IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.REG_MAN                          IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.CHNG_DTIM                        IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DD.CHNG_MAN                         IS '변경자'                                 ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_STS_OBD_DST    전화_아운바운드배분통계
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_STS_OBD_DST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_STS_OBD_DST (  
    CAM_TY                                                                VARCHAR2(10)      NOT NULL                  ,
    DSTR_DATE                                                             VARCHAR2(8)       NOT NULL                  ,
    USER_ID                                                               VARCHAR2(20)      NOT NULL                  ,
    VER_NO                                                                NUMBER(3)         NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    ORGZ_ID                                                               VARCHAR2(10)                                ,
    DSTR_CNT                                                              NUMBER(10)                                  ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_STS_OBD_DST PRIMARY KEY (CAM_TY,DSTR_DATE,USER_ID,VER_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_STS_OBD_DST                                 IS '아운바운드배분통계'                     ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.CAM_TY                          IS '캠페인구분'                             ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.DSTR_DATE                       IS '배분일자'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.USER_ID                         IS '사용자ID'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.VER_NO                          IS '버전번호'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.CENT_TY                         IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.ORGZ_ID                         IS '조직ID'                                 ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.DSTR_CNT                        IS '배분건수'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.REG_DTIM                        IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.REG_MAN                         IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.CHNG_DTIM                       IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_STS_OBD_DST.CHNG_MAN                        IS '변경자'                                 ;




--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_NTCN_TALK_SEND_HST    전화_알림톡 발송이력 정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_NTCN_TALK_SEND_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_NTCN_TALK_SEND_HST (	
    NTCN_TALK_SEND_HIST_NO                                                 VARCHAR2(30)     NOT NULL                   , 
    CENT_TY                                                                VARCHAR2(10)                                , 
    CUST_NO                                                                VARCHAR2(64)                                , 
    CUST_TEL_NO                                                            VARCHAR2(64)                                , 
    NTCN_TALK_NO                                                           NUMBER(10)                                , 
    NTCN_TALK_CNTN                                                         VARCHAR2(3000)                              , 
    NTCN_TALK_REQ_DTIM                                                     VARCHAR2(14)                                , 
    NTCN_TALK_PROC_RSLT                                                    VARCHAR2(10)                                , 
    REG_DTIM                                                               VARCHAR2(14)                                , 
    REG_MAN                                                                VARCHAR2(20)                                , 
    CHNG_DTIM                                                              VARCHAR2(14)                                , 
    CHNG_MAN                                                               VARCHAR2(20)                                ,
    CONSTRAINT XPK_PLT_PHN_NTCN_TALK_SEND_HST PRIMARY KEY (NTCN_TALK_SEND_HIST_NO)
) 
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE PLT_PHN_NTCN_TALK_SEND_HST                          IS '전화_알림톡발송이력'                       ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.NTCN_TALK_SEND_HIST_NO  IS '알림톡발송이력번호'                        ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.CENT_TY                 IS '센터구분'                                 ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.CUST_NO                 IS '고객번호'                                 ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.CUST_TEL_NO             IS '고객전화번호'                             ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.NTCN_TALK_NO            IS '알림톡번호'                               ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.NTCN_TALK_CNTN          IS '알림톡내용'                               ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.NTCN_TALK_REQ_DTIM      IS '알림톡요청일시'                           ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.NTCN_TALK_PROC_RSLT     IS '알림톡처리결과'                           ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.REG_DTIM                IS '등록일시'                                 ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.REG_MAN                 IS '등록자'                                   ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.CHNG_DTIM               IS '변경일시'                                 ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_SEND_HST.CHNG_MAN                IS '변경자'                                   ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_NTCN_TALK_TMPL    전화_알림톡 템플릿 정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_NTCN_TALK_TMPL CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_NTCN_TALK_TMPL (	
    NTCN_TALK_NO                                                           VARCHAR2(30)     NOT NULL                    ,  
    CENT_TY                                                                VARCHAR2(10)                                 , 
    NTCN_TALK_TY                                                           VARCHAR2(10)                                 , 
    NTCN_TALK_TMPLT                                                        VARCHAR2(10)                                 , 
    TIT                                                                    VARCHAR2(200)                                , 
    NTCN_TALK_CNTN                                                         VARCHAR2(3000)                               , 
    NTCN_TALK_TOT_SEND_YN                                                  VARCHAR2(1)                                  , 
    CUST_NM_ADD_YN                                                         VARCHAR2(1)                                  , 
    CU_INFO_ADD_YN                                                         VARCHAR2(1)                                  , 
    CODE_INFO_ADD_YN                                                       VARCHAR2(1)                                  , 
    USE_YN                                                                 VARCHAR2(1)                                  , 
    REG_DTIM                                                               VARCHAR2(14)                                 , 
    REG_MAN                                                                VARCHAR2(20)                                 , 
    CHNG_DTIM                                                              VARCHAR2(14)                                 , 
    CHNG_MAN                                                               VARCHAR2(20)                                 , 
    SCR_DISP_YN                                                            VARCHAR2(1)                                  ,
    CONSTRAINT XPK_PLT_PHN_NTCN_TALK_TMPL PRIMARY KEY (NTCN_TALK_NO)
)
--TABLESPACE TS_PALETTE
;


COMMENT ON TABLE  PLT_PHN_NTCN_TALK_TMPL                                IS '전화_NTCN_TALK템플릿'                    ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.NTCN_TALK_NO                   IS 'NTCN_TALK번호'                          ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.CENT_TY                        IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.NTCN_TALK_TY                   IS 'NTCN_TALK유형'                          ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.NTCN_TALK_TMPLT                IS 'NTCN_TALK템플릿'                        ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.TIT                            IS '제목'                                   ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.NTCN_TALK_CNTN                 IS 'NTCN_TALK내용'                          ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.NTCN_TALK_TOT_SEND_YN          IS 'NTCN_TALK일괄전송여부'                  ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.CUST_NM_ADD_YN                 IS '고객명추가여부'                         ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.CU_INFO_ADD_YN                 IS '조합정보추가여부'                       ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.CODE_INFO_ADD_YN               IS '코드정보추가여부'                       ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.USE_YN                         IS '사용여부'                               ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.CHNG_MAN                       IS '변경자'                                 ;
COMMENT ON COLUMN PLT_PHN_NTCN_TALK_TMPL.SCR_DISP_YN                    IS '즐겨찾기'                                 ;


--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_SMS_SND_HST    전화_SMS발송이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP TABLE PLT_PHN_SMS_SND_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_SMS_SND_HST (
  SMS_SEND_HIST_NO                                                         VARCHAR2(30)        NOT NULL                   , 
  CENT_TY                                                                  VARCHAR2(10)                                   , 
  CUST_NO                                                                  VARCHAR2(64)                                   , 
  CUST_TEL_NO                                                              VARCHAR2(64)                                   , 
  SMS_NO                                                                   NUMBER(10)                                     , 
  SMS_CNTN                                                                 VARCHAR2(3000)                                 , 
  SMS_REQ_DTIM                                                             VARCHAR2(14)                                   , 
  SMS_PROC_RSLT                                                            VARCHAR2(10)                                   , 
  REG_DTIM                                                                 VARCHAR2(14)                                   , 
  REG_MAN                                                                  VARCHAR2(20)                                   , 
  CHNG_DTIM                                                                VARCHAR2(14)                                   , 
  CHNG_MAN                                                                 VARCHAR2(20)                                   , 
  CONSTRAINT XPK_PLT_PHN_SMS_SND_HST PRIMARY KEY (SMS_SEND_HIST_NO)
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE PLT_PHN_SMS_SND_HST                                   IS '전화_SMS발송이력'                          ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.SMS_SEND_HIST_NO                 IS 'SMS발송이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.CENT_TY                          IS '센터구분'                                  ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.CUST_NO                          IS '고객번호'                                  ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.CUST_TEL_NO                      IS '고객전화번호'                              ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.SMS_NO                           IS 'SMS번호'                                   ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.SMS_CNTN                         IS 'SMS내용'                                   ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.SMS_REQ_DTIM                     IS 'SMS요청일시'                               ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.SMS_PROC_RSLT                    IS 'SMS처리결과'                               ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.REG_DTIM                         IS '등록일시'                                  ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.REG_MAN                          IS '등록자'                                    ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.CHNG_DTIM                        IS '변경일시'                                  ;
COMMENT ON COLUMN PLT_PHN_SMS_SND_HST.CHNG_MAN                         IS '변경자'                                    ;



--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_SMS_TMPL    전화_SMS템플릿
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_SMS_TMPL CASCADE CONSTRAINTS;
-- 2021.04.09 현은지 > SMS템플릿 관리 '즐겨찾기' 컬럼(SCR_DISP_YN) 추가

CREATE TABLE PLT_PHN_SMS_TMPL (  
    SMS_NO                                                                NUMBER(10)        NOT NULL                  ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    SMS_TY                                                                VARCHAR2(10)                                ,
    SMS_TMPLT                                                             VARCHAR2(10)                                ,
    TIT                                                                   VARCHAR2(200)                               ,
    SMS_CNTN                                                              VARCHAR2(3000)                              ,
    SMS_TOT_SEND_YN                                                       VARCHAR2(1)                                 ,
    CUST_NM_ADD_YN                                                        VARCHAR2(1)                                 ,
    CU_INFO_ADD_YN                                                        VARCHAR2(1)                                 ,
    CODE_INFO_ADD_YN                                                      VARCHAR2(1)                                 ,
    USE_YN                                                                VARCHAR2(1)                                 ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    SCR_DISP_YN                                                           VARCHAR2(1)                                 ,
    CONSTRAINT XPK_PLT_PHN_SMS_TMPL PRIMARY KEY (SMS_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_SMS_TMPL                                IS '전화_SMS템플릿'                         ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.SMS_NO                         IS 'SMS번호'                                ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.CENT_TY                        IS '센터구분'                               ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.SMS_TY                         IS 'SMS유형'                                ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.SMS_TMPLT                      IS 'SMS템플릿'                              ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.TIT                            IS '제목'                                   ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.SMS_CNTN                       IS 'SMS내용'                                ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.SMS_TOT_SEND_YN                IS 'SMS일괄전송여부'                        ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.CUST_NM_ADD_YN                 IS '고객명추가여부'                         ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.CU_INFO_ADD_YN                 IS '조합정보추가여부'                       ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.CODE_INFO_ADD_YN               IS '코드정보추가여부'                       ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.USE_YN                         IS '사용여부'                               ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.REG_DTIM                       IS '등록일시'                               ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.REG_MAN                        IS '등록자'                                 ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.CHNG_DTIM                      IS '변경일시'                               ;
COMMENT ON COLUMN PLT_PHN_SMS_TMPL.CHNG_MAN                       IS '변경자'                                 ;

-- 2021.05.13 추가 이동욱
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_EML_SND_HST    전화_EML발송이력
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
DROP   TABLE PLT_PHN_EML_SND_HST CASCADE CONSTRAINTS;

CREATE TABLE PLT_PHN_EML_SND_HST (  
    EML_SEND_HIST_NO                                                      VARCHAR2(30)        NOT NULL              ,
    CENT_TY                                                               VARCHAR2(10)                                ,
    CUST_NO                                                               VARCHAR2(64)                                ,
    CUST_TEL_NO                                                           VARCHAR2(64)                                ,
    EML_CNTN                                                              VARCHAR2(3000)                            ,
    EML_REQ_DTIM                                                          VARCHAR2(14)                              ,
    EML_PROC_RSLT                                                         VARCHAR2(10)                              ,
    REG_DTIM                                                              VARCHAR2(14)                                ,
    REG_MAN                                                               VARCHAR2(20)                                ,
    CHNG_DTIM                                                             VARCHAR2(14)                                ,
    CHNG_MAN                                                              VARCHAR2(20)                                ,
    CUSTOMER_ID                                                           VARCHAR2(80)                                ,
    CONSTRAINT XPK_PLT_PHN_EML_SND_HST PRIMARY KEY (EML_SEND_HIST_NO) 
)
--TABLESPACE TS_PALETTE
;

COMMENT ON TABLE  PLT_PHN_EML_SND_HST                             IS '전화_EML발송이력'                            ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.EML_SEND_HIST_NO            IS 'EML발송이력번호'                           ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.CENT_TY                     IS '센터구분'                                      ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.CUST_NO                     IS '고객번호'                                      ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.CUST_TEL_NO                 IS '고객전화번호'                                  ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.EML_CNTN                    IS 'EML내용'                                  ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.EML_REQ_DTIM                IS 'EML요청일시'                              ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.EML_PROC_RSLT               IS 'EML처리결과'                              ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.REG_DTIM                    IS '등록일시'                                     ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.REG_MAN                     IS '등록자'                                       ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.CHNG_DTIM                   IS '변경일시'                                     ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.CHNG_MAN                    IS '변경자'                                       ;
COMMENT ON COLUMN PLT_PHN_EML_SND_HST.CUSTOMER_ID                    IS '고객ID'                                       ;

