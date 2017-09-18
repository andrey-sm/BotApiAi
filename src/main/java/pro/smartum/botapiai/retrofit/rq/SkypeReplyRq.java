package pro.smartum.botapiai.retrofit.rq;

import lombok.Data;

@Data
public class SkypeReplyRq {

    private final String type = "message";
    private final String text;
}
