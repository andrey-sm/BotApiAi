package pro.smartum.botapiai.dto.rs;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ProgramORs {

    @SerializedName("convo_id")
    private final String conversationId;
    @SerializedName("usersay")
    private final String userSay;
    @SerializedName("botsay")
    private final String botSay;
}
