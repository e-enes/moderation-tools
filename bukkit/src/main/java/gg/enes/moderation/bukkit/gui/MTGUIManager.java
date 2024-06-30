package gg.enes.moderation.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public final class MTGUIManager {
    /**
     * The GUIs.
     */
    private final Map<Inventory, MTGUIHandler> guis = new HashMap<>();

    /**
     * The instance.
     */
    private static MTGUIManager instance;

    private MTGUIManager() {
    }

    /**
     * Constructs a new MTGUIManager.
     *
     * @return the instance
     */
    public static MTGUIManager getInstance() {
        if (instance == null) {
            instance = new MTGUIManager();
        }

        return instance;
    }

    /**
     * Gets the GUI handler.
     *
     * @param inventory the inventory
     * @param handler the handler
     */
    public void registerGUI(final Inventory inventory, final MTGUIHandler handler) {
        this.guis.put(inventory, handler);
    }

    /**
     * Unregisters the GUI handler.
     *
     * @param inventory the inventory
     */
    public void unregisterGUI(final Inventory inventory) {
        this.guis.remove(inventory);
    }

    /**
     * Opens the GUI.
     *
     * @param gui the GUI
     * @param player the player
     */
    public void openGUI(final MTGUI gui, final Player player) {
        this.registerGUI(gui.getInventory(), gui);
        player.openInventory(gui.getInventory());
    }

    /**
     * Called when the inventory is clicked.
     *
     * @param event the event
     */
    public void handleOpen(final InventoryOpenEvent event) {
        final MTGUIHandler handler = this.guis.get(event.getInventory());

        if (handler != null) {
            handler.onOpen(event);
        }
    }

    /**
     * Called when the inventory is clicked.
     *
     * @param event the event
     */
    public void handleClose(final InventoryCloseEvent event) {
        final MTGUIHandler handler = this.guis.get(event.getInventory());

        if (handler != null) {
            handler.onClose(event);
            this.unregisterGUI(event.getInventory());
        }
    }

    /**
     * Called when the inventory is clicked.
     *
     * @param event the event
     */
    public void handleClick(final InventoryClickEvent event) {
        final MTGUIHandler handler = this.guis.get(event.getInventory());

        if (handler != null) {
            handler.onClick(event);
        }
    }
}
