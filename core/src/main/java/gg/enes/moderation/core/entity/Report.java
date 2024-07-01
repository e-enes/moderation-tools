package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;
import gg.enes.moderation.core.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "mt_reports")
public class Report {
    /**
     * The unique identifier for the report.
     */
    @Id()
    @Column(name = "report_id")
    private Long id;

    /**
     * The user who reported the incident.
     */
    @Column(name = "reporter_uuid", nullable = false)
    private User reporter;

    /**
     * The user who was reported.
     */
    @Column(name = "reported_uuid", nullable = false)
    private User reported;

    /**
     * The server where the incident occurred.
     */
    @Column(name = "server", nullable = false)
    private String server;

    /**
     * The reason for the report.
     */
    @Column(name = "reason", nullable = false)
    private String reason;

    /**
     * Additional details about the report.
     */
    @Column(name = "details")
    private String details;

    /**
     * Indicates if the report has been treated (handled).
     */
    @Column(name = "treated", nullable = false, defaultValue = "false")
    private Boolean treated = false;

    /**
     * The user who handled the report.
     */
    @Column(name = "treated_by")
    private UUID treatedBy;

    /**
     * The timestamp when the report was handled.
     */
    @Column(name = "treated_at")
    private LocalDateTime treatedAt;

    /**
     * The timestamp when the report was made.
     */
    @Column(name = "reported_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime reportedAt = LocalDateTime.now();

    /**
     * Gets the unique identifier for the report.
     *
     * @return The ID of the report.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the report.
     *
     * @param newId The new ID for the report.
     * @return The current report instance.
     */
    public Report setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Gets the user who reported the incident.
     *
     * @return The user who reported the incident.
     */
    public User getReporter() {
        return reporter;
    }

    /**
     * Sets the user who reported the incident using the reporter's UUID.
     *
     * @param reporterUuid The UUID of the user who reported the incident.
     * @return The current report instance.
     */
    public Report setReporter(final UUID reporterUuid) {
        this.reporter = UserRepository.getInstance().read(reporterUuid, false);

        if (this.reporter == null) {
            this.reporter = new User().setUuid(reporterUuid);
        }

        return this;
    }

    /**
     * Gets the user who was reported.
     *
     * @return The user who was reported.
     */
    public User getReported() {
        return reported;
    }

    /**
     * Sets the user who was reported using the reported user's UUID.
     *
     * @param reportedUuid The UUID of the user who was reported.
     * @return The current report instance.
     */
    public Report setReported(final UUID reportedUuid) {
        this.reported = UserRepository.getInstance().read(reportedUuid, false);

        if (this.reported == null) {
            this.reported = new User().setUuid(reportedUuid);
        }

        return this;
    }

    /**
     * Gets the server where the incident occurred.
     *
     * @return The server where the incident occurred.
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the server where the incident occurred.
     *
     * @param newServer The new server where the incident occurred.
     * @return The current report instance.
     */
    public Report setServer(final String newServer) {
        this.server = newServer;
        return this;
    }

    /**
     * Gets the reason for the report.
     *
     * @return The reason for the report.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for the report.
     *
     * @param newReason The new reason for the report.
     * @return The current report instance.
     */
    public Report setReason(final String newReason) {
        this.reason = newReason;
        return this;
    }

    /**
     * Gets additional details about the report.
     *
     * @return Additional details about the report.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets additional details about the report.
     *
     * @param newDetails The new additional details for the report.
     * @return The current report instance.
     */
    public Report setDetails(final String newDetails) {
        this.details = newDetails;
        return this;
    }

    /**
     * Checks if the report has been treated (handled).
     *
     * @return True if the report has been treated; false otherwise.
     */
    public Boolean getTreated() {
        return treated;
    }

    /**
     * Sets whether the report has been treated (handled).
     *
     * @param newTreated The new treated status for the report.
     * @return The current report instance.
     */
    public Report setTreated(final Boolean newTreated) {
        this.treated = newTreated;
        return this;
    }

    /**
     * Gets the UUID of the user who handled the report.
     *
     * @return The UUID of the user who handled the report.
     */
    public UUID getTreatedBy() {
        return treatedBy;
    }

    /**
     * Sets the UUID of the user who handled the report.
     *
     * @param newTreatedBy The new UUID of the user who handled the report.
     * @return The current report instance.
     */
    public Report setTreatedBy(final UUID newTreatedBy) {
        this.treatedBy = newTreatedBy;
        return this;
    }

    /**
     * Gets the timestamp when the report was handled.
     *
     * @return The timestamp when the report was handled.
     */
    public LocalDateTime getTreatedAt() {
        return treatedAt;
    }

    /**
     * Sets the timestamp when the report was handled.
     *
     * @param newTreatedAt The new timestamp when the report was handled.
     * @return The current report instance.
     */
    public Report setTreatedAt(final LocalDateTime newTreatedAt) {
        this.treatedAt = newTreatedAt;
        return this;
    }

    /**
     * Gets the timestamp when the report was made.
     *
     * @return The timestamp when the report was made.
     */
    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    /**
     * Sets the timestamp when the report was made.
     *
     * @param newReportedAt The new timestamp when the report was made.
     * @return The current report instance.
     */
    public Report setReportedAt(final LocalDateTime newReportedAt) {
        this.reportedAt = newReportedAt;
        return this;
    }
}
