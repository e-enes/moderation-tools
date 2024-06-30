package gg.enes.moderation.bukkit;

import org.bukkit.ChatColor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ModerationLanguage {
    /**
     * The map of messages.
     */
    private static final Map<String, String> MESSAGES = new HashMap<>();

    /**
     * The singleton instance of ModerationTools.
     */
    private static final ModerationTools PLUGIN = ModerationTools.getInstance();

    private ModerationLanguage() {
    }

    /**
     * Loads the language file.
     *
     * @param languageCode The language code.
     */
    public static void loadLanguage(final String languageCode) {
        MESSAGES.clear();
        loadProperties(languageCode);
    }

    /**
     * Loads the properties file.
     *
     * @param languageCode The language code.
     */
    private static void loadProperties(final String languageCode) {
        try (InputStream input = PLUGIN.getResource("messages_" + languageCode + ".properties")) {
            if (input == null) {
                PLUGIN.getLogger().warning("Language file not found: " + languageCode + ". Make sure the language selected is valid.");
                return;
            }

            Properties properties = new Properties();
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));

            for (String key : properties.stringPropertyNames()) {
                MESSAGES.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            PLUGIN.getLogger().severe("Failed to load language file: " + languageCode);
        }
    }

    /**
     * Retrieves the message for the given key.
     *
     * @param key The key of the message.
     * @param args The arguments to format the message.
     * @return The formatted message.
     */
    public static String getMessage(final String key, final Object... args) {
        String template = MESSAGES.getOrDefault(key, key);
        return ChatColor.translateAlternateColorCodes('&', MessageFormat.format(template, args));
    }

    /**
     * Retrieves the messages for the given key.
     *
     * @param key The key of the message.
     * @param args The arguments to format the message.
     * @return The formatted messages.
     */
    public static ArrayList<String> getMessages(final String key, final Object... args) {
        String template = MESSAGES.getOrDefault(key, key);
        ArrayList<String> messages = new ArrayList<>();

        for (String message : template.split(";")) {
            messages.add(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(message, args)));
        }

        return messages;
    }
}
