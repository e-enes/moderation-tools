package gg.enes.moderation.bukkit.gui;

import gg.enes.moderation.bukkit.ModerationLanguage;
import gg.enes.moderation.bukkit.enums.ReportType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class ReportGUI extends BaseGUI {
    /**
     * The player to report.
     */
    private final Player target;

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
        return Bukkit.createInventory(null, 9 * 6, ModerationLanguage.getMessage("gui.report.title"));
    }

    @Override
    public void decorate() {
        ItemStack playerHead = createPlayerHead(this.target.getUniqueId(), ModerationLanguage.getMessage("gui.report.button.head", this.target.getName()), List.of());
        setItem(4, playerHead, event -> { });

        ItemStack cheating = createItem(Material.DIAMOND_SWORD, ModerationLanguage.getMessage("gui.report.button.cheating.title"), ModerationLanguage.getMessages("gui.report.button.cheating.lore"));
        setItem(20, cheating, event -> new ReportConfirmGUI(this.target, ReportType.CHEATING).open((Player) event.getWhoClicked()));
    }
}
