package gg.enes.moderation.dao;

import gg.enes.moderation.entities.MuteEntity;
import gg.enes.moderation.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MuteDAO - Data Access Object for MuteEntity
 * Handles database operations for MuteEntity, including CRUD operations.
 */
public class MuteDAO {
    /**
     * Constructor - Private to prevent direct instantiation of class.
     */
    private MuteDAO() {
        // Private constructor
    }

    /**
     * getInstance() - Returns the single instance of MuteDAO.
     *
     * @return The single instance of MuteDAO.
     */
    public static MuteDAO getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * create() - Creates a new MuteEntity in the database.
     *
     * @param muteEntity The MuteEntity to be created.
     * @throws SQLException If an SQL exception occurs.
     */
    public void create(MuteEntity muteEntity) throws SQLException {
        // Code for inserting a new MuteEntity into the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String create = "INSERT INTO mutes (player_uuid, moderator_uuid, reason, time, created_at, active) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = connection.prepareStatement(create, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, muteEntity.getPlayerUuid().toString());
                pstmt.setString(2, muteEntity.getModeratorUuid().toString());
                pstmt.setString(3, muteEntity.getReason());
                pstmt.setLong(4, muteEntity.getTime());
                pstmt.setTimestamp(5, muteEntity.getCreatedAt());
                pstmt.setBoolean(6, muteEntity.getActive());

                pstmt.executeUpdate();

                try (ResultSet res = pstmt.getGeneratedKeys()) {
                    if (res.next()) {
                        muteEntity.setId(res.getInt(1));
                    }
                }
            }
        }
    }

    /**
     * read() - Reads all MuteEntity records from the database.
     *
     * @return A list of MuteEntity records.
     * @throws SQLException If an SQL exception occurs.
     */
    public List<MuteEntity> read() throws SQLException {
        // Code for reading all MuteEntity records from the database
        // and returning them as a list

        List<MuteEntity> muteEntities = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String read = "SELECT * FROM mutes";

            try (Statement stmt = connection.createStatement();
                 ResultSet res = stmt.executeQuery(read)) {
                while (res.next()) {
                    Integer id = res.getInt("id");
                    String playerUuid = res.getString("player_uuid");
                    String moderatorUuid = res.getString("moderator_uuid");
                    String reason = res.getString("reason");
                    Long time = res.getLong("time");
                    Timestamp createdAt = res.getTimestamp("created_at");
                    Boolean active = res.getBoolean("active");

                    MuteEntity muteEntity = new MuteEntity(UUID.fromString(playerUuid), UUID.fromString(moderatorUuid), reason, time, createdAt, active);
                    muteEntity.setId(id);

                    muteEntities.add(muteEntity);
                }
            }
        }

        return muteEntities;
    }

    /**
     * update() - Updates an existing MuteEntity in the database.
     *
     * @param muteEntity The MuteEntity to be updated.
     * @throws SQLException If an SQL exception occurs.
     */
    public void update(MuteEntity muteEntity) throws SQLException {
        // Code for updating an existing MuteEntity in the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String update = "UPDATE mutes SET time = ?, active = ? WHERE id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(update)) {
                pstmt.setLong(1, muteEntity.getTime());
                pstmt.setBoolean(2, muteEntity.getActive());
                pstmt.setInt(3, muteEntity.getId());
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * delete() - Deletes a MuteEntity from the database.
     *
     * @param muteEntity The MuteEntity to be deleted.
     * @throws SQLException If an SQL exception occurs.
     */
    public void delete(MuteEntity muteEntity) throws SQLException {
        // Code for deleting a MuteEntity from the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String delete = "DELETE FROM mutes WHERE id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(delete)) {
                pstmt.setInt(1, muteEntity.getId());
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * getActiveMuteByPlayerUuid() - Retrieves the active mute of a player by their UUID.
     *
     * @param playerUuid The UUID of the player.
     * @return The active MuteEntity of the player, or null if not found.
     * @throws SQLException If an SQL exception occurs.
     */
    public MuteEntity getActiveMuteByPlayerUuid(UUID playerUuid) throws SQLException {
        // Code for retrieving the active mute of a player by their UUID

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM mutes WHERE player_uuid = ? AND active = 1";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, playerUuid.toString());

                try (ResultSet res = pstmt.executeQuery()) {
                    if (res.next()) {
                        Integer id = res.getInt("id");
                        String moderatorUuid = res.getString("moderator_uuid");
                        String reason = res.getString("reason");
                        Long time = res.getLong("time");
                        Timestamp createdAt = res.getTimestamp("created_at");
                        Boolean active = res.getBoolean("active");

                        MuteEntity muteEntity = new MuteEntity(playerUuid, UUID.fromString(moderatorUuid), reason, time, createdAt, active);
                        muteEntity.setId(id);

                        return muteEntity;
                    }
                }
            }
        }

        return null;
    }

    /**
     * getPlayerMutes() - Retrieves all mutes of a player by their UUID.
     *
     * @param playerUuid The UUID of the player.
     * @return A list of MuteEntity objects representing the mutes of the player.
     * @throws SQLException If an SQL exception occurs.
     */
    public List<MuteEntity> getPlayerMutes(UUID playerUuid) throws SQLException {
        // Code for retrieving all mutes of a player by their UUID

        List<MuteEntity> playerMutes = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM mutes WHERE player_uuid = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, playerUuid.toString());

                try (ResultSet res = pstmt.executeQuery()) {
                    while (res.next()) {
                        Integer id = res.getInt("id");
                        String moderatorUuid = res.getString("moderator_uuid");
                        String reason = res.getString("reason");
                        Long time = res.getLong("time");
                        Timestamp createdAt = res.getTimestamp("created_at");
                        Boolean active = res.getBoolean("active");

                        MuteEntity muteEntity = new MuteEntity(playerUuid, UUID.fromString(moderatorUuid), reason, time, createdAt, active);
                        muteEntity.setId(id);

                        playerMutes.add(muteEntity);
                    }
                }
            }
        }

        return playerMutes;
    }

    /**
     * InstanceHolder - Private static nested class that holds the single instance of MuteDAO.
     */
    private static final class InstanceHolder {
        private static final MuteDAO instance = new MuteDAO();
    }
}
