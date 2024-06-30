package gg.enes.moderation.bukkit.commands.player;

import gg.enes.moderation.bukkit.commands.MTCommand;
import org.bukkit.command.CommandSender;

public final class ReportCommand extends MTCommand {
    /**
     * Creates a new report command.
     */
    public ReportCommand() {
        super("report");
    }

    @Override
    protected void execute(final CommandSender sender, final String[] args) {
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
