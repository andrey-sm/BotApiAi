package pro.smartum.botapiai.services.impl;

import lombok.RequiredArgsConstructor;
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
import pro.smartum.botapiai.exceptions.NotImplementedYetException;
import pro.smartum.botapiai.exceptions.NotValidMessengerException;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.repositories.MessageRepository;
import pro.smartum.botapiai.retrofit.RetrofitClient;
import pro.smartum.botapiai.retrofit.rq.FbReplyRq;
import pro.smartum.botapiai.services.ConversationService;
import retrofit2.Call;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static pro.smartum.botapiai.configuration.MessengerConfig.*;

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

    @Override
    public MessageDto replyToConversation(long conversationId, ReplyRq replyRq) {
        final ConversationRecord convRecord = conversationRepository.get(conversationId);
        if (convRecord == null)
            throw new ConversationNotExistsException();

        if(!replyToMessenger(convRecord, replyRq))
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
        switch (convType) {
            case TELEGRAM:  return replyToTelegram(convRecord, replyRq);
            case FACEBOOK:  return replyToFacebook(convRecord, replyRq);
            default: throw new NotImplementedYetException();
        }
    }

    private boolean replyToTelegram(ConversationRecord convRecord, ReplyRq replyRq) {
        String tgUrlReply = TG_URL_REPLY
                .replace(TG_CHAT_ID, convRecord.getTgChatId())
                .replace(TG_MESSAGE, replyRq.getText());
        Call tgCall = RetrofitClient.getInstance().getTelegramController().reply(tgUrlReply);
        return executeCall(tgCall);
    }

    private boolean replyToFacebook(ConversationRecord convRecord, ReplyRq replyRq) {
        FbReplyRq fbReplyRq = new FbReplyRq(
                new FbReplyRq.Recipient(convRecord.getFbSenderId()),
                new FbReplyRq.Message(replyRq.getText()));
        Call fbCall = RetrofitClient.getInstance().getFacebookController().reply(FB_URL_REPLY, fbReplyRq);
        return executeCall(fbCall);
    }

    private boolean executeCall(Call call) {
        try {
            call.execute().body();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
