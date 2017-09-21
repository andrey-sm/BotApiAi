package pro.smartum.botapiai.repositories;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import pro.smartum.botapiai.db.tables.Message;
import pro.smartum.botapiai.db.tables.records.MessageRecord;

import java.util.List;

import static pro.smartum.botapiai.db.tables.Message.MESSAGE;

@Repository
public class MessageRepository extends BaseRepository<MessageRecord, Message> {

    MessageRepository(DSLContext jooq) {
        super(jooq);
    }

    @Override
    protected Message table() {
        return MESSAGE;
    }

    public List<MessageRecord> getSorted(int number, int count, long conversationId) {
        return jooq().select()
                .from(MESSAGE)
                .where(MESSAGE.CONVERSATION_ID.eq(conversationId))
                .orderBy(MESSAGE.TIMESTAMP.desc())
                .limit(number, count)
                .fetchInto(MESSAGE);
    }

    public void markAsRead(Long messageId) {
        jooq().update(MESSAGE).set(MESSAGE.READ, true).where(MESSAGE.ID.eq(messageId)).execute();
    }


    public MessageRecord get(Long messageId) {
        return jooq().selectFrom(MESSAGE).where(MESSAGE.ID.eq(messageId)).fetchOneInto(MESSAGE);
    }
}
