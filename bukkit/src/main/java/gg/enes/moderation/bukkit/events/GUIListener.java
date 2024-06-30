package gg.enes.moderation.bukkit.events;

import gg.enes.moderation.bukkit.gui.MTGUIManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GUIListener extends MTListener {
    /**
     * The GUI manager.
     */
    private final MTGUIManager guiManager = MTGUIManager.getInstance();

    /**
     * Called when the inventory is opened.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryOpen(final InventoryOpenEvent event) {
        this.guiManager.handleOpen(event);
    }

    /**
     * Called when the inventory is closed.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        this.guiManager.handleClose(event);
    }

    /**
     * Called when an item is clicked.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        this.guiManager.handleClick(event);
    }
}
