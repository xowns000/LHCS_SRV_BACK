###flyway 8.7까지 진행함.

create extension hstore;


--LH주거복지정보 데이터 초기화 - 메뉴 구성 변경, 권한 변경 등

--불필요 권한 정리 및 명칭 변경
delete from plt_btn_authrt where authrt_group_id in(3,4,5);
delete from plt_authrt_group_prgrm where authrt_group_id in(3,4,5);
delete from plt_authrt where authrt_group_id in(3,4,5);

update plt_authrt set
authrt_group_nm ='승인관리자',
group_expln ='승인관리자'
where authrt_group_id=2;

update plt_authrt set
authrt_group_nm ='일반사용자',
group_expln ='일반사용자'
where authrt_group_id=6;



--통합 메시지 - 하위 메뉴 레벨 2로 변경
update plt_menu set
menu_se_cd_lvl =2
where up_menu_id=15;

--통합 메시지 - 메뉴 레벨 1로 변경, up_menu_id is null 로 변경, 순서 2로 변경
update plt_menu set
menu_se_cd_lvl =1,
up_menu_id = null,
sort_ord=2
where menu_id=15;

-- 메신저 연동 관리 - 설정 하위로 위치 변경, 메뉴명 '연동 관리'로 변경, 순서 4로 변경
update plt_menu set
	menu_nm='연동 관리',
	menu_expln ='SNS, 이메일 연동을 위한 설정 정보를 등록하고 관리할 수 있습니다.',
	menu_se_cd_lvl=2,
	up_menu_id=60,
	sort_ord=4
where menu_id=71;

--확장항목 관리 - 사용 안함으로 변경
update plt_menu set
use_yn='N'
where menu_id=79;


--설문조사 - 순서 1로 변경
update plt_menu set
sort_ord=1
where menu_id=33;
--게시판 - 순서 3으로 변경
update plt_menu set
sort_ord=3
where menu_id=37;
--설정 - 순서 4로 변경
update plt_menu set
sort_ord=4
where menu_id=60;


--미사용 메뉴 삭제
delete from plt_btn_authrt where menu_id in(1,2,5,49);
delete from plt_btn where menu_id in(1,2,5,49);
delete from plt_authrt_group_prgrm where menu_id in(select menu_id from plt_menu where up_menu_id in(select menu_id from plt_menu where up_menu_id in(1, 20, 28, 41, 44,89,90)));
delete from plt_authrt_group_prgrm where menu_id in(select menu_id from plt_menu where up_menu_id in(1, 20, 28,41,44,64,67,89,90));
delete from plt_authrt_group_prgrm where menu_id in(select menu_id from plt_menu where menu_id in(1, 20, 28, 41,44,61,64,67,83,84,89,90));

delete from plt_menu where up_menu_id in(select menu_id from plt_menu where up_menu_id in(1, 20,28,41,44,89,90));
delete from plt_menu where lnkg_menu_id in (2,3);
delete from plt_menu where up_menu_id in(1, 20, 28, 41,44,64,67,89,90);
delete from plt_menu where menu_id in(1, 20, 28,41,44,61,64,67,83,84,89,90);


--Right 메뉴 삭제
delete from plt_authrt_group_prgrm where menu_id in(select menu_id from plt_menu where view_trgt ='RIGHT');
delete from plt_menu where view_trgt ='RIGHT';

--Left 메뉴 수정 -- icon 명 바꿔야 함
update plt_menu set 
menu_nm='설문조사 계획 관리',
menu_expln='설문조사 계획 관리',
path_nm='/SVY_M0100',
icon_class_nm ='svy-plan'
where menu_id=91;

update plt_menu set 
menu_nm='설문지 생성 관리',
menu_expln='설문지 생성 관리',
path_nm='/SVY_M0200',
icon_class_nm ='svy-create'
where menu_id=92;


update plt_menu set 
menu_nm='설문결과 분석',
menu_expln='설문결과 분석',
path_nm='/SVY_M0300',
icon_class_nm ='svy-analytic'
where menu_id=93;

update plt_menu set 
menu_nm='설문조사 승인 관리',
menu_expln='설문조사 승인 관리',
path_nm='/SVY_M0500',
icon_class_nm ='svy-confirm'
where menu_id=94;

update plt_menu set 
del_yn='Y'
where menu_id in(95,118);

--통합 메시지 아이콘
update plt_menu set 
icon_class_nm ='manage-sms'
where menu_id=15;

--


--설문 확장 속성 기본 값 생성.
INSERT INTO plt_srvy
(srvy_id, custco_id, srvy_yr, srvy_se_cd, srvy_nm, srvy_bgng_dt, srvy_end_dt, trgt_dsgn_yn, goal_psnal_dsgn_yn, goal_psnal, srvy_expln, stts_cd, pstg_yn, dpcn_sbmsn_pm_yn, psnal_lmt_sbmsn_stng_yn, rslt_rls_yn, srvy_ymd_indct_yn, srvy_url, sbmsn_end_msg, clct_agre_use_yn, del_yn, reg_dt, rgtr_id, mdfcn_dt, mdfr_id)
select custco.plt_func_get_tbl_seq(custco_id, 'SRVY_ID'), custco_id, null, null, '설문 확장항목 구성(삭제)', null, null, 'Y', 'N', NULL, '설문 확장항목 구성(삭제)', 'TERMIAT', 'Y', NULL, 'N', NULL, 'Y', null, null, 'N', 'Y', to_char(now(), 'yyyymmddhh24miss'), 2, null, 2 from plt_custco;

INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN, srvy_id) 
select custco.plt_func_get_tbl_seq(custco_id, 'ATTR_ID'), custco_id, 'N', 'SRVY', NULL, '구분1', '구분1', 'CUTT_TYPE_1', 'TXT', '100', NULL, 'Y', 'Y', 'N', 'Y', 'N', '1', NULL, NULL, 2, to_char(now(), 'yyyymmddhh24miss'), NULL, NULL, NULL, NULL, 'N', 'Y', 0 from plt_custco;

INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN, srvy_id) 
select custco.plt_func_get_tbl_seq(custco_id, 'ATTR_ID'), custco_id, 'N', 'SRVY', NULL, '구분2', '구분2', 'CUTT_TYPE_2', 'TXT', '100', NULL, 'Y', 'N', 'N', 'Y', 'N', '2', NULL, NULL, 2, to_char(now(), 'yyyymmddhh24miss'), NULL, NULL, NULL, NULL, 'N', 'Y', 0 from plt_custco;

INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN, srvy_id) 
select custco.plt_func_get_tbl_seq(custco_id, 'ATTR_ID'), custco_id, 'N', 'SRVY', NULL, '상담사', '상담사', 'CUSL_NM', 'TXT', '100', NULL, 'Y', 'N', 'N', 'Y', 'N', '3', NULL, NULL, 2, to_char(now(), 'yyyymmddhh24miss'), NULL, NULL, NULL, NULL, 'N', 'Y', 0 from plt_custco;

INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN, srvy_id) 
select custco.plt_func_get_tbl_seq(custco_id, 'ATTR_ID'), custco_id, 'N', 'SRVY', NULL, '상담일시', '상담일시', 'CUTT_DATETIME', 'TXT', '100', NULL, 'Y', 'N', 'N', 'Y', 'N', '4', NULL, NULL, 2, to_char(now(), 'yyyymmddhh24miss'), NULL, NULL, NULL, NULL, 'N', 'Y', 0 from plt_custco;

INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN, srvy_id) 
select custco.plt_func_get_tbl_seq(custco_id, 'ATTR_ID'), custco_id, 'N', 'SRVY', NULL, '상담유형', '상담유형', 'CUTT_TY', 'TXT', '100', NULL, 'Y', 'N', 'N', 'Y', 'N', '5', NULL, NULL, 2, to_char(now(), 'yyyymmddhh24miss'), NULL, NULL, NULL, NULL, 'N', 'Y', 0 from plt_custco;




INSERT INTO plt_eml_tmpl
(eml_tmpl_id, tmpl_clsf_id, tmpl_type_cd, tmpl_nm, tmpl_cn, file_group_key, sort_ord, use_yn, del_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
VALUES(0, 2, 'TP_SVY', '설문조사', '<div style="padding: 20px; background: #CCC;">
  <div style="max-width: 650px; margin: 0 auto; padding: 20px; background: #FFF; border-radius: 12px; font-size: 14px;">
    안녕하세요. LH주거복지정보 입니다.<br>
    아래에 표시된 ‘설문조사 바로가기’ 버튼을 클릭한 후 설문조사에 참여해주시기 바랍니다.<br>
    <div style="text-align: center; margin-top: 24px;">
      <a href="#{LINK}" target="_blank" rel="noopener" style="display: inline-block; padding:12px; border-radius: 4px; background: #4f46e5; color: #FFF; text-decoration: none;">
        설문조사 바로가기
      </a>
    </div>
    <p style="margin-top: 48px;">
      ※ 설문조사는 상담서비스 개선을 위한 자료로만 활용됩니다<br>
    </p>
    <table width="100%" cellpadding="0" cellspacing="0" border="0" style="margin-top: 50px; border-top: 1px solid #ddd;">
      <tr>
	    <td style="width: 150px;" align="center">
	      <img align="center" border="0" src="https://micc.hkpalette.com/upload/images/hkcloud_1/chat/2024/04/04/1712216322556_5bbe4f0f9b47433b8f1ab15754315168.png" alt="lh주거복지정보" title="lh주거복지정보"
              style="outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 124px;"
              width="124" />
	    </td>
	    <td>
	      <div style="font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;">
		    <p>대표전화 : ##{CUTT_TYPE_1,CENTER_TEL}##<br>COPYRIGHT ⓒ 엘에이치주거복지정보(주) Rights Reserved.</p>
	      </div>
	    </td>
      </tr>
    </table>
  </div>
</div>', NULL, 1, 'Y', 'N', 2, '20240313171643', 2, '20240313171643');


--발신 전화번호 목록 선택을 위한 공통 코드 추가.
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt, cd_nm_eng)
VALUES('CUSL_PHN_NO', '16708572', 43, '바로처리', '', NULL, 'N', 'Y', 'N', 1, '1', '20240502144938', '1', '20240502161218', NULL);
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt, cd_nm_eng)
VALUES('CUSL_PHN_NO', '16700002', 43, '전세임대', '', NULL, 'N', 'Y', 'N', 2, '1', '20240502144839', '1', '20240502161220', NULL);
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt, cd_nm_eng)
VALUES('CUSL_PHN_NO', '16700003', 43, '공기해소', '', NULL, 'N', 'Y', 'N', 3, '1', '20240502144856', '1', '20240502161234', NULL);
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt, cd_nm_eng)
VALUES('CUSL_PHN_NO', '16707004', 43, '공동주택', '', NULL, 'N', 'Y', 'N', 4, '1', '20240502144926', '1', '20240502161257', NULL);
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt, cd_nm_eng)
VALUES('CUSL_PHN_NO', '16708004', 43, '렌트홈', '', NULL, 'N', 'Y', 'N', 5, '1', '20240502144926', '1', '20240502161257', NULL);
INSERT INTO plt_comm_cd
(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt, cd_nm_eng)
VALUES('CUSL_PHN_NO', '16702288', 43, '유스타트', '', NULL, 'N', 'Y', 'N', 6, '1', '20240502144913', '1', '20240502161315', NULL);
