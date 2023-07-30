package gg.enes.moderation.entities;

import java.sql.Timestamp;
import java.util.UUID;

public class MuteEntity {
    private Integer id;
    private final UUID playerUuid;
    private final UUID moderatorUuid;
    private final String reason;
    private Long time;
    private final Timestamp createdAt;
    private Timestamp end;
    private Boolean active;

    public MuteEntity(UUID playerUuid, UUID moderatorUuid, String reason, Long time, Timestamp createdAt, Boolean active) {
        this.playerUuid = playerUuid;
        this.moderatorUuid = moderatorUuid;
        this.reason = reason;
        this.time = time;
        this.createdAt = createdAt;
        this.end = new Timestamp(createdAt.getTime() + time);
        this.active = active;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public UUID getModeratorUuid() {
        return moderatorUuid;
    }

    public String getReason() {
        return reason;
    }

    public void setTime(Long time) {
        this.time = time;
        this.end = new Timestamp(this.createdAt.getTime() + this.time);
    }

    public Long getTime() {
        return time;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }
}
