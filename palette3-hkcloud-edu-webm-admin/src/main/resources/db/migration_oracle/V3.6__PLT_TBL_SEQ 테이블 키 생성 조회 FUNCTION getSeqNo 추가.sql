create or replace FUNCTION getSeqNo(p_col_id in varchar2)
 RETURN number
 is 
    PRAGMA AUTONOMOUS_TRANSACTION;
    v_seq_vl number;
    v_cnt number;

begin
    select count(*) into v_cnt from plt_tbl_seq pts where col_id = p_col_id;

    if v_cnt = 0 then
        insert into plt_tbl_seq(col_id, seq_vl)
        values(p_col_id, 2);

        v_seq_vl := 1;
    else
        select seq_vl into v_seq_vl from plt_tbl_seq pts where col_id = p_col_id;
        update plt_tbl_seq set 
            seq_vl = seq_vl + 1
        where col_id = p_col_id;
    end if;
    commit;

    return v_seq_vl;
END;