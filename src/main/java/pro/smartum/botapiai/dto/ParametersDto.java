package pro.smartum.botapiai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParametersDto {

    @JsonProperty("slack_user_id")
    private final String slackUserId;
    @JsonProperty("slack_channel")
    private final String slackChannel;

    @JsonProperty("facebook_sender_id")
    private final String fbSenderId;

    @JsonProperty("telegram_chat_id")
    private final String tgChatId;
}
