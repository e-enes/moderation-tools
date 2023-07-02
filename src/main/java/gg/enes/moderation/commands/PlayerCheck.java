package gg.enes.moderation.commands;

import gg.enes.moderation.dao.PlayerDAO;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCheck implements CommandExecutor {
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (!player.hasPermission("moderator.tools.use")) {
                player.sendMessage("§cError! You do not have permission to use this command");
                return true;
            }

            if (strings.length < 1) {
                player.sendMessage("§cError! Usage: /player-check <player>");
                return true;
            }

            if (Bukkit.getPlayer(strings[0]) == null) {
                player.sendMessage("§cError! Usage: /player-check <player>");
                return true;
            }

            String message = playerDAO.get(player.getName());
            player.sendMessage(message);

            return true;
        }

        return false;
    }
}