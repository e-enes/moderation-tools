package gg.enes.moderation.bukkit.commands;

import gg.enes.moderation.bukkit.ModerationTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import static gg.enes.moderation.bukkit.ModerationLanguage.getMessage;

import java.util.List;

public abstract class BaseCommand implements CommandExecutor {
    /**
     * The plugin command.
     */
    private final PluginCommand pluginCommand;

    /**
     * Creates a new command.
     *
     * @param name The name of the command.
     */
    public BaseCommand(final String name) {
        this.pluginCommand = ModerationTools.getInstance().getCommand(name);

        if (this.pluginCommand == null) {
            throw new IllegalArgumentException("Command not found: " + name);
        }

        this.pluginCommand.setTabCompleter(this::tabCompleter);
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final @NonNull Command command, final @NonNull String label, final String[] args) {
        if (isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(getMessage("message.command.player_only"));
            return true;
        }

        if (getPermission() != null && !sender.hasPermission(getPermission())) {
            sender.sendMessage(getMessage("message.command.no_permission"));
            return true;
        }

        if (args.length < getMinArgs()) {
            sender.sendMessage(getMessage("message.command.usage", pluginCommand.getUsage()));
            return true;
        }

        execute(sender, args);
        return true;
    }

    /**
     * Retrieves the plugin command.
     *
     * @return The plugin command.
     */
    public PluginCommand getPluginCommand() {
        return pluginCommand;
    }

    /**
     * Retrieves the permission of the command.
     *
     * @return The permission of the command.
     */
    protected @Nullable String getPermission() {
        return null;
    }

    protected abstract void execute(CommandSender sender, String[] args);
    protected abstract int getMinArgs();
    protected abstract boolean isPlayerOnly();
    protected abstract List<String> tabCompleter(CommandSender commandSender, Command command, String s, String[] strings);
}
