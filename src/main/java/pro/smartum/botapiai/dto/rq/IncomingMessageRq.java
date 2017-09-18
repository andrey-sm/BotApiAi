package pro.smartum.botapiai.dto.rq;


import lombok.Data;
import pro.smartum.botapiai.dto.DataDto;
import pro.smartum.botapiai.dto.OriginalRequestDto;
import pro.smartum.botapiai.dto.ResultDto;

import java.sql.Timestamp;

@Data
public class IncomingMessageRq {

    private final String source;
    private final Timestamp timestamp;
    private final ResultDto result;
    private final OriginalRequestDto originalRequest;

}
