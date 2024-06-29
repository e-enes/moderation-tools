package gg.enes.moderation.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public final class ModerationTools extends JavaPlugin {
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    /**
     * Retrieves the ModerationTools instance.
     *
     * @return The ModerationTools instance.
     */
    public static ModerationTools getInstance() {
        return getPlugin(ModerationTools.class);
    }
}
