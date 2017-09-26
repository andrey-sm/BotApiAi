package pro.smartum.botapiai.dto;

import lombok.Data;
import pro.smartum.botapiai.db.enums.PushDeviceType;

@Data
public class PushDeviceRegistrationDto {

    private final PushDeviceType type;
    private final String token;
}