package dev.zwazel.internal.message.data;

import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record StartGameConfig(boolean fillEmptySlotsWithDummies) implements MessageData {
}
