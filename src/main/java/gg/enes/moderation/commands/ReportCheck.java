package gg.enes.moderation.commands;

import gg.enes.moderation.dao.ReportDAO;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCheck implements CommandExecutor {
    private final ReportDAO reportDAO = ReportDAO.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (!player.hasPermission("moderator.tools.use")) {
                player.sendMessage("§cError! You do not have permission to use this command");
                return true;
            }

            if (strings.length < 1) {
                player.sendMessage(ChatColor.RED + "Error! Usage: /report-check <player>");
                return true;
            }

            if (Bukkit.getPlayer(strings[0]) == null) {
                player.sendMessage(ChatColor.RED + "Error! Usage: /report-check <player>");
                return true;
            }

            String message = reportDAO.getLast(player.getName());

            player.sendMessage(message);

            return true;
        }

        return false;
    }
}