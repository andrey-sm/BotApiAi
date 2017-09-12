package pro.smartum.botapiai.exceptions;

public class NotImplementedYetException extends ApplicationException {

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NOT_IMPLEMENTED_YET;
    }
}
