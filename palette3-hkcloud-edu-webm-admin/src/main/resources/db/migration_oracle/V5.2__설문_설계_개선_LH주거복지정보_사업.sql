
CREATE TABLE PLT_SRVY_CLSF
(
	SRVY_CLSF_ID          NUMBER(19,0)  NOT NULL ,
	UP_SRVY_CLSF_ID       NUMBER(19,0)  NULL ,
	SRVY_CLSF_NM          VARCHAR2(200)  NULL ,
	SRVY_CLSF_EXPLN       VARCHAR2(2000)  NULL ,
	SORT_ORD              INT  NULL ,
	USE_YN                CHAR(1)  NULL ,
	DEL_YN                CHAR(1)  NULL ,
	RGTR_ID               NUMBER(19,0)  NOT NULL ,
	REG_DT                VARCHAR2(14)  NOT NULL ,
	MDFR_ID               NUMBER(19,0)  NULL ,
	MDFCN_DT              VARCHAR2(14)  NULL ,
	CUSTCO_ID             NUMBER(19,0)  NULL 
);



CREATE UNIQUE INDEX PLT_SRVY_CLSF_PK ON PLT_SRVY_CLSF
(SRVY_CLSF_ID  ASC);



ALTER TABLE PLT_SRVY_CLSF
	ADD PRIMARY KEY (SRVY_CLSF_ID);



CREATE INDEX PLT_SRVY_CLSF_F1 ON PLT_SRVY_CLSF
(UP_SRVY_CLSF_ID  ASC);



CREATE INDEX PLT_SRVY_CLSF_F2 ON PLT_SRVY_CLSF
(CUSTCO_ID  ASC);



CREATE TABLE PLT_SRVY_EXL_TRGT
(
	SRVY_EXL_TRGT_ID      NUMBER(19,0)  NOT NULL ,
	EXL_SE_CD             VARCHAR2(60)  NULL ,
	CUST_NM               VARCHAR2(300)  NULL ,
	CUST_PHN_NO           VARCHAR2(60)  NULL ,
	EXL_BGNG_DT           VARCHAR2(14)  NULL ,
	EXL_END_DT            VARCHAR2(14)  NULL ,
	EXL_RSN               VARCHAR2(1000)  NULL ,
	DEL_YN                CHAR(1)  NULL ,
	REG_DT                VARCHAR2(14)  NOT NULL ,
	RGTR_ID               NUMBER(19,0)  NOT NULL ,
	MDFCN_DT              VARCHAR2(14)  NULL ,
	MDFR_ID               NUMBER(19,0)  NULL ,
	CUSTCO_ID             NUMBER(19,0)  NULL 
);



CREATE UNIQUE INDEX PLT_SRVY_EXL_TRGT_PK ON PLT_SRVY_EXL_TRGT
(SRVY_EXL_TRGT_ID  ASC);



ALTER TABLE PLT_SRVY_EXL_TRGT
	ADD PRIMARY KEY (SRVY_EXL_TRGT_ID);



CREATE INDEX PLT_SRVY_EXL_TRGT_F1 ON PLT_SRVY_EXL_TRGT
(CUSTCO_ID  ASC);



ALTER TABLE PLT_SRVY_EXL_TRGT
	ADD CONSTRAINT  R_SRVY_EXL_TRGT_2__CUSTCO_c9 FOREIGN KEY (CUSTCO_ID) REFERENCES PLT_CUSTCO(CUSTCO_ID);



CREATE TABLE PLT_SRVY_STTS_HSTRY
(
	SRVY_STTS_HSTRY_ID    NUMBER(19,0)  NOT NULL ,
	SRVY_ID               NUMBER(19,0)  NULL ,
	STTS_CD               VARCHAR2(60)  NULL ,
	CN                    number(19,0) ,
	RGTR_ID               NUMBER(19,0)  NOT NULL ,
	REG_DT                VARCHAR2(14)  NOT NULL 
);



CREATE UNIQUE INDEX PLT_SRVY_STTS_HSTRY_PK ON PLT_SRVY_STTS_HSTRY
(SRVY_STTS_HSTRY_ID  ASC);



ALTER TABLE PLT_SRVY_STTS_HSTRY
	ADD PRIMARY KEY (SRVY_STTS_HSTRY_ID);



