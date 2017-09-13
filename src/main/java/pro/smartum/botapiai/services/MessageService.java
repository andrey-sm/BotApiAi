package pro.smartum.botapiai.services;


import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rs.OutgoingMessageRs;

public interface MessageService {

    OutgoingMessageRs handleMessage(IncomingMessageRq messageRq);
}
