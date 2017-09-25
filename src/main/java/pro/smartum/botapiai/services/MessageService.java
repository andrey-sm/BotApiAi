package pro.smartum.botapiai.services;


import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rs.OutgoingMessageRs;

import java.io.IOException;

public interface MessageService {

    OutgoingMessageRs handleMessage(IncomingMessageRq messageRq);

    MessageDto markAsRead(Long messageId);
    
    void sendPush() throws IOException;
}
