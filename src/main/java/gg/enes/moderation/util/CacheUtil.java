package gg.enes.moderation.util;

import gg.enes.moderation.entities.BanEntity;
import gg.enes.moderation.entities.MuteEntity;
import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.entities.ReportEntity;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.function.Function;

/**
 * Utility class for caching Player, Ban, Report, and Mute entities.
 */
public abstract class CacheUtil {
    private static final Map<UUID, PlayerEntity> playerEntityMap = new HashMap<>();
    private static final Map<UUID, List<BanEntity>> banEntityMap = new HashMap<>();
    private static final Map<UUID, List<ReportEntity>> reportEntityMap = new HashMap<>();
    private static final Map<UUID, List<MuteEntity>> muteEntityMap = new HashMap<>();

    /**
     * Initializes the cache with the provided entities.
     *
     * @param playerEntities  List of PlayerEntities
     * @param banEntities     List of BanEntities
     * @param reportEntities  List of ReportEntities
     * @param muteEntities    List of MuteEntities
     */
    public static void init(List<PlayerEntity> playerEntities, List<BanEntity> banEntities, List<ReportEntity> reportEntities, List<MuteEntity> muteEntities) {
        playerEntities.forEach(playerEntity -> playerEntityMap.put(playerEntity.getUuid(), playerEntity));

        addEntitiesToMap(banEntities, banEntityMap, BanEntity::getPlayerUuid);
        addEntitiesToMap(reportEntities, reportEntityMap, ReportEntity::getReportedUuid);
        addEntitiesToMap(muteEntities, muteEntityMap, MuteEntity::getPlayerUuid);
    }

    /**
     * Initializes the cache with the provided entities for a specific player.
     *
     * @param playerEntity    The PlayerEntity for the player
     * @param banEntities     List of BanEntities associated with the player
     * @param reportEntities  List of ReportEntities associated with the player
     * @param muteEntities    List of MuteEntities associated with the player
     */
    public static void initPlayer(PlayerEntity playerEntity, List<BanEntity> banEntities, List<ReportEntity> reportEntities, List<MuteEntity> muteEntities) {
        playerEntityMap.put(playerEntity.getUuid(), playerEntity);

        addEntitiesToMap(banEntities, banEntityMap, BanEntity::getPlayerUuid);
        addEntitiesToMap(reportEntities, reportEntityMap, ReportEntity::getReportedUuid);
        addEntitiesToMap(muteEntities, muteEntityMap, MuteEntity::getPlayerUuid);
    }

    private static <T> void addEntitiesToMap(List<T> entities, Map<UUID, List<T>> map, Function<T, UUID> keyExtractor) {
        entities.forEach(entity -> map.computeIfAbsent(keyExtractor.apply(entity), key -> new ArrayList<>()).add(entity));
    }

    /**
     * Removes the cache entries for a specific player.
     *
     * @param playerUuid The UUID of the player
     */
    public static void removePlayer(UUID playerUuid) {
        playerEntityMap.remove(playerUuid);
        banEntityMap.remove(playerUuid);
        reportEntityMap.remove(playerUuid);
        muteEntityMap.remove(playerUuid);
    }

    /**
     * Saves a PlayerEntity to the cache.
     *
     * @param playerEntity  The PlayerEntity to save
     */
    public static void savePlayerEntity(PlayerEntity playerEntity) {
        playerEntityMap.put(playerEntity.getUuid(), playerEntity);
    }

    /**
     * Retrieves a PlayerEntity from the cache.
     *
     * @param uuid  The UUID of the player
     * @return The PlayerEntity associated with the UUID, or null if not found
     */
    public static PlayerEntity getPlayerEntity(UUID uuid) {
        return playerEntityMap.getOrDefault(uuid, null);
    }

    /**
     * Removes a PlayerEntity from the cache.
     *
     * @param uuid  The UUID of the player to remove
     */
    public static void removePlayerEntity(UUID uuid) {
        playerEntityMap.remove(uuid);
    }

    /**
     * Retrieves a list of all PlayerEntities in the cache.
     *
     * @return List of all PlayerEntities
     */
    public static List<PlayerEntity> getPlayerEntities() {
        return new ArrayList<>(playerEntityMap.values());
    }

    /**
     * Saves a BanEntity to the cache.
     *
     * @param banEntity  The BanEntity to save
     */
    public static void saveBanEntities(BanEntity banEntity) {
        banEntityMap.computeIfAbsent(banEntity.getPlayerUuid(), key -> new ArrayList<>()).add(banEntity);
    }

    /**
     * Retrieves a list of BanEntities associated with a given UUID from the cache.
     *
     * @param uuid  The UUID of the player
     * @return List of BanEntities associated with the UUID
     */
    public static List<BanEntity> getBanEntities(UUID uuid) {
        return banEntityMap.getOrDefault(uuid, null);
    }

    /**
     * Removes all BanEntities associated with a given UUID from the cache.
     *
     * @param uuid  The UUID of the player
     */
    public static void removeBanEntity(UUID uuid) {
        banEntityMap.remove(uuid);
    }

    /**
     * Saves a ReportEntity to the cache.
     *
     * @param reportEntity  The ReportEntity to save
     */
    public static void saveReportEntity(ReportEntity reportEntity) {
        reportEntityMap.computeIfAbsent(reportEntity.getReportedUuid(), key -> new ArrayList<>()).add(reportEntity);
    }

    /**
     * Retrieves a list of ReportEntities associated with a given UUID from the cache.
     *
     * @param uuid  The UUID of the reported player
     * @return List of ReportEntities associated with the UUID
     */
    public static List<ReportEntity> getReportEntities(UUID uuid) {
        return reportEntityMap.getOrDefault(uuid, null);
    }

    /**
     * Removes all ReportEntities associated with a given UUID from the cache.
     *
     * @param uuid  The UUID of the reported player
     */
    public static void removeReportEntity(UUID uuid) {
        reportEntityMap.remove(uuid);
    }

    /**
     * Saves a MuteEntity to the cache.
     *
     * @param muteEntity  The MuteEntity to save
     */
    public static void saveMuteEntity(MuteEntity muteEntity) {
        muteEntityMap.computeIfAbsent(muteEntity.getPlayerUuid(), key -> new ArrayList<>()).add(muteEntity);
    }

    /**
     * Retrieves a list of MuteEntities associated with a given UUID from the cache.
     *
     * @param uuid  The UUID of the player
     * @return List of MuteEntities associated with the UUID
     */
    public static List<MuteEntity> getMuteEntities(UUID uuid) {
        return muteEntityMap.getOrDefault(uuid, null);
    }

    /**
     * Removes all MuteEntities associated with a given UUID from the cache.
     *
     * @param uuid  The UUID of the player
     */
    public static void removeMuteEntity(UUID uuid) {
        muteEntityMap.remove(uuid);
    }
}