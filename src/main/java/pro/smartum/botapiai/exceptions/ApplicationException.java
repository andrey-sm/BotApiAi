package pro.smartum.botapiai.exceptions;


public abstract class ApplicationException extends RuntimeException {

    public abstract ErrorCode getErrorCode();
}
