package gg.enes.moderation.bukkit.gui;

import gg.enes.moderation.bukkit.ModerationLanguage;
import gg.enes.moderation.bukkit.enums.ReportType;
import gg.enes.moderation.bukkit.utils.ModeratorUtil;
import gg.enes.moderation.core.entity.Report;
import gg.enes.moderation.core.repository.ReportRepository;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class ReportConfirmGUI extends BaseGUI {
    /**
     * The player that is being reported.
     */
    private final Player target;

    /**
     * The type of the report.
     */
    private final ReportType reportType;

    /**
     * Creates a new report confirm GUI.
     *
     * @param newTarget the player that is being reported
     * @param newReportType the type of the report
     */
    public ReportConfirmGUI(final Player newTarget, final ReportType newReportType) {
        this.target = newTarget;
        this.reportType = newReportType;

        decorate();
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9 * 3, ModerationLanguage.getMessage("gui.report.confirm.title"));
    }

    @Override
    public void decorate() {
        ItemStack submit = createItem(Material.LIME_WOOL, ModerationLanguage.getMessage("gui.report.confirm.button.submit"), List.of());
        setItem(11, submit, event -> {
            Report report = new Report()
                    .setServer(Bukkit.getServer().getName() + "\\" + Bukkit.getServer().getBukkitVersion() + " (" + Bukkit.getServer().getVersion() + ")")
                    .setReason(this.reportType.toString())
                    .setReported(this.target.getUniqueId())
                    .setReporter(event.getWhoClicked().getUniqueId());
            ReportRepository.getInstance().create(report);
            ModeratorUtil.notify(target.getUniqueId(), reportType);

            event.getWhoClicked().sendMessage(ModerationLanguage.getMessage("message.gui.report.submitted", reportType.toString()));
            event.getWhoClicked().closeInventory();
        });

        ItemStack playerHead = createPlayerHead(this.target.getUniqueId(), ModerationLanguage.getMessage("gui.report.confirm.button.head.title", this.target.getName()), ModerationLanguage.getMessages("gui.report.confirm.button.head.lore", this.target.getName(), reportType.toString()));
        setItem(13, playerHead, event -> { });

        ItemStack cancel = createItem(Material.RED_WOOL, ModerationLanguage.getMessage("gui.report.confirm.button.cancel"), List.of());
        setItem(15, cancel, event -> {
            event.getWhoClicked().sendMessage(ModerationLanguage.getMessage("message.gui.report.cancelled"));
            event.getWhoClicked().closeInventory();
        });
    }
}
