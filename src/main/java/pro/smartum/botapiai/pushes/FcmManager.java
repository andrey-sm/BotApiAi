package pro.smartum.botapiai.pushes;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pro.smartum.botapiai.db.tables.records.PushDeviceRecord;
import pro.smartum.botapiai.repositories.PushDeviceRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Log4j
public class FcmManager {

    public static final String SENDER = "SENDER";
    public static final String MESSAGE = "MESSAGE";
    public static final String CONVERSATION_ID = "CONVERSATION_ID";

    private static final String UNAVAILABLE_ERROR = "Unavailable";
    private static final int MULTICAST_SIZE = 1000;

    private final Sender sender;
    @Autowired
    private PushDeviceRepository pushDeviceRepository;

    public void sendNotification(final List<String> tokens, final Map<String, String> fields) {
        final Message message = buildMessage(fields);
        sendNotification(tokens, message);
    }

    private void sendNotification(List<String> tokens, Message message) {
        int total = tokens.size();
        List<String> partialDevices = new ArrayList<>(total);
        int counter = 0;
        for (String token : tokens) {
            counter++;
            partialDevices.add(token);
            int partialSize = partialDevices.size();
            if (partialSize == MULTICAST_SIZE || counter == total) {
                asyncSend(message, partialDevices);
                partialDevices.clear();
            }
        }
    }

    private Message buildMessage(final Map<String, String> fields) {
        final Message.Builder messageBuilder = new Message.Builder();
        if (MapUtils.isNotEmpty(fields))
            fields.forEach(messageBuilder::addData);
        return messageBuilder.build();
    }

    private void asyncSend(final Message message, List<String> partialDevices) {
        final List<String> devices = new ArrayList<>(partialDevices);
        MulticastResult multicastResult;
        try {
            multicastResult = sender.send(message, devices, 5);
        } catch (IOException e) {
            LOG.error("Android push sending problem", e);
            return;
        }

        List<Result> results = multicastResult.getResults();
        for (int i = 0; i < devices.size(); i++)
            checkResult(devices.get(i), results.get(i));
    }

    private void checkResult(String regId, Result result) {
        if (result.getErrorCodeName() != null && !result.getErrorCodeName().equalsIgnoreCase(UNAVAILABLE_ERROR)) {
            pushDeviceRepository.deletePushDeviceByToken(regId);
        } else if (StringUtils.isNoneBlank(result.getCanonicalRegistrationId())) {
            final PushDeviceRecord canonicalPushDevice = pushDeviceRepository.getPushDeviceByToken(result.getCanonicalRegistrationId());
            if (canonicalPushDevice != null) {
                pushDeviceRepository.deletePushDeviceByToken(regId);
            } else {
                final PushDeviceRecord pushDevice = pushDeviceRepository.getPushDeviceByToken(regId);
                pushDevice.setToken(result.getCanonicalRegistrationId());
                pushDeviceRepository.store(pushDevice);
            }
        }
    }
}
