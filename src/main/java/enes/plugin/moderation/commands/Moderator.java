package enes.plugin.moderation.coommands;

import enes.plugin.moderation.gui.Moderators;
import enes.plugin.moderation.storage.cache.Data;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Moderator implements CommandExecutor {
    private final Plugin plugin;
    public Moderator(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (Data.moderator.has(player.getName())) {
                Data.moderator.remove(player.getName());
                if (Data.moderator.vanish.has(player.getName())) {
                    Data.moderator.vanish.remove(player.getName());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.hidePlayer(plugin, player);
                    }
                }
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().setContents(Data.inventory.restore(player.getName()));
                player.sendMessage("§cYou have exited moderator mode.");
            } else {
                Data.moderator.add(player.getName());
                Data.inventory.save(player);
                player.setGameMode(GameMode.CREATIVE);
                Data.inventory.save(player);
                Moderators.inventory.set(player);
                player.sendMessage("§2You have joined moderator mode.");
            }
            return false;
        }
        return false;
    }
}
