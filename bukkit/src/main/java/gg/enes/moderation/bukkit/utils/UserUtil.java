package gg.enes.moderation.bukkit.utils;

import gg.enes.moderation.core.entity.User;
import gg.enes.moderation.core.repository.UserRepository;
import org.bukkit.entity.Player;

public final class UserUtil {

    private UserUtil() {
    }

    /**
     * Gets a user from the database safely.
     *
     * @param player The player to get the user from.
     * @return The user.
     */
    public static User getUserSafe(final Player player) {
        User user = UserRepository.getInstance().read(player.getUniqueId(), false);

        if (user == null) {
            user = new User()
                    .setUuid(player.getUniqueId())
                    .setUsername(player.getName())
                    .setIp(player.getAddress() == null ? "0.0.0.0" : player.getAddress().getAddress().getHostAddress());
            UserRepository.getInstance().create(user);
        }

        return user;
    }
}
