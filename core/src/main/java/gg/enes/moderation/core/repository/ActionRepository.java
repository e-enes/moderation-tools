package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.cache.CacheManager;
import gg.enes.moderation.core.cache.CaffeineCacheManager;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.entity.Action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ActionRepository implements BaseRepository<Long, Action> {
    /**
     * The singleton instance of the action repository.
     */
    private static ActionRepository instance;

    /**
     * The cache manager of the action repository.
     */
    private final CacheManager<Long, Action> cacheManager;

    private ActionRepository() {
        this.cacheManager = new CaffeineCacheManager<>(CacheConfig.getInstance());
    }

    /**
     * Retrieves the singleton instance of the action repository.
     *
     * @return The singleton instance of the action repository.
     */
    public static ActionRepository getInstance() {
        if (instance == null) {
            instance = new ActionRepository();
        }

        return instance;
    }

    @Override
    public void create(final Action entity) {
        String sql = "INSERT INTO mt_actions (action_id, user_id, action_type, description, server, details, ip, performed_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getId());
            stmt.setLong(2, entity.getUserId());
            stmt.setString(3, entity.getActionType());
            stmt.setString(4, entity.getDescription());
            stmt.setString(5, entity.getServer());
            stmt.setString(6, entity.getDetails());
            stmt.setString(7, entity.getIp());
            stmt.setObject(8, entity.getPerformedAt());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while creating an action entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Entity (Action): " + entity);
        }

        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public Action read(final Long id, final Boolean force) {
        Action action = this.cacheManager.get(id);
        if (action != null && (force == null || !force)) {
            return action;
        }

        String sql = "SELECT * FROM mt_actions WHERE action_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    action = new Action()
                            .setId(rs.getLong("action_id"))
                            .setUserId(rs.getLong("user_id"))
                            .setActionType(rs.getString("action_type"))
                            .setDescription(rs.getString("description"))
                            .setServer(rs.getString("server"))
                            .setDetails(rs.getString("details"))
                            .setIp(rs.getString("ip"))
                            .setPerformedAt(rs.getObject("performed_at", LocalDateTime.class));
                    this.cacheManager.set(id, action);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while reading an action entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Action): " + id);
        }

        return action;
    }

    @Override
    public void update(final Action entity) {
        String sql =
                "UPDATE mt_actions SET user_id = ?, action_type = ?, description = ?, server = ?, details = ?, ip = ?, performed_at = ? WHERE action_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, entity.getUserId());
            stmt.setString(2, entity.getActionType());
            stmt.setString(3, entity.getDescription());
            stmt.setString(4, entity.getServer());
            stmt.setString(5, entity.getDetails());
            stmt.setString(6, entity.getIp());
            stmt.setObject(7, entity.getPerformedAt());
            stmt.setLong(8, entity.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while updating an action entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Entity (Action): " + entity);
        }
        this.cacheManager.set(entity.getId(), entity);
    }

    @Override
    public void delete(final Long id) {
        String sql = "DELETE FROM mt_actions WHERE action_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while deleting an action entity.", e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("ID (Action): " + id);
        }
        this.cacheManager.del(id);
    }

    /**
     * Retrieves all actions for a given user ID.
     *
     * @param userId The ID of the user.
     * @return A list of actions for the user.
     */
    public List<Action> findByUserId(final Long userId) {
        List<Action> actions = new ArrayList<>();
        String sql = "SELECT * FROM mt_actions WHERE user_id = ? ORDER BY performed_at DESC";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Action action = new Action()
                            .setId(rs.getLong("action_id"))
                            .setUserId(rs.getLong("user_id"))
                            .setActionType(rs.getString("action_type"))
                            .setDescription(rs.getString("description"))
                            .setServer(rs.getString("server"))
                            .setDetails(rs.getString("details"))
                            .setIp(rs.getString("ip"))
                            .setPerformedAt(rs.getObject("performed_at", LocalDateTime.class));
                    actions.add(action);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while retrieving actions for user ID: " + userId, e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("User ID: " + userId);
        }
        return actions;
    }

    /**
     * Retrieves all actions performed on a given server.
     *
     * @param server The server where the actions were performed.
     * @return A list of actions performed on the server.
     */
    public List<Action> findByServer(final String server) {
        List<Action> actions = new ArrayList<>();
        String sql = "SELECT * FROM mt_actions WHERE server = ? ORDER BY performed_at DESC";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, server);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Action action = new Action()
                            .setId(rs.getLong("action_id"))
                            .setUserId(rs.getLong("user_id"))
                            .setActionType(rs.getString("action_type"))
                            .setDescription(rs.getString("description"))
                            .setServer(rs.getString("server"))
                            .setDetails(rs.getString("details"))
                            .setIp(rs.getString("ip"))
                            .setPerformedAt(rs.getObject("performed_at", LocalDateTime.class));
                    actions.add(action);
                }
            }
        } catch (Exception e) {
            ModerationLogger.error("An error occurred while retrieving actions for server: " + server, e);

            ModerationLogger.debug("SQL: " + sql);
            ModerationLogger.debug("Server: " + server);
        }
        return actions;
    }
}
