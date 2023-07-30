package gg.enes.moderation.events;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.ReportDAO;
import gg.enes.moderation.entities.ReportEntity;
import gg.enes.moderation.util.CacheUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportInventoryListener implements Listener {
    private final ReportDAO reportDAO = ReportDAO.getInstance();
    private final Logger logger = Main.getInstance().getLogger();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();

        if (humanEntity instanceof Player player) {
            if (player.getOpenInventory().getTitle().contains("Report")) {
                event.setCancelled(true);

                ItemStack item = event.getCurrentItem();
                if (item == null) return;

                ReportEntity reportEntity = new ReportEntity(player.getUniqueId(), Objects.requireNonNull(Bukkit.getPlayer(Objects.requireNonNull(Objects.requireNonNull(event.getInventory().getItem(8)).getItemMeta()).getDisplayName())).getUniqueId(), ChatColor.stripColor(Objects.requireNonNull(item.getItemMeta()).getDisplayName()), new Timestamp(System.currentTimeMillis()));

                try {
                    reportDAO.create(reportEntity);

                    CacheUtil.saveReportEntity(reportEntity);
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "An error occurred while reporting player " + player.getName() + " for freeze.");
                    logger.log(Level.WARNING, e.getMessage());
                }

                player.closeInventory();
                player.sendMessage(ChatColor.GREEN + "Your report has been recorded! There is no need to report this player again.");
            }
        }
    }
}
