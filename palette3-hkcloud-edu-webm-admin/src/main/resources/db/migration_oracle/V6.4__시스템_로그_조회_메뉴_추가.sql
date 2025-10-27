/*시스템 로그 조회 신규 메뉴 추가*/

INSERT INTO PLT_MENU(MENU_ID,UP_MENU_ID,LNKG_MENU_ID,MENU_SE_CD_LVL,MENU_NM,MENU_EXPLN,POPUP_FILE_NM,PRGRM_PARAM,VIEW_TRGT,PATH_NM,POPUP_WD_SZ,POPUP_HT_SZ,ICON_CLASS_NM,ACT_NM,INQ_AUTHRT,PRCS_AUTHRT,DEL_AUTHRT,OTPT_AUTHRT,EXCL_AUTHRT,ADD_AUTHRT,SORT_ORD,USE_YN,DEL_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) 
VALUES((SELECT MAX(MENU_ID) FROM PLT_MENU) + 1, 77, NULL, '3', '시스템 로그 조회', '시스템 로그를 조회합니다.', NULL, NULL, 'MAIN', '/STG_M0611', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '11', 'Y', 'N', '2', to_char(sysdate,'yyyymmddhh24miss'), NULL, NULL);

INSERT INTO plt_authrt_group_prgrm
(authrt_group_id, menu_id, inq_authrt, prcs_authrt, del_authrt, otpt_authrt, excl_authrt, add_authrt, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
select authrt_group_id, (SELECT MAX(MENU_ID) FROM PLT_MENU), NULL, NULL, NULL, NULL, NULL, NULL, '2', to_char(sysdate,'yyyymmddhh24miss'), NULL, null from plt_authrt where user_se_cd ='MANAGER';


UPDATE PLT_TBL_SEQ SET SEQ_VL = (SELECT MAX(MENU_ID) FROM PLT_MENU) + 1 WHERE COL_ID = 'MENU_ID';
