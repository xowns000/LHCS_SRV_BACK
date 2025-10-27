package kr.co.hkcloud.palette3.constant;

/**
 * Constants for database
 *
 * @author Jun Hyeong Jo
 * @since 2025-01-12
 */
public class DatabaseConstants {
    /**
     * Mapper Names
     */
    public static final String COMMENT_MAPPER_NAME = "kr.co.hkcloud.palette3.board.dao.CommentMapper";
    public static final String POST_MAPPER_NAME = "kr.co.hkcloud.palette3.board.dao.BoardMapper";
    public static final String TRANSFER_ALLOCATION_MAPPER_NAME = "kr.co.hkcloud.palette3.phone.transfer.dao.PhoneTransferManageMapper";

    /**
     * Columns
     */
    public static final String COMMENT_ID_KEY = "CMNT_ID";
    public static final String HIGHER_COMMENT_ID_KEY = "HIGR_CMNT_ID";
    public static final String BOARD_ID_KEY = "BBS_ID";
    public static final String CUSTOMER_COMPANY_ID_KEY = "CUSTCO_ID";
    public static final String POST_ID_KEY = "PST_ID";
    public static final String USER_ID_KEY = "USER_ID";
    public static final String REGISTER_ID_KEY = "RGTR_ID";
    public static final String POST_TITLE_KEY = "PST_TTL";

    public static final String TRANSFER_ALLOCATION_SEQ_KEY = "CUTT_TRNSF_ALTMNT_HSTRY_ID";
    public static final String CONSULTATION_TRANSFER_ID_KEY = "CUTT_TRNSF_ID";
    public static final String CONSULTANT_ID_KEY = "CUSL_ID";
    public static final String TYPE_KEY = "TYPE";
    public static final String REGISTRANT_ID_KEY = "RGTR_ID";
    public static final String REGISTRATION_DATE_KEY = "REG_DT";
    public static final String TARGET_OPTION_CODE_KEY = "TARGET_OPT_CD";
    public static final String TARGET_USER_ID_KEY = "TRGT_USER_ID";

    /**
     * Names
     */
    public static final String HAS_POST_PERMISSION_KEY = "HAS_POST_PERMISSION";

}
