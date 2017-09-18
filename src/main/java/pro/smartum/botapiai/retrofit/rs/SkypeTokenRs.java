package pro.smartum.botapiai.retrofit.rs;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SkypeTokenRs {

    @SerializedName("token_type")
    private final String tokenType;
    @SerializedName("expires_in")
    private final Integer expiresIn;
    @SerializedName("access_token")
    private final String accessToken;
}
