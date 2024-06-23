package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;

import java.time.LocalDateTime;

@Table(name = "mt_actions")
public class Action {
    /**
     * The ID of the action.
     */
    @Id()
    @Column(name = "action_id")
    private Long id;

    /**
     * The ID of the user who performed the action.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * The type of action performed.
     */
    @Column(name = "action_type", nullable = false)
    private String actionType;

    /**
     * A description or details about the action performed.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * The server where the action was performed.
     */
    @Column(name = "server", nullable = false)
    private String server;

    /**
     * Additional details about the action, such as context or metadata.
     */
    @Column(name = "details")
    private String details;

    /**
     * The IP address associated with the action.
     */
    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * The time when the action was performed.
     */
    @Column(name = "performed_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime performedAt;

    /**
     * Retrieves the ID of the action.
     *
     * @return The ID of the action.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the action.
     *
     * @param newId The ID to set.
     * @return The current Action instance.
     */
    public Action setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Retrieves the ID of the user who performed the action.
     *
     * @return The ID of the user.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user who performed the action.
     *
     * @param newUserId The user ID to set.
     * @return The current Action instance.
     */
    public Action setUserId(final Long newUserId) {
        this.userId = newUserId;
        return this;
    }

    /**
     * Retrieves the type of action performed.
     *
     * @return The type of action.
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * Sets the type of action performed.
     *
     * @param newActionType The action type to set.
     * @return The current Action instance.
     */
    public Action setActionType(final String newActionType) {
        this.actionType = newActionType;
        return this;
    }

    /**
     * Retrieves the description or details about the action performed.
     *
     * @return The description of the action.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description or details about the action performed.
     *
     * @param newDescription The description to set.
     * @return The current Action instance.
     */
    public Action setDescription(final String newDescription) {
        this.description = newDescription;
        return this;
    }

    /**
     * Retrieves the server where the action was performed.
     *
     * @return The server where the action was performed.
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the server where the action was performed.
     *
     * @param newServer The server to set.
     * @return The current Action instance.
     */
    public Action setServer(final String newServer) {
        this.server = newServer;
        return this;
    }

    /**
     * Retrieves additional details about the action.
     *
     * @return Additional details about the action.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets additional details about the action.
     *
     * @param newDetails Additional details to set.
     * @return The current Action instance.
     */
    public Action setDetails(final String newDetails) {
        this.details = newDetails;
        return this;
    }

    /**
     * Retrieves the IP address associated with the action.
     *
     * @return The IP address associated with the action.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the IP address associated with the action.
     *
     * @param newIp The IP address to set.
     * @return The current Action instance.
     */
    public Action setIp(final String newIp) {
        this.ip = newIp;
        return this;
    }

    /**
     * Retrieves the time when the action was performed.
     *
     * @return The time when the action was performed.
     */
    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    /**
     * Sets the time when the action was performed.
     *
     * @param newPerformedAt The time to set.
     * @return The current Action instance.
     */
    public Action setPerformedAt(final LocalDateTime newPerformedAt) {
        this.performedAt = newPerformedAt;
        return this;
    }
}
