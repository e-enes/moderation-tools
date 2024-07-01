package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "mt_users")
public class User {
    /**
     * The unique identifier for the user.
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
     * The IP address of the user.
     */
    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * Indicates if the user is muted.
     */
    @Column(name = "muted", nullable = false, defaultValue = "false")
    private Boolean muted = false;

    /**
     * Indicates if the user is banned.
     */
    @Column(name = "banned", nullable = false, defaultValue = "false")
    private Boolean banned = false;

    /**
     * The timestamp when the user was created.
     */
    @Column(name = "created_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Sets the unique identifier for the user.
     *
     * @param newId The new ID for the user.
     * @return The current user instance.
     */
    public User setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Gets the unique identifier for the user.
     *
     * @return The ID of the user.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the UUID for the user.
     *
     * @param newUuid The new UUID for the user.
     * @return The current user instance.
     */
    public User setUuid(final UUID newUuid) {
        this.uuid = newUuid;
        return this;
    }

    /**
     * Gets the UUID for the user.
     *
     * @return The UUID of the user.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Sets the username for the user.
     *
     * @param newUsername The new username for the user.
     * @return The current user instance.
     */
    public User setUsername(final String newUsername) {
        this.username = newUsername;
        return this;
    }

    /**
     * Gets the username for the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the IP address for the user.
     *
     * @param newIp The new IP address for the user.
     * @return The current user instance.
     */
    public User setIp(final String newIp) {
        this.ip = newIp;
        return this;
    }

    /**
     * Gets the IP address for the user.
     *
     * @return The IP address of the user.
     */
    public String getIp() {
        return this.ip;
    }

    /**
     * Sets the muted status for the user.
     *
     * @param newMuted The new muted status for the user.
     * @return The current user instance.
     */
    public User setMuted(final Boolean newMuted) {
        this.muted = newMuted;
        return this;
    }

    /**
     * Gets the muted status for the user.
     *
     * @return The muted status of the user.
     */
    public Boolean getMuted() {
        return this.muted;
    }

    /**
     * Sets the banned status for the user.
     *
     * @param newBanned The new banned status for the user.
     * @return The current user instance.
     */
    public User setBanned(final Boolean newBanned) {
        this.banned = newBanned;
        return this;
    }

    /**
     * Gets the banned status for the user.
     *
     * @return The banned status of the user.
     */
    public Boolean getBanned() {
        return this.banned;
    }

    /**
     * Sets the creation timestamp for the user.
     *
     * @param newCreatedAt The new creation timestamp for the user.
     * @return The current user instance.
     */
    public User setCreatedAt(final LocalDateTime newCreatedAt) {
        this.createdAt = newCreatedAt;
        return this;
    }

    /**
     * Gets the creation timestamp for the user.
     *
     * @return The creation timestamp of the user.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}