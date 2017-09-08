package pro.smartum.botapiai.services.impl;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.smartum.botapiai.dto.ResultDto;
import pro.smartum.botapiai.dto.rq.MessageRq;
import pro.smartum.botapiai.dto.rs.MessageRs;
import pro.smartum.botapiai.dto.rs.ProgramORs;
import pro.smartum.botapiai.services.MessageService;
import pro.smartum.botapiai.util.HttpURLConnectionUtils;
import pro.smartum.botapiai.repositories.ConversationRepository;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String PROGRAM_O = "Program-O";
    private static final String SMARTUM_BOT = "SmartumBot";

    private final ConversationRepository conversationRepository;

    @Override
    public MessageRs replyToMessage(MessageRq messageRq) {
        String question = null;
        ResultDto result = messageRq.getResult();
        if(result != null)
            question = result.getResolvedQuery();

        String answer = !StringUtils.isEmpty(question)
                ? fetchProgramOAnswer(question)
                : "Wrong or empty question";

        System.out.println("Size = " + conversationRepository.getAll().size());

        return new MessageRs(answer, "DisplayText: " + answer);
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
