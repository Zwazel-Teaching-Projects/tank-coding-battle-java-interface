package dev.zwazel.internal.message.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

/**
 * Represents an error message from the server.
 * Received when an error occurs.
 *
 * @param error        The type of error that occurred.
 * @param errorMessage The message describing the error.
 */
@Builder
public record MessageError(@JsonProperty("error_type") ErrorTypes error,
                           @JsonProperty("error_message") String errorMessage) implements MessageData {
    /**
     * Prints the error message to the console.
     *
     * @param internalWorld the world
     * @return true, meaning the bot can also handle this message if it wants to.
     */
    @Override
    public boolean applyOnReceive(InternalGameWorld internalWorld) {
        System.err.println("Received Error from Server:\n\t" + error + " - " + errorMessage);
        return true;
    }

    enum ErrorTypes {
        INVALID_TARGET,
        INVALID_FIRST_CONTACT,
        LOBBY_MANAGEMENT_ERROR,
        LOBBY_ALREADY_RUNNING,
        LOBBY_NOT_READY_TO_START,
        TEAM_DOES_NOT_EXIST,
        TEAM_FULL,
        INVALID_SENDER_STATE,
    }
}
