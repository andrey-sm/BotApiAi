package pro.smartum.botapiai.configuration.web;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pro.smartum.botapiai.dto.rs.ErrorRs;
import pro.smartum.botapiai.dto.rs.ValidationErrorsRs;
import pro.smartum.botapiai.exceptions.ApplicationException;
import pro.smartum.botapiai.exceptions.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ErrorHandler {

    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorRs handleApplicationException(Exception e) {
        return handleException(e);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorsRs processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return processFieldErrors(fieldErrors);
    }


    //@ExceptionHandler({NeedTokenException.class, InvalidTokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorRs processInvalidTokenException(ApplicationException ex) {
        return handleApplicationException(ex);
    }

    private ErrorRs handleException(Exception e) {
        ErrorCode errorCode = getErrorCodeForException(e);
        String message = errorCode.getMessage();
        if (e.getLocalizedMessage() != null) {
            message = e.getLocalizedMessage();
        }
        LOGGER.warn(e.getMessage(), e);
        return new ErrorRs(errorCode.getErrorCode(), message);
    }

    private ValidationErrorsRs processFieldErrors(List<FieldError> fieldErrors) {
        List<String> messages = new ArrayList<>();
        for (FieldError fieldError :  fieldErrors) {
            messages.add("field: '" + fieldError.getField() + "' with value: '" + fieldError.getRejectedValue() + "'" + " " + fieldError.getDefaultMessage());
        }
        return new ValidationErrorsRs(messages);
    }


    private ErrorCode getErrorCodeForException(Exception e) {
        if (e instanceof ApplicationException)
            return ((ApplicationException) e).getErrorCode();
        return ErrorCode.INTERNAL_SERVER;
    }
}
