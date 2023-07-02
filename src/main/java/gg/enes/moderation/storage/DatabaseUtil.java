package gg.enes.moderation.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseUtil {
    private static String URL;

    public static void setFilename(String fileName) throws SQLException {
        if (URL == null) {
            URL = fileName;
            createTable();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + URL);
    }

    private static void createTable() throws SQLException {
        Statement statement = getConnection().createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS reports (id INTEGER PRIMARY KEY AUTOINCREMENT, reporter TEXT, reported TEXT, reason TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        statement.execute("CREATE TABLE IF NOT EXISTS players (id INTEGER PRIMARY KEY AUTOINCREMENT, player_name TEXT, report_count TEXT NOT NULL DEFAULT 0, ban_count TEXT NOT NULL DEFAULT NaN, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        statement.close();
    }
}
