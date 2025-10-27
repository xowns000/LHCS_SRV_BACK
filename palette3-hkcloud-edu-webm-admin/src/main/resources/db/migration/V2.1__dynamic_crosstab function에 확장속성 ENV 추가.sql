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
		    FROM custco.crosstab(
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
	
		WHEN SE = 'ENV' THEN --설정
    			
	  	-- 동적 쿼리 생성
		dynamic_query := format('
		    SELECT *
		    FROM custco.crosstab(
		      ''SELECT PCS.CUSTCO_ID, PEA.EXPSN_ATTR_COL_ID::TEXT, PCS.ATTR_VL::TEXT
		         FROM PLT_CUSTCO_STNG PCS
				 LEFT OUTER JOIN PLT_EXPSN_ATTR PEA ON PEA.ATTR_ID = PCS.ATTR_ID
		         WHERE PEA.CUSTCO_ID = '''''||CUSTCO_ID||'''''::INT
				 AND PEA.SE = '''''||SE||'''''
				 AND PEA.DEL_YN = ''''N''''
				 AND PEA.BSC_PVSN_ATTR_YN = ''''N''''
				 AND PEA.SCRN_EXPSR_YN = ''''Y''''
		         ORDER BY PCS.CUSTCO_ID, PEA.SORT_ORD'',
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
		    ) AS ct (CUSTCO_ID INT, %s text)',
			column_list
		);
	
		WHEN SE = 'CAMP' THEN --캠페인
				
	  	-- 동적 쿼리 생성
		dynamic_query := format('
		    SELECT *
		    FROM custco.crosstab(
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