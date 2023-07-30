package gg.enes.moderation.commands;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.BanDAO;
import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.entities.BanEntity;
import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.util.CacheUtil;
import gg.enes.moderation.util.RelativeTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BanCommand implements CommandExecutor {
    private final BanDAO banDAO = BanDAO.getInstance();
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();
    private static final Logger logger = Main.getInstance().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("moderator.tools.ban")) {
            player.sendMessage(ChatColor.RED + "Error! You do not have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Error! Usage: /ban <check | [player]> <player | [time in hours]> <[reason]>");
            return true;
        }

        String attr = args[0];
        if (attr.equalsIgnoreCase("check")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Error! Usage: /ban check <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Error! Player not found.");
                return true;
            }

            UUID targetUUID = target.getUniqueId();
            List<BanEntity> banEntities = target.isOnline()
                    ? CacheUtil.getBanEntities(targetUUID)
                    : getBansFromDatabase(targetUUID);

            if (banEntities != null) {
                Optional<BanEntity> latestReportOpt = banEntities.stream()
                        .max(Comparator.comparing(BanEntity::getCreatedAt));

                BanEntity latestBan = latestReportOpt.get();
                String bansInfo = getBanInfo(target.getName(), targetUUID, banEntities.size(), latestBan);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', bansInfo));
            } else {
                player.sendMessage(ChatColor.RED + "This player has no ban.");
            }

            return true;

        }

        Player target = Bukkit.getPlayer(attr);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Error! Player not found.");
            return true;
        }

        if (Objects.equals(player.getName(), target.getName())) {
            player.sendMessage(ChatColor.RED + "Error! You can't ban yourself.");
            return true;
        }

        long time;
        try {
            time = Long.parseLong(args[1]) * 60 * 60 * 1000;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Error! Please specify a valid time in hours.");
            return true;
        }

        StringBuilder reason = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        if (reason.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Error! Please specify a valid reason.");
            return true;
        }

        UUID targetUUID = target.getUniqueId();
        PlayerEntity playerEntity = target.isOnline()
                ? CacheUtil.getPlayerEntity(targetUUID)
                : getPlayerFromDatabase(targetUUID);

        if (playerEntity != null) {
            if (playerEntity.getBanned()) {
                player.sendMessage(ChatColor.RED + "This player is already banned!");
                return true;
            }

            playerEntity.setBanned(true);
            BanEntity banEntity = new BanEntity(targetUUID, player.getUniqueId(), reason.toString(), time, new Timestamp(System.currentTimeMillis()), true);

            try {
                playerDAO.update(playerEntity);
                banDAO.create(banEntity);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Unable to update database.");
                logger.log(Level.WARNING, e.getMessage());

                player.sendMessage(ChatColor.RED + "Error! Unable to update database.");
                return true;
            }

            target.kickPlayer(ChatColor.RED + "You have been banned.");
            player.sendMessage(ChatColor.GREEN + "This player has been banned");
        } else {
            player.sendMessage(ChatColor.RED + "Error! This player does not exist in the database.");
        }

        return true;
    }

    private PlayerEntity getPlayerFromDatabase(UUID targetUUID) {
        try {
            return playerDAO.getByUuid(targetUUID);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find player!");
            logger.log(Level.WARNING, e.getMessage());
            return null;
        }
    }

    private List<BanEntity> getBansFromDatabase(UUID targetUUID) {
        try {
            return banDAO.getPlayerBans(targetUUID);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find bans!");
            logger.log(Level.WARNING, e.getMessage());
        }

        return List.of();
    }

    private String getBanInfo(String playerName, UUID playerUUID, int totalBans, BanEntity latestBan) {
        return "&f&l---- Ban Check -------------\n" +
                "&2Player: &3" + playerName + "\n" +
                "&2UUID: &b" + playerUUID + "\n" +
                "&2Total Bans: &c" + totalBans + "\n" +
                "&2Last Ban: &6" + RelativeTime.getPast(latestBan.getCreatedAt()) + "\n" +
                "&2By: &3" + Objects.requireNonNull(Bukkit.getPlayer(latestBan.getModeratorUuid())).getName() + "\n" +
                "&2For: &b" + latestBan.getReason() + "\n" +
                "&2Active: &c" + latestBan.getActive() + "\n" +
                "&2End: &6" + (latestBan.getActive() ? RelativeTime.getFuture(latestBan.getEnd()) : "Inactive") + "\n" +
                "&f&l------------------------------";
    }
}