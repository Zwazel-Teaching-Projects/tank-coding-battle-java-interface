package dev.zwazel.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zwazel.messages.model.GameStateUpdateData;

public class MessageParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Object parseMessageData(NetworkMessageType messageType, JsonNode messageData) throws Exception {
        return switch (messageType) {
            case GAME_UPDATE -> mapper.treeToValue(messageData, GameStateUpdateData.class);
            default -> throw new IllegalArgumentException("Unknown message type: " + messageType);
        };
    }

    public static MessageContainer parseMessage(byte[] data) throws Exception {
        // Parse JSON directly
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(data);

        // Map to MessageContainer
        MessageContainer message = new MessageContainer();
        message.setTarget(MessageTarget.fromOrdinal(root.get("target").asInt()));
        message.setMessageType(NetworkMessageType.fromOrdinal(root.get("message_type").asInt()));
        message.setMessageData(root.get("message_data"));
        Object specificData = parseMessageData(message.getMessageType(), message.getMessageData());
        System.out.println("Deserialized specific message data: " + specificData);

        return message;
    }
}
