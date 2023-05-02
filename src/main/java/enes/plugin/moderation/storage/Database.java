package enes.plugin.moderation.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final Connection connection;

    public Database(String filename) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:plugins/ModerationTools/database.db");
    }

    public void createTable() throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS reports (id INTEGER PRIMARY KEY AUTOINCREMENT, reporter TEXT, reported TEXT, reason TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        statement.execute("CREATE TABLE IF NOT EXISTS players (id INTEGER PRIMARY KEY AUTOINCREMENT, player_name TEXT, report_count TEXT NOT NULL DEFAULT 0, ban_count TEXT NOT NULL DEFAULT NaN, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        statement.close();
    }
}
