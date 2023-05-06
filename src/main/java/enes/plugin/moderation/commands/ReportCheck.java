package enes.plugin.moderation.coommands;

import enes.plugin.moderation.utils.Reports;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCheck implements CommandExecutor {
    private final Reports reports;

    public ReportCheck(Reports reports) {
        this.reports = reports;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length < 1) {
                commandSender.sendMessage(ChatColor.RED + "Error! Usage: /report-check <player>");
                return true;
            }

            if (Bukkit.getPlayer(strings[0]) == null) {
                commandSender.sendMessage(ChatColor.RED + "Error! Usage: /report-check <player>");
                return true;
            }

            String message = reports.getLast(commandSender.getName());

            commandSender.sendMessage(message);

            return true;
        }
        return false;
    }
}
