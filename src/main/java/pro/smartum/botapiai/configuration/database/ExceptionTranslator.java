package pro.smartum.botapiai.configuration.database;

import org.jooq.ExecuteContext;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;


public class ExceptionTranslator extends DefaultExecuteListener {

    private final SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator("PostgreSQL");

    @Override
    public void exception(ExecuteContext ctx) {
        if (ctx.sqlException() != null) {
            ctx.exception(translator.translate("JOOQ", ctx.sql(), ctx.sqlException()));
        }
    }
}
