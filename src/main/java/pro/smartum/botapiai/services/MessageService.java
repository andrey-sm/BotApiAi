package pro.smartum.botapiai.services;


import pro.smartum.botapiai.dto.rq.MessageRq;

public interface MessageService {

    void handleMessage(MessageRq messageRq);
}
