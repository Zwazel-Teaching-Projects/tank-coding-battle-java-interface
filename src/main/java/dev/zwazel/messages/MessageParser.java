package dev.zwazel.messages;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static MessageContainer parseMessage(byte[] data) throws Exception {
        // Deserialize the entire container
        return mapper.readValue(data, MessageContainer.class);
    }
}
