DROP FUNCTION itgrtmsgstatistics(custco_id integer, sch_gb character varying, sch_year character varying, sch_month character varying, sch_quarter character varying, sch_st_dt character varying, sch_end_dt character varying);


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
					FROM custco.CROSSTAB(
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