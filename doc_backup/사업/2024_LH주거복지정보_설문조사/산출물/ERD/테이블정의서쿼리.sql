--테이블목록표
SELECT 
ROW_NUMBER() OVER (ORDER BY t.tablename) as "번호"
, upper(t.tablename) as "테이블ID"
, (SELECT PD.DESCRIPTION AS TABLE_COMMENT
  FROM PG_STAT_USER_TABLES PS
      ,PG_DESCRIPTION      PD
 WHERE PS.RELNAME  = t.tablename 
 	and ps.schemaname = t.schemaname
   AND PS.RELID   = PD.OBJOID
   AND PD.OBJSUBID  = 0) as "테이블명"
, pg_total_relation_size(t.tablename::varchar) as "테이블 사이즈"
FROM pg_catalog.pg_tables t
where t.schemaname ='lhcstest'
	and t.tablename not like 'chatbot%'
	and t.tablename not like 'flyway%'
	and t.tablename not like 'plt_cht%'
	and t.tablename != 'plt_cust'
	and t.tablename not like 'plt_cust\_%'
	and t.tablename not like 'plt_cutt%'
	and t.tablename not like 'plt_kms%'
	and t.tablename not like 'plt_obd%'
	and t.tablename not like 'plt_phn%'
	and t.tablename not like 'plt_qa%'
	and t.tablename not like 'plt_schdl%'
	and t.tablename not like 'plt_short%'
	and t.tablename not like 'plt_sic%'
	and t.tablename not like 'plt_stats%'
	and t.tablename not like 'plt_user_cutt%'
	and t.tablename not like 'plt_voc%';





--테이블 정의서 쿼리
select 
 coalesce((select PD.DESCRIPTION 
from PG_STAT_USER_TABLES PS 
, PG_DESCRIPTION PD 
where PS.RELNAME = T.table_name 
 and PS.schemaname = T.table_schema 
 and PS.RELID = PD.OBJOID 
 and PD.OBJSUBID = 0 ), 'NULL' 
) as "테이블명" 
,   T.ordinal_position as "컬럼순서" 
, upper(T.table_name) as "테이블ID" 
, coalesce((select PD.DESCRIPTION 
from PG_STAT_ALL_TABLES PS 
, PG_DESCRIPTION PD 
, PG_ATTRIBUTE PA 
where PS.SCHEMANAME = T.table_schema 
and PS.RELNAME = T.table_name 
and PA.ATTNAME = T.column_name 
and PS.RELID = PD.OBJOID 
and PD.OBJSUBID <> 0 
and PD.OBJOID = PA.ATTRELID 
and PD.OBJSUBID = PA.ATTNUM ), 'NULL' 
) as "컬럼명" 
, upper(T.column_name) as "컬럼ID" 
, T.UDT_NAME as "데이터타입" 
-- , T.DATA_TYPE 
, case 
when T.data_type_gb = 1 then T.VCHAR 
when T.data_type_gb = 2 then 
case 
when T.SCAL = '0' then T.NUMB 
else T.NUMB || ',' || T.SCAL 
end 
when T.data_type_gb = 3 then null 
end as "컬럼길이" 
, 
( 
select 
case 
when CC.column_name is not null then 'Y' 
else 'N' 
end 
from 
INFORMATION_SCHEMA.TABLE_CONSTRAINTS TC 
, 
INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE CC 
where 
TC.TABLE_CATALOG = T.table_catalog 
and TC.TABLE_NAME = T.table_name 
and TC.TABLE_SCHEMA = T.table_schema 
and TC.CONSTRAINT_TYPE = 'PRIMARY KEY' 
and TC.TABLE_CATALOG = CC.TABLE_CATALOG 
and TC.TABLE_SCHEMA = CC.TABLE_SCHEMA 
and TC.TABLE_NAME = CC.TABLE_NAME 
and TC.CONSTRAINT_NAME = CC.CONSTRAINT_NAME 
and CC.column_name = T.column_name 
) as "PK" 
, 
( 
select 
case 
when count(cu.column_name) > 0 then 'Y' 
else 'N' 
end 
from 
INFORMATION_SCHEMA.TABLE_CONSTRAINTS TC 
JOIN information_schema.key_column_usage CU ON CU.constraint_name = TC.constraint_name and tc.table_name = cu.table_name and TC.TABLE_SCHEMA = cu.TABLE_SCHEMA and TC.TABLE_CATALOG = Cu.TABLE_CATALOG 
where 
cu.ordinal_position=1
and TC.CONSTRAINT_TYPE = 'FOREIGN KEY' 
and TC.TABLE_CATALOG = T.table_catalog 
and TC.TABLE_NAME = T.table_name 
and TC.TABLE_SCHEMA = T.table_schema 
and cu.column_name = T.column_name 
) as "FK" 
, T.NULL_YN as "NULL" 
--, T.DATA_TYPE 
from 
( 
select 
table_catalog 
, table_schema 
, table_name 
, ordinal_position 
, column_name 
, UDT_NAME 
, DATA_TYPE 
, case 
when character_maximum_length is not null then 1 
when numeric_precision is not null then 2 
else 3 
end data_type_gb 
, to_char(case when character_maximum_length is not null then character_maximum_length else null end, 'FM99999999') VCHAR 
, to_char(case when numeric_precision is not null then 
case when numeric_precision > 0 then numeric_precision 
else numeric_precision_radix end 
else null end, 'FM99999999') NUMB 
, to_char(case when numeric_scale > 0 then numeric_scale else 0 end , 'FM99999999') SCAL 
, case 
when is_nullable = 'NO' then 'N' 
else 'Y' 
end NULL_YN 
, column_default 
from 
information_schema.columns 
where 
	table_schema in ('lhcstest') 
	and table_name not like 'chatbot%'
	and table_name not like 'flyway%'
	and table_name not like 'plt_cht%'
	and t.tablename != 'plt_cust'
	and t.tablename not like 'plt_cust\_%'
	and  table_name not like 'plt_cutt%'
	and  table_name not like 'plt_kms%'
	and  table_name not like 'plt_obd%'
	and  table_name not like 'plt_phn%'
	and  table_name not like 'plt_qa%'
	and  table_name not like 'plt_schdl%'
	and  table_name not like 'plt_short%'
	and  table_name not like 'plt_sic%'
	and  table_name not like 'plt_stats%'
	and  table_name not like 'plt_user_cutt%'
	and  table_name not like 'plt_voc%'
order by 
table_catalog 
, table_schema 
, table_name 
, ordinal_position 
) T 
order by 
T.table_catalog 
, T.table_schema 
, T.table_name 
, T.ordinal_position ;