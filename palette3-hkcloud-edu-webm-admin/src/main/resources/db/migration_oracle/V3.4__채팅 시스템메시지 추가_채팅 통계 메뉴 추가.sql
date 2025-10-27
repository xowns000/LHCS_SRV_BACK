INSERT INTO PLT_CHT_SYS_MSG
(sys_msg_id, msg_se_cd, rcptn_dsptch_cd, msg_type_cd, msg_hr, msg_cn, msg_expln, lnk_type_cd, use_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
VALUES(28, 'SYSMSG', 'SND', 'GENMSG', 0, '안녕하세요! 문의유형을 선택해주시면 채팅상담을 시작하겠습니다.', '문의유형 자동인사', NULL, 'Y', 2, TO_CHAR(sysdate,'YYYYMMDDHH24MISS'), 2, TO_CHAR(sysdate,'YYYYMMDDHH24MISS'));
INSERT INTO PLT_CHT_SYS_MSG
(sys_msg_id, msg_se_cd, rcptn_dsptch_cd, msg_type_cd, msg_hr, msg_cn, msg_expln, lnk_type_cd, use_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
VALUES(29, 'SYSMSG', 'SND', 'GENMSG', 0, '안녕하세요! #{고객사}입니다!', '자동인사', NULL, 'Y', 2, TO_CHAR(sysdate,'YYYYMMDDHH24MISS'), 2, TO_CHAR(sysdate,'YYYYMMDDHH24MISS'));
UPDATE PLT_TBL_SEQ SET SEQ_VL = '30' WHERE COL_ID = 'SYS_MSG_ID';

UPDATE PLT_MENU SET SORT_ORD = 2, MENU_EXPLN = '상담직원별 SNS 상담 집계 현황을 확인할 수 있습니다' WHERE PATH_NM = '/STA_M0301';
UPDATE PLT_MENU SET SORT_ORD = 3, MENU_EXPLN = '문의유형별 SNS 상담 집계현황을 확인할 수 있습니다' WHERE PATH_NM = '/STA_M0302';
UPDATE PLT_MENU SET SORT_ORD = 4, MENU_NM = '채팅상담 유형별 통계', MENU_EXPLN = '상담유형별 SNS 상담 집계현황을 확인할 수 있습니다' WHERE PATH_NM = '/STA_M0303';
UPDATE PLT_MENU SET SORT_ORD = 5, MENU_EXPLN = '일자별 SNS 상담 집계현황을 확인할 수 있습니다' WHERE PATH_NM = '/STA_M0304';
UPDATE PLT_MENU SET SORT_ORD = 6, MENU_EXPLN = 'SNS 상담 통합생산성 집계현황을 확인할 수 있습니다' WHERE PATH_NM = '/STA_M0305';

INSERT INTO PLT_MENU
(MENU_ID, UP_MENU_ID, LNKG_MENU_ID, MENU_SE_CD_LVL, MENU_NM, MENU_EXPLN, POPUP_FILE_NM, PRGRM_PARAM, VIEW_TRGT, PATH_NM, POPUP_WD_SZ, POPUP_HT_SZ, ICON_CLASS_NM, ACT_NM, INQ_AUTHRT, PRCS_AUTHRT, DEL_AUTHRT, OTPT_AUTHRT, EXCL_AUTHRT, ADD_AUTHRT, USE_YN, DEL_YN, SORT_ORD, RGTR_ID, REG_DT, MDFR_ID, MDFCN_DT)
VALUES((SELECT MAX(MENU_ID) FROM PLT_MENU) + 1, (SELECT MENU_ID FROM PLT_MENU WHERE PATH_NM = '/STA_M0300'), NULL, 3, 'SNS상담종합통계', '월별 SNS상담 이력 정보를 SNS별, 상담시간별, 일별 추이 현황에 대한 통계를 확인할 수 있습니다', NULL, NULL, 'MAIN', '/STA_M0306', NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y', 'N', 1, 2, TO_CHAR(sysdate,'YYYYMMDDHH24MISS'), 2, TO_CHAR(sysdate,'YYYYMMDDHH24MISS'));
UPDATE PLT_TBL_SEQ SET SEQ_VL = (SELECT MAX(MENU_ID) FROM PLT_MENU) + 1 WHERE COL_ID = 'MENU_ID';


CREATE OR REPLACE FUNCTION getusercustcolist(p_lgn_id character varying)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE
	SCHEMA_RECORD RECORD;
	shema_list text;
	dynamic_query text;
	result_row record;
	loopCnt int ;
BEGIN
	
	loopCnt := 1;
	dynamic_query := 'SELECT SCHEMA_ID, CD, CD_NM, ASP_CUST_KEY, API_URI, USER_ID, EXT_NO, PDS_EXT_NO, DSPTCH_NO FROM (';
	
	FOR SCHEMA_RECORD IN
		SELECT
			PCC.SCHEMA_ID
			, PCC.ASP_CUST_KEY
		FROM custco.PLT_CERT_CUSTCO_USER PCCU
		INNER JOIN custco.PLT_CERT_CUSTCO PCC ON PCC.CERT_CUSTCO_ID = PCCU.CERT_CUSTCO_ID AND PCC.SRVC_STTS_CD = 'ON' --서비스 중인 고객사
		LEFT OUTER JOIN CUSTCO.PLT_CERT_IPCC_SRVR PCIS ON PCIS.IPCC_SRVR_ID = PCC.IPCC_SRVR_ID
		WHERE EXISTS (SELECT 1 FROM custco.PLT_CERT_USER WHERE CERT_USER_ID = PCCU.CERT_USER_ID AND LGN_ID = P_LGN_ID)
	LOOP
		
		IF loopCnt > 1 THEN
			dynamic_query := dynamic_query || ' UNION ALL ';
		END IF;
		
		dynamic_query := dynamic_query || 'SELECT '
				||loopCnt||' AS ORD
			 	, '''||SCHEMA_RECORD.SCHEMA_ID||''' AS SCHEMA_ID
				, PUO.USER_ID
			 	, PC.CUSTCO_ID AS CD
			 	, PC.CO_NM AS CD_NM
			 	, PC.ASP_CUST_KEY
			 	, PC.API_URI
			 	, PPIE.EXT_NO
			 	, PPIE.PDS_EXT_NO
			 	, (SELECT ARRAY_TO_STRING(ARRAY_AGG(DSPTCH_NO), '','') FROM PLT_CUSTCO_DSPTCH_NO WHERE CUSTCO_ID = PC.CUSTCO_ID) AS DSPTCH_NO
				, PUO.SORT_ORD
			 FROM '||SCHEMA_RECORD.SCHEMA_ID||'.PLT_USER_OGNZ PUO 
			 INNER JOIN '||SCHEMA_RECORD.SCHEMA_ID||'.PLT_CUSTCO PC ON PC.CUSTCO_ID = PUO.CUSTCO_ID
			 LEFT OUTER JOIN '||SCHEMA_RECORD.SCHEMA_ID||'.PLT_PHN_IP_EXT PPIE ON PPIE.CUSTCO_ID = PC.CUSTCO_ID AND PPIE.USER_ID = PUO.USER_ID
			 WHERE PUO.USE_YN = ''Y''
			 AND PUO.USER_ID = (SELECT USER_ID FROM '||SCHEMA_RECORD.SCHEMA_ID||'.PLT_USER WHERE LGN_ID = '''||P_LGN_ID||''')
			';
		
		loopCnt := loopCnt + 1;
		
	END LOOP;

	dynamic_query := dynamic_query || ') A ORDER BY A.ORD, A.SORT_ORD';
	
	RAISE NOTICE 'dynamic_query: %', dynamic_query;

  -- 동적 쿼리 실행 및 결과 반환
  FOR result_row IN EXECUTE dynamic_query LOOP
    RETURN NEXT result_row;
  END LOOP;

  RETURN;
END;
$function$
;