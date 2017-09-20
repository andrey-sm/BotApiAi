package pro.smartum.botapiai.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.db.tables.records.MessageRecord;
import pro.smartum.botapiai.dto.ConversationDto;
import pro.smartum.botapiai.dto.ConversationType;
import pro.smartum.botapiai.dto.GrantType;
import pro.smartum.botapiai.dto.MessageDto;
import pro.smartum.botapiai.dto.converters.ConverterHolder;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.dto.rs.ConversationsRs;
import pro.smartum.botapiai.dto.rs.MessagesRs;
import pro.smartum.botapiai.exceptions.ConversationNotExistsException;
import pro.smartum.botapiai.exceptions.GetSkypeTokenException;
import pro.smartum.botapiai.exceptions.NotImplementedYetException;
import pro.smartum.botapiai.exceptions.NotValidMessengerException;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.repositories.MessageRepository;
import pro.smartum.botapiai.retrofit.RetrofitClient;
import pro.smartum.botapiai.retrofit.rq.FbReplyRq;
import pro.smartum.botapiai.retrofit.rq.SkypeReplyRq;
import pro.smartum.botapiai.retrofit.rs.SkypeTokenRs;
import pro.smartum.botapiai.services.ConversationService;
import retrofit2.Call;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pro.smartum.botapiai.configuration.MessengerConfig.*;
import static pro.smartum.botapiai.retrofit.UrlManager.SKYPE_ACCESS_TOKEN_SCOPE;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    @Autowired
    private final RetrofitClient retrofitClient;

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
        switch (convType) {
            case FACEBOOK:
                return replyToFacebook(convRecord, replyRq);
            case SKYPE:
                return replyToSkype(convRecord, replyRq);
            case SLACK:
                return replyToSlack(convRecord, replyRq);
            case TELEGRAM:
                return replyToTelegram(convRecord, replyRq);
            default:
                throw new NotImplementedYetException();
        }
    }

    ////////////// REPLY TO FACEBOOK ///////////////////////////////////////////////////////////////////////////////////
    private boolean replyToFacebook(ConversationRecord convRecord, ReplyRq replyRq) {
        FbReplyRq fbReplyRq = new FbReplyRq(
                new FbReplyRq.Recipient(convRecord.getFbSenderId()),
                new FbReplyRq.Message(replyRq.getText()));
        Call fbCall = retrofitClient.getFacebookController().reply(FB_URL_REPLY, fbReplyRq);
        return executeCall(fbCall);
    }

    ////////////// REPLY TO SLACK //////////////////////////////////////////////////////////////////////////////////////
    private boolean replyToSkype(ConversationRecord convRecord, ReplyRq replyRq) {
        String skypeToken = fetchSkypeAccessToken(convRecord);

        SkypeReplyRq skypeReplyRq = new SkypeReplyRq(replyRq.getText());
        String skypeReplyUrl = SKYPE_URL_REPLY.replace(SKYPE_CONVERSATION_ID, convRecord.getSkypeConversationId());

        Call fbCall = retrofitClient.getSkypeController().reply(skypeReplyUrl, skypeToken, skypeReplyRq);
        return executeCall(fbCall);
    }

    private String fetchSkypeAccessToken(ConversationRecord convRecord) {
        Timestamp slackAccessTokenExpDate = convRecord.getSkypeAccessTokenExpDate();

        if (slackAccessTokenExpDate == null)
            return fetchNewSkypeAccessToken(convRecord);

        if (slackAccessTokenExpDate.getTime() > new Date().getTime())
            return convRecord.getSkypeAccessToken();
        return fetchNewSkypeAccessToken(convRecord);
    }

    private String fetchNewSkypeAccessToken(ConversationRecord convRecord) {
        try {
            SkypeTokenRs skypeTokenRs = retrofitClient.getSkypeController()
                    .fetchAccessToken(SKYPE_CLIENT_ID, SKYPE_CLIENT_SECRET,
                            GrantType.client_credentials, SKYPE_ACCESS_TOKEN_SCOPE)
                    .execute().body();

            String newSkypeToken = skypeTokenRs.getTokenType() + " " + skypeTokenRs.getAccessToken();
            Timestamp newSkypeTokenExpDate = new Timestamp(
                    new Date().getTime() + TimeUnit.SECONDS.toMillis(skypeTokenRs.getExpiresIn()));

            convRecord
                    .setSkypeAccessToken(newSkypeToken)
                    .setSkypeAccessTokenExpDate(newSkypeTokenExpDate);
            conversationRepository.store(convRecord);
            return newSkypeToken;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GetSkypeTokenException();
        }
    }

    ////////////// REPLY TO SLACK //////////////////////////////////////////////////////////////////////////////////////
    private boolean replyToSlack(ConversationRecord convRecord, ReplyRq replyRq) {
        String slackUrlReply = SLACK_URL_REPLY
                .replace(SLACK_CHANNEL, convRecord.getSlackChannelId())
                .replace(SLACK_TEXT, replyRq.getText());
        Call slackCall = retrofitClient.getSlackController().reply(slackUrlReply);
        return executeCall(slackCall);
    }

    ////////////// REPLY TO TELEGRAM ///////////////////////////////////////////////////////////////////////////////////
    private boolean replyToTelegram(ConversationRecord convRecord, ReplyRq replyRq) {
        String tgUrlReply = TG_URL_REPLY
                .replace(TG_CHAT_ID, convRecord.getTgChatId())
                .replace(TG_MESSAGE, replyRq.getText());
        Call tgCall = retrofitClient.getTelegramController().reply(tgUrlReply);
        return executeCall(tgCall);
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
