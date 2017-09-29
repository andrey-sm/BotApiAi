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
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.exceptions.NotValidMessengerException;
import pro.smartum.botapiai.helpers.messenger.MessengerHolder;
import pro.smartum.botapiai.pushes.FcmManager;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.repositories.MessageRepository;
import pro.smartum.botapiai.repositories.PushDeviceRepository;
import pro.smartum.botapiai.services.MessageService;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static pro.smartum.botapiai.configuration.MessengerConfig.REPLY_WITH_DEFAULT_TEXT_DELAY;
import static pro.smartum.botapiai.dto.ConversationType.*;
import static pro.smartum.botapiai.pushes.FcmManager.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String WILL_REPLY_SOON = "Thank you for the request, we will answer you as soon as possible.";

    private final FcmManager fcmManager;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final PushDeviceRepository pushDeviceRepository;
    private final MessengerHolder messengerHolder;

    @Override
    public void handleMessage(IncomingMessageRq messageRq) {
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

        sendPushNotification(convRecord, messageRecord);

        startCheckManualReplyTimer(messageRecord.getId());
    }

    private void sendPushNotification(ConversationRecord convRecord, MessageRecord messageRecord) {
        List<String> tokens = pushDeviceRepository.getAll().stream().map(it -> it.getToken()).collect(Collectors.toList());

        Map<String, String> fields = new HashMap<>();
        fields.put(MESSAGE, messageRecord.getText());
        fields.put(SENDER, convRecord.getSenderName());
        fields.put(CONVERSATION_ID, convRecord.getId().toString());

        fcmManager.sendNotification(tokens, fields);
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

    private void startCheckManualReplyTimer(final long messageId) {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        checkIfMessageRead(messageId);
                    }
                },
                REPLY_WITH_DEFAULT_TEXT_DELAY
        );
    }

    private void checkIfMessageRead(long messageId) {
        MessageRecord messageRecord = messageRepository.get(messageId);
        if (messageRecord.getRead())
            return;

        final ConversationRecord convRecord = conversationRepository.get(messageRecord.getConversationId());
        if (!replyToMessenger(convRecord, new ReplyRq(WILL_REPLY_SOON)))
            throw new NotValidMessengerException();
    }

    private boolean replyToMessenger(ConversationRecord convRecord, ReplyRq replyRq) {
        ConversationType convType = ConversationType.valueOf(convRecord.getType());
        return messengerHolder.fetchMessengerHelper(convType).reply(convRecord, replyRq);
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
