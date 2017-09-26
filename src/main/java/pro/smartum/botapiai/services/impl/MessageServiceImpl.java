package pro.smartum.botapiai.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.db.tables.records.MessageRecord;
import pro.smartum.botapiai.dto.ConversationType;
import pro.smartum.botapiai.dto.DataDto;
import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.dto.ParametersDto;
import pro.smartum.botapiai.dto.converters.ConverterHolder;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rs.OutgoingMessageRs;
import pro.smartum.botapiai.helpers.messenger.*;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.repositories.MessageRepository;
import pro.smartum.botapiai.services.MessageService;

import java.sql.Timestamp;
import java.util.Date;

import static pro.smartum.botapiai.dto.ConversationType.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String WILL_REPLY_SOON = "Thank you for your message. We will messenger soon.";

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessengerHolder messengerHolder;

    @Override
    public OutgoingMessageRs handleMessage(IncomingMessageRq messageRq) {
        ConversationRecord convRecord = buildConversationRecord(messageRq);
        convRecord = conversationRepository.getOrCreate(convRecord);

        Timestamp timestamp = new Timestamp(new Date().getTime());
        convRecord.setTimestamp(timestamp);
        convRecord.update();

        String question = messageRq.getResult().getResolvedQuery();
        MessageRecord messageRecord = messageRepository.newRecord();
        messageRecord.setText(question);
        messageRecord.setConversationId(convRecord.getId());
        messageRecord.setTimestamp(timestamp);

        messageRepository.store(messageRecord);

        return new OutgoingMessageRs(WILL_REPLY_SOON, WILL_REPLY_SOON);
    }

    private ConversationRecord buildConversationRecord(IncomingMessageRq messageRq) {
        ConversationRecord cr = conversationRepository.newRecord();

        ConversationType conversationType = fetchConversationType(messageRq);
        cr.setType(conversationType.name());
        messengerHolder.fetchMessengerHelper(conversationType).build(messageRq, cr);

        return cr;
    }

    private ConversationType fetchConversationType(IncomingMessageRq messageRq) {
        DataDto data = messageRq.getOriginalRequest().getData();
        if(data.getAddress() != null)
            return SKYPE;

        if(data.getEvent() != null)
            return SLACK;

        ParametersDto parameters = messageRq.getResult().getContexts().get(0).getParameters();
        if (!isEmpty(parameters.getFbSenderId()))
            return FACEBOOK;

        //if (!isEmpty(parameters.getTgChatId()))
            return TELEGRAM;
    }

    private boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    @Override
    public MessageDto markAsRead(Long messageId) {
        messageRepository.markAsRead(messageId);
        return ConverterHolder.INSTANCE.convert(messageRepository.get(messageId));
    }
}
