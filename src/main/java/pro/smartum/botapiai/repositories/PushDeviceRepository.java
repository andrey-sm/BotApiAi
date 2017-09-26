package pro.smartum.botapiai.repositories;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import pro.smartum.botapiai.db.tables.PushDevice;
import pro.smartum.botapiai.db.tables.records.PushDeviceRecord;

import java.util.List;

import static pro.smartum.botapiai.db.tables.PushDevice.PUSH_DEVICE;

@Repository
public class PushDeviceRepository extends BaseRepository<PushDeviceRecord, PushDevice> {

    PushDeviceRepository(DSLContext jooq) {
        super(jooq);
    }

    @Override
    protected PushDevice table() {
        return PUSH_DEVICE;
    }

    public List<PushDeviceRecord> getAll() {
        return jooq().select().from(PUSH_DEVICE).fetchInto(PUSH_DEVICE);
    }

    public PushDeviceRecord getPushDeviceByToken(String token) {
        return jooq().selectFrom(PUSH_DEVICE).where(PUSH_DEVICE.TOKEN.eq(token)).fetchOneInto(PUSH_DEVICE);
    }

    public void deletePushDeviceByToken(String token) {
        jooq().deleteFrom(PUSH_DEVICE).where(PUSH_DEVICE.TOKEN.eq(token)).execute();
    }
}
