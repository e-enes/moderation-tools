package gg.enes.moderation.entities;

import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.util.UUID;

public class PlayerEntity {
    private Integer id;
    private String name;
    private final UUID uuid;
    private String ip;
    private Boolean muted;
    private Boolean banned;
    private Timestamp lastConnection;
    private final Timestamp createdAt;

    // 'frozen' does not belong to the database
    // It is used as an additional flag in the entity
    private Boolean frozen = false;

    // 'frozenByModeratorUuid' does not belong to the database
    // It is used as an additional flag in the entity
    private UUID frozenByModeratorUuid;

    // 'moderatorMode' does not belong to the database
    // It is used as an additional flag in the entity
    private Boolean moderatorMode = false;

    // 'vanished' does not belong to the database
    // It is used as an additional flag in the entity
    private Boolean vanished = false;

    // 'inventory' does not belong to the database
    // It is used as an additional flag in the entity
    private ItemStack[] inventory;

    public PlayerEntity(String name, UUID uuid, String ip, Boolean muted, Boolean banned, Timestamp lastConnection, Timestamp createdAt) {
        this.name = name;
        this.uuid = uuid;
        this.ip = ip;
        this.muted = muted;
        this.banned = banned;
        this.lastConnection = lastConnection;
        this.createdAt = createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setLastConnection(Timestamp lastConnection) {
        this.lastConnection = lastConnection;
    }

    public Timestamp getLastConnection() {
        return lastConnection;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setter and Getter for 'frozen'
    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    // Setter and Getter for 'frozenByModeratorUuid'
    public void setFrozenByModeratorUuid(UUID frozenByModeratorUuid) {
        this.frozenByModeratorUuid = frozenByModeratorUuid;
    }

    public UUID getFrozenByModeratorUuid() {
        return frozenByModeratorUuid;
    }

    // Setter and Getter for 'moderatorMode'
    public void setModeratorMode(Boolean moderatorMode) {
        this.moderatorMode = moderatorMode;
    }

    public Boolean getModeratorMode() {
        return moderatorMode;
    }

    // Setter and Getter for 'vanished'
    public void setVanished(Boolean vanished) {
        this.vanished = vanished;
    }

    public Boolean getVanished() {
        return vanished;
    }

    // Setter and Getter for 'inventory'
    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }
}
