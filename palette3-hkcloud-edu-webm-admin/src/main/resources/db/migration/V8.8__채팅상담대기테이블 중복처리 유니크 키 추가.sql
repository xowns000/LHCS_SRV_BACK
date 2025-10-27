ALTER TABLE PLT_CHT_RDY
ADD CONSTRAINT unique_cust_id_custco_id_sndr_key_chn_type_cd
UNIQUE (cust_id, custco_id, sndr_key, chn_type_cd);
