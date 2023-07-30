package gg.enes.moderation.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class ReportUI {
    public static void set(Player player, String targetName) {
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Report");

        inventory.setItem(2, cheat());
        inventory.setItem(3, behavior());
        inventory.setItem(5, alliance());
        inventory.setItem(6, troll());
        inventory.setItem(8, target(targetName));

        player.openInventory(inventory);
    }

    private static ItemStack cheat() {
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Cheat");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Report a player", ChatColor.WHITE + "who cheats (fly, reach, no kb, ...)"));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack behavior() {
        ItemStack item = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Bad Behavior");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Report a player", ChatColor.WHITE + "who has a bad behavior (insults, threat, ...)"));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack alliance() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Alliance");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Report a player", ChatColor.WHITE + "who teams up"));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack troll() {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Troll");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Report a player", ChatColor.WHITE + "who trolls and annoys other players"));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack target(String target) {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(target);
        item.setItemMeta(meta);

        return item;
    }
}
