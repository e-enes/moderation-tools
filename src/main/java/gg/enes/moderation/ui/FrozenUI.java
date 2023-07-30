package gg.enes.moderation.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class FrozenUI {
    public static void set(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.BLUE + "You've been frozen!");

        inventory.setItem(12, itemAdmitted());
        inventory.setItem(14, itemWait());

        player.openInventory(inventory);
    }

    private static ItemStack itemAdmitted() {
        ItemStack item = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Admit");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to admit", ChatColor.WHITE + "and receive a 14-day ban"));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack itemWait() {
        ItemStack item = new ItemStack(Material.BLUE_WOOL, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Contact Moderation");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Join the discord", ChatColor.WHITE + "to get in touch with the moderation team", ChatColor.RED + "If you disconnect while being frozen, you will be banned."));
        item.setItemMeta(meta);

        return item;
    }
}