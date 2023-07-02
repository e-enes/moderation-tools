package gg.enes.moderation.events;

import gg.enes.moderation.gui.Inventory;
import gg.enes.moderation.storage.CacheManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ModeratorsJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        boolean isMod = CacheManager.moderator.has(name);
        if (isMod) {
            CacheManager.moderator.connected(name);
            CacheManager.inventory.save(event.getPlayer());
            Inventory.set(event.getPlayer());
            event.getPlayer().sendMessage("§2§lYou are in moderator mode.");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName();
        boolean isMod = CacheManager.moderator.has(name);
        if (isMod) {
            CacheManager.moderator.disconnected(name);
            event.getPlayer().getInventory().setContents(CacheManager.inventory.restore(event.getPlayer().getName()));
        }
    }
}