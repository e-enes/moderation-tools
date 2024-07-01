package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.cache.CacheManager;
import gg.enes.moderation.core.cache.CaffeineCacheManager;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.entity.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ConnectionRepository {
    /**
     * The singleton instance of the connection repository.
     */
    private static ConnectionRepository instance;

    /**
     * The cache manager for connections.
     */
    private final CacheManager<UUID, List<Connection>> cacheManager;

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

    /**
     * Creates a new connection entity in the database.
     *
     * @param entity The connection entity to create.
     */
    public void create(final Connection entity) {
        String sql = "INSERT INTO mt_connections (user_uuid, ip, connected_at, disconnected_at) VALUES (?, ?, ?, ?)";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, entity.getUser().getUuid().toString());
            stmt.setString(2, entity.getIp());
            stmt.setObject(3, entity.getConnectedAt());
            stmt.setObject(4, entity.getDisconnectedAt());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while creating a new connection entity.", e);
        }
        addToCache(entity.getUser().getUuid(), entity);
    }

    /**
     * Retrieves all connections for a given user UUID.
     *
     * @param userUuid The UUID of the user to retrieve connections for.
     * @return A list of connections associated with the user.
     */
    public List<Connection> readAll(final UUID userUuid) {
        List<Connection> connections = this.cacheManager.get(userUuid);
        if (connections != null && !connections.isEmpty()) {
            return connections;
        }

        connections = new ArrayList<>();
        String sql = "SELECT * FROM mt_connections WHERE user_uuid = ? ORDER BY connected_at DESC";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userUuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    connections.add(new Connection()
                            .setId(rs.getLong("connection_id"))
                            .setUser(UUID.fromString(rs.getString("user_uuid")))
                            .setIp(rs.getString("ip"))
                            .setConnectedAt(rs.getObject("connected_at", LocalDateTime.class))
                            .setDisconnectedAt(rs.getObject("disconnected_at", LocalDateTime.class)));
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading connections for user with UUID " + userUuid, e);
        }

        this.cacheManager.set(userUuid, connections);
        return connections;
    }

    /**
     * Retrieves the last connection for a given user UUID.
     *
     * @param userUuid The UUID of the user to retrieve the last connection for.
     * @return The last connection associated with the user.
     */
    public Connection readLastAdded(final UUID userUuid) {
        return readLastAdded(userUuid, false);
    }

    /**
     * Retrieves the last connection for a given user UUID.
     *
     * @param userUuid The UUID of the user to retrieve the last connection for.
     * @param force    If true, forces a database read even if the entity is cached.
     * @return The last connection associated with the user.
     */
    public Connection readLastAdded(final UUID userUuid, final boolean force) {
        if (!force) {
            List<Connection> connections = this.cacheManager.get(userUuid);
            if (connections != null && !connections.isEmpty()) {
                return connections.getFirst();
            }
        }

        this.cacheManager.del(userUuid);
        Connection connection = null;
        String sql = "SELECT * FROM mt_connections WHERE user_uuid = ? ORDER BY connection_id DESC LIMIT 1";
        try (java.sql.Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userUuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    connection = new Connection()
                            .setId(rs.getLong("connection_id"))
                            .setUser(UUID.fromString(rs.getString("user_uuid")))
                            .setIp(rs.getString("ip"))
                            .setConnectedAt(rs.getObject("connected_at", LocalDateTime.class))
                            .setDisconnectedAt(rs.getObject("disconnected_at", LocalDateTime.class));
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading the last connection for user with UUID " + userUuid, e);
        }
        return connection;
    }

    /**
     * Updates a connection entity in the database.
     *
     * @param entity The connection entity to update.
     */
    public void update(final Connection entity) {
        String sql = "UPDATE mt_connections SET disconnected_at = ? WHERE connection_id = ?";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setObject(1, entity.getDisconnectedAt());
            stmt.setLong(2, entity.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while updating a connection entity.", e);
        }

        this.cacheManager.del(entity.getUser().getUuid());
    }

    /**
     * Deletes all connections for a given user UUID.
     *
     * @param id The UUID of the user whose connections to delete.
     */
    public void deleteById(final Long id) {
        String sql = "DELETE FROM mt_connections WHERE connection_id = ?";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

            this.cacheManager.clear();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting connections with id " + id, e);
        }
    }

    /**
     * Deletes the last added connection for a given user UUID.
     *
     * @param userUuid The UUID of the user whose last connection to delete.
     */
    public void deleteLastAdded(final UUID userUuid) {
        String sql = "DELETE FROM mt_connections WHERE user_uuid = ? ORDER BY connection_id DESC LIMIT 1";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userUuid.toString());
            stmt.executeUpdate();

            List<Connection> connections = this.cacheManager.get(userUuid);
            if (connections != null && !connections.isEmpty()) {
                connections.removeFirst();
                this.cacheManager.set(userUuid, connections);
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting the last connection for user with UUID " + userUuid, e);
        }
    }

    /**
     * Adds a connection to the cache.
     *
     * @param userUuid   The UUID of the user associated with the connection.
     * @param connection The connection to add to the cache.
     */
    private void addToCache(final UUID userUuid, final Connection connection) {
        List<Connection> connections = this.cacheManager.get(userUuid);
        if (connections == null) {
            connections = new ArrayList<>();
        }
        connections.add(connection);
        this.cacheManager.set(userUuid, connections);
    }
}
