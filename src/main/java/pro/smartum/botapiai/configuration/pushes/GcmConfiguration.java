package pro.smartum.botapiai.configuration.pushes;

import com.google.android.gcm.server.Endpoint;
import com.google.android.gcm.server.Sender;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pro.smartum.botapiai.pushes.FcmManager;

import java.io.IOException;

@Component
public class GcmConfiguration {

    @Bean
    public FcmManager gcmManager() throws IOException {
        return new FcmManager(new Sender("AAAA8b8KKME:APA91bFr-sqYRXl-hU75-yV_bFCusc993d3DagjqKJLbKeqhGR_2l1vcVAQ-o3dvOFAWgUcY4m9zdgNqG_Y6fOoyuArlLH_zDwbAVzYV8gsSv-SV4ywh4HDKiD9qN4j6259NpIVrDnfz", Endpoint.FCM));
    }
}
