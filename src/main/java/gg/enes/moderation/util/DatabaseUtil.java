package gg.enes.moderation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing database operations.
 */
public abstract class DatabaseUtil {
    private static String url;

    /**
     * Sets the URL of the database and creates the necessary tables.
     *
     * @param databaseURL The URL of the database
     * @throws SQLException if an error occurs while creating the tables
     */
    public static void setURL(String databaseURL) throws SQLException {
        if (url == null) {
            url = databaseURL;

            createTable();
        }
    }

    /**
     * Gets a connection to the database.
     *
     * @return The database connection
     * @throws SQLException if an error occurs while establishing the connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    /**
     * Creates the necessary tables in the database.
     *
     * @throws SQLException if an error occurs while creating the tables
     */
    private static void createTable() throws SQLException {
        Statement statement = getConnection().createStatement();

        String[] queries = {
                "CREATE TABLE IF NOT EXISTS players (" +
                        "    id              INTEGER PRIMARY KEY AUTOINCREMENT   NOT NULL," +
                        "    name            VARCHAR(32)                         NOT NULL," +
                        "    uuid            VARCHAR(36)                         NOT NULL," +
                        "    ip              VARCHAR(15)                         NOT NULL," +
                        "    muted           BOOLEAN                             NOT NULL," +
                        "    banned          BOOLEAN                             NOT NULL," +
                        "    last_connection TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                        "    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL" +
                        ")",
                "CREATE TABLE IF NOT EXISTS reports (" +
                        "    id            INTEGER PRIMARY KEY AUTOINCREMENT   NOT NULL," +
                        "    reporter_uuid VARCHAR(36)                         NOT NULL," +
                        "    reported_uuid VARCHAR(36)                         NOT NULL," +
                        "    reason        VARCHAR(36)                         NOT NULL," +
                        "    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL" +
                        ")",
                "CREATE TABLE IF NOT EXISTS bans (" +
                        "    id             INTEGER PRIMARY KEY AUTOINCREMENT   NOT NULL," +
                        "    player_uuid    VARCHAR(36)                         NOT NULL," +
                        "    moderator_uuid VARCHAR(36)                         NOT NULL," +
                        "    reason         VARCHAR(255)                        NOT NULL," +
                        "    time           INTEGER                             NOT NULL," +
                        "    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                        "    active         BOOLEAN                             NOT NULL" +
                        ")",
                "CREATE TABLE IF NOT EXISTS mutes (" +
                        "    id             INTEGER PRIMARY KEY AUTOINCREMENT   NOT NULL," +
                        "    player_uuid    VARCHAR(36)                         NOT NULL," +
                        "    moderator_uuid VARCHAR(36)                         NOT NULL," +
                        "    reason         VARCHAR(255)                        NOT NULL," +
                        "    time           INTEGER                             NOT NULL," +
                        "    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                        "    active         BOOLEAN                             NOT NULL" +
                        ")"
        };

        for (String query : queries) {
            statement.execute(query);
        }

        statement.close();
    }
}
