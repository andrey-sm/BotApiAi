package pro.smartum.botapiai.helpers.messenger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.dto.FromDto;
import pro.smartum.botapiai.dto.rq.IncomingMessageRq;
import pro.smartum.botapiai.dto.rq.ReplyRq;
import pro.smartum.botapiai.helpers.UserHelper;
import pro.smartum.botapiai.retrofit.RetrofitClient;
import pro.smartum.botapiai.retrofit.rs.TgPhotosRs;
import retrofit2.Call;

import java.io.IOException;

import static pro.smartum.botapiai.configuration.MessengerConfig.*;

@Service
public class TelegramHelper extends BaseMessengerHelper {

    @Value("${tg.bot.id}")
    private String tgBotId;

    public TelegramHelper(RetrofitClient retrofitClient) {
        super(retrofitClient, null);
    }

    @Override
    public boolean reply(ConversationRecord convRecord, ReplyRq replyRq) {
        String tgUrlReply = TG_URL_REPLY
                .replace(TG_BOT_ID, tgBotId)
                .replace(TG_CHAT_ID, convRecord.getTgChatId())
                .replace(TEXT, replyRq.getText());
        Call tgCall = retrofitClient.getTelegramController().reply(tgUrlReply);
        return executeCall(tgCall);
    }

    @Override
    public void build(IncomingMessageRq messageRq, ConversationRecord convRecord) {
        FromDto from = messageRq.getOriginalRequest().getData().getMessage().getFrom();
        if(from == null)
            return;

        convRecord.setTgChatId(from.getId())
                .setSenderName(UserHelper.buildFullName(from.getFirstName(), from.getLastName()));

        String tgUrlGetPhotos = TG_URL_GET_USER_PHOTOS.replace(TG_BOT_ID, tgBotId).replace(USER_ID, convRecord.getTgChatId());
        try {
            TgPhotosRs tgUserPhotos = retrofitClient.getTelegramController().getUserPhotos(tgUrlGetPhotos).execute().body();
            if (tgUserPhotos == null || tgUserPhotos.getResult().getTotalCount() == 0)
                return;

            String tgUserPhotoFileId = tgUserPhotos.getResult().getPhotos().get(0)
                    .stream()
                    .filter( it -> it.getHeight().intValue() == TG_PHOTO_MAX_SIZE)
                    .findFirst()
                    .get().getFileId();

            String tgUrlGetPhotoPath = TG_URL_GET_USER_PHOTO.replace(TG_BOT_ID, tgBotId).replace(TG_FILE_ID, tgUserPhotoFileId);
            TgPhotosRs tgPhotoPath = retrofitClient.getTelegramController().getUserPhotoPath(tgUrlGetPhotoPath).execute().body();

            if(tgPhotoPath != null && tgPhotoPath.getResult() != null) {
                String photoUrl = TG_URL_PHOTO_PATH.replace(TG_BOT_ID, tgBotId).replace(TG_FILE_PATH, tgPhotoPath.getResult().getFilePath());
                convRecord.setPhotoUrl(photoUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
