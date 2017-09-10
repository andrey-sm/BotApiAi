package pro.smartum.botapiai.repositories;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import pro.smartum.botapiai.db.tables.Conversation;
import pro.smartum.botapiai.db.tables.Message;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;
import pro.smartum.botapiai.db.tables.records.MessageRecord;

import java.util.List;

import static pro.smartum.botapiai.db.tables.Conversation.CONVERSATION;
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
}
