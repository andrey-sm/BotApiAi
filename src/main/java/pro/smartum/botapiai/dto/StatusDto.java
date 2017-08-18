package pro.smartum.botapiai.dto;


import lombok.Data;

@Data
public class StatusDto {

    private final String errorType;
    private final int code;
}
