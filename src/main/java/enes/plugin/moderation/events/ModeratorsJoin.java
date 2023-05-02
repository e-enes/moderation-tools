package enes.plugin.moderation.events;

import enes.plugin.moderation.gui.Moderators;
import enes.plugin.moderation.storage.cache.Data;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ModeratorsJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        boolean isMod = Data.moderator.has(name);
        if (isMod) {
            Data.moderator.connected(name);
            Data.inventory.save(event.getPlayer());
            Moderators.inventory.set(event.getPlayer());
            event.getPlayer().sendMessage("§2§lYou are in moderator mode.");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName();
        boolean isMod = Data.moderator.has(name);
        if (isMod) {
            Data.moderator.disconnected(name);
            event.getPlayer().getInventory().setContents(Data.inventory.restore(event.getPlayer().getName()));
        }
    }
}