CREATE INDEX PLT_SRVY_STTS_HSTRY_F1 ON PLT_SRVY_STTS_HSTRY
(SRVY_ID  ASC);



CREATE TABLE PLT_SRVY_TRGT_DTL
(
	SRVY_TRGT_ID          NUMBER(19,0)  NOT NULL ,
	ATTR_VL               VARCHAR2(500)  NULL ,
	ATTR_ID               NUMBER(19,0)  NOT NULL 
);



CREATE UNIQUE INDEX PLT_SRVY_TRGT_DTL_PK ON PLT_SRVY_TRGT_DTL
(SRVY_TRGT_ID  ASC,ATTR_ID  ASC);



ALTER TABLE PLT_SRVY_TRGT_DTL
	ADD PRIMARY KEY (SRVY_TRGT_ID,ATTR_ID);



CREATE INDEX PLT_SRVY_TRGT_DTL_F1 ON PLT_SRVY_TRGT_DTL
(SRVY_TRGT_ID  ASC);



CREATE INDEX PLT_SRVY_TRGT_DTL_F2 ON PLT_SRVY_TRGT_DTL
(ATTR_ID  ASC);



ALTER TABLE PLT_SRVY_CLSF
	ADD CONSTRAINT  R_SRVY_CLSF_2__SRVY_CLSF_ef FOREIGN KEY (UP_SRVY_CLSF_ID) REFERENCES PLT_SRVY_CLSF(SRVY_CLSF_ID);



ALTER TABLE PLT_SRVY_CLSF
	ADD CONSTRAINT  R_SRVY_CLSF_2__CUSTCO_db FOREIGN KEY (CUSTCO_ID) REFERENCES PLT_CUSTCO(CUSTCO_ID);



ALTER TABLE PLT_SRVY_STTS_HSTRY
	ADD CONSTRAINT  R_SRVY_STTS_HSTRY_2__SRVY_ff FOREIGN KEY (SRVY_ID) REFERENCES PLT_SRVY(SRVY_ID);



ALTER TABLE PLT_SRVY_TRGT_DTL
	ADD CONSTRAINT  R_SRVY_TRGT_DTL_2__SRVY_TRGT_d FOREIGN KEY (SRVY_TRGT_ID) REFERENCES PLT_SRVY_TRGT(SRVY_TRGT_ID);



ALTER TABLE PLT_SRVY_TRGT_DTL
	ADD CONSTRAINT  R_SRVY_TRGT_DTL_2__EXPSN_ATTR_ FOREIGN KEY (ATTR_ID) REFERENCES PLT_EXPSN_ATTR(ATTR_ID);



COMMENT ON TABLE PLT_SRVY_CLSF IS '설문_분류';
COMMENT ON COLUMN PLT_SRVY_CLSF.SRVY_CLSF_ID IS '설문_분류_ID';
COMMENT ON COLUMN PLT_SRVY_CLSF.UP_SRVY_CLSF_ID IS '상위_설문_분류_ID';
COMMENT ON COLUMN PLT_SRVY_CLSF.SRVY_CLSF_NM IS '설문_분류_명';
COMMENT ON COLUMN PLT_SRVY_CLSF.SRVY_CLSF_EXPLN IS '설문_분류_설명';
COMMENT ON COLUMN PLT_SRVY_CLSF.SORT_ORD IS '정렬_순서';
COMMENT ON COLUMN PLT_SRVY_CLSF.USE_YN IS '사용_여부';
COMMENT ON COLUMN PLT_SRVY_CLSF.DEL_YN IS '삭제_여부';
COMMENT ON COLUMN PLT_SRVY_CLSF.RGTR_ID IS '등록자_ID';
COMMENT ON COLUMN PLT_SRVY_CLSF.REG_DT IS '등록_일시';
COMMENT ON COLUMN PLT_SRVY_CLSF.MDFR_ID IS '수정자_ID';
COMMENT ON COLUMN PLT_SRVY_CLSF.MDFCN_DT IS '수정_일시';
COMMENT ON COLUMN PLT_SRVY_CLSF.CUSTCO_ID IS '고객사_ID';

