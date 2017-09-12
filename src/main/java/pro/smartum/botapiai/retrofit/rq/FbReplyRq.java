package pro.smartum.botapiai.retrofit.rq;

import lombok.Data;

@Data
public class FbReplyRq {

    private final Recipient recipient;

    @Data
    class Recipient {
        private final String id;
    }
}
