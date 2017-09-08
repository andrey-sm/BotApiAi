package pro.smartum.botapiai.configuration.database;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Value("${datasource.driver}")
    private String driver;
    @Value("${datasource.url}")
    private String url;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;
    @Value("${connection.pool.minimum.idle}")
    private Integer connectionPoolMinimumIdle;
    @Value("${connection.pool.max.size}")
    private Integer connectionMaxSize;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        if (StringUtils.isNoneBlank(password)) {
            hikariConfig.setPassword(password);
        }

        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("HikariCP");
        hikariConfig.setMaximumPoolSize(connectionMaxSize);
        hikariConfig.setMinimumIdle(connectionPoolMinimumIdle);
        hikariConfig.setIdleTimeout(30 * 1000L);
        hikariConfig.setValidationTimeout(1000);


        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");

        return new HikariDataSource(hikariConfig);
    }


}
