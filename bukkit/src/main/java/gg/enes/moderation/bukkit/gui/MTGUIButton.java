package gg.enes.moderation.bukkit.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class MTGUIButton {
    /**
     * The item stack of the button.
     */
    private final ItemStack itemStack;

    /**
     * Creates a new button.
     *
     * @param newItemStack the item stack
     */
    public MTGUIButton(final ItemStack newItemStack) {
        this.itemStack = newItemStack;
    }

    /**
     * Gets the item stack of the button.
     *
     * @return the item stack
     */
    public ItemStack getItem() {
        return itemStack;
    }

    /**
     * Called when the button is clicked.
     *
     * @param event the event
     */
    protected abstract void onClick(InventoryClickEvent event);
}
