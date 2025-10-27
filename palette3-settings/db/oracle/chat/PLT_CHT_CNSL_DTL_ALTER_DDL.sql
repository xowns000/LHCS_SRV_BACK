
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_CHT_CUTT_DTL    채팅_고객상담이력상세
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
-- PLT_CHT_CUTT_DTL FILE_GROUP_KEY : 추가
ALTER TABLE PLT_CHT_CUTT_DTL  
ADD (FILE_GROUP_KEY VARCHAR2(90 BYTE) );

COMMENT ON COLUMN PLT_CHT_CUTT_DTL.FILE_GROUP_KEY IS '파일그룹키';

-- PLT_CHT_CUTT_DTL FILE_GROUP_KEY : 추가
ALTER TABLE PLT_CHT_USER_RDY_DTL  
ADD (FILE_GROUP_KEY VARCHAR2(90 BYTE) );

COMMENT ON COLUMN PLT_CHT_USER_RDY_DTL.FILE_GROUP_KEY IS '파일그룹키';