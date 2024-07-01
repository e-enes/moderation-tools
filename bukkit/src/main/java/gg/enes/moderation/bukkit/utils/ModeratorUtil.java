package gg.enes.moderation.bukkit.utils;

import gg.enes.moderation.bukkit.ModerationLanguage;
import gg.enes.moderation.bukkit.enums.ReportType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ModeratorUtil {
    /**
     * The map of moderators in mod mode.
     */
    private static final Map<UUID, Inventory> MODERATORS = new HashMap<>();

    private ModeratorUtil() {
    }

    /**
     * Adds a moderator to the mod mode.
     *
     * @param moderator The moderator to add.
     */
    public static void addModerator(final Player moderator) {
        MODERATORS.put(moderator.getUniqueId(), moderator.getInventory());
    }

    /**
     * Removes a moderator from the mod mode.
     *
     * @param moderator The moderator to remove.
     */
    public static void removeModerator(final Player moderator) {
        Inventory inventory = MODERATORS.remove(moderator.getUniqueId());
        moderator.getInventory().setContents(inventory.getContents());
    }

    /**
     * Notifies all moderators about a report.
     *
     * @param reported The reported player.
     * @param reportType The type of the report.
     */
    public static void notify(final UUID reported, final ReportType reportType) {
        Player target = Bukkit.getPlayer(reported);

        if (target == null) {
            return;
        }

        MODERATORS.forEach((uuid, inventory) -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                return;
            }

            player.sendMessage(ModerationLanguage.getMessage("message.moderation.notify.reported", target.getName(), reportType.toString()));
        });
    }
}
