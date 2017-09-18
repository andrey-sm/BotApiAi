package pro.smartum.botapiai.repositories;

import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.SortOrder;
import org.springframework.stereotype.Repository;
import pro.smartum.botapiai.db.tables.Conversation;
import pro.smartum.botapiai.db.tables.records.ConversationRecord;

import java.util.List;

import static org.jooq.SortOrder.DESC;
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

    public List<ConversationRecord> getAllSorted() {
        return jooq().select().from(CONVERSATION).orderBy(CONVERSATION.TIMESTAMP.desc()).fetchInto(CONVERSATION);
    }

    public ConversationRecord getOrCreate(ConversationRecord cr) {
        SelectConditionStep<ConversationRecord> where = jooq().selectFrom(CONVERSATION).where(CONVERSATION.TYPE.eq(cr.getType()));

        if(cr.getFbSenderId() != null)
            where.and(CONVERSATION.FB_SENDER_ID.eq(cr.getFbSenderId()));
        //.and(CONVERSATION.FB_RECIPIENT_ID.eq(cr.get()))
        if(cr.getTgChatId() != null)
            where.and(CONVERSATION.TG_CHAT_ID.eq(cr.getTgChatId()));
        //.and(CONVERSATION.TG_SENDER_NAME.eq(cr.getTgSenderName()))
        if(cr.getSlackChannelId() != null)
            where.and(CONVERSATION.SLACK_CHANNEL_ID.eq(cr.getSlackChannelId()));
        if(cr.getSlackUserId() != null)
            where.and(CONVERSATION.SLACK_USER_ID.eq(cr.getSlackUserId()));

        // Skype
        if(cr.getSkypeConversationId() != null)
            where.and(CONVERSATION.SKYPE_CONVERSATION_ID.eq(cr.getSkypeConversationId()));
        if(cr.getSkypeSenderId() != null)
            where.and(CONVERSATION.SKYPE_SENDER_ID.eq(cr.getSkypeSenderId()));
        if(cr.getSkypeSenderName() != null)
            where.and(CONVERSATION.SKYPE_SENDER_NAME.eq(cr.getSkypeSenderName()));


        ConversationRecord conversationRecord = where.fetchOneInto(CONVERSATION);
        if(conversationRecord != null)
            return conversationRecord;

        store(cr);
        return cr;
    }

    public ConversationRecord get(Long conversationId) {
        return jooq().selectFrom(CONVERSATION).where(CONVERSATION.ID.eq(conversationId)).fetchOneInto(CONVERSATION);
    }
}
