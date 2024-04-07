package gg.enes.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.enes.core.database.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private final HikariDataSource dataSource;

    public DatabaseManager(DatabaseConfig config) {
        HikariConfig hikariConfig = new HikariConfig();

        if ("sqlite".equalsIgnoreCase(config.getDbType())) {
            hikariConfig.setDriverClassName("org.sqlite.JDBC");
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + config.getFileName());
            hikariConfig.setMaximumPoolSize(10);
        } else if ("mysql".equalsIgnoreCase(config.getDbType())) {
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", config.getHost(), config.getPort(), config.getDatabaseName()));
            hikariConfig.setUsername(config.getUsername());
            hikariConfig.setPassword(config.getPassword());
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            hikariConfig.setMaximumPoolSize(20);
        } else {
            throw new IllegalArgumentException("Unsupported database type: " + config.getDbType());
        }

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Obtains a connection from the connection pool.
     *
     * @return A database connection.
     * @throws SQLException If a database access error occurs.
     */
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
