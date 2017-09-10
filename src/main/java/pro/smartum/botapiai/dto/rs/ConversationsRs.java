package pro.smartum.botapiai.dto.rs;

import lombok.Data;
import pro.smartum.botapiai.dto.ConversationDto;

import java.util.List;

@Data
public class ConversationsRs {

    private final List<ConversationDto> conversations;
    private final Integer conversationsNumber;
}
