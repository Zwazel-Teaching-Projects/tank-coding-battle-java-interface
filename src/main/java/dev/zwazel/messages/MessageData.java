package dev.zwazel.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.zwazel.messages.model.GameStateUpdateData;

@JsonSubTypes({
    @JsonSubTypes.Type(value = GameStateUpdateData.class, name = "GAME_STATE_UPDATE"),
})
public interface MessageData {
}
