package pro.smartum.botapiai.configuration;

import org.springframework.beans.factory.annotation.Value;

public class MessengerConfig {

    // Telegram
    private static String TG_BOT_ID = "421328021:AAEQlskNLS4gMzT7uitSk6Fj8dkcwC2D3gk";
    public static final String TG_CHAT_ID = "{TG_CHAT_ID}";
    public static final String TG_MESSAGE = "{TG_MESSAGE}";
    public static final String TG_URL_REPLY = "https://api.telegram.org/bot"
            + TG_BOT_ID + "/sendmessage?chat_id=" + TG_CHAT_ID + "&text=" + TG_MESSAGE;

    // Facebook
    private static String FB_BOT_ACCESS_TOKEN = "EAAHwqkDONrcBACbZA6cgj3CUsuy4ZAKYraHXM5KZBxcbr509IB8Qg9CBWqs46FgXaVPbjKus6KZCdUd1TcpzfXo0xEKaxNRbEGeYlJuVrcsP8RjxloaIDnh0O8pWZAT5CqbuD6gk4kVEbecrZBUiIzmRjONLfWvbGDqvJprHRB9LDCn3cOnhKXpnNFynXkBiwZD";
    public static final String FB_URL_REPLY = "https://graph.facebook.com/v2.6/me/messages?access_token=" + FB_BOT_ACCESS_TOKEN;
}
