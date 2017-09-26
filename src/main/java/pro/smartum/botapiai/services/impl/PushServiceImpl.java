package pro.smartum.botapiai.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.db.tables.records.PushDeviceRecord;
import pro.smartum.botapiai.dto.PushDeviceRegistrationDto;
import pro.smartum.botapiai.dto.PushDeviceUnRegistrationDto;
import pro.smartum.botapiai.repositories.PushDeviceRepository;
import pro.smartum.botapiai.services.PushService;

@Service
@RequiredArgsConstructor
@Log4j
public class PushServiceImpl implements PushService {

    private final PushDeviceRepository pushDeviceRepository;

    @Override
    public void registerDevice(PushDeviceRegistrationDto dto) {
        pushDeviceRepository.deletePushDeviceByToken(dto.getToken());

        PushDeviceRecord pushDeviceRecord = pushDeviceRepository.newRecord();
        pushDeviceRecord.setToken(dto.getToken());
        pushDeviceRecord.setDeviceType(dto.getType());
        pushDeviceRepository.store(pushDeviceRecord);
        LOG.info("Pushes: register device " + dto.getToken());
    }

    @Override
    public void unregisterDevice(PushDeviceUnRegistrationDto dto) {
        pushDeviceRepository.deletePushDeviceByToken(dto.getToken());
        LOG.info("Pushes: unregister device " + dto.getToken());
    }
}
