package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.cache.CacheManager;
import gg.enes.moderation.core.cache.CaffeineCacheManager;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.entity.Sanction;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class SanctionRepository implements BaseRepository<Long, Sanction> {
    /**
     * The singleton instance of the sanction repository.
     */
    private static SanctionRepository instance;

    /**
     * The cache manager of the sanction repository.
     */
    private final CacheManager<Long, Sanction> cacheManager;

    private SanctionRepository() {
        this.cacheManager = new CaffeineCacheManager<>(CacheConfig.build());
    }

    /**
     * Retrieves the singleton instance of the sanction repository.
     *
     * @return The singleton instance of the sanction repository.
     */
    public static SanctionRepository getInstance() {
        if (instance == null) {
            instance = new SanctionRepository();
        }

        return instance;
    }

    @Override
    public void create(final Sanction entity) {
        String sql = "INSERT INTO mt_sanctions (sanction_id, user_id, reason, type, created_at, expires_at, active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getId());
            stmt.setLong(2, entity.getUserId());
            stmt.setString(3, entity.getReason());
            stmt.setString(4, entity.getType());
            stmt.setObject(5, entity.getCreatedAt());
            stmt.setObject(6, entity.getExpiresAt());
            stmt.setBoolean(7, entity.getActive());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while creating a sanction entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Entity (Sanction): " + entity);
        }
        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public Sanction read(final Long id, final Boolean force) {
        Sanction sanction = this.cacheManager.get(id);
        if (sanction != null && (force == null || !force)) {
            return sanction;
        }

        String sql = "SELECT * FROM mt_sanctions WHERE sanction_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    sanction = new Sanction()
                            .setId(rs.getLong("sanction_id"))
                            .setUserId(rs.getLong("user_id"))
                            .setReason(rs.getString("reason"))
                            .setType(rs.getString("type"))
                            .setCreatedAt(rs.getObject("created_at", LocalDateTime.class))
                            .setExpiresAt(rs.getObject("expires_at", LocalDateTime.class))
                            .setActive(rs.getBoolean("active"));
                    this.cacheManager.set(id, sanction);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading a sanction entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Sanction): " + id);
        }

        return sanction;
    }

    @Override
    public void update(final Sanction entity) {
        String sql = "UPDATE mt_sanctions SET user_id = ?, reason = ?, type = ?, created_at = ?, expires_at = ?, active = ? WHERE sanction_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getUserId());
            stmt.setString(2, entity.getReason());
            stmt.setString(3, entity.getType());
            stmt.setObject(4, entity.getCreatedAt());
            stmt.setObject(5, entity.getExpiresAt());
            stmt.setBoolean(6, entity.getActive());
            stmt.setLong(7, entity.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while updating a sanction entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Entity (Sanction): " + entity);
        }

        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public void delete(final Long id) {
        String sql = "DELETE FROM mt_sanctions WHERE sanction_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting a sanction entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Sanction): " + id);
        }

        this.cacheManager.del(id);
    }

    /**
     * Retrieves all sanctions for a given user ID.
     *
     * @param userId The ID of the user.
     * @param activeOnly Whether to retrieve only active sanctions.
     * @return A list of sanctions for the user.
     */
    public List<Sanction> findByUserId(final Long userId, @Nullable final Boolean activeOnly) {
        List<Sanction> sanctions = new ArrayList<>();
        String sql = "SELECT * FROM mt_sanctions WHERE user_id = ?";

        if (activeOnly != null) {
            sql += " AND active = ?";
        }
        sql += " ORDER BY created_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            if (activeOnly != null) {
                stmt.setBoolean(2, activeOnly);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Sanction sanction = new Sanction()
                            .setId(rs.getLong("sanction_id"))
                            .setUserId(rs.getLong("user_id"))
                            .setReason(rs.getString("reason"))
                            .setType(rs.getString("type"))
                            .setCreatedAt(rs.getObject("created_at", LocalDateTime.class))
                            .setExpiresAt(rs.getObject("expires_at", LocalDateTime.class))
                            .setActive(rs.getBoolean("active"));
                    sanctions.add(sanction);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while retrieving sanctions for user ID: " + userId, e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (User): " + userId);
        }

        return sanctions;
    }
}
