package pro.smartum.botapiai.helpers.messenger;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.dto.ConversationType;
import pro.smartum.botapiai.exceptions.NotImplementedYetException;

@Service
@RequiredArgsConstructor
public class MessengerHolder {

    private final FacebookHelper facebookHelper;
    private final SkypeHelper skypeHelper;
    private final SlackHelper slackHelper;
    private final TelegramHelper telegramHelper;

    public BaseMessengerHelper fetchMessengerHelper(ConversationType convType) {
        switch (convType) {
            case FACEBOOK:  return facebookHelper;
            case SKYPE:     return skypeHelper;
            case SLACK:     return slackHelper;
            case TELEGRAM:  return telegramHelper;
            default:        throw new NotImplementedYetException();
        }
    }
}
