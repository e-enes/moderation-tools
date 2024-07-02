package gg.enes.moderation.core.entity;

import gg.enes.moderation.core.entity.annotations.Column;
import gg.enes.moderation.core.entity.annotations.Id;
import gg.enes.moderation.core.entity.annotations.Table;
import gg.enes.moderation.core.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "mt_messages")
public class Message {
    /**
     * The unique identifier for the message.
     */
    @Id()
    @Column(name = "message_id")
    private Long id;

    /**
     * The server where the message was sent.
     */
    @Column(name = "server", nullable = false)
    private String server;

    /**
     * The user who sent the message.
     */
    @Column(name = "sender_uuid", nullable = false)
    private User sender;

    /**
     * The type of the message (public/private).
     */
    @Column(name = "type", nullable = false)
    private String type;

    /**
     * The content of the message.
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * The timestamp when the message was sent.
     */
    @Column(name = "sent_at", nullable = false, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime sentAt = LocalDateTime.now();

    /**
     * Gets the unique identifier for the message.
     *
     * @return The ID of the message.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the message.
     *
     * @param newId The new ID for the message.
     * @return The current message instance.
     */
    public Message setId(final Long newId) {
        this.id = newId;
        return this;
    }

    /**
     * Gets the server where the message was sent.
     *
     * @return The server where the message was sent.
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the server where the message was sent.
     *
     * @param newServer The new server where the message was sent.
     * @return The current message instance.
     */
    public Message setServer(final String newServer) {
        this.server = newServer;
        return this;
    }

    /**
     * Gets the user who sent the message.
     *
     * @return The user who sent the message.
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets the user who sent the message.
     *
     * @param senderUuid The new user who sent the message.
     * @return The current message instance.
     */
    public Message setSender(final UUID senderUuid) {
        this.sender = UserRepository.getInstance().read(senderUuid, false);

        if (this.sender == null) {
            this.sender = new User().setUuid(senderUuid);
        }

        return this;
    }

    /**
     * Gets the type of the message.
     *
     * @return The type of the message.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the message.
     *
     * @param newType The new type of the message.
     * @return The current message instance.
     */
    public Message setType(final String newType) {
        this.type = newType;
        return this;
    }

    /**
     * Gets the content of the message.
     *
     * @return The content of the message.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the message.
     *
     * @param newContent The new content of the message.
     * @return The current message instance.
     */
    public Message setContent(final String newContent) {
        this.content = newContent;
        return this;
    }

    /**
     * Gets the timestamp when the message was sent.
     *
     * @return The timestamp when the message was sent.
     */
    public LocalDateTime getSentAt() {
        return sentAt;
    }

    /**
     * Sets the timestamp when the message was sent.
     *
     * @param newSentAt The new timestamp when the message was sent.
     * @return The current message instance.
     */
    public Message setSentAt(final LocalDateTime newSentAt) {
        this.sentAt = newSentAt;
        return this;
    }
}
