package pro.smartum.botapiai.helpers.messenger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.dto.AddressDto;
import pro.smartum.botapiai.dto.GrantType;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.exceptions.GetSkypeTokenException;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.retrofit.RetrofitClient;
import pro.smartum.botapiai.retrofit.rq.SkypeReplyRq;
import pro.smartum.botapiai.retrofit.rs.SkypeTokenRs;
import retrofit2.Call;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static pro.smartum.botapiai.configuration.MessengerConfig.SKYPE_CONVERSATION_ID;
import static pro.smartum.botapiai.configuration.MessengerConfig.SKYPE_URL_REPLY;
import static pro.smartum.botapiai.dto.ConversationType.SKYPE;
import static pro.smartum.botapiai.retrofit.UrlManager.SKYPE_ACCESS_TOKEN_SCOPE;

@Service
public class SkypeHelper extends BaseMessengerHelper {

    @Value("${skype.client.id}")
    private String skypeClientId;
    @Value("${skype.client.secret}")
    private String skypeClientSecret;

    public SkypeHelper(RetrofitClient retrofitClient, ConversationRepository conversationRepository) {
        super(retrofitClient, conversationRepository);
    }

    public boolean reply(ConversationRecord convRecord, ReplyRq replyRq) {
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
                    .fetchAccessToken(skypeClientId, skypeClientSecret, GrantType.client_credentials, SKYPE_ACCESS_TOKEN_SCOPE)
                    .execute().body();

            String newSkypeToken = skypeTokenRs.getTokenType() + " " + skypeTokenRs.getAccessToken();

            convRecord
                    .setSkypeAccessToken(newSkypeToken)
                    .setSkypeAccessTokenExpDate(fetchNewSkypeTokenExpDate(skypeTokenRs));
            conversationRepository.store(convRecord);
            return newSkypeToken;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GetSkypeTokenException();
        }
    }

    private Timestamp fetchNewSkypeTokenExpDate(SkypeTokenRs skypeTokenRs) {
        return new Timestamp(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(skypeTokenRs.getExpiresIn()));
    }

    @Override
    public void build(IncomingMessageRq messageRq, ConversationRecord convRecord) {
        if(messageRq.getOriginalRequest().getData() == null || messageRq.getOriginalRequest().getData().getAddress() == null)
            return;

        AddressDto address = messageRq.getOriginalRequest().getData().getAddress();
        convRecord.setSkypeConversationId(address.getConversation().getId())
                .setSkypeSenderId(address.getUser().getId())
                .setSenderName(address.getUser().getName());

        convRecord.setType(SKYPE.name());
    }
}
