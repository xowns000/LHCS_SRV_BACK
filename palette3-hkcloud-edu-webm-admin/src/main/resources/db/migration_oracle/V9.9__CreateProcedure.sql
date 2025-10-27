create or replace PROCEDURE plt_proc_add_custco_data(p_cert_custco_id IN number)
is
	/**
	 * custco의 기업고객관리 정보로 고객사 및 관련 데이터 생성
	 * p_cert_custco_id : custco.plt_cert_custco.cert_custco_id
	 */

    /* 기업고객관리 - 서비스 설정 - 서비스 정보 */
    CURSOR c_srvc is
        select pccs.cert_custco_id , pccs.srvc_gds_dtl_id, pccs.use_yn , pccs.reg_dt, pcsgd.srvc_gds_dtl_cd 
        from custco.plt_cert_custco_srvc pccs 
            left join custco.plt_cert_srvc_gds_dtl pcsgd on pccs.srvc_gds_dtl_id = pcsgd.srvc_gds_dtl_id  
        where cert_custco_id = p_cert_custco_id
        order by pccs.srvc_gds_dtl_id;


    /* 기업고객관리 - IPCC 연동 설정 - 전화번호 정보 */
    CURSOR c_dsptch_no is
        select cert_custco_dsptch_no_id, cert_custco_id, rprs_no, dsptch_no
        from custco.plt_cert_custco_dsptch_no
        where cert_custco_id = p_cert_custco_id
        order by cert_custco_dsptch_no_id;


--    ATTR_RECORD RECORD;


    v_rgtr_id NUMBER;					-- 등록자_ID
    v_reg_dt varchar2(14);			-- 등록_일시
    v_custco_id NUMBER;				-- 인증_고객사_ID
    v_custco_nm varchar2(200);		-- 고객사_명
    v_asp_cust_key varchar2(100);	-- ASP_고객_키
    v_srvc_stts_cd varchar2(60);		-- 서비스_상태_코드
    v_user_acnt_cnt NUMBER;			-- 사용자_계정_수
    v_lgn_id varchar2(100);			-- 로그인_ID
    v_schema_id varchar2(200);		-- 스키마_ID
    v_api_uri varchar2(300);			-- API_URI

    v_user_id NUMBER;					-- 사용자_ID
    v_hkc_user_id NUMBER;				-- superuser 사용자_ID
    v_svy_user_id NUMBER;				-- 설문 사용자_ID
    v_authrt_group_id NUMBER;			-- 권한_그룹_ID
    v_dept_id NUMBER;					-- 부서_ID
    v_tmpl_clsf_id NUMBER;			-- 템플릿_분류_ID
    v_up_tmpl_clsf_id NUMBER;			-- 상위_템플릿_분류_ID
    v_attr_id NUMBER; 				-- 확장속성_ID


