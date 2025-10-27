ALTER TABLE PLT_USER ADD IF NOT EXISTS CUSTCO_CUSL_ID VARCHAR(60) NULL;
COMMENT ON COLUMN PLT_USER.CUSTCO_CUSL_ID IS '고객사_상담사_ID';

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'EXPN_PT', 'TEL', CUSTCO_ID, '전화번호', 'Phone number', NULL, 'Y', 'Y', 'N', (select MAX(SORT_ORD)+1 from plt_comm_cd where GROUP_CD_ID='EXPN_PT'), '2', to_char(now(), 'yyyymmddhh24miss'), NULL, NULL from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;;