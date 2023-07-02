package gg.enes.moderation.commands;

import gg.enes.moderation.Main;
import gg.enes.moderation.gui.Inventory;
import gg.enes.moderation.storage.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Moderator implements CommandExecutor {
    private final Plugin plugin = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (!player.hasPermission("moderator.tools.use")) {
                player.sendMessage("§cError! You do not have permission to use this command");
                return true;
            }

            if (CacheManager.moderator.has(player.getName())) {
                CacheManager.moderator.remove(player.getName());
                if (CacheManager.moderator.vanish.has(player.getName())) {
                    CacheManager.moderator.vanish.remove(player.getName());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.hidePlayer(plugin, player);
                    }
                }
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().setContents(CacheManager.inventory.restore(player.getName()));
                player.sendMessage("§cYou have exited moderator mode.");
            } else {
                CacheManager.moderator.add(player.getName());
                CacheManager.inventory.save(player);
                player.setGameMode(GameMode.CREATIVE);
                CacheManager.inventory.save(player);
                Inventory.set(player);
                player.sendMessage("§2You have joined moderator mode.");
            }

            return true;
        }

        return false;
    }
}
