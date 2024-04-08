package gg.enes.core.entity;

import gg.enes.core.entity.annotations.Column;
import gg.enes.core.entity.annotations.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "uuid", primary = true)
    private UUID uuid;

    @Column(name = "username")
    private String username;

    @Column(name = "ip")
    private String ip;

    @Column(name = "muted")
    private Boolean muted;

    @Column(name = "banned")
    private Boolean banned;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User() {
        super();
    }

    // Getters et Setters
    public User setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public User setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getIp() {
        return this.ip;
    }

    public User setMuted(Boolean muted) {
        this.muted = muted;
        return this;
    }

    public Boolean getMuted() {
        return this.muted;
    }

    public User setBanned(Boolean banned) {
        this.banned = banned;
        return this;
    }

    public Boolean getBanned() {
        return this.banned;
    }

    public User setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
        return this;
    }

    public LocalDateTime getLastActivity() {
        return this.lastActivity;
    }

    public User setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
