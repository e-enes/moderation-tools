package gg.enes.moderation;

import gg.enes.moderation.commands.*;
import gg.enes.moderation.dao.BanDAO;
import gg.enes.moderation.dao.MuteDAO;
import gg.enes.moderation.dao.PlayerDAO;
import gg.enes.moderation.dao.ReportDAO;
import gg.enes.moderation.entities.PlayerEntity;
import gg.enes.moderation.events.*;
import gg.enes.moderation.util.CacheUtil;
import gg.enes.moderation.util.DatabaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    private static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Initializing plugin...");

        File dbFolder = new File(getDataFolder(), "db");

        if (!dbFolder.exists()) {
            boolean folderCreated = dbFolder.mkdirs();

            if (folderCreated) {
                getLogger().log(Level.INFO, "Data folder did not exist. Created new data folder.");
            } else {
                getLogger().log(Level.SEVERE, "Failed to create data folder!");
                setEnabled(false);
                return;
            }
        }

        try {
            DatabaseUtil.setURL("jdbc:sqlite:" + getDataFolder() + File.separator + "db" + File.separator + "database.db");
            getLogger().log(Level.INFO, "Successfully connected to the SQLite database.");
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Failed to create the SQLite database!");
            getLogger().log(Level.WARNING, e.getMessage());
            setEnabled(false);
            return;
        }

        try {
            CacheUtil.init(PlayerDAO.getInstance().read(), BanDAO.getInstance().read(), ReportDAO.getInstance().read(), MuteDAO.getInstance().read());

            for (PlayerEntity playerEntity : CacheUtil.getPlayerEntities()) {
                UUID playerUuid = playerEntity.getUuid();

                if (Bukkit.getPlayer(playerUuid) == null) {
                    CacheUtil.removePlayer(playerUuid);
                } else {
                    boolean onlinePlayer = Bukkit.getPlayer(playerUuid).isOnline();

                    if (!onlinePlayer) {
                        CacheUtil.removePlayer(playerUuid);
                    }
                }
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (CacheUtil.getPlayerEntity(player.getUniqueId()) == null) {
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    PlayerEntity playerEntity = new PlayerEntity(player.getName(), player.getUniqueId(), player.getAddress().getAddress().getHostAddress(), false, false, currentTime, currentTime);

                    PlayerDAO.getInstance().create(playerEntity);
                    CacheUtil.savePlayerEntity(playerEntity);
                }
            }

            getLogger().log(Level.INFO, "Cache initialized successfully.");
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "An error occurred while initializing the cache.");
            getLogger().log(Level.WARNING, e.getMessage());
            setEnabled(false);
            return;
        }

        instance = this;

        Objects.requireNonNull(getCommand("report")).setExecutor(new ReportCommand());
        Objects.requireNonNull(getCommand("moderator")).setExecutor(new ModeratorCommand());
        Objects.requireNonNull(getCommand("player")).setExecutor(new PlayerCommand());
        Objects.requireNonNull(getCommand("mute")).setExecutor(new MuteCommand());
        Objects.requireNonNull(getCommand("freeze")).setExecutor(new FreezeCommand());
        Objects.requireNonNull(getCommand("kick")).setExecutor(new KickCommand());
        Objects.requireNonNull(getCommand("unban")).setExecutor(new UnbanCommand());
        Objects.requireNonNull(getCommand("unmute")).setExecutor(new UnmuteCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ModeratorModeListener(), this);
        pluginManager.registerEvents(new ModeratorQuitListener(), this);
        pluginManager.registerEvents(new PlayerFreezeListener(), this);
        pluginManager.registerEvents(new PlayerConnectionListener(), this);
        pluginManager.registerEvents(new ReportInventoryListener(), this);

        getLogger().log(Level.INFO, "Plugin initialized successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.WARNING, "Disabling plugin...");

        CacheUtil.getPlayerEntities().forEach(playerEntity -> {
            if (playerEntity.getModeratorMode()) {
                Player player = Objects.requireNonNull(Bukkit.getPlayer(playerEntity.getUuid()));
                player.sendMessage(ChatColor.RED + "ModerationTools has been disable.");
                player.getInventory().setContents(playerEntity.getInventory());

                playerEntity.setModeratorMode(false);
                playerEntity.setInventory(null);
            }
        });

        getLogger().log(Level.WARNING, "Plugin disabled.");
    }
}