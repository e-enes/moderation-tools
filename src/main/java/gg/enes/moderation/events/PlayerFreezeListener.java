package gg.enes.moderation.events;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.BanDAO;
import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.entities.BanEntity;
import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.util.CacheUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerFreezeListener implements Listener {
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();
    private final BanDAO banDAO = BanDAO.getInstance();
    private final Logger logger = Main.getInstance().getLogger();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        PlayerEntity playerEntity = CacheUtil.getPlayerEntity(event.getPlayer().getUniqueId());

        if (playerEntity == null) {
            return;
        }

        if (playerEntity.getFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        PlayerEntity playerEntity = CacheUtil.getPlayerEntity(event.getPlayer().getUniqueId());

        if (playerEntity == null) {
            return;
        }

        if (playerEntity.getFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();

        if (humanEntity instanceof Player player) {
            PlayerEntity playerEntity = CacheUtil.getPlayerEntity(player.getUniqueId());

            if (playerEntity == null) {
                return;
            }

            if (playerEntity.getFrozen()) {
                event.setCancelled(true);

                ItemStack item = event.getCurrentItem();
                if (item != null && item.getType() == Material.RED_WOOL) {
                    banFreeze(player, playerEntity);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity humanEntity = event.getPlayer();

        if (humanEntity instanceof Player player) {
            PlayerEntity playerEntity = CacheUtil.getPlayerEntity(player.getUniqueId());

            if (playerEntity == null) {
                return;
            }

            if (playerEntity.getFrozen()) {
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    try {
                        player.openInventory(event.getInventory());
                    } catch (Exception e) {
                        return;
                    }
                }, 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerEntity playerEntity = CacheUtil.getPlayerEntity(player.getUniqueId());

        if (playerEntity == null) {
            return;
        }

        if (playerEntity.getFrozen()) {
            banFreeze(player, playerEntity);
        }
    }

    private void banFreeze(Player player, PlayerEntity playerEntity) {
        player.kickPlayer("You have been banned.");
        playerEntity.setBanned(true);

        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        long time = 14 * 24 * 60 * 60 * 1000;

        BanEntity banEntity = new BanEntity(player.getUniqueId(), playerEntity.getFrozenByModeratorUuid(), "Admit (Frozen) of cheating", time, createdAt, true);

        try {
            banDAO.create(banEntity);
            playerDAO.update(playerEntity);

            logger.log(Level.INFO, "Player " + player.getName() + " has been automatically banned for freeze.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An error occurred while banning player " + player.getName() + " for freeze.");
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}
