package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "mt_users")
public class User {
    /**
     * The ID of the user.
     */
    @Id()
    @Column(name = "user_id")
    private Long id;

    /**
     * The UUID of the user.
     */
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    /**
     * The username of the user.
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * The language of the user.
     */
    @Column(name = "language")
    private String language;

    /**
     * The IP of the user.
     */
    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * If the user is muted.
     */
    @Column(name = "muted", nullable = false, defaultValue = "false")
    private Boolean muted;

    /**
     * If the user is banned.
     */
    @Column(name = "banned", nullable = false, defaultValue = "false")
    private Boolean banned;

    /**
     * The time the user joined.
     */
    @Column(name = "created_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    /**
     * Sets the ID of the user.
     *
     * @param newId The ID of the user.
     * @return The current User instance.
     */
    public User setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Retrieves the ID of the user.
     *
     * @return The ID of the user.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the UUID of the user.
     *
     * @param newUuid The UUID of the user.
     * @return The current User instance.
     */
    public User setUuid(final UUID newUuid) {
        this.uuid = newUuid;
        return this;
    }

    /**
     * Retrieves the UUID of the user.
     *
     * @return The UUID of the user.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Sets the username of the user.
     *
     * @param newUsername The username of the user.
     * @return The current User instance.
     */
    public User setUsername(final String newUsername) {
        this.username = newUsername;
        return this;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the language of the user.
     *
     * @param newLanguage The language of the user.
     * @return The current User instance.
     */
    public User setLanguage(final String newLanguage) {
        this.language = newLanguage;
        return this;
    }

    /**
     * Retrieves the language of the user.
     *
     * @return The language of the user.
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Sets the IP of the user.
     *
     * @param newIp The IP of the user.
     * @return The current User instance.
     */
    public User setIp(final String newIp) {
        this.ip = newIp;
        return this;
    }

    /**
     * Retrieves the IP of the user.
     *
     * @return The IP of the user.
     */
    public String getIp() {
        return this.ip;
    }

    /**
     * Sets if the user is muted.
     *
     * @param newMuted If the user is muted.
     * @return The current User instance.
     */
    public User setMuted(final Boolean newMuted) {
        this.muted = newMuted;
        return this;
    }

    /**
     * Retrieves if the user is muted.
     *
     * @return If the user is muted.
     */
    public Boolean getMuted() {
        return this.muted;
    }

    /**
     * Sets if the user is banned.
     *
     * @param newBanned If the user is banned.
     * @return The current User instance.
     */
    public User setBanned(final Boolean newBanned) {
        this.banned = newBanned;
        return this;
    }

    /**
     * Retrieves if the user is banned.
     *
     * @return If the user is banned.
     */
    public Boolean getBanned() {
        return this.banned;
    }

    /**
     * Sets the time the user joined.
     *
     * @param newCreatedAt The time the user joined.
     * @return The current User instance.
     */
    public User setCreatedAt(final LocalDateTime newCreatedAt) {
        this.createdAt = newCreatedAt;
        return this;
    }

    /**
     * Retrieves the first time the user joined.
     *
     * @return The first time the user joined.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
