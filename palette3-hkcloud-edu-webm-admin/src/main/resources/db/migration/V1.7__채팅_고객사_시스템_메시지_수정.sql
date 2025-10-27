/*
공통 기초 데이터 수정
*/
update plt_cht_sys_msg set
MSG_CN='첨부 가능한 이미지 파일 크기는 최대 5MB입니다. 이미지 파일 크기 확인 후 재전송 부탁드립니다.'
where SYS_MSG_ID=25;


/*
고객사별 기초 데이터 수정
줄바꿈 문자로 인한 메세지 발송 오류 처리
고객사별 데이터 수정건으로 plt_proc_add_custco_data 프로시저도 수정
 - 기존 생성된 고객사는 이 flyway sql 로 반영.
 - 신규 고객사 추가 시에는 프로시저에서 수정된 내용으로 반영.
*/
update PLT_CHT_CUSTCO_SYS_MSG set
MSG_CN='첨부 가능한 이미지 파일 크기는 최대 5MB입니다. 이미지 파일 크기 확인 후 재전송 부탁드립니다.'
where SYS_MSG_ID=25;