package pro.smartum.botapiai.services;


import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;

public interface MessageService {

    void handleMessage(IncomingMessageRq messageRq);

    MessageDto markAsRead(Long messageId);
}
