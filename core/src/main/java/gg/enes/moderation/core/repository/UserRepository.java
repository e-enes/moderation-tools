package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.cache.CacheManager;
import gg.enes.moderation.core.cache.CaffeineCacheManager;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.entity.User;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.UUID;

public final class UserRepository {
    /**
     * The singleton instance of the user repository.
     */
    private static UserRepository instance;

    /**
     * The cache manager of the user repository.
     */
    private final CacheManager<UUID, User> cacheManager;

    private UserRepository() {
        this.cacheManager = new CaffeineCacheManager<>(CacheConfig.getInstance());
    }

    /**
     * Retrieves the singleton instance of the user repository.
     *
     * @return The singleton instance of the user repository.
     */
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }

        return instance;
    }

    /**
     * Creates a new user entity in the database.
     *
     * @param entity The user entity to create.
     */
    public void create(final User entity) {
        String sql = "INSERT INTO mt_users (user_id, uuid, username, ip, muted, banned, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getId());
            stmt.setObject(2, entity.getUuid());
            stmt.setString(3, entity.getUsername());
            stmt.setString(4, entity.getIp());
            stmt.setBoolean(5, entity.getMuted());
            stmt.setBoolean(6, entity.getBanned());
            stmt.setObject(7, entity.getCreatedAt());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while creating a user entity.", e);
        }

        this.cacheManager.set(entity.getUuid(), entity);
    }

    /**
     * Reads a user entity from the database using the provided UUID.
     * Optionally forces a database read bypassing the cache.
     *
     * @param uuid  The UUID of the user entity to read.
     * @param force If true, forces a database read even if the entity is cached.
     * @return The user entity, or null if not found.
     */
    public User read(final UUID uuid, final @Nullable Boolean force) {
        User user = this.cacheManager.get(uuid);

        if (user != null && (force == null || !force)) {
            return user;
        }

        String sql = "SELECT * FROM mt_users WHERE user_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User()
                            .setId(rs.getLong("user_id"))
                            .setUuid(UUID.fromString(rs.getString("uuid")))
                            .setUsername(rs.getString("username"))
                            .setIp(rs.getString("ip"))
                            .setMuted(rs.getBoolean("muted"))
                            .setBanned(rs.getBoolean("banned"))
                            .setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    this.cacheManager.set(uuid, user);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading a user entity.", e);
        }

        return user;
    }

    /**
     * Updates an existing user entity in the database and updates the cache.
     *
     * @param entity The user entity to update.
     */
    public void update(final User entity) {
        String sql = "UPDATE mt_users SET uuid = ?, username = ?, ip = ?, muted = ?, banned = ?, created_at = ? WHERE user_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, entity.getUuid().toString());
            stmt.setString(2, entity.getUsername());
            stmt.setString(3, entity.getIp());
            stmt.setBoolean(4, entity.getMuted());
            stmt.setBoolean(5, entity.getBanned());
            stmt.setObject(6, entity.getCreatedAt());
            stmt.setLong(7, entity.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while updating a user entity.", e);
        }

        this.cacheManager.set(entity.getUuid(), entity);
    }

    /**
     * Deletes a user entity from the database and removes it from the cache.
     *
     * @param uuid The UUID of the user entity to delete.
     */
    public void delete(final UUID uuid) {
        String sql = "DELETE FROM mt_users WHERE uuid = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting a user entity.", e);
        }

        this.cacheManager.del(uuid);
    }
}
