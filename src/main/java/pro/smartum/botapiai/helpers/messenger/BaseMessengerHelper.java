package pro.smartum.botapiai.helpers.messenger;

import lombok.RequiredArgsConstructor;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.repositories.ConversationRepository;
import pro.smartum.botapiai.retrofit.RetrofitClient;
import retrofit2.Call;

import java.io.IOException;

@RequiredArgsConstructor
abstract public class BaseMessengerHelper {
    
    protected final RetrofitClient retrofitClient;
    protected final ConversationRepository conversationRepository;

    abstract public boolean reply(ConversationRecord convRecord, ReplyRq replyRq);

    abstract public void build(IncomingMessageRq messageRq, ConversationRecord convRecord);

    protected boolean executeCall(Call call) {
        try {
            call.execute().body();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
