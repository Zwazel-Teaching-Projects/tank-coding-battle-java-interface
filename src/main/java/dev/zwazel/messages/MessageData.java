package dev.zwazel.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.zwazel.messages.data.BotConfig;
import dev.zwazel.messages.data.GameStateUpdateData;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "message_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameStateUpdateData.class),
        @JsonSubTypes.Type(value = BotConfig.class),
})
public interface MessageData {

}
