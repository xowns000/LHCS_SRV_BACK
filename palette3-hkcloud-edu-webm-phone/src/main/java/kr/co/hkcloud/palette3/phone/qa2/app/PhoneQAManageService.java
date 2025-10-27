package kr.co.hkcloud.palette3.phone.qa2.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneQAManageService
{

    /* QA평가 > 평가표 리스트 조회 */
    TelewebJSON selectRtnQaEva(TelewebJSON jsonParams) throws TelewebAppException;
    /* QA평가 > 평가표 등록 */
    TelewebJSON insertRtnQaEva(TelewebJSON jsonParams) throws TelewebAppException;
    /* QA평가 > 평가표 수정 */
    TelewebJSON updateRtnQaEva(TelewebJSON jsonParams) throws TelewebAppException;
    /* QA평가 > 평가표 삭제 */
    TelewebJSON deleteRtnQaEva(TelewebJSON jsonParams) throws TelewebAppException;
    /* QA평가 > 평가표 복사 */
    TelewebJSON insertRtnCopyQaEva(TelewebJSON jsonParams) throws TelewebAppException;

    /* QA평가 > 평가 문항 리스트 조회 */
    TelewebJSON selectRtnQaQs(TelewebJSON jsonParams) throws TelewebAppException;
    /* QA평가 > 평가 문항 등록 */
    TelewebJSON insertRtnQaQs(TelewebJSON jsonParams) throws TelewebAppException;
    /* QA평가 > 평가 문항 삭제 */
    TelewebJSON deleteRtnQaQs(TelewebJSON jsonParams) throws TelewebAppException;
    /* QA평가 > 평가 문항 복사 */
    TelewebJSON insertRtnCopyQaQs(TelewebJSON jsonParams) throws TelewebAppException;

    /* QA평가 > 평가 보기 리스트 조회 */
    TelewebJSON selectRtnQaVe(TelewebJSON jsonParams) throws TelewebAppException;

    /* QA평가 > 평가 상세 리스트 조회 */
    TelewebJSON selectRtnQaEvaRst(TelewebJSON jsonParams) throws TelewebAppException;

    /* QA평가 > 평가 상세 등록 */
    TelewebJSON insertRtnQaEvaRst(TelewebJSON jsonParams) throws TelewebAppException;

    /* QA평가 > 미리보기 */
    TelewebJSON selectRtnQaPreView(TelewebJSON jsonParams) throws TelewebAppException;

    /* QA평가 > 미리보기 항목 보기 조회 */
    TelewebJSON selectRtnQaVePreView(TelewebJSON jsonParams) throws TelewebAppException;
}
