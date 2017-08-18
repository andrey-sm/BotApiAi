package pro.smartum.botapiai.dto.rs;

import lombok.Data;

import java.util.List;
import java.util.StringJoiner;

@Data
public class MessageRs {

    private final String speech;
    private final String displayText;
    private final Object data;
    private final List<Object> contextOut;
    private final String source;

    public MessageRs(String speech, String displayText) {
        this.speech = speech;
        this.displayText = displayText;
        data = null;
        contextOut = null;
        source = null;
    }
}
