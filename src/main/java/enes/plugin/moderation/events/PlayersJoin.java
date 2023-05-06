package enes.plugin.moderation.events;

import enes.plugin.moderation.utils.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayersJoin implements Listener {
    private final Players players;

    public PlayersJoin(Players players) {
        this.players = players;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        players.add(event.getPlayer().getName());
    }
}
