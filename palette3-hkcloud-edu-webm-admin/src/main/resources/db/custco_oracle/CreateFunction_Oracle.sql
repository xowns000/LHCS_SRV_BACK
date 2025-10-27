

CREATE OR REPLACE FUNCTION getAllSchemaCustco()
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE
	SCHEMA_RECORD RECORD;
	dynamic_query text;
	result_row record;
	loopCnt int ;
BEGIN
	
	loopCnt := 1;
	dynamic_query := 'SELECT SCHEMA_ID, ASP_CUST_KEY, CUSTCO_ID, CO_NM FROM (';
	
	FOR SCHEMA_RECORD IN
		SELECT DISTINCT
			PCC.SCHEMA_ID
			, PCC.ASP_CUST_KEY
		FROM custco.PLT_CERT_CUSTCO PCC
		WHERE PCC.SRVC_CRT_DT IS NOT NULL
		AND PCC.SRVC_STTS_CD = 'ON' --서비스 중인 고객사
	LOOP
		
		IF loopCnt > 1 THEN
			dynamic_query := dynamic_query || ' UNION ALL ';
		END IF;
		
		dynamic_query := dynamic_query || 'SELECT '
				''''||SCHEMA_RECORD.SCHEMA_ID||''' AS SCHEMA_ID
			 	, PC.CUSTCO_ID
			 	, PC.CO_NM
			 	, PC.ASP_CUST_KEY
			 FROM '||SCHEMA_RECORD.SCHEMA_ID||'.PLT_CUSTCO PC
			 WHERE PC.ASP_CUST_KEY = '''||SCHEMA_RECORD.ASP_CUST_KEY||'''
			';
		
		loopCnt := loopCnt + 1;
		
	END LOOP;

	dynamic_query := dynamic_query || ') A';
	
	RAISE NOTICE 'dynamic_query: %', dynamic_query;

  -- 동적 쿼리 실행 및 결과 반환
  FOR result_row IN EXECUTE dynamic_query LOOP
    RETURN NEXT result_row;
  END LOOP;

  RETURN;
END;
$function$
;


CREATE OR REPLACE TYPE USER_CUSTCO_TYPE AS OBJECT (
    SCHEMA_ID	VARCHAR2(200 BYTE),
    CD	NUMBER(19,0),
    CD_NM	VARCHAR2(200 BYTE),
    ASP_CUST_KEY	VARCHAR2(60 BYTE),
    API_URI	VARCHAR2(300 BYTE),
    USER_ID	NUMBER(19,0),
    EXT_NO	VARCHAR2(60 BYTE),
    PDS_EXT_NO	VARCHAR2(60 BYTE),
    DSPTCH_NO	VARCHAR2(1000 BYTE),
    CUSTCO_ICON	VARCHAR2(2500 BYTE),
    CERT_CUSTCO_ID	NUMBER(19,0),
    SRVC_GDS_ID	NUMBER(19,0),
    SRVC_INFO	VARCHAR2(500 BYTE),
    LANG_INFO	VARCHAR2(500 BYTE),
    OMNI_INST_NM	VARCHAR2(500 BYTE)
);

CREATE OR REPLACE TYPE USER_CUSTCO_TABLE AS TABLE OF USER_CUSTCO_TYPE;


create or replace FUNCTION        getusercustcolist(p_lgn_id in varchar2)
 RETURN USER_CUSTCO_TABLE
IS
	V_RESULT USER_CUSTCO_TABLE;
	dynamic_query clob;
	loopCnt int ;
