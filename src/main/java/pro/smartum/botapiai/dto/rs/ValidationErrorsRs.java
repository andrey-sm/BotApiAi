package pro.smartum.botapiai.dto.rs;

import lombok.Data;
import pro.smartum.botapiai.exceptions.ErrorCode;

import java.util.List;

@Data
public class ValidationErrorsRs {

    private final int errorCode = ErrorCode.NOT_VALID_REQUEST_DATA.getErrorCode();
    private final List<String> errors;

}
