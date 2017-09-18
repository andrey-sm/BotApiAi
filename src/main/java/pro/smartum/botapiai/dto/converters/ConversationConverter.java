package pro.smartum.botapiai.dto.converters;

import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.dto.ConversationDto;

public class ConversationConverter implements Converter<ConversationRecord, ConversationDto> {

    @Override
    public ConversationDto apply(ConversationRecord convRecord) {
        return ConversationDto.builder()
                .id(convRecord.getId())
                .type(convRecord.getType())
                .fbSenderId(convRecord.getFbSenderId())
                .skypeConversationId(convRecord.getSkypeConversationId())
                .skypeSenderId(convRecord.getSkypeSenderId())
                .slackChannel(convRecord.getSlackChannelId())
                .slackUserId(convRecord.getSlackUserId())
                .tgChatId(convRecord.getTgChatId())
                .senderName(convRecord.getSenderName())
                .photoUrl(convRecord.getPhotoUrl())
                .timestamp(convRecord.getTimestamp())
                .build();
    }
}
