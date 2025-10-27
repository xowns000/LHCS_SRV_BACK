package kr.co.hkcloud.palette3.phone.transfer.enumer;

/**
 * @author Jun Hyeong Jo
 * @since 2025-01-22
 */
public enum TransferAltmntType {
    ASSIGNED("ASSIGNED"),
    REVOKED("REVOKED");

    private final String state;

    TransferAltmntType(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
