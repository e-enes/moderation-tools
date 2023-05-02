package enes.plugin.moderation.gui;

import enes.plugin.moderation.storage.cache.Data;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Moderators {
    public static class inventory {
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

        public static void save(Player player) {
            Data.inventory.save(player);
        }

        public static void restore(Player player) {
            ItemStack[] contents = Data.inventory.restore(player.getName());
            player.getInventory().setContents(contents);
        }
    }

    private static ItemStack eyeOfEnder() {
        ItemStack itemStack = new ItemStack(Material.ENDER_EYE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§eView a player's inventory");
        itemMeta.setLore(Arrays.asList("§7Click on a player to", "§7§lsee their inventory"));
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static ItemStack book() {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§eView a player's reports");
        meta.setLore(Arrays.asList("§7Click on a player to", "§7§lsee their reports"));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack ice() {
        ItemStack item = new ItemStack(Material.ICE, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§eFreeze the player");
        meta.setLore(Arrays.asList("§7Click on a player to", "§7§lfreeze them"));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack stick() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§eTest player's kb");
        meta.setLore(Arrays.asList("§7Click on a player to", "§7§ltest their kb"));
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.KNOCKBACK, 10, true);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack compass() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§eTeleport to player");
        meta.setLore(Arrays.asList("§7Click to teleport to", "§7§la random player"));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack barrier() {
        ItemStack item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§eActivate vanish");
        meta.setLore(Arrays.asList("§7Click to become", "§7§linvisible"));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

}
