package pro.smartum.botapiai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FromDto {

    @JsonProperty("last_name")
    private final String lastName;
    @JsonProperty("first_name")
    private final String firstName;
    private final String id;
}
