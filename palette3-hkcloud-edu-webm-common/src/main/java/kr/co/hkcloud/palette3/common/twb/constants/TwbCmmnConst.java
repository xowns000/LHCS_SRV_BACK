/********************************************************************************
 * @classDescription FrameWork에서 사용하는 상수정의 클래스
 * @author           MPC Corp.
 * @version          1.0 ------------------------------------------------------------------------------- Modification Information Date Developer Content ------- ------------- ------------------------- 2014.09.17 R&D팀
 *                       신규생성 ------------------------------------------------------------------------------- Copyright (C) 2014 by MPC Corp. All rights reserved.
 *********************************************************************************/
package kr.co.hkcloud.palette3.common.twb.constants;

public class TwbCmmnConst
{

    /**
     * 처리구분값(초기모드)
     */
    public static final String TRANS_NONE = "N";

    /**
     * 처리구분값(신규모드)
     */
    public static final String TRANS_INS = "I";

    /**
     * 처리구분값(업데이트모드)
     */
    public static final String TRANS_UPD = "U";

    /**
     * 처리구분값(삭제모드)
     */
    public static final String TRANS_DEL = "D";

    /**
     * 고정해더 데이터그룹
     */
    public static final String G_HEADER = "HEADER";

    /**
     * 기본데이터그룹
     */
    public static final String G_DATA = "DATA";

    /**
     * NULL 처리 타입
     */
    public static final String SQL_TYPE_NULL = "[!NULL!]";

    /**
     * 벨리데이션 체크
     */
    public static final Integer LIMITED_CHARACTERS_1300 = 1300;
    /**
     * 벨리데이션 체크
     */
    public static final Integer LIMITED_CHARACTERS_1000 = 1000;
}
