package pro.smartum.botapiai.dto.rs;

import lombok.Data;

import java.util.List;

@Data
public class OutgoingMessageRs {

    private final String speech;
    private final String displayText;
    private final Object data;
    private final List<Object> contextOut;
    private final String source;

    public OutgoingMessageRs(String speech, String displayText) {
        this.speech = speech;
        this.displayText = displayText;
        data = null;
        contextOut = null;
        source = null;
    }
}
