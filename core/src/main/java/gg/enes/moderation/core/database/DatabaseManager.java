package gg.enes.moderation.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.enes.moderation.core.database.config.DatabaseConfig;
import gg.enes.moderation.core.database.config.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static HikariDataSource dataSource;
    private static DatabaseType dbType;

    private DatabaseManager() {}

    /**
     * Initializes a single instance of the data source if it hasn't been initialized already.
     *
     * @param config The database configuration settings to be used by the HikariCP data source.
     */
    public static synchronized void initialize(DatabaseConfig config) {
        if (dataSource == null) {
            HikariConfig hikariConfig = new HikariConfig();

            dbType = config.getDbType();

            if (dbType == DatabaseType.Sqlite) {
                hikariConfig.setDriverClassName("org.sqlite.JDBC");
                hikariConfig.setJdbcUrl("jdbc:sqlite:" + config.getFileName());
                hikariConfig.setMaximumPoolSize(10);
            } else if (dbType == DatabaseType.Mysql) {
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
     * Retrieves the type of the database currently configured.
     *
     * @return The current database type as a DatabaseType enum.
     */
    public static DatabaseType getDatabaseType() {
        return dbType;
    }

    /**
     * Obtains a connection from the connection pool.
     *
     * @return A database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DatabaseManager is not initialized.");
        }

        return dataSource.getConnection();
    }

    /**
     * Closes the data source.
     */
    public static void close() {
        if (dataSource == null) {
            throw new IllegalStateException("DatabaseManager is not initialized.");
        }

        dataSource.close();
    }
}