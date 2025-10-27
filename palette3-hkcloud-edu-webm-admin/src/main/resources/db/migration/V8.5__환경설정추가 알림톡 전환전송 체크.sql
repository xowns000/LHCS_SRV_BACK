INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) 
select GETSEQNO('ATTR_ID'), custco_id, 'N', 'ENV', NULL, '알림톡 전환전송 체크 여부', '알림톡 전환전송 체크박스가 기본 체크됩니다.', 'ENV_ATALK_TRAN_SEND_CHECK_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', (select max(sort_ord)+1 from PLT_EXPSN_ATTR where se='ENV'), NULL, NULL, 2, to_char(now(), 'yyyymmddhh24miss'), NULL, NULL, NULL, NULL, 'N', 'Y' from plt_custco;

INSERT INTO plt_custco_stng
(custco_id, attr_id, attr_vl)
select pc.custco_id, (select attr_id from PLT_EXPSN_ATTR where se='ENV' and expsn_attr_col_id='ENV_ATALK_TRAN_SEND_CHECK_YN' and custco_id = pc.custco_id), 'N' from plt_custco pc;