COMMENT ON TABLE PLT_SRVY_EXL_TRGT IS '설문_제외_대상';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.SRVY_EXL_TRGT_ID IS '설문_제외_대상_ID';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.CUST_NM IS '고객_명';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.CUST_PHN_NO IS '고객_전화_번호';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.EXL_SE_CD IS '제외_구분_코드';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.EXL_BGNG_DT IS '제외_시작_일시';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.EXL_END_DT IS '제외_종료_일시';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.DEL_YN IS '삭제_여부';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.REG_DT IS '등록_일시';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.RGTR_ID IS '등록자_ID';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.MDFCN_DT IS '수정_일시';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.MDFR_ID IS '수정자_ID';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.EXL_RSN IS '제외_사유';
COMMENT ON COLUMN PLT_SRVY_EXL_TRGT.CUSTCO_ID IS '고객사_ID';

COMMENT ON TABLE PLT_SRVY_STTS_HSTRY IS '설문_상태_이력';
COMMENT ON COLUMN PLT_SRVY_STTS_HSTRY.SRVY_STTS_HSTRY_ID IS '설문_상태_이력_ID';
COMMENT ON COLUMN PLT_SRVY_STTS_HSTRY.STTS_CD IS '상태_코드';
COMMENT ON COLUMN PLT_SRVY_STTS_HSTRY.CN IS '내용';
COMMENT ON COLUMN PLT_SRVY_STTS_HSTRY.RGTR_ID IS '등록자_ID';
COMMENT ON COLUMN PLT_SRVY_STTS_HSTRY.REG_DT IS '등록_일시';
COMMENT ON COLUMN PLT_SRVY_STTS_HSTRY.SRVY_ID IS '설문_ID';

COMMENT ON TABLE PLT_SRVY_TRGT_DTL IS '설문_대상_상세';
COMMENT ON COLUMN PLT_SRVY_TRGT_DTL.SRVY_TRGT_ID IS '설문_대상_ID';
COMMENT ON COLUMN PLT_SRVY_TRGT_DTL.ATTR_ID IS '속성_ID';
COMMENT ON COLUMN PLT_SRVY_TRGT_DTL.ATTR_VL IS '속성_값';




ALTER TABLE PLT_SRVY_QITEM_GROUP ADD MVMN_SRVY_QITEM_GROUP_ID  NUMBER(19,0)  NULL;
COMMENT ON COLUMN PLT_SRVY_QITEM_GROUP.MVMN_SRVY_QITEM_GROUP_ID IS '이동_설문_문항_그룹_ID';





ALTER TABLE PLT_SRVY_QITEM ADD RLS_YN CHAR(1)  NULL;
ALTER TABLE PLT_SRVY_QITEM ADD SRVY_CLSF_ID NUMBER(19,0)  NULL;
ALTER TABLE PLT_SRVY_QITEM ADD SCR_USE_YN CHAR(1)  NULL;
ALTER TABLE PLT_SRVY_QITEM ADD GROUP_MVMN_USE_YN CHAR(1)  NULL;
COMMENT ON COLUMN PLT_SRVY_QITEM.SRVY_CLSF_ID IS '설문_분류_ID';
COMMENT ON COLUMN PLT_SRVY_QITEM.RLS_YN IS '공개_여부';
COMMENT ON COLUMN PLT_SRVY_QITEM.SCR_USE_YN IS '점수_사용_여부';
COMMENT ON COLUMN PLT_SRVY_QITEM.GROUP_MVMN_USE_YN IS '그룹_이동_사용_여부';

ALTER TABLE PLT_SRVY_QITEM MODIFY SRVY_QITEM_GROUP_ID  NULL;

ALTER TABLE PLT_SRVY_QITEM
	ADD CONSTRAINT  R_SRVY_QITEM_2__SRVY_CLSF_89 FOREIGN KEY (SRVY_CLSF_ID) REFERENCES PLT_SRVY_CLSF(SRVY_CLSF_ID);



ALTER TABLE PLT_SRVY_QITEM_CHC ADD MVMN_SRVY_QITEM_GROUP_ID NUMBER(19,0)  NULL;
ALTER TABLE PLT_SRVY_QITEM_CHC ADD RSPNS_USE_YN CHAR(1)  NULL;
ALTER TABLE PLT_SRVY_QITEM_CHC ADD SCR INT  NULL;
COMMENT ON COLUMN PLT_SRVY_QITEM_CHC.MVMN_SRVY_QITEM_GROUP_ID IS '이동_설문_문항_그룹_ID';
COMMENT ON COLUMN PLT_SRVY_QITEM_CHC.RSPNS_USE_YN IS '응답_사용_여부';
COMMENT ON COLUMN PLT_SRVY_QITEM_CHC.SCR IS '점수';


