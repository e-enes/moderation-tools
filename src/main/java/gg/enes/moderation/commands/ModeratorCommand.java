package gg.enes.moderation.commands;

import gg.enes.moderation.Main;
import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.ui.ModeratorUI;
import gg.enes.moderation.util.CacheUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModeratorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("moderator.tools.moderator")) {
            player.sendMessage(ChatColor.RED + "Error! You do not have permission to use this command");
            return true;
        }

        PlayerEntity playerEntity = CacheUtil.getPlayerEntity(player.getUniqueId());

        if (playerEntity.getModeratorMode()) {
            exitModeratorMode(player, playerEntity);
        } else {
            enterModeratorMode(player, playerEntity);
        }

        return true;
    }

    private void exitModeratorMode(Player moderator, PlayerEntity playerEntity) {
        playerEntity.setModeratorMode(false);
        moderator.getInventory().setContents(playerEntity.getInventory());

        if (playerEntity.getVanished()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showPlayer(Main.getInstance(), moderator);
            }
        }

        moderator.setGameMode(GameMode.SURVIVAL);
        moderator.setAllowFlight(false);
        moderator.sendMessage(ChatColor.RED + "You have exited moderator mode.");
    }

    private void enterModeratorMode(Player moderator, PlayerEntity playerEntity) {
        playerEntity.setModeratorMode(true);
        playerEntity.setInventory(moderator.getInventory().getContents());

        ModeratorUI.set(moderator);

        moderator.setGameMode(GameMode.ADVENTURE);
        moderator.setAllowFlight(true);
        moderator.sendMessage(ChatColor.GREEN + "You have joined moderator mode.");
    }
}