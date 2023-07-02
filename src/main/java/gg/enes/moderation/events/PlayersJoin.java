package gg.enes.moderation.events;

import gg.enes.moderation.dao.PlayerDAO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayersJoin implements Listener {
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerDAO.add(event.getPlayer().getName());
    }
}