ALTER TABLE PLT_SRVY_RSPNS_QITEM_CHC ADD RSPNS_CN VARCHAR2(4000)  NULL;
COMMENT ON COLUMN PLT_SRVY_RSPNS_QITEM_CHC.RSPNS_CN IS '응답_내용';


ALTER TABLE PLT_EXPSN_ATTR ADD SRVY_ID NUMBER(19,0)  NULL;
COMMENT ON COLUMN PLT_EXPSN_ATTR.SRVY_ID IS '설문_ID';


CREATE INDEX PLT_EXPSN_ATTR_F5 ON PLT_EXPSN_ATTR
(SRVY_ID  ASC);

ALTER TABLE PLT_EXPSN_ATTR
	ADD CONSTRAINT  R_EXPSN_ATTR_2__SRVY_25 FOREIGN KEY (SRVY_ID) REFERENCES PLT_SRVY(SRVY_ID);
	
	


--확장항목 분류 - 설문 추가 
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'CATG_EXT_CL', 'SRVY', custco_id, '설문참여자', 'Survey Target', NULL, 'Y', 'Y', 'N', 4, '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;


--설문 진행상태 추가
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'PROG_ST', 'RDY', custco_id, '승인대기', '승인대기', NULL, 'Y', 'Y', 'N', 2, '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'PROG_ST', 'RFSL', custco_id, '승인반려', '승인반려', NULL, 'Y', 'Y', 'N', 3, '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'PROG_ST', 'RTRVL', custco_id, '승인회수', '승인회수', NULL, 'Y', 'Y', 'N', 4, '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'PROG_ST', 'APPR', custco_id, '승인완료', '승인완료', NULL, 'Y', 'Y', 'N', 5, '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;

update plt_comm_cd set 
sort_ord=6
where group_cd_id='PROG_ST' and cd_id='PUBCMP';

update plt_comm_cd set 
sort_ord=7
where group_cd_id='PROG_ST' and cd_id='ONGONG';

update plt_comm_cd set 
sort_ord=8
where group_cd_id='PROG_ST' and cd_id='TERMIAT';




--설문 참여자 제외 유형 추가
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select '*', 'SUVY_EXL_TRGT_TP', CUSTCO_ID, '설문 참여자 제외 유형', '설문 참여자 제외 유형', NULL, 'Y', 'Y', 'N', (select MAX(SORT_ORD)+1 from plt_comm_cd where GROUP_CD_ID='*'), '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;;

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SUVY_EXL_TRGT_TP', 'DENY', custco_id, '설문거부', '설문거부', NULL, 'Y', 'Y', 'N', 1, '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SUVY_EXL_TRGT_TP', 'EXCLUDE', custco_id, '설문제외', '설문제외', NULL, 'Y', 'Y', 'N', 2, '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;


--설문 참여자 제외 검색 유형 추가
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select '*', 'SUVY_EXL_SRCH_TP', CUSTCO_ID, '설문 참여자 제외 검색 유형', '설문 참여자 제외 검색 유형', NULL, 'Y', 'Y', 'N', (select MAX(SORT_ORD)+1 from plt_comm_cd where GROUP_CD_ID='*'), '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;;

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SUVY_EXL_SRCH_TP', 'CUST_NM', custco_id, '성명', '성명', NULL, 'Y', 'Y', 'N', 1, '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SUVY_EXL_SRCH_TP', 'CUST_PHN_NO', custco_id, '전화번호', '전화번호', NULL, 'Y', 'Y', 'N', 2, '2', to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL from plt_custco;




--이메일 발송 관리 - 설문_대상_ID 추가.
ALTER TABLE PLT_EML_SNDNG_HSTRY ADD (SRVY_TRGT_ID NUMBER(19,0) NULL);
COMMENT ON COLUMN PLT_EML_SNDNG_HSTRY.SRVY_TRGT_ID IS '설문_대상_ID';

CREATE INDEX PLT_EML_SNDNG_HSTRY_F4 ON PLT_EML_SNDNG_HSTRY
(SRVY_TRGT_ID  ASC);

