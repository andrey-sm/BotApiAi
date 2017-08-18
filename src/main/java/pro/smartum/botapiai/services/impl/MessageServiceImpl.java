package pro.smartum.botapiai.services.impl;


import org.springframework.stereotype.Service;
import pro.smartum.botapiai.dto.rq.MessageRq;
import pro.smartum.botapiai.dto.rs.MessageRs;
import pro.smartum.botapiai.services.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public MessageRs replyToMessage(MessageRq messageRq) {
        return new MessageRs("AK speech", "AK displayText");
    }
}
