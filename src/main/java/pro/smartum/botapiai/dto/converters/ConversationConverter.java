package pro.smartum.botapiai.dto.converters;

import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.dto.ConversationDto;

public class ConversationConverter implements Converter<ConversationRecord, ConversationDto> {

    @Override
    public ConversationDto apply(ConversationRecord convRecord) {
        return ConversationDto.builder()
                .id(convRecord.getId())
                .type(convRecord.getType())
                .slackChannel(convRecord.getSlackChannelId())
                .slackUserId(convRecord.getSlackUserId())
                .fbSenderId(convRecord.getFbSenderId())
                .tgChatId(convRecord.getTgChatId())
                .tgSenderName(convRecord.getTgSenderName())
                .timestamp(convRecord.getTimestamp())
                .build();
    }
}
