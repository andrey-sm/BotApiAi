package pro.smartum.botapiai.helpers.messenger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.dto.EventDto;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.retrofit.RetrofitClient;
import pro.smartum.botapiai.retrofit.rs.SlackUserInfoRs;
import retrofit2.Call;

import java.io.IOException;

import static pro.smartum.botapiai.configuration.MessengerConfig.*;

@Service
public class SlackHelper extends BaseMessengerHelper {

    @Value("${slack.bot.token}")
    private String slackBotToken;

    public SlackHelper(RetrofitClient retrofitClient) {
        super(retrofitClient, null);
    }

    @Override
    public boolean reply(ConversationRecord convRecord, ReplyRq replyRq) {
        String slackUrlReply = SLACK_URL_REPLY
                .replace(TOKEN, slackBotToken)
                .replace(SLACK_CHANNEL, convRecord.getSlackChannelId())
                .replace(TEXT, replyRq.getText());
        Call slackCall = retrofitClient.getSlackController().reply(slackUrlReply);
        return executeCall(slackCall);
    }

    @Override
    public void build(IncomingMessageRq messageRq, ConversationRecord convRecord) {
        EventDto slackEvent = messageRq.getOriginalRequest().getData().getEvent();

        convRecord
                .setSlackUserId(slackEvent.getUser())
                .setSlackChannelId(slackEvent.getChannel());

        try {
            SlackUserInfoRs slackUserInfoRs = retrofitClient.getSlackController()
                    .getUserProfile(slackBotToken, convRecord.getSlackUserId()).execute().body();

            if(slackUserInfoRs != null && slackUserInfoRs.getUser() != null && slackUserInfoRs.getUser().getProfile() != null) {
                SlackUserInfoRs.SlackProfileDto profile = slackUserInfoRs.getUser().getProfile();
                convRecord.setSenderName(profile.getRealName());
                convRecord.setPhotoUrl(profile.getImageUrl());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
