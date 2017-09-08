package pro.smartum.botapiai.configuration.database;

import lombok.RequiredArgsConstructor;
import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.jooq.tools.JooqLogger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;

@RequiredArgsConstructor
public class SpringTransactionProvider implements TransactionProvider {

	private static final JooqLogger log = JooqLogger.getLogger(SpringTransactionProvider.class);

    private final DataSourceTransactionManager txMgr;

	@Override
	public void begin(TransactionContext ctx) {
		log.info("Begin transaction");
		ctx.transaction(new SpringTransaction(txMgr.getTransaction(new DefaultTransactionDefinition(PROPAGATION_NESTED))));
	}

	@Override
	public void commit(TransactionContext ctx) {
		log.info("commit transaction");
		txMgr.commit(((SpringTransaction) ctx.transaction()).tx);
	}

	@Override
	public void rollback(TransactionContext ctx) {
		log.info("rollback transaction");
		txMgr.rollback(((SpringTransaction) ctx.transaction()).tx);
	}
}
