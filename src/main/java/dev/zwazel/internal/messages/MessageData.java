package dev.zwazel.internal.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.data.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "message_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameState.class),
        @JsonSubTypes.Type(value = FirstContact.class),
        @JsonSubTypes.Type(value = SimpleTextMessage.class),
        @JsonSubTypes.Type(value = MessageError.class),
        @JsonSubTypes.Type(value = ServerConfig.class),
})
public interface MessageData {
    default void applyOnReceive(InternalGameWorld internalWorld) {
        // Do nothing by default
    }
}
