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

public final class UserRepository implements BaseRepository<Long, User> {
    /**
     * The singleton instance of the user repository.
     */
    private static UserRepository instance;

    /**
     * The cache manager of the user repository.
     */
    private final CacheManager<Long, User> cacheManager;

    private UserRepository() {
        this.cacheManager = new CaffeineCacheManager<>(CacheConfig.build());
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

    @Override
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

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Entity (User): " + entity);
        }

        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public User read(final Long id, final @Nullable Boolean force) {
        User user = this.cacheManager.get(id);

        if (user != null && (force == null || !force)) {
            return user;
        }

        String sql = "SELECT * FROM mt_users WHERE user_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User()
                            .setId(rs.getLong("user_id"))
                            .setUuid((UUID) rs.getObject("uuid"))
                            .setUsername(rs.getString("username"))
                            .setIp(rs.getString("ip"))
                            .setMuted(rs.getBoolean("muted"))
                            .setBanned(rs.getBoolean("banned"))
                            .setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
                    this.cacheManager.set(id, user);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading a user entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Entity (User): " + user);
        }

        return user;
    }

    @Override
    public void update(final User entity) {
        String sql = "UPDATE mt_users SET uuid = ?, username = ?, ip = ?, muted = ?, banned = ?, created_at = ? WHERE user_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setObject(1, entity.getUuid());
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
        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public void delete(final Long id) {
        String sql = "DELETE FROM mt_users WHERE user_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting a user entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (User): " + id);
        }

        this.cacheManager.del(id);
    }
}
