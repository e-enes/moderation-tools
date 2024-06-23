package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;

import java.time.LocalDateTime;

@Table(name = "mt_connections")
public class Connection {
    /**
     * The ID of the connection.
     */
    @Id()
    @Column(name = "connection_id")
    private Long id;

    /**
     * The ID of the user associated with the connection.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * The IP address of the connection.
     */
    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * The time when the connection was created.
     */
    @Column(name = "connected_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime connectedAt;

    /**
     * The time when the connection was closed.
     */
    @Column(name = "disconnected_at")
    private LocalDateTime disconnectedAt;

    /**
     * Retrieves the ID of the connection.
     *
     * @return The ID of the connection.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the connection.
     *
     * @param newId The ID to set.
     * @return The current Connection instance.
     */
    public Connection setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Retrieves the ID of the user associated with the connection.
     *
     * @return The ID of the user.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user associated with the connection.
     *
     * @param newUserId The user ID to set.
     * @return The current Connection instance.
     */
    public Connection setUserId(final Long newUserId) {
        this.userId = newUserId;
        return this;
    }

    /**
     * Retrieves the IP address of the connection.
     *
     * @return The IP address of the connection.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the IP address of the connection.
     *
     * @param newIp The IP address to set.
     * @return The current Connection instance.
     */
    public Connection setIp(final String newIp) {
        this.ip = newIp;
        return this;
    }

    /**
     * Retrieves the time when the connection was created.
     *
     * @return The creation time of the connection.
     */
    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    /**
     * Sets the time when the connection was created.
     *
     * @param newConnectedAt The creation time to set.
     * @return The current Connection instance.
     */
    public Connection setConnectedAt(final LocalDateTime newConnectedAt) {
        this.connectedAt = newConnectedAt;
        return this;
    }

    /**
     * Retrieves the time when the connection was closed.
     *
     * @return The closing time of the connection.
     */
    public LocalDateTime getDisconnectedAt() {
        return disconnectedAt;
    }

    /**
     * Sets the time when the connection was closed.
     *
     * @param newDisconnectedAt The closing time to set.
     * @return The current Connection instance.
     */
    public Connection setDisconnectedAt(final LocalDateTime newDisconnectedAt) {
        this.disconnectedAt = newDisconnectedAt;
        return this;
    }
}
