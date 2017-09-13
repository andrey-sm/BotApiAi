package pro.smartum.botapiai.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class MessageDto {

    private final Long id;
    private final String text;
    private final Long conversationId;
    private final Timestamp timestamp;
    private final Boolean botReply;
}
