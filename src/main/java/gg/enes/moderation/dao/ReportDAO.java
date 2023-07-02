package gg.enes.moderation.dao;

import gg.enes.moderation.storage.DatabaseUtil;

import java.sql.*;

public class ReportDAO {
    private ReportDAO() {

    }

    private static final class InstanceHolder {
        private static final ReportDAO instance = new ReportDAO();
    }

    public static ReportDAO getInstance() {
        return InstanceHolder.instance;
    }

    public void add(String reporter, String reported, String reason) {
        try (Connection connection = DatabaseUtil.getConnection()) {
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
        try (Connection connection = DatabaseUtil.getConnection()) {
            String selectLastReportSql = "SELECT * FROM reports WHERE reported = ? ORDER BY created_at DESC LIMIT 1";
            try (PreparedStatement pstmt = connection.prepareStatement(selectLastReportSql)) {
                pstmt.setString(1, name);
                try (ResultSet res = pstmt.executeQuery()) {
                    if (res.next()) {
                        String reporter = res.getString("reporter");
                        String reported = res.getString("reported");
                        String reason = res.getString("reason");
                        Timestamp createdAtTimestamp = res.getTimestamp("created_at");
                        return "§f§l---- Report Check (Last) -------------\n" +
                                "§2Player: §3§l" + reported + "\n" +
                                "§2Last reported by: §b§l" + reporter + "\n" +
                                "§2For: §9§l" + reason + "\n" +
                                "§2On: §6§l" + createdAtTimestamp + "\n" +
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