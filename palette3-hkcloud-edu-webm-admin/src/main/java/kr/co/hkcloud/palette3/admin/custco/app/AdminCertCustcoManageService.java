package kr.co.hkcloud.palette3.admin.custco.app;

import org.json.simple.parser.ParseException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface AdminCertCustcoManageService {

    public TelewebJSON selectCertCustco(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectCertCustcoInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectServiceSetting(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectServiceAccount(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectIpccSetting(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON insertCertCustcoInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON updateCertCustcoInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON syncTenantCustco(TelewebJSON serviceAccountJSON, TelewebJSON certCustcoJSON,
        TelewebJSON serviceSettingJSON) throws TelewebAppException;

    public TelewebJSON updateServiceSetting(TelewebJSON mjsonParams) throws TelewebAppException, ParseException;

    public TelewebJSON syncTenantCustcoSrvc(TelewebJSON serviceAccountJSON, TelewebJSON srvcGdsDtlJSON) throws TelewebAppException;

    public TelewebJSON updateServiceAccount(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON updateIpccSetting(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON updateLocaleInfoSetting(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON createTenant(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON migrationFlyway(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectCertCustco4TenantSave(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON createCustcoUser(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON createCustcoData(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON updateCustcoSrvcCrtDt(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectTenantOfLgnId(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON dpcnChkSchemaId(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON dpcnChkLgnId(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON dpcnChkAspCustKey(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON dpcnChkDsptchNo(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectSrvcGds(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectSrvcGdsDtl(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectIpccSrvr(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectCertCustcoDsptchNo(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectAllSchemaIds() throws TelewebAppException;

    public TelewebJSON selectIpccSrvrCertCustco(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON syncTenantCustcoApiUri(TelewebJSON serviceAccountJSON, TelewebJSON ipccSrvrJSON) throws TelewebAppException;

    public TelewebJSON syncTenantCustcoDsptchNo(TelewebJSON serviceAccountJSON,
        TelewebJSON certCustcoDsptchNoListJSON) throws TelewebAppException;

    public TelewebJSON chkDeleteDsptchNo(TelewebJSON serviceAccountJSON, TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectUserList(TelewebJSON mjsonParams) throws TelewebAppException;

    public TelewebJSON selectCertCustcoLocaleInfo(TelewebJSON mjsonParams) throws TelewebAppException;
    
    public TelewebJSON createDbUser4Oracle(TelewebJSON mjsonParams) throws TelewebAppException;

}
