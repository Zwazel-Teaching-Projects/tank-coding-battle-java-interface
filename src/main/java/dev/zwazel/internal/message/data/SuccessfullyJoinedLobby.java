package dev.zwazel.internal.message.data;

import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record SuccessfullyJoinedLobby(String message) implements MessageData {
}
