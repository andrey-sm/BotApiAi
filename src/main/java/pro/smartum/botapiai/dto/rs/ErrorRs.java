package pro.smartum.botapiai.dto.rs;

import lombok.Data;

@Data
public class ErrorRs {

    private final int errorCode;
    private final String message;
}
