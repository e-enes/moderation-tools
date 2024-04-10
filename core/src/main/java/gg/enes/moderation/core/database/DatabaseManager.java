package gg.enes.moderation.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.enes.moderation.core.database.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static HikariDataSource dataSource;

    private DatabaseManager() {}

    /**
     * Initializes a single instance of the data source if it hasn't been initialized already.
     *
     * @param config The database configuration settings to be used by the HikariCP data source.
     */
    public static synchronized void initialize(DatabaseConfig config) {
        if (dataSource == null) {
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

            dataSource = new HikariDataSource(hikariConfig);
        }
    }

    /**
     * Obtains a connection from the connection pool.
     *
     * @return A database connection.
     * @throws SQLException If a database access error occurs.
     */
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DatabaseManager is not initialized.");
        }

        return dataSource.getConnection();
    }

    /**
     * Returns the single instance of DatabaseManager.
     *
     * @return The single instance of DatabaseManager.
     */
    public static DatabaseManager getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * InstanceHolder - Private static nested class that holds the single instance of DatabaseManager.
     */
    private static final class InstanceHolder {
        private static final DatabaseManager instance = new DatabaseManager();
    }
}