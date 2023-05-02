package enes.plugin.moderation.coommands;

import enes.plugin.moderation.storage.Database;
import enes.plugin.moderation.storage.cache.Data;
import enes.plugin.moderation.utils.Players;
import enes.plugin.moderation.utils.Reports;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class Report implements CommandExecutor {
    private final Reports reports;
    private final Players players;

    public Report(Database database) {
        this.reports = Reports.getInstance(database);
        this.players = Players.getInstance(database);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length < 2) {
                commandSender.sendMessage("§cError! Usage: /report <player> <reason>");
                return true;
            }

            if (Bukkit.getPlayer(strings[0]) == null) {
                commandSender.sendMessage("§cError! Usage: /report <player> <reason>");
                return true;
            }

            if (Objects.equals(strings[0], commandSender.getName())) {
                commandSender.sendMessage("§cError! You can't report yourself.");
                return true;
            }

            StringBuilder reason = new StringBuilder();
            for (String part : strings) {
                if (!Objects.equals(part, strings[0])) {
                    reason.append(part).append(" ");
                }
            }

            reports.add(commandSender.getName(), strings[0], reason.toString());
            players.reported(strings[0]);

            String reportCount = players.reportCount(strings[0]);

            List<String> mods = Data.moderator.getAll();
            for (String name : mods) {
                Player player = Bukkit.getPlayer(name);
                if (player != null) {
                    player.sendMessage(
                            "§f§l---- New Report -------------\n" +
                                    "§2Player reported: §3§l" + strings[0] + "\n" +
                                    "§2By: §b§l" + commandSender.getName() + "\n" +
                                    "§2Reason: §9§l" + reason + "\n" +
                                    "§2Total reports: §c§l" + reportCount + "\n" +
                                    "§f§l------------------------------"
                    );
                }
            }

            commandSender.sendMessage("§2Your report has been sent to the moderators. Thank you!");
            return true;
        }
        return false;
    }
}