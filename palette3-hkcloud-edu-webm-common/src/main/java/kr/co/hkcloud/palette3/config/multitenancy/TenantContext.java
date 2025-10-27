package kr.co.hkcloud.palette3.config.multitenancy;

/**
 * 테넌트 Context DB Set Schema 처리를 위함.(currentTenant 값 == schema 값)
 *
 * @author 황종혁
 */
public class TenantContext {

    public static final String DEFAULT_TENANT_CODE = "custco";
    public static final String DEFAULT_CUSTCO_CODE = "1";
    private static ThreadLocal<String> currentTenant = ThreadLocal.withInitial(
        () -> DEFAULT_TENANT_CODE);
    private static ThreadLocal<String> currentCustco = ThreadLocal.withInitial(
        () -> DEFAULT_CUSTCO_CODE);

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void setCurrentCustco(String custco) {
        currentCustco.set(custco);
    }

    public static String getCurrentCustco() {
        return currentCustco.get();
    }

    public static String getTenantRedisPrefix() {
        return currentTenant.get() + ":" + currentCustco.get();
    }

    public static void clear() {
        currentTenant.remove();
        currentCustco.remove();
    }
}
