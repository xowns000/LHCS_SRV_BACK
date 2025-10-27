CREATE OR REPLACE FUNCTION custco.plt_func_get_tbl_seq(p_cert_custco_id bigint, p_col_id character varying)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
	/*
	 * 인증 고객사의 schema의 테이블 시퀀스 조회 펑션
	 * p_cert_custco_id : custco.plt_cert_custco.cert_custco_id
	 * p_col_id : schema_id.plt_tbl_seq.col_id
	 * */
	declare
		v_schema_id varchar(200);
		v_seq_vl int8;
		v_cnt integer;
	
	begin
		select schema_id
			into v_schema_id
		from custco.plt_cert_custco
		where cert_custco_id = p_cert_custco_id;
		
		if v_schema_id != '' then 
			-- schema_id 로 조회할 schema 설정.
			execute 'set schema '''|| v_schema_id || '''';
		
			-- col_id로 데이터가 있는지 조회 - 있으면 조회 후 값 없데이트, 없으면 신규 insert 및 값 1 리턴.
			select count(*) into v_cnt from plt_tbl_seq pts where col_id = p_col_id;
		
			if v_cnt = 0::integer then
				insert into plt_tbl_seq(col_id, seq_vl)
				values(p_col_id, 2);
			
				v_seq_vl = 1::int8;
			else
				select seq_vl into v_seq_vl from plt_tbl_seq pts where col_id = p_col_id;
				update plt_tbl_seq set 
					seq_vl = seq_vl + 1
				where col_id = p_col_id;
			end if;
		
		end if;
		
		-- 원래의 custco 스키마로 세팅 - 안하면 상단의 변경된 schema로 유지
		-- 유지 하는 것이 plt_proc_add_custco_data에서 쓸 때 더 이득
		-- set schema 'custco';
	
		
		return v_seq_vl;

	END;
$function$
;


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
			 FROM "'||SCHEMA_RECORD.SCHEMA_ID||'".PLT_CUSTCO PC
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


CREATE OR REPLACE FUNCTION custco.getusercustcolist(p_lgn_id character varying)
 RETURNS TABLE(_schema_id character varying, cd bigint, cd_nm character varying, asp_cust_key character varying, api_uri character varying, user_id bigint, ext_no character varying, pds_ext_no character varying, dsptch_no text, custco_icon text, cert_custco_id bigint, srvc_gds_id bigint, srvc_info text, lang_info text, omni_inst_nm text)
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
	dynamic_query := 'SELECT A.SCHEMA_ID, A.CD, A.CD_NM, A.ASP_CUST_KEY, A.API_URI, A.USER_ID, A.EXT_NO, A.PDS_EXT_NO, A.DSPTCH_NO, A.CUSTCO_ICON, A.CERT_CUSTCO_ID, A.SRVC_GDS_ID, A.SRVC_INFO, A.LANG_INFO, A.OMNI_INST_NM FROM (';

	FOR SCHEMA_RECORD IN
		SELECT DISTINCT SCHEMA_ID
		FROM CUSTCO.PLT_CERT_CUSTCO PCC
		WHERE PCC.SRVC_STTS_CD = 'ON' --서비스 중인 고객사
		AND EXISTS (SELECT 1 FROM CUSTCO.PLT_CERT_USER CU INNER JOIN CUSTCO.PLT_CERT_CUSTCO_USER PCCU ON PCCU.CERT_USER_ID = CU.CERT_USER_ID WHERE PCCU.CERT_CUSTCO_ID = PCC.CERT_CUSTCO_ID AND LGN_ID = P_LGN_ID)
	LOOP
		
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
				, (SELECT ARRAY_TO_STRING(ARRAY_AGG(DSPTCH_NO), '','') FROM PLT_CUSTCO_DSPTCH_NO WHERE CUSTCO_ID = PC.CUSTCO_ID) AS DSPTCH_NO
				, PCCU.SORT_ORD
				, COALESCE(PF.FILE_PATH || ''/'' || PF.STRG_FILE_NM,'''') AS CUSTCO_ICON
				, PCC.CERT_CUSTCO_ID
				, PCC.SRVC_GDS_ID
				, (SELECT ARRAY_TO_STRING(ARRAY_AGG(SRVC_GDS_DTL_CD||'',''||COALESCE((SELECT USE_YN FROM CUSTCO.PLT_CERT_CUSTCO_SRVC WHERE CERT_CUSTCO_ID = PCC.CERT_CUSTCO_ID AND SRVC_GDS_DTL_ID = PCSGD.SRVC_GDS_DTL_ID), ''N'')), ''_##_'') FROM CUSTCO.PLT_CERT_SRVC_GDS_DTL PCSGD WHERE PCSGD.SRVC_GDS_ID = PCC.SRVC_GDS_ID) AS SRVC_INFO
				, (SELECT ARRAY_TO_STRING(ARRAY_AGG((SELECT CD_NM FROM CUSTCO.PLT_COMM_CD WHERE GROUP_CD_ID = ''LOCALE'' AND CD_ID = PCCL.LANG_CD)||'',''||LANG_CD||'',''||YMD_EXPSR_MTHD_CD||'',''||(SELECT CD_VL FROM CUSTCO.PLT_COMM_CD WHERE GROUP_CD_ID = ''YMD_SE'' AND CD_ID = PCCL.YMD_SE_CD)||'',''||BSC_LANG_YN), ''_##_'') FROM CUSTCO.PLT_CERT_CUSTCO_LANG PCCL WHERE PCCL.CERT_CUSTCO_ID = PCC.CERT_CUSTCO_ID) AS LANG_INFO
				, (SELECT ARRAY_TO_STRING(ARRAY_AGG(INST_CD||'',''||INST_NM ORDER BY SORT_ORD), ''_##_'') FROM CUSTCO.PLT_CERT_CUSTCO_SRVC_INST PCCS WHERE PCCS.CERT_CUSTCO_ID = PCC.CERT_CUSTCO_ID AND EXISTS (SELECT 1 FROM CUSTCO.PLT_CERT_SRVC_GDS_DTL PCSGD WHERE PCSGD.SRVC_GDS_ID = PCC.SRVC_GDS_ID AND PCSGD.SRVC_GDS_DTL_CD = ''OMNIONE'' AND PCSGD.SRVC_GDS_DTL_ID = PCCS.SRVC_GDS_DTL_ID)) AS OMNI_INST_NM
			FROM CUSTCO.PLT_CERT_CUSTCO_USER PCCU
			INNER JOIN CUSTCO.PLT_CERT_USER PCU ON PCU.CERT_USER_ID = PCCU.CERT_USER_ID
			INNER JOIN CUSTCO.PLT_CERT_CUSTCO PCC ON PCC.CERT_CUSTCO_ID = PCCU.CERT_CUSTCO_ID
			INNER JOIN "'||SCHEMA_RECORD.SCHEMA_ID||'".PLT_CUSTCO PC ON PC.CUSTCO_ID = PCC.CUSTCO_ID
			INNER JOIN "'||SCHEMA_RECORD.SCHEMA_ID||'".PLT_USER_OGNZ PUO ON PUO.CUSTCO_ID = PC.CUSTCO_ID AND PUO.USE_YN = ''Y''
			INNER JOIN "'||SCHEMA_RECORD.SCHEMA_ID||'".PLT_USER PU ON PU.USER_ID = PUO.USER_ID AND PU.LGN_ID = PCU.LGN_ID
			LEFT OUTER JOIN "'||SCHEMA_RECORD.SCHEMA_ID||'".PLT_PHN_IP_EXT PPIE ON PPIE.CUSTCO_ID = PC.CUSTCO_ID AND PPIE.USER_ID = PU.USER_ID
			LEFT OUTER JOIN CUSTCO.PLT_FILE PF ON PF.FILE_GROUP_KEY = PCC.ICON_FILE_GROUP_KEY
			WHERE PCU.LGN_ID = '''||P_LGN_ID||'''
			';
		
		loopCnt := loopCnt + 1;
		
	END LOOP;

	dynamic_query := dynamic_query || ') A ORDER BY A.ORD, A.SORT_ORD';
	
	RAISE NOTICE 'dynamic_query: %', dynamic_query;

	RETURN QUERY EXECUTE dynamic_query;
END;
$function$
;


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
	dynamic_query := dynamic_query || '    	SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_GROUP_DTL A ';
	dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_GROUP B ON B.LIST_GROUP_ID = A.LIST_GROUP_ID ';
	dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = B.LIST_ID ';
	dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
	dynamic_query := dynamic_query || '  	WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
	dynamic_query := dynamic_query || ' UNION ';
	dynamic_query := dynamic_query || ' 	SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T';
	dynamic_query := dynamic_query || '		WHERE T.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
	dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || ' 	SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_THUMNAIL A';
    dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
	dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '		WHERE A. RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || ' 	SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_STTS A';
    dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
	dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
   	dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || ' 	SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_DWN_GROUP_DTL A ';
    dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_DWN_GROUP B ON B.LIST_DWN_GROUP_ID = A.LIST_DWN_GROUP_ID ';
	dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = B.LIST_ID ';
	dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || ' 	SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_SRCH A ';
    dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
	dynamic_query := dynamic_query || ' 	             INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
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
    dynamic_query := dynamic_query || '    	SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_GROUP_DTL A ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_GROUP B ON B.LIST_GROUP_ID = A.LIST_GROUP_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = B.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '      WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T';
    dynamic_query := dynamic_query || '	    WHERE T.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_THUMNAIL A';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '	    WHERE A. RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_STTS A';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
   	dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_DWN_GROUP_DTL A ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_DWN_GROUP B ON B.LIST_DWN_GROUP_ID = A.LIST_DWN_GROUP_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = B.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.RSPNS_ARTCL_ID IN ( SELECT RSPNS_ARTCL_ID FROM CUSTCO.PLT_LKAG_RSPNS_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' UNION ';
    dynamic_query := dynamic_query || '     SELECT T.LAYOUT_ID FROM "'||SCHEMA_ID||'".PLT_LAYOUT_LIST_SRCH A ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_LIST C ON C.LIST_ID = A.LIST_ID ';
    dynamic_query := dynamic_query || '                  INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT_TAB T ON T.TAB_ID = C.TAB_ID ';
    dynamic_query := dynamic_query || '     WHERE A.PARAM_ARTCL_ID IN ( SELECT PARAM_ARTCL_ID FROM CUSTCO.PLT_LKAG_PARAM_ARTCL WHERE LKAG_ID = '||lkag_id||')' ;
    dynamic_query := dynamic_query || ' ) TBL ';
    dynamic_query := dynamic_query || ' INNER JOIN "'||SCHEMA_ID||'".PLT_LAYOUT PL ON PL.LAYOUT_ID = TBL.LAYOUT_ID ';
EXECUTE dynamic_query INTO v_names;
return v_names;
END;
$function$
;

--------------------------------------------
-- 연동항목을 사용중인 스키마별 배치 테이블사용여부 조회하기.
--------------------------------------------
CREATE OR REPLACE FUNCTION custco.get_use_lkag_batch(schema_id character varying, lkag_id bigint)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
    declare dynamic_query text;
   v_cnt bigint := 0;
BEGIN
    dynamic_query := 'SELECT COUNT(*) FROM "'||SCHEMA_ID||'".PLT_CUSTCO_CHN_BBS_STNG A ';
    dynamic_query := dynamic_query || ' WHERE (A.INQ_API_ID ='||lkag_id||' OR A.REG_API_ID = '||lkag_id||' OR A.ORDR_INQ_API_ID = '||lkag_id||' OR A.GDS_INQ_API_ID = '||lkag_id||')' ;
EXECUTE dynamic_query INTO v_cnt;
return v_cnt;
END;
$function$
;

--------------------------------------------
-- 연동항목을 사용중인 스키마별 배치 명 조회하기.
--------------------------------------------
CREATE OR REPLACE FUNCTION custco.get_use_lkag_batch_names(schema_id character varying, lkag_id bigint)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
    declare dynamic_query text;
   v_names text;
BEGIN
    dynamic_query := 'SELECT STRING_AGG(CJM.JOB_NM, '', '') FROM "'||SCHEMA_ID||'".PLT_CUSTCO_CHN_BBS_STNG A ';
    dynamic_query := dynamic_query || ' INNER JOIN "'||SCHEMA_ID||'".PLT_CLCT_JOB_MNG CJM ON CJM.SNDR_KEY = A.SNDR_KEY ' ;
    dynamic_query := dynamic_query || ' WHERE (A.INQ_API_ID ='||lkag_id||' OR A.REG_API_ID = '||lkag_id||' OR A.ORDR_INQ_API_ID = '||lkag_id||' OR A.GDS_INQ_API_ID = '||lkag_id||')' ;
EXECUTE dynamic_query INTO v_names;
return v_names;
END;
$function$
;


CREATE OR REPLACE FUNCTION custco.getcustcoexpsnattrlist()
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE
	SCHEMA_RECORD RECORD;
	dynamic_query text;
	result_row record;
	loopCnt int ;
	V_CNT INT;
BEGIN
	
	loopCnt := 1;
	dynamic_query := 'SELECT CUSTCO_NM, SCHEMA_ID, CUSTCO_ID, SRVC_STTS_CD, ATTR_ID, EXPSN_ATTR_NM, SE, EXPSN_ATTR_COL_ID, DATA_TYPE_CD, GROUP_CD_ID, INDI_INFO_ENCPT_YN FROM (';
	
	FOR SCHEMA_RECORD IN
		SELECT DISTINCT
			PCC.SCHEMA_ID
			, PCC.SRVC_STTS_CD
		FROM custco.PLT_CERT_CUSTCO PCC
		WHERE PCC.SRVC_CRT_DT IS NOT NULL --스키마 생성 여부
		AND PCC.CUSTCO_ID IS NOT NULL
	LOOP
		
		SELECT COUNT(*) INTO V_CNT  FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = LOWER(SCHEMA_RECORD.SCHEMA_ID) AND TABLE_NAME = LOWER('PLT_EXPSN_ATTR') AND COLUMN_NAME = LOWER('INDI_INFO_ENCPT_YN');
		
		IF V_CNT > 0 THEN
		
			RAISE NOTICE 'loopCnt: %', loopCnt;
			RAISE NOTICE 'dynamic_query: %', dynamic_query;
		
			IF loopCnt > 1 THEN
				dynamic_query := dynamic_query || ' UNION ALL ';
			END IF;
			
			dynamic_query := dynamic_query || 'SELECT
							(SELECT CO_NM FROM "'||SCHEMA_RECORD.SCHEMA_ID||'".PLT_CUSTCO WHERE CUSTCO_ID = PEA.CUSTCO_ID) AS CUSTCO_NM
							, '''||SCHEMA_RECORD.SCHEMA_ID||''' AS SCHEMA_ID
							, PEA.CUSTCO_ID
							, '''||SCHEMA_RECORD.SRVC_STTS_CD||''' AS SRVC_STTS_CD
							, PEA.ATTR_ID
							, PEA.EXPSN_ATTR_NM
							, PEA.SE
							, PEA.EXPSN_ATTR_COL_ID
							, PEA.DATA_TYPE_CD
							, PEA.GROUP_CD_ID
							, PEA.INDI_INFO_ENCPT_YN
						FROM "'||SCHEMA_RECORD.SCHEMA_ID||'".PLT_EXPSN_ATTR PEA
						WHERE BSC_PVSN_ATTR_YN = ''N''
						AND PEA.INDI_INFO_ENCPT_YN = ''Y''
			';
		
			loopCnt := loopCnt + 1;
		END IF;
		
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
