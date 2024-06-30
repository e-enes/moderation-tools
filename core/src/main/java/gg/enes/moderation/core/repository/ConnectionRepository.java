package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.cache.CacheManager;
import gg.enes.moderation.core.cache.CaffeineCacheManager;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.entity.Connection;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ConnectionRepository implements BaseRepository<Long, Connection> {
    /**
     * The singleton instance of the connection repository.
     */
    private static ConnectionRepository instance;

    /**
     * The cache manager of the connection repository.
     */
    private final CacheManager<Long, Connection> cacheManager;

    private ConnectionRepository() {
        this.cacheManager = new CaffeineCacheManager<>(CacheConfig.getInstance());
    }

    /**
     * Retrieves the singleton instance of the connection repository.
     *
     * @return The singleton instance of the connection repository.
     */
    public static ConnectionRepository getInstance() {
        if (instance == null) {
            instance = new ConnectionRepository();
        }

        return instance;
    }

    @Override
    public void create(final Connection entity) {
        String sql = "INSERT INTO mt_connections (connection_id, user_id, ip, connected_at, disconnected_at) VALUES (?, ?, ?, ?, ?)";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getId());
            stmt.setLong(2, entity.getUserId());
            stmt.setString(3, entity.getIp());
            stmt.setObject(4, entity.getConnectedAt());
            stmt.setObject(5, entity.getDisconnectedAt());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while creating a connection entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Entity (Connection): " + entity);
        }
        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public Connection read(final Long id, final Boolean force) {
        Connection connectionEntity = this.cacheManager.get(id);
        if (connectionEntity != null && (force == null || !force)) {
            return connectionEntity;
        }

        String sql = "SELECT * FROM mt_connections WHERE connection_id = ?";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    connectionEntity = new Connection()
                            .setId(rs.getLong("connection_id"))
                            .setUserId(rs.getLong("user_id"))
                            .setIp(rs.getString("ip"))
                            .setConnectedAt(rs.getObject("connected_at", LocalDateTime.class))
                            .setDisconnectedAt(rs.getObject("disconnected_at", LocalDateTime.class));
                    this.cacheManager.set(id, connectionEntity);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading a connection entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Connection): " + id);
        }

        return connectionEntity;
    }

    @Override
    public void update(final Connection entity) {
        String sql = "UPDATE mt_connections SET user_id = ?, ip = ?, connected_at = ?, disconnected_at = ? WHERE connection_id = ?";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getUserId());
            stmt.setString(2, entity.getIp());
            stmt.setObject(3, entity.getConnectedAt());
            stmt.setObject(4, entity.getDisconnectedAt());
            stmt.setLong(5, entity.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while updating a connection entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Entity (Connection): " + entity);
        }

        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public void delete(final Long id) {
        String sql = "DELETE FROM mt_connections WHERE connection_id = ?";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting a connection entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Connection): " + id);
        }

        this.cacheManager.del(id);
    }

    /**
     * Retrieves all connections for a given user ID.
     *
     * @param userId The ID of the user.
     * @param activeOnly Whether to retrieve only active connections.
     * @return A list of connections for the user.
     */
    public List<Connection> findByUserId(final Long userId, @Nullable final Boolean activeOnly) {
        List<Connection> connections = new ArrayList<>();
        String sql = "SELECT * FROM mt_connections WHERE user_id = ?";

        if (activeOnly != null) {
            sql += " AND disconnected_at IS " + (activeOnly ? "NULL" : "NOT NULL");
        }
        sql += " ORDER BY connected_at DESC";

        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Connection connectionEntity = new Connection()
                            .setId(rs.getLong("connection_id"))
                            .setUserId(rs.getLong("user_id"))
                            .setIp(rs.getString("ip"))
                            .setConnectedAt(rs.getObject("connected_at", LocalDateTime.class))
                            .setDisconnectedAt(rs.getObject("disconnected_at", LocalDateTime.class));
                    connections.add(connectionEntity);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while retrieving connections for user ID: " + userId, e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (User): " + userId);
        }
        return connections;
    }
}
