package pro.smartum.botapiai.configuration;

import org.springframework.beans.factory.annotation.Value;

public class MessengerConfig {

    @Value("${telegram.bot.id}")
    private static String telegramBotId;

    // Telegram
    public static final String TG_CHAT_ID = "{TG_CHAT_ID}";
    public static final String TG_MESSAGE = "{TG_MESSAGE}";
    public static final String TG_URL_REPLY = "https://api.telegram.org/bot"
            + telegramBotId + "/sendmessage?chat_id=" + TG_CHAT_ID + "&text=" + TG_MESSAGE;
}
