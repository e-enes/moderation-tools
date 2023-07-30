package gg.enes.moderation.commands;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.PlayerDAO;
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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerCommand implements CommandExecutor {
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();
    private static final Logger logger = Main.getInstance().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("moderator.tools.player")) {
            player.sendMessage(ChatColor.RED + "Error! You do not have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Error! Usage: /player <check | purge> <player>");
            return true;
        }

        String attr = args[0];
        if (attr.equalsIgnoreCase("check")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Error! Player not found.");
                return true;
            }

            UUID targetUUID = target.getUniqueId();
            PlayerEntity playerEntity = target.isOnline()
                    ? CacheUtil.getPlayerEntity(targetUUID)
                    : getPlayerFromDatabase(targetUUID);

            if (playerEntity != null) {
                String playerInfo = getPlayerInfo(playerEntity);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', playerInfo));
            } else {
                player.sendMessage(ChatColor.RED + "Error! This player does not exist in the database.");
            }

            return true;
        }

        if (attr.equalsIgnoreCase("purge")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Error! Player not found.");
                return true;
            }

            CacheUtil.removePlayer(target.getUniqueId());
            target.kickPlayer(ChatColor.RED + "Your cache has been refreshed by an administrator.\nPlease log in again.");
            player.sendMessage(ChatColor.GREEN + "This player's cache has been reset!");
            return true;
        }

        player.sendMessage(ChatColor.RED + "Error! Usage: /player <check | purge> <player>");
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

    private String getPlayerInfo(PlayerEntity playerEntity) {
        return "&f&l---- Player Check -------------\n" +
                "&2Player: &3" + playerEntity.getName() + "\n" +
                "&2UUID: &b" + playerEntity.getUuid().toString() + "\n" +
                "&2Muted: &c" + playerEntity.getMuted() + "\n" +
                "&2Banned: &6" + playerEntity.getBanned() + "\n" +
                "&2Frozen: &3" + playerEntity.getFrozen() + "\n" +
                "&2Last Connection: &b" + RelativeTime.getPast(playerEntity.getLastConnection()) + "\n" +
                "&2First Joined: &c" + RelativeTime.getPast(playerEntity.getCreatedAt()) + "\n" +
                "&2IP: &6" + playerEntity.getIp() + "\n" +
                "&f&l------------------------------";
    }
}