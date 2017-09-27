package pro.smartum.botapiai.services;


import pro.smartum.botapiai.dto.ConversationDto;
import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.dto.rs.ConversationsRs;
import pro.smartum.botapiai.dto.rs.MessagesRs;

public interface ConversationService {

    ConversationsRs getConversations();
    ConversationDto getConversation(long conversationId);
    MessagesRs getConversationHistory(int number, int count, long conversationId);
    MessageDto replyToConversation(long conversationId, ReplyRq replyRq);
}
