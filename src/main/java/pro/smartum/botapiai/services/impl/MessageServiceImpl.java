package pro.smartum.botapiai.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.db.tables.records.MessageRecord;
import pro.smartum.botapiai.dto.*;
import pro.smartum.botapiai.dto.converters.ConverterHolder;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rs.OutgoingMessageRs;
import pro.smartum.botapiai.helpers.UserHelper;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.repositories.MessageRepository;
import pro.smartum.botapiai.retrofit.RetrofitClient;
import pro.smartum.botapiai.retrofit.rs.FbUserInfoRs;
import pro.smartum.botapiai.retrofit.rs.SlackUserInfoRs;
import pro.smartum.botapiai.retrofit.rs.TgPhotosRs;
import pro.smartum.botapiai.services.MessageService;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import static pro.smartum.botapiai.configuration.MessengerConfig.*;
import static pro.smartum.botapiai.dto.ConversationType.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RetrofitClient retrofitClient;

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

        ConversationType conversationType = fetchConversationType(messageRq);
        cr.setType(conversationType.name());

        switch (conversationType) {
            case FACEBOOK:  buildFacebookConv(messageRq, cr);   break;
            case SKYPE:     buildSkypeConv(messageRq, cr);      break;
            case SLACK:     buildSlackConv(messageRq, cr);      break;
            case TELEGRAM:  buildTelegramConv(messageRq, cr);   break;
        }

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

    ////////////// BUILD FACEBOOK CONVERSATION  ////////////////////////////////////////////////////////////////////////
    private void buildFacebookConv(IncomingMessageRq messageRq, ConversationRecord convRecord) {
        if(messageRq.getResult().getContexts().isEmpty())
            return;

        ParametersDto parameters = messageRq.getResult().getContexts().get(0).getParameters();
        convRecord.setFbSenderId(parameters.getFbSenderId());

        String fbGetProfileUrl = FB_URL_GET_PROFILE.replace(KEY_USER_ID, parameters.getFbSenderId());
        try {
            FbUserInfoRs profile = retrofitClient.getFacebookController().getUserProfile(fbGetProfileUrl).execute().body();
            convRecord.setSenderName(UserHelper.buildFullName(profile.getFirstName(), profile.getLastName()));
            convRecord.setPhotoUrl(profile.getProfilePic());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////// BUILD SKYPE CONVERSATION  ///////////////////////////////////////////////////////////////////////////
    private void buildSkypeConv(IncomingMessageRq messageRq, ConversationRecord cr) {
        if(messageRq.getOriginalRequest().getData() == null || messageRq.getOriginalRequest().getData().getAddress() == null)
            return;

        AddressDto address = messageRq.getOriginalRequest().getData().getAddress();
        cr.setSkypeConversationId(address.getConversation().getId())
                .setSkypeSenderId(address.getUser().getId())
                .setSenderName(address.getUser().getName());

        cr.setType(SKYPE.name());
    }

    ////////////// BUILD SLACK CONVERSATION  ///////////////////////////////////////////////////////////////////////////
    private void buildSlackConv(IncomingMessageRq messageRq, ConversationRecord convRecord) {
        EventDto slackEvent = messageRq.getOriginalRequest().getData().getEvent();

        convRecord
                .setSlackUserId(slackEvent.getUser())
                .setSlackChannelId(slackEvent.getChannel());

        try {
            SlackUserInfoRs slackUserInfoRs = retrofitClient.getSlackController().getUserProfile(SLACK_BOT_TOKEN, convRecord.getSlackUserId()).execute().body();
            if(slackUserInfoRs != null && slackUserInfoRs.getUser() != null && slackUserInfoRs.getUser().getProfile() != null) {
                SlackUserInfoRs.SlackProfileDto profile = slackUserInfoRs.getUser().getProfile();
                convRecord.setSenderName(profile.getRealName());
                convRecord.setPhotoUrl(profile.getImageUrl());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////// BUILD TELEGRAM CONVERSATION  ////////////////////////////////////////////////////////////////////////
    private void buildTelegramConv(IncomingMessageRq messageRq, ConversationRecord convRecord) {
        FromDto from = messageRq.getOriginalRequest().getData().getMessage().getFrom();
        if(from == null)
            return;

        convRecord
                .setTgChatId(from.getId())
                .setSenderName(UserHelper.buildFullName(from.getFirstName(), from.getLastName()));

        String tgUrlGetPhotos = TG_URL_GET_USER_PHOTOS.replace(KEY_USER_ID, convRecord.getTgChatId());

        try {
            TgPhotosRs tgUserPhotos = retrofitClient.getTelegramController().getUserPhotos(tgUrlGetPhotos).execute().body();
            if (tgUserPhotos == null || tgUserPhotos.getResult().getTotalCount() == 0)
                return;

            String tgUserPhotoFileId = tgUserPhotos.getResult().getPhotos().get(0)
                    .stream()
                    .filter( it -> it.getHeight() == TG_PHOTO_MAX_SIZE)
                    .findFirst()
                    .get().getFileId();

            String tgUrlGetPhotoPath = TG_URL_GET_USER_PHOTO.replace(TG_FILE_ID, tgUserPhotoFileId);
            TgPhotosRs tgPhotoPath = retrofitClient.getTelegramController().getUserPhotoPath(tgUrlGetPhotoPath).execute().body();

            if(tgPhotoPath != null && tgPhotoPath.getResult() != null) {
                String photoUrl = TG_URL_PHOTO_PATH.replace(TG_FILE_PATH, tgPhotoPath.getResult().getFilePath());
                convRecord.setPhotoUrl(photoUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    @Override
    public MessageDto markAsRead(Long messageId) {
        messageRepository.markAsRead(messageId);
        return ConverterHolder.INSTANCE.convert(messageRepository.get(messageId));
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
