package pro.smartum.botapiai.services;

import pro.smartum.botapiai.dto.PushDeviceRegistrationDto;
import pro.smartum.botapiai.dto.PushDeviceUnRegistrationDto;

public interface PushService {

    void registerDevice(PushDeviceRegistrationDto dto);

    void unregisterDevice(PushDeviceUnRegistrationDto dto);
}