begin
    -- 등록자 ID : 2 (system)
    v_rgtr_id := 2;
    v_reg_dt := to_char(SYSDATE,'yyyymmddhh24miss');

    /* 인증_고객사 정보 조회 */
    select custco_nm, asp_cust_key, srvc_stts_cd, user_acnt_cnt, schema_id, pcis.api_uri
        into v_custco_nm, v_asp_cust_key, v_srvc_stts_cd, v_user_acnt_cnt, v_schema_id, v_api_uri
    from custco.plt_cert_custco pcc left join custco.plt_cert_ipcc_srvr pcis on pcc.ipcc_srvr_id = pcis.ipcc_srvr_id 
    where cert_custco_id = p_cert_custco_id;

    /* 로그인_ID 조회 */
    select lgn_id into v_lgn_id
    from custco.plt_cert_user
    where cert_user_id = (
        select min(pcu.cert_user_id) 
        from custco.plt_cert_user pcu left join custco.plt_cert_custco_user pccu 
            on pcu.cert_user_id = pccu.cert_user_id 
        where INSTR(pcu.lgn_id, '#') = 0 AND pccu.cert_custco_id = p_cert_custco_id
    );



    if v_schema_id IS NOT NULL then 
        /* 고객사_ID 생성 */
        --select getSeqNo('CUSTCO_ID') into v_custco_id;
        /* 고객사_ID를 인증_고객사_ID 사용으로 변경 - 전 고객사_ID 유니크하게 관리하기 위함(멀티테넌시 오류 인지), by hjh. */
        v_custco_id := p_cert_custco_id;

        -- 고객사_ID 업데이트 2023.12.20 추가, by hjh.
        update custco.plt_cert_custco set custco_id = v_custco_id where cert_custco_id = p_cert_custco_id;


        -- schema_id 로 추가할 schema 설정.
        --execute 'set schema '''|| v_schema_id || '''';
--        EXECUTE IMMEDIATE ('ALTER SESSION SET CURRENT_SCHEMA = C##'|| v_schema_id);

        /* superUser 사용자_ID 조회 */
        select user_id into v_hkc_user_id from plt_user where lgn_id = 'hkc#'||v_asp_cust_key;

        /* 사용자_ID 조회 (사용자 - 비밀번호 때문에 자바단에서 insert) */
        select user_id into v_user_id from plt_user where lgn_id = v_lgn_id;

        /* 설문 사용자_ID 조회 (설문 사용자 - 비밀번호 때문에 자바단에서 insert) */
        select user_id into v_svy_user_id from plt_user where lgn_id = v_asp_cust_key||'svy';

        /* 고객사 */
        INSERT INTO PLT_CUSTCO(CUSTCO_ID,ASP_CUST_KEY,CO_NM,SRVC_MAINT_YN,RMRK,API_URI,USER_ACNT_CNT,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) 
        VALUES(v_custco_id, v_asp_cust_key, v_custco_nm, (case when v_srvc_stts_cd = 'ON' then 'Y' else 'N' end), '', v_api_uri, v_user_acnt_cnt, v_rgtr_id, to_char(SYSDATE,'yyyymmddhh24miss'), null, null);

        /* 고객사 서비스 - cursor 'c_srvc' loop */
        FOR R IN c_srvc LOOP
            insert into PLT_CUSTCO_SRVC(custco_id, srvc_gds_dtl_cd, use_yn, rgtr_id, reg_dt)
            values(v_custco_id, r.srvc_gds_dtl_cd, r.use_yn, v_rgtr_id, r.reg_dt);
        END LOOP;


        /* 고객사_발신_번호 - cursor 'c_dsptch_no' loop */
        FOR R IN c_dsptch_no LOOP
            insert into PLT_CUSTCO_DSPTCH_NO(custco_dsptch_no_id, custco_id, rprs_no, dsptch_no)
            values(getSeqNo('CUSTCO_DSPTCH_NO_ID'), v_custco_id, r.rprs_no, r.dsptch_no);
        END LOOP;


        /* 권한 추가 - 시스템관리자 */
        -- 권한_그룹_ID 생성 - 시스템관리자 
        select getSeqNo('AUTHRT_GROUP_ID') into v_authrt_group_id from dual;
        -- 권한 추가 - 시스템관리자 
        INSERT INTO PLT_AUTHRT(AUTHRT_GROUP_ID,AUTHRT_GROUP_NM,GROUP_EXPLN,MNGR_PM_CD,USER_SE_CD,SORT_ORD,ETC_INFO_01,ETC_INFO_02,ETC_INFO_03,RGTR_ID,REG_DT,CUSTCO_ID) VALUES(v_authrt_group_id, '시스템관리자', '개발자용 시스템 전체 권한', 'ALOW', 'MANAGER', '1', NULL, NULL, NULL, v_rgtr_id, v_reg_dt, v_custco_id);

        /*사용자_권한 추가 - superuser 계정을 '시스템 관리자'로 등록*/
        INSERT INTO PLT_USER_AUTHRT(AUTHRT_GROUP_ID,USER_ID,RGTR_ID,REG_DT) VALUES(v_authrt_group_id, v_hkc_user_id, v_rgtr_id, v_reg_dt);

        /*사용자_권한 추가 - 시스템 관리자 계정을 '시스템 관리자'로 등록*/
        INSERT INTO PLT_USER_AUTHRT(AUTHRT_GROUP_ID,USER_ID,RGTR_ID,REG_DT) VALUES(v_authrt_group_id, v_user_id, v_rgtr_id, v_reg_dt);

        /*사용자_권한 추가 - 설문 사용자 계정을 '시스템 관리자'로 등록*/
        INSERT INTO PLT_USER_AUTHRT(AUTHRT_GROUP_ID,USER_ID,RGTR_ID,REG_DT) VALUES(v_authrt_group_id, v_svy_user_id, v_rgtr_id, v_reg_dt);

        -- 권한_그룹_프로그램 추가 - 시스템관리자 
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 1, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 2, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 3, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 4, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 5, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 6, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 7, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 8, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 9, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 10, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 11, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 12, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 13, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 14, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 15, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 16, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 17, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 18, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 19, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 20, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 21, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 22, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 23, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 24, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 25, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 26, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 27, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 28, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 29, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 30, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 31, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 32, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 33, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 34, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 35, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 36, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 37, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 38, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 39, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 40, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 41, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 42, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 43, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 44, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 45, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 46, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 47, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 48, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 49, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 50, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 51, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 52, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 53, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 54, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 55, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 56, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 57, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 58, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 59, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 60, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 61, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 62, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 63, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 64, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 65, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 66, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 67, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 68, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 69, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 70, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 71, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 72, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 73, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 74, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 75, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 76, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 77, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 78, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 79, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 80, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 81, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 82, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 83, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 84, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 85, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 86, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 87, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 88, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 89, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 90, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 91, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 92, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 93, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 94, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 95, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 96, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 97, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 98, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 99, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 100, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 101, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 102, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 103, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 114, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 115, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 116, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 117, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 118, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 119, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0500'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0400'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0600'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0700'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=15 and path_nm='/CSL_M0605'), v_rgtr_id, v_reg_dt);

        -- 버튼_권한 - 시스템관리자
        INSERT INTO plt_btn_authrt (authrt_group_id, menu_id, btn_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 1, 2, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_btn_authrt (authrt_group_id, menu_id, btn_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 2, 3, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_btn_authrt (authrt_group_id, menu_id, btn_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 2, 4, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_btn_authrt (authrt_group_id, menu_id, btn_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 49, 5, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_btn_authrt (authrt_group_id, menu_id, btn_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 5, 6, v_rgtr_id, v_reg_dt);
    
    
        -- 권한_그룹_ID 생성 - 관리자
        select getSeqNo('AUTHRT_GROUP_ID') into v_authrt_group_id from dual;
        -- 권한 추가 - 관리자
        INSERT INTO PLT_AUTHRT(AUTHRT_GROUP_ID,AUTHRT_GROUP_NM,GROUP_EXPLN,MNGR_PM_CD,USER_SE_CD,SORT_ORD,ETC_INFO_01,ETC_INFO_02,ETC_INFO_03,RGTR_ID,REG_DT,CUSTCO_ID) VALUES(v_authrt_group_id, '관리자', '고객사 관리용 내부직원', 'ALOW', 'MANAGER', '2', NULL, NULL, NULL, v_rgtr_id, v_reg_dt, v_custco_id);
    
        -- 권한_그룹_프로그램 추가 - 관리자
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 1, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 2, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 3, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 4, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 5, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 6, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 7, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 8, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 9, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 10, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 11, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 12, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 13, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 14, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 15, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 16, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 17, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 18, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 19, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 20, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 21, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 22, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 23, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 24, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 25, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 26, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 27, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 28, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 29, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 30, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 31, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 32, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 33, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 34, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 35, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 36, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 37, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 38, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 39, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 40, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 41, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 42, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 43, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 44, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 45, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 46, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 47, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 48, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 49, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 50, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 51, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 52, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 53, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 54, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 55, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 56, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 57, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 58, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 59, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 60, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 61, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 62, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 63, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 64, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 65, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 66, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 67, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 68, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 69, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 70, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 71, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 72, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 73, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 74, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 75, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 76, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 77, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 78, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 79, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 80, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 81, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 82, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 83, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 85, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 86, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 87, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 88, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 89, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 90, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 91, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 92, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 93, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 94, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 95, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 96, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 97, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 98, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 99, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 100, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 101, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 102, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 103, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 114, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 115, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 116, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 117, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 118, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 119, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0500'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0400'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0600'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0700'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=15 and path_nm='/CSL_M0605'), v_rgtr_id, v_reg_dt);
    
        -- 권한_그룹_ID 생성 - 센터장
        select getSeqNo('AUTHRT_GROUP_ID') into v_authrt_group_id from dual;
        -- 권한 추가 - 센터장
        INSERT INTO PLT_AUTHRT(AUTHRT_GROUP_ID,AUTHRT_GROUP_NM,GROUP_EXPLN,MNGR_PM_CD,USER_SE_CD,SORT_ORD,ETC_INFO_01,ETC_INFO_02,ETC_INFO_03,RGTR_ID,REG_DT,CUSTCO_ID) VALUES(v_authrt_group_id, '센터장', '고객사의 전체 관리자', 'ALOW', 'MANAGER', '3', NULL, NULL, NULL, v_rgtr_id, v_reg_dt, v_custco_id);
    
        -- 권한_그룹_프로그램 추가 - 센터장
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 1, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 2, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 3, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 4, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 5, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 6, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 7, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 8, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 9, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 10, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 11, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 12, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 13, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 14, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 15, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 16, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 17, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 18, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 19, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 20, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 21, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 22, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 23, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 24, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 25, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 26, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 27, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 28, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 29, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 30, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 31, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 32, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 33, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 34, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 35, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 36, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 37, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 38, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 39, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 40, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 41, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 42, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 43, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 44, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 45, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 46, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 47, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 48, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 49, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 50, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 51, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 52, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 53, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 54, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 55, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 56, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 57, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 58, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 59, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 60, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 61, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 62, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 63, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 64, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 65, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 66, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 67, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 68, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 69, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 70, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 71, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 72, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 73, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 74, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 75, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 76, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 77, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 78, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 79, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 80, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 81, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 82, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 83, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 85, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 86, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 87, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 88, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 89, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 90, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 91, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 92, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 93, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 94, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 95, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 96, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 97, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 98, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 99, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 100, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 101, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 102, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 103, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 114, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 115, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 116, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 117, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 118, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 119, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0500'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0400'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0600'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0700'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=15 and path_nm='/CSL_M0605'), v_rgtr_id, v_reg_dt);
    
    
        -- 권한_그룹_ID 생성 - 팀장
        select getSeqNo('AUTHRT_GROUP_ID') into v_authrt_group_id from dual;
        -- 권한 추가 - 팀장
        INSERT INTO PLT_AUTHRT(AUTHRT_GROUP_ID,AUTHRT_GROUP_NM,GROUP_EXPLN,MNGR_PM_CD,USER_SE_CD,SORT_ORD,ETC_INFO_01,ETC_INFO_02,ETC_INFO_03,RGTR_ID,REG_DT,CUSTCO_ID) VALUES(v_authrt_group_id, '팀장', '고객사의 부분 관리자', 'ALOW', 'MANAGER', '4', NULL, NULL, NULL, v_rgtr_id, v_reg_dt, v_custco_id);
    
        -- 권한_그룹_프로그램 추가 - 팀장
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 1, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 2, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 3, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 4, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 5, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 6, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 7, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 8, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 9, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 10, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 11, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 12, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 13, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 14, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 15, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 16, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 17, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 18, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 19, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 20, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 21, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 22, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 23, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 24, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 25, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 26, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 27, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 28, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 29, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 30, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 31, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 32, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 33, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 34, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 35, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 36, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 37, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 38, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 39, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 40, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 41, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 42, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 43, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 44, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 45, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 46, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 47, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 48, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 49, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 50, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 51, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 52, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 53, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 54, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 55, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 56, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 57, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 58, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 59, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 60, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 61, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 62, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 63, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 64, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 65, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 66, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 67, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 68, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 69, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 70, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 71, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 72, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 73, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 74, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 75, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 76, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 77, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 78, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 79, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 80, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 81, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 82, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 83, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 85, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 86, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 87, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 88, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 89, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 90, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 91, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 92, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 93, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 94, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 95, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 96, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 97, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 98, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 99, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 100, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 101, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 102, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 103, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 114, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 115, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 116, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 117, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 118, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 119, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0500'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0400'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0600'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0700'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=15 and path_nm='/CSL_M0605'), v_rgtr_id, v_reg_dt);
    
        
        -- 권한_그룹_ID 생성 - 매니저
        select getSeqNo('AUTHRT_GROUP_ID') into v_authrt_group_id from dual;
        -- 권한 추가 - 매니저
        INSERT INTO PLT_AUTHRT(AUTHRT_GROUP_ID,AUTHRT_GROUP_NM,GROUP_EXPLN,MNGR_PM_CD,USER_SE_CD,SORT_ORD,ETC_INFO_01,ETC_INFO_02,ETC_INFO_03,RGTR_ID,REG_DT,CUSTCO_ID) VALUES(v_authrt_group_id, '매니저', '상담사 관리자', 'ALOW', 'MANAGER', '5', NULL, NULL, NULL, v_rgtr_id, v_reg_dt, v_custco_id);
    
        -- 권한_그룹_프로그램 추가 - 매니저
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 1, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 2, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 3, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 4, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 5, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 6, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 7, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 8, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 9, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 10, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 11, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 12, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 13, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 14, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 15, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 16, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 17, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 18, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 19, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 20, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 21, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 22, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 23, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 24, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 25, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 26, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 27, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 28, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 29, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 30, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 31, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 32, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 33, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 34, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 35, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 36, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 37, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 38, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 39, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 40, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 41, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 42, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 43, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 44, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 45, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 46, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 47, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 48, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 49, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 50, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 51, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 52, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 53, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 54, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 55, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 56, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 57, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 58, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 59, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 60, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 61, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 62, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 63, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 64, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 65, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 66, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 67, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 68, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 69, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 70, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 71, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 72, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 73, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 74, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 75, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 76, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 77, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 78, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 79, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 80, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 81, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 82, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 83, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 85, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 86, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 87, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 88, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 89, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 90, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 91, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 92, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 93, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 94, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 95, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 96, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 97, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 98, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 99, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 100, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 101, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 102, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 103, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 114, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 115, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 116, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 117, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 118, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 119, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0500'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0400'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0600'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0700'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=15 and path_nm='/CSL_M0605'), v_rgtr_id, v_reg_dt);
        
        -- 버튼_권한 - 매니저
        INSERT INTO plt_btn_authrt (authrt_group_id, menu_id, btn_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 5, 6, v_rgtr_id, v_reg_dt);
    
    
        -- 권한_그룹_ID 생성 - 상담사
        select getSeqNo('AUTHRT_GROUP_ID') into v_authrt_group_id from dual;
        -- 권한 추가 - 상담사
        INSERT INTO PLT_AUTHRT(AUTHRT_GROUP_ID,AUTHRT_GROUP_NM,GROUP_EXPLN,MNGR_PM_CD,USER_SE_CD,SORT_ORD,ETC_INFO_01,ETC_INFO_02,ETC_INFO_03,RGTR_ID,REG_DT,CUSTCO_ID) VALUES(v_authrt_group_id, '상담사', '상담직원', 'ALOW', 'GUEST', '6', NULL, NULL, NULL, v_rgtr_id, v_reg_dt, v_custco_id);
    
        -- 권한_그룹_프로그램 추가 - 상담사
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 1, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 2, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 3, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 4, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 5, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 6, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 7, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 8, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 9, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 10, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 11, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 12, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 13, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 14, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 15, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 16, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 17, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 18, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 19, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 20, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 21, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 22, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 23, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 24, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 25, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 26, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 27, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 28, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 29, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 30, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 31, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 32, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 33, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 34, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 35, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 36, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 37, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 38, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 39, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 40, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 41, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 42, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 43, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 44, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 45, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 46, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 47, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 48, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 49, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 50, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 51, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 52, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 53, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 54, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 55, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 56, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 57, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 58, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 59, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 60, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 61, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 62, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 63, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 64, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 65, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 66, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 67, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 68, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 69, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 70, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 71, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 72, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 73, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 74, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 75, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 76, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 77, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 78, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 79, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 80, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 81, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 82, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 83, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 85, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 86, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 87, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 88, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 89, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 90, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 91, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 92, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 93, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 94, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 95, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 96, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 97, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 98, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 99, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 100, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 101, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 102, v_rgtr_id, v_reg_dt);
        /*INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 103, v_rgtr_id, v_reg_dt);*/
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 114, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 115, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 116, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 117, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 118, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, 119, v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0400'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0600'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=33 and path_nm='/SVY_M0700'), v_rgtr_id, v_reg_dt);
        INSERT INTO plt_authrt_group_prgrm (authrt_group_id, menu_id, rgtr_id, reg_dt) VALUES(v_authrt_group_id, (select menu_id from plt_menu where up_menu_id=15 and path_nm='/CSL_M0605'), v_rgtr_id, v_reg_dt);
    
        
        /* 상담_유형 */
        INSERT INTO plt_cutt_type
        (cutt_type_id, custco_id, up_cutt_type_id, cutt_type_se_cd, cutt_type_nm, cutt_type_lvl, use_yn, sort_ord, rgtr_id, reg_dt, cutt_type_expln, del_yn, CUSTCO_DSPTCH_NO_ID)
        VALUES(getSeqNo('CUTT_TYPE_ID'), v_custco_id, NULL, NULL, v_custco_nm, 1, 'Y', 1, v_rgtr_id, v_reg_dt, NULL, 'N', null);
       
    
        /* 상담_유형_설정 */
        INSERT INTO PLT_CUTT_TYPE_STNG(CUTT_TYPE_STNG_ID,CUSTCO_ID,ITGRT_USE_YN,CUTT_TYPE_LMT_LVL_CD,RGTR_ID,REG_DT) 
        VALUES(getSeqNo('CUTT_TYPE_STNG_ID'), v_custco_id, 'Y', '4', v_rgtr_id, v_reg_dt);
        
    
        /* 조직 */
        select getSeqNo('DEPT_ID') into v_dept_id from dual;
    
        INSERT INTO PLT_OGNZ(DEPT_ID, CUSTCO_ID, DEPT_NM, UP_DEPT_ID, DEPT_SE_CD, DEPT_CRT_YMD, DEPT_ABL_YMD, SCRN_INDCT_YN, BLDG_PSTN, BLDG_NM, UDGD_YN, BLDG_NOFL, OFC_NO, USE_YN, DEL_YN, SORT_ORD, RGTR_ID, REG_DT) 
        VALUES(v_dept_id, v_custco_id, v_custco_nm, NULL, 'DEPT', '20230101', '29991231', 'Y', NULL, NULL, 'N', NULL, NULL, 'Y', 'N', '1', v_rgtr_id, v_reg_dt);
        
    
        /* superuser_조직 추가 */
        INSERT INTO PLT_USER_OGNZ(USER_ID, DEPT_ID, CUSTCO_ID, USE_YN, RGTR_ID, REG_DT) 
        VALUES(v_hkc_user_id, v_dept_id, v_custco_id, 'Y', v_rgtr_id, v_reg_dt);
        
        /* 사용자_조직 추가 */
        INSERT INTO PLT_USER_OGNZ(USER_ID, DEPT_ID, CUSTCO_ID, USE_YN, RGTR_ID, REG_DT) 
        VALUES(v_user_id, v_dept_id, v_custco_id, 'Y', v_rgtr_id, v_reg_dt);

        /* 설문 사용자_조직 추가 */
        INSERT INTO PLT_USER_OGNZ(USER_ID, DEPT_ID, CUSTCO_ID, USE_YN, RGTR_ID, REG_DT) 
        VALUES(v_svy_user_id, v_dept_id, v_custco_id, 'Y', v_rgtr_id, v_reg_dt);

    
        /* 게시판 - BBS_ID 고정 */
        INSERT INTO PLT_BBS(BBS_ID,BBS_TYPE_CD,BBS_NM,BBS_EXPLN,NTCMTTR_YN,ANS_PSBLTY_YN,CMNT_PSBLTY_YN,FILE_ATCH_YN,NEW_INDCT_YN,NEW_DAY,USE_YN,RTIME_NTC_YN,CUSTCO_ID,RGTR_ID,REG_DT) 
        VALUES('1', '1', '게시판', '게시판', 'Y', 'Y', 'Y', 'Y', 'Y', '3', 'Y', 'N', v_custco_id, v_rgtr_id, v_reg_dt);
        INSERT INTO PLT_BBS(BBS_ID,BBS_TYPE_CD,BBS_NM,BBS_EXPLN,NTCMTTR_YN,ANS_PSBLTY_YN,CMNT_PSBLTY_YN,FILE_ATCH_YN,NEW_INDCT_YN,NEW_DAY,USE_YN,RTIME_NTC_YN,CUSTCO_ID,RGTR_ID,REG_DT) 
        VALUES('2', '2', '채팅 이미지', '채팅 이미지', 'Y', 'Y', 'Y', 'Y', 'Y', NULL, 'Y', 'Y', v_custco_id, v_rgtr_id, v_reg_dt);

    
        /* 채팅설정 */
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'WORK_START_TIME', '상담시작시간', '0010', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'WORK_END_TIME', '상담종료시간', '2350', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'AUTO_GREETING_YN', '상담사자동인사사용여부', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'CONT_CHATAGENT_YN', '상담허용수일괄적용', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'CONT_CHATAGENT_CNT', '상담허용수일괄적용값', '5', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'CONSULT_ALRAM_YN', '제 3자채팅알림 여부', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'LUNCH_YN', '점심시간 사용여부', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'LUNCH_START_TIME', '점심시작시간', '0110', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'LUNCH_END_TIME', '점심종료시간', '1200', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'ROUTE_CD', '분배상태코드', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'ROUTE_WAITING_CNT', '분배대기인원', '10', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'RECEIVE_IMAGE_YN', '이미지 수신여부', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'RECEIVE_LONG_TXT_YN', '장문 수신여부', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'PROHIBITE_APPLY_YN', '금칙어사용여부', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'AUTO_MESSAGE_YN', '자동응답메시지사용여부', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'SPEC_CNSL_ROUTE', '전문상담만배분', 'NORMAL', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'BEFORE_AGENT_USE_YN', '직전상담사 배분 사용여부', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'BEFORE_AGENT_PERIOD', '직전상담사 배분 기간  ', '0', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'FIXED_ROUTE_INQRY_USE_YN', '지정상담사 배분 고객문의유형 사용여부 ', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'INQRY_TYPE_YN', '고객문의유형', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'INQRY_USE_LVL', '문의유형 사용레벨', '3', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'INQRY_SHOW_YN', '채팅방 문의유형 표시여부', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'CUST_NORESP_USE_YN', '고객무응답메시지사용여부', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'CUST_NORESP_CHATEND', '고객무응답메시지전송후채팅종료', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'CUST_WAIT_MSG_YN', '상담대기메시지 사용여부', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'BETCH_YN', '배치실행여부', 'Y', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'DMND_NPSBL', '상담요청불가건 통계포함여부', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'CUSL_ALTMNT_NPSBL', '상담사 배정불가건 통계포함여부', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'ALTMNT_GIVEUP', '상담요청중 포기건 통계포함여부', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'WAIT_GIVEUP', '상담대기중 포기건 통계포함여부', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
        INSERT INTO PLT_CHT_STNG(STNG_ID,STNG_CD,STNG_NM,STNG_VL,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID) VALUES(getSeqNo('STNG_ID'), 'DMND_NOCHC', '상담요청 미선택건 통계포함여부', 'N', 'Y', v_rgtr_id, v_reg_dt, null, null, v_custco_id);
    
    
        /* 확장_속성 */
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CONSEL', NULL, '상담 결과', '상담 결과', 'CUSL_RS', 'COE', '60', 'CUSL_RS', 'Y', 'Y', 'Y', 'Y', 'N', '1', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CONSEL', NULL, '처리 결과', '처리 결과', 'CAMP_RS', 'COE', '60', 'CAMP_RS', 'Y', 'Y', 'N', 'Y', 'N', '2', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CONSEL', NULL, '상담 유형', '상담 유형', 'CUSL_TP_CL', 'COE', '60', NULL, 'Y', 'Y', 'N', 'Y', 'N', '3', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CONSEL', NULL, '상담 내용', '상담 내용', 'CUSL_CN', 'TXT', '4000', NULL, 'Y', 'Y', 'N', 'Y', 'N', '4', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CONSEL', NULL, '특이사항', '특이사항', 'SPEC_NOTE', 'TXT', '500', NULL, 'Y', 'N', 'Y', 'Y', 'N', '5', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CONSEL', NULL, '예약콜', '예약 콜', 'RSVT_CALL', 'DAT', '14', NULL, 'Y', 'N', 'N', 'Y', 'N', '6', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CONSEL', NULL, '상담 태그', '상담 태그', 'CUSL_TAG', 'HASH', '500', NULL, 'Y', 'N', 'Y', 'Y', 'N', '7', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '직장 전화번호', '직장 전화번호', 'CUST_COM_PHN_NO', 'NUM', '20', NULL, 'Y', 'N', 'Y', 'Y', 'N', '1', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '자택 전화번호', '자택 전화번호', 'CUST_HOME_PHN_NO', 'NUM', '20', NULL, 'Y', 'N', 'Y', 'Y', 'N', '2', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '이메일', '이메일', 'CUST_EMAIL', 'TXT', '200', NULL, 'Y', 'N', 'Y', 'Y', 'N', '3', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '고객 상태', '고객 상태', 'CUST_STAT', 'COE', '60', 'CUST_ST', 'Y', 'Y', 'N', 'Y', 'N', '4', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '고객 구분', '고객 구분', 'CUST_SE', 'COE', '60', 'CUST_DV', 'Y', 'N', 'Y', 'Y', 'N', '5', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '고객 유형', '고객 유형', 'CUST_TP', 'COE', '60', 'CUST_TP', 'Y', 'N', 'Y', 'Y', 'N', '6', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '등급', '등급', 'CUST_LVL', 'COE', '60', 'CUST_RT', 'Y', 'N', 'Y', 'Y', 'N', '7', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '소속', '소속', 'CUST_OGNZ', 'TXT', '200', NULL, 'Y', 'N', 'Y', 'Y', 'N', '8', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '지역', '지역', 'CUST_ZONE', 'COE', '60', 'AREA', 'Y', 'N', 'Y', 'Y', 'N', '9', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '생년월일', '생년월일', 'CUST_BIRTH', 'NUM', '8', NULL, 'Y', 'N', 'Y', 'Y', 'N', '10', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '성별', '성별', 'CUST_GENDER', 'COE', '60', 'GNDR', 'Y', 'N', 'Y', 'Y', 'N', '11', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '자택 우편번호', '자택 우편번호', 'CUST_HOME_POST', 'POST', '60', NULL, 'Y', 'N', 'Y', 'Y', 'N', '12', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '직장 우편번호', '직장 우편번호', 'CUST_COM_POST', 'POST', '60', NULL, 'Y', 'N', 'Y', 'Y', 'N', '13', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '메모 내용', '메모 내용', 'CUST_MEMO', 'TXT', '500', NULL, 'Y', 'N', 'Y', 'Y', 'N', '14', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CUSTOM', NULL, '특이사항', '특이사항', 'CUST_SPEC_NOTE', 'TXT', '500', NULL, 'Y', 'N', 'Y', 'Y', 'N', '15', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CAMP', NULL, '고객명', '고객명', 'CAMP_CUST_NM', 'TXT', '200', NULL, 'Y', 'Y', 'N', 'Y', 'N', '1', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CAMP', NULL, '성별', '성별', 'CAMP_CUST_GNDR', 'COE', '60', 'GNDR', 'Y', 'N', 'Y', 'Y', 'Y', '2', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CAMP', NULL, '연령', '연령', 'CAMP_CUST_AGE', 'COE', '60', 'AGE_RNG', 'Y', 'N', 'Y', 'Y', 'Y', '3', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CAMP', NULL, '지역', '지역', 'CAMP_CUST_LOC', 'COE', '60', 'AREA', 'Y', 'N', 'Y', 'Y', 'Y', '4', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'Y', 'CAMP', NULL, '전화번호', '전화번호', 'CAMP_CUST_PHN_NO', 'TXT', '60', NULL, 'Y', 'Y', 'N', 'Y', 'N', '5', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '템플릿 변경', '템플릿 레이아웃을 변경할 수 있습니다.', 'ENV_TMPL_TP', 'COE', '60', 'LAYOUT_TMPL_TP', 'Y', 'Y', 'N', 'Y', 'N', '1', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '고객핵심정보', '고객핵심정보 표시를 변경할 수 있습니다.', 'ENV_CUST_INFO_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '2', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '콜백', '우측 사이드바의 콜백 목록을 상담직원 관계없이 전체 콜백 건을 조회 합니다.', 'ENV_CLBK_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '3', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '예약콜', '우측 사이드바의 예약콜 목록을 상담직원 관계없이 전체 예약콜 건을 조회 합니다.', 'ENV_RESERVECALL_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '4', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '전화번호 마스킹 설정', '고객 전화번호 일부를 마스킹(****) 처리 합니다.', 'ENV_PHNNO_MASKING_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '5', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '고객명 마스킹 설정', '고객명 일부를 마스킹(****) 처리 합니다.', 'ENV_CUSTNM_MASKING_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '6', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '개인정보 마스킹 설정', '개인정보 일부를 마스킹(****) 처리 합니다.', 'ENV_INDIINFO_MASKING_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '7', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '다른 상담사 녹취 재생 권한', '다른 상담사의 상담 (음성)녹취 파일을 재생할 수 있는 권한 여부를 설정할 수 있습니다.', 'ENV_AUDIO_PLAY_PERM_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '8', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '고객 LIKE 검색 사용 여부', '고객명 및 고객 전화번호에 대해 LIKE 검색을 설정할 수 있습니다.단, 고객 데이터 양에 따라 속도에 영향이 있을 수 있습니다.', 'ENV_CUST_LIKE_SCH_USE_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '9', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '설문 대상지정 사용 여부', '설문계획 등록시 설문대상자 지정 여부를 설정할 수 있습니다.', 'ENV_SRVY_TRGT_DSGN_USE_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '10', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '간편인증 사용 여부', '간편인증 사용 여부', 'ENV_CUST_SIMPLE_CERT_USE_YN', 'COE', '60', 'USE_WT', 'Y', 'Y', 'N', 'Y', 'N', '11', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'ENV', NULL, '설문 발신 번호 유형', '설문 발신 번호 유형을 설정합니다.\n - DEFAULT: 기본형(발신번호 목록에서 선택)\n - ##{참여자항목ID,GROUP_CD_ID}## : 참여자항목ID를 CD_ID 로하는 GROUP_CD_ID 공통코드 값(전화번호) 설정(예: ##{CUTT_TYPE_1,CENTER_TEL}##)', 'ENV_SRVY_DSPTCH_NO_TP', 'TXT', '60', NULL, 'Y', 'Y', 'N', 'Y', 'N', '12', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y');
                    
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CONSEL', '1', '상담 결과', '상담 결과', 'CUSL_RS', 'COE', '60', 'CUSL_RS', 'Y', 'Y', 'N', 'Y', 'N', '3', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CONSEL', '2', '처리 결과', '처리 결과', 'CAMP_RS', 'COE', '60', 'CAMP_RS', 'Y', 'Y', 'N', 'Y', 'N', '2', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CONSEL', '3', '상담 유형', '상담 유형', 'CUSL_TP_CL', 'COE', '60', NULL, 'Y', 'Y', 'N', 'Y', 'N', '1', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CONSEL', '4', '상담 내용', '상담 내용', 'CUSL_CN', 'TXT', '4000', NULL, 'Y', 'Y', 'N', 'Y', 'N', '6', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y', 'N');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CONSEL', '5', '특이사항', '특이사항', 'SPEC_NOTE', 'TXT', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '7', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'N');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CONSEL', '6', '예약콜', '예약콜', 'RSVT_CALL', 'DAT', '14', NULL, 'Y', 'N', 'Y', 'Y', 'N', '5', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CONSEL', '7', '상담 태그', '상담 태그', 'CUSL_TAG', 'HASH', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '8', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'N');
        /*INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '8', '직장 전화번호', '직장 전화번호', 'CUST_COM_PHN_NO', 'NUM', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '5', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y', 'N');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '9', '자택 전화번호', '자택 전화번호', 'CUST_HOME_PHN_NO', 'NUM', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '11', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y', 'N');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '10', '이메일', '이메일', 'CUST_EMAIL', 'TXT', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '9', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y', 'N');*/
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '11', '고객 상태', '고객 상태', 'CUST_STAT', 'COE', '60', 'CUST_ST', 'Y', 'Y', 'N', 'Y', 'N', '2', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '12', '고객 구분', '고객 구분', 'CUST_SE', 'COE', '60', 'CUST_DV', 'N', 'N', 'Y', 'Y', 'N', '1', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '13', '고객 유형', '고객 유형', 'CUST_TP', 'COE', '60', 'CUST_TP', 'Y', 'N', 'Y', 'Y', 'N', '3', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '14', '등급', '등급', 'CUST_LVL', 'COE', '60', 'CUST_RT', 'N', 'N', 'Y', 'Y', 'N', '6', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        /*INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '15', '소속', '소속', 'CUST_OGNZ', 'TXT', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '10', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '16', '지역', '지역', 'CUST_ZONE', 'COE', '60', 'AREA', 'N', 'N', 'Y', 'Y', 'N', '4', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '17', '생년월일', '생년월일', 'CUST_BIRTH', 'NUM', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '8', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'Y', 'N');*/
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '18', '성별', '성별', 'CUST_GENDER', 'COE', '60', 'GNDR', 'N', 'N', 'Y', 'Y', 'N', '7', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        /*INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '19', '자택 우편번호', '자택 우편번호', 'CUST_HOME_POST', 'POST', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '12', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'N');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '20', '직장 우편번호', '직장 우편번호', 'CUST_COM_POST', 'POST', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '13', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'N');*/
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '21', '메모 내용', '메모 내용', 'CUST_MEMO', 'TXT', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '14', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'N');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CUSTOM', '22', '특이사항', '특이사항', 'CUST_SPEC_NOTE', 'TXT', '60', NULL, 'N', 'N', 'Y', 'Y', 'N', '15', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'N');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CAMP', '23', '고객명', '고객명', 'CAMP_CUST_NM', 'TXT', '200', NULL, 'N', 'Y', 'N', 'Y', 'N', '1', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CAMP', '24', '성별', '성별', 'CAMP_CUST_GNDR', 'COE', '60', 'GNDR', 'N', 'N', 'Y', 'Y', 'Y', '4', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CAMP', '25', '연령', '연령', 'CAMP_CUST_AGE', 'COE', '60', 'AGE_RNG', 'N', 'N', 'Y', 'Y', 'Y', '3', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CAMP', '26', '지역', '지역', 'CAMP_CUST_LOC', 'COE', '60', 'AREA', 'N', 'N', 'Y', 'Y', 'Y', '5', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');
        INSERT INTO PLT_EXPSN_ATTR(ATTR_ID,CUSTCO_ID,BSC_PVSN_ATTR_YN,SE,LNKG_ATTR_ID,EXPSN_ATTR_NM,EXPSN_ATTR_EXPLN,EXPSN_ATTR_COL_ID,DATA_TYPE_CD,DATA_LEN,GROUP_CD_ID,SYS_BSC_YN,ESNTL_YN,MDFCN_PSBLTY_YN,USE_YN,DEL_YN,SORT_ORD,MSG_USE_YN,MSG_CN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CPI_ID,SIC_ID,HSTRY_LIST_EXPSR_YN,INDI_INFO_ENCPT_YN,SCRN_EXPSR_YN) VALUES(getSeqNo('ATTR_ID'), v_custco_id, 'N', 'CAMP', '27', '전화번호', '전화번호', 'CAMP_CUST_PHN_NO', 'TXT', '60', NULL, 'N', 'Y', 'N', 'Y', 'N', '2', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, NULL, NULL, 'N', 'N', 'Y');

        
        /*고객사 별 환경설정 정보 입력*/
        FOR ATTR_RECORD IN(
            SELECT ATTR_ID, SE, EXPSN_ATTR_NM, EXPSN_ATTR_EXPLN, EXPSN_ATTR_COL_ID, DATA_TYPE_CD, DATA_LEN, GROUP_CD_ID
            FROM PLT_EXPSN_ATTR
            WHERE CUSTCO_ID = v_custco_id
            AND BSC_PVSN_ATTR_YN = 'N'
            AND SE = 'ENV'
        
        ) LOOP
            INSERT INTO PLT_CUSTCO_STNG(CUSTCO_ID, ATTR_ID, ATTR_VL) VALUES(v_custco_id, ATTR_RECORD.ATTR_ID, CASE WHEN (ATTR_RECORD.EXPSN_ATTR_COL_ID = 'ENV_TMPL_TP' OR ATTR_RECORD.EXPSN_ATTR_COL_ID = 'ENV_SRVY_DSPTCH_NO_TP') THEN 'DEFAULT' WHEN ATTR_RECORD.EXPSN_ATTR_COL_ID = 'ENV_SRVY_TRGT_DSGN_USE_YN' THEN 'Y' ELSE 'N' END);
        END LOOP;
        

        /* 채팅_고객사_시스템_링크	*/
        INSERT INTO PLT_CHT_CUSTCO_SYS_LNK(CUSTCO_ID,SYS_MSG_ID,MSG_HR,BTN_NM,LNK_CD,URL_MOBILE,URL_PC,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_SYS_LNK_ID)
        VALUES(v_custco_id, '3', NULL, '!종료', 'LINK01', NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, getSeqNo('CUSTCO_SYS_LNK_ID'));
        
    
        /* 채팅_고객사_시스템_메시지 */
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '1', '1', '응대지연1', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '1', '2', '응대지연2', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '2', '0', '상담원이 상담을 종료하였습니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '3', '0', '종료버튼', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '6', '2', '고객무응답 2', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '6', '1', '고객무응답 1', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '7', '0', '응대지연 자동멘트', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '9', '1', '1', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '10', '0', '삼담사가 채팅을 전달하여 채팅이 종료되었습니다.', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '11', '0', '지금은 업무시간이 아닙니다.', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '12', '0', '지금은 점심시간입니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '13', '0', '오늘은 휴일입니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '14', '0', '[상담내용]을 입력해주시면 잠시후에 상담을 시작합니다. 상담을 종료하시려면 !종료 를 입력해주세요.', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '15', '0', '문의유형을 선택해주시면 상담을 시작할 수 있습니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '16', '0', '곧 상담사를 연결해드리겠습니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '17', '0', '고객이 무응답하여 자동종료됩니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '18', '0', '고객의 요청으로 채팅 상담이 종료되었습니다. 오늘도 즐거운 하루 보내세요!', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '19', '0', '고객님이 상담을 종료하였습니다.', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '20', '0', '고객님 상담중이던 상담원이 비정상 종료 되었습니다.', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '21', '0', '해당 이미지는 보낼 수 없습니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '22', '0', '현재버전에서 이미지는 지원되지 않습니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '23', '0', '현재버전에서 동영상은 지원되지 않습니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '24', '0', '현재버전에서 장문(1000자 이상)은 지원되지 않습니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '25', '0', '첨부 가능한 이미지 파일 크기는 최대 5MB입니다.
        이미지 파일 크기 확인 후 재전송 부탁드립니다.', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '26', '0', '자세한 상담을 위해 고객님의 정보를 요청합니다. 버튼을 눌러 정보를 입력해 주시기 바랍니다.', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '27', '0', '종료를 하시려면 아래 버튼을 클릭하여 주시기 바랍니다.', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '28', '0', '안녕하세요! 문의유형을 선택해주시면 상담을 시작하겠습니다.', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);
        INSERT INTO PLT_CHT_CUSTCO_SYS_MSG(CUSTCO_ID,SYS_MSG_ID,MSG_HR,MSG_CN,USE_YN,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,MSG_TYPE_CD) VALUES(v_custco_id, '29', '0', '안녕하세요! #{고객사}입니다! 상담원과 연결되었습니다', 'Y', v_rgtr_id, v_reg_dt, NULL, NULL, NULL);

        
        /* 채팅_상담_허용_설정 */
        INSERT INTO PLT_CHT_CUTT_PM_STNG(CUSL_ID,CHT_LMT_CNT,COMM_SCRIPT_EXPSR_YN) VALUES(v_user_id, '10', 'N');

    
        /* 채팅_상담_고객사 */			
        INSERT INTO PLT_CHT_CUTT_CUSTCO(CUSL_ID,CUSTCO_ID) VALUES(v_user_id, v_custco_id);
    
    
        /* 채팅_질문_유형 - 제외 - 고객사_채널(PLT_CUSTCO_CHN) 데이터가 있어야 함. 메뉴명 : 채팅설정 - 채팅 문의유형 관리, 채팅설정 - 삼담 이미지관리 */
--			INSERT INTO PLT_CHT_QSTN_TYPE(QSTN_TYPE_ID,UP_QSTN_TYPE_ID,QSTN_TYPE_NM,QSTN_TYPE_EXPLN,QSTN_TYPE_CD,SNDR_KEY,QSTN_TYPE_SE_CD,USE_YN,SORT_ORD,QSTN_TYPE_ENG_NM,ALTMNT_ORD,FILE_GROUP_KEY,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT,CUSTCO_ID,DEL_YN) 
--			VALUES(getSeqNo('QSTN_TYPE_ID'), NULL, v_custco_nm, '최상위 문의유형', 'CONSEL', '1', '0', 'Y', '1', NULL, NULL, NULL, v_rgtr_id, v_reg_dt, NULL, NULL, v_custco_id, 'N');
        
    
        /* 지식 - KMS 분류 */
        INSERT INTO plt_kms_clsf(kms_clsf_id, up_kms_clsf_id, kms_clsf_nm, kms_clsf_expln, sort_ord, use_yn, del_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt, custco_id)
        VALUES(getSeqNo('KMS_CLSF_ID'), NULL, v_custco_nm, v_custco_nm, 1, 'Y', 'N', v_rgtr_id, v_reg_dt, NULL, NULL, v_custco_id);
    

        /* QA 품질 분류 */
        INSERT INTO plt_qa_qlty_clsf(qlty_clsf_id, up_qlty_clsf_id, qlty_clsf_nm, sort_ord, use_yn, del_yn, reg_dt, rgtr_id, mdfcn_dt, mdfr_id, custco_id)
        VALUES(getSeqNo('QLTY_CLSF_ID'), NULL, v_custco_nm, 1, 'Y', 'N', v_reg_dt, v_rgtr_id, NULL, NULL, v_custco_id);
    
    
        /* 통합메시지 - 템플릿 분류 */
        select getSeqNo('TMPL_CLSF_ID') into v_up_tmpl_clsf_id FROM DUAL;
        INSERT INTO plt_tmpl_clsf(tmpl_clsf_id, up_tmpl_clsf_id, tmpl_clsf_nm, custco_id, sort_ord, use_yn, del_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
        VALUES(v_up_tmpl_clsf_id, NULL, v_custco_nm, v_custco_id, 1, 'Y', 'N', v_rgtr_id, v_reg_dt, NULL, NULL);
    
        select getSeqNo('TMPL_CLSF_ID') into v_tmpl_clsf_id FROM DUAL;
        INSERT INTO plt_tmpl_clsf(tmpl_clsf_id, up_tmpl_clsf_id, tmpl_clsf_nm, custco_id, sort_ord, use_yn, del_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
        VALUES(v_tmpl_clsf_id, v_up_tmpl_clsf_id, '기본 템플릿', v_custco_id, 1, 'Y', 'N', v_rgtr_id, v_reg_dt, NULL, NULL);
        
        /* 설문 분류 */
        INSERT INTO plt_srvy_clsf(srvy_clsf_id, up_srvy_clsf_id, srvy_clsf_nm, srvy_clsf_expln, sort_ord, use_yn, del_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt, custco_id)
        VALUES(getSeqNo('SRVY_CLSF_ID'), NULL, v_custco_nm, v_custco_nm, 1, 'Y', 'N', v_rgtr_id, v_reg_dt, NULL, NULL, v_custco_id);
        
    
        /* 공통_코드 */
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CALL_TP', v_custco_id, '콜 유형', '시스템 전체에 사용되는 상담채널의 분류 코드', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CHNL', v_custco_id, '채널', '인입된 콜의 채널을 관리하는 코드', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CHNL_CL', v_custco_id, '채널 분류', '채널이 SNS인 경우 하위 분류를 관리하는 코드', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CHNL_TP', v_custco_id, '채널 유형', '채팅설정의 메신저연동에서 사용되는 채널 분류별 형식을 관리하는 코드', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUSL_RS', v_custco_id, '상담결과', '인바운드 전화상담 시 사용되는 상담 처리결과 코드', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CAMP_ST', v_custco_id, '캠페인 처리 상태', '아웃바운드 캠페인에 사용되는 상태코드', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CAMP_RS', v_custco_id, '캠페인 처리 결과', '아웃바운드 캠페인에서 사용되는 처리상태 코드', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUST_DV', v_custco_id, '고객 구분', '고객 구분', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUST_TP', v_custco_id, '고객 유형', '고객 유형', NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUST_ST', v_custco_id, '고객 상태', '고객 상태', NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUST_RT', v_custco_id, '고객 등급', '고객 등급', NULL, 'Y', 'Y', 'N', 11, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'GNDR', v_custco_id, '성별', '성별', NULL, 'Y', 'Y', 'N', 12, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'USER_ST', v_custco_id, '사용자 상태', '사용자 상태', NULL, 'Y', 'Y', 'N', 13, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'JOB_POST', v_custco_id, '직책', '내부 사용자의 직책을 구분하는 코드', NULL, 'Y', 'Y', 'N', 14, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'AREA', v_custco_id, '지역', '국내 행정구역상의 지역을 구분하는 코드', NULL, 'Y', 'Y', 'N', 15, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CALB_WT', v_custco_id, '콜백 여부', '콜백 여부', NULL, 'Y', 'Y', 'N', 16, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUSL_TP_LV', v_custco_id, '상담유형 레벨', '상담정보 항목관에에서 사용되는 상담유형의 허용 레벨을 구분하는 코드', NULL, 'Y', 'Y', 'N', 17, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'EXPN_PT', v_custco_id, '확장 속성', '확장 속성', NULL, 'Y', 'Y', 'N', 18, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'REQR_WT', v_custco_id, '필수 여부', '필수 여부', NULL, 'Y', 'Y', 'N', 19, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'USE_WT', v_custco_id, '사용 여부', '사용 여부', NULL, 'Y', 'Y', 'N', 20, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SRVC_ST', v_custco_id, '서비스 상태', '채팅설정의 메신저연동에서 사용되는 채널상태를 구분하는 코드', NULL, 'Y', 'Y', 'N', 21, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MESG_CL', v_custco_id, '메시지 분류', '채팅설정의 시스템 메시지관리에서 사용되는 코드', NULL, 'Y', 'Y', 'N', 22, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MESG_TP', v_custco_id, '메시지 유형', '채팅설정의 시스템 메시지관리에서 사용되는 코드', NULL, 'Y', 'Y', 'N', 23, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUST_RG_CN', v_custco_id, '고객접수 제안 건', '채팅설정의 상담설정 중 고객접수 설정에 사용되는 코드', NULL, 'Y', 'Y', 'N', 24, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'INQU_USE_LV', v_custco_id, '문의유형 사용 레벨', '채팅설정의 상담설정 중 문의유형 사용 레벨 설정에 사용되는 코드', NULL, 'Y', 'Y', 'N', 25, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUSL_AT', v_custco_id, '상담 가능시간', '채팅설정의 상담설정 중 상담 가능시간 설정에 사용되는 코드', NULL, 'Y', 'Y', 'N', 26, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUSL_AW', v_custco_id, '상담허용 수 일괄 적용', '채팅설정의 상담설정 중 채팅 허용 수를 지정할때 사용하는 코드', NULL, 'Y', 'Y', 'N', 27, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'HODY_DV', v_custco_id, '휴일 구분', '휴일 구분', NULL, 'Y', 'Y', 'N', 28, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'VOC_TP', v_custco_id, 'VOC 유형', 'VOC 유형', NULL, 'Y', 'Y', 'N', 29, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'VOC_CUST_TP', v_custco_id, 'VOC 고객 유형', 'VOC 고객 유형', NULL, 'Y', 'Y', 'N', 30, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUST_PS', v_custco_id, '고객 성향', '고객 성향', NULL, 'Y', 'Y', 'N', 31, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'IMPT', v_custco_id, '중요도', '중요도', NULL, 'Y', 'Y', 'N', 32, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'NOTR_WT', v_custco_id, '결과통보 여부', '결과통보 여부', NULL, 'Y', 'Y', 'N', 33, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'NOTR_TL', v_custco_id, '결과통보 수단', '결과통보 수단', NULL, 'Y', 'Y', 'N', 34, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'QATS_ST', v_custco_id, 'QA 평가 진행상태', 'QA 평가 진행상태', NULL, 'Y', 'Y', 'N', 35, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CONT_ST', v_custco_id, '콘텐츠 상태', '지식관리의 콘텐츠에 대한 상태를 관리하는 코드', NULL, 'Y', 'Y', 'N', 36, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'TMPL_TP', v_custco_id, '템플릿 유형', '지식관리에 사용되는 템플릿의 유형을 관리하는 코드', NULL, 'Y', 'Y', 'N', 37, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'PROG_ST', v_custco_id, '진행 상태', '설문조사의 진행상태를 관리하는 코드', NULL, 'Y', 'Y', 'N', 38, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SUVY_CL', v_custco_id, '설문계획 구분', '설문조사 계획의 분류를 관리하는 코드', NULL, 'Y', 'Y', 'N', 39, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SUVY_QST_TP', v_custco_id, '설문 질문유형', '설문조사에 사용되는 질문내용의 유형을 관리하는 코드', NULL, 'Y', 'N', 'Y', 40, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SELT_ALW_NB', v_custco_id, '선택 허용수', '설문조사의 문항유형이 복수 선택형인 경우 답란 선택갯수를 관리하는 코드', NULL, 'Y', 'Y', 'N', 41, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'ANSW_SZ', v_custco_id, '답변 사이즈', '설문조사의 문항유형이 주관식 단답형 또는 서술형인경우 답안 입력 박스의 크기를 관리하는 코드', NULL, 'Y', 'Y', 'N', 42, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'BORD_TP', v_custco_id, '게시판 유형', '게시판의 유형을 관히하는 코드', NULL, 'Y', 'Y', 'N', 43, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUSL_TP_CL', v_custco_id, '상담유형 구분', '상담정보 항목을 인/아웃 분리 또는 통한 사용 설정 시 사용되는 코드', NULL, 'Y', 'Y', 'N', 44, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CHAT_INQ_TP', v_custco_id, '문의유형 타입', '채팅 설정의 문의유형을 관리하는 코드', NULL, 'Y', 'Y', 'N', 45, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CATG_EXT_CL', v_custco_id, '확장항목 구분', '시스템관리의 확장항목 관리에서 사용되는 구분을 관리하는 코드', NULL, 'Y', 'Y', 'N', 46, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MODI_WT', v_custco_id, '수정 여부', '시스템관리의 확장항목 관리에서 수정여부를 관리하는 코드', NULL, 'Y', 'Y', 'N', 47, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SHOW_WT', v_custco_id, '표시 여부', '시스템관리의 메뉴관리에서 사용되는 표시여부를 관리하는 코드', NULL, 'Y', 'Y', 'N', 48, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'VIEW_TG', v_custco_id, '뷰 타겟', '시스템관리의 메뉴관리에서 사용되는 뷰 타겟을 관리하는 코드', NULL, 'Y', 'Y', 'N', 49, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'BUTN_TP', v_custco_id, '버튼 타입', '시스템관리의 메뉴관리에서 사용되는 버튼 타입을 관리하는 코드', NULL, 'Y', 'Y', 'N', 50, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'USER_CL', v_custco_id, '사용자 구분', '시스템관리의 메뉴 권한 관리에서 사용되는 사용자 구분을 을 관리하는 코드', NULL, 'Y', 'Y', 'N', 51, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MNGR_WT', v_custco_id, '관리자 여부', '시스템관리의 메뉴 권한 관리에서 사용되는 관리자 여부를 을 관리하는 코드', NULL, 'Y', 'Y', 'N', 52, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CALD_CL', v_custco_id, '캘린더', '캘린더의 일정 등록에 사용되는 캘린더 구분을 관리하는 코드', NULL, 'Y', 'Y', 'N', 53, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'PUBL_WT', v_custco_id, '공개 여부 ', '캘린더의 등록시 공개 여부를 관리하는 코드', NULL, 'Y', 'Y', 'N', 54, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CD_TP', v_custco_id, '공통코드 유형', '공통코드 유형(시스템/고객용)', NULL, 'Y', 'Y', 'N', 55, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'USER_SRCH', v_custco_id, '사용자 검색조건', '사용자 검색조건', NULL, 'Y', 'Y', 'N', 56, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUST_SRCH', v_custco_id, '고객 검색조건', '고객 검색조건', NULL, 'Y', 'Y', 'N', 57, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'QAOJ_WT', v_custco_id, '이의제기 여부', '피 평가자자 평가결과에 대해 이의제기 가능여부를 관리하는 코드', NULL, 'Y', 'Y', 'N', 58, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'RSVT_ST', v_custco_id, '예약콜 상태', '예약콜의 상태를 관리하는 코드', NULL, 'Y', 'Y', 'N', 59, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SIC', v_custco_id, '표준산업분류', '표준산업분류 대분류', NULL, 'Y', 'Y', 'N', 60, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'AGE_RNG', v_custco_id, '연령대', '연령대', NULL, 'Y', 'Y', 'N', 61, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'DEPT_TP', v_custco_id, '부서 구분', '부서 구분', NULL, 'Y', 'Y', 'N', 62, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'ANY_WT', v_custco_id, '익명 여부', '익명 여부', NULL, 'Y', 'Y', 'N', 63, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'AGREE_TP', v_custco_id, '동의 구분', '동의 구분', NULL, 'Y', 'Y', 'N', 64, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MESG_SND_RCV', v_custco_id, '메시지 수발신 구분', '메시지 수발신 구분', NULL, 'Y', 'Y', 'N', 65, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SMS_TP', v_custco_id, '문자 유형', '문자 유형', NULL, 'Y', 'Y', 'N', 66, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MTS_TP', v_custco_id, 'MTS 구분', 'MTS 구분 코드', NULL, 'Y', 'Y', 'N', 67, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'RCPT_LMT', v_custco_id, '고객접수 제한건수', '고객접수 제한건수', NULL, 'Y', 'Y', 'N', 68, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'PM_CNT', v_custco_id, '상담 허용수', '상담 허용수', NULL, 'Y', 'Y', 'N', 69, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUTT_HIST_SRCH', v_custco_id, '상담이력 검색조건', '상담이력 검색조건', NULL, 'Y', 'Y', 'N', 70, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'QSTN_LVL', v_custco_id, '문의유형레벨', '문의유형레벨', NULL, 'Y', 'Y', 'N', 71, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CHAT_ST', v_custco_id, '채팅 상태', '채팅 상태', NULL, 'Y', 'Y', 'N', 72, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CMPN_CL', v_custco_id, '캠페인 구분', '시행하는 캠페인을 구분하는 코드', NULL, 'Y', 'Y', 'N', 73, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CMPN_ST', v_custco_id, '캠페인 진행 상태', '캠페인의 진행 상태를 관히는 코드', NULL, 'Y', 'Y', 'N', 74, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUSL_STAT', v_custco_id, '상담상태', '상담상태', NULL, 'Y', 'Y', 'N', 75, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CAUTION_TP', v_custco_id, '주의고객 유형', '요주의 고객 유형을 관리하는 코드', NULL, 'N', 'Y', 'N', 76, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'KMS_CL', v_custco_id, '지식 구분', '지식 구분', NULL, 'Y', 'Y', 'N', 77, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CMPN_EXP_TP', v_custco_id, '제외사유구분', '제외사유구분', NULL, 'Y', 'Y', 'N', 78, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'UPDOWN', v_custco_id, '검색 범위', '검색 범위', NULL, 'Y', 'Y', 'N', 79, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MTS_TMPL_MSG_TP', v_custco_id, 'MTS 템플릿 메시지 유형', 'MTS에 등록하는 카카오 알림톡 템플릿 메시지 유형 코드값', NULL, 'Y', 'Y', 'N', 80, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'PCS_PROD', v_custco_id, '서비스 상품 코드', '서비스 상품 코드', NULL, 'Y', 'Y', 'N', 81, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MTS_TMPL_EPSZ_TP', v_custco_id, 'MTS 템플릿 강조 유형', 'MTS에 등록하는 카카오 알림톡 템플릿 강조 유형 코드값', NULL, 'Y', 'Y', 'N', 82, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MTS_BTN_LINK_TP', v_custco_id, 'MTS 버튼 링크 유형', 'MTS에 등록하는 카카오 알림톡 버튼 링크 유형 코드값', NULL, 'Y', 'Y', 'N', 83, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'TRANSFER_SRCH', v_custco_id, '호전환 검색 조건', '호전환 검색 조건', NULL, 'Y', 'Y', 'N', 84, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CTI_STAT_TP', v_custco_id, 'CTI 상태 구분', 'CTI 상태 구분', NULL, 'Y', 'Y', 'N', 85, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MTS_TMPL_STAT_CD', v_custco_id, 'MTS 템플릿 상태', 'MTS 템플릿 등록 상태', NULL, 'Y', 'Y', 'N', 86, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MTS_ISPT_STAT_CD', v_custco_id, 'MTS 검수 상태 코드', 'MTS 검수 상태 코드', NULL, 'Y', 'Y', 'N', 87, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CHT_SRCH_TP', v_custco_id, '채팅 조회구분', '채팅 조회구분', NULL, 'Y', 'Y', 'N', 88, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'PHN_SE_TP', v_custco_id, '통화 시간 구분(초)', '통화 시간 구분(초)', NULL, 'Y', 'Y', 'N', 89, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'STATISTICS_TP', v_custco_id, '통계 구분', '통계 구분', NULL, 'Y', 'Y', 'N', 90, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'APRV_ST', v_custco_id, '상담이력 승인 처리상태', '상담이력 승인 처리상태', NULL, 'Y', 'Y', 'N', 91, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'DEPT_CUSL_TP', v_custco_id, '부서/개인 선택 구분', '부서/개인 선택 구분', NULL, 'Y', 'Y', 'N', 92, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUST_PHN_CHG_TP', v_custco_id, '고객 전화번호 변경 구분', '고객 전화번호 변경 구분', NULL, 'Y', 'Y', 'N', '93', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'PRVC_INQ_TASK_TP', v_custco_id, '개인정보 조회 업무 구분', '개인정보 조회 업무 구분', NULL, 'Y', 'Y', 'N', '94', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'PRVC_INQ_TGRT_TP', v_custco_id, '개인정보 조회 대상 구분', '개인정보 조회 대상 구분', NULL, 'Y', 'Y', 'N', '95', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'PRVC_SCH_TP', v_custco_id, '개인정보 조회 검색 구분', '개인정보 조회 검색 구분', NULL, 'Y', 'Y', 'N', '96', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'VOC_SCH_TP', v_custco_id, 'VOC 조회 검색 구분', 'VOC 조회 검색 구분', NULL, 'Y', 'Y', 'N', '97', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MSG_TMPL_TP', v_custco_id, '메시지 템플릿 유형', '메시지 템플릿 유형', NULL, 'Y', 'Y', 'N', '98', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'LAYOUT_TMPL_TP', v_custco_id, '레이아웃 템플릿 유형', '레이아웃 템플릿 유형', NULL, 'Y', 'Y', 'N', '99', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUTT_BMK_TP', v_custco_id, '상담유형 북마크 유형', '상담유형 북마크 유형', NULL, 'Y', 'Y', 'N', '100', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUST_REG_SCH_TP', v_custco_id, '고객 등록수정 일자 검색 유형', '고객 등록수정 일자 검색 유형', NULL, 'Y', 'Y', 'N', '101', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CHT_SE_TP', v_custco_id, '채팅 시간 구분(분)', '채팅 시간 구분(분)', NULL, 'Y', 'Y', 'N', '102', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SEAT_MOVE_TP', v_custco_id, '이석 구분', '이석 구분', NULL, 'Y', 'Y', 'N', '103', v_rgtr_id, v_reg_dt, NULL, NULL);
        --단축키_키_코드 추가. 2024.01.24. by hjh.
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SHROTCUT_CD', v_custco_id, '단축키 키 코드', '단축키 키보드 입력값의 keyCode', NULL, 'Y', 'Y', 'N', 106, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'CUSL_PHN_NO', v_custco_id, '상담사 전화번호', '상담사 전화번호', NULL, 'Y', 'Y', 'N', 107, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'MONITOR_VIEW_TP', v_custco_id, '모니터링 표시 구분', '모니터링 표시 구분', NULL, 'Y', 'Y', 'N', '108', v_rgtr_id, v_reg_dt, NULL, NULL);
        
        
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CALL_TP', 'IN', v_custco_id, '인바운드', 'Inboud', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CALL_TP', 'OUT', v_custco_id, '아웃바운드', 'Out bound', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHNL', 'TEL', v_custco_id, '전화', 'TEL', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHNL', 'SNS', v_custco_id, 'SNS', 'SNS', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHNL_CL', 'KAKAO', v_custco_id, '카카오톡', 'KAKAO', 'CHAT', 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHNL_CL', 'TTALK', v_custco_id, '티톡', 'TTALK', 'CHAT', 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHNL_TP', 'TALK', v_custco_id, '알림톡', 'Talk', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHNL_TP', 'CHAT', v_custco_id, '채팅', 'chatting', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_RS', 'COMPLETED', v_custco_id, '처리완료', 'Processing completed', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_RS', 'PROCESSING', v_custco_id, '처리중', 'Processing', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAMP_ST', 'COMPLETED', v_custco_id, '처리완료', 'Processing completed', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAMP_ST', 'UNTRIED', v_custco_id, '미시도', 'Untried', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAMP_RS', '1', v_custco_id, '달성', 'Attain', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAMP_RS', '2', v_custco_id, '부재', 'absence', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAMP_RS', '3', v_custco_id, '통화거부', 'Call Denial', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAMP_RS', '4', v_custco_id, '상담거부', 'Refusal to consult', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAMP_RS', '5', v_custco_id, '관심있음', 'interest', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAMP_RS', '6', v_custco_id, '재연락 요망', 'Callback', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAMP_RS', '7', v_custco_id, '메시지 발송', 'Send a message', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_DV', 'GENERAL', v_custco_id, '일반고객', 'a general customer', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_DV', 'ENTERPRISE', v_custco_id, '기업고객', 'a corporate customer', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_TP', 'GENERAL', v_custco_id, '일반회원', 'a general member', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_TP', 'PAID', v_custco_id, '유료회원', 'a paid member', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_TP', 'NON', v_custco_id, '비회원', 'a non-member', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_ST', 'NOML', v_custco_id, '정상', 'Normal', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_ST', 'SLEP', v_custco_id, '휴면', 'Dormant sleep', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_ST', 'DELT', v_custco_id, '삭제', 'Delete', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RT', '1', v_custco_id, '골드', 'Gold', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RT', '2', v_custco_id, '실버', 'Silver', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RT', '3', v_custco_id, '브론즈', 'Bronze', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RT', '4', v_custco_id, '일반', 'General', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('GNDR', 'MALE', v_custco_id, '남성', 'Male', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('GNDR', 'FEMALE', v_custco_id, '여성', 'Female', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('GNDR', 'UNKNOWN', v_custco_id, '알수없음', 'Unknown', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USER_ST', 'WORK', v_custco_id, '재직', 'Work', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USER_ST', 'ABSENCE', v_custco_id, '휴직', 'absence', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USER_ST', 'RESIGN', v_custco_id, '퇴직', 'Resign', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USER_ST', 'DELT', v_custco_id, '삭제', 'Delete', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('JOB_POST', '1', v_custco_id, '센터장', 'Master', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('JOB_POST', '2', v_custco_id, '매니저', 'Manager', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('JOB_POST', '3', v_custco_id, '상담직원', 'Counselor', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '11', v_custco_id, '서울특별시', 'Seoul Metropolitan Government', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '21', v_custco_id, '부산광역시', 'Busan Metropolitan City', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '22', v_custco_id, '대구광역시', 'Daegu Metropolitan City', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '23', v_custco_id, '인천광역시', 'Incheon Metropolitan City', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '24', v_custco_id, '광주광역시', 'Gwangju Metropolitan City', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '25', v_custco_id, '대전광역시', 'Daejeon Metropolitan City', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '26', v_custco_id, '울산광역시', 'Ulsan Metropolitan City', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '27', v_custco_id, '세종특별자치시', 'Sejong City', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '31', v_custco_id, '경기도', 'Gyeonggi Province', NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '32', v_custco_id, '강원도', 'Gangwon-do', NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '33', v_custco_id, '충청북도', 'Chungcheongbuk-do', NULL, 'Y', 'Y', 'N', 11, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '34', v_custco_id, '충청남도', 'Chungcheongnam-do', NULL, 'Y', 'Y', 'N', 12, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '35', v_custco_id, '전라북도', 'Jeollabuk-do', NULL, 'Y', 'Y', 'N', 13, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '36', v_custco_id, '전라남도', 'Jeollanam-do', NULL, 'Y', 'Y', 'N', 14, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '37', v_custco_id, '경상북도', 'Gyeongsangbuk-do', NULL, 'Y', 'Y', 'N', 15, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '38', v_custco_id, '경상남도', 'Gyeongsangnam-do', NULL, 'Y', 'Y', 'N', 16, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AREA', '39', v_custco_id, '제주특별자치도', 'Jeju Special Self-Governing Province', NULL, 'Y', 'Y', 'N', 17, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CALB_WT', 'Y', v_custco_id, '사용', 'Yes', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CALB_WT', 'N', v_custco_id, '사용안함', 'No', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_TP_LV', '1', v_custco_id, '1레벨', 'Level 1', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_TP_LV', '2', v_custco_id, '2레벨', 'Level 2', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_TP_LV', '3', v_custco_id, '3레벨', 'Level 3', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_TP_LV', '4', v_custco_id, '4레벨', 'Level 4', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_TP_LV', '5', v_custco_id, '5레벨', 'Level 5', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EXPN_PT', 'TXT', v_custco_id, '문자형', 'Text', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EXPN_PT', 'NUM', v_custco_id, '숫자형(일반)', 'Number', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EXPN_PT', 'NUM_COMMA', v_custco_id, '숫자형('','' 포함)', 'Number', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EXPN_PT', 'COE', v_custco_id, '단일 선택형', 'Choice', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EXPN_PT', 'COE_MULTI', v_custco_id, '다중 선택형', 'Choice', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EXPN_PT', 'DAT', v_custco_id, '날짜형', 'Date', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EXPN_PT', 'HASH', v_custco_id, '해시태그', 'HashTag', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EXPN_PT', 'POST', v_custco_id, '우편번호', 'Post', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('REQR_WT', 'Y', v_custco_id, '필수', 'Yes', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('REQR_WT', 'N', v_custco_id, '옵션', 'No', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USE_WT', 'Y', v_custco_id, '사용', 'Yes', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USE_WT', 'N', v_custco_id, '사용안함', 'No', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SRVC_ST', 'USE', v_custco_id, '사용', 'USE', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SRVC_ST', 'NOT', v_custco_id, '사용안함', 'Do not use', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SRVC_ST', 'REQ', v_custco_id, '등록요청', 'Registration request', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_CL', 'SYSMSG', v_custco_id, '시스템 메시지', 'System Message Message', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_CL', 'ATOMSG', v_custco_id, '자동응답 메시지', 'Automatic response Message', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_CL', 'DLYMSG', v_custco_id, '응대지연 메시지', 'Response delay Message', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_CL', 'NOAMSG', v_custco_id, '고객 무응답 메시지', 'No answer Message', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_CL', 'RCPMSG', v_custco_id, '접수지연 메시지', 'Reception delay Message', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_CL', 'CUTTEND', v_custco_id, '상담종료 메시지', 'Consultation End Message', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_CL', 'DLYAUTOMSG', v_custco_id, '응대지연자동 메시지', 'Response delay Message', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_CL', 'TRANMSG', v_custco_id, '채팅전달메시지', 'Translate To Other User', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_TP', 'GENMSG', v_custco_id, '일반 메시지', 'General Message', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_TP', 'LIKMSG', v_custco_id, '링크 메시지', 'Link Message', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_TP', 'WEBLINK', v_custco_id, '웹링크', 'WEBLINK Message', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_TP', 'AL', v_custco_id, '다중링크', '설명 필요', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_TP', 'BUTTON', v_custco_id, '버튼링크', '설명필요', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_TP', 'MD', v_custco_id, 'MD메시지', '버튼텍스트 + 메시지 본문 전송', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '3', v_custco_id, '3건', NULL, NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '4', v_custco_id, '4건', NULL, NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '5', v_custco_id, '5건', NULL, NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '6', v_custco_id, '6건', NULL, NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '7', v_custco_id, '7건', NULL, NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '8', v_custco_id, '8건', NULL, NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '9', v_custco_id, '9건', NULL, NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '10', v_custco_id, '10건', NULL, NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '15', v_custco_id, '15건', NULL, NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '20', v_custco_id, '20건', NULL, NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '25', v_custco_id, '25건', NULL, NULL, 'Y', 'Y', 'N', 11, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '30', v_custco_id, '30건', NULL, NULL, 'Y', 'Y', 'N', 12, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '35', v_custco_id, '35건', NULL, NULL, 'Y', 'Y', 'N', 13, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '40', v_custco_id, '40건', NULL, NULL, 'Y', 'Y', 'N', 14, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_RG_CN', '50', v_custco_id, '50건', NULL, NULL, 'Y', 'Y', 'N', 15, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('INQU_USE_LV', '1', v_custco_id, '1레벨', 'Level 1', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('INQU_USE_LV', '2', v_custco_id, '2레벨', 'Level 2', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('INQU_USE_LV', '3', v_custco_id, '3레벨', 'Level 3', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('INQU_USE_LV', '4', v_custco_id, '4레벨', 'Level 4', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('INQU_USE_LV', '5', v_custco_id, '5레벨', 'Level 5', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '0700', v_custco_id, '07:00', NULL, NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '0730', v_custco_id, '07:30', NULL, NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '0800', v_custco_id, '08:00', NULL, NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '0830', v_custco_id, '08:30', NULL, NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '0900', v_custco_id, '09:00', NULL, NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '0930', v_custco_id, '09:30', NULL, NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1000', v_custco_id, '10:00', NULL, NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1030', v_custco_id, '10:30', NULL, NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1100', v_custco_id, '11:00', NULL, NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1130', v_custco_id, '11:30', NULL, NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1200', v_custco_id, '12:00', NULL, NULL, 'Y', 'Y', 'N', 11, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1230', v_custco_id, '12:30', NULL, NULL, 'Y', 'Y', 'N', 12, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1300', v_custco_id, '13:00', NULL, NULL, 'Y', 'Y', 'N', 13, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1330', v_custco_id, '13:30', NULL, NULL, 'Y', 'Y', 'N', 14, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1400', v_custco_id, '14:00', NULL, NULL, 'Y', 'Y', 'N', 15, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1430', v_custco_id, '14:30', NULL, NULL, 'Y', 'Y', 'N', 16, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1500', v_custco_id, '15:00', NULL, NULL, 'Y', 'Y', 'N', 17, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1530', v_custco_id, '15:30', NULL, NULL, 'Y', 'Y', 'N', 18, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1600', v_custco_id, '16:00', NULL, NULL, 'Y', 'Y', 'N', 19, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1630', v_custco_id, '16:30', NULL, NULL, 'Y', 'Y', 'N', 20, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1700', v_custco_id, '17:00', NULL, NULL, 'Y', 'Y', 'N', 21, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1730', v_custco_id, '17:30', NULL, NULL, 'Y', 'Y', 'N', 22, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AT', '1800', v_custco_id, '18:00', NULL, NULL, 'Y', 'Y', 'N', 23, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '1', v_custco_id, '1개', 'ONE', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '2', v_custco_id, '2개', 'TWO', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '3', v_custco_id, '3개', 'THREE', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '4', v_custco_id, '4개', 'FOUR', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '5', v_custco_id, '5개', 'FIVE', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '6', v_custco_id, '6개', 'SIX', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '7', v_custco_id, '7개', 'SEVEN', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '8', v_custco_id, '8개', 'EIGHT', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '9', v_custco_id, '9개', 'NINE', NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_AW', '10', v_custco_id, '10개', 'TEN', NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('HODY_DV', 'SAT', v_custco_id, '토요일', 'Saturday', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('HODY_DV', 'SUN', v_custco_id, '일요일', 'Sunday', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('HODY_DV', 'HLD', v_custco_id, '공휴일', 'holiday', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('HODY_DV', 'THD', v_custco_id, '임시 공휴일', 'Temporary holiday', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_TP', '1', v_custco_id, '불만', 'Complaint', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_TP', '2', v_custco_id, '칭찬', 'Praise', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_TP', '3', v_custco_id, '제언', 'Proposal', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_TP', '9', v_custco_id, '기타', 'ETC', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_CUST_TP', '1', v_custco_id, '개인', 'Person', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_CUST_TP', '2', v_custco_id, '단체', 'Group', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_CUST_TP', '3', v_custco_id, '언론/인터넷', 'Media', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_CUST_TP', '9', v_custco_id, '기타', 'ETC', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_PS', '1', v_custco_id, '강성', 'Strong', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_PS', '2', v_custco_id, '중성', 'Normal', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_PS', '3', v_custco_id, '온화', 'Mild', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('IMPT', '1', v_custco_id, '긴급', 'emergency', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('IMPT', '2', v_custco_id, '높음', 'High', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('IMPT', '3', v_custco_id, '중간', 'the middle', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('IMPT', '4', v_custco_id, '낮음', 'Low', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('IMPT', '5', v_custco_id, '경미', 'a slight', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('NOTR_WT', 'Y', v_custco_id, '원함', 'Yes', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('NOTR_WT', 'N', v_custco_id, '원하지 않음', 'No', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('NOTR_TL', 'TEL', v_custco_id, '전화', 'Tel', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('NOTR_TL', 'MAIL', v_custco_id, '이메일', 'Mail', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('NOTR_TL', 'SMS', v_custco_id, '문자', 'Short Message Service', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('NOTR_TL', 'SNS', v_custco_id, 'SNS', 'social networking services', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('NOTR_TL', 'NOT', v_custco_id, '해당없음', 'Not Applicable', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('QATS_ST', 'PRPARG', v_custco_id, '준비중', 'Preparing', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('QATS_ST', 'ONGONG', v_custco_id, '진행중', 'ongoing', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('QATS_ST', 'TERMIAT', v_custco_id, '종료', 'Termination', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CONT_ST', 'WRTG', v_custco_id, '작성중', 'Writing', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CONT_ST', 'RVRQ', v_custco_id, '검토요청', 'Review request', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CONT_ST', 'APPR', v_custco_id, '승인', 'Approval', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CONT_ST', 'RFSL', v_custco_id, '반려', 'Refusal', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CONT_ST', 'PBIC', v_custco_id, '공개', 'Public', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CONT_ST', 'DELT', v_custco_id, '삭제', 'Delete', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('TMPL_TP', 'TEXT', v_custco_id, '텍스트', 'Text', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('TMPL_TP', 'TXTT', v_custco_id, '텍스트 + 테이블', 'Text & Table', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('TMPL_TP', 'TXTI', v_custco_id, '텍스트 + 이미지', 'Text & Image', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PROG_ST', 'PRPARG', v_custco_id, '준비중', 'Preparing', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) values('PROG_ST', 'RDY', v_custco_id, '승인대기', '승인대기', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, null);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) values('PROG_ST', 'RFSL', v_custco_id, '승인반려', '승인반려', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) values('PROG_ST', 'RTRVL', v_custco_id, '승인회수', '승인회수', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) values('PROG_ST', 'APPR', v_custco_id, '승인완료', '승인완료', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PROG_ST', 'PUBCMP', v_custco_id, '게시완료', 'Publishing complete', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PROG_ST', 'ONGONG', v_custco_id, '진행중', 'ongoing', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PROG_ST', 'TERMIAT', v_custco_id, '종료', 'Termination', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_CL', '1', v_custco_id, '만족도 조사', 'a satisfaction survey', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_CL', '2', v_custco_id, '신규모집', 'New recruitment', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_QST_TP', 'SNGL', v_custco_id, '단일 선택형', 'single-selective', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_QST_TP', 'MULT', v_custco_id, '복수 선택형', 'multiple choice type', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_QST_TP', 'LIST', v_custco_id, '목록 선택형', 'List Selection Type', NULL, 'Y', 'N', 'Y', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_QST_TP', 'SHOT', v_custco_id, '주관식 단답형', 'Subjective short answer type', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_QST_TP', 'DESC', v_custco_id, '주관식 서술형', 'Subjective descriptive type', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_QST_TP', 'DATE', v_custco_id, '날짜 선택형', 'Date Selection Type', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SELT_ALW_NB', '1', v_custco_id, '1개', NULL, NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SELT_ALW_NB', '2', v_custco_id, '2개', NULL, NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SELT_ALW_NB', '3', v_custco_id, '3개', NULL, NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SELT_ALW_NB', '9', v_custco_id, '제한없음', NULL, NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('ANSW_SZ', 'S', v_custco_id, '작게', 'small', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('ANSW_SZ', 'M', v_custco_id, '보통', 'middle', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('ANSW_SZ', 'B', v_custco_id, '크게', 'big', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('BORD_TP', 'GENR', v_custco_id, '일반', 'General', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('BORD_TP', 'NOTI', v_custco_id, '공지', 'Notice', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('BORD_TP', 'EMRG', v_custco_id, '긴급', 'Emergency', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_TP_CL', 'ITGRT', v_custco_id, '통합', 'Integration', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_TP_CL', 'IN', v_custco_id, '인바운드', 'Inbound', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_TP_CL', 'OUT', v_custco_id, '아웃바운드', 'Outbound', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHAT_INQ_TP', 'CONSEL', v_custco_id, '상담사 연결형', 'Counselor connection type', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHAT_INQ_TP', 'MESSAG', v_custco_id, '메시지형', 'Message Type', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHAT_INQ_TP', 'CHATBOT', v_custco_id, '챗봇 연결형', 'Chatbot Connected', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CATG_EXT_CL', 'CUSTOM', v_custco_id, '고객정보', 'Customer information', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CATG_EXT_CL', 'CONSEL', v_custco_id, '상담내용', 'Consultation details', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CATG_EXT_CL', 'CAMP', v_custco_id, '캠페인고객', 'Campain', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MODI_WT', 'Y', v_custco_id, '가능', 'possibility', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MODI_WT', 'N', v_custco_id, '불가능', 'impossibility', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHOW_WT', 'Y', v_custco_id, '표시', 'Display', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHOW_WT', 'N', v_custco_id, '표시안함', 'Don''t show', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VIEW_TG', 'MAIN', v_custco_id, '메인 화면', 'Main screen', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VIEW_TG', 'LEFT', v_custco_id, '좌측메뉴', 'left menu', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VIEW_TG', 'RIGHT', v_custco_id, '우측메뉴', 'right menu', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VIEW_TG', 'DEV', v_custco_id, '개발자메뉴', 'develop menu', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('BUTN_TP', 'INQU', v_custco_id, '조회 권한', 'Inquiry rights', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('BUTN_TP', 'PROC', v_custco_id, '처리 권한', 'Processing rights', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('BUTN_TP', 'DELT', v_custco_id, '삭제 권한', 'Delete Permissions', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('BUTN_TP', 'ADMN', v_custco_id, '관리 권한', 'administrative authority', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('BUTN_TP', 'OUPT', v_custco_id, '출력/엑셀 권한', 'Output & Excel Permissions', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('BUTN_TP', 'CPPT', v_custco_id, '복사방지 권한', 'Copy protection privileges', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USER_CL', 'GUEST', v_custco_id, '손님', 'guest', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USER_CL', 'MANAGER', v_custco_id, '관리자', 'Manager', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MNGR_WT', 'ALOW', v_custco_id, '허용', 'Allowed', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MNGR_WT', 'NOAW', v_custco_id, '허용안함', 'Do not allow', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CALD_CL', 'PSSD', v_custco_id, '개인 일정', 'personal schedule', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CALD_CL', 'CTSD', v_custco_id, '센터 일정', 'Center schedule', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PUBL_WT', 'Y', v_custco_id, '공개', 'Yes', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PUBL_WT', 'N', v_custco_id, '공개안함', 'No', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CD_TP', 'SYS', v_custco_id, '시스템용', '시스템용 공통코드', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CD_TP', 'CUSTOMER', v_custco_id, '고객용', '고객용 공통코드', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USER_SRCH', 'USER_NM', v_custco_id, '사용자명', '사용자이름 검색', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('USER_SRCH', 'USER_ID', v_custco_id, '사용자ID', '사용자ID 검색', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_SRCH', 'CUST_NM', v_custco_id, '고객명', '고객이름 검색', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_SRCH', v_custco_id, v_custco_id, '고객ID', '고객ID 검색', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_SRCH', 'CUST_PHN_NO', v_custco_id, '고객 전화번호', '고객 전화번호 검색', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('QAOJ_WT', 'Y', v_custco_id, '사용', 'Yes', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('QAOJ_WT', 'N', v_custco_id, '사용안함', 'No', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RSVT_ST', 'COMPLETED', v_custco_id, '처리완료', 'Processing completed', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RSVT_ST', 'RESERVATION', v_custco_id, '예약', 'Reservation', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'A', v_custco_id, '농업, 임업 및 어업', NULL, NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'B', v_custco_id, '광업', NULL, NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'C', v_custco_id, '제조업', NULL, NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'D', v_custco_id, '전기, 가스, 증기 및 공기 조절 공급업', NULL, NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'E', v_custco_id, '수도, 하수 및 폐기물 처리, 원료 재생업', NULL, NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'F', v_custco_id, '건설업', NULL, NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'G', v_custco_id, '도매 및 소매업', NULL, NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'H', v_custco_id, '운수 및 창고업', NULL, NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'I', v_custco_id, '숙박 및 음식점업', NULL, NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'J', v_custco_id, '정보통신업', NULL, NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'K', v_custco_id, '금융 및 보험업', NULL, NULL, 'Y', 'Y', 'N', 11, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'L', v_custco_id, '부동산업', NULL, NULL, 'Y', 'Y', 'N', 12, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'M', v_custco_id, '전문, 과학 및 기술 서비스업', NULL, NULL, 'Y', 'Y', 'N', 13, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'N', v_custco_id, '사업시설 관리, 사업 지원 및 임대 서비스업', NULL, NULL, 'Y', 'Y', 'N', 14, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'O', v_custco_id, '공공 행정, 국방 및 사회보장 행정', NULL, NULL, 'Y', 'Y', 'N', 15, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'P', v_custco_id, '교육 서비스업', NULL, NULL, 'Y', 'Y', 'N', 16, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'Q', v_custco_id, '보건업 및 사회복지 서비스업', NULL, NULL, 'Y', 'Y', 'N', 17, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'R', v_custco_id, '예술, 스포츠 및 여가관련 서비스업', NULL, NULL, 'Y', 'Y', 'N', 18, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'S', v_custco_id, '협회 및 단체, 수리 및 기타 개인 서비스업', NULL, NULL, 'Y', 'Y', 'N', 19, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'T', v_custco_id, '가구내 고용활동 및 달리 분류되지 않은 자가소비 생산활동', NULL, NULL, 'Y', 'Y', 'N', 20, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SIC', 'U', v_custco_id, '국제 및 외국기관', NULL, NULL, 'Y', 'Y', 'N', 21, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGE_RNG', '10', v_custco_id, '10대', '10대', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGE_RNG', '20', v_custco_id, '20대', '20대', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGE_RNG', '30', v_custco_id, '30대', '30대', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGE_RNG', '40', v_custco_id, '40대', '40대', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGE_RNG', '50', v_custco_id, '50대', '50대', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGE_RNG', '60', v_custco_id, '60대', '60대', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGE_RNG', '70', v_custco_id, '70대', '70대', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGE_RNG', '80', v_custco_id, '80대', '80대', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('DEPT_TP', 'AFFILIATE', v_custco_id, '계열사', '계열사', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('DEPT_TP', 'DEPT', v_custco_id, '부서', '부서', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('ANY_WT', 'Y', v_custco_id, '예', '예', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('ANY_WT', 'N', v_custco_id, '아니오', '아니오', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGREE_TP', 'PERSON', v_custco_id, '개인정보 활용', '개인정보 활용', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGREE_TP', 'SMS', v_custco_id, 'SMS', 'SMS', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGREE_TP', 'ALTALK', v_custco_id, '알림톡', '알림톡', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGREE_TP', 'EMAIL', v_custco_id, '이메일', '이메일', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('AGREE_TP', 'PUSH', v_custco_id, 'PUSH', 'PUSH', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_SND_RCV', 'SND', v_custco_id, '발신', '발신', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MESG_SND_RCV', 'RCV', v_custco_id, '수신', '수신', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SMS_TP', 'TP_SMS', v_custco_id, 'SMS', 'SMS 문자 유형', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SMS_TP', 'TP_LMS', v_custco_id, 'LMS', 'LMS 문자 유형', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SMS_TP', 'TP_MMS', v_custco_id, 'MMS', 'MMS 문자 유형', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TP', 'ATALK', v_custco_id, '알림톡', '알림톡', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TP', 'FTALK', v_custco_id, '친구톡', '친구톡', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TP', 'SMS', v_custco_id, 'SMS', 'SMS', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TP', 'MMS', v_custco_id, 'MMS', 'MMS', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TP', 'LMS', v_custco_id, 'LMS', 'LMS', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '3', v_custco_id, '3', '3건', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '5', v_custco_id, '5', '5건', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '7', v_custco_id, '7', '7건', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '10', v_custco_id, '10', '10건', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '15', v_custco_id, '15', '15건', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '20', v_custco_id, '20', '20건', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '25', v_custco_id, '25', '25건', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '30', v_custco_id, '30', '30건', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '35', v_custco_id, '35', '35건', NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '40', v_custco_id, '40', '40건', NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '45', v_custco_id, '45', '45건', NULL, 'Y', 'Y', 'N', 11, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('RCPT_LMT', '50', v_custco_id, '50', '50건', NULL, 'Y', 'Y', 'N', 12, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '3', v_custco_id, '3', '3건', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '5', v_custco_id, '5', '5건', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '7', v_custco_id, '7', '7건', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '10', v_custco_id, '10', '10건', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '15', v_custco_id, '15', '15건', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '20', v_custco_id, '20', '20건', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '25', v_custco_id, '25', '25건', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '30', v_custco_id, '30', '30건', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '35', v_custco_id, '35', '35건', NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '40', v_custco_id, '40', '40건', NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '45', v_custco_id, '45', '45건', NULL, 'Y', 'Y', 'N', 11, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PM_CNT', '50', v_custco_id, '50', '50건', NULL, 'Y', 'Y', 'N', 12, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('QSTN_LVL', '1', v_custco_id, '1', '1레벨', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('QSTN_LVL', '2', v_custco_id, '2', '2레벨', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('QSTN_LVL', '3', v_custco_id, '3', '3레벨', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('QSTN_LVL', '4', v_custco_id, '4', '4레벨', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUTT_HIST_SRCH', 'CUST_NM', v_custco_id, '고객명', '고객명', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUTT_HIST_SRCH', 'CUST_PHN_NO', v_custco_id, '고객 전화번호', '고객 전화번호', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUTT_HIST_SRCH', 'LGN_ID', v_custco_id, '상담사 ID', '상담사 ID', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUTT_HIST_SRCH', 'USER_NM', v_custco_id, '상담직원명', '상담직원명', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_CL', '1', v_custco_id, '정기 캠페인', '정기 캠페인', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_CL', '2', v_custco_id, '특별 캠페인', '특별 캠페인', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_CL', '3', v_custco_id, '회원모집 캠페인', '회원모집 캠페인', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_CL', '4', v_custco_id, '상품판매 캠페인', '상품판매 캠페인', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHAT_ST', 'CHT_WAIT', v_custco_id, '채팅ON', '상담이 가능한 상태', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHAT_ST', 'CHT_CANT', v_custco_id, '채팅OFF', '상담이 불가능한 상태', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_ST', 'PRPARG', v_custco_id, '준비중', '준비중', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_ST', 'ONGONG', v_custco_id, '진행중', '진행중', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_ST', 'TERMIAT', v_custco_id, '종료', '종료', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'CMPL_TRAN', v_custco_id, '전달완료', '상담 완료 후 전달', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'AFTPRCS', v_custco_id, '후처리', '후처리 상태', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'CMPL', v_custco_id, '상담완료', '상담 완료', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'AFTPRCS_NORSPNS', v_custco_id, '후처리(무응답)', '무응답으로 인한 상담 종료 후 후처리 상태', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'DMND_NPSBL', v_custco_id, '상담요청불가', '상담요청불가 상태', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'ALTMNT_NPSBL', v_custco_id, '상담배정불가', '상담배정불가 상태', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'DMND_NOCHC', v_custco_id, '상담요청미선택', '상담요청미선택 상태', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'CUSL_ALTMNT_NPSBL', v_custco_id, '상담사배정불가', '상담사배정불가 상태', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'DMND_GIVEUP', v_custco_id, '상담요청중포기', '상담요청중포기 상태', NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'ALTMNT_GIVEUP', v_custco_id, '상담배정중포기', '상담배정중포기 상태', NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'WAIT_GIVEUP', v_custco_id, '상담대기중포기', '상담대기중포기 상태', NULL, 'Y', 'Y', 'N', 11, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'TRAN_WAIT_GIVEUP', v_custco_id, '전달대기중포기', '전달대기중포기 상태', NULL, 'Y', 'Y', 'N', 12, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'CHATBOT', v_custco_id, '챗봇상담', '챗봇상담', NULL, 'Y', 'Y', 'N', 13, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'QSTN_CHCING', v_custco_id, '문의유형선택중', '문의유형선택중 상태', NULL, 'Y', 'Y', 'N', 14, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'ALTMNT_WAIT', v_custco_id, '배분대기', '배분대기 상태', NULL, 'Y', 'Y', 'N', 15, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'WAIT', v_custco_id, '채팅대기', '채팅대기상태', NULL, 'Y', 'Y', 'N', 16, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'ING', v_custco_id, '상담중', '상담중 상태', NULL, 'Y', 'Y', 'N', 17, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'TRAN_WAIT', v_custco_id, '전달대기', '전달완료 후 대기 상태', NULL, 'Y', 'Y', 'N', 18, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'TEMP_SAVE', v_custco_id, '임시저장', '상담 임시저장', NULL, 'Y', 'Y', 'N', 19, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUSL_STAT', 'CLBK_WAIT', v_custco_id, '콜백대기', '콜백 대기상태', NULL, 'Y', 'Y', 'N', 20, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAUTION_TP', '1', v_custco_id, '상', '상', NULL, 'N', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAUTION_TP', '2', v_custco_id, '중', '중', NULL, 'N', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CAUTION_TP', '3', v_custco_id, '하', '하', NULL, 'N', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('KMS_CL', 'KNOWLEDGE', v_custco_id, '지식', '지식', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('KMS_CL', 'SCRIPT', v_custco_id, '스크립트', '스크립트', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_EXP_TP', '1', v_custco_id, '정상', '정상', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_EXP_TP', '2', v_custco_id, '연차', '연차', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_EXP_TP', '3', v_custco_id, '반차', '반차', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CMPN_EXP_TP', '4', v_custco_id, '기타', '기타', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_MSG_TP', 'BA', v_custco_id, '기본형', '카카오 알림톡 템플릿 메시지 기본형 유형', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_MSG_TP', 'EX', v_custco_id, '부가 정보형', '카카오 알림톡 템플릿 메시지 부가정보형 유형', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_MSG_TP', 'AD', v_custco_id, '채널 추가형', '카카오 알림톡 템플릿 메시지 채널추가형 유형', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_MSG_TP', 'MI', v_custco_id, '복합형', '카카오 알림톡 템플릿 메시지 복합형 유형', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('UPDOWN', 'UP', v_custco_id, '이상', '이상', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('UPDOWN', 'DOWN', v_custco_id, '이하', '이하', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PCS_PROD', 'CALL', v_custco_id, '전화 상담', '전화 상담', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PCS_PROD', 'SNS', v_custco_id, 'SNS 상담', 'SNS 상담', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PCS_PROD', 'SMS', v_custco_id, '문자 메시지', '문자 메시지', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PCS_PROD', 'ALTALK', v_custco_id, '알림톡', '알림톡', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_EPSZ_TP', 'NONE', v_custco_id, '사용안함', '카카오 알림톡 템플릿 강조 사용안함 유형', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_EPSZ_TP', 'TEXT', v_custco_id, '강조 표기형', '카카오 알림톡 템플릿 강조 강조표기형 유형', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_EPSZ_TP', 'IMAGE', v_custco_id, '이미지형', '카카오 알림톡 템플릿 강조 이미지형 유형', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_EPSZ_TP', 'ITEM_LIST', v_custco_id, '아이템 리스트형', '카카오 알림톡 템플릿 강조 아이템 리스트형 유형', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('TRANSFER_SRCH', 'EXT_NO', v_custco_id, '내선번호', '내선번호', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('TRANSFER_SRCH', 'CUSL_NM', v_custco_id, '상담직원 명', '상담직원 명', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('TRANSFER_SRCH', 'CUSL_ID', v_custco_id, '상담직원 ID', '상담직원 ID', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_BTN_LINK_TP', 'WL', v_custco_id, '웹 링크', '카카오톡 버튼 링크 웹링크 유형', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_BTN_LINK_TP', 'AL', v_custco_id, '앱 링크', '카카오톡 버튼 링크 앱링크 유형', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_BTN_LINK_TP', 'DS', v_custco_id, '배송 조회', '카카오톡 버튼 링크 배송조회 유형', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_BTN_LINK_TP', 'BK', v_custco_id, '봇 키워드', '카카오톡 버튼 링크 봇키워드 유형', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_BTN_LINK_TP', 'MD', v_custco_id, '메시지 전달', '카카오톡 버튼 링크 메시지전달 유형', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_BTN_LINK_TP', 'BC', v_custco_id, '상담톡 전환', '카카오톡 버튼 링크 상담톡전환 유형', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_BTN_LINK_TP', 'BT', v_custco_id, '봇 전환', '카카오톡 버튼 링크 봇전환 유형', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_BTN_LINK_TP', 'AC', v_custco_id, '채널 추가', '카카오톡 버튼 링크 채널 추가 유형', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_STAT_CD', 'R', v_custco_id, '대기(발송전)', 'MTS 알림톡 템플릿 상태 발송 대기 상태', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_STAT_CD', 'A', v_custco_id, '정상', 'MTS 알림톡 템플릿 상태 정상 발송 가능 상태', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_STAT_CD', 'S', v_custco_id, '중단', 'MTS 알림톡 템플릿 상태 발송 불가 상태', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_TMPL_STAT_CD', 'D', v_custco_id, '휴면', 'MTS 알림톡 템플릿 상태 휴면 상태', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '1', v_custco_id, '로그아웃', '로그아웃', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '2', v_custco_id, 'IN 통화', 'IN 통화', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '3', v_custco_id, 'IN 연결중', 'IN 연결중', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '4', v_custco_id, 'OB 통화', 'OB 통화', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '5', v_custco_id, 'OB 연결중', 'OB 연결중', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '6', v_custco_id, '대기', '대기', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '7', v_custco_id, '후처리', '후처리', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '8', v_custco_id, '휴식', '휴식', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '9', v_custco_id, '교육', '교육', NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '10', v_custco_id, '식사', '식사', NULL, 'Y', 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CTI_STAT_TP', '11', v_custco_id, '기타', '기타', NULL, 'Y', 'Y', 'N', 11, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_ISPT_STAT_CD', 'NOTREG', v_custco_id, '미등록', 'MTS 검수 상태 코드 미등록 상태 ', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_ISPT_STAT_CD', 'REG', v_custco_id, '등록', 'MTS 검수 상태 코드 등록 상태', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_ISPT_STAT_CD', 'REQ', v_custco_id, '심사요청', 'MTS 검수 상태 코드 심사요청 상태', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_ISPT_STAT_CD', 'APR', v_custco_id, '승인', 'MTS 검수 상태 코드 승인 상태', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MTS_ISPT_STAT_CD', 'REJ', v_custco_id, '반려', 'MTS 검수 상태 코드 반려 상태', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SRCH_TP', 'ALTMNT_RDY_REG_DT', v_custco_id, '상담접수일', '상담접수기준 조회', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SRCH_TP', 'CUTT_BGNG_DT', v_custco_id, '상담시작일', '상담시작일기준 조회', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SRCH_TP', 'CUTT_END_DT', v_custco_id, '상담종료일', '상담종료일기준 조회', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PHN_SE_TP', '0', v_custco_id, '30초 미만', '30초 미만', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PHN_SE_TP', '1', v_custco_id, '1분 미만', '1분 미만', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PHN_SE_TP', '2', v_custco_id, '2분 미만', '2분 미만', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PHN_SE_TP', '3', v_custco_id, '3분 미만', '3분 미만', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PHN_SE_TP', '4', v_custco_id, '4분 미만', '4분 미만', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PHN_SE_TP', '5', v_custco_id, '5분 미만', '5분 미만', NULL, 'Y', 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PHN_SE_TP', '7', v_custco_id, '7분 미만', '7분 미만', NULL, 'Y', 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PHN_SE_TP', '10', v_custco_id, '10분 미만', '10분 미만', NULL, 'Y', 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PHN_SE_TP', '11', v_custco_id, '10분 이상', '10분 이상', NULL, 'Y', 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('STATISTICS_TP', 'DEPT', v_custco_id, '부서별', '부서별', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('STATISTICS_TP', 'PERSONAL', v_custco_id, '개인별', '개인별', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('APRV_ST', 'RDY', v_custco_id, '대기', '대기', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('APRV_ST', 'APRV', v_custco_id, '승인', '승인', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('DEPT_CUSL_TP', 'DEPT', v_custco_id, '부서별', '부서별', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('DEPT_CUSL_TP', 'CUSL', v_custco_id, '개인별', '개인별', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_PHN_CHG_TP', 'NEW', v_custco_id, '신규', '신규', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_PHN_CHG_TP', 'ADD', v_custco_id, '통합', '통합', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_PHN_CHG_TP', 'ADD_DEL', v_custco_id, '통합제거', '통합제거', NULL, 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_PHN_CHG_TP', 'MOD', v_custco_id, '수정', '수정', NULL, 'Y', 'Y', 'N', '4', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_PHN_CHG_TP', 'MOD_DEL', v_custco_id, '수정제거', '수정제거', NULL, 'Y', 'Y', 'N', '5', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'CUST_LIST', v_custco_id, '고객 목록', '고객 목록', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'CUST_SEL', v_custco_id, '고객 상세 조회', '고객 상세 조회', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'CUST_MOD', v_custco_id, '고객 수정', '고객 수정', NULL, 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'CUST_REG', v_custco_id, '고객 등록', '고객 등록', NULL, 'Y', 'Y', 'N', '4', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'CUST_DEL', v_custco_id, '고객 삭제', '고객 삭제', NULL, 'Y', 'Y', 'N', '5', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'CUST_DOWN', v_custco_id, '고객 출력 다운로드', '고객 출력 다운로드', NULL, 'Y', 'Y', 'N', '6', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'USER_LIST', v_custco_id, '사용자 목록', '사용자 목록', NULL, 'Y', 'Y', 'N', '7', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'USER_SEL', v_custco_id, '사용자 상세 조회', '사용자 상세 조회', NULL, 'Y', 'Y', 'N', '8', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'USER_MOD', v_custco_id, '사용자 수정', '사용자 수정', NULL, 'Y', 'Y', 'N', '9', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'USER_REG', v_custco_id, '사용자 등록', '사용자 등록', NULL, 'Y', 'Y', 'N', '10', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'USER_DEL', v_custco_id, '사용자 삭제', '사용자 삭제', NULL, 'Y', 'Y', 'N', '11', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'USER_DOWN', v_custco_id, '사용자 출력 다운로드', '사용자 출력 다운로드', NULL, 'Y', 'Y', 'N', '12', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'CUTT_HIST_DOWN', v_custco_id, '상담 이력 다운로드', '상담 이력 다운로드', NULL, 'Y', 'Y', 'N', '13', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'AUDIO_PLAY', v_custco_id, '녹취파일 재생', '녹취파일 재생', NULL, 'Y', 'Y', 'N', '14', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'AUDIO_DOWN', v_custco_id, '녹취파일 다운로드', '녹취파일 다운로드', NULL, 'Y', 'Y', 'N', '15', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TASK_TP', 'LIST_EXCEL_DOWN', v_custco_id, '목록 엑셀 다운로드', '목록 엑셀 다운로드', NULL, 'Y', 'Y', 'N', '16', v_rgtr_id, v_reg_dt, NULL, NULL);
        
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TGRT_TP', 'CUST', v_custco_id, '고객', '고객', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_INQ_TGRT_TP', 'USER', v_custco_id, '사용자', '사용자', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_SCH_TP', 'NM', v_custco_id, '접근자 명', '접근자 명', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_SCH_TP', 'IP', v_custco_id, '접근자 IP', '접근자 IP', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('PRVC_SCH_TP', 'PATH', v_custco_id, '접근 메뉴 경로', '접근 메뉴 경로', NULL, 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_SCH_TP', 'CUST_NM', v_custco_id, '고객명', '고객명', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('VOC_SCH_TP', 'CUSL_NM', v_custco_id, '상담직원', '상담직원', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MSG_TMPL_TP', 'TP_LOCAL', v_custco_id, '위치정보 템플릿', '위치정보 템플릿', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MSG_TMPL_TP', 'TP_CONTACT', v_custco_id, '연락처정보 템플릿', '연락처정보 템플릿', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MSG_TMPL_TP', 'TP_SVY', v_custco_id, '설문조사', '설문조사', NULL, 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('LAYOUT_TMPL_TP', 'DEFAULT', v_custco_id, '기본형', '기본형', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('LAYOUT_TMPL_TP', 'ECOMMERCE', v_custco_id, '이커머스', '이커머스', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUTT_BMK_TP', 'RECENT_REG', v_custco_id, '최근등록', '최근등록', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUTT_BMK_TP', 'CUSL_REG', v_custco_id, '상담사등록', '상담사등록', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_REG_SCH_TP', 'REG_DT', v_custco_id, '등록일 기준', '등록일 기준', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CUST_REG_SCH_TP', 'MDFCN_DT', v_custco_id, '수정일 기준', '수정일 기준', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        
        --시간 아직 확정안됨 그래서 이거 아직 올리면 안됨
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SE_TP', '5', v_custco_id, '5분 미만', '5분 미만', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SE_TP', '10', v_custco_id, '10분 미만', '10분 미만', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SE_TP', '30', v_custco_id, '30분 미만', '30분 미만', NULL, 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SE_TP', '60', v_custco_id, '1시간 미만', '1시간 미만', NULL, 'Y', 'Y', 'N', '4', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SE_TP', '180', v_custco_id, '3시간 미만', '3시간 미만', NULL, 'Y', 'Y', 'N', '5', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SE_TP', '360', v_custco_id, '6시간 미만', '6시간 미만', NULL, 'Y', 'Y', 'N', '6', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SE_TP', '720', v_custco_id, '12시간 미만', '12시간 미만', NULL, 'Y', 'Y', 'N', '7', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SE_TP', '1440', v_custco_id, '1일 미만', '1일 미만', NULL, 'Y', 'Y', 'N', '8', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_SE_TP', '1440P', v_custco_id, '1일 이상', '1일 이상', NULL, 'Y', 'Y', 'N', '9', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_PRDCTN_TP', 'TOT', v_custco_id, '인입', '인입', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_PRDCTN_TP', 'TRAN', v_custco_id, '전달', '전달', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_PRDCTN_TP', 'CUTT', v_custco_id, '상담', '상담', NULL, 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHT_PRDCTN_TP', 'CMPL', v_custco_id, '완료', '완료', NULL, 'Y', 'Y', 'N', '4', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHAT_ST', 'CHT_END', v_custco_id, '배정중지', '상담은 가능하지만 더이상 배분이 되지 않는 상태', NULL, 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        
        --이석 구분(기본)
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SEAT_MOVE_TP', 'SMT_REST', v_custco_id, '휴식', '휴식', 'stat-rest', 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SEAT_MOVE_TP', 'SMT_EDU', v_custco_id, '교육', '교육', 'stat-edu', 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SEAT_MOVE_TP', 'SMT_LAUNCH', v_custco_id, '식사', '식사', 'stat-launch', 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SEAT_MOVE_TP', 'SMT_ETC', v_custco_id, '기타', '기타', 'stat-etc', 'Y', 'Y', 'N', '4', v_rgtr_id, v_reg_dt, NULL, NULL);
        
        --단축키_키_코드 추가. 2024.01.24. by hjh.
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '53', v_custco_id, '5', '5', NULL, NULL, 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '54', v_custco_id, '6', '6', NULL, NULL, 'Y', 'N', 6, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '55', v_custco_id, '7', '7', NULL, NULL, 'Y', 'N', 7, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '56', v_custco_id, '8', '8', NULL, NULL, 'Y', 'N', 8, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '57', v_custco_id, '9', '9', NULL, NULL, 'Y', 'N', 9, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '49', v_custco_id, '1', '1', NULL, NULL, 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '50', v_custco_id, '2', '2', NULL, NULL, 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '51', v_custco_id, '3', '3', NULL, NULL, 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '52', v_custco_id, '4', '4', NULL, NULL, 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', '48', v_custco_id, '0', '0', NULL, NULL, 'Y', 'N', 10, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SHROTCUT_CD', 'NO_USE', v_custco_id, '사용 안함', '단축키 사용 안함', NULL, NULL, 'Y', 'N', 0, v_rgtr_id, v_reg_dt, NULL, NULL);
        
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MONITOR_VIEW_TP', 'MVT_PHONE', v_custco_id, '전화상담만 표시', '전화상담만 표시', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MONITOR_VIEW_TP', 'MVT_SNS', v_custco_id, 'SNS상담만 표시', 'SNS상담만 표시', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('MONITOR_VIEW_TP', 'MVT_NOT_ALL', v_custco_id, '모니터링 표시 제외', '모니터링 표시 제외', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        
        /* 메시지 템플릿 등록(위치정보, 연락처정보 보내기) */
        INSERT INTO plt_sms_tmpl (sms_tmpl_id, tmpl_clsf_id, sms_tmpl_type_cd, sms_tmpl_nm, sms_tmpl_cn, file_group_key, sort_ord, use_yn, del_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
        VALUES(getSeqNo('SMS_TMPL_ID'), v_tmpl_clsf_id, 'TP_LOCAL', '위치정보 보내기', '#{CUS_NM} 회원님 안녕하세요.
위치정보 안내드립니다.

■ 위치명
#{LOC_NM}

■ 위   치
#{LOC}

■ 주   소
#{ADDR}', NULL, 2, 'Y', 'N', v_rgtr_id, v_reg_dt, v_rgtr_id, v_reg_dt);
        
        INSERT INTO plt_sms_tmpl (sms_tmpl_id, tmpl_clsf_id, sms_tmpl_type_cd, sms_tmpl_nm, sms_tmpl_cn, file_group_key, sort_ord, use_yn, del_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
        VALUES(getSeqNo('SMS_TMPL_ID'), v_tmpl_clsf_id, 'TP_CONTACT', '연락처정보 보내기', '#{CUS_NM} 회원님 안녕하세요.
문의하신 연락처정보 안내드립니다.

■ 연락처
#{TEL}

■ 지역
#{LOC}

■ 담당부서
#{OBJ}

■ 담당업무
#{WORK}', NULL, 3, 'Y', 'N', v_rgtr_id, v_reg_dt, v_rgtr_id, v_reg_dt);

        INSERT INTO plt_sms_tmpl (sms_tmpl_id, tmpl_clsf_id, sms_tmpl_type_cd, sms_tmpl_nm, sms_tmpl_cn, file_group_key, sort_ord, use_yn, del_yn, rgtr_id, reg_dt, mdfr_id, mdfcn_dt)
        VALUES(getSeqNo('SMS_TMPL_ID'), v_tmpl_clsf_id, 'TP_SVY', '설문조사', '#{CUS_NM} 회원님 안녕하세요.
설문조사 링크를 보내드릴게요!

■ 설문조사
#{LINK}', NULL, 4, 'Y', 'N', v_rgtr_id, v_reg_dt, v_rgtr_id, v_reg_dt);


        --채널 분류 추가. 2024.02.26. by hjh.
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHNL_CL', 'NTT', v_custco_id, '네이버톡톡', '네이버톡톡', 'CHAT', 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHNL_CL', 'BBS', v_custco_id, '게시판', '게시판', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('CHNL_CL', 'EMAIL', v_custco_id, '이메일', '이메일', NULL, 'Y', 'Y', 'N', 5, v_rgtr_id, v_reg_dt, NULL, NULL);
        --이메일 프로토콜 유형 추가. 2024.02.26. by hjh.
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'EMAIL_PROTOCOL_TP', v_custco_id, '이메일 프로토콜 유형', '이메일 프로토콜 유형', NULL, 'Y', 'Y', 'N', 109, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EMAIL_PROTOCOL_TP', 'imaps', v_custco_id, 'imaps', 'imap Secure', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EMAIL_PROTOCOL_TP', 'imap', v_custco_id, 'imap', 'imap', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        --수집 작업 관리 상태 추가. 2024.02.29. by hjh.
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'JOB_MNG_ST', v_custco_id, '수집 작업 관리 상태', '수집 작업 관리 상태', NULL, 'Y', 'Y', 'N', 112, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('JOB_MNG_ST', 'WAIT', v_custco_id, '대기', '대기', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('JOB_MNG_ST', 'RUN', v_custco_id, '실행중', '실행중', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd (group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('JOB_MNG_ST', 'STOP', v_custco_id, '사용중지', '사용중지', NULL, 'Y', 'Y', 'N', 3, v_rgtr_id, v_reg_dt, NULL, NULL);
        
        --SNS 통계 상담 구분 코드
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('*', 'CUTT_SE_TP', v_custco_id, '상담 유형 구분', '상담 유형 구분', NULL, 'Y', 'Y', 'N', '113', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('CUTT_SE_TP', 'CST_GENR', v_custco_id, '일반상담', '일반상담', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('CUTT_SE_TP', 'CST_CHBT', v_custco_id, '챗봇상담', '챗봇상담', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        
        -- 확장항목 구분 - 설문 참여자 추가. 20240401. by hjh.
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID, CD_ID, CUSTCO_ID, CD_NM, CD_EXPLN, CD_VL, SYS_CD_YN, USE_YN, DEL_YN, SORT_ORD, RGTR_ID, REG_DT, MDFR_ID, MDFCN_DT) VALUES('CATG_EXT_CL', 'SRVY', v_custco_id, '설문참여자', 'Survey Target', NULL, 'Y', 'Y', 'N', 4, v_rgtr_id, v_reg_dt, NULL, NULL);
        
        --게시판 문의 유형 코드
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('*', 'BBS_INQ_TP', v_custco_id, '게시판 문의 유형', '게시판 문의 유형', 'CHAT', 'Y', 'Y', 'N', '120', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('BBS_INQ_TP', 'INQRY_CUST', v_custco_id, '고객문의', '고객문의', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('BBS_INQ_TP', 'INQRY_GDS', v_custco_id, '상품문의', '상품문의', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);

        --게시물 문의 유형 코드
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('*', 'PST_INQ_TP', v_custco_id, '게시물 문의 유형', '게시물 문의 유형', NULL, 'Y', 'Y', 'N', '121', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('PST_INQ_TP', 'PRODUCT', v_custco_id, '상품', '상품', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('PST_INQ_TP', 'DELIVERY', v_custco_id, '배송', '배송', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('PST_INQ_TP', 'RETURN', v_custco_id, '반품', '반품', NULL, 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('PST_INQ_TP', 'EXCHANGE', v_custco_id, '교환', '교환', NULL, 'Y', 'Y', 'N', '4', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('PST_INQ_TP', 'CANCEL', v_custco_id, '취소 및 환불', '취소 및 환불', NULL, 'Y', 'Y', 'N', '5', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('PST_INQ_TP', 'ETC', v_custco_id, '기타', '기타', NULL, 'Y', 'Y', 'N', '6', v_rgtr_id, v_reg_dt, NULL, NULL);

        --검색 기간 코드
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('*', 'LKAG_SRCH_TERM', v_custco_id, '검색 기간', '검색 기간', NULL, 'Y', 'Y', 'N', '122', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('LKAG_SRCH_TERM', 'LST_1_DAY', v_custco_id, '오늘', '오늘', NULL, 'Y', 'Y', 'N', '1', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('LKAG_SRCH_TERM', 'LST_3_DAY', v_custco_id, '최근 3일', '최근 3일', NULL, 'Y', 'Y', 'N', '2', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('LKAG_SRCH_TERM', 'LST_1_WEEK', v_custco_id, '최근 1주', '최근 1주', NULL, 'Y', 'Y', 'N', '3', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('LKAG_SRCH_TERM', 'LST_1_MONTH', v_custco_id, '최근 1달', '최근 1달', NULL, 'Y', 'Y', 'N', '4', v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO PLT_COMM_CD(GROUP_CD_ID,CD_ID,CUSTCO_ID,CD_NM,CD_EXPLN,CD_VL,SYS_CD_YN,USE_YN,DEL_YN,SORT_ORD,RGTR_ID,REG_DT,MDFR_ID,MDFCN_DT) VALUES('LKAG_SRCH_TERM', 'LST_3_MONTH', v_custco_id, '최근 3달', '최근 3달', NULL, 'Y', 'Y', 'N', '5', v_rgtr_id, v_reg_dt, NULL, NULL);
        
        --설문 참여자 제외 유형 추가
        INSERT INTO plt_comm_cd(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SUVY_EXL_TRGT_TP', v_custco_id, '설문 참여자 제외 유형', '설문 참여자 제외 유형', NULL, 'Y', 'Y', 'N', (select MAX(SORT_ORD)+1 from plt_comm_cd where GROUP_CD_ID='*'), v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_EXL_TRGT_TP', 'DENY', v_custco_id, '설문거부', '설문거부', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_EXL_TRGT_TP', 'EXCLUDE', v_custco_id, '설문제외', '설문제외', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        
        --설문 참여자 제외 검색 유형 추가
        INSERT INTO plt_comm_cd(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'SUVY_EXL_SRCH_TP', v_custco_id, '설문 참여자 제외 검색 유형', '설문 참여자 제외 검색 유형', NULL, 'Y', 'Y', 'N', (select MAX(SORT_ORD)+1 from plt_comm_cd where GROUP_CD_ID='*'), v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_EXL_SRCH_TP', 'CUST_NM', v_custco_id, '성명', '성명', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('SUVY_EXL_SRCH_TP', 'CUST_PHN_NO', v_custco_id, '전화번호', '전화번호', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        
        --설문조사 메뉴 정렬 변경
        update plt_menu set sort_ord=3 where menu_id=35;
        update plt_menu set sort_ord=4 where menu_id=36;
    
    
        INSERT INTO plt_comm_cd(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('*', 'EML_TP', v_custco_id, '이메일 발송 유형', '이메일 발송 유형', NULL, 'Y', 'Y', 'N', (select MAX(SORT_ORD)+1 from plt_comm_cd where GROUP_CD_ID='*'), v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EML_TP', 'chatemail', v_custco_id, '채팅상담', '채팅상담', NULL, 'Y', 'Y', 'N', 1, v_rgtr_id, v_reg_dt, NULL, NULL);
        INSERT INTO plt_comm_cd(group_cd_id, cd_id, custco_id, cd_nm, cd_expln, cd_vl, sys_cd_yn, use_yn, del_yn, sort_ord, rgtr_id, reg_dt, mdfr_id, mdfcn_dt) VALUES('EML_TP', 'svy', v_custco_id, '설문조사', '설문조사', NULL, 'Y', 'Y', 'N', 2, v_rgtr_id, v_reg_dt, NULL, NULL);
        
    end if;

END;