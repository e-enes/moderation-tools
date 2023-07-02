package gg.enes.moderation.dao;

import gg.enes.moderation.storage.DatabaseUtil;

import java.sql.*;

public class PlayerDAO {
    private PlayerDAO() {

    }

    private static final class InstanceHolder {
        private static final PlayerDAO instance = new PlayerDAO();
    }

    public static PlayerDAO getInstance() {
        return InstanceHolder.instance;
    }

    public void add(String name) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String existsPlayerSql = "SELECT player_name FROM players WHERE player_name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(existsPlayerSql)) {
                pstmt.setString(1, name);
                try (ResultSet res = pstmt.executeQuery()) {
                    boolean exists = res.next();
                    if (!exists) {
                        try (PreparedStatement addPstmt = connection.prepareStatement("INSERT INTO players (player_name) VALUES (?)")) {
                            addPstmt.setString(1, name);
                            addPstmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reported(String name) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String updatePlayerSql = "UPDATE players SET report_count = report_count + 1 WHERE player_name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updatePlayerSql)) {
                pstmt.setString(1, name);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String reportCount(String name) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String selectPlayerSql = "SELECT report_count FROM players WHERE player_name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(selectPlayerSql)) {
                pstmt.setString(1, name);
                try (ResultSet res = pstmt.executeQuery()) {
                    if (res.next()) {
                        return res.getString("report_count");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public String get(String name) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String selectPlayerSql = "SELECT * FROM players WHERE player_name = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(selectPlayerSql)) {
                pstmt.setString(1, name);
                try (ResultSet res = pstmt.executeQuery()) {
                    if (res.next()) {
                        String player = res.getString("player_name");
                        String reportCount = res.getString("report_count");
                        String banCount = res.getString("ban_count");
                        Timestamp createdAtTimestamp = res.getTimestamp("created_at");
                        return "§f§l---- Player Check -------------\n" +
                                "§2Player: §3§l" + player + "\n" +
                                "§2Total reports: §b§l" + reportCount + "\n" +
                                "§2Total ban: §c§l" + banCount + "\n" +
                                "§2First Joined: §6§l" + createdAtTimestamp + "\n" +
                                "§f§l------------------------------";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "§4An error occurred while recovering data.";
        }
        return "§cNo data was found for this player.";
    }
}