package gg.enes.moderation.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.database.config.DatabaseConfig;
import gg.enes.moderation.core.database.config.DatabaseType;
import gg.enes.moderation.core.entity.Action;
import gg.enes.moderation.core.entity.Report;
import gg.enes.moderation.core.entity.Sanction;
import gg.enes.moderation.core.entity.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


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
    public static synchronized void initialize(@NonNull final DatabaseConfig config) throws SQLException {
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

            Set<Class<?>> entityRegistry = new HashSet<>();
            entityRegistry.add(User.class);
            entityRegistry.add(Action.class);
            entityRegistry.add(Report.class);
            entityRegistry.add(Sanction.class);
            entityRegistry.add(gg.enes.moderation.core.entity.Connection.class);

            for (Class<?> entity : entityRegistry) {
                String query = TableCreator.initialize(entity);
                ModerationLogger.debug(query);
            }
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