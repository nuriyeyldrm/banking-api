package moneyTransfer.enums;

public enum Http {

    OK(200, "OK");

    private final int code;
    private final String reason;

    private Http(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
