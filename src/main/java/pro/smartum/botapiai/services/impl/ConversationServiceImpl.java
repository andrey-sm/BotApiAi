package pro.smartum.botapiai.services.impl;

import lombok.RequiredArgsConstructor;
import org.jooq.Record2;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.db.tables.records.MessageRecord;
import pro.smartum.botapiai.dto.ConversationDto;
import pro.smartum.botapiai.dto.ConversationType;
import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.dto.converters.ConverterHolder;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.dto.rs.ConversationsRs;
import pro.smartum.botapiai.dto.rs.MessagesRs;
import pro.smartum.botapiai.exceptions.ConversationNotExistsException;
import pro.smartum.botapiai.exceptions.NotValidMessengerException;
import pro.smartum.botapiai.helpers.messenger.MessengerHolder;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.repositories.MessageRepository;
import pro.smartum.botapiai.services.ConversationService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessengerHolder messengerHolder;

    @Override
    public ConversationsRs getConversations() {
        final List<ConversationRecord> conversations = conversationRepository.getAllSorted();
        final List<ConversationDto> conversationDtos =
                conversations.stream().map(c -> ConverterHolder.INSTANCE.convert(c)).collect(Collectors.toList());

        Map<Long, Integer> unreadCountersMap = conversationRepository.getUnreadCounters()
                .stream().collect(Collectors.toMap(Record2::value1, Record2::value2));

        conversationDtos.stream().forEach(c -> c.setUnreadNumber(unreadCountersMap.get(c.getId())));

        return new ConversationsRs(conversationDtos, conversationDtos.size());
    }

    @Override
    public ConversationDto getConversation(long conversationId) {
        final ConversationRecord convRecord = conversationRepository.get(conversationId);
        if (convRecord == null)
            throw new ConversationNotExistsException();

        return ConverterHolder.INSTANCE.convert(convRecord);
    }

    @Override
    public MessagesRs getConversationHistory(int number, int count, long conversationId) {
        final ConversationRecord convRecord = conversationRepository.get(conversationId);
        if (convRecord == null)
            throw new ConversationNotExistsException();

        final List<MessageRecord> messages = messageRepository.getSorted(number, count, conversationId);
        final List<MessageDto> messageDtos =
                messages.stream().map(m -> ConverterHolder.INSTANCE.convert(m)).collect(Collectors.toList());

        return new MessagesRs(messageDtos, messageDtos.size());
    }

    @Override
    public MessageDto replyToConversation(long conversationId, ReplyRq replyRq) {
        final ConversationRecord convRecord = conversationRepository.get(conversationId);
        if (convRecord == null)
            throw new ConversationNotExistsException();

        if (!replyToMessenger(convRecord, replyRq))
            throw new NotValidMessengerException();

        MessageRecord messageRecord = messageRepository.newRecord();
        messageRecord.setText(replyRq.getText());
        messageRecord.setTimestamp(new Timestamp(new Date().getTime()));
        messageRecord.setConversationId(convRecord.getId());
        messageRecord.setBotreply(true);
        messageRepository.store(messageRecord);

        return ConverterHolder.INSTANCE.convert(messageRecord);
    }

    private boolean replyToMessenger(ConversationRecord convRecord, ReplyRq replyRq) {
        ConversationType convType = ConversationType.valueOf(convRecord.getType());
        return messengerHolder.fetchMessengerHelper(convType).reply(convRecord, replyRq);
    }
}
