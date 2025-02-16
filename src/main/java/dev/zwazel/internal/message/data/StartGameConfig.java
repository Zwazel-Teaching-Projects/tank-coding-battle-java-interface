package dev.zwazel.internal.message.data;

import dev.zwazel.internal.message.MessageData;

public record StartGameConfig(boolean fillEmptySlotsWithDummies) implements MessageData {
}
