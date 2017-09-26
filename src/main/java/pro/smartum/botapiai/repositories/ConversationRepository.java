package pro.smartum.botapiai.repositories;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.SelectConditionStep;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import pro.smartum.botapiai.db.tables.Conversation;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;

import java.util.List;

import static pro.smartum.botapiai.db.tables.Conversation.CONVERSATION;
import static pro.smartum.botapiai.db.tables.Message.MESSAGE;

@Repository
public class ConversationRepository extends BaseRepository<ConversationRecord, Conversation> {

    ConversationRepository(DSLContext jooq) {
        super(jooq);
    }

    @Override
    protected Conversation table() {
        return CONVERSATION;
    }

    public List<ConversationRecord> getAllSorted() {
        return jooq().select().from(CONVERSATION).orderBy(CONVERSATION.TIMESTAMP.desc()).fetchInto(CONVERSATION);
    }

    public List<Record2<Long, Integer>> getUnreadCounters() {
        return jooq()
                .select(CONVERSATION.ID, DSL.count())
                .from(MESSAGE)
                .join(CONVERSATION).onKey()
                .where(MESSAGE.BOTREPLY.eq(false).and(MESSAGE.READ.eq(false)))
                .groupBy(CONVERSATION.ID)
                .orderBy(CONVERSATION.TIMESTAMP.desc()).fetch();
    }

    public ConversationRecord getOrCreate(ConversationRecord cr) {
        SelectConditionStep<ConversationRecord> where = jooq().selectFrom(CONVERSATION).where(CONVERSATION.TYPE.eq(cr.getType()));

        // Facebook
        andIfNotNull(where, CONVERSATION.FB_SENDER_ID, cr.getFbSenderId());

        // Skype
        andIfNotNull(where, CONVERSATION.SKYPE_CONVERSATION_ID, cr.getSkypeConversationId());
        andIfNotNull(where, CONVERSATION.SKYPE_SENDER_ID, cr.getSkypeSenderId());

        // Slack
        andIfNotNull(where, CONVERSATION.SLACK_CHANNEL_ID, cr.getSlackChannelId());
        andIfNotNull(where, CONVERSATION.SLACK_USER_ID, cr.getSlackUserId());

        // Telegram
        andIfNotNull(where, CONVERSATION.TG_CHAT_ID, cr.getTgChatId());

        ConversationRecord conversationRecord = where.fetchOneInto(CONVERSATION);
        if(conversationRecord != null) {
            cr.setId(conversationRecord.getId());
            cr.update();
        } else store(cr);

        return cr;
    }

    private void andIfNotNull(SelectConditionStep<ConversationRecord> where,  TableField<ConversationRecord, String> field, String value) {
        if(value != null)
            where.and(field.eq(value));
    }

    public ConversationRecord get(Long conversationId) {
        return jooq().selectFrom(CONVERSATION).where(CONVERSATION.ID.eq(conversationId)).fetchOneInto(CONVERSATION);
    }
}
