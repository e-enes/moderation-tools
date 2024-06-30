package gg.enes.moderation.bukkit.commands;

import gg.enes.moderation.bukkit.ModerationLanguage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class MTCommand implements CommandExecutor {
    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(ModerationLanguage.getMessage("no_permission"));
            return true;
        }

        execute(sender, args);
        return true;
    }

    protected abstract void execute(CommandSender sender, String[] args);
    protected abstract String getPermission();
}
