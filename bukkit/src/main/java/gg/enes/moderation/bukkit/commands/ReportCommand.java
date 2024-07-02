package gg.enes.moderation.bukkit.commands;

import gg.enes.moderation.bukkit.gui.ReportGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static gg.enes.moderation.bukkit.ModerationLanguage.getMessage;

import java.util.Arrays;
import java.util.List;

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
        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(getMessage("message.command.player_not_found"));
            return;
        }

        new ReportGUI(target).open(player);
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }

    @Override
    protected int getMinArgs() {
        return 1;
    }

    @Override
    public List<String> tabCompleter(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (args.length > 1 || !(sender instanceof Player)) {
            return null;
        }

        return Arrays.stream(Bukkit.getServer().getOfflinePlayers()).map(OfflinePlayer::getName).filter(offPlayer -> offPlayer != null && offPlayer.toLowerCase().startsWith(args[0].toLowerCase())).toList();
    }
}
