package gg.enes.moderation.commands;

import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.ui.FrozenUI;
import gg.enes.moderation.util.CacheUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FreezeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission("moderator.tools.freeze")) {
            player.sendMessage(ChatColor.RED + "Error! You do not have permission to use this command");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Error! Usage: /freeze <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Error! Player not found.");
            return true;
        }

        if (!target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Error! Player not found (online).");
            return true;
        }

        PlayerEntity playerEntity = CacheUtil.getPlayerEntity(target.getUniqueId());

        if (playerEntity.getFrozen()) {
            exitFreezeMode(playerEntity, target);
            player.sendMessage(ChatColor.YELLOW + "This player is no longer frozen.");
        } else {
            enterFreezeMode(playerEntity, target, player.getUniqueId());
            player.sendMessage(ChatColor.YELLOW + "This player has been frozen.");
        }

        return true;
    }

    private void exitFreezeMode(PlayerEntity playerEntity, Player target) {
        playerEntity.setFrozen(false);
        playerEntity.setFrozenByModeratorUuid(null);
        CacheUtil.savePlayerEntity(playerEntity);

        target.setGameMode(GameMode.SURVIVAL);
        target.closeInventory();
    }

    private void enterFreezeMode(PlayerEntity playerEntity, Player target, UUID moderatorUUID) {
        playerEntity.setFrozen(true);
        playerEntity.setFrozenByModeratorUuid(moderatorUUID);
        CacheUtil.savePlayerEntity(playerEntity);

        target.setGameMode(GameMode.ADVENTURE);
        FrozenUI.set(target);
    }
}
