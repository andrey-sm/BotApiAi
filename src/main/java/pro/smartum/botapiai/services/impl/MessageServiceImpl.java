package pro.smartum.botapiai.services.impl;


import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.dto.rq.MessageRq;
import pro.smartum.botapiai.dto.rs.MessageRs;
import pro.smartum.botapiai.dto.rs.ProgramORs;
import pro.smartum.botapiai.services.MessageService;
import pro.smartum.botapiai.util.HttpURLConnectionUtils;

@Service
public class MessageServiceImpl implements MessageService {

    private static final String PROGRAM_O = "Program-O";
    private static final String SMARTUM_BOT = "SmartumBot";

    @Override
    public MessageRs replyToMessage(MessageRq messageRq) {
        //fetchProgramOAnswer()
        return new MessageRs("AK speech", "AK displayText");
    }

    public static String fetchProgramOAnswer(String message) {
        message = message.replaceAll(" ", "%20");
        try {
            String url = "http://api.program-o.com/v2/chatbot/?" +
                    "bot_id=6" +
                    "&say=" + message +
                    "&convo_id=x1" +
                    "&format=json";

            String programOResponseJson = HttpURLConnectionUtils.sendGet(url);
            ProgramORs apiBotResponse = new Gson().fromJson(programOResponseJson, ProgramORs.class);
            String botSay = apiBotResponse.getBotSay().replaceAll(PROGRAM_O, SMARTUM_BOT);
            return botSay;
        } catch (Exception e) {
            e.printStackTrace();
            return "O-Program error";
        }
    }
}
