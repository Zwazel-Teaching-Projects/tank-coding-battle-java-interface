package dev.zwazel.internal.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.data.FirstContact;
import dev.zwazel.internal.messages.data.GameState;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "message_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameState.class),
        @JsonSubTypes.Type(value = FirstContact.class),
})
public interface MessageData {
    void applyOnReceive(InternalGameWorld internalWorld);
}
