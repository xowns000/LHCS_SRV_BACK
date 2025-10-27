INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select '*', 'SHROTCUT_CD', custco_id, '단축키 키 코드', '단축키 키보드 입력값의 keyCode', NULL, NULL, 'Y', 'N', 106, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '53', custco_id, '5', '5', NULL, NULL, 'Y', 'N', 5, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '54', custco_id, '6', '6', NULL, NULL, 'Y', 'N', 6, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '55', custco_id, '7', '7', NULL, NULL, 'Y', 'N', 7, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '56', custco_id, '8', '8', NULL, NULL, 'Y', 'N', 8, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '57', custco_id, '9', '9', NULL, NULL, 'Y', 'N', 9, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '49', custco_id, '1', '1', NULL, NULL, 'Y', 'N', 1, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '50', custco_id, '2', '2', NULL, NULL, 'Y', 'N', 2, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '51', custco_id, '3', '3', NULL, NULL, 'Y', 'N', 3, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '52', custco_id, '4', '4', NULL, NULL, 'Y', 'N', 4, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', '48', custco_id, '0', '0', NULL, NULL, 'Y', 'N', 10, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'SHROTCUT_CD', 'NO_USE', custco_id, '사용 안함', '단축키 사용 안함', NULL, NULL, 'Y', 'N', 0, 2, to_char(now(),'yyyymmddhh24miss'), null, null from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
