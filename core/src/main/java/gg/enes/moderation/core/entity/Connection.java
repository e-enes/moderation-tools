package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;
import gg.enes.moderation.core.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a connection entity mapped to the "mt_connections" table.
 */
@Table(name = "mt_connections")
public class Connection {

    /**
     * The unique identifier for the connection.
     */
    @Id()
    @Column(name = "connection_id")
    private Long id;

    /**
     * The user associated with the connection.
     */
    @Column(name = "user_uuid", nullable = false)
    private User user;

    /**
     * The IP address associated with the connection.
     */
    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * The timestamp when the connection was established.
     */
    @Column(name = "connected_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime connectedAt = LocalDateTime.now();

    /**
     * The timestamp when the connection was terminated.
     */
    @Column(name = "disconnected_at")
    private LocalDateTime disconnectedAt;

    /**
     * Gets the unique identifier for the connection.
     *
     * @return The ID of the connection.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the connection.
     *
     * @param newId The new ID for the connection.
     * @return The current connection instance.
     */
    public Connection setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Gets the user associated with the connection.
     *
     * @return The user associated with the connection.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the connection using the user's UUID.
     *
     * @param userUuid The UUID of the user to associate with the connection.
     * @return The current connection instance.
     */
    public Connection setUser(final UUID userUuid) {
        this.user = UserRepository.getInstance().read(userUuid, false);

        if (this.user == null) {
            this.user = new User().setUuid(userUuid);
        }

        return this;
    }

    /**
     * Gets the IP address associated with the connection.
     *
     * @return The IP address associated with the connection.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the IP address associated with the connection.
     *
     * @param newIp The new IP address for the connection.
     * @return The current connection instance.
     */
    public Connection setIp(final String newIp) {
        this.ip = newIp;
        return this;
    }

    /**
     * Gets the timestamp when the connection was established.
     *
     * @return The timestamp when the connection was established.
     */
    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    /**
     * Sets the timestamp when the connection was established.
     *
     * @param newConnectedAt The new timestamp when the connection was established.
     * @return The current connection instance.
     */
    public Connection setConnectedAt(final LocalDateTime newConnectedAt) {
        this.connectedAt = newConnectedAt;
        return this;
    }

    /**
     * Gets the timestamp when the connection was terminated.
     *
     * @return The timestamp when the connection was terminated.
     */
    public LocalDateTime getDisconnectedAt() {
        return disconnectedAt;
    }

    /**
     * Sets the timestamp when the connection was terminated.
     *
     * @param newDisconnectedAt The new timestamp when the connection was terminated.
     * @return The current connection instance.
     */
    public Connection setDisconnectedAt(final LocalDateTime newDisconnectedAt) {
        this.disconnectedAt = newDisconnectedAt;
        return this;
    }
}
