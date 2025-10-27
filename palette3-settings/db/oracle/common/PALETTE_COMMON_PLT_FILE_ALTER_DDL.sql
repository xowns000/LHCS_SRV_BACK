
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
--■PLT_FILE    공통첨부파일정보
--■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
-- FILE_ACS_TYPE_CD 컬럼 추가
ALTER TABLE PLT_FILE 
ADD (FILE_ACS_TYPE_CD VARCHAR2(20) DEFAULT 'PRIVATE' NOT NULL);

COMMENT ON COLUMN PLT_FILE.FILE_ACS_TYPE_CD IS '파일엑세스유형(PUBLIC:공개,PRIVATE:비공개)';