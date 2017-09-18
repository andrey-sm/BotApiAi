package pro.smartum.botapiai.exceptions;

public class GetSkypeTokenException extends ApplicationException {

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.GET_SKYPE_TOKEN;
    }
}
