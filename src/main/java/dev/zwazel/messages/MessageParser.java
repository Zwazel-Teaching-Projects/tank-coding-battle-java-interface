package dev.zwazel.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageParser {
    public static MessageContainer parseMessage(byte[] data) throws Exception {
        // Parse JSON directly
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(data);

        // Map to MessageContainer
        MessageContainer message = new MessageContainer();
        message.setTarget(MessageTarget.fromOrdinal(root.get("target").asInt()));
        message.setMessageType(NetworkMessageType.fromOrdinal(root.get("message_type").asInt()));
        message.setMessageData(root.get("message_data"));

        return message;
    }
}
