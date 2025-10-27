CREATE OR REPLACE FUNCTION getSeqNo(p_col_id character varying)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$

	declare
		v_seq_vl int8;
		v_cnt integer;
	
	begin
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
	
		return v_seq_vl;

	END;
$function$
;