package pro.smartum.botapiai.configuration;

public class MessengerConfig {

    public static final String USER_ID = "{USER_ID}";
    public static final String TOKEN = "{TOKEN}";
    public static final String TEXT = "{TEXT}";

    // Facebook
    public static final String FB_URL_REPLY = "https://graph.facebook.com/v2.10/me/messages?access_token={TOKEN}";
    public static final String FB_URL_GET_PROFILE = "https://graph.facebook.com/v2.10/{USER_ID}?access_token={TOKEN}";

    // Skype
    public static final String SKYPE_CONVERSATION_ID = "{SKYPE_CONVERSATION_ID}";
    public static final String SKYPE_URL_REPLY = "https://smba.trafficmanager.net/apis/v3/conversations/"
            + SKYPE_CONVERSATION_ID + "/activities";

    // Slack
    public static final String SLACK_CHANNEL = "{SLACK_CHANNEL}";
    public static final String SLACK_URL_REPLY = "https://slack.com/api/chat.postMessage?token=" + TOKEN
            + "&channel=" + SLACK_CHANNEL + "&text=" + TEXT;

    // Telegram
    public static final String TG_BOT_ID = "{TG_BOT_ID}";
    public static final String TG_FILE_ID = "{TG_FILE_ID}";
    public static final String TG_FILE_PATH = "{TG_FILE_PATH}";
    public static final String TG_CHAT_ID = "{TG_CHAT_ID}";
    public static final String TG_URL_REPLY = "https://api.telegram.org/bot"
            + TG_BOT_ID + "/sendmessage?chat_id=" + TG_CHAT_ID + "&text=" + TEXT;

    public static final String TG_URL_BASE = "https://api.telegram.org/bot" + TG_BOT_ID;
    public static final String TG_URL_GET_USER_PHOTOS = TG_URL_BASE + "/getUserProfilePhotos?user_id=" + USER_ID;
    public static final String TG_URL_GET_USER_PHOTO = TG_URL_BASE + "/getFile?file_id=" + TG_FILE_ID;
    public static final String TG_URL_PHOTO_PATH = "https://api.telegram.org/file/bot" + TG_BOT_ID + "/" + TG_FILE_PATH;

    public static final Integer TG_PHOTO_MAX_SIZE = 640;

    public static final Integer REPLY_WITH_DEFAULT_TEXT_DELAY = 30000;  // 30 sec.
}
