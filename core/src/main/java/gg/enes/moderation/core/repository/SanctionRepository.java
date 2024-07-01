package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.cache.CacheManager;
import gg.enes.moderation.core.cache.CaffeineCacheManager;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.entity.Sanction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SanctionRepository {
    /**
     * The singleton instance of the sanction repository.
     */
    private static SanctionRepository instance;

    /**
     * The cache manager of the sanction repository.
     */
    private final CacheManager<UUID, List<Sanction>> cacheManager;

    private SanctionRepository() {
        this.cacheManager = new CaffeineCacheManager<>(CacheConfig.getInstance());
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

    /**
     * Creates a new sanction entity in the database.
     *
     * @param entity The sanction entity to create.
     */
    public void create(final Sanction entity) {
        String sql = "INSERT INTO mt_sanctions (user_uuid, reason, type, created_at, expires_at, active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, entity.getUser().getUuid().toString());
            stmt.setString(2, entity.getReason());
            stmt.setString(3, entity.getType());
            stmt.setObject(4, entity.getCreatedAt());
            stmt.setObject(5, entity.getExpiresAt());
            stmt.setBoolean(6, entity.getActive());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while creating a sanction entity.", e);
        }

        addToCache(entity.getUser().getUuid(), entity);
    }

    /**
     * Retrieves all sanctions for a given user UUID.
     *
     * @param userUuid The UUID of the user to retrieve sanctions for.
     * @return A list of sanctions associated with the user.
     */
    public List<Sanction> readAll(final UUID userUuid) {
        List<Sanction> sanctions = this.cacheManager.get(userUuid);
        if (sanctions != null) {
            return sanctions;
        }

        sanctions = new ArrayList<>();
        String sql = "SELECT * FROM mt_sanctions WHERE user_uuid = ? ORDER BY sanction_id DESC";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userUuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sanctions.add(new Sanction()
                            .setId(rs.getLong("sanction_id"))
                            .setUser(userUuid)
                            .setReason(rs.getString("reason"))
                            .setType(rs.getString("type"))
                            .setCreatedAt(rs.getObject("created_at", LocalDateTime.class))
                            .setExpiresAt(rs.getObject("expires_at", LocalDateTime.class))
                            .setActive(rs.getBoolean("active"))
                    );
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading sanctions for user " + userUuid, e);
        }

        this.cacheManager.set(userUuid, sanctions);
        return sanctions;
    }

    /**
     * Retrieves the last added sanction for a user from the database.
     *
     * @param userUuid The UUID of the user whose last added sanction to read.
     * @return The last added sanction for the user.
     */
    public Sanction readLastAdded(final UUID userUuid) {
        return readLastAdded(userUuid, false);
    }

    /**
     * Retrieves the last added sanction for a user from the database.
     *
     * @param userUuid The UUID of the user whose last added sanction to read.
     * @param force    Whether to force reading from the database even if cached data is available.
     * @return The last added sanction for the user.
     */
    public Sanction readLastAdded(final UUID userUuid, final boolean force) {
        if (!force) {
            List<Sanction> sanctions = this.cacheManager.get(userUuid);
            if (sanctions != null && !sanctions.isEmpty()) {
                return sanctions.getFirst();
            }
        }

        this.cacheManager.del(userUuid);
        Sanction sanction = null;
        String sql = "SELECT * FROM mt_sanctions WHERE user_uuid = ? ORDER BY sanction_id DESC LIMIT 1";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userUuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    sanction = new Sanction()
                            .setId(rs.getLong("sanction_id"))
                            .setUser(userUuid)
                            .setReason(rs.getString("reason"))
                            .setType(rs.getString("type"))
                            .setCreatedAt(rs.getObject("created_at", LocalDateTime.class))
                            .setExpiresAt(rs.getObject("expires_at", LocalDateTime.class))
                            .setActive(rs.getBoolean("active"));
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading last added sanction for user " + userUuid, e);
        }
        return sanction;
    }

    /**
     * Retrieves active sanctions for a user from the database.
     *
     * @param userUuid The UUID of the user to retrieve active sanctions for.
     * @return A list of active sanctions for the user.
     */
    public List<Sanction> readActiveSanctions(final UUID userUuid) {
        return readActiveSanctions(userUuid, false);
    }

    /**
     * Retrieves active sanctions for a user from the database.
     *
     * @param userUuid The UUID of the user to retrieve active sanctions for.
     * @param force    Whether to force reading from the database even if cached data is available.
     * @return A list of active sanctions for the user.
     */
    public List<Sanction> readActiveSanctions(final UUID userUuid, final boolean force) {
        List<Sanction> activeSanctions = new ArrayList<>();

        if (!force) {
            List<Sanction> sanctions = this.cacheManager.get(userUuid);
            if (sanctions != null && !sanctions.isEmpty()) {
                for (Sanction sanction : sanctions) {
                    if (sanction.getActive()) {
                        activeSanctions.add(sanction);
                    }
                }
                return activeSanctions;
            }
        }

        this.cacheManager.del(userUuid);
        String sql = "SELECT * FROM mt_sanctions WHERE user_uuid = ? AND active = true";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userUuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Sanction sanction = new Sanction()
                            .setId(rs.getLong("sanction_id"))
                            .setUser(userUuid)
                            .setReason(rs.getString("reason"))
                            .setType(rs.getString("type"))
                            .setCreatedAt(rs.getObject("created_at", LocalDateTime.class))
                            .setExpiresAt(rs.getObject("expires_at", LocalDateTime.class))
                            .setActive(rs.getBoolean("active"));
                    activeSanctions.add(sanction);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading active sanctions for user " + userUuid, e);
        }
        return activeSanctions;
    }

    /**
     * Updates an existing sanction entity in the database.
     *
     * @param entity The sanction entity to update.
     */
    public void update(final Sanction entity) {
        String sql = "UPDATE mt_sanctions SET user_uuid = ?, reason = ?, type = ?, created_at = ?, expires_at = ?, active = ? WHERE sanction_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, entity.getUser().getUuid().toString());
            stmt.setString(2, entity.getReason());
            stmt.setString(3, entity.getType());
            stmt.setObject(4, entity.getCreatedAt());
            stmt.setObject(5, entity.getExpiresAt());
            stmt.setBoolean(6, entity.getActive());
            stmt.setLong(7, entity.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while updating a sanction entity.", e);
        }
        this.cacheManager.del(entity.getUser().getUuid());
    }

    /**
     * Deletes a sanction by its ID from the database.
     *
     * @param id The ID of the sanction to delete.
     */
    public void deleteById(final Long id) {
        String sql = "DELETE FROM mt_sanctions WHERE sanction_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

            this.cacheManager.clear();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting sanction with ID " + id, e);
        }
    }

    /**
     * Deletes the last added sanction for a user from the database.
     *
     * @param userUuid The UUID of the user whose last added sanction to delete.
     */
    public void deleteLastAdded(final UUID userUuid) {
        String sql = "DELETE FROM mt_sanctions WHERE user_uuid = ? ORDER BY sanction_id DESC LIMIT 1";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userUuid.toString());
            stmt.executeUpdate();

            List<Sanction> sanctions = this.cacheManager.get(userUuid);
            if (sanctions != null && !sanctions.isEmpty()) {
                sanctions.removeFirst();
                this.cacheManager.set(userUuid, sanctions);
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting last added sanction for user " + userUuid, e);
        }
    }

    /**
     * Adds a sanction to the cache.
     *
     * @param userUuid The UUID of the user associated with the sanction.
     * @param sanction The sanction to add to the cache.
     */
    private void addToCache(final UUID userUuid, final Sanction sanction) {
        List<Sanction> sanctions = this.cacheManager.get(userUuid);
        if (sanctions == null) {
            sanctions = new ArrayList<>();
        }
        sanctions.add(sanction);
        this.cacheManager.set(userUuid, sanctions);
    }
}
