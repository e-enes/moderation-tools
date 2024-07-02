package gg.enes.moderation.core.repository;

import gg.enes.moderation.core.ModerationLogger;
import gg.enes.moderation.core.database.DatabaseManager;
import gg.enes.moderation.core.entity.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MessageRepository {
    /**
     * The singleton instance of the message repository.
     */
    private static MessageRepository instance;

    private MessageRepository() {
    }

    /**
     * Retrieves the singleton instance of the message repository.
     *
     * @return The singleton instance of the message repository.
     */
    public static MessageRepository getInstance() {
        if (instance == null) {
            instance = new MessageRepository();
        }
        return instance;
    }

    /**
     * Creates a new message entity in the database.
     *
     * @param entity The message entity to create.
     */
    public void create(final Message entity) {
        String sql = "INSERT INTO mt_messages (server, sender_uuid, type, content, sent_at) VALUES (?, ?, ?, ?, ?)";
        try (java.sql.Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, entity.getServer());
            stmt.setString(2, entity.getSender().getUuid().toString());
            stmt.setString(3, entity.getType());
            stmt.setString(4, entity.getContent());
            stmt.setObject(5, entity.getSentAt());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            ModerationLogger.error("Failed to create message entity", e);
        }
    }

    /**
     * Reads all messages from the database.
     *
     * @param senderUuid The UUID of the sender.
     * @param type       The type of the message.
     * @return A list of messages.
     */
    public List<Message> readAll(final UUID senderUuid, final String type) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM mt_messages WHERE sender_uuid = ? AND type = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, senderUuid.toString());
            stmt.setString(2, type);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message()
                        .setId(rs.getLong("message_id"))
                        .setServer(rs.getString("server"))
                        .setSender(UUID.fromString(rs.getString("sender_uuid")))
                        .setType(rs.getString("type"))
                        .setContent(rs.getString("content"))
                        .setSentAt(rs.getObject("sent_at", LocalDateTime.class))
                );
            }
            return messages;
        } catch (SQLException e) {
            ModerationLogger.error("Failed to read messages", e);
        }

        return messages;
    }

    /**
     * Reads the last message from the database.
     *
     * @param senderUuid The UUID of the sender.
     * @param type       The type of the message.
     * @return The last message.
     */
    public Message readLast(final UUID senderUuid, final String type) {
        String sql = "SELECT * FROM mt_messages WHERE sender_uuid = ? AND type = ? ORDER BY message_id DESC LIMIT 1";
        Message message = null;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, senderUuid.toString());
            stmt.setString(2, type);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                message = new Message()
                        .setId(rs.getLong("message_id"))
                        .setServer(rs.getString("server"))
                        .setSender(UUID.fromString(rs.getString("sender_uuid")))
                        .setType(rs.getString("type"))
                        .setContent(rs.getString("content"))
                        .setSentAt(rs.getObject("sent_at", LocalDateTime.class));
            }
        } catch (SQLException e) {
            ModerationLogger.error("Failed to read messages", e);
        }

        return message;
    }
}
