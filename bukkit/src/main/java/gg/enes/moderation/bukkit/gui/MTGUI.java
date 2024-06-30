package gg.enes.moderation.bukkit.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class MTGUI implements MTGUIHandler {
    /**
     * The inventory.
     */
    private final Inventory inventory;

    /**
     * The buttons.
     */
    private final Map<Integer, MTGUIButton> buttons = new HashMap<>();

    /**
     * Constructs a new MTGUI.
     */
    public MTGUI() {
        this.inventory = this.createInventory();
    }

    /**
     * Gets the inventory.
     *
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Sets a button.
     *
     * @param slot the slot
     * @param button the button
     */
    public void setButton(final int slot, final MTGUIButton button) {
        buttons.put(slot, button);
    }

    /**
     * Decorates the inventory.
     */
    public void decorate() {
        this.buttons.forEach((slot, button) -> {
            this.inventory.setItem(slot, button.getItem());
        });
    }

    @Override
    public final void onOpen(final InventoryOpenEvent event) {
        this.decorate();
    }

    @Override
    public final void onClose(final InventoryCloseEvent event) {
    }

    @Override
    public final void onClick(final InventoryClickEvent event) {
        event.setCancelled(true);
        final MTGUIButton button = this.buttons.get(event.getSlot());

        if (button != null) {
            button.onClick(event);
        }
    }

    protected abstract Inventory createInventory();
}
