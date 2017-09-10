package pro.smartum.botapiai.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.db.tables.records.MessageRecord;
import pro.smartum.botapiai.dto.ConversationDto;
import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.dto.converters.ConverterHolder;
import pro.smartum.botapiai.dto.rs.ConversationsRs;
import pro.smartum.botapiai.dto.rs.MessagesRs;
import pro.smartum.botapiai.exceptions.ConversationNotExistsException;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.repositories.MessageRepository;
import pro.smartum.botapiai.services.ConversationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    @Override
    public ConversationsRs getConversations() {
        final List<ConversationRecord> conversations = conversationRepository.getAllSorted();
        final List<ConversationDto> conversationDtos =
                conversations.stream().map(c -> ConverterHolder.INSTANCE.convert(c)).collect(Collectors.toList());

        return new ConversationsRs(conversationDtos, conversationDtos.size());
    }

    @Override
    public MessagesRs getConversationHistory(long conversationId) {
        final ConversationRecord convRecord = conversationRepository.get(conversationId);
        if (convRecord == null)
            throw new ConversationNotExistsException();

        final List<MessageRecord> messages = messageRepository.getSorted(conversationId);
        final List<MessageDto> messageDtos =
                messages.stream().map(m -> ConverterHolder.INSTANCE.convert(m)).collect(Collectors.toList());

        return new MessagesRs(messageDtos, messageDtos.size());
    }
}
