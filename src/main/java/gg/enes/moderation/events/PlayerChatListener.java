package gg.enes.moderation.events;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.MuteDAO;
import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.entities.MuteEntity;
import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.util.CacheUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerChatListener implements Listener {
    private final MuteDAO muteDAO = MuteDAO.getInstance();
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();
    private final Logger logger = Main.getInstance().getLogger();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PlayerEntity playerEntity = CacheUtil.getPlayerEntity(uuid);

        if (playerEntity == null) {
            return;
        }

        if (playerEntity.getMuted()) {
            if (event.getMessage().startsWith("/")) return;

            event.setCancelled(true);

            List<MuteEntity> muteEntities = CacheUtil.getMuteEntities(uuid);

            for (MuteEntity muteEntity : muteEntities) {
                if (muteEntity.getActive()) {
                    if (System.currentTimeMillis() > muteEntity.getEnd().getTime()) {
                        playerEntity.setMuted(false);
                        muteEntity.setActive(false);

                        logger.log(Level.INFO, "Player " + player.getName() + " mute has expired.");

                        try {
                            muteDAO.update(muteEntity);
                            playerDAO.update(playerEntity);
                        } catch (SQLException e) {
                            logger.log(Level.SEVERE, "An SQL exception occurred while updating player.");
                            logger.log(Level.WARNING, e.getMessage());
                        }
                    }

                    player.sendMessage(ChatColor.RED + "You have been muted for " + muteEntity.getReason() + ", you can speak again on " + new Date(muteEntity.getEnd().getTime()));
                    break;
                }
            }
        }
    }
}