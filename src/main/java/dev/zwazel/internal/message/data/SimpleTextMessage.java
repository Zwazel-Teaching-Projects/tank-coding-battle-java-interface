package dev.zwazel.internal.message.data;

import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

@Builder
public record SimpleTextMessage(String message) implements MessageData {
}
