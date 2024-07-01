package gg.enes.moderation.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ReportCommand extends BaseCommand {
    /**
     * Creates a new report command.
     */
    public ReportCommand() {
        super("report");
    }

    @Override
    protected void execute(final CommandSender sender, final String[] args) {
        Player player = (Player) sender;
    }

    @Override
    protected String getPermission() {
        return null;
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }
}
