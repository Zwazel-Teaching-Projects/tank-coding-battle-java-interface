package dev.zwazel.internal.message.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageData;

public record MessageError(@JsonProperty("error_type") ErrorTypes error,
                           @JsonProperty("error_message") String errorMessage) implements MessageData {
    @Override
    public boolean applyOnReceive(InternalGameWorld internalWorld) {
        System.err.println("Received Error from Server:\n\t" + error + " - " + errorMessage);
        return true;
    }

    enum ErrorTypes {
        INVALID_TARGET,
        LOBBY_MANAGEMENT_ERROR,
        LOBBY_ALREADY_RUNNING
    }
}
