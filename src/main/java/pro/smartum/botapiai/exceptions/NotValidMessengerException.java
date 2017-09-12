package pro.smartum.botapiai.exceptions;

public class NotValidMessengerException extends ApplicationException {

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NOT_VALID_MESSENGER;
    }
}
