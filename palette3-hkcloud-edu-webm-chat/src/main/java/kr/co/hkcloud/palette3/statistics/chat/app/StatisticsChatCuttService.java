package kr.co.hkcloud.palette3.statistics.chat.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface StatisticsChatCuttService
{
    TelewebJSON chatCuttTotalStatistics(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON chatCuttChnTypeStatistics(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON chatCuttHrStatistics(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON chatCuttTypeStatistics(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON chatCuttMonStatistics(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON chatCuttPrdctnStatistics(TelewebJSON jsonParams) throws TelewebAppException;
    /**
     *
     * 채팅상담 상담 직원별 통계 - 상단 건수
     * @Method Name  	: chatCuttCuslStatistics
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON chatCuttCuslStatistics(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     *
     * 채팅상담 상담 직원별 통계 - 상단 건수
     * @Method Name  	: chatCuttCuslStatistics
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON chatCuttCuslStatisticsList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     *
     * 채팅상담 문의유형별 통계 - 상담 건수
     * @Method Name  	: selectStatisticsByInqryType
     * @date   			: 2023. 12. 14.
     * @author   		: njy
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectStatisticsByInqryType(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     *
     * 채팅상담 문의유형별 통계 - 문의유형 조회
     * @Method Name  	: selectInqryTypeTree
     * @date   			: 2023. 12. 14.
     * @author   		: njy
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectInqryTypeTree(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     *
     * 채팅상담 유형별 통계
     * @Method Name  	: selectCuttTypeStatistics
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCuttTypeStatistics(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     *
     * 채팅상담 일자별 통계 - 선 그래프
     * @Method Name  	: chatCuttDateStatistics
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON chatCuttDateStatistics(TelewebJSON jsonParams) throws TelewebAppException;
    /**
     *
     * 채팅상담 일자별 통계 - 그리드 데이터
     * @Method Name  	: chatCuttDateStatisticsList
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON chatCuttDateStatisticsList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     *
     * 채팅상담 통합생산성 통계 - 선 그래프
     * @Method Name  	: selectCuttPrdctnStatistics
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCuttPrdctnStatistics(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     *
     * 채팅상담 통합생산성 통계 - 그리드 그래프
     * @Method Name  	: selectCuttPrdctnStatisticsList
     * @date   			: 2023. 12. 14.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCuttPrdctnStatisticsList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     *
     * 채팅통계 상담건수 포함항목 설정값 조회
     * @Method Name  	: getStatStng
     * @date   			: 2024. 3. 21.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON getStatStng(TelewebJSON jsonParams) throws TelewebAppException;

}
