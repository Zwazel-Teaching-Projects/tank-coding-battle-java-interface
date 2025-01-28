package dev.zwazel.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
public class MessageContainer {
    private MessageTarget target;

    @JsonProperty("message_type")
    private NetworkMessageType messageType;

    @JsonProperty("message_data")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "message_type")
    private MessageData messageData;
}
