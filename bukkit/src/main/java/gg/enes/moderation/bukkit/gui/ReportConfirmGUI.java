package gg.enes.moderation.bukkit.gui;

import gg.enes.moderation.bukkit.enums.ReportConfirm;
import gg.enes.moderation.bukkit.enums.ReportType;
import gg.enes.moderation.bukkit.utils.ModeratorUtil;
import gg.enes.moderation.core.entity.Report;
import gg.enes.moderation.core.repository.ReportRepository;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import static gg.enes.moderation.bukkit.ModerationLanguage.getMessage;
import static gg.enes.moderation.bukkit.ModerationLanguage.getMessages;

import java.util.List;
import java.util.Map;

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
     * Defines the confirm buttons with their properties.
     */
    private static final Map<Integer, ReportConfirm> CONFIRM_BUTTONS = Map.of(
            11, ReportConfirm.SUBMIT,
            15, ReportConfirm.CANCEL
    );

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
        return Bukkit.createInventory(null, 9 * 3, getMessage("gui.report.confirm.title"));
    }

    @Override
    public void decorate() {
        ItemStack playerHead = createPlayerHead(this.target.getUniqueId(), getMessage("gui.report.confirm.button.head.title", this.target.getName()), getMessages("gui.report.confirm.button.head.lore", this.target.getName(), reportType.toString()));
        setItem(13, playerHead, event -> { });

        CONFIRM_BUTTONS.forEach((slot, properties) -> {
            Material material = properties.getMaterial();
            String key = properties.toString();

            ItemStack item = createItem(material, getMessage("gui.report.confirm.button." + key + ".title"), List.of());
            setItem(slot, item, event -> {
                if (key.equals(ReportConfirm.SUBMIT.toString())) {
                    handleSubmit((Player) event.getWhoClicked());
                } else {
                    handleCancel((Player) event.getWhoClicked());
                }
            });
        });
    }

    private void handleSubmit(final Player player) {
        Report report = new Report()
                .setServer(Bukkit.getServer().getName() + "\\" + Bukkit.getServer().getBukkitVersion() + " (" + Bukkit.getServer().getVersion() + ")")
                .setReason(this.reportType.toString())
                .setReported(this.target.getUniqueId())
                .setReporter(player.getUniqueId());
        ReportRepository.getInstance().create(report);
        ModeratorUtil.notify(target.getUniqueId(), reportType);

        player.sendMessage(getMessage("message.gui.report.submitted", reportType.toString()));
        player.closeInventory();
    }

    private void handleCancel(final Player player) {
        player.sendMessage(getMessage("message.gui.report.cancelled"));
        player.closeInventory();
    }
}
