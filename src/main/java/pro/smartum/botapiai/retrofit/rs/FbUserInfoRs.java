package pro.smartum.botapiai.retrofit.rs;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FbUserInfoRs {

    @SerializedName("first_name")
    private final String firstName;
    @SerializedName("last_name")
    private final String lastName;
    @SerializedName("profile_pic")
    private final String profilePic;
}
