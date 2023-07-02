package gg.enes.moderation;

import gg.enes.moderation.commands.Moderator;
import gg.enes.moderation.commands.PlayerCheck;
import gg.enes.moderation.commands.Report;
import gg.enes.moderation.commands.ReportCheck;
import gg.enes.moderation.events.ModeratorsEnable;
import gg.enes.moderation.events.ModeratorsJoin;
import gg.enes.moderation.events.PlayersFreeze;
import gg.enes.moderation.events.PlayersJoin;
import gg.enes.moderation.storage.DatabaseUtil;
import gg.enes.moderation.storage.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Main extends JavaPlugin {
    private static Plugin instance;

    @Override
    public void onEnable() {
        try {
            DatabaseUtil.setFilename(getDataFolder() + File.separator + "database.db");
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Failed to create databases!");
            setEnabled(false);
            return;
        }

        instance = this;

        Objects.requireNonNull(getCommand("report")).setExecutor(new Report());
        Objects.requireNonNull(getCommand("moderator")).setExecutor(new Moderator());
        Objects.requireNonNull(getCommand("report-check")).setExecutor(new ReportCheck());
        Objects.requireNonNull(getCommand("player-check")).setExecutor(new PlayerCheck());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ModeratorsEnable(), this);
        pluginManager.registerEvents(new ModeratorsJoin(), this);
        pluginManager.registerEvents(new PlayersFreeze(), this);
        pluginManager.registerEvents(new PlayersJoin(), this);

        getLogger().info("§2§lModerationTools has been enabled.");
    }

    @Override
    public void onDisable() {
        List<String> mods = CacheManager.moderator.getAll();
        for (String name : mods) {
            Objects.requireNonNull(Bukkit.getPlayer(name)).getInventory().setContents(CacheManager.inventory.restore(name));
            Objects.requireNonNull(Bukkit.getPlayer(name)).sendMessage("§2§lModerationTools has been disable.");
        }
    }

    public static Plugin getInstance() {
        return instance;
    }
}