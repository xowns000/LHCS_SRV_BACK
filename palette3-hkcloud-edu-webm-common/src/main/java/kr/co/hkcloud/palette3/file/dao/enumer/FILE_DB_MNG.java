package kr.co.hkcloud.palette3.file.dao.enumer;

/**
 * 파일 DB 관리 관련 테이블과 컬럼 정의 ㄴ Rule : 케밥케이스, 모두 대문자로 정의
 * 
 * @author leeiy
 *
 */
public final class FILE_DB_MNG {

    //테이블
    public enum TABLE {

        //첨부파일공통
        PLT_FILE
    }

    //컬럼정의
    public static final class COLUMN {

        //첨부파일공통
        public enum PLT_FILE {
            FILE_GROUP_KEY      	//파일그룹키
            , FILE_KEY           	//파일키
            , CUSTCO_ID        		//고객사_ID
            , TASK_TYPE_CD          //업무구분
            , TRGT_TYPE_CD         	//저장소대상구분
            , PATH_TYPE_CD          //경로구분
            , MIME_TYPE_CD          //MIME유형
            , ACTL_FILE_NM   		//진짜파일명
            , STRG_FILE_NM       	//저장된파일명
            , FILE_PATH           	//파일경로
            , FILE_SZ           	//파일크기
            , FILE_EXTN           	//파일확장자
            , DWNLD_CNT           	//다운로드수
            , FILE_URL            	//파일URL
            , FILE_BLOB           	//파일
            , ACS_KEY          		//접근키
            , FILE_ACS_TYPE_CD    	//파일엑세스유형(public:공개,private:비공개)
            , USE_YN              	//사용여부
            , RGTR_ID				//등록자_ID
            , REG_DT				//등록일시
            , MDFR_ID				//수정자_ID
            , MDFCN_DT				//수정일시
        }
    }
}
