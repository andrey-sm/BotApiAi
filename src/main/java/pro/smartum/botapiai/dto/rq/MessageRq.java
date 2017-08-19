package pro.smartum.botapiai.dto.rq;


import lombok.Data;
import pro.smartum.botapiai.dto.OriginalRequestDto;
import pro.smartum.botapiai.dto.ResultDto;
import pro.smartum.botapiai.dto.StatusDto;

@Data
public class MessageRq {

    private final String id;
    private final String lang;
    private final StatusDto status;
    private final String timestamp;
    private final String sessionId;
    private final ResultDto result;
    private final OriginalRequestDto originalRequest;

}
