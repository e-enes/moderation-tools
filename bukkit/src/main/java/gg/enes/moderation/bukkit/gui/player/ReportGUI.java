package gg.enes.moderation.bukkit.gui.player;

import gg.enes.moderation.bukkit.gui.MTGUI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public final class ReportGUI extends MTGUI {
    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9, "Report");
    }
}
