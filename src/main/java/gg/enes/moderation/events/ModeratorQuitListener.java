package gg.enes.moderation.events;

import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.util.CacheUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ModeratorQuitListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerEntity playerEntity = CacheUtil.getPlayerEntity(player.getUniqueId());

        if (playerEntity == null) {
            return;
        }

        if (playerEntity.getModeratorMode()) {
            player.getInventory().setContents(playerEntity.getInventory());

            playerEntity.setModeratorMode(false);
            playerEntity.setInventory(null);
        }
    }
}