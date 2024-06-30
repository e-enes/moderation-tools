package gg.enes.moderation.bukkit;

import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.database.config.DatabaseConfig;
import gg.enes.moderation.core.database.config.DatabaseType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;

public final class ModerationTools extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        CacheConfig.setMaximumSize(getConfig().getInt("cache.maxSize"));
        CacheConfig.setTimeToLive(getConfig().getLong("cache.maxTime"));

        boolean isSqlite = getConfig().getString("dbType").equalsIgnoreCase("sqlite");
        if (isSqlite) {
            loadSqlite();
        } else {
            loadMysql();
        }

        loadListeners();
        loadCommands();

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        DatabaseManager.close();

        getLogger().info("Plugin disabled.");
    }

    /**
     * Retrieves the ModerationTools instance.
     *
     * @return The ModerationTools instance.
     */
    public static ModerationTools getInstance() {
        return getPlugin(ModerationTools.class);
    }

    private void loadSqlite() {
        getLogger().info("Loading SQLite... (HikariCP Enabling)");

        // Load DB folder
        File dbFolder = new File(getDataFolder(), "db");
        if (!dbFolder.exists()) {
            boolean created = dbFolder.mkdirs();

            if (created) {
                getLogger().info("Created db folder.");
            } else {
                getLogger().severe("Failed to create db folder.");
                setEnabled(false);
                return;
            }
        }

        // Load SQLite
        try {
            DatabaseConfig databaseConfig = new DatabaseConfig()
                    .setDbType(DatabaseType.SQLITE)
                    .setFileName(getDataFolder() + File.separator + "db" + File.separator + "moderation-tools.db");
            DatabaseManager.initialize(databaseConfig);
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Failed to initialize SQLite.", e);
            setEnabled(false);
            return;
        }

        getLogger().info("SQLite loaded. (HikariCP Enabled)");
    }

    private void loadMysql() {
        getLogger().info("Loading MySQL... (HikariCP Enabling)");

        // Load MySQL
        try {
            DatabaseConfig databaseConfig = new DatabaseConfig()
                    .setDbType(DatabaseType.MYSQL)
                    .setHost(getConfig().getString("mysql.host"))
                    .setPort(getConfig().getInt("mysql.port"))
                    .setDatabaseName(getConfig().getString("mysql.database"))
                    .setUsername(getConfig().getString("mysql.username"))
                    .setPassword(getConfig().getString("mysql.password"));
            DatabaseManager.initialize(databaseConfig);
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Failed to initialize MySQL.", e);
            setEnabled(false);
            return;
        }

        getLogger().info("MySQL loaded. (HikariCP Enabled)");
    }

    private void loadListeners() {
        // Register listeners
    }

    private void loadCommands() {
        // Register commands
    }
}
