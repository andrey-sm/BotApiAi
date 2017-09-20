package pro.smartum.botapiai.retrofit.rs;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SlackUserInfoRs {

    private final SlackUserDto user;

    @Data
    public class SlackUserDto {
        private final SlackProfileDto profile;
    }

    @Data
    public class SlackProfileDto {
        @SerializedName("real_name")
        private final String realName;
        @SerializedName("image_192")
        private final String imageUrl;
    }
}
