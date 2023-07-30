package gg.enes.moderation.entities;

import java.sql.Timestamp;
import java.util.UUID;

public class ReportEntity {
    private Integer id;
    private final UUID reporterUuid;
    private final UUID reportedUuid;
    private final String reason;
    private final Timestamp createdAt;

    public ReportEntity(UUID reporterUuid, UUID reportedUuid, String reason, Timestamp createdAt) {
        this.reporterUuid = reporterUuid;
        this.reportedUuid = reportedUuid;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public UUID getReporterUuid() {
        return reporterUuid;
    }

    public UUID getReportedUuid() {
        return reportedUuid;
    }

    public String getReason() {
        return reason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
