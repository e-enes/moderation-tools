package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;
import gg.enes.moderation.core.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "mt_sanctions")
public class Sanction {
    /**
     * The unique identifier for the sanction.
     */
    @Id()
    @Column(name = "sanction_id")
    private Long id;

    /**
     * The user associated with the sanction.
     */
    @Column(name = "user_uuid", nullable = false)
    private User user;

    /**
     * The reason for the sanction.
     */
    @Column(name = "reason", nullable = false)
    private String reason;

    /**
     * The type of the sanction.
     */
    @Column(name = "type", nullable = false)
    private String type;

    /**
     * The timestamp when the sanction was created.
     */
    @Column(name = "created_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * The timestamp when the sanction expires.
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /**
     * Indicates if the sanction is currently active.
     */
    @Column(name = "active", nullable = false, defaultValue = "true")
    private Boolean active;

    /**
     * Gets the unique identifier for the sanction.
     *
     * @return The ID of the sanction.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the sanction.
     *
     * @param newId The new ID for the sanction.
     * @return The current sanction instance.
     */
    public Sanction setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Gets the user associated with the sanction.
     *
     * @return The user associated with the sanction.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the sanction using the user's UUID.
     *
     * @param userUuid The UUID of the user to associate with the sanction.
     * @return The current sanction instance.
     */
    public Sanction setUser(final UUID userUuid) {
        this.user = UserRepository.getInstance().read(userUuid, false);

        if (this.user == null) {
            this.user = new User().setUuid(userUuid);
        }

        return this;
    }

    /**
     * Gets the reason for the sanction.
     *
     * @return The reason for the sanction.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for the sanction.
     *
     * @param newReason The new reason for the sanction.
     * @return The current sanction instance.
     */
    public Sanction setReason(final String newReason) {
        this.reason = newReason;
        return this;
    }

    /**
     * Gets the type of the sanction.
     *
     * @return The type of the sanction.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the sanction.
     *
     * @param newType The new type for the sanction.
     * @return The current sanction instance.
     */
    public Sanction setType(final String newType) {
        this.type = newType;
        return this;
    }

    /**
     * Gets the timestamp when the sanction was created.
     *
     * @return The creation timestamp of the sanction.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the sanction was created.
     *
     * @param newCreatedAt The new creation timestamp for the sanction.
     * @return The current sanction instance.
     */
    public Sanction setCreatedAt(final LocalDateTime newCreatedAt) {
        this.createdAt = newCreatedAt;
        return this;
    }

    /**
     * Gets the timestamp when the sanction expires.
     *
     * @return The expiration timestamp of the sanction.
     */
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /**
     * Sets the timestamp when the sanction expires.
     *
     * @param newExpiresAt The new expiration timestamp for the sanction.
     * @return The current sanction instance.
     */
    public Sanction setExpiresAt(final LocalDateTime newExpiresAt) {
        this.expiresAt = newExpiresAt;
        return this;
    }

    /**
     * Gets the active status of the sanction.
     *
     * @return The active status of the sanction.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the active status of the sanction.
     *
     * @param newActive The new active status for the sanction.
     * @return The current sanction instance.
     */
    public Sanction setActive(final Boolean newActive) {
        this.active = newActive;
        return this;
    }
}
