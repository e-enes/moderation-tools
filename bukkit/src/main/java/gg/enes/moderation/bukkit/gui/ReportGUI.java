package gg.enes.moderation.bukkit.gui;

import gg.enes.moderation.bukkit.ModerationLanguage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.UUID;

public final class ReportGUI extends BaseGUI {
    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates a new report GUI.
     *
     * @param newPlayer the player
     */
    public ReportGUI(final Player newPlayer) {
        this.player = newPlayer;
        decorate();
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9 * 6, ModerationLanguage.getMessage("gui.report.title"));
    }

    @Override
    public void decorate() {
        ItemStack playerHead = createPlayerHead(player.getUniqueId(), ModerationLanguage.getMessage("gui.report.button.head", player.getName()));
        setItem(4, playerHead, event -> { });

        ItemStack cheating = createItem(Material.DIAMOND_SWORD, ModerationLanguage.getMessage("gui.report.button.cheating.title"), ModerationLanguage.getMessages("gui.report.button.cheating.lore"));
        setItem(20, cheating, event -> { });
    }

    private ItemStack createPlayerHead(final UUID playerUuid, final String name, final String... lore) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();

        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUuid));
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        playerHead.setItemMeta(meta);

        return playerHead;
    }
}
