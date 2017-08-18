package pro.smartum.botapiai.services;


import pro.smartum.botapiai.dto.rq.MessageRq;
import pro.smartum.botapiai.dto.rs.MessageRs;

public interface MessageService {

    MessageRs replyToMessage(MessageRq messageRq);
}
