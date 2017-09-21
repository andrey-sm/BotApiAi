package pro.smartum.botapiai.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ConversationDto {

    private final Long id;
    private final String type;

    // Facebook
    private final String fbSenderId;

    // Sлype
    private final String skypeConversationId;
    private final String skypeSenderId;

    // Slack
    private final String slackUserId;
    private final String slackChannel;

    // Telegram
    private final String tgChatId;

    private final String senderName;
    private final String photoUrl;

    private final Timestamp timestamp;

    private Integer unreadNumber;
}
