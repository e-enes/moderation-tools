package gg.enes.moderation.dao;

import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * PlayerDAO - Data Access Object for PlayerEntity
 * Handles database operations for PlayerEntity, including CRUD operations.
 */
public class PlayerDAO {
    /**
     * Constructor - Private to prevent direct instantiation of the class.
     */
    private PlayerDAO() {
        // Private constructor
    }

    /**
     * getInstance() - Returns the single instance of PlayerDAO.
     *
     * @return The single instance of PlayerDAO.
     */
    public static PlayerDAO getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * create() - Creates a new PlayerEntity in the database.
     *
     * @param playerEntity The PlayerEntity to be created.
     * @throws SQLException If an SQL exception occurs.
     */
    public void create(PlayerEntity playerEntity) throws SQLException {
        // Code for inserting a new PlayerEntity into the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String create = "INSERT INTO players (name, uuid, ip, muted, banned) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = connection.prepareStatement(create, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, playerEntity.getName());
                pstmt.setString(2, playerEntity.getUuid().toString());
                pstmt.setString(3, playerEntity.getIp());
                pstmt.setBoolean(4, playerEntity.getMuted());
                pstmt.setBoolean(5, playerEntity.getBanned());

                pstmt.executeUpdate();

                try (ResultSet res = pstmt.getGeneratedKeys()) {
                    if (res.next()) {
                        playerEntity.setId(res.getInt(1));
                    }
                }
            }
        }
    }

    /**
     * read() - Reads all PlayerEntity records from the database.
     *
     * @return A list of PlayerEntity records.
     * @throws SQLException If an SQL exception occurs.
     */
    public List<PlayerEntity> read() throws SQLException {
        // Code for reading all PlayerEntity records from the database
        // and returning them as a list

        List<PlayerEntity> playerEntities = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String read = "SELECT * FROM players";

            try (Statement stmt = connection.createStatement();
                 ResultSet res = stmt.executeQuery(read)) {
                while (res.next()) {
                    Integer id = res.getInt("id");
                    String name = res.getString("name");
                    String uuid = res.getString("uuid");
                    String ip = res.getString("ip");
                    Boolean muted = res.getBoolean("muted");
                    Boolean banned = res.getBoolean("banned");
                    Timestamp lastConnection = res.getTimestamp("last_connection");
                    Timestamp createdAt = res.getTimestamp("created_at");

                    PlayerEntity playerEntity = new PlayerEntity(name, UUID.fromString(uuid), ip, muted, banned, lastConnection, createdAt);
                    playerEntity.setId(id);

                    playerEntities.add(playerEntity);
                }
            }
        }

        return playerEntities;
    }

    /**
     * update() - Updates an existing PlayerEntity in the database.
     *
     * @param playerEntity The PlayerEntity to be updated.
     * @throws SQLException If an SQL exception occurs.
     */
    public void update(PlayerEntity playerEntity) throws SQLException {
        // Code for updating an existing PlayerEntity in the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String update = "UPDATE players SET name = ?, ip = ?, muted = ?, banned = ?, last_connection = ? WHERE id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(update)) {
                pstmt.setString(1, playerEntity.getName());
                pstmt.setString(2, playerEntity.getIp());
                pstmt.setBoolean(3, playerEntity.getMuted());
                pstmt.setBoolean(4, playerEntity.getBanned());
                pstmt.setTimestamp(5, playerEntity.getLastConnection());
                pstmt.setInt(6, playerEntity.getId());
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * delete() - Deletes a PlayerEntity from the database.
     *
     * @param playerEntity The PlayerEntity to be deleted.
     * @throws SQLException If an SQL exception occurs.
     */
    public void delete(PlayerEntity playerEntity) throws SQLException {
        // Code for deleting a PlayerEntity from the database

        try (Connection connection = DatabaseUtil.getConnection()) {
            String delete = "DELETE FROM players WHERE id = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(delete)) {
                pstmt.setInt(1, playerEntity.getId());
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * getByUuid() - Retrieves a player from the database using their UUID.
     *
     * @param uuid The UUID of the player to retrieve.
     * @return The PlayerEntity if found, null otherwise.
     * @throws SQLException If an SQL exception occurs.
     */
    public PlayerEntity getByUuid(UUID uuid) throws SQLException {
        // Code for retrieving a player from the database using their UUID

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM players WHERE uuid = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, uuid.toString());

                try (ResultSet res = pstmt.executeQuery()) {
                    if (res.next()) {
                        Integer id = res.getInt("id");
                        String name = res.getString("name");
                        String ip = res.getString("ip");
                        Boolean muted = res.getBoolean("muted");
                        Boolean banned = res.getBoolean("banned");
                        Timestamp lastConnection = res.getTimestamp("last_connection");
                        Timestamp createdAt = res.getTimestamp("created_at");

                        PlayerEntity playerEntity = new PlayerEntity(name, uuid, ip, muted, banned, lastConnection, createdAt);
                        playerEntity.setId(id);

                        return playerEntity;
                    }
                }
            }
        }

        return null;
    }

    /**
     * InstanceHolder - Private static nested class that holds the single instance of PlayerDAO.
     */
    private static final class InstanceHolder {
        private static final PlayerDAO instance = new PlayerDAO();
    }
}