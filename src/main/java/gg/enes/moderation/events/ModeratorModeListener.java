package gg.enes.moderation.events;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.dao.ReportDAO;
import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.ui.FrozenUI;
import gg.enes.moderation.util.CacheUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModeratorModeListener implements Listener {
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();
    private final ReportDAO reportDAO = ReportDAO.getInstance();

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        boolean mode = CacheUtil.getPlayerEntity(event.getPlayer().getUniqueId()).getModeratorMode();

        if (mode) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        boolean mode = CacheUtil.getPlayerEntity(event.getWhoClicked().getUniqueId()).getModeratorMode();

        if (mode) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            boolean mode = CacheUtil.getPlayerEntity(player.getUniqueId()).getModeratorMode();

            if (mode) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        boolean mode = CacheUtil.getPlayerEntity(event.getPlayer().getUniqueId()).getModeratorMode();

        if (mode) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        boolean mode = CacheUtil.getPlayerEntity(event.getPlayer().getUniqueId()).getModeratorMode();

        if (mode) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            boolean mode = CacheUtil.getPlayerEntity(player.getUniqueId()).getModeratorMode();

            if (mode) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        boolean mode = CacheUtil.getPlayerEntity(event.getPlayer().getUniqueId()).getModeratorMode();

        if (!mode && event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        if (event.getRightClicked() instanceof Player player) {
            event.setCancelled(true);

            Player moderator = event.getPlayer();
            ItemStack item = moderator.getItemInUse();

            if (item == null) {
                return;
            }

            Material type = item.getType();

            if (type == Material.ENDER_EYE) {
                moderator.openInventory(player.getInventory());

                return;
            }

            if (type == Material.BOOK) {
                int count = CacheUtil.getReportEntities(player.getUniqueId()).size();
                moderator.sendMessage(ChatColor.YELLOW + "This player has " + ChatColor.GRAY + count + ChatColor.YELLOW + "reports.");

                return;
            }

            if (type == Material.ICE) {
                PlayerEntity playerEntity = CacheUtil.getPlayerEntity(player.getUniqueId());
                boolean frozen = playerEntity.getFrozen();

                if (frozen) {
                    playerEntity.setFrozen(false);
                    CacheUtil.savePlayerEntity(playerEntity);
                    player.closeInventory();

                    moderator.sendMessage(ChatColor.YELLOW + "This player is no longer frozen.");
                } else {
                    playerEntity.setFrozen(true);
                    CacheUtil.savePlayerEntity(playerEntity);
                    FrozenUI.set(player);

                    moderator.sendMessage(ChatColor.YELLOW + "This player has been frozen.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        boolean mode = CacheUtil.getPlayerEntity(event.getPlayer().getUniqueId()).getModeratorMode();

        if (!mode) {
            return;
        }

        event.setCancelled(true);

        Player moderator = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        Material type = item.getType();

        if (type == Material.COMPASS) {
            List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
            int size = playerList.size();

            if (size <= 1) {
                moderator.sendMessage(ChatColor.RED + "There are not enough players.");
            } else {
                Player player = playerList.get(new Random().nextInt(size));

                moderator.teleport(player.getLocation());
                moderator.sendMessage(ChatColor.YELLOW + "You have been teleported to " + ChatColor.GRAY + player.getName());
            }

            return;
        }

        if (type == Material.BARRIER) {
            PlayerEntity playerEntity = CacheUtil.getPlayerEntity(moderator.getUniqueId());
            boolean vanished = playerEntity.getVanished();

            if (vanished) {
                moderator.sendMessage(ChatColor.BLUE + "Vanish has been disabled.");

                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + "Activate vanish");
                item.setItemMeta(meta);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.showPlayer(Main.getInstance(), moderator);
                }

                playerEntity.setVanished(false);
            } else {
                moderator.sendMessage(ChatColor.BLUE + "Vanish has been enabled");

                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + "Disable vanish");
                item.setItemMeta(meta);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(Main.getInstance(), moderator);
                }

                playerEntity.setVanished(true);
            }
        }
    }
}