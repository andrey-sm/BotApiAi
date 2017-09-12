package pro.smartum.botapiai.exceptions;

public enum ErrorCode {

    INTERNAL_SERVER(1, "Server error. Please, try later"),
    NOT_VALID_REQUEST_DATA(2, "Not valid data"),
    NOT_IMPLEMENTED_YET(3, "Not implemented yet"),
    CONVERSATION_NOT_EXISTS(4, "Conversation not exists"),
    NOT_VALID_MESSENGER(5, "Not valid messenger");

    private final int errorCode;
    private final String message;

    ErrorCode(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorCode parseErrorCode(int errorCode) {
        for (ErrorCode code : values()) {
            if (code.getErrorCode() == errorCode)
                return code;
        }
        return ErrorCode.INTERNAL_SERVER;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

}

