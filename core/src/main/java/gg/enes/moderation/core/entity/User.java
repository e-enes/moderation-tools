package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Table(name = "users")
public class User extends BaseEntity {
    @Id()
    @Column(name = "user_id")
    private Long id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "muted", nullable = false, defaultValue = "false")
    private Boolean muted;

    @Column(name = "banned", nullable = false, defaultValue = "false")
    private Boolean banned;

    @Column(name = "last_login", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private Timestamp lastLogin;

    @Column(name = "joined_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private Timestamp joinedAt;

    public User() {
        super();
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        return this.id;
    }

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

    public User setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public Timestamp getLastLogin() {
        return this.lastLogin;
    }

    public User setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
        return this;
    }

    public Timestamp getJoinedAt() {
        return this.joinedAt;
    }
}
