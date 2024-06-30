package gg.enes.moderation.bukkit.commands;

import gg.enes.moderation.bukkit.gui.ReportGUI;
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
        new ReportGUI((Player) sender).open((Player) sender);
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
