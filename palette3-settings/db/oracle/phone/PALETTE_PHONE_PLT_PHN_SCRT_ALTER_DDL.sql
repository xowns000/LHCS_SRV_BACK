
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_PHN_SCRT    전화_스크립트
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
-- FILE_GROUP_KEY : 30BYTE -> 90BYTE로 변경
ALTER TABLE PLT_PHN_SCRT  
MODIFY (FILE_GROUP_KEY VARCHAR2(90 BYTE) );