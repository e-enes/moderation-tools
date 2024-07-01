package gg.enes.moderation.bukkit.gui;

import gg.enes.moderation.bukkit.ModerationTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class BaseGUI implements Listener {
    /**
     * The inventory.
     */
    private final Inventory inventory;

    /**
     * The buttons.
     */
    private final Map<Integer, ClickHandler> clickHandlers = new HashMap<>();

    /**
     * Constructs a new GUI.
     */
    public BaseGUI() {
        this.inventory = createInventory();
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
     * Opens the inventory.
     *
     * @param player the player to open the inventory for
     */
    public void open(final Player player) {
        player.openInventory(this.inventory);
        Bukkit.getPluginManager().registerEvents(this, ModerationTools.getInstance());
    }

    /**
     * Close the inventory.
     *
     * @param player the player to close the inventory for
     */
    public void close(final Player player) {
        player.closeInventory();
    }

    /**
     * Close Handler.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public final void onClose(final InventoryCloseEvent event) {
        if (event.getInventory().equals(this.inventory)) {
            HandlerList.unregisterAll(this);
        }
    }

    /**
     * CLick Handler.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public final void onClick(final InventoryClickEvent event) {
        if (event.getInventory().equals(this.inventory)) {
            event.setCancelled(true);

            ClickHandler clickHandler = this.clickHandlers.get(event.getSlot());
            if (clickHandler != null) {
                clickHandler.onClick(event);
            }
        }
    }

    protected final ItemStack createPlayerHead(final UUID playerUuid, final String name, final List<String> lore) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();

        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUuid));
        meta.setDisplayName(name);
        meta.setLore(lore);
        playerHead.setItemMeta(meta);

        return playerHead;
    }

    protected final ItemStack createItem(final Material material, final String name, final @Nullable List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    protected final void setItem(final int slot, final ItemStack item, final ClickHandler clickHandler) {
        this.inventory.setItem(slot, item);
        this.clickHandlers.put(slot, clickHandler);
    }

    protected abstract Inventory createInventory();
    protected abstract void decorate();

    @FunctionalInterface
    public interface ClickHandler {
        /**
         * Handles the click event.
         *
         * @param event the event
         */
        void onClick(InventoryClickEvent event);
    }
}
