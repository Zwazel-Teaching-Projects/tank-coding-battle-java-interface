package dev.zwazel.messages;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class MessageContainer {
    private MessageTarget target;
    private NetworkMessageType messageType;
    private JsonNode messageData;
}
