package enes.plugin.moderation.events;

import enes.plugin.moderation.storage.cache.Data;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayersFreeze implements Listener {
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        boolean isFrozen = Data.freeze.has(event.getPlayer().getName());
        if (isFrozen) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        boolean isFrozen = Data.freeze.has(event.getPlayer().getName());
        if (isFrozen) {
            event.setCancelled(true);
        }
    }
}
