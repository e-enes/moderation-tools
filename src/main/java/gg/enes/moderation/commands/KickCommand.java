package gg.enes.moderation.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class KickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("moderator.tools.kick")) {
            player.sendMessage(ChatColor.RED + "Error! You do not have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Error! Usage: /kick <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Error! Player not found.");
            return true;
        }

        if (!target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Error! Player not found (online).");
            return true;
        }

        if (Objects.equals(player.getName(), target.getName())) {
            player.sendMessage(ChatColor.RED + "Error! You can't mute yourself.");
            return true;
        }

        target.kickPlayer(ChatColor.RED + "You have been kicked out of this server.");
        player.sendMessage(ChatColor.GREEN + "This player was ejected");

        return true;
    }
}