ALTER TABLE PLT_EML_SNDNG_HSTRY
   ADD CONSTRAINT  R_EML_SNDNG_HSTRY_2__SRVY_TRGT FOREIGN KEY (SRVY_TRGT_ID) REFERENCES PLT_SRVY_TRGT(SRVY_TRGT_ID);




CREATE TABLE PLT_EML_TMPL
(
   EML_TMPL_ID           NUMBER(19,0)  NOT NULL ,
   TMPL_CLSF_ID          NUMBER(19,0)  NULL ,
   TMPL_TYPE_CD          VARCHAR2(60)  NULL ,
   TMPL_NM               VARCHAR2(200)  NULL ,
   TMPL_CN               VARCHAR2(4000)  NULL ,
   FILE_GROUP_KEY        VARCHAR2(90)  NULL ,
   SORT_ORD              INT  NULL ,
   USE_YN                CHAR(1)  NULL ,
   DEL_YN                CHAR(1)  NULL ,
   RGTR_ID               NUMBER(19,0)  NOT NULL ,
   REG_DT                VARCHAR2(14)  NOT NULL ,
   MDFR_ID               NUMBER(19,0)  NULL ,
   MDFCN_DT              VARCHAR2(14)  NULL 
);



CREATE UNIQUE INDEX PLT_EML_TMPL_PK ON PLT_EML_TMPL
(EML_TMPL_ID  ASC);



ALTER TABLE PLT_EML_TMPL
   ADD PRIMARY KEY (EML_TMPL_ID);



CREATE INDEX PLT_EML_TMPL_F1 ON PLT_EML_TMPL
(TMPL_CLSF_ID  ASC);



ALTER TABLE PLT_EML_TMPL
   ADD CONSTRAINT  R_EML_TMPL_2__TMPL_CLSF_dd FOREIGN KEY (TMPL_CLSF_ID) REFERENCES PLT_TMPL_CLSF(TMPL_CLSF_ID);



COMMENT ON TABLE PLT_EML_TMPL IS '이메일_템플릿';
COMMENT ON COLUMN PLT_EML_TMPL.EML_TMPL_ID IS '이메일_템플릿_ID';
COMMENT ON COLUMN PLT_EML_TMPL.TMPL_CLSF_ID IS '템플릿_분류_ID';
COMMENT ON COLUMN PLT_EML_TMPL.TMPL_TYPE_CD IS '템플릿_유형_코드';
COMMENT ON COLUMN PLT_EML_TMPL.TMPL_NM IS '템플릿_명';
COMMENT ON COLUMN PLT_EML_TMPL.TMPL_CN IS '템플릿_내용';
COMMENT ON COLUMN PLT_EML_TMPL.FILE_GROUP_KEY IS '파일_그룹_키';
COMMENT ON COLUMN PLT_EML_TMPL.SORT_ORD IS '정렬_순서';
COMMENT ON COLUMN PLT_EML_TMPL.USE_YN IS '사용_여부';
COMMENT ON COLUMN PLT_EML_TMPL.DEL_YN IS '삭제_여부';
COMMENT ON COLUMN PLT_EML_TMPL.RGTR_ID IS '등록자_ID';
COMMENT ON COLUMN PLT_EML_TMPL.REG_DT IS '등록_일시';
COMMENT ON COLUMN PLT_EML_TMPL.MDFR_ID IS '수정자_ID';
COMMENT ON COLUMN PLT_EML_TMPL.MDFCN_DT IS '수정_일시';



--참여자 개인정보 암호화
ALTER TABLE plt_srvy_trgt ADD cust_nm_orgnl VARCHAR2(300) NULL;
COMMENT ON COLUMN plt_srvy_trgt.cust_nm_orgnl IS '고객_명_원본';
ALTER TABLE plt_srvy_trgt ADD cust_phn_no_orgnl VARCHAR2(60) NULL;
COMMENT ON COLUMN plt_srvy_trgt.cust_phn_no_orgnl IS '고객_전화_번호_원본';
ALTER TABLE plt_srvy_trgt ADD eml_orgnl VARCHAR2(60) NULL;
COMMENT ON COLUMN plt_srvy_trgt.eml_orgnl IS '이메일_원본';


