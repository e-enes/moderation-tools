package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Index;
import gg.enes.moderation.core.entity.annotations.OneToMany;
import gg.enes.moderation.core.entity.annotations.Table;

import java.time.LocalDateTime;
import java.util.List;
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
    @Index()
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    /**
     * The username of the user.
     */
    @Column(name = "username", nullable = false)
    private String username;

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
     * The connections of the user.
     */
    @OneToMany()
    private List<Connection> connections;

    /**
     * The sanctions applied to the user.
     */
    @OneToMany()
    private List<Sanction> sanctions;

    /**
     * The reports made by the user.
     */
    @OneToMany()
    private List<Report> reports;

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
     * Retrieves the time the user joined.
     *
     * @return The time the user joined.
     */
    public LocalDateTime getJoinedAt() {
        return this.createdAt;
    }

    /**
     * Sets the connections of the user.
     *
     * @param newConnections The connections of the user.
     * @return The current User instance.
     */
    public User setConnections(final List<Connection> newConnections) {
        this.connections = newConnections;
        return this;
    }

    /**
     * Retrieves the connections of the user.
     *
     * @return The connections of the user.
     */
    public List<Connection> getConnections() {
        return this.connections;
    }

    /**
     * Sets the sanctions applied to the user.
     *
     * @param newSanctions The sanctions applied to the user.
     * @return The current User instance.
     */
    public User setSanctions(final List<Sanction> newSanctions) {
        this.sanctions = newSanctions;
        return this;
    }

    /**
     * Retrieves the sanctions applied to the user.
     *
     * @return The sanctions applied to the user.
     */
    public List<Sanction> getSanctions() {
        return this.sanctions;
    }

    /**
     * Sets the reports made by the user.
     *
     * @param newReports The reports made by the user.
     * @return The current User instance.
     */
    public User setReports(final List<Report> newReports) {
        this.reports = newReports;
        return this;
    }

    /**
     * Retrieves the reports made by the user.
     *
     * @return The reports made by the user.
     */
    public List<Report> getReports() {
        return this.reports;
    }
}
