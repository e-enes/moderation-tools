package gg.enes.moderation.commands;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.BanDAO;
import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.entities.BanEntity;
import gg.enes.moderation.entities.PlayerEntity;
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

public class UnbanCommand implements CommandExecutor {
    private final BanDAO banDAO = BanDAO.getInstance();
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();
    private final Logger logger = Main.getInstance().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("moderator.tools.unban")) {
            player.sendMessage(ChatColor.RED + "Error! You do not have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Error! Usage: /unban <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Error! Player not found.");
            return true;
        }

        if (target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Error! This player is not banned.");
            return true;
        }

        PlayerEntity playerEntity = getPlayerFromDatabase(target.getUniqueId());
        if (playerEntity == null) {
            player.sendMessage(ChatColor.RED + "Error! This player does not exist in the database.");
            return true;
        }

        if (!playerEntity.getBanned()) {
            player.sendMessage(ChatColor.RED + "Error! This player is not banned.");
            return true;
        }

        BanEntity banEntity = getActiveBanFromDatabase(target.getUniqueId());
        if (banEntity == null) {
            player.sendMessage(ChatColor.RED + "Error! The ban of this player does not exist in the database.");
            return true;
        }

        playerEntity.setBanned(false);
        banEntity.setActive(false);

        try {
            playerDAO.update(playerEntity);
            banDAO.update(banEntity);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Unable to update database.");
            logger.log(Level.WARNING, e.getMessage());

            player.sendMessage(ChatColor.RED + "Error! Unable to update database.");
            return true;
        }

        player.sendMessage(ChatColor.GREEN + "This player is no longer banned.");
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

    private BanEntity getActiveBanFromDatabase(UUID targetUUID) {
        try {
            return banDAO.getActiveBanByPlayerUuid(targetUUID);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find ban!");
            logger.log(Level.WARNING, e.getMessage());
            return null;
        }
    }
}
