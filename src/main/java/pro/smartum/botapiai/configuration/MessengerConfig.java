package pro.smartum.botapiai.configuration;

public class MessengerConfig {

    public static final String KEY_USER_ID = "{KEY_USER_ID}";

    // Facebook
    private static String FB_BOT_ACCESS_TOKEN = "EAABtoDMZBuQUBALYEqXIUChlC832MEhkHjZCPmVEES2HMUovDgF3zxGSqyXDZBBn78iWCmPMaNBkEQrBhEgeJjVynxImxxmaluYiqXXaS4d57DNq5cZAcUwqaRQENrnKEfoC2Mh38zeZBuH2oO92DY6ZAVZAKkoXaYdy1bxiJ7fbAZDZD";
    public static final String FB_URL_REPLY = "https://graph.facebook.com/v2.10/me/messages?access_token=" + FB_BOT_ACCESS_TOKEN;
    public static final String FB_URL_GET_PROFILE = "https://graph.facebook.com/v2.10/{KEY_USER_ID}?access_token=" + FB_BOT_ACCESS_TOKEN;

    // Skype
    public static final String SKYPE_CLIENT_ID = "824af996-b071-4c6e-86a2-2a39bc86c624";
    public static final String SKYPE_CLIENT_SECRET = "AVgVQs1Sjca3eEGp4HpjJoo";
    public static final String SKYPE_BOT_ID = "28:" + SKYPE_CLIENT_ID;
    public static final String SKYPE_CONVERSATION_ID = "{SKYPE_CONVERSATION_ID}";
    public static final String SKYPE_URL_REPLY = "https://smba.trafficmanager.net/apis/v3/conversations/{SKYPE_CONVERSATION_ID}/activities";

    // Slack

    // Telegram
    private static String TG_BOT_ID = "421328021:AAEQlskNLS4gMzT7uitSk6Fj8dkcwC2D3gk";
    public static final String TG_FILE_ID = "{TG_FILE_ID}";
    public static final String TG_FILE_PATH = "{TG_FILE_PATH}";
    public static final String TG_CHAT_ID = "{TG_CHAT_ID}";
    public static final String TG_MESSAGE = "{TG_MESSAGE}";
    public static final String TG_URL_REPLY = "https://api.telegram.org/bot"
            + TG_BOT_ID + "/sendmessage?chat_id=" + TG_CHAT_ID + "&text=" + TG_MESSAGE;

    public static final String TG_URL_BASE = "https://api.telegram.org/bot" + TG_BOT_ID;
    public static final String TG_URL_GET_USER_PHOTOS = TG_URL_BASE + "/getUserProfilePhotos?user_id={KEY_USER_ID}";
    public static final String TG_URL_GET_USER_PHOTO = TG_URL_BASE + "/getFile?file_id={TG_FILE_ID}";
    public static final String TG_URL_PHOTO_PATH = "https://api.telegram.org/file/bot" + TG_BOT_ID + "/{TG_FILE_PATH}";

    public static final Integer TG_PHOTO_MAX_SIZE = 640;
}
