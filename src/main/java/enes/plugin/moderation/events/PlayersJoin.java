package enes.plugin.moderation.events;

import enes.plugin.moderation.storage.Database;
import enes.plugin.moderation.utils.Players;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayersJoin implements Listener {
    private final Players players;

    public PlayersJoin(Database playerDatabase) {
        this.players = Players.getInstance(playerDatabase);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        players.add(event.getPlayer().getName());
    }
}
