update plt_comm_cd set 
sys_cd_yn='N'
where group_cd_id='SUVY_EXL_TRGT_TP' or cd_id ='SUVY_EXL_TRGT_TP';

update plt_menu set 
use_yn='N',
del_yn='Y'
where menu_nm='시스템 로그 조회';
