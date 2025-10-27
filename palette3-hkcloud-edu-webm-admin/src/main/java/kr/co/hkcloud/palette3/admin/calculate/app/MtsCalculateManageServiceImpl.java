package kr.co.hkcloud.palette3.admin.calculate.app;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import kr.co.hkcloud.palette3.admin.config.FlywayBuilder;
import kr.co.hkcloud.palette3.admin.custco.domain.IpccRollbackDTO;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserBizService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.security.TeletalkAuthority;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.agent.app.SettingAgentManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Slf4j
@RequiredArgsConstructor
@Service("MtsCalculateManageService")
public class MtsCalculateManageServiceImpl implements MtsCalculateManageService {

    private final TwbComDAO mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final SettingAgentManageService settingAgentManageService;
    private final TwbUserBizService twbUserBizService;
    private final RestTemplate restTemplate;
    private final FlywayBuilder flywayBuilder;
    //예약된 스키마 ID는 사용 불가 - public, custco, ttalk, chatgpt
    private static List<String> RESERVED_SCHEMA_IDS = Arrays.asList("public", "custco", "ttalk", "chatgpt");

    /**
     * 기업고객관리 목록 조회
     */
    @Override
    @Secured(TeletalkAuthority.ROLES.SYSTEM)
    @Transactional(readOnly = true)
    public TelewebJSON sndngStat(TelewebJSON mjsonParams) throws TelewebAppException {
    	mjsonParams.setString("CUSTCO_ID","1");
        return mobjDao.select("kr.co.hkcloud.palette3.admin.custco.dao.MtsCalculateManageMapper", "sndngStat", mjsonParams);
    }

}
