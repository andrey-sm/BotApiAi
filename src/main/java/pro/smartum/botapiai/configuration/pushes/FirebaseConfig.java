package pro.smartum.botapiai.configuration.pushes;

import com.google.android.gcm.server.Endpoint;
import com.google.android.gcm.server.Sender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pro.smartum.botapiai.pushes.FcmManager;

import java.io.IOException;

@Component
public class FirebaseConfig {

    @Value("${fcm.key}")
    private String fcmKey;

    @Bean
    public FcmManager gcmManager() throws IOException {
        return new FcmManager(new Sender(fcmKey, Endpoint.FCM));
    }

}
