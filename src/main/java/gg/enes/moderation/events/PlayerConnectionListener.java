package gg.enes.moderation.events;

import gg.enes.moderation.Main;
import gg.enes.moderation.dao.BanDAO;
import gg.enes.moderation.dao.MuteDAO;
import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.dao.ReportDAO;
import gg.enes.moderation.entities.BanEntity;
import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.util.CacheUtil;
import gg.enes.moderation.util.IPUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerConnectionListener implements Listener {
    private final PlayerDAO playerDAO = PlayerDAO.getInstance();
    private final BanDAO banDAO = BanDAO.getInstance();
    private final MuteDAO muteDAO = MuteDAO.getInstance();
    private final ReportDAO reportDAO = ReportDAO.getInstance();
    private final Logger logger = Main.getInstance().getLogger();
    private PlayerEntity playerEntity;

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        try {
            playerEntity = playerDAO.getByUuid(player.getUniqueId());

            if (playerEntity == null || !playerEntity.getBanned()) {
                event.allow();
                return;
            }

            BanEntity banEntity = banDAO.getActiveBanByPlayerUuid(playerEntity.getUuid());
            String reason = banEntity.getReason();
            Date timeLeft = new Date(banEntity.getEnd().getTime());

            if (System.currentTimeMillis() > timeLeft.getTime()) {
                event.allow();

                logger.log(Level.INFO, "Player " + player.getName() + " ban has expired.");

                playerEntity.setBanned(false);
                banEntity.setActive(false);
                banDAO.update(banEntity);
                playerDAO.update(playerEntity);

                return;
            }

            event.setKickMessage(ChatColor.RED + "You are still banned for " + reason + ".\nYou will be unbanned in " + timeLeft + ".\n\n" + ChatColor.GRAY + "If you believe this is a mistake, please contact the administration.\nYour ID is: " + playerEntity.getUuid());
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);

            logger.log(Level.INFO, "Player " + player.getName() + " tried to log in, but is still banned for " + reason + ". Unban in " + timeLeft);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL exception occurred while checking player ban status");
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        String ip = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();

        if (!player.hasPermission("moderation.tools.vpn")) {
            switch (IPUtil.check(ip)) {
                case "mobile" ->
                        player.kickPlayer(ChatColor.RED + "You are not allowed to connect using a mobile network.\n\n" + ChatColor.GRAY + "If this problem persists, please contact the administration.\nYour ID is: " + uuid);
                case "proxy" ->
                        player.kickPlayer(ChatColor.RED + "You are not allowed to connect using a proxy.\n\n" + ChatColor.GRAY + "If this problem persists, please contact the administration.\nYour ID is: " + uuid);
                case "hosting" ->
                        player.kickPlayer(ChatColor.RED + "You are not allowed to connect from a hosting provider.\n\n" + ChatColor.GRAY + "If this problem persists, please contact the administration.\nYour ID is: " + uuid);
                default -> {

                }
            }
        }

        if (playerEntity == null) {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            playerEntity = new PlayerEntity(player.getName(), uuid, ip, false, false, currentTimestamp, currentTimestamp);

            try {
                playerDAO.create(playerEntity);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "An SQL exception occurred while creating a new player entity");
                logger.log(Level.WARNING, e.getMessage());
            }

            CacheUtil.savePlayerEntity(playerEntity);
            logger.log(Level.INFO, "Player " + player.getName() + " joined the server successfully");
            playerEntity = null;

            return;
        }

        if (CacheUtil.getPlayerEntity(playerEntity.getUuid()) != null) {
            player.kickPlayer(ChatColor.RED + "You are already connected to the server.\n\n" + ChatColor.GRAY + "If this problem persists, please contact the administration.\nYour ID is: " + uuid);
            logger.log(Level.INFO, "Player " + player.getName() + " tried to join the server, but is already connected");

            return;
        }

        if (!Objects.equals(playerEntity.getIp(), ip)) {
            playerEntity.setIp(ip);
        }

        if (!Objects.equals(playerEntity.getName(), player.getName())) {
            playerEntity.setName(player.getName());
        }

        playerEntity.setLastConnection(new Timestamp(System.currentTimeMillis()));

        try {
            playerDAO.update(playerEntity);
            CacheUtil.initPlayer(playerEntity, banDAO.getPlayerBans(uuid), reportDAO.getPlayerReports(uuid), muteDAO.getPlayerMutes(uuid));

            playerEntity = null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An error occurred while updating the player entity in the database.");
            logger.log(Level.WARNING, e.getMessage());
        }

        logger.log(Level.INFO, "Player " + player.getName() + " joined the server successfully");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        playerEntity = CacheUtil.getPlayerEntity(uuid);

        if (playerEntity == null) {
            return;
        }

        try {
            playerDAO.update(playerEntity);
            playerEntity = null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An SQL exception occurred while updating a player entity");
            logger.log(Level.WARNING, e.getMessage());
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> CacheUtil.removePlayer(uuid), 5 * 20);
    }
}