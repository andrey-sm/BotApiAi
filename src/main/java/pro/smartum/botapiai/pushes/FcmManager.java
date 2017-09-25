package pro.smartum.botapiai.pushes;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class FcmManager {

    private final Sender sender;

    public void send() {
        final Message.Builder messageBuilder = new Message.Builder();
        final Message message = messageBuilder.addData("MESSAGE", "Work!!!").build();
        try {
            final Result send = sender.send(message, "APA91bE9hOFtfvU_p6MX4xFv0CJGodY24ByGwedi4MMyjU_p-OwkJQx6cLN0BCx6qEGFxEpzyo7cwHhzTSNSSnvqBTyLOmXWpH2BrrjO8OjXO74yQhNNpJY", 5);
            int a = 5;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
