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
	dynamic_query := 'SELECT SCHEMA_ID, CD, CD_NM, ASP_CUST_KEY, API_URI, EXT_NO, PDS_EXT_NO, DSPTCH_NO FROM (';
	
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





CREATE OR REPLACE FUNCTION dynamic_crosstab(custco_id integer, se character varying, cpi_id integer)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE
	column_list text;
  dynamic_query text;
  result_row record;
BEGIN
	-- 동적으로 생성될 컬럼 목록을 조회
	EXECUTE format('

		SELECT ARRAY_TO_STRING(ARRAY_AGG(X.EXPSN_ATTR_COL_ID ORDER BY X.SORT_ORD), '' text ,'')
		FROM (
			SELECT PEA.EXPSN_ATTR_COL_ID, PEA.SORT_ORD, COALESCE(PEA.CPI_ID, 0) AS CPI_ID
			FROM PLT_EXPSN_ATTR PEA
			WHERE PEA.CUSTCO_ID = '''||CUSTCO_ID||'''::INT
			AND PEA.SE = '''||SE||'''
			AND PEA.DEL_YN = ''N''
			AND PEA.BSC_PVSN_ATTR_YN = ''N''
			AND PEA.SCRN_EXPSR_YN = ''Y''
		) X
		WHERE X.CPI_ID = (CASE WHEN '''||SE||''' = ''CAMP'' THEN '''||CPI_ID||'''::INT ELSE COALESCE(X.CPI_ID, 0) END)	-- 캠페인사용으로 인해 수정
	') INTO column_list;
	
	CASE
    	WHEN SE = 'CUSTOM' THEN --고객
    			
	  	-- 동적 쿼리 생성
		dynamic_query := format('
		    SELECT *
		    FROM crosstab(
		      ''SELECT PCDE.CUST_ID, PEA.EXPSN_ATTR_COL_ID::TEXT, COALESCE(PCC.CD_NM, PCDE.ATTR_VL)::TEXT
		         FROM PLT_CUST_DTL_EXPSN PCDE
				 LEFT OUTER JOIN PLT_EXPSN_ATTR PEA ON PEA.ATTR_ID = PCDE.ATTR_ID
				 LEFT OUTER JOIN PLT_COMM_CD PCC ON PCC.CD_ID = PCDE.ATTR_VL AND PCC.GROUP_CD_ID = PEA.GROUP_CD_ID
		         WHERE PEA.CUSTCO_ID = '''''||CUSTCO_ID||'''''::INT
				 AND PEA.SE = '''''||SE||'''''
				 AND PEA.DEL_YN = ''''N''''
				 AND PEA.BSC_PVSN_ATTR_YN = ''''N''''
				 AND PEA.SCRN_EXPSR_YN = ''''Y''''
		         ORDER BY PCDE.CUST_ID, PEA.SORT_ORD'',
		      ''SELECT
		    		EXPSN_ATTR_COL_ID
		    	FROM (
					SELECT DISTINCT EXPSN_ATTR_COL_ID::TEXT, SORT_ORD
					FROM PLT_EXPSN_ATTR
					WHERE CUSTCO_ID = '''''||CUSTCO_ID||'''''::INT
					AND SE = '''''||SE||'''''
					AND DEL_YN = ''''N''''
					AND BSC_PVSN_ATTR_YN = ''''N''''
					AND SCRN_EXPSR_YN = ''''Y''''
					ORDER BY SORT_ORD
				) A''
		    ) AS ct (CUST_ID INT, %s text)',
			column_list
		);
	
		WHEN SE = 'CAMP' THEN --캠페인
				
	  	-- 동적 쿼리 생성
		dynamic_query := format('
		    SELECT *
		    FROM crosstab(
		      ''SELECT POCCD.CUST_ID, PEA.EXPSN_ATTR_COL_ID::TEXT, COALESCE(PCC.CD_NM, POCCD.ATTR_VL)::TEXT
		         FROM PLT_OBD_CPI_CUST_DTL POCCD
				 LEFT OUTER JOIN PLT_EXPSN_ATTR PEA ON PEA.ATTR_ID = POCCD.ATTR_ID
				 LEFT OUTER JOIN PLT_COMM_CD PCC ON PCC.CD_ID = POCCD.ATTR_VL AND PCC.GROUP_CD_ID = PEA.GROUP_CD_ID
		         WHERE POCCD.CPI_ID = '''''||CPI_ID||'''''::INT
				 AND PEA.CUSTCO_ID = '''''||CUSTCO_ID||'''''::INT
				 AND PEA.SE = '''''||SE||'''''
				 AND PEA.DEL_YN = ''''N''''
				 AND PEA.BSC_PVSN_ATTR_YN = ''''N''''
				 AND PEA.SCRN_EXPSR_YN = ''''Y''''
		         ORDER BY POCCD.CUST_ID, PEA.SORT_ORD'',
		      ''SELECT
		    		EXPSN_ATTR_COL_ID
		    	FROM (
					SELECT DISTINCT EXPSN_ATTR_COL_ID::TEXT, SORT_ORD
					FROM PLT_EXPSN_ATTR PEA
					WHERE PEA.CUSTCO_ID = '''''||CUSTCO_ID||'''''::INT
					AND PEA.SE = '''''||SE||'''''
					AND PEA.DEL_YN = ''''N''''
					AND PEA.BSC_PVSN_ATTR_YN = ''''N''''
					AND PEA.SCRN_EXPSR_YN = ''''Y''''
					AND PEA.CPI_ID = '''''||CPI_ID||'''''
					ORDER BY SORT_ORD
				) A''
		    ) AS ct (CUST_ID INT, %s text)',
			column_list
		);
	
	END CASE;
	
	RAISE NOTICE 'column_list: %', column_list;
	RAISE NOTICE 'dynamic_query: %', dynamic_query;
  -- 동적 쿼리 실행 및 결과 반환
  FOR result_row IN EXECUTE dynamic_query LOOP
    RETURN NEXT result_row;
  END LOOP;

  RETURN;
END;
$function$
;



CREATE OR REPLACE FUNCTION get_first_char(p_txt character varying)
 RETURNS character
 LANGUAGE plpgsql
AS $function$
    declare v_no char := '';
BEGIN

    select case octet_length(p_txt::char(1)) when 3 then
	  case ((((get_byte(decode(p_txt::char(1), 'escape'), 0) & 15) << 12)
	    | ((get_byte(decode(p_txt::char(1), 'escape'), 1) & 63) << 6)
	    | ((get_byte(decode(p_txt::char(1), 'escape'), 2) & 63)))
	    - 44032) / 588
	      when  0 then E'\xe3\x84\xb1'
	      when  1 then E'\xe3\x84\xb2'
	      when  2 then E'\xe3\x84\xb4'
	      when  3 then E'\xe3\x84\xb7'
	      when  4 then E'\xe3\x84\xb8'
	      when  5 then E'\xe3\x84\xb9'
	      when  6 then E'\xe3\x85\x81'
	      when  7 then E'\xe3\x85\x82'
	      when  8 then E'\xe3\x85\x84'
	      when  9 then E'\xe3\x85\x85'
	      when  10 then E'\xe3\x85\x86'
	      when  11 then E'\xe3\x85\x87'
	      when  12 then E'\xe3\x85\x88'
	      when  13 then E'\xe3\x85\x89'
	      when  14 then E'\xe3\x85\x8a'
	      when  15 then E'\xe3\x85\x8b'
	      when  16 then E'\xe3\x85\x8c'
	      when  17 then E'\xe3\x85\x8d'
	      when  18 then E'\xe3\x85\x8e'
	      when -53 then p_txt::char(1)
	      else '??'
	      end
	  else p_txt::char(1)
	  end INTO v_no;

    return v_no;

END;
$function$
;




CREATE OR REPLACE FUNCTION itgrtmsgstatistics(custco_id integer, sch_gb character varying, sch_year character varying, sch_month character varying, sch_quarter character varying, sch_st_dt character varying, sch_end_dt character varying)
 RETURNS SETOF record
 LANGUAGE plpgsql
AS $function$
DECLARE
	dynamic_query text;
	result_row record;
BEGIN
	
	dynamic_query := 'SELECT
						COALESCE(SEND_DT, ''0'') AS SEND_DT
						, SUM(COALESCE(ATALK::INT, 0)) AS ATALK
						, SUM(COALESCE(ATALK_FAIL::INT, 0)) AS ATALK_FAIL
						, SUM(COALESCE(ATALK_SUCCESS::INT, 0)) AS ATALK_SUCCESS
						, SUM(COALESCE(FTALK::INT, 0)) AS FTALK
						, SUM(COALESCE(FTALK_FAIL::INT, 0)) AS FTALK_FAIL
						, SUM(COALESCE(FTALK_SUCCESS::INT, 0)) AS FTALK_SUCCESS
						, SUM(COALESCE(SMS::INT, 0)) AS SMS
						, SUM(COALESCE(SMS_FAIL::INT, 0)) AS SMS_FAIL
						, SUM(COALESCE(SMS_SUCCESS::INT, 0)) AS SMS_SUCCESS
						, SUM(COALESCE(MMS::INT, 0)) AS MMS
						, SUM(COALESCE(MMS_FAIL::INT, 0)) AS MMS_FAIL
						, SUM(COALESCE(MMS_SUCCESS::INT, 0)) AS MMS_SUCCESS
						, SUM(COALESCE(LMS::INT, 0)) AS LMS
						, SUM(COALESCE(LMS_FAIL::INT, 0)) AS LMS_FAIL
						, SUM(COALESCE(LMS_SUCCESS::INT, 0)) AS LMS_SUCCESS
					FROM CROSSTAB(
					  ''SELECT
							A.SEND_DT
							, A.SNDNG_SE_CD
							, SUM(COALESCE(A.CNT, 0)) AS CNT
						FROM (
						SELECT
							SUBSTRING(SNDNG_DT, 1, 8) AS SEND_DT
							, SNDNG_SE_CD
							, COUNT(*) AS CNT
						FROM PLT_MTS_SNDNG_HSTRY
						WHERE CUSTCO_ID = ' || CUSTCO_ID;
					
RAISE NOTICE 'dynamic_query1: %', dynamic_query;

	CASE
		WHEN SCH_GB = 'DAY' THEN
			dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN '''''||SCH_ST_DT||'000000'''' AND '''''||SCH_END_DT||'235959''''';
		WHEN SCH_GB = 'MONTH' THEN
			dynamic_query = dynamic_query || ' AND SUBSTRING(SNDNG_DT, 1, 6) = CONCAT('''''||SCH_YEAR||''''', '''''||SCH_MONTH||''''')';
		WHEN SCH_GB = 'QUARTER' THEN
			CASE
				WHEN SCH_QUARTER = '1' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''0101000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''0331235959'''')';
				WHEN SCH_QUARTER = '2' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''0401000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''0630235959'''')';
				WHEN SCH_QUARTER = '3' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''0701000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''0930235959'''')';
				WHEN SCH_QUARTER = '4' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''1001000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''1231235959'''')';
			END CASE;
	END CASE;

RAISE NOTICE 'dynamic_query2: %', dynamic_query;
	
	dynamic_query = dynamic_query || ' GROUP BY SUBSTRING(SNDNG_DT, 1, 8), SNDNG_SE_CD
										UNION ALL
										SELECT
											SUBSTRING(SNDNG_DT, 1, 8) AS SEND_DT
											, SNDNG_SE_CD || ''''_SUCCESS''''
											, SUM(CASE WHEN RSLT_CD = ''''0000'''' THEN 1 ELSE 0 END) AS CNT
										FROM PLT_MTS_SNDNG_HSTRY
										WHERE CUSTCO_ID = ' || CUSTCO_ID;
									
RAISE NOTICE 'dynamic_query3: %', dynamic_query;

	CASE
		WHEN SCH_GB = 'DAY' THEN
			dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN '''''||SCH_ST_DT||'000000'''' AND '''''||SCH_END_DT||'235959''''';
		WHEN SCH_GB = 'MONTH' THEN
			dynamic_query = dynamic_query || ' AND SUBSTRING(SNDNG_DT, 1, 6) = CONCAT('''''||SCH_YEAR||''''', '''''||SCH_MONTH||''''')';
		WHEN SCH_GB = 'QUARTER' THEN
			CASE
				WHEN SCH_QUARTER = '1' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''0101000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''0331235959'''')';
				WHEN SCH_QUARTER = '2' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''0401000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''0630235959'''')';
				WHEN SCH_QUARTER = '3' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''0701000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''0930235959'''')';
				WHEN SCH_QUARTER = '4' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''1001000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''1231235959'''')';
			END CASE;
	END CASE;

RAISE NOTICE 'dynamic_query4: %', dynamic_query;
	
	dynamic_query = dynamic_query || ' GROUP BY SUBSTRING(SNDNG_DT, 1, 8), SNDNG_SE_CD || ''''_SUCCESS''''
										UNION ALL
										SELECT
											SUBSTRING(SNDNG_DT, 1, 8) AS SEND_DT
											, SNDNG_SE_CD || ''''_FAIL''''
											, COUNT(*) - SUM(CASE WHEN RSLT_CD = ''''0000'''' THEN 1 ELSE 0 END) AS CNT
										FROM PLT_MTS_SNDNG_HSTRY
										WHERE CUSTCO_ID = ' || CUSTCO_ID;
									
RAISE NOTICE 'dynamic_query5: %', dynamic_query;
									
	CASE
		WHEN SCH_GB = 'DAY' THEN
			dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN '''''||SCH_ST_DT||'000000'''' AND '''''||SCH_END_DT||'235959''''';
		WHEN SCH_GB = 'MONTH' THEN
			dynamic_query = dynamic_query || ' AND SUBSTRING(SNDNG_DT, 1, 6) = CONCAT('''''||SCH_YEAR||''''', '''''||SCH_MONTH||''''')';
		WHEN SCH_GB = 'QUARTER' THEN
			CASE
				WHEN SCH_QUARTER = '1' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''0101000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''0331235959'''')';
				WHEN SCH_QUARTER = '2' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''0401000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''0630235959'''')';
				WHEN SCH_QUARTER = '3' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''0701000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''0930235959'''')';
				WHEN SCH_QUARTER = '4' THEN
					dynamic_query = dynamic_query || ' AND SNDNG_DT BETWEEN CONCAT('''''||SCH_YEAR||''''',''''1001000000'''') AND CONCAT('''''||SCH_YEAR||''''',''''1231235959'''')';
			END CASE;
	END CASE;

RAISE NOTICE 'dynamic_query6: %', dynamic_query;
	
	dynamic_query = dynamic_query || ' GROUP BY SUBSTRING(SNDNG_DT, 1, 8), SNDNG_SE_CD || ''''_FAIL''''
										) A
										GROUP BY A.SEND_DT, A.SNDNG_SE_CD
										ORDER BY A.SNDNG_SE_CD '',
									''SELECT
										SNDNG_SE_CD
									FROM (
										SELECT
											SNDNG_SE_CD
										FROM (
											SELECT DISTINCT CD_ID::TEXT AS SNDNG_SE_CD, SORT_ORD
											FROM PLT_COMM_CD
											WHERE GROUP_CD_ID = ''''MTS_TP''''
											ORDER BY SORT_ORD
										) A
										UNION ALL
										SELECT
											SNDNG_SE_CD || ''''_SUCCESS''''
										FROM (
											SELECT DISTINCT CD_ID::TEXT AS SNDNG_SE_CD, SORT_ORD
											FROM PLT_COMM_CD
											WHERE GROUP_CD_ID = ''''MTS_TP''''
											ORDER BY SORT_ORD
										) A
										UNION ALL
										SELECT
											SNDNG_SE_CD || ''''_FAIL''''
										FROM (
											SELECT DISTINCT CD_ID::TEXT AS SNDNG_SE_CD, SORT_ORD
											FROM PLT_COMM_CD
											WHERE GROUP_CD_ID = ''''MTS_TP''''
											ORDER BY SORT_ORD
										) A
									) A
									ORDER BY SNDNG_SE_CD''
								) AS CT (SEND_DT VARCHAR, ATALK INT, ATALK_FAIL INT, ATALK_SUCCESS INT, FTALK INT, FTALK_FAIL INT, FTALK_SUCCESS INT, SMS TEXT, SMS_FAIL INT, SMS_SUCCESS INT, MMS INT, MMS_FAIL INT, MMS_SUCCESS INT, LMS INT, LMS_FAIL INT, LMS_SUCCESS INT)
								GROUP BY CT.SEND_DT';
	
	RAISE NOTICE 'dynamic_query7: %', dynamic_query;
  -- 동적 쿼리 실행 및 결과 반환
  FOR result_row IN EXECUTE dynamic_query LOOP
    RETURN NEXT result_row;
  END LOOP;

  RETURN;
END;
$function$
;


CREATE EXTENSION IF NOT EXISTS tablefunc;
