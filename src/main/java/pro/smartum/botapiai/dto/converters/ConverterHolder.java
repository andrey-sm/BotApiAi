package pro.smartum.botapiai.dto.converters;


import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.db.tables.records.MessageRecord;
import pro.smartum.botapiai.dto.ConversationDto;
import pro.smartum.botapiai.dto.MessageDto;

public enum ConverterHolder {

    INSTANCE;

    private final ConversationConverter conversationConverter;
    private final MessageConverter messageConverter;

    ConverterHolder() {
        conversationConverter = new ConversationConverter();
        messageConverter = new MessageConverter();
    }

    public ConversationDto convert(ConversationRecord record) { return conversationConverter.apply(record); }
    public MessageDto convert(MessageRecord record) { return messageConverter.apply(record); }
}
