package enes.plugin.moderation;

import enes.plugin.moderation.coommands.Moderator;
import enes.plugin.moderation.coommands.PlayerCheck;
import enes.plugin.moderation.coommands.Report;
import enes.plugin.moderation.coommands.ReportCheck;
import enes.plugin.moderation.events.ModeratorsEnable;
import enes.plugin.moderation.events.ModeratorsJoin;
import enes.plugin.moderation.events.PlayersFreeze;
import enes.plugin.moderation.events.PlayersJoin;
import enes.plugin.moderation.storage.Database;
import enes.plugin.moderation.storage.cache.Data;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class Main extends JavaPlugin {
    private Database database;

    @Override
    public void onEnable() {
        try {
            database = new Database(getDataFolder() + File.separator + "database.db");
            database.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Failed to create databases!");
            setEnabled(false);
            return;
        }

        getCommand("report").setExecutor(new Report(database));
        getCommand("moderator").setExecutor(new Moderator(this));
        getCommand("report-check").setExecutor(new ReportCheck(database));
        getCommand("player-check").setExecutor(new PlayerCheck(database));

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ModeratorsEnable(database, this), this);
        pluginManager.registerEvents(new ModeratorsJoin(), this);
        pluginManager.registerEvents(new PlayersFreeze(), this);
        pluginManager.registerEvents(new PlayersJoin(database), this);
    }

    @Override
    public void onDisable() {
        List<String> mods = Data.moderator.getAll();
        for (String name : mods) {
            Bukkit.getPlayer(name).getInventory().setContents(Data.inventory.restore(name));
            Bukkit.getPlayer(name).sendMessage("§2§lModerationTools has been disable.");
        }
    }
}
