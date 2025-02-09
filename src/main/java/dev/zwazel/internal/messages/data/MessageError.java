package dev.zwazel.internal.messages.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.zwazel.internal.messages.MessageData;

public record MessageError(@JsonProperty("error_type") ErrorTypes error,
                           @JsonProperty("error_message") String errorMessage) implements MessageData {
    enum ErrorTypes {
        INVALID_TARGET,
        LOBBY_MANAGEMENT_ERROR,
    }
}
