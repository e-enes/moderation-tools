package gg.enes.moderation.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.enes.moderation.core.database.config.DatabaseConfig;
import gg.enes.moderation.core.database.config.DatabaseType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class DatabaseManager {
    /**
     * The HikariCP data source instance.
     */
    private static HikariDataSource dataSource;

    /**
     * The type of the database currently configured.
     */
    private static DatabaseType dbType;

    private DatabaseManager() {
    }

    /**
     * Initializes the database connection pool with the provided configuration.
     *
     * @param config The database configuration.
     */
    public static synchronized void initialize(@NonNull final DatabaseConfig config) {
        if (dataSource == null) {
            HikariConfig hikariConfig = new HikariConfig();

            dbType = config.getDbType();

            if (dbType == DatabaseType.SQLITE) {
                hikariConfig.setDriverClassName("org.sqlite.JDBC");
                hikariConfig.setJdbcUrl("jdbc:sqlite:" + config.getFileName());
            } else if (dbType == DatabaseType.MYSQL) {
                hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
                hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", config.getHost(), config.getPort(), config.getDatabaseName()));
                hikariConfig.setUsername(config.getUsername());
                hikariConfig.setPassword(config.getPassword());
                hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
                hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
                hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
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