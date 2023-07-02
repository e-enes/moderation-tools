package gg.enes.moderation.commands;

import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.dao.ReportDAO;
import gg.enes.moderation.storage.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class Report implements CommandExecutor {
    private final ReportDAO reportDAO = ReportDAO.getInstance();
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length < 2) {
                player.sendMessage("§cError! Usage: /report <player> <reason>");
                return true;
            }

            if (Bukkit.getPlayer(strings[0]) == null) {
                player.sendMessage("§cError! Usage: /report <player> <reason>");
                return true;
            }

            if (Objects.equals(strings[0], player.getName())) {
                player.sendMessage("§cError! You can't report yourself.");
                return true;
            }

            StringBuilder reason = new StringBuilder();
            for (String part : strings) {
                if (!Objects.equals(part, strings[0])) {
                    reason.append(part).append(" ");
                }
            }

            reportDAO.add(player.getName(), strings[0], reason.toString());
            playerDAO.reported(strings[0]);

            String reportCount = playerDAO.reportCount(strings[0]);

            List<String> mods = CacheManager.moderator.getAll();
            for (String name : mods) {
                Player moderator = Bukkit.getPlayer(name);
                if (moderator != null) {
                    moderator.sendMessage(
                            "§f§l---- New Report -------------\n" +
                                    "§2Player reported: §3§l" + strings[0] + "\n" +
                                    "§2By: §b§l" + player.getName() + "\n" +
                                    "§2Reason: §9§l" + reason + "\n" +
                                    "§2Total reports: §c§l" + reportCount + "\n" +
                                    "§f§l------------------------------"
                    );
                }
            }

            player.sendMessage("§2Your report has been sent to the moderators. Thank you!");
            return true;
        }

        return false;
    }
}