package pro.smartum.botapiai.dto;

import lombok.Data;

@Data
public class DataDto {

    private final AddressDto address;                   // Skype
    private final EventDto event;                       // Slack
    private final OriginalRqMessageDto message;         // Telegram
}
