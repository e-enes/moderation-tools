package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;

import java.time.LocalDateTime;

@Table(name = "mt_reports")
public class Report {

    /**
     * The ID of the report.
     */
    @Id()
    @Column(name = "report_id")
    private Long id;

    /**
     * The ID of the user who made the report.
     */
    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    /**
     * The ID of the user being reported.
     */
    @Column(name = "reported_id", nullable = false)
    private Long reportedId;

    /**
     * The server where the report was made.
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
     * Indicates whether the report is active.
     */
    @Column(name = "treated", nullable = false, defaultValue = "false")
    private Boolean treated;

    /**
     * The ID of the user who treated the report.
     */
    @Column(name = "treated_by")
    private Long treatedBy;

    /**
     * The time when the report was treated.
     */
    @Column(name = "treated_at")
    private LocalDateTime treatedAt;

    /**
     * The time when the report was made.
     */
    @Column(name = "reported_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime reportedAt;

    /**
     * Retrieves the ID of the report.
     *
     * @return The ID of the report.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the report.
     *
     * @param newId The ID to set.
     * @return The current Report instance.
     */
    public Report setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Retrieves the ID of the user who made the report.
     *
     * @return The ID of the reporter.
     */
    public Long getReporterId() {
        return reporterId;
    }

    /**
     * Sets the ID of the user who made the report.
     *
     * @param newReporterId The reporter ID to set.
     * @return The current Report instance.
     */
    public Report setReporterId(final Long newReporterId) {
        this.reporterId = newReporterId;
        return this;
    }

    /**
     * Retrieves the ID of the user being reported.
     *
     * @return The ID of the reported user.
     */
    public Long getReportedId() {
        return reportedId;
    }

    /**
     * Sets the ID of the user being reported.
     *
     * @param newReportedId The reported ID to set.
     * @return The current Report instance.
     */
    public Report setReportedId(final Long newReportedId) {
        this.reportedId = newReportedId;
        return this;
    }

    /**
     * Retrieves the server where the report was made.
     *
     * @return The server where the report was made.
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the server where the report was made.
     *
     * @param newServer The server to set.
     * @return The current Report instance.
     */
    public Report setServer(final String newServer) {
        this.server = newServer;
        return this;
    }

    /**
     * Retrieves the reason for the report.
     *
     * @return The reason for the report.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for the report.
     *
     * @param newReason The reason to set.
     * @return The current Report instance.
     */
    public Report setReason(final String newReason) {
        this.reason = newReason;
        return this;
    }

    /**
     * Retrieves additional details about the report.
     *
     * @return Additional details about the report.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets additional details about the report.
     *
     * @param newDetails Additional details to set.
     * @return The current Report instance.
     */
    public Report setDetails(final String newDetails) {
        this.details = newDetails;
        return this;
    }

    /**
     * Indicates whether the report is active.
     *
     * @return Whether the report is active.
     */
    public Boolean getTreated() {
        return treated;
    }

    /**
     * Sets whether the report is active.
     *
     * @param newTreated Whether the report is active.
     * @return The current Report instance.
     */
    public Report setTreated(final Boolean newTreated) {
        this.treated = newTreated;
        return this;
    }

    /**
     * Retrieves the ID of the user who treated the report.
     *
     * @return The ID of the user who treated the report.
     */
    public Long getTreatedBy() {
        return treatedBy;
    }

    /**
     * Sets the ID of the user who treated the report.
     *
     * @param newTreatedBy The ID of the user who treated the report.
     * @return The current Report instance.
     */
    public Report setTreatedBy(final Long newTreatedBy) {
        this.treatedBy = newTreatedBy;
        return this;
    }

    /**
     * Retrieves the time when the report was treated.
     *
     * @return The time when the report was treated.
     */
    public LocalDateTime getTreatedAt() {
        return treatedAt;
    }

    /**
     * Sets the time when the report was treated.
     *
     * @param newTreatedAt The time to set.
     * @return The current Report instance.
     */
    public Report setTreatedAt(final LocalDateTime newTreatedAt) {
        this.treatedAt = newTreatedAt;
        return this;
    }

    /**
     * Retrieves the time when the report was made.
     *
     * @return The time when the report was made.
     */
    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    /**
     * Sets the time when the report was made.
     *
     * @param newReportedAt The time to set.
     * @return The current Report instance.
     */
    public Report setReportedAt(final LocalDateTime newReportedAt) {
        this.reportedAt = newReportedAt;
        return this;
    }
}
