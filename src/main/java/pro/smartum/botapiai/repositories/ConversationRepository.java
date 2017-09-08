package pro.smartum.botapiai.repositories;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import pro.smartum.botapiai.db.tables.Conversation;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;

import java.util.List;

import static pro.smartum.botapiai.db.tables.Conversation.CONVERSATION;

@Repository
public class ConversationRepository extends BaseRepository<ConversationRecord, Conversation> {

    ConversationRepository(DSLContext jooq) {
        super(jooq);
    }

    @Override
    protected Conversation table() {
        return CONVERSATION;
    }

    public List<ConversationRecord> getAll() {
        return jooq().select().from(CONVERSATION).fetchInto(CONVERSATION);
    }
}
