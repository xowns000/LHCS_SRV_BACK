ALTER TABLE plt_mts_sndng_hstry ADD IF NOT EXISTS atalk_tran_sndng_cd bpchar(1) NULL;
COMMENT ON COLUMN plt_mts_sndng_hstry.atalk_tran_sndng_cd IS '알림톡_전환_발송_코드';
ALTER TABLE plt_mts_sndng_hstry ADD IF NOT EXISTS atalk_tran_sndng_yn bpchar(1) NULL;
COMMENT ON COLUMN plt_mts_sndng_hstry.atalk_tran_sndng_yn IS '알림톡_전환_발송_여부';
ALTER TABLE plt_mts_sndng_hstry ADD IF NOT EXISTS atalk_tran_sndng_cn varchar(4000) NULL;
COMMENT ON COLUMN plt_mts_sndng_hstry.atalk_tran_sndng_cn IS '알림톡_전환_발송_내용';
ALTER TABLE plt_mts_sndng_hstry ADD IF NOT EXISTS atalk_tran_sndng_rslt_cd varchar(60) NULL;
COMMENT ON COLUMN plt_mts_sndng_hstry.atalk_tran_sndng_rslt_cd IS '알림톡_전환_발송_결과_코드';
