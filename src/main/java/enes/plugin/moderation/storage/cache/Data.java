package enes.plugin.moderation.storage.cache;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    private static final HashMap<String, Boolean> isMod = new HashMap<>();
    private static final HashMap<String, Boolean> isVanish = new HashMap<>();
    private static final HashMap<String, Boolean> isFrozen = new HashMap<>();

    private static final HashMap<String, ItemStack[]> inventory = new HashMap<>();

    public static class moderator {
        public static boolean has(String name) {
            return isMod.containsKey(name);
        }

        public static void add(String name) {
            isMod.put(name, true);
        }

        public static void remove(String name) {
            isMod.remove(name);
        }

        public static void connected(String name) {
            isMod.remove(name);
            isMod.put(name, true);
        }

        public static void disconnected(String name) {
            isMod.remove(name);
            isMod.put(name, false);
        }

        public static List<String> getAll() {
            List<String> mods = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : isMod.entrySet()) {
                if (entry.getValue()) {
                    mods.add(entry.getKey());
                }
            }
            return mods;
        }

        public static class vanish {
            public static boolean has(String name) {
                return isVanish.containsKey(name);
            }

            public static void add(String name) {
                isVanish.put(name, true);
            }

            public static void remove(String name) {
                isVanish.remove(name);
            }
        }
    }

    public static class freeze {
        public static boolean has(String name) {
            return isFrozen.containsKey(name);
        }

        public static void add(String name) {
            isFrozen.put(name, true);
        }

        public static void remove(String name) {
            isFrozen.remove(name);
        }
    }

    public static class inventory {
        public static void save(Player player) {
            PlayerInventory inv = player.getInventory();
            ItemStack[] contents = inv.getContents();
            inventory.put(player.getName(), contents);
        }

        public static ItemStack[] restore(String name) {
            ItemStack[] contents = inventory.get(name);
            inventory.remove(name);
            return contents;
        }
    }
}
