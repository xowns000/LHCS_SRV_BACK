CREATE TABLE plt_cht_cutt_dtl_expsn (
cht_cutt_id bigint NOT NULL,
attr_id bigint NOT NULL,
attr_vl text NULL,
CONSTRAINT plt_cht_cutt_dtl_expsn_pkey PRIMARY KEY (cht_cutt_id, attr_id)
);

COMMENT ON TABLE plt_cht_cutt_dtl_expsn IS '채팅_상담_상세_확장';
COMMENT ON COLUMN plt_cht_cutt_dtl_expsn.cht_cutt_id IS '채팅_상담_ID';
COMMENT ON COLUMN plt_cht_cutt_dtl_expsn.attr_id IS '속성_ID';
COMMENT ON COLUMN plt_cht_cutt_dtl_expsn.attr_vl IS '속성_값';
