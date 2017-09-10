package pro.smartum.botapiai.dto.rs;

import lombok.Data;
import pro.smartum.botapiai.dto.MessageDto;

import java.util.List;

@Data
public class MessagesRs {

    private final List<MessageDto> messages;
    private final Integer messagesNumber;
}
