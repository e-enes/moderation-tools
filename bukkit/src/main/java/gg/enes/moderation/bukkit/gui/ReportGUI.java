package gg.enes.moderation.bukkit.gui;

import gg.enes.moderation.bukkit.enums.ReportType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import static gg.enes.moderation.bukkit.ModerationLanguage.getMessage;
import static gg.enes.moderation.bukkit.ModerationLanguage.getMessages;

import java.util.List;
import java.util.Map;

public final class ReportGUI extends BaseGUI {
    /**
     * The player to report.
     */
    private final Player target;

    /**
     * Defines the report buttons with their properties.
     */
    private static final Map<Integer, ReportType> REPORT_BUTTONS = Map.of(
            20, ReportType.CHEATING,
            22, ReportType.BAD_NAME,
            24, ReportType.BAD_SKIN,
            30, ReportType.CROSS_TEAMING,
            32, ReportType.MESSAGE
    );

    /**
     * Creates a new report GUI.
     *
     * @param newTarget the player to report
     */
    public ReportGUI(final Player newTarget) {
        this.target = newTarget;
        decorate();
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9 * 6, getMessage("gui.report.title"));
    }

    @Override
    public void decorate() {
        ItemStack playerHead = createPlayerHead(this.target.getUniqueId(), getMessage("gui.report.button.head", this.target.getName()), List.of());
        setItem(4, playerHead, event -> { });

        // Add report buttons to the GUI
        REPORT_BUTTONS.forEach((slot, properties) -> {
            Material material = properties.getMaterial();
            String key = properties.getKey();

            ItemStack item = createItem(material, getMessage("gui.report.button." + key + ".title"), getMessages("gui.report.button." + key + ".lore"));
            if (!properties.equals(ReportType.MESSAGE)) {
                setItem(slot, item, event -> new ReportConfirmGUI(this.target, properties).open((Player) event.getWhoClicked()));
            } else {
                setItem(slot, item, event -> new ReportMessageGUI(this.target).open((Player) event.getWhoClicked()));
            }
        });
    }
}