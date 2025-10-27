package kr.co.hkcloud.palette3.admin.layoutMng.service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

/**
 * packageName    : kr.co.hkcloud.palette3.admin.layoutMng.app
 * fileName       :
 * author         : njy
 * date           : 2024-03-20
 * description    : <<여기 설명>>
 * <pre>
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-03-20           njy            최초 생성
 * </pre>
 */
public interface LayoutMngBscInfoService {
    public TelewebJSON selectLayoutList(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON selectLayoutClsfList(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON selectCertCustcoInfo4Layout(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON selectLkagByCertCustco(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON insertUpdateBscInfo(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON deleteBscInfo(TelewebJSON jsonParam) throws TelewebAppException;
    public TelewebJSON updateUseLayout(TelewebJSON jsonParam) throws TelewebAppException;
}
