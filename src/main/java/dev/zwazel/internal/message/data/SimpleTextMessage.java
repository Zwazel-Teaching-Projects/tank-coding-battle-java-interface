package dev.zwazel.internal.message.data;

import dev.zwazel.internal.message.MessageData;
import lombok.Builder;

/**
 * A simple text message.
 * Can be sent to other clients.
 * Received when another client sends a message to me directly, the team i'm in, or the lobby i'm in.
 *
 * @param message The message.
 */
@Builder
public record SimpleTextMessage(String message) implements MessageData {
}
