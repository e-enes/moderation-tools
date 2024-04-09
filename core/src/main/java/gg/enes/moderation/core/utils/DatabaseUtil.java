package gg.enes.moderation.core.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    /**
     * Safely closes a SQL Connection without throwing an exception.
     *
     * @param conn The connection to close.
     */
    public static void closeQuietly(@NonNull Connection conn) {
        try {
            conn.close();
        } catch (SQLException ignored) {}
    }

    /**
     * Safely closes a SQL Statement without throwing an exception.
     *
     * @param stmt The statement to close.
     */
    public static void closeQuietly(@NonNull Statement stmt) {
        try {
            stmt.close();
        } catch (SQLException ignored) {}
    }

    /**
     * Safely closes a SQL ResultSet without throwing an exception.
     *
     * @param rs The ResultSet to close.
     */
    public static void closeQuietly(@NonNull ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException ignored) {}
    }
}
