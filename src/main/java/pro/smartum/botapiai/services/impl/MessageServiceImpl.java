package pro.smartum.botapiai.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.db.tables.records.MessageRecord;
import pro.smartum.botapiai.dto.AddressDto;
import pro.smartum.botapiai.dto.ConversationType;
import pro.smartum.botapiai.dto.ParametersDto;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rs.OutgoingMessageRs;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.repositories.MessageRepository;
import pro.smartum.botapiai.services.MessageService;

import java.sql.Timestamp;
import java.util.Date;

import static pro.smartum.botapiai.dto.ConversationType.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String PROGRAM_O = "Program-O";
    private static final String SMARTUM_BOT = "SmartumBot";
    private static final String WILL_REPLY_SOON = "Thank you for your message. We will reply soon.";

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

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

        if(!messageRq.getResult().getContexts().isEmpty()) {
            ParametersDto parameters = messageRq.getResult().getContexts().get(0).getParameters();
            cr.setFbSenderId(parameters.getFbSenderId());
            cr.setTgChatId(parameters.getTgChatId());
            cr.setSlackUserId(parameters.getSlackUserId());
            cr.setSlackChannelId(parameters.getSlackChannel());

            cr.setType(fetchConversationType(parameters).name());
        }

        if(messageRq.getOriginalRequest().getData() != null && messageRq.getOriginalRequest().getData().getAddress() != null) {
            // Skype
            AddressDto address = messageRq.getOriginalRequest().getData().getAddress();
            cr.setSkypeConversationId(address.getConversation().getId())
                    .setSkypeSenderId(address.getUser().getId())
                    .setSkypeSenderName(address.getUser().getName());

            cr.setType(SKYPE.name());
        }

        return cr;
    }

    private ConversationType fetchConversationType(ParametersDto parameters) {
        if (!isEmpty(parameters.getFbSenderId()))
            return FACEBOOK;
        if (!isEmpty(parameters.getTgChatId()))
            return TELEGRAM;
        if (!isEmpty(parameters.getSlackChannel()) && !isEmpty(parameters.getSlackUserId()))
            return SLACK;
        return SKYPE;
    }

    private boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }



    // https://monosnap.com/file/dQ7OC0j3rQzOrzdWUWPWikeYG3qHkZ#
//        String answer = !StringUtils.isEmpty(question)
//                ? fetchProgramOAnswer(question)
//                : "Wrong or empty question";
//
//        System.out.println("Size = " + conversationRepository.getAllSorted().size());

//    public static String fetchProgramOAnswer(String message) {
//        message = message.replaceAll(" ", "%20");
//        try {
//            String url = "http://api.program-o.com/v2/chatbot/?" +
//                    "bot_id=6" +
//                    "&say=" + message +
//                    "&convo_id=x1" +
//                    "&format=json";
//
//            String programOResponseJson = HttpURLConnectionUtils.sendGet(url);
//            ProgramORs apiBotResponse = new Gson().fromJson(programOResponseJson, ProgramORs.class);
//            String botSay = apiBotResponse.getBotSay().replaceAll(PROGRAM_O, SMARTUM_BOT);
//            return botSay;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "O-Program error";
//        }
//    }
}
