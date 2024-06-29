package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;

import java.time.LocalDateTime;

@Table(name = "mt_sanctions")
public class Sanction {
    /**
     * The ID of the sanction.
     */
    @Id()
    @Column(name = "sanction_id")
    private Long id;

    /**
     * The ID of the user to whom the sanction is applied.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * The reason for the sanction.
     */
    @Column(name = "reason", nullable = false)
    private String reason;

    /**
     * The type of the sanction (e.g., "ban", "mute").
     */
    @Column(name = "type", nullable = false)
    private String type;

    /**
     * The time when the sanction was created.
     */
    @Column(name = "created_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    /**
     * The time when the sanction expires, if applicable.
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /**
     * Indicates whether the sanction is currently active.
     */
    @Column(name = "active", nullable = false, defaultValue = "true")
    private Boolean active;

    /**
     * Retrieves the ID of the sanction.
     *
     * @return The ID of the sanction.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the sanction.
     *
     * @param newId The ID to set.
     * @return The current Sanction instance.
     */
    public Sanction setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Retrieves the ID of the user to whom the sanction is applied.
     *
     * @return The ID of the user.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user to whom the sanction is applied.
     *
     * @param newUserId The user ID to set.
     * @return The current Sanction instance.
     */
    public Sanction setUserId(final Long newUserId) {
        this.userId = newUserId;
        return this;
    }

    /**
     * Retrieves the reason for the sanction.
     *
     * @return The reason for the sanction.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for the sanction.
     *
     * @param newReason The reason to set.
     * @return The current Sanction instance.
     */
    public Sanction setReason(final String newReason) {
        this.reason = newReason;
        return this;
    }

    /**
     * Retrieves the type of the sanction.
     *
     * @return The type of the sanction.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the sanction.
     *
     * @param newType The type to set.
     * @return The current Sanction instance.
     */
    public Sanction setType(final String newType) {
        this.type = newType;
        return this;
    }

    /**
     * Retrieves the time when the sanction was created.
     *
     * @return The creation time of the sanction.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the time when the sanction was created.
     *
     * @param newCreatedAt The creation time to set.
     * @return The current Sanction instance.
     */
    public Sanction setCreatedAt(final LocalDateTime newCreatedAt) {
        this.createdAt = newCreatedAt;
        return this;
    }

    /**
     * Retrieves the time when the sanction expires, if applicable.
     *
     * @return The expiration time of the sanction.
     */
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /**
     * Sets the time when the sanction expires.
     *
     * @param newExpiresAt The expiration time to set.
     * @return The current Sanction instance.
     */
    public Sanction setExpiresAt(final LocalDateTime newExpiresAt) {
        this.expiresAt = newExpiresAt;
        return this;
    }

    /**
     * Retrieves whether the sanction is currently active.
     *
     * @return True if the sanction is active, false otherwise.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets whether the sanction is currently active.
     *
     * @param newActive The active status to set.
     * @return The current Sanction instance.
     */
    public Sanction setActive(final Boolean newActive) {
        this.active = newActive;
        return this;
    }
}
