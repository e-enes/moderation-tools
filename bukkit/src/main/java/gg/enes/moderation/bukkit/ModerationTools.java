package gg.enes.moderation.bukkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import gg.enes.moderation.bukkit.commands.ReportCommand;
import gg.enes.moderation.core.cache.config.CacheConfig;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.database.config.DatabaseConfig;
import gg.enes.moderation.core.database.config.DatabaseType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.logging.Level;

public final class ModerationTools extends JavaPlugin {
    /**
     * The URL to check for updates.
     */
    private static final String GITHUB_API_URL = "https://api.github.com/repos/e-enes/moderation-tools/releases/latest";

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Load language
        ModerationLanguage.loadLanguage(getConfig().getString("language"));

        // Check for updates
        checkUpdate();

        // Set cache config
        CacheConfig.setMaximumSize(getConfig().getInt("cache.maxSize"));
        CacheConfig.setTimeToLive(getConfig().getLong("cache.maxTime"));

        // Load database
        boolean isSqlite = getConfig().getString("dbType").equalsIgnoreCase("sqlite");
        if (isSqlite) {
            loadSqlite();
        } else {
            loadMysql();
        }

        // Load listeners and commands
        loadListeners();
        loadCommands();

        getLogger().info("Enabled ModerationTools v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        DatabaseManager.close();

        getLogger().info("Disabled ModerationTools v" + getDescription().getVersion());
    }

    /**
     * Retrieves the ModerationTools instance.
     *
     * @return The ModerationTools instance.
     */
    public static ModerationTools getInstance() {
        return getPlugin(ModerationTools.class);
    }

    private void checkUpdate() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URI(GITHUB_API_URL).toURL().openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github+json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
                JsonElement jsonElement = JsonParser.parseReader(reader);
                String latestVersion = jsonElement.getAsJsonObject().get("tag_name").getAsString();
                String htmlUrl = jsonElement.getAsJsonObject().get("html_url").getAsString();

                String currentVersion = getDescription().getVersion();

                if (!currentVersion.equalsIgnoreCase(latestVersion.replace("v", ""))) {
                    getLogger().warning("An update is available! Current version: v" + currentVersion + ", Latest version: " + latestVersion + ". Update here: " + htmlUrl);
                }
            } catch (Exception e) {
                getLogger().severe("Failed to check for updates: " + e.getMessage());
            }
        });
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
        PluginManager pluginManager = Bukkit.getPluginManager();

        // Register listeners
    }

    private void loadCommands() {
        // Register commands
        getCommand("report").setExecutor(new ReportCommand());
    }
}
