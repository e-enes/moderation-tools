package enes.plugin.moderation.utils;

import enes.plugin.moderation.storage.Database;

import java.sql.*;

public class Reports {
    private static Reports instance;
    private final Players players;
    private final Database database;

    public Reports(Database database) {
        this.database = database;
        this.players = Players.getInstance(database);
    }

    public static synchronized Reports getInstance(Database database) {
        if (instance == null) {
            instance = new Reports(database);
        }
        return instance;
    }

    public void add(String reporter, String reported, String reason) {
        try (Connection connection = database.getConnection()) {
            String insertReportSql = "INSERT INTO reports (reporter, reported, reason) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertReportSql)) {
                pstmt.setString(1, reporter);
                pstmt.setString(2, reported);
                pstmt.setString(3, reason);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLast(String name) {
        try (Connection connection = database.getConnection()) {
            String selectLastReportSql = "SELECT * FROM reports WHERE reported = ? ORDER BY created_at DESC LIMIT 1";
            try (PreparedStatement pstmt = connection.prepareStatement(selectLastReportSql)) {
                pstmt.setString(1, name);
                try (ResultSet res = pstmt.executeQuery()) {
                    if (res.next()) {
                        String reporter = res.getString("reporter");
                        String reported = res.getString("reported");
                        String reason = res.getString("reason");
                        Timestamp createdAtTimestamp = res.getTimestamp("created_at");
                        String reportCount = players.reportCount(name);
                        return "§f§l---- Report Check (Last) -------------\n" +
                                "§2Player: §3§l" + reported + "\n" +
                                "§2Last reported by: §b§l" + reporter + "\n" +
                                "§2For: §9§l" + reason + "\n" +
                                "§2On: §6§l" + createdAtTimestamp + "\n" +
                                "§2Total reports: §9§l" + reportCount + "\n" +
                                "§f§l---------------------------------------";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "§4An error occurred while recovering data.";
        }
        return "§cThis player has no report";
    }
}
