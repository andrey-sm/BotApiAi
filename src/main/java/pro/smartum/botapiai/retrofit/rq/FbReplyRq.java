package pro.smartum.botapiai.retrofit.rq;

import lombok.Data;

@Data
public class FbReplyRq {

    private final Recipient recipient;
    private final Message message;

    @Data
    public static class Recipient {
        private final String id;
    }

    @Data
    public static class Message {
        private final String text;
    }
}
