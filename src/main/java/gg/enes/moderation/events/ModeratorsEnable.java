package gg.enes.moderation.events;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.storage.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
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
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Random;

public class ModeratorsEnable implements Listener {
    private final Plugin plugin = Main.getInstance();
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        boolean isMod = CacheManager.moderator.has(event.getPlayer().getName());
        if (isMod) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        boolean isMod = CacheManager.moderator.has(event.getWhoClicked().getName());
        if (isMod) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            boolean isMod = CacheManager.moderator.has(event.getEntity().getName());
            if (isMod) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        boolean isMod = CacheManager.moderator.has(event.getPlayer().getName());
        if (isMod) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        boolean isMod = CacheManager.moderator.has(event.getPlayer().getName());
        if (isMod) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Player)) {
            boolean isMod = CacheManager.moderator.has(event.getEntity().getName());
            if (isMod) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.OFF_HAND && event.getRightClicked().getType() == EntityType.PLAYER) {
            boolean isMod = CacheManager.moderator.has(event.getPlayer().getName());
            if (isMod) {
                Player player = event.getPlayer();
                Player player1 = Bukkit.getPlayer(event.getRightClicked().getName());
                ItemStack itemStack = player.getItemInUse();
                if (itemStack != null) {
                    String item = String.valueOf(itemStack.getType());
                    switch (item) {
                        case ("EYE_OF_ENDER") -> {
                            player.openInventory(player1.getInventory());
                            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 4f, 4f);
                            event.setCancelled(true);
                        }
                        case ("BOOK") -> {
                            String reportCount = playerDAO.reportCount(player1.getName());
                            player.sendMessage("§2This player has §c§l" +  reportCount + " §2reports.");
                            event.setCancelled(true);
                        }
                        case ("ICE") -> {
                            boolean isFrozen = CacheManager.freeze.has(player1.getName());
                            if (isFrozen) {
                                player.sendMessage("§2This player is no longer frozen.");
                                player1.sendMessage("§2You are no longer frozen.");
                                CacheManager.freeze.remove(player1.getName());
                            } else {
                                player.sendMessage("§3This player has been frozen.");
                                player1.sendMessage("§cYou have been frozen. You can't move.");
                                CacheManager.freeze.add(player1.getName());
                            }
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 4f, 4f);
                            player1.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 4f, 4f);
                            player1.setGameMode(isFrozen ? GameMode.SURVIVAL : GameMode.ADVENTURE);
                            event.setCancelled(true);
                        }
                        default -> event.setCancelled(true);
                    }
                }

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        boolean isMod = CacheManager.moderator.has(event.getPlayer().getName());
        if (isMod) {
            Player player = event.getPlayer();
            ItemStack itemStack = event.getItem();
            if (itemStack != null) {
                String item = String.valueOf(itemStack.getType());
                switch (item) {
                    case ("COMPASS") -> {
                        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                        if (players.size() <= 1) {
                            player.sendMessage("§cThere are not enough players.");
                            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 4f, 4f);
                        } else {
                            Player randomPlayer = players.get(new Random().nextInt(players.size()));
                            player.teleport(randomPlayer.getLocation());
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 4f, 4f);
                            player.sendMessage("§2You have been teleported to §3§l" + randomPlayer.getName());
                        }
                    }
                    case ("BARRIER") -> {
                        if (CacheManager.moderator.vanish.has(player.getName())) {
                            player.sendMessage("§3Vanish has been §c§ldisabled.");
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName("§6Activate vanish");
                            itemStack.setItemMeta(itemMeta);
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.showPlayer(plugin, player);
                            }
                            CacheManager.moderator.vanish.remove(player.getName());
                        } else {
                            player.sendMessage("§3Vanish has been §2§lenabled.");
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName("§6Disable vanish");
                            itemStack.setItemMeta(itemMeta);
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.hidePlayer(plugin, player);
                            }
                            CacheManager.moderator.vanish.add(player.getName());
                        }
                    }
                }
            }

            event.setCancelled(true);
        }
    }
}