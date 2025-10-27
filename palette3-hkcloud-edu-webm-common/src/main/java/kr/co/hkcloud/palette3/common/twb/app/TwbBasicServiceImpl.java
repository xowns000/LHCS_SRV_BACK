package kr.co.hkcloud.palette3.common.twb.app;


import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.datasources.enumer.DatasourcePoolName;
import kr.co.hkcloud.palette3.config.properties.datasources.DatasourcesProperties;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("twbBasicService")
public class TwbBasicServiceImpl implements TwbBasicService
{
    private final DatasourcesProperties datasourcesProperties;
    private final TwbComDAO             twbComDAO;


    /**
     * TelewebDAO 실행
     * 
     * @param  acJson : Input Json
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON excuteCom(TelewebJSON acJson, HttpServletRequest objRequest) throws TelewebAppException
    {
        String sqlNameSpace = acJson.getHeaderString("TWB_SQL_NAME_SPACE");
        String sqlId = acJson.getHeaderString("TWB_SQL_ID");
        String sqlType = sqlId.substring(0, 6).toUpperCase();

        DatasourcePoolName datasourcePoolName = null;
        if(acJson.containsHeaderKey("POOL_NM")) {
            datasourcePoolName = DatasourcePoolName.valueOf(acJson.getHeaderString("POOL_NM"));
        }
        else {
            datasourcePoolName = datasourcesProperties.getDatasource().getDefaultPoolName();
        }

        try {
            if("SELECT".equals(sqlType.substring(0, 6))) {
                acJson = twbComDAO.select(sqlNameSpace, sqlId, acJson, objRequest, datasourcePoolName);
            }
            else if("INSERT".equals(sqlType.substring(0, 6))) {
                acJson = twbComDAO.insert(sqlNameSpace, sqlId, acJson, objRequest, datasourcePoolName);
            }
            else if("UPDATE".equals(sqlType.substring(0, 6))) {
                acJson = twbComDAO.update(sqlNameSpace, sqlId, acJson, objRequest, datasourcePoolName);
            }
            else if("DELETE".equals(sqlType.substring(0, 6))) {
                acJson = twbComDAO.delete(sqlNameSpace, sqlId, acJson, objRequest, datasourcePoolName);
            }
            else if("PROCEDURE".equals(sqlType.substring(0, 9))) {
                /* 데이터 치리후 추출 시 SELECT, 데이터 수정 후 값 받을 경우는 UPDATE 사용 */
                /* 프로시져 정의는 마이바티스 매뉴얼 참조 */
            }
            else {
                acJson.setHeader("COUNT", 0);
                acJson.setHeader("TOT_COUNT", 0);
                acJson.setHeader("ERROR_FLAG", true);
                acJson.setHeader("ERROR_MSG", sqlId + " :: SQL 정의 오류 입니다. Type 정보를 확인 하세요.");
            }
        }
        catch(Exception ex) {
            Throwable cause = ex;
            while(cause.getCause() != null) {
                cause = cause.getCause();
            }
            acJson.setHeader("ERROR_FLAG", true);
            acJson.setHeader("ERROR_MSG", sqlId + ":처리 중 오류가 발생하였습니다.\n" + cause.getMessage());

            String strLog = "\n";
            strLog += "IDENTIFIER     : " + acJson.getHeaderString("TELEWEB_IDENTIFIER") + "\n";
            strLog += "CALL_TYPE      : " + "XML_SERVICE" + "\n";
            strLog += "SQL_NAME_SPACE : " + sqlNameSpace + "\n";
            strLog += "SQL_ID         : " + sqlId + "\n";
            strLog += "ERROR_MSG      : " + "처리 중 오류가 발생하였습니다.\n" + ex.getMessage() + "\n";

            log.error("{} ::: {}", strLog, ex);
        }
        return acJson;
    }

}
