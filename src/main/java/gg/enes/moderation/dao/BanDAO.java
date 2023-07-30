package gg.enes.moderation.dao;

import gg.enes.moderation.entities.BanEntity;
import gg.enes.moderation.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * BanDAO - Data Access Object for BanEntity
 * Handles database operations for BanEntity, including CRUD operations.
 */
public class BanDAO {
    /**
     * Constructor - Private to prevent direct instantiation of class.
     */
    private BanDAO() {
        // Private constructor
    }

    /**
     * getInstance() - Returns the single instance of BanDAO.
     *
     * @return The single instance of BanDAO.
     */
    public static BanDAO getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * create() - Creates a new BanEntity in the database.
     *
     * @param banEntity The BanEntity to be created.
     * @throws SQLException If an SQL exception occurs.
     */
    public void create(BanEntity banEntity) throws SQLException {
        // Code for inserting a new BanEntity into the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String create = "INSERT INTO bans (player_uuid, moderator_uuid, reason, time, created_at, active) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = connection.prepareStatement(create, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, banEntity.getPlayerUuid().toString());
                pstmt.setString(2, banEntity.getModeratorUuid().toString());
                pstmt.setString(3, banEntity.getReason());
                pstmt.setLong(4, banEntity.getTime());
                pstmt.setTimestamp(5, banEntity.getCreatedAt());
                pstmt.setBoolean(6, banEntity.getActive());

                pstmt.executeUpdate();

                try (ResultSet res = pstmt.getGeneratedKeys()) {
                    if (res.next()) {
                        banEntity.setId(res.getInt(1));
                    }
                }
            }
        }
    }

    /**
     * read() - Reads all BanEntity records from the database.
     *
     * @return A list of BanEntity records.
     * @throws SQLException If an SQL exception occurs.
     */
    public List<BanEntity> read() throws SQLException {
        // Code for reading all BanEntity records from the database
        // and returning them as a list

        List<BanEntity> banEntities = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String read = "SELECT * FROM bans";

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

                    BanEntity banEntity = new BanEntity(UUID.fromString(playerUuid), UUID.fromString(moderatorUuid), reason, time, createdAt, active);
                    banEntity.setId(id);

                    banEntities.add(banEntity);
                }
            }
        }

        return banEntities;
    }

    /**
     * update() - Updates an existing BanEntity in the database.
     *
     * @param banEntity The BanEntity to be updated.
     * @throws SQLException If an SQL exception occurs.
     */
    public void update(BanEntity banEntity) throws SQLException {
        // Code for updating an existing BanEntity in the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String update = "UPDATE bans SET time = ?, active = ? WHERE id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(update)) {
                pstmt.setLong(1, banEntity.getTime());
                pstmt.setBoolean(2, banEntity.getActive());
                pstmt.setInt(3, banEntity.getId());
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * delete() - Deletes a BanEntity from the database.
     *
     * @param banEntity The BanEntity to be deleted.
     * @throws SQLException If an SQL exception occurs.
     */
    public void delete(BanEntity banEntity) throws SQLException {
        // Code for deleting a BanEntity from the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String delete = "DELETE FROM bans WHERE id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(delete)) {
                pstmt.setInt(1, banEntity.getId());
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * getActiveBanByPlayerUuid() - Retrieves the active ban of a player by their UUID.
     *
     * @param playerUuid The UUID of the player.
     * @return The active BanEntity of the player, or null if not found.
     * @throws SQLException If an SQL exception occurs.
     */
    public BanEntity getActiveBanByPlayerUuid(UUID playerUuid) throws SQLException {
        // Code for retrieving the active ban of a player by their UUID

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM bans WHERE player_uuid = ? AND active = 1";

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

                        BanEntity banEntity = new BanEntity(playerUuid, UUID.fromString(moderatorUuid), reason, time, createdAt, active);
                        banEntity.setId(id);

                        return banEntity;
                    }
                }
            }
        }

        return null;
    }

    /**
     * getPlayerBans() - Retrieves all bans of a player by their UUID.
     *
     * @param playerUuid The UUID of the player.
     * @return A list of BanEntity objects representing the bans of the player.
     * @throws SQLException If an SQL exception occurs.
     */
    public List<BanEntity> getPlayerBans(UUID playerUuid) throws SQLException {
        // Code for retrieving all bans of a player by their UUID

        List<BanEntity> playerBans = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM bans WHERE player_uuid = ?";

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

                        BanEntity banEntity = new BanEntity(playerUuid, UUID.fromString(moderatorUuid), reason, time, createdAt, active);
                        banEntity.setId(id);

                        playerBans.add(banEntity);
                    }
                }
            }
        }

        return playerBans;
    }

    /**
     * InstanceHolder - Private static nested class that holds the single instance of BanAO.
     */
    private static final class InstanceHolder {
        private static final BanDAO instance = new BanDAO();
    }
}
