/*설문, 이메일 발송 관리 신규 메뉴 추가*/

INSERT INTO PLT_MENU(MENU_ID,UP_MENU_ID,LNKG_MENU_ID,MENU_SE_CD_LVL,MENU_NM,MENU_EXPLN,POPUP_FILE_NM,PRGRM_PARAM,VIEW_TRGT,PATH_NM,POPUP_WD_SZ,POPUP_HT_SZ,ICON_CLASS_NM,ACT_NM,INQ_AUTHRT,PRCS_AUTHRT,DEL_AUTHRT,OTPT_AUTHRT,EXCL_AUTHRT,ADD_AUTHRT,SORT_ORD,USE_YN,DEL_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) 
VALUES((SELECT MAX(MENU_ID) FROM PLT_MENU) + 1, 33, NULL, '2', '설문조사 통계', '설문조사 계획에 대한 승인 및 설문발송 통계 정보를 확인할 수 있습니다.', NULL, NULL, 'MAIN', '/SVY_M0600', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '6', 'Y', 'N', '2', to_char(now(),'yyyymmddhh24miss'), NULL, NULL);

INSERT INTO plt_authrt_group_prgrm
(authrt_group_id, menu_id, inq_authrt, prcs_authrt, del_authrt, otpt_authrt, excl_authrt, add_authrt, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select authrt_group_id, (SELECT MAX(MENU_ID) FROM PLT_MENU), NULL, NULL, NULL, NULL, NULL, NULL, '2', to_char(now(),'yyyymmddhh24miss'), NULL, null from plt_authrt;


INSERT INTO PLT_MENU(MENU_ID,UP_MENU_ID,LNKG_MENU_ID,MENU_SE_CD_LVL,MENU_NM,MENU_EXPLN,POPUP_FILE_NM,PRGRM_PARAM,VIEW_TRGT,PATH_NM,POPUP_WD_SZ,POPUP_HT_SZ,ICON_CLASS_NM,ACT_NM,INQ_AUTHRT,PRCS_AUTHRT,DEL_AUTHRT,OTPT_AUTHRT,EXCL_AUTHRT,ADD_AUTHRT,SORT_ORD,USE_YN,DEL_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) 
VALUES((SELECT MAX(MENU_ID) FROM PLT_MENU) + 1, 33, NULL, '2', '설문참여 제외 관리', '설문참여에 대한 거부 및 제외자를 관리할 수 있습니다.', NULL, NULL, 'MAIN', '/SVY_M0700', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '7', 'Y', 'N', '2', to_char(now(),'yyyymmddhh24miss'), NULL, NULL);

INSERT INTO plt_authrt_group_prgrm
(authrt_group_id, menu_id, inq_authrt, prcs_authrt, del_authrt, otpt_authrt, excl_authrt, add_authrt, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select authrt_group_id, (SELECT MAX(MENU_ID) FROM PLT_MENU), NULL, NULL, NULL, NULL, NULL, NULL, '2', to_char(now(),'yyyymmddhh24miss'), NULL, null from plt_authrt;


update PLT_MENU set sort_ord =3 where menu_id=35;
update PLT_MENU set sort_ord =4 where menu_id=36;


INSERT INTO PLT_MENU(MENU_ID,UP_MENU_ID,LNKG_MENU_ID,MENU_SE_CD_LVL,MENU_NM,MENU_EXPLN,POPUP_FILE_NM,PRGRM_PARAM,VIEW_TRGT,PATH_NM,POPUP_WD_SZ,POPUP_HT_SZ,ICON_CLASS_NM,ACT_NM,INQ_AUTHRT,PRCS_AUTHRT,DEL_AUTHRT,OTPT_AUTHRT,EXCL_AUTHRT,ADD_AUTHRT,SORT_ORD,USE_YN,DEL_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) 
VALUES((SELECT MAX(MENU_ID) FROM PLT_MENU) + 1, 15, NULL, '3', '이메일 발송 관리', '이메일 발송이력을 조회할 수 있습니다.', NULL, NULL, 'MAIN', '/CSL_M0605', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '5', 'Y', 'N', '2', to_char(now(),'yyyymmddhh24miss'), NULL, NULL);

INSERT INTO plt_authrt_group_prgrm
(authrt_group_id, menu_id, inq_authrt, prcs_authrt, del_authrt, otpt_authrt, excl_authrt, add_authrt, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select authrt_group_id, (SELECT MAX(MENU_ID) FROM PLT_MENU), NULL, NULL, NULL, NULL, NULL, NULL, '2', to_char(now(),'yyyymmddhh24miss'), NULL, null from plt_authrt;


UPDATE PLT_TBL_SEQ SET SEQ_VL = (SELECT MAX(MENU_ID) FROM PLT_MENU) + 1 WHERE COL_ID = 'MENU_ID';



INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select '*', 'EML_TP', CUSTCO_ID, '이메일 발송 유형', '이메일 발송 유형', NULL, 'Y', 'Y', 'N', (select MAX(SORT_ORD)+1 from plt_comm_cd where GROUP_CD_ID='*'), '2', to_char(now(), 'yyyymmddhh24miss'), NULL, NULL from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;;

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'EML_TP', 'chatemail', custco_id, '채팅상담', '채팅상담', NULL, 'Y', 'Y', 'N', 1, '2', to_char(now(), 'yyyymmddhh24miss'), NULL, NULL from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;

INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select 'EML_TP', 'svy', custco_id, '설문조사', '설문조사', NULL, 'Y', 'Y', 'N', 2, '2', to_char(now(), 'yyyymmddhh24miss'), NULL, NULL from plt_custco ON CONFLICT (group_cd_id, cd_id, custco_id) DO nothing;
