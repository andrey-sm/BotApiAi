package pro.smartum.botapiai.dto.converters;

import pro.smartum.botapiai.db.tables.records.MessageRecord;
import pro.smartum.botapiai.dto.MessageDto;

public class MessageConverter implements Converter<MessageRecord, MessageDto> {

    @Override
    public MessageDto apply(MessageRecord messageRecord) {
        return MessageDto.builder()
                .id(messageRecord.getId())
                .text(messageRecord.getText())
                .conversationId(messageRecord.getConversationId())
                .timestamp(messageRecord.getTimestamp())
                .build();
    }
}
