package pro.smartum.botapiai.retrofit.rs;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class TgPhotosRs {

    private final TgResultDto result;

    @Data
    public class TgResultDto {

        @SerializedName("total_count")
        private final Integer totalCount;
        private final List<List<TgPhotoDto>> photos;

        @SerializedName("file_path")
        private final String filePath;
    }

    @Data
    public class TgPhotoDto {

        @SerializedName("file_id")
        private final String fileId;
        @SerializedName("file_size")
        private final Integer fileSize;
        private final Integer width;
        private final Integer height;
    }
}
