package gg.enes.moderation.commands;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.ReportDAO;
import gg.enes.moderation.entities.ReportEntity;
import gg.enes.moderation.ui.ReportUI;
import gg.enes.moderation.util.CacheUtil;
import gg.enes.moderation.util.RelativeTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportCommand implements CommandExecutor {
    private final ReportDAO reportDAO = ReportDAO.getInstance();
    private static final Logger logger = Main.getInstance().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Error! Usage: /report <check | [player]> <player>");
            return true;
        }

        String attr = args[0];
        if (attr.equalsIgnoreCase("check") && player.hasPermission("moderator.tools.report")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Error! Usage: /report check <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Error! Player not found.");
                return true;
            }

            UUID targetUUID = target.getUniqueId();
            List<ReportEntity> reportEntities = target.isOnline()
                    ? CacheUtil.getReportEntities(targetUUID)
                    : getReportsFromDatabase(targetUUID);

            if (reportEntities != null) {
                Optional<ReportEntity> latestReportOpt = reportEntities.stream()
                        .max(Comparator.comparing(ReportEntity::getCreatedAt));

                ReportEntity latestReport = latestReportOpt.get();
                String reportsInfo = getReportInfo(target.getName(), targetUUID, reportEntities.size(), latestReport);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', reportsInfo));
            } else {
                player.sendMessage(ChatColor.RED + "This player has no report.");
            }

            return true;
        }

        Player target = Bukkit.getPlayer(attr);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Error! Player not found.");
            return true;
        }

        if (Objects.equals(player.getName(), target.getName())) {
            player.sendMessage(ChatColor.RED + "Error! You can't report yourself.");
            return true;
        }

        ReportUI.set(player, target.getName());
        return true;
    }

    private List<ReportEntity> getReportsFromDatabase(UUID targetUUID) {
        try {
            return reportDAO.getPlayerReports(targetUUID);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find reports!");
            logger.log(Level.WARNING, e.getMessage());
        }

        return List.of();
    }

    private String getReportInfo(String playerName, UUID playerUUID, int totalReports, ReportEntity latestReport) {
        return "&f&l---- Report Check -------------\n" +
                "&2Player: &3" + playerName + "\n" +
                "&2UUID: &b" + playerUUID + "\n" +
                "&2Total Reports: &c" + totalReports + "\n" +
                "&2Last Report: &6" + RelativeTime.getPast(latestReport.getCreatedAt()) + "\n" +
                "&2By: &3" + Objects.requireNonNull(Bukkit.getPlayer(latestReport.getReporterUuid())).getName() + "\n" +
                "&2For: &b" + latestReport.getReason() + "\n" +
                "&f&l------------------------------";
    }
}