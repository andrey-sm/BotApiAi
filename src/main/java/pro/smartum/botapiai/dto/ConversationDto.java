package pro.smartum.botapiai.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ConversationDto {

    private final Long id;
    private final String type;
    private final String slackUserId;
    private final String slackChannel;
    private final String fbSenderId;
    //private final String fbRecipientId;
    private final String tgChatId;
    private final String tgSenderName;
    private final Timestamp timestamp;
}