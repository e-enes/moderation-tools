package gg.enes.moderation.commands;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.MuteDAO;
import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.entities.BanEntity;
import gg.enes.moderation.entities.MuteEntity;
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

public class MuteCommand implements CommandExecutor {
    private final MuteDAO muteDAO = MuteDAO.getInstance();
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();
    private static final Logger logger = Main.getInstance().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("moderator.tools.mute")) {
            player.sendMessage(ChatColor.RED + "Error! You do not have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Error! Usage: /mute <check | [player]> <player | [time in hours]> <[reason]>");
            return true;
        }

        String attr = args[0];
        if (attr.equalsIgnoreCase("check")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Error! Usage: /mute check <player>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Error! Player not found.");
                return true;
            }

            UUID targetUUID = target.getUniqueId();
            List<MuteEntity> muteEntities = target.isOnline()
                    ? CacheUtil.getMuteEntities(targetUUID)
                    : getMutesFromDatabase(targetUUID);

            if (muteEntities != null) {
                Optional<MuteEntity> latestMuteOpt = muteEntities.stream()
                        .max(Comparator.comparing(MuteEntity::getCreatedAt));

                MuteEntity latestMute = latestMuteOpt.get();
                String mutesInfo = getMuteInfo(target.getName(), targetUUID, muteEntities.size(), latestMute);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', mutesInfo));
            } else {
                player.sendMessage(ChatColor.RED + "Thus player has no mute.");
            }

            return true;

        }

        Player target = Bukkit.getPlayer(attr);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Error! Player not found.");
            return true;
        }

        if (Objects.equals(player.getName(), target.getName())) {
            player.sendMessage(ChatColor.RED + "Error! You can't mute yourself.");
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
            if (playerEntity.getMuted()) {
                player.sendMessage(ChatColor.RED + "This player is already muted!");
                return true;
            }

            if (playerEntity.getBanned()) {
                player.sendMessage(ChatColor.RED + "This player is banned!");
                return true;
            }

            playerEntity.setMuted(true);
            MuteEntity muteEntity = new MuteEntity(targetUUID, player.getUniqueId(), reason.toString(), time, new Timestamp(System.currentTimeMillis()), true);

            try {
                playerDAO.update(playerEntity);
                muteDAO.create(muteEntity);
                CacheUtil.savePlayerEntity(playerEntity);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Unable to update database.");
                logger.log(Level.WARNING, e.getMessage());

                player.sendMessage(ChatColor.RED + "Error! Unable to update database.");
                return true;
            }

            target.sendMessage(ChatColor.RED + "You were silenced for" + ChatColor.GRAY + args[1] + ChatColor.RED + "hours for" + ChatColor.WHITE + reason);
            player.sendMessage(ChatColor.GREEN + "This player has been silenced");
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

    private List<MuteEntity> getMutesFromDatabase(UUID targetUUID) {
        try {
            return muteDAO.getPlayerMutes(targetUUID);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find mutes!");
            logger.log(Level.WARNING, e.getMessage());
        }

        return List.of();
    }

    private String getMuteInfo(String playerName, UUID playerUUID, int totalBans, MuteEntity latestMute) {
        return "&f&l---- Mute Check -------------\n" +
                "&2Player: &3" + playerName + "\n" +
                "&2UUID: &b" + playerUUID + "\n" +
                "&2Total Bans: &c" + totalBans + "\n" +
                "&2Last Ban: &6" + RelativeTime.getPast(latestMute.getCreatedAt()) + "\n" +
                "&2By: &3" + Objects.requireNonNull(Bukkit.getPlayer(latestMute.getModeratorUuid())).getName() + "\n" +
                "&2For: &b" + latestMute.getReason() + "\n" +
                "&2Active: &c" + latestMute.getActive() + "\n" +
                "&2End: &6" + (latestMute.getActive() ? RelativeTime.getFuture(latestMute.getEnd()) : "Inactive") + "\n" +
                "&f&l------------------------------";
    }
}
