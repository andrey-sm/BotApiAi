package pro.smartum.botapiai.exceptions;

public class ConversationNotExistsException extends ApplicationException {

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CONVERSATION_NOT_EXISTS;
    }
}