BEGIN

	loopCnt := 1;
	dynamic_query := 'SELECT USER_CUSTCO_TYPE(A.SCHEMA_ID, A.CD, A.CD_NM, A.ASP_CUST_KEY, A.API_URI, A.USER_ID, A.EXT_NO, A.PDS_EXT_NO, A.DSPTCH_NO, A.CUSTCO_ICON, A.CERT_CUSTCO_ID, A.SRVC_GDS_ID, A.SRVC_INFO, A.LANG_INFO, A.OMNI_INST_NM) FROM (';



	FOR SCHEMA_RECORD IN (
		SELECT DISTINCT SCHEMA_ID
		FROM CUSTCO.PLT_CERT_CUSTCO PCC
		WHERE PCC.SRVC_STTS_CD = 'ON' --서비스 중인 고객사
		AND EXISTS (SELECT 1 FROM CUSTCO.PLT_CERT_USER CU INNER JOIN CUSTCO.PLT_CERT_CUSTCO_USER PCCU ON PCCU.CERT_USER_ID = CU.CERT_USER_ID WHERE PCCU.CERT_CUSTCO_ID = PCC.CERT_CUSTCO_ID AND LGN_ID = P_LGN_ID)
    ) LOOP

		IF loopCnt > 1 THEN
			dynamic_query := dynamic_query || ' UNION ALL ';
		END IF;

		dynamic_query := dynamic_query || 'SELECT '
				||loopCnt||' AS ORD
				, PCC.SCHEMA_ID
				, PU.USER_ID
				, PC.CUSTCO_ID AS CD
				, PC.CO_NM AS CD_NM
				, PC.ASP_CUST_KEY
				, PC.API_URI
				, PPIE.EXT_NO
				, PPIE.PDS_EXT_NO
				, (SELECT LISTAGG(DSPTCH_NO, '','') WITHIN GROUP ( ORDER BY NULL) FROM PLT_CUSTCO_DSPTCH_NO WHERE CUSTCO_ID = PC.CUSTCO_ID) AS DSPTCH_NO
				, PCCU.SORT_ORD
				, NVL(PF.FILE_PATH || ''/'' || PF.STRG_FILE_NM,'''') AS CUSTCO_ICON
				, PCC.CERT_CUSTCO_ID
				, PCC.SRVC_GDS_ID
				, (SELECT LISTAGG(SRVC_GDS_DTL_CD||'',''||NVL((SELECT USE_YN FROM CUSTCO.PLT_CERT_CUSTCO_SRVC WHERE CERT_CUSTCO_ID = PCC.CERT_CUSTCO_ID AND SRVC_GDS_DTL_ID = PCSGD.SRVC_GDS_DTL_ID), ''N''), ''_##_'') WITHIN GROUP ( ORDER BY NULL) FROM CUSTCO.PLT_CERT_SRVC_GDS_DTL PCSGD WHERE PCSGD.SRVC_GDS_ID = PCC.SRVC_GDS_ID) AS SRVC_INFO
				, (SELECT LISTAGG((SELECT CD_NM FROM CUSTCO.PLT_COMM_CD WHERE GROUP_CD_ID = ''LOCALE'' AND CD_ID = PCCL.LANG_CD)||'',''||LANG_CD||'',''||YMD_EXPSR_MTHD_CD||'',''||(SELECT CD_VL FROM CUSTCO.PLT_COMM_CD WHERE GROUP_CD_ID = ''YMD_SE'' AND CD_ID = PCCL.YMD_SE_CD)||'',''||BSC_LANG_YN, ''_##_'') WITHIN GROUP ( ORDER BY NULL) FROM CUSTCO.PLT_CERT_CUSTCO_LANG PCCL WHERE PCCL.CERT_CUSTCO_ID = PCC.CERT_CUSTCO_ID) AS LANG_INFO
				, (SELECT LISTAGG(INST_CD||'',''||INST_NM, ''_##_'') WITHIN GROUP (ORDER BY SORT_ORD) FROM CUSTCO.PLT_CERT_CUSTCO_SRVC_INST PCCS WHERE PCCS.CERT_CUSTCO_ID = PCC.CERT_CUSTCO_ID AND EXISTS (SELECT 1 FROM CUSTCO.PLT_CERT_SRVC_GDS_DTL PCSGD WHERE PCSGD.SRVC_GDS_ID = PCC.SRVC_GDS_ID AND PCSGD.SRVC_GDS_DTL_CD = ''OMNIONE'' AND PCSGD.SRVC_GDS_DTL_ID = PCCS.SRVC_GDS_DTL_ID)) AS OMNI_INST_NM
			FROM CUSTCO.PLT_CERT_CUSTCO_USER PCCU
			INNER JOIN CUSTCO.PLT_CERT_USER PCU ON PCU.CERT_USER_ID = PCCU.CERT_USER_ID
			INNER JOIN CUSTCO.PLT_CERT_CUSTCO PCC ON PCC.CERT_CUSTCO_ID = PCCU.CERT_CUSTCO_ID
			INNER JOIN C##'||SCHEMA_RECORD.SCHEMA_ID||'.PLT_CUSTCO PC ON PC.CUSTCO_ID = PCC.CUSTCO_ID
			INNER JOIN C##'||SCHEMA_RECORD.SCHEMA_ID||'.PLT_USER_OGNZ PUO ON PUO.CUSTCO_ID = PC.CUSTCO_ID AND PUO.USE_YN = ''Y''
			INNER JOIN C##'||SCHEMA_RECORD.SCHEMA_ID||'.PLT_USER PU ON PU.USER_ID = PUO.USER_ID AND PU.LGN_ID = PCU.LGN_ID
			LEFT OUTER JOIN C##'||SCHEMA_RECORD.SCHEMA_ID||'.PLT_PHN_IP_EXT PPIE ON PPIE.CUSTCO_ID = PC.CUSTCO_ID AND PPIE.USER_ID = PU.USER_ID
			LEFT OUTER JOIN CUSTCO.PLT_FILE PF ON PF.FILE_GROUP_KEY = PCC.ICON_FILE_GROUP_KEY
			WHERE PCU.LGN_ID = '''||P_LGN_ID||'''
			';

		loopCnt := loopCnt + 1;

	END LOOP;

	dynamic_query := dynamic_query || ') A ORDER BY A.ORD, A.SORT_ORD';
    DBMS_OUTPUT.PUT_LINE('dynamic_query=' || dynamic_query);
    EXECUTE IMMEDIATE dynamic_query BULK COLLECT INTO V_RESULT;

	RETURN V_RESULT;
END;


CREATE EXTENSION IF NOT EXISTS tablefunc SCHEMA CUSTCO;

CREATE EXTENSION IF NOT EXISTS pg_trgm SCHEMA CUSTCO;

CREATE EXTENSION IF NOT EXISTS pgcrypto SCHEMA CUSTCO; --암복호화

CREATE EXTENSION IF NOT EXISTS hstore SCHEMA CUSTCO;



--------------------------------------------
-- 연동항목을 사용중인 스키마별 layout테이블의 카운트 조회하기.
--------------------------------------------
CREATE OR REPLACE FUNCTION custco.get_use_lkag_layout(schema_id character varying, lkag_id bigint)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
    declare dynamic_query text;
   v_cnt bigint := 0;
BEGIN
    dynamic_query := 'SELECT COUNT(LAYOUT_ID) FROM ( ';
    dynamic_query := dynamic_query || '    	SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_GROUP_DTL A ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST_GROUP B ON B.LIST_GROUP_ID = A.LIST_GROUP_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = B.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '      WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_TAB T';
    dynamic_query := dynamic_query || '	    WHERE T.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_THUMNAIL A';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '	    WHERE A. RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_STTS A';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
   	dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_DWN_GROUP_DTL A ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST_DWN_GROUP B ON B.LIST_DWN_GROUP_ID = A.LIST_DWN_GROUP_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = B.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_SRCH A ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.PARAM_ARTCL_ID IN ( SELECT PARAM_ARTCL_ID FROM CUSTCO.PLT_LKAG_PARAM_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
   	dynamic_query := dynamic_query || ' ) TBL ';
EXECUTE dynamic_query INTO v_cnt;
return v_cnt;
END;
$function$
;

--------------------------------------------
-- 연동항목을 사용중인 스키마별 layout테이블의 레이아웃 명칭조회
--------------------------------------------
CREATE OR REPLACE FUNCTION custco.get_use_lkag_layout_names(schema_id character varying, lkag_id bigint)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
    declare dynamic_query text;
   v_names text;
BEGIN
    dynamic_query := 'SELECT STRING_AGG(PL.LAYOUT_NM, '', '') FROM ( ';
    dynamic_query := dynamic_query || '    	SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_GROUP_DTL A ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST_GROUP B ON B.LIST_GROUP_ID = A.LIST_GROUP_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = B.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '      WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_TAB T';
    dynamic_query := dynamic_query || '	    WHERE T.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_THUMNAIL A';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '	    WHERE A. RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_STTS A';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
   	dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_DWN_GROUP_DTL A ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST_DWN_GROUP B ON B.LIST_DWN_GROUP_ID = A.LIST_DWN_GROUP_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = B.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM '||SCHEMA_ID||'.PLT_LAYOUT_LIST_SRCH A ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.PARAM_ARTCL_ID IN ( SELECT PARAM_ARTCL_ID FROM CUSTCO.PLT_LKAG_PARAM_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' ) TBL ';
    dynamic_query := dynamic_query || ' INNER JOIN '||SCHEMA_ID||'.PLT_LAYOUT PL ON PL.LAYOUT_ID = TBL.LAYOUT_ID ';
EXECUTE dynamic_query INTO v_names;
return v_names;
END;
$function$
;

--------------------------------------------
-- 연동항목을 사용중인 스키마별 배치 테이블사용여부 조회하기.
--------------------------------------------
CREATE OR REPLACE FUNCTION custco.get_use_lkag_batch(schema_id character VARYING, lkag_id bigint)
 RETURNS int
 LANGUAGE plpgsql
AS $function$
    declare dynamic_query text;
   v_cnt bigint := 0;
BEGIN
    dynamic_query := 'SELECT COUNT(*) FROM '||SCHEMA_ID||'.PLT_CUSTCO_CHN_BBS_STNG A ';
    dynamic_query := dynamic_query || ' WHERE (A.INQ_API_ID ='||lkag_id||' OR A.REG_API_ID = '||lkag_id||' OR A.ORDR_INQ_API_ID = '||lkag_id||' OR A.GDS_INQ_API_ID = '||lkag_id||')' ;
EXECUTE dynamic_query INTO v_cnt;
return v_cnt;
END;
$function$
;

--------------------------------------------
-- 연동항목을 사용중인 스키마별 배치 명 조회하기.
--------------------------------------------
CREATE OR REPLACE FUNCTION custco.get_use_lkag_batch_names(schema_id character VARYING, lkag_id bigint)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
    declare dynamic_query text;
   v_names text;
BEGIN
    dynamic_query := 'SELECT STRING_AGG(CJM.JOB_NM, '', '') FROM '||SCHEMA_ID||'.PLT_CUSTCO_CHN_BBS_STNG A ';
    dynamic_query := dynamic_query || ' INNER JOIN '||SCHEMA_ID||'.PLT_CLCT_JOB_MNG CJM ON CJM.SNDR_KEY = A.SNDR_KEY ' ;
    dynamic_query := dynamic_query || ' WHERE (A.INQ_API_ID ='||lkag_id||' OR A.REG_API_ID = '||lkag_id||' OR A.ORDR_INQ_API_ID = '||lkag_id||' OR A.GDS_INQ_API_ID = '||lkag_id||')' ;
EXECUTE dynamic_query INTO v_names;
return v_names;
END;
$function$
;
