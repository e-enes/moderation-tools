package gg.enes.moderation.bukkit.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface MTGUIHandler {
    /**
     * Called when the inventory is opened.
     *
     * @param event the event
     */
    void onOpen(InventoryOpenEvent event);

    /**
     * Called when the inventory is closed.
     *
     * @param event the event
     */
    void onClose(InventoryCloseEvent event);

    /**
     * Called when an item is clicked.
     *
     * @param event the event
     */
    void onClick(InventoryClickEvent event);
}
