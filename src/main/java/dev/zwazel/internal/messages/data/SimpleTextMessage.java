package dev.zwazel.internal.messages.data;

import dev.zwazel.internal.messages.MessageData;
import lombok.Builder;

@Builder
public record SimpleTextMessage(String message) implements MessageData {
}
