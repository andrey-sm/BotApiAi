package pro.smartum.botapiai.services;


import pro.smartum.botapiai.dto.rs.ConversationsRs;
import pro.smartum.botapiai.dto.rs.MessagesRs;

public interface ConversationService {

    ConversationsRs getConversations();
    MessagesRs getConversationHistory(long conversationId);
}
