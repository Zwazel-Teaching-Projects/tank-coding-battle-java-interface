package dev.zwazel.internal.messages.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.zwazel.internal.InternalGameWorld;
import dev.zwazel.internal.messages.MessageData;
import lombok.Builder;

@Builder
public record FirstContact(String botName, String lobbyId, String mapName, String teamName) implements MessageData {
    @Override
    public void applyOnReceive(InternalGameWorld internalWorld) {
        // Do nothing, we should never receive this message, only send it to the server
    }
}
