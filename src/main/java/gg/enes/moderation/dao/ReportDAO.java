package gg.enes.moderation.dao;

import gg.enes.moderation.entities.ReportEntity;
import gg.enes.moderation.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ReportDAO - Data Access Object for ReportEntity
 * Handles database operations for ReportEntity, including create, read and delete operations.
 */
public class ReportDAO {
    /**
     * Constructor - Private to prevent direct instantiation of class.
     */
    private ReportDAO() {
        // Private constructor
    }

    /**
     * getInstance() - Returns the single instance of ReportDAO.
     *
     * @return The single instance of ReportDAO.
     */
    public static ReportDAO getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * create() - Creates a new ReportEntity in the database.
     *
     * @param reportEntity The ReportEntity to be created.
     * @throws SQLException If an SQL exception occurs.
     */
    public void create(ReportEntity reportEntity) throws SQLException {
        // Code for inserting a new ReportEntity into the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String create = "INSERT INTO reports (reporter_uuid, reported_uuid, reason) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = connection.prepareStatement(create, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, reportEntity.getReporterUuid().toString());
                pstmt.setString(2, reportEntity.getReportedUuid().toString());
                pstmt.setString(3, reportEntity.getReason());

                pstmt.executeUpdate();

                try (ResultSet res = pstmt.getGeneratedKeys()) {
                    if (res.next()) {
                        reportEntity.setId(res.getInt(1));
                    }
                }
            }
        }
    }

    /**
     * read() - Reads all ReportEntity records from the database.
     *
     * @return A list of ReportEntity records.
     * @throws SQLException If an SQL exception occurs.
     */
    public List<ReportEntity> read() throws SQLException {
        // Code for reading all ReportEntity records from the database
        // and returning them as a list

        List<ReportEntity> reportEntities = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String read = "SELECT * FROM reports";

            try (Statement stmt = connection.createStatement();
                 ResultSet res = stmt.executeQuery(read)) {
                while (res.next()) {
                    Integer id = res.getInt("id");
                    String reporterUuid = res.getString("reporter_uuid");
                    String reportedUuid = res.getString("reported_uuid");
                    String reason = res.getString("reason");
                    Timestamp createdAt = res.getTimestamp("created_at");

                    ReportEntity reportEntity = new ReportEntity(UUID.fromString(reporterUuid), UUID.fromString(reportedUuid), reason, createdAt);
                    reportEntity.setId(id);

                    reportEntities.add(reportEntity);
                }
            }
        }

        return reportEntities;
    }

    /**
     * delete() - Deletes a ReportEntity from the database.
     *
     * @param reportEntity The ReportEntity to be deleted.
     * @throws SQLException If an SQL exception occurs.
     */
    public void delete(ReportEntity reportEntity) throws SQLException {
        // Code for deleting a ReportEntity from the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String delete = "DELETE FROM reports WHERE id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(delete)) {
                pstmt.setInt(1, reportEntity.getId());
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * getPlayerReports() - Retrieves all reports of a player by their UUID.
     *
     * @param playerUuid The UUID of the player.
     * @return A list of ReportEntity objects representing the reports of the player.
     * @throws SQLException If an SQL exception occurs.
     */
    public List<ReportEntity> getPlayerReports(UUID playerUuid) throws SQLException {
        // Code for retrieving all reports of a player by their UUID

        List<ReportEntity> playerReports = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM reports WHERE reported_uuid = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, playerUuid.toString());

                try (ResultSet res = pstmt.executeQuery()) {
                    while (res.next()) {
                        Integer id = res.getInt("id");
                        String reporterUuid = res.getString("reporter_uuid");
                        String reason = res.getString("reason");
                        Timestamp time = res.getTimestamp("time");
                        Timestamp createdAt = res.getTimestamp("created_at");

                        ReportEntity reportEntity = new ReportEntity(playerUuid, UUID.fromString(reporterUuid), reason, createdAt);
                        reportEntity.setId(id);

                        playerReports.add(reportEntity);
                    }
                }
            }
        }

        return playerReports;
    }

    /**
     * InstanceHolder - Private static nested class that holds the single instance of ReportDAO.
     */
    private static final class InstanceHolder {
        private static final ReportDAO instance = new ReportDAO();
    }
}