ALTER TABLE plt_srvy_trgt MODIFY cust_phn_no varchar2(300);
ALTER TABLE plt_srvy_trgt MODIFY eml varchar2(300);


update plt_srvy_trgt set
cust_nm_orgnl = cust_nm,
cust_phn_no_orgnl = cust_phn_no,
eml_orgnl = eml;





/*설문, 이메일 발송 관리 신규 메뉴 추가*/
INSERT INTO PLT_MENU(MENU_ID,UP_MENU_ID,LNKG_MENU_ID,MENU_SE_CD_LVL,MENU_NM,MENU_EXPLN,POPUP_FILE_NM,PRGRM_PARAM,VIEW_TRGT,PATH_NM,POPUP_WD_SZ,POPUP_HT_SZ,ICON_CLASS_NM,ACT_NM,INQ_AUTHRT,PRCS_AUTHRT,DEL_AUTHRT,OTPT_AUTHRT,EXCL_AUTHRT,ADD_AUTHRT,SORT_ORD,USE_YN,DEL_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) 
VALUES((SELECT MAX(MENU_ID) FROM PLT_MENU) + 1, 33, NULL, '2', '설문조사 승인 관리', '설문조사 진행을 위해 설문 시행계획을 승인하고 관리할 수 있습니다.', NULL, NULL, 'MAIN', '/SVY_M0500', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '5', 'Y', 'N', '2', to_char(SYSDATE,'yyyymmddhh24miss'), NULL, NULL);

INSERT INTO plt_authrt_group_prgrm
(authrt_group_id, menu_id, inq_authrt, prcs_authrt, del_authrt, otpt_authrt, excl_authrt, add_authrt, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select authrt_group_id, (SELECT MAX(MENU_ID) FROM PLT_MENU), NULL, NULL, NULL, NULL, NULL, NULL, '2', to_char(SYSDATE,'yyyymmddhh24miss'), NULL, null from plt_authrt where user_se_cd ='MANAGER';


INSERT INTO PLT_MENU(MENU_ID,UP_MENU_ID,LNKG_MENU_ID,MENU_SE_CD_LVL,MENU_NM,MENU_EXPLN,POPUP_FILE_NM,PRGRM_PARAM,VIEW_TRGT,PATH_NM,POPUP_WD_SZ,POPUP_HT_SZ,ICON_CLASS_NM,ACT_NM,INQ_AUTHRT,PRCS_AUTHRT,DEL_AUTHRT,OTPT_AUTHRT,EXCL_AUTHRT,ADD_AUTHRT,SORT_ORD,USE_YN,DEL_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) 
VALUES((SELECT MAX(MENU_ID) FROM PLT_MENU) + 1, 33, NULL, '2', '설문문항 관리', '설문지에 사용할 설문문항을 관리합니다.', NULL, NULL, 'MAIN', '/SVY_M0400', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', 'Y', 'N', '2', to_char(SYSDATE,'yyyymmddhh24miss'), NULL, NULL);

INSERT INTO plt_authrt_group_prgrm
(authrt_group_id, menu_id, inq_authrt, prcs_authrt, del_authrt, otpt_authrt, excl_authrt, add_authrt, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select authrt_group_id, (SELECT MAX(MENU_ID) FROM PLT_MENU), NULL, NULL, NULL, NULL, NULL, NULL, '2', to_char(SYSDATE,'yyyymmddhh24miss'), NULL, null from plt_authrt;


INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) 
select (select max(ATTR_ID)+1 from PLT_EXPSN_ATTR), custco_id, 'N', 'ENV', NULL, '설문 대상지정 사용 여부', '설문 대상지정 사용 여부', 'ENV_SRVY_TRGT_DSGN_USE_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', (select max(sort_ord)+1 from PLT_EXPSN_ATTR where se='ENV'), NULL, NULL, 2, to_char(SYSDATE, 'yyyymmddhh24miss'), NULL, NULL, NULL, NULL, 'N', 'Y' from plt_custco;

update plt_tbl_seq set seq_vl = seq_vl+1 where col_id ='ATTR_ID';

INSERT INTO plt_custco_stng
(custco_id, attr_id, attr_vl)
select custco_id, (select attr_id from PLT_EXPSN_ATTR where se='ENV' and expsn_attr_col_id='ENV_SRVY_TRGT_DSGN_USE_YN'), 'Y' from plt_custco;