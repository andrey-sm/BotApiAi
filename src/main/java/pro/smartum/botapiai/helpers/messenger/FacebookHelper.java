package pro.smartum.botapiai.helpers.messenger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.dto.ParametersDto;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.helpers.UserHelper;
import pro.smartum.botapiai.retrofit.RetrofitClient;
import pro.smartum.botapiai.retrofit.rq.FbReplyRq;
import pro.smartum.botapiai.retrofit.rs.FbUserInfoRs;
import retrofit2.Call;

import java.io.IOException;

import static pro.smartum.botapiai.configuration.MessengerConfig.*;

@Service
public class FacebookHelper extends BaseMessengerHelper {

    @Value("${fb.bot.access.token}")
    private String fbAccessToken;

    public FacebookHelper(RetrofitClient retrofitClient) {
        super(retrofitClient, null);
    }

    @Override
    public boolean reply(ConversationRecord convRecord, ReplyRq replyRq) {
        FbReplyRq fbReplyRq = new FbReplyRq(
                new FbReplyRq.Recipient(convRecord.getFbSenderId()),
                new FbReplyRq.Message(replyRq.getText()));

        String fbUrlReply = FB_URL_REPLY.replace(TOKEN, fbAccessToken);

        Call fbCall = retrofitClient.getFacebookController().reply(fbUrlReply, fbReplyRq);
        return executeCall(fbCall);
    }

    @Override
    public void build(IncomingMessageRq messageRq, ConversationRecord convRecord) {
        if (messageRq.getResult().getContexts().isEmpty())
            return;

        ParametersDto parameters = messageRq.getResult().getContexts().get(0).getParameters();
        convRecord.setFbSenderId(parameters.getFbSenderId());

        String fbGetProfileUrl = FB_URL_GET_PROFILE
                .replace(USER_ID, parameters.getFbSenderId())
                .replace(TOKEN, fbAccessToken);
        try {
            FbUserInfoRs profile = retrofitClient.getFacebookController().getUserProfile(fbGetProfileUrl).execute().body();
            convRecord.setSenderName(UserHelper.buildFullName(profile.getFirstName(), profile.getLastName()));
            convRecord.setPhotoUrl(profile.getProfilePic());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
