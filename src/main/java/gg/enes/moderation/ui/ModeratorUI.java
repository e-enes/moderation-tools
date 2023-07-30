package gg.enes.moderation.ui;

import gg.enes.moderation.util.CacheUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class ModeratorUI {
    public static void set(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        player.getInventory().setItem(0, eyeOfEnder());
        player.getInventory().setItem(1, book());
        player.getInventory().setItem(2, ice());
        player.getInventory().setItem(4, stick());
        player.getInventory().setItem(5, compass());
        player.getInventory().setItem(8, barrier());

        player.updateInventory();
    }

    private static ItemStack eyeOfEnder() {
        ItemStack item = new ItemStack(Material.ENDER_EYE, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "View a player's inventory");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click on a player to", ChatColor.WHITE + "see their inventory"));
        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack book() {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "View a player's reports");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click on a player to", ChatColor.WHITE + "see their reports"));
        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack ice() {
        ItemStack item = new ItemStack(Material.ICE, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "Freeze the player");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click on a player to", ChatColor.WHITE + "freeze them"));
        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack stick() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "Test player's kb");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click on a player to", ChatColor.WHITE + "test their kb"));
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.KNOCKBACK, 10, true);

        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack compass() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "Teleport to player");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to teleport to", ChatColor.WHITE + "la random player"));
        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack barrier() {
        ItemStack item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.YELLOW + "Activate vanish");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to become", ChatColor.WHITE + "invisible"));
        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        return item;
    }

}
